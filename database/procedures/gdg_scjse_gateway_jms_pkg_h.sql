REM Need to drop the array type, before we can ammend the
REM object type

DROP type gdg_jms_message_array;

CREATE OR REPLACE TYPE gdg_jms_message_type AS OBJECT (
    message_id  number(16),
    message_type varchar2(50),
    expiry_time DATE
 );
/


CREATE OR REPLACE TYPE gdg_jms_message_array 
    AS TABLE OF gdg_jms_message_type;
/
    

CREATE OR REPLACE PACKAGE gdg_scjse_gateway_jms_pkg
AS

   PROCEDURE process_outbound_message (
      p_request_id           IN   gdg_outbound_messages.request_id%TYPE,
      p_outbound_status_id   IN   gdg_outbound_messages.outbound_status_id%TYPE,
      p_send_attempts        IN   gdg_outbound_messages.send_attempts%TYPE
   );

   PROCEDURE process_inbound_message (
      p_inbound_message_id   IN   gdg_inbound_messages.inbound_message_id%TYPE
   );

   PROCEDURE create_jms_message (
      p_message_id     IN   gdg_jms_messages.message_id%TYPE,
      p_message_type   IN   gdg_jms_messages.MESSAGE_TYPE%TYPE,
      p_delay          IN   NUMBER
   );

   PROCEDURE get_jms_message_array (
      p_message_type   IN       gdg_jms_messages.MESSAGE_TYPE%TYPE,
      p_msg_array      OUT      gdg_jms_message_array,
      p_count          OUT      NUMBER
   );

   FUNCTION get_jms_message_refcur ( 
      p_message_type   IN       gdg_jms_messages.MESSAGE_TYPE%TYPE )
   RETURN SYS_REFCURSOR;
   
   /*
   ** Assign default values to the globals.
   ** These will be initialised as part of package initialisation
   */
   g_new_status              gdg_outbound_statuses.outbound_status_id%TYPE;
   g_error_status            gdg_outbound_statuses.outbound_status_id%TYPE;
   g_max_send_attempts       NUMBER                                      := 0;
   g_max_lock_attempts       NUMBER                                      := 3;
   g_max_consec_fail_count   NUMBER                                      := 0;
   g_service_avail_send      NUMBER                                      := 0;
   g_service_avail_retry     NUMBER                                      := 5;
   g_service_unavail_send    NUMBER                                      := 10;
   g_service_unavail_retry   NUMBER                                      := 15;
   
   g_bulk_count              NUMBER                                     := 32;
   g_service_avail_bulk_count NUMBER                                    := 32;
   g_service_unavail_bulk_count NUMBER                                   := 1;
   g_config_read_time DATE;
   
   g_serv_current_delay      NUMBER                                      := 10;
   
END gdg_scjse_gateway_jms_pkg;
/

SHOW errors;

