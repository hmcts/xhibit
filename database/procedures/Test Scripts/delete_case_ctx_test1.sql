/**
* CGI CREST to XHIBIT Program
*
* MODULE      : delete_case_ctx_test1
*
* DESCRIPTION : This script tests the delete_case_ctx stored procedure. 
* 				delete_case_ctx accepts 2 user-selectable parameters (court_id and week-ending (date)).
*
* PROCEDURE delete_case_ctx(p_case_id    IN  XHB_CASE.CASE_ID%TYPE 
*                        ,  p_del_reason IN  VARCHAR2)
*                          
*
*		This test creates a case and its subsidiary records, then calls delete_case_ctx
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 02/07/2018    J Riley         1.0         First Version
**/
SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found              VARCHAR2(1) := 'Y';
    v_row                NUMBER(8)   := 0;

    v_message_filename   VARCHAR2(100)   := 'delete_case_ctx_test1';
    
    v_system             VARCHAR2(100)   := NULL;
    v_directory          VARCHAR2(4000)  := NULL;
    v_database           VARCHAR2(100)   := NULL;
    v_slash              INTEGER;
    v_step               VARCHAR2(2) ;
    v_str_doc            VARCHAR2(30);
    v_user               VARCHAR2(10)    := 'd82015';
    v_case_id            NUMBER(8)       := 0;  
    v_case_number        NUMBER(8)       := 0;
    v_defendant_id       NUMBER(8)       := 0;
    v_case_pros_agy_id   NUMBER(8)       := 0;
    v_skeleton_id        NUMBER(8)       := 0;
    v_skeleton_day_id    NUMBER(8)       := 0;
    v_session_id         NUMBER(8)       := 0;
    v_def_on_case_id     NUMBER(8)       := 0;
    v_legal_aid_order_id NUMBER(8)       := 0;
    v_test_fail_msg      VARCHAR2(1000)   := ' ';
    v_bicester_mag_ct    NUMBER(8)       := 110517;
    v_count              NUMBER(8)       := 0;
    v_resultset          SYS_REFCURSOR   := NULL;  
	
    -- Parameters to pass to the procedure under test	
    v_court_id          NUMBER(8)      := 81;  
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 delete_case_ctx_test1 START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);
    
    v_step := '2';
    
    -- Insert data (to delete) in the following tables:
    -- XHB_CASE, XHB_DEFENDANT, XHB_DEFENDANT_ON_CASE, XHB_PROSECUTOR_REF_SOL_FIRM, XHB_WITNESS, XHB_SKELETON_SESSION, XHB_SKELETON_DAY, XHB_LEGAL_AID_AMENDMENT
    SELECT Max(case_number) + 1 
    Into   v_case_number 
    From   xhb_case ;
    
    v_step := '3';
    
    -- XHB_CASE (cols in CAPS are mandatory)
    INSERT INTO XHB_CASE (CASE_ID,              case_number,      case_type, mag_conviction_date, case_title, case_description,                           ref_court_id,      COURT_ID,   LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, VIDEO_LINK_REQUIRED )
                  VALUES (xhb_case_seq.nextval, v_case_number,    'T',       null,                'CTX-2227', 'Test case for delete_case_ctx (CTX-2227)', v_bicester_mag_ct, v_court_id, sysdate,          sysdate,       v_user  ,   v_user,          1,      'N'); 
 
    v_step := '4';
    SELECT case_id 
    INTO v_case_id 
    FROM XHB_CASE 
    where case_number = v_case_number
    and case_title = 'CTX-2227';
    
    v_step := '5';
   
    -- XHB_DEFENDANT 
    INSERT INTO XHB_DEFENDANT (DEFENDANT_ID,              crest_defendant_id,        first_name, middle_name, surname,  initials, date_of_birth, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, address_id, COURT_ID)
                       VALUES (xhb_defendant_seq.nextval, xhb_defendant_seq.nextval, 'Long',     'John',      'Silver', 'LJS',    '04-Jul-1958', sysdate,          sysdate,       v_user,     v_user,          1,       1979,       v_court_id);    
  
                       
--67
    v_step := '6';
     
    -- Now get the defendant_id we just created
    SELECT defendant_id 
    INTO   v_defendant_id 
    FROM   XHB_DEFENDANT 
    WHERE  created_by = v_user 
    AND    surname = 'Silver' 
    AND    creation_date > sysdate - 0.001;  -- created in the last 1.4 minss
    
    v_step := '7';
    
    -- XHB_DEFENDANT_ON_CASE (mandatory cols only.  Is dependant on XHB_CASE, XHB_DEFENDANT)
    INSERT INTO XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID,              CASE_ID,   DEFENDANT_ID,   LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) 
                               VALUES (XHB_DEFENDANT_ON_CASE_SEQ.nextval, v_case_id, v_defendant_id, sysdate,          sysdate,       v_user,     v_user,          1);
                               
    SELECT   DEFENDANT_ON_CASE_ID 
    INTO     v_def_on_case_id 
    FROM     XHB_DEFENDANT_ON_CASE 
    WHERE    CREATED_BY =v_user 
    AND      DEFENDANT_ID = v_defendant_id;
    
    v_step := '8';
    
    -- XHB_CASE_PROSECUTOR_AGENCY 
    INSERT INTO XHB_CASE_PROSECUTOR_AGENCY (CASE_PROS_AGENCY_ID,                   prosecutor_type, case_id,   REF_PROSECUTOR_AGENCY_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, respondent_status, obs_ind)
                                    VALUES (XHB_CASE_PROSECTOR_AGENCY_SEQ.nextval, null,            v_case_id, 25748,                    sysdate,          sysdate,       v_user,     v_user,          1,       null,              'N') ;
                        
    v_step := '9';
    
    SELECT  CASE_PROS_AGENCY_ID 
    INTO    v_case_pros_agy_id 
    FROM    XHB_CASE_PROSECUTOR_AGENCY  
    WHERE   REF_PROSECUTOR_AGENCY_ID = 25748 
    AND     CREATED_BY = 'd82015' 
    AND     CREATION_DATE > sysdate - 0.001;

    DBMS_OUTPUT.PUT_LINE('v_case_id = '|| v_case_id || ', v_case_number = ' || v_case_number ||', v_defendant_id = ' || v_defendant_id || ', v_case_pros_agy_id = ' || v_case_pros_agy_id);
    
    v_step := '10';
   
    -- XHB_PROSECUTOR_REF_SOL_FIRM (is dependant on XHB_CASE_PROSECUTOR_AGENCY)
    INSERT INTO XHB_PROSECUTOR_REF_SOL_FIRM (PROSECUTOR_REF_SOL_FIRM_ID,        ref_solicitor_firm_id, case_pros_agency_id, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, version, solicitor_ref, obs_ind)
                                      VALUES(XHB_PROS_REF_SOL_FIRM_SEQ.nextval, 91582,                 v_case_pros_agy_id,  sysdate,          sysdate,       v_user,     v_user,          1,       null,          'N');    
    v_step := '11';
    
    -- XHB_SKELETON_SCHEDULE
    INSERT INTO XHB_SKELETON_SCHEDULE (SKELETON_ID,                       LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, skeleton_delivery_status_id, DELIVERABLE, CASE_ID )
                               VALUES (XHB_SKELETON_SCHEDULE_SEQ.nextval, sysdate,          sysdate,       v_user,     v_user,          1,       null,                        'Y',         v_case_id) ;   
                               
    v_step := '12';
    
    SELECT   SKELETON_ID
    INTO     v_skeleton_id     
    FROM     XHB_SKELETON_SCHEDULE 
    WHERE    CREATED_BY = v_user 
    AND      CASE_ID =  v_case_id;
    
    v_step := '13';
    -- XHB_SKELETON_DAY (is dependent on XHB_SKELETON_SCHEDULE)
    INSERT INTO XHB_SKELETON_DAY (SKELETON_DAY_ID,              DAY_NUMBER, skeleton_date, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, WEEK_NUMBER, skeleton_id)   
                          VALUES (XHB_SKELETON_DAY_SEQ.nextval, 1,          null,          sysdate,          sysdate,       v_user,     v_user,          1,       0,           v_skeleton_id);
                     
    v_step := '14';
    
     SELECT SKELETON_DAY_ID 
     INTO   v_skeleton_day_id
     FROM   XHB_SKELETON_DAY 
     WHERE  CREATED_BY =  v_user
     AND    skeleton_id = v_skeleton_id;
     
     v_step := '15';
    
    -- XHB_SKELETON_SESSION (is dependant on XHB_SKELETON_DAY)
    INSERT INTO XHB_SKELETON_SESSION (SKELETON_SESSION_ID,              MORNING_OR_AFTERNOON, notes,      LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, SKELETON_DAY_ID,   skeleton_id,   obs_ind)
                              VALUES (XHB_SKELETON_SESSION_SEQ.nextval, 'M',                  'CTX-2227', sysdate,          sysdate,       v_user,     v_user,          1,       v_skeleton_day_id, v_skeleton_id, 'N');
    
    v_step := '16';
    
    SELECT SKELETON_SESSION_ID 
    INTO   v_session_id 
    FROM   XHB_SKELETON_SESSION
    WHERE  skeleton_id = v_skeleton_id 
    AND    CREATED_BY = v_user;
    
    v_step := '17';
    
    -- XHB_LEGAL_AID_AMENDMENT (need to create record in XHB_LEGAL_AID_ORDER in order to do this.  XHB_LEGAL_AID_ORDER is dependent on XHB_CASE_PROSECUTOR_AGENCY)
    INSERT INTO XHB_LEGAL_AID_ORDER(LEGAL_AID_ORDER_ID,              CREST_LEO_ID, defendant_on_case_id, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, obs_ind, case_pros_agency_id, order_date)
                             VALUES(XHB_LEGAL_AID_ORDER_SEQ.nextval, 99999,        v_def_on_case_id,     sysdate,          sysdate,       v_user,     v_user,          1,       'N',     v_case_pros_agy_id,  sysdate - 1 );
     v_step := '18';
                            
    SELECT LEGAL_AID_ORDER_ID 
    INTO   v_legal_aid_order_id 
    FROM   XHB_LEGAL_AID_ORDER 
    WHERE  defendant_on_case_id = v_def_on_case_id 
    AND    CREATED_BY = v_user;
    
    v_step := '19';
    
    -- AMENDMENT_TYPEs are listed in XHB_REF_SYSTEM_CODE where code_type = 'LEGAL_AID_AMENDMENT'                         
    INSERT INTO XHB_LEGAL_AID_AMENDMENT (LEGAL_AID_AMENDMENT_ID,              LEGAL_AID_ORDER_ID,   AMENDMENT_DATE, CREATED_BY, LAST_UPDATED_BY, CREATION_DATE, LAST_UPDATE_DATE, VERSION, obs_ind, AMENDMENT_TYPE)
                                 VALUES (XHB_LEGAL_AID_AMENDMENT_SEQ.nextval, v_legal_aid_order_id, sysdate - 1,    v_user,     v_user,          sysdate,       sysdate,          1,       'N',     'SOLC');
    
    v_step := '20';
    
    -- XHB_WITNESS  delete_case_ctx looks in xhb_skeleton_schedule for the session_id <- it did, I have changed it to delete using case_id.
    INSERT INTO XHB_WITNESS (WITNESS_ID,              NAME,              age,    WITNESS_TYPE, EXPECTED_ARRIVAL_TIME, SESSION_ID,   LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, CASE_ID)
                     VALUES (xhb_witness_seq.nextval, 'Fred Flintstone', 900,    'Defence',    sysdate + 1,           v_session_id, sysdate,          sysdate,       v_user,     v_user,          1,       v_case_id); 
    
    COMMIT;
    
    v_step := '21';
    
    -- Call the procedure we are testing
    XHB_HOUSEKEEPING_PKG.delete_case_ctx( p_case_id       =>  v_case_id 
                                        , p_del_reason    =>  'CTX-2227');

    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := '22';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
    v_step := '23';
    
  -- check the records we have created have been deleted.
  SELECT COUNT(LEGAL_AID_AMENDMENT_ID) 
  INTO   v_count 
  FROM   XHB_LEGAL_AID_AMENDMENT 
  WHERE  LEGAL_AID_ORDER_ID = v_legal_aid_order_id;
  
    v_step := '24';
    
  IF v_count > 0 THEN
      v_test_fail_msg := 'Table XHB_LEGAL_AID_AMENDMENT: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;

    v_step := '25';
    
  SELECT COUNT(LEGAL_AID_ORDER_ID) 
  INTO   v_count 
  FROM   XHB_LEGAL_AID_ORDER 
  WHERE  LEGAL_AID_ORDER_ID = v_legal_aid_order_id;
  
    v_step := '26';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_LEGAL_AID_ORDER: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;

    v_step := '27';
    
  SELECT   count(DEFENDANT_ON_CASE_ID)
  INTO     v_count 
  FROM     XHB_DEFENDANT_ON_CASE 
  WHERE    DEFENDANT_ON_CASE_ID = v_def_on_case_id 
  AND      DEFENDANT_ID = v_defendant_id;
 
    v_step := '28';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_DEFENDANT_ON_CASE: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;
  
    v_step := '29';
    
  SELECT   count(WITNESS_ID)
  INTO     v_count 
  FROM     XHB_WITNESS 
  WHERE    case_id = v_case_id;
 
    v_step := '30';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_WITNESS: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;
  
    v_step := '31';
    
  SELECT count(PROSECUTOR_REF_SOL_FIRM_ID) 
  INTO   v_count 
  FROM   XHB_PROSECUTOR_REF_SOL_FIRM 
  WHERE  case_pros_agency_id = v_case_pros_agy_id;
  
    v_step := '32';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_PROSECUTOR_REF_SOL_FIRM: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;
  
    v_step := '33';
    
  SELECT  count(CASE_PROS_AGENCY_ID)
  INTO    v_count 
  FROM    XHB_CASE_PROSECUTOR_AGENCY  
  WHERE   CASE_PROS_AGENCY_ID = v_case_pros_agy_id ;

    v_step := '34';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_CASE_PROSECUTOR_AGENCY: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;
  
    v_step := '35';
    
  SELECT count(SKELETON_ID)
  INTO   v_count 
  FROM   XHB_SKELETON_SCHEDULE
  WHERE  SKELETON_ID = v_skeleton_id;

    v_step := '36';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_SKELETON_SCHEDULE: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;
  
    v_step := '37';
    
  SELECT count(SKELETON_DAY_ID)
  INTO   v_count 
  FROM   XHB_SKELETON_DAY
  WHERE  SKELETON_ID = v_skeleton_id;

    v_step := '38';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_SKELETON_DAY: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;
  
    v_step := '39';
    
  SELECT count(SKELETON_SESSION_ID)
  INTO   v_count 
  FROM   XHB_SKELETON_SESSION
  WHERE  SKELETON_ID = v_skeleton_id;

    v_step := '40';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_SKELETON_SESSION: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;
  
    v_step := '41';
    
  SELECT count(case_id)
  INTO   v_count 
  FROM   XHB_CASE 
  WHERE  case_id = v_case_id;
 
    v_step := '42';
    
  IF v_count > 0 THEN
      v_test_fail_msg := v_test_fail_msg  || 'Table XHB_CASE: rows found = ' || v_count || ', expected 0' || chr(10);
  END IF;
      
    v_step := '43';
    
  IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
  ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
  END IF;   

--    DBMS_OUTPUT.PUT_LINE('output message file in utl_file_hold directory = ' || v_message_filename || chr(10));
 
    DBMS_OUTPUT.PUT_LINE('         --------- Test 1 delete_case_ctx_test1  END --------------------------' || chr(10));
       
    EXCEPTION 
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE(SUBSTR(SQLERRM, 1, 150 ));
            DBMS_OUTPUT.PUT_LINE('ERROR HAS OCCURRED !!!!!!!!!!!');         
            DBMS_OUTPUT.PUT_LINE('AT STEP ' || v_step);         
END;
/
SPOOL OFF

--select * from xhb_hk_error_log where case_id = 624711;

--select * from xhb_case where case_id = 670459;

--select * from xhb_hk_error_log order by hk_run_id desc;

