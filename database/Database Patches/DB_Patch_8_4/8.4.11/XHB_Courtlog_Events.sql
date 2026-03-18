---New court log events for the appeal

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID,FLAGGED_EVENT, 
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
                                                                    EVENT_TYPE,
                                                                    PUBLIC_NOTICE,
                                                                    SHORT_DESCRIPTION) 
                                                  VALUES ((select max(event_desc_id) +1 from xhb_court_log_event_desc),0,0,0,1,1,0,1,1,'LC_TEXT_','Appeal-Cross Examination of Witness',1,31000,0,'Appeal_Cross_Examination_of_Witness');

INSERT INTO XHB_COURT_LOG_EVENT_DESC (EVENT_DESC_ID,FLAGGED_EVENT, 
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
                                                                    EVENT_TYPE,
                                                                    PUBLIC_NOTICE,
                                                                    SHORT_DESCRIPTION) 
                                                  VALUES ((select max(event_desc_id) +1 from xhb_court_log_event_desc),0,0,0,1,1,0,1,1,'LC_TEXT_','Appeal-Re-examination of Witness',1,32000,0,'Appeal_Re_examination_of_Witness');

-- Update the category table

insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(20,(select event_desc_id from xhb_court_log_event_desc where event_type=31000));

insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(21,(select event_desc_id from xhb_court_log_event_desc where event_type=31000));

insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(20,(select event_desc_id from xhb_court_log_event_desc where event_type=32000));

insert into XHB_COURT_LOG_CATEGORY
(CATEGORY_DESC_ID,
 EVENT_DESC_ID)
values
(21,(select event_desc_id from xhb_court_log_event_desc where event_type=32000));


COMMIT;
