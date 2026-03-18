INSERT INTO EXI_REF_TYPE 
(internal_code, group_id, operation_id, internal_name, schema_name, version) 
SELECT subqry.internal_code, 23 group_id, 2 operation_id, subqry.internal_name, 'http://www.courtservice.gov.uk/schemas/courtservice' schema_name, '5.9' version
FROM (
	SELECT '13823' internal_code, 'Serious Disruption Prevention Order made on conviction (s20 POA 2023)' internal_name FROM DUAL UNION
	SELECT '13824' internal_code, 'Serious Disruption Prevention Order varied (s28 POA 2023)' internal_name FROM DUAL UNION
	SELECT '13825' internal_code, 'Serious Disruption Prevention Order discharged (s28 POA 2023)' internal_name FROM DUAL UNION
	SELECT '13826' internal_code, 'Serious Disruption Prevention Order renewed (s28 POA 2023)' internal_name FROM DUAL UNION
	SELECT '13827' internal_code, 'Surety declared forfeit (to pay)' internal_name FROM DUAL
) subqry
WHERE NOT EXISTS (SELECT 1 FROM EXI_REF_TYPE x2
                  WHERE  x2.internal_code = subqry.internal_code);
commit;