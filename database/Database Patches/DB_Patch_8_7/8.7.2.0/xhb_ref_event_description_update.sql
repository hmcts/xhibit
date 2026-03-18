UPDATE xhb_ref_event_description
SET event_sub_description = 'Bench Warrant Executed - Absconding - Put - Admitted'
WHERE external_event_code = 10404;

UPDATE xhb_ref_event_description
SET event_sub_description = 'Bench Warrant Executed - Absconding - Put - Not Admitted'
WHERE external_event_code = 10405;

UPDATE xhb_ref_event_description
SET event_sub_description = 'Bench Warrant Executed - Absconding - Not Put'
WHERE external_event_code = 10406;

COMMIT;