/*
 * Filename:    wmb_message_route_alter.sql (CREST to XHIBIT functionality release)
 *
 * Changes for Defendant Differences report trigger
 *
 * HISTORY
 * =======
 * DATE         WHO          	COMMENT
 * ----         ---          	-------
 * 03/04/2018   Brian Hingston  Defendant Differences trigger (ctx-1647)
 */



/*
 * Insert Standing data for routing the new database trigger
 */
INSERT INTO WMB_MESSAGE_ROUTE (SCHEMA_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_VALUE,OPERATION,QUEUE_NAME, MAP_NAME)
	VALUES ('XHIBIT','XHB_DEFENDANT_ON_CASE','DIFFERENCE_REPORT','Y','INSERT','TRIGGER.IN', 'Difference_Report_Trigger');
INSERT INTO WMB_MESSAGE_ROUTE (SCHEMA_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_VALUE,OPERATION,QUEUE_NAME, MAP_NAME)
	VALUES ('XHIBIT','XHB_DEFENDANT_ON_CASE','DIFFERENCE_REPORT','Y','UPDATE','TRIGGER.IN', 'Difference_Report_Trigger');

COMMIT;