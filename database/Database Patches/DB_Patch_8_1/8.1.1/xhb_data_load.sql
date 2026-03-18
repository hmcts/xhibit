
INSERT into xhb_progress_trigger_status 
          ( status_text 
           )
 VALUES   ( 'Case Details Updated');


INSERT into xhb_progress_trigger_status 
          ( status_text 
           )
 VALUES   ( 'In Process');

INSERT into xhb_progress_trigger_status 
          ( status_text 
           )
 VALUES   ('Completed');


INSERT into xhb_progress_trigger_status 
          ( status_text 
           )
 VALUES   ('Failed');


delete from xhb_selector_queues;
delete from xhb_selectors;
delete from xhb_queues;

insert into xhb_queues (queue_id, jndi_name, description) values (1, 'xhibit/jms/MessageBrokerQueue', 'Message Broker Queue');
insert into xhb_queues (queue_id, jndi_name, description) values (2, 'xhibit/jms/ExissMessageBuilderQueue', 'Exiss Message Builder Queue');
insert into xhb_queues (queue_id, jndi_name, description) values (3, 'xhibit/jms/ExissItemTrackingNotifierQueue', 'Exiss Item Tracking Notifier Queue');
insert into xhb_queues (queue_id, jndi_name, description) values (4, 'xhibit/jms/ExissMessageBrokerQueue', 'Exiss Message Broker Queue');
insert into xhb_queues (queue_id, jndi_name, description) values (5, 'xhibit/jms/ExissItemTrackingQueue', 'Exiss Item Tracking Queue');
insert into xhb_queues (queue_id, jndi_name, description) values (6, 'xhibit/jms/ExissDeadLetterQueue', 'Exiss Dead Letter Queue');
insert into xhb_queues (queue_id, jndi_name, description) values (7, 'xhibit/jms/DeadLetterQueue', 'Dead Letter Queue');

INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'Y', 9, 'XHBTarget = ''EXISS''', 'Generic EXISS selector');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''UPDCASE''', 'Selector for Updated Case');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''ARS''', 'Selector for Appeal Record Sheet');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''BO''', 'Selector for Bail Order');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''BW''', 'Selector for Bench Warrant');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''CHG''', 'Selector for Charges');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''CO''', 'Selector for Community Order');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''CPO''', 'Selector for Community Punishment Order');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''CPRO''', 'Selector for Community Punishment Rehabilitation Order');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''CRO''', 'Selector for Community Rehabilitation Order');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''CRS''', 'Selector for Committal (for Sentence) Record Sheet');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''DISCASE''', 'Selector for Discontinued Case');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''DL''', 'Selector for Daily List');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''FL''', 'Selector for Firm List');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''IO''', 'Selector for Imprisonment Order');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''NEWCASE''', 'Selector for New Case');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''PDL''', 'Selector for Prison Daily List');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''RL''', 'Selector for Running List');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''RO''', 'Selector for Remand Order');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''SS''', 'Selector for Skeleton Schedule');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''TRS''', 'Selector for Trial Record Sheet');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''WL''', 'Selector for Warned List');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''YOO''', 'Selector for Young Offender Order');
INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.nextval, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''EVENT''', 'Selector for Court Log Events');

INSERT INTO XHB_SELECTOR_QUEUES (selector_id, queue_id)
SELECT s.selector_id, q.queue_id
FROM XHB_SELECTORS s,XHB_QUEUES q
WHERE q.queue_id IN (2,3);

insert into XHB_CONFIG_PROP (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) VALUES (4, 'TIME_DELAY_EXISS_SEND', '10');

insert into XHB_CONFIG_PROP (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) VALUES (5, 'EXISS_MAX_RETRY', '5');
INSERT INTO XHB_CONFIG_PROP (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) VALUES (1, 'ALL_SMTP', 'N'); 
INSERT INTO XHB_CONFIG_PROP (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) VALUES (2, 'MAX_SMTP_SEND_ATTEMPTS', '5'); 
INSERT INTO XHB_CONFIG_PROP (CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE) VALUES (3, 'MAX_MAPI_SEND_ATTEMPTS', '5'); 

INSERT INTO MTBL_TIME_TRIGGER_STATUS ( MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS ) VALUES ( 'EmailFax_Poll.mmc',  TO_Date( '05/10/2006 12:27:05 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'MCMAIL2', 1, 'C'); 
COMMIT;


insert into  xhb_sys_audit(TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) 
values 
( 'XHB_EMAIL2','AUD_EMAIL2','Y');

insert into  xhb_sys_audit(TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) 
values 
('XHB_EMAIL_DOMAIN','AUD_EMAIL_DOMAIN','Y');

/* Wiki request 6 */
INSERT INTO XHB_CREST_IMPORT_TYPE (import_type, description) VALUES ('NC', 'New Cases'); 
/* Wiki request 6 end */

/* Wiki request 7 */
INSERT INTO MTBL_TIME_TRIGGER_STATUS (mapname, lastruntimedate, hostname, frequency, progress) VALUES ('Xhibit_Update_Case', SYSDATE, 'CSA00027', 600, 'C');
/* Wiki request 7 end */

/* Wiki request 37 */

INSERT INTO XHB_SELECTORS (selector_id, enabled, precedence, selector, description) VALUES (xhb_selectors_seq.NEXTVAL, 'N', 1, 'XHBTarget = ''EXISS'' AND XHBItemType = ''DELIVERERROR''', 'Selector for Deliver Error');

INSERT INTO XHB_SELECTOR_QUEUES VALUES (xhb_selector_queues_seq.NEXTVAL, xhb_selectors_seq.CURRVAL, 2);
INSERT INTO XHB_SELECTOR_QUEUES VALUES (xhb_selector_queues_seq.NEXTVAL, xhb_selectors_seq.CURRVAL, 3);

/* Wiki request 42 */


INSERT INTO MTBL_TIME_TRIGGER_STATUS VALUES ('Resend_Fail_SMTP.mmc', to_date('30-11-06 10:00:00', 'DD-MM-YY HH24:MI:SS'), 'csa00027', '300', 'C');
commit;