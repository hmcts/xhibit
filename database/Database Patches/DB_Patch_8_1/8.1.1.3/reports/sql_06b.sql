set autoprint off;

clear breaks;
clear columns;
ttitle off;

set linesize 120;
set pagesize 30;
set long     100000;
set null     <NULL>;

column request_id             noprint new_value new_request_id;
column source_identifier      noprint new_value new_source_identifier;
column destination_identifier noprint new_value new_destination_identifier;
column exec_mode              noprint new_value new_exec_mode;
column request_timestamp      noprint new_value new_request_timestamp;
column internal_code          noprint new_value new_internal_code;
column send_attempts          noprint new_value new_send_attempts;
column clob_data format a120  heading 'Message Payload';

variable results_out refcursor;

prompt
prompt This report shows details for an outbound record for a given request ID
prompt

accept requestid number prompt 'Request ID: '

ttitle center 'Details For Outbound Record: ' &requestid skip 3 -
left '        Source ID: ' new_source_identifier         skip 1 -
left '   Destination ID: ' new_destination_identifier    skip 1 -
left '        Exec Mode: ' new_exec_mode                 skip 1 -
left 'Request Timestamp: ' new_request_timestamp         skip 1 -
left '      Status Code: ' new_internal_code             skip 1 -
left '    Send Attempts: ' new_send_attempts             skip 2;

exec gdg_application_support_pkg.get_outbound_by_request_id(:results_out, &requestid);

print results_out;

clear columns;
clear breaks;
