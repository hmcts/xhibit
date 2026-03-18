create or replace PACKAGE "XHB_REPORT_PKG" AS
  

  PROCEDURE get_defendants_put_back_report(p_results_out OUT SYS_REFCURSOR  
                            ,p_court_id    IN XHB_CASE.COURT_ID%TYPE
                            ,p_refSystemCode IN VARCHAR); 
                            
  PROCEDURE get_adjss_report(p_results_out OUT SYS_REFCURSOR  
                            ,p_court_id    IN XHB_CASE.COURT_ID%TYPE);


  PROCEDURE get_docar_report(p_results_out OUT SYS_REFCURSOR,
                             p_papers_sent_date IN XHB_DEFENDANT_ON_CASE.FORM_NG_SENT_DATE%TYPE,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE);

 PROCEDURE get_nfix_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id     IN XHB_CASE.COURT_ID%TYPE);
                                

 PROCEDURE get_list_officers_diary_report(p_results_out OUT SYS_REFCURSOR
                          ,p_court_id            IN XHB_CASE.COURT_ID%TYPE
                          ,p_screen_date         IN XHB_DIARY_NOTE_ENTRY.DIARY_DATE%TYPE);

/*Nested loop functionality for Dave Burden*/
 FUNCTION get_charge (p_case_id IN xhb_case.case_id%TYPE) RETURN VARCHAR2;
 
 FUNCTION GET_SOLICITOR_ID(p_doc_id IN xhb_defendant_on_case.defendant_on_case_id%TYPE) RETURN XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE;
 
FUNCTION GET_MOST_RECENT_HEARING(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XHB_DEF_HEARING_RECORD.HEARING_RECORD_ID%TYPE;

 END XHB_REPORT_PKG;
 /
show errors