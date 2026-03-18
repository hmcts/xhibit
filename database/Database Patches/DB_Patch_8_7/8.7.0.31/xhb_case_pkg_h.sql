CREATE OR REPLACE PACKAGE xhb_case_pkg AS 

  FUNCTION determine_case_status (pn_case_id IN xhb_case.case_id%TYPE) RETURN VARCHAR2;

END xhb_case_pkg;
/
show errors