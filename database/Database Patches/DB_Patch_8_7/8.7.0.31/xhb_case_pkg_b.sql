CREATE OR REPLACE PACKAGE BODY xhb_case_pkg AS

	FUNCTION determine_case_status (pn_case_id IN xhb_case.case_id%TYPE) 
	RETURN VARCHAR2 AS
	
		CURSOR c_get_case_data IS
		SELECT case_type, no_defendants_for_case, date_trans_to
		FROM xhb_case
		WHERE case_id = pn_case_id;
		
		CURSOR c_get_count_defendants IS
		SELECT COUNT(*)
		FROM xhb_defendant_on_case
		WHERE case_id = pn_case_id
		AND NVL(obs_ind,'N') = 'N';
		
		CURSOR c_get_count_pros_resp IS
		SELECT COUNT(*)
		FROM xhb_case_prosecutor_agency
		WHERE case_id = pn_case_id
		AND NVL(obs_ind,'N') = 'N'
		AND ( prosecutor_type = 'P' 
			OR 
			( prosecutor_type = 'R' AND respondent_status = 'R' ) 
		);
		
		CURSOR c_get_count_verified_defs IS
		SELECT COUNT(*)
		FROM xhb_defendant_on_case
		WHERE case_id = pn_case_id
		AND NVL(obs_ind,'N') = 'N'
		AND NVL(results_verified,'-') = 'E';
		
		CURSOR c_get_count_def_bench_wrnt IS
		SELECT COUNT(*)
		FROM xhb_defendant_on_case xdoc
		WHERE xdoc.case_id = pn_case_id
		AND NVL(xdoc.obs_ind,'N') = 'N'
		AND NVL(xdoc.results_verified,'-') <> 'E'
		AND EXISTS (
			SELECT NULL FROM xhb_bw_history xbh
			WHERE xbh.defendant_on_case_id = xdoc.defendant_on_case_id
			AND NVL(xbh.obs_ind,'N') = 'N'
			AND xbh.bw_issue_date IS NOT NULL
			AND xbh.bw_end_date IS NULL
		);
		
		v_status 				VARCHAR2(20) := 'Open';
		v_case_type				xhb_case.case_type%TYPE;
		n_defs_for_case			xhb_case.no_defendants_for_case%TYPE;
		d_date_trans_to			xhb_case.date_trans_to%TYPE;
		
		n_count_defendants		NUMBER;
		n_count_pros_resp		NUMBER;
		n_count_verified_defs	NUMBER;
		n_count_def_bench_wrnt	NUMBER;

	BEGIN
	
		-- Retrieve details from the XHB_CASE table
		OPEN c_get_case_data;
		FETCH c_get_case_data INTO v_case_type, n_defs_for_case, d_date_trans_to;
		CLOSE c_get_case_data;
		
		-- Only perform checks on A, S or T cases - ignore U and B cases
		IF v_case_type IN ('A','S','T') THEN
		
			-- Get count of non-obsolete defendants on case
			OPEN c_get_count_defendants;
			FETCH c_get_count_defendants INTO n_count_defendants;
			CLOSE c_get_count_defendants;
			
			-- Get count of non-obsolete prosecutors and respondents on case
			OPEN c_get_count_pros_resp;
			FETCH c_get_count_pros_resp INTO n_count_pros_resp;
			CLOSE c_get_count_pros_resp;
			
			IF n_count_defendants = 0 OR n_count_pros_resp = 0 THEN
				-- The number of defendants is 0 or number of Prosecutors/Respondents is 0
				v_status := 'Incomplete_N';
				GOTO end_function;
			END IF;
			
			IF n_count_defendants <> n_defs_for_case THEN
				-- The number of defendants on the case is not equal to XHB_CASE.NO_DEFENDANTS_FOR_CASE
				v_status := 'Incomplete_I';
				GOTO end_function;
			END IF;
			
			-- Get count of non-obsolete defendants that have been verified on case
			OPEN c_get_count_verified_defs;
			FETCH c_get_count_verified_defs INTO n_count_verified_defs;
			CLOSE c_get_count_verified_defs;
			
			IF n_count_defendants = n_count_verified_defs THEN
				-- All defendants on case have been verified
				v_status := 'Dealt With';
				GOTO end_function;
			END IF;
			
			-- Get count of non-obsolete, non verified defendants that have an existing bench warrant on case
			OPEN c_get_count_def_bench_wrnt;
			FETCH c_get_count_def_bench_wrnt INTO n_count_def_bench_wrnt;
			CLOSE c_get_count_def_bench_wrnt;
			
			IF ( n_count_defendants - n_count_verified_defs ) = n_count_def_bench_wrnt THEN
				-- All non verified defendants have an existing bench warrant
				v_status := 'Bench Warrant';
				GOTO end_function;
			END IF;
			
			IF d_date_trans_to IS NOT NULL THEN
				-- Case has been transferred out
				v_status := 'Transferred Out';
				GOTO end_function;
			END IF;

		END IF;

		<<end_function>>
		RETURN v_status;

	END determine_case_status;
  
END xhb_case_pkg;
/
show errors