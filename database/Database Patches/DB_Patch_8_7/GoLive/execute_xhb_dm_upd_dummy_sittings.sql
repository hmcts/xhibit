--call xhb_dm_dummy_sittings

BEGIN
 dbms_output.enable(1000000);
 dbms_output.put_line('Parameter 1 is p_xhibit_court_id...');
 xhb_dm_upd_dummy_sittings(&1);
END;
/
