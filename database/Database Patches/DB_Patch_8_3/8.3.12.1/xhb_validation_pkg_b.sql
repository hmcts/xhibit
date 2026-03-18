SET serveroutput on
	SET echo on

CREATE OR REPLACE PACKAGE BODY xhb_validation_pkg AS
    FUNCTION request_validation(p_validation_id IN NUMBER,
                                p_schema_name IN VARCHAR2,
                                p_clob_data IN CLOB) RETURN RAW IS
        l_enqueue_options      DBMS_AQ.ENQUEUE_OPTIONS_T;
        l_message_properties   DBMS_AQ.MESSAGE_PROPERTIES_T;
        l_message_id           RAW(16);
        l_jms_message          sys.aq$_jms_text_message;
    BEGIN
        l_jms_message := sys.aq$_jms_text_message.construct;
	l_jms_message.set_long_property('validation_id', p_validation_id);
	l_jms_message.set_string_property('schema_name', p_schema_name);
        l_jms_message.set_text(p_clob_data);
        dbms_aq.enqueue(queue_name => 'xhb_validation_queue',
                        enqueue_options => l_enqueue_options,
                        message_properties => l_message_properties,
                        payload => l_jms_message,
                        msgid => l_message_id);
	return l_message_id;
    END request_validation;

    FUNCTION update_validation(p_validation_id IN NUMBER,
                                p_status IN VARCHAR2,
                                p_details IN VARCHAR2) RETURN NUMBER IS
    BEGIN
         UPDATE xhb_validation SET status = p_status, details = p_details WHERE validation_id = p_validation_id;
         RETURN p_validation_id;
    END update_validation;

END xhb_validation_pkg;
/
SHOW ERRORS;