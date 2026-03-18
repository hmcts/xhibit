CREATE OR REPLACE PACKAGE exi_flow_to_exiss_pkg AS

  /*
   * Get all Tracking Statuses
   */
    PROCEDURE get_tracking_statuses(p_results_out    OUT SYS_REFCURSOR);
  
  /*
   * Get all Queue rows for a given Selector
   */
    PROCEDURE get_tracking_status_by_code(p_results_out OUT SYS_REFCURSOR,
                                            p_code_in      IN EXI_REF_TRACKING_STATUS.INTERNAL_CODE%TYPE);
  
  /*
   * Get clob data for message payload for a given item
   */
    PROCEDURE get_message_payload(p_clob_data  OUT SYS_REFCURSOR,
                                   p_item_id    IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE);

  /*
   * Get all JMS properties for a given item
   */
    PROCEDURE get_message_properties(p_properties OUT SYS_REFCURSOR,
                                      p_item_id    IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE);
  /*
   * Insert Tracking Statuses to Item Outbound Tracking
   */
    FUNCTION insert_item_outbound_tracking(p_item_id_in        IN EXI_ITEM_OUTBOUND_TRACKING.ITEM_ID%TYPE,
                                           p_internal_code_in  IN EXI_REF_TRACKING_STATUS.INTERNAL_CODE%TYPE,
                                           p_tracking_date_in  IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE)
                                           RETURN EXI_ITEM_OUTBOUND_TRACKING.tracking_id%TYPE;                                        
END exi_flow_to_exiss_pkg;
/
show errors
