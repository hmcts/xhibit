
---- Remove Defendant On Count
insert into xhb_court_log_event_desc 
(EVENT_DESC_ID, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_DISPLAY, LINKED_CASE_TEXT, EVENT_DESCRIPTION, VERSION, LAST_UPDATED_BY, EVENT_TYPE, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, PUBLIC_NOTICE, SHORT_DESCRIPTION)
values 
(3000, 0, 1, 0, 1, 1, 0, 0, 0, 'LC_TEXT_', 'Remove Defendant on Count', 1, 'XHIBIT', 40475, 'XHIBIT', to_date('19-01-2010 15:33:37', 'dd-mm-yyyy hh24:mi:ss'), to_date('19-01-2010 15:33:37', 'dd-mm-yyyy hh24:mi:ss'), 0, 'Remove_Defendant_on_Count');

---- Renumber Counts
insert into xhb_court_log_event_desc 
(EVENT_DESC_ID, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_DISPLAY, LINKED_CASE_TEXT, EVENT_DESCRIPTION, 
VERSION, 
LAST_UPDATED_BY, 
EVENT_TYPE, 
CREATED_BY, 
CREATION_DATE, 
LAST_UPDATE_DATE, 
PUBLIC_NOTICE, 
SHORT_DESCRIPTION)
values 
(3001, 
0, 
1, 
0, 
1, 
1, 
0, 
0, 
0, 
'LC_TEXT_',
 'Renumber_Counts', 
1, 
'XHIBIT',
 40476,
'XHIBIT',
to_date('19-01-2010 15:33:37', 
'dd-mm-yyyy hh24:mi:ss'), 
to_date('19-01-2010 15:33:37', 
'dd-mm-yyyy hh24:mi:ss'), 
0, 
'Renumber_Counts');

---- Add Counts To Joinders

insert into xhb_court_log_event_desc 
(EVENT_DESC_ID, 
FLAGGED_EVENT, 
EDITABLE, 
SEND_TO_MERCATOR, 
UPDATE_LINKED_CASES, 
PUBLISH_TO_SUBSCRIBERS, 
CLEAR_PUBLIC_DISPLAYS, 
E_INFORM, 
PUBLIC_DISPLAY, 
LINKED_CASE_TEXT, 
EVENT_DESCRIPTION, 
VERSION, 
LAST_UPDATED_BY, 
EVENT_TYPE, 
CREATED_BY, 
CREATION_DATE, 
LAST_UPDATE_DATE, 
PUBLIC_NOTICE, 
SHORT_DESCRIPTION)
values 
(3002, 
0, 
1, 
0, 
1, 
1, 
0, 
0, 
0, 
'LC_TEXT_',
 'Add_Counts_Joinders', 
1, 
'XHIBIT',
 40477,
'XHIBIT',
to_date('19-01-2010 15:33:37', 
'dd-mm-yyyy hh24:mi:ss'), 
to_date('19-01-2010 15:33:37', 
'dd-mm-yyyy hh24:mi:ss'), 
0, 
'Add_Counts_Joinders');

insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(14,3000);


insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(14,3001);

insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(14,3002);

commit;






