
--------------------------------------------------------
--  DDL for Package XHB_REPORT_PKG
--------------------------------------------------------

  create or replace PACKAGE          "XHB_REPORT_PKG" AS

 PROCEDURE get_docar_report(p_results_out OUT SYS_REFCURSOR,
                             p_papers_sent_date IN XHB_DEFENDANT_ON_CASE.FORM_NG_SENT_DATE%TYPE,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE);

 PROCEDURE get_adjss_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id      IN XHB_CASE.COURT_ID%TYPE);
 
 PROCEDURE get_nfix_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE);
 
 PROCEDURE get_unlc_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE);

END XHB_REPORT_PKG;

/
--------------------------------------------------------
--  DDL for Package Body XHB_REPORT_PKG
--------------------------------------------------------

create or replace PACKAGE BODY          "XHB_REPORT_PKG" AS

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

  PROCEDURE get_adjss_report(p_results_out OUT SYS_REFCURSOR  
                            ,p_court_id    IN XHB_CASE.COURT_ID%TYPE) AS
  BEGIN
  OPEN p_results_out FOR
	 select xc.CASE_TYPE || xc.case_number as case_number
	,      upper(xd.SURNAME)||' '|| xd.FIRST_NAME ||' '|| xd.MIDDLE_NAME AS defendant_name
	,      xdhr.ADJOURNED_DATE 
	,      xdhr.HEARING_END_DATE AS HEARING_DATE
	,      upper(xrj.SURNAME) ||  ' ' || xrj.FIRST_NAME ||' '|| xrj.MIDDLE_NAME AS judge_name
  ,      xdoc.ASN
  ,     substr(xrsc.DE_CODE, instr(xrsc.DE_CODE, '-', 1,1)) AS reason
  ,     xcdf.LISTING_DATE
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
	and       xdhr.HEARING_END_DATE = (SELECT MAX(xdhr2.HEARING_END_DATE) -- Is this the most recent hearing for this defendant?
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
    and       xdhr.HEARING_END_DATE = (SELECT MAX(xdhr2.HEARING_END_DATE) --Is this the most recent Adjourned and Put Back for Sentence hearing
                                             FROM xhb_def_hearing_record xdhr2
                                             ,    XHB_REF_SYSTEM_CODE xrsc2
                                              WHERE xdhr2.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                              AND   xdhr2.IS_ADJOURNED = 'Y' 
                                              AND   xdhr.ref_adjournment_id = xrsc2.ref_system_code_id (+) --outer join on for put back data
                                              AND   NVL(xrsc2.OBS_IND, '-') <> 'Y'
                                              AND   xrsc2.code_type = 'PB_TYPE'
                                              AND   xrsc2.code_title = 'ADJOURNMENT TYPE'
                                              AND   (xrsc2.code IN ('SC','SE','SM','SO','SS','S')
                                              OR    xrsc2.DE_CODE like 'PUT BACK FOR SENTENCE%'))
                                              )
		 ))
    and xsh.start_time = (SELECT MAX(xsh2.start_time)
                                FROM xhb_scheduled_hearing xsh2
                                WHERE xh.hearing_id = xsh2.hearing_id)
  and xc.court_id = p_court_id   
  and xc.CASE_ID = xcle.CASE_ID (+)
  and xcle.CASE_LISTING_ENTRY_ID = xcdf.CASE_LISTING_ENTRY_ID (+)
  ORDER BY xh.HEARING_END_DATE
  ,        	xc.case_number
  ,        xd.DEFENDANT_ID;  

	END get_adjss_report;


 PROCEDURE get_nfix_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE) AS
  BEGIN
  OPEN p_results_out FOR
	  SELECT NVL(xrsf1.SOLICITOR_FIRM_NAME,xd.first_name||' '||xd.middle_name||' '||xd.surname) as SOLICITOR_DEFENDANT
		,     NVL(NVL(sxa.address_1||' '||sxa.address_2||' '||sxa.address_3||' '||sxa.address_4||' '||sxa.postcode
					, sxa1.address_1||' '||sxa1.address_2||' '||sxa1.address_3||' '||sxa1.address_4||' '||sxa1.postcode)
					 ,dxa.address_1||' '||dxa.address_2||' '||dxa.address_3||' '||dxa.address_4||' '||dxa.postcode
				  ) SOLICITOR_DEFENDANT_ADDRESS --if the initial solicitor address is null then use the next one.  If both are null, use the defendant address
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
		,    xhb_defendant_on_case xdoc1
		,    xhb_defendant xd
		,    xhb_def_on_case_ref_sol_firm xdocrsf
		,    xhb_legal_aid_order xlao
		,    xhb_case xc
		,    xhb_court xcrt
		,    xhb_address cxa --court address
		,    xhb_ref_solicitor_firm xrsf
		,    xhb_address sxa -- xhb_def_on_case_ref_sol_firm solicitor address
		,    xhb_ref_solicitor_firm xrsf1
		,    xhb_address sxa1 -- xhb_legal_aid_order solicitor address
		,    xhb_address dxa --defendant address
		,    xhb_case_diary_fixture xcdf
		,    xhb_ref_hearing_type xrht
		,    XHB_CONTACT_DETAIL xcd
		WHERE xcle.case_listing_entry_id = xcdtr.case_listing_entry_id
		AND   xcle.case_id = xdoc.case_id
		AND   xdoc.defendant_id = xd.defendant_id
		AND   xcle.case_id = xdoc1.case_id
		AND   xdoc1.defendant_id = xd.defendant_id
		AND   xdoc.defendant_on_case_id = xdocrsf.defendant_on_case_id (+)
		AND   xdoc1.defendant_on_case_id = xlao.defendant_on_case_id (+)
		AND   xdocrsf.rep_end_date IS NULL
		AND   xlao.ref_solicitor_firm_id IS NOT NULL
		AND   xlao.rep_end_date IS NULL
		AND   trunc(xcdtr.first_listing_date) > trunc(sysdate)
		AND   xcle.case_id = xc.case_id
		AND   xc.court_id = xcrt.court_id
		and   xcrt.address_id = cxa.address_id
		AND   xdocrsf.ref_solicitor_firm_id = xrsf.ref_solicitor_firm_id
		AND   xrsf.address_id = sxa.address_id
		AND   xlao.ref_solicitor_firm_id = xrsf1.ref_solicitor_firm_id
		AND   xrsf1.address_id = sxa1.address_id
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
		AND   xcd.CONTACT_TYPE = 'Phone'            
		;
	END get_nfix_report;


END XHB_REPORT_PKG;

/

