CREATE OR REPLACE PACKAGE BODY gdg_scjse_gateway_outbound_pkg
AS
   FUNCTION create_gdg_outbound_clob_msg (
      p_request_id    IN   gdg_outbound_clobs.request_id%TYPE,
      p_source_id     IN   gdg_outbound_messages.source_identifier%type,
      p_dest_id       IN   gdg_outbound_messages.destination_identifier%type,
      p_exec_mode     IN   gdg_outbound_messages.exec_mode%type,
      p_request_time  IN   gdg_outbound_messages.request_timestamp%type,
      p_send_attempts IN   gdg_outbound_messages.send_attempts%type,
      p_clob_in       IN   gdg_outbound_clobs.clob_data%TYPE
   )
      RETURN gdg_outbound_clobs.request_id%TYPE
   IS
      l_NEW_status gdg_outbound_statuses.outbound_status_id%TYPE;
   BEGIN

      SELECT outbound_status_id
      INTO   l_NEW_status
      FROM   gdg_outbound_statuses
      WHERE  internal_code = 'NEW';

      INSERT INTO gdg_outbound_clobs (
                  request_id, 
                  clob_data)
           VALUES(p_request_id,
                  p_clob_in);

      INSERT INTO gdg_outbound_messages (
                  request_id,
                  source_identifier,
                  destination_identifier,
                  exec_mode,
                  request_timestamp,
                  send_attempts,
                  outbound_status_id)
           VALUES(p_request_id,
                  p_source_id,
                  p_dest_id,
                  p_exec_mode,
                  p_request_time,
                  p_send_attempts,
                  l_NEW_status);

      RETURN p_request_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END create_gdg_outbound_clob_msg;

   PROCEDURE read_outbound_message_and_clob (
      p_gdg_outbound_msg_properties   OUT      sys_refcursor,
      p_request_id                    IN       gdg_outbound_messages.request_id%TYPE
   )
   IS
   BEGIN
      OPEN p_gdg_outbound_msg_properties
       FOR
          SELECT om.request_id request_id,
                 om.source_identifier source_identifier,
                 om.destination_identifier destination_identifier,
                 om.exec_mode exec_mode,
                 om.request_timestamp request_timestamp,
                 om.send_attempts send_attempts,
                 om.outbound_status_id outbound_status_id,
                 oc.clob_data clob_data
            FROM gdg_outbound_messages om, gdg_outbound_clobs oc
           WHERE om.request_id = p_request_id
             AND om.request_id = oc.request_id;
   END read_outbound_message_and_clob;

   FUNCTION update_gdg_outbound_message (
      p_request_id           IN   gdg_outbound_messages.request_id%TYPE,
      p_internal_code        IN   gdg_outbound_statuses.internal_code%TYPE,
      p_failure_code         IN   gdg_outbound_failures.failure_code%TYPE,
      p_failure_text         IN   gdg_outbound_failures.failure_text%TYPE
   )
      RETURN gdg_outbound_messages.request_id%TYPE
   IS
     l_msg_consecutive_failure_cnt NUMBER;
     l_max_msg_consecutive_failure NUMBER;

     l_outbound_status_id gdg_outbound_statuses.outbound_status_id%TYPE;
   BEGIN

      SELECT outbound_status_id
      INTO   l_outbound_status_id
      FROM   gdg_outbound_statuses
      WHERE  internal_code = p_internal_code;

      IF (p_internal_code = 'ERROR')
      THEN
         UPDATE gdg_outbound_messages
            SET request_timestamp = SYSDATE
          WHERE request_id = p_request_id;

          gdg_scjse_gateway_outbound_pkg.increment_consec_failures;

          l_msg_consecutive_failure_cnt :=
               gdg_scjse_gateway_outbound_pkg.get_consec_failures;

          SELECT TO_NUMBER (property_value)
          INTO l_max_msg_consecutive_failure
          FROM GDG_CONFIG_PROPERTIES
          WHERE property_code = 'MAX_MSG_CONSEC_FAIL_COUNT';

         IF l_msg_consecutive_failure_cnt < l_max_msg_consecutive_failure
         THEN
            UPDATE gdg_outbound_messages
               SET send_attempts = send_attempts + 1
             WHERE request_id = p_request_id;
         END IF;
      END IF;
      
      IF (p_internal_code = 'FATAL')
      THEN
         UPDATE gdg_outbound_messages
            SET request_timestamp = SYSDATE
          WHERE request_id = p_request_id;

          gdg_scjse_gateway_outbound_pkg.increment_consec_failures;

      END IF;

      /*
      ** Reset consec fail count after a successful send
      */
      IF (p_internal_code = 'SUCCESS')
      THEN
          /* Only reset the expiry time and "go slow" counter of held messages if still in "go slow" mode */
          IF (gdg_scjse_gateway_outbound_pkg.get_consec_failures > gdg_scjse_gateway_outbound_pkg.get_max_consec_failure_count)
          THEN
              gdg_scjse_gateway_outbound_pkg.reset_expiry_time;
          END IF;
          
          gdg_scjse_gateway_outbound_pkg.reset_consec_failures;
      END IF;

      /*
      ** Finally set the status field.
      ** This will cause the update trigger on
      ** gdg_outbound_messages to be fired.
      */
       UPDATE gdg_outbound_messages
         SET outbound_status_id = 
             l_outbound_status_id
       WHERE request_id = p_request_id;
       
       
      IF (p_failure_text IS NOT NULL)
      THEN
         INSERT INTO gdg_outbound_failures
                     (request_id, failure_code,
                      failure_text, failure_timestamp
                     )
              VALUES (p_request_id, NVL (p_failure_code, '-9999'),
                      p_failure_text, SYSDATE
                     );
      END IF;

      RETURN p_request_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         DBMS_OUTPUT.put_line (SQLERRM (SQLCODE));
         RAISE;
   END update_gdg_outbound_message;


   FUNCTION get_consec_failures  RETURN NUMBER IS
    l_count NUMBER := 0;
   BEGIN
      BEGIN
        select to_number(property_value)
        into   l_count
        from    gdg_config_properties
        WHERE property_timestamp = TRUNC (SYSDATE)
       AND   property_code = 'MSG_CONSEC_FAIL_COUNT';
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
        /* No failures for today */
           null;
      END;

      RETURN l_count;

   END get_consec_failures;

   FUNCTION get_max_consec_failure_count  RETURN NUMBER
   IS
       l_count NUMBER := 0;
   BEGIN
       BEGIN
           select to_number(property_value)
           into   l_count
           from   gdg_config_properties
           WHERE  property_code         = 'MAX_MSG_CONSEC_FAIL_COUNT';
       EXCEPTION
           WHEN NO_DATA_FOUND THEN
               /* No max found.  Use default */
               null;
       END;

       RETURN l_count;
   END get_max_consec_failure_count;


PROCEDURE increment_consec_failures
IS
l_date   DATE := NULL;
BEGIN
   UPDATE    gdg_config_properties
         SET property_value = to_char( to_number(property_value) + 1 )
       WHERE property_timestamp = TRUNC (SYSDATE)
       AND   property_code = 'MSG_CONSEC_FAIL_COUNT'
   RETURNING property_timestamp
        INTO l_date;

   IF (l_date IS NULL)
   THEN
      /* First failure for today */
      UPDATE gdg_config_properties
         SET property_value = '1',
             property_timestamp = TRUNC (SYSDATE)
       WHERE property_code = 'MSG_CONSEC_FAIL_COUNT';
   END IF;
END;


/*
** We have had a succesful send so reset the consec failure count
*/
   PROCEDURE reset_consec_failures
   IS
   BEGIN
     UPDATE gdg_config_properties
         SET property_value     = '0',
             property_timestamp =  TRUNC (SYSDATE)
       WHERE property_code      = 'MSG_CONSEC_FAIL_COUNT';
   END;

/* ===================================================
** reset_expiry_time - Resets the expiry time for all
**                     messages waiting to be sent
** ===================================================
*/
   PROCEDURE reset_expiry_time
   IS
   BEGIN
       UPDATE gdg_jms_messages
          SET expiry_time      =  SYSDATE
        WHERE message_type     = 'OUTBOUND';
   END;

END gdg_scjse_gateway_outbound_pkg;
/
show errors;
