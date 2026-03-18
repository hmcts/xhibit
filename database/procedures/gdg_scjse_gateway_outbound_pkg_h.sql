CREATE OR REPLACE PACKAGE gdg_scjse_gateway_outbound_pkg
AS
   /*
    * CREATE OUTBOUND_CLOB and MESSAGE
    */
   FUNCTION create_gdg_outbound_clob_msg (
      p_request_id    IN   gdg_outbound_clobs.request_id%TYPE,
      p_source_id     IN   gdg_outbound_messages.source_identifier%type,
      p_dest_id       IN   gdg_outbound_messages.destination_identifier%type,
      p_exec_mode     IN   gdg_outbound_messages.exec_mode%type,
      p_request_time  IN   gdg_outbound_messages.request_timestamp%type,
      p_send_attempts IN   gdg_outbound_messages.send_attempts%type,
      p_clob_in       IN   gdg_outbound_clobs.clob_data%TYPE
   )
      RETURN gdg_outbound_clobs.request_id%TYPE;

   /*
   * READ OUTBOUND MESSAGE AND CLOB
   */
   PROCEDURE read_outbound_message_and_clob (
      p_gdg_outbound_msg_properties   OUT      sys_refcursor,
      p_request_id                    IN       gdg_outbound_messages.request_id%TYPE
   );

   /*
    * Update OUTBOUND MESSAGE
    */
   FUNCTION update_gdg_outbound_message (
      p_request_id           IN   gdg_outbound_messages.request_id%TYPE,
      p_internal_code        IN   gdg_outbound_statuses.internal_code%TYPE,
      p_failure_code         IN   gdg_outbound_failures.failure_code%TYPE,
      p_failure_text         IN   gdg_outbound_failures.failure_text%TYPE
   )
      RETURN gdg_outbound_messages.request_id%TYPE;
    
    /*
    ** Get consecutive failure count
    */  
    FUNCTION get_consec_failures  RETURN NUMBER;

    /*
    ** Get max consecutive failure count
    */  
    FUNCTION get_max_consec_failure_count RETURN NUMBER;
    
    /*
    ** Increment consecutive failure count
    */
    PROCEDURE increment_consec_failures;

    /*
    ** Reset consecutive failure count to 0
    */
    PROCEDURE reset_consec_failures;

    PROCEDURE reset_expiry_time;   

END gdg_scjse_gateway_outbound_pkg;
/
show errors;

