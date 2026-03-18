DECLARE

	CURSOR c_get_cases_no_class_code IS
	SELECT xca.case_id, xca.case_number, xca.court_id, xct.court_name
	FROM xhb_case xca, xhb_court xct
	WHERE xca.case_type = 'T'
	AND xca.class_code IS NULL
	AND xct.court_id = xca.court_id
	AND NVL(xct.is_pilot,'N') = 'Y';
	
	lv_class_code	xhb_case.class_code%TYPE;

BEGIN

	FOR case_rec IN c_get_cases_no_class_code LOOP
	
		BEGIN
			SELECT class_code INTO lv_class_code 
			FROM aud_case 
			WHERE case_id = case_rec.case_id 
			AND class_code IS NOT NULL 
			AND ROWNUM = 1 
			ORDER BY last_update_date DESC;
			
			UPDATE xhb_case
			SET class_code = lv_class_code
			WHERE case_id = case_rec.case_id;
			
			COMMIT;
		
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				DBMS_OUTPUT.PUT_LINE('Unable to set CLASS_CODE for case T' || case_rec.case_number || ' at court ' || case_rec.court_name || ' (' || case_rec.court_id || ')');
				
		END;
	
	END LOOP;
	
END;
/

