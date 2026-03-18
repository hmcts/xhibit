create or replace PACKAGE "XHB_REPORT_PKG" AS
    /**
  * CGI CREST to XHIBIT Program
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
  * 14/06/2018    John Riley         1.1       CTX-2145 Add RUMO report
  *
  **************************************************************************************/


 FUNCTION get_bc_status_ind(p_case_id IN XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE) 
 RETURN VARCHAR2;
  
PROCEDURE get_defendants_put_back_report(p_results_out OUT SYS_REFCURSOR
                            ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                            ,p_refSystemCode IN VARCHAR
                            , p_report_name IN VARCHAR2);

PROCEDURE get_docar_report(p_results_out OUT SYS_REFCURSOR,
                             p_papers_sent_date IN XHB_DEFENDANT_ON_CASE.FORM_NG_SENT_DATE%TYPE,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE);

PROCEDURE get_nfix_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE);


PROCEDURE get_list_officers_diary_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id            IN XHB_CASE.COURT_ID%TYPE
                          ,p_screen_date         IN XHB_DIARY_NOTE_ENTRY.DIARY_DATE%TYPE);
                          
PROCEDURE get_cfix_report(p_results_out OUT SYS_REFCURSOR,
						 p_court_id     IN XHB_CASE.COURT_ID%TYPE,
						 p_hearing_from_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE,
						 p_hearing_end_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE);

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
						  p_SORTBY IN VARCHAR2);	

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
						  p_SORTBY IN VARCHAR2);

PROCEDURE get_drsr_report(p_results_out OUT SYS_REFCURSOR,                       
                          p_court_id     IN XHB_CASE.COURT_ID%TYPE,  
                          p_MONTH_PERIOD IN VARCHAR2,
                          p_YEAR_PERIOD IN VARCHAR2); 						  
						  
						 
 PROCEDURE get_lod_report_between_dates(p_results_out OUT SYS_REFCURSOR
                                ,p_court_id    IN  XHB_CASE.COURT_ID%TYPE
                          ,p_from_date           IN XHB_DIARY_NOTE_ENTRY.DIARY_DATE%TYPE
                          ,p_to_date             IN XHB_DIARY_NOTE_ENTRY.DIARY_DATE%TYPE DEFAULT NULL);
                          
PROCEDURE get_list_of_fixed_dates_report(p_results_out OUT SYS_REFCURSOR
                                ,p_court_id    IN  XHB_CASE.COURT_ID%TYPE
                                ,p_run_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE);

PROCEDURE update_case_diary_fix_run_date(casediaryfixturelist IN VARCHAR);

PROCEDURE get_run_date(p_results_out OUT SYS_REFCURSOR,
                        p_court_id IN XHB_CASE.COURT_ID%TYPE,
                        p_report_type IN XHB_REPORT_LOG.CREST_REPORT_CODE%TYPE);

PROCEDURE get_prlis_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE
                          ,p_previous_list_id IN XHB_CASE.PUB_RUNNING_LIST_ID%TYPE);
						
PROCEDURE publish_running_list(p_cases_to_publish IN CLOB, p_court_id IN INTEGER);


PROCEDURE get_ctlrp_report(p_results_out OUT SYS_REFCURSOR
                                ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                                ,p_screen_time_limit IN XHB_DEFENDANT_ON_CASE.CUSTODY_TIME_LIMIT%TYPE);
								
PROCEDURE get_ctlrl_report(p_results_out OUT SYS_REFCURSOR
                                ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                                ,p_screen_time_limit IN XHB_DEFENDANT_ON_CASE.CUSTODY_TIME_LIMIT%TYPE);
								
PROCEDURE update_case_reminder_printed(p_cases IN CLOB);
						
/*Nested loop functionality for Dave Burden*/
 FUNCTION get_charge (p_case_id IN xhb_case.case_id%TYPE) RETURN VARCHAR2;

 FUNCTION GET_SOLICITOR_ID(p_doc_id IN xhb_defendant_on_case.defendant_on_case_id%TYPE) RETURN XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE;

 FUNCTION GET_MOST_RECENT_HEARING(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XHB_DEF_HEARING_RECORD.HEARING_RECORD_ID%TYPE;

PROCEDURE UPDATE_REPORT_LOG( p_court_id IN XHB_REPORT_LOG.COURT_ID%TYPE,
                            p_report_code IN XHB_REPORT_LOG.CREST_REPORT_CODE%TYPE,
                            p_report_name IN XHB_REPORT_LOG.REPORT_NAME%TYPE);
PROCEDURE getAppealHearingNotifnRpt(p_resultset OUT SYS_REFCURSOR,
									p_court_id  IN  XHB_COURT.COURT_ID%TYPE);

Function  getAddress(p_address_id  xhb_address.address_id%type)
RETURN VARCHAR2;

Function getPhoneNum(p_address_id  xhb_address.address_id%type) 
RETURN xhb_contact_detail.contact_value%type;

Function getAppellantAddress(p_deft_on_case_id  xhb_defendant_on_case.defendant_on_case_id%type)
RETURN VARCHAR2;

FUNCTION GET_DEFENDANTS_LIST(p_court_id IN XHB_CASE.COURT_ID%TYPE,p_case_id IN xhb_case.case_id%TYPE,
                              p_screen_time_limit IN XHB_DEFENDANT_ON_CASE.CUSTODY_TIME_LIMIT%TYPE) RETURN CLOB;
                                  

PROCEDURE get_obw_report(p_results_out OUT SYS_REFCURSOR,
                             p_bw_issue_date IN XHB_BW_HISTORY.BW_ISSUE_DATE%TYPE,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE);                                   
                                  

PROCEDURE getRUMO_Rpt(p_resultset OUT SYS_REFCURSOR
                    , p_court_id  IN  XHB_REF_COURT.REF_COURT_ID%TYPE ) ; 

END XHB_REPORT_PKG;
 /
show errors
