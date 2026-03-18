CREATE OR REPLACE PACKAGE BODY XHIBIT.xhb_data_reset_pkg AS

/*********************************************************************************
* XHB_DATA_RESET_PKG_B.SQL
* Used FOR NLE maintenance OF cases used FOR testing
*
* Version  DATE       Author   		Comments
* 1.0      24/09/2015 S Atwell  	Creation
************************************************************************************/

  l_error_point    VARCHAR2(50);

--
----
--

  PROCEDURE get_court (p_results_out OUT SYS_REFCURSOR, p_court_name IN VARCHAR2) IS

  BEGIN

    OPEN p_results_out FOR
        SELECT c.court_name, c.court_id, c.display_name
	FROM xhb_court c
	WHERE UPPER(c.court_name) = p_court_name;

  END get_court;

--
----
--

  /*
   * Retrieves ALL managed cases plus associated CASE DATA
   * If court id = 0 THEN return DATA FOR ALL courts
   */
  PROCEDURE get_managed_cases_by_court (p_results_out OUT SYS_REFCURSOR, p_court_id IN NUMBER) IS

  BEGIN

    IF p_court_id=0 THEN

	    OPEN p_results_out FOR
        	SELECT cas.case_id, cas.case_type, cas.case_number, cas.court_id, doc.defendant_id, d.first_name, d.middle_name, d.surname, d.date_of_birth, c.court_name
		FROM xhb_defendant_on_case doc, xhb_case cas, xhb_defendant d, xhb_court c  WHERE doc.case_id IN (
			SELECT cas.case_id
			FROM xhb_case cas
			WHERE case_id IN (SELECT nmc.case_id FROM xhb_nle_managed_cases nmc)
		)
		AND cas.case_id=doc.case_id
		AND doc.defendant_id=d.defendant_id
		AND cas.court_id=c.court_id
		ORDER BY cas.court_id, cas.case_id
	    ;

    ELSE

	    OPEN p_results_out FOR
        	SELECT cas.case_id, cas.case_type, cas.case_number, cas.court_id, doc.defendant_id, d.first_name, d.middle_name, d.surname, d.date_of_birth, c.court_name
		FROM xhb_defendant_on_case doc, xhb_case cas, xhb_defendant d, xhb_court c  WHERE doc.case_id IN (
			SELECT cas.case_id
			FROM xhb_case cas
			WHERE case_id IN (SELECT nmc.case_id FROM xhb_nle_managed_cases nmc)
			AND cas.court_id=p_court_id
		)
		AND cas.case_id=doc.case_id
		AND doc.defendant_id=d.defendant_id
		AND cas.court_id=c.court_id
		ORDER BY cas.court_id, cas.case_id
	    ;

    END IF;

  END get_managed_cases_by_court;

--
----
--

  PROCEDURE update_date_of_birth (p_dob IN DATE, p_defendant_id IN NUMBER) IS

  BEGIN

    UPDATE xhb_defendant SET date_of_birth=p_dob
    WHERE defendant_id=p_defendant_id;


  END update_date_of_birth;


--
----
--

  PROCEDURE add_case(p_case_id IN NUMBER) IS

  BEGIN

	INSERT INTO xhb_nle_managed_cases (case_id) 
	VALUES(p_case_id);

  END add_case;

--
----
--

  PROCEDURE remove_case(p_case_id IN NUMBER) IS

  BEGIN

	DELETE FROM xhb_nle_managed_cases WHERE case_id=p_case_id;

  END remove_case;

--
----
--

  PROCEDURE check_case_already_added(p_results_out OUT SYS_REFCURSOR, p_case_id IN NUMBER) IS

  BEGIN

	OPEN p_results_out FOR
	    SELECT COUNT(*) AS v_recordexists
	    FROM xhb_nle_managed_cases
	    WHERE case_id=p_case_id;

  END check_case_already_added;

--
----
--

  PROCEDURE check_case_exists(p_results_out OUT SYS_REFCURSOR, p_court_id IN NUMBER, p_case_type IN VARCHAR2, p_casenumber IN NUMBER) IS

  BEGIN

	OPEN p_results_out FOR
    	SELECT case_id
    	FROM xhb_case
    	WHERE court_id=p_court_id AND case_type=p_case_type AND case_number=p_casenumber;
        EXCEPTION
            WHEN OTHERS
                THEN raise_application_error(-20011,'Unknown exception in check_case_exists proc');

  END check_case_exists;


END xhb_data_reset_pkg;
/
