/** 
  * CGI crest to xhibit program
  *
  * MODULE      : xhb_court_log_event_desc_insert
  *
  * DESCRIPTION : JIRA tickets ctx-1949 
  *
  * PURPOSE : CTX-1949 requires to insert this event type reord for End Bench Warrant into XHB_COURT_LOG_EVENT_DESC table. But it is required to run  xhb_reset_court_log_event_desc_sequence.SQL script before this insert SQL to update the sequence XHB_COURT_LOG_EVENT_DESC_SEQ. 
  *               
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 08/05/2018    Deepak Rath          1.0         First Version
  *
  **/
Insert into XHB_COURT_LOG_EVENT_DESC (EVENT_DESC_ID,FLAGGED_EVENT,EDITABLE,SEND_TO_MERCATOR,UPDATE_LINKED_CASES,PUBLISH_TO_SUBSCRIBERS,CLEAR_PUBLIC_DISPLAYS,E_INFORM,PUBLIC_DISPLAY,LINKED_CASE_TEXT,EVENT_DESCRIPTION,VERSION,LAST_UPDATED_BY,EVENT_TYPE,CREATED_BY,CREATION_DATE,LAST_UPDATE_DATE,PUBLIC_NOTICE,SHORT_DESCRIPTION)
values (XHB_COURT_LOG_EVENT_DESC_SEQ.NEXTVAL,0,1,0,1,1,0,0,0,'LC_TEXT_','Bench Warrant Options',1,
'Xhibit',20101,'Xhibit',to_date(SYSDATE,'DD-MON-RR'),to_date(SYSDATE,'DD-MON-RR'),0,'End Bench_Warrant');

commit;