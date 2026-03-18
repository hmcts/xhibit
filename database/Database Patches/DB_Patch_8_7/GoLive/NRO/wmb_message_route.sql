-- Remove the Broker Case links to XHIBIT (CTX-194)

DELETE FROM wmb_message_route wmr
WHERE wmr.table_name = 'XHB_CASE'
AND wmr.column_name = 'CHARGE_IMPORT_INDICATOR'
AND wmr.column_value = 'S';

commit;

-- Remove the Broker Case links to XHIBIT (CTX-402)

DELETE FROM wmb_message_route wmr
WHERE wmr.table_name = 'XHB_CASE'
AND wmr.column_name = 'IND_CHANGE_STATUS' 
AND wmr.column_value = 'R';

commit;