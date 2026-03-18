set termout on;
update xhb_courtel_list set last_attempt_datetime=sysdate where last_attempt_datetime is null and trunc(creation_date)>trunc(sysdate-1);
set termout off;
commit;
exit;
