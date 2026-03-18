create or replace TRIGGER "XHIBIT".XHB_DOC_COA_U_WSM
AFTER UPDATE OF coa_status ON xhb_defendant_on_case FOR EACH ROW
DECLARE
BEGIN
    wmb_message_pkg.send_wmb_message('XHIBIT',
                                     'XHB_DEFENDANT_ON_CASE',
                                     'COA_STATUS',
                                     :new.coa_status,
                                     'UPDATE',
                                     :new.defendant_on_case_id);
END;
/