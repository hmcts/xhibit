CREATE OR REPLACE PACKAGE xhb_pdda_pkg AS

  ------------------------------------------------------------------------------
  -- Return just ONE overall record (the latest across all courts that qualifies).
  -- If no qualifying record exists, the result set will be empty.
  ------------------------------------------------------------------------------
  FUNCTION get_iwp_next_eligible_record
  (
    p_for_date IN DATE DEFAULT SYSDATE
  )
    RETURN SYS_REFCURSOR;


  ------------------------------------------------------------------------------
  -- Return one record per COURT_ID (0 or 1 per court).
  -- If a court has no qualifying record, nothing is returned for that court.
  ------------------------------------------------------------------------------
  FUNCTION get_iwp_all_eligible_courts
  (
    p_for_date IN DATE DEFAULT SYSDATE
  )
    RETURN SYS_REFCURSOR;

END xhb_pdda_pkg;
/
