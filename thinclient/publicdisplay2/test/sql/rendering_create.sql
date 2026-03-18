UPDATE XHB_HEARING_LIST
SET start_date = TRUNC(SYSDATE),
    end_date = TRUNC(SYSDATE)
WHERE list_id = 5;

UPDATE XHB_SCHEDULED_HEARING
SET original_time = TRUNC(SYSDATE),
    not_before_time = SYSDATE
WHERE sitting_id IN (SELECT sitting_id FROM XHB_SITTING WHERE list_id = 5);