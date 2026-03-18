
---- Witness Read (5.3.4 of Func Spec)
insert into xhb_court_log_event_desc 
(EVENT_DESC_ID, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_DISPLAY, LINKED_CASE_TEXT, EVENT_DESCRIPTION, VERSION, LAST_UPDATED_BY, EVENT_TYPE, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, PUBLIC_NOTICE, SHORT_DESCRIPTION)
values 
(4050, 0, 1, 0, 1, 1, 0, 0, 0, 'LC_TEXT_', 'Trial - Witness Read', 1, 'XHIBIT', 20935, 'XHIBIT', to_date('01-07-2014 12:00:00', 'dd-mm-yyyy hh24:mi:ss'), sysdate, 0, 'Witness_Read');

INSERT INTO xhb_court_log_category_desc(category_type,category_description, created_by,last_updated_by) VALUES(31,'Witness_Read','Xhibit','Xhibit');

insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(31,4050);

commit;






