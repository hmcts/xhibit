
---- Convicted of a sexual offence
insert into xhb_court_log_event_desc 
(EVENT_DESC_ID, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_DISPLAY, LINKED_CASE_TEXT, EVENT_DESCRIPTION, VERSION, LAST_UPDATED_BY, EVENT_TYPE, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, PUBLIC_NOTICE, SHORT_DESCRIPTION)
values 
(4022, 0, 1, 0, 1, 1, 0, 0, 0, 'LC_TEXT_', 'Convicted of a sexual offence', 1, 'XHIBIT', 21600, 'XHIBIT', to_date('18-05-2011 12:00:00', 'dd-mm-yyyy hh24:mi:ss'), sysdate, 0, 'Conviction_Of_A_Sexual_Offence');


insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(14,4022);

commit;



commit;






