set autoprint off;

clear breaks;
clear columns;
ttitle off;

set linesize 120;
set pagesize 999;
set long     100000;
set null     <NULL>;

column item_id       noprint;
column date_created  noprint new_value new_date_created;
column message       format a120 heading 'Message Payload';

variable results_out refcursor;

prompt
prompt This report shows details of an inbound record for a given item ID
prompt

accept itemid number prompt 'Item ID: '

ttitle center 'Details For Inbound Record: ' &itemid skip 3 -
left 'Date Created: ' new_date_created               skip 2;

exec exi_application_support_pkg.get_inbound_by_item_id(:results_out, &itemid);

print results_out;

clear columns;
clear breaks;
