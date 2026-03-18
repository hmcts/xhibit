UPDATE xhb_ref_system_code SET de_code = 'KC' WHERE code_type = 'ADV_TYPE' AND de_code = 'QC';

UPDATE xhb_ref_system_code SET de_code = 'NOT KC' WHERE code_type = 'ADV_TYPE' AND de_code = 'NOT QC';

UPDATE xhb_ref_system_code SET de_code = 'A KC HAS NOT BEEN GRANTED ON THE REPRESENTATION ORDER' 
WHERE code_type = 'LA_ERROR_TYPE' AND de_code = 'A QC HAS NOT BEEN GRANTED ON THE REPRESENTATION ORDER';

UPDATE xhb_ref_system_code SET de_code = 'JUNIOR COUNSEL LED BY KINGS COUNSEL' 
WHERE code_type = 'LA_BAR_TYPE' AND de_code = 'JUNIOR COUNSEL LED BY QUEENS COUNSEL';

UPDATE xhb_ref_system_code SET de_code = 'Amendment to number of KCs only' 
WHERE code_type = 'LEGAL_AID_AMENDMENT' AND de_code = 'Amendment to number of QCs only';

COMMIT;
/