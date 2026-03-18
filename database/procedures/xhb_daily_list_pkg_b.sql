CREATE OR REPLACE PACKAGE BODY xhb_daily_list_pkg AS

  PROCEDURE delete_daily_list(court_id_in IN  XHB_HEARING_LIST.COURT_ID%TYPE,
                              list_id_in  IN  XHB_HEARING_LIST.LIST_ID%TYPE) AS

  BEGIN

    DELETE FROM xhb_sched_hearing_defendant
    WHERE       scheduled_hearing_id IN (SELECT scheduled_hearing_id
                                         FROM   xhb_scheduled_hearing
                                         WHERE  sitting_id IN (SELECT sitting_id
                                                               FROM   xhb_sitting
                                                               WHERE  list_id IN (SELECT list_id
                                                                                  FROM   xhb_hearing_list
                                                                                  WHERE  crest_list_id = list_id_in
                                                                                  AND    court_id = court_id_in)));

    DELETE FROM xhb_scheduled_hearing
    WHERE       sitting_id IN (SELECT sitting_id
                               FROM   xhb_sitting
                               WHERE  list_id IN (SELECT list_id
                                                  FROM   xhb_hearing_list
                                                  WHERE  crest_list_id = list_id_in
                                                  AND    court_id = court_id_in));

    DELETE FROM xhb_sitting
    WHERE       list_id IN (SELECT list_id
                            FROM   xhb_hearing_list
                            WHERE  crest_list_id = list_id_in
                            AND    court_id = court_id_in);

    DELETE FROM xhb_hearing_list
    WHERE       crest_list_id = list_id_in
    AND         court_id = court_id_in;

  END delete_daily_list;

END xhb_daily_list_pkg;
/
show errors