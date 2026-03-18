
-- For the 'link cases added via add hearing' functionality

alter table XHB_SCHEDULED_HEARING add ADD_HEARING_USED varchar2(1);
alter table AUD_SCHEDULED_HEARING add ADD_HEARING_USED varchar2(1);
@xhb_scheduled_hearing_bur_tr.sql



-- For the remove cases/names from the public display

alter table XHB_CASE add PUBLIC_DISPLAY_HIDE varchar2(1);
alter table AUD_CASE add PUBLIC_DISPLAY_HIDE varchar2(1);
@xhb_case_bur_tr.sql

alter table XHB_DEFENDANT_ON_CASE add PUBLIC_DISPLAY_HIDE varchar2(1);
alter table AUD_DEFENDANT_ON_CASE add PUBLIC_DISPLAY_HIDE varchar2(1);
@xhb_defendantoncase_bur_tr.sql

alter table XHB_DEFENDANT add PUBLIC_DISPLAY_HIDE varchar2(1);
alter table AUD_DEFENDANT add PUBLIC_DISPLAY_HIDE varchar2(1);
@xhb_defendant_bur_tr.sql


@xhb_public_display_pkg_b.sql



-- For the pre-authorisation warning message

@xhb_authorise_check_pkg_h.sql
@xhb_authorise_check_pkg_b.sql



-- Additional court log events

insert into XHB_COURT_LOG_EVENT_DESC 
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
 EVENT_TYPE, 
 PUBLIC_NOTICE, 
 SHORT_DESCRIPTION)
values 
(2000,
 0, 
 0, 
 0, 
 1, 
 1, 
 0, 
 1, 
 1, 
 'LC_TEXT_', 
 'Trial - Cross Examination of Witness', 
 20931, 
 0, 
 'Trial_Cross_Examination_of_Witness');


insert into XHB_COURT_LOG_EVENT_DESC 
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
 EVENT_TYPE, 
 PUBLIC_NOTICE, 
 SHORT_DESCRIPTION)
values 
(2001,
 0, 
 0, 
 0, 
 1, 
 1, 
 0, 
 1, 
 1, 
 'LC_TEXT_', 
 'Trial - Re-examination of Witness', 
 20932, 
 0, 
 'Trial_Re_examination_of_Witness');


insert into XHB_COURT_LOG_EVENT_DESC 
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
 EVENT_TYPE, 
 PUBLIC_NOTICE, 
 SHORT_DESCRIPTION)
values 
(2002,
 0, 
 1, 
 0, 
 1, 
 1, 
 0, 
 0, 
 0, 
 'LC_TEXT_', 
 'Judge Sentences', 
 20933, 
 0, 
 'Judge_Sentences');


insert into XHB_COURT_LOG_EVENT_DESC 
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
 EVENT_TYPE, 
 PUBLIC_NOTICE, 
 SHORT_DESCRIPTION)
values 
(2003,
 0, 
 1, 
 0, 
 1, 
 1, 
 0, 
 0, 
 0, 
 'LC_TEXT_', 
 'Special Measures Application', 
 20934, 
 0, 
 'Special_Measures_Application');


insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(20,2000);


insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(21,2000);


insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(20,2001);


insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(21,2001);



insert into XHB_COURT_LOG_EVENT_DESC 
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
 EVENT_TYPE, 
 PUBLIC_NOTICE, 
 SHORT_DESCRIPTION)
values 
(2004,
 0, 
 0, 
 1, 
 0, 
 1, 
 0, 
 1, 
 1, 
 'LC_TEXT_', 
 'Deleted - Hearing finished', 
 30501, 
 1, 
 'Deleted_Hearing_Finished');

insert into XHB_COURT_LOG_EVENT_DESC 
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
 EVENT_TYPE, 
 PUBLIC_NOTICE, 
 SHORT_DESCRIPTION)
values 
(2005,
 0, 
 0, 
 1, 
 0, 
 1, 
 0, 
 1, 
 1, 
 'LC_TEXT_', 
 'Deleted - Hearing finished', 
 30601, 
 1, 
 'Deleted_Hearing_Finished');


commit;

