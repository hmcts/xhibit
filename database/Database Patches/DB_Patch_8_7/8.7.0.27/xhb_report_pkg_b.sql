create or replace PACKAGE BODY "XHB_REPORT_PKG" AS
  
  /**
  * CGI CREST toXHIBIT Program
  *
  * MODULE      : XHB_REPORT_PKG
  *
  * DESCRIPTION : This package contains stored procedures for CTX reports
  *
  * VERSION HISTORY:
  *
  * Date          Author            Version    Nature of Change
  * ----------    -------           --------   ----------------------------------------
  * 09/05/2018    John Riley         1.0       CTX-1335 First revision
  *
  **************************************************************************************/
  

  PROCEDURE get_defendants_put_back_report(p_results_out OUT SYS_REFCURSOR  
                            ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                            ,p_refSystemCode IN VARCHAR) AS
  BEGIN
  OPEN p_results_out FOR
	 select xc.case_number
	,      upper(xd.SURNAME)||' '|| xd.FIRST_NAME ||' '|| xd.MIDDLE_NAME AS defendant_name
	,      xh.HEARING_START_DATE
	,      xdhr.HEARING_START_DATE AS HEARING_END_DATE--PUT_BACK_START_DATE
	,      xrj.first_name||' '||xrj.surname AS judge_name
  ,      xdoc.ASN
  ,     substr(xrsc.DE_CODE, instr(xrsc.DE_CODE, '-', 1,1)) AS reason
  ,     sysdate as list_date
	from xhb_case xc
	,    xhb_defendant_on_case xdoc
	,    xhb_defendant xd
	,    xhb_hearing xh
	,    xhb_def_hearing_record xdhr
	,    xhb_ref_system_code xrsc
	,    xhb_scheduled_hearing xsh
	,    xhb_sched_hearing_attendee xsha
	,    xhb_ref_judge xrj
	where xc.case_id = xdoc.case_id
	and   xdoc.defendant_id = xd.defendant_id
  and   NVL(xdoc.OBS_IND, '-') <> 'Y'
	and   xc.case_id = xh.case_id
	and   xh.hearing_id = xdhr.hearing_id (+)
	and   xdhr.ref_adjournment_id = xrsc.ref_system_code_id (+) --outer join on for put back data
  and   NVL(xrsc.OBS_IND, '-') <> 'Y'
	and   xrsc.code_type = 'PB_TYPE'
	and   xrsc.code_title = 'ADJOURNMENT TYPE'
	and   xrsc.code IN (SELECT regexp_substr(p_refSystemCode, '[^,]+', 1, level) FROM DUAL
                      CONNECT BY regexp_substr(p_refSystemCode, '[^,]+', 1, level) IS NOT NULL)
	and   xsh.hearing_id = xh.hearing_id
	and   xsh.scheduled_hearing_id = xsha.scheduled_hearing_id (+)
	and   xsha.attendee_type in ('J','JP') --should this be Judge and JP (Justice of Peace)
	and   xsha.ref_judge_id = xrj.ref_judge_id (+)
  and   NVL(xrj.OBS_IND, '-') <> 'Y'
	and   (xdoc.results_verified = 'R' --R means that there are no subsequent hearings
	 or      NVL(xdoc.results_verified,'x') <> 'R' --if there are subsequet hearings, make sure there are no disposals
	 and      not exists (select 'x' --can either be joined to defendant on case or defendant on offence
						  from xhb_disposal2 xdisp
						  where xdisp.defendant_on_case_id = xdoc.defendant_on_case_id
						  )
	 and      not exists (select 'x'--can either be joined to defendant on case or defendant on offence
						  from xhb_disposal2 xdisp
						  where xdisp.defendant_on_offence_id = xdoc.defendant_on_case_id
						  )
		 )
  and xc.court_id = p_court_id   
	ORDER BY xc.case_number
	,        xd.DEFENDANT_ID;   --is this different from defendant number as per the report document?

	END get_defendants_put_back_report;

	PROCEDURE get_adjss_report(p_results_out OUT SYS_REFCURSOR  
                            ,p_court_id    IN XHB_CASE.COURT_ID%TYPE) AS
  BEGIN
  OPEN p_results_out FOR
	 select xdoc.defendant_on_case_id, 
   --xc.CASE_TYPE || 
   xc.case_number as case_number
	,      upper(xd.SURNAME)||' '|| xd.FIRST_NAME ||' '|| xd.MIDDLE_NAME AS defendant_name
	,      xdhr.ADJOURNED_DATE AS hearing_start_date
	,      xdhr.HEARING_END_DATE --AS HEARING_DATE
	,      upper(xrj.SURNAME) ||  ' ' || xrj.FIRST_NAME ||' '|| xrj.MIDDLE_NAME AS judge_name
  ,      xdoc.ASN
  ,     substr(xrsc.DE_CODE, instr(xrsc.DE_CODE, '-', 1,1)) AS reason
  ,     xcdf.LISTING_DATE as list_date
	from xhb_case xc
	,    xhb_defendant_on_case xdoc
	,    xhb_defendant xd
	,    xhb_hearing xh
	,    xhb_def_hearing_record xdhr
	,    xhb_ref_system_code xrsc
	,    xhb_scheduled_hearing xsh
	,    xhb_sched_hearing_attendee xsha
	,    xhb_ref_judge xrj
  ,    XHB_CASE_DIARY_FIXTURE xcdf
  ,    XHB_CASE_LISTING_ENTRY xcle
	where xc.case_id = xdoc.case_id
	and   xdoc.defendant_id = xd.defendant_id
  and   NVL(xdoc.OBS_IND, '-') <> 'Y'
	and   xc.case_id = xh.case_id
	and   xh.hearing_id = xdhr.hearing_id (+)
  and   xdhr.IS_ADJOURNED = 'Y'
	and   xdhr.ref_adjournment_id = xrsc.ref_system_code_id (+) --outer join on for put back data
  and   NVL(xrsc.OBS_IND, '-') <> 'Y'
	and   xrsc.code_type = 'PB_TYPE'
	and   xrsc.code_title = 'ADJOURNMENT TYPE'
	and   (xrsc.code IN ('SC','SE','SM','SO','SS','S')
  or    xrsc.DE_CODE like 'PUT BACK FOR SENTENCE%')
	and   xsh.hearing_id = xh.hearing_id
	and   xsh.scheduled_hearing_id = xsha.scheduled_hearing_id (+)
	and   xsha.attendee_type in ('J','JP') --should this be Judge and JP (Justice of Peace)
	and   xsha.ref_judge_id = xrj.ref_judge_id (+)
  and   NVL(xrj.OBS_IND, '-') <> 'Y'
	and   (xdoc.results_verified = 'R' --R means that there are no subsequent hearings
  and       xdhr.HEARING_END_DATE = (SELECT MAX(xdhr2.HEARING_END_DATE)
                                             FROM xhb_def_hearing_record xdhr2
                                              WHERE xdhr2.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID )   
	 or     ( NVL(xdoc.results_verified,'x') <> 'R' --if there are subsequet hearings, make sure there are no disposals
	 and      not exists (select 'x' --can either be joined to defendant on case or defendant on offence
						  from xhb_disposal2 xdisp
						  where xdisp.defendant_on_case_id = xdoc.defendant_on_case_id
						  )
	 and      not exists (select 'x'--can either be joined to defendant on case or defendant on offence
						  from xhb_disposal2 xdisp
						  where xdisp.defendant_on_offence_id = xdoc.defendant_on_case_id
						  )
    and       xdhr.HEARING_END_DATE = (SELECT MAX(xdhr2.HEARING_END_DATE)
                                             FROM xhb_def_hearing_record xdhr2
                                             ,    XHB_REF_SYSTEM_CODE xrsc2
                                              WHERE xdhr2.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                              AND   xdhr2.IS_ADJOURNED = 'Y' 
                                              AND   xdhr.ref_adjournment_id = xrsc2.ref_system_code_id (+) --outer join on for put back data
                                              AND   NVL(xrsc2.OBS_IND, '-') <> 'Y'
                                              AND   xrsc2.code_type = 'PB_TYPE'
                                              AND   xrsc2.code_title = 'ADJOURNMENT TYPE'
                                              AND   (xrsc2.code IN ('SC','SE','SM','SO','SS','S')
                                              OR    xrsc2.DE_CODE like 'PUT BACK FOR SENTENCE%')
                                              )
		 ))
    and xsh.start_time = (SELECT MAX(xsh2.start_time)
                                FROM xhb_scheduled_hearing xsh2
                                WHERE xh.hearing_id = xsh2.hearing_id)
 -- and xc.court_id = 81   
  and xc.CASE_ID = xcle.CASE_ID (+)
  and xcle.CASE_LISTING_ENTRY_ID = xcdf.CASE_LISTING_ENTRY_ID (+)
	ORDER BY xh.HEARING_END_DATE
  ,        xc.case_number
	,        xd.DEFENDANT_ID;  

	END get_adjss_report;


  PROCEDURE get_docar_report(p_results_out OUT SYS_REFCURSOR,
                             p_papers_sent_date IN XHB_DEFENDANT_ON_CASE.FORM_NG_SENT_DATE%TYPE,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE) AS
  BEGIN
  OPEN p_results_out FOR
    SELECT c.CASE_TYPE, c.CASE_NUMBER, dof.DEFENDANT_NUMBER, d.FIRST_NAME, d.MIDDLE_NAME, d.SURNAME, dof.FORM_NG_SENT_DATE
    FROM XHB_DEFENDANT_ON_CASE dof, XHB_CASE c, XHB_DEFENDANT d
    WHERE dof.CASE_ID = c.CASE_ID
    AND dof.DEFENDANT_ID = d.DEFENDANT_ID
    AND dof.FORM_NG_SENT_DATE IS NOT NULL
    AND dof.FORM_NG_SENT_DATE < p_papers_sent_date
    AND dof.CACD_APPEAL_RESULT_DATE IS NULL
    AND c.COURT_ID = p_court_id;
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

 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORTS_LOG(REPORTS_LOG_ID, error_message, run_date)
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
 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORTS_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_LIST_OFFICERS_DIARY_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;                  
  END get_list_officers_diary_report; 
  
  
 /**********************************************************************************
  *
  * Procedure getAppealHearingNotifnRpt  (CTX-1335)
  *
  * Returns a list of Appeal hearings which have been listed for a specified court (usually, the one where the user is located).
  * It supplies the information needed to produce notification letters to the Appellant(s) and Respondent(s).
  *
  * Columns:      case_id, case_number, court_code, court_name, appellant_id, appellant_name, appellant_address, 
  *               respondent_name, respondent_address, court_address, appeal_type, clerk_to_justice, 
  *               mag_conviction_date, list_start_date, time_listed, rpt_run_date 
  *
  **************************************************************************************/  
PROCEDURE getAppealHearingNotifnRpt(p_resultset OUT SYS_REFCURSOR 
                                  , p_court_id IN XHB_COURT.COURT_ID%TYPE) AS 
                                           
-- Local variables here

BEGIN                                           
    open p_resultset for
    select  case.case_id, case.case_number, ct.court_code, ct.court_name, 
            -- Appellant columns
            doc.defendant_number, df.first_name || ' ' || df.middle_name || ' ' || df.surname as appellant_name, dfad.address_1 || CHR(10) || dfad.postcode as appellant_address, 
            -- Respondent columns
            rpa.prosecutor_name_1 || ' ' || rpa.prosecutor_name_2 || ' ' || rpa.prosecutor_name_3 as respondent_name, rpad.address_1 || CHR(10) || rpad.postcode as respondent_address, 
            -- Court columns 
            ctad.address_1 || chr(10) || ctad.postcode as court_address,  Replace(r.de_code, 'APPEAL AGAINST ') as appeal_type, 'Clerk to Justice' as clerk_to_justice,
            case.mag_conviction_date, lst.list_start_date, csol.time_listed, to_date(sysdate) as rpt_run_date, cdet.contact_value as ct_phone_no 
    from    xhb_case case, xhb_defendant df, xhb_defendant_on_case doc, xhb_address dfad, xhb_address rpad, xhb_address ctad,   
            xhb_case_prosecutor_agency cpa, xhb_ref_prosecutor_agency rpa, 
            xhb_case_on_list csol, xhb_list lst,
            xhb_court ct, xhb_ref_system_code r, xhb_contact_detail cdet   
    where  df.defendant_id = doc.defendant_id 
    and    df.address_id   = dfad.address_id 
    and    doc.case_id = case.case_id 
    and    cpa.case_id = case.case_id
    and    cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id 
    and    rpa.address_id = rpad.address_id
    and    ct.address_id = ctad.address_id  
    and    ct.address_id = cdet.address_id 
    and    cdet.contact_type = 'Phone' 
    and    case.case_id = csol.case_id 
    and    csol.list_id = lst.list_id  
    and    ct.court_id = lst.court_id
    and    ct.court_id = p_court_id -- parameter here
    and    case.case_type = 'A'     -- Appeal
    and    case.case_sub_type = r.code(+)
    and    r.code_type like '%APPEAL_TYP%' 

    union 

    select  case.case_id, case.case_number, ct.court_code, ct.court_name, 
            -- Appellant columns
            doc.defendant_number, df.first_name || ' ' || df.middle_name || ' ' || df.surname as appellant_name, dfad.address_1 || CHR(10) || dfad.postcode as appellant_address, 
            -- Respondent columns
            rpa.prosecutor_name_1 || ' ' || rpa.prosecutor_name_2 || ' ' || rpa.prosecutor_name_3 as respondent_name, rpad.address_1 || CHR(10) || rpad.postcode as respondent_address, 
            -- Court columns 
            ctad.address_1 || CHR(10) || ctad.postcode, Replace(r.de_code, 'APPEAL AGAINST ') as appeal_type, 'Clerk to Justice' as clerk_to_justice,
            case.mag_conviction_date, cdf.listing_date, cdf.listing_date, to_date(sysdate), cdet.contact_value as ct_phone_no 
    from    xhb_case case, xhb_defendant df, xhb_defendant_on_case doc, xhb_address dfad, xhb_address rpad, xhb_address ctad,   
            xhb_case_prosecutor_agency cpa, xhb_ref_prosecutor_agency rpa, 
            xhb_case_listing_entry cle, xhb_case_diary_fixture cdf, 
            xhb_court ct, xhb_ref_system_code r, xhb_contact_detail cdet   
    where  df.defendant_id = doc.defendant_id 
    and    df.address_id   = dfad.address_id 
    and    doc.case_id = case.case_id 
    and    cpa.case_id = case.case_id
    and    cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id 
    and    rpa.address_id = rpad.address_id
    and    ct.address_id = ctad.address_id  
    and    ct.address_id = cdet.address_id 
    and    cdet.contact_type = 'Phone' 
    and    case.case_id = cle.case_id  
    and    cle.case_listing_entry_id = cdf.case_listing_entry_id  
    and    ct.court_id = p_court_id -- parameter here
    and    case.case_type = 'A'     -- Appeal
    and    case.case_sub_type = r.code(+)
    and    r.code_type like '%APPEAL_TYP%' 
    order by 1	;
   
END getAppealHearingNotifnRpt;

PROCEDURE get_list_of_fixed_dates_report(p_results_out out SYS_REFCURSOR
                                        ,p_court_id IN XHB_CASE.COURT_ID%TYPE
                                        ,p_run_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE) AS

 v_err_code NUMBER;
 v_err_msg  VARCHAR2(1000);
   BEGIN
     OPEN p_results_out FOR
      SELECT
        xrht.HEARING_TYPE_DESC AS hearingtype,
        xc.CASE_TYPE || xc.CASE_NUMBER AS casenumber , 
        CASE
           WHEN xd.CURRENT_PRISON_STATUS = 'Y' THEN '*' || UPPER(xd.SURNAME) || ' ' || UPPER(SUBSTR(xd.MIDDLE_NAME,1,1)) || ' ' || LOWER(xd.SURNAME)
           ELSE UPPER(xd.SURNAME) || ' ' || UPPER(SUBSTR(xd.MIDDLE_NAME,1,1)) || ' ' || LOWER(xd.SURNAME)
        END AS defendant,
        xrs.SOLICITOR_FIRM_NAME AS representative,
        xdc.PTIURN AS ptiurn,
        xcdf.LISTING_DATE AS hearingdate,
        xco.COURT_NAME AS hearingvenue,
        xrpa.prosecutor_name_1 || ' ' || xrpa.prosecutor_name_2 || ' ' || xrpa.prosecutor_name_3 AS prosecutor,
        xcdf.LIST_NOTE_TEXT AS notes,
        xa.ADDRESS_1 || ' '  || xa.ADDRESS_2 || ' ' || xa.ADDRESS_3 || ' ' || xa.ADDRESS_4 || ' ' ||  xa.TOWN || ' ' || xa.COUNTY || ' ' || xa.POSTCODE AS courtaddress
        
        FROM XHB_CASE xc
        INNER JOIN XHB_DEFENDANT_ON_CASE xdc ON xc.CASE_ID = xdc.CASE_ID
        INNER JOIN XHB_HEARING xh ON xc.CASE_ID = xh.CASE_ID
        INNER JOIN XHB_REF_HEARING_TYPE xrht ON xh.REF_HEARING_TYPE_ID =  xrht.REF_HEARING_TYPE_ID
        INNER JOIN XHB_DEFENDANT xd ON xdc.DEFENDANT_ID = xd.DEFENDANT_ID
        INNER JOIN XHB_COURT xco ON xd.COURT_ID = xco.COURT_ID
        INNER JOIN XHB_ADDRESS xa ON xco.ADDRESS_ID = xa.ADDRESS_ID
        INNER JOIN XHB_DEF_ON_CASE_REF_SOL_FIRM xdocrsf ON xdc.DEFENDANT_ON_CASE_ID = xdocrsf.DEFENDANT_ON_CASE_ID
        INNER JOIN XHB_REF_SOLICITOR_FIRM xrs ON  xdocrsf.REF_SOLICITOR_FIRM_ID = xrs.REF_SOLICITOR_FIRM_ID
        INNER JOIN XHB_FIXTURE_DEFT_ATTENDING xfda ON xdc.DEFENDANT_ON_CASE_ID = xfda.DEFENDANT_ON_CASE_ID
        INNER JOIN XHB_CASE_DIARY_FIXTURE xcdf ON xfda.CASE_DIARY_FIXTURE_ID = xcdf.CASE_DIARY_FIXTURE_ID
        INNER JOIN XHB_CASE_PROSECUTOR_AGENCY xcpa ON xc.CASE_ID = xcpa.CASE_ID
        INNER JOIN XHB_REF_PROSECUTOR_AGENCY xrpa ON xcpa.REF_PROSECUTOR_AGENCY_ID = xrpa.REF_PROSECUTOR_AGENCY_ID

        WHERE xc.CASE_SUB_TYPE in ('B','S','C') AND xc.COURT_ID = p_court_id AND xcdf.LISTING_DATE <= p_run_date
        
        ORDER BY xrht.HEARING_TYPE_DESC,xc.CASE_TYPE || xc.CASE_NUMBER,UPPER(xd.SURNAME) || ' ' || UPPER(SUBSTR(xd.MIDDLE_NAME,1,1)) || ' ' || LOWER(xd.SURNAME);

  EXCEPTION
     WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      INSERT INTO XHB_REPORTS_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_LIST_OF_FIXED_DATES_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
      RAISE;
END get_list_of_fixed_dates_report;
  
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
 
 FUNCTION GET_SOLICITOR_ID(p_doc_id IN xhb_defendant_on_case.defendant_on_case_id%TYPE) RETURN XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE IS
  solicitor_return XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE := NULL; 
      BEGIN
    SELECT CASE WHEN xrsf.ref_solicitor_firm_id IS NOT NULL 
             AND xdocrsf.rep_end_date IS NULL --primary solictor found and rep_end is null then return primary name
               THEN xrsf.REF_SOLICITOR_FIRM_ID
            WHEN xrsf.ref_solicitor_firm_id IS NOT NULL --has a primary
             AND xrsf1.ref_solicitor_firm_id IS NOT NULL --and a secondary
             AND xdocrsf.rep_end_date IS NOT NULL --primary solictor has an end date
            -- AND xlao.rep_end_date IS NULL        --but the secondary solcitor doesnt
               THEN xrsf1.REF_SOLICITOR_FIRM_ID     --then return the secondary solicitor
            WHEN xrsf.ref_solicitor_firm_id IS NULL --no primary solicitor
             AND xrsf1.ref_solicitor_firm_id IS NOT NULL --but there is a secondary
             --AND xlao.rep_end_date IS NULL  --primary is null but secondary is not null use secondary
               THEN xrsf1.REF_SOLICITOR_FIRM_ID --use secondary
          ELSE NULL 
        END
        INTO solicitor_return
        FROM xhb_def_on_case_ref_sol_firm xdocrsf
    ,    xhb_legal_aid_order xlao
    ,    xhb_ref_solicitor_firm xrsf
    ,    xhb_ref_solicitor_firm xrsf1
  WHERE p_doc_id = xdocrsf.defendant_on_case_id (+)
  AND   xdocrsf.ref_solicitor_firm_id = xrsf.ref_solicitor_firm_id
  AND   NVL(xrsf.OBS_IND, '-') <> 'Y'
  AND   p_doc_id = xlao.defendant_on_case_id (+)
  AND   NVL(xlao.OBS_IND, '-') <> 'Y'
 -- AND   xlao.ref_solicitor_firm_id = xrsf1.ref_solicitor_firm_id
  AND   NVL(xrsf1.OBS_IND, '-') <> 'Y';
    
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

END XHB_REPORT_PKG;
/