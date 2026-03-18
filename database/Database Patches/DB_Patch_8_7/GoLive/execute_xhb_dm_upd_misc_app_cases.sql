--call XHB_DM_UPD_MISC_APP_CASES

BEGIN
 dbms_output.enable(1000000);
 dbms_output.put_line('Parameter 1 is p_xhibit_court_id...');
 xhibit.xhb_dm_upd_misc_app_cases(&1);
END;
/
