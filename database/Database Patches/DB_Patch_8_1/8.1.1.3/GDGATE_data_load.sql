/*  Change 2 on Wiki   */

update gdg_config_properties
set property_value  = '9'
where property_code = 'SERV_AVAIL_BATCH_SIZE';

update gdg_config_properties
set property_value  = '18'
where property_code = 'MAX_MSG_CONSEC_FAIL_COUNT';

update gdg_config_properties
set property_value  = '9000'
where property_code = 'WEB_SERVICE_CONNECTION_TIMEOUT';

update gdg_config_properties
set property_value  = '9000'
where property_code = 'WEB_SERVICE_READ_TIMEOUT';

commit;

/*  Change 3(1) on Wiki   */

delete from gdg_config_properties where property_code = 'SERV_UNAVAIL_MSG_COUNT';

commit;





commit;




