set autoprint off;
set recsep off;

clear breaks;
clear columns;
ttitle off;

set linesize 150;
set pagesize 40;
set long     100000;
set null     <NULL>;

break on item_id page on date_created nodup;

column item_id        format  9999999999 heading 'Item ID';
column date_created   format  a20        heading 'Date Created';
column property_name  format  a40        heading 'Property Name';
column property_value format  a60        heading 'Property Value';

accept corr_id     char prompt 'Corellation ID          : '
accept start_date  char prompt 'Start Date (DD-MON-YYYY): '
accept end_date    char prompt 'End Date   (DD-MON-YYYY): '

ttitle center 'Inbound Properties For Corellation ID: ' &corr_id skip 2 -
left 'Start Date: ' &start_date                                  skip 1 -
left '  End Date: ' &end_date                                    skip 2;

exec exi_application_support_pkg.get_inbound_props_by_corr_id(:results_out, '&corr_id', '&start_date', '&end_date');

print results_out;

clear columns;
clear breaks;
