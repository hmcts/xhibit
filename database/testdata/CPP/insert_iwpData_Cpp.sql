DECLARE

	ln_clob_id	xhb_clob.clob_id%TYPE;

BEGIN


	SELECT XHB_CLOB_SEQ.NEXTVAL INTO ln_clob_id FROM DUAL;

	INSERT INTO XHB_CLOB (CLOB_ID, CLOB_DATA)
	VALUES(ln_clob_id, 'x');
	INSERT INTO XHB_FORMATTING(date_in,format_status,Distribution_type,mime_type,document_type,created_by,court_id,xml_document_clob_id,language) VALUES
(SYSDATE,'ND','FTP','HTM','IWP','TESTDATAIWP',5,0,'cy');

	INSERT INTO XHB_CPP_FORMATTING(STAGING_TABLE_ID,DATE_IN,DOCUMENT_TYPE,COURT_ID, XML_DOCUMENT_CLOB_ID) VALUES (1,SYSDATE,'IWP',5,ln_clob_id);

	COMMIT;
	
END;

/