CREATE OR REPLACE PACKAGE xhb_1745_data_migration_pkg AS

    /*
     * Returns selected court details for courts to be processed
     * Inputs : None
     */
    PROCEDURE get_courts(p_results_out             OUT SYS_REFCURSOR);

    /*
     * Returns selected court details for a specific court
     */
    PROCEDURE get_court(p_results_out OUT SYS_REFCURSOR,
                        p_court_id     IN XHB_CASE.COURT_ID%TYPE);

    /*
     * Returns HO Police Force Codes for a given court
     */
    PROCEDURE get_police_force_data(p_results_out  OUT SYS_REFCURSOR,
                                    p_court_id      IN XHB_REF_SYSTEM_CODE.COURT_ID%TYPE);

    /*
     * Returns defendant on case data for the given court ID
     */
    PROCEDURE get_defendants_by_court(p_results_out  OUT SYS_REFCURSOR,
                                      p_court_id     IN  XHB_CASE.COURT_ID%TYPE);
                                      
  /*
   * Updates a defendant on case record for the given defendant_on_case_id
   */
  PROCEDURE update_defendant_on_case(p_defendant_on_case_id IN  XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE,
                     p_asn          IN  XHB_DEFENDANT_ON_CASE.ASN%TYPE,
                     p_ptiurn           IN  XHB_DEFENDANT_ON_CASE.PTIURN%TYPE);

  /*
   * Updates a defendant on case record for the given defendant_on_offence_id
   */
  PROCEDURE update_defendant_on_offence(p_defendant_on_offence_id   IN  XHB_DEFENDANT_ON_OFFENCE.DEFENDANT_ON_OFFENCE_ID%TYPE,
                     p_seq_no           IN  XHB_DEFENDANT_ON_OFFENCE.SEQ_NO%TYPE);



  /*
   * Updates the count of records updated for a given court
   */   
  PROCEDURE update_data_migration_totals(p_court_id IN  XHB_1745_DATA_MIGRATION_TOTALS.COURT_ID%TYPE,
                    p_total_defendants_on_case  IN  XHB_1745_DATA_MIGRATION_TOTALS.TOTAL_DEFENDANTS_ON_CASE%TYPE,
                        P_total_defendants_on_offence   IN  XHB_1745_DATA_MIGRATION_TOTALS.TOTAL_DEFENDANTS_ON_OFFENCE%TYPE);
                 

/*
*  Updates the status of the data migration process to a given value for a given court
*/
PROCEDURE update_data_migration_status(p_court_id IN XHB_1745_DATA_MIGRATION_TOTALS.COURT_ID%TYPE, p_status IN XHB_1745_DATA_MIGRATION_TOTALS.STATUS%TYPE);



/*
*  Returns defendant on offence data by defendant on case ID.  
*  The records are ordered using the rules of precedence specified in the Data Migration design
*/
PROCEDURE get_offences_by_defendant(p_results_out          OUT SYS_REFCURSOR,
                                    p_defendant_on_case_id IN XHB_DEFENDANT_ON_CASE.defendant_on_case_id%TYPE);


END xhb_1745_data_migration_pkg;
/
show errors
