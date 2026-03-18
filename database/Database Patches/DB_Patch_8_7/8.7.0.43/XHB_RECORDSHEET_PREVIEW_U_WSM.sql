CREATE OR REPLACE TRIGGER XHB_RECORDSHEET_PREVIEW_U_WSM 
AFTER UPDATE OF status ON XHB_RECORDSHEET FOR EACH ROW
BEGIN
    wmb_message_pkg.send_wmb_message('XHIBIT',
                                     'XHB_RECORDSHEET',
                                     'STATUS',
                                     :new.status,
                                     'UPDATE',
                                     :new.recordsheet_id);
END;
/