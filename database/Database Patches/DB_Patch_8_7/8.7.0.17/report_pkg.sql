create or replace PACKAGE          "XHB_REPORT_PKG" AS

  PROCEDURE get_defendants_put_back_report(p_results_out OUT SYS_REFCURSOR  
                            ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                            ,p_refSystemCode IN VARCHAR);


  PROCEDURE get_docar_report(p_results_out OUT SYS_REFCURSOR,
                             p_papers_sent_date IN XHB_DEFENDANT_ON_CASE.FORM_NG_SENT_DATE%TYPE,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE);

  PROCEDURE get_nfix_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE);
  
  PROCEDURE get_unlc_report(p_results_out OUT SYS_REFCURSOR
                         ,p_court_id     IN XHB_CASE.COURT_ID%TYPE);

PROCEDURE get_prlis_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE);                                 


/*Nested loop functionality for Dave Burden*/
 FUNCTION get_charge (p_case_id IN xhb_case.case_id%TYPE) RETURN VARCHAR2;
 
 FUNCTION GET_SOLICITOR_ID(p_doc_id IN xhb_defendant_on_case.defendant_on_case_id%TYPE) RETURN XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE;
 
 PROCEDURE set_prlis_report_run(p_cases_run VARCHAR2);
 
 END XHB_REPORT_PKG;

create or replace PACKAGE BODY          "XHB_REPORT_PKG" AS
  

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
    AND c.COURT_ID = p_court_id;
	
  EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORTS_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.get_docar_report '|| v_err_code || ' : ' ||v_err_msg, sysdate);
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
,    xhb_case_diary_time_rqmt xcdtr
,    xhb_defendant_on_case xdoc
,    xhb_defendant xd
,    xhb_case xc
,    xhb_court xcrt
,    xhb_address cxa --court address
,    xhb_address dxa --defendant address
,    xhb_case_diary_fixture xcdf
,    xhb_ref_hearing_type xrht
,    xhb_contact_detail xcd
WHERE xcle.case_listing_entry_id = xcdtr.case_listing_entry_id
AND   xcle.case_id = xdoc.case_id
AND   NVL(xdoc.OBS_IND, '-') <> 'Y'
AND   xdoc.defendant_id = xd.defendant_id
AND   trunc(xcdtr.first_listing_date) > trunc(sysdate)
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


PROCEDURE get_unlc_report(p_results_out OUT SYS_REFCURSOR
                         ,p_court_id     IN XHB_CASE.COURT_ID%TYPE) AS
  BEGIN
  OPEN p_results_out FOR
	  SELECT xc.case_number
    ,      xc.case_title
    ,      NVL(xc.committal_date,xc.sent_for_trial_date) "Commited/Sent for trial"
    ,      xrht.hearing_type_code
    ,      xdfc.trial_time_estimate
    ,      xro.offence_group
    ,      xro.offence_class
    ,      'FIRST NAD' as first_nad -- not sure what this field is
    FROM xhb_case xc
    ,    xhb_hearing xh
    ,    xhb_ref_hearing_type xrht
    ,    xhb_directions_for_case xdfc
    ,    xhb_charge xcg
    ,    xhb_offence xo
    ,    xhb_ref_offence xro
    WHERE xc.case_id = xh.case_id
    AND   xh.ref_hearing_type_id = xrht.ref_hearing_type_id
    AND   xc.case_id = xdfc.case_id
    AND   xc.case_id = xcg.case_id
    AND   xcg.charge_id = xo.charge_id
    AND   xo.ref_offence_id = xro.ref_offence_id
    AND   xc.court_id = p_court_id
    ORDER BY xc.case_number ASC
    ,      xc.committal_date
    ,      xc.sent_for_trial_date                    
    ;
END get_unlc_report;

PROCEDURE get_prlis_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE) AS
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
        WHERE xc.PRLIS_JOB_ID IS NULL
        AND xc.COURT_ID = p_court_id
        AND xc.CASE_ID = xdoc.CASE_ID (+) 
        AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID (+)
        AND xc.CASE_ID = xh.CASE_ID (+)       
        AND xc.COURT_ID = xcrt.COURT_ID
        AND xc.CASE_ID = xcpa.CASE_ID (+)
        AND xcpa.REF_PROSECUTOR_AGENCY_ID  = xrpa.REF_PROSECUTOR_AGENCY_ID (+)
        AND (xrpa.OBS_IND <> 'Y'
        OR xrpa.OBS_IND IS NULL)
        ORDER BY xc.CASE_NUMBER;
 EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := SQLERRM;
   INSERT INTO XHB_REPORTS_LOG(REPORTS_LOG_ID, error_message, run_date)
           VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, 'Exception handler raised when others in XHB_REPORT_PKG.GET_PRLIS_REPORT '|| v_err_code || ' : ' ||v_err_msg, sysdate);
   RAISE;                  
  END get_prlis_report;                                 


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
             AND xlao.rep_end_date IS NULL        --but the secondary solcitor doesnt
               THEN xrsf1.REF_SOLICITOR_FIRM_ID     --then return the secondary solicitor
            WHEN xrsf.ref_solicitor_firm_id IS NULL --no primary solicitor
             AND xrsf1.ref_solicitor_firm_id IS NOT NULL --but there is a secondary
             AND xlao.rep_end_date IS NULL  --primary is null but secondary is not null use secondary
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
  AND   xlao.ref_solicitor_firm_id = xrsf1.ref_solicitor_firm_id
  AND   NVL(xrsf1.OBS_IND, '-') <> 'Y';
    
  RETURN solicitor_return;  
    
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL; -- If no solicitors found, return null
    
END GET_SOLICITOR_ID;

END XHB_REPORT_PKG;
/