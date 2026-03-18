set autoprint off;

clear breaks;
clear columns;
ttitle off;

set linesize 40;
set pagesize 60;

break on property_value page on date_created nodup;

column item_id        format 9999999999999999 heading 'Item ID';
column date_created   format a20 heading 'Date/Time Created';
column property_value noprint new_value new_property_value;

variable results_out refcursor;

prompt
prompt This report shows inbound EXCEPTIONs between 2 user selected dates
prompt - the dates are optional.  Where either is not specified, it will not be used to bound the query
prompt

accept start_date char prompt 'Start Date (DD-MON-YYYY): '
accept end_date   char prompt '  End Date (DD-MON-YYYY): '

ttitle center 'Inbound EXCEPTIONs'       skip 2 -
left 'Message Type: ' new_property_value skip 1 -
left '  Start Date: ' &start_date        skip 1 -
left '    End Date: ' &end_date          skip 2;

exec exi_application_support_pkg.get_inbound_exceptions(:results_out, '&start_date', '&end_date');

print results_out;

clear columns;
clear breaks;
