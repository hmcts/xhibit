CREATE OR REPLACE PACKAGE BODY xhb_case_pkg AS

	----------------------------------------------------------------------------
	-- get_pros_resp_count
	-- Private function to determine the number of prosecutors or respondents
	-- on a case based upon case type and case sub type
	----------------------------------------------------------------------------
  
  ----------------------------------------------------------------------------
  -- John.R     v1.0 created
  --
  -- 06/03/2019 S.Sethuraman   v1.1   added code as per latest Listings FSv1.11
  --
  ----------------------------------------------------------------------------
  
	FUNCTION get_pros_resp_count(pn_case_id IN xhb_case.case_id%TYPE
								,pv_case_type IN xhb_case.case_type%TYPE
								,pv_case_sub_type IN xhb_case.case_sub_type%TYPE)
	RETURN NUMBER IS

		CURSOR c_get_count_pros_resp_1 IS
		SELECT COUNT(*)
		FROM xhb_case_prosecutor_agency
		WHERE case_id = pn_case_id
		AND NVL(obs_ind,'N') = 'N'
		AND ( prosecutor_type = 'P'
			OR
			( prosecutor_type = 'R' AND respondent_status = 'R' )
		);

		CURSOR c_get_count_pros_resp_2 IS
		SELECT COUNT(*)
		FROM xhb_case_prosecutor_agency
		WHERE case_id = pn_case_id
		AND NVL(obs_ind,'N') = 'N'
		AND prosecutor_type = 'R'
		AND respondent_status = 'R';

		CURSOR c_get_count_pros_resp_3 IS
		SELECT COUNT(*)
		FROM xhb_case_prosecutor_agency
		WHERE case_id = pn_case_id
		AND NVL(obs_ind,'N') = 'N'
		AND prosecutor_type = 'R';

		n_count_pros_resp		NUMBER;

	BEGIN

		IF pv_case_type = 'A' AND pv_case_sub_type = 'O' THEN
			-- Miscellaneous Appeal case.  Ignore prosecutors and only look for respondents
			OPEN c_get_count_pros_resp_2;
			FETCH c_get_count_pros_resp_2 INTO n_count_pros_resp;
			CLOSE c_get_count_pros_resp_2;
		ELSIF pv_case_type = 'A' AND pv_case_sub_type <> 'O' THEN
			-- Criminal Appeal case.  Ignore prosecutors and only look for respondents
			OPEN c_get_count_pros_resp_3;
			FETCH c_get_count_pros_resp_3 INTO n_count_pros_resp;
			CLOSE c_get_count_pros_resp_3;
		ELSE
			-- Non Appeal cases.  Look for prosecutors and respondents
			OPEN c_get_count_pros_resp_1;
			FETCH c_get_count_pros_resp_1 INTO n_count_pros_resp;
			CLOSE c_get_count_pros_resp_1;
		END IF;
		RETURN n_count_pros_resp;

	END get_pros_resp_count;

	----------------------------------------------------------------------------
	-- determine_case_status
	-- Performs numerous checks on a given case and returns an interpreted status
	----------------------------------------------------------------------------
	FUNCTION determine_case_status (pn_case_id IN xhb_case.case_id%TYPE)
	RETURN VARCHAR2 AS

		CURSOR c_get_case_data IS
		SELECT NVL(case_type,'-'), NVL(no_defendants_for_case,0), date_trans_to, NVL(case_sub_type,'-')
		FROM xhb_case
		WHERE case_id = pn_case_id;

		CURSOR c_get_count_defendants IS
		SELECT COUNT(*)
		FROM xhb_defendant_on_case
		WHERE case_id = pn_case_id
		AND NVL(obs_ind,'N') = 'N';

		CURSOR c_get_count_verified_defs IS
		SELECT COUNT(*)
		FROM xhb_defendant_on_case
		WHERE case_id = pn_case_id
		AND NVL(obs_ind,'N') = 'N'
		AND NVL(results_verified,'-') = 'E'
		AND date_exported IS NOT NULL;

		CURSOR c_get_count_closed_verdicts IS
		SELECT COUNT(*)
		FROM xhb_verdict xv, xhb_ref_system_code xrsc
		WHERE xv.case_id IS NOT NULL
		AND xv.case_id = pn_case_id
		AND NVL(xv.obs_ind,'N') = 'N'
		AND xv.ref_verdict_id IS NOT NULL
		AND xrsc.ref_system_code_id = xv.ref_verdict_id
		AND NVL(xrsc.code,'NULL') NOT IN ('NH','PH','NULL'); -- SS added NULL to match specs

		CURSOR c_get_count_def_bench_wrnt IS
		SELECT COUNT(*)
		FROM xhb_defendant_on_case xdoc
		WHERE xdoc.case_id = pn_case_id
		AND NVL(xdoc.obs_ind,'N') = 'N'
		AND NVL(xdoc.results_verified,'-') <> 'E'
		AND xdoc.date_exported IS NULL
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
		v_case_sub_type			xhb_case.case_sub_type%TYPE;

		n_count_defendants		NUMBER;
		n_count_pros_resp		NUMBER;
		n_count_verified_defs	NUMBER;
		n_count_closed_verdicts	NUMBER;
		n_count_def_bench_wrnt	NUMBER;

	BEGIN

		-- Retrieve details from the XHB_CASE table
		OPEN c_get_case_data;
		FETCH c_get_case_data INTO v_case_type, n_defs_for_case, d_date_trans_to, v_case_sub_type;
		CLOSE c_get_case_data;

		-- Only perform checks on A, S or T cases - ignore U and B cases
		IF v_case_type IN ('A','S','T') THEN

			-- Get count of non-obsolete defendants on case
			OPEN c_get_count_defendants;
			FETCH c_get_count_defendants INTO n_count_defendants;
			CLOSE c_get_count_defendants;

			-- Get count of non-obsolete prosecutors and respondents on case
			n_count_pros_resp := get_pros_resp_count(pn_case_id, v_case_type, v_case_sub_type);

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

			IF v_case_type = 'A' AND v_case_sub_type = 'O' THEN
				-- Miscellaneous Appeal case: check verdicts
				OPEN c_get_count_closed_verdicts;
				FETCH c_get_count_closed_verdicts INTO n_count_closed_verdicts;
				CLOSE c_get_count_closed_verdicts;

				IF n_count_closed_verdicts > 0 THEN
					-- There is at least one closed verdict on the case
					v_status := 'Dealt With';
					GOTO end_function;
				END IF;

			ELSE
				-- All other cases: check verified case and bench warrants
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
	
	PROCEDURE get_case_offences(p_results_out OUT SYS_REFCURSOR,
                                p_case_id     IN XHB_CASE.CASE_ID%TYPE) IS
                                   
    BEGIN
    
        IF ( p_case_id is null ) THEN
             RAISE_APPLICATION_ERROR(-20102, 'Case ID MUST be supplied');
        END IF;

        OPEN p_results_out FOR
            select	decode(xro.offence_code, 'ZZ99999', xo.crest_offence_freetext, xro.offence_desc) as offence_desc, 
					xo.offence_id, 
					xo.charge_id, 
					xo.crest_offence_seq_no
			from 	xhb_charge xc, 
					xhb_offence xo, 
					xhb_ref_offence xro
			where 	xc.case_id = p_case_id 
			and 	nvl(xc.obs_ind,'N') <> 'Y'
			and 	xo.charge_id = xc.charge_id 
			and 	nvl(xo.obs_ind,'N') <> 'Y'
			and 	xro.ref_offence_id = xo.ref_offence_id
			order by xo.charge_id, xo.crest_offence_id;

    END get_case_offences;

END xhb_case_pkg;
/