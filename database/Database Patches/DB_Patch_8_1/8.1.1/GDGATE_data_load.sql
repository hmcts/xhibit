insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SERV_AVAIL_MSG_SEND_FREQ', 'Service Available Message Send Frequency', 0);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SERV_UNAVAIL_MSG_SEND_FREQ', 'Service Unavailable Message Send Frequency', 10);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SERV_AVAIL_MSG_RETRY_FREQ', 'Service Available Message Retry Frequency', 5);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SERV_UNAVAIL_MSG_RETRY_FREQ', 'Service Unavailable Message Retry Frequency', 10);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SERV_AVAIL_BATCH_SIZE', 'Service Available Batch Size', 32);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SERV_UNAVAIL_BATCH_SIZE', 'Service Unavailable Batch Size', 1);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SUCCESSFUL_STORAGE_TIME', 'Successful Storage Time', 3);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('UNSUCCESSFUL_STORAGE_TIME', 'Unsuccessful Storage Time', 21);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('MAX_MSG_ATTEMPTS', 'Max Message Attempts', 3);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('MAX_MSG_CONSEC_FAIL_COUNT', 'Max Message Consecutive Failure Count', 20);
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SCJSE_WEB_IP_ADDR', 'SCJSE Web Service IP Address', '123.123.1.12');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SCJSE_WEB_PORT', 'SCJSE Web Service Port', '30');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SCJSE_WEB_NAME', 'SCJSE Web Service Name', 'name');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SCJSE_LOCATION_ID', 'SCJSE Location ID', 'Z00CJSE');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('XHB_LOCATION_ID', 'XHIBIT Location ID', 'C00CourtServicesHub');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('MSG_MALFORMED_XML_FAIL_LIMIT', 'Message Malformed XML Failure Limit', '2000');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('SCJSE_MSG_IMPL_CLASS', 'SCJSE Messaging Implementation Class', 'GD');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('MSG_CONSEC_FAIL_COUNT', 'Message Consecutive Failure Count', '0');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('MSG_MALFORMED_XML_FAIL_COUNT', 'Message Malformed XML Failure Count', '0');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('MAX_LOCK_ATTEMPTS', 'Maximum attempts to lock a set of rows', '3');
insert into gdg_config_properties (  PROPERTY_CODE, PROPERTY_NAME, PROPERTY_VALUE) VALUES  ('CONFIG_PROPERTIES_REFRESH_TIME', 'Config properties Java cache refresh time in minutes', '30');


INSERT INTO gdg_table_timestamp   (NAME, last_updated ) VALUES ('GDG_CONFIG_PROPERTIES', SYSDATE );

INSERT INTO GDG_OUTBOUND_STATUSES ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ('FATAL', 'FATAL');
INSERT INTO GDG_OUTBOUND_STATUSES ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ('ERROR', 'ERROR');
INSERT INTO GDG_OUTBOUND_STATUSES ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ('SUCCESS', 'SUCCESS');
INSERT INTO GDG_OUTBOUND_STATUSES ( INTERNAL_CODE, INTERNAL_NAME ) VALUES ('NEW', 'NEW');

/*  Change 37 on Wiki   */

insert into gdg_config_properties (property_code, property_name, property_value, property_timestamp)
values ('WEB_SERVICE_CONNECTION_TIMEOUT', 'Web Service Connection Timeout', 0, sysdate);

insert into gdg_config_properties (property_code, property_name, property_value, property_timestamp)
values ('WEB_SERVICE_READ_TIMEOUT', 'Web Service Read Timeout', 0, sysdate);

COMMIT;


/*  Change 39 on Wiki   */

insert  into gdg_config_properties (property_code, property_name, property_value, property_timestamp)
select 'SERV_CURRENT_DELAY', 'Service Current Delay', 10, sysdate
from    dual
where not exists (
    select 1 
    from   gdg_config_properties
    where  property_code = 'SERV_CURRENT_DELAY'
);

insert  into gdg_config_properties (property_code, property_name, property_value, property_timestamp)
select 'SERV_UNAVAIL_MSG_COUNT', 'The number of messages that have been processed during the time the service has been unavailable', 0, sysdate
from    dual
where not exists (
    select 1 
    from   gdg_config_properties
    where  property_code = 'SERV_UNAVAIL_MSG_COUNT'
);

update gdg_config_properties
set    property_value = '12'
where  property_code  = 'SERV_AVAIL_BATCH_SIZE';

commit;


/*  Change 42 on Wiki   */

insert  into gdg_config_properties (property_code, property_name, property_value, property_timestamp)
select 'PROXY_HOST', 'The host name of the web service proxy for SCJSE', 'csa00013', sysdate
from    dual
where not exists (
    select 1 
    from   gdg_config_properties
    where  property_code = 'PROXY_HOST'
);

insert  into gdg_config_properties (property_code, property_name, property_value, property_timestamp)
select 'PROXY_PORT', 'The port number of the web service proxy for SCJSE', '8080', sysdate
from    dual
where not exists (
    select 1 
    from   gdg_config_properties
    where  property_code = 'PROXY_PORT'
);

insert  into gdg_config_properties (property_code, property_name, property_value, property_timestamp)
select 'PROXY_USER', 'The username for the web service proxy for SCJSE', 'mercator', sysdate
from    dual
where not exists (
    select 1 
    from   gdg_config_properties
    where  property_code = 'PROXY_USER'
);

commit;


/*  Change 3 on Wiki for 8.1.1  */

update GDG_CONFIG_PROPERTIES set property_value=10 where property_code='SERV_AVAIL_BATCH_SIZE';

commit;