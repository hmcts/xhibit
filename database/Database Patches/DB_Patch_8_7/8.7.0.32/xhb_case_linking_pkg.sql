create or replace PACKAGE XHB_CASE_LINKING_PKG AS 

    PROCEDURE get_defendants_on_active_cases(p_results_out 		OUT 	SYS_REFCURSOR, 
                                             p_court_id_in		IN 		XHB_CASE.court_id%TYPE,
                                             p_defendant_id_in	IN 	 	XHB_DEFENDANT_ON_CASE.defendant_id%TYPE);
                                             
    PROCEDURE get_cases_by_group_number(p_results_out 		OUT 	SYS_REFCURSOR, 
                                      p_court_id_in		IN 		XHB_CASE.court_id%TYPE,
                                      p_group_number_in	IN 	 	XHB_CASE.case_group_number%TYPE);
                                      
    PROCEDURE get_linked_cases(p_results_out      OUT SYS_REFCURSOR,
                             p_court_id_in      IN  XHB_CASE.court_id%TYPE,
                             p_defendant_id_in  IN  XHB_DEFENDANT_ON_CASE.DEFENDANT_ID%TYPE,
                             p_group_number_in  IN  XHB_CASE.CASE_GROUP_NUMBER%TYPE);
                             
     PROCEDURE return_count_active_cases(p_num_defs_out    OUT NUMBER,
                                      p_case_id_in      IN  XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE);
                                      
     PROCEDURE find_active_cases_with_group(p_results_out      OUT SYS_REFCURSOR,
                             p_court_id_in      IN  XHB_CASE.court_id%TYPE,
                             p_group_number_in  IN  XHB_CASE.CASE_GROUP_NUMBER%TYPE);
                             
     PROCEDURE find_common_defendants_grouped(p_results_out      OUT SYS_REFCURSOR,
                             p_case_id_in       IN  XHB_CASE.case_id%TYPE,
                             p_court_id_in      IN  XHB_CASE.court_id%TYPE,
                             p_group_number_in  IN  XHB_CASE.CASE_GROUP_NUMBER%TYPE);           

END XHB_CASE_LINKING_PKG;
/