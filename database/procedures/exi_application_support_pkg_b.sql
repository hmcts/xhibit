CREATE OR REPLACE PACKAGE BODY exi_application_support_pkg AS

    /*
     * Get tracking by item ID
     */
    PROCEDURE get_tracking_by_item_id(p_results_out OUT SYS_REFCURSOR,
                                      p_item_id      IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE)
    IS
    BEGIN
	OPEN p_results_out FOR
	    select rt.internal_code itemtype,
	           rt.internal_name itemdesc,
		   io.identifier,
		   io.crest_court_id,
		   io.description,
		   to_char(io.item_created,'YYYY-MM-DD HH24:MI:SS') created,
		   to_char(io.item_expires,'YYYY-MM-DD HH24:MI:SS') expires,
	           to_char(iot.tracking_date,'YYYY-MM-DD HH24:MI:SS') tracking_date,
		   rts.internal_code
	    from   exi_ref_type               rt,
		   exi_ref_tracking_status    rts,
		   exi_item_outbound_tracking iot,
		   exi_item_outbound          io
	    where  io.item_id    = p_item_id
	    and    io.type_id    = rt.type_id
	    and    io.item_id    = iot.item_id(+)
	    and    iot.status_id = rts.status_id(+)
	    order by
		   decode(rts.INTERNAL_CODE, 'NEW_ITEM', 0, 9),
		   iot.TRACKING_DATE,
		   iot.tracking_id;

    END get_tracking_by_item_id;

    /*
     * Get all tracking statuses
     */
    PROCEDURE get_all_tracking_statuses(p_results_out   OUT SYS_REFCURSOR)
    IS    
    BEGIN
        OPEN p_results_out FOR
	    select rts.internal_code,
	           rts.internal_name
            from   exi_ref_tracking_status    rts
            order by
                   rts.internal_code;
                   
    END get_all_tracking_statuses;

    /*
     * Get items by tracking status between two dates
     */
    PROCEDURE get_items_by_tracking_status(p_results_out   OUT SYS_REFCURSOR,
                                           p_internal_code  IN EXI_REF_TRACKING_STATUS.INTERNAL_CODE%TYPE,
                                           p_start_date     IN EXI_ITEM_OUTBOUND.ITEM_CREATED%TYPE,
                                           p_end_date       IN EXI_ITEM_OUTBOUND.ITEM_CREATED%TYPE)
    IS    
    BEGIN
        OPEN p_results_out FOR
	    select io.item_id, 
	           rt.internal_code || ' - ' || rt.internal_name item_type,
	           io.identifier,
	           io.crest_court_id,
	           io.description,
		   to_char(io.item_created,'YYYY-MM-DD HH24:MI:SS') created,
		   to_char(io.item_expires,'YYYY-MM-DD HH24:MI:SS') expires,
		   rts.internal_code
            from   exi_item_outbound          io, 
                   exi_ref_type               rt,
                   exi_item_outbound_tracking iot, 
                   exi_ref_tracking_status    rts
            where  rts.internal_code like upper(p_internal_code)
            and    rts.status_id     =    iot.status_id
            and    iot.ITEM_ID       =    io.ITEM_ID
            and    io.type_id        =    rt.type_id
	    and  ((p_start_date is null or trunc(io.item_created) >= p_start_date)
	    and   (p_end_date   is null or trunc(io.item_created) <= p_end_date))
            order by
                   rts.internal_code,
                   io.item_created,
                   io.item_id;
                   
    END get_items_by_tracking_status;

    /*
     * Get item by item ID
     */
    PROCEDURE get_item_by_item_id(p_results_out OUT SYS_REFCURSOR,
                                  p_item_id      IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE)
    IS
    BEGIN
	OPEN p_results_out FOR
	    select rt.internal_code itemtype,
		   rt.internal_name itemdesc, 
		   io.identifier,
		   io.crest_court_id,
		   io.description,
		   to_char(io.item_created,'YYYY-MM-DD HH24:MI:SS') created,
		   to_char(io.item_expires,'YYYY-MM-DD HH24:MI:SS') expires,
		   io.clob_data
	    from   exi_ref_type               rt,
		   exi_item_outbound          io
	    where  io.item_id    = p_item_id
	    and    io.type_id    = rt.type_id;

    END get_item_by_item_id;

    /*
     * Get inbound exceptions between two dates
     */
    PROCEDURE get_inbound_exceptions(p_results_out  OUT SYS_REFCURSOR,
                                     p_start_date    IN EXI_ITEM_INBOUND.DATE_CREATED%TYPE,
                                     p_end_date      IN EXI_ITEM_INBOUND.DATE_CREATED%TYPE)
    IS    
    BEGIN
	OPEN p_results_out FOR
            select iip.property_value,
                   to_char(ii.date_created,'YYYY-MM-DD HH24:MI:SS') date_created,
                   ii.item_id
            from   exi_item_inbound               ii,
                   exi_item_inbound_properties    iip
            where  ii.item_id                   = iip.item_id(+)
            and   'SCJMessageTypeType'          = iip.PROPERTY_NAME(+)
            and    exists (select 1
                           from   exi_item_inbound_properties x
	                   where  ii.item_id       =  x.ITEM_ID
		 	   and    x.property_name  = 'SCJType'
			   and    x.property_value = 'EXCEPTION')
	    and  ((p_start_date is null or trunc(ii.date_created) >= p_start_date)
	    and   (p_end_date   is null or trunc(ii.date_created) <= p_end_date))
            order by
                   iip.PROPERTY_VALUE,
 	           ii.date_created,
 	           ii.item_id;

    END get_inbound_exceptions;
    
    /*
     * Get inbound properties by correlation ID between two dates
     */
    PROCEDURE get_inbound_props_by_corr_id(p_results_out  OUT SYS_REFCURSOR,
                                           p_corr_id       IN EXI_ITEM_INBOUND_PROPERTIES.PROPERTY_VALUE%TYPE,
                                           p_start_date    IN EXI_ITEM_INBOUND.DATE_CREATED%TYPE,
                                           p_end_date      IN EXI_ITEM_INBOUND.DATE_CREATED%TYPE)
    IS    
    BEGIN
        OPEN p_results_out FOR
            select ii.item_id, 
                   to_char(ii.date_created,'YYYY-MM-DD HH24:MI:SS') date_created,
                   iip.property_name,
                   iip.property_value
	    from   exi_item_inbound            ii,
	           exi_item_inbound_properties iip 
	    where  ii.item_id   =  iip.item_id
	    and  ((p_start_date is null or trunc(ii.date_created) >= p_start_date)
	    and   (p_end_date   is null or trunc(ii.date_created) <= p_end_date))
	    and    iip.item_id in (select x.item_id
	                           from   exi_item_inbound_properties x
	                           where  x.property_name  = 'SCJCorrelationId' 
	                           and    x.property_value =  p_corr_id)
            order by
                   ii.item_id,
                   iip.property_name;
                   
    END get_inbound_props_by_corr_id;

    /*
     * Get inbound details by item_id
     */
    PROCEDURE get_inbound_by_item_id(p_results_out  OUT SYS_REFCURSOR,
                                     p_item_id       IN EXI_ITEM_INBOUND.ITEM_ID%TYPE)
    IS
    BEGIN
        OPEN p_results_out FOR
            select ii.item_id, 
                   to_char(ii.date_created,'YYYY-MM-DD HH24:MI:SS') date_created,
                   ii.message
            from   exi_item_inbound ii
            where  ii.item_id        = p_item_id;

    END get_inbound_by_item_id;

    /*
     * Get inbound properties by item_id
     */
    PROCEDURE get_inbound_props_by_item_id(p_results_out  OUT SYS_REFCURSOR,
                                           p_item_id       IN EXI_ITEM_INBOUND_PROPERTIES.ITEM_ID%TYPE)
    IS    
    BEGIN
        OPEN p_results_out FOR
            select iip.property_name,
                   iip.property_value
	    from   exi_item_inbound_properties iip 
	    where  iip.item_id                   = p_item_id
            order by
                   iip.property_name;

    END get_inbound_props_by_item_id;

    /*
     * Get outbound failures between two dates
     */
    PROCEDURE get_outbound_failures(p_results_out  OUT SYS_REFCURSOR,
                                    p_start_date    IN EXI_ITEM_OUTBOUND.ITEM_CREATED%TYPE,
                                    p_end_date      IN EXI_ITEM_OUTBOUND.ITEM_CREATED%TYPE)
    IS
    BEGIN
        OPEN p_results_out FOR
            select io.item_id, 
                   rt.internal_code || ' - ' || rt.internal_name item_type,
                   io.identifier,
                   io.crest_court_id,
                   io.description,
                   to_char(io.item_created,'YYYY-MM-DD HH24:MI:SS') created,
                   to_char(io.item_expires,'YYYY-MM-DD HH24:MI:SS') expires
            from   exi_item_outbound   io,
                   exi_ref_type        rt
            where  io.type_id        = rt.type_id
            and    exists  (
                       select 1
                       from   exi_item_outbound_tracking x, 
                              exi_ref_tracking_status    y
                       where  io.item_id  = x.item_id
                       and    x.status_id = y.status_id
                       and    y.internal_code in (
                             'UNDELIVERABLE',
                             'SCJSE_MESSAGE_GDDB_ERROR',
                             'SCJSE_MESSAGE_SENT_FATAL',
                             'SCJSE_MESSAGE_SENT_ERROR'
                      )
                  )
            and   not exists  (
                       select 1
                       from   exi_item_outbound_tracking x, 
                              exi_ref_tracking_status    y
                       where  io.item_id  = x.item_id
                       and    x.status_id = y.status_id
                       and    y.internal_code in (
                             'SCJSE_MESSAGE_SENT_OK'
                      )
                  )
	    and  ((p_start_date is null or trunc(io.item_created) >= p_start_date)
	    and   (p_end_date   is null or trunc(io.item_created) <= p_end_date))
            order by
                   io.item_created,
                   io.item_id;

    END get_outbound_failures;

    /*
     * Get tracking counts by date between two dates
     */
    PROCEDURE get_tracking_counts_by_date(p_results_out  OUT SYS_REFCURSOR,
                                          p_start_date    IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE,
                                          p_end_date      IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE)
    IS
    BEGIN
        OPEN p_results_out FOR
            select trunc(iot.tracking_date) tracking_date,
                   rts.internal_code,
                   count(*) count_by_status
            from   exi_item_outbound_tracking   iot,
                   exi_ref_tracking_status      rts
            where  iot.status_id              = rts.status_id
            and  ((p_start_date is null or trunc(iot.tracking_date) >= p_start_date)
            and   (p_end_date   is null or trunc(iot.tracking_date) <= p_end_date))
            group by
                   trunc(iot.tracking_date),
                   rts.internal_code;
                   
    END get_tracking_counts_by_date;

    /*
     * Get tracking counts between two dates
     */
    PROCEDURE get_tracking_counts(p_results_out  OUT SYS_REFCURSOR,
                                  p_start_date    IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE,
                                  p_end_date      IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE)
    IS
    BEGIN
        OPEN p_results_out FOR
            select rts.internal_code,
                   count(*) count_by_status
            from   exi_item_outbound_tracking   iot,
                   exi_ref_tracking_status      rts
            where  iot.status_id              = rts.status_id
            and  ((p_start_date is null or trunc(iot.tracking_date) >= p_start_date)
            and   (p_end_date   is null or trunc(iot.tracking_date) <= p_end_date))
            group by
                   rts.internal_code;

    END get_tracking_counts;

    /*
     * Resend a message to EXISS using item_id
     */
    FUNCTION resend_message_to_exiss(p_item_id IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE,
                                     p_target  IN EXI_JMS_MESSAGE.TARGET%TYPE   ) RETURN NUMBER
    IS
        l_rowcount number;
    BEGIN
        insert
        into   exi_jms_message
              (item_id,
               target,
               item_type)
        select i.item_id,
               nvl(upper(p_target),'EXISS'),
               decode(o.internal_code,'DOCUMENT',t.internal_code,'DELIVERERROR',t.internal_code,'EVENT')
        from   exi_item_outbound   i,
               exi_ref_type        t,
               exi_ref_operation   o
        where  i.item_id         = p_item_id
        and    i.type_id         = t.type_id
        and    t.operation_id    = o.operation_id;
    
        l_rowcount := sql%rowcount;
        
        commit;
        
        return l_rowcount;
        
    END resend_message_to_exiss;
    
END exi_application_support_pkg;
/

show errors;