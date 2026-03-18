UPDATE xhb_ref_disp_retention_policy
SET has_life = 'Y'
WHERE disposal_code = 'LIMM'
AND NVL(has_life,'N') != 'Y';

COMMIT;