INSERT INTO EXI_REF_TYPE 
(internal_code, group_id, operation_id, internal_name, schema_name, version) 
SELECT subqry.internal_code, 23 group_id, 2 operation_id, subqry.internal_name, 'http://www.courtservice.gov.uk/schemas/courtservice' schema_name, '5.9' version
FROM (
	SELECT '13822' internal_code, 'Serious Violence Reduction Order' internal_name FROM DUAL
) subqry
WHERE NOT EXISTS (SELECT 1 FROM EXI_REF_TYPE x2
                  WHERE  x2.internal_code = subqry.internal_code);
commit;