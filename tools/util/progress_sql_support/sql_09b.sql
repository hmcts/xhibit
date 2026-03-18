set autoprint off;
set recsep off;

clear breaks;
clear columns;
ttitle off;

set linesize 40;
set pagesize 30;
set long     100000;
set null     <NULL>;

prompt
prompt This report shows counts of tracking status codes between 2 dates
prompt Note:
prompt - the dates are optional.  Where either is not specificed, the current date is used as a default

break on report;

column internal_code   format a25     heading 'Tracking Status Code';
column count_by_status format 9999999 heading 'Total';

variable results_out refcursor;

compute sum label 'Report Total' of count_by_status on report;

accept p_start_date  char prompt 'Start Date (DD-MON-YYYY): '
accept p_end_date    char prompt 'End Date   (DD-MON-YYYY): '

ttitle center 'Tracking Status Counts' skip 2 -
left 'Start Date: ' &p_start_date      skip 1 -
left '  End Date: ' &p_end_date        skip 2;

exec exi_application_support_pkg.get_tracking_counts(:results_out, '&p_start_date', '&p_end_date');

print results_out;

clear columns;
clear breaks;
