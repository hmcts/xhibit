CREATE OR REPLACE PACKAGE BODY xhb_defendants_pkg AS
    /*
     * Get by case_id.  Returns selected defendant details using the case_id supplied
     */
    PROCEDURE get_by_case_id(p_results_out OUT SYS_REFCURSOR,
                             p_case_id      IN XHB_ORIGINAL_CHARGES_DEFS_V.CASE_ID%TYPE) IS

    BEGIN

        OPEN p_results_out FOR
            select court_id,
                   case_id,
                   case_type,
                   case_number,
                   defendant_on_case_id,
                   asn,
                   defendant_id,
                   first_name,
                   middle_name,
                   surname,
                   xhb_charges_pkg.count_charges(defendant_on_case_id, 'I') total_indictments,
                   xhb_charges_pkg.count_charges(defendant_on_case_id, 'G') total_original_charges
            from   xhb_original_charges_defs_v
            where  case_id = p_case_id;

    END get_by_case_id;

END xhb_defendants_pkg;
/
show errors
