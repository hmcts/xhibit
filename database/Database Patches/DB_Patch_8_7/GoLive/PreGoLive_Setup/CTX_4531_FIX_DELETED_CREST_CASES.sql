CREATE OR REPLACE PROCEDURE fix_deleted_crest_cases (p_court_id IN xhibit.xhb_court.court_id%TYPE)
AS

	CURSOR c_deleted_crest_cases IS
	SELECT xc.case_id
	FROM xhibit.xhb_case xc
	WHERE xc.court_id = p_court_id
	AND NVL(xc.case_status,'-') NOT IN ('H','D','I','F') -- Ignore any cases already earmarked for housekeeping
      AND xc.case_type in ('A','S','T') -- CTX-4531 Amendment to avoid U cases being deleted
	AND NOT EXISTS (
		SELECT NULL FROM data_mig.xhbstg_case_dm xcd 
		WHERE xc.case_number = xcd.case_no
		AND xc.case_type = xcd.case_type
		AND xc.court_id = xcd.xhibit_court_id);
		
	TYPE t_case_id IS TABLE OF xhibit.xhb_case.case_id%TYPE;
	v_case_ids	t_case_id;
	
	v_total_updated	NUMBER := 0;

BEGIN

	DBMS_OUTPUT.PUT_LINE('Executing fix_deleted_crest_cases for court ID: '||p_court_id||'.  Start Time: '||TO_CHAR(SYSDATE,'DD-MM-YYYY HH24:MI:SS')||'.');

	OPEN c_deleted_crest_cases;
    LOOP
		FETCH c_deleted_crest_cases BULK COLLECT INTO v_case_ids LIMIT 1000;
		
		-- Increment the count
		v_total_updated := v_total_updated + v_case_ids.COUNT;
		
		-- Perform the update
		FORALL i IN 1..v_case_ids.COUNT
			UPDATE xhibit.xhb_case 
			SET case_status = 'D' 
			WHERE case_id = v_case_ids(i);
			
		FORALL i IN 1..v_case_ids.COUNT
			UPDATE xhibit.xhb_defendant_on_case 
			SET ctl_applies = 'N' 
			WHERE case_id = v_case_ids(i)
			AND NVL(obs_ind,'N') <> 'Y';
			
		FORALL i IN 1..v_case_ids.COUNT
			UPDATE xhibit.xhb_defendant_on_case 
			SET results_verified = 'E',
				date_exported = ADD_MONTHS(SYSDATE, -84) -- 7 years in the past
			WHERE case_id = v_case_ids(i)
			AND NVL(obs_ind,'N') <> 'Y'
			AND NVL(results_verified,'-') <> 'E';

		COMMIT;
         
		EXIT WHEN v_case_ids.COUNT = 0;
		
		-- Reinitialise collections
		IF v_case_ids IS NOT NULL THEN
			v_case_ids.DELETE;
		END IF;
	
	END LOOP;
	CLOSE c_deleted_crest_cases;
	
	DBMS_OUTPUT.PUT_LINE('Executing fix_deleted_crest_cases.  Total cases updated: '||v_total_updated);
    DBMS_OUTPUT.PUT_LINE('Executing fix_deleted_crest_cases.  End Time: '||TO_CHAR(SYSDATE,'DD-MM-YYYY HH24:MI:SS')||'.');

EXCEPTION
	WHEN OTHERS THEN
		ROLLBACK;
		DBMS_OUTPUT.PUT_LINE('Error Executing fix_deleted_crest_cases: ' || SQLCODE || ' : ' || SQLERRM);

END fix_deleted_crest_cases;

/