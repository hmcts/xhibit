/** 
  * CGI crest to xhibit program
  *
  * MODULE      : xhb_court_log_event_desc_insert
  *
  * DESCRIPTION : JIRA tickets XLC-19
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 27/02/2020    N Walters          1.0         First Version
  *
  **/
Insert into XHB_COURT_LOG_EVENT_DESC (EVENT_DESC_ID,FLAGGED_EVENT,EDITABLE,SEND_TO_MERCATOR,UPDATE_LINKED_CASES,PUBLISH_TO_SUBSCRIBERS,CLEAR_PUBLIC_DISPLAYS,E_INFORM,LINKED_CASE_TEXT,EVENT_DESCRIPTION,VERSION,LAST_UPDATED_BY,EVENT_TYPE,CREATED_BY,CREATION_DATE,LAST_UPDATE_DATE,PUBLIC_NOTICE,SHORT_DESCRIPTION, PUBLIC_DISPLAY)
values (XHB_COURT_LOG_EVENT_DESC_SEQ.NEXTVAL,0,1,0,1,1,0,0,'LC_TEXT_','Trial - Sentence Record For Broadcast',1,
'Xhibit',20937,'Xhibit',to_date(SYSDATE,'DD-MON-YY'),to_date(SYSDATE,'DD-MON-YY'),0,'Trial_Sentence_Record_For_Broadcast',0);

commit;