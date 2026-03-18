create or replace PACKAGE BODY XHB_REDEL_PKG AS

/**
  * DESCRIPTION :
  *   Procedures          Purpose
  *   =========           =======
  *   sentence_trial_delete       Search tables where defendantOnCaseId exists and set obsolete
  *
  */
  l_error_point    VARCHAR2(50);

PROCEDURE sentence_trial_delete
      (p_defendant_on_case_id IN xhb_defendant_on_case.defendant_on_case_id%TYPE )
  IS

  BEGIN

    l_error_point := 'SENTENCE TRIAL DELETE';

    -- 1
    UPDATE xhb_defendant_on_offence
    SET obs_ind = 'Y'
    WHERE  defendant_on_case_id = p_defendant_on_case_id;

    l_error_point := 'SENTENCE TRIAL DELETE 1';

    -- 2
    UPDATE xhb_defendant_charge
    SET obs_ind = 'Y'
    WHERE  defendant_on_case_id = p_defendant_on_case_id;

     l_error_point := 'SENTENCE TRIAL DELETE 2';

    -- 3
    UPDATE xhb_charge
    SET obs_ind = 'Y'
    WHERE  charge_id IN (select dc.charge_id from xhb_defendant_charge dc where dc.defendant_on_case_id=p_defendant_on_case_id);

    l_error_point := 'SENTENCE TRIAL DELETE 3';

    -- 4
    UPDATE xhb_breach
    SET obs_ind = 'Y'
    WHERE  charge_id IN (select dc.charge_id from xhb_defendant_charge dc where dc.defendant_on_case_id=p_defendant_on_case_id);

    l_error_point := 'SENTENCE TRIAL DELETE 4';

    -- 5
    UPDATE xhb_offence
    SET obs_ind = 'Y'
    WHERE  charge_id IN (select dc.charge_id from xhb_defendant_charge dc where dc.defendant_on_case_id=p_defendant_on_case_id);

    l_error_point := 'SENTENCE TRIAL DELETE 5';

    -- 6
    UPDATE xhb_plea
    SET obs_ind = 'Y'
    WHERE  defendant_charge_id IN (select dc.defendant_charge_id from xhb_defendant_charge dc where dc.defendant_on_case_id=p_defendant_on_case_id);

    l_error_point := 'SENTENCE TRIAL DELETE 6';

    -- 7
    UPDATE xhb_defendant_on_case
    SET obs_ind = 'Y'
    WHERE  defendant_on_case_id = p_defendant_on_case_id;

    l_error_point := 'SENTENCE TRIAL DELETE 7';


  EXCEPTION
        WHEN OTHERS THEN
          -- Rollback all the obsoletes for this defendantOnCaseId
          ROLLBACK;
          RAISE;

  END sentence_trial_delete;

PROCEDURE appeal_delete
      (p_case_id IN xhb_case.case_id%TYPE,
       p_defendant_on_case_id IN xhb_defendant_on_case.defendant_on_case_id%TYPE )
  IS

  BEGIN

    l_error_point := 'APPEAL DELETE';

    -- 1
    UPDATE xhb_charge
    SET obs_ind = 'Y'
    WHERE  (CHARGE_TYPE = 'C' or CHARGE_TYPE = 'M') and CASE_ID = p_case_id;

    l_error_point := 'APPEAL DELETE 1';

    -- 2
    UPDATE xhb_offence
    SET obs_ind = 'Y'
    WHERE  charge_id IN (select dc.charge_id from xhb_charge dc where (dc.CHARGE_TYPE = 'C' or dc.CHARGE_TYPE = 'M') and dc.CASE_ID = p_case_id);

    l_error_point := 'APPEAL DELETE 2';

    -- 3
    UPDATE xhb_defendant_on_offence
    SET obs_ind = 'Y'
    WHERE offence_id IN (SELECT o.offence_id
                          FROM xhb_offence o,
                          xhb_charge c
                          WHERE o.charge_id = c.charge_id
                          AND (c.charge_type = 'C' or c.charge_type = 'M')
                          AND c.case_id = p_case_id);


    l_error_point := 'APPEAL DELETE 3';

    -- 4
    UPDATE xhb_disposal2
    SET obs_ind = 'Y'
    WHERE  defendant_on_offence_id IN (SELECT xdof.defendant_on_offence_id
                                     FROM xhb_defendant_on_offence xdof,
                                     xhb_offence o,
                                     xhb_charge c
                                     WHERE xdof.offence_id = o.offence_id
                                     AND o.charge_id = c.charge_id
                                     AND (c.charge_type = 'C' or c.charge_type = 'M')
                                     AND c.case_id = p_case_id);


    l_error_point := 'APPEAL DELETE 4';

    -- 5
    UPDATE xhb_disposal_line
    SET obs_ind = 'Y'
    WHERE disposal2_id IN (SELECT disposal2_id
                           FROM xhb_disposal2 d,
                           xhb_defendant_on_offence xdof,
                           xhb_offence o,
                           xhb_charge c
                           WHERE d.defendant_on_offence_id = xdof.defendant_on_offence_id
                           AND xdof.offence_id = o.offence_id
                           AND o.charge_id = c.charge_id
                           AND (c.charge_type = 'C' or c.charge_type = 'M')
                           AND c.case_id = p_case_id);

    l_error_point := 'APPEAL DELETE 5';

    -- 6
    UPDATE xhb_defendant_on_case
    SET obs_ind = 'Y'
    WHERE  defendant_on_case_id = p_defendant_on_case_id;

    l_error_point := 'APPEAL DELETE 6';


  EXCEPTION
        WHEN OTHERS THEN
          -- Rollback all the obsoletes for this defendantOnCaseId
          ROLLBACK;
          RAISE;

  END appeal_delete;

PROCEDURE find_future_fixtures (p_results_out OUT SYS_REFCURSOR,
      p_case_id IN xhb_case.case_id%TYPE, p_current_date date)
  IS

  BEGIN

    l_error_point := 'FIND FUTURE FIXTURES';

    -- 1
    OPEN p_results_out	FOR
      SELECT cdf.CASE_DIARY_FIXTURE_ID, cdf.CASE_LISTING_ENTRY_ID, cdf.HEARING_TYPE_ID, cdf.OBS_IND 
	  from XHB_CASE_DIARY_FIXTURE cdf where cdf.LISTING_DATE > p_current_date and cdf.case_listing_entry_id in
    (select case_listing_entry_id from xhb_case_listing_entry where case_id = p_case_id);

    l_error_point := 'FIND FUTURE FIXTURES 1';


  EXCEPTION
        WHEN OTHERS THEN
          ROLLBACK;
          RAISE;

  END find_future_fixtures;


PROCEDURE find_future_listings (p_results_out OUT SYS_REFCURSOR,
      p_case_id IN xhb_case.case_id%TYPE, p_current_date date)
  IS

  BEGIN

    l_error_point := 'FIND FUTURE LISTINGS';

    -- 1
    OPEN p_results_out	FOR
    SELECT col.CASE_ON_LIST_ID, col.CASE_ID, col.LIST_ID, col.OBS_IND 
	from XHB_CASE_ON_LIST col where col.case_id = p_case_id and col.list_id in
    (select list_id from xhb_list where list_start_date > p_current_date);

    l_error_point := 'FIND FUTURE LISTINGS 1';


  EXCEPTION
        WHEN OTHERS THEN
          ROLLBACK;
          RAISE;

  END find_future_listings;


PROCEDURE find_future_fixtures_def( p_results_out  OUT SYS_REFCURSOR,
                                   p_case_id              IN  XHB_CASE_ON_LIST.CASE_ID%TYPE,
                                   p_defendant_on_case_id IN  XHB_FIXTURE_DEFT_ATTENDING.DEFENDANT_ON_CASE_ID%TYPE,
                                   p_current_date date) AS
  BEGIN
        OPEN p_results_out FOR
        SELECT xfda.DEFENDANT_ON_CASE_ID, xfda.CASE_DIARY_FIXTURE_ID, xfda.ATTENDING, xfda.OBS_IND, xfda.FIXTURE_DEFT_ATTENDING_ID
        FROM  XHB_FIXTURE_DEFT_ATTENDING xfda
        WHERE NVL(xfda.OBS_IND, 'N') <> 'Y'
	AND xfda.ATTENDING = 'Y'
        AND   xfda.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
        AND   xfda.CASE_DIARY_FIXTURE_ID IN (
              SELECT xcdf.CASE_DIARY_FIXTURE_ID
              FROM   XHB_CASE_DIARY_FIXTURE xcdf
              WHERE  xcdf.LISTING_DATE > p_current_date
              AND    xcdf.CASE_LISTING_ENTRY_ID IN (
                    SELECT xcle.CASE_LISTING_ENTRY_ID
                    FROM   XHB_CASE_LISTING_ENTRY xcle
                    WHERE xcle.CASE_ID = p_case_id
              )
        );
END find_future_fixtures_def;

PROCEDURE find_future_hearings_def( p_results_out  OUT SYS_REFCURSOR,
                                   p_case_id              IN  XHB_CASE_ON_LIST.CASE_ID%TYPE,
                                   p_defendant_on_case_id IN  XHB_FIXTURE_DEFT_ATTENDING.DEFENDANT_ON_CASE_ID%TYPE,
                                   p_current_date date) AS
  BEGIN
        OPEN p_results_out FOR
        SELECT xdocol.DEFENDANT_ON_CASE_ID, xdocol.CASE_ON_LIST_ID, xdocol.DEF_ON_CASE_ON_LIST_ID, xdocol.CASE_ID
        FROM  XHB_DEF_ON_CASE_ON_LIST xdocol
        WHERE NVL(xdocol.OBS_IND, 'N') <> 'Y'
        AND   xdocol.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
        AND   xdocol.CASE_ON_LIST_ID IN (
              SELECT xcol.CASE_ON_LIST_ID
              FROM XHB_CASE_ON_LIST xcol
              WHERE xcol.LIST_ID IN (
                SELECT xl.LIST_ID
                FROM XHB_LIST xl
                WHERE xl.LIST_START_DATE > p_current_date
              )
              AND xcol.CASE_ID = p_case_id
        );
END find_future_hearings_def;

END XHB_REDEL_PKG;
/
show errors
