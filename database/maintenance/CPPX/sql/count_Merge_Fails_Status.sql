set termout off;
spool &1

SELECT (SELECT count(*) from xhb_cpp_list xcl where status='MF' AND last_update_date > (sysdate-1/24)) +
        (SELECT count(*) from xhb_cpp_formatting xcl where format_status='MF' AND last_update_date > (sysdate-1/24))
	AS total
	FROM dual;

exit;

