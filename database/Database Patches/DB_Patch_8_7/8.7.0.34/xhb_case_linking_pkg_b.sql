create or replace PACKAGE BODY XHB_CASE_LINKING_PKG AS

  PROCEDURE get_defendants_on_active_cases(p_results_out 		OUT 	SYS_REFCURSOR, 
                                             p_court_id_in		IN 		XHB_CASE.court_id%TYPE,
                                             p_defendant_id_in	IN 	 	XHB_DEFENDANT_ON_CASE.defendant_id%TYPE) AS
  BEGIN
    --log_entry('get_defendants_on_active_cases');
  
    OPEN p_results_out	FOR
      SELECT DISTINCT	d.*
      FROM 	XHB_DEFENDANT_ON_CASE d, XHB_CASE c
      WHERE	c.court_id = p_court_id_in
      AND   d.case_id = c.case_id
      AND 	d.defendant_id = p_defendant_id_in
      AND 	(d.results_verified is null or d.results_verified != 'E')
      AND 	(d.obs_ind is null or d.obs_ind = 'N' or d.obs_ind = '');
  END get_defendants_on_active_cases;
  
  
  PROCEDURE get_cases_by_group_number(p_results_out 		OUT 	SYS_REFCURSOR, 
                                      p_court_id_in		IN 		XHB_CASE.court_id%TYPE,
                                      p_group_number_in	IN 	 	XHB_CASE.case_group_number%TYPE) AS
  BEGIN
    --log_entry('get_cases_by_group_number');
  
    OPEN p_results_out	FOR
      SELECT DISTINCT	c.*
           ,xhb_case_pkg.determine_case_status(c.case_id) live_status
           ,rc.court_full_name
      FROM 	XHB_CASE c
           ,XHB_REF_COURT rc
      WHERE	c.case_group_number = p_group_number_in
      AND   c.court_id = p_court_id_in
      AND   rc.ref_court_id(+) = c.ref_court_id
      ORDER BY c.case_type DESC, c.case_number;
  END get_cases_by_group_number;
  
  PROCEDURE get_linked_cases(p_results_out      OUT SYS_REFCURSOR,
                             p_court_id_in      IN  XHB_CASE.court_id%TYPE,
                             p_defendant_id_in  IN  XHB_DEFENDANT_ON_CASE.DEFENDANT_ID%TYPE,
                             p_group_number_in  IN  XHB_CASE.CASE_GROUP_NUMBER%TYPE)  AS
                             
  BEGIN
    --log_entry('get_linked_cases');
  
    OPEN p_results_out	FOR
      SELECT DISTINCT	c.*
      FROM 	XHB_CASE c
      WHERE (c.case_id IN (
              SELECT d.case_id 
              FROM  XHB_DEFENDANT_ON_CASE d
              WHERE d.defendant_id = p_defendant_id_in
              AND 	(d.results_verified is null or d.results_verified != 'E')
              AND 	(d.obs_ind is null or d.obs_ind = 'N' or d.obs_ind = '')
            )
      OR  c.case_group_number = p_group_number_in)
      AND c.court_id = p_court_id_in;
  END get_linked_cases;  
  
  PROCEDURE return_count_active_cases(p_num_defs_out    OUT NUMBER,
                                      p_case_id_in      IN  XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE) AS 
  BEGIN
    --log_entry('get_linked_cases');
  SELECT COUNT(*)
  INTO p_num_defs_out
    FROM XHB_DEFENDANT_ON_CASE d
    WHERE d.case_id = p_case_id_in
    AND 	(d.results_verified is null or d.results_verified != 'E')
    AND 	(d.obs_ind is null or d.obs_ind = 'N' or d.obs_ind = '');
  END return_count_active_cases;
  
  PROCEDURE find_active_cases_with_group(p_results_out      OUT SYS_REFCURSOR,
                             p_court_id_in      IN  XHB_CASE.court_id%TYPE,
                             p_group_number_in  IN  XHB_CASE.CASE_GROUP_NUMBER%TYPE)  AS
                             
  BEGIN
    --log_entry('find_active_cases_with_group');
    OPEN p_results_out	FOR
      SELECT DISTINCT	c.*
      FROM 	XHB_CASE c
      WHERE c.case_group_number = p_group_number_in
      AND (c.case_id IN (
              SELECT d.case_id 
              FROM  XHB_DEFENDANT_ON_CASE d
              WHERE 	(d.results_verified is null or d.results_verified != 'E')
              AND 	(d.obs_ind is null or d.obs_ind = 'N' or d.obs_ind = '')
            ))
      AND c.court_id = p_court_id_in
      ORDER BY c.case_number ASC, c.case_type DESC;
  END find_active_cases_with_group;  
  
  PROCEDURE find_common_defendants_grouped(p_results_out      OUT SYS_REFCURSOR,
                             p_case_id_in       IN  XHB_CASE.case_id%TYPE,
                             p_court_id_in      IN  XHB_CASE.court_id%TYPE,
                             p_group_number_in  IN  XHB_CASE.CASE_GROUP_NUMBER%TYPE) AS 
                                                  
  BEGIN 
    OPEN p_results_out FOR 
      SELECT distinct c.* 
      FROM XHB_DEFENDANT_ON_CASE d, XHB_CASE c
      WHERE c.case_id IN (
        SELECT distinct d.case_id
        FROM XHB_DEFENDANT_ON_CASE d, XHB_CASE c
        WHERE c.case_id = p_case_id_in
        AND   (d.defendant_id IN (
            SELECT d.defendant_id
            FROM XHB_DEFENDANT_ON_CASE d, XHB_CASE c
            WHERE c.case_group_number = p_group_number_in
            AND   c.case_id <> p_case_id_in
            AND   c.case_id = d.case_id
            AND   c.court_id = p_court_id_in
            AND 	(d.obs_ind is null or d.obs_ind = 'N' or d.obs_ind = '')
            ))
        AND d.case_id = c.case_id
        );
  END find_common_defendants_grouped;
  
END XHB_CASE_LINKING_PKG;
/