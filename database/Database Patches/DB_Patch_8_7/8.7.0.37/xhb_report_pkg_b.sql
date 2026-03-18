create or replace PACKAGE BODY "XHB_REPORT_PKG" AS
  /**
  * CGI CREST to XHIBIT Program
  *
  * MODULE      : XHB_REPORT_PKG
  *
  * DESCRIPTION : This package contains stored procedures for CTX reports
  *				It comprises:   PROCEDURE get_ctlrl_report
  *                             PROCEDURE update_case_reminder_printed
  *                             FUNCTION  GET_DEFENDANTS_LIST
  *                             function  get_bc_status_ind
  *                             procedure get_defendants_put_back_report
  *                             procedure get_docar_report
  *                             procedure get_nfix_report
  *                             procedure get_list_officers_diary_report
  *								procedure get_cfix_report
  *							    procedure get_outc_report
  *								procedure get_unlc_report
  *                             PROCEDURE get_list_of_fixed_dates_report
  *                             PROCEDURE update_case_diary_fix_run_date
  *                             procedure get_run_date
  *                             PROCEDURE get_prlis_report
  *                             PROCEDURE publish_running_list
  *                             PROCEDURE get_ctlrp_report
  *                             function  get_charge
  *                             function  get_solicitor_id
  *                             function  get_most_recent_hearing
  *                             PROCEDURE UPDATE_REPORT_LOG
  *                             function  getAddress
  *                             Function  getPhoneNum
  *                             function  get_appellant_address
  *                             PROCEDURE getAppealHearingNotifnRpt
  *                             PROCEDURE getRUMO_Rpt
  *                             FUNCTION  GET_EARLIEST_LISTING_DATE
  *                             PROCEDURE get_rage_report
  *                             FUNCTION  GET_BENCH_WARRANT_DATE
  *                             FUNCTION  GET_LINKED_CASES_LIST
  *                             FUNCTION  GET_JUVENILE_STATUS
  *                             FUNCTION  GET_OFFENCES
  *                             FUNCTION  GET_DIARY_NOTES_LIST
  *                             FUNCTION  GET_LIST_HISTORY
  *                             PROCEDURE getRJS_Rpt
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
      xrpa.prosecutor_name_1 || ' ' || xrpa.prosecutor_name_2 || ' ' || xrpa.prosecutor_name_3 AS prosecutorname,
      xc.CASE_ID AS caseid,
      xc.CASE_TYPE || xc.CASE_NUMBER AS casenumber,
      TO_CHAR(xdc.CUSTODY_TIME_LIMIT,'DD Month YYYY') AS custodytimelimit,
      UPPER(xd.SURNAME) || ' ' || LOWER(xd.FIRST_NAME) AS defendantname,
      GET_DEFENDANTS_LIST(p_court_id,xc.CASE_ID,p_screen_time_limit) AS defendantnames,
      xdc.ASN AS asn,
      xcd.CONTACT_VALUE AS courttelephoneno,
      xa1.ADDRESS_1 || '  ' || xa1.TOWN || '  ' || xa1.COUNTY || ' ' || xa1.POSTCODE AS courtaddress,
      xa2.ADDRESS_1 AS prosecutoraddress1, xa2.TOWN AS prosecutoraddress2, xa2.COUNTY AS prosecutoraddress3, xa2.POSTCODE AS prosecutoraddress4
      
      FROM XHB_CASE xc
      INNER JOIN XHB_DEFENDANT_ON_CASE xdc ON xc.CASE_ID = xdc.CASE_ID AND (xdc.OBS_IND <> 'Y' OR xdc.OBS_IND IS NULL)
      INNER JOIN XHB_DEFENDANT xd ON xdc.DEFENDANT_ID = xd.DEFENDANT_ID
      INNER JOIN XHB_COURT xco ON xd.COURT_ID = xco.COURT_ID
      INNER JOIN XHB_CASE_PROSECUTOR_AGENCY xcpa ON xc.CASE_ID = xcpa.CASE_ID AND (xcpa.OBS_IND <> 'Y' OR xcpa.OBS_IND IS NULL)
      INNER JOIN XHB_REF_PROSECUTOR_AGENCY xrpa ON xcpa.REF_PROSECUTOR_AGENCY_ID = xrpa.REF_PROSECUTOR_AGENCY_ID AND (xrpa.OBS_IND <> 'Y' OR xrpa.OBS_IND IS NULL)
      INNER JOIN XHB_ADDRESS xa1 ON xco.ADDRESS_ID = xa1.ADDRESS_ID
      INNER JOIN XHB_CONTACT_DETAIL xcd ON xa1.ADDRESS_ID = xcd.ADDRESS_ID AND xcd.CONTACT_TYPE = 'Phone'
      INNER JOIN XHB_ADDRESS xa2 ON xrpa.ADDRESS_ID = xa2.ADDRESS_ID

      WHERE xc.COURT_ID = p_court_id AND xdc.CUSTODY_TIME_LIMIT BETWEEN sysdate AND p_screen_time_limit 
      AND (xc.DATE_CTL_REMINDER_PRINTED > p_screen_time_limit OR xc.DATE_CTL_REMINDER_PRINTED IS NULL)
      
      ORDER BY  xrpa.prosecutor_name_1 || ' ' || xrpa.prosecutor_name_2 || ' ' || xrpa.prosecutor_name_3, xc.CASE_TYPE || xc.CASE_NUMBER, xdc.CUSTODY_TIME_LIMIT;
        
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
  SELECT  UPPER(xd.INITIALS) || ' ' || LOWER(xd.SURNAME) AS defendantname
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
  
  
  FUNCTION get_bc_status_ind(p_case_id IN XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE) 
     RETURN VARCHAR2 IS
     v_bc_status VARCHAR2(3);
     CURSOR C_xdoc IS
     SELECT NVL(xdoc.CURRENT_BC_STATUS,'N/A')
      FROM XHB_DEFENDANT_ON_CASE xdoc
     WHERE xdoc.CASE_ID = p_case_id
       AND NVL(xdoc.OBS_IND, 'N') <> 'Y'
       AND NVL(xdoc.CURRENT_BC_STATUS,'N/A') IN ('C','J','B','N/A')
    ORDER BY CASE NVL(xdoc.CURRENT_BC_STATUS,'N/A') 
               WHEN 'C' THEN 1
               WHEN 'J' THEN 1
               WHEN 'B' THEN 2
               WHEN 'N/A' THEN 3 END;
   BEGIN
     OPEN C_xdoc;
     FETCH C_xdoc INTO v_bc_status;
     IF C_xdoc%NOTFOUND THEN 
        v_bc_status := 'N/A';
     END IF;
     CLOSE C_xdoc;
     IF v_bc_status = 'J' THEN 
        v_bc_status := 'C';
     END IF;
     RETURN v_bc_status;
   END get_bc_status_ind;
  
    
  PROCEDURE get_defendants_put_back_report(p_results_out OUT SYS_REFCURSOR  
                            ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                            ,p_refSystemCode IN VARCHAR
                            , p_report_name IN VARCHAR2) AS
 
  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
  BEGIN
  OPEN p_results_out FOR
	 select	xc.CASE_TYPE || xc.case_number as case_number
	,      upper(xd.SURNAME)||' '|| xd.FIRST_NAME ||' '|| xd.MIDDLE_NAME AS defendant_name
	,      xdhr.ADJOURNED_DATE
	,      xdhr.HEARING_START_DATE AS HEARING_DATE
	,      upper(xrj.SURNAME) ||  ' ' || xrj.FIRST_NAME ||' '|| xrj.MIDDLE_NAME AS judge_name
	,      xdoc.ASN
	,     substr(xrsc.DE_CODE, instr(xrsc.DE_CODE, '-')) AS reason
  , xdoc.defendant_on_case_id
  , xdhr.HEARING_RECORD_ID
  , GET_EARLIEST_LISTING_DATE(xc.CASE_ID,xdoc.DEFENDANT_ON_CASE_ID) AS listing_date
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
  and xdoc.DEFENDANT_ON_CASE_ID = xdhr.DEFENDANT_ON_CASE_ID
	and   xdhr.IS_ADJOURNED = 'Y'
	and   xdhr.REF_ADJOURNMENT_ID = xrsc.REF_SYSTEM_CODE_ID
	and   NVL(xrsc.OBS_IND, '-') <> 'Y'
	and   xrsc.CODE_TYPE = 'PB_TYPE'
	and   xrsc.CODE_TITLE = 'ADJOURNMENT TYPE'
	and   xrsc.code IN (SELECT regexp_substr(p_refSystemCode, '[^,]+', 1, level) FROM DUAL
                      CONNECT BY regexp_substr(p_refSystemCode,'[^,]+', 1, level) IS NOT NULL)
  and   xrj.REF_JUDGE_ID = (select xsha.REF_JUDGE_ID from XHB_SCHED_HEARING_ATTENDEE xsha  
                           where xsha.SH_ATTENDEE_ID = (select max(xsha2.SH_ATTENDEE_ID) 
                           from XHB_SCHED_HEARING_ATTENDEE xsha2, XHB_SCHEDULED_HEARING xsh, XHB_REF_JUDGE xrj2
                           where xsha2.REF_JUDGE_ID = xrj2.REF_JUDGE_ID
                           and xsha2.SCHEDULED_HEARING_ID = xsh.SCHEDULED_HEARING_ID
                           and xsh.HEARING_ID = xh.HEARING_ID
                           and NVL(xrj2.OBS_IND, '-') <> 'Y'
                           and xsha2.ATTENDEE_TYPE in ('J','JP')))
	and   NVL(xrj.OBS_IND, '-') <> 'Y'
	and   (  xdoc.RESULTS_VERIFIED = 'R' --R means that there are no subsequent hearings  
  or     ( NVL(xdoc.RESULTS_VERIFIED,'x') <> 'R' --if there are subsequet hearings, make sure there are no disposals
	and      not exists (select 'x' --can either be joined to defendant on case or defendant on offence
						  from XHB_DISPOSAL2 xdisp
						  where xdisp.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
              AND (xdisp.OBS_IND IS NULL OR xdisp.OBS_IND <> 'Y')
						  )
  and      not exists (select 'x'--can either be joined to defendant on case or defendant on offence
						  from xhb_disposal2 xdisp
						  where xdisp.DEFENDANT_ON_OFFENCE_ID = xdoc.DEFENDANT_ON_CASE_ID
              AND (xdisp.OBS_IND IS NULL OR xdisp.OBS_IND <> 'Y')
						  )
		 ))
 
  	and xc.COURT_ID = p_court_id
    
	ORDER BY LISTING_DATE
	,        xc.CASE_NUMBER
	,        xd.DEFENDANT_ID;  

	UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE COURT_ID = p_court_id AND CREST_REPORT_CODE = p_report_name;
    
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
    SELECT c.CASE_TYPE, c.CASE_NUMBER, dof.DEFENDANT_NUMBER, d.FIRST_NAME, d.MIDDLE_NAME, d.SURNAME, dof.FORM_NG_SENT_DATE
    FROM XHB_DEFENDANT_ON_CASE dof, XHB_CASE c, XHB_DEFENDANT d
    WHERE dof.CASE_ID = c.CASE_ID
    AND dof.DEFENDANT_ID = d.DEFENDANT_ID
    AND dof.FORM_NG_SENT_DATE IS NOT NULL
    AND dof.FORM_NG_SENT_DATE < p_papers_sent_date
    AND dof.CACD_APPEAL_RESULT_DATE IS NULL
	AND (dof.OBS_IND <> 'Y' OR dof.OBS_IND IS NULL)
    AND c.COURT_ID = p_court_id;
    
     UPDATE_REPORT_LOG(p_court_id, 'DOCAR', 'Defendants with Outstanding Court of Appeal Results');
    
     EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_DOCAR_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
  END get_docar_report;

 
 PROCEDURE get_nfix_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE) AS
  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
  BEGIN
  OPEN p_results_out FOR
	  SELECT CASE WHEN get_solicitor_id(xdoc.DEFENDANT_ON_CASE_ID) IS NULL 
            THEN xd.first_name||' '||xd.middle_name||' '||xd.surname --use defendant name
        ELSE (SELECT SOLICITOR_FIRM_NAME FROM xhb_ref_solicitor_firm xrsf
                WHERE xrsf.ref_solicitor_firm_id = get_solicitor_id(xdoc.DEFENDANT_ON_CASE_ID)
               AND  NVL(xrsf.OBS_IND, '-') <> 'Y')
       END  SOLICITOR_DEFENDANT  
/*****************************solictor / defendant address******************************************************/       
,      CASE WHEN get_solicitor_id(xdoc.DEFENDANT_ON_CASE_ID) IS NULL 
              THEN dxa.address_1||' '||dxa.address_2||' '||dxa.address_3||' '||dxa.address_4||' '||dxa.postcode
        ELSE (SELECT sxa.address_1||' '||sxa.address_2||' '||sxa.address_3||' '||sxa.address_4||' '||sxa.postcode
                FROM xhb_ref_solicitor_firm xrsf,
                     xhb_address sxa
                WHERE xrsf.ref_solicitor_firm_id = get_solicitor_id(xdoc.DEFENDANT_ON_CASE_ID)
                AND   xrsf.address_id = sxa.address_id)
      END SOLICITOR_DEFENDANT_ADDRESS
,     trunc(xcdf.listing_date) AS FIXTURE_DATE -- the first date for the defendants case
,     xcrt.court_name
,     cxa.address_1||' '||cxa.address_2||' '||cxa.address_3||' '||cxa.address_4||' '||cxa.postcode AS crt_address_1
,     xrht.hearing_type_desc
,     xc.CASE_TYPE||xc.case_number AS CASE_NO 
,     xd.surname||' '||xd.first_name||' '||xd.middle_name AS DEFENDANT_NAME
,     xcd.CONTACT_VALUE AS COURT_TELEPHONE
FROM xhb_case_listing_entry xcle
,    xhb_defendant_on_case xdoc
,    xhb_defendant xd
,    xhb_case xc
,    xhb_court xcrt
,    xhb_address cxa --court address
,    xhb_address dxa --defendant address
,    xhb_case_diary_fixture xcdf
,    xhb_ref_hearing_type xrht
,    xhb_contact_detail xcd
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
AND   xcd.ADDRESS_ID = cxa.address_id
AND   xc.court_id = p_court_id
/*get the earliest listing date*/
AND   trunc(xcdf.listing_date) = (SELECT MIN (trunc(f.listing_date))
                                  FROM xhb_case_diary_fixture f
                                  WHERE f.case_listing_entry_id = xcdf.case_listing_entry_id)
AND EXISTS (SELECT 'x'
            FROM xhb_case_diary_fixture xcdf
           WHERE xcle.case_listing_entry_id = xcdf.case_listing_entry_id
           AND xcdf.fixture_notice_required = 'Y'
           )
AND   xcd.CONTACT_TYPE = 'Phone';

  UPDATE_REPORT_LOG(p_court_id, 'NFIX', 'Notification Of Fixture');
    
 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_NFIX_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_nfix_report;

  

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
        ORDER BY  NVL(CASE_NUMBER, 0), CREATION_DATE) c)
              
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
        ORDER BY  NVL(CASE_NUMBER, 0), CREATION_DATE) c)
                
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
Select distinct 
                xcdf.CASE_DIARY_FIXTURE_ID,
                xcdf.LISTING_DATE,
                to_char(xcdf.LISTING_DATE,'dd-mm-yyyy') as HEARING_DATE,
                xc.CASE_TYPE||xc.CASE_NUMBER as CASE_NUMBER ,
                xc.CASE_TITLE,
                xc.CLASS_CODE,
                get_bc_status_ind(xc.CASE_ID) as BC_STATUS,
                xrht.HEARING_TYPE_CODE as HEARING_TYPE,
                xdfc.TRIAL_TIME_ESTIMATE||decode(xdfc.TRIAL_TIME_UNIT,'1','D','2','W','3','M') as EST,
                xrs.COURT_SITE_CODE as SITE,
                Highlight_Notes.DIARY_NOTE_TEXT as HIGHLIGHT_NOTE,
                Interpreter_Notes.DIARY_NOTE_TEXT as INTERPRETER_NOTE,
                xrf.SURNAME || '(' || xrf.JUDGE_TYPE || ')'  as REQUIRED_JUDGE,
                decode(xc.VIDEO_LINK_REQUIRED,'Y','Video Link','Not Required')as VIDEO_LINK_REQUIRED,
                xcdf.LIST_NOTE_TEXT as LN     
from   XHB_CASE_LISTING_ENTRY xcle
      ,XHB_CASE xc
      ,XHB_CASE_DIARY_FIXTURE xcdf
      ,XHB_REF_HEARING_TYPE xrht
      ,XHB_DIRECTIONS_FOR_CASE xdfc 
      ,XHB_COURT_SITE xrs      
      ,(SELECT xdne.CASE_LISTING_ENTRY_ID,
               nvl(xrld_pref.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) DIARY_NOTE_TEXT
        FROM XHB_REF_LISTING_DATA xrld
            ,XHB_DIARY_NOTE_ENTRY xdne
            ,XHB_REF_LISTING_DATA xrld_pref      
        WHERE xrld.REF_DATA_TYPE = 'NOTE_TYPE'
          and xrld.REF_DATA_VALUE = 'IN'
          and NVL(xdne.OBS_IND, '-') <> 'Y'
          and xrld_pref.ref_listing_data_id(+) = xdne.DIARY_NOTE_PRE_DEFINED_ID
          and xrld_pref.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE'
          and xrld.REF_LISTING_DATA_ID = xdne.NOTE_TYPE_ID) Interpreter_Notes 
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
      and xcdf.VACATION_PRE_DEFINED_RSON_ID is NULL
      and xcdf.VACATION_FREETEXT_REASON is NULL       
      and xrht.REF_HEARING_TYPE_ID = xcdf.HEARING_TYPE_ID
      and xdfc.CASE_ID =  xc.CASE_ID
      and xrs.COURT_ID = p_court_id
      and xrs.COURT_SITE_ID = xcdf.COURT_SITE_ID      
      and Highlight_Notes.CASE_LISTING_ENTRY_ID(+) = xcle.case_listing_entry_id   
      and Interpreter_Notes.CASE_LISTING_ENTRY_ID(+) = xcle.case_listing_entry_id   
      and trunc(xcdf.LISTING_DATE) BETWEEN trunc(p_hearing_from_date) and trunc(nvl(p_hearing_end_date,xcdf.LISTING_DATE))
      and xcle.JUDGE_ID = xrf.REF_JUDGE_ID(+) 
      and NVL(xcdf.OBS_IND, '-') <> 'Y'
      and NVL(xcle.OBS_IND, '-') <> 'Y' 
ORDER BY xcdf.LISTING_DATE;

   	UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE COURT_ID = p_court_id AND CREST_REPORT_CODE = 'CFIX';

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_CFIX_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_cfix_report ;

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
 v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);	
  BEGIN
  OPEN p_results_out FOR
SELECT     
           notes.DIARY_NOTE_ENTRY_ID,
           xc.CASE_TYPE||xc.CASE_NUMBER as CASE_NUMBER,
           xc.CASE_TITLE,
          NVL((SELECT decode(MAX(xdoc.is_juvenile),'Y','Juv',' ') FROM xhb_defendant_on_case xdoc
            WHERE xdoc.case_id = xc.case_id),' ') AS Juvenile,
           to_char(NVL(xc.committal_date,xc.sent_for_trial_date),'dd-mm-yyyy') as  Commited_Sent,
	         xc.CLASS_CODE,
           xrht.HEARING_TYPE_CODE as HEARING_TYPE,
		       xrmc.monitoring_category_code,
           xdfc.TRIAL_TIME_ESTIMATE||decode(xdfc.TRIAL_TIME_UNIT,'1','D','2','W','3','M') as LOEST,
		       xc.CASE_GROUP_NUMBER,
             to_char((select min(xcnad.start_date)
             from xhb_case_non_avail_days xcnad 
             where  xcnad.CASE_ID = xc.case_id
             group by xcnad.CASE_ID),'dd-mm-yyyy') as First_NAD,
            nvl((select 'Y' 
                     from   xhb_case_on_list xconl
                     where  xconl.case_id = xc.case_id
                      and rownum = 1),'N') as Listed,
			     notes.NOTE_CLASSIFICATION,
           notes.DIARY_NOTE_TEXT          
    FROM   XHB_CASE xc
          ,XHB_REF_HEARING_TYPE xrht
          ,XHB_DIRECTIONS_FOR_CASE xdfc
          ,XHB_REF_MONITORING_CATEGORY xrmc
		  ,(SELECT xdne.CASE_ID,
               xdne.DIARY_NOTE_ENTRY_ID DIARY_NOTE_ENTRY_ID,
               nvl(xrld_pref.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) DIARY_NOTE_TEXT,
               xrld.REF_DATA_VALUE NOTE_CLASSIFICATION
        FROM XHB_REF_LISTING_DATA xrld
            ,XHB_DIARY_NOTE_ENTRY xdne
            ,XHB_REF_LISTING_DATA xrld_pref      
        WHERE xrld.REF_DATA_TYPE = 'NOTE_CLASSIFICATION'
          AND xdne.CASE_ID IS NOT NULL
          AND NVL(xdne.OBS_IND, '-') <> 'Y'
          AND xrld_pref.ref_listing_data_id(+) = xdne.DIARY_NOTE_PRE_DEFINED_ID
          AND xrld_pref.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE'
          AND xrld.REF_LISTING_DATA_ID = xdne.NOTE_CLASSIFICATION_ID
          AND  CASE xrld.REF_DATA_VALUE 
                    WHEN 'Priority' THEN p_PRIOITY_NOTES_Y_N  
	                  WHEN 'Restricted' THEN p_RESTRICTED_NOTES_Y_N
                    ELSE p_STANDARD_NOTES_Y_N END = 'Y' 
          ) notes    
    WHERE xc.DEFAULT_HEARING_TYPE = xrht.REF_HEARING_TYPE_ID(+)
    AND   xrmc.REF_MONITORING_CATEGORY_ID = xc.MONITORING_CATEGORY_ID
    AND 	XHB_CASE_PKG.determine_case_status(xc.CASE_ID) =  'Open' 
    AND   xc.CASE_ID = xdfc.CASE_ID
    AND   xc.COURT_ID = p_court_id
    AND   notes.CASE_ID(+) = xc.CASE_ID
    AND   CASE WHEN ((INSTR(p_CASE_CLASS,'1')>0 AND xc.CLASS_CODE = 1) OR
		         (INSTR(p_CASE_CLASS,'2')>0 AND xc.CLASS_CODE = 2) OR
			         (INSTR(p_CASE_CLASS,'3')>0 AND xc.CLASS_CODE = 3)) THEN 'Y' 
			   WHEN p_CASE_CLASS IS NULL THEN 'Y'	 
			   ELSE 'N' END = 'Y'
   AND   (p_CASE_TYPE IS NULL OR xc.CASE_TYPE = p_CASE_TYPE)
   AND   (p_BC_STATUS IS NULL OR XHB_REPORT_PKG.get_bc_status_ind(xc.CASE_ID) = p_BC_STATUS )
   AND   (p_DEFAULT_HEARING_TYPE IS NULL OR NVL(xrht.HEARING_TYPE_CODE,'~') = p_DEFAULT_HEARING_TYPE)    AND   (p_TIME_EST_FROM IS NULL OR xdfc.TRIAL_TIME_ESTIMATE BETWEEN p_TIME_EST_FROM AND p_TIME_EST_TO)
   AND   (p_TIME_EST_FROM IS NULL or xdfc.TRIAL_TIME_UNIT = p_UNITS)
   AND   (p_REQUIRED_JUDGE_TYPE IS NULL OR exists ( select 1 from xhb_case_listing_entry where case_id = xc.case_id and p_REQUIRED_JUDGE_TYPE = xhb_case_listing_entry.ref_judge_type_id) )
   AND   (p_UNITS_WEEKS IS NULL OR (xdfc.TRIAL_TIME_ESTIMATE > p_UNITS_WEEKS AND xdfc.TRIAL_TIME_UNIT = 3) )
   AND   (p_SECURE_COURTROOM  = 'N' OR xc.SECURE_COURT = p_SECURE_COURTROOM )
   AND   (p_JUVENILE_ONLY = 'N' OR EXISTS (SELECT 1 FROM xhb_defendant_on_case xdoc WHERE xdoc.case_id = xc.case_id AND NVL(xdoc.is_juvenile,'N') = 'Y' AND ROWNUM =1 ))
        
    ORDER BY CASE p_SORTBY WHEN 'CASENUMBER' THEN xc.case_number ELSE 0 END,
      NVL(xc.COMMITTAL_DATE,xc.SENT_FOR_TRIAL_DATE) ;
 
    	UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE COURT_ID = p_court_id AND CREST_REPORT_CODE = 'OUTC';

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_OUTC_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_outc_report;

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
 v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);	
  BEGIN
  OPEN p_results_out FOR
SELECT     
		   notes.DIARY_NOTE_ENTRY_ID,
           xc.CASE_TYPE||xc.CASE_NUMBER as CASE_NUMBER,
           xc.CASE_TITLE,
         NVL((SELECT decode(MAX(xdoc.is_juvenile),'Y','Juv',' ') FROM xhb_defendant_on_case xdoc
            WHERE xdoc.case_id = xc.case_id),' ') AS Juvenile,
           to_char(NVL(xc.committal_date,xc.sent_for_trial_date),'dd-mm-yyyy') as  Commited_Sent,
	         xc.CLASS_CODE,
           xrht.HEARING_TYPE_CODE as HEARING_TYPE,
		       xrmc.monitoring_category_code,
           xdfc.TRIAL_TIME_ESTIMATE||decode(xdfc.TRIAL_TIME_UNIT,'1','D','2','W','3','M') as LOEST,
		       xc.CASE_GROUP_NUMBER,
             to_char((select min(xcnad.start_date)
             from xhb_case_non_avail_days xcnad 
             where  xcnad.CASE_ID = xc.case_id
             group by xcnad.CASE_ID),'dd-mm-yyyy') as First_NAD,
            nvl((select 'Y' 
                     from   xhb_case_on_list xconl
                     where  xconl.case_id = xc.case_id
                      and rownum = 1),'N') as Listed,
			     notes.NOTE_CLASSIFICATION,
           notes.DIARY_NOTE_TEXT 		  
    FROM   XHB_CASE xc
          ,XHB_REF_HEARING_TYPE xrht
          ,XHB_DIRECTIONS_FOR_CASE xdfc
          ,XHB_REF_MONITORING_CATEGORY xrmc
		  ,(SELECT xdne.CASE_ID,
               xdne.DIARY_NOTE_ENTRY_ID DIARY_NOTE_ENTRY_ID,
               nvl(xrld_pref.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) DIARY_NOTE_TEXT,
               xrld.REF_DATA_VALUE NOTE_CLASSIFICATION
        FROM XHB_REF_LISTING_DATA xrld
            ,XHB_DIARY_NOTE_ENTRY xdne
            ,XHB_REF_LISTING_DATA xrld_pref      
        WHERE xrld.REF_DATA_TYPE = 'NOTE_CLASSIFICATION'
          AND xdne.CASE_ID IS NOT NULL
          AND NVL(xdne.OBS_IND, '-') <> 'Y'
          AND xrld_pref.ref_listing_data_id(+) = xdne.DIARY_NOTE_PRE_DEFINED_ID
          AND xrld_pref.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE'
          AND xrld.REF_LISTING_DATA_ID = xdne.NOTE_CLASSIFICATION_ID
          AND  CASE xrld.REF_DATA_VALUE 
                    WHEN 'Priority' THEN p_PRIOITY_NOTES_Y_N  
	                  WHEN 'Restricted' THEN p_RESTRICTED_NOTES_Y_N
                    ELSE p_STANDARD_NOTES_Y_N END = 'Y' 
          ) notes   
    WHERE xc.DEFAULT_HEARING_TYPE = xrht.REF_HEARING_TYPE_ID(+)
    AND   xrmc.REF_MONITORING_CATEGORY_ID = xc.MONITORING_CATEGORY_ID
    AND   NOT EXISTS (SELECT 1 FROM  XHB_CASE_ON_LIST xcol WHERE xcol.CASE_ID = XC.CASE_ID AND ROWNUM = 1)
    AND   NOT EXISTS (SELECT 1 FROM  XHB_CASE_LISTING_ENTRY xcle, XHB_CASE_DIARY_FIXTURE xcdf 
                      WHERE xcle.CASE_ID = XC.CASE_ID  AND xcle.CASE_LISTING_ENTRY_ID = xcdf.CASE_LISTING_ENTRY_ID AND ROWNUM = 1)
    AND 	XHB_CASE_PKG.determine_case_status(xc.CASE_ID) =  'Open'
    AND   xc.CASE_ID = xdfc.CASE_ID
    AND   xc.COURT_ID = p_court_id
	AND   notes.CASE_ID(+) = xc.CASE_ID
    AND   CASE WHEN ((INSTR(p_CASE_CLASS,'1')>0 AND xc.CLASS_CODE = 1) OR
		         (INSTR(p_CASE_CLASS,'2')>0 AND xc.CLASS_CODE = 2) OR
			         (INSTR(p_CASE_CLASS,'3')>0 AND xc.CLASS_CODE = 3)) THEN 'Y' 
			   WHEN p_CASE_CLASS IS NULL THEN 'Y'	 
			   ELSE 'N' END = 'Y'
   AND   (p_CASE_TYPE IS NULL OR xc.CASE_TYPE = p_CASE_TYPE)
   AND   (p_BC_STATUS IS NULL OR XHB_REPORT_PKG.get_bc_status_ind(xc.CASE_ID) = p_BC_STATUS )
   AND   (p_DEFAULT_HEARING_TYPE IS NULL OR NVL(xrht.HEARING_TYPE_CODE,'~') = p_DEFAULT_HEARING_TYPE)    AND   (p_TIME_EST_FROM IS NULL OR xdfc.TRIAL_TIME_ESTIMATE BETWEEN p_TIME_EST_FROM AND p_TIME_EST_TO)
   AND   (p_TIME_EST_FROM IS NULL or xdfc.TRIAL_TIME_UNIT = p_UNITS)
   AND   (p_REQUIRED_JUDGE_TYPE IS NULL OR exists ( select 1 from xhb_case_listing_entry where case_id = xc.case_id and p_REQUIRED_JUDGE_TYPE = xhb_case_listing_entry.ref_judge_type_id) )
   AND   (p_UNITS_WEEKS IS NULL OR (xdfc.TRIAL_TIME_ESTIMATE > p_UNITS_WEEKS AND xdfc.TRIAL_TIME_UNIT = 3) )
   AND   (p_SECURE_COURTROOM  = 'N' OR xc.SECURE_COURT = p_SECURE_COURTROOM )
   AND   (p_JUVENILE_ONLY = 'N' OR EXISTS (SELECT 1 FROM xhb_defendant_on_case xdoc WHERE xdoc.case_id = xc.case_id AND NVL(xdoc.is_juvenile,'N') = 'Y' AND ROWNUM =1 ))
    ORDER BY CASE p_SORTBY WHEN 'CASENUMBER' THEN xc.case_number ELSE 0 END,
      NVL(xc.COMMITTAL_DATE,xc.SENT_FOR_TRIAL_DATE) ;
 
    	UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE COURT_ID = p_court_id AND CREST_REPORT_CODE = 'UNLC';

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_UNLC_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_unlc_report;
  
PROCEDURE get_drsr_report(p_results_out OUT SYS_REFCURSOR                        
                              ,p_court_id IN XHB_CASE.COURT_ID%TYPE
                                      ,p_MONTH_PERIOD IN VARCHAR2
                                     ,p_YEAR_PERIOD IN VARCHAR2 )AS

  v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
BEGIN
  OPEN p_results_out FOR
	Select          
                xsh.NOT_BEFORE_TIME,
                to_char(xsh.NOT_BEFORE_TIME,'dd-mm-yyyy') as LISTING_DATE,
                xc.CASE_TYPE||xc.CASE_NUMBER as CASE_NUMBER ,
                xrht.HEARING_TYPE_CODE as HTYP,
                xs.SITTING_SEQUENCE_NO,
                xcr.COURT_ROOM_NAME,
                xcs.COURT_SITE_NAME as SITE                                 
from   XHB_CASE xc
      ,XHB_SCHEDULED_HEARING xsh
      ,XHB_HEARING xh
      ,XHB_SITTING xs
      ,XHB_REF_HEARING_TYPE xrht
      ,XHB_COURT_SITE xcs
      ,XHB_COURT_ROOM xcr
WHERE  xsh.HEARING_ID = xh.HEARING_ID 
       AND xc.CASE_ID = xh.CASE_ID
       AND xrht.REF_HEARING_TYPE_ID(+) = xh.REF_HEARING_TYPE_ID
       AND xs.SITTING_ID = xsh.SITTING_ID
       AND xcr.COURT_ROOM_ID = xs.COURT_ROOM_ID
       AND xcs.COURT_SITE_ID = xs.COURT_SITE_ID 
       AND to_char(xsh.NOT_BEFORE_TIME,'Mon-YYYY')= p_MONTH_PERIOD||'-'||p_YEAR_PERIOD
       AND xrht.HEARING_TYPE_CODE  IN ( 'TRL','TFL','TBK')
	     AND   xc.COURT_ID = p_court_id
       AND xsh.REF_CRACKED_EFFECTIVE_ID IS NULL
       ORDER BY 1,3;
	   
	   UPDATE XHB_REPORT_LOG SET DATE_LAST_RUN = sysdate WHERE COURT_ID = p_court_id AND CREST_REPORT_CODE = 'DRSR';

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_DRSR_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_drsr_report;

PROCEDURE get_relcj_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE) AS 
   v_err_code NUMBER;
  v_err_msg   VARCHAR2(1000);
  BEGIN
OPEN p_results_out FOR
 SELECT distinct
       xcdf.HEARING_TYPE_ID,
       xc.CASE_ID,
			 xc.CASE_TYPE||xc.CASE_NUMBER as CASE_NUMBER,
       xc.CASE_TITLE,
       xc.CLASS_CODE , 
			 xrht.HEARING_TYPE_CODE as HEARING_TYPE,
			 xdfc.TRIAL_TIME_ESTIMATE||decode(xdfc.TRIAL_TIME_UNIT,'1','D','2','W','3','M') as EST,
			 xrf.SURNAME as REQUIRED_JUDGE,
      get_charges_info(xc.CASE_ID)CHARGES_INFO
FROM XHB_CASE_LISTING_ENTRY xcle
    ,XHB_CASE xc
    ,XHB_REF_HEARING_TYPE xrht
    ,XHB_DIRECTIONS_FOR_CASE xdfc 
    ,XHB_REF_JUDGE xrf 
    ,XHB_CASE_DIARY_FIXTURE xcdf
where xc.CASE_ID = xcle.CASE_ID
and xdfc.CASE_ID(+) =  xc.CASE_ID
and xrht.REF_HEARING_TYPE_ID = xcdf.HEARING_TYPE_ID
and xcdf.CASE_LISTING_ENTRY_ID = xcle.CASE_LISTING_ENTRY_ID
and xcle.JUDGE_ID = xrf.REF_JUDGE_ID
and xc.COURT_ID = p_court_id 
and NVL(xcdf.OBS_IND, '-') <> 'Y'
and NVL(xcle.OBS_IND, '-') <> 'Y'
and XHB_CASE_PKG.determine_case_status(xc.CASE_ID) =  'Open' ;


  UPDATE_REPORT_LOG(p_court_id, 'RELCJ', 'CASES WITH REQUIRED JUDGE');
    
 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_RELCJ_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;
END get_relcj_report;
  
  PROCEDURE get_list_of_fixed_dates_report(p_results_out out SYS_REFCURSOR
                                        ,p_court_id IN XHB_CASE.COURT_ID%TYPE
                                        ,p_run_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE) AS

 v_err_code NUMBER;
 v_err_msg  VARCHAR2(1000);
   BEGIN
     OPEN p_results_out FOR 
      SELECT
      xdc.defendant_on_case_id docid,
         xrht.HEARING_TYPE_DESC AS hearingtype,
        xc.CASE_TYPE || xc.CASE_NUMBER AS casenumber , 
        CASE
           WHEN xd.CURRENT_PRISON_STATUS = 'Y' THEN '*' || UPPER(xd.FIRST_NAME) || ' ' || UPPER(SUBSTR(xd.MIDDLE_NAME,1,1)) || ' ' || LOWER(xd.SURNAME)
           ELSE UPPER(xd.FIRST_NAME) || ' ' || UPPER(SUBSTR(xd.MIDDLE_NAME,1,1)) || ' ' || LOWER(xd.SURNAME)
        END AS defendant,
        (SELECT xrs.SOLICITOR_FIRM_NAME FROM XHB_REF_SOLICITOR_FIRM xrs
                  WHERE xrs.REF_SOLICITOR_FIRM_ID = GET_SOLICITOR_ID(xdc.DEFENDANT_ON_CASE_ID)) AS representative,
        xcd.CONTACT_VALUE AS telephoneno,
        xdc.PTIURN AS ptiurn,
        xcdf.LISTING_DATE AS hearingdate,
        xco.COURT_NAME AS hearingvenue,
        xrpa.prosecutor_name_1 || ' ' || xrpa.prosecutor_name_2 || ' ' || xrpa.prosecutor_name_3 AS prosecutor,
        xcdf.LIST_NOTE_TEXT AS notes,
        xa.ADDRESS_1 || ' '  || xa.ADDRESS_2 || ' ' || xa.ADDRESS_3 || ' ' || xa.ADDRESS_4 || ' ' ||  xa.TOWN || ' ' || xa.COUNTY || ' ' || xa.POSTCODE AS courtaddress,
        xcdf.CASE_DIARY_FIXTURE_ID AS casediaryfixture,
        xcpa.REF_PROSECUTOR_AGENCY_ID
        
        FROM XHB_CASE xc
        INNER JOIN XHB_DEFENDANT_ON_CASE xdc ON xc.CASE_ID = xdc.CASE_ID
        INNER JOIN XHB_HEARING xh ON xc.CASE_ID = xh.CASE_ID
        INNER JOIN XHB_REF_HEARING_TYPE xrht ON xh.REF_HEARING_TYPE_ID =  xrht.REF_HEARING_TYPE_ID
        INNER JOIN XHB_DEFENDANT xd ON xdc.DEFENDANT_ID = xd.DEFENDANT_ID
        INNER JOIN XHB_COURT xco ON xd.COURT_ID = xco.COURT_ID
        INNER JOIN XHB_ADDRESS xa ON xco.ADDRESS_ID = xa.ADDRESS_ID
        INNER JOIN XHB_CONTACT_DETAIL xcd ON xa.ADDRESS_ID = xcd.ADDRESS_ID AND xcd.CONTACT_TYPE = 'Phone'
        INNER JOIN XHB_FIXTURE_DEFT_ATTENDING xfda ON xdc.DEFENDANT_ON_CASE_ID = xfda.DEFENDANT_ON_CASE_ID
        INNER JOIN XHB_CASE_DIARY_FIXTURE xcdf ON xfda.CASE_DIARY_FIXTURE_ID = xcdf.CASE_DIARY_FIXTURE_ID
        INNER JOIN XHB_CASE_PROSECUTOR_AGENCY xcpa ON xc.CASE_ID = xcpa.CASE_ID
        INNER JOIN XHB_REF_PROSECUTOR_AGENCY xrpa ON xcpa.REF_PROSECUTOR_AGENCY_ID = xrpa.REF_PROSECUTOR_AGENCY_ID

        WHERE xc.CASE_SUB_TYPE in ('B','S','C') AND xc.COURT_ID = p_court_id AND (xcdf.FXL_RUN_DATE > p_run_date OR xcdf.FXL_RUN_DATE IS NULL)
        
        ORDER BY hearingtype,casenumber,defendant,representative,telephoneno,ptiurn,hearingdate,hearingvenue,prosecutor,notes,courtaddress,casediaryfixture;
         
        UPDATE XHB_REPORT_LOG SET  DATE_LAST_RUN = sysdate WHERE REPORT_NAME = 'List of Fixed Dates' AND COURT_ID = p_court_id;
        
  EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_LIST_OF_FIXED_DATES_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
END get_list_of_fixed_dates_report;

PROCEDURE update_case_diary_fix_run_date(casediaryfixturelist IN VARCHAR) AS
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);

BEGIN
  UPDATE XHB_CASE_DIARY_FIXTURE SET FXL_RUN_DATE = sysdate 
  WHERE  ','||casediaryfixturelist||',' LIKE '%,'||CAST(CASE_DIARY_FIXTURE_ID AS VARCHAR(1000))||',%';
  
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
               xc.CASE_TYPE || xc.CASE_NUMBER || '-' || xdoc.DEFENDANT_NUMBER AS CASE_NUMBER,
               xc.CASE_TYPE,
               xd.SURNAME,
               xd.FIRST_NAME,
               xd.MIDDLE_NAME,
               xd.GENDER,
               xd.DATE_OF_BIRTH,
               xc.PRELIMINARY_DATE_OF_HEARING,
               (SELECT xrsf.SOLICITOR_FIRM_NAME FROM XHB_REF_SOLICITOR_FIRM xrsf 
                  WHERE xrsf.REF_SOLICITOR_FIRM_ID = GET_SOLICITOR_ID(xdoc.DEFENDANT_ON_CASE_ID)) AS SOLICITOR_FIRM_NAME,
               (SELECT xcd.CONTACT_VALUE FROM XHB_REF_SOLICITOR_FIRM xrsf, XHB_CONTACT_DETAIL xcd
                  WHERE xrsf.REF_SOLICITOR_FIRM_ID = GET_SOLICITOR_ID(xdoc.DEFENDANT_ON_CASE_ID)
                  AND xrsf.ADDRESS_ID = xcd.ADDRESS_ID
                  AND xcd.CONTACT_TYPE = 'Phone')  AS SOLICITOR_PHONE_NUMBER,   
               xdoc.CURRENT_BC_STATUS,
               xcrt.COURT_NAME,
               xrpa.PROSECUTOR_NAME_1 || ' ' || xrpa.PROSECUTOR_NAME_2 || ' ' || xrpa.PROSECUTOR_NAME_3 as PROSECUTOR_NAME,
               xdoc.PTIURN,
               xc.CLASS_CODE,
               xc.SENT_FOR_TRIAL_DATE,
               xc.COMMITTAL_DATE,
               xc.APPEAL_LODGED_DATE,
               get_charge(xc.case_id) charges,
               CASE WHEN xd.SURNAME IS NULL 
                    OR xrpa.PROSECUTOR_NAME_3 IS NULL
                 THEN 1 
                 ELSE 0
               END AS INCOMPLETE_CASE
            FROM XHB_CASE xc,
            XHB_DEFENDANT_ON_CASE xdoc,
            XHB_DEFENDANT xd,
            XHB_HEARING xh,
            XHB_COURT xcrt,
            XHB_CASE_PROSECUTOR_AGENCY xcpa,
            XHB_REF_PROSECUTOR_AGENCY xrpa
        WHERE xc.COURT_ID = p_court_id
        AND xc.CASE_ID = xdoc.CASE_ID (+)
        AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID (+)
        AND xc.CASE_ID = xh.CASE_ID (+)       
        AND xc.COURT_ID = xcrt.COURT_ID
        AND xc.CASE_ID = xcpa.CASE_ID (+)
        AND xcpa.REF_PROSECUTOR_AGENCY_ID  = xrpa.REF_PROSECUTOR_AGENCY_ID (+)
        AND (xrpa.OBS_IND <> 'Y'
        OR xrpa.OBS_IND IS NULL)
        AND ((p_previous_list_id = 0
          AND xc.PUB_RUNNING_LIST_ID IS NULL)
        OR (p_previous_list_id > 0
          AND xc.PUB_RUNNING_LIST_ID = p_previous_list_id))
        ORDER BY xc.CASE_NUMBER;

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_PRLIS_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;                  
  END get_prlis_report; 
  
PROCEDURE publish_running_list(p_cases_to_publish IN CLOB, p_court_id IN INTEGER) AS
v_err_code NUMBER;
v_err_msg  VARCHAR2(1000);
v_next_pub_running_list_id NUMBER;
v_offset number default 1;
v_chunk_size number := 1000;
v_chunk VARCHAR2(2000);
p_cases_to_publish_list CLOB := p_cases_to_publish || ',';
BEGIN

 SELECT XHB_PUB_RUNNING_LIST_SEQ.nextval INTO v_next_pub_running_list_id FROM dual;
 INSERT INTO XHB_PUB_RUNNING_LIST (PUB_RUNNING_LIST_ID, COURT_ID,PUBLISHED_DATE) VALUES (v_next_pub_running_list_id, p_court_id, sysdate);
 
    LOOP
       EXIT WHEN v_offset >= dbms_lob.getlength(p_cases_to_publish_list);
          v_chunk := REGEXP_SUBSTR(p_cases_to_publish_list, '.{1,' || v_chunk_size  || '},', v_offset); --Get the next 1000 characters that ends with ,
          v_offset := v_offset +  LENGTH(v_chunk); 
          UPDATE XHB_CASE SET PUB_RUNNING_LIST_ID = v_next_pub_running_list_id
          WHERE  ','||v_chunk||',' LIKE '%,'||CAST(CASE_ID AS VARCHAR2(1000))||',%';
  END LOOP;
  
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
      xc.CASE_LISTED AS listed, xc.RECEIPT_TYPE as receipttype,
      xrpa.prosecutor_name_1 || ' ' || xrpa.prosecutor_name_2 || ' ' || xrpa.prosecutor_name_3 AS prosecutorname,
      CASE WHEN xdc.CUSTODY_TIME_LIMIT+7 >= p_screen_time_limit THEN 'Y'
      ELSE 'N'
      END AS highlightedrow
      
      FROM XHB_CASE xc
      INNER JOIN XHB_DEFENDANT_ON_CASE xdc ON xc.CASE_ID = xdc.CASE_ID AND (xdc.OBS_IND <> 'Y' OR xdc.OBS_IND IS NULL)
      INNER JOIN XHB_DEFENDANT xd ON xdc.DEFENDANT_ID = xd.DEFENDANT_ID 
      INNER JOIN XHB_CASE_PROSECUTOR_AGENCY xcpa ON xc.CASE_ID = xcpa.CASE_ID AND (xcpa.OBS_IND <> 'Y' OR xcpa.OBS_IND IS NULL)
      INNER JOIN XHB_REF_PROSECUTOR_AGENCY xrpa ON xcpa.REF_PROSECUTOR_AGENCY_ID = xrpa.REF_PROSECUTOR_AGENCY_ID AND (xrpa.OBS_IND <> 'Y' OR xrpa.OBS_IND IS NULL)

      WHERE xc.RECEIPT_TYPE in ('CT','TC','VB','ST', 'IO', 'EW') AND xc.COURT_ID = p_court_id AND xdc.CUSTODY_TIME_LIMIT <= p_screen_time_limit
      
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
 FUNCTION get_charge (p_case_id IN xhb_case.case_id%TYPE) RETURN VARCHAR2 IS
 
 charges_return VARCHAR2(4000) := NULL; --first call make sure the return value is null
 
 CURSOR get_charge_c 
 IS
 SELECT xrsc.DE_CODE
 FROM xhb_charge xch
 ,    xhb_ref_system_code xrsc
 WHERE xch.ref_system_code_id = xrsc.ref_system_code_id
 AND xch.case_id = p_case_id;
 
 BEGIN
 
  FOR get_charge_row IN get_charge_c
   LOOP
     charges_return := charges_return|| get_charge_row.DE_CODE || chr(13) || chr(10); --For each loop iteration concatenate the charges   
    END LOOP;
 
 RETURN charges_return;

  EXCEPTION
   WHEN OTHERS THEN
		RETURN (SQLCODE||' '||'When others exception ' || SQLERRM);
 END get_charge;
 
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
      AND   NOT EXISTS (SELECT 1 FROM XHB_LEGAL_AID_ORDER xlao 
                            WHERE p_doc_id  = xlao.DEFENDANT_ON_CASE_ID 
                            AND   NVL(xlao.OBS_IND, '-') <> 'Y'
                            AND   xlao.DATE_OF_REVOCATION < sysdate)
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
  * Function getPhoneNum  (CTX-2145)
  *
  * Returns a phone number for a given address_id.
  *
  *
  **************************************************************************************/
Function getPhoneNum(p_address_id  xhb_address.address_id%type) RETURN xhb_contact_detail.contact_value%type IS 

v_phone_num   xhb_contact_detail.contact_value%type;

BEGIN
    SELECT  cdet.contact_value 
    INTO    v_phone_num 
    FROM    xhb_contact_detail cdet 
    WHERE   cdet.contact_type = 'Phone' 
    AND     cdet.address_id = p_address_id;
    
    RETURN  v_phone_num;
    
END getPhoneNum;

  /**********************************************************************************
  *
  * Function getAppellantAddress  (CTX-1335)
  *
  * Returns the address of an Appellant, or the address of their solicitor (if they have one) 
  * Takes a defendant_on_case_id as input parameter.
  *
  **************************************************************************************/
Function getAppellantAddress(p_deft_on_case_id  xhb_defendant_on_case.defendant_on_case_id%type) RETURN VARCHAR2 IS 

v_ref_solicitor_firm_id xhb_ref_solicitor_firm.ref_solicitor_firm_id%type;
v_address               varchar2(500);
v_address_id            xhb_address.address_id%type;

BEGIN
    -- First we look up the ID of the Appellant's solicitor. If null, they don't have one
     v_ref_solicitor_firm_id := XHB_REPORT_PKG.get_solicitor_id(p_deft_on_case_id);
     
     IF v_ref_solicitor_firm_id is null then
         -- Appellant has no solicitor. We use his own address
         Select df.address_id 
         Into   v_address_id 
         from   xhb_defendant_on_case doc, xhb_defendant df 
         where  doc.defendant_id = df.defendant_id 
         and    doc.defendant_on_case_id = p_deft_on_case_id;
     Else
         select rsf.address_id 
         into   v_address_id 
         from   xhb_ref_solicitor_firm rsf 
         where  rsf.ref_solicitor_firm_id = v_ref_solicitor_firm_id;
     End If; 
         
     v_address := getAddress(v_address_id);
     
     Return v_address;
         
END getAppellantAddress;

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
v_step      VARCHAR2(10);

BEGIN  
    v_step := '1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;
    
    v_step := '2';    
    open p_resultset for
    -- This query considers cases which are listed in a future Firm list
    select  cas.case_id, cas.case_number, ct.crest_court_id, ct.court_name, 
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
            to_date(sysdate) as rpt_run_date, cdet.contact_value as ct_phone_no 
    from    xhb_case cas, xhb_defendant df, xhb_defendant_on_case doc, xhb_ref_solicitor_firm rsf,   
            xhb_case_prosecutor_agency cpa, xhb_ref_prosecutor_agency rpa, 
            -- tables unique to the 1st query
            xhb_case_on_list csol, xhb_list lst, xhb_ref_listing_data rld, 
            xhb_sitting_on_list sol,                     
            -- tables unique to the 1st query (end)
            xhb_court ct, xhb_ref_system_code r, xhb_contact_detail cdet   
    where  df.defendant_id = doc.defendant_id 
    and    doc.case_id = cas.case_id 
    and    rsf.ref_solicitor_firm_id(+) = XHB_REPORT_PKG.get_solicitor_id(doc.defendant_on_case_id) 
    and    cpa.case_id = cas.case_id
    and    cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id 
    and    ct.address_id = cdet.address_id 
    and    cdet.contact_type = 'Phone' 
    -- clauses unique to 1st query 
    and    cas.case_id = csol.case_id 
    and    csol.list_id = lst.list_id  
    and    csol.sitting_on_list_id = sol.sitting_on_list_id(+) -- added for CTX-2348 Is outer join as per Action 3.
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
    and    r.code_type like '%APPEAL_TYP%' 

    union 
    -- This query considers cases which have a future fixture
    select  cas.case_id, cas.case_number, ct.crest_court_id, ct.court_name, 
            -- Appellant columns
            doc.defendant_number, (df.first_name || ' ' || df.middle_name || ' ' || df.surname) as appellant_name, 
            rsf.solicitor_firm_name, getAppellantAddress(doc.defendant_on_case_id) defendant_address, 
            -- Respondent columns
            rpa.prosecutor_name_1 || ' ' || rpa.prosecutor_name_2 || ' ' || rpa.prosecutor_name_3 as respondent_name, 
            getaddress(rpa.address_id) as respondent_address, 
            -- Court columns 
            getaddress(ct.address_id) as court_address,  
            Replace(r.de_code, 'APPEAL AGAINST ') as appeal_type, 'Clerk to Justice' as clerk_to_justice,
            cas.mag_conviction_date, cdf.listing_date,
            to_char(case (cdf.listing_date - trunc(cdf.listing_date))   -- added for CTX-2348
                    when 0.0 THEN TO_DATE(ct.court_start_time, 'HH24:MI')        -- added for CTX-2348
                    else cdf.listing_date                    -- added for CTX-2348
                    end ,'HH24:MI')  as time_listed,
            to_date(sysdate), cdet.contact_value as ct_phone_no 
    from    xhb_case cas, xhb_defendant df, xhb_defendant_on_case doc, xhb_ref_solicitor_firm rsf,  
            xhb_case_prosecutor_agency cpa, xhb_ref_prosecutor_agency rpa, 
            -- tables unique to the 2nd query
            xhb_case_listing_entry cle, xhb_case_diary_fixture cdf, 
            -- tables unique to the 2nd query (end)
            xhb_court ct, xhb_ref_system_code r, xhb_contact_detail cdet   
    where  df.defendant_id = doc.defendant_id 
    and    doc.case_id = cas.case_id 
    and    rsf.ref_solicitor_firm_id(+) = XHB_REPORT_PKG.get_solicitor_id(doc.defendant_on_case_id) 
    and    cpa.case_id = cas.case_id
    and    cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id 
    and    ct.address_id = cdet.address_id 
    and    cdet.contact_type = 'Phone' 
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
    and    r.code_type like '%APPEAL_TYP%' 
    order by 1	;
    
    -- We have to set the Fixture_Notice_Required and NHA_Firm_List flags. This prevents the letters being re-printed next time the report is run.
    UPDATE XHB_CASE_DIARY_FIXTURE set fixture_notice_required = 'N' 
    WHERE  case_listing_entry_id in (select cle.case_listing_entry_id 
                                     from   xhb_case_listing_entry cle, xhb_case_diary_fixture cdf, xhb_case cas, xhb_ref_system_code r  
                                     where  cas.case_type = 'A'       -- Appeal
                                     and    cas.court_id = p_court_id -- parameter here
                                     and    cas.case_sub_type = r.code(+)
                                     and    r.code_type like '%APPEAL_TYP%'
                                     and    cas.case_id = cle.case_id
                                     and    cle.case_listing_entry_id = cdf.case_listing_entry_id  
                                     and    cdf.listing_date > sysdate 
                                     and    nvl(cdf.fixture_notice_required, 'N') = 'Y' );
                                     
    UPDATE XHB_CASE_ON_LIST set nha_firm_list = 'N' 
    WHERE  case_id in (select cas.case_id
                       from   xhb_case cas, xhb_ref_system_code r, xhb_case_on_list csol, xhb_list lst, xhb_ref_listing_data rld 
                       where  cas.court_id = p_court_id -- parameter here
                       and    cas.case_type = 'A'      -- Appeal
                       and    cas.case_sub_type = r.code(+)
                       and    r.code_type like '%APPEAL_TYP%' 
                       and    cas.case_id = csol.case_id 
                       and    csol.list_id = lst.list_id  
                       and    nvl(csol.nha_firm_list, 'N') = 'Y'
                       and    lst.list_start_date > sysdate 
                       and    lst.list_type_id = rld.ref_listing_data_id 
                       and    rld.ref_data_type = 'LIST_TYPE'
                       and    rld.ref_data_value = 'Firm' );

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
   ORDER BY DIARY_DATE, NVL(CASE_NUMBER, 0), CREATION_DATE;
 
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
and rsf.REF_SOLICITOR_FIRM_ID(+) = XHB_REPORT_PKG.GET_SOLICITOR_ID(doc.DEFENDANT_ON_CASE_ID)
and cas4.court_id = p_court_id
 and NVL(doc.OBS_IND, '-') <> 'Y'
      and NVL(bwh.OBS_IND, '-') <> 'Y' 
      and NVL(rsf.OBS_IND, '-') <> 'Y';

 
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
  * Columns:      monetary_order_tracking_id, court_full_name, court_address, case_number,  
  *               defendant, order_date, fined (amount), compensation (amount), costs (amount), collect_magistrates_court_id
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
    select mot.monetary_order_tracking_id, ct.court_name as court_name, getAddress(ct.address_id)as ct_address, getPhoneNum(ct.address_id) as ct_phone, ct2.court_full_name as collect_court_name, getAddress(ct2.address_id) as collect_court_address, 
           case.case_type || case.case_number as case_number, doc.defendant_number,  df.surname || ' ' || df.first_name || ' ' || df.middle_name as defendant, doc.ptiurn, 
           to_char(mot.order_date, 'DD-MON-YYYY') as order_date, 
           case when mot.fined is null then '' else '£' || mot.fined end  as fined, 
           case when mot.compensation is null then '' else'£'|| mot.compensation end as compensation,  
           case when mot.costs is null then '' else'£'|| mot.costs end as costs, 
           to_char(to_date(sysdate), 'DD-MON-YYYY') as today_date
    from   xhb_monetary_order_tracking mot, xhb_case case, xhb_defendant_on_case doc, xhb_defendant df, xhb_court ct, xhb_ref_court ct2
    where  mot.case_id = case.case_id 
    and    case.court_id = ct.court_id -- By restricting cases to those for the selected court, we ensure that any monetary orders found are issued by that court.
    and    ct.court_id = p_court_id     -- parameter here
    and    nvl(ct.obs_ind, 'N') != 'Y'
    and    mot.acknowledgement_date is null  -- The monetary order has not been acknowledged
    and    mot.defendant_on_case_id = doc.defendant_on_case_id 
    and    doc.defendant_id = df.defendant_id  
    and    nvl(doc.obs_ind, 'N') != 'Y'
    and    mot.collect_magistrates_court_id = ct2.ref_court_id 
    and    nvl(ct2.obs_ind, 'N') != 'Y'
    order by ct2.court_full_name, case.case_number; 
    
    
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

  listing_date DATE:=NULL;
  listing_date1 DATE;
  listing_date2 DATE;
  
  BEGIN
 
   SELECT MIN(xcdf2.LISTING_DATE) INTO listing_date1
   FROM XHB_CASE_DIARY_FIXTURE xcdf2, XHB_CASE_LISTING_ENTRY xcle 
   WHERE xcdf2.CASE_LISTING_ENTRY_ID =  xcle.CASE_LISTING_ENTRY_ID 
   AND xcle.CASE_ID = p_case_id;
 
   SELECT MIN(xcol2.TIME_LISTED) INTO listing_date2
   FROM XHB_CASE_ON_LIST xcol2, XHB_DEF_ON_CASE_ON_LIST xdocol
   WHERE xcol2.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID AND
   xdocol.DEFENDANT_ON_CASE_ID = p_def_case_id;
   
   IF listing_date1 IS NULL THEN
    listing_date:=listing_date2;
   ELSIF listing_date2 IS NULL THEN
    listing_date:=listing_date1;
   ELSE
    listing_date:=LEAST(listing_date1,listing_date2);
   END IF;
   
  RETURN LISTING_DATE;
  
   EXCEPTION
   WHEN OTHERS THEN
		RETURN (SQLCODE||' '||'When others exception ' || SQLERRM);
  
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
      xc.CASE_TYPE || xc.CASE_NUMBER AS casenumber,
      xc.CASE_TITLE AS casetitle,
      GET_JUVENILE_STATUS(xc.CASE_ID) AS juvenile,
      TO_CHAR(xc.CLASS_CODE) AS classcode, 
      get_bc_status_ind(xc.CASE_ID) AS bcstatus,
      regexp_replace(TO_CHAR(NVL(xc.committal_date,xc.sent_for_trial_date),'DD Month YYYY'),'[[:space:]]+',chr(32)) AS commitalsent,
      GET_LINKED_CASES_LIST(p_court_id,xc.CASE_GROUP_NUMBER) AS linkedcaseslist,
      GET_LIST_HISTORY(xc.CASE_ID) AS listhistorylist,
      GET_DIARY_NOTES_LIST(xc.CASE_id) AS noteslist,
      xcrt.COURT_NAME AS sitecommited,
      TO_CHAR(TRUNC((SYSDATE-NVL(xc.committal_date,xc.sent_for_trial_date))/7)) AS weekdiff,
      GET_OFFENCES(xc.CASE_ID) AS offences,
      xrmc.MONITORING_CATEGORY_CODE AS monitoringcategory,
      GET_BENCH_WARRANT_DATE(xc.CASE_ID) AS benchwarrantdate

      FROM XHB_CASE xc
      INNER JOIN XHB_COURT xcrt ON xc.COURT_ID = xcrt.COURT_ID
      INNER JOIN XHB_REF_MONITORING_CATEGORY xrmc ON xc.MONITORING_CATEGORY_ID = xrmc.REF_MONITORING_CATEGORY_ID
  
      WHERE xc.COURT_ID = p_court_id AND XHB_CASE_PKG.determine_case_status(xc.CASE_ID) = 'Open' AND  ((p_class_code_list IS NULL AND xc.CLASS_CODE IS NULL) OR  (','||NVL(p_class_code_list,'')||',' LIKE '%,'||CAST(xc.CLASS_CODE AS VARCHAR(1000))||',%' AND p_class_code_list IS NOT NULL)) AND
      (((sysdate - (p_from_between*7)) <= (NVL(xc.committal_date,xc.sent_for_trial_date)) AND p_to_between IS NULL)
      OR (NVL(xc.committal_date,xc.sent_for_trial_date) BETWEEN (sysdate - (p_to_between*7)) AND (sysdate - (p_from_between*7)))) AND
      (p_bc_status IS NULL OR get_bc_status_ind(xc.CASE_ID) = NVL(p_bc_status,''))
      
      ORDER BY commitalsent;
        
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
v_bw_date DATE:=NULL;

CURSOR get_bench_warrant_c IS
  SELECT xbw.BW_END_DATE
  FROM XHB_BW_HISTORY xbw
  WHERE xbw.DEFENDANT_ON_CASE_ID = p_case_id;
  
    BEGIN
      FOR get_bench_warrant_row IN get_bench_warrant_c
        LOOP
          IF get_bench_warrant_row.BW_END_DATE IS NOT NULL THEN 
            v_bw_date := get_bench_warrant_row.BW_END_DATE;
            EXIT;
         END IF;
       END LOOP;
  RETURN v_bw_date;
  
END GET_BENCH_WARRANT_DATE;

FUNCTION GET_LINKED_CASES_LIST( p_court_id IN XHB_CASE.court_id%TYPE,
                                  p_group_number IN XHB_CASE.CASE_GROUP_NUMBER%TYPE) RETURN CLOB IS

linked_cases_list CLOB := NULL;                      
pos int;

    CURSOR linked_cases_list_c IS
      SELECT xc.CASE_TYPE ||  xc.CASE_ID AS casenumber
      FROM 	XHB_CASE xc
      WHERE xc.CASE_GROUP_NUMBER = p_group_number
      AND xc.COURT_ID = p_court_id;
      
      BEGIN
        FOR linked_cases_list_row IN linked_cases_list_c
    LOOP
    
    linked_cases_list:= linked_cases_list || linked_cases_list_row.casenumber || '|';
    
    END LOOP;

  pos:=INSTR(linked_cases_list,'|',-1);
  
  IF (pos>0) THEN
    linked_cases_list:=REGEXP_REPLACE(linked_cases_list,'|','',pos,1);
  END IF;
  
  RETURN linked_cases_list;
                             
END GET_LINKED_CASES_LIST;

FUNCTION GET_JUVENILE_STATUS(p_case_id IN XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE) RETURN VARCHAR2 IS
v_juv_status VARCHAR2(1):='N';

CURSOR get_juvenile_status_c IS
  SELECT xdoc.IS_JUVENILE
  FROM XHB_DEFENDANT_ON_CASE xdoc
  WHERE xdoc.CASE_ID = p_case_id;
  
  BEGIN
      FOR get_juvenile_status_row IN get_juvenile_status_c
        LOOP
          IF get_juvenile_status_row.IS_JUVENILE ='Y' THEN 
            v_juv_status := 'Y';
            EXIT;
         END IF;
       END LOOP;
  RETURN v_juv_status;

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

  CURSOR diary_notes_list_c IS
  SELECT xde.DIARY_NOTE_TEXT AS diarynote
  FROM XHB_DIARY_NOTE_ENTRY xde,XHB_REF_LISTING_DATA xrld
  WHERE 
  xrld.REF_LISTING_DATA_ID = xde.NOTE_TYPE_ID AND
  xrld.REF_DATA_TYPE = 'NOTE_TYPE' AND
  xrld.REF_DATA_VALUE IN ('IN','HN','CN','GDN') AND
  xrld.REF_LISTING_DATA_ID = xde.NOTE_TYPE_ID AND
  xde.CASE_ID = p_case_id AND
  xde.DIARY_NOTE_TEXT IS NOT NULL AND
  (xde.OBS_IND <> 'Y' OR xde.OBS_IND is NULL);
  
  BEGIN
  
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
                xlluv.LIST_START_DATE,
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
          ORDER BY 3 DESC) subquery;
          
  BEGIN
 
  FOR history_list_row IN history_list_c
   LOOP
    IF history_list_row.DATE_OF_REMOVAL IS NULL AND history_list_row.LIST_TYPE <> 'Fixture' THEN
      history_list:=history_list || history_list_row.LIST_TYPE || ' ' || LIST_SUFFIX || ' ' || history_list_row.LIST_START_DATE || ' ' || history_list_row.HEARING_DESC || '|';
    ELSIF history_list_row.DATE_OF_REMOVAL IS NULL AND history_list_row.LIST_TYPE = 'Fixture' || '|' THEN
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
		  MAX(j.surname || ', ' || substr(j.first_name,1,1)) as JUDGE_NAME,
		  j.judge_type AS JUDGE_TYPE,
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
		  END) AS Total  
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
		WHERE
		  c.court_id = p_court_id
		  AND judge_usage.court_chambers_ind in ('CRT','CHA')
		  AND trunc(last_day(judge_usage.sitting_date),'DD') = trunc(last_day(p_sitting_date),'DD')
		GROUP BY
		  COURT_NAME, j.ref_judge_id,j.judge_type, trunc(last_day(judge_usage.sitting_date),'DD')
		ORDER BY
		  j.judge_type,
		  j.ref_judge_id;
    
    
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
  * Function getCaseType  (CTX-2160)
  *
  * Returns the text for the case type code of a case, looked up from xhb_ref_system_code. 
  * Takes a case_id as input parameter.
  *
  **************************************************************************************/
Function getCaseType(p_case_id  xhb_case.case_id%type) RETURN VARCHAR2 IS 

v_retval              varchar2(50);

BEGIN
    -- First we get the information about the case.
    select rsc1.de_code 
    into   v_retval
    from   xhb_ref_system_code rsc1, xhb_case cas
    where  rsc1.code_type = 'CASE_TYPE' 
    and    cas.case_type = rsc1.code
    and    cas.case_id = p_case_id
    and    rsc1.version = (select max(rsc2.version) from xhb_ref_system_code rsc2 where rsc1.code = rsc2.code and nvl(rsc2.obs_ind, 'N') != 'Y' );
        
    Return v_retval;
         
END getCaseType;

  /**********************************************************************************
  *
  * Function getCaseSubHdg  (CTX-2160)
  *
  * Returns the sub-heading of a case, which is different according to case type 
  * Takes a case_id as input parameter.
  *
  **************************************************************************************/
Function getCaseSubHdg(p_case_id  xhb_case.case_id%type) RETURN VARCHAR2 IS 

v_case_type           xhb_case.case_type%type;         -- varchar2(1)
v_case_class          varchar2(5);    -- xhb_case.case_class%type;        -- number(5,0)
v_case_receipt_type   xhb_case.receipt_type%type;      -- varchar2(2)
v_case_sub_type       varchar2(2);    -- xhb_case.case_sub_type%type;     -- varchar2(1)
v_retval              varchar2(50);

BEGIN
    -- First we get the information about the case.
    SELECT nvl(case_type, 'N') as case_type, nvl(to_char(case_class), 'NL') as case_class, upper(nvl(receipt_type, 'NL')) as receipt_type, nvl(case_sub_type , 'NL') as case_sub_type
    INTO   v_case_type, v_case_class, v_case_receipt_type, v_case_sub_type 
    FROM   xhb_case 
    WHERE  case_id = p_case_id;
     
    CASE Upper(v_case_type) -- Normally is upper case but don't want to be tripped up by a rogue lower case entry 
        WHEN 'A' THEN  -- Appeals
            v_retval := Replace(v_case_sub_type, 'B', 'C');  -- Treat instances of subtype 'C' as 'B'
            CASE Upper(v_case_sub_type)
                WHEN 'B' THEN
                    v_retval := 'Appeal against Conviction and Sentence';
                    
                WHEN 'C' THEN  -- We map 'C' to 'B' (both)
                    v_retval := 'Appeal against Conviction and Sentence';
                    
                
                WHEN 'O' THEN
                    v_retval := 'Other Appeal';
                    
                ELSE
                    v_retval := 'NULL';
        END CASE;
         
        WHEN 'S' THEN  -- for Sentencing
            -- Lookup the case receipt type
            IF v_case_receipt_type = 'NL' THEN
                v_retval := 'NULL';   
            ELSE
                SELECT rsc1.de_code 
                into   v_retval
                from   xhb_ref_system_code rsc1 
                where  rsc1.code_type = 'CASE_RECEIPT_TYPE' 
                and    rsc1.code = v_case_receipt_type 
                and    rsc1.version = (select max(rsc2.version) from xhb_ref_system_code rsc2 where rsc1.code = rsc2.code and nvl(rsc2.obs_ind, 'N') != 'Y');
                
                IF v_retval is null then 
                    v_retval := 'NULL';
                END IF;
            END IF;
 
        WHEN 'T'  THEN  -- for Trial
            IF v_case_class = 'NL' THEN
                v_retval := 'NULL';            
            ELSE
                v_retval := 'Class ' || v_case_class;
            END IF;
        
        ELSE 
            -- This should not happen
            v_retval := 'NULL';            
    END CASE;
     
    Return v_retval;
         
END getCaseSubHdg;

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
    v_step              VARCHAR2(10);
    v_rpt_name          VARCHAR2(30)  := 'Cases Received and Disposed of';
    v_rpt_code          VARCHAR2(6)   := 'RREC';
    v_user              VARCHAR2(30);
    v_day_of_week       INTEGER;
    i                   INTEGER := 0;     --) miscellaneous 
    j                   INTEGER := 0;     --) loop 
    k                   INTEGER := 0;     --) variables
    v_start_date        DATE;
    v_end_date          DATE;
    v_os_at_strt_cnt    INTEGER;     -- 1
    v_recd_frm_psd_cnt  INTEGER;     -- 2
    v_xfr_fm_cts_cnt    INTEGER;     -- 3
    v_bnch_wrnt_cnt     INTEGER;     -- 4
    v_xfr_to_cts_cnt    INTEGER;     -- 5
    v_net_receipts_cnt  INTEGER;     -- 6
    v_delt_wth_cnt      INTEGER;     -- 7
    v_os_at_end_cnt     INTEGER;     -- 8
    v_full_result_cnt   INTEGER;     -- Total
    v_section           INTEGER;
    v_last_section      INTEGER;
    v_case_type         VARCHAR2(50);
    v_case_sub_type     VARCHAR2(50);

    -- Declare the arrays
    v_outstanding_at_start     rrec_case_array  ;  -- section 1
    v_received_from_psd        rrec_case_array  ;  -- section 2
    v_xferred_frm_other_cts    rrec_case_array  ;  -- section 3
    v_bench_warrants_executed  rrec_case_array  ;  -- section 4
    v_xferred_to_other_cts     rrec_case_array  ;  -- section 5
    v_net_receipts             rrec_case_array  ;  -- section 6  = (2) +(3) + (4) - (5)
    v_cases_dealt_with         rrec_case_array  ;  -- section 7
    v_outstanding_at_end       rrec_case_array  ;  -- section 8
    v_full_result_set          rrec_case_array  ;  -- all sections combined

    v_case_row                 rrec_case_info;    
    v_total                    rrec_totals_array;

   
    cursor c_outstanding_at_start is 
        select 1 as section, getCaseType(cas1.case_id) as case_type, cas1.case_number as case_number, getCaseSubHdg(cas1.case_id) as case_subhdg, cas1.case_id -- Outstanding cases at start of period (1)
        from  xhb_case cas1 
        where cas1.court_id = p_court_id 
        and   nvl(cas1.case_type, '-') in ('A', 'S', 'T')
        and   cas1.received_date < v_start_date -- parameter here 
        and   exists(select doc.defendant_on_case_id  -- For a case to be open at the start of the period, it has to 
                     from  xhb_defendant_on_case doc  -- have at least one defendant unexported at the start date.
                     where nvl(doc.date_exported, v_end_date) > v_start_date   -- We map null values to a date after the start 
                     and   doc.case_id = cas1.case_id                          -- of the period rather than deal with the nulls
                     and   nvl(doc.obs_ind, 'N') = 'N')
        order by cas1.case_type, case_subhdg, case_number;          
                    
    cursor c_received_from_psd is 
        select 2 as section, getCaseType(cas2.case_id) as case_type, cas2.case_number as case_number, getCaseSubHdg(cas2.case_id) as case_subhdg, cas2.case_id   -- Cases received from PSD (2)
        from   xhb_case cas2
        where  cas2.court_id = p_court_id
        and    nvl(cas2.case_type, '-') in ('A', 'S', 'T')
        and    cas2.creation_date > v_start_date -- parameter here p_start_date
        and    cas2.creation_date < v_end_date   -- parameter here p_end_date 
        and    cas2.date_trans_from is null      -- exclude transferred cases - we'll do them next.
        order by  cas2.case_type, case_subhdg, case_number;    
                    
    cursor c_xferred_frm_other_cts is 
        select 3 as section, getCaseType(cas3.case_id) as case_type, cas3.case_number as case_number, getCaseSubHdg(cas3.case_id) as case_subhdg, cas3.case_id   -- Cases received from other courts (3)
        from   xhb_case cas3 
        where  cas3.court_id = p_court_id
        and    nvl(cas3.case_type, '-') in ('A', 'S', 'T')
        and    nvl(date_trans_from, '01-Jan-1900')  < v_end_date    -- parameter here 
        and    nvl(date_trans_from, '01-Jan-1900')  > v_start_date  -- parameter here 
        order by  cas3.case_type, case_subhdg, case_number;   
   
    cursor c_bench_warrants_executed is 
        -- The issuing of a bench warrant (for no-shows at trial) puts a case on hold but it is treated as temporarily closed.
        -- The execution of the warrant (arrest of the AWOL defendant) re-opens the case
        select 4 as section, getCaseType(cas4.case_id) as case_type, cas4.case_number as case_number, getCaseSubHdg(cas4.case_id) as case_subhdg, cas4.case_id   -- Cases for which bench warrants have been executed (4)
        from   xhb_case cas4, xhb_defendant_on_case doc, xhb_bw_history bwh                      
        where  cas4.court_id = p_court_id
        and    nvl(cas4.case_type, '-') in ('A', 'S', 'T')
        and    nvl(doc.obs_ind, 'N') = 'N'
        and    doc.defendant_on_case_id = bwh.defendant_on_case_id
        and    bwh.bw_end_date  > v_start_date -- parameter here 
        and    bwh.bw_end_date  < v_end_date   -- parameter here 
        and    nvl(bwh.obs_ind, 'N') = 'N' 
        and    cas4.case_id = doc.case_id 
        order by  cas4.case_type, case_subhdg, case_number;     

    cursor c_xferred_to_other_cts is        
        select 5 as section, getCaseType(cas5.case_id) as case_type, cas5.case_number as case_number, getCaseSubHdg(cas5.case_id) as case_subhdg, cas5.case_id   -- Cases transferred to other courts (5)
        from xhb_case cas5 
        where  cas5.court_id = p_court_id 
        and    nvl(cas5.case_type, '-') in ('A', 'S', 'T')
        and    nvl(cas5.date_trans_to, '01-Jan-1900')  < v_end_date   -- parameter here 
        and    nvl(cas5.date_trans_to, '01-Jan-1900')  > v_start_date -- parameter here
        order by  cas5.case_type, case_subhdg, case_number;        
 
    cursor c_cases_dealt_with is       
        select 7 as section, getCaseType(cas7.case_id) as case_type, cas7.case_number as case_number, getCaseSubHdg(cas7.case_id) as case_subhdg, cas7.case_id   -- Cases dealt with (closed). All defendants need a Date_Exported and at least one must be in the reporting period.
        from   xhb_case cas7 
        where  cas7.court_id = p_court_id
        and    nvl(cas7.case_type, '-') in ('A', 'S', 'T')
        and    exists(select doc.defendant_on_case_id  -- Atleast one date_exported must lie within the reporting period
                      from xhb_defendant_on_case doc 
                      where nvl(doc.date_exported, v_start_date) > v_start_date -- parameter here 
                      and   nvl(doc.date_exported, v_start_date) < v_end_date   -- parameter here 
                      and   nvl(doc.obs_ind, 'N') = 'N'                  
                      and   doc.case_id = cas7.case_id) -- join to outer query  
        order by  cas7.case_type, case_subhdg, case_number;     

    cursor c_outstanding_at_end is 
        select 8 as section, getCaseType(cas8.case_id) as case_type, cas8.case_number as case_number, getCaseSubHdg(cas8.case_id) as case_subhdg, cas8.case_id -- Outstanding cases at end of period
        from   xhb_case cas8
        where  cas8.court_id = p_court_id      -- parameter here 
        and    nvl(cas8.case_type, '-') in ('A', 'S', 'T')
        and    cas8.received_date < v_end_date -- parameter here 
        and    exists(select doc.defendant_on_case_id  -- For a case to be open at the end of the period, it has to 
                      from  xhb_defendant_on_case doc  -- have at least one defendant unexported at the end date.
                      where nvl(doc.date_exported, v_end_date) > v_end_date   -- We map null values to a date after the end 
                      and   doc.case_id = cas8.case_id                            -- of the period rather than deal with the nulls
                      and   nvl(doc.obs_ind, 'N') = 'N')                        
        order by  cas8.case_type, case_subhdg, case_number;                                 
    
  BEGIN  
    v_step := '1';
    SELECT SYS_CONTEXT('USERENV', 'CURRENT_USER') 
    INTO   v_user
    FROM   dual;                
    v_step := '2.1';    
    
    -- Initialise the arrays
    v_outstanding_at_start     := rrec_case_array();
    v_step := '2.2';    
    v_received_from_psd        := rrec_case_array();
    v_step := '2.3';    
    v_xferred_frm_other_cts    := rrec_case_array();
    v_step := '2.4';    
    v_bench_warrants_executed  := rrec_case_array();
    v_step := '2.5';    
    v_xferred_to_other_cts     := rrec_case_array();
    v_step := '2.6';    
    v_net_receipts             := rrec_case_array();
    v_step := '2.7';    
    v_cases_dealt_with         := rrec_case_array();
    v_step := '2.8';    
    v_outstanding_at_end       := rrec_case_array();
    v_step := '2.9';    
    v_full_result_set          := rrec_case_array();
    v_step := '2.10';    
 
    v_case_row                 := rrec_case_info(0, 'T', 0, 'TL', 1653400);    -- These are junk values just to initialise the variable.
    
    v_step := '3';  
    
    
    IF p_end_date is null then 
        -- Monthly mode.    
        v_start_date := Last_Day(ADD_MONTHS(sysdate, -2)) + 1;
        v_end_date   := Last_Day(ADD_MONTHS(sysdate, -1));
        
    ELSE
        v_step := '4';   
        
        -- Weekly mode
        v_day_of_week  := TO_NUMBER(TO_CHAR(p_end_date, 'd'));    -- Find out the day of the week for the date they supplied.
        v_end_date     := p_end_date  + (6 - v_day_of_week);      -- This makes sure the date we use is a Saturday
        v_start_date   := v_end_date - 7;
        
        IF v_end_date > sysdate THEN
            -- This is an error condition
            v_step := '4a';    
            
            RAISE VALUE_ERROR;
      
        END IF;
    END IF;
    
    DBMS_OUTPUT.PUT_LINE('get_RREC_Detail: step ' || v_step || '  start date = ' || v_start_date || ', end date = ' || v_end_date); 
    
    -- Populate the array of cases outstanding at start (1)
    v_step := '5';    
    open c_outstanding_at_start;
    
    v_step := '6';    
    i := 0;
    
    LOOP
        v_step := '6.' || to_char(i + 1) ;   
        
        FETCH c_outstanding_at_start INTO v_case_row.section_num, v_case_row.case_type,  v_case_row.case_number, v_case_row.case_subhdg, v_case_row.case_id;
        EXIT WHEN c_outstanding_at_start%NOTFOUND;
        
        i := i + 1;   -- We don't increment the index until we know a row is fetched.
        v_outstanding_at_start.extend;
        v_outstanding_at_start(i) := v_case_row;
        v_full_result_set.extend;
        v_full_result_set(i) := v_case_row;
    END LOOP;
        
    k := i;
    v_os_at_strt_cnt := c_outstanding_at_start%ROWCOUNT;  -- This should be the same as i
    
    DBMS_OUTPUT.PUT_LINE('get_RREC_Detail: step ' || v_step || ' section 1 rowcount = ' || v_os_at_strt_cnt); 
    
    close c_outstanding_at_start;
    
    -- Populate the array of cases received from PSD (2)
    v_step := '7';    
    open c_received_from_psd;
    
    v_step := '8';    
    i := 0;
    
    LOOP
        v_step := '8.' || to_char(i + 1) ;   

        FETCH c_received_from_psd INTO v_case_row.section_num, v_case_row.case_type,  v_case_row.case_number, v_case_row.case_subhdg, v_case_row.case_id;
        EXIT WHEN c_received_from_psd%NOTFOUND;
            
        i := i + 1;   -- We don't increment the index until we know a row is fetched.
        v_received_from_psd.extend;
        v_received_from_psd(i) := v_case_row;
            
        -- Add the Section 2 rows to the full result set
        v_full_result_set.extend;
        v_full_result_set(k + i) := v_case_row;
        -- Start Populating section 6 at the same time
        
        v_case_row.section_num := 6;
        v_net_receipts.extend;
        v_net_receipts(i) := v_case_row; 
    END LOOP;
        
    v_step := '9' ;   
    j := i;
    k := k + i;
    v_recd_frm_psd_cnt := c_received_from_psd%ROWCOUNT;
     
    DBMS_OUTPUT.PUT_LINE('get_RREC_Detail: step ' || v_step || ' section 2 rowcount = ' || v_recd_frm_psd_cnt); 
    
    close c_received_from_psd;
    
    -- Populate the array of Cases received from other courts (3)
    v_step := '10';    
    open c_xferred_frm_other_cts;
    
    v_step := '11';    
    i := 0;
    
    LOOP
        v_step := '11.' || to_char(i + 1) ; 
            
        FETCH c_xferred_frm_other_cts INTO v_case_row.section_num, v_case_row.case_type,  v_case_row.case_number, v_case_row.case_subhdg, v_case_row.case_id;
        EXIT WHEN c_xferred_frm_other_cts%NOTFOUND;
            
        i := i + 1;
        j := j + 1;
        v_xferred_frm_other_cts.extend;
        v_xferred_frm_other_cts(i) :=  v_case_row;

        v_full_result_set.extend;
        v_full_result_set(k + i) := v_case_row;
        
        v_net_receipts.extend;
        v_case_row.section_num := 6;
        v_net_receipts(j) := v_case_row;
    END LOOP;
        
    k := k + i;
    
    v_xfr_fm_cts_cnt := c_xferred_frm_other_cts%ROWCOUNT;
     
    DBMS_OUTPUT.PUT_LINE('get_RREC_Detail: step ' || v_step || ' section 3 rowcount = ' || v_xfr_fm_cts_cnt); 

    close c_xferred_frm_other_cts;
    
    -- Populate the array of Cases for which bench warrants have been executed for all defendants(4)
    v_step := '12';    
    open c_bench_warrants_executed;
    
    v_step := '13';    
    i := 0;
    
    LOOP
        v_step := '13.' || to_char(i + 1) ;   
            
        FETCH c_bench_warrants_executed INTO v_case_row.section_num, v_case_row.case_type,  v_case_row.case_number, v_case_row.case_subhdg, v_case_row.case_id;
        EXIT WHEN c_bench_warrants_executed%NOTFOUND;

        i := i + 1;
        j := j + 1;
        v_bench_warrants_executed.extend;
        v_bench_warrants_executed(i) := v_case_row;
        
        v_full_result_set.extend;
        v_full_result_set(k + i):= v_case_row;

        v_net_receipts.extend;
        v_case_row.section_num := 6;
        v_net_receipts(j) := v_case_row;
    END LOOP;
        
    k := k + i;
    
    v_bnch_wrnt_cnt := c_bench_warrants_executed%ROWCOUNT;
    
    DBMS_OUTPUT.PUT_LINE('get_RREC_Detail: step ' || v_step || ' section 4 rowcount = ' || v_bnch_wrnt_cnt); 

    close c_bench_warrants_executed;
    
    -- Populate the array of Cases transferred to other courts (5)
    v_step := '14';    
    open c_xferred_to_other_cts;
    
    v_step := '15';    
    i := 0;
    
    LOOP
        v_step := '15.' || to_char(i + 1) ;   
            
        FETCH c_xferred_to_other_cts INTO v_case_row.section_num, v_case_row.case_type,  v_case_row.case_number, v_case_row.case_subhdg, v_case_row.case_id;
        EXIT WHEN c_xferred_to_other_cts%NOTFOUND;
            
        i := i + 1;
        v_xferred_to_other_cts.extend;
        v_xferred_to_other_cts(i) := v_case_row;
        
        -- Now we need to search v_net_receipts() for this case and remove it if found
        j := v_net_receipts.FIRST;
            
        WHILE j IN (v_net_receipts.FIRST, v_net_receipts.LAST)
        LOOP
            IF v_net_receipts(j).case_id = v_case_row.case_id THEN
                -- If this case has been transferred, we remove it from the list of net receipts
                v_net_receipts.DELETE(j);  -- Doing this results in gaps in the subscripts hence use of first, last, next
            END IF;
            
            j := v_net_receipts.NEXT(j);
        END LOOP;
            
        v_full_result_set.extend;
        v_full_result_set(k + i) := v_case_row;
    END LOOP;
   
    -- The transferred cases have now been subtracted from the received cases to give net receipts. Set the count.
    v_step := '16' ;  
    k := k + i;
    v_net_receipts_cnt := v_net_receipts.count(); 
    
    v_xfr_to_cts_cnt := c_xferred_to_other_cts%ROWCOUNT;
    close c_xferred_to_other_cts;
    
    DBMS_OUTPUT.PUT_LINE('get_RREC_Detail: step ' || v_step || ' section 5 rowcount = ' || v_xfr_to_cts_cnt || ', section 6 rowcount = ' || v_net_receipts_cnt); 

    -- Now we add the net receipts data to the overall result set.
    WHILE j IN (v_net_receipts.FIRST, v_net_receipts.LAST)
    LOOP
        v_step := '17.' || to_char(j);    
        k := k + 1;
        v_full_result_set.extend;
        v_full_result_set(k) := v_net_receipts(j);   -- Whilst v_net_receipts may have gaps in its subscripts, v_full_result_set won't.
        j := v_net_receipts.NEXT(j);
    END LOOP;

    
    -- Populate the array of Cases dealt with (closed) (7)
    v_step := '18';    
    open c_cases_dealt_with;
    
    v_step := '19';    
    i := 0;
    
    LOOP
        v_step := '19.' || to_char(i + 1);    -- Include the loop count in the step number
        FETCH c_cases_dealt_with INTO v_case_row.section_num, v_case_row.case_type, v_case_row.case_number, v_case_row.case_subhdg, v_case_row.case_id;
        EXIT WHEN c_cases_dealt_with%NOTFOUND;
            
        i := i + 1;
        v_cases_dealt_with.extend;
        v_cases_dealt_with(i) := v_case_row;
        
        v_full_result_set.extend;
        v_full_result_set(k + i) := v_case_row;
    END LOOP;
       
    k := k + i;
    
    v_step := '20';    
    v_delt_wth_cnt := c_cases_dealt_with%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE('get_RREC_Detail: step ' || v_step || ' section 6 rowcount = ' || v_delt_wth_cnt); 

    close c_cases_dealt_with;
    
    -- Populate the array of cases outstanding at end (8)
 
    v_step := '21';    
    open c_outstanding_at_end;
    
    v_step := '22';    
    i := 0;
    
    LOOP
        v_step := '23.' || to_char(i);    
        
        FETCH c_outstanding_at_end INTO v_case_row.section_num, v_case_row.case_type, v_case_row.case_number, v_case_row.case_subhdg, v_case_row.case_id;
        EXIT WHEN c_outstanding_at_end%NOTFOUND;
        
        i := i + 1;
        v_outstanding_at_end.extend;
        v_outstanding_at_end(i) := v_case_row;
        
        v_full_result_set.extend;           
        v_full_result_set(k + i) := v_case_row;
    END LOOP;
    
    v_step := '24';    
    v_os_at_end_cnt := c_outstanding_at_end%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE('get_RREC_Detail: step ' || v_step || ' section 8 rowcount = ' || v_os_at_end_cnt ); 
    close c_outstanding_at_end;
    
    -- Put the report detail in the output variable.
    v_step := '25';    
    OPEN   p_resultset FOR  
        SELECT rs.section_num, rs.case_type, rs.case_number, rs.case_subhdg, rs.case_id  
        FROM table(cast(v_full_result_set as rrec_case_array)) rs;
    
    -- Record that this report has been run.
        v_step := '26 '; 
    UPDATE_REPORT_LOG(p_court_id, v_rpt_code, v_rpt_name);
    
    -- Exception handling 
    EXCEPTION
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in XHB_REPORT_PKG.GET_RREC_DETAIL at step ' || v_step || '  Err code ' || v_err_code || ' : ' ||SQLERRM;
            
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
        v_start_date := Last_Day(ADD_MONTHS(sysdate, -2)) + 1;
        v_end_date   := Last_Day(ADD_MONTHS(sysdate, -1));
        
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
        select XHB_REPORT_PKG.getCasetype(cas.case_id) as case_type, XHB_REPORT_PKG.getCaseSubHdg(cas.case_id) as case_subhdg, 
--        sum(1) as section0,   -- in for dev only
        sum(case when (nvl(cas.received_date, v_start_date) < v_start_date)
                 and exists(select doc.defendant_on_case_id  -- For a case to be open at the start of the period, it has to 
                            from  xhb_defendant_on_case doc  -- have at least one defendant unexported at the start date.
                            where nvl(doc.date_exported, v_end_date) > v_start_date   -- We map null values to a date after the start 
                            and   doc.case_id = cas.case_id                          -- of the period rather than deal with the nulls
                            and   nvl(doc.obs_ind, 'N') = 'N') Then 1 Else 0 END) as section1,   -- Cases Outstanding At Start
        sum(case when  (cas.creation_date > v_start_date -- parameter here p_start_date
                 and    cas.creation_date < v_end_date   -- parameter here p_end_date 
                 and    cas.date_trans_from is null ) then 1 else 0 end) as section2   , 
        sum(case when  (nvl(cas.date_trans_from, '01-Jan-1900')  < v_end_date    -- parameter here 
                  and   nvl(cas.date_trans_from, '01-Jan-1900')  > v_start_date) then 1 else 0 end) as section3,  
        sum(case when exists(select 1 
                             from xhb_defendant_on_case doc, xhb_bw_history bwh 
                             where  nvl(doc.obs_ind, 'N') = 'N' 
                             and    doc.defendant_on_case_id = bwh.defendant_on_case_id
                             and    bwh.bw_end_date  > v_start_date -- parameter here 
                             and    bwh.bw_end_date  < v_end_date   -- parameter here 
                             and    nvl(bwh.obs_ind, 'N') = 'N' 
                             and    cas.case_id = doc.case_id) then 1 else 0 end) as section4,  -- Bench Warrant Cases 
        sum(case when (nvl(cas.date_trans_to, '01-Jan-1900')  < v_end_date   -- parameter here 
                       and    nvl(cas.date_trans_to, '01-Jan-1900')  > v_start_date) then 1 else 0 end) as section5,  -- Cases Transfered In
        -- Section 6 is calculated from (6) = (2) + (3) + (4) - (5)                        
        sum(case when exists(select doc.defendant_on_case_id  -- Atleast one date_exported must lie within the reporting period
                             from  xhb_defendant_on_case doc 
                             where nvl(doc.date_exported, v_start_date) > v_start_date -- parameter here 
                             and   nvl(doc.date_exported, v_start_date) < v_end_date   -- parameter here 
                             and   nvl(doc.obs_ind, 'N') = 'N'   
                             and   doc.case_id = cas.case_id) then 1 else 0 end) as section7,   -- Cases Dealt With  
        sum(case when exists(select doc.defendant_on_case_id  -- For a case to be open at the end of the period, it has to 
                             from  xhb_defendant_on_case doc  -- have at least one defendant unexported at the end date.
                             and   nvl(doc.obs_ind, 'N') = 'N'                      -- rather than deal with the nulls
                             and   doc.case_id = cas.case_id    
                             and   cas.received_date < v_end_date) then 1 else 0 end) as section8     -- exclude cases which arrived after the end date
        from  xhb_case cas 
        where cas.court_id = p_court_id 
        and   nvl(cas.case_type, '-') in ('A', 'S', 'T')
        and   nvl(cas.received_date, v_start_date) < v_end_date -- parameter here 
        group by XHB_REPORT_PKG.getCasetype(cas.case_id), XHB_REPORT_PKG.getCaseSubHdg(cas.case_id)  
        UNION  -- This second select is essentially the same as the first but with subheading replaced by the string literal 'Total'
               -- It calculates the totals for each case type and they appear afer the values broken down by subheading.
        select XHB_REPORT_PKG.getCasetype(cas.case_id) as case_type, 'Total' as case_subhdg,
--        sum(1) as section0,   -- in for dev only
        sum(case when (nvl(cas.received_date, v_start_date) < v_start_date)
                 and exists(select doc.defendant_on_case_id  -- For a case to be open at the start of the period, it has to 
                            from  xhb_defendant_on_case doc  -- have at least one defendant unexported at the start date.
                            where nvl(doc.date_exported, v_end_date) > v_start_date   -- We map null values to a date after the start 
                            and   doc.case_id = cas.case_id                          -- of the period rather than deal with the nulls
                            and   nvl(doc.obs_ind, 'N') = 'N') Then 1 Else 0 END) as section1,   -- Cases Outstanding At Start
        sum(case when  (cas.creation_date > v_start_date -- parameter here p_start_date
                 and    cas.creation_date < v_end_date   -- parameter here p_end_date 
                 and    cas.date_trans_from is null ) then 1 else 0 end) as section2   , 
        sum(case when  (nvl(cas.date_trans_from, '01-Jan-1900')  < v_end_date    -- parameter here 
                  and   nvl(cas.date_trans_from, '01-Jan-1900')  > v_start_date) then 1 else 0 end) as section3,  
        sum(case when exists(select 1 
                             from xhb_defendant_on_case doc, xhb_bw_history bwh 
                             where  nvl(doc.obs_ind, 'N') = 'N' 
                             and    doc.defendant_on_case_id = bwh.defendant_on_case_id
                             and    bwh.bw_end_date  > v_start_date -- parameter here 
                             and    bwh.bw_end_date  < v_end_date   -- parameter here 
                             and    nvl(bwh.obs_ind, 'N') = 'N' 
                             and    cas.case_id = doc.case_id) then 1 else 0 end) as section4,  -- Bench Warrant Cases 
        sum(case when (nvl(cas.date_trans_to, '01-Jan-1900')  < v_end_date   -- parameter here 
                       and    nvl(cas.date_trans_to, '01-Jan-1900')  > v_start_date) then 1 else 0 end) as section5,  -- Cases Transfered In
        -- Section 6 is calculated from (6) = (2) + (3) + (4) - (5)                        
        sum(case when exists(select doc.defendant_on_case_id  -- Atleast one date_exported must lie within the reporting period
                             from  xhb_defendant_on_case doc 
                             where nvl(doc.date_exported, v_start_date) > v_start_date -- parameter here 
                             and   nvl(doc.date_exported, v_start_date) < v_end_date   -- parameter here 
                             and   nvl(doc.obs_ind, 'N') = 'N'   
                             and   doc.case_id = cas.case_id) then 1 else 0 end) as section7,   -- Cases Dealt With  
        sum(case when exists(select doc.defendant_on_case_id  -- For a case to be open at the end of the period, it has to 
                             from  xhb_defendant_on_case doc  -- have at least one defendant unexported at the end date.
                             and   nvl(doc.obs_ind, 'N') = 'N'                      -- rather than deal with the nulls
                             and   doc.case_id = cas.case_id    
                             and   cas.received_date < v_end_date) then 1 else 0 end) as section8     -- exclude cases which arrived after the end date
        from  xhb_case cas 
        where cas.court_id = p_court_id 
        and   nvl(cas.case_type, '-') in ('A', 'S', 'T')
        and   nvl(cas.received_date, v_start_date) < v_end_date -- parameter here 
        group by XHB_REPORT_PKG.getCasetype(cas.case_id), 'Total'          
        order by case_type, case_subhdg;     
 
    
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

END XHB_REPORT_PKG;
/
show errors
