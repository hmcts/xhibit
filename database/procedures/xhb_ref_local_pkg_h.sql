CREATE OR REPLACE PACKAGE xhb_ref_local_pkg AS
    PROCEDURE end_hearings(p_court_id_in         IN XHB_HEARING.court_id%TYPE,
                           p_hearing_end_date_in IN VARCHAR2);
END xhb_ref_local_pkg;
/
show errors