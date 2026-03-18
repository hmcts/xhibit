create or replace PACKAGE BODY wmb_message_pkg AS
/*********************************************************************************
* WMB_MESSAGE_PKG
*
* Version  Date           Author   	     Comments
* 1.0      
* 1.1      13/08/2018     A Dennis       CTX-2542 Amended send_wmb_message to change the value of queue_name_val from wmb_message_queue to xhibit.wmb_message_queue
*                                        This is so that during Data Migration, which is performed from a different database schema called data_mig, when there is an insert
*                                        into XHB_CASE table and the trigger fires and a load is put on this AQ, it will not fall over but recognise the fact that this queue
*                                        belongs to the XHIBIT schema (user) and not data_mig schema that called it.
************************************************************************************/

    FUNCTION create_wmb_message_xml(p_schema_name IN VARCHAR2,
                                    p_table_name IN VARCHAR2,
                                    p_column_name IN VARCHAR2,
                                    p_column_value IN VARCHAR2,
                                    p_operation IN VARCHAR2,
                                    p_row_id IN NUMBER) RETURN VARCHAR2 IS
    BEGIN
        return '<WBI_MESSAGE><SCHEMA_NAME>' || p_schema_name || '</SCHEMA_NAME>' ||
               '<TABLE_NAME>' || p_table_name || '</TABLE_NAME>' ||
               '<COLUMN_NAME>' || p_column_name || '</COLUMN_NAME>' ||
               '<COLUMN_VALUE>' || p_column_value || '</COLUMN_VALUE>' ||
               '<OPERATION>' || p_operation || '</OPERATION>' ||
               '<ROW_ID>' || p_row_id || '</ROW_ID></WBI_MESSAGE>';
    END create_wmb_message_xml;

    PROCEDURE send_wmb_message(p_schema_name IN VARCHAR2,
                               p_table_name IN VARCHAR2,
                               p_column_name IN VARCHAR2,
                               p_column_value IN VARCHAR2,
                               p_operation IN VARCHAR2,
                               p_row_id IN NUMBER) IS
        queue_options        DBMS_AQ.ENQUEUE_OPTIONS_T;
        message_properties   DBMS_AQ.MESSAGE_PROPERTIES_T;
        message_id           RAW(16);
        queue_name_val       varchar2(100);
        v_agent              sys.aq$_agent := sys.aq$_agent(' ', null, 0);
        v_jms_message        sys.aq$_jms_text_message;
    BEGIN
        queue_name_val := 'xhibit.wmb_message_queue';
        v_jms_message := sys.aq$_jms_text_message.construct;
        v_jms_message.set_text(create_wmb_message_xml(p_schema_name,
                                                      p_table_name,
                                                      p_column_name,
                                                      p_column_value,
                                                      p_operation,
                                                      p_row_id));
        dbms_aq.enqueue(queue_name => queue_name_val,
                        enqueue_options => queue_options,
                        message_properties => message_properties,
                        payload => v_jms_message,
                        msgid => message_id);
    END send_wmb_message;

END wmb_message_pkg;
/