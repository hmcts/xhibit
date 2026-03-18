CREATE OR REPLACE PACKAGE BODY xhb_1745_data_migration_pkg AS

    /*
     * Returns selected court details for courts to be processed
     * Inputs : None
     */
    PROCEDURE get_courts(p_results_out             OUT SYS_REFCURSOR) IS

    BEGIN

        OPEN p_results_out FOR
            SELECT c.court_id,
                   c.display_name,
                   c.crest_court_id,
                   mig.status
            FROM   XHB_COURT                        c,
                   XHB_1745_DATA_MIGRATION_TOTALS   mig
            WHERE  mig.status   = 'S'
            AND    mig.court_id =  c.court_id
            ORDER BY c.court_id;

     END get_courts;

    /*
     * Returns selected court details for a specific court
     */
    PROCEDURE get_court(p_results_out OUT SYS_REFCURSOR,
                        p_court_id     IN XHB_CASE.COURT_ID%TYPE) IS
    BEGIN
    
        OPEN p_results_out FOR
            SELECT c.court_id,
                   c.display_name,
                   c.crest_court_id,
                   mig.status
            FROM   XHB_COURT                        c,
                   XHB_1745_DATA_MIGRATION_TOTALS   mig
            WHERE  c.court_id   = p_court_id
            AND    c.court_id   = mig.court_id;
    
    END get_court;

    /*
     * Returns HO Police Force Codes for a given court
     */
    PROCEDURE get_police_force_data(p_results_out  OUT SYS_REFCURSOR,
                                    p_court_id      IN XHB_REF_SYSTEM_CODE.COURT_ID%TYPE) IS

    BEGIN

        OPEN p_results_out FOR
            SELECT court_id,
                   code
            FROM   XHB_REF_SYSTEM_CODE
            WHERE  code_type = 'HO_POL_FORCE'
            AND    court_id  =  p_court_id
            ORDER BY code;

    END get_police_force_data;

    /*
     * Returns defendant on case data for the given court ID
     */
    PROCEDURE get_defendants_by_court(p_results_out  OUT SYS_REFCURSOR,
                                      p_court_id     IN  XHB_CASE.COURT_ID%TYPE) IS

    BEGIN

        OPEN p_results_out FOR
            SELECT c.court_id,
                   c.case_id,
                   c.case_type,
                   c.case_sub_type,
                   c.case_number,
                   doc.defendant_on_case_id,
                   doc.ptiurn,
                   d.first_name,
                   d.surname,
                   d.defendant_id,
                   d.crest_defendant_id
            FROM   XHB_CASE               c,
                   XHB_DEFENDANT_ON_CASE  doc,
                   XHB_DEFENDANT          d
            WHERE  c.court_id            = p_court_id
            AND    c.case_id             = doc.case_id
            AND    doc.defendant_id      = d.defendant_id
            ORDER BY c.case_id,
                     doc.defendant_on_case_id;

    END get_defendants_by_court;
    
    /*
     * Updates a defendant on case record for the given defendant_on_case_id
     */
    PROCEDURE update_defendant_on_case( p_defendant_on_case_id  IN  XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE,
                            p_asn           IN  XHB_DEFENDANT_ON_CASE.ASN%TYPE,
                        p_ptiurn            IN  XHB_DEFENDANT_ON_CASE.PTIURN%TYPE) IS
    
    BEGIN
            
        UPDATE XHB_DEFENDANT_ON_CASE  
        SET asn=p_asn, 
        ptiurn = p_ptiurn 
        WHERE defendant_on_case_id = p_defendant_on_case_id;
            
    END update_defendant_on_case;

/*
   * Updates a defendant on case record for the given defendant_on_offence_id
   */
  PROCEDURE update_defendant_on_offence(p_defendant_on_offence_id   IN  XHB_DEFENDANT_ON_OFFENCE.DEFENDANT_ON_OFFENCE_ID%TYPE,
                     p_seq_no           IN  XHB_DEFENDANT_ON_OFFENCE.SEQ_NO%TYPE) IS
     BEGIN
                
            UPDATE XHB_DEFENDANT_ON_OFFENCE
            SET seq_no=p_seq_no 
            WHERE defendant_on_offence_id = p_defendant_on_offence_id;
                
    END update_defendant_on_offence;
    

PROCEDURE update_data_migration_totals(p_court_id   IN  XHB_1745_DATA_MIGRATION_TOTALS.COURT_ID%TYPE,
                    p_total_defendants_on_case  IN  XHB_1745_DATA_MIGRATION_TOTALS.TOTAL_DEFENDANTS_ON_CASE%TYPE,
                        p_total_defendants_on_offence   IN  XHB_1745_DATA_MIGRATION_TOTALS.TOTAL_DEFENDANTS_ON_OFFENCE%TYPE) IS
    BEGIN                
                     
    UPDATE XHB_1745_DATA_MIGRATION_TOTALS 
    SET total_defendants_on_case = p_total_defendants_on_case, 
    total_defendants_on_offence = p_total_defendants_on_offence 
    WHERE court_id = p_court_id; 

    END update_data_migration_totals;

    /*
     *  Updates the status of the data migration process to a given value for a given court
     */

PROCEDURE update_data_migration_status(p_court_id IN XHB_1745_DATA_MIGRATION_TOTALS.COURT_ID%TYPE, 
                                       p_status   IN XHB_1745_DATA_MIGRATION_TOTALS.STATUS%TYPE) IS     
    BEGIN

    update xhb_1745_data_migration_totals
    set    status   = p_status
    where  court_id = p_court_id;

    END update_data_migration_status;
    
/*
*  Returns defendant on offence data by defendant on case ID.  
*  The records are ordered using the rules of precedence specified in the Data Migration design
*/
PROCEDURE get_offences_by_defendant(p_results_out          OUT SYS_REFCURSOR,
                                    p_defendant_on_case_id IN XHB_DEFENDANT_ON_CASE.defendant_on_case_id%TYPE) IS
    BEGIN
    
    OPEN p_results_out FOR
    select   case.case_type,
             case.case_number,
             case.case_sub_type,
             c.charge_id,
             c.crest_charge_id,
             c.crest_charge_seq_no,
             c.charge_type,
             o.offence_id,
             o.crest_offence_id,
             o.crest_offence_seq_no,
             doo.seq_no,
             doo.defendant_on_offence_id,
             doo.crn_id,
             doc.asn
    from     xhb_case                      case,
             xhb_charge                    c,
             xhb_offence                   o,
             xhb_defendant_on_offence      doo,
             xhb_defendant_on_case         doc
    where    doc.defendant_on_case_id  =   p_defendant_on_case_id
    and      doc.case_id               =   case.case_id
    and      doc.defendant_on_case_id  =   doo.defendant_on_case_id
    and      doo.offence_id            =   o.offence_id
    and      o.charge_id               =   c.charge_id
    and      c.charge_type            in ('O','I','B','S','C')
    and    ((case.case_type           in ('S','T'))
    or      (case.case_type            =  'A'
    and      case.case_sub_type       in ('S','C','B')))
    order by decode(case.case_type,
                'T',decode(c.charge_type,'O',0,'I',1,'B',2,3),
                'S',decode(c.charge_type,'S',0,'B',1,2),
                'A',decode(c.charge_type,'C',0,1)
             ),
             c.crest_charge_seq_no,
             o.crest_offence_seq_no;

    
    END get_offences_by_defendant;

END xhb_1745_data_migration_pkg;
/
show errors
