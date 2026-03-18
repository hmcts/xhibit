--Run the sctipt for update the xhb_populate_ref_system_code
BEGIN
 xhb_populate_ref_system_code(p_court_id => &1);
END;
/