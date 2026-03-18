@@RCS_remove_duplicates.sql;

-- Drop the current constraints
ALTER TABLE xhb_judge_usage DROP CONSTRAINT xhb_judge_usage_uc;

-- Recreate the constraints
ALTER TABLE xhb_judge_usage
ADD CONSTRAINT xhb_judge_usage_uc UNIQUE (ref_judge_id, sitting_date);