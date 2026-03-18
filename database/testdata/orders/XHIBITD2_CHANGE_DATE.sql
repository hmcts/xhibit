update xhb_hearing_list
set start_date = trunc(sysdate),
end_date = trunc(sysdate)
where list_id = 6;

update xhb_scheduled_hearing
set not_before_time = sysdate,
original_time = trunc(sysdate)
where sitting_id in (select sitting_id from xhb_sitting where list_id = 6);

commit;