CREATE OR REPLACE PACKAGE BODY exi_flow_to_exiss_pkg AS
/*
 *
 * get_tracking_statuses
 *
 */
    PROCEDURE get_tracking_statuses(p_results_out    OUT SYS_REFCURSOR) IS

    BEGIN

        OPEN p_results_out FOR
        
            SELECT   status_id         status_id
                    ,internal_code     internal_code
                    ,internal_name     internal_name
                    ,tracking_enabled
            FROM     exi_ref_tracking_status;

    END get_tracking_statuses;

/*
 *
 * get_tracking_status_by_code
 *
 */
    PROCEDURE get_tracking_status_by_code(p_results_out    OUT SYS_REFCURSOR,
                                          p_code_in        IN  EXI_REF_TRACKING_STATUS.INTERNAL_CODE%TYPE) IS

    BEGIN

        OPEN p_results_out FOR

            SELECT   status_id         status_id
                    ,internal_code     internal_code
                    ,internal_name     internal_name
                    ,tracking_enabled
            FROM     exi_ref_tracking_status
            WHERE    internal_code   = p_code_in;
            
    END get_tracking_status_by_code;

/*
 *
 * get_message_payload
 *
 */
     PROCEDURE get_message_payload(p_clob_data  OUT SYS_REFCURSOR,
                                   p_item_id    IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE) IS
     BEGIN

      OPEN p_clob_data FOR

      SELECT CLOB_DATA
      FROM  EXI_ITEM_OUTBOUND
      WHERE ITEM_ID = p_item_id;

     END get_message_payload;

/*
 *
 * get_message_properties
 *
 */
     PROCEDURE get_message_properties(p_properties OUT SYS_REFCURSOR,
                                      p_item_id    IN EXI_ITEM_OUTBOUND.ITEM_ID%TYPE)IS

     BEGIN

      OPEN p_properties FOR

       SELECT 'XHBMessageIdentifier' AS "property_name", TO_CHAR(p_item_id) AS "property_value"
        FROM DUAL
       UNION ALL
       SELECT p.NAME AS "property_name", p.VALUE AS "property_value"
        FROM EXI_JMS_PROPERTY p
        WHERE LOWER(p.MESSAGE_TYPE) = 'exissmessagebuilder'
        AND p.VALUE IS NOT NULL
       UNION
       SELECT 'XHBMessageTypeType' AS "property_name", g.EXTERNAL_NAME AS "property_value"
        FROM EXI_REF_GROUP g,
         EXI_REF_TYPE t,
         EXI_ITEM_OUTBOUND o
        WHERE o.ITEM_ID = p_item_id
        AND o.TYPE_ID = t.TYPE_ID
        AND t.GROUP_ID = g.GROUP_ID
        AND g.EXTERNAL_NAME IS NOT NULL
       UNION
       SELECT 'XHBMessageTypeVersion' AS "property_name", t.VERSION AS "property_value"
        FROM EXI_REF_TYPE t,
         EXI_ITEM_OUTBOUND o
        WHERE o.ITEM_ID = p_item_id
        AND o.TYPE_ID = t.TYPE_ID
        AND t.VERSION IS NOT NULL
       UNION
       SELECT 'XHBMessageSchemaNamespace' AS "property_name", t.SCHEMA_NAME AS "property_value"
        FROM EXI_REF_TYPE t,
         EXI_ITEM_OUTBOUND o
        WHERE o.ITEM_ID = p_item_id
        AND o.TYPE_ID = t.TYPE_ID
        AND t.SCHEMA_NAME IS NOT NULL
       UNION
       SELECT 'XHBMessageSchemaIdentifier' AS "property_name", t.SCHEMA_LOCATION AS "property_value"
        FROM EXI_REF_TYPE t,
         EXI_ITEM_OUTBOUND o
        WHERE o.ITEM_ID = p_item_id
        AND o.TYPE_ID = t.TYPE_ID
        AND t.SCHEMA_LOCATION IS NOT NULL
       UNION
       SELECT 'XHBMessageSchemaVersion' AS "property_name", t.SCHEMA_VERSION AS "property_value"
        FROM EXI_REF_TYPE t,
         EXI_ITEM_OUTBOUND o
        WHERE o.ITEM_ID = p_item_id
        AND o.TYPE_ID = t.TYPE_ID
        AND t.SCHEMA_VERSION IS NOT NULL
       UNION
       SELECT 'XHBCreationDateTime' AS "property_name", TO_CHAR(o.ITEM_CREATED,'YYYY-MM-DD"T"HH24:MI:SS') AS "property_value"
        FROM EXI_ITEM_OUTBOUND o
        WHERE o.ITEM_ID = p_item_id
        AND o.ITEM_CREATED IS NOT NULL
       UNION
       SELECT 'XHBExpiryDateTime' AS "property_name", TO_CHAR(o.ITEM_EXPIRES,'YYYY-MM-DD"T"HH24:MI:SS') AS "property_value"
        FROM EXI_ITEM_OUTBOUND o
        WHERE o.ITEM_ID = p_item_id
        AND o.ITEM_EXPIRES IS NOT NULL;

     END get_message_properties;
     
/*
 *
 * insert_item_outbound_tracking
 *
 */
    FUNCTION insert_item_outbound_tracking(p_item_id_in        IN EXI_ITEM_OUTBOUND_TRACKING.ITEM_ID%TYPE,
                                           p_internal_code_in  IN EXI_REF_TRACKING_STATUS.INTERNAL_CODE%TYPE,
                                           p_tracking_date_in  IN EXI_ITEM_OUTBOUND_TRACKING.TRACKING_DATE%TYPE)
                                           RETURN EXI_ITEM_OUTBOUND_TRACKING.tracking_id%TYPE
         IS
             v_tracking_id      EXI_ITEM_OUTBOUND_TRACKING.TRACKING_ID%TYPE;
             v_status_id        EXI_REF_TRACKING_STATUS.STATUS_ID%TYPE;
             v_tracking_enabled EXI_REF_TRACKING_STATUS.TRACKING_ENABLED%TYPE;
         BEGIN
             SELECT status_id
                   ,tracking_enabled
             INTO   v_status_id
                   ,v_tracking_enabled
             FROM   EXI_REF_TRACKING_STATUS
             WHERE  internal_code           = p_internal_code_in;
             IF (NVL(v_tracking_enabled,'Y') = 'Y')
             THEN
                 INSERT INTO EXI_ITEM_OUTBOUND_TRACKING
                     (item_id, status_id, tracking_date)
                 VALUES
                     (p_item_id_in, v_status_id, p_tracking_date_in)
                 RETURNING tracking_id
                 INTO      v_tracking_id;
                 RETURN v_tracking_id;
             ELSE
                 RAISE_APPLICATION_ERROR(-20401, 'Tracking has been disabled for ' || p_internal_code_in);
             END IF;
         END insert_item_outbound_tracking;

END exi_flow_to_exiss_pkg;
/
show errors
