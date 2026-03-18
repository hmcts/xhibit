--call fix_deleted_crest_cases

BEGIN
 dbms_output.enable(1000000);
 data_mig.fix_deleted_crest_cases(&1);
END;
/
