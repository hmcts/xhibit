create or replace PACKAGE BODY        xhb_housekeeping_pkg AS

/*********************************************************************************
* XHB_HOUSEKEEPING_PKG_B.SQL
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
*
* 2.0      29/08/2019 S Sethuraman   Enhanced XHIBIT HOUSEKEEPING Changes
*                                    - CTX-4490 - Find all cases eligible for Housekeeping.
*                                    Procedure find_all_cases_eligible_for_hk created.
*
*          03/09/2019 S Sethuraman  CTX-4491 - Procedure find_all_hk_cases_for_deletion  created
*
*          09/09/2019 S Sethuraman  CTX-4494 - Procedure write_del_record_to_history created
*          11/09/2019 S Sethuraman  CTX-4495 - Rollback transaction of above 4494 task if error at any stage
*          17/09/2019 S Sethuraman  CTX-4498 : Delete records from XHB_HEARING, XHB_SITTING
*          17/09/2019 S Sethuraman  CTX-4498 : Delete records from XHB_HEARING, XHB_SITTING
*          18/09/2019 S Sethuraman  CTX-4499 : Delete XHB_LIST and XHB_SITTING_ON_LIST
*          19/09/2019 S Sethuraman  CTX-4500 : Delete XHB_COURTEL_LIST
*          30/09/2019 S Sethuraman  CTX-4501 : Housekeep running_list
************************************************************************************/
  
  START_GREATER_END EXCEPTION;
  PRAGMA EXCEPTION_INIT(START_GREATER_END, -20105);
  
  DAYS_LIMIT_EXCEEDED EXCEPTION;
  PRAGMA EXCEPTION_INIT(DAYS_LIMIT_EXCEEDED, -20110);
	
  l_error_point    VARCHAR2(100);
  l_hk_run_id      xhb_hk_results.hk_run_id%TYPE;
  l_cases_deleted  xhb_hk_results.cases_deleted%TYPE;
  l_lists_deleted  xhb_hk_results.lists_deleted%TYPE;
  l_cases_error    xhb_hk_results.cases_error%TYPE;
  l_success_log    BOOLEAN;
  l_hk_run_results xhb_hk_results%ROWTYPE;

  l_hk3_run_id		xhb_hk3_results.hk3_run_id%TYPE;
  l_hk3_run_results xhb_hk3_results%ROWTYPE;

  l_hk_cpp_run_id      xhb_hk_cpp_results.hk_cpp_run_id%TYPE;
  l_hk_cpp_run_results xhb_hk_cpp_results%ROWTYPE;

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
                               p_case_status IN XHB_CASE.CASE_STATUS%TYPE) IS
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


  FUNCTION get_next_hk_cpp_run_id RETURN XHB_HK_CPP_RESULTS.HK_CPP_RUN_ID%TYPE IS
     v_hk_cpp_run_id XHB_HK_CPP_RESULTS.HK_CPP_RUN_ID%TYPE;
  BEGIN
     SELECT hk_cpp_run_id_seq.NEXTVAL INTO v_hk_cpp_run_id FROM DUAL;
     RETURN v_hk_cpp_run_id;
  END get_next_hk_cpp_run_id;
  

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

  PROCEDURE log_no_history_hk_error(p_hk_run_id     IN XHB_HK_ERROR_LOG.HK_RUN_ID%TYPE,
                                     p_court_id      IN XHB_HK_ERROR_LOG.COURT_ID%TYPE,
                                     p_case_id       IN XHB_HK_ERROR_LOG.CASE_ID%TYPE,
                                     p_case_type     IN XHB_HK_ERROR_LOG.CASE_TYPE%TYPE,
                                     p_case_no       IN XHB_HK_ERROR_LOG.CASE_NO%TYPE) IS
     PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN

     -- Remove case from MTBL_CASE_HISTORY
     DELETE FROM mtbl_case_history  mtb
	 WHERE  mtb.case_no   = p_case_no
	 AND    mtb.case_type = p_case_type
	 AND    mtb.court_id  = p_court_id;

     -- Log the case as failed deletion
     update_case_status(p_case_id     => p_case_id,
                        p_case_status => NULL);

     -- this will log an error for an individual case
     INSERT INTO xhb_hk_error_log  (hk_run_id,   case_no,   case_type,   court_id,   case_id,   error_message)
          VALUES (p_hk_run_id, p_case_no, p_case_type, p_court_id, p_case_id, 'Case cannot be deleted - no data in XHB_CASE_HISTORY');

     COMMIT;
  END log_no_history_hk_error;


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

  PROCEDURE insert_hk_cpp_log IS
  BEGIN
    l_hk_cpp_run_id := get_next_hk_cpp_run_id();         
    l_hk_cpp_run_results := NULL;            
    
    INSERT INTO xhb_hk_cpp_results
    (hk_cpp_run_id, run_start_date)
    VALUES
    (l_hk_cpp_run_id, SYSDATE);
    COMMIT;
  END insert_hk_cpp_log;

  PROCEDURE update_hk_cpp_log(p_hk_cpp_results IN xhb_hk_cpp_results%ROWTYPE) IS
  BEGIN
    UPDATE xhb_hk_cpp_results hk SET    
    hk.run_end_date       = NVL(p_hk_cpp_results.run_end_date, hk.run_end_date),
    hk.LIST_STATUS        = NVL(p_hk_cpp_results.LIST_STATUS, hk.LIST_STATUS),
    hk.LIST_ERROR_MESSAGE = NVL(p_hk_cpp_results.LIST_ERROR_MESSAGE, hk.LIST_ERROR_MESSAGE),
    hk.LIST_ERROR			    = NVL(p_hk_cpp_results.LIST_ERROR, hk.LIST_ERROR),
    hk.LIST_DELETED		    = NVL(p_hk_cpp_results.LIST_DELETED, hk.LIST_DELETED),
    hk.IWP_STATUS			    = NVL(p_hk_cpp_results.IWP_STATUS, hk.IWP_STATUS),
    hk.IWP_ERROR_MESSAGE  = NVL(p_hk_cpp_results.IWP_ERROR_MESSAGE, hk.IWP_ERROR_MESSAGE),
    hk.IWP_ERROR				  = NVL(p_hk_cpp_results.IWP_ERROR, hk.IWP_ERROR),
    hk.IWP_DELETED			  = NVL(p_hk_cpp_results.IWP_DELETED, hk.IWP_DELETED),
    hk.PD_STATUS				  = NVL(p_hk_cpp_results.PD_STATUS, hk.PD_STATUS),
    hk.PD_ERROR_MESSAGE   = NVL(p_hk_cpp_results.PD_ERROR_MESSAGE, hk.PD_ERROR_MESSAGE),
    hk.PD_ERROR				    = NVL(p_hk_cpp_results.PD_ERROR, hk.PD_ERROR),
    hk.PD_DELETED			    = NVL(p_hk_cpp_results.PD_DELETED, hk.PD_DELETED),
	hk.STAGING_STATUS				  = NVL(p_hk_cpp_results.STAGING_STATUS, hk.STAGING_STATUS),
    hk.STAGING_ERROR_MESSAGE   = NVL(p_hk_cpp_results.STAGING_ERROR_MESSAGE, hk.STAGING_ERROR_MESSAGE),
    hk.STAGING_ERROR				    = NVL(p_hk_cpp_results.STAGING_ERROR, hk.STAGING_ERROR),
    hk.STAGING_DELETED			    = NVL(p_hk_cpp_results.STAGING_DELETED, hk.STAGING_DELETED)
    WHERE  hk_cpp_run_id = l_hk_cpp_run_id;
    COMMIT;
  END update_hk_cpp_log;

--
----
--


  PROCEDURE log_cpp_hk_error(p_hk_cpp_run_id IN XHB_HK_CPP_ERROR_LOG.HK_CPP_RUN_ID%TYPE,
                             p_error_message IN XHB_HK_CPP_ERROR_LOG.ERROR_MESSAGE%TYPE) IS
     PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
     INSERT INTO xhb_hk_cpp_error_log  
     (hk_cpp_run_id, error_message)
     VALUES 
     (p_hk_cpp_run_id, p_error_message);
     COMMIT;
  END log_cpp_hk_error;
  

  PROCEDURE process_cpp_listing(p_MS_days IN NUMBER DEFAULT 7,
                                p_MF_days IN NUMBER DEFAULT 30) IS

    PROCEDURE delete_record(p_table_name IN VARCHAR2, p_where_clause IN VARCHAR2) IS
    BEGIN
       l_error_point := 'process_cpp_listing' || ' ' || p_table_name || ' ';
       EXECUTE IMMEDIATE 'DELETE '||Upper(p_table_name)||' WHERE '||p_where_clause;
    END delete_record;

    PROCEDURE delete_cpplist(p_no_of_days IN NUMBER, p_status IN xhb_cpp_list.status%TYPE) IS
    BEGIN
      FOR rec IN (SELECT xcl.cpp_list_id, xcl.list_clob_id, xcl.merged_clob_id
                    FROM xhb_cpp_list xcl
                   WHERE xcl.list_end_date < TRUNC(SYSDATE) - 1
                     AND xcl.last_update_date < TRUNC(SYSDATE) - p_no_of_days
                     AND xcl.status = p_status)  LOOP
          BEGIN
            delete_record(p_table_name => 'XHB_CPP_LIST', p_where_clause => 'CPP_LIST_ID = '||TO_CHAR(rec.cpp_list_id));            
            delete_record(p_table_name => 'AUD_CPP_LIST', p_where_clause => 'CPP_LIST_ID = '||TO_CHAR(rec.cpp_list_id));
            IF rec.list_clob_id IS NOT NULL THEN 
               -- Try and delete the CLOBs although they may be referenced by XHB_CPP_STAGING_INBOUND so if that happens then leave the CLOB alone, it will
			         -- be cleaned up when XHB_CPP_STAGING_INBOUND is processed by housekeeping.
			         BEGIN
               delete_record(p_table_name => 'XHB_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.list_clob_id));    
               delete_record(p_table_name => 'AUD_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.list_clob_id));    
               EXCEPTION 
                    WHEN OTHERS THEN NULL;
               END;
            END IF;
            IF rec.merged_clob_id IS NOT NULL THEN 
               -- Try and delete the CLOBs although they may be referenced by XHB_CPP_STAGING_INBOUND so if that happens then leave the CLOB alone, it will
			         -- be cleaned up when XHB_CPP_STAGING_INBOUND is processed by housekeeping.
			         BEGIN
               delete_record(p_table_name => 'XHB_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.merged_clob_id));              
               delete_record(p_table_name => 'AUD_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.merged_clob_id));              
               EXCEPTION 
                    WHEN OTHERS THEN NULL;
               END;
            END IF;
            l_hk_cpp_run_results.list_deleted := NVL(l_hk_cpp_run_results.list_deleted,0) + 1;
            COMMIT;
          EXCEPTION WHEN OTHERS THEN 
            ROLLBACK;
            l_hk_cpp_run_results.list_error := NVL(l_hk_cpp_run_results.list_error,0) + 1;
            l_hk_cpp_run_results.list_error_message := SUBSTR(l_error_point||' '||SQLERRM, 1,500);
            log_cpp_hk_error(p_hk_cpp_run_id => l_hk_cpp_run_id, p_error_message => l_hk_cpp_run_results.list_error_message);
          END;
      END LOOP;      
    END delete_cpplist;

  BEGIN
    l_hk_cpp_run_results.list_status := 'I';
    l_hk_cpp_run_results.list_error := 0;
    l_hk_cpp_run_results.list_deleted := 0;
    update_hk_cpp_log(l_hk_cpp_run_results);
    
    delete_cpplist(p_no_of_days => p_MS_days, p_status => 'MS');
    delete_cpplist(p_no_of_days => p_MF_days, p_status => 'MF');
    delete_cpplist(p_no_of_days => p_MF_days, p_status => 'NP');


    l_hk_cpp_run_results.list_status := CASE WHEN NVL(l_hk_cpp_run_results.list_error,0) > 0 THEN 'E' ELSE 'S' END;
    update_hk_cpp_log(l_hk_cpp_run_results);
  END process_cpp_listing;


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
    DELETE XHB_BLOB
    WHERE BLOB_ID in (SELECT blob_id
                      FROM XHB_COURTEL_LIST
                      WHERE XML_DOCUMENT_ID in (SELECT xml_document_id
                                                FROM   xhb_hk_document_temp
                                               )
                              )';

    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 5.1';
    
    
    l_delete := '
    DELETE xhb_courtel_list
    WHERE  xml_document_id IN (SELECT xml_document_id
                               FROM   xhb_hk_document_temp
                              )';

    EXECUTE IMMEDIATE l_delete;
    l_delete := REPLACE (l_delete,'xhb_courtel','aud_courtel');
    EXECUTE IMMEDIATE l_delete;
    l_error_point := 'PROCESS LISTINGS 5.2';
    
    
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
	l_error_point := 'DELETE CHARGE 3.1';

	delete_case_retention_policy(p_case_id => p_case_id, p_log_delete => p_log_delete);
	l_error_point := 'DELETE CHARGE 3.1.1';
	
    l_delete := 'DELETE xhb_disposal_line
                 WHERE  disposal2_id IN (SELECT disposal2_id
                                         FROM   xhb_disposal2
                                         WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                                         FROM   xhb_defendant_on_offence
                                                                         WHERE  offence_id IN (	SELECT offence_id
																								FROM xhb_offence
																								WHERE  charge_id IN (SELECT charge_id
																													 FROM   xhb_charge
																													 WHERE  case_id = :1) ) ) )';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL_LINE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CHARGE 3.2';

    l_delete := 'DELETE xhb_disposal2
                 WHERE  defendant_on_offence_id IN (SELECT defendant_on_offence_id
                                                    FROM   xhb_defendant_on_offence
                                                    WHERE  offence_id IN (	SELECT offence_id
																			FROM xhb_offence
																			WHERE  charge_id IN (SELECT charge_id
																								FROM   xhb_charge
																								WHERE  case_id = :1) ) )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DISPOSAL2' );
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

  
  PROCEDURE delete_aggravating_reasons(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE) IS
    l_delete VARCHAR2(2000);

    PROCEDURE log_deletion(p_table_name IN VARCHAR2) IS
    BEGIN
      IF p_log_delete THEN
         log_delete(p_table_name => p_table_name);
      END IF;
    END log_deletion;  
  BEGIN
      l_delete := 'DELETE xhb_aggravating_reasons xrr
                 WHERE  xrr.defendant_on_case_id IN (SELECT xdoc.defendant_on_case_id
                                                 FROM   xhb_defendant_on_case xdoc
                                                 WHERE  xdoc.case_id = :1)';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_REMAND_REASONS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT - AGGRAVATING ';
  END delete_aggravating_reasons;
  PROCEDURE delete_case_retention_policy(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE) IS
    l_delete VARCHAR2(2000);

    PROCEDURE log_deletion(p_table_name IN VARCHAR2) IS
    BEGIN
      IF p_log_delete THEN
         log_delete(p_table_name => p_table_name);
      END IF;
    END log_deletion;  
  BEGIN
    l_error_point := 'CLEAR RETENTION_POLICY DATA - 1';
	l_delete := 'UPDATE xhb_case x1 SET x1.DAR_RETENTION_POLICY_ID = NULL WHERE x1.case_id = :1';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
	
	l_error_point := 'CLEAR RETENTION_POLICY DATA - 2';
	l_delete := 'UPDATE xhb_defendant_on_case x1 SET x1.DAR_RETENTION_POLICY_ID = NULL WHERE x1.case_id = :1';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
  
    l_error_point := 'CLEAR RETENTION_POLICY DATA - 3';
	l_delete := 'UPDATE xhb_defendant_on_offence x1 SET x1.DAR_RETENTION_POLICY_ID = NULL
                 WHERE  x1.defendant_on_offence_id IN (SELECT x2.defendant_on_offence_id
													     FROM xhb_defendant_on_offence x2, xhb_defendant_on_case x3
														WHERE x3.defendant_on_case_id = x2.defendant_on_case_id
														  AND x3.case_id = :1)';
    EXECUTE IMMEDIATE l_delete USING p_case_id;
  
    l_error_point := 'DELETE RETENTION_POLICY 1';
    l_delete := 'DELETE xhb_dar_retention_policy x1
                 WHERE  x1.disposal2_id IN (SELECT x6.disposal2_id
                                         FROM   xhb_defendant_on_offence x2, xhb_offence x3, xhb_charge x4, xhb_disposal2 x6
                                         WHERE  x6.defendant_on_offence_id = x2.defendant_on_offence_id
                                           AND x2.offence_id = x3.offence_id
										   AND x3.charge_id = x4.charge_id
										   AND x4.case_id = :1)';
					 
	EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DAR_RETENTION_POLICY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
					 
    l_error_point := 'DELETE RETENTION_POLICY 2';
	l_delete := 'DELETE xhb_dar_retention_policy x1
                 WHERE  x1.defendant_on_offence_id IS NOT NULL 
				 AND    x1.defendant_on_offence_id IN (SELECT x2.defendant_on_offence_id
													FROM   xhb_defendant_on_offence x2, xhb_offence x3, xhb_charge x4
													WHERE  x2.offence_id = x3.offence_id
													  AND x3.charge_id = x4.charge_id
								                      AND x4.case_id = :1)';
													  
	EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DAR_RETENTION_POLICY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
					 
    l_error_point := 'DELETE RETENTION_POLICY 2';
	l_delete := 'DELETE xhb_dar_retention_policy x1
                 WHERE  x1.defendant_on_case_id IS NOT NULL
				 AND    x1.defendant_on_case_id IN (SELECT x5.defendant_on_case_id
													FROM   xhb_defendant_on_case x5
													WHERE  x5.case_id = :1)';
    
	EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DAR_RETENTION_POLICY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
	
	l_error_point := 'DELETE RETENTION_POLICY 3';
	l_delete := 'DELETE xhb_dar_retention_policy x1
                 WHERE  x1.case_id IS NOT NULL
				 AND    x1.case_id = :1';
    
	EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_DAR_RETENTION_POLICY' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
  END delete_case_retention_policy;
  
--
----
--
  PROCEDURE delete_remand_reasons(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE) IS
    l_delete VARCHAR2(2000);

    PROCEDURE log_deletion(p_table_name IN VARCHAR2) IS
    BEGIN
      IF p_log_delete THEN
         log_delete(p_table_name => p_table_name);
      END IF;
    END log_deletion;  
  BEGIN
    l_delete := 'DELETE xhb_remand_reasons xrr
                 WHERE  xrr.defendant_on_case_id IN (SELECT xdoc.defendant_on_case_id
                                                 FROM   xhb_defendant_on_case xdoc
                                                 WHERE  xdoc.case_id = :1
												) 
                 OR  xrr.order_id IN (SELECT xo.order_id 
                                                 FROM   xhb_defendant_on_case xdoc, xhb_order xo
                                                 WHERE  xo.defendant_on_case_id = xdoc.defendant_on_case_id
												 AND xdoc.case_id = :2
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id, p_case_id;
    log_deletion (p_table_name      => 'XHB_REMAND_REASONS' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id, p_case_id;
    l_error_point := 'DELETE DEFENDANT - REMAND_REASONS';
  END delete_remand_reasons;

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

	-- 7
    delete_remand_reasons(p_case_id => p_case_id, p_log_delete => p_log_delete);

    delete_aggravating_reasons(p_case_id => p_case_id, p_log_delete => p_log_delete);
	
    -- 8
    l_delete := 'DELETE xhb_order
                 WHERE  defendant_on_case_id IN (SELECT defendant_on_case_id
                                                 FROM   xhb_defendant_on_case
                                                 WHERE  case_id = :1
                                                )';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_ORDER' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE DEFENDANT 8 ';

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
    delete_records(p_table_name => 'XHB_DIARY_NOTE_ENTRY');
	delete_records(p_table_name => 'XHB_DIARY_NOTE_ENTRY',
                  p_sql         =>
      'DELETE XHB_DIARY_NOTE_ENTRY WHERE CASE_LISTING_ENTRY_ID IN (' ||
											'SELECT cle.CASE_LISTING_ENTRY_ID '||
											'FROM XHB_CASE_LISTING_ENTRY cle '||
											'WHERE cle.CASE_ID = :1)',
                  p_param1      => p_case_id);

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

    -- 18
    l_delete := 'DELETE XHB_REFUSED_BROADCAST_CASE
                 WHERE  case_id = :1';

    EXECUTE IMMEDIATE l_delete USING p_case_id;
    log_deletion (p_table_name      => 'XHB_REFUSED_BROADCAST_CASE' );
    l_delete := REPLACE (l_delete,'xhb','aud');
    EXECUTE IMMEDIATE l_delete USING p_case_id;
    l_error_point := 'DELETE CASE 18 ';
    
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
	l_case_history_records	NUMBER;

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
		-- Check that an XHB_CASE_HISTORY record exists
		SELECT COUNT(*) INTO l_case_history_records
		FROM xhb_case_history
		WHERE case_number = l_cases.case_no
		AND case_type = l_cases.case_type
		AND court_id = l_cases.court_id;

		IF l_case_history_records = 0 THEN
			-- No XHB_CASE_HISTORY record, log it and do not attempt to delete the case
			log_no_history_hk_error(p_hk_run_id => l_hk_run_id
				,p_court_id => l_cases.court_id
				,p_case_id => l_cases.case_id
				,p_case_type => l_cases.case_type
				,p_case_no => l_cases.case_no);
		ELSE
			-- Ok to proceed with delete
        delete_hearing(p_case_id => l_case_history.case_id);

        delete_listing(p_case_id => l_case_history.case_id);

        delete_defendant(p_case_id => l_case_history.case_id);

        delete_charge(p_case_id => l_case_history.case_id);

        delete_case(p_case_id => l_case_history.case_id);

        COMMIT;

        l_cases_deleted := l_cases_deleted + 1;
		END IF;

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
  
  
  PROCEDURE write_hk_cpp_error_log_file(p_hk_cpp_run_id IN NUMBER) IS

    l_file utl_file.file_type;

    CURSOR c_xhb_hk_cpp_error_log (b_hk_cpp_run_id NUMBER) IS
      SELECT *
      FROM   xhb_hk_cpp_error_log
      WHERE  hk_cpp_run_id = b_hk_cpp_run_id;

  BEGIN

    l_error_point := 'WRITE ERROR LOG FILE ';

    FOR l_errors IN c_xhb_hk_cpp_error_log (p_hk_cpp_run_id)  LOOP

      IF c_xhb_hk_cpp_error_log%ROWCOUNT = 1 THEN
        l_file := utl_file.fopen('HK_LOGS','HK_CPP_'||LPAD(p_hk_cpp_run_id,10,'0')||'_ERROR.log','a');
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

  END write_hk_cpp_error_log_file;

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

	
	PROCEDURE write_hk_cpp_metrics_log_file(p_hk_cpp_run_id IN NUMBER
											,p_MS_days IN NUMBER
											,p_MF_days IN NUMBER) IS

		l_file utl_file.file_type;

		CURSOR c_xhb_hk_cpp_results (b_hk_cpp_run_id NUMBER) IS
		SELECT *
		FROM   xhb_hk_cpp_results
		WHERE  hk_cpp_run_id = b_hk_cpp_run_id;

		l_rec_results c_xhb_hk_cpp_results%ROWTYPE;

		l_status_desc VARCHAR2(100);

	BEGIN

		l_file := utl_file.fopen('HK_LOGS','HK_CPP_'||LPAD(p_hk_cpp_run_id,10,'0')||'_METRICS.log','a');

		OPEN  c_xhb_hk_cpp_results(p_hk_cpp_run_id);
		FETCH c_xhb_hk_cpp_results INTO l_rec_results;

		IF c_xhb_hk_cpp_results%NOTFOUND THEN
			-- No results found
			utl_file.put_line(l_file,'No record of CPP HK run '||p_hk_cpp_run_id||' can be found',TRUE);
		ELSE
			-- Header
			utl_file.put_line(l_file,'Log file produced on '||TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),TRUE);
			utl_file.new_line(l_file,1);
			utl_file.put_line(l_file,'CPP Housekeeping Run Id. '||p_hk_cpp_run_id,TRUE);
			utl_file.put_line(l_file,'Started On   : '||TO_CHAR(l_rec_results.run_start_date,'DD-MON-YYYY HH24:MI:SS'),TRUE);
			utl_file.put_line(l_file,'Completed On : '||TO_CHAR(l_rec_results.run_end_date  ,'DD-MON-YYYY HH24:MI:SS'),TRUE);
			utl_file.new_line(l_file,1);
			utl_file.put_line(l_file,'Retention Periods (days) : ',TRUE);
			utl_file.put_line(l_file,'Successful records (MS) : '||p_MS_days,TRUE);
			utl_file.put_line(l_file,'Failed records (MF) : '||p_MF_days,TRUE);
			utl_file.new_line(l_file,1);

			-- List Statistics
			utl_file.put_line(l_file,'List Statistics',TRUE);
			IF l_rec_results.list_status = 'S' THEN
			l_status_desc := ' - Success';
			ELSIF l_rec_results.list_status = 'F' THEN
			l_status_desc := ' - Failure - see error message below';
			ELSIF l_rec_results.list_status = 'E' THEN
			l_status_desc := ' - Some records not deleted - see count below';
			ELSE
			l_status_desc := ' - Unknown status';
			END IF;

			utl_file.put_line(l_file,'Run Status : '||l_rec_results.list_status||l_status_desc,TRUE);
			IF l_rec_results.list_status = 'F' THEN
			utl_file.put_line(l_file,l_rec_results.list_error_message,TRUE);
			END IF;

			utl_file.put_line(l_file,'Records Deleted : '||l_rec_results.list_deleted,TRUE);
			utl_file.put_line(l_file,'Records Error   : '||l_rec_results.list_error,TRUE);
			utl_file.new_line(l_file,1);

			-- IWP Statistics
			utl_file.put_line(l_file,'IWP Statistics',TRUE);
			IF l_rec_results.iwp_status = 'S' THEN
			l_status_desc := ' - Success';
			ELSIF l_rec_results.iwp_status = 'F' THEN
			l_status_desc := ' - Failure - see error message below';
			ELSIF l_rec_results.iwp_status = 'E' THEN
			l_status_desc := ' - Some records not deleted - see count below';
			ELSE
			l_status_desc := ' - Unknown status';
			END IF;

			utl_file.put_line(l_file,'Run Status : '||l_rec_results.iwp_status||l_status_desc,TRUE);
			IF l_rec_results.iwp_status = 'F' THEN
			utl_file.put_line(l_file,l_rec_results.iwp_error_message,TRUE);
			END IF;

			utl_file.put_line(l_file,'Records Deleted : '||l_rec_results.iwp_deleted,TRUE);
			utl_file.put_line(l_file,'Records Error   : '||l_rec_results.iwp_error,TRUE);
			utl_file.new_line(l_file,1);

			-- Public Display Statistics
			utl_file.put_line(l_file,'Public Display Statistics',TRUE);
			IF l_rec_results.pd_status = 'S' THEN
			l_status_desc := ' - Success';
			ELSIF l_rec_results.pd_status = 'F' THEN
			l_status_desc := ' - Failure - see error message below';
			ELSIF l_rec_results.pd_status = 'E' THEN
			l_status_desc := ' - Some records not deleted - see count below';
			ELSE
			l_status_desc := ' - Unknown status';
			END IF;

			utl_file.put_line(l_file,'Run Status : '||l_rec_results.pd_status||l_status_desc,TRUE);
			IF l_rec_results.pd_status = 'F' THEN
			utl_file.put_line(l_file,l_rec_results.pd_error_message,TRUE);
			END IF;

			utl_file.put_line(l_file,'Records Deleted : '||l_rec_results.pd_deleted,TRUE);
			utl_file.put_line(l_file,'Records Error   : '||l_rec_results.pd_error,TRUE);
			utl_file.new_line(l_file,1);
			
			-- CPP Staging Inbound Statistics
			utl_file.put_line(l_file,'CPP Staging Inbound Statistics',TRUE);
			IF l_rec_results.staging_status = 'S' THEN
			l_status_desc := ' - Success';
			ELSIF l_rec_results.staging_status = 'F' THEN
			l_status_desc := ' - Failure - see error message below';
			ELSIF l_rec_results.staging_status = 'E' THEN
			l_status_desc := ' - Some records not deleted - see count below';
			ELSE
			l_status_desc := ' - Unknown status';
			END IF;

			utl_file.put_line(l_file,'Run Status : '||l_rec_results.staging_status||l_status_desc,TRUE);
			IF l_rec_results.staging_status = 'F' THEN
			utl_file.put_line(l_file,l_rec_results.staging_error_message,TRUE);
			END IF;

			utl_file.put_line(l_file,'Records Deleted : '||l_rec_results.staging_deleted,TRUE);
			utl_file.put_line(l_file,'Records Error   : '||l_rec_results.staging_error,TRUE);
			utl_file.new_line(l_file,1);

			-- End of Report
			utl_file.new_line(l_file,1);
			utl_file.put_line(l_file,'End of Report',TRUE);

		END IF;

		CLOSE c_xhb_hk_cpp_results;

		utl_file.fclose(l_file);

	EXCEPTION
		-- any exceptions for file handling will go here
		WHEN OTHERS THEN
			-- close cursor
			IF c_xhb_hk_cpp_results%ISOPEN THEN
			CLOSE c_xhb_hk_cpp_results;
			END IF;

			-- close file
			IF utl_file.is_open(l_file) THEN
			utl_file.fclose(l_file);
			END IF;

	END write_hk_cpp_metrics_log_file;
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
  
  PROCEDURE insert_defendantoncase_history(p_court_id             IN xhb_Defendant.court_id%TYPE,
                                           p_case_id              IN xhb_defendant_on_Case.case_id%TYPE,
                                           p_defendant_id         IN xhb_Defendant.defendant_id%TYPE,
                                           p_defendant_history_id IN XHB_DEFENDANT_HISTORY.DEFENDANT_HISTORY_ID%TYPE,
                                           p_case_history_id      IN xhb_case_history.case_history_id%TYPE,
                                           p_created_by           IN xhb_case_history.created_by%TYPE,
                                           pio_error_message      IN OUT xhb_hk_error_log.error_message%TYPE) IS
  
  CURSOR wr_def_on_case_history_recs IS
   SELECT  distinct xdc.defendant_number
    FROM   xhb_Defendant  xd,xhb_defendant_on_Case xdc
    WHERE  xd.court_id = p_court_id
    AND    xdc.case_id = p_case_id
    AND    xd.defendant_id  = xdc.defendant_id
    AND    xd.defendant_id = p_defendant_id;
  BEGIN
    FOR k in wr_def_on_case_history_recs LOOP
        pio_error_message := 'DEF_ON_CASE_HISTORY_INSERT - COURT_ID : '||p_court_id||' , CASE_ID : '||p_case_id||' , DEFENDANT_ID : '||p_defendant_id;
        INSERT INTO XHB_DEFENDANT_ON_CASE_HISTORY (
                                         DEFENDANT_ON_CASE_HISTORY_ID,
                                         DEFENDANT_HISTORY_ID,
                                         CASE_HISTORY_ID,
                                         DEFENDANT_NUMBER,
                                         LAST_UPDATE_DATE,
                                         CREATION_DATE,
                                         LAST_UPDATED_BY,
                                         CREATED_BY
                                         )
                                VALUES (
                                         xhb_defendant_on_case_hist_seq.nextval,
                                         p_defendant_history_id,
                                         p_case_history_id,
                                         k.defendant_number,
                                         sysdate,
                                         sysdate,
                                         p_created_by,
                                         p_created_by
                                        );
    END LOOP;
  END insert_defendantoncase_history;

  PROCEDURE insert_defendant_history(p_court_id        IN xhb_Defendant.court_id%TYPE,
                                     p_case_id         IN xhb_defendant_on_Case.case_id%TYPE,
                                     p_change_reason   IN xhb_case_history.reason_deleted%TYPE,
                                     p_case_history_id IN xhb_case_history.case_history_id%TYPE,
                                     p_created_by      IN xhb_case_history.created_by%TYPE,
                                     pio_error_message IN OUT xhb_hk_error_log.error_message%TYPE) IS
  
   v_defendant_history_id XHB_DEFENDANT_HISTORY.DEFENDANT_HISTORY_ID%TYPE;
   CURSOR wr_def_history_recs IS
   SELECT  xd.*
    FROM   xhb_Defendant  xd,xhb_defendant_on_Case xdc
    WHERE  xd.court_id = p_court_id
    AND    xdc.case_id = p_case_id
    AND    xd.defendant_id  = xdc.defendant_id
    AND   nvl(xdc.obs_ind,'N') != 'Y'
    -- Exclude those already written to DEFENDANT_HISTORY
    AND    NOT EXISTS (SELECT 'X' FROM XHB_DEFENDANT_HISTORY XDH
                        WHERE XDH.COURT_ID = XD.COURT_ID AND
                              XDH.DEFENDANT_ID = XD.DEFENDANT_ID AND
                              XDH.CREST_DEFENDANT_ID = XD.CREST_DEFENDANT_ID);
  
  BEGIN
    FOR j in wr_def_history_recs LOOP
        pio_error_message :=  'DEFENDANT_HISTORY_INSERT - COURT_ID : '||p_court_id||' , CASE_ID : '||P_case_id||' , DEFENDANT_ID : '||j.defendant_id;
        SELECT XHB_DEFENDANT_HISTORY_SEQ.NEXTVAL INTO v_defendant_history_id from dual;

        INSERT INTO XHB_DEFENDANT_HISTORY (
                                         DEFENDANT_HISTORY_ID,
                                         DEFENDANT_ID,
                                         CREST_DEFENDANT_ID,
                                         COURT_ID,
                                         SURNAME,
                                         FIRST_NAME,
                                         MIDDLE_NAME,
                                         DATE_OF_BIRTH,
                                         GENDER,
                                         REASON_DELETED,
                                         DATE_ARCHIVED,
                                         LAST_UPDATE_DATE,
                                         CREATION_DATE,
                                         LAST_UPDATED_BY,
                                         CREATED_BY
                                         )
                                VALUES (
                                         v_defendant_history_id,
                                         j.DEFENDANT_ID,
                                         j.CREST_DEFENDANT_ID,
                                         j.COURT_ID,
                                         j.SURNAME,
                                         j.FIRST_NAME,
                                         j.MIDDLE_NAME,
                                         j.DATE_OF_BIRTH,
                                         j.GENDER,
                                         p_change_reason,
                                         SYSDATE,
                                         sysdate,
                                         sysdate,
                                         p_created_by,
                                         p_created_by
                                        );
                                        
        insert_defendantoncase_history(p_court_id             => p_court_id,
                                       p_case_id              => p_case_id,
                                       p_defendant_id         => j.defendant_id,
                                       p_defendant_history_id => v_defendant_history_id,
                                       p_case_history_id      => p_case_history_id,
                                       p_created_by           => p_created_by,
                                       pio_error_message      => pio_error_message);                      
    END LOOP;
  END insert_defendant_history;

/**
  * DESCRIPTION :
  *   Procedures            Purpose
  *   =========             =======
  *   insert_case_history   After the record has been made obsolete, create a record in the xhb_case_history_table
  *                         This procedure can be called outside of the obsolete_case so has been placed outside of the obsolete_case procedure.
  *
  */
  PROCEDURE insert_case_history (p_case_id          IN xhb_case.case_id%TYPE
                               ,p_change_reason    IN xhb_case_history.reason_deleted%TYPE
                               ,p_created_by       IN xhb_case_history.created_by%TYPE
                               ,pio_error_message  IN OUT xhb_hk_error_log.error_message%TYPE)
 IS
  v_case_history_id xhb_case_history.case_history_id%TYPE;
  CURSOR case_c IS
  SELECT xc.*
 , xrc.crest_code
 FROM xhb_case xc
 ,    xhb_ref_court xrc
 WHERE xc.case_id = p_case_id
 AND   xc.ref_court_id = xrc.ref_court_id (+)
 ;

 BEGIN

  FOR case_r IN case_c
   LOOP
    pio_error_message := 'CASE_HISTORY_INSERT - COURT_ID : '||case_r.court_id||' , CASE_NUMBER : '||case_r.case_number||' , CASE_TYPE : '||case_r.case_type;
    SELECT xhb_case_history_seq.nextval INTO v_case_history_id FROM dual;

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
    VALUES(v_case_history_id
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
           ,p_created_by
           ,p_created_by
           ,1
           );
           
   insert_defendant_history(p_court_id        => case_r.court_id,
                            p_case_id         => case_r.case_id,
                            p_change_reason   => p_change_reason,
                            p_case_history_id => v_case_history_id,
                            p_created_by      => p_created_by,
                            pio_error_message => pio_error_message);
           
   END LOOP;


 END insert_case_history;
 
 PROCEDURE insert_case_history (p_case_id       IN xhb_case.case_id%TYPE,
                                 p_change_reason IN VARCHAR2)
  IS
    v_error_message VARCHAR2(1000);
  BEGIN
    insert_case_history(p_case_id          => p_case_id,
                        p_change_reason    => p_change_reason,
                        p_created_by       => 'XHIBIT',
                        pio_error_message  => v_error_message);
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
  
  PROCEDURE set_case_to_historic(p_case_id IN XHB_CASE.CASE_ID%TYPE) IS
     v_error_message VARCHAR2(1000);
  BEGIN
     update_case_status(p_case_id     => p_case_id,
                        p_case_status => 'H');
      
     -- CTX-4660 Write out the case history records at point of status change 
     insert_case_history(p_case_id          => p_case_id,
                         p_change_reason    => 'Case Housekeeping',
                         p_created_by       => 'XHIBIT',
                         pio_error_message  => v_error_message);
  EXCEPTION WHEN OTHERS THEN 
      dbms_output.put_line('set_case_to_historic: '||nvl(v_error_message,'Update Case Status'));
      RAISE;
  END set_case_to_historic;

  PROCEDURE find_all_cases_eligible_for_hk (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                            , p_upper_limit IN NUMBER DEFAULT 10000) IS
  /*
     30/08/2019    S.Sethuraman    CTX-4490 : Find all CASES ELIGIBLE FOR HOUSEKEEPING AND MARK THEM with case_status = 'H'
  */
       v_court_id NUMBER;
       v_upper_limit NUMBER;
       v_number_of_years number := NVL(TO_NUMBER(get_config_property(p_property_name => 'XHB_HK_NO_OF_YEARS')),7);

  CURSOR eligible_B_U_cases IS
         select xc.court_id,
                xc.case_id,
                xc.case_number,
                xc.case_type,
                xc.last_update_date,
                xc.date_trans_to,
                xc.case_status
          from xhb_case xc
         where xc.court_id = nvl(v_court_id,xc.court_id) and -- check for court_id if supplied ONLY
               xc.case_type in ('B','U') and -- B / U Cases only
               xc.case_status <> 'H' and -- eliminate previously marked HK Cases
               -- 1.Look for all cases in selected court(s) where the case number is more than 6 years previous:
               to_char(sysdate,'YYYY')-substr(xc.case_number,1,4) > v_number_of_years-1 and
               -- 2.For B and U cases: a. Is the XHB_CASE.LAST_UPDATE_DATE > 7 years ago?  b. If so then this case is eligible. Otherwise not.
               (decode(xc.case_type,'B',months_between(trunc(sysdate),trunc(xc.last_update_date))/12,
                                     'U',months_between(trunc(sysdate),trunc(xc.last_update_date))/12,
                                      0) > v_number_of_years) AND
               rownum <= v_upper_limit
               order by xc.court_id,xc.case_id;

  CURSOR eligible_non_B_U_cases IS
         select xc.court_id,
                xc.case_id,
                xc.case_number,
                xc.case_type,
                xc.last_update_date,
                xc.date_trans_to,
                xc.case_status
          from xhb_case xc
         where xc.court_id = nvl(v_court_id,xc.court_id) and -- check forcourt_id if supplied ONLY
               xc.case_type NOT in ('B','U') and -- Non B / U Cases only
               xc.case_status <> 'H' and -- eliminate previously marked HK Cases
               -- 1.Look for all cases in selected court(s) where the case number is more than 6 years previous:
               to_char(sysdate,'YYYY')-substr(xc.case_number,1,4) > v_number_of_years-1 and
                 -- 3.For A, S or T cases:
                 --   a. Both steps (b) and (c) below need to be proven as eligible if the case is to be eligible.
                 --   b. For each case if the case is transferred out, XHB_CASE.TRANSFERRED_CASE=.Y., then this is case may be eligible. Check 3(b)(i). If not, then go to 3(c)
                 --      i. If the date transferred, XHB_CASE.DATE_TRANS_TO, is > 7 years ago and there are no outstanding BW.s on the case then the case is eligible for HK.
                 --      1. Check that for each entry in XHB_DEFENDANT_ON_CASE there are no entries in XHB_BW_HISTORY where BW_ISSUE_DATE is not null and BW_END_DATE is null
                 --      ii. Otherwise the case is not eligible for HK.
                 --      iii. Processing of this case can stop.
                ((decode(xc.case_type,'A',decode(nvl(xc.transferred_case,'N'),'Y',months_between(trunc(sysdate),trunc(xc.date_trans_to))/12,v_number_of_years+1),
                                     'S',decode(nvl(xc.transferred_case,'N'),'Y',months_between(trunc(sysdate),trunc(xc.date_trans_to))/12,v_number_of_years+1),
                                     'T',decode(nvl(xc.transferred_case,'N'),'Y',months_between(trunc(sysdate),trunc(xc.date_trans_to))/12,v_number_of_years+1),0) > v_number_of_years AND
                     --c. For each case, for each defendant on the case:
                     --   i. Check the XHB_DEFENDANT_ON_CASE.AMENDED_DATE_EXPORTED field.
                     --     1. If any are not null and the date is less than 7 years ago then this case is not eligible. Processing of this case can stop.
                     --     2. Otherwise continue
                     --   ii. Check XHB_DEFENDANT_ON_CASE for any records where AMENDED_DATE_EXPORTED is null and/or DATE_EXPORTED is null.[HB1] [AS2]
                     --     1. If any are not null and the date is less than 7 years ago then this case is not eligible. Processing of this case can stop.
                     --     2. If both dates are null then continue regardless of whether XHB_DEFENDANT_ON_CASE.RESULTS_VERIFIED=.E.. This could be because the BAU team have directly set the authorization status but the case may still be Live so we will not use this status to determine eligibility.
                     --     3. Otherwise continue
                     --   iii. All XHB_DEFENDANT_ON_CASE records for each identified case should now have been covered.
                     --        If no record has marked the case as .Not Eligible. then the case in this step is .Eligible.
                  NOT EXISTS (select 'X' from xhb_bw_history xbh,xhb_defendant_on_case xdoc where xbh.defendant_on_Case_id = xdoc.defendant_on_case_id and xdoc.case_id = xc.case_id and xbh.bw_issue_date is NOT NULL AND xbh.bw_end_date IS NULL)
                 ) AND (NOT EXISTS (select 'X' from xhb_Defendant_on_case xdoc1 where xdoc1. case_id = xc.case_id) OR
                        NOT EXISTS (select 'X' from xhb_Defendant_on_Case xdoc2
                                 where xdoc2.case_id = xc.case_id and
                                       (( xdoc2.amended_date_exported is NOT NULL AND months_between(trunc(sysdate),trunc(xdoc2.amended_date_exported))/12 < v_number_of_years) OR
                                       ( xdoc2.date_exported is NOT NULL AND months_between(trunc(sysdate),trunc(xdoc2.date_exported))/12 < v_number_of_years))))) AND
                  rownum <= v_upper_limit -- fetch as per limit adviced
                  order by xc.court_id,xc.case_id;

       v_get_case_status varchar2(250) := null;

  BEGIN

      select decode(p_court_id,0,NULL,p_court_id),
             decode(p_upper_limit,0,10000,p_upper_limit)
        into v_court_id,v_upper_limit from dual;

      --dbms_output.enable(1000000);
      --dbms_output.put_line('Court_id,Case_id,CASE_Number,Case_type,Case_status,Last_update_date,Date_trans_to,Case_status');
   --  Mark Eligible B / U CASEs for HOUSEKEEPING
   FOR i in eligible_B_U_cases loop
      --dbms_output.put_line(i.court_id||','||i.case_id||','||i.case_number||','||i.case_type||','||i.case_status||','||i.last_update_date||','||i.date_trans_to);
       set_case_to_historic(p_case_id => i.CASE_ID);
   END LOOP;
   COMMIT;

   -- Mark Eligible Appeal / Sentence / Trial Cases for HOUSEKEEPING
   FOR i in eligible_non_B_U_cases loop
       v_get_case_status := XHB_CASE_PKG.determine_case_status(i.case_id);
       if v_get_case_status in ('Dealt With','Incomplete_I','Incomplete_N') THEN
          --dbms_output.put_line(i.court_id||','||i.case_id||i.case_number||','||i.case_type||','||','||i.case_status||','||i.last_update_date||','||i.date_trans_to||','||v_get_case_status);
          set_case_to_historic(p_case_id => i.CASE_ID);
       end if;
   END LOOP;
   COMMIT;

  END find_all_cases_eligible_for_hk;

  PROCEDURE find_all_hk_cases_for_deletion (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                            , p_upper_limit IN NUMBER DEFAULT 10000) IS
/*
   03/09/2019    S.Sethuraman    CTX-4491 : Find all Housekept cases and move to MTBL_CASE_HISTORY after a said period to enable deletion
*/
       v_court_id NUMBER;
       v_upper_limit NUMBER;
       v_number_of_days NUMBER := NVL(TO_NUMBER(get_config_property(p_property_name => 'XHB_HK_NO_OF_DAYS_TO_HOUSEKEEP')),30);

  CURSOR hk_cases_for_deletion IS
  select xc.court_id,
         xc.case_id,
         xc.case_number,
         xc.case_type,
         xc.last_update_date,
         xc.case_status
    from xhb_case xc
   where xc.court_id = nvl(v_court_id,xc.court_id) and xc.case_status = 'H' and
         trunc(sysdate)-trunc(xc.last_update_date) >= v_number_of_days and
         not exists
         (select 'X' from mtbl_case_history mh
           where mh.court_id = xc.court_id and
                 mh.case_no = xc.case_number and
                 mh.case_type = xc.case_type) and
         rownum <= v_upper_limit
         order by xc.court_id,xc.case_id;


  BEGIN

       select decode(p_court_id,0,NULL,p_court_id),
              decode(p_upper_limit,0,10000,p_upper_limit)
        into v_court_id,v_upper_limit from dual;


      --dbms_output.enable(1000000);
      --dbms_output.put_line('Court_id,Case_id,Case_Number,Case_type,Last_Update_date,Case_status');
   --  Move to MTBL_CASE_HISTORY
   FOR i in hk_cases_for_deletion loop
      --dbms_output.put_line(i.court_id||','||i.case_id||','||i.case_number||','||i.case_type||','||i.last_update_date||','||i.case_status);

       INSERT INTO MTBL_CASE_HISTORY (
                                      COURT_ID,
                                      CASE_TYPE,
                                      CASE_NO,
                                      LAST_UPDATE_DATE)
                              VALUES (i.court_id,
                                      i.case_type,
                                      i.case_number,
                                      SYSDATE) ;
   END LOOP;
   COMMIT;

  END find_all_hk_cases_for_deletion;

  PROCEDURE write_del_record_to_history (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                         , p_upper_limit IN NUMBER DEFAULT 10000) IS
/*
   09/09/2019    S.Sethuraman    CTX-4494 : Write the deletion records that are moved to MTBL_CASE_HISTORY
                                            into HISTORY Tables before actual Deletion
   10/09/2019    S.Sethuraman    CTX-4495 : Rollback HISTORY INSERTS in case of error at any stage of processing a CASE
                                            and WRITE TO ERR_LOG , SET CASE_STATUS TO NULL
*/
       l_hk_run_id      xhb_hk_results.hk_run_id%TYPE;
       l_err_message xhb_hk_results.error_message%TYPE;
       l_err_stage    varchar2(1000)  := NULL;
       l_run_start_date  date := SYSDATE;
       v_no_of_cases NUMBER := 0;
       v_hk_period NUMBER := NVL(TO_NUMBER(get_config_property(p_property_name => 'XHB_HK_NO_OF_YEARS')),7);
       v_court_id xhb_case.court_id%type;
       v_upper_limit NUMBER;
       v_defendant_id xhb_defendant.defendant_id%type;
       v_defendant_number xhb_defendant_on_case.defendant_number%type;

  CURSOR wr_case_history_recs IS
   SELECT  xc.*
    FROM   mtbl_case_history  mtb
    ,      xhb_case           xc
    WHERE  xc.court_id = nvl(v_court_id,xc.court_id)
    AND    xc.case_status = 'H'
    AND    mtb.court_id  = xc.court_id
    AND    mtb.case_no   = xc.case_number
    AND    mtb.case_type = xc.case_type
    -- Exclude those already written to CASE_HISTORY
    AND    NOT EXISTS (SELECT 'X' FROM XHB_CASE_HISTORY XCH
                        WHERE XCH.COURT_ID = XC.COURT_ID AND
                              XCH.CASE_NUMBER = XC.CASE_NUMBER AND
                              XCH.CASE_TYPE = XC.CASE_TYPE) and
         rownum <= v_upper_limit
    order by xc.court_id,xc.case_id;

  BEGIN

       select decode(p_court_id,0,NULL,p_court_id),
              decode(p_upper_limit,0,10000,p_upper_limit)
        into v_court_id,v_upper_limit from dual;

      -- get hk_run_id
      l_hk_run_id := get_next_hk_run_id();

      --dbms_output.enable(1000000);
      --dbms_output.put_line('Court_id,Case_id,Case_Number,Case_type,Case_status');

      l_run_start_date := SYSDATE; -- record start time to log info in xhb_hk_results

   v_no_of_cases := 0;
   --  INSERT INTO CASE_HISTORY
   FOR i in wr_case_history_recs
   LOOP
      --dbms_output.put_line(i.court_id||','||i.case_id||','||i.case_number||','||i.case_type||','||i.case_status);

      BEGIN
       
       insert_case_history (p_case_id          => i.case_id,
                            p_change_reason    => '7 Year Case Housekeeping',
                            p_created_by       => 'WRITE_DEL_RECORD_TO_HISTORY',
                            pio_error_message   => l_err_stage);

    EXCEPTION  WHEN OTHERS THEN

          -- Rollback all the HISTORY INSERTS for this CASE as there is an ERROR at some stage
          ROLLBACK;

        BEGIN
          UPDATE XHB_CASE
             SET CASE_STATUS = NULL,
                 LAST_UPDATE_DATE = TO_DATE(to_char(sysdate,'YYYY')-v_hk_period,'YYYY')
           WHERE CASE_ID = i.case_id;

          l_err_message := SUBSTR(l_err_stage||' '||SQLERRM,1,500);

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
          ,i.case_number
          ,i.case_type
          ,i.court_id
          ,i.case_id
          ,l_err_message
          );

          -- remove from MTBL_CASE_HISTORY as well so this is NOT deleted without HISTORY
          DELETE FROM MTBL_CASE_HISTORY
                WHERE COURT_ID = i.court_id and
                      CASE_NO = i.case_number and
                      CASE_TYPE = i.case_type;

          COMMIT;
      EXCEPTION WHEN OTHERS THEN

               l_err_message := SUBSTR(l_err_stage||' '||SQLERRM,1,500);
     -- this will log an error if there is exception in the above exception block
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
          ,i.case_number
          ,i.case_type
          ,i.court_id
          ,i.case_id
          ,l_err_message
          );
          COMMIT;
      END;
    END;

      COMMIT; -- Commit the CASE and deffendant History INSERTS made for this CASE
      v_no_of_cases := v_no_of_cases + 1;
   END LOOP;

     -- LOG INFO AFTER THE RUN completed successfuly


      INSERT INTO XHB_HK_RESULTS
                               (
                                HK_RUN_ID         ,
                                RUN_TYPE          ,
                                RUN_START_DATE    ,
                                RUN_END_DATE      ,
                                ERROR_MESSAGE     )
                     VALUES    (
                                 l_hk_run_id,
                                 'H', -- HOUSEKEEPING HISTORY RUN
                                 l_run_start_date,
                                 SYSDATE,
                                 'xhb_housekeeping_pkg.WRITE_DEL_RECORD_TO_HISTORY - Completed, No of Cases inserted in History : '||v_no_of_cases);
       COMMIT;

  END write_del_record_to_history;


  PROCEDURE process_cad_hk (p_age IN NUMBER DEFAULT 180) IS

	CURSOR c_get_cad_records IS
	SELECT distinct(l.dmi_cad_run_history_id), l.log_message
	FROM xhb_dmi_cad_run_log l, xhb_dmi_cad_run_history h
	WHERE UPPER(l.log_message) LIKE 'PROCESS COMPLETE%'
	AND h.dmi_cad_run_history_id = l.dmi_cad_run_history_id
	AND TRUNC(h.creation_date) < TRUNC(SYSDATE - p_age);

  lv_clob_string  VARCHAR2(30) := 'CLOB_ID IN XHB_CLOB TABLE: ';
  ln_clob_string_index  NUMBER;
  ln_clob_id_string VARCHAR2(15);

  BEGIN

	FOR rec IN c_get_cad_records LOOP

		-- Try and extract the CLOB_ID
		ln_clob_string_index := INSTR(UPPER(rec.log_message), lv_clob_string);
		IF ln_clob_string_index > 0 THEN
		  -- If have CLOB_ID, delete the XHB_CLOB record
		  ln_clob_id_string := SUBSTR(rec.log_message, ln_clob_string_index + LENGTH(lv_clob_string));
		  DELETE FROM xhb_clob WHERE clob_id = TO_NUMBER(ln_clob_id_string);
		END IF;

		-- Delete all XHB_DMI_CAD_RUN_LOG records linked to the same parent XHB_DMI_CAD_RUN_HISTORY
		DELETE FROM xhb_dmi_cad_run_log WHERE dmi_cad_run_history_id = rec.dmi_cad_run_history_id;

		-- Delete the parent XHB_DMI_CAD_RUN_HISTORY
		DELETE FROM xhb_dmi_cad_run_history WHERE dmi_cad_run_history_id = rec.dmi_cad_run_history_id;

		COMMIT;

    END LOOP;

  EXCEPTION
	WHEN OTHERS THEN
        ROLLBACK;
		RAISE;

  END process_cad_hk;

  PROCEDURE process_report_hk (p_age IN NUMBER DEFAULT 180) IS

  BEGIN
	-- Delete any AUD_REPORT_LOG records older than the number of days specified.  The table
	-- has a CREATION_DATE but that represents the XHB_REPORT_LOG table and never changes so
	-- have to use LAST_UPDATE_DATE
	DELETE FROM aud_report_log
	WHERE TRUNC(last_update_date) < TRUNC(SYSDATE - p_age);

	COMMIT;

  EXCEPTION
	WHEN OTHERS THEN
        ROLLBACK;
		RAISE;

  END process_report_hk;

  PROCEDURE del_list_data_part1 (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                         , p_upper_limit IN NUMBER DEFAULT 10000) IS
/*
   17/09/2019    S.Sethuraman    CTX-4498 : Delete records from XHB_HEARING, XHB_SITTING
*/
       l_hk_run_id      xhb_hk_results.hk_run_id%TYPE;
       l_err_message xhb_hk_results.error_message%TYPE;
       l_err_stage    varchar2(1000)  := NULL;
       l_run_start_date  date := SYSDATE;
       v_no_of_lists NUMBER := 0;
       v_no_of_sittings NUMBER := 0;
       v_hk_list_days NUMBER := 180;
       v_court_id xhb_case.court_id%type;
       l_court_id xhb_case.court_id%type;
       v_list_id xhb_list.list_id%type;
       v_sitting_id xhb_sitting.sitting_id%type;
       v_upper_limit NUMBER;

  CURSOR del_xhb_sitting IS
   SELECT xl.court_id,xs.list_id,xs.sitting_id
     FROM xhb_sitting xs , xhb_list xl
    WHERE xl.court_id = nvl(v_court_id,xl.court_id) AND
          xs.list_id = xl.list_id AND
    NOT EXISTS (select 'X' from xhb_scheduled_hearing xsh where xs.sitting_id = xsh.sitting_id) AND
    rownum <= v_upper_limit
    ORDER BY xl.court_id,xs.sitting_id ;

  CURSOR del_xhb_hearing_list IS
   SELECT xhl.court_id,xhl.list_id
     FROM xhb_hearing_list xhl
    WHERE xhl.court_id = nvl(v_court_id,xhl.court_id) AND
    NOT EXISTS (select 'X' from xhb_sitting xs where xs.list_id = xhl.list_id) AND
    rownum <= v_upper_limit
    ORDER BY xhl.court_id,xhl.list_id ;

  BEGIN

     BEGIN
       select decode(p_court_id,0,NULL,p_court_id),
              decode(p_upper_limit,0,10000,p_upper_limit)
        into v_court_id,v_upper_limit from dual;


     EXCEPTION WHEN OTHERS THEN
         NULL;
--          v_hk_list_days := 180;
     END;

      -- get hk_run_id
      l_hk_run_id := get_next_hk_run_id();

      --dbms_output.enable(1000000);
      --dbms_output.put_line('Court_id,List_id,Sitting_id');

      l_run_start_date := SYSDATE; -- record start time to log info in xhb_hk_results

   v_no_of_lists := 0;
   v_no_of_sittings := 0;

   --  DELETE from XHB_SITTING
   FOR i in del_xhb_sitting
   LOOP
      l_court_id := i.court_id;
      v_sitting_id := i.sitting_id;
      v_list_id := i.list_id;

      --dbms_output.put_line('DELETE XHB_SITTING : v_court_id : '||l_court_id||', v_list_id : '||v_list_id||', v_sitting_on_list_id : '||v_sitting_id);
    BEGIN
       -- delete from both XHB and AUD tables
        DELETE FROM XHB_SITTING WHERE SITTING_ID = i.sitting_id;
        DELETE FROM AUD_SITTING WHERE SITTING_ID = i.sitting_id;

    EXCEPTION  WHEN OTHERS THEN

       l_err_message := 'XHB_HK_LIST_DELETE_SITTING: sitting_id: '||v_sitting_id||' - '||substr(SQLERRM,1,500);
          -- Rollback transaction and record error
          ROLLBACK;

        BEGIN

          -- this will log an error
          INSERT INTO xhb_hk_error_log
          (hk_run_id
          ,court_id
          ,error_message
          )
          VALUES
          (l_hk_run_id
          ,l_court_id
          ,l_err_message
          );

           COMMIT;
         END;
      END;

      COMMIT; -- Commit the sitting deletion

      v_no_of_sittings := v_no_of_sittings + 1;

   END LOOP;

  --  DELETE from XHB_HEARING
   FOR i in del_xhb_hearing_list
   LOOP
      l_court_id := i.court_id;
      v_list_id := i.list_id;

      --dbms_output.put_line('DELETE_XHB_HEARING_LITS: v_court_id : '||l_court_id||', v_list_id : '||v_list_id);
    BEGIN
       -- delete from both XHB and AUD tables
        DELETE FROM XHB_HEARING_LIST WHERE LIST_ID = i.list_id;
        DELETE FROM AUD_HEARING_LIST WHERE LIST_ID = i.list_id;

    EXCEPTION  WHEN OTHERS THEN

       l_err_message := 'XHB_HK_LIST_DELETE_HEARING_LIST: list_id: '||v_list_id||' - '||substr(SQLERRM,1,500);
          -- Rollback transaction and record error
          ROLLBACK;

        BEGIN

          -- this will log an error
          INSERT INTO xhb_hk_error_log
          (hk_run_id
          ,court_id
          ,error_message
          )
          VALUES
          (l_hk_run_id
          ,l_court_id
          ,l_err_message
          );

           COMMIT;
         END;
      END;

     COMMIT; -- Commit the sitting deletion

      v_no_of_lists := v_no_of_lists + 1;

   END LOOP;

       dbms_output.put_line('No of sitting recs deleted : '||v_no_of_sittings);
       dbms_output.put_line('No of hearing_list recs deleted : '||v_no_of_lists);
  END del_list_data_part1;

PROCEDURE del_list_data_part2 (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                         , p_upper_limit IN NUMBER DEFAULT 10000) IS
/*
   18/09/2019    S.Sethuraman    CTX-4499 : Delete records from XHB_LIST, XHB_SITTING_ON_LIST
*/
       l_hk_run_id      xhb_hk_results.hk_run_id%TYPE;
       l_err_message xhb_hk_results.error_message%TYPE;
       l_err_stage    varchar2(1000)  := NULL;
       l_run_start_date  date := SYSDATE;
       v_no_of_lists NUMBER := 0;
       v_no_of_sittings NUMBER := 0;
       v_hk_list_days NUMBER := 180;
       v_court_id xhb_case.court_id%type;
       l_court_id xhb_case.court_id%type;
       v_list_id xhb_list.list_id%type;
       v_sitting_on_list_id xhb_sitting_on_list.sitting_on_list_id%type;
       v_upper_limit NUMBER;

  CURSOR del_xhb_sitting_on_list IS
   SELECT xl.court_id,xl.list_id,xsl.sitting_on_list_id
     FROM xhb_sitting_on_list xsl,xhb_list xl
    WHERE xl.list_id = xsl.list_id AND
          xl.court_id = nvl(v_court_id,xl.court_id) AND
          TRUNC(SYSDATE)-TRUNC(xl.list_end_date) >= v_hk_list_days AND
          NOT EXISTS (SELECT 'X' from xhb_case_on_list xcol where xcol.list_id = xl.list_id and xcol.sitting_on_list_id = xsl.sitting_on_list_id) AND
          rownum <= v_upper_limit
    ORDER BY xl.court_id,xl.list_id,xsl.sitting_on_list_id ;

  CURSOR del_xhb_list IS
   SELECT xl.court_id,xl.list_id
     FROM xhb_list xl
    WHERE xl.court_id = nvl(v_court_id,xl.court_id) AND
          TRUNC(SYSDATE)-TRUNC(xl.list_end_date) >= v_hk_list_days AND
           NOT EXISTS (SELECT 'X' from xhb_case_on_list xcol where xcol.list_id = xl.list_id) AND
           NOT EXISTS (SELECT 'X' from xhb_sitting_on_list xsol where xsol.list_id = xl.list_id) AND
           rownum <= v_upper_limit
    ORDER BY xl.court_id,xl.list_id ;

  BEGIN

     BEGIN
       select decode(p_court_id,0,NULL,p_court_id),
              decode(p_upper_limit,0,10000,p_upper_limit)
        into v_court_id,v_upper_limit from dual;

      SELECT PROPERTY_VALUE INTO v_hk_list_days
         FROM XHB_CONFIG_PROP WHERE PROPERTY_NAME = 'XHB_HK_NO_OF_DAYS_LISTS_RETAINED_AFTER_CASE_DEALT';

     EXCEPTION WHEN OTHERS THEN
          v_hk_list_days := 180;
     END;

      -- get hk_run_id
      l_hk_run_id := get_next_hk_run_id();

      --dbms_output.enable(1000000);
      --dbms_output.put_line('Court_id,List_id,Sitting_on_list_id,Case_on_list_id');

      l_run_start_date := SYSDATE; -- record start time to log info in xhb_hk_results

   v_no_of_lists := 0;
   v_no_of_sittings := 0;

   --  DELETE from XHB_SITTING_ON_LIST
   FOR i in del_xhb_sitting_on_list
   LOOP
      l_court_id := i.court_id;
      v_sitting_on_list_id := i.sitting_on_list_id;
      v_list_id := i.list_id;

      --dbms_output.put_line('DELETE XHB_SITTING_ON_LIST : l_court_id : '||l_court_id||', v_list_id : '||v_list_id||', v_sitting_on_list_id : '||v_sitting_on_list_id);
    BEGIN
       -- delete from both XHB and AUD tables
        DELETE FROM XHB_SITTING_ON_LIST WHERE SITTING_ON_LIST_ID = i.sitting_on_list_id;
        DELETE FROM AUD_SITTING_ON_LIST WHERE SITTING_ON_LIST_ID = i.sitting_on_list_id;

    EXCEPTION  WHEN OTHERS THEN

       l_err_message := 'XHB_HK_LIST_DELETE_SITTING_ON_LIST: sitting_on_list_id: '||v_sitting_on_list_id||' - '||substr(SQLERRM,1,500);
          -- Rollback transaction and record error
          ROLLBACK;

        BEGIN

          -- this will log an error
          INSERT INTO xhb_hk_error_log
          (hk_run_id
          ,court_id
          ,error_message
          )
          VALUES
          (l_hk_run_id
          ,l_court_id
          ,l_err_message
          );

           COMMIT;
         END;
      END;

      COMMIT; -- Commit the sitting_on_list deletion

      v_no_of_sittings := v_no_of_sittings + 1;

   END LOOP;

  --  DELETE from XHB_LIST
   FOR i in del_xhb_list
   LOOP
      l_court_id := i.court_id;
      v_list_id := i.list_id;

      dbms_output.put_line('DELETE_XHB_LIST: v_court_id : '||l_court_id||','||v_list_id);
    BEGIN
       -- delete from both XHB and AUD tables
        DELETE FROM XHB_LIST WHERE court_id = l_court_id AND LIST_ID = i.list_id;
        DELETE FROM AUD_LIST WHERE court_id = l_court_id AND LIST_ID = i.list_id;

    EXCEPTION  WHEN OTHERS THEN

       l_err_message := 'XHB_HK_LIST_DELETE_LIST: list_id: '||v_list_id||' - '||substr(SQLERRM,1,500);
          -- Rollback transaction and record error
          ROLLBACK;

        BEGIN

          -- this will log an error
          INSERT INTO xhb_hk_error_log
          (hk_run_id
          ,court_id
          ,error_message
          )
          VALUES
          (l_hk_run_id
          ,l_court_id
          ,l_err_message
          );

           COMMIT;
         END;
      END;

     COMMIT; -- Commit the sitting deletion

      v_no_of_lists := v_no_of_lists + 1;

   END LOOP;

       dbms_output.put_line('No of sitting_on_list recs deleted : '||v_no_of_sittings);
       dbms_output.put_line('No of list recs deleted : '||v_no_of_lists);
  END del_list_data_part2;

PROCEDURE del_courtel_list_data (p_upper_limit IN NUMBER DEFAULT 10000,
                                 p_courtel_period IN NUMBER DEFAULT 180) IS
/*
   19/09/2019    S.Sethuraman    CTX-4500 : Delete records from XHB_COURTEL_LIST
*/
       l_hk_run_id      xhb_hk_results.hk_run_id%TYPE;
       l_err_message xhb_hk_results.error_message%TYPE;
       l_err_stage    varchar2(1000)  := NULL;
       l_run_start_date  date := SYSDATE;
       v_no_of_clists NUMBER := 0;
       v_hk_clist_days NUMBER := 180;
       v_clist_id xhb_list.list_id%type;
       v_upper_limit NUMBER;
       v_blob_id NUMBER;


  CURSOR del_xhb_courtel_list IS
   SELECT xcl.courtel_list_id,xcl.blob_id
     FROM xhb_courtel_list xcl
    WHERE TRUNC(SYSDATE)-TRUNC(xcl.last_update_date) >= v_hk_clist_days AND
          nvl(xcl.sent_to_courtel,'N') = 'Y' AND
           rownum <= v_upper_limit
    ORDER BY xcl.courtel_list_id ;

  BEGIN

     BEGIN
       select decode(p_upper_limit,0,10000,p_upper_limit)
        into v_upper_limit from dual;

      SELECT decode(p_courtel_period,0,PROPERTY_VALUE,p_courtel_period) INTO v_hk_clist_days
         FROM XHB_CONFIG_PROP WHERE PROPERTY_NAME = 'XHB_HK_NO_OF_DAYS_COURTEL_LISTS_RETAINED_AFTER_CASE_DEALT';

     EXCEPTION WHEN OTHERS THEN
          v_hk_clist_days := 180;
     END;

      -- get hk_run_id
      l_hk_run_id := get_next_hk_run_id();

      --dbms_output.enable(1000000);
      --dbms_output.put_line('Courtel_List_id,Blob_id');

      l_run_start_date := SYSDATE; -- record start time to log info in xhb_hk_results

   v_no_of_clists := 0;

  --  DELETE from XHB_COURTEL_LIST
   FOR i in del_xhb_courtel_list
   LOOP
      v_clist_id := i.courtel_list_id;
      v_blob_id := i.blob_id;
      --dbms_output.put_line('DELETE_XHB_COURTEL_LIST: courtel_list_id ,'||v_clist_id||' , blob_id : '||v_blob_id);
    BEGIN
       -- delete from both XHB and AUD tables
        DELETE FROM XHB_COURTEL_LIST WHERE COURTEL_LIST_ID = i.courtel_list_id;
        DELETE FROM AUD_COURTEL_LIST WHERE COURTEL_LIST_ID = i.courtel_list_id;
        DELETE FROM XHB_BLOB WHERE BLOB_ID = i.blob_id;

    EXCEPTION  WHEN OTHERS THEN

       l_err_message := 'XHB_HK_LIST_DELETE_COURTEL_LIST: courtel_list_id: '||v_clist_id||' , blob_id : '||v_blob_id||SQLERRM;
          -- Rollback transaction and record error
          ROLLBACK;

        BEGIN

          -- this will log an error
          INSERT INTO xhb_hk_error_log
          (hk_run_id
          ,error_message
          )
          VALUES
          (l_hk_run_id
          ,l_err_message
          );

           COMMIT;
         END;
      END;

     COMMIT; -- Commit the courtel_list deletion

      v_no_of_clists := v_no_of_clists + 1;

   END LOOP;

       dbms_output.put_line('No of list Courtel recs deleted : '||v_no_of_clists);
  END del_courtel_list_data;

PROCEDURE housekeep_running_list (p_upper_limit IN NUMBER DEFAULT 10000,
                                  p_running_obs_period IN NUMBER DEFAULT 180,
                                  p_running_del_period IN NUMBER DEFAULT 730) IS
/*
   19/09/2019    S.Sethuraman    CTX-4501 : Delete records from XHB_PUB_RUNNING_LIST
*/
       l_hk_run_id      xhb_hk_results.hk_run_id%TYPE;
       l_err_message xhb_hk_results.error_message%TYPE;
       l_err_stage    varchar2(1000)  := NULL;
       l_run_start_date  date := SYSDATE;
       v_no_of_plists NUMBER := 0;
       v_no_of_del_plists NUMBER := 0;
       v_hk_plist_obs_days NUMBER := 180;
       v_hk_plist_del_days NUMBER := 730;
       v_plist_id xhb_pub_running_list.pub_running_list_id%type;
       v_upper_limit NUMBER;
       v_court_id NUMBER;


  CURSOR obs_xhb_plist IS
   SELECT xpl.court_id,xpl.pub_running_list_id,xpl.published_date
     FROM xhb_pub_running_list xpl
    WHERE TRUNC(SYSDATE)-TRUNC(xpl.published_date) >= v_hk_plist_obs_days AND
           NVL(xpl.obs_ind,'N') != 'Y' AND
           rownum <= v_upper_limit
    ORDER BY xpl.court_id,xpl.pub_running_list_id ;

  CURSOR del_xhb_plist IS
    SELECT xpl.court_id,xpl.pub_running_list_id,xpl.published_date
      FROM xhb_pub_running_list xpl
     WHERE TRUNC(SYSDATE)-TRUNC(xpl.published_date) >= v_hk_plist_del_days AND
           NVL(xpl.obs_ind,'N') = 'Y' AND
           rownum <= v_upper_limit
     ORDER BY xpl.court_id,xpl.pub_running_list_id;

  BEGIN

     BEGIN
       select decode(p_upper_limit,0,10000,p_upper_limit)
        into v_upper_limit from dual;

      SELECT decode(p_running_obs_period,0,PROPERTY_VALUE,p_running_obs_period) INTO v_hk_plist_obs_days
         FROM XHB_CONFIG_PROP WHERE PROPERTY_NAME = 'XHB_HK_NO_OF_DAYS_BEFORE_LIST_MARKED_AS_OBSOLETE';

      SELECT decode(p_running_del_period,0,PROPERTY_VALUE,p_running_del_period) INTO v_hk_plist_del_days
         FROM XHB_CONFIG_PROP WHERE PROPERTY_NAME = 'XHB_HK_NO_OF_DAYS_BEFORE_A_LIST_IS_DELETED';

     EXCEPTION WHEN OTHERS THEN
          v_hk_plist_obs_days := 180;
          v_hk_plist_del_days := 730;
     END;

      -- get hk_run_id
      l_hk_run_id := get_next_hk_run_id();

      --dbms_output.enable(1000000);
      --dbms_output.put_line('Courtel_List_id,Blob_id');

      l_run_start_date := SYSDATE; -- record start time to log info in xhb_hk_results

   v_no_of_plists := 0;
   v_no_of_del_plists := 0;
  --  OBSOLETE PUB_RUNNING_LIST
   FOR i in obs_xhb_plist
   LOOP
      v_court_id := i.court_id;
      v_plist_id := i.pub_running_list_id;
      --dbms_output.put_line('OBSOLETE_XHB_PUB_RUNNING_LIST: court_id - '||v_court_id||', pub_running_list_id - '||v_plist_id);
    BEGIN
       -- Mark as OBSOLETE
        UPDATE XHB_PUB_RUNNING_LIST SET OBS_IND = 'Y'
        WHERE court_id = i.court_id AND
              pub_running_list_id = i.pub_running_list_id;

    EXCEPTION  WHEN OTHERS THEN

       l_err_message := 'OBSOLETE_XHB_PUB_RUNNING_LIST: court_id - '||v_court_id||', pub_running_list_id - '||v_plist_id||SQLERRM;
          -- Rollback transaction and record error
          ROLLBACK;

        BEGIN

          -- this will log an error
          INSERT INTO xhb_hk_error_log
          (hk_run_id
          ,error_message
          )
          VALUES
          (l_hk_run_id
          ,l_err_message
          );

           COMMIT;
         END;
      END;

     COMMIT; -- Commit the pub_running_list changes

      v_no_of_plists := v_no_of_plists + 1;

   END LOOP;

   v_no_of_del_plists := 0;
  --  DELETE PUB_RUNNING_LIST
   FOR i in del_xhb_plist
   LOOP
      v_court_id := i.court_id;
      v_plist_id := i.pub_running_list_id;
      --dbms_output.put_line('DELETE_XHB_PUB_RUNNING_LIST: court_id - '||v_court_id||', pub_running_list_id - '||v_plist_id);
    BEGIN
       -- Mark as OBSOLETE
        DELETE FROM XHB_PUB_RUNNING_LIST
        WHERE court_id = i.court_id AND
              pub_running_list_id = i.pub_running_list_id AND
              obs_ind = 'Y';

    EXCEPTION  WHEN OTHERS THEN

       l_err_message := 'DELETE_XHB_PUB_RUNNING_LIST: court_id - '||v_court_id||', pub_running_list_id - '||v_plist_id||SQLERRM;
          -- Rollback transaction and record error
          ROLLBACK;

        BEGIN

          -- this will log an error
          INSERT INTO xhb_hk_error_log
          (hk_run_id
          ,error_message
          )
          VALUES
          (l_hk_run_id
          ,l_err_message
          );

           COMMIT;
         END;
      END;

     COMMIT; -- Commit the pub_running_list changes

      v_no_of_del_plists := v_no_of_del_plists + 1;

   END LOOP;

       dbms_output.put_line('No of Pub Running Lists Obsoleted : '||v_no_of_plists);
       dbms_output.put_line('No of Pub Running Lists Deleted : '||v_no_of_del_plists);
  END housekeep_running_list;

  PROCEDURE process_cpp_formatting(p_MS_days IN NUMBER DEFAULT 7,
								   p_MF_days IN NUMBER DEFAULT 30) IS

    PROCEDURE delete_record(p_table_name IN VARCHAR2, p_where_clause IN VARCHAR2) IS
    BEGIN
       l_error_point := 'process_cpp_formatting' || ' ' || p_table_name || ' ';
       EXECUTE IMMEDIATE 'DELETE '||Upper(p_table_name)||' WHERE '||p_where_clause;
    END delete_record;

    PROCEDURE delete_cpp_publicdisplay(p_no_of_days IN NUMBER, p_status IN xhb_cpp_formatting.format_status%TYPE) IS
    BEGIN
      FOR rec IN (SELECT xcf.cpp_formatting_id, xcf.xml_document_clob_id
                    FROM xhb_cpp_formatting xcf
                   WHERE xcf.document_type = 'PD'
                     AND xcf.last_update_date < TRUNC(SYSDATE) - p_no_of_days
                     AND xcf.format_status = p_status)  LOOP
          BEGIN
            delete_record(p_table_name => 'XHB_CPP_FORMATTING', p_where_clause => 'CPP_FORMATTING_ID = '||TO_CHAR(rec.cpp_formatting_id));            
            delete_record(p_table_name => 'AUD_CPP_FORMATTING', p_where_clause => 'CPP_FORMATTING_ID = '||TO_CHAR(rec.cpp_formatting_id));
            IF rec.xml_document_clob_id IS NOT NULL THEN 
			   -- Try and delete the CLOBs although they may be referenced by XHB_CPP_STAGING_INBOUND so if that happens then leave the CLOB alone, it will
			   -- be cleaned up when XHB_CPP_STAGING_INBOUND is processed by housekeeping.
			   BEGIN
				   delete_record(p_table_name => 'XHB_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.xml_document_clob_id));    
				   delete_record(p_table_name => 'AUD_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.xml_document_clob_id)); 
			   EXCEPTION
					WHEN OTHERS THEN NULL;
			   END;			   
            END IF;
            l_hk_cpp_run_results.PD_deleted := NVL(l_hk_cpp_run_results.PD_deleted,0) + 1;
            COMMIT;
          EXCEPTION WHEN OTHERS THEN
            ROLLBACK;
            l_hk_cpp_run_results.PD_error := NVL(l_hk_cpp_run_results.PD_error,0) + 1;
            l_hk_cpp_run_results.PD_error_message := SUBSTR(l_error_point||' '||SQLERRM, 1,500);
            log_cpp_hk_error(p_hk_cpp_run_id => l_hk_cpp_run_id, p_error_message => l_hk_cpp_run_results.PD_error_message);
          END;
      END LOOP;
    END delete_cpp_publicdisplay;
	
	PROCEDURE delete_cpp_iwp(p_no_of_days IN NUMBER, p_status IN xhb_cpp_formatting.format_status%TYPE) IS
    BEGIN
      FOR rec IN (SELECT xcf.cpp_formatting_id, xcf.xml_document_clob_id
                    FROM xhb_cpp_formatting xcf
                   WHERE xcf.document_type = 'IWP'
                     AND xcf.last_update_date < TRUNC(SYSDATE) - p_no_of_days
                     AND xcf.format_status = p_status)  LOOP
          BEGIN
            -- Delete the XHB_CPP_FORMATTING_MERGE records and any associated XHB_CLOB records
            FOR merge_rec IN (SELECT xcfm.cpp_formatting_merge_id, xcfm.xhibit_clob_id
                    FROM xhb_cpp_formatting_merge xcfm
                    WHERE xcfm.cpp_formatting_id = rec.cpp_formatting_id)  LOOP
               delete_record(p_table_name => 'XHB_CPP_FORMATTING_MERGE', p_where_clause => 'CPP_FORMATTING_MERGE_ID = '||TO_CHAR(merge_rec.cpp_formatting_merge_id));
               delete_record(p_table_name => 'AUD_CPP_FORMATTING_MERGE', p_where_clause => 'CPP_FORMATTING_MERGE_ID = '||TO_CHAR(merge_rec.cpp_formatting_merge_id));
               IF merge_rec.xhibit_clob_id IS NOT NULL THEN
                  -- Try and delete the CLOBs although they may be referenced by XHB_FORMATTING so if that happens then leave the CLOB alone, it will
				  -- be cleaned up when XHB_FORMATTING is processed by housekeeping.
				  BEGIN
					  delete_record(p_table_name => 'XHB_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(merge_rec.xhibit_clob_id));    
					  delete_record(p_table_name => 'AUD_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(merge_rec.xhibit_clob_id));
				  EXCEPTION
					WHEN OTHERS THEN NULL;
				  END;
               END IF;	 
            END LOOP;
		  
            -- Delete the XHB_CPP_FORMATTING records and any associated XHB_CLOB records		 
            delete_record(p_table_name => 'XHB_CPP_FORMATTING', p_where_clause => 'CPP_FORMATTING_ID = '||TO_CHAR(rec.cpp_formatting_id));            
            delete_record(p_table_name => 'AUD_CPP_FORMATTING', p_where_clause => 'CPP_FORMATTING_ID = '||TO_CHAR(rec.cpp_formatting_id));
            IF rec.xml_document_clob_id IS NOT NULL THEN 
			   -- Try and delete the CLOBs although they may be referenced by XHB_FORMATTING so if that happens then leave the CLOB alone, it will
			   -- be cleaned up when XHB_FORMATTING is processed by housekeeping.
			   BEGIN
					delete_record(p_table_name => 'XHB_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.xml_document_clob_id));    
					delete_record(p_table_name => 'AUD_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.xml_document_clob_id));
			   EXCEPTION
					WHEN OTHERS THEN NULL;
			   END;
            END IF;
            l_hk_cpp_run_results.IWP_deleted := NVL(l_hk_cpp_run_results.IWP_deleted,0) + 1;
            COMMIT;
          EXCEPTION WHEN OTHERS THEN
            ROLLBACK;
            l_hk_cpp_run_results.IWP_error := NVL(l_hk_cpp_run_results.IWP_error,0) + 1;
            l_hk_cpp_run_results.IWP_error_message := SUBSTR(l_error_point||' '||SQLERRM, 1,500);
            log_cpp_hk_error(p_hk_cpp_run_id => l_hk_cpp_run_id, p_error_message => l_hk_cpp_run_results.IWP_error_message);
          END;  
      END LOOP;
    END delete_cpp_iwp;

  BEGIN
    -- Start PD process
    l_hk_cpp_run_results.PD_status := 'I';
    l_hk_cpp_run_results.PD_error := 0;
    l_hk_cpp_run_results.PD_deleted := 0;
    update_hk_cpp_log(l_hk_cpp_run_results);
    
    delete_cpp_publicdisplay(p_no_of_days => p_MS_days, p_status => 'MS');
    delete_cpp_publicdisplay(p_no_of_days => p_MF_days, p_status => 'MF');
    delete_cpp_publicdisplay(p_no_of_days => p_MF_days, p_status => 'ND');

    -- End PD process and Start IWP process
    l_hk_cpp_run_results.PD_status := CASE WHEN NVL(l_hk_cpp_run_results.PD_error,0) > 0 THEN 'E' ELSE 'S' END;
    l_hk_cpp_run_results.IWP_status := 'I';
    l_hk_cpp_run_results.IWP_error := 0;
    l_hk_cpp_run_results.IWP_deleted := 0;
    update_hk_cpp_log(l_hk_cpp_run_results);
    
    delete_cpp_iwp(p_no_of_days => p_MS_days, p_status => 'MS');
    delete_cpp_iwp(p_no_of_days => p_MF_days, p_status => 'MF');
    delete_cpp_iwp(p_no_of_days => p_MF_days, p_status => 'ND');

    -- End IWP process
    l_hk_cpp_run_results.IWP_status := CASE WHEN NVL(l_hk_cpp_run_results.IWP_error,0) > 0 THEN 'E' ELSE 'S' END;
    update_hk_cpp_log(l_hk_cpp_run_results);
  END process_cpp_formatting;
  
  PROCEDURE process_cpp_staging(p_vs_no_of_days IN NUMBER DEFAULT 7,
								   p_other_no_of_days IN NUMBER DEFAULT 30) IS

    PROCEDURE delete_record(p_table_name IN VARCHAR2, p_where_clause IN VARCHAR2) IS
    BEGIN
       l_error_point := 'process_cpp_staging' || ' ' || p_table_name || ' ';
       EXECUTE IMMEDIATE 'DELETE '||Upper(p_table_name)||' WHERE '||p_where_clause;
    END delete_record;

    PROCEDURE delete_cpp_staging(p_vs_no_of_days IN NUMBER, p_other_no_of_days IN NUMBER) IS
    BEGIN
	  -- Delete the records with a validation status of 'VS'
      FOR rec IN (SELECT xcsi.cpp_staging_inbound_id, xcsi.clob_id
                    FROM xhb_cpp_staging_inbound xcsi
                   WHERE xcsi.validation_status = 'VS'
				     AND xcsi.acknowledgment_status = 'AS'
                     AND xcsi.time_loaded < TRUNC(SYSDATE) - p_vs_no_of_days
                     AND xcsi.cpp_staging_inbound_id NOT IN (SELECT xcp.staging_table_id FROM xhb_cpp_formatting xcp))  LOOP
          BEGIN
            delete_record(p_table_name => 'XHB_CPP_STAGING_INBOUND', p_where_clause => 'CPP_STAGING_INBOUND_ID = '||TO_CHAR(rec.cpp_staging_inbound_id));            
            delete_record(p_table_name => 'AUD_CPP_STAGING_INBOUND', p_where_clause => 'CPP_STAGING_INBOUND_ID = '||TO_CHAR(rec.cpp_staging_inbound_id));
            IF rec.clob_id IS NOT NULL THEN 
			   -- Try and delete the CLOBs although they may be referenced by XHB_FORMATTING so if that happens then leave the CLOB alone, it will
			   -- be cleaned up when XHB_FORMATTING is processed by housekeeping.
			   BEGIN
				   delete_record(p_table_name => 'XHB_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.clob_id));    
				   delete_record(p_table_name => 'AUD_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.clob_id)); 
			   EXCEPTION
					WHEN OTHERS THEN NULL;
			   END;			   
            END IF;
            l_hk_cpp_run_results.staging_deleted := NVL(l_hk_cpp_run_results.staging_deleted,0) + 1;
            COMMIT;
          EXCEPTION WHEN OTHERS THEN
            ROLLBACK;
            l_hk_cpp_run_results.staging_error := NVL(l_hk_cpp_run_results.staging_error,0) + 1;
            l_hk_cpp_run_results.staging_error_message := SUBSTR(l_error_point||' '||SQLERRM, 1,500);
            log_cpp_hk_error(p_hk_cpp_run_id => l_hk_cpp_run_id, p_error_message => l_hk_cpp_run_results.staging_error_message);
          END;
      END LOOP;
	  
	  -- Delete the other records
	  FOR rec IN (SELECT xcsi.cpp_staging_inbound_id, xcsi.clob_id
                    FROM xhb_cpp_staging_inbound xcsi
                   WHERE xcsi.acknowledgment_status = 'AS'
                     AND xcsi.time_loaded < TRUNC(SYSDATE) - p_other_no_of_days
                    AND xcsi.cpp_staging_inbound_id NOT IN (SELECT xcp.staging_table_id FROM xhb_cpp_formatting xcp))  LOOP
          BEGIN
            delete_record(p_table_name => 'XHB_CPP_STAGING_INBOUND', p_where_clause => 'CPP_STAGING_INBOUND_ID = '||TO_CHAR(rec.cpp_staging_inbound_id));            
            delete_record(p_table_name => 'AUD_CPP_STAGING_INBOUND', p_where_clause => 'CPP_STAGING_INBOUND_ID = '||TO_CHAR(rec.cpp_staging_inbound_id));
            IF rec.clob_id IS NOT NULL THEN 
			   -- Try and delete the CLOBs although they may be referenced by XHB_FORMATTING so if that happens then leave the CLOB alone, it will
			   -- be cleaned up when XHB_FORMATTING is processed by housekeeping.
			   BEGIN
				   delete_record(p_table_name => 'XHB_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.clob_id));    
				   delete_record(p_table_name => 'AUD_CLOB', p_where_clause => 'CLOB_ID = '||TO_CHAR(rec.clob_id)); 
			   EXCEPTION
					WHEN OTHERS THEN NULL;
			   END;			   
            END IF;
            l_hk_cpp_run_results.staging_deleted := NVL(l_hk_cpp_run_results.staging_deleted,0) + 1;
            COMMIT;
          EXCEPTION WHEN OTHERS THEN
            ROLLBACK;
            l_hk_cpp_run_results.staging_error := NVL(l_hk_cpp_run_results.staging_error,0) + 1;
            l_hk_cpp_run_results.staging_error_message := SUBSTR(l_error_point||' '||SQLERRM, 1,500);
            log_cpp_hk_error(p_hk_cpp_run_id => l_hk_cpp_run_id, p_error_message => l_hk_cpp_run_results.staging_error_message);
          END;
      END LOOP;
	  
    END delete_cpp_staging;

  BEGIN
    -- Start process
    l_hk_cpp_run_results.staging_status := 'I';
    l_hk_cpp_run_results.staging_error := 0;
    l_hk_cpp_run_results.staging_deleted := 0;
    update_hk_cpp_log(l_hk_cpp_run_results);
    
    delete_cpp_staging(p_vs_no_of_days => p_vs_no_of_days, p_other_no_of_days => p_other_no_of_days);
    
    -- End IWP process
    l_hk_cpp_run_results.staging_status := CASE WHEN NVL(l_hk_cpp_run_results.staging_error,0) > 0 THEN 'E' ELSE 'S' END;
    update_hk_cpp_log(l_hk_cpp_run_results);
  END process_cpp_staging;

  PROCEDURE process_cpp(p_MS_days IN NUMBER DEFAULT 7,
                        p_MF_days IN NUMBER DEFAULT 30) IS
  BEGIN        
    insert_hk_cpp_log;
    
    process_cpp_listing(p_MS_days => p_MS_days, p_MF_days => p_MF_days);
    process_cpp_formatting(p_MS_days => p_MS_days, p_MF_days => p_MF_days);
	  process_cpp_staging(p_vs_no_of_days => p_MS_days, p_other_no_of_days => p_MF_days);
	
	-- write errors to external file
    write_hk_cpp_error_log_file(l_hk_cpp_run_id);
    
    l_hk_cpp_run_results.run_end_date := SYSDATE;
    update_hk_cpp_log(l_hk_cpp_run_results);
	
	-- write metrics to external file
    write_hk_cpp_metrics_log_file(l_hk_cpp_run_id, p_MS_days, p_MF_days);
  END process_cpp;
  
  PROCEDURE generate_darts_report(p_start_date IN XHB_CASE.CRP_LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE - 1)
							   ,p_end_date IN XHB_CASE.CRP_LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE)) IS
    DELIMITER CONSTANT VARCHAR2(1) := ',';
    SPEECHMARKS CONSTANT VARCHAR2(1) := '"';
    DATE_FORMAT CONSTANT VARCHAR2(10) := 'DD/MM/YYYY';
    v_refcursor SYS_REFCURSOR;
    v_crp_last_update_date xhb_case.crp_last_update_date%TYPE;
	v_case_type xhb_case.case_type%TYPE;
	v_case_number xhb_case.case_number%TYPE;
	v_court_id xhb_case.court_id%TYPE;
	v_court_name xhb_court.court_name%TYPE;
	v_policy_no xhb_ref_dar_retention_policies.policy_no%TYPE;
	v_policy_description xhb_ref_dar_retention_policies.policy_description%TYPE;
	v_duration_days xhb_dar_retention_policy.duration_days%TYPE;
	v_duration_months xhb_dar_retention_policy.duration_months%TYPE;
	v_duration_years xhb_dar_retention_policy.duration_years%TYPE;
	v_has_life xhb_dar_retention_policy.has_life%TYPE;
		
  BEGIN
  
	IF (p_start_date > p_end_date) THEN
	  RAISE START_GREATER_END;
	ELSIF (p_end_date - p_start_date > 60) THEN
      RAISE DAYS_LIMIT_EXCEEDED;
	END IF;
	 
     -- Get the report data
    XHB_REPORT_PKG.get_darts_report(p_results_out => v_refcursor,
                                    p_start_date  => p_start_date,
                                    p_end_date    => p_end_date);
    
    DBMS_OUTPUT.PUT_LINE(SPEECHMARKS || 'Reconciliation Report from ' ||  TO_CHAR(p_start_date, DATE_FORMAT) || ' to ' || TO_CHAR(p_end_date, DATE_FORMAT) || SPEECHMARKS);
    DBMS_OUTPUT.PUT_LINE(SPEECHMARKS || 'Date' || SPEECHMARKS || DELIMITER || SPEECHMARKS || 'Case Number' || SPEECHMARKS || DELIMITER || 
                         SPEECHMARKS || 'Case Retention Period Applied' || SPEECHMARKS || DELIMITER || SPEECHMARKS || 'Case Total Sentence' || SPEECHMARKS || DELIMITER ||
                         SPEECHMARKS || 'Court' || SPEECHMARKS);
     -- Loop through the data
    LOOP
        FETCH v_refcursor INTO 
              v_crp_last_update_date,
              v_case_type,
              v_case_number,
              v_court_id,
              v_court_name,
              v_policy_no,
              v_policy_description,
              v_duration_days,
              v_duration_months,
              v_duration_years,
              v_has_life;
        EXIT WHEN v_refcursor%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(SPEECHMARKS || TO_CHAR(v_crp_last_update_date, DATE_FORMAT) || SPEECHMARKS || DELIMITER || SPEECHMARKS || v_case_type || v_case_number || SPEECHMARKS || DELIMITER || 
                             SPEECHMARKS || v_policy_no || '-' || v_policy_description || SPEECHMARKS || DELIMITER || SPEECHMARKS || v_duration_years || 'Y ' || v_duration_months || 'M ' || v_duration_days || 'D' || SPEECHMARKS || DELIMITER ||
                             SPEECHMARKS || v_court_name || SPEECHMARKS);
    END LOOP;
  
  END generate_darts_report;
  
  PROCEDURE generate_darts_report(p_start_date IN VARCHAR2
								 ,p_end_date IN VARCHAR2) IS 
								  
    DATE_FORMAT CONSTANT VARCHAR2(10) := 'DD/MM/YYYY';
	v_start_date DATE;
	v_end_date DATE;
	
  BEGIN
  
	IF p_start_date IS NOT NULL AND TRIM(p_start_date) != ' ' THEN
      v_start_date := TO_DATE(p_start_date, DATE_FORMAT);
	ELSE
	  v_start_date := TRUNC(SYSDATE) - 1;
	END IF;		
   
	IF p_end_date IS NOT NULL AND TRIM(p_end_date) != ' ' THEN
	  v_end_date := TO_DATE(p_end_date, DATE_FORMAT);
	ELSE
	  v_end_date := TRUNC(SYSDATE);
	END IF;
  
	generate_darts_report(v_start_date, v_end_date);
		
  END generate_darts_report;
								 
END xhb_housekeeping_pkg;
/
show errors