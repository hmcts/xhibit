create or replace PACKAGE BODY xhb_charges_pkg AS

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
            and   (p_charge_type is null or p_charge_type = charge_type)
            and    obsolete = 'N';

    END get_charges;

    /*
     * Get by defendant_on_case_id.  Returns selected charges details for obsoleted records only using the 
     * defendant_on_case_id supplied.
     * The user may also provide a charge_type to further refine the set of records returned.
     * The details are used to populate a uk.gov.courtservice.xhibit.client.business.vos.services.charge.originalcharges.ChargeVO
     */
    PROCEDURE get_obsolete_charges(p_results_out             OUT SYS_REFCURSOR,
                                   p_defendant_on_case_id     IN XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE,
                                   p_charge_type              IN XHB_CHARGE.CHARGE_TYPE%TYPE) IS
                                   
    BEGIN
    
        IF ( p_defendant_on_case_id is null ) THEN
             RAISE_APPLICATION_ERROR(-20102, 'Defendant On Case ID MUST be supplied');
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
            and   (p_charge_type is null or p_charge_type = charge_type)
            and    obsolete = 'Y';

    END get_obsolete_charges;

    /*
     * Counts the number of charges of a given type and returns this value.
     */
    FUNCTION count_charges(p_defendant_on_case_id_in IN xhb_defendant_on_case.defendant_on_case_id%type,
                           p_charge_type_in          IN xhb_charge.charge_type%type) 
    RETURN NUMBER IS
        total_charges NUMBER;    
    BEGIN
    
        select count(1)
        into   total_charges
        from   xhb_defendant_on_offence     doo,
               xhb_offence                  o,
               xhb_charge                   c
        where  p_defendant_on_case_id_in =  doo.defendant_on_case_id
        and    doo.offence_id            =  o.offence_id
        and    o.charge_id               =  c.charge_id
        and    c.charge_type             =  p_charge_type_in
        and   (doo.obs_ind is null or doo.obs_ind = 'N')
        and   (o.obs_ind   is null or o.obs_ind   = 'N')
        and   (c.obs_ind   is null or c.obs_ind   = 'N');
        
        return total_charges;

    END count_charges;

    PROCEDURE get_defendants_by_charge_id(p_results_out    OUT SYS_REFCURSOR,
                                          p_charge_id      IN  XHB_OFFENCE.CHARGE_ID%TYPE,
                                          p_ref_offence_id IN  XHB_OFFENCE.REF_OFFENCE_ID%TYPE,
                                          p_address_id     IN  XHB_OFFENCE.LOCATION_ADDRESS_ID%TYPE) IS
    BEGIN
        IF ( p_charge_id is null ) THEN
             RAISE_APPLICATION_ERROR(-20101, 'CHARGE ID MUST be supplied');
        END IF;

        OPEN p_results_out FOR
        SELECT doc.defendant_id 
        FROM   xhb_offence o,
               xhb_defendant_on_offence dof,
               xhb_defendant_on_case doc
        WHERE  o.charge_id = p_charge_id
        AND    o.ref_offence_id = p_ref_offence_id
        AND    o.location_address_id = p_address_id
        AND    dof.offence_id = o.offence_id
        AND    doc.defendant_on_case_id = dof.defendant_on_case_id
        AND    NVL(dof.obs_ind, 'N') = 'N'
        AND    NVL(doc.obs_ind, 'N') = 'N'
        AND    NVL(o.obs_ind, 'N') = 'N';
      
    END get_defendants_by_charge_id;
    
END xhb_charges_pkg;
/
show errors
