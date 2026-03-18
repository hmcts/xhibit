MERGE INTO CJI_DOCUMENT_TYPE cdt USING 
(SELECT cdt.document_type_id, 
        cdt.schema_name, 
		REPLACE(cdt.schema_name,subqry.old_version||'.xsd',subqry.NEW_version||'.xsd') new_schema_name
   FROM CJI_DOCUMENT_TYPE cdt,
        (SELECT '6-2' old_version,
                '6-3' new_version
           FROM DUAL) subqry
WHERE cdt.schema_name LIKE '%'||subqry.old_version||'.xsd') qry
 ON (cdt.DOCUMENT_TYPE_ID = qry.DOCUMENT_TYPE_ID)
    WHEN MATCHED THEN UPDATE SET cdt.schema_name = qry.new_schema_name;

COMMIT;
/