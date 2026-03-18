create or replace trigger "XHIBIT".xhb_defendant_on_case_dd_u_wsm 
after update of difference_report on xhb_defendant_on_case FOR EACH ROW
DECLARE
begin
  wmb_message_pkg.send_wmb_message('XHIBIT',
                                   'XHB_DEFENDANT_ON_CASE',
                                   'DIFFERENCE_REPORT',
                                   :new.difference_report,
                                   'UPDATE',
                                   :new.defendant_on_case_id);
end xhb_defendant_on_case_dd_u_wsm;

/
