--Run the sctipt for update the xhb_update_xhb_defendant defaults
BEGIN
 xhb_update_xhb_defendant(p_court_id => &1);
END;
/