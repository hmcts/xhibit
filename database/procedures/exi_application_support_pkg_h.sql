CREATE OR REPLACE PACKAGE exi_application_support_pkg AS

    /*
     * Get tracking by item ID
     */
    PROCEDURE get_tracking_by_item_id(p_results_out OUT SYS_REFCURSOR,
                                      p_item_id      IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE);

    /*
     * Get all tracking statuses
     */
    PROCEDURE get_all_tracking_statuses(p_results_out   OUT SYS_REFCURSOR);

    /*
     * Get items by tracking status between two dates
     */
    PROCEDURE get_items_by_tracking_status(p_results_out   OUT SYS_REFCURSOR,
                                           p_internal_code  IN EXI_REF_TRACKING_STATUS.INTERNAL_CODE%TYPE,
                                           p_start_date     IN EXI_ITEM_OUTBOUND.ITEM_CREATED%TYPE,
                                           p_end_date       IN EXI_ITEM_OUTBOUND.ITEM_CREATED%TYPE);

    /*
     * Get item by item ID
     */
    PROCEDURE get_item_by_item_id(p_results_out OUT SYS_REFCURSOR,
                                  p_item_id      IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE);

    /*
     * Get inbound exceptions between two dates
     */
    PROCEDURE get_inbound_exceptions(p_results_out  OUT SYS_REFCURSOR,
                                     p_start_date    IN EXI_ITEM_INBOUND.DATE_CREATED%TYPE,
                                     p_end_date      IN EXI_ITEM_INBOUND.DATE_CREATED%TYPE);
    
    /*
     * Get inbound properties by correlation ID between two dates
     */
    PROCEDURE get_inbound_props_by_corr_id(p_results_out  OUT SYS_REFCURSOR,
                                           p_corr_id       IN EXI_ITEM_INBOUND_PROPERTIES.PROPERTY_VALUE%TYPE,
                                           p_start_date    IN EXI_ITEM_INBOUND.DATE_CREATED%TYPE,
                                           p_end_date      IN EXI_ITEM_INBOUND.DATE_CREATED%TYPE);

    /*
     * Get inbound details by item_id
     */
    PROCEDURE get_inbound_by_item_id(p_results_out  OUT SYS_REFCURSOR,
                                     p_item_id       IN EXI_ITEM_INBOUND.ITEM_ID%TYPE);

    /*
     * Get inbound properties by item_id
     */
    PROCEDURE get_inbound_props_by_item_id(p_results_out  OUT SYS_REFCURSOR,
                                           p_item_id       IN EXI_ITEM_INBOUND_PROPERTIES.ITEM_ID%TYPE);

    /*
     * Get outbound failures between two dates
     */
    PROCEDURE get_outbound_failures(p_results_out  OUT SYS_REFCURSOR,
                                    p_start_date    IN EXI_ITEM_OUTBOUND.ITEM_CREATED%TYPE,
                                    p_end_date      IN EXI_ITEM_OUTBOUND.ITEM_CREATED%TYPE);

    /*
     * Get tracking counts by date between two dates
     */
    PROCEDURE get_tracking_counts_by_date(p_results_out  OUT SYS_REFCURSOR,
                                          p_start_date    IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE,
                                          p_end_date      IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE);

    /*
     * Get tracking counts between two dates
     */
    PROCEDURE get_tracking_counts(p_results_out  OUT SYS_REFCURSOR,
                                  p_start_date    IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE,
                                  p_end_date      IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE);

    /*
     * Resend a message to EXISS using item_id
     */
    FUNCTION resend_message_to_exiss(p_item_id IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE,
                                     p_target  IN EXI_JMS_MESSAGE.TARGET%TYPE   ) RETURN NUMBER;

END exi_application_support_pkg;
/

show errors;