set autoprint off;
set recsep off;

clear breaks;
clear columns;
ttitle off;

set linesize 80;
set pagesize 30;
set null     <NULL>;

column results_out format 9999999999 heading 'Messages Removed';

variable results_out number;

prompt
prompt This script removes a record from GDG_OUTBOUND_MESSAGES and related tables 
prompt for a given request ID
prompt

ttitle center 'Remove Outbound Records For Request ID: ' requestid skip 3;

accept requestid number prompt 'Request ID: '

exec :results_out := gdg_application_support_pkg.remove_outbound_record_by_id(&requestid);

print results_out;

clear columns;
clear breaks;
