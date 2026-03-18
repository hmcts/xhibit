CREATE OR REPLACE PACKAGE gdg_application_support_pkg AS

    /*
     * Get outbound failures between two dates
     */
    PROCEDURE get_outbound_failures(p_results_out  OUT SYS_REFCURSOR,
                                    p_start_date    IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                                    p_end_date      IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE);

    /*
     * Get outbound details by request_id
     */
    PROCEDURE get_outbound_by_request_id(p_results_out  OUT SYS_REFCURSOR,
                                         p_request_id    IN GDG_OUTBOUND_MESSAGES.REQUEST_ID%TYPE);

    /*
     * Get status counts by date between two dates
     */
    PROCEDURE get_status_counts_by_date(p_results_out  OUT SYS_REFCURSOR,
                                        p_start_date    IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                                        p_end_date      IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE);

    /*
     * Get status counts between two dates
     */
    PROCEDURE get_status_counts(p_results_out  OUT SYS_REFCURSOR,
                                p_start_date    IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                                p_end_date      IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE);

    /*
     * Remove successful outbound records
     */
    FUNCTION remove_outbound_records RETURN NUMBER;

    /*
     * Remove inbound records
     */
    FUNCTION remove_inbound_records RETURN NUMBER;

    /*
     * Get inbound counts between two dates
     */
    PROCEDURE get_inbound_counts(p_results_out  OUT SYS_REFCURSOR,
                                 p_start_date    IN GDG_INBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                                 p_end_date      IN GDG_INBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE);

    /*
     * Remove outbound record by request_id
     */
    FUNCTION remove_outbound_record_by_id(p_request_id IN GDG_OUTBOUND_MESSAGES.REQUEST_ID%TYPE) RETURN NUMBER;

END gdg_application_support_pkg;
/

show errors;