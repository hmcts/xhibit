/*
 * Filename:    GDGATE_PROD_DATALOAD_UPDATE.sql
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
