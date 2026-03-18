/** 
  * CGI crest to xhibit program
  *
  * MODULE      : xhb_reset_case_sequences
  *
  * DESCRIPTION : JIRA tickets ctx-113 and its sub tasks
  *
  *               Procedure                   Purpose
  *               =========                   =======
  *               xhb_reset_case_sequences    The sequence numbers for the case number generation will be re-set to 0001 (7000 for Trial Indictment) on the first
  *                                           day of each year.  The DMBS_SCHEDULER API will be used to manage the job schedule.
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 11/04/2018    C Cash          1.0         First Version
  *
  **/

create or replace PROCEDURE xhb_reset_case_type_sequences
AS 
 
BEGIN
   UPDATE xhb_case_number_seq_generator
   SET current_sequence = 7000
   WHERE case_type = 'TRIAL_INDICTMENT';
 
   UPDATE xhb_case_number_seq_generator
   SET current_sequence = 0
   WHERE case_type != 'TRIAL_INDICTMENT';
 
END xhb_reset_case_type_sequences;
/