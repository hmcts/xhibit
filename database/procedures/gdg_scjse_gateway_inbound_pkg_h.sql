CREATE OR REPLACE PACKAGE gdg_scjse_gateway_inbound_pkg AS
  /*
   * Get the inbound message record for a given ID
   */
  PROCEDURE get_inbound_message(p_results_out           OUT SYS_REFCURSOR,
                                p_inbound_message_id_in IN  GDG_INBOUND_MESSAGES.inbound_message_id%TYPE);
  /*
   * Create inbound mesage and clob records with given identifiers
   */
  FUNCTION create_inbound_msg_clob (    p_request_identifier        IN  GDG_INBOUND_MESSAGES.REQUEST_IDENTIFIER%TYPE,
                    p_source_identifier     IN  GDG_INBOUND_MESSAGES.SOURCE_IDENTIFIER%TYPE,
                    p_destination_identifier    IN  GDG_INBOUND_MESSAGES.DESTINATION_IDENTIFIER%TYPE,
                    p_exec_mode         IN  GDG_INBOUND_MESSAGES.EXEC_MODE%TYPE,
                    p_request_timestamp     IN  GDG_INBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                    p_clob_data         IN  GDG_INBOUND_CLOBS.CLOB_DATA%TYPE)
                    RETURN  GDG_INBOUND_MESSAGES.INBOUND_MESSAGE_ID%TYPE;
  
  FUNCTION increment_malformed_xml_count return gdg_config_properties.property_value%type;
  
  g_malformed_xml_limit number := 2000;
END gdg_scjse_gateway_inbound_pkg;
/
show errors;

