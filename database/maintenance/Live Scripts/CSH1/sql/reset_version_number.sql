set termout off;
ALTER TRIGGER XHB_CREST_IMPORT_BUR_TR DISABLE;
set termout on;
update xhb_crest_import set version=1 where version > 95000;
set termout off;
commit;
ALTER TRIGGER XHB_CREST_IMPORT_BUR_TR ENABLE;
commit;
exit;

