/*
 * Filename:    GDGATE_PROD_DATALOAD_UPDATE.sql

 * Update this with info on how to get the details of remote web service which are mandatory and the proxy
 * if required.  Will be either 3(without proxy as in pre-prod) or 6 properties if proxy is required.
 * The 3 mandatory web service properties are :
  update gdg_config_properties 
	set property_value = 'XXXXXX'
	where property_code = 'SCJSE_WEB_IP_ADDR';


update gdg_config_properties 
	set property_value = 'XXXX'
	where property_code = 'SCJSE_WEB_PORT';


update gdg_config_properties 
	set property_value = 'XXXXXX'
	where property_code = 'SCJSE_WEB_NAME';

update gdg_config_properties
	set property_value = 'XXXXXX'
  	where property_code = 'PROXY_HOST';

update gdg_config_properties
	set property_value = 'XXXXXX'
  	where property_code = 'PROXY_PORT';

update gdg_config_properties
	set property_value = 'XXXXXX'
  	where property_code = 'PROXY_USER';
 *
 *  The PROXY_PASSWORD is set in Weblogic -  Craig Herbert and Rob Abrahamiam
 *
 *
 */
/*
 * HISTORY
 * =======
 * DATE     	WHO 	CHANGE ID   	COMMENT
 * ----         ---     ---------       -------
 *
 * 30/11/2006 	Kadu Shah 		Update to the gdg_config_properties for production environment only
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'GDGATE_PROD_DATALOAD_UPDATE_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/

set term on
spool &&spool_filename

update gdg_config_properties 
	set property_value = 'x2proxy'
	where property_code = 'PROXY_USER';


spool off
