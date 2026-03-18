create or replace PACKAGE BODY        xhb_housekeeping_pkg AS

/*********************************************************************************
* xhb_housekeeping_pkg_PKB.SQL
* CCN0365 - Housekeping
* Deletes Case and Listing data. Procedures identified as Ext have declarations in the 
* package header and can therefore be called externally.
*
*  Procedures in this package:
*          PROCEDURE update_log
*          PROCEDURE process_listings
*          PROCEDURE log_delete
*          PROCEDURE delete_hearing
*          PROCEDURE delete_charge
*          PROCEDURE delete_defendant
*          PROCEDURE delete_case
*          PROCEDURE process_cases       
*   Ext    PROCEDURE write_error_log_file
*          PROCEDURE write_success_log_file
*   Ext    PROCEDURE write_metrics_log_file
*   Ext    PROCEDURE initiate_run
*   Ext    PROCEDURE obsolete_case
*   Ext    PROCEDURE insert_case_history
*          PROCEDURE delete_table_ctx
*   Ext    PROCEDURE delete_case_ctx 
*
* Version  Date       Author   	Comments
* 1.0      10/03/2009 D Field  	Creation
* 1.1      27/03/2009 D Field  	Added multi streaming functionality
* 1.3      05/10/2009 D Field  	Added delete to XHB_LEO_ADV_LINK and XHB_LEGAL_AID_ORDER tables
*                              	Corrected delete count for Listings
* 1.4	   19/08/2010 S Atwell 	  Added delete to XHB_BAIL_APPLICATION and amended typo in deletion of
*			       	                  aud_formatting
* 1.5	   08/04/2011 B Hingston  Added delete to XHB_INDICTMENT_LOG (RFC2867)
* 1.6	   10/07/2014 B Hingston  Added delete to XHB_HATE_SENTENCING (RFS4224)
* 1.7	   05/05/2017 B Hingston  Added delete to XHB_CHARGES_LOG (L-S-4975-01)
* 1.8      12/06/2018 C Cash      Obsolete Cases and populate XHB_CASE_HISTORY
* 1.9      02/07/2018 J Riley     CTX-2227 Added delete_case_ctx - does more tables than delete_case and 
*                                 can be called independently
* 1.10     26/02/2019 M Newman  Added delete for XHB_SH_JUSTICE for CTX_3666
************************************************************************************/

  l_error_point    VARCHAR2(50);
  l_hk_run_id      xhb_hk_results.hk_run_id%TYPE;
  l_cases_deleted  xhb_hk_results.cases_deleted%TYPE;
  l_lists_deleted  xhb_hk_results.lists_deleted%TYPE;
  l_cases_error    xhb_hk_results.cases_error%TYPE;
  l_success_log    BOOLEAN;
  l_hk_run_results xhb_hk_results%ROWTYPE;
  
  l_hk3_run_id		xhb_hk3_results.hk3_run_id%TYPE;
  l_hk3_run_results xhb_hk3_results%ROWTYPE;

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
  FUNCTION get_config_property(p_property_name IN XHB_CONFIG_PROP.PROPERTY_NAME%TYPE) 
    RETURN XHB_CONFIG_PROP.PROPERTY_VALUE%TYPE IS
    v_result XHB_CONFIG_PROP.PROPERTY_VALUE%TYPE;
    CURSOR C_cp IS
    SELECT xcp.PROPERTY_VALUE
      FROM XHB_CONFIG_PROP xcp
     WHERE xcp.PROPERTY_NAME = p_property_name;
  BEGIN
    OPEN C_cp;
    FETCH C_cp INTO v_result;
    CLOSE C_cp;
    RETURN v_result;
  END get_config_property;


  FUNCTION get_max_cases_to_delete RETURN NUMBER IS
  BEGIN
    RETURN NVL(TO_NUMBER(get_config_property(p_property_name => 'BATCH_CASES_ALLOWED_TO_DELETE')),0);
  END get_max_cases_to_delete;

  FUNCTION cases_for_deletion RETURN cases_for_deletion_tab PIPELINED IS
    v_row cases_for_deletion_rec;
    v_no_of_cases NUMBER := 0;
    v_no_of_cases_allowed NUMBER := get_max_cases_to_delete;
  BEGIN
    FOR rec IN (SELECT xc.case_id 
                  FROM xhb_case xc
                 WHERE xc.case_status = 'D'
                ORDER BY xc.creation_date) LOOP
        v_no_of_cases := v_no_of_cases + 1;
        EXIT WHEN v_no_of_cases > v_no_of_cases_allowed;
        PIPE ROW(rec);
    END LOOP;
    RETURN;
  END cases_for_deletion;


  PROCEDURE update_case_status(p_case_id     IN XHB_CASE.CASE_ID%TYPE,
                               p_case_status IN XHB_CASE.CASE_STATUS%TYPE,
                               p_commit      IN BOOLEAN DEFAULT TRUE) IS
  BEGIN
    UPDATE xhb_case xc
       SET xc.case_status = p_case_status
     WHERE xc.case_id = p_case_id;
  END update_case_status;


  FUNCTION get_next_hk_run_id RETURN XHB_HK_ERROR_LOG.HK_RUN_ID%TYPE IS 
     v_hk_run_id XHB_HK_ERROR_LOG.HK_RUN_ID%TYPE;
  BEGIN
     SELECT hk_run_id_seq.NEXTVAL INTO v_hk_run_id FROM DUAL;
     RETURN v_hk_run_id;
  END get_next_hk_run_id;
  
  FUNCTION get_next_hk3_run_id RETURN XHB_HK3_RESULTS.HK3_RUN_ID%TYPE IS 
     v_hk3_run_id XHB_HK3_RESULTS.HK3_RUN_ID%TYPE;
  BEGIN
     SELECT hk3_run_id_seq.NEXTVAL INTO v_hk3_run_id FROM DUAL;
     RETURN v_hk3_run_id;
  END get_next_hk3_run_id;


  PROCEDURE log_case_deletion_error(p_hk_run_id     IN XHB_HK_ERROR_LOG.HK_RUN_ID%TYPE,
                                     p_court_id      IN XHB_HK_ERROR_LOG.COURT_ID%TYPE,
                                     p_case_id       IN XHB_HK_ERROR_LOG.CASE_ID%TYPE,
                                     p_case_type     IN XHB_HK_ERROR_LOG.CASE_TYPE%TYPE,
                                     p_case_no       IN XHB_HK_ERROR_LOG.CASE_NO%TYPE,
                                     p_error_message IN XHB_HK_ERROR_LOG.ERROR_MESSAGE%TYPE) IS
     PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
     -- Log the case as failed deletion
     update_case_status(p_case_id     => p_case_id,
                        p_case_status => 'F');

     -- this will log an error for an individual case
     INSERT INTO xhb_hk_error_log  (hk_run_id,   case_no,   case_type,   court_id,   case_id,   error_message)
          VALUES (p_hk_run_id, p_case_no, p_case_type, p_court_id, p_case_id, p_error_message);
 
     COMMIT;
  END log_case_deletion_error; 
  
  PROCEDURE log_judge_hk_error(p_hk3_run_id     IN XHB_HK3_ERROR_LOG.HK3_RUN_ID%TYPE,
                               p_error_message 	IN XHB_HK_ERROR_LOG.ERROR_MESSAGE%TYPE) IS
     PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN

     INSERT INTO xhb_hk3_error_log  (hk3_run_id, error_message)
          VALUES (p_hk3_run_id, p_error_message);
 
     COMMIT;
  END log_judge_hk_error;
 

  PROCEDURE execute_deletion(p_sql IN VARCHAR2, p_param1 IN NUMBER, p_param2 IN NUMBER DEFAULT NULL) IS
     l_delete  VARCHAR2(4000);
     PROCEDURE run_statement IS
     BEGIN
        IF p_param2 IS NOT NULL THEN 
           EXECUTE IMMEDIATE l_delete USING p_param1, p_param2;
        ELSE 
           EXECUTE IMMEDIATE l_delete USING p_param1;
        END IF;   
     END run_statement;
  BEGIN
     -- Delete the main record
     l_delete := p_sql;
     run_statement;
    
     -- Delete the audit record
     l_delete := REPLACE (l_delete, 'XHB', 'AUD');   
     run_statement;
  END execute_deletion;

 
  PROCEDURE update_log (p_hk_results IN xhb_hk_results%ROWTYPE) IS

  BEGIN

    UPDATE xhb_hk_results
    SET    case_start_date    = NVL(p_hk_results.case_start_date, case_start_date)
    ,      case_end_date      = NVL(p_hk_results.case_end_date, case_end_date)
    ,      case_status        = NVL(p_hk_results.case_status, case_status)
    ,      case_error_message = NVL(p_hk_results.case_error_message, case_error_message)
    ,      cases_error        = NVL(p_hk_results.cases_error, cases_error)
    ,      cases_deleted      = NVL(p_hk_results.cases_deleted, cases_deleted)
    ,      list_start_date    = NVL(p_hk_results.list_start_date, list_start_date)
    ,      list_end_date      = NVL(p_hk_results.list_end_date, list_end_date)
    ,      list_status        = NVL(p_hk_results.list_status, list_status)
    ,      lists_deleted      = NVL(p_hk_results.lists_deleted, lists_deleted)
    ,      list_error_message = NVL(p_hk_results.list_error_message, list_error_message)
    ,      run_end_date       = NVL(p_hk_results.run_end_date, run_end_date)
    ,      error_message      = NVL(p_hk_results.error_message, error_message)
    WHERE  hk_run_id = l_hk_run_id;

    COMMIT;

  END;
  
  PROCEDURE update_hk3_log (p_hk3_results IN xhb_hk3_results%ROWTYPE) IS

  BEGIN

    UPDATE xhb_hk3_results
    SET    run_end_date      		 	= NVL(p_hk3_results.run_end_date, run_end_date)
    ,      judge_usage_status      		= NVL(p_hk3_results.judge_usage_status, judge_usage_status)
    ,      judge_usage_error_message	= NVL(p_hk3_results.judge_usage_error_message, judge_usage_error_message)
    ,      judge_usage_deleted 			= NVL(p_hk3_results.judge_usage_deleted, judge_usage_deleted)
    ,      judge_usage_error        	= NVL(p_hk3_results.judge_usage_error, judge_usage_error)
    ,      crtrm_usage_status      		= NVL(p_hk3_results.crtrm_usage_status, crtrm_usage_status)
    ,      crtrm_usage_error_message    = NVL(p_hk3_results.crtrm_usage_error_message, crtrm_usage_error_message)
    ,      crtrm_usage_recs_deleted     = NVL(p_hk3_results.crtrm_usage_recs_deleted, crtrm_usage_recs_deleted)
    ,      crtrm_usage_recs_error       = NVL(p_hk3_results.crtrm_usage_recs_error, crtrm_usage_recs_error)
    ,      ref_judge_status      		= NVL(p_hk3_results.ref_judge_status, ref_judge_status)
    ,      ref_judge_error_message 		= NVL(p_hk3_results.ref_judge_error_message, ref_judge_error_message)
    ,      ref_judges_deleted       	= NVL(p_hk3_results.ref_judges_deleted, ref_judges_deleted)
    ,      ref_judges_error      		= NVL(p_hk3_results.ref_judges_error, ref_judges_error)
    WHERE  hk3_run_id = l_hk3_run_id;

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
    l_delete := REPLACE (l_delete,'xhb_formatting','aud_formatting');
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

  PROCEDURE delete_hearing(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE) IS

    l_delete VARCHAR2(2000);

    PROCEDURE log_deletion(p_table_name IN VARCHAR2) IS
    BEGIN
      IF p_log_delete THEN 
         log_delete(p_table_name => p_table_name);
      END IF;
    END log_deletion;
  BEGIN

    l_error_point := 'DELETE HEARING ';

    -- 5
    l_delete := 'DELETE xhb_exporta
                 WHERE  hearing_id IN (SELECT hearing_id
                                       FROM   xhb_hearing
                                       WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_EXPORTA' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE HEARING 1 ';

    -- 4
    l_delete := '
    DELETE xhb_hearing_leg_rep
    WHERE  hearing_id IN (SELECT hearing_id
                          FROM   xhb_hearing
                          WHERE  case_id = :1
                         )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_HEARING_LEG_REP');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE HEARING 2 ';

    -- 3
    l_delete := '
    DELETE xhb_def_hearing_record
    WHERE  hearing_id IN (SELECT hearing_id
                          FROM   xhb_hearing
                          WHERE  case_id = :1
                         )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DEF_HEARING_RECORD');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SH_JUDGE');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SCHED_HEARING_ATTENDEE');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE HEARING 5 ';

    -- 2
    l_delete := '
    DELETE xhb_sh_justice
    WHERE  hearing_id IN (SELECT hearing_id
                          FROM   xhb_hearing
                          WHERE  case_id = :1
                         )';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SH_JUSTICE');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CR_LIVE_DISPLAY');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CR_LIVE_INTERNET' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CR_LIVE_STATUS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_COURT_LOG_ENTRY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SH_LEG_REP' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SCHED_HEARING_DEFENDANT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SH_JUDGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SCHED_HEARING_ATTENDEE');
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE HEARING 14 ';

    -- 1
    l_delete := '
    DELETE xhb_scheduled_hearing
    WHERE  hearing_id IN (SELECT hearing_id
                          FROM   xhb_hearing
                          WHERE  case_id = :1
                         )';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SCHEDULED_HEARING' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE HEARING 15 ';

    -- 0
    l_delete := '
    DELETE xhb_hearing
    WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_HEARING' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE HEARING 16 ';

  END delete_hearing;


--
----
--

  PROCEDURE delete_charge(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE) IS

    l_delete VARCHAR2(2000);

   PROCEDURE log_deletion(p_table_name IN VARCHAR2) IS
    BEGIN
      IF p_log_delete THEN 
         log_delete(p_table_name => p_table_name);
      END IF;
    END log_deletion;
  BEGIN

    l_error_point := 'DELETE CHARGE ';

    -- 4
    l_delete := 'DELETE xhb_defendant_charge
                 WHERE  charge_id IN (SELECT charge_id
                                      FROM   xhb_charge
                                      WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DEFENDANT_CHARGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CHARGE 1';

    -- 3
    l_delete := 'DELETE xhb_joinder_charge
                 WHERE  charge_id IN (SELECT charge_id
                                      FROM   xhb_charge
                                      WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_JOINDER_CHARGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CHARGE 2';

    -- 2
    l_delete := 'DELETE xhb_breach
                 WHERE  charge_id IN (SELECT charge_id
                                      FROM   xhb_charge
                                      WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_BREACH' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
	l_error_point := 'DELETE CHARGE 3';
    
	l_delete := 'DELETE xhb_verdict
                 WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
													FROM   xhb_defendant_on_offence
													WHERE  offence_id IN (	SELECT offence_id
																			FROM xhb_offence
																			WHERE  charge_id IN (SELECT charge_id
																								 FROM   xhb_charge
																								 WHERE  case_id = :1)
													)
                                      )';
	
	EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
	l_error_point := 'DELETE CHARGE 4';

	-- 1.5
    l_delete := 'DELETE xhb_defendant_on_offence
                 WHERE  offence_id IN (SELECT offence_id
									   FROM xhb_offence
									   WHERE  charge_id IN (SELECT charge_id
															FROM   xhb_charge
															WHERE  case_id = :1)
                                      )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DEFENDANT_ON_OFFENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CHARGE 5';
	
    -- 1
    l_delete := 'DELETE xhb_offence
                 WHERE  charge_id IN (SELECT charge_id
                                      FROM   xhb_charge
                                      WHERE  case_id = :1
                                      )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_OFFENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CHARGE 6';

    -- 0
    l_delete := 'DELETE xhb_charge
                 WHERE  case_id = :1
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CHARGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CHARGE 7';


  END delete_charge;

--
----
--

  PROCEDURE delete_defendant(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE) IS

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

   PROCEDURE log_deletion(p_table_name IN VARCHAR2) IS
    BEGIN
      IF p_log_delete THEN 
         log_delete(p_table_name => p_table_name);
      END IF;   
    END log_deletion;
  BEGIN

    l_error_point := 'DELETE DEFENDANT';

    l_error_point := 'DELETE DEFENDANT - XHB_BAIL_APPLICATION';
    l_delete := 'DELETE xhb_bail_application
                 WHERE  defendant_on_case_id in (select defendant_on_case_id
		 FROM   xhb_defendant_on_case where case_id = :1)';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_BAIL_APPLICATION' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;

    l_error_point := 'DELETE DEFENDANT - XHB_MIS_EVENT_LOG';
    l_delete := 'DELETE XHB_MIS_EVENT_LOG WHERE case_id = :1';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_MIS_EVENT_LOG' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;

    l_error_point := 'DELETE DEFENDANT - XHB_VERDICT';
    l_delete := 'DELETE xhb_verdict
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL_LINE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 2 ';

    -- 15
    l_delete := 'DELETE xhb_disposal2
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL2' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 3 ';

    -- 14
   l_delete := 'DELETE xhb_verdict
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 4 ';

    -- 13
    l_delete := 'DELETE xhb_court_log_entry
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_COURT_LOG_ENTRY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 5 ';

    -- 12
    l_delete := 'DELETE xhb_import_export_status
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_IMPORT_EXPORT_STATUS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 6 ';

    -- 11
    l_delete := 'DELETE xhb_order
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_ORDER' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 7 ';

    -- 9
    l_delete := 'DELETE xhb_psr_request
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_PSR_REQUEST' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 9 ';

    -- 8
    l_delete := 'DELETE xhb_direction_attend
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DIRECTION_ATTEND' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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


    EXECUTE IMMEDIATE l_delete USING p_case_id, p_case_id;
    log_deletion (p_table_name      => 'XHB_JOINDER_DEFENDANT_ON_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id,p_case_id;
    l_error_point := 'DELETE DEFENDANT 11 ';

    -- 6
    l_delete := 'DELETE xhb_def_on_case_ref_sol_firm
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DEF_ON_CASE_REF_SOL_FIRM' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 12 ';

    -- 5
    l_delete := 'DELETE xhb_court_log_entry
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_COURT_LOG_ENTRY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 13 ';

    -- 4
    l_delete := 'DELETE xhb_directions_for_defendant
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DIRECTIONS_FOR_DEFENDANT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 14 ';

    -- 3
    l_delete := 'DELETE xhb_import_export_status
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_IMPORT_EXPORT_STATUS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_PLEA' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 16 ';


    -- 2
    l_delete := 'DELETE xhb_defendant_charge
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DEFENDANT_CHARGE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL_LINE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 18 ';

    l_delete := 'UPDATE xhb_disposal2 x1 SET x1.PSD_DISPOSAL2_ID = NULL WHERE x1.PSD_DISPOSAL2_ID IN (
                 SELECT x2.DISPOSAL2_ID FROM xhb_disposal2 x2
                 WHERE  x2.defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence
                                                    WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                    FROM   xhb_defendant_on_case
                                                                                    WHERE  case_id = :1
                                                                                   )
                                                   ))';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 18a';

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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL2' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 19 ';

    -- 1.4
    l_delete := 'DELETE xhb_plea
                 WHERE  defendant_on_offence_id IN (SELECT xdof.defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence xdof
                                                    WHERE  xdof.defendant_on_case_id IN (SELECT xdoc.defendant_on_case_id
                                                                                    FROM   xhb_defendant_on_case xdoc
                                                                                    WHERE  xdoc.case_id = :1)
                                                   )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_PLEA' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 20 ';
	
	-- 1.3.5
    l_delete := 'DELETE xhb_plea
                 WHERE  defendant_on_offence_id IN (SELECT xdof.defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence xdof
                                                    WHERE  xdof.offence_id IN (SELECT xoff.offence_id
																			  FROM xhb_offence xoff, xhb_charge xchg
																			 WHERE xoff.charge_id = xchg.charge_id
																						   AND xchg. case_id = :1)
                                                   )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_PLEA' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 21 ';

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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_VERDICT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 22 ';

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

    EXECUTE IMMEDIATE l_delete USING p_case_id,p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL_REFERENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id,p_case_id;
    l_error_point := 'DELETE DEFENDANT 23 ';

    -- 1.2
    l_delete := 'DELETE xhb_disposal
                 WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence
                                                    WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                                                    FROM   xhb_defendant_on_case
                                                                                    WHERE  case_id = :1
                                                                                   )
                                                   )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 24';

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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_COURT_lOG_ENTRY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 25';

    -- 1
    l_delete := 'DELETE xhb_defendant_on_offence
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )
                ';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DEFENDANT_ON_OFFENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 26';

    -- 10
    l_delete := 'DELETE xhb_disposal
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 27';

    -- 17
    l_delete := 'DELETE xhb_leo_adv_link
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_LEO_ADV_LINK' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 28';

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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_LEO_ADV_LINK' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 29';

    l_error_point := 'DELETE DEFENDANT - XHB_LEGAL_AID_AMENDMENT';
    l_delete := 'DELETE XHB_LEGAL_AID_AMENDMENT 
            WHERE LEGAL_AID_ORDER_ID IN (SELECT LEGAL_AID_ORDER_ID 
                                        FROM XHB_LEGAL_AID_ORDER
			                            WHERE case_pros_agency_id IN(SELECT case_pros_agency_id 
                                                                     FROM xhb_case_prosecutor_agency
				                                                     WHERE  case_id = :1))';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_LEGAL_AID_AMENDMENT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
                                               
    l_error_point := 'DELETE DEFENDANT - XHB_LEGAL_AID_AMENDMENT(2)';
    l_delete := 'DELETE XHB_LEGAL_AID_AMENDMENT 
            WHERE LEGAL_AID_ORDER_ID IN (SELECT LEGAL_AID_ORDER_ID 
                                        FROM XHB_LEGAL_AID_ORDER
			                                  WHERE DEFENDANT_ON_CASE_ID IN(SELECT DEFENDANT_ON_CASE_ID 
                                                                        FROM xhb_defendant_on_case
				                                                               WHERE case_id = :1))';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_LEGAL_AID_AMENDMENT' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    
    l_error_point := 'DELETE DEFENDANT - XHB_LEGAL_AID_ORDER';
    l_delete := 'DELETE xhb_legal_aid_order
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_LEGAL_AID_ORDER' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;

    l_error_point := 'DELETE DEFENDANT - XHB_RECORDSHEET';
    l_delete := 'DELETE XHB_RECORDSHEET 
            WHERE DEFENDANT_ON_CASE_ID IN(SELECT DEFENDANT_ON_CASE_ID 
                                            FROM xhb_defendant_on_case
				                                   WHERE case_id = :1)';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_RECORDSHEET' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;

    l_error_point := 'DELETE DEFENDANT - XHB_HATE_SENTENCING';
    l_delete := 'DELETE xhb_hate_sentencing
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_HATE_SENTENCING' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 30';

    l_error_point := 'DELETE DEFENDANT - XHB_MONETARY_ORDER_TRACKING';
    l_delete := 'DELETE XHB_MONETARY_ORDER_TRACKING
                 WHERE  case_id  = :1';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_MONETARY_ORDER_TRACKING' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;

    l_error_point := 'DELETE DEFENDANT - XHB_BW_HISTORY';
    l_delete := 'DELETE XHB_BW_HISTORY
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_BW_HISTORY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;

    l_error_point := 'DELETE DEFENDANT - XHB_DEFENDANT_ON_CASE';
    l_delete := 'DELETE xhb_defendant_on_case
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DEFENDANT_ON_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    l_error_point := 'DELETE DEFENDANT 31';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 32';

    -- loop through all defendants on case which are not on any other cases
    FOR l_def IN c_defendant_on_case (p_case_id) LOOP

      -- 0.1.1
      l_delete := 'DELETE xhb_defendant_reference
                   WHERE  defendant_id = :1';

      EXECUTE IMMEDIATE l_delete USING l_def.defendant_id;
      log_deletion (p_table_name      => 'XHB_DEFENDANT_REFERENCE' );
      l_delete := REPLACE (l_delete,'xhb','aud');
      EXECUTE IMMEDIATE l_delete USING l_def.defendant_id;
      l_error_point := 'DELETE DEFENDANT 33';

      -- 0.1
      l_delete := 'DELETE xhb_defendant
                   WHERE  defendant_id = :1';

      EXECUTE IMMEDIATE l_delete USING l_def.defendant_id;
      log_deletion (p_table_name      => 'XHB_DEFENDANT' );
      l_delete := REPLACE (l_delete,'xhb','aud');
      EXECUTE IMMEDIATE l_delete USING l_def.defendant_id;
      l_error_point := 'DELETE DEFENDANT 34';

    END LOOP;

    l_error_point := 'DELETE DEFENDANT 35';

  END delete_defendant;

--
----
--
  PROCEDURE delete_listing(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE) IS
    l_proc_name VARCHAR2(14) := 'DELETE_LISTING';
    PROCEDURE delete_records(p_table_name IN VARCHAR2) IS 
    BEGIN
       l_error_point := l_proc_name || ' ' || p_table_name || ' ';
       execute_deletion(p_sql => 'DELETE '||Upper(p_table_name)||' WHERE case_id = :1 ', p_param1 => p_case_id);
	   IF p_log_delete THEN 
         log_delete(p_table_name => p_table_name);
       END IF; 
    END delete_records;
    PROCEDURE delete_records(p_table_name IN VARCHAR2,
                             p_sql        IN VARCHAR2,
                             p_param1     IN NUMBER DEFAULT NULL,
                             p_param2     IN NUMBER DEFAULT NULL) IS 
    BEGIN
       l_error_point := l_proc_name || ' ' || p_table_name || ' ';
       execute_deletion(p_sql => p_sql, p_param1 => p_param1, p_param2 => p_param2);
	   IF p_log_delete THEN 
         log_delete(p_table_name => p_table_name);
       END IF; 
    END delete_records;
  BEGIN
      delete_records(p_table_name => 'XHB_DIARY_NOTE_ENTRY',
                  p_sql         => 
      'DELETE XHB_DIARY_NOTE_ENTRY WHERE CASE_ID = :1 OR '||
                 'CASE_LISTING_ENTRY_ID IN (SELECT cle.CASE_LISTING_ENTRY_ID '||
                                           'FROM XHB_CASE_LISTING_ENTRY cle '||
                                           'WHERE cle.CASE_ID = :2)',
                  p_param1      => p_case_id,
                  p_param2      => p_case_id);
    delete_records(p_table_name => 'XHB_DEF_ON_CASE_ON_LIST');
    delete_records(p_table_name => 'XHB_CASE_ON_LIST',
                  p_sql         => 
      'UPDATE XHB_CASE_ON_LIST xcol1 SET xcol1.PARENT_CASE_ON_LIST_ID = NULL '||
                 'WHERE xcol1.PARENT_CASE_ON_LIST_ID IS NOT NULL'||
                 ' AND xcol1.PARENT_CASE_ON_LIST_ID IN'||
                 ' (SELECT xcol2.CASE_ON_LIST_ID '||
                     'FROM XHB_CASE_ON_LIST xcol2 '||
                     'WHERE xcol2.CASE_ID = :1)',
                  p_param1      => p_case_id);
    delete_records(p_table_name => 'XHB_CASE_ON_LIST');
    
    delete_records(p_table_name => 'XHB_FIXTURE_DEFT_ATTENDING',
                   p_sql        =>
      'DELETE XHB_FIXTURE_DEFT_ATTENDING '||
         'WHERE CASE_DIARY_FIXTURE_ID IN (SELECT cdf.CASE_DIARY_FIXTURE_ID '||
                                           'FROM XHB_CASE_LISTING_ENTRY cle,'||
                                                'XHB_CASE_DIARY_FIXTURE cdf '||
                                           'WHERE cle.CASE_ID = :1 '||
                                           'AND cle.CASE_LISTING_ENTRY_ID = cdf.CASE_LISTING_ENTRY_ID)',
                   p_param1     => p_case_id);
                   
    delete_records(p_table_name => 'XHB_CASE_DIARY_FIXTURE',
                   p_sql        =>
      'DELETE XHB_CASE_DIARY_FIXTURE '||
         'WHERE CASE_LISTING_ENTRY_ID IN (SELECT cle.CASE_LISTING_ENTRY_ID '||
                                           'FROM XHB_CASE_LISTING_ENTRY cle '||
                                           'WHERE cle.CASE_ID = :1)',
                   p_param1     => p_case_id);   
                   
    delete_records(p_table_name => 'XHB_CASE_LISTING_ENTRY');
    delete_records(p_table_name => 'XHB_CASE_NON_AVAIL_DAYS');
  END delete_listing;

--
----
--
  PROCEDURE delete_case(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE) IS

    l_delete VARCHAR2(4000);

   PROCEDURE log_deletion(p_table_name IN VARCHAR2) IS
    BEGIN
      IF p_log_delete THEN 
         log_delete(p_table_name => p_table_name);
      END IF;   
    END log_deletion;
  BEGIN

    l_error_point := 'DELETE CASE ';

    -- 1
    -- Has no audit table
    DELETE xhb_case_refresh_resynch
    WHERE  case_id = p_case_id;

    log_deletion (p_table_name      => 'XHB_CASE_REFRESH_RESYNCH' );
    l_error_point := 'DELETE CASE 1 ';

    -- 2
    l_delete := 'DELETE xhb_charge_differences
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CHARGE_DIFFERENCES' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 2 ';

    -- 3
    l_delete := 'DELETE xhb_case_reference
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CASE_REFERENCE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 3 ';

    -- 4.0
    l_delete := 'DELETE xhb_prosecutor_ref_sol_firm
			WHERE case_pros_agency_id IN(
						     SELECT case_pros_agency_id FROM xhb_case_prosecutor_agency
				                     WHERE  case_id = :1
				   		    )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_PROSECUTOR_REF_SOL_FIRM' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 4.0 ';

    -- 4.1
    l_delete := 'DELETE xhb_case_prosecutor_agency
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CASE_PROSECUTOR_AGENCY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 4.1 ';

    -- 5
    l_delete := 'DELETE xhb_progress_trigger
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_PROGRESS_TRIGGER' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_WITNESS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 6 ';

    -- 6.1
    l_delete := 'DELETE xhb_skeleton_session
                 WHERE  skeleton_id IN (SELECT skeleton_id
                                        FROM xhb_skeleton_schedule
                                        WHERE  case_id = :1
                                       )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SKELETON_SESSION' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 7 ';

    -- 6.2
    l_delete := 'DELETE xhb_skeleton_day
                 WHERE  skeleton_id IN (SELECT skeleton_id
                                        FROM xhb_skeleton_schedule
                                        WHERE  case_id = :1
                                       )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SKELETON_DAY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 8 ';


    -- 6
    l_delete := 'DELETE xhb_skeleton_schedule
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_SKELETON_SCHEDULE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 9 ';

    -- 7
    l_delete := 'DELETE xhb_time
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_TIME' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 10 ';

    -- 8
    l_delete := 'DELETE xhb_case_app_reason
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CASE_APP_REASON' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 11 ';

    -- 9
    l_delete := 'DELETE xhb_directions_for_case
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DIRECTIONS_FOR_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 12 ';

    -- 10
    l_delete := 'DELETE xhb_witness
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_WITNESS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 13 ';

    -- 11
    l_delete := 'DELETE xhb_indictment_history
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_INDICTMENT_HISTORY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 14 ';

    -- 12
    l_delete := 'DELETE xhb_rs_case
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_RS_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 15 ';

    -- 13
    l_delete := 'DELETE xhb_indictment_log
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_INDICTMENT_LOG' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 16 ';


    -- 14
    l_delete := 'DELETE xhb_charges_log
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CHARGES_LOG' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 17 ';

    -- 0
    l_delete := 'DELETE xhb_case
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
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

        delete_hearing(p_case_id => l_case_history.case_id);

        delete_listing(p_case_id => l_case_history.case_id);

        delete_defendant(p_case_id => l_case_history.case_id);

        delete_charge(p_case_id => l_case_history.case_id);

        delete_case(p_case_id => l_case_history.case_id);

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
  
  PROCEDURE write_hk3_error_log_file(p_hk3_run_id IN NUMBER) IS

    l_file utl_file.file_type;

    CURSOR c_xhb_hk3_error_log (b_hk3_run_id NUMBER) IS
      SELECT *
      FROM   xhb_hk3_error_log
      WHERE  hk3_run_id = b_hk3_run_id;

  BEGIN

    l_error_point := 'WRITE ERROR LOG FILE ';

    FOR l_errors IN c_xhb_hk3_error_log (p_hk3_run_id)  LOOP

      IF c_xhb_hk3_error_log%ROWCOUNT = 1 THEN
        l_file := utl_file.fopen('HK_LOGS','HK_JUDGE_'||LPAD(p_hk3_run_id,10,'0')||'_ERROR.log','a');
        utl_file.put_line(l_file,'ERROR_MESSAGE',TRUE);
      END IF;

      utl_file.put_line(l_file, l_errors.error_message, TRUE);

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

  END write_hk3_error_log_file;

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
  
  
	PROCEDURE write_hk3_metrics_log_file(p_hk3_run_id IN NUMBER) IS

		l_file utl_file.file_type;

		CURSOR c_xhb_hk3_results (b_hk3_run_id NUMBER) IS
		SELECT *
		FROM   xhb_hk3_results
		WHERE  hk3_run_id = b_hk3_run_id;

		l_rec_results c_xhb_hk3_results%ROWTYPE;

		l_status_desc VARCHAR2(100);
		l_run_type    VARCHAR2(100);

	BEGIN

		l_file := utl_file.fopen('HK_LOGS','HK_JUDGE_'||LPAD(p_hk3_run_id,10,'0')||'_METRICS.log','a');

		OPEN  c_xhb_hk3_results(p_hk3_run_id);
		FETCH c_xhb_hk3_results INTO l_rec_results;

		IF c_xhb_hk3_results%NOTFOUND THEN
			-- No results found
			utl_file.put_line(l_file,'No record of Judge HK run '||p_hk3_run_id||' can be found',TRUE);
		ELSE
			-- Header
			utl_file.put_line(l_file,'Log file produced on '||TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),TRUE);
			utl_file.new_line(l_file,1);
			utl_file.put_line(l_file,'Judge Housekeeping Run Id. '||p_hk3_run_id,TRUE);
			utl_file.put_line(l_file,'Started On   : '||TO_CHAR(l_rec_results.run_start_date,'DD-MON-YYYY HH24:MI:SS'),TRUE);
			utl_file.put_line(l_file,'Completed On : '||TO_CHAR(l_rec_results.run_end_date  ,'DD-MON-YYYY HH24:MI:SS'),TRUE);
			utl_file.new_line(l_file,1);

			-- Judge Usage Statistics
			utl_file.put_line(l_file,'Judge Usage Statistics',TRUE);
			IF l_rec_results.judge_usage_status = 'S' THEN
			l_status_desc := ' - Success';
			ELSIF l_rec_results.judge_usage_status = 'F' THEN
			l_status_desc := ' - Failure - see error message below';
			ELSIF l_rec_results.judge_usage_status = 'E' THEN
			l_status_desc := ' - Some records not deleted - see count below';
			ELSE
			l_status_desc := ' - Unknown status';
			END IF;

			utl_file.put_line(l_file,'Run Status : '||l_rec_results.judge_usage_status||l_status_desc,TRUE);
			IF l_rec_results.judge_usage_status = 'F' THEN
			utl_file.put_line(l_file,l_rec_results.judge_usage_error_message,TRUE);
			END IF;

			utl_file.put_line(l_file,'Records Deleted : '||l_rec_results.judge_usage_deleted,TRUE);
			utl_file.put_line(l_file,'Records Error   : '||l_rec_results.judge_usage_error,TRUE);
			utl_file.new_line(l_file,1);
			utl_file.put_line(l_file,'Age of Records Deleted (days) : '||l_rec_results.judge_usage_limit,TRUE);
			utl_file.new_line(l_file,1);
			
			-- Court Room Usage Statistics
			utl_file.put_line(l_file,'Court Room Usage Statistics',TRUE);
			IF l_rec_results.crtrm_usage_status = 'S' THEN
			l_status_desc := ' - Success';
			ELSIF l_rec_results.crtrm_usage_status = 'F' THEN
			l_status_desc := ' - Failure - see error message below';
			ELSIF l_rec_results.crtrm_usage_status = 'E' THEN
			l_status_desc := ' - Some records not deleted - see count below';
			ELSE
			l_status_desc := ' - Unknown status';
			END IF;

			utl_file.put_line(l_file,'Run Status : '||l_rec_results.crtrm_usage_status||l_status_desc,TRUE);
			IF l_rec_results.crtrm_usage_status = 'F' THEN
			utl_file.put_line(l_file,l_rec_results.crtrm_usage_error_message,TRUE);
			END IF;

			utl_file.put_line(l_file,'Records Deleted : '||l_rec_results.crtrm_usage_recs_deleted,TRUE);
			utl_file.put_line(l_file,'Records Error   : '||l_rec_results.crtrm_usage_recs_error,TRUE);
			utl_file.new_line(l_file,1);
			utl_file.put_line(l_file,'Age of Records Deleted (days) : '||l_rec_results.crtrm_usage_limit,TRUE);
			utl_file.new_line(l_file,1);
			
			-- Ref Judge Statistics
			utl_file.put_line(l_file,'Ref Judge Statistics',TRUE);
			IF l_rec_results.ref_judge_status = 'S' THEN
			l_status_desc := ' - Success';
			ELSIF l_rec_results.ref_judge_status = 'F' THEN
			l_status_desc := ' - Failure - see error message below';
			ELSIF l_rec_results.ref_judge_status = 'E' THEN
			l_status_desc := ' - Some records not deleted - see count below';
			ELSE
			l_status_desc := ' - Unknown status';
			END IF;

			utl_file.put_line(l_file,'Run Status : '||l_rec_results.ref_judge_status||l_status_desc,TRUE);
			IF l_rec_results.ref_judge_status = 'F' THEN
			utl_file.put_line(l_file,l_rec_results.ref_judge_error_message,TRUE);
			END IF;

			utl_file.put_line(l_file,'Records Deleted : '||l_rec_results.ref_judges_deleted,TRUE);
			utl_file.put_line(l_file,'Records Error   : '||l_rec_results.ref_judges_error,TRUE);
			utl_file.new_line(l_file,1);
			IF l_rec_results.ref_judge_limit > 0 THEN
				utl_file.put_line(l_file,'Ref Judge Limit : '||l_rec_results.ref_judge_limit,TRUE);
			ELSE
				utl_file.put_line(l_file,'No limit on records deleted',TRUE);
			END IF;
			utl_file.new_line(l_file,1);

			-- End of Report
			utl_file.new_line(l_file,1);
			utl_file.put_line(l_file,'End of Report',TRUE);

		END IF;

		CLOSE c_xhb_hk3_results;

		utl_file.fclose(l_file);

	EXCEPTION
		-- any exceptions for file handling will go here
		WHEN OTHERS THEN
			-- close cursor
			IF c_xhb_hk3_results%ISOPEN THEN
			CLOSE c_xhb_hk3_results;
			END IF;

			-- close file
			IF utl_file.is_open(l_file) THEN
			utl_file.fclose(l_file);
			END IF;

	END write_hk3_metrics_log_file;

--
----
--
  
  PROCEDURE end_delete_run IS
  
  BEGIN
  
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
  
  END end_delete_run;
  
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

    l_hk_run_id := get_next_hk_run_id();

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

	-- Write logs and end the deletion run
    end_delete_run();

  EXCEPTION
    WHEN OTHERS THEN

     -- Update log record with metrics
    l_hk_run_results := NULL;
    l_hk_run_results.run_end_date := SYSDATE;
    l_hk_run_results.error_message := SUBSTR(l_error_point ||SQLERRM,1,2000);

    update_log(l_hk_run_results);

  END initiate_run;

/** 
  * DESCRIPTION :  
  *   Procedures          Purpose
  *   =========           =======
  *   obsolete_case       Rather than delete a case, the case is made obsolete and all of the foreign key tables are updated accordingly to have their obs_ind value set to 'Y'
  *                       Where and exception is raised, call update_log with the result. 
  *  
  */
 PROCEDURE obsolete_case (p_case_id    IN xhb_case.case_id%TYPE
                         ,p_del_reason IN VARCHAR2)
  IS
  
  BEGIN

    l_error_point := 'OBSOLETE CASE';

    -- 1
    UPDATE xhb_charges_log
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_charges_log
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;               
    
    l_error_point := 'OBSOLETE CASE 1';
    
    -- 2
    UPDATE xhb_defendant_on_case
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_defendant_on_case
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id; 
    
    -- 3
    UPDATE xhb_case_prosecutor_agency
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_case_prosecutor_agency
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 3';
    
    -- 4
    UPDATE xhb_charge
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_charge
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 4';
    
    -- 5
    UPDATE xhb_offence
    SET obs_ind = 'Y'
    WHERE  charge_id IN (SELECT charge_id
                         FROM xhb_charge
                         WHERE case_id = p_case_id);
    
    UPDATE aud_offence
    SET obs_ind = 'Y'
    WHERE  charge_id IN (SELECT charge_id
                         FROM xhb_charge
                         WHERE case_id = p_case_id);
    
    l_error_point := 'OBSOLETE CASE 5';
    
    -- 6
    UPDATE xhb_defendant_on_offence
    SET obs_ind = 'Y'
    WHERE  offence_id IN (SELECT o.offence_id
                          FROM xhb_offence o
                          ,    xhb_charge c
                          WHERE o.charge_id = c.charge_id
                          AND c.case_id = p_case_id);

    
    UPDATE aud_defendant_on_offence
    SET obs_ind = 'Y'
    WHERE  offence_id IN (SELECT o.offence_id
                          FROM xhb_offence o
                          ,    xhb_charge c
                          WHERE o.charge_id = c.charge_id
                          AND c.case_id = p_case_id);
    
    l_error_point := 'OBSOLETE CASE 6';
    -- 7
    
    UPDATE xhb_disposal2
    SET obs_ind = 'Y'
    WHERE defendant_on_offence_id IN (SELECT xdof.defendant_on_offence_id
                                     FROM xhb_defendant_on_offence xdof
                                     ,    xhb_offence o
                                     ,    xhb_charge c
                                     WHERE xdof.offence_id = o.offence_id
                                     AND o.charge_id = c.charge_id
                                     AND c.case_id = p_case_id)
    OR defendant_on_case_id IN (SELECT defendant_on_case_id
                               FROM xhb_defendant_on_case d
                               WHERE d.case_id = p_case_id);                                     
    
    
    UPDATE aud_disposal2
    SET obs_ind = 'Y'
    WHERE defendant_on_offence_id IN (SELECT xdof.defendant_on_offence_id
                                     FROM xhb_defendant_on_offence xdof
                                     ,    xhb_offence o
                                     ,    xhb_charge c
                                     WHERE xdof.offence_id = o.offence_id
                                     AND o.charge_id = c.charge_id
                                     AND c.case_id = p_case_id)
    OR defendant_on_case_id IN (SELECT defendant_on_case_id
                               FROM xhb_defendant_on_case d
                               WHERE d.case_id = p_case_id);
    
    l_error_point := 'OBSOLETE CASE 7';

     -- 8
    
    UPDATE xhb_disposal_line
    SET obs_ind = 'Y'
    WHERE disposal2_id IN (SELECT disposal2_id
                           FROM xhb_disposal2 d
                           ,    xhb_defendant_on_offence xdof
                           ,    xhb_offence o
                           ,    xhb_charge c
                           WHERE d.defendant_on_offence_id = xdof.defendant_on_offence_id
                           AND xdof.offence_id = o.offence_id
                           AND o.charge_id = c.charge_id
                           AND c.case_id = p_case_id)
  OR disposal2_id IN (SELECT d.disposal2_id
                      FROM xhb_disposal2 d
                      ,    xhb_defendant_on_case xdoc
                      WHERE d.defendant_on_case_id = xdoc.defendant_on_case_id
                      AND xdoc.case_id = p_case_id);
    
    UPDATE aud_disposal_line
    SET obs_ind = 'Y'
    WHERE disposal2_id IN (SELECT disposal2_id
                           FROM xhb_disposal2 d
                           ,    xhb_defendant_on_offence xdof
                           ,    xhb_offence o
                           ,    xhb_charge c
                           WHERE d.defendant_on_offence_id = xdof.defendant_on_offence_id
                           AND xdof.offence_id = o.offence_id
                           AND o.charge_id = c.charge_id
                           AND c.case_id = p_case_id)
  OR disposal2_id IN (SELECT d.disposal2_id
                      FROM xhb_disposal2 d
                      ,    xhb_defendant_on_case xdoc
                      WHERE d.defendant_on_case_id = xdoc.defendant_on_case_id
                      AND xdoc.case_id = p_case_id);
    
    l_error_point := 'OBSOLETE CASE 8';

    -- 9
    UPDATE xhb_verdict
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_verdict
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 9';
    
    -- 10
    
    UPDATE xhb_plea
    SET obs_ind = 'Y'
    WHERE defendant_on_offence_id IN (SELECT xdoo.defendant_on_offence_id
                                     FROM xhb_defendant_on_offence xdoo
                                     ,    xhb_defendant_on_case xdoc
                                     WHERE xdoo.defendant_on_case_id = xdoc.defendant_on_case_id
                                     AND xdoc.case_id = p_case_id);
    
	UPDATE aud_plea
    SET obs_ind = 'Y'
    WHERE defendant_on_offence_id IN (SELECT xdoo.defendant_on_offence_id
                                     FROM xhb_defendant_on_offence xdoo
                                     ,    xhb_defendant_on_case xdoc
                                     WHERE xdoo.defendant_on_case_id = xdoc.defendant_on_case_id
                                     AND xdoc.case_id = p_case_id);
									 
	l_error_point := 'OBSOLETE CASE 9.5';
							  
	UPDATE xhb_plea
    SET obs_ind = 'Y'
    WHERE defendant_charge_id IN (SELECT xdc.defendant_charge_id
								  FROM xhb_defendant_charge xdc
								  ,    xhb_defendant_on_case xdoc
								  WHERE xdc.defendant_on_case_id = xdoc.defendant_on_case_id
								  AND xdoc.case_id = p_case_id);
    
	UPDATE aud_plea
    SET obs_ind = 'Y'
    WHERE defendant_charge_id IN (SELECT xdc.defendant_charge_id
								  FROM xhb_defendant_charge xdc
								  ,    xhb_defendant_on_case xdoc
								  WHERE xdc.defendant_on_case_id = xdoc.defendant_on_case_id
								  AND xdoc.case_id = p_case_id);
    
    l_error_point := 'OBSOLETE CASE 10';
    
    UPDATE xhb_defendant_charge
    SET obs_ind = 'Y'
    WHERE defendant_on_case_id IN (SELECT xdoc.defendant_on_case_id
                                  FROM xhb_defendant_on_case xdoc
                                  WHERE xdoc.case_id = p_case_id);
    
    l_error_point := 'OBSOLETE CASE 10.1';
    
    -- 11
    UPDATE xhb_case_app_reason
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_case_app_reason
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 11';
    
    -- 12
    
    UPDATE xhb_case_listing_entry
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_case_listing_entry
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 12';
    
    -- 13
    
    UPDATE xhb_case_non_avail_days
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_case_non_avail_days
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 13';
    
    -- 14
    
    UPDATE xhb_case_on_list
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_case_on_list
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 14';
    
    -- 15
    UPDATE xhb_def_on_case_on_list
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_def_on_case_on_list
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 15';
    
     -- 16
    UPDATE xhb_diary_note_entry
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_diary_note_entry
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
     
    l_error_point := 'OBSOLETE CASE 16';
    
    -- 17
    UPDATE xhb_indictment_log
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_indictment_log
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    l_error_point := 'OBSOLETE CASE 17';
    
    -- 18
    UPDATE xhb_monetary_order_tracking
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_monetary_order_tracking
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
   
    l_error_point := 'OBSOLETE CASE 18';
    
     -- 19
    UPDATE xhb_nle_managed_cases
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
    
    UPDATE aud_nle_managed_cases
    SET obs_ind = 'Y'
    WHERE  case_id = p_case_id;
     
    l_error_point := 'OBSOLETE CASE 19';
    
    
    EXCEPTION
    WHEN OTHERS THEN
    
     l_hk_run_results.error_message := SUBSTR(l_error_point ||SQLERRM,1,2000);
     
     update_log(l_hk_run_results);
    
  END obsolete_case;
   
/** 
  * DESCRIPTION :  
  *   Procedures            Purpose
  *   =========             =======
  *   insert_case_history   After the record has been made obsolete, create a record in the xhb_case_history_table
  *                         This procedure can be called outside of the obsolete_case so has been placed outside of the obsolete_case procedure.
  *  
  */
 PROCEDURE insert_case_history (p_case_id    IN xhb_case.case_id%TYPE
                               ,p_change_reason IN VARCHAR2)
 IS
 
  CURSOR case_c IS
  SELECT xc.*
 , xrc.crest_code
 FROM xhb_case xc
 ,    xhb_ref_court xrc
 WHERE 1=1
 AND xc.case_id = p_case_id
 AND   xc.ref_court_id = xrc.ref_court_id (+)
 ;

 BEGIN
 
  FOR case_r IN case_c
   LOOP
    INSERT INTO xhb_case_history (case_history_id
                                 ,case_type
                                 ,court_id
                                 ,case_number
                                 ,psd_ct_code
                                 ,committal_date
                                 ,reason_deleted
                                 ,case_title
                                 ,date_archived
                                 ,sent_for_trial_date
                                 ,last_update_date
                                 ,creation_date
                                 ,last_updated_by
                                 ,created_by
                                 ,version
                                 )
    VALUES(xhb_case_history_seq.nextval
           ,case_r.case_type
           ,case_r.court_id
           ,case_r.case_number
           ,case_r.crest_code
           ,case_r.committal_date
           ,p_change_reason
           ,case_r.case_title
           ,sysdate
           ,case_r.sent_for_trial_date
           ,sysdate
           ,sysdate
           ,'XHIBIT'
           ,'XHIBIT'
           ,1
           );
   END LOOP;
   
 
 END insert_case_history;

    
 /************************************************************************************************
 *    PROCEDURE process_cases_ctx(p_hk_run_id, p_case_id)      CTX-2227
 *    Req [X.CM.071]   Section 5.5.23 of Case Maintenance FS
 *
 *    Similar to delete_case  but includes ctx-specific tables that are dependent on XHB_CASE, 
 *    and has external interface.
 *    Tables: XHB_CASE, XHB_CHARGES_LOG, XHB_DEFENDANT_ON_CASE, XHB_CASE_PROSECUTOR_AGENCY, 
 *            XHB_CHARGE, XHB_OFFENCE, XHB_DEFENDANT_ON_OFFENCE, XHB_DISPOSAL2, XHB_DISPOSAL_LINE, 
 *            XHB_VERDICT, XHB_PLEA
 *    Records in tables with foreign keys dependent on the above tables will also have to be deleted.
 *
 **************************************************************************************************/
   PROCEDURE process_cases_ctx  IS

    l_delete         VARCHAR2(4000);     -- Holds SQL string for delete statement
    l_proc_name      VARCHAR2(30)  := 'PROCESS_CASES_CTX';   -- Procedure name - used for error reporting
    v_case_no        XHB_CASE.case_number%type := 0;
    v_case_type      XHB_CASE.case_type%type   := ''; 
    v_court_id       XHB_CASE.court_id%type    := 0;
    v_case_title     XHB_CASE.case_title%type  := '';
    v_error_message  VARCHAR2(2000);
	l_cases_deleted	 NUMBER := 0;
	l_cases_error	 NUMBER := 0;

  BEGIN
  
	l_error_point := l_proc_name;
  
	-- Update log record with case metrics
    l_hk_run_results := NULL;
    l_hk_run_results.case_start_date := SYSDATE;
    l_hk_run_results.case_status := 'I';
    update_log(l_hk_run_results);
	
    -- Loop through the cases for deletion 
    FOR rec IN (SELECT x.case_id FROM TABLE(cases_for_deletion) x) LOOP
	
		BEGIN
			-- Indicate have started delete
			update_case_status(	p_case_id     => rec.case_id,
								p_case_status => 'I');
			COMMIT;
			
			SELECT cas.case_number, cas.case_type, cas.court_id, cas.case_title
			INTO   v_case_no, v_case_type, v_court_id, v_case_title 
			FROM   XHB_CASE cas
			WHERE  cas.case_id = rec.case_id;

			-- Has no audit table
			DELETE xhb_case_refresh_resynch
			WHERE  case_id = rec.case_id;

			-- Delete the case from the various areas of the system
			delete_hearing(p_case_id   => rec.case_id);
			delete_listing(p_case_id   => rec.case_id);
			delete_defendant(p_case_id => rec.case_id);
			delete_charge(p_case_id    => rec.case_id);
			delete_case(p_case_id      => rec.case_id);

			COMMIT;

			l_cases_deleted := l_cases_deleted + 1;
			
		EXCEPTION
			WHEN OTHERS THEN
				ROLLBACK;
				l_cases_error := l_cases_error + 1;
				
				log_case_deletion_error(p_hk_run_id     => l_hk_run_id,
                                  p_court_id      => v_court_id,
                                  p_case_id       => rec.case_id,
                                  p_case_type     => v_case_type,
                                  p_case_no       => v_case_no,
                                  p_error_message => SUBSTR(l_error_point || ' ' || SQLERRM, 1, 2000));
		END;
					
    END LOOP;
	
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
 
  END process_cases_ctx;


  PROCEDURE delete_sh_justice (p_justice_id IN XHB_SH_JUSTICE.SH_JUSTICE_ID%TYPE)  IS
  BEGIN
  DELETE XHB_SCHED_HEARING_ATTENDEE xsha
  WHERE xsha.SH_JUSTICE_ID = p_justice_id;
  
  DELETE XHB_SH_JUSTICE xshj
  WHERE xshj.SH_JUSTICE_ID = p_justice_id;
  
  EXCEPTION
  WHEN OTHERS THEN
  ROLLBACK;
  
  END delete_sh_justice;

 
  PROCEDURE delete_case_ctx (p_success_log  IN BOOLEAN DEFAULT FALSE) IS
    v_hk_run_id XHB_HK_ERROR_LOG.HK_RUN_ID%TYPE;
	
  BEGIN
    -- Get the next run id
    l_hk_run_id := get_next_hk_run_id();
	
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
    ,'C'
    ,SYSDATE
    ,0
    ,0
    ,0
    ,0
    ,0
    ,0
    ,0
    ,0);

    COMMIT;
	
	l_success_log := p_success_log;
	
	process_cases_ctx();
	
	-- Write logs and end the deletion run
    end_delete_run();
	
  END delete_case_ctx;
  
  PROCEDURE delete_judge_usage (p_age IN NUMBER DEFAULT 2500) IS
    
	CURSOR c_judge_usage IS
	SELECT judge_usage_id 
	FROM xhb_judge_usage
	WHERE TRUNC(sitting_date) <= TRUNC(SYSDATE-p_age);
	
	v_total_deleted	NUMBER := 0;
	v_total_error	NUMBER := 0;
	
  BEGIN
  
    l_hk3_run_results := NULL;
    l_hk3_run_results.judge_usage_status := 'I';
    update_hk3_log(l_hk3_run_results);
	
	FOR rec IN c_judge_usage LOOP
	
		BEGIN
		
			DELETE FROM xhb_judge_usage WHERE judge_usage_id = rec.judge_usage_id;
			DELETE FROM aud_judge_usage WHERE judge_usage_id = rec.judge_usage_id;
			COMMIT;
			
			v_total_deleted := v_total_deleted + 1;
		
		EXCEPTION
			WHEN OTHERS THEN
				ROLLBACK;
				v_total_error := v_total_error + 1;
				log_judge_hk_error(p_hk3_run_id   => l_hk3_run_id,
                                  p_error_message => SUBSTR('delete_judge_usage ' || SQLERRM, 1, 500));
		END;
	
	END LOOP;
	
	l_hk3_run_results := NULL;

    IF v_total_error > 0 THEN
      l_hk3_run_results.judge_usage_status := 'E';
    ELSE
      l_hk3_run_results.judge_usage_status := 'S';
    END IF;
    l_hk3_run_results.judge_usage_deleted := v_total_deleted;
    l_hk3_run_results.judge_usage_error   := v_total_error;
    update_hk3_log(l_hk3_run_results);
	
  EXCEPTION
	WHEN OTHERS THEN
		l_hk3_run_results := NULL;
		l_hk3_run_results.judge_usage_status   := 'F';
		l_hk3_run_results.judge_usage_error_message := SUBSTR('delete_judge_usage ' || SQLERRM, 1, 2000);
		l_hk3_run_results.judge_usage_deleted := v_total_deleted;
		l_hk3_run_results.judge_usage_error   := v_total_error;
		update_hk3_log(l_hk3_run_results);
	
  END delete_judge_usage;
  
  PROCEDURE delete_courtroom_usage (p_age IN NUMBER DEFAULT 2500) IS
    
	CURSOR c_courtroom_usage IS
	SELECT court_room_usage_id 
	FROM xhb_court_room_usage
	WHERE TRUNC(sitting_date) <= TRUNC(SYSDATE-p_age);
	
	v_total_error	NUMBER := 0;
	v_total_deleted	NUMBER := 0;
	
  BEGIN
  
    l_hk3_run_results := NULL;
    l_hk3_run_results.crtrm_usage_status := 'I';
    update_hk3_log(l_hk3_run_results);
	
	FOR rec IN c_courtroom_usage LOOP
	
		BEGIN
		
			DELETE FROM xhb_court_room_usage WHERE court_room_usage_id = rec.court_room_usage_id;
			DELETE FROM aud_court_room_usage WHERE court_room_usage_id = rec.court_room_usage_id;
			COMMIT;
			
			v_total_deleted := v_total_deleted + 1;
		
		EXCEPTION
			WHEN OTHERS THEN
				ROLLBACK;
				v_total_error := v_total_error + 1;
				log_judge_hk_error(p_hk3_run_id   => l_hk3_run_id,
                                  p_error_message => SUBSTR('delete_courtroom_usage ' || SQLERRM, 1, 500));
		END;
	
	END LOOP;
	
	l_hk3_run_results := NULL;

    IF v_total_error > 0 THEN
      l_hk3_run_results.crtrm_usage_status := 'E';
    ELSE
      l_hk3_run_results.crtrm_usage_status := 'S';
    END IF;
    l_hk3_run_results.crtrm_usage_recs_deleted := v_total_deleted;
    l_hk3_run_results.crtrm_usage_recs_error   := v_total_error;
    update_hk3_log(l_hk3_run_results);
	
  EXCEPTION
	WHEN OTHERS THEN
		l_hk3_run_results := NULL;
		l_hk3_run_results.crtrm_usage_status   := 'F';
		l_hk3_run_results.crtrm_usage_error_message := SUBSTR('delete_courtroom_usage ' || SQLERRM, 1, 2000);
		l_hk3_run_results.crtrm_usage_recs_deleted := v_total_deleted;
		l_hk3_run_results.crtrm_usage_recs_error   := v_total_error;
		update_hk3_log(l_hk3_run_results);
	
  END delete_courtroom_usage;
  
  PROCEDURE delete_obsolete_judges (p_judge_limit IN NUMBER DEFAULT 0) IS
    
	CURSOR c_get_ref_judges IS
	SELECT xrj.ref_judge_id
	FROM xhb_ref_judge xrj
	WHERE NVL(xrj.obs_ind,'N') = 'Y'
	AND TRUNC(xrj.last_update_date) < ADD_MONTHS(SYSDATE,-84);
	
	v_total_error	NUMBER := 0;
	v_total_deleted	NUMBER := 0;
	
  BEGIN
  
    l_hk3_run_results := NULL;
    l_hk3_run_results.ref_judge_status := 'I';
    update_hk3_log(l_hk3_run_results);
	
	FOR rec IN c_get_ref_judges LOOP
	
		IF p_judge_limit > 0 THEN
          -- exit out of cursor loop if limit reached
          IF v_total_deleted + v_total_error >= p_judge_limit THEN
            EXIT;
          END IF;
        END IF;
	
		BEGIN
		
			DELETE FROM xhb_ref_judge WHERE ref_judge_id = rec.ref_judge_id;
			DELETE FROM aud_ref_judge WHERE ref_judge_id = rec.ref_judge_id;
			COMMIT;
			
			v_total_deleted := v_total_deleted + 1;
		
		EXCEPTION
			WHEN OTHERS THEN
				ROLLBACK;
				v_total_error := v_total_error + 1;
				log_judge_hk_error(p_hk3_run_id   => l_hk3_run_id,
                                  p_error_message => SUBSTR('delete_obsolete_judges ' || SQLERRM, 1, 500));
		END;
	
	END LOOP;
	
	l_hk3_run_results := NULL;

    IF v_total_error > 0 THEN
      l_hk3_run_results.ref_judge_status := 'E';
    ELSE
      l_hk3_run_results.ref_judge_status := 'S';
    END IF;
    l_hk3_run_results.ref_judges_deleted := v_total_deleted;
    l_hk3_run_results.ref_judges_error   := v_total_error;
    update_hk3_log(l_hk3_run_results);
	
  EXCEPTION
	WHEN OTHERS THEN
		l_hk3_run_results := NULL;
		l_hk3_run_results.ref_judge_status   := 'F';
		l_hk3_run_results.ref_judge_error_message := SUBSTR('delete_obsolete_judges ' || SQLERRM, 1, 2000);
		l_hk3_run_results.ref_judges_deleted := v_total_deleted;
		l_hk3_run_results.ref_judges_error   := v_total_error;
		update_hk3_log(l_hk3_run_results);
	
  END delete_obsolete_judges;
  
  PROCEDURE obsolete_unused_judges IS
  
	CURSOR c_unused_judges IS
	SELECT xrj.ref_judge_id
	FROM xhb_ref_judge xrj
	WHERE NVL(xrj.obs_ind,'N') <> 'Y'
	AND NOT EXISTS (SELECT NULL FROM xhb_case_listing_entry xcle WHERE xcle.judge_id = xrj.ref_judge_id)
	AND NOT EXISTS (SELECT NULL FROM xhb_joinder xj WHERE xj.ref_judge_id = xrj.ref_judge_id)
	AND NOT EXISTS (SELECT NULL FROM xhb_judge_usage xju WHERE xju.ref_judge_id = xrj.ref_judge_id)
	AND NOT EXISTS (SELECT NULL FROM xhb_ref_judge_ticket xrjt WHERE xrjt.judge_id = xrj.ref_judge_id)
	AND NOT EXISTS (SELECT NULL FROM xhb_sched_hearing_attendee xsha WHERE xsha.ref_judge_id = xrj.ref_judge_id)
	AND NOT EXISTS (SELECT NULL FROM xhb_sh_judge xsj WHERE xsj.ref_judge_id = xrj.ref_judge_id)
	AND NOT EXISTS (SELECT NULL FROM xhb_sitting xs WHERE xs.ref_judge_id = xrj.ref_judge_id)
	AND NOT EXISTS (SELECT NULL FROM xhb_sitting_on_list xsol WHERE xsol.judge_ref_id = xrj.ref_judge_id);
	
	TYPE t_ref_judge_id IS TABLE OF xhb_ref_judge.ref_judge_id%TYPE;
	v_ref_judge_ids	t_ref_judge_id;
	
	v_total_updated	NUMBER := 0;
  
  BEGIN
  
	OPEN c_unused_judges;
    LOOP
		FETCH c_unused_judges BULK COLLECT INTO v_ref_judge_ids LIMIT 1000;
		
		-- Increment the count
		v_total_updated := v_total_updated + v_ref_judge_ids.COUNT;
		
		-- Perform the update
		FORALL i IN 1..v_ref_judge_ids.COUNT
			UPDATE xhb_ref_judge SET obs_ind = 'Y' WHERE ref_judge_id = v_ref_judge_ids(i);

		COMMIT;
         
		EXIT WHEN v_ref_judge_ids.COUNT = 0;
		
		-- Reinitialise collections
		IF v_ref_judge_ids IS NOT NULL THEN
			v_ref_judge_ids.DELETE;
		END IF;
	
	END LOOP;
	CLOSE c_unused_judges;
  
  EXCEPTION
	WHEN OTHERS THEN
		ROLLBACK;
		log_judge_hk_error(p_hk3_run_id   => l_hk3_run_id,
                           p_error_message => SUBSTR('obsolete_unused_judges ' || SQLERRM, 1, 500));
	
  END obsolete_unused_judges;
  
  PROCEDURE process_judges (p_age IN NUMBER DEFAULT 2500,
							p_judge_limit IN NUMBER DEFAULT 0) IS
  
  BEGIN
  
	-- Get the next run id
    l_hk3_run_id := get_next_hk3_run_id();
	
	INSERT INTO xhb_hk3_results
    (hk3_run_id
    ,run_type
    ,run_start_date
    ,judge_usage_deleted
	,judge_usage_error
    ,crtrm_usage_recs_deleted
	,crtrm_usage_recs_error
    ,ref_judges_deleted
    ,ref_judges_error
    ,judge_usage_limit
    ,crtrm_usage_limit
    ,ref_judge_limit)
    VALUES
    (l_hk3_run_id
    ,'J'
    ,SYSDATE
    ,0
    ,0
    ,0
    ,0
    ,0
    ,0
	,p_age
    ,p_age
    ,p_judge_limit);

    COMMIT;
	
	-- Run the judge updates
	delete_judge_usage( p_age => p_age );
	delete_courtroom_usage( p_age => p_age );
	delete_obsolete_judges( p_judge_limit => p_judge_limit );
	obsolete_unused_judges();
	
	-- write errors to external file
    write_hk3_error_log_file(l_hk3_run_id);

     -- Update log record with metrics
    l_hk3_run_results := NULL;
    l_hk3_run_results.run_end_date := SYSDATE;
    update_hk3_log(l_hk3_run_results);

    -- write metrics to external file
    write_hk3_metrics_log_file(l_hk3_run_id);
	
  END process_judges;

END xhb_housekeeping_pkg;
/