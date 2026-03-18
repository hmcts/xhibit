
---- Disqualified from Working With Children (4.2)
insert into xhb_court_log_event_desc 
(EVENT_DESC_ID, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_DISPLAY, LINKED_CASE_TEXT, EVENT_DESCRIPTION, VERSION, LAST_UPDATED_BY, EVENT_TYPE, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, PUBLIC_NOTICE, SHORT_DESCRIPTION)
values 
(4020, 0, 1, 0, 1, 1, 0, 0, 0, 'LC_TEXT_', 'Disqualified from Working With Children Options', 1, 'XHIBIT', 21400, 'XHIBIT', to_date('26-04-2011 12:00:00', 'dd-mm-yyyy hh24:mi:ss'), sysdate, 0, 'Disqualified_from_Working_With_Children');


insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(14,4020);

commit;


---- Electronic Monitoring (4.3)
insert into xhb_court_log_event_desc 
(EVENT_DESC_ID, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_DISPLAY, LINKED_CASE_TEXT, EVENT_DESCRIPTION, VERSION, LAST_UPDATED_BY, EVENT_TYPE, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, PUBLIC_NOTICE, SHORT_DESCRIPTION)
values 
(4021, 0, 1, 0, 1, 1, 0, 0, 0, 'LC_TEXT_', 'Electronic Monitoring Options', 1, 'XHIBIT', 21500, 'XHIBIT', to_date('17-05-2011 12:00:00', 'dd-mm-yyyy hh24:mi:ss'), sysdate, 0, 'Electronic_Monitoring');


insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(14,4021);


---- Bail Conditions Ceased (4.6) --- nothing to be added!!


commit;






