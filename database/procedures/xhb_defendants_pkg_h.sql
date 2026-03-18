CREATE OR REPLACE PACKAGE xhb_defendants_pkg AS

    /*
     * Get by case_id.  Returns selected defendant details using the case_id supplied
     */
    PROCEDURE get_by_case_id(p_results_out OUT SYS_REFCURSOR,
                             p_case_id      IN XHB_ORIGINAL_CHARGES_DEFS_V.CASE_ID%TYPE);

END xhb_defendants_pkg;
/
show errors
