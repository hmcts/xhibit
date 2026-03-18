CREATE OR REPLACE PACKAGE BODY darts_new_msgs_pkg
AS

/******************************************************
** darts_process_msgs_pkg: This package handles the flow
**                      into and out of DAR_NEW MESSAGES.
**                      Functions in this package
**                      form part of the calling module's
**                      transaction. Therefore, no commit
**                      or rollback are performed.
*******************************************************/

/* ===================================================
**  read_config_params  - local function to read
**                config params into globals
** ===================================================
*/

   PROCEDURE read_config_params
   IS
   BEGIN

      /* Get config properties */
     BEGIN
      SELECT TO_NUMBER(darts_property_value)
        INTO g_darts_retry_interval
        FROM dar_darts_config
       WHERE DARTS_PROPERTY_NAME = 'darts.retry.interval';
       EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

     BEGIN
      SELECT TO_NUMBER(darts_property_value)
        INTO g_max_lock_attempts
        FROM dar_darts_config
       WHERE DARTS_PROPERTY_NAME = 'MAX_LOCK_ATTEMPTS';
       EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

     BEGIN
      SELECT TO_NUMBER (darts_property_value)
        INTO g_bulk_count
        FROM dar_darts_config
       WHERE DARTS_PROPERTY_NAME = 'BULK_COUNT';
       EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

     BEGIN
         SELECT SYSDATE +
         (
           (SELECT DARTS_PROPERTY_VALUE
              FROM DAR_DARTS_CONFIG
             WHERE DARTS_PROPERTY_NAME = 'cache.time')
                     /(24*60*60*1000) /* Config in milliseconds */
         ) INTO g_config_next_update
         FROM DUAL;
         EXCEPTION
         WHEN OTHERS THEN
          /* Ignore error. Use default */
          NULL;
       END;

   END;


/* ===================================================
**  get_max_id  - local function to get max
**                item_id of the previous set
**                of rows that we tried to lock
** ===================================================
*/
   FUNCTION get_max_id( p_min_id NUMBER )
      RETURN NUMBER
   IS
      v_max_id   NUMBER;
   BEGIN
      SELECT MAX (MESSAGE_ID)
      INTO v_max_id
      FROM
        (SELECT MESSAGE_ID FROM
         DAR_NEW_MESSAGES
         WHERE
           SYSDATE >= NEXT_RETRY_TIME
           AND MESSAGE_ID > p_min_id
           AND MESSAGE_ID < p_min_id + 20000
           ORDER BY MESSAGE_ID
        )
      WHERE ROWNUM <=  g_bulk_count * 3
      ;

      RETURN v_max_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         DBMS_OUTPUT.put_line ('failure in get_max_id from DAR_NEW_MESSAGES');
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END get_max_id;


/*
** ==============================================================
** get_messages -    lock next set of rows in DAR_NEW_MESSAGES,
**                   delete the rows and return the data in an
**                   array of type darts_message_array along
**                   with a count of number of rows selected.
** =============================================================
*/
   PROCEDURE get_darts_message_array (
      p_msg_array      OUT      darts_message_array,
      p_count          OUT      NUMBER
   )
   IS
      resource_busy     EXCEPTION;
      PRAGMA EXCEPTION_INIT (resource_busy, -54);
      v_tries                NUMBER                  := 0;
      v_rows_selected        BOOLEAN                 := FALSE;
      v_min_message_id       NUMBER                  := 0;

   BEGIN

     BEGIN
       /* Read config params if current config expired */
       IF g_config_next_update < SYSDATE
       THEN
         read_config_params;
       END IF;
       EXCEPTION
       WHEN OTHERS THEN
         dbms_output.put_line('Unable to determine config cache time.');
             RAISE;
     END;

     /****************************
      ** GET MESSAGES
      *****************************/
     SELECT MIN(MESSAGE_ID)
     INTO v_min_message_id
     FROM DAR_NEW_MESSAGES;

     WHILE (v_tries < g_max_lock_attempts) AND (NOT v_rows_selected)
     LOOP
       BEGIN
               /*
               ** Exception is not raised by ORACLE when no rows are
               ** selected as a result of no more rows being available
               ** for processing
               */
               SELECT     darts_message_type (MESSAGE_ID,
                                              XHIBIT_MESSAGE_CODE,
                                              EXISS_MESSAGE_CODE,
                                              PAYLOAD,
                                              RETRY_COUNT,
                                              NEXT_RETRY_TIME,
                                              CREATION_DATE,
                                              LAST_UPDATE_DATE
                                             )
               BULK COLLECT INTO p_msg_array
               FROM DAR_NEW_MESSAGES
               WHERE MESSAGE_ID IN
                 ( SELECT MESSAGE_ID FROM
                    ( SELECT MESSAGE_ID FROM          
                       DAR_NEW_MESSAGES
                       /* Select Messages that have elapsed retry delay */
                       WHERE SYSDATE >= NEXT_RETRY_TIME
                       AND MESSAGE_ID >= v_min_message_id
                       AND MESSAGE_ID < v_min_message_id + 20000
                       ORDER BY MESSAGE_ID
                    )
                   WHERE ROWNUM <=  g_bulk_count
                  ) 
               FOR UPDATE NOWAIT;

            /*
            ** NB Set when rows successfully locked or when no more
            ** rows to process.
            */
            v_rows_selected := TRUE;
         EXCEPTION
            WHEN resource_busy
            THEN
               /*
               ** rows are currently locked by another session.
               ** so retry, but skip the ones that we just tried
               ** to lock, by calculating new v_min_message_id. We are allowed
               ** g_max_lock_attempts number of tries.
               */
               /* DBMS_OUTPUT.put_line ('resource busy - get next set of rows'); */
               v_tries := v_tries + 1;
               v_min_message_id := get_max_id( v_min_message_id);

            WHEN OTHERS
            THEN
               DBMS_OUTPUT.put_line ('get_darts_message_array failure');
               DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
               RAISE;
         END;
      END LOOP;

      IF (v_tries = g_max_lock_attempts)
      THEN
         /*
         ** Raise user defined error. We have used up the maxnimum number of
         ** retries that we are allowed.
         */
         raise_application_error (-20101, 'Max tries attempted.');
      END IF;

       /*
       ** We managed to successfully select and lock a set of rows. Now, before
      ** passing the details to the calling module, we need to delete the
       ** corresponding rows
      */
      FOR r IN (SELECT *
                  FROM TABLE (CAST (p_msg_array AS darts_message_array)))
      LOOP
         BEGIN
            DELETE FROM DAR_NEW_MESSAGES
                  WHERE MESSAGE_ID = r.message_id;
         EXCEPTION
            WHEN OTHERS
            THEN
               DBMS_OUTPUT.put_line
                   (   'get_darts_message_array: delete failed. message_id is '
                    || TO_CHAR (r.message_id )
                   );
               DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
               RAISE;
         END;
      END LOOP;

      /*
      ** No commit is done here. The calling module will do a commit when all the
      ** items have been successfully processed, otherwise a rollback may be
      ** issued.
      */
      p_count := p_msg_array.COUNT;
      /*insert into darts_stats values(p_count, sysdate, v_tries);*/
   END get_darts_message_array;



/******************************************************************
 ** insert_darts_message_resend - Insert an array of messages into
 **                   dar_new_messages to be resent after configurable
 **                   time.
 ******************************************************************/
   FUNCTION insert_darts_message_retry( message_id_in        IN DAR_NEW_MESSAGES.MESSAGE_ID%TYPE,
                                         xhibit_msg_code_in   IN DAR_NEW_MESSAGES.XHIBIT_MESSAGE_CODE%TYPE,
                                         exiss_msg_code_in    IN DAR_NEW_MESSAGES.EXISS_MESSAGE_CODE%TYPE,
                                         payload_in           IN DAR_NEW_MESSAGES.PAYLOAD%TYPE,
                                         retry_count_in       IN DAR_NEW_MESSAGES.RETRY_COUNT%TYPE,
                                         next_retry_time_in   IN DAR_NEW_MESSAGES.NEXT_RETRY_TIME%TYPE,
                                         creation_date_in     IN DAR_NEW_MESSAGES.CREATION_DATE%TYPE,
                                         last_update_date_in  IN DAR_NEW_MESSAGES.LAST_UPDATE_DATE%TYPE
   ) RETURN NUMBER
   IS

   new_retry_time            DATE                    := SYSDATE;

 BEGIN
     BEGIN
       /* Read config params if current config expired */
       IF g_config_next_update < SYSDATE
       THEN
         read_config_params;
       END IF;
       EXCEPTION
       WHEN OTHERS THEN
         dbms_output.put_line('Unable to determine config cache time.');
       RAISE;
     END;

     IF (retry_count_in = 0)
      THEN
         /*
          * Added so that the initial retry is resent in short period of time
          * (30 secs) so token refresh failure doesn't cause long delivery delays
          */
          new_retry_time := (next_retry_time_in + ( 30 /(24*60*60)));
      ELSE
         new_retry_time := (next_retry_time_in + g_darts_retry_interval/(24*60*60*1000));
    END IF;

    INSERT INTO DAR_NEW_MESSAGES(MESSAGE_ID,
                                 XHIBIT_MESSAGE_CODE,
                                 EXISS_MESSAGE_CODE,
                                 PAYLOAD,
                                 RETRY_COUNT,
                                 NEXT_RETRY_TIME,
                                 CREATION_DATE,
                                 LAST_UPDATE_DATE)
                          VALUES(message_id_in,
                                 xhibit_msg_code_in,
                                 exiss_msg_code_in,
                                 payload_in,
                                 (retry_count_in + 1),
                                 new_retry_time,
                                 creation_date_in,
                                 last_update_date_in);
       RETURN 0;
    EXCEPTION
      WHEN OTHERS THEN
         DBMS_OUTPUT.put_line ('dar_new_messageS INSERT failed.');
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;

   END insert_darts_message_retry;



/******************************************************************
 ** get_darts_message_refcur - get next set of rows by calling
 **                   get_darts_message_array. Then open a ref
 **                   cursor on the result. Return the ref cursor.
 ******************************************************************/
   FUNCTION get_darts_message_refcur
       RETURN SYS_REFCURSOR
   IS
      v_msg_array   darts_message_array;
      v_count       NUMBER;
      rc_msg        SYS_REFCURSOR;
   BEGIN
      /* First get the results into the SQL type array */
      get_darts_message_array ( v_msg_array, v_count);

      OPEN rc_msg
       FOR SELECT * FROM TABLE (CAST (v_msg_array AS darts_message_array));

      RETURN rc_msg;
   EXCEPTION
      WHEN OTHERS THEN
         DBMS_OUTPUT.put_line ('get_darts_message_refcur failure');
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END get_darts_message_refcur;

BEGIN
/*
** Package initialisation
*/
read_config_params;

END darts_new_msgs_pkg;
