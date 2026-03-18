/*DARTS Database modifications*/

/*
* Add new Priority new messages table
*/
create table DAR_PRIORITY_NEW_MESSAGES
(
  MESSAGE_ID          NUMBER not null,
  XHIBIT_MESSAGE_CODE VARCHAR2(50) not null,
  EXISS_MESSAGE_CODE  VARCHAR2(50) not null,
  PAYLOAD             CLOB,
  RETRY_COUNT         NUMBER not null,
  NEXT_RETRY_TIME     DATE not null,
  LAST_UPDATE_DATE    DATE not null,
  CREATION_DATE       DATE not null,
  CREATED_BY          VARCHAR2(30) not null,
  LAST_UPDATED_BY     VARCHAR2(30) not null,
  VERSION             NUMBER(5)
);
/



/*
* Update Packages
*/
@DAR_MESSAGE_PKG_h.sql
/
@DAR_MESSAGE_PKG_b.sql
/
@DARTS_PR_NEW_MSGS_PKG_h.sql
/
@DARTS_PR_NEW_MSGS_PKG_b.sql
/




/*
* Modify Triggers
*/
@DAR_PR_NEW_MESSAGES_BIR_TR.sql
/
@DAR_MESSAGE_STORE_AIR_TR.sql
/
@DAR_MESSAGE_STORE_BUR_TR.sql
/




/*
* Insert Reference data to hold priority types
*/
INSERT INTO DAR_DARTS_CONFIG(DARTS_CONFIG_ID,DARTS_PROPERTY_NAME,DARTS_PROPERTY_VALUE)
VALUES ((SELECT MAX(DARTS_CONFIG_ID)+1 FROM DAR_DARTS_CONFIG), 'darts.priorityMessages', '10100,10500,20913,30100,30300,30500,30600');

commit;

/
