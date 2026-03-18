UPDATE XHB_HEARING_LIST
SET start_date = '10-Jul-2003',
    end_date = '10-Jul-2003'
WHERE list_id = 5;

UPDATE XHB_SCHEDULED_HEARING
SET original_time = '10-Jul-2003',
    not_before_time = '10-Jul-2003'
WHERE sitting_id IN (SELECT sitting_id FROM XHB_SITTING WHERE list_id = 5);