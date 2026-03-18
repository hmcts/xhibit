CREATE OR REPLACE PACKAGE xhb_authorise_check_pkg AS
	PROCEDURE get_warnings(
		p_results_out OUT SYS_REFCURSOR,
		p_case_id     IN XHB_CASE.CASE_ID%TYPE);
END xhb_authorise_check_pkg;
/
show errors;
