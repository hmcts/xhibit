create or replace PACKAGE BODY "XHB_REPORT_PKG" AS
  /**
  * CGI CREST to XHIBIT Program
  *
  * MODULE      : XHB_REPORT_PKG
  *
  * DESCRIPTION : This package contains stored procedures for CTX reports
  *               It comprises: procedure get_defendants_put_back_report
  *                             procedure get_docar_report
  *                             procedure get_nfix_report
  *                             procedure get_list_officers_diary_report
  *								procedure get_cfix_report
  *                             function  get_charge
  *                             function  get_solicitor_id
  *                             function  get_most_recent_hearing
  *                             function  get_address
  *                             function  get_appellant_address
  *							    function  get_bc_status_ind
  *                             PROCEDURE getAppealHearingNotifnRpt
  *
  *
  **************************************************************************************/

  
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
	,     xcdf.LISTING_DATE
	from XHB_CASE xc
	,    XHB_DEFENDANT_ON_CASE xdoc
	,    XHB_DEFENDANT xd
	,    XHB_HEARING xh
	,    XHB_DEF_HEARING_RECORD xdhr
	,    XHB_REF_SYSTEM_CODE xrsc
	,    XHB_SCHEDULED_HEARING xsh
	,    XHB_SCHED_HEARING_ATTENDEE xsha
	,    XHB_REF_JUDGE xrj
	,    XHB_CASE_DIARY_FIXTURE xcdf
	,    XHB_CASE_LISTING_ENTRY xcle
	where xc.CASE_ID = xdoc.CASE_ID
	and   xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
	and   NVL(xdoc.OBS_IND, '-') <> 'Y'
	and   xc.CASE_ID = xh.CASE_ID
	and   xh.HEARING_ID = xdhr.HEARING_ID (+)
	and   xdhr.IS_ADJOURNED = 'Y'
	and   xdhr.REF_ADJOURNMENT_ID = xrsc.REF_SYSTEM_CODE_ID (+) --outer join on for put back data
	and   NVL(xrsc.OBS_IND, '-') <> 'Y'
	and   xrsc.CODE_TYPE = 'PB_TYPE'
	and   xrsc.CODE_TITLE = 'ADJOURNMENT TYPE'
	and   xrsc.code IN (SELECT regexp_substr(p_refSystemCode, '[^,]+', 1, level) FROM DUAL
                      CONNECT BY regexp_substr(p_refSystemCode, '[^,]+', 1, level) IS NOT NULL)
	and   xsh.HEARING_ID = xh.HEARING_ID
	and   xsh.SCHEDULED_HEARING_ID = xsha.SCHEDULED_HEARING_ID (+)
	and   xsha.ATTENDEE_TYPE in ('J','JP') --should this be Judge and JP (Justice of Peace)
	and   xsha.REF_JUDGE_ID = xrj.REF_JUDGE_ID (+)
	and   NVL(xrj.OBS_IND, '-') <> 'Y'
	and   (xdoc.RESULTS_VERIFIED = 'R' --R means that there are no subsequent hearings
	and       xdhr.HEARING_END_DATE = (SELECT MAX(xdhr2.HEARING_END_DATE)
                                             FROM XHB_DEF_HEARING_RECORD xdhr2
                                              WHERE xdhr2.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID )   
	 or     ( NVL(xdoc.RESULTS_VERIFIED,'x') <> 'R' --if there are subsequet hearings, make sure there are no disposals
	 and      not exists (select 'x' --can either be joined to defendant on case or defendant on offence
						  from XHB_DISPOSAL2 xdisp
						  where xdisp.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
						  )
	 and      not exists (select 'x'--can either be joined to defendant on case or defendant on offence
						  from xhb_disposal2 xdisp
						  where xdisp.DEFENDANT_ON_OFFENCE_ID = xdoc.DEFENDANT_ON_CASE_ID
						  )
    and       xdhr.HEARING_END_DATE = (SELECT MAX(xdhr2.HEARING_END_DATE)
                                             FROM XHB_DEF_HEARING_RECORD xdhr2
                                             ,    XHB_REF_SYSTEM_CODE xrsc2
                                              WHERE xdhr2.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                              AND   xdhr2.IS_ADJOURNED = 'Y' 
                                              AND   xdhr.REF_ADJOURNMENT_ID = xrsc2.REF_SYSTEM_CODE_ID (+) --outer join on for put back data
                                              AND   NVL(xrsc2.OBS_IND, '-') <> 'Y'
                                              AND   xrsc2.CODE_TYPE = 'PB_TYPE'
                                              AND   xrsc2.CODE_TITLE = 'ADJOURNMENT TYPE'
                                             and   xrsc.code IN (SELECT regexp_substr(p_refSystemCode, '[^,]+', 1, level) FROM DUAL
                                                              CONNECT BY regexp_substr(p_refSystemCode, '[^,]+', 1, level) IS NOT NULL)
                                              )
		 ))
    and xsh.START_TIME = (SELECT MAX(xsh2.START_TIME)
                                FROM XHB_SCHEDULED_HEARING xsh2
                                WHERE xh.HEARING_ID = xsh2.HEARING_ID)
  	and xc.COURT_ID = p_court_id   
	and xc.CASE_ID = xcle.CASE_ID (+)
	and xcle.CASE_LISTING_ENTRY_ID = xcdf.CASE_LISTING_ENTRY_ID (+)
	ORDER BY xh.HEARING_END_DATE
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
  *               mag_conviction_date, list_start_date, time_listed, rpt_run_date 
  *
  **************************************************************************************/

PROCEDURE getAppealHearingNotifnRpt(p_resultset OUT SYS_REFCURSOR,
									p_court_id  IN  XHB_COURT.COURT_ID%TYPE) AS 
                                           
-- Local variables here
v_rpt_name      VARCHAR2(30)  := 'getAppealHearingNotifnRpt';
v_rpt_code      VARCHAR2(6)   := 'NHA';
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
    -- This query considers cases which are listed in a future Firm list
    select  cas.case_id, cas.case_number, ct.court_code, ct.court_name, 
            -- Appellant columns
            doc.defendant_number, df.first_name || ' ' || df.middle_name || ' ' || df.surname as appellant_name, 
            rsf.solicitor_firm_name, getAppellantAddress(doc.defendant_on_case_id)  defendant_address, 
             -- Respondent columns
            rpa.prosecutor_name_1 || ' ' || rpa.prosecutor_name_2 || ' ' || rpa.prosecutor_name_3 as respondent_name, 
            getAddress(rpa.address_id) as respondent_address, 
            -- Court columns 
            getAddress(ct.address_id) as court_address,  
            Replace(r.de_code, 'APPEAL AGAINST ') as appeal_type, 'Clerk to Justice' as clerk_to_justice,
            cas.mag_conviction_date, lst.list_start_date, to_char(sol.time_listed,'HH24:MM') as time_listed, to_date(sysdate) as rpt_run_date, cdet.contact_value as ct_phone_no 
    from    xhb_case cas, xhb_defendant df, xhb_defendant_on_case doc, xhb_ref_solicitor_firm rsf,   
            xhb_case_prosecutor_agency cpa, xhb_ref_prosecutor_agency rpa, 
            -- tables unique to the 1st query
            xhb_case_on_list csol, xhb_list lst, xhb_ref_listing_data rld, 
            xhb_sitting_on_list sol,  -- added for CTX-2348
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
    and    csol.sitting_on_list_id = sol.sitting_on_list_id -- added for CTX-2348
    and    lst.list_start_date > sysdate 
    and    lst.list_type_id = rld.ref_listing_data_id 
    and    rld.ref_data_type = 'LIST_TYPE'
    and    rld.ref_data_value = 'Firm' 
    -- clauses unique to 1st query (end)
    and    ct.court_id = lst.court_id
    and    ct.court_id = p_court_id -- parameter here
    and    cas.case_type = 'A'     -- Appeal
    and    cas.case_sub_type = r.code(+)
    and    r.code_type like '%APPEAL_TYP%' 

    union 
    -- This query considers cases which have a future fixture
    select  cas.case_id, cas.case_number, ct.court_code, ct.court_name, 
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
            case nvl(TO_CHAR(cdf.listing_date,'HH24:MM'),0.0)   -- added for CTX-2348
              when '00.00' THEN TO_CHAR(ct.court_start_time,'HH24:MM')        -- added for CTX-2348
              else TO_CHAR(cdf.listing_date,'HH24:MM')                    -- added for CTX-2348
            end as time_listed,
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
    -- clauses unique to 2nd query (end)
    and    ct.court_id = p_court_id -- parameter here
    and    cas.case_type = 'A'     -- Appeal
    and    cas.case_sub_type = r.code(+)
    and    r.code_type like '%APPEAL_TYP%' 
    order by 1	;
    
    -- Now we have to log that we have run this report. Note that by setting a variable for the report code, this update statement is completely generic.
    v_step := '3';
    UPDATE  XHB_REPORT_LOG SET  date_last_run = sysdate
    WHERE   court_id = p_court_id 
    AND     crest_report_code = v_rpt_code;   
                         
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

PROCEDURE get_obw_report(p_results_out OUT SYS_REFCURSOR,
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
select cas4.case_id, cas4.case_type||cas4.case_number||doc.defendant_number as case_number, 
def.surname|| ' ' ||def.initials as defendant_name,
cas4.case_class,
bwh.bw_issue_date,
rsf.solicitor_firm_name,
doc.asn,
sysdate as today_date
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

END XHB_REPORT_PKG;
/
show errors
