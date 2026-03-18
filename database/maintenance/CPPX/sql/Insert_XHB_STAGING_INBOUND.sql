declare 									


PROCEDURE xhb_staging_inbound_insert(
        p_document_name     IN VARCHAR2 ,
        p_court_code        IN VARCHAR2,
        p_document_type     IN VARCHAR2,
        p_time_loaded       IN DATE,
        p_validation_status IN VARCHAR2,
	p_clob_id	    IN VARCHAR2
        )AS
        
BEGIN
        --
        -- Insert a new entry into the xhb_cpp_staging_inbound table, and retrieve the
        -- primary key 
        --
        INSERT INTO XHB_CPP_STAGING_INBOUND (document_name,
                                             court_code,
                                             document_type,
                                             time_loaded,
                                             clob_id,
                                             validation_status
                                            )
                                     VALUES (p_document_name,
                                             p_court_code,
                                             p_document_type,
                                             p_time_loaded,
                                             p_clob_id,
                                             p_validation_status
                                             );
    END xhb_staging_inbound_insert;
	
BEGIN
     xhb_staging_inbound_insert(
                  p_clob_id      	=> '&&6',
                  p_document_name	=> '&&1',
                  p_court_code		=> '&&3',
                  p_document_type	=> '&&4',
                  p_time_loaded		=> TO_DATE('&5','YYYYMMDDHH24MISS'),
                  p_validation_status	=> '&&2');
COMMIT;	
END;
/
