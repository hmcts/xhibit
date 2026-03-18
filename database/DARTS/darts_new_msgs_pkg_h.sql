CREATE OR REPLACE PACKAGE darts_new_msgs_pkg
AS

   PROCEDURE get_darts_message_array  (
      p_msg_array      OUT      darts_message_array,
      p_count          OUT      NUMBER
   );

   FUNCTION get_darts_message_refcur    
   RETURN SYS_REFCURSOR;

   FUNCTION insert_darts_message_retry( message_id_in        IN DAR_NEW_MESSAGES.MESSAGE_ID%TYPE,
                                         xhibit_msg_code_in   IN DAR_NEW_MESSAGES.XHIBIT_MESSAGE_CODE%TYPE,
                                         exiss_msg_code_in    IN DAR_NEW_MESSAGES.EXISS_MESSAGE_CODE%TYPE,
                                         payload_in           IN DAR_NEW_MESSAGES.PAYLOAD%TYPE,
                                         retry_count_in       IN DAR_NEW_MESSAGES.RETRY_COUNT%TYPE,
                                         next_retry_time_in   IN DAR_NEW_MESSAGES.NEXT_RETRY_TIME%TYPE,
                                         creation_date_in     IN DAR_NEW_MESSAGES.CREATION_DATE%TYPE,
                                         last_update_date_in  IN DAR_NEW_MESSAGES.LAST_UPDATE_DATE%TYPE
   ) RETURN NUMBER;


   /*
   ** Assign default values to the globals.
   ** These will be initialised as part of package initialisation
   */
   g_max_lock_attempts       NUMBER                                      := 3;
   g_bulk_count              NUMBER                                      := 32;
   g_config_next_update        DATE                                      := SYSDATE;
   g_darts_retry_interval    NUMBER                                      := 450000;

END darts_new_msgs_pkg;
/