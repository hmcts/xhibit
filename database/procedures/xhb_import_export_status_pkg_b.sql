CREATE OR REPLACE PACKAGE BODY xhb_import_export_status_pkg AS
    --
    -- Query the XHB_IMPORT_EXPORT_STATUS table by the case_id
    --
    PROCEDURE get_by_case_id(p_results_out       OUT SYS_REFCURSOR,
                             p_case_id_in        IN  XHB_IMPORT_EXPORT_STATUS.case_id%TYPE)
    AS
    BEGIN
        OPEN p_results_out FOR
            SELECT ies.import_export_status_id as "id",
                   ies.*
            FROM   XHB_IMPORT_EXPORT_STATUS ies
            WHERE  ies.case_id = p_case_id_in;
    END get_by_case_id;

    --
    -- Query the XHB_IMPORT_EXPORT_STATUS table by the court_id
    --
    PROCEDURE get_by_court_id(p_results_out       OUT SYS_REFCURSOR,
                              p_court_id_in       IN  XHB_IMPORT_EXPORT_STATUS.court_id%TYPE)
    AS
    BEGIN
        OPEN p_results_out FOR
            SELECT ies.import_export_status_id as "id",
                   ies.*
            FROM   XHB_IMPORT_EXPORT_STATUS ies
            WHERE  ies.court_id  = p_court_id_in
            AND    ies.case_id  IS NULL;
    END get_by_court_id;

END xhb_import_export_status_pkg;
/
show errors