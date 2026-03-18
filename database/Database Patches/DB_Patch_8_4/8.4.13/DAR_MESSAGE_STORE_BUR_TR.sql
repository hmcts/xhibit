CREATE OR REPLACE TRIGGER DAR_MESSAGE_STORE_BUR_TR
  BEFORE UPDATE
  ON DAR_MESSAGE_STORE
  FOR EACH ROW

BEGIN

SELECT SYSDATE 
       INTO :NEW.LAST_UPDATE_DATE 
       FROM DUAL;
       
SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

IF (:NEW.STATUS_CODE = 'R' )
THEN
 IF DAR_MESSAGE_PKG.IS_PRIORITY_MESSAGE(:NEW.XHIBIT_MESSAGE_CODE) > 0
 THEN
     INSERT INTO DAR_PRIORITY_NEW_MESSAGES
          (MESSAGE_ID                                                               ,
          XHIBIT_MESSAGE_CODE,
          EXISS_MESSAGE_CODE,
          PAYLOAD,
          RETRY_COUNT,
          NEXT_RETRY_TIME)
          VALUES(
          :NEW.MESSAGE_ID,
          :NEW.XHIBIT_MESSAGE_CODE,
          :NEW.EXISS_MESSAGE_CODE,
          :NEW.PAYLOAD,
          0,
          SYSDATE);
 ELSE
     INSERT INTO DAR_NEW_MESSAGES
          (MESSAGE_ID,
          XHIBIT_MESSAGE_CODE,
          EXISS_MESSAGE_CODE,
          PAYLOAD,
          RETRY_COUNT,
          NEXT_RETRY_TIME)
          VALUES(
          :NEW.MESSAGE_ID,
          :NEW.XHIBIT_MESSAGE_CODE,
          :NEW.EXISS_MESSAGE_CODE,
          :NEW.PAYLOAD,
          0,
          SYSDATE);        
 END IF;
END IF;

END;
/