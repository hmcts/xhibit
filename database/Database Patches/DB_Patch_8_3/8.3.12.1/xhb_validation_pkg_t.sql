SET serveroutput on
SET echo on

DECLARE
    l_dequeue_options      DBMS_AQ.DEQUEUE_OPTIONS_T;
    l_message_properties   DBMS_AQ.MESSAGE_PROPERTIES_T;
    l_enqueued_id          RAW(16);
    l_dequeued_id          RAW(16);
    l_jms_message          sys.aq$_jms_text_message;
    l_text                 VARCHAR(1024);
BEGIN
    dbms_output.put_line('xhb_validation_pkg.request_validation:');
    l_enqueued_id := xhb_validation_pkg.request_validation(101, 'Hello.xsd', '<Message>Hello, World</Message>');
    dbms_output.put_line('-   ' || 'enqueued id: ' || l_enqueued_id);
    l_dequeue_options.wait := 5;
    -- l_dequeue_options.msgid := l_enqueued_id;
    dbms_aq.dequeue(queue_name => 'xhb_validation_queue',
                    dequeue_options => l_dequeue_options,
                    message_properties => l_message_properties,
                    payload => l_jms_message,
                    msgid => l_dequeued_id);
    l_jms_message.get_text(l_text);
    dbms_output.put_line('-   ' || 'dequeued id: ' ||  l_dequeued_id);
    dbms_output.put_line('-   ' || 'validation id: ' ||  l_jms_message.get_long_property('validation_id'));
    dbms_output.put_line('-   ' || 'schema name: ' ||  l_jms_message.get_string_property('schema_name'));
    dbms_output.put_line('-   ' || 'clob data: ' ||  l_text);

    commit;
END;
/

DECLARE
    l_validation_id       NUMBER(16);
    l_status              VARCHAR2(1);
    l_details             VARCHAR2(4000);
BEGIN
    -- Setup Test
    dbms_output.put_line('xhb_validation_pkg.update_validation:');		
    select xhb_validation_seq.NEXTVAL into l_validation_id from dual;
    dbms_output.put_line('-     validation id: ' || l_validation_id);
    insert into xhb_validation (validation_id, code, clob_data, schema_name, status, details) 
	values(l_validation_id, 'HD', '<Message>Hello, World</Message>', 'Hello.xsd', 'N', NULL);

    -- Run Test
    xhb_validation_pkg.update_validation(l_validation_id, 'F', 'Detils of fail');

    -- Check Test
    select status, details into l_status, l_details from xhb_validation where validation_id = l_validation_id;
    dbms_output.put_line('-     status: ' || l_status);
    dbms_output.put_line('-     details: ' || l_details);

    -- Clean Test
    delete from xhb_validation where validation_id = l_validation_id;
    commit;
END;
/

DECLARE
    l_validation_id       NUMBER(16);
    l_status              VARCHAR2(1);
    l_details             VARCHAR2(4000);
BEGIN
    -- Setup Test
    dbms_output.put_line('xhb_validation_pkg.update_validation:');		
    select xhb_validation_seq.NEXTVAL into l_validation_id from dual;
    dbms_output.put_line('-     validation id: ' || l_validation_id);
    insert into xhb_validation (validation_id, code, clob_data, schema_name, status, details) 
	values(l_validation_id, 'HD', '<Message>Hello, World</Message>', 'Hello.xsd', 'N', NULL);

    -- Run Test
    xhb_validation_pkg.update_validation(l_validation_id, 'S', NULL);

    -- Check Test
    select status, details into l_status, l_details from xhb_validation where validation_id = l_validation_id;
    dbms_output.put_line('-     status: ' || l_status);
    dbms_output.put_line('-     details: ' || l_details);

    -- Clean Test
    delete from xhb_validation where validation_id = l_validation_id;
    commit;
END;
/

