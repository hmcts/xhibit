create or replace PACKAGE BODY "XHB_REPORT_PKG" AS
  /**
  * CGI CREST to XHIBIT Program
  *
  * MODULE      : XHB_REPORT_PKG
  *
  * DESCRIPTION : This package contains stored procedures for CTX reports
  *				It comprises:   
  *    Line     Procedure/function name
  *    ----     -----------------------
  *      67     PROCEDURE get_ctlrl_report
  *     113     PROCEDURE update_case_reminder_printed
  *     141     FUNCTION GET_DEFENDANTS_LIST
  *     189     FUNCTION get_bc_status_ind
  *     206     PROCEDURE get_defendants_put_back_report
  *     286     PROCEDURE get_docar_report
  *     320     PROCEDURE get_nfix_report
  *     430     PROCEDURE get_list_officers_diary_report
  *     482     PROCEDURE get_cfix_report
  *     571     PROCEDURE get_outc_report
  *     698     PROCEDURE get_unlc_report
  *     828     PROCEDURE get_drsr_report
  *     892     PROCEDURE get_relcj_report
  *     937     PROCEDURE get_inftrpc_main_report
  *    1688     PROCEDURE get_inftrpc_casenumbers_report
  *    1726     PROCEDURE get_list_of_fixed_dates_report
  *    1785     PROCEDURE update_case_diary_fix_run_date
  *    1803     PROCEDURE get_run_date
  *    1820     PROCEDURE get_prlis_report
  *    1881     PROCEDURE publish_running_listget_diary_notes_list
  *    1942     PROCEDURE get_ctlrp_report   
  *    1984     FUNCTION get_charge 
  *    2010     FUNCTION get_charges_info
  *    2036     FUNCTION GET_SOLICITOR_ID
  *    2063     FUNCTION GET_MOST_RECENT_HEARING
  *    2084     PROCEDURE UPDATE_REPORT_LOG
  *    2102     Function getAddress
  *    2126     Function getPhoneNum
  *    2152     Function getAppellantAddress
  *    2201     PROCEDURE getAppealHearingNotifnRpt
  *    2361     PROCEDURE get_lod_report_between_dates
  *    2388     PROCEDURE get_obw_report
  *    2456     PROCEDURE getRUMO_Rpt   
  *    2571     FUNCTION GET_EARLIEST_LISTING_DATE 
  *    2604     PROCEDURE get_RAGE_report
  *    2653     FUNCTION GET_BENCH_WARRANT_DATE
  *    2673     FUNCTION GET_LINKED_CASES_LIST    
  *    2703     FUNCTION GET_JUVENILE_STATUS
  *    2723     function get_offences
  *    2753     function get_diary_notes_list
  *    2790     function get_list_history
  *    2874     PROCEDURE getRJS_Rpt
  *    2967     Function getCaseType
  *    2993     function  getCaseSubHdg
  *    3411     FUNCTION is_valid_RREC_Case
  *    3449     FUNCTION is_dealtwith_RREC_Case
  *    3112     PROCEDURE get_RREC_detail
  *    3607     PROCEDURE get_RREC_summary
  *    3789     Function get_composite_casenum
  *    3845     Function get_trial_wait_time
  *    4079     PROCEDURE get_RRCA_detail
  *    4159     PROCEDURE get_RRCA_summary
  *    4326     PROCEDURE getCtRmSittingTimes
  *
  *
  **************************************************************************************/
  
 PROCEDURE get_ctlrl_report(p_results_out OUT SYS_REFCURSOR
                                ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                                ,p_screen_time_limit IN XHB_DEFENDANT_ON_CASE.CUSTODY_TIME_LIMIT%TYPE) AS
  
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
   BEGIN
     OPEN p_results_out FOR 
      SELECT
      get_prosecutor_name(xc.CASE_ID) AS prosecutorname,
      xc.CASE_ID AS caseid,
      xc.CASE_TYPE || xc.CASE_NUMBER AS casenumber,
      TO_CHAR(xdc.CUSTODY_TIME_LIMIT,'DD Month YYYY') AS custodytimelimit,
      UPPER(xd.SURNAME) || ' ' || INITCAP(xd.FIRST_NAME) AS defendantname,
      GET_DEFENDANTS_LIST(p_court_id,xc.CASE_ID,p_screen_time_limit) AS defendantnames,
      xdc.PTIURN AS urn,
      cdet.contact_value AS courttelephoneno,
      xa1.ADDRESS_1 || '  ' || xa1.TOWN || '  ' || xa1.COUNTY || ' ' || xa1.POSTCODE AS courtaddress,
      CASE WHEN xrpa.DX_REF IS NOT NULL 
           THEN xrpa.DX_REF 
           ELSE xa2.ADDRESS_1 || chr(10) || xa2.TOWN || chr(10) || xa2.COUNTY || chr(10) || xa2.POSTCODE END AS prosecutor_address,
      xrpa.REF_PROSECUTOR_AGENCY_ID as PROSECUTOR_ID
      FROM XHB_CASE xc
      INNER JOIN XHB_DEFENDANT_ON_CASE xdc ON xc.CASE_ID = xdc.CASE_ID AND (xdc.OBS_IND <> 'Y' OR xdc.OBS_IND IS NULL)
      INNER JOIN XHB_DEFENDANT xd ON xdc.DEFENDANT_ID = xd.DEFENDANT_ID
      INNER JOIN XHB_COURT xco ON xd.COURT_ID = xco.COURT_ID
      INNER JOIN XHB_CASE_PROSECUTOR_AGENCY xcpa ON xcpa.CASE_PROS_AGENCY_ID = get_prosecutor (xc.CASE_ID)
      INNER JOIN XHB_REF_PROSECUTOR_AGENCY xrpa ON xcpa.REF_PROSECUTOR_AGENCY_ID = xrpa.REF_PROSECUTOR_AGENCY_ID AND (xrpa.OBS_IND <> 'Y' OR xrpa.OBS_IND IS NULL)
      INNER JOIN XHB_ADDRESS xa1 ON xco.ADDRESS_ID = xa1.ADDRESS_ID
      INNER JOIN XHB_ADDRESS xa2 ON xrpa.ADDRESS_ID = xa2.ADDRESS_ID
      INNER JOIN XHB_CONTACT_DETAIL cdet ON (cdet.ADDRESS_ID = xa1.ADDRESS_ID AND cdet.contact_type = 'TEL')
      
      WHERE xc.RECEIPT_TYPE in ('CT','TC','VB','ST','IO','EW') AND xc.COURT_ID = p_court_id AND xc.DATE_TRANS_TO IS NULL
	  AND   xdc.CUSTODY_TIME_LIMIT BETWEEN TRUNC(sysdate) AND TRUNC(p_screen_time_limit)
	  AND (xdc.RESULTS_VERIFIED IS NULL OR xdc.RESULTS_VERIFIED <> 'E') AND xdc.DATE_EXPORTED IS NULL
      
      ORDER BY prosecutorname, xc.CASE_TYPE || xc.CASE_NUMBER, xdc.CUSTODY_TIME_LIMIT;
        
    UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE CREST_REPORT_CODE = 'CTLRL' AND COURT_ID = p_court_id;

     EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_CTLRL_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
        
 END get_ctlrl_report;
  
  PROCEDURE update_case_reminder_printed(p_cases IN CLOB) AS
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
v_offset number default 1;
v_chunk_size number := 1000;
v_chunk VARCHAR2(2000);
v_cases_list CLOB := p_cases || ',';

BEGIN
  
  LOOP
       EXIT WHEN v_offset >= dbms_lob.getlength(v_cases_list);
          v_chunk := REGEXP_SUBSTR(v_cases_list, '.{1,' || v_chunk_size  || '},', v_offset); --Get the next 1000 characters that ends with ,
          v_offset := v_offset +  LENGTH(v_chunk); 
          UPDATE XHB_CASE SET DATE_CTL_REMINDER_PRINTED  = sysdate 
          WHERE  ','||v_chunk||',' LIKE '%,'||CAST(CASE_ID AS VARCHAR(1000))||',%';
  END LOOP;
  
   EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.UPDATE_CASE_REMINDER_PRINTED '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;

END update_case_reminder_printed;
  
 FUNCTION GET_DEFENDANTS_LIST(p_court_id IN XHB_CASE.COURT_ID%TYPE,p_case_id IN XHB_CASE.CASE_ID%TYPE,
                                p_screen_time_limit IN XHB_DEFENDANT_ON_CASE.CUSTODY_TIME_LIMIT%TYPE) RETURN CLOB IS
defendants_list  CLOB := NULL;
pos int;

  CURSOR defendants_list_c IS
  SELECT  UPPER(xd.INITIALS) || ' ' || UPPER(xd.SURNAME) AS defendantname
  FROM XHB_CASE xc,XHB_DEFENDANT_ON_CASE xdc,XHB_DEFENDANT xd,XHB_COURT xco
  WHERE xc.CASE_ID = xdc.CASE_ID 
  AND xdc.DEFENDANT_ID = xd.DEFENDANT_ID AND (xdc.OBS_IND <> 'Y' OR xdc.OBS_IND IS NULL)
  AND xd.COURT_ID = xco.COURT_ID
  AND xc.CASE_ID = p_case_id
  AND xc.COURT_ID = p_court_id AND xdc.CUSTODY_TIME_LIMIT BETWEEN sysdate AND p_screen_time_limit 
  AND (xc.DATE_CTL_REMINDER_PRINTED > p_screen_time_limit OR xc.DATE_CTL_REMINDER_PRINTED IS NULL);
  
  BEGIN
 
  FOR defendants_list_row IN defendants_list_c
   LOOP
     defendants_list := defendants_list || defendants_list_row.defendantname || ', '; 
    END LOOP;
    
  pos:=INSTR(defendants_list,',',-1);
  
  defendants_list:=REGEXP_REPLACE(defendants_list,',','',pos,1);
    
  pos:=INSTR(defendants_list,',',-1);
  
  IF (pos>0) THEN
    defendants_list:=REGEXP_REPLACE(defendants_list,',',' AND',pos,1);
  END IF;
 
 RETURN defendants_list;
 
 EXCEPTION
   WHEN OTHERS THEN
		RETURN (SQLCODE||' '||'When others exception ' || SQLERRM);

END GET_DEFENDANTS_LIST;
  
  
/**********************************************************************************
*
* Procedure get_bc_status_ind  (CTX-2912)
*
* Returns 'C' for Custody cases, 'B' for Bail cases, 'N/A' otherwise
*
**************************************************************************************/
FUNCTION get_bc_status_ind(p_case_id IN XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE) 
     RETURN VARCHAR2 IS
	 
v_bc_status VARCHAR2(3);

BEGIN
  SELECT decode(min(DECODE(nvl(xdoc.CURRENT_BC_STATUS, 'N/A'), 'C', '1', 'J', '1', 'B', '2', 'N', '3', 'N/A', '3')), '1', 'C', '2', 'B', '3', 'N/A')
  INTO   v_bc_status
  FROM   XHB_DEFENDANT_ON_CASE xdoc
  WHERE  NVL(xdoc.OBS_IND, 'N') <> 'Y'
  AND    xdoc.CASE_ID = p_case_id
  AND    NVL(xdoc.CURRENT_BC_STATUS, 'N/A') IN ('C', 'J', 'B', 'N', 'N/A');
  
  RETURN v_bc_status;
END get_bc_status_ind;
  

/**********************************************************************************
*
* Procedure get_defendants_put_back_report  
*
* Returns data for the following reports:
*    ADJSS     Defendants Put Back For Sentence
*    DEFSS     Defendants With Deferred Sentences
*
**************************************************************************************/
  PROCEDURE get_defendants_put_back_report(p_results_out   OUT SYS_REFCURSOR  
                                         , p_court_id      IN  XHB_CASE.COURT_ID%TYPE
                                         , p_refSystemCode IN  VARCHAR
                                         , p_report_name   IN  VARCHAR2) AS
 
  v_err_code  NUMBER;
  v_err_msg   VARCHAR2(1000);
  v_rpt_name  VARCHAR2(50) := 'Defendants Put Back Report';
  
  BEGIN
      OPEN p_results_out FOR
	  select	xc.CASE_TYPE || xc.case_number as case_number
         ,      upper(xd.SURNAME)  || ' ' || xd.FIRST_NAME || ' ' || xd.MIDDLE_NAME AS defendant_name
         ,      xdhr.ADJOURNED_DATE
         ,      xdhr.HEARING_START_DATE AS HEARING_DATE
         ,      upper(xrj.SURNAME) || ' ' || xrj.FIRST_NAME || ' ' || xrj.MIDDLE_NAME AS judge_name
         ,      xdoc.PTIURN  
         ,      substr(xrsc.DE_CODE, case when instr(xrsc.DE_CODE, '-') > 0 then instr(xrsc.DE_CODE, '-')+1 else 0 end ) reason
         ,      xdoc.defendant_on_case_id
         ,      xdhr.HEARING_RECORD_ID
         ,      GET_EARLIEST_LISTING_DATE(xc.CASE_ID, xdoc.DEFENDANT_ON_CASE_ID) AS listing_date
	  from XHB_CASE xc
      ,    XHB_DEFENDANT_ON_CASE xdoc
      ,    XHB_DEFENDANT xd
      ,    XHB_HEARING xh
      ,    XHB_DEF_HEARING_RECORD xdhr
      ,    XHB_REF_SYSTEM_CODE xrsc
      ,    XHB_REF_JUDGE xrj
	  where xc.CASE_ID = xdoc.CASE_ID
	  and   xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
	  and   NVL(xdoc.OBS_IND, '-') <> 'Y'
	  and   xc.CASE_ID = xh.CASE_ID
	  and   xh.HEARING_ID (+) = xdhr.HEARING_ID 
      and   xdoc.DEFENDANT_ON_CASE_ID = xdhr.DEFENDANT_ON_CASE_ID
	  and   xdhr.HEARING_END_DATE IS NOT NULL
      and   xdhr.HEARING_END_DATE = (SELECT MAX(xdhr2.HEARING_END_DATE)
                                    FROM xhb_def_hearing_record xdhr2, XHB_REF_SYSTEM_CODE xrsc2
                                    WHERE xdhr2.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
									AND xdhr2.HEARING_END_DATE IS NOT NULL
                                    AND NVL(xdhr2.FORMA_STATUS,'-') = 'S'
									AND NVL(xdhr2.IS_ADJOURNED,'N') = 'Y'
									AND   xdhr2.REF_ADJOURNMENT_ID = xrsc2.REF_SYSTEM_CODE_ID
									AND   xrsc2.CODE_TYPE = 'PB_TYPE'
									AND   xrsc2.CODE_TITLE = 'ADJOURNMENT TYPE')
	  AND   NVL(xdhr.FORMA_STATUS,'-') = 'S'
      and   NVL(xdhr.IS_ADJOURNED,'N') = 'Y'
	  and   xdhr.REF_ADJOURNMENT_ID = xrsc.REF_SYSTEM_CODE_ID
	  and   xrsc.CODE_TYPE = 'PB_TYPE'
	  and   xrsc.CODE_TITLE = 'ADJOURNMENT TYPE'
	  and   xrsc.CODE IN (SELECT regexp_substr(p_refSystemCode, '[^,]+', 1, level) FROM DUAL
                          CONNECT BY regexp_substr(p_refSystemCode, '[^,]+', 1, level) IS NOT NULL)
    and   xrj.REF_JUDGE_ID = (select xsha.REF_JUDGE_ID from XHB_SCHED_HEARING_ATTENDEE xsha
                              where  xsha.SH_ATTENDEE_ID = (select max(xsha2.SH_ATTENDEE_ID)
                                                              from   XHB_SCHED_HEARING_ATTENDEE xsha2, XHB_SCHEDULED_HEARING xsh, XHB_REF_JUDGE xrj2
                                                              where  xsha2.REF_JUDGE_ID = xrj2.REF_JUDGE_ID
                                                              and    xsha2.SCHEDULED_HEARING_ID = xsh.SCHEDULED_HEARING_ID
                                                              and    xsh.HEARING_ID = xh.HEARING_ID
                                                              and    xsha2.ATTENDEE_TYPE in ('J', 'JP')))
    and xc.COURT_ID = p_court_id

	  ORDER BY LISTING_DATE
	  ,        xc.CASE_NUMBER
	  ,        xd.DEFENDANT_ID;  

	 -- Log that we have run this report
	UPDATE_REPORT_LOG(p_court_id, p_report_name, v_rpt_name);

    
     EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_DEFENDANTS_PUT_BACK_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;  
  END get_defendants_put_back_report;


  PROCEDURE get_docar_report(p_results_out OUT SYS_REFCURSOR,
                             p_papers_sent_date IN XHB_DEFENDANT_ON_CASE.FORM_NG_SENT_DATE%TYPE,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE) AS
  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
  BEGIN
  OPEN p_results_out FOR
    SELECT c.CASE_TYPE, c.CASE_NUMBER, dof.DEFENDANT_NUMBER, d.SURNAME, d.FIRST_NAME, d.MIDDLE_NAME, dof.FORM_NG_SENT_DATE
    FROM XHB_DEFENDANT_ON_CASE dof, XHB_CASE c, XHB_DEFENDANT d
    WHERE dof.CASE_ID = c.CASE_ID
    AND dof.DEFENDANT_ID = d.DEFENDANT_ID
    AND dof.FORM_NG_SENT_DATE IS NOT NULL
    AND dof.FORM_NG_SENT_DATE < p_papers_sent_date
    AND dof.CACD_APPEAL_RESULT_DATE IS NULL
	AND (dof.OBS_IND <> 'Y' OR dof.OBS_IND IS NULL)
    AND c.COURT_ID = p_court_id
    ORDER BY dof.FORM_NG_SENT_DATE, c.CASE_TYPE desc, c.CASE_NUMBER asc, dof.DEFENDANT_NUMBER;

     UPDATE_REPORT_LOG(p_court_id, 'DOCAR', 'Defendants with Outstanding Court of Appeal Results');

     EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_DOCAR_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
  END get_docar_report;

 
 /**********************************************************************************
*
* Procedure get_nfix_report  
*
* Notification of Fixture Report
*
**************************************************************************************/
PROCEDURE get_nfix_report(p_results_out OUT SYS_REFCURSOR
                        , p_court_id    IN XHB_CASE.COURT_ID%TYPE) AS

  -- Local variables here
  v_rpt_name  VARCHAR2(40)  := 'Notification of Fixture Report';
  v_proc_name VARCHAR2(30)  := 'get_nfix_report';
  v_rpt_code  VARCHAR2(6)   := 'NFIX';
  v_err_code  NUMBER;
  v_err_msg   VARCHAR2(1000);
  v_user      VARCHAR2(30);
  v_step      VARCHAR2(10);
  
 BEGIN
    v_step  :=' 1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;
    
    v_step  :=' 2';

  OPEN p_results_out FOR
	  SELECT CASE WHEN get_solicitor_id(xdoc.DEFENDANT_ON_CASE_ID) IS NULL 
                    THEN INITCAP(xd.first_name || ' ' || xd.middle_name || ' ' || xd.surname) --use defendant name
                  ELSE (SELECT INITCAP(SOLICITOR_FIRM_NAME) FROM xhb_ref_solicitor_firm xrsf
                        WHERE xrsf.ref_solicitor_firm_id = get_solicitor_id(xdoc.DEFENDANT_ON_CASE_ID)
                        AND  NVL(xrsf.OBS_IND, '-') <> 'Y')
             END  DEFENDANT_SOLICITOR_NAME
/*****************************solictor / defendant address******************************************************/       
,             CASE WHEN get_solicitor_id(xdoc.DEFENDANT_ON_CASE_ID) IS NULL 
                     THEN dxa.address_1 ||'|'||dxa.address_2||'|'||dxa.address_3||'|'||dxa.address_4 || '|' || dxa.postcode
                   ELSE (SELECT  CASE WHEN xrsf.DX_REF IS NOT NULL 
                                  THEN xrsf.DX_REF
                                  ELSE sxa.address_1 || '|' || sxa.address_2 || '|' || sxa.address_3 || '|' || sxa.address_4 || '|' || sxa.postcode END
                         FROM xhb_ref_solicitor_firm xrsf,
                              xhb_address sxa
                         WHERE xrsf.ref_solicitor_firm_id = get_solicitor_id(xdoc.DEFENDANT_ON_CASE_ID)
                         AND   xrsf.address_id = sxa.address_id)
              END DEFENDANT_SOLICITOR_ADDRESS
,             xdocrsf.SOLICITOR_REF as SOLICITOR_REFERENCE
,             trunc(xcdf.listing_date) AS FIXTURE_DATE
,             INITCAP(xcrt.court_name) AS COURT_NAME
,             INITCAP(cxa.address_1 || ' ' || cxa.address_2 || ' ' || cxa.address_3 || ' ' || cxa.address_4) || ' ' || UPPER(cxa.postcode) AS court_address
,             xrht.hearing_type_desc AS HEARING_CODE_DESCRIPTION 
,             xc.CASE_TYPE || xc.case_number AS CASE_NUMBER 
,             UPPER(xd.surname) || ' ' || INITCAP(xd.first_name) || ' ' || INITCAP(xd.middle_name) AS DEFENDANT_NAME
,             getPhoneNum(xcrt.address_id) AS COURT_TELEPHONE   --ctx-2963  changed to use getPhoneNum
,             xcdf.case_diary_fixture_id AS FIXTURE_ID
,             xcdf.LIST_NOTE_TEXT as LIST_NOTE
,             xrld.REF_DATA_VALUE AS PREDEFINED_LIST_NOTE
,             xdocrsf.REF_SOLICITOR_FIRM_ID AS SOLICITOR_ID
FROM xhb_case_listing_entry xcle
,    xhb_defendant_on_case xdoc
,    xhb_defendant xd
,    xhb_case xc
,    xhb_court xcrt
,    xhb_address cxa --court address
,    xhb_address dxa --defendant address
,    xhb_case_diary_fixture xcdf
,    xhb_ref_hearing_type xrht
,    xhb_fixture_deft_attending xfda
,     XHB_REF_LISTING_DATA xrld
,     XHB_DEF_ON_CASE_REF_SOL_FIRM xdocrsf
WHERE xcle.case_id = xdoc.case_id
AND   NVL(xdoc.OBS_IND, '-') <> 'Y'
AND   xdoc.defendant_id = xd.defendant_id
AND   trunc(xcdf.listing_date) > trunc(sysdate)
AND   xcle.case_id = xc.case_id
AND   xc.court_id = xcrt.court_id
and   xcrt.address_id = cxa.address_id
AND   xd.address_id = dxa.address_id
AND   xcle.case_listing_entry_id = xcdf.case_listing_entry_id
AND   xcdf.hearing_type_id = xrht.ref_hearing_type_id
AND   xc.court_id = p_court_id
AND   xfda.defendant_on_case_id = xdoc.defendant_on_case_id
AND   xc.CASE_TYPE <> 'A'
AND   NVL(xfda.OBS_IND, '-') <> 'Y'
AND   xfda.attending = 'Y'
AND   xfda.case_diary_fixture_id = xcdf.case_diary_fixture_id
AND   xcdf.fixture_notice_required = 'Y'
AND   xrld.REF_LISTING_DATA_ID(+) = xcdf.LIST_NOTE_PRE_DEFINED_ID
AND   xrld.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE'

AND   xdocrsf.DEFENDANT_ON_CASE_ID(+) = xdoc.DEFENDANT_ON_CASE_ID
AND   xdocrsf.REP_ST_DATE(+) <= sysdate
AND   NVL(xdocrsf.REP_END_DATE(+), sysdate+1) >= sysdate
AND   NVL(xdocrsf.OBS_IND(+),'N') <> 'Y'
ORDER BY CASE_NUMBER, FIXTURE_DATE, FIXTURE_ID, DEFENDANT_NAME;

  -- Record that we have run this report
  v_step  :=' 4';
  UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
                         
    EXCEPTION     
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' || v_proc_name || ', ' || v_err_code || ' : ' || SQLERRM;
            
            dbms_output.put_line(v_proc_name || ': ' || v_err_msg);

            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;  
   
END get_nfix_report;

PROCEDURE MARK_FIXTURES_AS_REPORTED(p_fixtures IN CLOB) AS
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
v_offset number default 1;
v_chunk_size number := 1000;
v_chunk VARCHAR2(2000);
v_fixtures_list CLOB := p_fixtures || ',';

BEGIN

-- We have to set the Fixture_Notice_Required. This prevents the letters being re-printed next time the report is run.
  LOOP
       EXIT WHEN v_offset >= dbms_lob.getlength(v_fixtures_list);
          v_chunk := REGEXP_SUBSTR(v_fixtures_list, '.{1,' || v_chunk_size  || '},', v_offset); --Get the next 1000 characters that ends with ,
          v_offset := v_offset +  LENGTH(v_chunk);
          UPDATE XHB_CASE_DIARY_FIXTURE SET FIXTURE_NOTICE_REQUIRED = 'N'
          WHERE  ','||v_chunk||',' LIKE '%,'||CAST(CASE_DIARY_FIXTURE_ID AS VARCHAR(1000))||',%';
  END LOOP;

   EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.MARK_FIXTURES_AS_REPORTED '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
END MARK_FIXTURES_AS_REPORTED;


 PROCEDURE get_list_officers_diary_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id            IN XHB_CASE.COURT_ID%TYPE
                          ,p_screen_date         IN XHB_DIARY_NOTE_ENTRY.DIARY_DATE%TYPE) AS

  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
    BEGIN
      OPEN p_results_out FOR
        SELECT ROWNUM as ROW_ID, CASE_NUMBER, CASE_TITLE, NULL as DIARY_NOTE_TEXT, NULL as CREATION_DATE FROM(   		 
        WITH RESULTS AS (select c.*, ROWNUM ROW_ID from 
       (SELECT  xc.CASE_TYPE || xc.CASE_NUMBER as CASE_NUMBER, xc.CASE_TITLE as case_title, xdne.DIARY_NOTE_TEXT as diary_note_text, xdne.CREATION_DATE 
        FROM XHB_DIARY_NOTE_ENTRY xdne
        LEFT JOIN XHB_CASE xc ON xdne.CASE_ID = xc.CASE_ID
        WHERE  xdne.diary_date = p_screen_date AND xdne.COURT_ID =  p_court_id AND (xdne.obs_ind <> 'Y' or xdne.obs_ind is null)
        ORDER BY DIARY_DATE, xc.CASE_TYPE desc, NVL(xc.CASE_NUMBER, 0) asc, CREATION_DATE) c)   -- Amended for ctx-2645
              
        SELECT CASE when a.case_number = b.case_number then ' ' else a.CASE_NUMBER end case_number, 
           CASE when a.case_number = b.case_number then ' ' else a.case_title end case_title, 
           a.diary_note_text, a.creation_date 
        FROM RESULTS a LEFT OUTER JOIN RESULTS b 
          ON a.ROW_ID = b.ROW_ID + 1 
          ORDER BY a.ROW_ID)
    
        UNION
    
       SELECT  ROWNUM as row_id, NULL as CASE_NUMBER, NULL as CASE_TITLE, DIARY_NOTE_TEXT,CREATION_DATE from(   		 
       WITH RESULTS AS (select c.*, ROWNUM ROW_ID from 
       ( SELECT  xc.CASE_TYPE || xc.CASE_NUMBER as CASE_NUMBER, xc.CASE_TITLE as case_title, xdne.DIARY_NOTE_TEXT as diary_note_text, xdne.CREATION_DATE 
        FROM XHB_DIARY_NOTE_ENTRY xdne
        LEFT JOIN XHB_CASE xc ON xdne.CASE_ID = xc.CASE_ID
        WHERE  xdne.diary_date = p_screen_date AND xdne.COURT_ID =  p_court_id AND (xdne.obs_ind <> 'Y' or xdne.obs_ind is null)
        ORDER BY DIARY_DATE, xc.CASE_TYPE desc, NVL(xc.CASE_NUMBER, 0) asc, CREATION_DATE) c)     -- Amended for ctx-2645
                
       SELECT CASE when a.case_number = b.case_number then ' ' else a.CASE_NUMBER end case_number, 
           CASE when a.case_number = b.case_number then ' ' else a.case_title end case_title, 
           a.diary_note_text, a.creation_date 
       FROM RESULTS a LEFT OUTER JOIN RESULTS b 
        ON a.ROW_ID = b.ROW_ID + 1 
        ORDER BY a.ROW_ID);
        
       UPDATE_REPORT_LOG(p_court_id, 'LODR', 'Listing Officers Diary');
    
 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_LIST_OFFICERS_DIARY_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;                  
  END get_list_officers_diary_report;

  
  PROCEDURE get_cfix_report(p_results_out OUT SYS_REFCURSOR,
						 p_court_id     IN XHB_CASE.COURT_ID%TYPE,
						 p_hearing_from_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE,
						 p_hearing_end_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE
                         ) AS
  
   v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);						 
  BEGIN
  OPEN p_results_out FOR
Select 
                xcdf.CASE_DIARY_FIXTURE_ID,
                xcdf.LISTING_DATE,
                to_char(xcdf.LISTING_DATE,'dd-mm-yyyy') as HEARING_DATE,
                xc.CASE_TYPE||xc.CASE_NUMBER as CASE_NUMBER,
                xc.CASE_TITLE,
                xc.CLASS_CODE,
                get_bc_status_ind(xc.CASE_ID) as BC_STATUS,
                xrht.HEARING_TYPE_CODE as HEARING_TYPE,
                xdfc.TRIAL_TIME_ESTIMATE||decode(xdfc.TRIAL_TIME_UNIT,'1','H','2','D','3','W','4','M') as EST,
                xrs.COURT_SITE_CODE as SITE,
                Highlight_Notes.DIARY_NOTE_TEXT as HIGHLIGHT_NOTE,
                Interpreter_Notes.DIARY_NOTE_TEXT as INTERPRETER_NOTE,
                xrf.SURNAME || '(' || xrf.JUDGE_TYPE || ')'  as REQUIRED_JUDGE,
                decode(xc.VIDEO_LINK_REQUIRED, 'Y', 'Video Link', 'Not Required') as VIDEO_LINK_REQUIRED,
                xcdf.LIST_NOTE_TEXT as LN,
                xrld.REF_DATA_VALUE AS PREDEFINEDLN   
from   XHB_CASE_LISTING_ENTRY xcle
     , XHB_CASE xc
     , XHB_CASE_DIARY_FIXTURE xcdf
     , XHB_REF_HEARING_TYPE xrht
     , XHB_DIRECTIONS_FOR_CASE xdfc 
     , XHB_COURT_SITE xrs
	 , XHB_REF_LISTING_DATA xrld
     , (SELECT xdne.CASE_LISTING_ENTRY_ID,
               nvl(xrld_pref.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) DIARY_NOTE_TEXT
        FROM   XHB_REF_LISTING_DATA xrld
            ,  XHB_DIARY_NOTE_ENTRY xdne
            ,  XHB_REF_LISTING_DATA xrld_pref      
        WHERE  xrld.REF_DATA_TYPE = 'NOTE_TYPE'
          and  xrld.REF_DATA_VALUE = 'IN'
          and  NVL(xdne.OBS_IND, '-') <> 'Y'
          and  xrld_pref.ref_listing_data_id(+) = xdne.DIARY_NOTE_PRE_DEFINED_ID
          and  xrld_pref.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE'
          and  xrld.REF_LISTING_DATA_ID = xdne.NOTE_TYPE_ID) Interpreter_Notes 
      ,(SELECT xdne.CASE_LISTING_ENTRY_ID, 
               nvl(xrld_pref.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) DIARY_NOTE_TEXT
        FROM XHB_REF_LISTING_DATA xrld
            ,XHB_DIARY_NOTE_ENTRY xdne
            ,XHB_REF_LISTING_DATA xrld_pref      
        WHERE xrld.REF_DATA_TYPE = 'NOTE_TYPE'
          and xrld.REF_DATA_VALUE = 'HN'
          and NVL(xdne.OBS_IND, '-') <> 'Y'
          and xrld_pref.ref_listing_data_id(+) = xdne.DIARY_NOTE_PRE_DEFINED_ID
          and xrld_pref.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE'
          and xrld.REF_LISTING_DATA_ID = xdne.NOTE_TYPE_ID) Highlight_Notes 
      ,XHB_REF_JUDGE xrf    
      where xc.CASE_ID = xcle.CASE_ID
      and xcdf.case_listing_entry_id = xcle.case_listing_entry_id
	  and xrld.REF_LISTING_DATA_ID(+) = xcdf.LIST_NOTE_PRE_DEFINED_ID
      and xrld.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE'
      and xcdf.VACATION_PRE_DEFINED_RSON_ID is NULL
      and xcdf.VACATION_FREETEXT_REASON is NULL       
      and xrht.REF_HEARING_TYPE_ID = xcdf.HEARING_TYPE_ID
      and xdfc.CASE_ID(+) =  xc.CASE_ID   -- CTX-3609 Make this join outer
      and xrs.COURT_ID = p_court_id
      and xrs.COURT_SITE_ID = xcdf.COURT_SITE_ID      
      and Highlight_Notes.CASE_LISTING_ENTRY_ID(+) = xcle.case_listing_entry_id   
      and Interpreter_Notes.CASE_LISTING_ENTRY_ID(+) = xcle.case_listing_entry_id   
      and trunc(xcdf.LISTING_DATE) BETWEEN trunc(p_hearing_from_date) and trunc(nvl(p_hearing_end_date, xcdf.LISTING_DATE))
      and xcle.JUDGE_ID = xrf.REF_JUDGE_ID(+) 
      and NVL(xcdf.OBS_IND, '-') <> 'Y'
      and NVL(xcle.OBS_IND, '-') <> 'Y' 
ORDER BY xcdf.LISTING_DATE,xc.CASE_TYPE DESC,xc.CASE_NUMBER ASC;

   	UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE COURT_ID = p_court_id AND CREST_REPORT_CODE = 'CFIX';

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_CFIX_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_cfix_report;

/**********************************************************************************
*
* Procedure get_outc_report
*
* Reports on outstanding cases using various selection criteria.
*
**************************************************************************************/
PROCEDURE get_outc_report(p_results_out OUT SYS_REFCURSOR,
                          p_court_id IN XHB_CASE.COURT_ID%TYPE,
						  p_CASE_TYPE IN VARCHAR2,
						  p_CASE_CLASS IN VARCHAR2,
						  p_BC_STATUS IN VARCHAR2,
						  p_DEFAULT_HEARING_TYPE IN xhb_ref_hearing_type.HEARING_TYPE_CODE%TYPE,
						  p_TIME_EST_FROM IN NUMBER,
						  p_TIME_EST_TO IN NUMBER,
						  p_UNITS IN NUMBER,
						  p_REQUIRED_JUDGE_TYPE IN xhb_case_listing_entry.REF_JUDGE_TYPE_ID%TYPE,
						  p_UNITS_WEEKS IN NUMBER,
						  p_SECURE_COURTROOM IN VARCHAR2,
                          p_JUVENILE_ONLY IN VARCHAR2,
						  p_PRIOITY_NOTES_Y_N IN VARCHAR2,
						  p_RESTRICTED_NOTES_Y_N IN VARCHAR2,
						  p_STANDARD_NOTES_Y_N IN VARCHAR2,
						  p_SORTBY IN VARCHAR2
						 ) AS

 -- Local variables here
v_rpt_name      VARCHAR2(40)  := 'Outstanding Cases By Various Criteria';
v_rpt_code      VARCHAR2(6)   := 'OUTC';
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);
v_step          VARCHAR2(10);
v_report_sql    VARCHAR2(10000);
v_where         VARCHAR2(4000);
v_count         NUMBER := 0;
 
BEGIN
  v_step := ' 1';
  v_report_sql := 
  
'    SELECT  notes.DIARY_NOTE_ENTRY_ID,
            xc.CASE_TYPE || xc.CASE_NUMBER as CASE_NUMBER,
          DECODE(xhb_report_pkg.get_bc_status_ind(xc.CASE_ID), ''C'', ''*'', ''B'', '' '', ''N/A'', '' '') as CUSTODY_CASE,
           xc.CASE_TITLE,
		   NVL((SELECT decode(MAX(xdoc.is_juvenile), ''Y'', ''Juv'', '' '') 
		         FROM xhb_defendant_on_case xdoc
                 WHERE xdoc.case_id = xc.case_id), '' '') AS Juvenile,
           to_char(NVL(NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE), xc.APPEAL_LODGED_DATE), ''dd-mm-yyyy'') as Commited_Sent,
	        xc.CLASS_CODE,
            xrht.HEARING_TYPE_CODE as HEARING_TYPE,
		    xrmc.monitoring_category_code,
            xdfc.TRIAL_TIME_ESTIMATE || decode(xdfc.TRIAL_TIME_UNIT, ''1'', ''H'', ''2'', ''D'', ''3'', ''W'', ''4'', ''M'') as LOEST,
		    xc.CASE_GROUP_NUMBER,
            to_char((select min(xcnad.start_date)
                     from xhb_case_non_avail_days xcnad
                     where  xcnad.CASE_ID = xc.case_id
                     group by xcnad.CASE_ID), ''dd-mm-yyyy'') as First_NAD,
              nvl((select ''Y''
                  from   xhb_case_on_list xconl
								WHERE xconl.case_id = xc.case_id
								AND ROWNUM = 1 AND NVL(xconl.obs_ind, ''N'') = ''N'' AND trunc (xconl.TIME_LISTED) > trunc(sysdate)),
					nvl((select ''Y''
								from    xhb_case_listing_entry xcle
                       ,xhb_case_diary_fixture xcdf
								where  xcle.case_id = xc.case_id
								and    xcle.case_listing_entry_id = xcdf.case_listing_entry_id
								and    NVL(xcdf.obs_ind, ''N'') = ''N'' AND ROWNUM = 1 AND trunc (xcdf.LISTING_DATE) >  trunc(sysdate)), '' '')) as Listed,
            notes.NOTE_CLASSIFICATION,
            notes.DIARY_NOTE_TEXT,
            notes.NOTE_DATE,
            notes.DIARY_DATE,
            notes.NOTE_TYPE NOTE_PREFIX,
            notes.NOTE_TYPE
    FROM    XHB_CASE xc
          , XHB_REF_HEARING_TYPE xrht
          , XHB_DIRECTIONS_FOR_CASE xdfc
          , XHB_REF_MONITORING_CATEGORY xrmc
		  , (SELECT NVL(xcle.CASE_ID,xdne.CASE_ID) as CASE_ID,
                    xdne.DIARY_NOTE_ENTRY_ID DIARY_NOTE_ENTRY_ID,
                    nvl(xrld_pref.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) DIARY_NOTE_TEXT,
                    xrld.REF_DATA_VALUE NOTE_CLASSIFICATION,
                    TO_CHAR(xdne.CREATION_DATE,''DD-Mon-YYYY'') NOTE_DATE,
                    CASE WHEN xrld_type.REF_DATA_VALUE = ''GDN'' THEN TO_CHAR(xdne.DIARY_DATE,''DD-Mon-YYYY'') ELSE NULL END DIARY_DATE,
                    xrld_type.REF_DATA_VALUE NOTE_TYPE
             FROM XHB_REF_LISTING_DATA xrld
                , XHB_REF_LISTING_DATA xrld_type
                , XHB_DIARY_NOTE_ENTRY xdne
                , XHB_REF_LISTING_DATA xrld_pref
                , XHB_CASE_LISTING_ENTRY xcle
             WHERE xrld.REF_DATA_TYPE = ''NOTE_CLASSIFICATION''
             AND   xrld_type.REF_DATA_TYPE = ''NOTE_TYPE''
             AND   xrld_type.REF_DATA_VALUE NOT IN (''DCN'')
             AND   xrld_type.REF_LISTING_DATA_ID = xdne.NOTE_TYPE_ID
             AND   xcle.CASE_LISTING_ENTRY_ID(+) = xdne.CASE_LISTING_ENTRY_ID
             AND   NVL(xdne.OBS_IND, ''-'') <> ''Y''
             AND   xrld_pref.ref_listing_data_id(+) = xdne.DIARY_NOTE_PRE_DEFINED_ID
             AND   xrld_pref.REF_DATA_TYPE(+) = ''PREDEFINED_LIST_NOTE''
             AND   xrld.REF_LISTING_DATA_ID = xdne.NOTE_CLASSIFICATION_ID
             AND ( CASE xrld.REF_DATA_VALUE 
                     WHEN ''Priority''   THEN ''' || p_PRIOITY_NOTES_Y_N  || '''' ||
	                 ' WHEN ''Restricted'' THEN ''' || p_RESTRICTED_NOTES_Y_N || '''' || 
                   ' WHEN ''Standard''   THEN ''' || p_STANDARD_NOTES_Y_N || '''' ||
                  ' END = ''Y'' OR ''HN'' = xrld_type.REF_DATA_VALUE ) 
            ) notes     
    WHERE   xc.DEFAULT_HEARING_TYPE = xrht.REF_HEARING_TYPE_ID(+)
    AND     xrmc.REF_MONITORING_CATEGORY_ID(+) = xc.MONITORING_CATEGORY_ID
    AND 	XHB_CASE_PKG.determine_case_status(xc.CASE_ID) =  ''Open'' 
    AND     xc.CASE_ID = xdfc.CASE_ID(+)  -- Added for CTX-2927 as the UNLC bug also applies to this report
    AND     xc.COURT_ID = ' || To_Char(p_court_id) || '
    AND     notes.CASE_ID(+) = xc.CASE_ID ';

	IF p_CASE_CLASS IS  NULL THEN 
	    v_where := ' ';
	ELSE	    
        v_where := 'AND     CASE ';
		IF INSTR(p_CASE_CLASS, '1') > 0 THEN 
	        v_where := v_where || ' WHEN xc.CLASS_CODE = 1 THEN ''Y''';	 
			v_count := v_count + 1;
		END IF;
		IF INSTR(p_CASE_CLASS, '2') > 0 THEN 
	        v_where := v_where || ' WHEN xc.CLASS_CODE = 2 THEN ''Y''';	 
			v_count := v_count + 1;
		END IF;
		IF INSTR(p_CASE_CLASS, '3') > 0 THEN 
	        v_where := v_where || ' WHEN xc.CLASS_CODE = 3 THEN ''Y''';	 
		    v_count := v_count + 1;
		END IF;
		-- At least one of the above needs to be true, or we don't include it.
		IF v_count = 0 THEN
	        v_where := ' ';
		ELSE
		    v_where := v_where || ' ELSE ''N'' END = ''Y''';
	    END IF;
	END IF;
	IF p_CASE_TYPE IS NOT NULL THEN 
	    v_where := v_where || '   AND      xc.CASE_TYPE = ''' || p_CASE_TYPE  || '''';
	ELSE
	    v_where := v_where || '   AND      xc.case_type IN (''T'',''S'',''A'')';
	END IF;
	IF p_BC_STATUS  IS NOT NULL THEN 
	    v_where := v_where || '   AND      XHB_REPORT_PKG.get_bc_status_ind(xc.CASE_ID) = ''' || p_BC_STATUS || '''';
	END IF;
	IF p_DEFAULT_HEARING_TYPE IS NOT NULL THEN 
	    v_where := v_where || '   AND      NVL(xrht.HEARING_TYPE_CODE, ''~'') = ''' || p_DEFAULT_HEARING_TYPE || '''' ;
	END IF;
	IF p_TIME_EST_FROM IS NOT NULL THEN
		IF p_TIME_EST_TO IS NULL THEN
			v_where := v_where || '   AND      xdfc.TRIAL_TIME_ESTIMATE BETWEEN ' || to_char(p_TIME_EST_FROM) || ' AND NULL ' || '
								  AND      xdfc.TRIAL_TIME_UNIT = ' || to_char(p_UNITS) ;
		ELSE
			v_where := v_where || '   AND      xdfc.TRIAL_TIME_ESTIMATE BETWEEN ' || to_char(p_TIME_EST_FROM) || ' AND ' || to_char(p_TIME_EST_TO) || '
								  AND      xdfc.TRIAL_TIME_UNIT = ' || to_char(p_UNITS) ;
		END IF;
	END IF;
    IF p_REQUIRED_JUDGE_TYPE IS NOT NULL THEN	
	    v_where := v_where || '   AND      exists ( select 1 from xhb_case_listing_entry where case_id = xc.case_id and ' || to_char(p_REQUIRED_JUDGE_TYPE) || ' = xhb_case_listing_entry.ref_judge_type_id) ';
    END IF;
	IF p_UNITS_WEEKS IS NOT NULL THEN 
	    v_where := v_where || '   AND      (TRUNC(sysdate) - ' || to_char(p_UNITS_WEEKS * 7) || ' >= TRUNC( NVL(NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE), xc.APPEAL_LODGED_DATE))) ';
    END IF;
	IF p_SECURE_COURTROOM  != 'N' THEN 
	    v_where := v_where || '   AND      xc.SECURE_COURT = ''' || p_SECURE_COURTROOM || '''';
    END IF;
	IF p_JUVENILE_ONLY != 'N' THEN 
	    v_where := v_where || '   AND      EXISTS (SELECT 1 FROM xhb_defendant_on_case xdoc WHERE xdoc.case_id = xc.case_id AND NVL(xdoc.is_juvenile, ''N'') = ''Y'' AND ROWNUM = 1 ) ';
    END IF;
    IF p_SORTBY =  'CASENUMBER' THEN 
	    v_where := v_where || '   ORDER BY xc.case_number, NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE)';
    ELSE
	    v_where := v_where || '	  ORDER BY NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE)';
	END IF;
    v_where := v_where || ',CASE notes.note_type WHEN ''HN'' THEN 1 WHEN ''IN'' THEN 2 ELSE 99 END, notes.NOTE_DATE';
 
	v_report_sql :=  v_report_sql || v_where;

	-- Now we have assembled the dynamic SQL, run it.
	OPEN p_results_out FOR v_report_sql;

 
    -- Now we have to log that we have run this report. 
  v_step := ' 2';
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);

    EXCEPTION
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' || v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;

            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;

END get_outc_report;

/**********************************************************************************
*
* Procedure get_unlc_report  
*
* Reports on unlisted cases using various selection criteria.
*
**************************************************************************************/
PROCEDURE get_unlc_report(p_results_out OUT SYS_REFCURSOR,
                          p_court_id IN XHB_CASE.COURT_ID%TYPE,
                          p_CASE_TYPE IN VARCHAR2,
                          p_CASE_CLASS IN VARCHAR2,
                          p_BC_STATUS IN VARCHAR2,
                          p_DEFAULT_HEARING_TYPE IN xhb_ref_hearing_type.HEARING_TYPE_CODE%TYPE,
                          p_TIME_EST_FROM IN NUMBER,
                          p_TIME_EST_TO IN NUMBER,
                          p_UNITS IN NUMBER,
                          p_REQUIRED_JUDGE_TYPE IN xhb_case_listing_entry.REF_JUDGE_TYPE_ID%TYPE,
                          p_UNITS_WEEKS IN NUMBER,
                          p_SECURE_COURTROOM IN VARCHAR2,
                          p_JUVENILE_ONLY IN VARCHAR2,
                          p_PRIOITY_NOTES_Y_N IN VARCHAR2,
                          p_RESTRICTED_NOTES_Y_N IN VARCHAR2,
                          p_STANDARD_NOTES_Y_N IN VARCHAR2,
                          p_SORTBY IN VARCHAR2				   						 
                         ) AS
						 
 -- Local variables here
v_rpt_name      VARCHAR2(40)  := 'Unlisted Cases By Various Criteria';
v_rpt_code      VARCHAR2(6)   := 'UNLC';
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);
v_step          VARCHAR2(10);
v_report_sql    VARCHAR2(10000);
v_where         VARCHAR2(4000);
v_count         NUMBER := 0;
 
BEGIN
  v_step := ' 1';
  
  v_report_sql := 
  
'    SELECT notes.DIARY_NOTE_ENTRY_ID,
           xc.CASE_TYPE || xc.CASE_NUMBER as CASE_NUMBER,
          DECODE(XHB_REPORT_PKG.get_bc_status_ind(xc.CASE_ID), ''C'', ''*'', ''B'', '' '', ''N/A'', '' '') as CUSTODY_CASE,
           xc.CASE_TITLE,
		   NVL((SELECT decode(MAX(xdoc.is_juvenile), ''Y'', ''Juv'', '' '') 
		        FROM   xhb_defendant_on_case xdoc
                WHERE  xdoc.case_id = xc.case_id), '' '') AS Juvenile,
           to_char(NVL(NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE), xc.APPEAL_LODGED_DATE), ''dd-mm-yyyy'') as Commited_Sent,
	       xc.CLASS_CODE,
           xrht.HEARING_TYPE_CODE as HEARING_TYPE,
		   xrmc.monitoring_category_code,
           xdfc.TRIAL_TIME_ESTIMATE || decode(xdfc.TRIAL_TIME_UNIT, ''1'', ''H'', ''2'', ''D'', ''3'', ''W'', ''4'', ''M'') as LOEST,
		   xc.CASE_GROUP_NUMBER,
           to_char((select min(xcnad.start_date)
                    from xhb_case_non_avail_days xcnad 
                    where  xcnad.CASE_ID = xc.case_id
                    group by xcnad.CASE_ID), ''dd-mm-yyyy'') as First_NAD,
           notes.NOTE_CLASSIFICATION,
           notes.DIARY_NOTE_TEXT,
           notes.NOTE_DATE,
           notes.DIARY_DATE,
           notes.NOTE_TYPE NOTE_PREFIX,
           notes.NOTE_TYPE
    FROM   XHB_CASE xc
         , XHB_REF_HEARING_TYPE xrht
         , XHB_DIRECTIONS_FOR_CASE xdfc
         , XHB_REF_MONITORING_CATEGORY xrmc
		 , (SELECT NVL(xcle.CASE_ID, xdne.CASE_ID) as CASE_ID,   -- This Select is being used as a virtual table called notes.
                   xdne.DIARY_NOTE_ENTRY_ID DIARY_NOTE_ENTRY_ID,
                   nvl(xrld_pref.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) DIARY_NOTE_TEXT,
                   xrld.REF_DATA_VALUE NOTE_CLASSIFICATION,
                   TO_CHAR(xdne.CREATION_DATE,''DD-Mon-YYYY'') NOTE_DATE,
                   CASE WHEN xrld_type.REF_DATA_VALUE = ''GDN'' THEN TO_CHAR(xdne.DIARY_DATE,''DD-Mon-YYYY'') ELSE NULL END DIARY_DATE,
                   xrld_type.REF_DATA_VALUE NOTE_TYPE
            FROM XHB_REF_LISTING_DATA xrld
               , XHB_REF_LISTING_DATA xrld_type
               , XHB_DIARY_NOTE_ENTRY xdne
               , XHB_REF_LISTING_DATA xrld_pref
               , XHB_CASE_LISTING_ENTRY xcle
            WHERE xrld.REF_DATA_TYPE = ''NOTE_CLASSIFICATION''
            AND xrld_type.REF_DATA_TYPE = ''NOTE_TYPE''
            AND xrld_type.REF_DATA_VALUE NOT IN (''DCN'')
            AND xrld_type.REF_LISTING_DATA_ID = xdne.NOTE_TYPE_ID
            AND xcle.CASE_LISTING_ENTRY_ID(+) = xdne.CASE_LISTING_ENTRY_ID 
            AND NVL(xdne.OBS_IND, ''-'') <> ''Y''
            AND xrld_pref.ref_listing_data_id(+) = xdne.DIARY_NOTE_PRE_DEFINED_ID
            AND xrld_pref.REF_DATA_TYPE(+) = ''PREDEFINED_LIST_NOTE''
            AND xrld.REF_LISTING_DATA_ID = xdne.NOTE_CLASSIFICATION_ID
            AND ( CASE xrld.REF_DATA_VALUE 
                     WHEN ''Priority''   THEN ''' || p_PRIOITY_NOTES_Y_N  || '''' ||
	                 ' WHEN ''Restricted'' THEN ''' || p_RESTRICTED_NOTES_Y_N || '''' || 
                   ' WHEN ''Standard''   THEN ''' || p_STANDARD_NOTES_Y_N || '''' ||
                  ' END = ''Y'' OR ''HN'' = xrld_type.REF_DATA_VALUE ) 
          ) notes   
    WHERE xc.DEFAULT_HEARING_TYPE = xrht.REF_HEARING_TYPE_ID(+)
    AND   xrmc.REF_MONITORING_CATEGORY_ID(+) = xc.MONITORING_CATEGORY_ID
    AND   XHB_REPORT_PKG.unlc_check_case_on_list(xc.CASE_ID) = ''Y''
    AND   XHB_CASE_PKG.determine_case_status(xc.CASE_ID) =  ''Open''
    AND	  XHB_REPORT_PKG.get_exported_defendants(xc.CASE_ID, xc.case_type, xc.case_sub_type) = ''N''
    AND   xc.CASE_ID = xdfc.CASE_ID(+)  -- CTX-2927 Made this an outer join as some cases do not have directions for case.
    AND     xc.COURT_ID = ' || To_Char(p_court_id) || '
    AND   notes.CASE_ID(+) = xc.CASE_ID';
 
 
	IF p_CASE_CLASS IS  NULL THEN 
	    v_where := ' ';
	ELSE	    
        v_where := ' AND     CASE ';
		IF INSTR(p_CASE_CLASS, '1') > 0 THEN 
	        v_where := v_where || ' WHEN xc.CLASS_CODE = 1 THEN ''Y''';	 
			v_count := v_count + 1;
		END IF;
		IF INSTR(p_CASE_CLASS, '2') > 0 THEN 
	        v_where := v_where || ' WHEN xc.CLASS_CODE = 2 THEN ''Y''';	 
			v_count := v_count + 1;
		END IF;
		IF INSTR(p_CASE_CLASS, '3') > 0 THEN 
	        v_where := v_where || ' WHEN xc.CLASS_CODE = 3 THEN ''Y''';	 
		    v_count := v_count + 1;
		END IF;
		-- At least one of the above needs to be true, or we don't include it.
		IF v_count = 0 THEN
	        v_where := ' ';
		ELSE
		    v_where := v_where || ' ELSE ''N'' END = ''Y''';
	    END IF;
	END IF;
	IF p_CASE_TYPE IS NOT NULL THEN 
	    v_where := v_where || '   AND      xc.CASE_TYPE = ''' || p_CASE_TYPE  || '''';
	ELSE
	    v_where := v_where || '   AND      xc.case_type IN (''T'',''S'',''A'')';
	END IF;
	IF p_BC_STATUS  IS NOT NULL THEN 
	    v_where := v_where || '   AND      XHB_REPORT_PKG.get_bc_status_ind(xc.CASE_ID) = ''' || p_BC_STATUS || '''';
	END IF;
	IF p_DEFAULT_HEARING_TYPE IS NOT NULL THEN 
	    v_where := v_where || '   AND      NVL(xrht.HEARING_TYPE_CODE, ''~'') = ''' || p_DEFAULT_HEARING_TYPE || '''' ;
	END IF;
	IF p_TIME_EST_FROM IS NOT NULL THEN
		IF p_TIME_EST_TO IS NULL THEN
			v_where := v_where || '   AND      xdfc.TRIAL_TIME_ESTIMATE BETWEEN ' || to_char(p_TIME_EST_FROM) || ' AND NULL ' || '
								  AND      xdfc.TRIAL_TIME_UNIT = ' || to_char(p_UNITS) ;
		ELSE
			v_where := v_where || '   AND      xdfc.TRIAL_TIME_ESTIMATE BETWEEN ' || to_char(p_TIME_EST_FROM) || ' AND ' || to_char(p_TIME_EST_TO) || '
								  AND      xdfc.TRIAL_TIME_UNIT = ' || to_char(p_UNITS) ;
		END IF;
	END IF;
    IF p_REQUIRED_JUDGE_TYPE IS NOT NULL THEN	
	    v_where := v_where || '   AND      exists ( select 1 from xhb_case_listing_entry where case_id = xc.case_id and ' || to_char(p_REQUIRED_JUDGE_TYPE) || ' = xhb_case_listing_entry.ref_judge_type_id) ';
    END IF;
	IF p_UNITS_WEEKS IS NOT NULL THEN 
	    v_where := v_where || '   AND      (TRUNC(sysdate) - ' || to_char(p_UNITS_WEEKS * 7) || ' >= TRUNC( NVL(NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE), xc.APPEAL_LODGED_DATE))) ';
    END IF;
	IF p_SECURE_COURTROOM  != 'N' THEN 
	    v_where := v_where || '   AND      xc.SECURE_COURT = ''' || p_SECURE_COURTROOM || '''';
    END IF;
	IF p_JUVENILE_ONLY != 'N' THEN 
	    v_where := v_where || '   AND      EXISTS (SELECT 1 FROM xhb_defendant_on_case xdoc WHERE xdoc.case_id = xc.case_id AND NVL(xdoc.is_juvenile, ''N'') = ''Y'' AND ROWNUM = 1 ) ';
    END IF;
    IF p_SORTBY =  'CASENUMBER' THEN 
	    v_where := v_where || '   ORDER BY xc.case_number, NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE)';
    ELSE
	    v_where := v_where || '	  ORDER BY NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE)';
	END IF;
    v_where := v_where || ',CASE notes.note_type WHEN ''HN'' THEN 1 WHEN ''IN'' THEN 2 ELSE 99 END, notes.NOTE_DATE';
	
	v_report_sql :=  v_report_sql || v_where;

	-- Now we have assembled the dynamic SQL, run it.
	OPEN p_results_out FOR v_report_sql;

 
    -- Now we have to log that we have run this report. 
    v_step := ' 2';
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
                         
    EXCEPTION     
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' || v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;  

END get_unlc_report;

/**********************************************************************************
*
* Procedure unlc_check_case_on_list  
*
* Indicates if a case is ok to be displayed in the UNLC based upon the case's presence
* in the XHB_CASE_ON_LIST or XHB_CASE_DIARY_FIXTURE table (Y), else N
*
**************************************************************************************/
FUNCTION unlc_check_case_on_list (p_case_id IN xhb_case.case_id%TYPE) RETURN VARCHAR2 IS

	v_valid_case		VARCHAR2(1) := 'Y';
	v_case_on_list_id	xhb_case_on_list.case_on_list_id%TYPE;
	v_time_listed		xhb_case_on_list.time_listed%TYPE;
	v_list_end_date		xhb_list.list_end_date%TYPE;
	v_list_type			VARCHAR2(20);
	v_reserved			xhb_case_on_list.reserved%TYPE;
	
	-- Retrieve any XHB_CASE_ON_LIST records (past and present) or any XHB_CASE_DIARY_FIXTURE records (future only) for the case
	CURSOR c_get_case_on_list IS
	SELECT * FROM
	(SELECT xcol.case_on_list_id AS case_on_list_id, TRUNC(xcol.time_listed) AS time_listed, TRUNC(xl.list_end_date) AS list_end_date, xrld.ref_data_value AS list_type, NVL(xcol.reserved,'N') AS reserved
	FROM  xhb_case_on_list xcol, xhb_list xl, xhb_ref_listing_data xrld
	WHERE xcol.case_id = p_case_id 
	AND NVL(xcol.obs_ind, 'N') <> 'Y'
	AND xl.list_id = xcol.list_id
	AND NVL(xl.obs_ind, 'N') <> 'Y'
	AND xrld.ref_listing_data_id = xl.list_type_id
	UNION ALL
	SELECT NULL AS case_on_list_id, TRUNC(xcdf.listing_date) AS time_listed, TRUNC(xcdf.listing_date) AS list_end_date, 'Fixture' AS list_type, 'N' AS reserved           
	FROM xhb_case_diary_fixture xcdf, xhb_case_listing_entry xcle
	WHERE xcle.case_id = p_case_id
	AND NVL(xcle.obs_ind, 'N') <> 'Y'
	AND xcle.case_listing_entry_id = xcdf.case_listing_entry_id
	AND NVL(xcdf.obs_ind, 'N') <> 'Y'
	AND TRUNC(xcdf.listing_date) >= TRUNC(SYSDATE)
	ORDER BY time_listed DESC, case_on_list_id DESC) subquery
	WHERE ROWNUM = 1;

BEGIN

	BEGIN
		OPEN c_get_case_on_list;
		FETCH c_get_case_on_list INTO v_case_on_list_id, v_time_listed, v_list_end_date, v_list_type, v_reserved;
		CLOSE c_get_case_on_list;
		
		-- If case listed/fixture in the future or today then case should not appear in UNLC
		IF v_time_listed >= TRUNC(SYSDATE) THEN
			v_valid_case := 'N';
			GOTO end_function;
		END IF;
		
		-- Any records left will be lists in the past (no past fixtures are returned), check the latest record
		-- If it's a Warned List or a Firm list on reserved case with list end date today or in the future, do not report, else do
		IF (v_list_type = 'Warned' OR (v_list_type = 'Firm' AND v_reserved = 'Y')) AND v_list_end_date >= TRUNC(SYSDATE) THEN
			v_valid_case := 'N';
			GOTO end_function;
		END IF;
		
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			v_valid_case := 'Y';
			GOTO end_function;
	END;

	<<end_function>>
	RETURN v_valid_case;
END unlc_check_case_on_list;

/**********************************************************************************
*
* Procedure get_exported_defendants  
*
* Indicates if any defendants on the case have a date exported value (Y), else N
*
**************************************************************************************/
FUNCTION get_exported_defendants (p_case_id IN xhb_case.case_id%TYPE
								 ,p_case_type IN xhb_case.case_type%TYPE
								 ,p_case_sub_type IN xhb_case.case_sub_type%TYPE) RETURN VARCHAR2 IS

	v_exported_defendants	VARCHAR2(1);
	v_count_defendants		NUMBER;
	v_count_exported		NUMBER;

BEGIN
	
	IF p_case_type = 'A' AND NVL(p_case_sub_type, '-') = 'O' THEN
		-- Ignore Misc Appeal cases
		v_exported_defendants := 'N';
	ELSE
		-- Get count of defendants and get count of exported defendants
		SELECT COUNT(*) INTO v_count_defendants 
		FROM xhb_defendant_on_case
		WHERE case_id = p_case_id
		AND NVL(obs_ind,'N') <> 'Y';
		
		SELECT COUNT(*) INTO v_count_exported
		FROM xhb_defendant_on_case
		WHERE case_id = p_case_id
		AND NVL(obs_ind,'N') <> 'Y'
		AND date_exported IS NOT NULL;
		
		IF v_count_defendants = v_count_exported THEN
			-- All defendants should be exported before the value of 'Y' can be returned
			v_exported_defendants := 'Y';
		ELSE
			v_exported_defendants := 'N';
		END IF;
	
	END IF;

	RETURN v_exported_defendants;
END;

/**********************************************************************************
*
* Procedure get_drsr_report  (CTX-2082)
*
* Missing Cracked/Effective Codes Report (DRSR)
*
**************************************************************************************/
PROCEDURE get_drsr_report(p_results_out  OUT SYS_REFCURSOR                        
                        , p_court_id     IN XHB_CASE.COURT_ID%TYPE
                        , p_MONTH_PERIOD IN VARCHAR2
                        , p_YEAR_PERIOD  IN VARCHAR2 ) AS

-- Local variables here
v_rpt_name      VARCHAR2(40)  := 'Missing Cracked/Effective Codes Report';
v_rpt_code      VARCHAR2(6)   := 'DRSR';
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);
v_step          VARCHAR2(10);
  
BEGIN
  v_step := ' 1';
  
  OPEN p_results_out FOR
	Select    to_char(xcol.TIME_LISTED, 'dd-mm-yyyy') as LISTING_DATE,
            xc.CASE_TYPE || xc.CASE_NUMBER as CASE_NUMBER,
            xrht.HEARING_TYPE_CODE as HTYP,
            xsol.SITTING_NUMBER AS SITTING_SEQUENCE_NO,
            xcr.COURT_ROOM_NAME,
            xcs.COURT_SITE_NAME as SITE                                 
    from    XHB_CASE xc
          , XHB_CASE_ON_LIST xcol
          , XHB_SITTING_ON_LIST xsol
          , XHB_LIST xl
          , XHB_REF_LISTING_DATA xrld
          , XHB_REF_HEARING_TYPE xrht
          , XHB_COURT_SITE xcs
          , XHB_COURT_ROOM xcr
    WHERE   xc.CASE_ID = xcol.CASE_ID
    AND     xrht.REF_HEARING_TYPE_ID(+) = xcol.HEARING_TYPE_ID
    AND     xsol.SITTING_ON_LIST_ID(+) = xcol.SITTING_ON_LIST_ID
    AND     xcr.COURT_ROOM_ID(+) = xcol.COURT_ROOM_ID
    AND     xcs.COURT_SITE_ID = xcol.COURT_SITE_ID 
    AND     to_char(xcol.TIME_LISTED, 'Mon-YYYY') = p_MONTH_PERIOD || '-' || p_YEAR_PERIOD
    AND     xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
    AND     xc.CASE_TYPE = 'T'
    AND     xc.COURT_ID = p_court_id
    AND     xcol.CRACKED_INEFFECTIVE_ID IS NULL
    AND     NVL(xcol.OBS_IND, '-') <> 'Y'
    AND     xcol.LIST_ID = xl.LIST_ID
    AND     xl.LIST_TYPE_ID = xrld.REF_LISTING_DATA_ID
    AND     xrld.REF_DATA_VALUE = 'Daily'
    ORDER BY LISTING_DATE, CASE_NUMBER;
	   
    -- Now we have to log that we have run this report. 
  v_step := ' 2';
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
                         
    EXCEPTION     
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' || v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;  
   
END get_drsr_report;

/**********************************************************************************
*
* Procedure get_relcj_report  (CTX-2289)
*
* Cases with Required Judge Report (RELCJ)
*
**************************************************************************************/
PROCEDURE get_relcj_report(p_results_out OUT SYS_REFCURSOR
                         , p_court_id    IN XHB_CASE.COURT_ID%TYPE) AS 
  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
  
  BEGIN
	OPEN p_results_out FOR
		SELECT xc.DEFAULT_HEARING_TYPE as HEARING_TYPE_ID,
			xc.CASE_ID,
			xc.CASE_TYPE||xc.CASE_NUMBER as CASE_NUMBER,
			xc.CASE_TITLE,
			xc.CLASS_CODE, 
			xrht.HEARING_TYPE_CODE as HEARING_TYPE,
			xdfc.TRIAL_TIME_ESTIMATE||decode(xdfc.TRIAL_TIME_UNIT,'1','H','2','D','3','W','4','M') as EST,
			xrf.SURNAME as REQUIRED_JUDGE,
			get_charges_info(xc.CASE_ID)CHARGES_INFO
		FROM XHB_CASE_LISTING_ENTRY xcle
			,XHB_CASE xc
			,XHB_REF_HEARING_TYPE xrht
			,XHB_DIRECTIONS_FOR_CASE xdfc 
			,XHB_REF_JUDGE xrf 
		where xc.CASE_ID = xcle.CASE_ID
		and xdfc.CASE_ID(+) =  xc.CASE_ID
		and xrht.REF_HEARING_TYPE_ID(+) = xc.DEFAULT_HEARING_TYPE
		and xcle.JUDGE_ID is not null
		and xcle.JUDGE_ID = xrf.REF_JUDGE_ID
		and xc.COURT_ID = p_court_id  
		and NVL(xcle.OBS_IND, '-') <> 'Y'
		and XHB_CASE_PKG.determine_case_status(xc.CASE_ID) =  'Open' 
		ORDER BY CASE_NUMBER;   --  Added for CTX-2816


      UPDATE_REPORT_LOG(p_court_id, 'RELCJ', 'CASES WITH REQUIRED JUDGE');
    
      EXCEPTION
          WHEN OTHERS THEN
              v_err_code := SQLCODE;
              v_err_msg  := 'Exception handler raised when others in XHB_REPORT_PKG.GET_RELCJ_REPORT '|| v_err_code || ' : ' || SQLERRM;
			  
          INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                 VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
          RAISE;
  END get_relcj_report;



PROCEDURE get_inftrpc_main_report(p_results_out OUT SYS_REFCURSOR                        
                              ,p_court_id IN XHB_CASE.COURT_ID%TYPE
                                      ,p_MONTH_PERIOD IN VARCHAR2
                                     ,p_YEAR_PERIOD IN VARCHAR2 )AS

  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
BEGIN
  OPEN p_results_out FOR
    select subqry.*,
	 to_char( LAST_DAY(to_date('01-'||p_MONTH_PERIOD||'-'||p_YEAR_PERIOD,'dd-Mon-YYYY')),'dd-Mon-YYYY') CALCMONTH,
     CASE WHEN (TNUMBER_DISPOSED_TRIAL+BWENDDATE_COUNT) <> 0 THEN round(RNUMBER_CRACKED_TRIALS/(TNUMBER_DISPOSED_TRIAL+BWENDDATE_COUNT)*100,0) ELSE 0 END BROAD_CRACKED_TRIAL,
	 round(TNUMBER_DISPOSED_TRIAL+BWENDDATE_COUNT,0)TNUMBER_DISPOSED_TRIALS,
     CASE WHEN PNUMBER_LISTED_TRIALS <> 0 THEN round(RNUMBER_CRACKED_TRIALS/PNUMBER_LISTED_TRIALS*100,0) ELSE 0 END RNUMBER_CRACKED_TRIALS_perc,
     CASE WHEN PNUMBER_LISTED_TRIALS <> 0 THEN round(QNUMBER_EFFECTIVE_TRIALS/PNUMBER_LISTED_TRIALS*100,0) ELSE 0 END QNUMBER_EFFECTIVE_TRIALS_perc,
     CASE WHEN PNUMBER_LISTED_TRIALS <> 0 THEN round(SNUMBER_INEFFECTIVE_LISTINGS/PNUMBER_LISTED_TRIALS*100,0) ELSE 0 END SNUMBER_INEFFECTIVE_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(AREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END AREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(BREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END BREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(CREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END CREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(DREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END DREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(EREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END EREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(FREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END FREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(GREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END GREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(HREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END HREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(IREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END IREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(JREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END JREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(KREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END KREASONS_CRACKED_TRIALS_perc,
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round(LREASONS_CRACKED_TRIALS/RNUMBER_CRACKED_TRIALS*100,0) ELSE 0 END LREASONS_CRACKED_TRIALS_perc,       
     round(BREASONS_CRACKED_TRIALS+DREASONS_CRACKED_TRIALS+FREASONS_CRACKED_TRIALS+IREASONS_CRACKED_TRIALS+JREASONS_CRACKED_TRIALS+KREASONS_CRACKED_TRIALS+LREASONS_CRACKED_TRIALS,0) ACRACKED_PROSECUTION,
	 CASE WHEN RNUMBER_CRACKED_TRIALS  <> 0 THEN round ( (BREASONS_CRACKED_TRIALS+DREASONS_CRACKED_TRIALS+FREASONS_CRACKED_TRIALS+IREASONS_CRACKED_TRIALS+JREASONS_CRACKED_TRIALS+KREASONS_CRACKED_TRIALS+LREASONS_CRACKED_TRIALS) / RNUMBER_CRACKED_TRIALS*100,0)ELSE 0 END ACRACKED_PROSECUTION_perc,
     round(AREASONS_CRACKED_TRIALS+CREASONS_CRACKED_TRIALS+EREASONS_CRACKED_TRIALS+HREASONS_CRACKED_TRIALS+GREASONS_CRACKED_TRIALS,0) BCRACKED_DEFENDANT, 
     CASE WHEN RNUMBER_CRACKED_TRIALS <> 0 THEN round ((AREASONS_CRACKED_TRIALS+CREASONS_CRACKED_TRIALS+EREASONS_CRACKED_TRIALS+HREASONS_CRACKED_TRIALS) / RNUMBER_CRACKED_TRIALS*100,0)ELSE 0 END BCRACKED_DEFENDANT_perc, 	 
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(M1_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END M1_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(M2_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END M2_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(M3_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END M3_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(N1_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END N1_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(N2_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END N2_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(N3_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END N3_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(O1_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END O1_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(O2_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END O2_INEFFECTIVE_perc,     
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(PINEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END PINEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(Q1_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END Q1_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(Q2_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END Q2_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(Q3_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END Q3_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(RINEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END RINEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(S1_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END S1_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(S2_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END S2_INEFFECTIVE_perc,
      CASE WHEN S3_INEFFECTIVE <> 0 THEN round(S3_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END S3_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(TINEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END TINEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(U1_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END U1_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(U2_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END U2_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(VINEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END VINEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(W1_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END W1_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(W2_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END W2_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(W3_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END W3_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(W4_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END W4_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(W5_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END W5_INEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(XINEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END XINEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(YINEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END YINEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(ZINEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END ZINEFFECTIVE_perc,
      CASE WHEN SNUMBER_INEFFECTIVE_LISTINGS <> 0 THEN round(S4_INEFFECTIVE/SNUMBER_INEFFECTIVE_LISTINGS*100,0) ELSE 0 END S4_INEFFECTIVE_perc
FROM (
Select
      nvl((
        select count(1)                                   
        from XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
              ,XHB_REF_HEARING_TYPE xrht
              , XHB_LIST xl
              , XHB_REF_LISTING_DATA xrld
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
        AND   xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
        AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND xcol.LIST_ID = xl.LIST_ID
        AND xl.LIST_TYPE_ID = xrld.REF_LISTING_DATA_ID
        AND xrld.REF_DATA_VALUE = 'Daily'
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0) as PNUMBER_LISTED_TRIALS,
     nvl((
        Select count(1) 
        from  XHB_CASE xc
            ,XHB_CASE_ON_LIST xcol
			 ,XHB_REF_HEARING_TYPE xrht
             ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'E'
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as QNUMBER_EFFECTIVE_TRIALS,
      nvl((
        Select count(1)                            
         from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			  ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0) as RNUMBER_CRACKED_TRIALS,
      nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			  ,XHB_REF_HEARING_TYPE xrht			  
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as SNUMBER_INEFFECTIVE_LISTINGS,
     nvl((
        select count(1)                                   
        from XHB_CASE xc
             ,XHB_DEFENDANT_ON_CASE xdoc
         WHERE to_char(xdoc.DATE_EXPORTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'|| p_YEAR_PERIOD
         AND xc.CASE_ID = xdoc.CASE_ID
         AND xc.COURT_ID = p_court_id 
         AND xc.CASE_TYPE = 'T'
         GROUP BY TRUNC(xdoc.DATE_EXPORTED,'Mon')
      ),0) as TNUMBER_DISPOSED_TRIAL,
        nvl((
          select count(1)                                   
        from XHB_CASE xc
             ,XHB_BW_HISTORY xbh
             ,XHB_DEFENDANT_ON_CASE xdoc
         WHERE xbh.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
         AND to_char(xbh.BW_END_DATE,'Mon-YYYY')= p_MONTH_PERIOD||'-'|| p_YEAR_PERIOD   
         AND xc.CASE_ID = xdoc.CASE_ID
         AND xc.COURT_ID = p_court_id 
         AND xc.CASE_TYPE = 'T'
         GROUP BY TRUNC(xbh.BW_END_DATE,'Mon')
    ),0) as BWENDDATE_COUNT,
        nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			  ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'A'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as AREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			  ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'B'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as BREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			  ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'C'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as CREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			  ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'D'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as DREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			  ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'E'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as EREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			  ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'F'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND  to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as FREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'G'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND  to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as GREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'H'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as HREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                                  
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
		AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'I'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as IREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'J'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as JREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
         from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'K'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
        ),0)as KREASONS_CRACKED_TRIALS,
        nvl((
        Select count(1)                            
          from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'C'
        AND xrce.CODE = 'L'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as LREASONS_CRACKED_TRIALS,
       nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'M1'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as M1_INEFFECTIVE,
       nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'M2'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as M2_INEFFECTIVE,
       nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'M3'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as M3_INEFFECTIVE,
       nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'N1'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as N1_INEFFECTIVE,
       nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'N2'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as N2_INEFFECTIVE,
       nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'N3'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as N3_INEFFECTIVE,
       nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'O1'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as O1_INEFFECTIVE,
       nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'O2'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as O2_INEFFECTIVE,
       nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'P'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as PINEFFECTIVE,
       nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
              ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
        AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'Q1'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
        AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as Q1_INEFFECTIVE,
       nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
              ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'Q2'
		AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as Q2_INEFFECTIVE,
      nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'Q3'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as Q3_INEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'R'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as RINEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'S1'
		AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as S1_INEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'S2'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as S2_INEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'S3'
		AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as S3_INEFFECTIVE,
      nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'T'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as TINEFFECTIVE,
      nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'U1'
		AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as U1_INEFFECTIVE,
      nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'U2'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as U2_INEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'V'
		AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as VINEFFECTIVE,
      nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'W1'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as W1_INEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'W2'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as W2_INEFFECTIVE,
      nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'W3'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as W3_INEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'W4'
		AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as W4_INEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'W5'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as W5_INEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'X'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as XINEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'Y'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as YINEFFECTIVE,
      nvl((
        Select count(1)                            
        from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y' 
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'Z'
		AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as ZINEFFECTIVE,
      nvl((
        Select count(1)                            
       from  XHB_CASE xc
              ,XHB_CASE_ON_LIST xcol
			   ,XHB_REF_HEARING_TYPE xrht
              ,XHB_REF_CRACKED_EFFECTIVE xrce
        WHERE xc.CASE_ID = xcol.CASE_ID AND NVL(xcol.OBS_IND, '-') <> 'Y'  
		AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
        AND xcol.CRACKED_INEFFECTIVE_ID = xrce.REF_CRACKED_EFFECTIVE_ID
        AND xrce.TRIAL_CODE_TYPE = 'I'
        AND xrce.CODE = 'S4'
        AND xc.COURT_ID = p_court_id 
        AND xc.CASE_TYPE = 'T'
		AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
        AND to_char(xcol.TIME_LISTED,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
        GROUP BY TRUNC(xcol.TIME_LISTED,'Mon')
      ),0)as S4_INEFFECTIVE
      FROM DUAL) subqry ;
	  
	  	   UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE COURT_ID = p_court_id AND CREST_REPORT_CODE = 'INFTRP/C';

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_INFTRP_MAIN_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_inftrpc_main_report;

PROCEDURE get_inftrpc_casenumbers_report(p_results_out OUT SYS_REFCURSOR                        
                                          ,p_court_id IN XHB_CASE.COURT_ID%TYPE
                                          ,p_MONTH_PERIOD IN VARCHAR2
                                          ,p_YEAR_PERIOD IN VARCHAR2 )AS

  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
BEGIN
  OPEN p_results_out FOR
   select      xrce.CODE,
               xrce.DESCRIPTION, 
               0 AS TOTAL,
			   to_char( LAST_DAY(to_date('01-'||p_MONTH_PERIOD||'-'||p_YEAR_PERIOD,'dd-Mon-YYYY')),'dd-Mon-YYYY') CALCMONTH,
               cases.CASE_NUMBER,
			   xrce.TRIAL_CODE_TYPE
               FROM 
           (Select xcol.CRACKED_INEFFECTIVE_ID,
                   cas.CASE_TYPE || cas.CASE_NUMBER AS CASE_NUMBER
            FROM  XHB_CASE cas 
                  ,XHB_CASE_ON_LIST xcol
                  ,XHB_REF_HEARING_TYPE xrht

            where cas.COURT_ID = p_court_id
            AND   cas.CASE_ID = xcol.CASE_ID 
            AND   NVL(xcol.OBS_IND, '-') <> 'Y'
            AND   xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
            AND   cas.CASE_TYPE = 'T'
            AND   xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
            AND   to_char(xcol.TIME_LISTED, 'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD) cases
            , XHB_REF_CRACKED_EFFECTIVE xrce
   WHERE cases.CRACKED_INEFFECTIVE_ID(+) = xrce.REF_CRACKED_EFFECTIVE_ID
   ORDER BY   xrce.CODE,cases.CASE_NUMBER ;         
   UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE COURT_ID = p_court_id AND CREST_REPORT_CODE = 'INFTRP/C';

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_INFTRPC_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_inftrpc_casenumbers_report;


PROCEDURE get_list_of_fixed_dates_report(p_results_out out SYS_REFCURSOR
                                        ,p_court_id IN XHB_CASE.COURT_ID%TYPE
                                        ,p_run_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE
										,p_run_again_ind IN NUMBER) AS

 v_err_code NUMBER;
 v_err_msg  VARCHAR2(1000);
BEGIN
	OPEN p_results_out FOR
		SELECT xdc.defendant_on_case_id docid,
        xrht.HEARING_TYPE_DESC AS hearingtype,
        xc.CASE_ID AS caseid,
        xc.CASE_TYPE || xc.CASE_NUMBER AS casenumber ,
        CASE
           WHEN xdc.CURRENT_BC_STATUS IN ('C','J') THEN '* ' || UPPER(xd.SURNAME) || ' ' || INITCAP(xd.FIRST_NAME) || ' ' || UPPER(SUBSTR(xd.MIDDLE_NAME,1,1))
           ELSE UPPER(xd.SURNAME) || ' ' || INITCAP(xd.FIRST_NAME) || ' ' || UPPER(SUBSTR(xd.MIDDLE_NAME,1,1))
        END AS defendant,
        TO_CHAR(xd.DATE_OF_BIRTH,'DD-MON-YYYY') as dob,
        DECODE(xd.GENDER,1,'M',2,'F',NULL) AS gender,
        (SELECT xrs.SOLICITOR_FIRM_NAME || DECODE(NVL(xrs.ADDRESS_ID,0), 0, '', ', ' || xa2.TOWN)
		 FROM XHB_REF_SOLICITOR_FIRM xrs, XHB_ADDRESS xa2
         WHERE xrs.REF_SOLICITOR_FIRM_ID = GET_SOLICITOR_ID(xdc.DEFENDANT_ON_CASE_ID)
		 AND xa2.ADDRESS_ID (+)= xrs.ADDRESS_ID) AS representative,
		 getPhoneNum(xa.ADDRESS_ID) AS courttelephoneno,
        xdc.PTIURN AS ptiurn,
        TO_CHAR(xcdf.LISTING_DATE,'DD Month YYYY') AS hearingdate,
        xcos.COURT_SITE_NAME AS hearingvenue,
        get_prosecutor_name(xc.CASE_ID) AS prosecutor,
        xcdf.LIST_NOTE_TEXT AS notes,
        xa.ADDRESS_1 || ' '  || xa.ADDRESS_2 || ' ' || xa.ADDRESS_3 || ' ' || xa.ADDRESS_4 || ' ' ||  xa.TOWN || ' ' || xa.COUNTY || ' ' || xa.POSTCODE AS courtaddress,
        xcdf.CASE_DIARY_FIXTURE_ID AS casediaryfixture

        FROM 	XHB_CASE xc,
				XHB_COURT xco,
				XHB_COURT_SITE xcos,
				XHB_CASE_LISTING_ENTRY xcle,
				XHB_CASE_DIARY_FIXTURE xcdf,
				XHB_FIXTURE_DEFT_ATTENDING xfda,
				XHB_REF_HEARING_TYPE xrht,
				XHB_DEFENDANT_ON_CASE xdc,
				XHB_DEFENDANT xd,
				XHB_ADDRESS xa

		WHERE 	xc.CASE_TYPE in ('S','T','A')
		AND 	xc.COURT_ID = p_court_id
		AND		xcle.CASE_ID = xc.CASE_ID
		AND		NVL(xcle.OBS_IND,'N') <> 'Y'
		AND		xcdf.CASE_LISTING_ENTRY_ID = xcle.CASE_LISTING_ENTRY_ID
		AND		NVL(xcdf.OBS_IND,'N') <> 'Y'
		AND 	((p_run_again_ind = 1 AND TRUNC(xcdf.FXL_RUN_DATE) = TRUNC(p_run_date))
				OR
				(p_run_again_ind = 0 AND TRUNC(xcdf.LISTING_DATE) >= TRUNC(SYSDATE) AND xcdf.FXL_RUN_DATE IS NULL))
		AND		xrht.REF_HEARING_TYPE_ID = xcdf.HEARING_TYPE_ID
		AND		xfda.CASE_DIARY_FIXTURE_ID = xcdf.CASE_DIARY_FIXTURE_ID
		AND		xfda.ATTENDING = 'Y'
		AND		NVL(xcdf.OBS_IND,'N') <> 'Y'
		AND		xdc.DEFENDANT_ON_CASE_ID = xfda.DEFENDANT_ON_CASE_ID
		AND		NVL(xdc.OBS_IND,'N') <> 'Y'
		AND		xd.DEFENDANT_ID = xdc.DEFENDANT_ID
		AND		xco.COURT_ID = xc.COURT_ID 
		AND   	xcos.COURT_ID = xco.COURT_ID
		AND   	xcos.COURT_SITE_ID = xcdf.COURT_SITE_ID
		AND		xa.ADDRESS_ID = xco.ADDRESS_ID

        ORDER BY xrht.LIST_SEQUENCE asc,casenumber;
        
        UPDATE XHB_REPORT_LOG SET  DATE_LAST_RUN = sysdate WHERE REPORT_NAME = 'List of Fixed Dates' AND COURT_ID = p_court_id;

  EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_LIST_OF_FIXED_DATES_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
END get_list_of_fixed_dates_report;



PROCEDURE update_case_diary_fix_run_date(casediaryfixturelist IN CLOB) AS
	v_err_code NUMBER;
	v_err_msg  VARCHAR2(1000);
	v_offset number default 1;
	v_chunk_size number := 1000;
	v_chunk VARCHAR2(2000);
	v_fixtures_list CLOB := casediaryfixturelist || ',';

BEGIN
  
	LOOP
		EXIT WHEN v_offset >= dbms_lob.getlength(v_fixtures_list);
		v_chunk := REGEXP_SUBSTR(v_fixtures_list, '.{1,' || v_chunk_size  || '},', v_offset); --Get the next 1000 characters that ends with ,
		v_offset := v_offset +  LENGTH(v_chunk); 
		UPDATE XHB_CASE_DIARY_FIXTURE SET FXL_RUN_DATE = sysdate 
		WHERE  ','||v_chunk||',' LIKE '%,'||CAST(CASE_DIARY_FIXTURE_ID AS VARCHAR(1000))||',%';
	END LOOP;
  
EXCEPTION
	WHEN OTHERS THEN
		v_err_code := SQLCODE;
		v_err_msg  := SQLERRM;
		INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
		VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.UPDATE_CASE_DIARY_FIX_RUN_DATE '|| v_err_code || ' : ' ||v_err_msg, sysdate);
		RAISE;

END update_case_diary_fix_run_date;

PROCEDURE get_run_date(p_results_out OUT SYS_REFCURSOR,p_court_id IN XHB_CASE.COURT_ID%TYPE, p_report_type IN XHB_REPORT_LOG.CREST_REPORT_CODE%TYPE) AS
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
BEGIN
  OPEN p_results_out FOR
   SELECT MAX(xrl.DATE_LAST_RUN) AS rundate FROM XHB_REPORT_LOG xrl
                  WHERE xrl.COURT_ID = p_court_id AND xrl.CREST_REPORT_CODE = p_report_type;

  EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_RUN_DATE '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
END get_run_date;

PROCEDURE get_prlis_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE
                          ,p_previous_list_id IN XHB_CASE.PUB_RUNNING_LIST_ID%TYPE) AS
  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
    BEGIN
      OPEN p_results_out FOR
        SELECT xc.CASE_ID,
               xc.CASE_TYPE || xc.CASE_NUMBER AS CASE_NUMBER,
			   DECODE(xdoc.DEFENDANT_NUMBER, NULL, '','-') || LPAD(NVL(TO_CHAR(xdoc.DEFENDANT_NUMBER),'***'),3,'0') AS DEFENDANT_NUMBER,
			   (SELECT NVL(MIN(xdoc2.defendant_number),0) FROM XHB_DEFENDANT_ON_CASE xdoc2 
				WHERE xdoc2.CASE_ID = xc.CASE_ID AND NVL(xdoc2.OBS_IND, 'N') <> 'Y') AS FIRST_DEF_NO,
			   NVL(xdoc.DEFENDANT_NUMBER, 0) AS CURRENT_DEF_NO,
               xc.CASE_TYPE,
               xd.SURNAME,
               xd.FIRST_NAME,
               xd.MIDDLE_NAME,
               xd.GENDER,
               xd.DATE_OF_BIRTH,
               xc.PRELIMINARY_DATE_OF_HEARING,
               (SELECT xrsf.SHORT_NAME FROM XHB_REF_SOLICITOR_FIRM xrsf
                  WHERE xrsf.REF_SOLICITOR_FIRM_ID = GET_SOLICITOR_ID(xdoc.DEFENDANT_ON_CASE_ID)) AS SOLICITOR_FIRM_NAME,
               (SELECT getPhoneNum(xrsf.ADDRESS_ID) FROM XHB_REF_SOLICITOR_FIRM xrsf
                  WHERE xrsf.REF_SOLICITOR_FIRM_ID = GET_SOLICITOR_ID(xdoc.DEFENDANT_ON_CASE_ID))  AS SOLICITOR_PHONE_NUMBER,   -- ctx-2963  changed to use getPhoneNum
               DECODE(xdoc.CURRENT_BC_STATUS, NULL, 'N/A', 'N', 'N/A', 'B', 'On Bail', 'C', 'In Custody', 'J', 'In Custody', NULL) as CURRENT_BC_STATUS,
               (SELECT INITCAP(xcrt.COURT_FULL_NAME)
               FROM XHB_REF_COURT xcrt
               WHERE xc.CCC_TRANS_FROM_REF_COURT_ID = xcrt.REF_COURT_ID
               AND xc.TRANSFERRED_CASE='Y') AS MAGISTRATES_TRANSFERRED_COURT,
			   INITCAP(xcrt.COURT_FULL_NAME) AS MAGISTRATES_COURT,
               get_prosecutor_name(xc.CASE_ID) as PROSECUTOR_NAME,
               xdoc.PTIURN,
               xc.CLASS_CODE,
               xc.SENT_FOR_TRIAL_DATE,
			   xc.DATE_TRANS_FROM,
               xc.COMMITTAL_DATE,
               xc.APPEAL_LODGED_DATE,
               get_charges_info(xc.case_id) charges
            FROM XHB_CASE xc,
            XHB_DEFENDANT_ON_CASE xdoc,
            XHB_DEFENDANT xd,
            XHB_REF_COURT xcrt
        WHERE xc.COURT_ID = p_court_id
        AND xc.CASE_ID = xdoc.CASE_ID (+)
        AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID (+)
		AND NVL(xdoc.OBS_IND (+),'N') <> 'Y'        
        AND xc.REF_COURT_ID = xcrt.REF_COURT_ID(+)
        AND ((p_previous_list_id = 0 AND xc.PUB_RUNNING_LIST_ID IS NULL)
        OR (p_previous_list_id > 0 AND xc.PUB_RUNNING_LIST_ID = p_previous_list_id))
        ORDER BY 2,3;

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_PRLIS_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;                  
  END get_prlis_report;
  
PROCEDURE publish_running_list(p_cases_to_publish IN CLOB, p_court_id IN XHB_COURT.COURT_ID%TYPE) AS
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
v_next_pub_running_list_id NUMBER;
v_offset number default 1;
v_chunk_size number := 1000;
v_chunk VARCHAR2(2000);
p_cases_to_publish_list CLOB := p_cases_to_publish || ',';
v_return_xml CLOB;
v_document_type_code VARCHAR2(2) := 'RL';
l_validation_id VARCHAR(50);
v_list_court XHB_COURT%ROWTYPE;
v_unique_id VARCHAR(50);
v_today_date VARCHAR2(10);
v_end_date VARCHAR2(20);
v_list_name VARCHAR2(100);
v_document_title VARCHAR2(255);
v_document_name VARCHAR2(50);
v_document_description VARCHAR2(200);
BEGIN

 SELECT XHB_PUB_RUNNING_LIST_SEQ.nextval INTO v_next_pub_running_list_id FROM dual;
 INSERT INTO XHB_PUB_RUNNING_LIST (PUB_RUNNING_LIST_ID, COURT_ID,PUBLISHED_DATE) VALUES (v_next_pub_running_list_id, p_court_id, sysdate);
 
 --update PUB_RUNNING_LIST_ID on cases on the list 
    LOOP
       EXIT WHEN v_offset >= dbms_lob.getlength(p_cases_to_publish_list);
          v_chunk := REGEXP_SUBSTR(p_cases_to_publish_list, '.{1,' || v_chunk_size  || '},', v_offset); --Get the next 1000 characters that ends with ,
          v_offset := v_offset +  LENGTH(v_chunk); 
          UPDATE XHB_CASE SET PUB_RUNNING_LIST_ID = v_next_pub_running_list_id
          WHERE  ','||v_chunk||',' LIKE '%,'||CAST(CASE_ID AS VARCHAR2(1000))||',%';
  END LOOP;
  
  SELECT XHB_VALIDATION_SEQ.NEXTVAL INTO l_validation_id FROM DUAL;
  v_unique_id := 'CSD' || v_document_type_code || LPAD(l_validation_id, 15, 0);
  v_return_xml:= XHB_GET_XML_REPORTS.GET_RUNNING_LIST(p_court_id, v_next_pub_running_list_id, v_unique_id);
  
  SELECT * INTO v_list_court FROM XHB_COURT xc WHERE xc.COURT_ID = p_court_id;
  v_today_date := TO_CHAR(sysdate, 'DD/MM/YY');
  v_end_date := TO_CHAR(sysdate, 'MON DD, YYYY');
  v_list_name := 'Running List';
  v_document_title:= v_list_name || ' ending ' || v_end_date;
  v_document_name := v_document_type_code || ' ' || v_today_date || ' ending ' || v_end_date;
  v_document_description := v_list_name || ', ' || INITCAP(v_list_court.COURT_NAME) || ' ' || INITCAP(v_list_court.COURT_PREFIX)
                            || ' on ' || v_today_date || ' ending ' || v_end_date;
  XHB_LIST_DISTRIBUTION_PKG.STORE_XML_DOCUMENT(v_return_xml, v_document_title, trunc(sysdate), v_document_type_code, v_list_court.COURT_ID, v_list_court.COUNTRY, v_list_court.LANGUAGE);
  XHB_LIST_DISTRIBUTION_PKG.STORE_VALIDATION_DOCUMENT(v_return_xml, v_document_name, v_document_description, v_document_type_code, l_validation_id, v_unique_id, 17, v_list_court.CREST_COURT_ID);
  
  UPDATE_REPORT_LOG(p_court_id, 'PRLIS', 'Running List');

   EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.publish_running_list '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;

END publish_running_list;

PROCEDURE get_ctlrp_report(p_results_out OUT SYS_REFCURSOR
                                ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                                ,p_screen_time_limit IN XHB_DEFENDANT_ON_CASE.CUSTODY_TIME_LIMIT%TYPE) AS
                                
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
   BEGIN
     OPEN p_results_out FOR 
      SELECT
      xc.CASE_TYPE || xc.CASE_NUMBER || '-' || xdc.DEFENDANT_NUMBER AS casedefendantnumber,
      UPPER(xd.SURNAME) || ' ' || LOWER(xd.FIRST_NAME) AS defendantname,
      TO_CHAR(xc.COMMITTAL_DATE,'dd/mm/yyyy') AS committaldate, TO_CHAR(xdc.MAG_COURT_FIRST_HEARING_DATE,'dd/mm/yyyy') AS convictiondate,
      TO_CHAR(xdc.CUSTODY_TIME_LIMIT,'dd/mm/yyyy') AS custodytimelimit, TO_CHAR(xc.DATE_CTL_REMINDER_PRINTED,'dd/mm/yyyy') AS reminderprinted, 
      (SELECT DECODE(COUNT(*),0,'N','Y')
        FROM XHB_CASE_ON_LIST xcol
        WHERE xcol.CASE_ID = xc.CASE_ID
        AND NVL(xcol.OBS_IND,'N') <> 'Y' ) AS listed, 
		xc.RECEIPT_TYPE as receipttype,
      DECODE(xrpa.PROSECUTOR_NAME_1,NUll,'',xrpa.PROSECUTOR_NAME_1 || ' ') || DECODE(xrpa.PROSECUTOR_NAME_2,NUll,'',xrpa.PROSECUTOR_NAME_2 || ' ') || xrpa.PROSECUTOR_NAME_3 AS prosecutorname,
     CASE WHEN TRUNC(xdc.CUSTODY_TIME_LIMIT) BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE+7) THEN 'Y'
      ELSE 'N'
      END AS highlightedrow
      
      FROM XHB_CASE xc
      INNER JOIN XHB_DEFENDANT_ON_CASE xdc ON xc.CASE_ID = xdc.CASE_ID AND (xdc.OBS_IND <> 'Y' OR xdc.OBS_IND IS NULL)
      INNER JOIN XHB_DEFENDANT xd ON xdc.DEFENDANT_ID = xd.DEFENDANT_ID 
      INNER JOIN XHB_CASE_PROSECUTOR_AGENCY xcpa 
      ON xc.CASE_ID = xcpa.CASE_ID 
      AND ( xc.CASE_TYPE != 'A' 
            OR (xc.CASE_TYPE = 'A'
                AND xcpa.PROSECUTOR_TYPE = 'R'     -- Added for CTX-2843  ) Appeal cases can have 2 prosecutors, a Respondent and an Objector.
                AND (xc.CASE_SUB_TYPE != 'O' 
                     OR xc.CASE_SUB_TYPE IS NULL        
                     OR xcpa.RESPONDENT_STATUS = 'R'   -- Added for CTX-2843  ) We only want the Respondent.
                    )
                )
           )
      AND (xcpa.OBS_IND <> 'Y' OR xcpa.OBS_IND IS NULL)
      
      INNER JOIN XHB_REF_PROSECUTOR_AGENCY xrpa 
      ON xcpa.REF_PROSECUTOR_AGENCY_ID = xrpa.REF_PROSECUTOR_AGENCY_ID 
      AND (xrpa.OBS_IND <> 'Y' OR xrpa.OBS_IND IS NULL)

      WHERE xc.RECEIPT_TYPE in ('CT','TC','VB','ST', 'IO', 'EW') AND xc.COURT_ID = p_court_id AND xc.DATE_TRANS_TO IS NULL
	  AND TRUNC(xdc.CUSTODY_TIME_LIMIT) BETWEEN TRUNC(SYSDATE) AND p_screen_time_limit
	  AND (xdc.RESULTS_VERIFIED <> 'E' OR xdc.RESULTS_VERIFIED IS NULL) AND xdc.DATE_EXPORTED IS NULL
	  AND NVL(xdc.CTL_APPLIES,'Y') = 'Y'
      
      ORDER BY xdc.CUSTODY_TIME_LIMIT, xc.CASE_NUMBER, xdc.DEFENDANT_NUMBER;
        
    UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE CREST_REPORT_CODE = 'CTLRP' AND COURT_ID = p_court_id;
        
  EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_CTRLP_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
        
 END get_ctlrp_report;
  
/*Nested loop functionality for Dave Burden*/

 FUNCTION get_charges_info (p_case_id IN xhb_case.case_id%TYPE) RETURN VARCHAR2 IS
 
 charges_return VARCHAR2(4000) := NULL; --first call make sure the return value is null
 
 CURSOR get_charge_c 
 IS
SELECT xcl.charges_info
 FROM xhb_charges_log xcl
 WHERE xcl.case_id = p_case_id
 and NVL(xcl.OBS_IND, '-') <> 'Y';

 BEGIN
 
  FOR get_charge_row IN get_charge_c
   LOOP
       charges_return := charges_return|| get_charge_row.charges_info || chr(13) || chr(10); --For each loop iteration concatenate the charges
    END LOOP;
 
 RETURN charges_return;

  EXCEPTION
   WHEN OTHERS THEN
		RETURN (SQLCODE||' '||'When others exception ' || SQLERRM);
 END get_charges_info;
  
  
  FUNCTION GET_SOLICITOR_ID(p_doc_id IN xhb_defendant_on_case.DEFENDANT_ON_CASE_ID%TYPE) RETURN XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE IS
  solicitor_return XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE := NULL;
      BEGIN
    SELECT xrsf.REF_SOLICITOR_FIRM_ID
        INTO solicitor_return
        FROM xhb_def_on_case_ref_sol_firm xdocrsf
      ,    xhb_ref_solicitor_firm xrsf
      WHERE p_doc_id  = xdocrsf.DEFENDANT_ON_CASE_ID (+)
      AND   xdocrsf.REF_SOLICITOR_FIRM_ID = xrsf.REF_SOLICITOR_FIRM_ID
      AND   NVL(xdocrsf.OBS_IND, '-') <> 'Y' 
      AND   NVL(xrsf.OBS_IND, '-') <> 'Y'
      AND   xdocrsf.REP_ST_DATE <= sysdate
      AND   (xdocrsf.REP_END_DATE IS NULL
      OR    xdocrsf.REP_END_DATE >= sysdate)
      AND rownum < 2;
  RETURN solicitor_return;

    EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL; -- If no solicitors found, return null

END GET_SOLICITOR_ID;

FUNCTION GET_MOST_RECENT_HEARING(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE ) RETURN XHB_DEF_HEARING_RECORD.HEARING_RECORD_ID%TYPE IS
most_recent_hearing_id XHB_DEF_HEARING_RECORD.HEARING_RECORD_ID%TYPE := null;
BEGIN
 SELECT h2.HEARING_RECORD_ID  into most_recent_hearing_id from
                  (select DEFENDANT_ON_CASE_ID, MAX(HEARING_END_DATE) as hearing_end_date
                          from XHB_DEF_HEARING_RECORD 
                          where DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
                          group by DEFENDANT_ON_CASE_ID) h
                          JOIN XHB_DEF_HEARING_RECORD h2 ON h.DEFENDANT_ON_CASE_ID = h2.DEFENDANT_ON_CASE_ID AND h.hearing_end_date = h2.hearing_end_date;
  RETURN most_recent_hearing_id;                  
END GET_MOST_RECENT_HEARING;   

  /**********************************************************************************
  *
  * Procedure UPDATE_REPORT_LOG  (CTX-2163)
  *
  * As all the reports have to update XHB_REPORT_LOG to say when they were run and for  
  * which court, this procedure does that, and defensively takes care of the case where 
  * the row to update does not exist.
  *
  **************************************************************************************/
PROCEDURE UPDATE_REPORT_LOG( p_court_id IN XHB_REPORT_LOG.COURT_ID%TYPE, p_report_code IN XHB_REPORT_LOG.CREST_REPORT_CODE%TYPE, p_report_name IN XHB_REPORT_LOG.REPORT_NAME%TYPE) IS
BEGIN
  MERGE INTO XHB_REPORT_LOG USING DUAL ON (COURT_ID = p_court_id AND CREST_REPORT_CODE = p_report_code)
    WHEN NOT MATCHED THEN INSERT (report_log_id, court_id, report_name,  crest_report_code, date_last_run) 
                     VALUES(xhb_report_log_seq.Nextval, p_court_id, p_report_name, p_report_code , sysdate)
    
    WHEN MATCHED THEN UPDATE SET DATE_LAST_RUN = sysdate;
END UPDATE_REPORT_LOG;

  /**********************************************************************************
  *
  * Function getAddress  (CTX-1335)
  *
  * Returns a composite string of address columns, separated by NewLine chrs.  Care is taken not to add null 
  * columns which would make the whole returned string null.  It also avoids adding NewLine chrs for null fields.
  *
  *
  **************************************************************************************/
Function getAddress(p_address_id  xhb_address.address_id%type) RETURN VARCHAR2 IS 

v_composite_address   VARCHAR2(500);

BEGIN
    -- Create string containing composite address. The Replace(s) remove any spurious NewLines/* from where an address column contains a null value.
    SELECT  Replace(Replace(ad.address_1 || chr(10) || nvl(ad.address_2, '*') || chr(10) || nvl(ad.address_3, '*') || chr(10) || nvl(ad.address_4, '*') || chr(10) || nvl(ad.town, '*') || chr(10) || nvl(ad.county, '*') || chr(10) || nvl(ad.postcode, '*'), '*' || chr(10)), '*') 
    INTO    v_composite_address 
    FROM    XHB_ADDRESS ad 
    WHERE   ad.address_id = p_address_id;
    
    RETURN  v_composite_address;
    
END getAddress;

  /**********************************************************************************
  *
  * Function getAddressSingleLine
  *
  * Returns a composite string of address columns, separated by commas designed to appear on a single line.  
  * Care is taken not to add null columns which would make the whole returned string null.  It also avoids 
  * adding commas for null fields.
  *
  **************************************************************************************/
Function getAddressSingleLine(p_address_id  xhb_address.address_id%type) RETURN VARCHAR2 IS 

	v_composite_address   VARCHAR2(500);
	v_adline1	XHB_ADDRESS.address_1%TYPE;
	v_adline2	XHB_ADDRESS.address_2%TYPE;
	v_adline3	XHB_ADDRESS.address_3%TYPE;
	v_adline4	XHB_ADDRESS.address_4%TYPE;
	v_town		XHB_ADDRESS.town%TYPE;
	v_county	XHB_ADDRESS.county%TYPE;
	v_postcode	XHB_ADDRESS.postcode%TYPE;

BEGIN
    SELECT  ad.address_1, ad.address_2, ad.address_3, ad.address_4, ad.town, ad.county, ad.postcode
    INTO    v_adline1, v_adline2, v_adline3, v_adline4, v_town, v_county, v_postcode
    FROM    XHB_ADDRESS ad 
    WHERE   ad.address_id = p_address_id;
	
	v_composite_address := INITCAP(v_adline1);	-- Address Line 1 is always populated
	IF v_adline2 IS NOT NULL THEN
		v_composite_address := v_composite_address || ', ' || INITCAP(v_adline2);
	END IF;
	IF v_adline3 IS NOT NULL THEN
		v_composite_address := v_composite_address || ', ' || INITCAP(v_adline3);
	END IF;
	IF v_adline4 IS NOT NULL THEN
		v_composite_address := v_composite_address || ', ' || INITCAP(v_adline4);
	END IF;
	IF v_town IS NOT NULL THEN
		v_composite_address := v_composite_address || ', ' || INITCAP(v_town);
	END IF;
	IF v_county IS NOT NULL THEN
		v_composite_address := v_composite_address || ', ' || INITCAP(v_county);
	END IF;
	IF v_postcode IS NOT NULL THEN
		v_composite_address := v_composite_address || '. ' || UPPER(v_postcode);
	END IF;
    
    RETURN  v_composite_address;
    
END getAddressSingleLine;

  /**********************************************************************************
  *
  * Function getPhoneNum  (CTX-2145) 
  * Amended for CTX-2963 - Accept 'TEL', 'Phone', 'PS_Phone'
  *
  * Returns a phone number for a given address_id.
  *
  *
  **************************************************************************************/
Function getPhoneNum(p_address_id  xhb_address.address_id%type) RETURN xhb_contact_detail.contact_value%type IS 

v_phone_num   xhb_contact_detail.contact_value%type;

BEGIN
    SELECT phone_num 
    INTO   v_phone_num 
    FROM (SELECT  cdet.contact_value as phone_num, decode(contact_type, 'TEL', 1, 'Phone', 2, 'PS_Phone', 3, 4) as ordering_col
          FROM    xhb_contact_detail cdet 
          WHERE   cdet.contact_type in('Phone', 'TEL', 'PS_Phone')
          AND     cdet.address_id = p_address_id
		  ORDER BY ordering_col)
    WHERE rownum < 2;
    
    RETURN  v_phone_num;
	
END getPhoneNum;


  /**********************************************************************************
  *
  * Function getAppellantSolAddress
  *
  * Returns the address of an Appellant's Solicitor (if present) 
  *
  **************************************************************************************/
Function getAppellantSolAddress(p_deft_on_case_id  xhb_defendant_on_case.defendant_on_case_id%type) RETURN VARCHAR2 IS 

v_ref_solicitor_firm_id xhb_ref_solicitor_firm.ref_solicitor_firm_id%type;
v_address               varchar2(500) := NULL;
v_address_id            xhb_address.address_id%type;
v_dx_ref                XHB_REF_SOLICITOR_FIRM.DX_REF%type;

BEGIN
    -- First we look up the ID of the Appellant's solicitor. If null, they don't have one
     v_ref_solicitor_firm_id := get_solicitor_id(p_deft_on_case_id);
     
     IF v_ref_solicitor_firm_id is not null then
         select rsf.address_id, rsf.DX_REF
         into   v_address_id, v_dx_ref
         from   xhb_ref_solicitor_firm rsf 
         where  rsf.ref_solicitor_firm_id =  v_ref_solicitor_firm_id; 
         v_address := CASE WHEN v_dx_ref IS NOT NULL THEN v_dx_ref ELSE getAddress(v_address_id) END;
     End If;

     Return v_address;
         
END getAppellantSolAddress;


  /**********************************************************************************
  *
  * Function getProsAgencySolName  
  *
  * Returns the address of a Solicitor associated with a xhb_case_prosecutor_agency record
  *
  **************************************************************************************/
FUNCTION getProsAgencySolName(p_case_pros_agency_id  xhb_case_prosecutor_agency.case_pros_agency_id%type) RETURN VARCHAR2 IS 

v_ref_solicitor_firm_id	xhb_ref_solicitor_firm.ref_solicitor_firm_id%type;
v_sol_name              xhb_ref_solicitor_firm.solicitor_firm_name%type := NULL;

BEGIN
    -- First we look up the ID of the Prosecutor's solicitor. If null, they don't have one
	SELECT ref_solicitor_firm_id INTO v_ref_solicitor_firm_id FROM xhb_prosecutor_ref_sol_firm 
	WHERE case_pros_agency_id = p_case_pros_agency_id AND NVL(obs_ind,'N') <> 'Y' AND rep_end_date IS NULL;
	
	IF v_ref_solicitor_firm_id IS NOT NULL THEN 
		SELECT solicitor_firm_name
		INTO   v_sol_name
		FROM   xhb_ref_solicitor_firm rsf 
		WHERE  rsf.ref_solicitor_firm_id =  v_ref_solicitor_firm_id;
	END IF;

	RETURN v_sol_name;
         
END getProsAgencySolName;

  /**********************************************************************************
  *
  * Function getProsAgencySolAddress  
  *
  * Returns the address of a Solicitor associated with a xhb_case_prosecutor_agency record
  *
  **************************************************************************************/
FUNCTION getProsAgencySolAddress(p_case_pros_agency_id  xhb_case_prosecutor_agency.case_pros_agency_id%type) RETURN VARCHAR2 IS 

v_ref_solicitor_firm_id xhb_ref_solicitor_firm.ref_solicitor_firm_id%type;
v_address               varchar2(500) := NULL;
v_address_id            xhb_address.address_id%type;
v_dx_ref                XHB_REF_SOLICITOR_FIRM.DX_REF%type;

BEGIN
    -- First we look up the ID of the Prosecutor's solicitor. If null, they don't have one
	SELECT ref_solicitor_firm_id INTO v_ref_solicitor_firm_id FROM xhb_prosecutor_ref_sol_firm 
	WHERE case_pros_agency_id = p_case_pros_agency_id AND NVL(obs_ind,'N') <> 'Y' AND rep_end_date IS NULL;
	
	IF v_ref_solicitor_firm_id IS NOT NULL THEN 
		SELECT rsf.address_id, rsf.DX_REF
		INTO   v_address_id, v_dx_ref
		FROM   xhb_ref_solicitor_firm rsf 
		WHERE  rsf.ref_solicitor_firm_id =  v_ref_solicitor_firm_id; 
		v_address := CASE WHEN v_dx_ref IS NOT NULL THEN v_dx_ref ELSE getAddress(v_address_id) END;
	END IF;

	RETURN v_address;
         
END getProsAgencySolAddress;


  /**********************************************************************************
  *
  * Procedure getAppealHearingNotifnRpt  (CTX-1335)
  *
  * Returns a list of Appeal hearings which have been listed for a specified court (usually, the one where the user is located).
  * It supplies the information needed to produce notification letters to the Appellant(s) (or their solicitor) and Respondent(s).
  *
  * Columns:      case_id, case_number, court_code, court_name, 
  *               appellant_id, appellant_name, solicitor_firm_name, appellant_address, 
  *               respondent_name, respondent_address, court_address, appeal_type, clerk_to_justice, 
  *               mag_conviction_date, list_start_date, time_listed, rpt_run_date, ct_phone_no 
  *
  *  13-Aug-2018  J Riley     CTX-2348 Alternate column added for time_listed, also now returned as a Varchar
  *                           to simplify front-end formatting.
  *  03-Oct-2018  J Riley     CTX-2703 Change to get court address from XHB_REF_COURT not XHB_COURT 
  *                           as it is the Magistrate's Court address that is required.
  *
  **************************************************************************************/

PROCEDURE getAppealHearingNotifnRpt(p_resultset OUT SYS_REFCURSOR,
									p_court_id  IN  XHB_COURT.COURT_ID%TYPE) AS 
                                           
-- Local variables here
v_rpt_name      VARCHAR2(30)  := 'getAppealHearingNotifnRpt';
v_rpt_code      VARCHAR2(6)   := 'NHA';
v_user          VARCHAR2(30);
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);
v_step          VARCHAR2(10);

BEGIN  
    v_step := '1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;
    
    v_step := '2';    
    open p_resultset for
    -- This query considers cases which are listed in a future Firm list
	select  cas.case_id,
			cas.case_type||cas.case_number as case_number,
			cas.case_sub_type,
			ct.crest_court_id,
			INITCAP(ct.court_name) as court_name,
			INITCAP(rct.court_full_name) as mags_court_name,
            -- Appellant columns
            doc.defendant_number, 
			INITCAP(DECODE(df.first_name,NUll,'',df.first_name || ' ') || DECODE(df.middle_name,NUll,'',df.middle_name || ' ') || df.surname) as appellant_name, 
            INITCAP(rsf.solicitor_firm_name) as solicitor_firm_name, getAppellantSolAddress(doc.defendant_on_case_id) as solicitor_address,
			getaddress(df.address_id) as defendant_address,			
             -- Respondent columns
            INITCAP(get_prosecutor_name(cas.CASE_ID)) as respondent_name,
			INITCAP(rpa.prosecutor_name_3) as respondent_surname,
      		CASE WHEN rpa.DX_REF IS NOT NULL THEN rpa.DX_REF ELSE getaddress(rpa.address_id) END as respondent_address,
			INITCAP(getProsAgencySolName(cpa.case_pros_agency_id)) as resp_sol_name,
            INITCAP(getProsAgencySolAddress(cpa.case_pros_agency_id)) as resp_sol_address,
            -- Court columns 
            rct.court_full_name || chr(10) || CASE WHEN rct.DX_REF IS NOT NULL THEN rct.DX_REF ELSE getaddress(rct.address_id) END as magistrate_address,       -- Changed ct to rct for CTX-2703
			getAddressSingleLine(ct.address_id) as court_address, 
            lower(Replace(r.de_code, 'APPEAL ')) as appeal_type, 'Clerk to Justice' as clerk_to_justice,
            cas.mag_conviction_date, cas.orig_body_decision_date,
			nvl(sol.time_listed, lst.list_start_date) as listing_date,   -- for CTX-2348, action 2
            to_char((case nvl(sol.time_marking_id, 0)                     -- added for CTX-2348  )
                     when 0 then TO_DATE(ct.court_start_time, 'HH24:MI')  -- added for CTX-2348  ) action 1
                     else sol.time_listed                                 -- added for CTX-2348  )
                     end),'HH24:MI') as time_listed,                      -- added for CTX-2348  )
            to_date(sysdate) as rpt_run_date, 
			getPhoneNum(ct.address_id) as ct_phone_no,    -- ctx-2963  changed to use getPhoneNum
			NVL(df.CURRENT_PRISON_STATUS,'N') as in_custody
    from    xhb_case cas, xhb_defendant df, xhb_defendant_on_case doc, xhb_ref_solicitor_firm rsf,   
            xhb_case_prosecutor_agency cpa, xhb_ref_prosecutor_agency rpa, 
            -- tables unique to the 1st query
            xhb_case_on_list csol, xhb_list lst, xhb_ref_listing_data rld, 
            xhb_sitting_on_list sol,                    -- added for CTX-2348
            -- tables unique to the 1st query (end)
            xhb_court ct, xhb_ref_system_code r, xhb_ref_court rct       -- Added xhb_ref_court for CTX-2703
    where  df.defendant_id = doc.defendant_id 
    and    doc.case_id = cas.case_id 
    and    rsf.ref_solicitor_firm_id(+) = get_solicitor_id(doc.defendant_on_case_id) 
    and    cpa.CASE_PROS_AGENCY_ID = get_prosecutor (cas.CASE_ID)
    and    cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id 
    and    cas.ref_court_id = rct.ref_court_id(+)    -- Added for CTX-2703  
    and    nvl(rpa.obs_ind, 'N')  != 'Y'    -- Added for CTX-2703
    -- clauses unique to 1st query 
    and    cas.case_id = csol.case_id 
    and    csol.list_id = lst.list_id  
    and    csol.sitting_on_list_id = sol.sitting_on_list_id(+) -- added for CTX-2348 Is outer join as per Action 3.
    and    nvl(csol.obs_ind, 'N') != 'Y'    -- Added for CTX-2703
    and    nvl(csol.nha_firm_list, 'N') = 'Y'
    and    lst.list_start_date > sysdate  
    and    lst.list_type_id = rld.ref_listing_data_id 
    and    rld.ref_data_type = 'LIST_TYPE'
    and    rld.ref_data_value = 'Firm' 
    -- clauses unique to 1st query (end)
    and    ct.court_id = lst.court_id
    and    ct.court_id = p_court_id -- parameter here
    and    cas.case_type = 'A'      -- Appeal
    and    cas.case_sub_type = r.code(+)
    and    r.code_type IN ('CASE_APPEAL_TYPE','MISCCASE_APPEAL_TYPE')
    and    ct.court_id = r.court_id    -- Added for CTX-2703
	and    NVL(r.OBS_IND,'N') <> 'Y'
    union 
    -- This query considers cases which have a future fixture
    select  cas.case_id,
            cas.case_type||cas.case_number as case_number,
			cas.case_sub_type,
            ct.crest_court_id,
            INITCAP(ct.court_name) as court_name,
			INITCAP(rct.court_full_name) as mags_court_name,			
            -- Appellant columns
            doc.defendant_number, 
			INITCAP(DECODE(df.first_name,NUll,'',df.first_name || ' ') || DECODE(df.middle_name,NUll,'',df.middle_name || ' ') || df.surname) as appellant_name, 
            INITCAP(rsf.solicitor_firm_name) as solicitor_firm_name, getAppellantSolAddress(doc.defendant_on_case_id) as solicitor_address, 
			getaddress(df.address_id) as defendant_address,	
            -- Respondent columns
            INITCAP(get_prosecutor_name(cas.CASE_ID)) as respondent_name, 
			INITCAP(rpa.prosecutor_name_3) as respondent_surname,
            CASE WHEN rpa.DX_REF IS NOT NULL THEN rpa.DX_REF ELSE getaddress(rpa.address_id) END as respondent_address,
			INITCAP(getProsAgencySolName(cpa.case_pros_agency_id)) as resp_sol_name,
            INITCAP(getProsAgencySolAddress(cpa.case_pros_agency_id)) as resp_sol_address,
            -- Court columns 
            rct.court_full_name || chr(10) || CASE WHEN rct.DX_REF IS NOT NULL THEN rct.DX_REF ELSE getaddress(rct.address_id) END as magistrate_address,      -- Changed ct to rct for CTX-2703
			getAddressSingleLine(ct.address_id) as court_address, 
            lower(Replace(r.de_code, 'APPEAL ')) as appeal_type, 'Clerk to Justice' as clerk_to_justice,
            cas.mag_conviction_date,  cas.orig_body_decision_date,
			cdf.listing_date,
            to_char(case (cdf.listing_date - trunc(cdf.listing_date))   -- added for CTX-2348
                    when 0.0 THEN TO_DATE(ct.court_start_time, 'HH24:MI')        -- added for CTX-2348
                    else cdf.listing_date                    -- added for CTX-2348
                    end ,'HH24:MI')  as time_listed,
            to_date(sysdate), 
			getPhoneNum(ct.address_id) as ct_phone_no,    -- ctx-2963  changed to use getPhoneNum
			NVL(df.CURRENT_PRISON_STATUS,'N') as in_custody
    from    xhb_case cas, xhb_defendant df, xhb_defendant_on_case doc, xhb_ref_solicitor_firm rsf,  
            xhb_case_prosecutor_agency cpa, xhb_ref_prosecutor_agency rpa, 
            -- tables unique to the 2nd query
            xhb_case_listing_entry cle, xhb_case_diary_fixture cdf, 
            -- tables unique to the 2nd query (end)
            xhb_court ct, xhb_ref_system_code r, xhb_ref_court rct           -- Added xhb_ref_court for CTX-2703
    where  df.defendant_id = doc.defendant_id 
    and    doc.case_id = cas.case_id 
    and    rsf.ref_solicitor_firm_id(+) = get_solicitor_id(doc.defendant_on_case_id) 
    and	   cpa.CASE_PROS_AGENCY_ID = get_prosecutor (cas.CASE_ID)
    and    cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id 
    and    nvl(rpa.obs_ind, 'N')  != 'Y'    -- Added for CTX-2703
    and    cas.ref_court_id = rct.ref_court_id(+)    -- Added for CTX-2703
    -- clauses unique to 2nd query 
    and    cas.case_id = cle.case_id  
    and    cle.case_listing_entry_id = cdf.case_listing_entry_id  
    and    cdf.listing_date > sysdate 
    and    nvl(cdf.fixture_notice_required, 'N') = 'Y'
    -- clauses unique to 2nd query (end)
    and    ct.court_id = p_court_id -- parameter here
    and    ct.court_id = cas.court_id
    and    cas.case_type = 'A'      -- Appeal
    and    cas.case_sub_type = r.code(+)
    and    r.code_type IN ('CASE_APPEAL_TYPE','MISCCASE_APPEAL_TYPE')
    and    ct.court_id = r.court_id    -- Added for CTX-2703
	and    NVL(r.OBS_IND,'N') <> 'Y'
    order by 1	;

    -- Now we have to log that we have run this report. 
    v_step := '3';
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, 'Notification of Appeal Hearing');
                         
    EXCEPTION     

        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' ||v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;  
   
END getAppealHearingNotifnRpt;       


  /**********************************************************************************
  *
  * Procedure getNHAMiscAppealObjectors
  *
  * Returns a list of objectors that need to receive a copy of the NHA report for Miscellaneous
  * Appeal cases.
  *
  **************************************************************************************/

PROCEDURE getNHAMiscAppealObjectors(p_resultset OUT SYS_REFCURSOR,
									p_court_id  IN  XHB_COURT.COURT_ID%TYPE) AS 
                                           
-- Local variables here
v_rpt_name      VARCHAR2(30)  := 'getNHAMiscAppealObjectors';
v_rpt_code      VARCHAR2(6)   := 'NHA';
v_user          VARCHAR2(30);
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);
v_step          VARCHAR2(10);

BEGIN  
    v_step := '1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;
    
    v_step := '2';    
    open p_resultset for
    -- This query considers cases which are listed in a future Firm list
	select  cas.case_id,
            cas.case_type||cas.case_number as case_number,
			INITCAP(DECODE(objref.PROSECUTOR_NAME_1,NUll,'',objref.PROSECUTOR_NAME_1 || ' ') || DECODE(objref.PROSECUTOR_NAME_2,NUll,'',objref.PROSECUTOR_NAME_2 || ' ') || objref.PROSECUTOR_NAME_3) as objector_name,
            CASE WHEN objref.DX_REF IS NOT NULL THEN objref.DX_REF ELSE xhb_report_pkg.getaddress(objref.address_id) END as objector_address,
            INITCAP(getProsAgencySolName(obj.case_pros_agency_id)) as solicitor_name,
            INITCAP(getProsAgencySolAddress(obj.case_pros_agency_id)) as solicitor_address
    from    xhb_case cas, xhb_case_prosecutor_agency obj, xhb_ref_prosecutor_agency objref,   
            -- tables unique to the 1st query
            xhb_case_on_list csol, xhb_list lst, xhb_ref_listing_data rld, 
            xhb_sitting_on_list sol,
            -- tables unique to the 1st query (end)
            xhb_ref_system_code r
    where  obj.case_id = cas.case_id
    and    nvl(obj.obs_ind,'N') <> 'Y'
    and    obj.PROSECUTOR_TYPE = 'R'
    and    obj.RESPONDENT_STATUS = 'O'
    and    obj.ref_prosecutor_agency_id = objref.ref_prosecutor_agency_id 
    and    nvl(objref.obs_ind, 'N')  != 'Y'
    -- clauses unique to 1st query 
    and    cas.case_id = csol.case_id 
    and    csol.list_id = lst.list_id  
    and    csol.sitting_on_list_id = sol.sitting_on_list_id(+)
    and    nvl(csol.obs_ind, 'N') != 'Y'
    and    nvl(csol.nha_firm_list, 'N') = 'Y'
    and    lst.list_start_date > sysdate  
    and    lst.list_type_id = rld.ref_listing_data_id 
    and    rld.ref_data_type = 'LIST_TYPE'
    and    rld.ref_data_value = 'Firm' 
    -- clauses unique to 1st query (end)
    and    lst.court_id = p_court_id -- parameter here
    and    cas.case_type = 'A'      -- Appeal
    and    cas.case_sub_type = r.code(+)
    and    r.code_type  = 'MISCCASE_APPEAL_TYPE'
    and    lst.court_id = r.court_id
	and    NVL(r.OBS_IND,'N') <> 'Y'
    union 
    -- This query considers cases which have a future fixture
    select  cas.case_id,
            cas.case_type||cas.case_number as case_number,
            INITCAP(DECODE(objref.PROSECUTOR_NAME_1,NUll,'',objref.PROSECUTOR_NAME_1 || ' ') || DECODE(objref.PROSECUTOR_NAME_2,NUll,'',objref.PROSECUTOR_NAME_2 || ' ') || objref.PROSECUTOR_NAME_3) as objector_name,
            CASE WHEN objref.DX_REF IS NOT NULL THEN objref.DX_REF ELSE xhb_report_pkg.getaddress(objref.address_id) END as objector_address,
            INITCAP(getProsAgencySolName(obj.case_pros_agency_id)) as solicitor_name,
            INITCAP(getProsAgencySolAddress(obj.case_pros_agency_id)) as solicitor_address
    from    xhb_case cas, xhb_case_prosecutor_agency obj, xhb_ref_prosecutor_agency objref,
            -- tables unique to the 2nd query
            xhb_case_listing_entry cle, xhb_case_diary_fixture cdf, xhb_ref_system_code r

    where  obj.case_id = cas.case_id
    and    nvl(obj.obs_ind,'N') <> 'Y'
    and    obj.PROSECUTOR_TYPE = 'R'
    and    obj.RESPONDENT_STATUS = 'O'
    and    obj.ref_prosecutor_agency_id = objref.ref_prosecutor_agency_id 
    and    nvl(objref.obs_ind, 'N')  != 'Y'
    
    -- clauses unique to 2nd query 
    and    cas.case_id = cle.case_id  
    and    cle.case_listing_entry_id = cdf.case_listing_entry_id  
    and    cdf.listing_date > sysdate 
    and    nvl(cdf.fixture_notice_required, 'N') = 'Y'
    -- clauses unique to 2nd query (end)
    and    cas.court_id = p_court_id -- parameter here
    and    cas.case_type = 'A'      -- Appeal
    and    cas.case_sub_type = r.code(+)
    and    r.code_type = 'MISCCASE_APPEAL_TYPE'
    and    cas.court_id = r.court_id
	and    NVL(r.OBS_IND,'N') <> 'Y'
    order by 1	;
                         
    EXCEPTION     

        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' ||v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;  
   
END getNHAMiscAppealObjectors;                                           


PROCEDURE MARK_CASES_AS_NHA_REPORTED(p_cases IN CLOB) AS
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
v_offset number default 1;
v_chunk_size number := 1000;
v_chunk VARCHAR2(2000);
v_cases_list CLOB := p_cases || ',';

BEGIN

-- We have to set the Fixture_Notice_Required and NHA_Firm_List flags. This prevents the letters being re-printed next time the report is run.
  LOOP
       EXIT WHEN v_offset >= dbms_lob.getlength(v_cases_list);
          v_chunk := REGEXP_SUBSTR(v_cases_list, '.{1,' || v_chunk_size  || '},', v_offset); --Get the next 1000 characters that ends with ,
          v_offset := v_offset +  LENGTH(v_chunk);
          UPDATE XHB_CASE_DIARY_FIXTURE SET FIXTURE_NOTICE_REQUIRED = 'N' 
          WHERE  CASE_LISTING_ENTRY_ID IN (SELECT CASE_LISTING_ENTRY_ID FROM XHB_CASE_LISTING_ENTRY 
                 WHERE  ','||v_chunk||',' LIKE '%,'||CAST(CASE_ID AS VARCHAR(1000))||',%');
  
          UPDATE XHB_CASE_ON_LIST SET NHA_FIRM_LIST = 'N'
          WHERE  ','||v_chunk||',' LIKE '%,'||CAST(CASE_ID AS VARCHAR(1000))||',%';
  END LOOP;

   EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.UPDATE_CASE_REMINDER_PRINTED '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
END MARK_CASES_AS_NHA_REPORTED;

PROCEDURE get_lod_report_between_dates(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE
                          ,p_from_date    IN XHB_DIARY_NOTE_ENTRY.DIARY_DATE%TYPE 
                          ,p_to_date      IN XHB_DIARY_NOTE_ENTRY.DIARY_DATE%TYPE) AS

v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
BEGIN
  OPEN p_results_out FOR
   SELECT  xdne.DIARY_DATE as diary_date, xc.CASE_TYPE || xc.CASE_NUMBER as CASE_NUMBER, xc.CASE_TITLE as case_title, xdne.DIARY_NOTE_TEXT as diary_note_text, xdne.CREATION_DATE 
   FROM XHB_DIARY_NOTE_ENTRY xdne
   LEFT JOIN XHB_CASE xc ON xdne.CASE_ID = xc.CASE_ID
   WHERE  ( (xdne.diary_date BETWEEN p_from_date AND p_to_date AND p_to_date IS NOT NULL)
   OR (xdne.diary_date BETWEEN p_from_date AND p_from_date AND p_to_date IS NULL) ) AND xdne.COURT_ID = p_court_id AND (xdne.obs_ind <> 'Y' or xdne.obs_ind is NULL)
   ORDER BY DIARY_DATE, xc.CASE_TYPE desc, NVL(xc.CASE_NUMBER, 0) asc, CREATION_DATE;
 
   UPDATE XHB_REPORT_LOG SET  DATE_LAST_RUN = sysdate WHERE CREST_REPORT_CODE = 'LODR' AND COURT_ID = p_court_id;
 
  EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_LOD_REPORT_BETWEEN_DATES '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
END get_lod_report_between_dates;

PROCEDURE get_obw_report(p_results_out   OUT SYS_REFCURSOR,
                         p_bw_issue_date IN XHB_BW_HISTORY.BW_ISSUE_DATE%TYPE,
                         p_court_id      IN XHB_CASE.COURT_ID%TYPE) AS
-- Local variables here
v_rpt_name      VARCHAR2(30)  := 'get_obw_report';
v_rpt_code      VARCHAR2(6)   := 'OBW';
v_user          VARCHAR2(30);
v_step          VARCHAR2(5);
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);


  BEGIN
      v_step := '1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;
	
	v_step := '2'; 
  OPEN p_results_out FOR
select cas4.case_id, cas4.case_type||cas4.case_number as case_number, 
doc.defendant_number,
def.surname|| ' ' ||def.initials as defendant_name,
cas4.class_code,
to_char(bwh.bw_issue_date,'DD-MON-YYYY') as bw_issue_date,
rsf.solicitor_firm_name,
doc.ptiurn,
to_char (sysdate, 'DD-MON-YYYY') as today_date
from xhb_case cas4, xhb_defendant def, xhb_defendant_on_case doc, xhb_bw_history bwh, xhb_ref_solicitor_firm rsf
where cas4.case_id = doc.case_id
and def.defendant_id = doc.defendant_id
and doc.defendant_on_case_id = bwh.defendant_on_case_id
and bwh.bw_end_date is null
and bwh.bw_issue_date >= nvl(p_bw_issue_date, bwh.bw_issue_date)
and rsf.REF_SOLICITOR_FIRM_ID(+) = GET_SOLICITOR_ID(doc.DEFENDANT_ON_CASE_ID)
and cas4.court_id = p_court_id
 and NVL(doc.OBS_IND, '-') <> 'Y'
      and NVL(bwh.OBS_IND, '-') <> 'Y' 
      and NVL(rsf.OBS_IND, '-') <> 'Y'
      order by bwh.bw_issue_date asc; 
 
    -- Now we have to log that we have run this report. 
    v_step := '3';
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, 'Outstanding Bench Warrants');
                         
    EXCEPTION     

	        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' ||v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;  
  END get_obw_report; 

  /**********************************************************************************
  *
  * Procedure getRUMO_Rpt  (CTX-2145)
  *
  * Returns a list of unacknowledged Monetary Orders for a given Court (usually High/Crown court). These orders are sent
  * to Magistrates Courts to arrange collection. The Magistrates Courts are required to acknowledge receipt of the 
  * orders. This report finds Orders where that has not happened, so that chasing letters can be sent.
  *
  * Columns:      monetary_order_tracking_id, court_name, ct_address, ct_phone, collect_court_name, collect_court_address, 
  *               case_number, defendantnum, defendant, ptiurn, order_date, fined (amount), compensation (amount), costs (amount), 
  *               today_date
  *
  **************************************************************************************/

PROCEDURE getRUMO_Rpt(p_resultset OUT SYS_REFCURSOR 
                    , p_court_id  IN  XHB_REF_COURT.REF_COURT_ID%TYPE) AS 
                                           
-- Local variables here
v_rpt_name      VARCHAR2(30)  := 'Unacknowledged Monetary Orders';
v_rpt_code      VARCHAR2(6)   := 'RUMO';
v_user          VARCHAR2(30);
v_step          VARCHAR2(10);
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);

BEGIN  
    v_step := '1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;
    
    v_step := '2';    
    open p_resultset for
    -- Query for Unacknowledged Monetary Orders Report (RUMO)
    select mot.monetary_order_tracking_id, ct.court_name as court_name, getAddress(ct.address_id) as ct_address, getPhoneNum(ct.address_id) as ct_phone, ct2.court_full_name as collect_court_name, getAddress(ct2.address_id) as collect_court_address, 
           case.case_type || case.case_number as case_number, doc.defendant_number,  df.surname || ' ' || df.first_name || ' ' || df.middle_name as defendant, doc.ptiurn, 
           to_char(mot.order_date, 'DD-MON-YYYY') as order_date, 
           case when mot.fined is null then '' else to_char(mot.fined,'L9,999,999,999') end  as fined,
           case when mot.compensation is null then '' else to_char(mot.compensation,'L9,999,999,999') end as compensation,
           case when mot.costs is null then '' else to_char(mot.costs,'L9,999,999,999')  end as costs,
           to_char(to_date(sysdate), 'DD-MON-YYYY') as today_date
    from   xhb_monetary_order_tracking mot, xhb_case case, xhb_defendant_on_case doc, xhb_defendant df, xhb_court ct, xhb_ref_court ct2
    where  mot.case_id = case.case_id 
    and    case.court_id = ct.court_id -- By restricting cases to those for the selected court, we ensure that any monetary orders found are issued by that court.
    and    ct.court_id = p_court_id     -- parameter here
    and    nvl(ct.obs_ind, 'N') != 'Y'
    and    mot.acknowledgement_date is null  -- The monetary order has not been acknowledged
    and    mot.defendant_on_case_id = doc.defendant_on_case_id 
	and	   TRUNC(mot.order_date) < TRUNC(SYSDATE - 14)
    and    doc.defendant_id = df.defendant_id  
    and    nvl(doc.obs_ind, 'N') != 'Y'
    and    mot.collect_magistrates_court_id = ct2.ref_court_id 
    and    nvl(ct2.obs_ind, 'N') != 'Y'
    order by ct2.court_full_name, mot.order_date; 
    
    
    -- Now we have to log that we have run this report. 
    v_step := '3';
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
                         
    EXCEPTION     
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' || v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;  
   
END getRUMO_Rpt;

PROCEDURE get_ntrsf_report(p_results_out OUT SYS_REFCURSOR
                                ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                                ,p_case_id     IN XHB_CASE.CASE_ID%TYPE) AS

v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
   BEGIN
     OPEN p_results_out FOR 
      SELECT
      xc.CASE_TYPE || xc.CASE_NUMBER AS casenumber,
      UPPER(xd.FIRST_NAME) || ' ' || UPPER(xd.INITIALS) || ' ' ||  UPPER(xd.SURNAME) AS defendantname,
      UPPER(xd.SURNAME) || ' ' || UPPER(xd.FIRST_NAME) || ' ' ||  UPPER(xd.MIDDLE_NAME) AS defendantname2,
      get_prosecutor_name(xc.CASE_ID) AS prosecutorname,
      xrs.SOLICITOR_FIRM_NAME AS representative,
      xrc.DX_REF  AS COURT_TO_DX_REF,
      xa1.ADDRESS_1 || '|' || xa1.ADDRESS_2 || '|' || xa1.ADDRESS_3 || '|' || xa1.TOWN || '|' || xa1.COUNTY || '|' || xa1.POSTCODE AS courttoaddress,
      xa5.ADDRESS_1 || ' ' || xa5.ADDRESS_2 || '|' || xa5.ADDRESS_3 || '|' || xa5.TOWN || ' ' || xa5.COUNTY || ' ' || xa5.POSTCODE AS courtfromaddress,
      CASE WHEN xrpa.DX_REF IS NOT NULL
           THEN xrpa.DX_REF 
           ELSE xa2.ADDRESS_1 || '|' || xa2.ADDRESS_2 || '|' || xa2.ADDRESS_3 || '|' || xa2.TOWN || '|' || xa2.COUNTY || '|' || xa2.POSTCODE || '|' || xcd1.CONTACT_VALUE END AS prosecutoraddress,
      xa3.ADDRESS_1 || '|' || xa3.ADDRESS_2 || '|' || xa3.ADDRESS_3 || '|' || xa3.TOWN || '|' || xa3.COUNTY || '|' || xa3.POSTCODE AS defaddress,
      
      CASE WHEN xrs.DX_REF IS NOT NULL
           THEN xrs.DX_REF 
           ELSE xa4.ADDRESS_1 || '|' || xa4.ADDRESS_2 || '|' || xa4.ADDRESS_3 || '|' || xa4.TOWN || '|' || xa4.COUNTY || '|' || xa4.POSTCODE || '|' || xcd2.CONTACT_VALUE END AS soladdress,
      NVL(xd.CURRENT_PRISON_STATUS,'N') as incustody,
	  xrc.COURT_FULL_NAME AS courtto,
	  INITCAP(xrc.COURT_FULL_NAME) AS courttobody,
      getPhoneNum(xa5.ADDRESS_ID) AS courtfromtelephoneno,    -- ctx-2963  changed to use getPhoneNum
      INITCAP(xco.COURT_NAME) AS courtfrom,
      regexp_replace(TO_CHAR(xc.DATE_TRANS_TO,'DD Month YYYY'),'[[:space:]]+',chr(32)) AS datetrans,
      rownum AS rownumber
    
      FROM XHB_CASE xc
      INNER JOIN XHB_DEFENDANT_ON_CASE xdc ON xc.CASE_ID = xdc.CASE_ID AND (xdc.OBS_IND <> 'Y' OR xdc.OBS_IND IS NULL)
      INNER JOIN XHB_DEFENDANT xd ON xdc.DEFENDANT_ID = xd.DEFENDANT_ID
      INNER JOIN XHB_COURT xco ON xc.COURT_ID = xco.COURT_ID
      LEFT JOIN XHB_CASE_PROSECUTOR_AGENCY xcpa ON xcpa.CASE_PROS_AGENCY_ID = get_prosecutor (xc.CASE_ID)
      LEFT JOIN XHB_REF_PROSECUTOR_AGENCY xrpa ON xcpa.REF_PROSECUTOR_AGENCY_ID = xrpa.REF_PROSECUTOR_AGENCY_ID AND (xrpa.OBS_IND <> 'Y' OR xrpa.OBS_IND IS NULL)
      LEFT JOIN XHB_REF_COURT xrc ON xc.CCC_TRANS_TO_REF_COURT_ID = xrc.REF_COURT_ID AND (xrc.OBS_IND <> 'Y' OR xrc.OBS_IND IS NULL)
      LEFT JOIN XHB_REF_SOLICITOR_FIRM xrs ON GET_SOLICITOR_ID(xdc.DEFENDANT_ON_CASE_ID) = xrs.REF_SOLICITOR_FIRM_ID
      LEFT JOIN XHB_ADDRESS xa1 ON xrc.ADDRESS_ID = xa1.ADDRESS_ID
      LEFT JOIN XHB_ADDRESS xa2 ON xrpa.ADDRESS_ID = xa2.ADDRESS_ID
      LEFT JOIN XHB_ADDRESS xa3 ON xd.ADDRESS_ID = xa3.ADDRESS_ID
      LEFT JOIN XHB_ADDRESS xa4 ON xrs.ADDRESS_ID = xa4.ADDRESS_ID
	  LEFT JOIN XHB_ADDRESS xa5 ON xco.ADDRESS_ID = xa5.ADDRESS_ID
      LEFT JOIN XHB_CONTACT_DETAIL xcd1 ON xa2.ADDRESS_ID = xcd1.ADDRESS_ID AND xcd1.CONTACT_TYPE = 'Non Secure Email'
      LEFT JOIN XHB_CONTACT_DETAIL xcd2 ON xa4.ADDRESS_ID = xcd2.ADDRESS_ID AND xcd2.CONTACT_TYPE = 'Non Secure Email'

      WHERE xc.COURT_ID = p_court_id AND xc.CASE_ID = p_case_id
      AND (xc.TRANSFERRED_CASE = 'Y' OR xc.DATE_TRANS_TO IS NOT NULL)
      
      ORDER BY rownumber;
        
    UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE CREST_REPORT_CODE = 'NTRSF' AND COURT_ID = p_court_id;
        
  EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_NTRSF_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
        
 END get_ntrsf_report;

/************************************************************************************
*
* Function GET_EARLIEST_LISTING_DATE
*
* Called from  get_defendants_put_back_report 
* Looks for listings in XHB_CASE_DIARY_FIXTURE and XHB_CASE_ON_LIST, returns the earliest 
* listing found (CTX-3586) that is in the future (i.e. after today). 
*
*
**************************************************************************************/
FUNCTION GET_EARLIEST_LISTING_DATE(p_case_id IN xhb_case.CASE_ID%TYPE 
                                 , p_def_case_id IN xhb_defendant_on_case.DEFENDANT_ON_CASE_ID%TYPE) RETURN DATE IS
  listing_date  DATE := NULL;
  listing_date1 DATE;
  listing_date2 DATE;
  
  BEGIN
 
   SELECT MIN(xcdf2.LISTING_DATE) INTO listing_date1
   FROM   XHB_CASE_DIARY_FIXTURE xcdf2, XHB_CASE_LISTING_ENTRY xcle 
   WHERE  xcdf2.CASE_LISTING_ENTRY_ID =  xcle.CASE_LISTING_ENTRY_ID 
   AND    xcdf2.LISTING_DATE >= trunc(sysdate) + 1
   AND    xcle.CASE_ID = p_case_id;
 
   SELECT MIN(xcol2.TIME_LISTED) INTO listing_date2
   FROM XHB_CASE_ON_LIST xcol2, XHB_DEF_ON_CASE_ON_LIST xdocol
   WHERE xcol2.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
   AND   xcol2.TIME_LISTED >= trunc(sysdate) + 1
   AND   xdocol.DEFENDANT_ON_CASE_ID = p_def_case_id;
   
   IF listing_date1 IS NULL THEN
     listing_date := listing_date2;
   ELSIF listing_date2 IS NULL THEN
     listing_date := listing_date1;
   ELSE
     listing_date := LEAST(listing_date1, listing_date2);
   END IF;
   
  RETURN LISTING_DATE;
  
   EXCEPTION
   WHEN OTHERS THEN
		RETURN (SQLCODE || ' ' || 'When others exception in GET_EARLIEST_LISTING_DATE: ' || SQLERRM);
  
 END GET_EARLIEST_LISTING_DATE;
 
PROCEDURE get_rage_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id IN XHB_CASE.COURT_ID%TYPE
                          ,p_bc_status IN XHB_DEFENDANT_ON_CASE.CURRENT_BC_STATUS%TYPE
                          ,p_class_code_list IN VARCHAR2
                          ,p_from_between IN INTEGER
                          ,p_to_between IN INTEGER) AS
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);

BEGIN
     OPEN p_results_out FOR 
      SELECT
      xc.CASE_TYPE || TO_CHAR(xc.CASE_NUMBER) AS casenumber,
      xc.CASE_TITLE AS casetitle,
      GET_JUVENILE_STATUS(xc.CASE_ID) AS juvenile,
      TO_CHAR(xc.CLASS_CODE) AS classcode, 
      get_bc_status_ind(xc.CASE_ID) AS bcstatus,
      regexp_replace(TO_CHAR(NVL(xc.committal_date,xc.sent_for_trial_date),'DD Month YYYY'),'[[:space:]]+', chr(32)) AS commitalsent,
	  NVL(xc.committal_date,xc.sent_for_trial_date) As sortdate,
      GET_LINKED_CASES_LIST(p_court_id,xc.CASE_GROUP_NUMBER) AS linkedcaseslist,
      GET_LIST_HISTORY(xc.CASE_ID) AS listhistorylist,
      GET_DIARY_NOTES_LIST(xc.CASE_id) AS noteslist,
      xcrt.COURT_NAME AS sitecommited,
      TO_CHAR(TRUNC((SYSDATE-NVL(xc.committal_date,xc.sent_for_trial_date))/7)) AS weekdiff,
      GET_OFFENCES(xc.CASE_ID) AS offences,
      xrmc.MONITORING_CATEGORY_CODE AS monitoringcategory, 
      GET_BENCH_WARRANT_DATE(xc.CASE_ID) AS benchwarrantdate,
      TO_CHAR(xdfc.TRIAL_TIME_ESTIMATE) || DECODE(NVL(xdfc.TRIAL_TIME_UNIT,0),1,' Hr(s)',2,' Day(s)',3,' Wk(s)',4,' Mth(s)','') as timeestimate

      FROM XHB_CASE xc, XHB_COURT xcrt, XHB_REF_MONITORING_CATEGORY xrmc, XHB_DIRECTIONS_FOR_CASE xdfc
  
      WHERE xc.COURT_ID = p_court_id 
	  AND xc.case_type = 'T'
	  AND XHB_CASE_PKG.determine_case_status(xc.CASE_ID) = 'Open' 
	  AND  ((p_class_code_list IS NULL AND xc.CLASS_CODE IS NULL) 
			OR  (',' || NVL(p_class_code_list,'') || ',' LIKE '%,' || CAST(xc.CLASS_CODE AS VARCHAR(1000)) || ',%' AND p_class_code_list IS NOT NULL)) 
	  AND ((p_from_between IS NOT NULL AND p_to_between IS NULL AND NVL(xc.committal_date,xc.sent_for_trial_date) <= (SYSDATE - (p_from_between*7)))
			OR (p_from_between IS NOT NULL AND p_to_between IS NOT NULL AND NVL(xc.committal_date,xc.sent_for_trial_date) BETWEEN (SYSDATE - (p_to_between*7)) AND (SYSDATE - (p_from_between*7))))
	  AND (p_bc_status IS NULL 
			OR get_bc_status_ind(xc.CASE_ID) = NVL(p_bc_status,''))
	  AND xc.COURT_ID = xcrt.COURT_ID
      AND xc.MONITORING_CATEGORY_ID = xrmc.REF_MONITORING_CATEGORY_ID
      AND xc.case_id = xdfc.case_id(+)
      
      ORDER BY sortdate;
        
    UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE CREST_REPORT_CODE = 'RAGE' AND COURT_ID = p_court_id;
        
  EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_RAGE_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
END get_rage_report;

FUNCTION GET_BENCH_WARRANT_DATE(p_case_id IN XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE) RETURN DATE IS
v_bw_date DATE;

CURSOR get_bench_warrant_c IS
  SELECT xbw.BW_END_DATE
  FROM XHB_BW_HISTORY xbw
  WHERE xbw.DEFENDANT_ON_CASE_ID = p_case_id
    AND xbw.BW_END_DATE IS NOT NULL
    AND ROWNUM = 1;
  
BEGIN
  OPEN get_bench_warrant_c;
  FETCH get_bench_warrant_c INTO v_bw_date;
  CLOSE get_bench_warrant_c;
  RETURN v_bw_date;
  
END GET_BENCH_WARRANT_DATE;

FUNCTION GET_LINKED_CASES_LIST( p_court_id IN XHB_CASE.court_id%TYPE,
                                  p_group_number IN XHB_CASE.CASE_GROUP_NUMBER%TYPE) RETURN CLOB IS

linked_cases_list CLOB := NULL;                      
pos int;

    CURSOR linked_cases_list_c IS
      SELECT xc.CASE_TYPE || TO_CHAR(xc.CASE_NUMBER) AS casenumber
      FROM 	XHB_CASE xc
      WHERE xc.CASE_GROUP_NUMBER = p_group_number
      AND xc.CASE_GROUP_NUMBER IS NOT NULL
      AND xc.COURT_ID = p_court_id;
      
BEGIN
  IF p_group_number IS NOT NULL THEN 
        FOR linked_cases_list_row IN linked_cases_list_c
    LOOP
    
    linked_cases_list:= linked_cases_list || linked_cases_list_row.casenumber || '|';
    
    END LOOP;

  pos:=INSTR(linked_cases_list,'|',-1);
  
  IF (pos>0) THEN
    linked_cases_list:=REGEXP_REPLACE(linked_cases_list,'|','',pos,1);
  END IF;
  END IF;
  
  RETURN linked_cases_list;
                             
END GET_LINKED_CASES_LIST;

FUNCTION GET_JUVENILE_STATUS(p_case_id IN XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE) RETURN VARCHAR2 IS
v_juv_status VARCHAR2(1);

CURSOR get_juvenile_status_c IS
  SELECT 'Y'
  FROM XHB_DEFENDANT_ON_CASE xdoc
  WHERE xdoc.CASE_ID = p_case_id
  AND NVL(xdoc.IS_JUVENILE,'N') ='Y'
  AND ROWNUM = 1;
  
BEGIN
  OPEN get_juvenile_status_c;
  FETCH get_juvenile_status_c INTO v_juv_status;
  CLOSE get_juvenile_status_c;
  RETURN NVL(v_juv_status,'N');
END GET_JUVENILE_STATUS;

FUNCTION GET_OFFENCES(p_case_id IN xhb_case.case_id%TYPE) RETURN CLOB IS
offences_list CLOB := NULL;
pos int;

  CURSOR offences_list_c IS
  SELECT xcl.CHARGES_INFO AS charges
  FROM XHB_CHARGES_LOG xcl
  WHERE xcl.CASE_ID = p_case_id;
  
  BEGIN
  
  FOR offences_list_row IN offences_list_c
    LOOP 
      offences_list:= offences_list || offences_list_row.charges || '|';
    END LOOP;

  pos:=INSTR(offences_list,'|',-1);
  
  IF (pos>0) THEN
    offences_list:=REGEXP_REPLACE(offences_list,'|','',pos,1);
  END IF;
  
  RETURN offences_list;

EXCEPTION
   WHEN OTHERS THEN
		RETURN (SQLCODE||' '||'When others exception ' || SQLERRM);

END GET_OFFENCES;

FUNCTION GET_DIARY_NOTES_LIST(p_case_id IN xhb_case.case_id%TYPE) RETURN CLOB IS
	diary_notes_list CLOB :=NULL;
	pos int;
	l_case_listing_entry_id	xhb_case_listing_entry.case_listing_entry_id%TYPE;

	CURSOR diary_notes_list_c IS
	SELECT CASE xrld.REF_DATA_VALUE
		WHEN 'HN' THEN 1
		WHEN 'IN' THEN 2
		WHEN 'CN' THEN 3
		WHEN 'GDN' THEN 4
		ELSE 5 
	END AS sort_order,
	CASE xrld.REF_DATA_VALUE
		WHEN 'HN' THEN 'H ' || TO_CHAR(xde.CREATION_DATE,'DD-Mon-YYYY') || ' ' || xde.DIARY_NOTE_TEXT
		WHEN 'IN' THEN 'I ' || TO_CHAR(xde.CREATION_DATE,'DD-Mon-YYYY') || ' ' || xde.DIARY_NOTE_TEXT
		WHEN 'CN' THEN 'C ' || TO_CHAR(xde.CREATION_DATE,'DD-Mon-YYYY') || ' ' || xde.DIARY_NOTE_TEXT
		WHEN 'GDN' THEN 'D ' || TO_CHAR(xde.CREATION_DATE,'DD-Mon-YYYY') || ' ' || xde.DIARY_NOTE_TEXT || ' [' || TO_CHAR(xde.DIARY_DATE,'DD-Mon-YYYY') || ']'
		ELSE NULL 
	END AS diarynote,
	xde.CREATION_DATE,
	xde.diary_note_entry_id
	FROM XHB_DIARY_NOTE_ENTRY xde,XHB_REF_LISTING_DATA xrld
	WHERE 
	xrld.REF_LISTING_DATA_ID = xde.NOTE_TYPE_ID AND
	xrld.REF_DATA_TYPE = 'NOTE_TYPE' AND
	xrld.REF_DATA_VALUE IN ('IN','HN','CN','GDN') AND
	xrld.REF_LISTING_DATA_ID = xde.NOTE_TYPE_ID AND
	(xde.CASE_ID = p_case_id OR (l_case_listing_entry_id IS NOT NULL AND xde.case_listing_entry_id = l_case_listing_entry_id) ) AND
	xde.DIARY_NOTE_TEXT IS NOT NULL AND
	(xde.OBS_IND <> 'Y' OR xde.OBS_IND is NULL)
	ORDER BY sort_order, xde.CREATION_DATE, xde.diary_note_entry_id;
  
BEGIN
  
	BEGIN
		SELECT case_listing_entry_id INTO l_case_listing_entry_id
		FROM xhb_case_listing_entry WHERE case_id = p_case_id
		AND NVL(obs_ind,'N') <> 'Y';
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			l_case_listing_entry_id := NULL;
	END;
  
	FOR diary_notes_list_row IN diary_notes_list_c
	LOOP 
		diary_notes_list:= diary_notes_list || diary_notes_list_row.diarynote || '|';
	END LOOP;

	pos:=INSTR(diary_notes_list,'|',-1);

	IF (pos>0) THEN
		diary_notes_list:=REGEXP_REPLACE(diary_notes_list,'|','',pos,1);
	END IF;

	RETURN diary_notes_list;

EXCEPTION
   WHEN OTHERS THEN
		RETURN (SQLCODE||' '||'When others exception ' || SQLERRM);

END GET_DIARY_NOTES_LIST;

FUNCTION GET_LIST_HISTORY(p_case_id IN xhb_case.CASE_ID%TYPE) RETURN CLOB IS
history_list CLOB := NULL;
list_suffix VARCHAR(4):= 'LIST';
pos int;

CURSOR history_list_c IS
 SELECT subquery.* FROM  
         (SELECT xcol.CASE_ON_LIST_ID,
                NULL CASE_DIARY_FIXTURE_ID,
                TRUNC(xcol.TIME_LISTED) as LIST_START_DATE,
                xlluv.LIST_END_DATE,
                xrld.REF_DATA_VALUE as LIST_TYPE,
                xrht.HEARING_TYPE_DESC as HEARING_DESC,
                xlluv.LIST_NUMBER,
                xlluv.LAST_UPDATE_DATE,
                xcol.DATE_OF_REMOVAL,
                xcol.REASON_FOR_REMOVAL
          FROM XHB_CASE_ON_LIST xcol,
               XHB_SITTING_ON_LIST xsol,
               XHB_LIST_LAST_UPDATED_V xlluv,
               XHB_REF_LISTING_DATA xrld,
               XHB_REF_HEARING_TYPE xrht
          WHERE xcol.CASE_ID = p_case_id
		  AND (NVL(xcol.obs_ind,'N') = 'N' OR (NVL(xcol.obs_ind,'N') = 'Y' AND xcol.REASON_FOR_REMOVAL IS NOT NULL))
          AND xlluv.LIST_ID = xcol.LIST_ID
          AND xrld.REF_LISTING_DATA_ID = xlluv.LIST_TYPE_ID
          AND xcol.LIST_ID = xlluv.LIST_ID
          AND xrht.REF_HEARING_TYPE_ID = xcol.HEARING_TYPE_ID
          AND xrld.REF_DATA_TYPE = 'LIST_TYPE'
          AND xcol.SITTING_ON_LIST_ID = xsol.SITTING_ON_LIST_ID(+)
          UNION ALL
          SELECT NULL CASE_ON_LIST_ID,
                xcdf.CASE_DIARY_FIXTURE_ID,
                xcdf.LISTING_DATE as LIST_START_DATE,
                xcdf.LISTING_DATE as LIST_END_DATE,
                'Fixture' as LIST_TYPE,
                xrht.HEARING_TYPE_DESC as HEARING_DESC,
                NULL LIST_NUMBER,
                xcdf.LAST_UPDATE_DATE,
                xcdf.DATE_VACATED as DATE_OF_REMOVAL,
                NVL(xrsc.DE_CODE,xcdf.VACATION_FREETEXT_REASON) as REASON_FOR_REMOVAL              
          FROM XHB_CASE_DIARY_FIXTURE xcdf,
               XHB_CASE_LISTING_ENTRY xcle,
               XHB_REF_SYSTEM_CODE xrsc,
               XHB_REF_HEARING_TYPE xrht
          WHERE xcle.CASE_LISTING_ENTRY_ID = xcdf.CASE_LISTING_ENTRY_ID
            AND xrht.REF_HEARING_TYPE_ID(+) = xcdf.HEARING_TYPE_ID
            AND xrsc.REF_SYSTEM_CODE_ID(+) = xcdf.VACATION_PRE_DEFINED_RSON_ID
            AND xcle.CASE_ID = p_case_id
			AND NVL(xrsc.OBS_IND(+),'N') <> 'Y'
          ORDER BY 3 DESC) subquery;
          
  BEGIN
 
  FOR history_list_row IN history_list_c
   LOOP
    IF history_list_row.DATE_OF_REMOVAL IS NULL AND history_list_row.LIST_TYPE <> 'Fixture' THEN
      history_list:=history_list || history_list_row.LIST_TYPE || ' ' || LIST_SUFFIX || ' ' || history_list_row.LIST_START_DATE || ' ' || history_list_row.HEARING_DESC || '|';
    ELSIF history_list_row.DATE_OF_REMOVAL IS NULL AND history_list_row.LIST_TYPE = 'Fixture' THEN
      history_list:=history_list || history_list_row.LIST_TYPE || ' ' || history_list_row.LIST_START_DATE || ' ' || history_list_row.HEARING_DESC || '|';
    ELSIF history_list_row.DATE_OF_REMOVAL IS NOT NULL THEN
      history_list:=history_list || 'Removed ' || history_list_row.DATE_OF_REMOVAL || ' ' || history_list_row.REASON_FOR_REMOVAL || '|';
    END IF;
   END LOOP;
    
  pos:=INSTR(history_list,'|',-1);
  
  IF (pos>0) THEN
    history_list:=REGEXP_REPLACE(history_list,',','|',pos,1);
  END IF;
 
 RETURN history_list;
          
END GET_LIST_HISTORY;

  /**********************************************************************************
  *
  * Procedure getRJS_Rpt  (CTX-1955)
  *
  * Returns a list of Judge Sittings
  *
  * Columns:      court_name, judge_name, judge_type, sitting_date,  
  *               sat_in_chambers, sat_in_court, total
  *
  **************************************************************************************/

PROCEDURE getRJS_Rpt(p_resultset OUT SYS_REFCURSOR 
                    ,p_court_id  IN  XHB_REF_COURT.REF_COURT_ID%TYPE
					,p_sitting_date IN XHB_JUDGE_USAGE.SITTING_DATE%TYPE) AS 
                                           
-- Local variables here
v_rpt_name      VARCHAR2(30)  := 'Report of Judge Sittings';
v_rpt_code      VARCHAR2(6)   := 'RJS';
v_user          VARCHAR2(30);
v_step          VARCHAR2(5);
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);

BEGIN  
    v_step := '1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;
    
    v_step := '2';    
    open p_resultset for
    -- Query for Judge Sittings Report (RJS)
     SELECT 
		  c.court_name as COURT_NAME,
    	  0 AS total_sat_in_chambers,
		  0 AS total_sat_in_court,
	      0 AS total_all_sittings,
		  MAX(j.surname || ', ' || substr(j.first_name,1,1)) as JUDGE_NAME,
		  INITCAP(ref_system_code.de_code)  AS JUDGE_TYPE,
		  trunc(last_day(judge_usage.sitting_date),'DD') AS SITTING_DATE,
		  SUM(CASE 
			judge_usage.court_chambers_ind
			when 'CHA' THEN 1 ELSE 0
		  END) AS SAT_IN_CHAMBERS,
		  SUM(CASE
			judge_usage.court_chambers_ind
			when 'CRT' THEN 1 ELSE 0
		  END) AS SAT_IN_COURT,
		  SUM(CASE
			judge_usage.court_chambers_ind
			when 'CRT' THEN 1
			when 'CHA' THEN 1 
			ELSE 0
		  END) AS Total,
		  CASE INITCAP(ref_system_code.de_code)
		  WHEN 'High Court Judge' THEN 1
		  WHEN 'Circuit Judge' THEN 2
		  WHEN 'Recorder' THEN 3
		  WHEN 'Assistant Recorder' THEN 4
		  ELSE 5 END AS JUDGE_TYPE_SORT_ORDER
		FROM 
		  xhb_judge_usage judge_usage
		JOIN
		  xhb_court_room room
		ON
		  judge_usage.court_room_id = room.court_room_id
		JOIN
		  xhb_court_site site
		ON
		  room.court_site_id = site.court_site_id
		JOIN
		  xhb_court c
		ON
		  site.court_id = c.court_id
		JOIN
		  xhb_REF_JUDGE j
		ON
		  judge_usage.ref_judge_id = j.ref_judge_id
    JOIN
			xhb_ref_system_code ref_system_code
		ON
		   ref_system_code.code = j.judge_type
       AND ref_system_code.court_id = c.court_id
       AND ref_system_code.code_type= 'JUDGE_TYPE'
       AND NVL(ref_system_code.code_type, '-') <> 'Y'
	   AND NVL(ref_system_code.OBS_IND,'N') <> 'Y'
    WHERE
		  c.court_id = p_court_id
		  AND judge_usage.court_chambers_ind in ('CRT','CHA')
		  AND trunc(last_day(judge_usage.sitting_date),'DD') = trunc(last_day(p_sitting_date),'DD')
		GROUP BY
		  COURT_NAME, j.ref_judge_id,j.judge_type, trunc(last_day(judge_usage.sitting_date),'DD'),ref_system_code.de_code
		ORDER BY
      		JUDGE_TYPE_SORT_ORDER, JUDGE_NAME asc;
    -- Now we have to log that we have run this report.
    v_step := '3';
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
                         
    EXCEPTION     
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' || v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;  
   
END getRJS_Rpt;  


  /**********************************************************************************
  *
  * Function get_RREC_CaseType_Hdr  (CTX-2160)
  *
  * Returns the text for the case type code of a case 
  * Takes a case_id as input parameter.
  *
  **************************************************************************************/
FUNCTION get_RREC_CaseType_Hdr(p_case_id  xhb_case.case_id%type) RETURN VARCHAR2 IS 

	v_retval              varchar2(50);

BEGIN
	SELECT DECODE(NVL(case_type,'N/A'), 'T', 'FOR TRIAL', 'S', 'FOR SENTENCE', 'A', 'APPEALS', 'OTHER') INTO v_retval
	FROM xhb_case
	WHERE case_id = p_case_id;
		
    RETURN v_retval;
         
END get_RREC_CaseType_Hdr;

  /**********************************************************************************
  *
  * Function get_RREC_CaseType_Subhdr  (CTX-2160)
  *
  * Returns the sub-heading of a case, which is different according to case type 
  * Takes a case_id as input parameter.  
  *
  **************************************************************************************/
FUNCTION get_RREC_CaseType_Subhdr(p_case_id  xhb_case.case_id%type) RETURN VARCHAR2 IS 
                     
v_case_type           xhb_case.case_type%type;                            -- varchar2(1)
v_class_code          xhb_case.class_code%type;
v_case_receipt_type   xhb_case.receipt_type%type;                         -- varchar2(2)
v_case_sub_type       varchar2(2);    -- xhb_case.case_sub_type%type;     -- varchar2(1)
v_court_id            xhb_case.court_id%type;  
v_retval              varchar2(50);
v_path                varchar2(20);

BEGIN
    v_path := '0';
    -- First we get the information about the case.
    SELECT nvl(case_type, 'N') as case_type, nvl(class_code, 0) as class_code, upper(nvl(receipt_type, 'NL')) as receipt_type, nvl(case_sub_type , 'NL') as case_sub_type, court_id 
    INTO   v_case_type, v_class_code, v_case_receipt_type, v_case_sub_type, v_court_id 
    FROM   xhb_case 
    WHERE  case_id = p_case_id;
     
    CASE Upper(v_case_type) -- Normally is upper case but don't want to be tripped up by a rogue lower case entry 
        WHEN 'A' THEN  -- Appeals
            v_path := '1';
            CASE Upper(v_case_sub_type)
                WHEN 'B' THEN
                    v_path := '11';
                    v_retval := 'Appeal against Conviction and Sentence';
                    
                WHEN 'C' THEN  -- We map 'C' to 'B' (both)
                    v_path := '12';
                    v_retval := 'Appeal against Conviction and Sentence';              
                
                WHEN 'O' THEN
                    v_path := '13';
                    v_retval := 'Other Appeal';
                    
                WHEN 'S' THEN
                    v_path := '14';
                    v_retval := 'Appeal against Sentence';
                    
                WHEN 'NL' THEN
                    v_path := '15';
                    v_retval := 'Null Case Sub_Type';
                    
                ELSE
                    v_path := '16';
                    v_retval := 'NULL';
        END CASE;
         
        WHEN 'S' THEN  -- for Sentencing
		  BEGIN
            v_path := '2';
            -- Lookup the case receipt type
            IF v_case_receipt_type = 'NL' THEN
                v_path := '21';
                v_retval := 'NULL';   
            ELSE
                v_path := '22';
                SELECT initcap(rsc.de_code)
                into   v_retval
                from   xhb_ref_system_code rsc 
                where  rsc.code_type = 'CASE_RECEIPT_TYPE' 
                and    rsc.code = v_case_receipt_type 
                and    rsc.court_id = v_court_id   -- This is needed because each court has its own data in this table.
                and    nvl(rsc.obs_ind, 'N') != 'Y';
                
                IF v_retval is null then 
                    v_path := '221';
                    v_retval := 'NULL';
                END IF;
            END IF;
		  END;
 
        WHEN 'T'  THEN  -- for Trial 
         BEGIN
            v_path := '3';
            IF v_class_code = 0 THEN
                v_path := '31';
                v_retval := 'NULL';            
            ELSE
                v_path := '32';
                v_retval := 'Class ' || v_class_code;
            END IF;
          END;

        WHEN 'N'  THEN  -- Null case type
         BEGIN
                v_path := '4';
                v_retval := 'NULL';            		 
		 END;
		ELSE 
            v_path := '5';
            -- This should not happen
            v_retval := 'NULL';            
    END CASE;

	IF v_retval is null then v_retval := 'get_RREC_CaseType_Subhdr;' || v_path; end if;
     
    Return v_retval;
         
END get_RREC_CaseType_Subhdr;

/**********************************************************************************
*
* Function is_valid_RREC_Case
*
* Determines if a case is valid for the RREC report based upon case type and other
* properties
* Takes a case_id as input parameter.
*
**************************************************************************************/
FUNCTION is_valid_RREC_Case (p_case_id  xhb_case.case_id%type) RETURN VARCHAR2 IS

	v_retval		VARCHAR2(1) := 'N';
	v_case_type		xhb_case.case_type%TYPE;
	v_class_code	xhb_case.class_code%TYPE;
	v_receipt_type	xhb_case.receipt_type%TYPE;
	v_sub_type		xhb_case.case_sub_type%TYPE;

BEGIN
	SELECT NVL(case_type, '-'), NVL(class_code, 0), NVL(receipt_type, '-'), NVL(case_sub_type, '-')
	INTO v_case_type, v_class_code, v_receipt_type, v_sub_type
	FROM xhb_case
	WHERE case_id = p_case_id;
	
	IF v_case_type = 'T' AND v_class_code IN (1, 2, 3) THEN
		v_retval := 'Y';
	ELSIF v_case_type = 'S' AND v_receipt_type IN ('CS', 'CB', 'BB') THEN
		v_retval := 'Y';
	ELSIF v_case_type = 'A' AND v_sub_type IN ('B', 'S', 'O', 'C') THEN
		v_retval := 'Y';
	ELSE
		v_retval := 'N';
	END IF;
		
    RETURN v_retval;

END is_valid_RREC_Case;

/**********************************************************************************
*
* Function is_dealtwith_RREC_Case
*
* Determines if a case is dealt with for the RREC report based upon case type and other
* properties
* Takes a case_id and start and end dates and court_id as input parameters.
* Returns Y if dealt with, N otherwise.
*
**************************************************************************************/
FUNCTION is_dealtwith_RREC_Case (p_case_id    xhb_case.case_id%type 
                               , p_start_date DATE 
							   , p_end_date   DATE 
							   , p_court_id   XHB_COURT.COURT_ID%TYPE) RETURN VARCHAR2 IS
							   
	CURSOR c_verdict_no_end IS
	Select count(xv.VERDICT_ID)
	From   XHB_VERDICT xv, xhb_ref_system_code xrsc
	Where  xv.case_id = p_case_id
	and    nvl(xv.obs_ind, 'N') != 'Y'
	and    xv.VERDICT_DATE < p_start_date
	and    xv.ref_verdict_id is not null
	AND    xrsc.ref_system_code_id = xv.ref_verdict_id
	AND    xrsc.code NOT IN ('NH','PH')
	AND    NVL(xrsc.OBS_IND,'N') <> 'Y';
	
	CURSOR c_verdict_with_end IS
	Select count(xv.VERDICT_ID)
	From   XHB_VERDICT xv, xhb_ref_system_code xrsc
	Where  xv.case_id = p_case_id
	and    nvl(xv.obs_ind, 'N') != 'Y'
	and    TRUNC(xv.VERDICT_DATE) >= p_start_date AND TRUNC(xv.VERDICT_DATE) <= p_end_date
	and    xv.ref_verdict_id is not null
	AND    xrsc.ref_system_code_id = xv.ref_verdict_id
	AND    xrsc.code NOT IN ('NH','PH')
	AND    NVL(xrsc.OBS_IND,'N') <> 'Y';
	
	CURSOR c_exported_at_start IS
	Select count(doc.defendant_on_case_id)
	From   xhb_defendant_on_case doc
	where  doc.date_exported < p_start_date
	and    nvl(doc.obs_ind, 'N') <> 'Y'
	and    doc.case_id = p_case_id;
	
	CURSOR c_exported_at_end IS
	Select count(doc.defendant_on_case_id)
	From   xhb_defendant_on_case doc
	where  TRUNC(doc.date_exported) <= p_end_date
	and    nvl(doc.obs_ind, 'N') <> 'Y'
	and    doc.case_id = p_case_id;
	
	CURSOR c_exported_in_period IS
	Select count(doc.defendant_on_case_id)
	From   xhb_defendant_on_case doc
	where  TRUNC(doc.date_exported) >= p_start_date AND TRUNC(doc.date_exported) <= p_end_date
	and    nvl(doc.obs_ind, 'N') <> 'Y'
	and    doc.case_id = p_case_id;
	
	CURSOR c_bw_at_start IS
	SELECT COUNT(*)
	FROM xhb_defendant_on_case xdoc, xhb_bw_history xbh
	WHERE xdoc.case_id = p_case_id
	AND NVL(xdoc.obs_ind,'N') = 'N'
	AND (xdoc.date_exported IS NULL OR TRUNC(xdoc.date_exported) >= p_start_date)
	AND xbh.defendant_on_case_id = xdoc.defendant_on_case_id
	AND NVL(xbh.obs_ind,'N') = 'N'
	AND xbh.bw_issue_date IS NOT NULL 
	AND xbh.bw_issue_date < p_start_date
	AND (xbh.bw_end_date IS NULL OR xbh.bw_end_date > p_start_date);
	
	CURSOR c_bw_at_end IS
	SELECT COUNT(*)
	FROM xhb_defendant_on_case xdoc, xhb_bw_history xbh
	WHERE xdoc.case_id = p_case_id
	AND NVL(xdoc.obs_ind,'N') = 'N'
	AND (xdoc.date_exported IS NULL OR TRUNC(xdoc.date_exported) > p_end_date)
	AND xbh.defendant_on_case_id = xdoc.defendant_on_case_id
	AND NVL(xbh.obs_ind,'N') = 'N'
	AND xbh.bw_issue_date IS NOT NULL 
	AND xbh.bw_issue_date <= p_end_date
	AND (xbh.bw_end_date IS NULL OR xbh.bw_end_date > p_end_date);
	
	CURSOR c_bw_in_period IS
	SELECT COUNT(*)
	FROM xhb_defendant_on_case xdoc, xhb_bw_history xbh
	WHERE xdoc.case_id = p_case_id
	AND NVL(xdoc.obs_ind,'N') = 'N'
	AND (xdoc.date_exported IS NULL OR TRUNC(xdoc.date_exported) > p_end_date)
	AND xbh.defendant_on_case_id = xdoc.defendant_on_case_id
	AND NVL(xbh.obs_ind,'N') = 'N'
	AND xbh.bw_issue_date IS NOT NULL 
	AND TRUNC(xbh.bw_issue_date) >= p_start_date AND TRUNC(xbh.bw_issue_date) <= p_end_date
	AND (xbh.bw_end_date IS NULL OR xbh.bw_end_date > p_end_date);

	v_retval		         VARCHAR2(1) := 'N';
	v_case_type		       xhb_case.case_type%TYPE;
	v_sub_type		       xhb_case.case_sub_type%TYPE;
	v_date_trans_rec_to  xhb_case.date_trans_recorded_to%TYPE;
	v_date_trans_to  xhb_case.date_trans_to%TYPE;
	v_verdict_count      NUMBER(8) := 0;
	v_exported_count 	NUMBER(8) := 0;
	v_exp_in_period_cnt	NUMBER(8) := 0;
	v_def_count          NUMBER(8) := 0;
	v_bw_count           NUMBER(8) := 0;
	v_bw_iss_in_period_cnt  NUMBER(8) := 0;

BEGIN
	SELECT NVL(case_type, '-'), NVL(case_sub_type, '-'), nvl(date_trans_recorded_to, p_start_date), date_trans_to
	INTO v_case_type, v_sub_type, v_date_trans_rec_to, v_date_trans_to
	FROM xhb_case
	WHERE case_id = p_case_id;
	
	IF p_end_date IS NULL AND v_date_trans_to IS NOT NULL AND v_date_trans_rec_to < p_start_date THEN
		-- Transferred out (not required for section 7 of RREC where end date is specified)
        v_retval := 'Y';
		GOTO end_function;
	END IF;
		
	IF v_case_type = 'A' AND v_sub_type = 'O' THEN
		-- Miscellaneous Appeal Case
		IF p_end_date IS NULL THEN
			OPEN c_verdict_no_end;
			FETCH c_verdict_no_end INTO v_verdict_count;
			CLOSE c_verdict_no_end;
		ELSE
			OPEN c_verdict_with_end;
			FETCH c_verdict_with_end INTO v_verdict_count;
			CLOSE c_verdict_with_end;
		END IF;
		
		IF v_verdict_count > 0 THEN
			-- Any verdicts mean that the case is Dealt With
			v_retval := 'Y';
			GOTO end_function;
		END IF;
		
	ELSE
		-- Non-Miscellaneous Appeal Case
		-- Get total number of defendants
		SELECT COUNT(*) INTO v_def_count FROM xhb_defendant_on_case WHERE case_id = p_case_id AND NVL(obs_ind, 'N') <> 'Y';
		IF v_def_count > 0 THEN 
		
			IF p_end_date IS NULL THEN
				-- Running for section 1 of the report (Outstanding at start of period)
				-- See if there are any defendants that have been exported before the report start date
				OPEN c_exported_at_start;
				FETCH c_exported_at_start INTO v_exported_count;
				CLOSE c_exported_at_start;
				
				IF v_def_count = v_exported_count THEN
					-- All defendants have been exported prior to the start of the reporting period so case is dealt with/not outstanding
					v_retval := 'Y';
					GOTO end_function;
				END IF;
				
				-- Check for open bench warrants at the start of the period
				OPEN c_bw_at_start;
				FETCH c_bw_at_start INTO v_bw_count;
				CLOSE c_bw_at_start;
				
				IF v_def_count = (v_exported_count + v_bw_count) THEN
					-- All defendants either have been exported before the start date or had an open bench warrant before the start date
					-- Therefore case is dealt with/not outstanding
					v_retval := 'Y';
					GOTO end_function;
				END IF;
				
			ELSE
				-- Running for section 7 of the report (Dealt With)				
				-- Check if any defendants have been exported within the reporting period
				OPEN c_exported_in_period;
				FETCH c_exported_in_period INTO v_exp_in_period_cnt;
				CLOSE c_exported_in_period;
				
				IF v_exp_in_period_cnt = 0 THEN
					-- Check for any new bench warrants issued during the period and have not been closed in the period
					-- Only bother if no defendants have been exported during the period as if they have then this is a
					-- redundant query
					OPEN c_bw_in_period;
					FETCH c_bw_in_period INTO v_bw_iss_in_period_cnt;
					CLOSE c_bw_in_period;
				END IF;
				
				IF v_exp_in_period_cnt > 0 OR v_bw_iss_in_period_cnt > 0 THEN
					-- At least one defendant has been exported or had a bench warrant issued in the reporting period
					
					-- Check if any defendants have have been exported before the end of the reporting period
					OPEN c_exported_at_end;
					FETCH c_exported_at_end INTO v_exported_count;
					CLOSE c_exported_at_end;
					IF v_def_count = v_exported_count THEN
						-- All defendants have been exported including one in the reporting period so case is dealt with/not outstanding
						v_retval := 'Y';
						GOTO end_function;
					END IF;
					
					-- Check if any defendants that are not exported (or exported after the end of the reporting period) 
					-- and have a bench warrant open at the end of the reporting period
					OPEN c_bw_at_end;
					FETCH c_bw_at_end INTO v_bw_count;
					CLOSE c_bw_at_end;
					IF v_def_count = (v_exported_count + v_bw_count) THEN
						-- All defendants either have been exported before the reporting end date or had a bench warrant open 
						-- at the end of the reporting period, therefore case is dealt with/not outstanding
						v_retval := 'Y';
						GOTO end_function;
					END IF;
					
				END IF;
				
			END IF;
		
		END IF;

	END IF;
	
	<<end_function>>
    RETURN v_retval;

END is_dealtwith_RREC_Case;

/**********************************************************************************
*
* Function is_benchwarrant_RREC_Case
*
* Determines if a case has an executed bench warrant for the RREC report 
* Takes a case_id and start and end dates and court_id as input parameters.
* Returns Y if bench warrant, N otherwise.
*
**************************************************************************************/
FUNCTION is_benchwarrant_RREC_Case (p_case_id    xhb_case.case_id%type 
                               , p_start_date DATE 
							   , p_end_date   DATE) RETURN VARCHAR2 IS
							   
	CURSOR c_defendants IS
	SELECT doc.defendant_on_case_id, doc.date_exported, bwh.bw_history_id, bwh.bw_issue_date, bwh.bw_end_date
	FROM xhb_defendant_on_case doc, xhb_bw_history bwh
	WHERE doc.case_id = p_case_id
	AND NVL(doc.obs_ind, 'N') <> 'Y'
	AND bwh.defendant_on_case_id (+)= doc.defendant_on_case_id
	AND NVL(bwh.obs_ind(+), 'N') <> 'Y'
	AND bwh.bw_issue_date(+) < p_start_date
	ORDER BY doc.defendant_on_case_id, bwh.bw_history_id DESC;

	v_retval			VARCHAR2(1) := 'N';
	v_exported_count	NUMBER(8) := 0;
	v_def_count			NUMBER(8) := 0;
	v_bw_count 			NUMBER(8) := 0;
	v_current_def_id	xhb_defendant_on_case.defendant_on_case_id%TYPE := -1;

BEGIN
	-- Loop through all defendants on the case
	-- 		if defendant has NULL date_exported and no bench warrant, exit with 'N' as case will be open
	-- 		if bench warrant was issued >= p_start_date then exit with 'N'
	--		if bench warrant is NULL end date or end date is not in the report period, exit with 'N'
	FOR def_rec IN c_defendants LOOP
		IF v_current_def_id != def_rec.defendant_on_case_id THEN 
			-- Can get multiple rows for a single defendant returned in the cursor as can have multiple bench warrants.
			-- We are only interested in the most recent bench warrant issued before the reporting start date so ignore 
			-- anything but the first row for any given defendant
			v_current_def_id := def_rec.defendant_on_case_id;
			v_def_count := v_def_count + 1;
			
			IF def_rec.date_exported IS NULL AND def_rec.bw_issue_date IS NULL THEN
				-- Defendant is not exported and has no bench warrant so exit loop and function - no bench warrant
				GOTO end_function;
			END IF;
			
			IF def_rec.date_exported IS NOT NULL AND def_rec.date_exported < p_start_date THEN 
				-- Defendant is exported so skip this defendant and onto the next one as only interested in bench warrants
				v_exported_count := v_exported_count + 1;
			ELSE
				-- Not exported so check the bench warrant status
				IF TRUNC(def_rec.bw_issue_date) >= p_start_date THEN
					-- The Bench warrant has been issued after the report start date so exit loop and function as
					-- only interested in bench warrants issued before the report start date
					GOTO end_function;
				END IF;
				
				IF def_rec.bw_end_date IS NULL THEN
					-- Bench warrant is still open so exit loop and function
					GOTO end_function;
				END IF;
				
				IF TRUNC(def_rec.bw_end_date) >= p_start_date AND TRUNC(def_rec.bw_end_date) <= p_end_date THEN
					-- Bench Warrant End Date is within the reporting period
					v_bw_count := v_bw_count + 1;
				ELSE
					-- Bench Warrant End Date is NOT within the reporting period, exit loop and function
					GOTO end_function;
				END IF;
				
			END IF;
		
		END IF;

    END LOOP;

	IF v_bw_count > 0 AND (v_bw_count + v_exported_count) = v_def_count THEN
		-- There is at least one bench warrant closed in the reporting period and all defendants were either
		-- exported before the reporting start date or they had bench warrants issued before the reporting
		-- start date and have been closed during the reporting period.
		v_retval := 'Y';
	END IF;

	<<end_function>>
    RETURN v_retval;

END is_benchwarrant_RREC_Case;

/**********************************************************************************
  *
  * Procedure get_RREC_detail  (CTX-2160)
  *
  * Returns detail data for the RREC report (Cases Received and Disposed of)
  *
  *      Procedure get_RREC_detail (p_resultset  OUT SYS_REFCURSOR 
  *                              ,  p_court_id   IN  XHB_COURT.COURT_ID%TYPE
  *                              ,  p_end_date   IN  DATE ) 
  * The report supports a weekly and monthly mode. To invoke weekly mode, parameter p_end_date 
  * should be set to the Saturday at the end of the period.  In practice, for robustness it will 
  * accept any date and will establish a Sunday to Saturday period containing that date.
  * The functional spec does not place any constraints on which week is selected, but clearly
  * a date in the future is inappropriate and will cause this SP to error.  To invoke monthly mode, 
  * the p_end_date should be null. The month period reported on will be the month previous to the 
  * current month.
  **************************************************************************************/
  Procedure get_RREC_detail (p_resultset  OUT SYS_REFCURSOR 
                          ,  p_court_id    IN  XHB_COURT.COURT_ID%TYPE 
                          ,  p_end_date    IN  DATE ) IS 
    -- Local variables here
    v_err_code          NUMBER;
    v_err_msg           VARCHAR2(1000);
    v_rpt_name          VARCHAR2(30)  := 'Cases Received and Disposed of';
    v_rpt_code          VARCHAR2(6)   := 'RREC';
    v_user              VARCHAR2(30);
    v_day_of_week       INTEGER;
    v_start_date        DATE;
    v_end_date          DATE;
    
BEGIN  
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;                
    
    IF p_end_date is null then 
        -- Monthly mode.    
        v_start_date := TRUNC(LAST_DAY(ADD_MONTHS(SYSDATE, -2)) + 1);
        v_end_date   := TRUNC(LAST_DAY(ADD_MONTHS(SYSDATE, -1)));
	ELSE 
        -- Weekly mode
        v_day_of_week  := TO_NUMBER(TO_CHAR(p_end_date, 'd'));    -- Find out the day of the week for the date they supplied.
        v_end_date     := p_end_date  + (6 - v_day_of_week);      -- This makes sure the date we use is a Saturday
        v_start_date   := v_end_date - 7;
        
        IF v_end_date > sysdate THEN
            -- This is an error condition
            RAISE VALUE_ERROR;
        END IF;
    END IF;
    
    -- Put the report detail in the output variable.   
    OPEN   p_resultset FOR  
        select 7 as section_num, cs.court_site_name, get_RREC_CaseType_Hdr(cas.case_id) as case_type, cas.case_number as case_number, get_RREC_CaseType_Subhdr(cas.case_id) as case_subhdg, cas.case_id
        from   xhb_case cas, xhb_court_site cs  
        where  cas.court_id = p_court_id
        and    is_valid_RREC_Case(cas.case_id) = 'Y'
        and    cas.court_id_receiving_site = cs.court_site_id
        and   nvl(cs.obs_ind, 'N') != 'Y'
		AND	   (cs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite xcs WHERE xcs.court_site_id = cs.court_site_id AND NVL(xcs.obs_ind, 'N') <> 'Y'))
        AND    is_dealtwith_RREC_Case(cas.case_id, v_start_date, v_end_date, p_court_id) = 'Y'
        order by  cs.court_site_name, cas.case_type desc, case_subhdg, case_number; 
    
    -- Record that this report has been run.
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
    
    -- Exception handling 
    EXCEPTION
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in XHB_REPORT_PKG.GET_RREC_DETAIL. Err code ' || v_err_code || ' : ' ||SQLERRM;
            
            DBMS_OUTPUT.PUT_LINE('get_RREC_rpt:' || v_err_msg);
            
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;    
    
END get_RREC_detail;

/**********************************************************************************
  *
  * Procedure get_RREC_summary  (CTX-2160)
  *
  * Returns summary data for the RREC report (Cases Received and Disposed of)
  *
  *      Procedure get_RREC_summary (p_resultset  OUT SYS_REFCURSOR p_court_id   IN  XHB_COURT.COURT_ID%TYPE
  *                               ,  p_court_id   IN  XHB_COURT.COURT_ID%TYPE
  *                               ,  p_end_date   IN  DATE ) 
  * The report supports a weekly and monthly mode. To invoke weekly mode, parameter p_end_date 
  * should be set to the Saturday at the end of the period.  In practice, for robustness it will 
  * accept any date and will establish a Sunday to Saturday period containing that date.
  * The functional spec does not place any constraints on which week is selected, but clearly
  * a date in the future is inappropriate and will cause this SP to error.  To invoke monthly mode, 
  * the p_end_date should be null. The month period reported on will be the month previous to the 
  * current month.
  **************************************************************************************/
    Procedure get_RREC_summary(p_resultset  OUT SYS_REFCURSOR
                          ,  p_court_id    IN  XHB_COURT.COURT_ID%TYPE
                          ,  p_end_date    IN  DATE ) IS

    -- Local variables here
    v_err_code          NUMBER;
    v_err_msg           VARCHAR2(1000);
    v_step              VARCHAR2(10);
    v_rpt_name          VARCHAR2(50)  := 'Cases Received and Disposed of (Summary)';
    v_rpt_code          VARCHAR2(6)   := 'RREC';
    v_user              VARCHAR2(30);
    v_day_of_week       INTEGER;
    v_start_date        DATE;
    v_end_date          DATE;


    BEGIN
    v_step := '1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER')
    INTO   v_user
    FROM   dual;
    v_step := '2';

    IF p_end_date is null then
        -- Monthly mode.
        v_start_date := TRUNC(LAST_DAY(ADD_MONTHS(SYSDATE, -2)) + 1);
        v_end_date   := TRUNC(LAST_DAY(ADD_MONTHS(SYSDATE, -1)));

    ELSE
        v_step := '3';

        -- Weekly mode
        v_day_of_week  := TO_NUMBER(TO_CHAR(p_end_date, 'd'));    -- Find out the day of the week for the date they supplied.
        v_end_date     := p_end_date  + (6 - v_day_of_week);      -- This makes sure the date we use is a Saturday
        v_start_date   := v_end_date - 7;

        IF v_end_date > sysdate THEN
            -- This is an error condition
            v_step := '4';

            RAISE VALUE_ERROR;

        END IF;
    END IF;

    DBMS_OUTPUT.PUT_LINE('get_RREC_Summary: step ' || v_step || '  start date = ' || v_start_date || ', end date = ' || v_end_date);

      v_step := '5';
      open p_resultset for
		-- First part of the query returns totals for each subheading for each court site
        select 1 as sort_order, cs.court_site_name, get_RREC_CaseType_Hdr(cas.case_id) as case_type, get_RREC_CaseType_Subhdr(cas.case_id) as case_subhdg,
        sum(case when (nvl(cas.creation_date, v_start_date) < v_start_date)  -- ctx-3827 changed from received_date to creation_date
                 and is_dealtwith_RREC_Case(cas.case_id, v_start_date, NULL, p_court_id) = 'N' then 1 
			     Else 0 END) as section1,   -- Cases Outstanding At Start
        sum(case when  (TRUNC(cas.creation_date) >= v_start_date -- parameter here p_start_date
                 and    TRUNC(cas.creation_date) <= v_end_date   -- parameter here p_end_date
                 and    cas.date_trans_from is null ) then 1 else 0 end) as section2   ,
        sum(case when  (TRUNC(cas.creation_date)  <= v_end_date    -- parameter here
                 and   TRUNC(cas.creation_date)   >= v_start_date  
                 and   cas.date_trans_from is not null) then 1 else 0 end) as section3,
        sum(case when is_benchwarrant_RREC_Case(cas.case_id, v_start_date, v_end_date) = 'Y' then 1 else 0 end) as section4,  -- Bench Warrant Cases
        sum(case when (TRUNC(nvl(cas.date_trans_recorded_to, '01-Jan-1900'))  <= v_end_date   -- parameter here
                       and    TRUNC(nvl(cas.date_trans_recorded_to, '01-Jan-1900'))  >= v_start_date) then 1 else 0 end) as section5,  -- Cases Transferred Out
        0 as section6,         -- Section 6 is calculated from (6) = (2) + (3) + (4) - (5), but we just put in a dummy column
         sum(case when (is_dealtwith_RREC_Case(cas.case_id, v_start_date, v_end_date, p_court_id) = 'Y') then 1 
                  Else 0 END) as  section7,   -- Cases Dealt With
        0 as section8
        from  xhb_case cas, xhb_court_site cs
        where cas.court_id = p_court_id
        and   is_valid_RREC_Case(cas.case_id) = 'Y'
        and    cas.court_id_receiving_site = cs.court_site_id
        and   nvl(cs.obs_ind, 'N') != 'Y'
		AND	  (cs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite xcs WHERE xcs.court_site_id = cs.court_site_id AND NVL(xcs.obs_ind,'N') <> 'Y'))
        and   TRUNC(nvl(cas.received_date, v_start_date)) <= v_end_date -- parameter here
        group by cs.court_site_name, get_RREC_CaseType_Hdr(cas.case_id), get_RREC_CaseType_Subhdr(cas.case_id)
        UNION  -- The second part of the query calculates the totals for each case type for each court site
        select 1 as sort_order, cs.court_site_name, get_RREC_CaseType_Hdr(cas.case_id) as case_type, 'Total' as case_subhdg,
        sum(case when (nvl(cas.creation_date, v_start_date) < v_start_date)  -- ctx-3827 changed from received_date to creation_date
                 and is_dealtwith_RREC_Case(cas.case_id, v_start_date, NULL, p_court_id) = 'N' then 1 
			     Else 0 END) as section1,   -- Cases Outstanding At Start
        sum(case when  (TRUNC(cas.creation_date) >= v_start_date -- parameter here p_start_date
                 and    TRUNC(cas.creation_date) <= v_end_date   -- parameter here p_end_date
                 and    cas.date_trans_from is null ) then 1 else 0 end) as section2   ,
        sum(case when  (TRUNC(cas.creation_date)  <= v_end_date    -- parameter here
                 and   TRUNC(cas.creation_date)   >= v_start_date  
                 and   cas.date_trans_from is not null) then 1 else 0 end) as section3,
        sum(case when is_benchwarrant_RREC_Case(cas.case_id, v_start_date, v_end_date) = 'Y' then 1 else 0 end) as section4,  -- Bench Warrant Cases
        sum(case when (TRUNC(nvl(cas.date_trans_recorded_to, '01-Jan-1900'))  <= v_end_date   -- parameter here
                       and    TRUNC(nvl(cas.date_trans_recorded_to, '01-Jan-1900'))  >= v_start_date) then 1 else 0 end) as section5,  -- Cases Transferred Out
        0 as section6,         -- Section 6 is calculated from (6) = (2) + (3) + (4) - (5), but we just put in a dummy column
         sum(case when (is_dealtwith_RREC_Case(cas.case_id, v_start_date, v_end_date, p_court_id) = 'Y') then 1 
                  Else 0 END) as  section7,   -- Cases Dealt With
        0 as section8
        from  xhb_case cas, xhb_court_site cs
        where cas.court_id = p_court_id
        and   cas.court_id_receiving_site = cs.court_site_id
        and   is_valid_RREC_Case(cas.case_id) = 'Y'
        and   nvl(cs.obs_ind, 'N') != 'Y'
		AND	  (cs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite xcs WHERE xcs.court_site_id = cs.court_site_id AND NVL(xcs.obs_ind,'N') <> 'Y'))
        and   TRUNC(nvl(cas.received_date, v_start_date)) <= v_end_date -- parameter here
        group by cs.court_site_name, get_RREC_CaseType_Hdr(cas.case_id), 'Total'
		UNION	-- The third section returns the totals for each subtype for all sites at the court
		select 2 as sort_order, 'ALL' as court_site_name, get_RREC_CaseType_Hdr(cas.case_id) as case_type, get_RREC_CaseType_Subhdr(cas.case_id) as case_subhdg,
        sum(case when (nvl(cas.creation_date, v_start_date) < v_start_date)  -- ctx-3827 changed from received_date to creation_date
                 and is_dealtwith_RREC_Case(cas.case_id, v_start_date, NULL, p_court_id) = 'N' then 1 
			     Else 0 END) as section1,   -- Cases Outstanding At Start
        sum(case when  (TRUNC(cas.creation_date) >= v_start_date -- parameter here p_start_date
                 and    TRUNC(cas.creation_date) <= v_end_date   -- parameter here p_end_date
                 and    cas.date_trans_from is null ) then 1 else 0 end) as section2   ,
        sum(case when  (TRUNC(cas.creation_date)  <= v_end_date    -- parameter here
                 and   TRUNC(cas.creation_date)   >= v_start_date  
                 and   cas.date_trans_from is not null) then 1 else 0 end) as section3,
        sum(case when is_benchwarrant_RREC_Case(cas.case_id, v_start_date, v_end_date) = 'Y' then 1 else 0 end) as section4,  -- Bench Warrant Cases
        sum(case when (TRUNC(nvl(cas.date_trans_recorded_to, '01-Jan-1900'))  <= v_end_date   -- parameter here
                       and    TRUNC(nvl(cas.date_trans_recorded_to, '01-Jan-1900'))  >= v_start_date) then 1 else 0 end) as section5,  -- Cases Transferred Out
        0 as section6,         -- Section 6 is calculated from (6) = (2) + (3) + (4) - (5), but we just put in a dummy column
         sum(case when (is_dealtwith_RREC_Case(cas.case_id, v_start_date, v_end_date, p_court_id) = 'Y') then 1 
                  Else 0 END) as  section7,   -- Cases Dealt With
        0 as section8
        from  xhb_case cas
        where cas.court_id = p_court_id
        and   is_valid_RREC_Case(cas.case_id) = 'Y'
		and   NVL(cas.court_id_receiving_site,-1) in (
			select xcs.court_site_id 
			from xhb_court_site xcs 
			where xcs.court_id = p_court_id 
			and nvl(xcs.obs_ind,'N') <> 'Y'
			and	  (xcs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite sat WHERE sat.court_site_id = xcs.court_site_id AND NVL(sat.obs_ind,'N') <> 'Y')))
        and   TRUNC(nvl(cas.received_date, v_start_date)) <= v_end_date -- parameter here
        group by 'ALL', get_RREC_CaseType_Hdr(cas.case_id), get_RREC_CaseType_Subhdr(cas.case_id)
		UNION	-- The fourth section returns the totals for each type for all sites at the court
        select 2 as sort_order, 'ALL' as court_site_name, get_RREC_CaseType_Hdr(cas.case_id) as case_type, 'Total' as case_subhdg,
        sum(case when (nvl(cas.creation_date, v_start_date) < v_start_date)  -- ctx-3827 changed from received_date to creation_date
                 and is_dealtwith_RREC_Case(cas.case_id, v_start_date, NULL, p_court_id) = 'N' then 1 
			           Else 0 END) as section1,   -- Cases Outstanding At Start
        sum(case when  (TRUNC(cas.creation_date) >= v_start_date -- parameter here p_start_date
                 and    TRUNC(cas.creation_date) <= v_end_date   -- parameter here p_end_date
                 and    cas.date_trans_from is null ) then 1 else 0 end) as section2   ,
        sum(case when  (TRUNC(cas.creation_date)  <= v_end_date    -- parameter here
                 and   TRUNC(cas.creation_date)   >= v_start_date  
                 and   cas.date_trans_from is not null) then 1 else 0 end) as section3,
        sum(case when is_benchwarrant_RREC_Case(cas.case_id, v_start_date, v_end_date) = 'Y' then 1 else 0 end) as section4,  -- Bench Warrant Cases
        sum(case when (TRUNC(nvl(cas.date_trans_recorded_to, '01-Jan-1900'))  <= v_end_date   -- parameter here
                       and    TRUNC(nvl(cas.date_trans_recorded_to, '01-Jan-1900'))  >= v_start_date) then 1 else 0 end) as section5,  -- Cases Transferred Out
        0 as section6,         -- Section 6 is calculated from (6) = (2) + (3) + (4) - (5), but we just put in a dummy column
         sum(case when (is_dealtwith_RREC_Case(cas.case_id, v_start_date, v_end_date, p_court_id) = 'Y') then 1 
                  Else 0 END) as  section7,   -- Cases Dealt With
        0 as section8
        from  xhb_case cas
        where cas.court_id = p_court_id
        and   is_valid_RREC_Case(cas.case_id) = 'Y'
		and   NVL(cas.court_id_receiving_site,-1) in (
			select xcs.court_site_id 
			from xhb_court_site xcs 
			where xcs.court_id = p_court_id 
			and nvl(xcs.obs_ind,'N') <> 'Y'
			and	  (xcs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite sat WHERE sat.court_site_id = xcs.court_site_id AND NVL(sat.obs_ind,'N') <> 'Y')))
        and   TRUNC(nvl(cas.received_date, v_start_date)) <= v_end_date -- parameter here
        group by 'ALL', get_RREC_CaseType_Hdr(cas.case_id), 'Total'
        order by case_type desc, case_subhdg, sort_order;


    -- Record that this report has been run.
        v_step := '6 ';
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);

    -- Exception handling
    EXCEPTION
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in XHB_REPORT_PKG.GET_RREC_SUMMARY at step ' || v_step || '  Err code ' || v_err_code || ' : ' ||SQLERRM;

            DBMS_OUTPUT.PUT_LINE('get_RREC_summary:' || v_err_msg);

            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
            RAISE;

END get_RREC_summary;

  /**********************************************************************************
  *
  * Function  get_rrca_composite_casenum  (CTX-2223)
  *
  * Returns the composite case number:
  * '*' if a bench warrant has been executed, space otherwise
  * 'T' for case type (always trial) 
  * Case number
  * ' - ' || defendant number against whom a bench warrant has been executed (if applicable)
  *          
  *

  **************************************************************************************/
  Function get_rrca_composite_casenum(p_case_id XHB_CASE.case_id%TYPE, p_end_date DATE) 
      Return VARCHAR2 IS
      
  v_deft_count         NUMBER(8);
  v_composite_casenum  VARCHAR2(20);
      
  BEGIN
      -- We need to know if there are any Bench Warrants executed for this case before the end date
      select count(doc.defendant_on_case_id) 
      into   v_deft_count      -- For a case to be open at the start of the period, it has to 
      from   xhb_defendant_on_case doc, xhb_bw_history bwh           -- have at least one defendant unexported at the start date.
      where  nvl(doc.date_exported, p_end_date) >= p_end_date        -- We map null values to the end of the period rather than deal with the nulls.
                                                                     -- We are looking for the date_exported to be null up to the end of the period.
      and    doc.defendant_on_case_id = bwh.defendant_on_case_id
      and    (nvl(bwh.bw_end_date, p_end_date) < p_end_date)         -- Bench Warrant has to be executed before the end of the period 
      and    nvl(bwh.withdrawn, 'N') != 'Y'                          -- and not be withdrawn for the case to be open
      and    nvl(bwh.obs_ind, 'N') != 'Y'                            -- and the record has to be not obsolete
      and    doc.case_id = p_case_id;  
      
      IF v_deft_count > 0 THEN 
          -- Get the defendant number  for the defendant that had the Bench Warrant
          SELECT '*' || cas.case_type || To_Char(cas.case_number) || ' - ' || Min(doc.defendant_number)
          INTO   v_composite_casenum 
          FROM   xhb_defendant_on_case doc, xhb_bw_history bwh, xhb_case cas  
          WHERE  nvl(doc.date_exported, p_end_date) >= p_end_date        -- We map null values to the end of the period rather than deal with the nulls.
                                                                         -- We are looking for the date_exported to be null up to the end of the period.
          and    doc.defendant_on_case_id = bwh.defendant_on_case_id
          and    (nvl(bwh.bw_end_date, p_end_date) < p_end_date)         -- Bench Warrant has to be executed before the end of the period 
          and    nvl(bwh.withdrawn, 'N') != 'Y'                          -- and not be withdrawn for the case to be open
          and    nvl(bwh.obs_ind, 'N') != 'Y'                            -- and the record has to be not obsolete
          and    doc.case_id = cas.case_id  
          and    cas.case_id = p_case_id  
          group by cas.case_type, cas.case_number; 

      ELSE
          -- No bench warrants.  Just return the case type and case number.
          SELECT ' ' || cas.case_type || To_char(cas.case_number) || '    '
          INTO   v_composite_casenum
          FROM   XHB_CASE cas 
          WHERE  cas.case_id = p_case_id;
      END IF;

      RETURN v_composite_casenum;
      
  END get_rrca_composite_casenum;   

  /**********************************************************************************
  *
  * Function  get_rrca_waiting_from_date
  *
  * This function returns the date to be used when calculating waiting time for a given
  * case record.
  *
  **************************************************************************************/
  FUNCTION get_rrca_waiting_from_date (p_case_id XHB_CASE.case_id%TYPE, p_end_date DATE) RETURN DATE IS
  
	v_return_date	DATE;
	v_deft_count    NUMBER;
  
  BEGIN
  
      -- We need to know if there are any Bench Warrants executed for this case before the end date
      SELECT COUNT(doc.defendant_on_case_id) 
      INTO   v_deft_count      -- For a case to be open at the start of the period, it has to 
      FROM   xhb_defendant_on_case doc, xhb_bw_history bwh           -- have at least one defendant unexported at the start date.
      WHERE  NVL(doc.date_exported, p_end_date) >= p_end_date        -- We map null values to the end of the period rather than deal with the nulls.
                                                                     -- We are looking for the date_exported to be null up to the end of the period.
      AND    doc.defendant_on_case_id = bwh.defendant_on_case_id
      AND    (NVL(bwh.bw_end_date, p_end_date) < p_end_date)         -- Bench Warrant has to be executed before the end of the period 
      AND    NVL(bwh.withdrawn, 'N') != 'Y'                          -- and not be withdrawn for the case to be open
      AND    NVL(bwh.obs_ind, 'N') != 'Y'                            -- and the record has to be not obsolete
      AND    doc.case_id = p_case_id;  
      
      IF v_deft_count > 0 THEN 
          -- Return the Bench Warrant execution date
          SELECT MAX(bwh.bw_end_date)                                    -- The most recent if there is more than one.
          INTO   v_return_date 
          FROM   xhb_defendant_on_case doc, xhb_bw_history bwh  
          WHERE  NVL(doc.date_exported, p_end_date) >= p_end_date        -- We map null values to the end of the period rather than deal with the nulls.
                                                                         -- We are looking for the date_exported to be null up to the end of the period.
          AND    doc.defendant_on_case_id = bwh.defendant_on_case_id
          AND    (NVL(bwh.bw_end_date, p_end_date) < p_end_date)         -- Bench Warrant has to be executed before the end of the period 
          AND    NVL(bwh.withdrawn, 'N') != 'Y'                          -- and not be withdrawn for the case to be open
          AND    NVL(bwh.obs_ind, 'N') != 'Y'                            -- and the record has to be not obsolete
          AND    doc.case_id = p_case_id ; 
      ELSE 
          -- Return the trial date or committal date
          SELECT NVL(cas.sent_for_trial_date, NVL(cas.committal_date, (p_end_date-728)))
          INTO   v_return_date 
          FROM   XHB_CASE cas 
          WHERE  cas.case_id = p_case_id;
      END IF;
	  
	  RETURN v_return_date;
  
  END get_rrca_waiting_from_date;  
  
  /**********************************************************************************
  *
  * Function  get_rrca_age_band_desc
  *
  * This function returns the description of a specific age band passed in
  *
  **************************************************************************************/
  Function get_rrca_age_band_desc (p_age_band VARCHAR2) 
      Return VARCHAR2 IS
      
  v_retval             VARCHAR2(30);

  BEGIN
      CASE  p_age_band
          WHEN 'A' THEN
		      v_retval := 'Up to 8 weeks';
			  
          WHEN 'B' THEN
		      v_retval := 'Over 8 weeks to 16 weeks';
			  
          WHEN 'C' THEN
		      v_retval := 'Over 16 weeks to 24 weeks';
			  
          WHEN 'D' THEN
		      v_retval := 'Over 24 weeks to 36 weeks';
			  
          WHEN 'E' THEN
		      v_retval := 'Over 36 weeks to 48 weeks';
			  
          WHEN 'F' THEN
		      v_retval := 'Over 48 weeks to 60 weeks';
			  
          WHEN 'G' THEN
		      v_retval := 'Over 60 weeks to 72 weeks';
			  
          WHEN 'H' THEN
		      v_retval := 'Over 72 weeks to 88 weeks';
			  
          WHEN 'I' THEN
		      v_retval := 'Over 88 weeks to 104 weeks';
			  
          WHEN 'J' THEN
		      v_retval := 'Over 104 weeks';
			  
		      ELSE
		          -- This should not happen.
		          v_retval := 'Over 104 weeks';
      END CASE;  
		  
    RETURN v_retval;
  END  get_rrca_age_band_desc;

  /**********************************************************************************
  *
  * Function  get_rrca_age_band
  *
  * This function determines the age of a case and allocates it to the appropriate age band.  
  *
  **************************************************************************************/
  Function get_rrca_age_band(p_case_id XHB_CASE.case_id%TYPE, p_end_date DATE) 
      Return VARCHAR2 IS
      
  v_retval             VARCHAR2(30);
      
  -- This string defines the case age bands, which vary in width but are all in multiples of 28 days.
  v_agebands      VARCHAR2(120) := 'AABBCCDDDEEEFFFGGGHHHHIIIIJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ';
  v_ageband       VARCHAR2(1);
  v_waiting_from_date	DATE;

  BEGIN
  
	v_waiting_from_date := get_rrca_waiting_from_date(p_case_id, p_end_date);
	select substr(v_agebands, (trunc((to_number(to_char(p_end_date, 'J')) - to_number(to_char(v_waiting_from_date, 'J')))/28) + 1), 1) 
	into   v_retval
	from   xhb_case cas
	where  cas.case_id = p_case_id;
		  
    RETURN v_retval;
  END  get_rrca_age_band;	  
	  
  /**********************************************************************************
  *
  * Function  get_rrca_age_band_text  (CTX-2223)
  *
  * This function determines the age of a case and allocates it to the appropriate age band. 
  * The text of the age band is returned.  
  *
  **************************************************************************************/
  Function get_rrca_age_band_text(p_case_id XHB_CASE.case_id%TYPE, p_end_date DATE) 
      Return VARCHAR2 IS
      
  v_ageband       VARCHAR2(1);

  BEGIN
      -- We need to know if there are any Bench Warrants executed for this case before the end date
      select get_rrca_age_band(p_case_id, p_end_date)
	  into   v_ageband
      from   dual;
		  
    RETURN get_rrca_age_band_desc(v_ageband);
  END  get_rrca_age_band_text;       
      
  /**********************************************************************************
  *
  * Procedure get_RRCA_detail  (CTX-2223) - DETAIL
  *
  * Returns a list of Outstanding Cases banded by age for a given Court/Court Site (usually High/Crown court). 
  * Case_number is a composite of {space|asterisk}{case_type}{case_number}{emptystring|' - 'defendant_number}
  * The first field is asterisk if a bench warrant has been executed for the case. (This also affects the waiting time calculation)
  * Case_type is always T (trial) in this report. Where a bench warrant has been executed, the applicable defendants defendant_number
  * is added as a suffix.  N.B. The report returns waiting time.  
  *
  * Columns:     court_name, trial_date, custody_status, wait_time, case_number, age_band 
  *
  **************************************************************************************/

PROCEDURE get_RRCA_detail(p_resultset  OUT SYS_REFCURSOR 
                        , p_date       IN DATE 
                        , p_court_id   IN  XHB_COURT.COURT_ID%TYPE) AS 
                                           
-- Local variables here
v_rpt_name      VARCHAR2(50)  := 'Report on Outstanding Trial Cases By Age (Detail)';
v_rpt_code      VARCHAR2(6)   := 'RRCA';
v_proc_name     VARCHAR2(20)  := 'get_RRCA_Detail';

v_user          VARCHAR2(30);
v_end_date      DATE;
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);

BEGIN  
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;
        
	-- Get last day of the month passed in
    v_end_date := LAST_DAY(nvl(p_date, SYSDATE));
    
    OPEN p_resultset FOR
    -- Query for RRCA report
	WITH age_bands AS (SELECT CHR( ASCII('A')+LEVEL-1) letter FROM dual CONNECT BY LEVEL <= 10) -- Lists first 10 letters of alphabet for all the age categories
    SELECT  ct.court_name, cs.court_site_name, NVL(cas.sent_for_trial_date, cas.committal_date) AS trial_date, get_bc_status_ind(cas.case_id) AS status, 
            get_rrca_composite_casenum(cas.case_id,  v_end_date) AS case_num, 
            get_rrca_age_band_text(cas.case_id, v_end_date) AS age_band, 
			get_rrca_age_band(cas.case_id, v_end_date) AS age_band_code
    FROM xhb_court ct, xhb_court_site cs, xhb_case cas 
    WHERE cas.court_id  = ct.court_id 
    AND   cas.court_id_receiving_site = cs.court_site_id 
	AND   NVL(cs.obs_ind,'N') <> 'Y'
	AND	  (cs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite xcs WHERE xcs.court_site_id = cs.court_site_id AND NVL(xcs.obs_ind,'N') <> 'Y'))
    AND   ct.court_id = p_court_id 
    AND   cas.case_type = 'T'
	AND   get_rrca_waiting_from_date(cas.case_id,  v_end_date) < v_end_date
    AND    xhb_case_pkg.determine_case_status(cas.case_id) = 'Open'
	UNION
	-- This union provides a dummy case in each age category which will not appear in the report
	-- but forces the age category sub header to appear even if there is no cases to show
    SELECT ct.court_name, 
        cs.court_site_name, 
        SYSDATE AS trial_date, 
        'C' AS status, 
        'NOCASE' || ab.letter AS case_num, 
        get_rrca_age_band_desc(ab.letter) AS age_band,
        ab.letter AS age_band_code
    FROM xhb_court ct, xhb_court_site cs, age_bands ab
    WHERE ct.court_id = p_court_id 
    AND   cs.court_id = ct.court_id
    AND   NVL(cs.obs_ind,'N') <> 'Y'
	AND	  (cs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite xcs WHERE xcs.court_site_id = cs.court_site_id AND NVL(xcs.obs_ind,'N') <> 'Y'))
    ORDER BY court_site_name, age_band_code, case_num; 
      
    -- Now we have to log that we have run this report. 
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
                         
    EXCEPTION     
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in XHB_REPORT_PKG.' || v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            
            dbms_output.put_line(v_proc_name || ': ' || v_err_msg);

            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.NEXTVAL, v_err_msg,     SYSDATE);
            RAISE;  
   
END get_RRCA_Detail;  
  
  /**********************************************************************************
  *
  * Procedure get_RRCA_Summary  (CTX-2223) - SUMMARY
  *
  * Returns a count of Outstanding Cases banded by age for a given Court/Court Site (usually High/Crown court). 
  * Case_type is always T (trial) in this report.  
  *
  * Columns:     ResultSet: court_name, court site, custody_status, age_band, case_count
  *
  **************************************************************************************/
PROCEDURE get_RRCA_Summary(p_resultset OUT SYS_REFCURSOR 
                         , p_date      IN DATE 
                         , p_court_id  IN  XHB_COURT.COURT_ID%TYPE) AS

-- Local variables here
v_rpt_name      VARCHAR2(50)  := 'Report on Outstanding Trial Cases By Age - Summary';
v_rpt_code      VARCHAR2(6)   := 'RRCA';
v_proc_name     VARCHAR2(20)  := 'get_RRCA_Summary';

v_user          VARCHAR2(30);
v_end_date      DATE;
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);

v_court_name	xhb_court.court_name%TYPE;

CURSOR get_rrca_data_c IS
SELECT 1 AS sort_order, 
	   ct.court_name, 
	   cs.court_site_name AS court_site_name, 
	   SUM(CASE get_bc_status_ind(cas.case_id) 
			   WHEN 'C' THEN 1 ELSE 0 END) AS custody_cases, 
	   SUM(CASE get_bc_status_ind(cas.case_id)
			   WHEN 'C' THEN 0 ELSE 1 END) AS non_custody_cases, 
	   SUM(1) AS total, 
	   get_rrca_age_band(cas.case_id, v_end_date) AS age_band_code		   
FROM   xhb_court ct, xhb_case cas, xhb_court_site cs  
WHERE  cas.court_id  = ct.court_id 
AND    cas.court_id_receiving_site = cs.court_site_id
AND    NVL(cs.obs_ind,'N') <> 'Y'
AND	  (cs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite xcs WHERE xcs.court_site_id = cs.court_site_id AND NVL(xcs.obs_ind,'N') <> 'Y'))
AND    ct.court_id = p_court_id 
AND    ct.court_id = cs.court_id 
AND    cas.case_type = 'T'
AND    get_rrca_waiting_from_date(cas.case_id,  v_end_date) < v_end_date
AND    xhb_case_pkg.determine_case_status(cas.case_id) = 'Open'
GROUP BY ct.court_name
	   , cs.court_site_name
	   , get_rrca_age_band(cas.case_id, v_end_date)  
UNION		   
SELECT 1 AS sort_order, 
	   ct.court_name, 
	   cs.court_site_name AS court_site_name,
	   SUM(CASE get_bc_status_ind(cas.case_id) 
			   WHEN 'C' THEN 1 ELSE 0 END) AS custody_cases, 
	   SUM(CASE get_bc_status_ind(cas.case_id)
			   WHEN 'C' THEN 0 ELSE 1 END) AS non_custody_cases, 
	   SUM(1) AS total, 
	   'Z' AS age_band_code	-- The purpose of this dummy value is so that the Totals row can be ordered to appear at the end of the Age bands for each court site		   	   
FROM   xhb_court ct, xhb_case cas, xhb_court_site cs  
WHERE  cas.court_id  = ct.court_id 
AND    cas.court_id_receiving_site = cs.court_site_id
AND    ct.court_id = p_court_id 
AND    ct.court_id = cs.court_id 
AND    NVL(cs.obs_ind,'N') <> 'Y'
AND	  (cs.court_site_code = 'A' OR EXISTS (SELECT NULL FROM xhb_court_satellite xcs WHERE xcs.court_site_id = cs.court_site_id AND NVL(xcs.obs_ind,'N') <> 'Y'))
AND    cas.case_type = 'T' 
AND    get_rrca_waiting_from_date(cas.case_id,  v_end_date) < v_end_date
AND    xhb_case_pkg.determine_case_status(cas.case_id) = 'Open'
GROUP BY ct.court_name
	   , cs.court_site_name         
	   , 'Z'
ORDER BY sort_order
	   ,court_site_name
	   ,age_band_code;
						 
CURSOR cs_sites IS
SELECT cs.court_site_id, cs.court_site_name
FROM xhb_court_site cs
WHERE cs.court_id = p_court_id
AND NVL(cs.obs_ind, 'N') <> 'Y'
AND	  (	cs.court_site_code = 'A' 
		OR EXISTS (SELECT NULL FROM xhb_court_satellite xcs WHERE xcs.court_site_id = cs.court_site_id AND NVL(xcs.obs_ind,'N') <> 'Y'));
						 
TYPE array_t IS TABLE OF VARCHAR2(1);
letters_array array_t := array_t('A','B','C','D','E','F','G','H','I','J');

BEGIN  
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;

    -- Get last day of the month passed in   
    v_end_date := LAST_DAY(NVL(p_date, SYSDATE));
	
	-- Get the court name
	SELECT court_name INTO v_court_name FROM xhb_court WHERE court_id = p_court_id;
	
	-- Loop through each court site and age category (A-J) and Insert new rows into the global temporary table
	FOR site_rec IN cs_sites LOOP
		FOR i IN 1..letters_array.COUNT LOOP
			INSERT INTO rrca_summary_data (sort_order, court_name, court_site_name, age_band_text, custody_cases, non_custody_cases, total, age_band_code)
			VALUES (1, v_court_name, site_rec.court_site_name, get_rrca_age_band_desc(letters_array(i)), 0, 0, 0, letters_array(i) );
		END LOOP;
		
		-- Insert total row for the court site
		INSERT INTO rrca_summary_data (sort_order, court_name, court_site_name, age_band_text, custody_cases, non_custody_cases, total, age_band_code)
		VALUES (1, v_court_name, site_rec.court_site_name, 'Total', 0, 0, 0, 'Z' );
    END LOOP;
	
	-- Insert the total rows for the court
	FOR i IN 1..letters_array.COUNT LOOP
		INSERT INTO rrca_summary_data (sort_order, court_name, court_site_name, age_band_text, custody_cases, non_custody_cases, total, age_band_code)
		VALUES (2, v_court_name, 'ALL', get_rrca_age_band_desc(letters_array(i)), 0, 0, 0, letters_array(i) );
	END LOOP;
	
	-- Insert the total row for the court
	INSERT INTO rrca_summary_data (sort_order, court_name, court_site_name, age_band_text, custody_cases, non_custody_cases, total, age_band_code)
	VALUES (2, v_court_name, 'ALL', 'Total', 0, 0, 0, 'Z' );
	
	FOR rrca_rec IN get_rrca_data_c LOOP

		-- Update the rrca data row for this court site and age band
		UPDATE 	rrca_summary_data
		SET 	custody_cases = rrca_rec.custody_cases
				,non_custody_cases = rrca_rec.non_custody_cases
				,total = rrca_rec.total
		WHERE 	age_band_code = rrca_rec.age_band_code
		AND 	court_site_name = rrca_rec.court_site_name;
		
		-- Increment the court total for the age band
		UPDATE 	rrca_summary_data
		SET 	custody_cases = custody_cases + rrca_rec.custody_cases
				,non_custody_cases = non_custody_cases + rrca_rec.non_custody_cases
				,total = total + rrca_rec.total
		WHERE 	age_band_code = rrca_rec.age_band_code
		AND 	court_site_name = 'ALL';
		
    END LOOP;
	
	-- Retrieve data from global temporary table
	OPEN   p_resultset FOR  
        SELECT sort_order, court_name, court_site_name, age_band_text, custody_cases, non_custody_cases, total, age_band_code
        FROM rrca_summary_data
		ORDER BY sort_order, court_site_name, age_band_code;
		
	-- The Java code will commit to purge data from global temporary table
    
    -- Now we have to log that we have run this report. 
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
                         
    EXCEPTION     
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in XHB_REPORT_PKG.' || v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            
            dbms_output.put_line(v_proc_name || ': ' || v_err_msg);

            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.NEXTVAL, v_err_msg,     SYSDATE);
            RAISE;  
   
END get_RRCA_Summary;

  /**********************************************************************************
  *
  * Procedure getCtRmSittingTimes  (CTX-2075)
  *
  * Returns for a given court, court site* and week, the sitting times for each court room 
  * for each day of the week, broken down by am / pm sessions.
  *  * If p_court_site_id is zero or null, all sites for the given court are searched.
  *
  * Columns:      court_name, court_room_no, court_site_name, court_room_name, am_pm, 
  *               am_pm, monday, tuesday, wednesday, thursday, friday, saturday, total 
  *
  **************************************************************************************/

PROCEDURE getCtRmSittingTimes(p_resultset OUT SYS_REFCURSOR
                           ,  p_court_id      IN  XHB_COURT.COURT_ID%TYPE 
                           ,  p_court_site_id IN  XHB_COURT_SITE.COURT_SITE_ID%TYPE
                           ,  p_week_ending   IN  DATE
                            ) AS 
    
-- Local variables here
v_rpt_name      VARCHAR2(50)  := 'Courtroom Sitting Times Report';
v_rpt_code      VARCHAR2(6)   := 'RSIT';
v_proc_name     VARCHAR2(20)  := 'getCtRmSittingTimes';
v_step          VARCHAR2(10);
v_day_of_week   INTEGER;
v_start_date    DATE;
v_end_date      DATE;
v_err_code      NUMBER;
v_err_msg       VARCHAR2(1000);

v_week_starting     DATE;

BEGIN          
    v_step := '1';  
        
    -- Weekly mode
    v_day_of_week  := TO_NUMBER(TO_CHAR(p_week_ending, 'd'));    -- Find out the day of the week for the date they supplied.
    v_end_date     := p_week_ending  + (6 - v_day_of_week);      -- This makes sure the date we use is a Saturday
    v_start_date   := v_end_date - 7;
        
    v_step := '3';   
    IF TRUNC(v_end_date) > (TRUNC(sysdate) + (6 - TO_NUMBER(TO_CHAR(SYSDATE, 'd')))) THEN
        -- The end date cannot be after the Saturday of the current week
        v_step := '4';    
            
        RAISE VALUE_ERROR;
      
    END IF;

    v_step := '5';   
                                 
    open p_resultset for   -- 1st query = mornings
        select distinct ct.court_name as court_name, ctrm.crest_court_room_no as court_room_no, cts.court_site_code as court_site_code, cts.court_site_name as court_site_name, ctrm.court_room_name as court_room_name, 
                        'am' as am_pm, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '1'  -- Monday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Monday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '2'  -- Tuesday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Tuesday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '3' -- Wednesday 
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Wednesday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '4' -- Thursday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Thursday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '5' -- Friday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Friday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '6' -- Saturday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Saturday, 
                        null as total_hours, 
                        null as total_days, 
                        1 as sort						
        from  xhb_court ct, xhb_court_room ctrm, xhb_court_site cts
        where cts.court_site_id = ctrm.court_site_id
        and   (cts.court_site_id = nvl(p_court_site_id, 0) OR nvl(p_court_site_id, 0) = 0)   -- parameter here 
        and   cts.court_id = ct.court_id 
        and   ct.court_id  = p_court_id   -- parameter here 
		and   nvl(ct.obs_ind,   'N') != 'Y' 
		and   nvl(ctrm.obs_ind, 'N') != 'Y' 
		and   nvl(cts.obs_ind,  'N') != 'Y' 
		UNION    -- 2nd query = afternoons
        select distinct ct.court_name as court_name, ctrm.crest_court_room_no as court_room_no, cts.court_site_code as court_site_code, cts.court_site_name as court_site_name, ctrm.court_room_name as court_room_name, 
                        'pm' as am_pm, 
                        NVL((select sum((ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '1'  -- Monday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Monday, 
                        NVL((select sum((ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '2'  -- Tuesday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Tuesday, 
                        NVL((select sum((ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '3' -- Wednesday 
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Wednesday, 
                        NVL((select sum((ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '4' -- Thursday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Thursday, 
                        NVL((select sum((ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '5' -- Friday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Friday, 
                        NVL((select sum((ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '6' -- Saturday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Saturday, 
                        null	 as total_hours,  
                        null	 as total_days, 
 						2 as sort 
        from  xhb_court ct, xhb_court_room ctrm, xhb_court_site cts
        where cts.court_site_id = ctrm.court_site_id
        and   (cts.court_site_id = nvl(p_court_site_id, 0) OR nvl(p_court_site_id, 0) = 0)   -- parameter here 
        and   cts.court_id = ct.court_id 
        and   ct.court_id  = p_court_id   -- parameter here 
		and   nvl(ct.obs_ind,   'N') != 'Y' 
		and   nvl(ctrm.obs_ind, 'N') != 'Y' 
		and   nvl(cts.obs_ind,  'N') != 'Y' 
		UNION    -- 3rd query = day totals
        select distinct ct.court_name as court_name, ctrm.crest_court_room_no as court_room_no, cts.court_site_code as court_site_code, cts.court_site_name as court_site_name, ctrm.court_room_name as court_room_name, 
                        'Total' as am_pm, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins + (ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '1'  -- Monday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Monday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins + (ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '2'  -- Tuesday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Tuesday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins + (ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '3' -- Wednesday 
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Wednesday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins + (ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '4' -- Thursday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Thursday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins + (ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '5' -- Friday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Friday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins + (ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where TO_CHAR(ctu.sitting_date(+), 'd') = '6' -- Saturday
                             and   ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as Saturday, 
                        NVL((select sum((ctu.am_time_hours*60) + ctu.am_time_mins + (ctu.pm_time_hours*60) + ctu.pm_time_mins)
                             from  xhb_court_room_usage ctu
                             where ctu.sitting_date(+) between v_start_date and v_end_date 
                             -- join to outer query
                             and   ctrm.court_room_id = ctu.court_room_id), 0) as total_hours,  
                        0 as total_days,
                        3 as sort 							 
        from  xhb_court ct, xhb_court_room ctrm, xhb_court_site cts
        where cts.court_site_id = ctrm.court_site_id
        and   (cts.court_site_id = nvl(p_court_site_id, 0) OR nvl(p_court_site_id, 0) = 0)   -- parameter here 
        and   cts.court_id = ct.court_id 
        and   ct.court_id  = p_court_id   -- parameter here 
		and   nvl(ct.obs_ind,   'N') != 'Y' 
		and   nvl(ctrm.obs_ind, 'N') != 'Y' 
		and   nvl(cts.obs_ind,  'N') != 'Y' 
        order by court_site_code, court_site_name, court_room_no, sort;
        
    v_step := '6';   
	-- Log that we have run this report.
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
                         
    EXCEPTION     
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others at step ' || v_step || ' in XHB_REPORT_PKG.' || v_rpt_name || ', ' || v_err_code || ' : ' || SQLERRM;
            
            dbms_output.put_line(v_proc_name || ': ' || v_err_msg);

            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
			RAISE;  
   
END  getCtRmSittingTimes;

FUNCTION get_prosecutor(p_case_id      IN  XHB_CASE.CASE_ID%TYPE) RETURN VARCHAR2 AS
 
  v_prosecutor_id VARCHAR2(500);
  
 BEGIN
  SELECT xcpa.CASE_PROS_AGENCY_ID INTO v_prosecutor_id
     FROM  XHB_CASE_PROSECUTOR_AGENCY xcpa,
        XHB_CASE xc
    WHERE  xc.CASE_ID = p_case_id               
        AND xc.CASE_ID = xcpa.CASE_ID (+)
        AND ( xc.CASE_TYPE != 'A' 
        OR (xc.CASE_TYPE = 'A'
        AND   xcpa.PROSECUTOR_TYPE = 'R'     -- Added for CTX-2843  ) Appeal cases can have 2 prosecutors, a Respondent and an Objector.
        AND (xc.CASE_SUB_TYPE != 'O' OR xc.CASE_SUB_TYPE IS NULL        
        OR   xcpa.RESPONDENT_STATUS = 'R'   -- Added for CTX-2843  ) We only want the Respondent.
        )))
        AND   NVL(xcpa.OBS_IND (+), 'N') <> 'Y';


        
RETURN v_prosecutor_id;
 
END get_prosecutor;

FUNCTION get_prosecutor_name(p_case_id      IN  XHB_CASE.CASE_ID%TYPE) RETURN VARCHAR2 AS
 
  v_prosecutor_name VARCHAR2(500);
  
 BEGIN 
  SELECT DECODE(xrpa.PROSECUTOR_NAME_1,NUll,'',xrpa.PROSECUTOR_NAME_1 || ' ') || DECODE(xrpa.PROSECUTOR_NAME_2,NUll,'',xrpa.PROSECUTOR_NAME_2 || ' ') || xrpa.PROSECUTOR_NAME_3 INTO v_prosecutor_name
     FROM  XHB_CASE_PROSECUTOR_AGENCY xcpa,
        XHB_REF_PROSECUTOR_AGENCY xrpa
    WHERE xcpa.CASE_PROS_AGENCY_ID = get_prosecutor(p_case_id)
        AND   xcpa.REF_PROSECUTOR_AGENCY_ID  = xrpa.REF_PROSECUTOR_AGENCY_ID (+)
        AND   NVL(xrpa.OBS_IND (+), 'N') <> 'Y';
        
RETURN v_prosecutor_name;
 
END get_prosecutor_name;

END XHB_REPORT_PKG;
/
show errors
