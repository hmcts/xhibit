create or replace PACKAGE xhb_orders_pkg AS

FUNCTION get_ref_courts_by_court_Id (
                COURT_ID_IN IN XHB_REF_COURT.COURT_ID%TYPE) RETURN SYS_REFCURSOR;

FUNCTION get_appeal_result_order( p_case_id IN XHB_CASE.CASE_ID%TYPE) RETURN CLOB;

FUNCTION get_disposal_lines(p_disposal_id XHB_DISPOSAL2.DISPOSAL2_ID%TYPE) RETURN VARCHAR2;

FUNCTION format_disposal_line(p_data VARCHAR2, p_val XHB_REF_DISPOSAL_LINE.VALIDATION%TYPE) RETURN VARCHAR2;

END xhb_orders_pkg;

/
show errors