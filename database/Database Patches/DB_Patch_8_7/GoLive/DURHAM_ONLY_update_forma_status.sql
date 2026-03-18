UPDATE xhb_def_hearing_record
SET forma_status = 'S'
WHERE forma_status IS NOT NULL
AND forma_status = 'Y';

COMMIT;