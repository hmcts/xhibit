create or replace PACKAGE xhb_formatting_pkg AS

    FUNCTION get_next_document_id RETURN XHB_FORMATTING.formatting_id%TYPE;


    FUNCTION get_document_details(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE)
                                  RETURN SYS_REFCURSOR;


    -- 1 represents true, 0 represents false...
    PROCEDURE update_document_status(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE,
                                     p_success_in       IN NUMBER);
                                     
    PROCEDURE update_cpp_formatting(p_cpp_formatting_id IN XHB_CPP_FORMATTING.cpp_formatting_id%TYPE,
                                    p_error_message IN VARCHAR2);

    FUNCTION parse_XML_string(p_string IN VARCHAR2, p_replacement_char IN CHAR DEFAULT NULL) RETURN VARCHAR2;
    
    FUNCTION wordwrapping_position(p_text IN VARCHAR2, p_field_length IN NUMBER) RETURN NUMBER;
    FUNCTION get_wordwrapped_string(p_text IN VARCHAR2, p_field_length IN NUMBER) RETURN VARCHAR2;
    FUNCTION get_latest_xhibit_clob_Id(p_court_id IN XHB_FORMATTING.court_id%TYPE, p_document_type IN XHB_FORMATTING.DOCUMENT_TYPE%TYPE, p_language IN XHB_FORMATTING.LANGUAGE%TYPE) RETURN NUMBER;
END xhb_formatting_pkg;
/
show errors
