BEGIN

	-- Update Court CREST IP Address
	UPDATE xhb_court
	SET crest_ip_address = 'xx_' || crest_ip_address 
	WHERE court_id = &1;
	
	-- Disable the CREST Import Jobs for the court
	UPDATE xhb_crest_import
	SET time_to_run = NULL
	WHERE court_id = &1
	AND import_type IN ('AD','CS','GL','LL','NC','UC');
	
	COMMIT;

END;
/