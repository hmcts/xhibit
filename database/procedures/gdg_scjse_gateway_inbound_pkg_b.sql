CREATE OR REPLACE PACKAGE BODY gdg_scjse_gateway_inbound_pkg AS
  /*
   * Get the inbound message record for a given ID
   */
  PROCEDURE get_inbound_message(p_results_out           OUT SYS_REFCURSOR,
                                p_inbound_message_id_in IN  GDG_INBOUND_MESSAGES.inbound_message_id%TYPE) IS
    BEGIN
        OPEN p_results_out FOR
            SELECT i.inbound_message_id ID
                  ,i.request_identifier
                  ,i.source_identifier
                  ,i.destination_identifier
                  ,i.exec_mode
                  ,i.request_timestamp
                  ,c.clob_data
            FROM   GDG_INBOUND_MESSAGES   i
                  ,GDG_INBOUND_CLOBS      c
            WHERE  i.INBOUND_MESSAGE_ID = p_inbound_message_id_in
            AND    i.INBOUND_MESSAGE_ID = c.INBOUND_MESSAGE_ID;
    END get_inbound_message;
  FUNCTION create_inbound_msg_clob  (   p_request_identifier        IN  GDG_INBOUND_MESSAGES.REQUEST_IDENTIFIER%TYPE,
                    p_source_identifier     IN  GDG_INBOUND_MESSAGES.SOURCE_IDENTIFIER%TYPE,
                    p_destination_identifier    IN  GDG_INBOUND_MESSAGES.DESTINATION_IDENTIFIER%TYPE,
                    p_exec_mode         IN  GDG_INBOUND_MESSAGES.EXEC_MODE%TYPE,
                    p_request_timestamp     IN  GDG_INBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                    p_clob_data         IN  GDG_INBOUND_CLOBS.CLOB_DATA%TYPE)
                    RETURN  GDG_INBOUND_MESSAGES.INBOUND_MESSAGE_ID%TYPE IS
v_inbound_message_id    GDG_INBOUND_MESSAGES.INBOUND_MESSAGE_ID%TYPE;
    BEGIN
        SELECT gdg_inbound_clobs_seq.NEXTVAL
            INTO v_inbound_message_id
            FROM DUAL;
        INSERT INTO GDG_INBOUND_CLOBS   (   INBOUND_MESSAGE_ID,
                            CLOB_DATA)
        VALUES              (   v_inbound_message_id,
                            p_clob_data);
                            
        begin
            INSERT INTO GDG_INBOUND_MESSAGES (
                INBOUND_MESSAGE_ID,
                REQUEST_IDENTIFIER,
                SOURCE_IDENTIFIER,
                DESTINATION_IDENTIFIER,
                EXEC_MODE,
                REQUEST_TIMESTAMP)
            VALUES (
                v_inbound_message_id,
                p_request_identifier,
                p_source_identifier,
                p_destination_identifier,
                p_exec_mode,
                p_request_timestamp);
        exception
            when DUP_VAL_ON_INDEX
            then raise_application_error(-20201, 'An insert was attempted into GDG_INBOUND_MESSAGES for a REQUEST_IDENTIFIER/SOURCE_IDENTIFIER that has already been used');
        end;
        
        RETURN v_inbound_message_id;
                 
    END create_inbound_msg_clob;

  FUNCTION increment_malformed_xml_count return gdg_config_properties.property_value%type IS
      v_malformed_xml_limit NUMBER;
      v_malformed_xml_count NUMBER;
      r_property_value      gdg_config_properties.property_value%type;
  BEGIN
      begin
          select to_number(property_value)
          into   v_malformed_xml_limit
          from   gdg_config_properties
          where  property_code         = 'MSG_MALFORMED_XML_FAIL_LIMIT';
      exception
          when no_data_found
          then v_malformed_xml_limit := g_malformed_xml_limit;
      end;

      select to_number(property_value)
      into   v_malformed_xml_count
      from   gdg_config_properties
      where  property_code             = 'MSG_MALFORMED_XML_FAIL_COUNT'
      and    trunc(property_timestamp) =  trunc(sysdate);

      if v_malformed_xml_limit <= 0 
      or v_malformed_xml_count <  v_malformed_xml_limit
      then
          update    gdg_config_properties
          set       property_value        =  to_char(v_malformed_xml_count + 1)
          where     property_code         = 'MSG_MALFORMED_XML_FAIL_COUNT'
          returning property_value
          into      r_property_value;
          
          return    r_property_value;
      else
          raise_application_error(-20001, 'Message Malformed XML Failure Limit <' || v_malformed_xml_limit || '> has been exceeded');
      end if;
      
      exception
          when no_data_found
          then
              update    gdg_config_properties
              set       property_value        = '1'
                       ,property_timestamp    =  sysdate
              where     property_code         = 'MSG_MALFORMED_XML_FAIL_COUNT';
          
              return   '1';
  END increment_malformed_xml_count;
  
END gdg_scjse_gateway_inbound_pkg;
/
show errors;
