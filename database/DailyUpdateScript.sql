UPDATE xhb_hearing_list
SET start_date = TRUNC(SYSDATE), end_date = TRUNC(SYSDATE)
WHERE list_id = 2

UPDATE xhb_scheduled_hearing
SET not_before_time = SYSDATE,
original_time = TRUNC(SYSDATE)
WHERE sitting_id IN (SELECT sitting_id FROM xhb_sitting WHERE list_id = 2);

COMMIT;