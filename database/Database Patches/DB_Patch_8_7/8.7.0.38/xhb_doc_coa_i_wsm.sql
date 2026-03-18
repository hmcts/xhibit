CREATE OR REPLACE TRIGGER "XHIBIT".xhb_doc_coa_i_wsm 
AFTER INSERT ON xhb_defendant_on_case FOR EACH ROW
DECLARE
BEGIN
    wmb_message_pkg.send_wmb_message('XHIBIT',
                                     'XHB_DEFENDANT_ON_CASE',
                                     'COA_STATUS',
                                     :new.coa_status,
                                     'INSERT',
                                     :new.defendant_on_case_id);
END;
/