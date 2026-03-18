CREATE OR REPLACE PACKAGE BODY gdg_scjse_gateway_jms_pkg
AS

/******************************************************
** gdg_scjse_gateway_jms_pkg: This package handles the 
**                      triggers generated for the creation of
**                      GDG JMS messages. The procedures and
**                      and functions in this package
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
      /* Getstatus values */
      SELECT outbound_status_id
        INTO g_new_status
        FROM gdg_outbound_statuses
       WHERE internal_code = 'NEW';

      SELECT outbound_status_id
        INTO g_error_status
        FROM gdg_outbound_statuses
       WHERE internal_code = 'ERROR';

      /* Get config properties */
      
      BEGIN
      SELECT TO_NUMBER (property_value)
        INTO g_max_send_attempts
        FROM gdg_config_properties
       WHERE property_code = 'MAX_MSG_ATTEMPTS';
       EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

     BEGIN
      SELECT TO_NUMBER (property_value)
        INTO g_max_lock_attempts
        FROM gdg_config_properties
       WHERE property_code = 'MAX_LOCK_ATTEMPTS';
       EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;


      BEGIN
      SELECT TO_NUMBER (property_value)
        INTO g_max_consec_fail_count
        FROM gdg_config_properties
       WHERE property_code = 'MAX_MSG_CONSEC_FAIL_COUNT';
       EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

      BEGIN
      SELECT TO_NUMBER (property_value)
        INTO g_service_avail_send
        FROM gdg_config_properties
       WHERE property_code = 'SERV_AVAIL_MSG_SEND_FREQ';
       EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

      BEGIN
      SELECT TO_NUMBER (property_value)
        INTO g_service_unavail_send
        FROM gdg_config_properties
       WHERE property_code = 'SERV_UNAVAIL_MSG_SEND_FREQ';
      EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

      BEGIN
      SELECT TO_NUMBER (property_value)
        INTO g_service_avail_retry
        FROM gdg_config_properties
       WHERE property_code = 'SERV_AVAIL_MSG_RETRY_FREQ';
       EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

      BEGIN
      SELECT TO_NUMBER (property_value)
        INTO g_service_unavail_retry
        FROM gdg_config_properties
       WHERE property_code = 'SERV_UNAVAIL_MSG_RETRY_FREQ';
      EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

      BEGIN
      SELECT TO_NUMBER (property_value)
        INTO g_service_avail_bulk_count
        FROM gdg_config_properties
       WHERE property_code = 'SERV_AVAIL_BATCH_SIZE';
        EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;

      BEGIN
       SELECT TO_NUMBER (property_value)
        INTO g_service_unavail_bulk_count
        FROM gdg_config_properties
       WHERE property_code = 'SERV_UNAVAIL_BATCH_SIZE';
      EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
       END;
      
      BEGIN
        SELECT TO_NUMBER(property_value)
        INTO   g_serv_current_delay
        FROM   gdg_config_properties
        WHERE  property_code = 'SERV_CURRENT_DELAY';
      EXCEPTION
         WHEN OTHERS THEN
           /* Ignore error. Use default */
           NULL;
      END;
      
      BEGIN
        SELECT last_updated 
        INTO   g_config_read_time
        FROM   gdg_table_timestamp
        WHERE  name = 'GDG_CONFIG_PROPERTIES';
      EXCEPTION
       WHEN OTHERS THEN
         dbms_output.put_line('Unable to determine last update date');
             RAISE;
      END;

   END;
   
/* ===================================================
**  get_max_id  - local function to get max
**                message_id of the previous set
**                of rows that we tried to lock
** ===================================================
*/
   FUNCTION get_max_id (
      p_tries   IN   NUMBER,
      p_type    IN   gdg_jms_messages.MESSAGE_TYPE%TYPE
   )
      RETURN NUMBER
   IS
      v_max_id   NUMBER;
   BEGIN
      IF ( p_type = 'ALL' ) THEN
          SELECT MAX (message_id)
            INTO v_max_id
           FROM (SELECT   message_id
                  FROM gdg_jms_messages
                 WHERE ROWNUM <= (g_bulk_count * p_tries)
                   AND expiry_time < SYSDATE
              ORDER BY message_id);
        ELSE
          SELECT MAX (message_id)
            INTO v_max_id
           FROM (SELECT   message_id
                  FROM gdg_jms_messages
                 WHERE ROWNUM <= (g_bulk_count * p_tries)
                   AND MESSAGE_TYPE = p_type
                   AND expiry_time < SYSDATE
              ORDER BY message_id);
        END IF;

      RETURN v_max_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         DBMS_OUTPUT.put_line ('failure in get_max_id');
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END get_max_id;

/*=================================================
** create_jms_message - given item_id, type and
**                      delay period. Create a record
**                in gdg_jms_messages table
** ================================================
*/
   PROCEDURE create_jms_message (
      p_message_id     IN   gdg_jms_messages.message_id%TYPE,
      p_message_type   IN   gdg_jms_messages.MESSAGE_TYPE%TYPE,
      p_delay          IN   NUMBER
   )
   IS
   BEGIN
      /*
      ** insert row in GDG_JMS_MESSAGES table. Adjust current time with the
      ** number of mins correspoinding to the p_delay param.
      */
      INSERT INTO gdg_jms_messages
                  (message_id, MESSAGE_TYPE, expiry_time
                  )
           VALUES (p_message_id, p_message_type, SYSDATE + p_delay / 1440
                  );
   END;

/*=================================================
** process_inbound_message - given inbound_message_id, 
**                 create a corresponding entry in
**                 gdg_jms_messages table.
** ================================================
*/

   PROCEDURE process_inbound_message (
      p_inbound_message_id   IN   gdg_inbound_messages.inbound_message_id%TYPE
   )
   IS
      /*
      ** No delay. Process inbound messages immediately 
      */
      l_delay   NUMBER := 0;
   BEGIN
      create_jms_message (p_inbound_message_id, 'INBOUND', l_delay);
   END;

/*=================================================
** process_outbound_message - given request_id, 
**                 calculate appropriate delay period
**                 and create row in gdg_jms_messages
**                 table.
** ================================================
*/
   PROCEDURE process_outbound_message (
      p_request_id           IN   gdg_outbound_messages.request_id%TYPE,
      p_outbound_status_id   IN   gdg_outbound_messages.outbound_status_id%TYPE,
      p_send_attempts        IN   gdg_outbound_messages.send_attempts%TYPE
   )
   IS
      l_consec_fail_count      NUMBER;
      l_service_avail          BOOLEAN := FALSE;
      l_consec_failures        NUMBER  := 0;
      l_delay                  NUMBER  := 0;
      l_serv_unavail_msg_count NUMBER  := 0;
   BEGIN
      /*
      ** Only process the record if
      **  - the status is 'NEW' or
      **  - the status is ERROR and max send attempts has not
      **    been exceeded
      */
      IF    (p_outbound_status_id  = g_new_status)
      OR    (p_outbound_status_id  = g_error_status
      AND    p_send_attempts      <= g_max_send_attempts)
      THEN
         l_consec_failures := gdg_scjse_gateway_outbound_pkg.get_consec_failures;
         IF (l_consec_failures <= g_max_consec_fail_count)
         THEN
            l_service_avail := TRUE;
         ELSE
            l_service_avail := FALSE;
         END IF;

         IF (l_service_avail)
         THEN
             IF (p_outbound_status_id = g_new_status)
             THEN
                 l_delay := g_service_avail_send;
             ELSE
                 l_delay := g_service_avail_retry;
             END IF;             
         ELSE             
             l_serv_unavail_msg_count := (l_consec_failures - g_max_consec_fail_count);

             IF (p_outbound_status_id = g_new_status)
             THEN
                 l_delay := g_service_unavail_send  + (g_serv_current_delay * l_serv_unavail_msg_count);
             ELSE
                 l_delay := g_service_unavail_retry + (g_serv_current_delay * l_serv_unavail_msg_count);
             END IF;
         END IF;

         create_jms_message (p_request_id, 'OUTBOUND', l_delay);
      END IF;

      
   END process_outbound_message;

/*
** ==============================================================
** get_jms_message_array - lock next set of rows in gdg_jms_message,
**                   delete the rows and return the ids in an
**                   array of type gdg_jms_message_array along
**                   with a count of number of rows selected.
** =============================================================
*/
   PROCEDURE get_jms_message_array (
      p_message_type   IN       gdg_jms_messages.MESSAGE_TYPE%TYPE,
      p_msg_array      OUT      gdg_jms_message_array,
      p_count          OUT      NUMBER
   )
   IS
      resource_busy     EXCEPTION;
      PRAGMA EXCEPTION_INIT (resource_busy, -54);      
      v_tries           NUMBER                  := 0;
      v_rows_selected   BOOLEAN                 := FALSE;
      v_max_id          NUMBER                  := 0;
      v_consec_fail_count NUMBER                := 0;
      v_config_last_updated   DATE;  
   BEGIN
     BEGIN
        SELECT last_updated 
        INTO   v_config_last_updated
        FROM   gdg_table_timestamp
        WHERE  name = 'GDG_CONFIG_PROPERTIES';
      EXCEPTION
       WHEN OTHERS THEN
         dbms_output.put_line('Unable to determine last update date');
             RAISE;
      END;
   
      IF ( v_config_last_updated != G_config_read_time ) THEN
          read_config_params;
      END IF;

     v_consec_fail_count := gdg_scjse_gateway_outbound_pkg.get_consec_failures; 
     IF (v_consec_fail_count < g_max_consec_fail_count)
     THEN       
        g_bulk_count := g_service_avail_bulk_count;
     ELSE
        g_bulk_count := g_service_unavail_bulk_count; 
     END IF;
         
      WHILE (v_tries < g_max_lock_attempts) AND (NOT v_rows_selected)
      LOOP
         BEGIN
            IF (v_tries = 0)
            THEN
               /*
               ** Exception is not raised by ORACLE when no rows are
               ** selected as a result of no more rows being available
               ** for processing
               */
               IF ( p_message_type = 'ALL' ) THEN

               SELECT     gdg_jms_message_type (message_id,
                                                MESSAGE_TYPE,
                                                expiry_time
                                               )
               BULK COLLECT INTO p_msg_array
                     FROM gdg_jms_messages
                    WHERE ROWNUM <= g_bulk_count
                      AND expiry_time < SYSDATE
                 ORDER BY message_id
               FOR UPDATE NOWAIT;

               ELSE

               SELECT     gdg_jms_message_type (message_id,
                                                MESSAGE_TYPE,
                                                expiry_time
                                               )
               BULK COLLECT INTO p_msg_array
                     FROM gdg_jms_messages
                    WHERE ROWNUM <= g_bulk_count
                      AND MESSAGE_TYPE = p_message_type
                      AND expiry_time < SYSDATE
                 ORDER BY message_id
               FOR UPDATE NOWAIT;

               END IF;
            ELSE
               IF ( p_message_type = 'ALL' ) THEN
               SELECT     gdg_jms_message_type (message_id,
                                                MESSAGE_TYPE,
                                                expiry_time
                                               )
               BULK COLLECT INTO p_msg_array
                     FROM gdg_jms_messages
                    WHERE ROWNUM <= g_bulk_count
                      AND message_id > v_max_id
                      AND expiry_time < SYSDATE
                 ORDER BY message_id
               FOR UPDATE NOWAIT;
               ELSE
                   SELECT     gdg_jms_message_type (message_id,
                                                MESSAGE_TYPE,
                                                expiry_time
                                               )
                   BULK COLLECT INTO p_msg_array
                         FROM gdg_jms_messages
                        WHERE ROWNUM <= g_bulk_count
                          AND message_id > v_max_id
                          AND MESSAGE_TYPE = p_message_type
                          AND expiry_time < SYSDATE
                     ORDER BY message_id
                   FOR UPDATE NOWAIT;
               END IF;

            END IF;

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
               ** to lock, by calculating v_max_id. We are allowed
               ** g_max_lock_attempts number of tries.
               */
               /* DBMS_OUTPUT.put_line ('resource busy - get next set of rows'); */
               v_tries := v_tries + 1;
               v_max_id := get_max_id (v_tries, p_message_type);
            WHEN OTHERS
            THEN
               DBMS_OUTPUT.put_line ('get_jms_message_array failure');
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
                  FROM TABLE (CAST (p_msg_array AS gdg_jms_message_array)))
      LOOP
         /*    DBMS_OUTPUT.put_line (r.message_id || ', ' || r.message_type); */
         BEGIN
            DELETE FROM gdg_jms_messages
                  WHERE message_id = r.message_id;
         EXCEPTION
            WHEN OTHERS
            THEN
               DBMS_OUTPUT.put_line
                   (   'get_jms_message_array: delete failed. message_id is '
                    || TO_CHAR (r.message_id || ' ' || p_message_type)
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
   END get_jms_message_array;

/*==============================================================
** get_jms_message_refcur - get next set of rows by calling
**                   get_jms_message_array. Then open a ref
**                   cursor on the result. Return the ref cursor.
** =============================================================
*/
   FUNCTION get_jms_message_refcur( 
   p_message_type   IN       gdg_jms_messages.MESSAGE_TYPE%TYPE )
       RETURN SYS_REFCURSOR
   IS
      v_msg_array   gdg_jms_message_array;
      v_count       NUMBER;
      rc_msg        SYS_REFCURSOR;
   BEGIN
      /* First get the results into the SQL type array */
      get_jms_message_array (p_message_type, v_msg_array, v_count);

      OPEN rc_msg
       FOR
          SELECT *
            FROM TABLE (CAST (v_msg_array AS gdg_jms_message_array));

      RETURN rc_msg;
   EXCEPTION
      WHEN OTHERS THEN
         DBMS_OUTPUT.put_line ('get_jms_message_refcur failure');
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END get_jms_message_refcur;

BEGIN  
/*
** Package initialisation
*/
read_config_params;

END gdg_scjse_gateway_jms_pkg;
/

SHOW errors;