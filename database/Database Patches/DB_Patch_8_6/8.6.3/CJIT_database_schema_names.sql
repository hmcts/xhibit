--- Update Imprisonment Order XSL

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v2-8.xsl'
WHERE INTERNAL_CODE='IO';

--- Update Young Offender Order XSL

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044D-v1-5.xsl'
WHERE INTERNAL_CODE='COD';

--- Update Bail Order XSL

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/bailorder-v2-4.xsl'
WHERE INTERNAL_CODE='BO';

--- Insert new row for Detention and Training Order document

Insert into CJI_DOCUMENT_TYPE 
(
 DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME,
 EXTERNAL_NAME, STYLESHEET_NAME,
 SECURITY_CLASSIFICATION,
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG,
 REGISTERED_IN_CJSE,
 REGISTER_AGAINST_LOCATION,
 REGISTER_AGAINST_CRN,
 REGISTER_AGAINST_DEFENDANT,
 SCHEMA_NAME
)
VALUES((select max(document_type_id) + 1 from cji_document_type),'DTO','DetentionAndTrainingOrder','DTO',
'http://www.courtservice.gov.uk/transforms/courtservice/DetentionAndTrainingOrder-v1-0.xsl',
'NPM','BITS.DetentionAndTrainingOrderHandler','BITS.XMLTransformHandler','Y','N','N','Y',
'http://www.courtservice.gov.uk/schemas/courtservice DetentionAndTrainingOrder-v5-7.xsd');

--- Insert new row for Youth Rehabilitation Order document

Insert into CJI_DOCUMENT_TYPE 
(
 DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME,
 EXTERNAL_NAME, STYLESHEET_NAME,
 SECURITY_CLASSIFICATION,
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG,
 REGISTERED_IN_CJSE,
 REGISTER_AGAINST_LOCATION,
 REGISTER_AGAINST_CRN,
 REGISTER_AGAINST_DEFENDANT,
 SCHEMA_NAME
)
VALUES((select max(document_type_id) + 1 from cji_document_type),'YRO','YouthRehabilitationOrder','YRO',
'http://www.courtservice.gov.uk/transforms/courtservice/YouthRehabilitationOrder-v1-0.xsl',
'NPM','BITS.YouthRehabilitationOrderHandler','BITS.XMLTransformHandler','Y','N','N','Y',
'http://www.courtservice.gov.uk/schemas/courtservice YouthRehabilitationOrder-v5-7.xsd');

COMMIT;

--- Insert new row for DVLA D20 document

Insert into CJI_DOCUMENT_TYPE 
(
 DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME,
 EXTERNAL_NAME, STYLESHEET_NAME,
 SECURITY_CLASSIFICATION,
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG,
 REGISTERED_IN_CJSE,
 REGISTER_AGAINST_LOCATION,
 REGISTER_AGAINST_CRN,
 REGISTER_AGAINST_DEFENDANT,
 SCHEMA_NAME
)
VALUES((select max(document_type_id) + 1 from cji_document_type),'D20','DVLAD20','D20',
'http://www.courtservice.gov.uk/transforms/courtservice/DVLAD20-v1-0.xsl',
'NPM','BITS.DVLAD20Handler','BITS.XMLTransformHandler','Y','N','N','Y',
'http://www.courtservice.gov.uk/schemas/courtservice DVLAD20-v5-7.xsd');

COMMIT;


---Update Schema version
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice PSRRequest-v5-7.xsd'
WHERE INTERNAL_CODE='PSR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v5-7.xsd'
WHERE INTERNAL_CODE='DL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice RunningList-v5-7.xsd'
WHERE INTERNAL_CODE='RL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice WarnedList-v5-7.xsd'
WHERE INTERNAL_CODE='WL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice FirmList-v5-7.xsd'
WHERE INTERNAL_CODE='FL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice Indictment-v5-7.xsd'
WHERE INTERNAL_CODE='CH';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice Skeleton-v5-7.xsd'
WHERE INTERNAL_CODE='SS';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityRehabOrder-v5-7.xsd'
WHERE INTERNAL_CODE='CRO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentOrder-v5-7.xsd'
WHERE INTERNAL_CODE='CPO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentRehabOrder-v5-7.xsd'
WHERE INTERNAL_CODE='CPR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice RemandOrder-v5-7.xsd'
WHERE INTERNAL_CODE='RO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice BailOrder-v5-7.xsd'
WHERE INTERNAL_CODE='BO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v5-7.xsd'
WHERE INTERNAL_CODE='IO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v5-7.xsd'
WHERE INTERNAL_CODE='YOI';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v5-7.xsd'
WHERE INTERNAL_CODE='SR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v5-7.xsd'
WHERE INTERNAL_CODE='AR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v5-7.xsd'
WHERE INTERNAL_CODE='TR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v5-7.xsd'
WHERE INTERNAL_CODE='BW';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v5-7.xsd'
WHERE INTERNAL_CODE='DLP';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityOrder-v5-7.xsd'
WHERE INTERNAL_CODE='CO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice SuspendedSentenceOrder-v5-7.xsd'
WHERE INTERNAL_CODE='SSO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice WarrantAfterFailureOrder-v5-7.xsd'
WHERE INTERNAL_CODE='BWA';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice WarrantAfterFailureOrder-v5-7.xsd'
WHERE INTERNAL_CODE='BWB';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderCustodialOrder-v5-7.xsd'
WHERE INTERNAL_CODE='COC';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderCustodialOrder-v5-6.xsd'
WHERE INTERNAL_CODE='COD';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DefendantDetails-v5-7.xsd'
WHERE INTERNAL_CODE='DD';


COMMIT;
