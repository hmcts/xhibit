CREATE OR REPLACE PACKAGE xhb_case_pkg AS 

  FUNCTION determine_case_status (pn_case_id IN xhb_case.case_id%TYPE) RETURN VARCHAR2;
  
  PROCEDURE get_case_offences(p_results_out OUT SYS_REFCURSOR,
                                p_case_id     IN XHB_CASE.CASE_ID%TYPE);

END xhb_case_pkg;
/
show errors