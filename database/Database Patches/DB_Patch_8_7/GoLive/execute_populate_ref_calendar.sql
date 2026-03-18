--Run the sctipt for update the xhb_populate_ref_calendar
BEGIN
 xhb_populate_ref_calendar(p_court_id => &1);
END;
/