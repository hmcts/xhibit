CREATE OR REPLACE PACKAGE xhb_import_export_status_pkg AS
    PROCEDURE get_by_case_id(p_results_out       OUT SYS_REFCURSOR,
                             p_case_id_in        IN  XHB_IMPORT_EXPORT_STATUS.case_id%TYPE);

    PROCEDURE get_by_court_id(p_results_out       OUT SYS_REFCURSOR,
                              p_court_id_in        IN XHB_IMPORT_EXPORT_STATUS.court_id%TYPE);

end xhb_import_export_status_pkg;
/
show errors