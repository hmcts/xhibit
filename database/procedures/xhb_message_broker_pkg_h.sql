CREATE OR REPLACE PACKAGE xhb_message_broker_pkg AS

  /*
   * Get all Selector rows or the Selector that matches the ID
   */
  PROCEDURE get_selectors(p_results_out    OUT SYS_REFCURSOR);

  /*
   * Get all Queue rows for a given Selector
   */
  PROCEDURE get_queues_by_selector_id(p_results_out    OUT SYS_REFCURSOR,
                                      p_selector_id_in IN  XHB_SELECTOR_QUEUES.SELECTOR_ID%TYPE);

END xhb_message_broker_pkg;
/
show errors
