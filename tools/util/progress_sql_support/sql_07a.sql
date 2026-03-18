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
prompt This script removes from GDG_OUTBOUND_MESSAGES and related tables any 
prompt successfully transmitted messages from that are > 3 days old
prompt

ttitle center 'Remove Outbound Records' skip 3;

exec :results_out := gdg_application_support_pkg.remove_outbound_records;

print results_out;
       
clear columns;
clear breaks;
