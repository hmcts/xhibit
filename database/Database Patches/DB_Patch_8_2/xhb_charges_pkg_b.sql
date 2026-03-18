CREATE OR REPLACE PACKAGE BODY xhb_charges_pkg AS

    /*
     * Get by defendant_on_case_id.  Returns selected charges details using the defendant_on_case_id supplied.
     * An optional charge_type may be supplied to further refine the set of records returned.
     * The details are used to populate a uk.gov.courtservice.xhibit.client.business.vos.services.charge.originalcharges.ChargeVO
     */
    PROCEDURE get_charges(p_results_out             OUT SYS_REFCURSOR,
                          p_defendant_on_case_id     IN XHB_ORIGINAL_CHARGES_CHGS_V.DEFENDANT_ON_CASE_ID%TYPE,
                          p_charge_type              IN XHB_ORIGINAL_CHARGES_CHGS_V.CHARGE_TYPE%TYPE) IS

    BEGIN
    
        IF ( p_defendant_on_case_id is null ) THEN
             RAISE_APPLICATION_ERROR(-20101, 'Defendant On Case ID MUST be supplied');
        END IF;

        OPEN p_results_out FOR
            select defendant_on_case_id,
                   defendant_on_offence_id,
                   seq_no,
                   offence_id,
                   crest_offence_freetext,
                   charge_id,
                   charge_type,
                   crest_charge_seq_no,
                   crest_offence_seq_no,
                   ref_offence_id,
                   offence_code,
                   offence_desc
            from   xhb_original_charges_chgs_v
            where  defendant_on_case_id = p_defendant_on_case_id
            and   (p_charge_type is null or p_charge_type = charge_type);

    END get_charges;

END xhb_charges_pkg;
/
show errors
