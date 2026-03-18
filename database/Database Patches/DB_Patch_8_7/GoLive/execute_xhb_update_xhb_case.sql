--Run the sctipt for update the xhb_case defaults
BEGIN
 xhb_update_xhb_case(p_court_id => &1);
END;
/