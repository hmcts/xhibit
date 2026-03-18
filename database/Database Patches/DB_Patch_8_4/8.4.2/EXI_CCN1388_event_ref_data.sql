-----------------------------------------------------------------------
--CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------



-- UPDATE the ExISS database, using table EXI_REF_TYPE

--Set new schema version number

UPDATE EXI_REF_TYPE SET VERSION=5.3 WHERE TYPE_ID>0;

--Add Warrant for arrest 5061A issued
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DEFENDANT'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10407','Warrant for arrest 5061A issued',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.3');


--Add Warrant for arrest 5061B issued
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DEFENDANT'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10408','Warrant for arrest 5061B issued',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.3');

COMMIT;
