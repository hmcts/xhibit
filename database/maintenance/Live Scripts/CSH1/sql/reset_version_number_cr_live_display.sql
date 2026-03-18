set termout off;
ALTER TRIGGER XHB_CR_LIVE_DISPLAY_BUR_TR DISABLE;
set termout on;
update XHB_CR_LIVE_DISPLAY set version=1 where version > 95000;
set termout off;
commit;
ALTER TRIGGER XHB_CR_LIVE_DISPLAY_BUR_TR ENABLE;
commit;
exit;

