set autoprint off;
set recsep off;

clear breaks;
clear columns;
ttitle off;

set linesize 80;
set pagesize 30;
set null     <NULL>;

column results_out format 9999999999 heading 'Messages Sent';

variable results_out number;

prompt
prompt This script adds records to EXI_JMS_MESSAGES which has the effect of re-sending
prompt the message to EXISS.
prompt 
prompt The user is required to enter the item_id of the record on EXI_ITEM_OUTBOUND
prompt to be sent and an optional target.  If no target is supplied, 'EXISS' is used
prompt as the default
prompt

ttitle center 'Re-send Message For Item ID: ' itemid skip 3 -
left 'Target: ' target skip 2;

accept itemid number prompt 'Item ID: '
accept target char   prompt 'Target : '

exec :results_out := exi_application_support_pkg.resend_message_to_exiss(&itemid, '&target');

print results_out;

clear columns;
clear breaks;
