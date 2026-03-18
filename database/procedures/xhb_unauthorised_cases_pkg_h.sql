CREATE OR REPLACE PACKAGE xhb_unauthorised_cases_pkg AS
	PROCEDURE get_unauthorised_cases(p_results_out OUT SYS_REFCURSOR,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE);
END xhb_unauthorised_cases_pkg;
/
show errors