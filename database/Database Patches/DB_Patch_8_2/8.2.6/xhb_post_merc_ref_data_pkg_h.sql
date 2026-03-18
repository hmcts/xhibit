CREATE OR REPLACE PACKAGE xhb_post_merc_ref_data_pkg AS

  PROCEDURE standing_post_merc(p_main_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE standing_cr_live_status;

  PROCEDURE standing_formb_result(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE standing_order_disposal_xref(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE standing_orders_types_temps(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE standing_ref_disp_menu_ctype(p_court_id IN XHB_COURT.COURT_ID%TYPE);

END xhb_post_merc_ref_data_pkg;
/
show errors
