CREATE OR REPLACE PACKAGE xhb_charges_pkg AS

    /*
     * Get by defendant_on_case_id.  Returns selected charges details using the defendant_on_case_id supplied.
     * The user may also provide a charge_type to further refine the set of records returned.
     * The details are used to populate a uk.gov.courtservice.xhibit.client.business.vos.services.charge.originalcharges.ChargeVO
     */
    PROCEDURE get_charges(p_results_out             OUT SYS_REFCURSOR,
                          p_defendant_on_case_id     IN XHB_ORIGINAL_CHARGES_CHGS_V.DEFENDANT_ON_CASE_ID%TYPE,
                          p_charge_type              IN XHB_ORIGINAL_CHARGES_CHGS_V.CHARGE_TYPE%TYPE);

    /*
     * Get by defendant_on_case_id.  Returns selected charges details for obsoleted records only using the 
     * defendant_on_case_id supplied.
     * The user may also provide a charge_type to further refine the set of records returned.
     * The details are used to populate a uk.gov.courtservice.xhibit.client.business.vos.services.charge.originalcharges.ChargeVO
     */
    PROCEDURE get_obsolete_charges(p_results_out             OUT SYS_REFCURSOR,
                                   p_defendant_on_case_id     IN XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE,
                                   p_charge_type              IN XHB_CHARGE.CHARGE_TYPE%TYPE);
    
    /*
     * Counts the number of charges of a given type and returns this value.
     */
    FUNCTION count_charges(p_defendant_on_case_id_in IN xhb_defendant_on_case.defendant_on_case_id%type,
                           p_charge_type_in          IN xhb_charge.charge_type%type) 
    RETURN NUMBER;

END xhb_charges_pkg;
/
show errors
