DECLARE
	
	-- Identify all the current courts using this reference data
	CURSOR c_get_court_ids IS
	SELECT DISTINCT court_id
	FROM xhb_ref_system_code
	WHERE code_type = 'HO_PROC_BREACH' 
	AND code_title = 'HO_PROCEEDINGS_CODE'
	AND obs_ind = 'N'
	ORDER BY 1;

BEGIN

	-- Renumber codes 43-48 to increment by 1
	UPDATE xhb_ref_system_code SET code = TO_CHAR(TO_NUMBER(code)+1)
	WHERE code_type = 'HO_PROC_BREACH' 
	AND code_title = 'HO_PROCEEDINGS_CODE'
	AND code IN ('43','44','45','46','47','48');

	-- For each court, insert two new records with codes of '43' and '87'
	FOR rec IN c_get_court_ids LOOP
		INSERT INTO xhb_ref_system_code (code, code_type, code_title, de_code, court_id, obs_ind)
		VALUES ('43', 'HO_PROC_BREACH', 'HO_PROCEEDINGS_CODE', 'Breach of Sexual Harm Prevention Order (SHPO)', rec.court_id, 'N');
		
		INSERT INTO xhb_ref_system_code (code, code_type, code_title, de_code, court_id, obs_ind)
		VALUES ('97', 'HO_PROC_BREACH', 'HO_PROCEEDINGS_CODE', 'Application to amend Community Order/Suspended Sentence Order', rec.court_id, 'N');
	END LOOP;
	
	COMMIT;

END;
/