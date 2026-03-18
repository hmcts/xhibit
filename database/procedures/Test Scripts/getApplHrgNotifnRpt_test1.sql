/**
* CGI CREST to XHIBIT Program
*
* MODULE      : getApplHrgNotifnRpt_test1
*
* DESCRIPTION : This script tests the getAppealHearingNotifnRpt. (All listed Appeal hearings for a given court)
* 				getAppealHearingNotifnRpt accepts 1 user-selectable parameter (court_id).
*
*   PROCEDURE getAppealHearingNotifnRpt(p_court_id IN Number(8)
*                                     , p_resultset OUT SYS_REFCURSOR )
*
*
*		This test looks for Appeal hearings at court_id = 81 (Snaresbrook).
*
* Columns:      case_id, case_number, court_code, court_name, appellant_name, appellant_address, respondent_name, 
*               respondent_address, court_address1, court_postcode, appeal_type, clerk_to_justice, mag_conviction_date, 
*               list_start_date, time_listed, rpt_run_date 
*
*************************************************************************************

*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 11/05/2018    J Riley         1.0         First Version
* 21/08/2018    J Riley         1.1         Changed to use main version of package, not _JR and parms order reversed.
* 04-Oct-2018   J Riley         1.2         CTX-2703 Time_Listed is now a Varchar, rows expectred = 6.
**/
SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found             VARCHAR2(1) := 'Y';
    v_row               NUMBER(8)   := 0;

    v_message_filename  VARCHAR2(100)   := 'getApplHrgNotifnRpt_test1';
    
    v_system            VARCHAR2(100)   := NULL;
    v_directory         VARCHAR2(4000)  := NULL;
    v_database          VARCHAR2(100)   := NULL;
    v_slash             INTEGER;
    v_step              VARCHAR2(2) ;
    v_str_doc           VARCHAR2(30);
    v_test_fail_msg     VARCHAR2(500)   := ' ';
    v_resultset         SYS_REFCURSOR   := NULL;  
	
    -- Parameters to pass to the procedure under test	
    v_court_id          NUMBER(8)      := 81;  
    
    -- Variables to hold returned database
    v_case_id                Number(8);
    v_case_number            VARCHAR2(30);
    v_court_code             VARCHAR2(30);
    v_court_name             VARCHAR2(100) ;
    v_court_address          VARCHAR2(100) ; --line 28
    v_ct_phone_num           VARCHAR2(255 byte);
    v_appellant_id           NUMBER(8);
    v_appellant_name         VARCHAR2(100) ;
    v_appellant_address      VARCHAR2(100) ;
    v_respondent_name        VARCHAR2(100) ;
    v_respondent_address     VARCHAR2(100) ;
    v_appeal_type            VARCHAR2(30);
    v_clerk_to_justice       VARCHAR2(30);
    v_solicitor_firm_name    VARCHAR2(100) ;
    v_mag_conviction_date    DATE;
    v_list_start_date        DATE;
    v_time_listed            VARCHAR2(100) ;
    v_rpt_run_date           DATE;
    
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 getAppealHearingNotifnRpt START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT  dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO    v_system, v_database 
    FROM    dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
    -- Call the procedure we are testing
    XHB_REPORT_PKG.getAppealHearingNotifnRpt( p_resultset      =>  v_resultset
	                                        , p_court_id       =>  v_court_id );

    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := ' 3';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
  -- check message is correct

    v_step := ' 4';
    LOOP        
/*    select  cas.case_id, cas.case_number, ct.court_code, ct.court_name, 
            -- Appellant columns
            doc.defendant_number, df.first_name || ' ' || df.middle_name || ' ' || df.surname as appellant_name, 
            rsf.solicitor_firm_name, getAppellantAddress(doc.defendant_on_case_id)  defendant_address, 
             -- Respondent columns
            rpa.prosecutor_name_1 || ' ' || rpa.prosecutor_name_2 || ' ' || rpa.prosecutor_name_3 as respondent_name, 
            getAddress(rpa.address_id) as respondent_address, 
            -- Court columns 
            getAddress(ct.address_id) as court_address,  
            Replace(r.de_code, 'APPEAL AGAINST ') as appeal_type, 'Clerk to Justice' as clerk_to_justice,
            cas.mag_conviction_date, nvl(sol.time_listed, lst.list_start_date) as listing_date,   -- for CTX-2348, action 2
            to_char((case nvl(sol.time_marking_id, 0)                     -- added for CTX-2348  )
                     when 0 then TO_DATE(ct.court_start_time, 'HH24:MI')  -- added for CTX-2348  ) action 1
                     else sol.time_listed                                 -- added for CTX-2348  )
                     end),'HH24:MI') as time_listed,                      -- added for CTX-2348  )
            to_date(sysdate) as rpt_run_date, cdet.contact_value as ct_phone_no                                     */
        FETCH v_resultset INTO v_case_id, v_case_number, v_court_code, v_court_name, v_appellant_id, v_appellant_name, v_solicitor_firm_name, v_appellant_address, 
                               v_respondent_name, v_respondent_address, v_court_address, v_appeal_type, v_clerk_to_justice, v_mag_conviction_date,  
                               v_list_start_date, v_time_listed, v_rpt_run_date, v_ct_phone_num ;
        EXIT  WHEN v_resultset%NOTFOUND;
        
        v_row := v_row + 1;
        
        v_mag_conviction_date := NVL(v_mag_conviction_date, TO_DATE('01/01/1800', 'DD/MM/YYYY'));
        
        DBMS_OUTPUT.PUT_LINE('case_id, case_number, court_code, court_name, appellant_number, appellant_name, appellant_address');
        DBMS_OUTPUT.PUT_LINE(v_case_id|| '   '||v_case_number ||'     '||  v_court_code ||'        '|| v_court_name ||' '|| v_appellant_id || '        ' || v_appellant_name ||'    '|| v_appellant_address);
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE('respondent_name, respondent_address, court_address,                    , appeal_type,          clerk_to_justice');
        DBMS_OUTPUT.PUT_LINE(v_respondent_name||'             '|| v_respondent_address||'    ' || v_court_address||'         '|| v_appeal_type||' ' || v_clerk_to_justice);
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE('mag_ct_conviction_date, list_start_date, time_listed, report_run_date');
        DBMS_OUTPUT.PUT_LINE(v_mag_conviction_date ||'               '||v_list_start_date||'        '|| v_time_listed||'    '|| v_rpt_run_date);
        DBMS_OUTPUT.PUT_LINE(' ');
    END LOOP;
    
    CLOSE v_resultset;
        
    -- Check we have retrieved the data we expect to
    IF v_case_id != 620918 THEN 
        v_test_fail_msg := 'v_case_id: expected 620918, got ' || v_case_id || Chr(10);
    END IF;
    IF v_court_name != 'SNARESBROOK' THEN  
        v_test_fail_msg := v_test_fail_msg || 'v_court_name: expected SNARESBROOK, got ' || v_court_name || Chr(10);   -- Has to contain this tag
    END IF;
    IF v_row != 6 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Rows retrieved: expected 6, got ' || v_row  || Chr(10); 
    END IF;
    
    IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
    END IF;   

--    DBMS_OUTPUT.PUT_LINE('output message file in utl_file_hold directory = ' || v_message_filename || chr(10));
 
    DBMS_OUTPUT.PUT_LINE('         --------- Test 1 getAppealHearingNotifnRpt  END --------------------------' || chr(10));
    
    ROLLBACK;
    
    EXCEPTION 
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE(SUBSTR(SQLERRM, 1, 150 ));
            DBMS_OUTPUT.PUT_LINE('ERROR HAS OCCURRED !!!!!!!!!!!');         
            DBMS_OUTPUT.PUT_LINE('AT STEP ' || v_step);         
        
END;
/
SPOOL OFF

