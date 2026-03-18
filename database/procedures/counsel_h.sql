-- See the body script for possible future enhancements
CREATE OR REPLACE PACKAGE counselfacilities AS
    TYPE counsel_type IS REF CURSOR;

    PROCEDURE get_counsel_sign_in(p_counsel_cursor_out OUT counsel_type,
                                  p_court_id_in        IN  NUMBER,
                                  p_start_date_in      IN  DATE,
                                  p_court_room_id_in   IN  NUMBER);


    PROCEDURE search_counsel(p_counsel_cursor_out OUT counsel_type,
                             p_court_id_in        IN  NUMBER,
                             p_start_date_in      IN  DATE,
                             p_first_name_in      IN  VARCHAR2,
                             p_surname_in         IN  VARCHAR2);


    PROCEDURE search_defendants(p_counsel_cursor_out OUT counsel_type,
                                p_court_id_in        IN  NUMBER,
                                p_start_date_in      IN  DATE,
                                p_first_name_in      IN  VARCHAR2,
                                p_surname_in         IN  VARCHAR2);



    --
    -- NEW FUNCTION FOR CR51...
    --
    FUNCTION get_court_room_list(p_court_id_in      IN NUMBER,
                                 p_start_date_in    IN  DATE,
                                 p_court_room_id_in IN NUMBER) RETURN SYS_REFCURSOR;
END counselfacilities;
/
show errors