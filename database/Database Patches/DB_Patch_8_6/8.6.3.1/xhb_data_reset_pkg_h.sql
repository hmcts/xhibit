CREATE OR REPLACE PACKAGE XHIBIT.xhb_data_reset_pkg
AS
	PROCEDURE get_court (p_results_out OUT SYS_REFCURSOR, p_court_name IN VARCHAR2);
	PROCEDURE get_managed_cases_by_court (p_results_out OUT SYS_REFCURSOR,  p_court_id IN NUMBER);
	PROCEDURE update_date_of_birth (p_dob IN DATE, p_defendant_id IN NUMBER);
	PROCEDURE add_case(p_case_id IN NUMBER);
	PROCEDURE remove_case(p_case_id IN NUMBER);
	PROCEDURE check_case_already_added(p_results_out OUT SYS_REFCURSOR, p_case_id IN NUMBER);
	PROCEDURE check_case_exists(p_results_out OUT SYS_REFCURSOR, p_court_id IN NUMBER, p_case_type IN VARCHAR2, p_casenumber IN NUMBER);
END xhb_data_reset_pkg;
/
