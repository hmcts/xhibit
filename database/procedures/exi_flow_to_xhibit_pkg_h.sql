CREATE OR REPLACE PACKAGE exi_flow_to_xhibit_pkg AS
    /*
     * Create an "inbound message" record
     */
    FUNCTION create_item_inbound_record(p_message_in IN exi_item_inbound.message%type)
                                        RETURN exi_item_inbound.item_id%type;

    /*
     * Create an "inbound message property" record
     */
    FUNCTION create_item_inbound_props(p_item_id_in        IN exi_item_inbound_properties.item_id%type
                                      ,p_property_name_in  IN exi_item_inbound_properties.property_name%type
                                      ,p_property_value_in IN exi_item_inbound_properties.property_value%type)
                                       RETURN exi_item_inbound_properties.item_property_id%type;

    /*
     * Create an "item outbound" record
     */
    FUNCTION create_item_outbound_record(p_internal_code_in  IN exi_ref_type.internal_code%type
                                        ,p_identifier_in     IN exi_item_outbound.identifier%type
                                        ,p_crest_court_id_in IN exi_item_outbound.crest_court_id%type
                                        ,p_description_in    IN exi_item_outbound.description%type
                                        ,p_item_created_in   IN exi_item_outbound.item_created%type
                                        ,p_item_expires_in   IN exi_item_outbound.item_expires%type
                                        ,p_clob_data_in      IN exi_item_outbound.clob_data%type)
                                         RETURN exi_item_outbound.item_id%type;

END exi_flow_to_xhibit_pkg;
/
show errors
