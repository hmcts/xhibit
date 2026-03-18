--call XHB_DM_UPD_DUP_CPA

BEGIN
 dbms_output.enable(1000000);
 dbms_output.put_line('Parameter 1 is p_xhibit_court_id...');
 XHB_DM_UPD_DUP_CPA(&1);
END;
/
