CREATE OR REPLACE PACKAGE BODY gdg_application_support_pkg AS

    /*
     * Get outbound failures between two dates
     */
    PROCEDURE get_outbound_failures(p_results_out  OUT SYS_REFCURSOR,
                                    p_start_date    IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                                    p_end_date      IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE)
    IS
        l_max_send_attempts number;
    BEGIN
        BEGIN
            select to_number(property_value)
            into   l_max_send_attempts
            from   gdg_config_properties
            where  property_code = 'MAX_MSG_ATTEMPTS';
        EXCEPTION
            WHEN OTHERS THEN
                l_max_send_attempts := 3;
        END;

        OPEN p_results_out FOR
            select m.request_id, 
                   m.source_identifier,
                   m.destination_identifier, 
                   m.exec_mode,
                   to_char(m.request_timestamp,'YYYY-MM-DD HH24:MI:SS') request_timestamp, 
                   s.internal_code, 
                   m.send_attempts,
                   f.failure_code, 
                   f.failure_text, 
                   to_char(f.failure_timestamp,'YYYY-MM-DD HH24:MI:SS') failure_timestamp
            from   gdg_outbound_statuses    s,
                   gdg_outbound_messages    m,
                   gdg_outbound_failures    f
            where  m.OUTBOUND_STATUS_ID  =  s.OUTBOUND_STATUS_ID
            and   (s.INTERNAL_CODE       = 'FATAL'
            or     m.send_attempts       >  l_max_send_attempts)
            and    m.REQUEST_ID          =  f.request_id(+)
	    and  ((p_start_date is null or trunc(m.request_timestamp) >= p_start_date)
	    and   (p_end_date   is null or trunc(m.request_timestamp) <= p_end_date))
            order by
                   m.request_id, 
                   m.source_identifier,
                   m.destination_identifier, 
                   m.exec_mode,
                   request_timestamp;
            
    END get_outbound_failures;

    /*
     * Get outbound details by request_id
     */
    PROCEDURE get_outbound_by_request_id(p_results_out  OUT SYS_REFCURSOR,
                                         p_request_id    IN GDG_OUTBOUND_MESSAGES.REQUEST_ID%TYPE)
    IS
    BEGIN
        OPEN p_results_out FOR
            select m.request_id, 
                   m.source_identifier,
                   m.destination_identifier, 
                   m.exec_mode,
                   to_char(m.request_timestamp,'YYYY-MM-DD HH24:MI:SS') request_timestamp, 
                   s.internal_code, 
                   m.send_attempts,
                   c.clob_data
            from   gdg_outbound_statuses    s,
                   gdg_outbound_messages    m,
                   gdg_outbound_clobs       c
            where  m.request_id          =  p_request_id
            and    m.outbound_status_id  =  s.outbound_status_id
            and    m.request_id          =  c.request_id;

    END get_outbound_by_request_id;

    /*
     * Get status counts by date between two dates
     */
    PROCEDURE get_status_counts_by_date(p_results_out  OUT SYS_REFCURSOR,
                                        p_start_date    IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                                        p_end_date      IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE)
    IS
    BEGIN
        OPEN p_results_out FOR
            select trunc(om.request_timestamp) request_timestamp,
                   os.internal_code,
                   count(*) count_by_status
            from   gdg_outbound_messages om,
                   gdg_outbound_statuses os
            where  om.outbound_status_id = os.outbound_status_id
	    and  ((p_start_date is null or trunc(om.request_timestamp) >= p_start_date)
	    and   (p_end_date   is null or trunc(om.request_timestamp) <= p_end_date))
            group by 
                   trunc(om.request_timestamp),
                   os.internal_code;
                   
    END get_status_counts_by_date;

    /*
     * Get status counts between two dates
     */
    PROCEDURE get_status_counts(p_results_out  OUT SYS_REFCURSOR,
                                p_start_date    IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                                p_end_date      IN GDG_OUTBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE)
    IS
    BEGIN
        OPEN p_results_out FOR
            select os.internal_code,
                   count(*) count_by_status
            from   gdg_outbound_messages om,
                   gdg_outbound_statuses os
            where  om.outbound_status_id = os.outbound_status_id
	    and  ((p_start_date is null or trunc(om.request_timestamp) >= p_start_date)
	    and   (p_end_date   is null or trunc(om.request_timestamp) <= p_end_date))
            group by 
                   os.internal_code;
                   
    END get_status_counts;

    /*
     * Remove successful outbound records
     */
    FUNCTION remove_outbound_records RETURN NUMBER
    IS
        l_successful_storage_time number;
        l_rowcount                number;
    BEGIN
        BEGIN
            select to_number(p.property_value)
            into   l_successful_storage_time
            from   gdg_config_properties p
            where  p.property_code = 'SUCCESSFUL_STORAGE_TIME';
        EXCEPTION
            WHEN OTHERS THEN
                l_successful_storage_time := 3;
        END;

        /* delete from failures table as this has a foreign key to messages */
        delete
        from   gdg_outbound_failures f
        where  exists
              (select 1
               from   gdg_outbound_messages         m
               where  f.request_id               =  m.request_id
               and    trunc(m.request_timestamp) <  trunc(sysdate) - l_successful_storage_time
               and    m.outbound_status_id       = (select s.outbound_status_id 
                                                    from gdg_outbound_statuses s 
                                                    where  s.internal_code = 'SUCCESS'));

        /* delete from messages table as this has a foreign key to clobs */
        delete
        from   gdg_outbound_messages         m
        where  trunc(m.request_timestamp) <  trunc(sysdate) - l_successful_storage_time
        and    m.outbound_status_id       = (select s.outbound_status_id 
                                             from gdg_outbound_statuses s 
                                             where  s.internal_code = 'SUCCESS');
        
        /* delete from clobs */        
        delete
        from   gdg_outbound_clobs c
        where  not exists
              (select 1
               from   gdg_outbound_messages    m
               where  c.request_id          =  m.request_id);
        
        l_rowcount := sql%rowcount;
        
        commit;
        
        return l_rowcount;

    END remove_outbound_records;

    /*
     * Remove inbound records
     */
    FUNCTION remove_inbound_records RETURN NUMBER
    IS
        l_rowcount number;
    BEGIN
        /* delete from messages table as this has a foreign key to clobs */
        delete
        from   gdg_inbound_messages m
        where  trunc(m.request_timestamp) < trunc(sysdate) - 21;

        /* delete from clobs */        
        delete
        from   gdg_inbound_clobs c
        where  not exists
              (select 1
               from   gdg_inbound_messages    m
               where  c.inbound_message_id =  m.inbound_message_id);

        l_rowcount := sql%rowcount;
        
        commit;
        
        return l_rowcount;
        
    END remove_inbound_records;

    /*
     * Get inbound counts between two dates
     */
    PROCEDURE get_inbound_counts(p_results_out  OUT SYS_REFCURSOR,
                                 p_start_date    IN GDG_INBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE,
                                 p_end_date      IN GDG_INBOUND_MESSAGES.REQUEST_TIMESTAMP%TYPE)
    IS
    BEGIN
        OPEN p_results_out FOR
            select  trunc(request_timestamp) request_timestamp,
                    count(*)                 count
            from    gdg_inbound_messages
            where ((p_start_date is null or trunc(request_timestamp) >= p_start_date)
	    and    (p_end_date   is null or trunc(request_timestamp) <= p_end_date))
            group by
                    trunc(request_timestamp);
                   
    END get_inbound_counts;

    /*
     * Remove outbound record by request_id
     */
    FUNCTION remove_outbound_record_by_id(p_request_id IN GDG_OUTBOUND_MESSAGES.REQUEST_ID%TYPE) RETURN NUMBER
    IS
        l_rowcount number;
    BEGIN
        /* delete from failures table as this has a foreign key to messages */
        delete
        from   gdg_outbound_failures   f
        where  request_id            = p_request_id;

        /* delete from messages table as this has a foreign key to clobs */
        delete
        from   gdg_outbound_messages   m
        where  request_id            = p_request_id;
        
        /* delete from clobs */        
        delete
        from   gdg_outbound_clobs   c
        where  request_id         = p_request_id;
        
        l_rowcount := sql%rowcount;
        
        commit;
        
        return l_rowcount;
        
    END remove_outbound_record_by_id;

END gdg_application_support_pkg;
/

show errors;