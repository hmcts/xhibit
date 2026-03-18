CREATE OR REPLACE PACKAGE BODY exi_flow_to_xhibit_pkg AS

  /*
   * Create an "inbound message" record
   */
    FUNCTION create_item_inbound_record(p_message_in IN exi_item_inbound.message%type)
                                        RETURN exi_item_inbound.item_id%type
    IS
        v_item_id exi_item_inbound.item_id%TYPE;
    BEGIN
        INSERT INTO exi_item_inbound
            (message
            ,date_created)
        VALUES
            (p_message_in
            ,sysdate)
        RETURNING item_id 
        INTO      v_item_id;
            
        RETURN v_item_id;
            
    END create_item_inbound_record;
    
    /*
     * Create an "inbound message property" record
     */
    FUNCTION create_item_inbound_props(p_item_id_in        IN exi_item_inbound_properties.item_id%type
                                      ,p_property_name_in  IN exi_item_inbound_properties.property_name%type
                                      ,p_property_value_in IN exi_item_inbound_properties.property_value%type)
                                       RETURN exi_item_inbound_properties.item_property_id%type
    IS
        v_item_property_id exi_item_inbound_properties.item_property_id%TYPE;
    BEGIN
        INSERT INTO exi_item_inbound_properties
            (item_id, property_name, property_value)
        VALUES
            (p_item_id_in, p_property_name_in, p_property_value_in)
        RETURNING item_property_id 
        INTO      v_item_property_id;
            
        RETURN v_item_property_id;
            
    END create_item_inbound_props;

    /*
     * Create an "item outbound" record
     */
    FUNCTION create_item_outbound_record(p_internal_code_in  IN exi_ref_type.internal_code%type,
                                         p_identifier_in     IN exi_item_outbound.identifier%type,
                                         p_crest_court_id_in IN exi_item_outbound.crest_court_id%type,
                                         p_description_in    IN exi_item_outbound.description%type,
                                         p_item_created_in   IN exi_item_outbound.item_created%type,
                                         p_item_expires_in   IN exi_item_outbound.item_expires%type,
                                         p_clob_data_in      IN exi_item_outbound.clob_data%type)
                                         RETURN exi_item_outbound.item_id%type
    IS
        v_type_id exi_ref_type.type_id%type;
        v_item_id exi_item_outbound.item_id%TYPE;
    BEGIN
        select type_id
        into   v_type_id
        from   exi_ref_type
        where  internal_code = p_internal_code_in;

        INSERT INTO exi_item_outbound
            (type_id
            ,identifier
            ,crest_court_id
            ,description
            ,item_created
            ,item_expires
            ,clob_data)
        VALUES
            (v_type_id
            ,p_identifier_in
            ,p_crest_court_id_in
            ,p_description_in
            ,p_item_created_in
            ,p_item_expires_in
            ,p_clob_data_in)
        RETURNING item_id 
        INTO      v_item_id;
           
        RETURN v_item_id;
            
    END create_item_outbound_record;

END exi_flow_to_xhibit_pkg;
/
show errors
