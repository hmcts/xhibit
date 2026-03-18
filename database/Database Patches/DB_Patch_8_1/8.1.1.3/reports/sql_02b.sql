set autoprint off;
set recsep off;

clear breaks;
clear columns;
ttitle off;

set linesize 160;
set pagesize 30;
set long     100000;
set null     <NULL>;

variable results_out refcursor;

prompt
prompt This report shows all outbound records that failed to be sent to the SCJSE between 2 dates
prompt Note:
prompt - the dates are optional.  Where either is not specified, it will not be used to bound the query
prompt

break on date_created nodup;

column item_id        format  9999999999 heading 'Item ID';
column identifier     format  a30        heading 'Identifier';
column item_type      format  a30        heading 'Type';
column crest_court_id format  a5         heading 'CREST|Court|ID';
column description    format  a30        heading 'Description';
column created        format  a20        heading 'Item Created';
column expires        format  a20        heading 'Item Expires';

accept start_date  char prompt 'Start Date (DD-MON-YYYY): '
accept end_date    char prompt 'End Date   (DD-MON-YYYY): '

ttitle center 'Outbound Failures' skip 2 -
left 'Start Date: ' &start_date   skip 1 -
left '  End Date: ' &end_date     skip 2;

exec exi_application_support_pkg.get_outbound_failures(:results_out, '&start_date', '&end_date');

print results_out;

clear columns;
clear breaks;
