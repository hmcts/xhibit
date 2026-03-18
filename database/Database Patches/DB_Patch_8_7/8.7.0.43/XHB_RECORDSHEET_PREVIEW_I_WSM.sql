CREATE OR REPLACE TRIGGER XHB_RECORDSHEET_PREVIEW_I_WSM 
AFTER INSERT ON XHB_RECORDSHEET FOR EACH ROW
BEGIN
  wmb_message_pkg.send_wmb_message('XHIBIT',
                                     'XHB_RECORDSHEET',
                                     'STATUS',
                                     :new.status,
                                     'INSERT',
                                     :new.recordsheet_id);
END;
/