CREATE OR REPLACE PACKAGE training_list_pkg AS

  /*
   * Get daily list selection from CREST
   */

  PROCEDURE get_tomorrows_list(p_results_out OUT SYS_REFCURSOR,
                               p_court_id    IN  XHB_COURT.COURT_ID%TYPE,
                               p_date        IN  DATE);

  PROCEDURE get_warned_list(p_results_out OUT SYS_REFCURSOR,
                            p_court_id    IN  XHB_COURT.COURT_ID%TYPE,
                            p_date        IN  DATE);

  PROCEDURE get_daily_list_dates(p_results_out OUT SYS_REFCURSOR,
                                 p_court_id   IN  XHB_COURT.COURT_ID%TYPE);

  PROCEDURE amend_daily_list_date(p_results_out OUT SYS_REFCURSOR,
                                  p_court_id    IN   XHB_COURT.COURT_ID%TYPE,
                                  p_list_id     IN   XHB_HEARING_LIST.LIST_ID%TYPE,
                                  p_date        IN   DATE);

END training_list_pkg;
/
show errors
