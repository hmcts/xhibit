-----------------------------------------------------------------------
--CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------


-- Add new group type for Custodial Order

INSERT INTO EXI_REF_GROUP (GROUP_ID, EXTERNAL_NAME) VALUES(NULL, 'XHIBITCustodial');

-- Add new group type for Suspended Sentence

INSERT INTO EXI_REF_GROUP (GROUP_ID, EXTERNAL_NAME) VALUES(NULL, 'XHIBITSuspendedSentence');

-- UPDATE the ExISS database, using table EXI_REF_TYPE

--Set new schema version number

UPDATE EXI_REF_TYPE SET VERSION=5.3 WHERE TYPE_ID>0;

--Add Custodial Order 5044C
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
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITCustodial'),
                     'COC','Custodial Order 5044C',NULL,'http://www.courtservice.gov.uk/transforms/courtservice',NULL,NULL,NULL,'5.3');


--Add Custodial Order 5044D
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
                               (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
                               (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITCustodial'),
                               'COD','Custodial Order 5044D',NULL,'http://www.courtservice.gov.uk/transforms/courtservice',NULL,NULL,NULL,'5.3');

--Add Bench Warrant 5061A
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
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITBenchWarrant'),
                    'BWA','Warrant After Failure Order 5061A',NULL,'http://www.courtservice.gov.uk/transforms/courtservice',
                     NULL,NULL,NULL,'5.3');

--Add Bench Warrant 5061B
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
                          (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
                          (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITBenchWarrant'),
                          'BWB','Warrant After Failure Order 5061B',NULL,'http://www.courtservice.gov.uk/transforms/courtservice',NULL,NULL,NULL,'5.3');

--Add Suspended Sentence Order
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
                          (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
                          (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITSuspendedSentence'),
                          'SSO','Suspended Sentence Order',NULL,'http://www.courtservice.gov.uk/transforms/courtservice',NULL,NULL,NULL,'5.3');

COMMIT;
