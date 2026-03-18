/** 
  * CGI crest to xhibit program
  *
  * MODULE      : XHB_RESET_CASE_TYPE_SEQUENCES
  *
  * DESCRIPTION : JIRA tickets ctx-113 and its sub tasks
  *
  *               Procedure                     Purpose
  *               =========                     =======
  *               XHB_RESET_CASE_TYPE_SEQUENCES The sequence numbers for the case number generation will be re-set to 0001 (7000 for Trial Indictment) on the first
  *                                             day of each year.  The DMBS_SCHEDULER API will be used to manage the job schedule.
  *                                             This call will schedule a job to call XHB_SET_CASE_NUMBER_SEQ_ALL procedure
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 11/04/2018    C Cash          1.0         First Version
  * 20/12/2018    J Riley         1.1         CTX-3421 Change to call XHB_SET_CASE_NUMBER_SEQ_ALL, start year to be 2020.
  *
  **/
  BEGIN
   DBMS_SCHEDULER.CREATE_JOB (job_name        => 'ResetCaseTypeSeqNumbers'
                            , job_type        => 'stored_procedure'
                            , job_action      => 'XHIBIT.XHB_SET_CASE_NUMBER_SEQ_ALL'
                            , start_date      => '01-JAN-20 00.01.00.00'
                            , repeat_interval => 'FREQ=YEARLY;BYMONTH=JAN;BYMONTHDAY=01;BYHOUR=00;BYMINUTE=01;BYSECOND=0'
                            , enabled         => true
                            , comments        => 'Reset database sequences on 1st day of each year'
                             );
  END;
/