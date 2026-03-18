/*
 * Filename:    GDGATE_PREPROD_DATALOAD_UPDATE.sql
 *
 *
 */
/*
 * HISTORY
 * =======
 * DATE     	WHO 	CHANGE ID   	COMMENT
 * ----         ---     ---------       -------
 *
 * 08/12/2006 	Kadu Shah 		Update to the gdg_config_properties for production environment only
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'GDGATE_PREPROD_DATALOAD_UPDATE_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/

set term on
spool &&spool_filename

update gdg_config_properties 
	set property_value = '10.30.10.169'
	where property_code = 'SCJSE_WEB_IP_ADDR';


update gdg_config_properties 
	set property_value = '7001'
	where property_code = 'SCJSE_WEB_PORT';


update gdg_config_properties 
	set property_value = 'stubdelivery/stubservices/StubDelivery'
	where property_code = 'SCJSE_WEB_NAME';


commit;

spool off
