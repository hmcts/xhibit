set autoprint off;

clear breaks;
clear columns;
ttitle off;

set linesize 160;
set pagesize 30;

break on request_id nodup -
      on source_identifier nodup -
      on destination_identifier nodup -
      on exec_mode nodup -
      on request_timestamp nodup -
      on send_attempts nodup -
      on internal_code nodup;

column request_id             format 9999999999 heading 'Request ID';
column source_identifier      format a20        heading 'Source ID';
column destination_identifier format a15        heading 'Destination ID';
column exec_mode              format a6         heading 'Exec|Mode';
column request_timestamp      format a20        heading 'Request Timestamp';
column send_attempts          format 99999      heading 'Times|Sent';
column failure_code           format a4         heading 'Fail|Code';
column failure_text           format a40        heading 'Fail Text';
column failure_timestamp      format a20        heading 'Failure Timestamp';
column internal_code          format a6         heading 'Status';

variable results_out refcursor;

prompt
prompt This report shows all outbound failures between 2 dates
prompt Note:
prompt - the dates are optional.  Where either is not specified, it will not be used to bound the query
prompt

accept start_date  char prompt 'Start Date (DD-MON-YYYY): '
accept end_date    char prompt 'End Date   (DD-MON-YYYY): '

ttitle center 'Outbound Failures' skip 2 -
left 'Start Date: ' &start_date   skip 1 -
left '  End Date: ' &end_date     skip 2;

exec gdg_application_support_pkg.get_outbound_failures(:results_out, '&start_date', '&end_date');

print results_out;

clear columns;
clear breaks;
