CREATE OR REPLACE PACKAGE BODY xhb_message_broker_pkg AS
/*
 *
 * GET_SELECTORS
 *
 */
    PROCEDURE get_selectors(p_results_out    OUT SYS_REFCURSOR) IS

    BEGIN

        OPEN p_results_out FOR
        
            SELECT   selector_id         selector_id
                    ,selector            selector
                    ,description         description
                    ,enabled             enabled
                    ,precedence          precedence
            FROM     xhb_selectors
            WHERE    enabled          = 'Y';

    END get_selectors;

/*
 *
 * GET_QUEUES_BY_SELECTOR_ID
 *
 */
    PROCEDURE get_queues_by_selector_id(p_results_out    OUT SYS_REFCURSOR,
                                        p_selector_id_in IN  XHB_SELECTOR_QUEUES.SELECTOR_ID%TYPE) IS

    BEGIN

        OPEN p_results_out FOR

            SELECT   q.queue_id             queue_id
                    ,q.jndi_name            jndi_name
                    ,q.description          description
            FROM     xhb_selector_queues    sq
                    ,xhb_queues             q
            WHERE    sq.selector_id      =  p_selector_id_in
            AND      sq.queue_id         =  q.queue_id;
            
    END get_queues_by_selector_id;

END xhb_message_broker_pkg;
/
show errors
