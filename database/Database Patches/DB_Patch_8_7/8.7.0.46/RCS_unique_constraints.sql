@@RCS_remove_duplicates.sql;

ALTER TABLE xhb_court_room_usage
ADD CONSTRAINT xhb_court_room_usage_uc UNIQUE (court_room_id, sitting_date);

ALTER TABLE xhb_judge_usage
ADD CONSTRAINT xhb_judge_usage_uc UNIQUE (ref_judge_id, court_room_id, sitting_date);