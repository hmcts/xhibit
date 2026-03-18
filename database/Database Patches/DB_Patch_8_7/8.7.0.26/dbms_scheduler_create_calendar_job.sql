/** 
  * CGI crest to xhibit program
  *
  * MODULE      : XHB_POPULATE_REF_CALENDAR
  *
  * DESCRIPTION : JIRA tickets ctx-1978 and its sub tasks
  *
  * Procedure                  Purpose
  * =========                  =======
  * XHB_POPULATE_REF_CALENDAR  Populate the xhb_ref_calendar table at the start of each year with 3 years worth of dates
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 21/05/2018    C Cash          1.0         First Version
  *
  **/
  BEGIN
   DBMS_SCHEDULER.CREATE_JOB (job_name        => 'PopulateRefCalendar'
                             ,job_type        => 'stored_procedure'
                             ,job_action      => 'XHIBIT.XHB_POPULATE_REF_CALENDAR'
                             ,start_date      => '01-JAN-19 00.01.00.00'
                             ,repeat_interval => 'FREQ=YEARLY;BYMONTH=JAN;BYMONTHDAY=01;BYHOUR=00;BYMINUTE=01;BYSECOND=0'
                             ,enabled         => true
                             ,comments        => 'Populate the ref calendar table with 36 month of data on 1st day of each year'
                             );
  END;
  /