CREATE OR REPLACE PACKAGE xhb_daily_list_pkg AS

  PROCEDURE delete_daily_list(court_id_in IN  XHB_HEARING_LIST.COURT_ID%TYPE,
                              list_id_in  IN  XHB_HEARING_LIST.LIST_ID%TYPE);

END xhb_daily_list_pkg;
/
show errors