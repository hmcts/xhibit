---------------------------------------------------------------------
--CHANGES TO REMOVE THE VERSION NUMBER FROM THE SCHEMA NAME 
---------------------------------------------------------------------


-- UPDATE the CJIT database, using table CJI_DOCUMENT_TYPE
--PreSentenceReport
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice PreSentenceReport-v5-3.xsd'
WHERE INTERNAL_CODE='PSR';

--DailyList
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v5-3.xsd'
WHERE INTERNAL_CODE='DL'; 

--RunningList
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice RunningList-v5-3.xsd'
WHERE INTERNAL_CODE='RL';

--WarnedList
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice WarnedList-v5-3.xsd'
WHERE INTERNAL_CODE='WL'; 

--FirmList
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice FirmList-v5-3.xsd'
WHERE INTERNAL_CODE='FL'; 

--Indictment
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice Indictment-v5-3.xsd'
WHERE INTERNAL_CODE='CH'; 

--Skeleton
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice Skeleton-v5-3.xsd'
WHERE INTERNAL_CODE='SS'; 

--CommunityRehabOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommunityRehabOrder-v5-3.xsd'
WHERE INTERNAL_CODE='CRO'; 

--CommunityOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommunityOrder-v5-3.xsd'
WHERE INTERNAL_CODE='CO'; 


--CommunityPunishmentOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentOrder-v5-3.xsd'
WHERE INTERNAL_CODE='CPO'; 

--CommunityPunishmentRehabOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentRehabOrder-v5-3.xsd'
WHERE INTERNAL_CODE='CPR'; 

--RemandOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice RemandOrder-v5-3.xsd'
WHERE INTERNAL_CODE='RO'; 

--BailOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice BailOrder-v5-3.xsd'
WHERE INTERNAL_CODE='BO'; 

--ImprisonmentOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v5-3.xsd'
WHERE INTERNAL_CODE='IO'; 

--YoungOffenderOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v5-3.xsd'
WHERE INTERNAL_CODE='YOI'; 

--CommittalRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v5-3.xsd'
WHERE INTERNAL_CODE='SR'; 

--AppealRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v5-3.xsd'
WHERE INTERNAL_CODE='AR'; 

--TrialRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v5-3.xsd'
WHERE INTERNAL_CODE='TR'; 

--BenchWarrant
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v5-3.xsd'
WHERE INTERNAL_CODE='BW'; 

--Prison Daily List
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v5-3.xsd'
WHERE INTERNAL_CODE='DLP';

--Bench Warrant 5061A
INSERT INTO CJI_DOCUMENT_TYPE (DOCUMENT_TYPE_ID,
                               INTERNAL_CODE, 
                               INTERNAL_NAME, 
                               EXTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SECURITY_CLASSIFICATION, 
                               BITS_DOCUMENT_CONFIG,
                               BITS_TRANSFORM_CONFIG, 
                               REGISTERED_IN_CJSE, 
                               REGISTER_AGAINST_LOCATION, 
                               REGISTER_AGAINST_CRN,
                               REGISTER_AGAINST_DEFENDANT,
                               SCHEMA_NAME) 
              VALUES((select max(document_type_id) + 1 from cji_document_type),'BWA','Warrant After Failure Order 5061A','WARRANT5061A','http://www.courtservice.gov.uk/transforms/courtservice/BenchWarrant5061A-v1-0.xsl',
                     'NPM','BITS.BenchWarrantOrderAHandler','BITS.XMLTransformHandler','Y','N','N','Y','http://www.courtservice.gov.uk/schemas/courtservice WarrantAfterFailureOrder-v5-3.xsd');

--Bench Warrant 5061B
INSERT INTO CJI_DOCUMENT_TYPE (DOCUMENT_TYPE_ID,
                               INTERNAL_CODE, 
                               INTERNAL_NAME, 
                               EXTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SECURITY_CLASSIFICATION, 
                               BITS_DOCUMENT_CONFIG, 
                               BITS_TRANSFORM_CONFIG,
                               REGISTERED_IN_CJSE, 
                               REGISTER_AGAINST_LOCATION, 
                               REGISTER_AGAINST_CRN,
                               REGISTER_AGAINST_DEFENDANT,
                               SCHEMA_NAME) 
              VALUES((select max(document_type_id) + 1 from cji_document_type),'BWB','Warrant After Failure Order 5061B','WARRANT5061B','http://www.courtservice.gov.uk/transforms/courtservice/BenchWarrant5061B-v1-0.xsl',
                     'NPM','BITS.BenchWarrantOrderBHandler','BITS.XMLTransformHandler','Y','N','N','Y','http://www.courtservice.gov.uk/schemas/courtservice WarrantAfterFailureOrder-v5-3.xsd');

--Custodial Order 5044C
INSERT INTO CJI_DOCUMENT_TYPE (DOCUMENT_TYPE_ID,
                               INTERNAL_CODE, 
                               INTERNAL_NAME, 
                               EXTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SECURITY_CLASSIFICATION, 
                               BITS_DOCUMENT_CONFIG, 
                               BITS_TRANSFORM_CONFIG,
                               REGISTERED_IN_CJSE, 
                               REGISTER_AGAINST_LOCATION, 
                               REGISTER_AGAINST_CRN,
                               REGISTER_AGAINST_DEFENDANT,
                               SCHEMA_NAME) 
              VALUES((select max(document_type_id) + 1 from cji_document_type),'COC','Custodial Order 5044C','CUSTODIAL5044C','http://www.courtservice.gov.uk/transforms/courtservice/CustodialOrder5044C-v1-0.xsl',
                     'Restricted','BITS.CustodialOrderCHandler','BITS.XMLTransformHandler','Y','N','Y','N','http://www.courtservice.gov.uk/schemas/courtservice CustodialOrder-v5-3.xsd');
--Custodial Order 5044D
INSERT INTO CJI_DOCUMENT_TYPE (DOCUMENT_TYPE_ID,
                               INTERNAL_CODE, 
                               INTERNAL_NAME, 
                               EXTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SECURITY_CLASSIFICATION, 
                               BITS_DOCUMENT_CONFIG, 
                               BITS_TRANSFORM_CONFIG,
                               REGISTERED_IN_CJSE, 
                               REGISTER_AGAINST_LOCATION, 
                               REGISTER_AGAINST_CRN,
                               REGISTER_AGAINST_DEFENDANT,
                               SCHEMA_NAME) 
              VALUES((select max(document_type_id) + 1 from cji_document_type),'COD','Custodial Order 5044D','CUSTODIAL5044D','http://www.courtservice.gov.uk/transforms/courtservice/CustodialOrder5044D-v1-0.xsl',
                     'Restricted','BITS.CustodialOrderCHandler','BITS.XMLTransformHandler','Y','N','Y','N','http://www.courtservice.gov.uk/schemas/courtservice CustodialOrder-v5-3.xsd');

--Suspended Sentence Order
INSERT INTO CJI_DOCUMENT_TYPE (DOCUMENT_TYPE_ID,
                               INTERNAL_CODE, 
                               INTERNAL_NAME, 
                               EXTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SECURITY_CLASSIFICATION, 
                               BITS_DOCUMENT_CONFIG, 
                               BITS_TRANSFORM_CONFIG,
                               REGISTERED_IN_CJSE, 
                               REGISTER_AGAINST_LOCATION, 
                               REGISTER_AGAINST_CRN,
                               REGISTER_AGAINST_DEFENDANT,
                               SCHEMA_NAME) 
              VALUES((select max(document_type_id) + 1 from cji_document_type),'SSO','Suspended Sentence','SUSPENDEDSENTENCE','http://www.courtservice.gov.uk/transforms/courtservice/SuspendedSentenceOrder-v1-0.xsl',
                     'NPM','BITS.SuspendedSentenceOrderHandler','BITS.XMLTransformHandler','Y','N','Y','N','http://www.courtservice.gov.uk/schemas/courtservice SuspendedSentenceOrder-v5-3.xsd');

--- Update Imprisonment Order XSL

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v2-3.xsl'
WHERE INTERNAL_CODE='IO';

COMMIT;
