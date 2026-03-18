/* Insert Document Type Data */

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
 SCHEMA_NAME,
 MAJOR_SCHEMA_VERSION,
 MINOR_SCHEMA_VERSION
)
values
 (29,'PSR','Pre-Sentence Report Request',
'PSRRequest','http://www.courtservice.gov.uk/transforms/courtservice/PSRRequest-v1-0.xsl',
'NPM',
'BITS.PSRRequestHandler', 'BITS.XMLTransformHandler',
'Y',
'N',
'Y',
'N',
'http://www.courtservice.gov.uk/transforms/courtservice PSRRequest-v4-4.xsd',
NULL,
NULL);




/* Add Document type to Probation Service */

Insert into CJI_DOCUMENT_SECURITY (DOCUMENT_SECURITY_ID, DOCUMENT_TYPE_ID, CJO_ROLE_ID, CREATION_DATE)
values (	247	,	29	,	6	, sysdate);



/* Update Schema Version to Latest NUmber */

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 1;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice RunningList-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 2;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice WarnedList-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 3;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice FirmList-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 4;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice Indictment-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice Skeleton-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 6;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityRehabOrder-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 7;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentOrder-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 8;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentRehabOrder-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 9;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice RemandOrder-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 10;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BailOrder-v4-4.xsd' 
WHERE DOCUMENT_TYPE_ID = 11;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 12;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 13;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 14;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 15;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 16;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 20;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 21;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityOrder-v4-4.xsd'
WHERE DOCUMENT_TYPE_ID = 28;

/* DB Release 8.1 Swiki Request 28 */

/* Update Schema Version to Latest NUmber */

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 1;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice RunningList-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 2;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice WarnedList-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 3;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice FirmList-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 4;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice Indictment-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice Skeleton-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 6;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityRehabOrder-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 7;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentOrder-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 8;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentRehabOrder-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 9;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice RemandOrder-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 10;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BailOrder-v4-5.xsd' 
WHERE DOCUMENT_TYPE_ID = 11;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 12;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 13;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 14;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 15;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 16;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 20;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 21;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityOrder-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 28;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice PreSentenceReport-v4-5.xsd'
WHERE DOCUMENT_TYPE_ID = 29;

/* DB Release 8.1 Swiki Request 30 */


Update cji_document_type
set BITS_DOCUMENT_CONFIG = 'BITS.PreSentenceReportHandler'
where document_type_id = 29;
