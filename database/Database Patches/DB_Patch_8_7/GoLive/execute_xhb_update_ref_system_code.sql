-- Run the script for update the xhb_update_ref_system_code
BEGIN
    xhb_update_ref_system_code(p_court_id => &1);
END;
/