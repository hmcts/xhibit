create or replace trigger "XHIBIT".xhb_defendant_on_case_dd_i_wsm 
after insert on xhb_defendant_on_case FOR EACH ROW
DECLARE
begin
  wmb_message_pkg.send_wmb_message('XHIBIT',
                                     'XHB_DEFENDANT_ON_CASE',
                                     'DIFFERENCE_REPORT',
                                     :new.difference_report,
                                     'INSERT',
                                     :new.defendant_on_case_id);
end xhb_defendant_on_case_dd_i_wsm;

/

