set termout off;
spool tmp_out.txt;
select item_id, tracking_date from exi_item_outbound_tracking
where tracking_date > to_date('&1','dd/mm/yyyy hh24:mi') and status_id = 8;
exit;
