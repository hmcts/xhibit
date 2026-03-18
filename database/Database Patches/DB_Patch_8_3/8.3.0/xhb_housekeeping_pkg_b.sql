CREATE OR REPLACE PACKAGE BODY xhb_housekeeping_pkg AS

/*********************************************************************************
* XHB_HOUSEKEEPING_PKG_B.SQL
* CCN0365 - Housekeping
* Deletes Case and Listing data
*
* Version  Date       Author   Comments
* 1.0      10/03/2009 D Field  Creation
* 1.1      27/03/2009 D Field  Added multi streaming functionality
* 1.3      05/10/2009 D Field  Added delete to XHB_LEO_ADV_LINK and XHB_LEGAL_AID_ORDER tables
*                              Corrected delete count for Listings
************************************************************************************/

  l_error_point    VARCHAR2(50);
  l_hk_run_id      xhb_hk_results.hk_run_id%TYPE;
  l_cases_deleted  xhb_hk_results.cases_deleted%TYPE;
  l_lists_deleted  xhb_hk_results.lists_deleted%TYPE;
  l_cases_error    xhb_hk_results.cases_error%TYPE;
  l_success_log    BOOLEAN;
  l_hk_run_results xhb_hk_results%ROWTYPE;

  TYPE t_log_case_id   IS TABLE OF NUMBER INDEX BY BINARY_INTEGER;
  TYPE t_log_case_no   IS TABLE OF NUMBER INDEX BY BINARY_INTEGER;
  TYPE t_log_case_type IS TABLE OF VARCHAR2(1) INDEX BY BINARY_INTEGER;
  TYPE t_log_court_id  IS TABLE OF NUMBER INDEX BY BINARY_INTEGER;
  TYPE t_log_table     IS TABLE OF VARCHAR2(30) INDEX BY BINARY_INTEGER;

  l_log_case_id     t_log_case_id;
  l_log_case_no     t_log_case_no;
  l_log_case_type   t_log_case_type;
  l_log_court_id    t_log_court_id;
  l_log_table       t_log_table;

   -- selects all cases due for deletion
  CURSOR c_mtbl_case_history(p_total_streams IN NUMBER  
                            ,p_stream_number IN NUMBER 
                            ) IS
    SELECT cas.case_number case_no
    ,      cas.case_type
    ,      cas.court_id
    ,      cas.case_id
    FROM   mtbl_case_history  mtb
    ,      xhb_case           cas 
    WHERE  mtb.case_no   = cas.case_number
    AND    mtb.case_type = cas.case_type
    AND    mtb.court_id  = cas.court_id
    AND    MOD(mtb.case_no,p_total_streams) + 1 = p_stream_number
    ;
    
  l_case_history c_mtbl_case_history%ROWTYPE;

--
----
--

  PROCEDURE update_log (p_hk_results IN xhb_hk_results%ROWTYPE) IS

  BEGIN

    UPDATE xhb_hk_results
    SET    case_start_date = NVL(p_hk_results.case_start_date,case_start_date)
    ,      case_end_date   = NVL(p_hk_results.case_end_date,case_end_date)
    ,      case_status     = NVL(p_hk_results.case_status,case_status)
    ,      case_error_message = NVL(p_hk_results.case_error_message,case_error_message)
    ,      cases_error      = NVL(p_hk_results.cases_error,cases_error)
    ,      cases_deleted      = NVL(p_hk_results.cases_deleted,cases_deleted)
    ,      list_start_date = NVL(p_hk_results.list_start_date,list_start_date)
    ,      list_end_date   = NVL(p_hk_results.list_end_date,list_end_date)
    ,      list_status     = NVL(p_hk_results.list_status,list_status)
    ,      lists_deleted     = NVL(p_hk_results.lists_deleted,lists_deleted)
    ,      list_error_message = NVL(p_hk_results.list_error_message,list_error_message)
    ,      run_end_date   = NVL(p_hk_results.run_end_date,run_end_date)
    ,      error_message   = NVL(p_hk_results.error_message,error_message)
    WHERE  hk_run_id = l_hk_run_id;

    COMMIT;

  END;

--
----
--


  PROCEDURE process_listings  (p_running_list IN NUMBER
                              ,p_warned_list  IN NUMBER
                              ,p_firm_list    IN NUMBER
                              ,p_daily_list   IN NUMBER) IS

    l_delete  VARCHAR2(4000);

  BEGIN

    l_error_point := 'PROCESS LISTINGS 1';

     -- Update log record with case metrics
    l_hk_run_results := NULL;
    l_hk_run_results.list_start_date := SYSDATE;
    l_hk_run_results.list_status := 'I';
    update_log(l_hk_run_results);

    l_error_point := 'PROCESS LISTINGS 1a ';

    INSERT INTO xhb_hk_document_temp
    (clob_id
    ,xml_document_id
    )
    SELECT xml_document_clob_id
    ,      xml_document_id
    FROM   xhb_xml_document
    WHERE  (document_type IN ('RL')
    AND    ADD_MONTHS(creation_date,p_running_list) < SYSDATE)
    OR     (document_type IN ('WL','WLL','WLD')
    AND    ADD_MONTHS(creation_date,p_warned_list) < SYSDATE)
    OR     (document_type IN ('FL','FLD','FLL')
    AND    ADD_MONTHS(creation_date,p_firm_list) < SYSDATE)
    OR     (document_type IN ('DL','DLD','DLL','DLP')
    AND    ADD_MONTHS(creation_date,p_daily_list) < SYSDATE);

    INSERT INTO xhb_hk_blob_id_temp
    (blob_id)
    SELECT formatted_document_blob_id
    FROM   xhb_document_control
    WHERE  xml_document_id IN (SELECT xml_document_id
                               FROM   xhb_hk_document_temp
                              );

    -- 1.1
    l_delete := '
    DELETE xhb_document_recipient
    WHERE  doc_control_id IN (SELECT doc_control_id
                              FROM   xhb_document_control
                              WHERE  xml_document_id IN (SELECT xml_document_id
                                                         FROM   xhb_hk_document_temp
                                                        )
                             )';

    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_doc','aud_doc');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 2';

    -- 1 - ep147
    l_delete := '
    DELETE xhb_document_control
    WHERE  xml_document_id IN (SELECT xml_document_id
                               FROM   xhb_hk_document_temp
                               )';

    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_doc','aud_doc');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 3';

    -- 2.1
    l_delete := '
    DELETE xhb_wll_document
    WHERE  wll_control_id IN (SELECT wll_control_id
                              FROM   xhb_wll_control
                              WHERE  xml_document_id IN (SELECT xml_document_id
                                                         FROM   xhb_hk_document_temp
                                                        )
                             )';

    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_wll','aud_wll');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 4';

    -- 2
    l_delete := '
    DELETE xhb_wll_control
    WHERE  xml_document_id IN (SELECT xml_document_id
                               FROM   xhb_hk_document_temp
                              )';

    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_wll','aud_wll');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 5';

    -- 3  - 166
    l_delete := '
    DELETE xhb_wll_document
    WHERE  xml_document_id IN (SELECT xml_document_id
                               FROM   xhb_hk_document_temp
                              )';

    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_wll','aud_wll');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 6';

    -- 0
    l_delete := '
    DELETE xhb_xml_document
    WHERE  xml_document_id IN (SELECT xml_document_id
                               FROM   xhb_hk_document_temp
                              )' ;

    EXECUTE IMMEDIATE l_delete;

    l_lists_deleted := l_lists_deleted + SQL%ROWCOUNT;

    l_delete := REPLACE (l_delete,'xhb_xml','aud_xml');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 7';


    -- 4.1
    l_delete := '
    DELETE xhb_formatting
    WHERE  xml_document_clob_id IN (SELECT clob_id
                                    FROM   xhb_hk_document_temp
                                   )';

    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_clob','aud_clob');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 8 ';

    -- 4
    l_delete := '
    DELETE xhb_clob
    WHERE  clob_id IN (SELECT clob_id
                       FROM   xhb_hk_document_temp
                      )';

    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_clob','aud_clob');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 8 ';

    -- 5.3
    l_delete := '
    DELETE xhb_email
    WHERE  mime_body_blob_id IN (SELECT blob_id
                                 FROM   xhb_hk_blob_id_temp
                                )';
    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_email','aud_email');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 9 ';

    -- 5.2
    l_delete := '
    DELETE xhb_internet_html
    WHERE  html_blob_id IN (SELECT blob_id
                            FROM   xhb_hk_blob_id_temp
                            )';
    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_inter','aud_inter');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 10 ';

    -- 5.1
    l_delete := '
    DELETE xhb_email2
    WHERE  mime_body_blob_id IN (SELECT blob_id
                                 FROM   xhb_hk_blob_id_temp
                                )';
    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_email','aud_email');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 11 ';

    -- 5
    l_delete := '
    DELETE xhb_blob
    WHERE  blob_id IN (SELECT blob_id
                       FROM   xhb_hk_blob_id_temp
                      )';
    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_blob','aud_blob');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 12 ';

    COMMIT;

    -- Update log record with case metrics
    l_hk_run_results := NULL;
    l_hk_run_results.list_end_date := SYSDATE;
    l_hk_run_results.lists_deleted := l_lists_deleted;
    l_hk_run_results.list_status := 'S';
    update_log(l_hk_run_results);

  EXCEPTION
    WHEN OTHERS THEN

      ROLLBACK;

      -- Update log record with case metrics
      l_hk_run_results := NULL;
      l_hk_run_results.list_end_date := SYSDATE;
      l_hk_run_results.list_status := 'F';
      l_hk_run_results.list_error_message := SUBSTR(l_error_point ||' '||SQLERRM,1,2000);

      update_log(l_hk_run_results);

  END process_listings;

--
----
--

  PROCEDURE log_delete (p_table_name IN VARCHAR2) IS


  BEGIN

    -- Delete statement may not have actually deleted any records
    IF l_success_log AND SQL%ROWCOUNT > 0 THEN

      l_log_case_id  (l_log_case_id.COUNT   + 1) := l_case_history.case_id;
      l_log_case_no  (l_log_case_no.COUNT   + 1) := l_case_history.case_no;
      l_log_case_type(l_log_case_type.COUNT + 1) := l_case_history.case_type;
      l_log_court_id (l_log_court_id.COUNT  + 1) := l_case_history.court_id;
      l_log_table    (l_log_table.COUNT     + 1) := p_table_name;

    END IF;

  END log_delete;

--
----
--

  PROCEDURE delete_hearing IS

    l_delete VARCHAR2(2000);

  BEGIN

    l_error_point := 'DELETE HEARING ';

    -- 5
    l_delete := 'DELETE xhb_exporta
                 WHERE  hearing_id IN (SELECT hearing_id
                                       FROM   xhb_hearing
                                       WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_EXPORTA' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 1 ';

    -- 4
    l_delete := '
    DELETE xhb_hearing_leg_rep
    WHERE  hearing_id IN (SELECT hearing_id
                          FROM   xhb_hearing
                          WHERE  case_id = :1
                         )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_HEARING_LEG_REP');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 2 ';

    -- 3
    l_delete := '
    DELETE xhb_def_hearing_record
    WHERE  hearing_id IN (SELECT hearing_id
                          FROM   xhb_hearing
                          WHERE  case_id = :1
                         )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DEF_HEARING_RECORD');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 3 ';

    -- 2.1.1
    l_delete := '
    DELETE xhb_sh_judge
    WHERE  sh_attendee_id IN (SELECT sh_attendee_id
                              FROM   xhb_sched_hearing_attendee
                              WHERE  sh_justice_id IN (SELECT sh_justice_id
                                                       FROM   xhb_sh_justice
                                                       WHERE  hearing_id IN (SELECT hearing_id
                                                                             FROM   xhb_hearing
                                                                             WHERE  case_id = :1
                                                                            )
                                                      )
                             )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SH_JUDGE');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 4 ';

    -- 2.1
    l_delete := '
    DELETE xhb_sched_hearing_attendee
    WHERE  sh_justice_id IN (SELECT sh_justice_id
                             FROM   xhb_sh_justice
                             WHERE  hearing_id IN (SELECT hearing_id
                                                   FROM   xhb_hearing
                                                   WHERE  case_id = :1
                                                  )
                            )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SCHED_HEARING_ATTENDEE');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 5 ';

    -- 2
    l_delete := '
    DELETE xhb_sh_justice
    WHERE  hearing_id IN (SELECT hearing_id
                          FROM   xhb_hearing
                          WHERE  case_id = :1
                         )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SH_JUSTICE');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 6 ';

    -- 1.1
    l_delete := '
    DELETE xhb_cr_live_display
    WHERE  scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                    FROM   xhb_scheduled_hearing
                                    WHERE  hearing_id IN (SELECT hearing_id
                                                          FROM   xhb_hearing
                                                          WHERE  case_id = :1
                                                          )
                                   )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CR_LIVE_DISPLAY');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 7 ';

    -- 1.2
    l_delete := '
    DELETE xhb_cr_live_internet
    WHERE  scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                    FROM   xhb_scheduled_hearing
                                    WHERE  hearing_id IN (SELECT hearing_id
                                                          FROM   xhb_hearing
                                                          WHERE  case_id = :1
                                                          )
                                   )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CR_LIVE_INTERNET' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 8 ';

    -- 1.3
    l_delete := '
    DELETE xhb_cr_live_status
    WHERE  scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                    FROM   xhb_scheduled_hearing
                                    WHERE  hearing_id IN (SELECT hearing_id
                                                          FROM   xhb_hearing
                                                          WHERE  case_id = :1
                                                          )
                                   )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CR_LIVE_STATUS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 9 ';

    -- 1.4
    l_delete := '
    DELETE xhb_court_log_entry
    WHERE  scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                    FROM   xhb_scheduled_hearing
                                    WHERE  hearing_id IN (SELECT hearing_id
                                                          FROM   xhb_hearing
                                                          WHERE  case_id = :1
                                                          )
                                   )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_COURT_LOG_ENTRY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 10 ';

    -- 1.5
    l_delete := '
    DELETE xhb_sh_leg_rep
    WHERE  scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                    FROM   xhb_scheduled_hearing
                                    WHERE  hearing_id IN (SELECT hearing_id
                                                          FROM   xhb_hearing
                                                          WHERE  case_id = :1
                                                          )
                                   )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SH_LEG_REP' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 11 ';

    -- 1.6
    l_delete := '
    DELETE xhb_sched_hearing_defendant
    WHERE  scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                    FROM   xhb_scheduled_hearing
                                    WHERE  hearing_id IN (SELECT hearing_id
                                                          FROM   xhb_hearing
                                                          WHERE  case_id = :1
                                                          )
                                   )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SCHED_HEARING_DEFENDANT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 12 ';

    -- 1.7.1
    l_delete := '
    DELETE xhb_sh_judge
    WHERE  sh_attendee_id IN (SELECT sh_attendee_id
                              FROM   xhb_sched_hearing_attendee
                              WHERE  scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                                              FROM   xhb_scheduled_hearing
                                                              WHERE  hearing_id IN (SELECT hearing_id
                                                                                    FROM   xhb_hearing
                                                                                    WHERE  case_id = :1
                                                                                    )
                                                             )
                              )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SH_JUDGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 13 ';

    -- 1.7
    l_delete := '
    DELETE xhb_sched_hearing_attendee
    WHERE  scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                    FROM   xhb_scheduled_hearing
                                    WHERE  hearing_id IN (SELECT hearing_id
                                                          FROM   xhb_hearing
                                                          WHERE  case_id = :1
                                                          )
                                   )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SCHED_HEARING_ATTENDEE');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 14 ';

    -- 1
    l_delete := '
    DELETE xhb_scheduled_hearing
    WHERE  hearing_id IN (SELECT hearing_id
                          FROM   xhb_hearing
                          WHERE  case_id = :1
                         )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SCHEDULED_HEARING' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 15 ';

    -- 0
    l_delete := '
    DELETE xhb_hearing
    WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_HEARING' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE HEARING 16 ';

  END delete_hearing;


--
----
--

  PROCEDURE delete_charge IS

    l_delete VARCHAR2(2000);

  BEGIN

    l_error_point := 'DELETE CHARGE ';

    -- 4
    l_delete := 'DELETE xhb_defendant_charge
                 WHERE  charge_id IN (SELECT charge_id
                                      FROM   xhb_charge
                                      WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DEFENDANT_CHARGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CHARGE 1';

    -- 3
    l_delete := 'DELETE xhb_joinder_charge
                 WHERE  charge_id IN (SELECT charge_id
                                      FROM   xhb_charge
                                      WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_JOINDER_CHARGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CHARGE 2';

    -- 2
    l_delete := 'DELETE xhb_breach
                 WHERE  charge_id IN (SELECT charge_id
                                      FROM   xhb_charge
                                      WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_BREACH' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CHARGE 3';

    -- 1
    l_delete := 'DELETE xhb_offence
                 WHERE  charge_id IN (SELECT charge_id
                                      FROM   xhb_charge
                                      WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_OFFENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CHARGE 4';

    -- 0
    l_delete := 'DELETE xhb_charge
                 WHERE  case_id = :1
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CHARGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CHARGE 5';


  END delete_charge;

--
----
--

  PROCEDURE delete_defendant IS

    l_delete VARCHAR2(4000);

    -- Returns defendants on case which are not on any other cases
    CURSOR c_defendant_on_case (b_case_id NUMBER) IS
      SELECT doc.defendant_id
      FROM   xhb_defendant_on_case  doc
      WHERE  doc.case_id = b_case_id
      AND    NOT EXISTS (SELECT '1'
                         FROM   xhb_defendant_on_case doc2
                         WHERE  doc2.case_id != doc.case_id
                         AND    doc2.defendant_id = doc.defendant_id
                         );

  BEGIN

    l_error_point := 'DELETE DEFENDANT';

    -- 16
    l_delete := 'DELETE xhb_verdict
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 1a ';

    -- 1.15.2
    l_delete := 'DELETE xhb_verdict
                 WHERE  disposal2_id IN (SELECT disposal2_id
                                         FROM   xhb_disposal2
                                         WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                         FROM   xhb_defendant_on_case
                                                                         WHERE  case_id = :1
                                                                        )
                                        )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 1b ';


    -- 1.15.1
    l_delete := 'DELETE xhb_disposal_line
                 WHERE  disposal2_id IN (SELECT disposal2_id
                                         FROM   xhb_disposal2
                                         WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                         FROM   xhb_defendant_on_case
                                                                         WHERE  case_id = :1
                                                                        )
                                        )';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DISPOSAL_LINE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 2 ';

    -- 15
    l_delete := 'DELETE xhb_disposal2
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DISPOSAL2' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 3 '; 

    -- 14
   l_delete := 'DELETE xhb_verdict
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 4 ';

    -- 13
    l_delete := 'DELETE xhb_court_log_entry
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_COURT_LOG_ENTRY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 5 ';

    -- 12
    l_delete := 'DELETE xhb_import_export_status
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_IMPORT_EXPORT_STATUS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 6 ';

    -- 11
    l_delete := 'DELETE xhb_order
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_ORDER' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 7 ';

    -- 9
    l_delete := 'DELETE xhb_psr_request
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_PSR_REQUEST' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 9 ';

    -- 8
    l_delete := 'DELETE xhb_direction_attend
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DIRECTION_ATTEND' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 10 ';

    -- 7
    l_delete := 'DELETE xhb_joinder_defendant_on_case
                 WHERE  defendant_on_case_id_1 IN (SELECT defendant_on_case_id
                                                   FROM   xhb_defendant_on_case
                                                   WHERE  case_id = :1
                                                   )
                 OR     defendant_on_case_id_2 IN (SELECT defendant_on_case_id
                                                   FROM   xhb_defendant_on_case
                                                   WHERE  case_id = :2
                                                   )';


    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id, l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_JOINDER_DEFENDANT_ON_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id,l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 11 ';

    -- 6
    l_delete := 'DELETE xhb_def_on_case_ref_sol_firm
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DEF_ON_CASE_REF_SOL_FIRM' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 12 ';

    -- 5
    l_delete := 'DELETE xhb_court_log_entry
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_COURT_LOG_ENTRY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 13 ';

    -- 4
    l_delete := 'DELETE xhb_directions_for_defendant
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DIRECTIONS_FOR_DEFENDANT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 14 ';

    -- 3
    l_delete := 'DELETE xhb_import_export_status
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_IMPORT_EXPORT_STATUS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 15 ';

    -- 2.2
    l_delete := 'DELETE xhb_plea
                 WHERE  defendant_charge_id IN (SELECT defendant_charge_id
                                                FROM   xhb_defendant_charge
                                                WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                FROM   xhb_defendant_on_case
                                                                                WHERE  case_id = :1
                                                                               )
                                               )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_PLEA' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 15a ';

    -- 2.1
    l_delete := 'DELETE xhb_verdict
                 WHERE  defendant_charge_id IN (SELECT defendant_charge_id
                                                FROM   xhb_defendant_charge
                                                WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                FROM   xhb_defendant_on_case
                                                                                WHERE  case_id = :1
                                                                               )
                                               )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 16 ';


    -- 2
    l_delete := 'DELETE xhb_defendant_charge
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DEFENDANT_CHARGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 17 ';


    -- 1.5.1
    l_delete := 'DELETE xhb_disposal_line
                 WHERE  disposal2_id IN (SELECT disposal2_id
                                         FROM   xhb_disposal2
                                         WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                                            FROM   xhb_defendant_on_offence
                                                                            WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                                            FROM   xhb_defendant_on_case
                                                                                                            WHERE  case_id = :1
                                                                                                            )
                                                                           )
                                        )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DISPOSAL_LINE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 18 ';


    -- 1.5
    l_delete := 'DELETE xhb_disposal2
                 WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence
                                                    WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                    FROM   xhb_defendant_on_case
                                                                                    WHERE  case_id = :1
                                                                                   )
                                                   )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DISPOSAL2' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 19 ';

    -- 1.4
    l_delete := 'DELETE xhb_plea
                 WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence
                                                    WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                    FROM   xhb_defendant_on_case
                                                                                    WHERE  case_id = :1
                                                                                   )
                                                   )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_PLEA' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 20 ';

    -- 1.3
    l_delete := 'DELETE xhb_verdict
                 WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence
                                                    WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                    FROM   xhb_defendant_on_case
                                                                                    WHERE  case_id = :1
                                                                                   )
                                                   )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 21 ';

    -- 1.2.1
    l_delete := 'DELETE xhb_disposal_reference
                 WHERE  disposal_id IN (SELECT disposal_id
                                        FROM   xhb_disposal
                                        WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                                           FROM   xhb_defendant_on_offence
                                                                           WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                                           FROM   xhb_defendant_on_case
                                                                                                           WHERE  case_id = :1
                                                                                                           )
                                                                          )
                                        OR      defendant_on_case_ID IN (SELECT defendant_on_case_id
                                                                         FROM   xhb_defendant_on_case
                                                                         WHERE  case_id = :2
                                                                        )
                                        )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id,l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DISPOSAL_REFERENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id,l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 22 ';

    -- 1.2
    l_delete := 'DELETE xhb_disposal
                 WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence
                                                    WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                    FROM   xhb_defendant_on_case
                                                                                    WHERE  case_id = :1
                                                                                   )
                                                   )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DISPOSAL' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 23';

    -- 1.1
    l_delete := 'DELETE xhb_court_log_entry
                 WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence
                                                    WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                    FROM   xhb_defendant_on_case
                                                                                    WHERE  case_id = :1
                                                                                   )
                                                   )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_COURT_lOG_ENTRY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 24';

    -- 1
    l_delete := 'DELETE xhb_defendant_on_offence
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DEFENDANT_ON_OFFENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 25';

    -- 10
    l_delete := 'DELETE xhb_disposal
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DISPOSAL' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 25a';

    -- 17
    l_delete := 'DELETE xhb_leo_adv_link
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_LEO_ADV_LINK' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 25b';

    -- 18.1
    l_delete := 'DELETE xhb_leo_adv_link
                 WHERE  legal_aid_order_id IN (SELECT legal_aid_order_id
                                               FROM   xhb_legal_aid_order
                                               WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                               FROM   xhb_defendant_on_case
                                                                               WHERE  case_id = :1
                                                                              )
                                              )
                ';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_LEO_ADV_LINK' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 25c';

    -- 18
    l_delete := 'DELETE xhb_legal_aid_order
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_LEGAL_AID_ORDER' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 25d';


    -- 0
    l_delete := 'DELETE xhb_defendant_on_case
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DEFENDANT_ON_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    l_error_point := 'DELETE DEFENDANT 25 b';
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE DEFENDANT 26';

    -- loop through all defendants on case which are not on any other cases
    FOR l_def IN c_defendant_on_case (l_case_history.case_id) LOOP

      -- 0.1.1
      l_delete := 'DELETE xhb_defendant_reference
                   WHERE  defendant_id = :1';

      EXECUTE IMMEDIATE l_delete USING l_def.defendant_id;
      log_delete (p_table_name      => 'XHB_DEFENDANT_REFERENCE' );
      l_delete := REPLACE (l_delete,'xhb','aud');
      EXECUTE IMMEDIATE l_delete USING l_def.defendant_id;
      l_error_point := 'DELETE DEFENDANT 27';

      -- 0.1
      l_delete := 'DELETE xhb_defendant
                   WHERE  defendant_id = :1';

      EXECUTE IMMEDIATE l_delete USING l_def.defendant_id;
      log_delete (p_table_name      => 'XHB_DEFENDANT' );
      l_delete := REPLACE (l_delete,'xhb','aud');
      EXECUTE IMMEDIATE l_delete USING l_def.defendant_id;
      l_error_point := 'DELETE DEFENDANT 28';

    END LOOP;

    l_error_point := 'DELETE DEFENDANT 29';

  END delete_defendant;

--
----
--

  PROCEDURE delete_case IS

    l_delete VARCHAR2(4000);

  BEGIN

    l_error_point := 'DELETE CASE ';

    -- 1
    -- Has no audit table
    DELETE xhb_case_refresh_resynch
    WHERE  case_id = l_case_history.case_id;

    log_delete (p_table_name      => 'XHB_CASE_REFRESH_RESYNCH' );
    l_error_point := 'DELETE CASE 1 ';

    -- 2
    l_delete := 'DELETE xhb_charge_differences
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CHARGE_DIFFERENCES' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 2 ';

    -- 3
    l_delete := 'DELETE xhb_case_reference
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CASE_REFERENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 3 ';

    -- 4
    l_delete := 'DELETE xhb_case_prosecutor_agency
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CASE_PROSECUTOR_AGENCY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 4 ';

    -- 5
    l_delete := 'DELETE xhb_progress_trigger
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_PROGRESS_TRIGGER' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 5 ';

    -- 6.1.1
    l_delete := 'DELETE xhb_witness
                 WHERE  session_id IN (SELECT session_id
                                       FROM   xhb_skeleton_session
                                       WHERE  skeleton_id IN (SELECT skeleton_id
                                                              FROM xhb_skeleton_schedule
                                                              WHERE  case_id = :1
                                                             )
                                      )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_WITNESS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 6 ';

    -- 6.1
    l_delete := 'DELETE xhb_skeleton_session
                 WHERE  skeleton_id IN (SELECT skeleton_id
                                        FROM xhb_skeleton_schedule
                                        WHERE  case_id = :1
                                       )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SKELETON_SESSION' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 7 ';

    -- 6.2
    l_delete := 'DELETE xhb_skeleton_day
                 WHERE  skeleton_id IN (SELECT skeleton_id
                                        FROM xhb_skeleton_schedule
                                        WHERE  case_id = :1
                                       )';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SKELETON_DAY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 8 ';


    -- 6
    l_delete := 'DELETE xhb_skeleton_schedule
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_SKELETON_SCHEDULE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 9 ';

    -- 7
    l_delete := 'DELETE xhb_time
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_TIME' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 10 ';

    -- 8
    l_delete := 'DELETE xhb_case_app_reason
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CASE_APP_REASON' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 11 ';

    -- 9
    l_delete := 'DELETE xhb_directions_for_case
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_DIRECTIONS_FOR_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 12 ';

    -- 10
    l_delete := 'DELETE xhb_witness
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_WITNESS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 13 ';

    -- 11
    l_delete := 'DELETE xhb_indictment_history
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_INDICTMENT_HISTORY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 14 ';

    -- 12
    l_delete := 'DELETE xhb_rs_case
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_RS_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 15 ';

    -- 0
    l_delete := 'DELETE xhb_case
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    log_delete (p_table_name      => 'XHB_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING l_case_history.case_id;
    l_error_point := 'DELETE CASE 16 ';

  END delete_case;

--
----
--


  PROCEDURE process_cases(p_case_limit    IN NUMBER
                         ,p_total_streams IN NUMBER 
                         ,p_stream_number IN NUMBER 
                         ) IS

    l_error_message xhb_hk_results.error_message%TYPE;

  BEGIN

    l_error_point := 'PROCESS CASES 1';

    -- Update log record with case metrics
    l_hk_run_results := NULL;
    l_hk_run_results.case_start_date := SYSDATE;
    l_hk_run_results.case_status := 'I';
    update_log(l_hk_run_results);

    -- loop through all cases due for deletion
    FOR l_cases IN c_mtbl_case_history(p_total_streams, p_stream_number) LOOP

      BEGIN
        -- A case limit of zero means there is no limit
        IF p_case_limit > 0 THEN

          -- exit out of cursor loop if limit reached
          IF l_cases_deleted + l_cases_error >= p_case_limit THEN
            EXIT;
          END IF;

        END IF;

        l_case_history := l_cases;

        delete_hearing;

        delete_defendant;

        delete_charge;

        delete_case;

        COMMIT;

        l_cases_deleted := l_cases_deleted + 1;

      EXCEPTION
        WHEN OTHERS THEN

          -- Rollback all the deletes for this case
          ROLLBACK;

          l_error_message := SUBSTR(l_error_point||' '||SQLERRM,1,2000);

          -- this will log an error for an individual case
          INSERT INTO xhb_hk_error_log
          (hk_run_id
          ,case_no
          ,case_type
          ,court_id
          ,case_id
          ,error_message
          )
          VALUES
          (l_hk_run_id
          ,l_cases.case_no
          ,l_cases.case_type
          ,l_cases.court_id
          ,l_cases.case_id
          ,l_error_message
          );

          COMMIT;

          l_cases_error := l_cases_error + 1;

      END;

      l_error_point := 'PROCESS CASES 2';


      COMMIT;


    END LOOP;

    -- Update log record with case metrics
    l_hk_run_results := NULL;

    IF l_cases_error > 0 THEN
      l_hk_run_results.case_status := 'E';
    ELSE
      l_hk_run_results.case_status := 'S';
    END IF;
    l_hk_run_results.cases_deleted := l_cases_deleted;
    l_hk_run_results.cases_error   := l_cases_error;
    l_hk_run_results.case_end_date := SYSDATE;
    update_log(l_hk_run_results);

  EXCEPTION

    WHEN OTHERS THEN

    -- Update log record with case metrics
    l_hk_run_results := NULL;

    l_hk_run_results.case_status   := 'F';
    l_hk_run_results.case_error_message := SUBSTR(l_error_point ||' '||SQLERRM,1,2000);
    l_hk_run_results.cases_deleted := l_cases_deleted;
    l_hk_run_results.cases_error   := l_cases_error;
    l_hk_run_results.case_end_date := SYSDATE;
    update_log(l_hk_run_results);

  END process_cases;

--
----
--

  PROCEDURE write_error_log_file(p_hk_run_id IN NUMBER) IS

    l_file utl_file.file_type;

    CURSOR c_xhb_hk_error_log (b_hk_run_id NUMBER) IS
      SELECT *
      FROM   xhb_hk_error_log
      WHERE  hk_run_id = b_hk_run_id;

  BEGIN

    l_error_point := 'WRITE ERROR LOG FILE ';

    FOR l_errors IN c_xhb_hk_error_log (p_hk_run_id)  LOOP

      IF c_xhb_hk_error_log%ROWCOUNT = 1 THEN
        l_file := utl_file.fopen('HK_LOGS','HK_'||LPAD(p_hk_run_id,10,'0')||'_ERROR.log','a');
        utl_file.put_line(l_file,'CASE_ID  CASE_NO  T COURT_ID ERROR_MESSAGE',TRUE);
      END IF;

      utl_file.put_line(l_file,LPAD(l_errors.case_id,8)||','||
                               LPAD(l_errors.case_no,8)||','||
                               l_errors.case_type||','||
                               LPAD(l_errors.court_id,8)||','||
                               l_errors.error_message
                               ,TRUE);

    END LOOP;

    -- close file
    IF utl_file.is_open(l_file) THEN
      utl_file.fclose(l_file);
    END IF;

  EXCEPTION

    -- any exceptions for file handling will go here
    WHEN OTHERS THEN

      -- close file
      IF utl_file.is_open(l_file) THEN
        utl_file.fclose(l_file);
      END IF;

  END write_error_log_file;

--
----
--

  PROCEDURE write_success_log_file(p_hk_run_id IN NUMBER) IS

    l_file utl_file.file_type;


  BEGIN

    l_error_point := 'WRITE SUCCESS LOG FILE 2';

    IF l_success_log AND l_log_case_id.COUNT > 0 THEN

      l_file := utl_file.fopen('HK_LOGS','HK_'||LPAD(p_hk_run_id,10,'0')||'_SUCCESS.log','a');
      utl_file.put_line(l_file,'CASE_ID  CASE_NO  T COURT_ID TABLE',TRUE);

      FOR l_success IN 1..l_log_case_id.COUNT  LOOP


        utl_file.put_line(l_file,LPAD(l_log_case_id(l_success),8)||' '||
                                 LPAD(l_log_case_no(l_success),8)||','||
                                 l_log_case_type(l_success)||','||
                                 LPAD(l_log_court_id(l_success),8)||','||
                                 l_log_table(l_success)
                                 ,TRUE);

      END LOOP;

      -- close file
      IF utl_file.is_open(l_file) THEN
        utl_file.fclose(l_file);
      END IF;

    END IF;

  EXCEPTION

    -- any exceptions for file handling will go here
    WHEN OTHERS THEN

      -- close file
      IF utl_file.is_open(l_file) THEN
        utl_file.fclose(l_file);
      END IF;

  END write_success_log_file;

--
----
--

  PROCEDURE write_metrics_log_file(p_hk_run_id IN NUMBER) IS

    l_file utl_file.file_type;

    CURSOR c_xhb_hk_results (b_hk_run_id NUMBER) IS
      SELECT *
      FROM   xhb_hk_results
      WHERE  hk_run_id = b_hk_run_id;

    l_rec_results c_xhb_hk_results%ROWTYPE;

    l_status_desc VARCHAR2(100);
    l_run_type    VARCHAR2(100);

  BEGIN

    l_error_point := 'WRITE METRICS LOG FILE ';

    l_file := utl_file.fopen('HK_LOGS','HK_'||LPAD(p_hk_run_id,10,'0')||'_METRICS.log','a');

    OPEN  c_xhb_hk_results(p_hk_run_id);
    FETCH c_xhb_hk_results INTO l_rec_results;

    IF c_xhb_hk_results%NOTFOUND THEN
      utl_file.put_line(l_file,'No record of HK run '||p_hk_run_id||' can be found',TRUE);
    ELSE
      utl_file.put_line(l_file,'Log file produced on '||TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),TRUE);
      utl_file.new_line(l_file,1);
      utl_file.put_line(l_file,'Housekeeping Run Id. '||p_hk_run_id,TRUE);
      utl_file.put_line(l_file,'Started On   : '||TO_CHAR(l_rec_results.run_start_date,'DD-MON-YYYY HH24:MI:SS'),TRUE);
      utl_file.put_line(l_file,'Completed On : '||TO_CHAR(l_rec_results.run_end_date  ,'DD-MON-YYYY HH24:MI:SS'),TRUE);
      utl_file.new_line(l_file,1);

      IF l_rec_results.run_type = 'C' THEN
        l_run_type := 'Cases only';
      ELSIF l_rec_results.run_type = 'L' THEN
        l_run_type := 'Listings only';
      ELSIF l_rec_results.run_type = 'A' THEN
        l_run_type := 'Cases and Listings';
      ELSE
        l_run_type := 'UNKNOWN';
      END IF;

      utl_file.put_line(l_file,'Run Type : '||l_run_type);
      utl_file.new_line(l_file,1);

      IF l_rec_results.error_message IS NOT NULL THEN
        utl_file.put_line(l_file,'ERROR ENCOUNTERED DURING HK RUN');
        utl_file.put_line(l_file,l_rec_results.error_message);
      END IF;

      IF l_rec_results.run_type IN ('C','A') THEN
        utl_file.put_line(l_file,'Cases Started On   : '||TO_CHAR(l_rec_results.case_start_date,'DD-MON-YYYY HH24:MI:SS'),TRUE);
        utl_file.put_line(l_file,'Cases Completed On : '||TO_CHAR(l_rec_results.case_end_date,'DD-MON-YYYY HH24:MI:SS'),TRUE);

        IF l_rec_results.case_status = 'S' THEN
          l_status_desc := ' - Success';
        ELSIF l_rec_results.case_status = 'F' THEN
          l_status_desc := ' - Failure - see error message below';
        ELSIF l_rec_results.case_status = 'E' THEN
          l_status_desc := ' - Some cases not deleted - see count below';
        ELSE
          l_status_desc := ' - Unknown status';
        END IF;

        utl_file.put_line(l_file,'Cases Status : '||l_rec_results.case_status||l_status_desc,TRUE);
        IF l_rec_results.case_status = 'F' THEN
          utl_file.put_line(l_file,l_rec_results.case_error_message,TRUE);
        END IF;

        utl_file.put_line(l_file,'Cases Deleted :'||l_rec_results.cases_deleted,TRUE);
        utl_file.put_line(l_file,'Cases Error   :'||l_rec_results.cases_error,TRUE);
        utl_file.new_line(l_file,1);
        utl_file.put_line(l_file,'Cases Limit   :'||l_rec_results.case_limit,TRUE);
        utl_file.new_line(l_file,1);

      END IF;

      IF l_rec_results.run_type IN ('L','A') THEN
        utl_file.put_line(l_file,'Lists Started On   : '||TO_CHAR(l_rec_results.list_start_date,'DD-MON-YYYY HH24:MI:SS'),TRUE);
        utl_file.put_line(l_file,'Lists Completed On : '||TO_CHAR(l_rec_results.list_end_date,'DD-MON-YYYY HH24:MI:SS'),TRUE);

        IF l_rec_results.list_status = 'S' THEN
          l_status_desc := ' - Success';
        ELSIF l_rec_results.list_status = 'F' THEN
          l_status_desc := ' - Failure - see error message below';
        ELSE
          l_status_desc := ' - Unknown status';
        END IF;

        utl_file.put_line(l_file,'Lists Status : '||l_rec_results.list_status||l_status_desc,TRUE);
        IF l_rec_results.list_status = 'F' THEN
          utl_file.put_line(l_file,l_rec_results.list_error_message,TRUE);
        END IF;

        utl_file.new_line(l_file,1);
        utl_file.put_line(l_file,'Running Parameter :'||l_rec_results.running_list,TRUE);
        utl_file.put_line(l_file,'Warned Parameter  :'||l_rec_results.warned_list,TRUE);
        utl_file.put_line(l_file,'Firm Parameter    :'||l_rec_results.firm_list,TRUE);
        utl_file.put_line(l_file,'Daily Parameter   :'||l_rec_results.daily_list,TRUE);

        utl_file.new_line(l_file,1);
        utl_file.put_line(l_file,'Lists Deleted :'||l_rec_results.lists_deleted,TRUE);

      END IF;

      -- show oracle error message for a failure
      utl_file.new_line(l_file,1);
      IF l_rec_results.case_status = 'F' THEN
        utl_file.put_line(l_file,l_rec_results.error_message,TRUE);
        utl_file.new_line(l_file,1);
      END IF;

      utl_file.new_line(l_file,1);
      utl_file.put_line(l_file,'End of Report',TRUE);

    END IF;

    CLOSE c_xhb_hk_results;

    utl_file.fclose(l_file);

  EXCEPTION

    -- any exceptions for file handling will go here
    WHEN OTHERS THEN

      -- close cursor
      IF c_xhb_hk_results%ISOPEN THEN
        CLOSE c_xhb_hk_results;
      END IF;

      -- close file
      IF utl_file.is_open(l_file) THEN
        utl_file.fclose(l_file);
      END IF;


  END write_metrics_log_file;

--
----
--

  PROCEDURE initiate_run (p_run_type     IN VARCHAR2
                         ,p_case_limit   IN NUMBER DEFAULT 0
                         ,p_running_list IN NUMBER
                         ,p_warned_list  IN NUMBER
                         ,p_firm_list    IN NUMBER
                         ,p_daily_list   IN NUMBER
                         ,p_success_log  IN BOOLEAN DEFAULT FALSE
                         ,p_total_streams IN NUMBER  DEFAULT 1  
                         ,p_stream_number IN NUMBER  DEFAULT 1  
                         ) IS

  BEGIN

    l_error_point := 'INITIATE RUN ';

    l_cases_deleted := 0;
    l_lists_deleted := 0;
    l_cases_error   := 0;

    SELECT hk_run_id_seq.NEXTVAL
    INTO   l_hk_run_id
    FROM   sys.dual;

    INSERT INTO xhb_hk_results
    (hk_run_id
    ,run_type
    ,run_start_date
    ,cases_deleted
    ,lists_deleted
    ,cases_error
    ,case_limit
    ,running_list
    ,warned_list
    ,firm_list
    ,daily_list)
    VALUES
    (l_hk_run_id
    ,UPPER(p_run_type)
    ,SYSDATE
    ,0
    ,0
    ,0
    ,p_case_limit
    ,p_running_list
    ,p_warned_list
    ,p_firm_list
    ,p_daily_list);

    COMMIT;

    l_success_log := p_success_log;

    -- (A)ll or (C)ases
    IF UPPER(p_run_type) IN ('A','C') THEN
      process_cases(p_case_limit
                   ,p_total_streams 
                   ,p_stream_number
                   );

      l_error_point := 'INITIATE RUN 2 ';

      -- remove Cases from interface table which have have been deleted
      DELETE mtbl_case_history mch
      WHERE  MOD(mch.case_no,p_total_streams) + 1 = p_stream_number 
      AND NOT EXISTS (SELECT 1
                      FROM   xhb_case  cas
                      WHERE  cas.case_number = mch.case_no
                      AND    cas.case_type   = mch.case_type
                      AND    cas.court_id    = mch.court_id
                      );

      COMMIT;

      l_error_point := 'INITIATE RUN 3 ';

    END IF;

    -- (A)ll or (L)istings
    IF UPPER(p_run_type) IN ('A','L') THEN

      process_listings(p_running_list
                       ,p_warned_list
                       ,p_firm_list
                       ,p_daily_list);

    END IF;

    -- write errors to external file
    write_error_log_file(l_hk_run_id);

    -- write success to external file
    write_success_log_file(l_hk_run_id);

     -- Update log record with metrics
    l_hk_run_results := NULL;
    l_hk_run_results.run_end_date := SYSDATE;
    update_log(l_hk_run_results);

    -- write metrics to external file
    write_metrics_log_file(l_hk_run_id);

  EXCEPTION
    WHEN OTHERS THEN

     -- Update log record with metrics
    l_hk_run_results := NULL;
    l_hk_run_results.run_end_date := SYSDATE;
    l_hk_run_results.error_message := SUBSTR(l_error_point ||SQLERRM,1,2000);

    update_log(l_hk_run_results);

  END initiate_run;


END xhb_housekeeping_pkg;

/

