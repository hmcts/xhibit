CREATE OR REPLACE PACKAGE xhb_formatting_pkg AS

    FUNCTION get_next_document_id RETURN XHB_FORMATTING.formatting_id%TYPE;


    FUNCTION get_document_details(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE)
                                  RETURN SYS_REFCURSOR;


    -- 1 represents true, 0 represents false...
    PROCEDURE update_document_status(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE,
                                     p_success_in       IN NUMBER);

    FUNCTION parse_XML_string(p_string IN VARCHAR2, p_replacement_char IN CHAR DEFAULT NULL) RETURN VARCHAR2;
    
END xhb_formatting_pkg;
/
show errors
