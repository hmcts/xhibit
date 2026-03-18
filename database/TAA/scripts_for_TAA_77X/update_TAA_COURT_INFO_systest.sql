set lines 120
set pages 200
set echo on
spool update_TAA_COURT_INFO_systest.log

select * from xhb_taa_court_info;

update xhb_taa_court_info set value = '10.25.5.1';

PROMPT ** 5 rows should be updated **

select * from xhb_taa_court_info;

commit;

select CREST_IP_ADDRESS from xhb_court;

update xhb_court set CREST_IP_ADDRESS ='csa00112:90';

PROMPT ** 5 rows should be updated **

select CREST_IP_ADDRESS from xhb_court;

commit;

spool off
exit



