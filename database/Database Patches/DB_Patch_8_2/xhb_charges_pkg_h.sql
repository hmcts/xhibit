CREATE OR REPLACE PACKAGE xhb_charges_pkg AS

    /*
     * Get by defendant_on_case_id.  Returns selected charges details using the defendant_on_case_id supplied.
     * The user may also provide a charge_type to further refine the set of records returned.
     * The details are used to populate a uk.gov.courtservice.xhibit.client.business.vos.services.charge.originalcharges.ChargeVO
     */
    PROCEDURE get_charges(p_results_out             OUT SYS_REFCURSOR,
                          p_defendant_on_case_id     IN XHB_ORIGINAL_CHARGES_CHGS_V.DEFENDANT_ON_CASE_ID%TYPE,
                          p_charge_type              IN XHB_ORIGINAL_CHARGES_CHGS_V.CHARGE_TYPE%TYPE);

END xhb_charges_pkg;
/
show errors
