--call fix_xhb_address_postcode

BEGIN
 dbms_output.enable(1000000);
 data_mig.fix_xhb_address_postcode(&1);
END;
/
