INSERT INTO EXI_REF_TYPE 
(internal_code, group_id, operation_id, internal_name, schema_name, version) 
SELECT subqry.internal_code, 23 group_id, 2 operation_id, subqry.internal_name, 'http://www.courtservice.gov.uk/schemas/courtservice' schema_name, '5.9' version
FROM (
	SELECT '11533' internal_code, 'Imprisonment for Life (Adult) for manslaughter of emergency service worker' internal_name FROM DUAL UNION
	SELECT '11534' internal_code, 'Detention for Life (Youth) for manslaughter of emergency service worker' internal_name FROM DUAL UNION
	SELECT '13820' internal_code, 'Electronic Whereabouts Monitoring Requirement' internal_name FROM DUAL UNION
	SELECT '13821' internal_code, 'Activity Requirement' internal_name FROM DUAL
) subqry
WHERE NOT EXISTS (SELECT 1 FROM EXI_REF_TYPE x2
                  WHERE  x2.internal_code = subqry.internal_code);
commit;