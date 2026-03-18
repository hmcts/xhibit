/** 
  * CGI crest to xhibit program
  *
  * MODULE      : xhb_reset_court_log_event_desc_sequence
  *
  * DESCRIPTION : JIRA tickets ctx-1949 
  *
  * PURPOSE : CTX-1949 requires to insert event type reord for End Bench Warrant into XHB_COURT_LOG_EVENT_DESC as per xhb_court_log_event_desc_insert.sql
  *           But it is found during defect fixing that the sequence XHB_COURT_LOG_EVENT_DESC_SEQ which needs to be used as EVENT_DESC_ID value in the insert statement is not in sync with number of records in the table.So this PL/SQL block needs to run before the insert SQL to reset the value of XHB_COURT_LOG_EVENT_DESC_SEQ to the maximum value of EVENT_DESC_ID in the table XHB_COURT_LOG_EVENT_DESC + 1 to make sure the future inserts use the correct sequence number.   
  *               
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 08/05/2018    Deepak Rath          1.0         First Version
  *
  **/
  
SET SERVEROUTPUT ON SIZE 1000000
 DECLARE 
 new_sequence_id NUMBER;
 old_sequence_id NUMBER;
 temp_seq NUMBER; 
 temp_diff NUMBER; 
 
 BEGIN
 SELECT NVL(MAX(EVENT_DESC_ID),0) +1 INTO new_sequence_id from XHB_COURT_LOG_EVENT_DESC; 
 SELECT  XHB_COURT_LOG_EVENT_DESC_SEQ.NEXTVAL INTO old_sequence_id  from DUAL ;
 EXECUTE IMMEDIATE 'ALTER SEQUENCE XHB_COURT_LOG_EVENT_DESC_SEQ INCREMENT BY ' ||  (new_sequence_id - old_sequence_id);
 SELECT XHB_COURT_LOG_EVENT_DESC_SEQ.NEXTVAL INTO temp_seq from dual;
 SELECT new_sequence_id - old_sequence_id INTO temp_diff from dual;
 EXECUTE IMMEDIATE 'ALTER SEQUENCE XHB_COURT_LOG_EVENT_DESC_SEQ INCREMENT BY 1 ';
 DBMS_OUTPUT.put_line('Executed "ALTER SEQUENCE XHB_COURT_LOG_EVENT_DESC_SEQ INCREMENTED BY ' || temp_diff || '"');
 
 END;
/