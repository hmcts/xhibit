-- Please note that this script is not a deliverable as part of the Monarch
-- release, but can be used to insert the required new reference data into a
-- test database.  BAU are supposed to be creating this reference data in 
-- production, but can use this script if required.
DECLARE
	
	-- Identify all the current courts using this reference data
	CURSOR c_get_court_ids IS
	SELECT DISTINCT court_id
	FROM xhb_ref_system_code
	WHERE code_type = 'ADVOCATE_TYPE' 
	AND obs_ind = 'N'
	ORDER BY 1;

BEGIN

	-- For each court, insert new records for KC and reorder the rest
	FOR rec IN c_get_court_ids LOOP
		
		INSERT INTO xhb_ref_system_code (code, code_type, code_title, de_code, court_id)
		VALUES ('0', 'ADVOCATE_TYPE', 'ADVOCATE TYPE', 'KC Alone', rec.court_id );
		
		INSERT INTO xhb_ref_system_code (code, code_type, code_title, de_code, court_id)
		VALUES ('1', 'ADVOCATE_TYPE', 'ADVOCATE TYPE', 'Leading KC', rec.court_id );
		
		UPDATE xhb_ref_system_code SET code='2' where code_type='ADVOCATE TYPE' and de_code='Leading J';
		
		INSERT INTO xhb_ref_system_code (code, code_type, code_title, de_code, court_id)
		VALUES ('3', 'ADVOCATE_TYPE', 'ADVOCATE TYPE', 'J Led By KC', rec.court_id );
		
		UPDATE xhb_ref_system_code SET code='4' where code_type='ADVOCATE TYPE' and de_code='QC Alone';
		
		UPDATE xhb_ref_system_code SET code='5' where code_type='ADVOCATE TYPE' and de_code='Leading QC';
		
		UPDATE xhb_ref_system_code SET code='6' where code_type='ADVOCATE TYPE' and de_code='J Led By QC';
		
		UPDATE xhb_ref_system_code SET code='7' where code_type='ADVOCATE TYPE' and de_code='J Led By J';
		
		UPDATE xhb_ref_system_code SET code='8' where code_type='ADVOCATE TYPE' and de_code='J Alone';
		
		UPDATE xhb_ref_system_code SET code='9' where code_type='ADVOCATE TYPE' and de_code='Noting J';
    	
	END LOOP;
	
	COMMIT;

END;
/