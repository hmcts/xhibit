
UPDATE xhb_ref_system_code SET ref_code_order = 1 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%J LED BY KC%';
UPDATE xhb_ref_system_code SET ref_code_order = 2 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%LEADING KC%';
UPDATE xhb_ref_system_code SET ref_code_order = 3 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%KC ALONE%';
UPDATE xhb_ref_system_code SET ref_code_order = 4 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%QC ALONE%';
UPDATE xhb_ref_system_code SET ref_code_order = 5 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%LEADING QC%';
UPDATE xhb_ref_system_code SET ref_code_order = 6 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%LEADING J%';
UPDATE xhb_ref_system_code SET ref_code_order = 7 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%J LED BY QC%';
UPDATE xhb_ref_system_code SET ref_code_order = 8 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%J LED BY J%';
UPDATE xhb_ref_system_code SET ref_code_order = 9 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%J ALONE%';
UPDATE xhb_ref_system_code SET ref_code_order = 10 WHERE code_type = 'ADVOCATE_TYPE' AND TRIM(UPPER(de_code)) LIKE '%NOTING J%';
	
COMMIT;
/