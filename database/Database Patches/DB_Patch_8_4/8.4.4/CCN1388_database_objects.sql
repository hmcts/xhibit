
-------------------------------------------------------------------------------
--
-- This element of calls SQL relating to CCN1388 
--
-------------------------------------------------------------------------------
-- Put a call to your SQL file here e.g. @xhb_Form5061B.sql

update XHB_ORDER_TEMPLATE t 
set t.OBS_IND = 'Y' 
where t.ORDER_TEMPLATE_ID in (5, 7);
/

COMMIT;
/
