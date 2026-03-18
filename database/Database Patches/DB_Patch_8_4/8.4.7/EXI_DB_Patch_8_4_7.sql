-----------------------------------------------------------------------
--CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------


-- UPDATE the ExISS database, using table EXI_REF_TYPE

--Add new event for Bail refused
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
                     '10310','Bail refused',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.3');

COMMIT;
