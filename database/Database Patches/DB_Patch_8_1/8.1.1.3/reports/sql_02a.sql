set autoprint off;
set recsep off;

clear breaks;
clear columns;
ttitle off;

set linesize 160;
set pagesize 30;
set long     100000;
set null     <NULL>;

ttitle center 'All Tracking Statuses' skip 3;

column internal_code format a25 heading 'Status';
column internal_name format a85 heading 'Description';

variable results_out refcursor;

prompt
prompt This report shows all outbound records that have a given tracking status between 2 dates
prompt Note:
prompt - the dates are optional.  Where either is not specified, it will not be used to bound the query
prompt - the status code accepts % wild cards and IS NOT case sensitive
prompt

exec exi_application_support_pkg.get_all_tracking_statuses(:results_out);

print results_out;

break on internal_code page on created nodup;

column item_id        format  9999999999 heading 'Item ID';
column identifier     format  a30        heading 'Identifier';
column item_type      format  a30        heading 'Type';
column crest_court_id format  a5         heading 'CREST|Court|ID';
column description    format  a30        heading 'Description';
column created        format  a20        heading 'Item Created';
column expires        format  a20        heading 'Item Expires';
column internal_code  noprint new_value new_internal_code;

accept status_code char prompt 'Tracking Status         : '
accept start_date  char prompt 'Start Date (DD-MON-YYYY): '
accept end_date    char prompt 'End Date   (DD-MON-YYYY): '

ttitle center 'Outbound Items With Status: ' &status_code skip 2 -
left 'Tracking Status: ' new_internal_code                skip 1 -
left '     Start Date: ' &start_date                      skip 1 -
left '       End Date: ' &end_date                        skip 2;

exec exi_application_support_pkg.get_items_by_tracking_status(:results_out, '&status_code', '&start_date', '&end_date');

print results_out;

clear columns;
clear breaks;
