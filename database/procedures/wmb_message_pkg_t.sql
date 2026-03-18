SET serveroutput on
SET echo on
DECLARE
BEGIN

    dbms_output.put_line('wmb_message_pkg.create_wmb_message_xml: ');
    dbms_output.put_line(wmb_message_pkg.create_wmb_message_xml('XHBIT',
                                                                'CASE',
                                                                'CHARGE_IMPORT_INDICATOR',
                                                                'BW',
                                                                'UPDATE',
                                                                101));

     dbms_output.put_line('wmb_message_pkg.send_xml:');
     wmb_message_pkg.send_wmb_message('XHBIT',
                                      'CASE',
                                      'CHARGE_IMPORT_INDICATOR',
                                      'BW',
                                      'UPDATE',
                                      101);
     dbms_output.put_line('Message Sent.');
     commit;
END;
/
