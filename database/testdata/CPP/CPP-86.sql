
BEGIN

	INSERT INTO XHB_XML_DOCUMENT(DATE_CREATED,DOCUMENT_TITLE,STATUS,DOCUMENT_TYPE,COURT_ID,XML_DOCUMENT_CLOB_ID) VALUES
(SYSDATE,'Daily List DRAFT v3 2020-01-16 15:00:22','ND','DL',5,1509252);

	INSERT INTO XHB_FORMATTING(date_in,format_status,Distribution_type,mime_type,document_type,court_id,xml_document_clob_id) VALUES
(SYSDATE,'ND','FTP','HTM','DL',5,1509252);
	
	INSERT INTO XHB_FORMATTING(date_in,format_status,Distribution_type,mime_type,document_type,court_id,xml_document_clob_id) VALUES
(SYSDATE,'ND','FTP','PDF','DL',5,1509252);
	
	COMMIT;
END;

/