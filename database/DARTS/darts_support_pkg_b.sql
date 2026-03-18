CREATE OR REPLACE PACKAGE BODY DARTS_SUPPORT_PKG
AS

/******************************************************
** DARTS_SUPPORT_PKG: This is the package that contains
**                    all the support procedures for the
                      DARTS interface.
*******************************************************/

/* ===================================================
**  CLEARUP - Deletes all records in DAR_MESSAGE_STORE
**          that are not needed.
** ===================================================
*/

   PROCEDURE CLEARUP
   IS
   BEGIN
       DELETE FROM dar_message_store
       WHERE
         status_code = 'S'
       OR
         (status_code = 'F' 
          AND 
          status_detail like '404 : %');
   COMMIT;
   END;

   PROCEDURE RESEND_FAILURES
   IS
   BEGIN
       UPDATE dar_message_store
       SET status_detail = ''
       WHERE
         status_code = 'R';
       COMMIT;

       INSERT INTO DAR_NEW_MESSAGES(MESSAGE_ID,
                                    XHIBIT_MESSAGE_CODE,
                                    EXISS_MESSAGE_CODE,
                                    PAYLOAD,
                                    RETRY_COUNT,
                                    NEXT_RETRY_TIME)
                             SELECT MESSAGE_ID,
                                    XHIBIT_MESSAGE_CODE,
                                    EXISS_MESSAGE_CODE,
                                    PAYLOAD,
                                    0,
                                    SYSDATE
                               FROM DAR_MESSAGE_STORE
                              WHERE status_code = 'R';
        COMMIT;                                                              
   END;
   
   FUNCTION ERROR_TEST
   RETURN NUMBER
   IS
   error_count                NUMBER;
   BEGIN
   SELECT COUNT(*) 
   INTO error_count
     FROM DAR_MESSAGE_STORE
     WHERE status_code = 'F'
     AND status_detail like '%Retries Exceeded%';
   
   
   IF (error_count > 0) 
   THEN
     RETURN 1;
   ELSE
     RETURN 0;
   END IF;
   END;
   
  

END DARTS_SUPPORT_PKG;
/
show errors
