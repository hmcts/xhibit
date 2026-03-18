create or replace PACKAGE XHB_REDEL_PKG AS 
 
   PROCEDURE sentence_trial_delete (p_defendant_on_case_id IN xhb_defendant_on_case.defendant_on_case_id%TYPE );
   
   PROCEDURE appeal_delete (p_case_id IN xhb_case.case_id%TYPE, 
       p_defendant_on_case_id IN xhb_defendant_on_case.defendant_on_case_id%TYPE );
       
   PROCEDURE find_future_fixtures (p_results_out OUT SYS_REFCURSOR,
      p_case_id IN xhb_case.case_id%TYPE, p_current_date date);    
       
   PROCEDURE find_future_listings (p_results_out OUT SYS_REFCURSOR,
      p_case_id IN xhb_case.case_id%TYPE, p_current_date date);

END XHB_REDEL_PKG;
/
show errors