UPDATE xhb_ref_disp_retention_policy
SET has_duration = NULL
WHERE disposal_code = 'DTTO'
AND has_duration IS NOT NULL;

COMMIT;