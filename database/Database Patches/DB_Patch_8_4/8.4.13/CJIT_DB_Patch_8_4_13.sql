--Schema Version Updates
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice PSRRequest-v5-4.xsd'
WHERE INTERNAL_CODE='PSR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v5-4.xsd'
WHERE INTERNAL_CODE='DL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice RunningList-v5-4.xsd'
WHERE INTERNAL_CODE='RL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice WarnedList-v5-4.xsd'
WHERE INTERNAL_CODE='WL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice FirmList-v5-4.xsd'
WHERE INTERNAL_CODE='FL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice Indictment-v5-4.xsd'
WHERE INTERNAL_CODE='CH';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice Skeleton-v5-4.xsd'
WHERE INTERNAL_CODE='SS';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityRehabOrder-v5-4.xsd'
WHERE INTERNAL_CODE='CRO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentOrder-v5-4.xsd'
WHERE INTERNAL_CODE='CPO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentRehabOrder-v5-4.xsd'
WHERE INTERNAL_CODE='CPR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice RemandOrder-v5-4.xsd'
WHERE INTERNAL_CODE='RO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice BailOrder-v5-4.xsd'
WHERE INTERNAL_CODE='BO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v5-4.xsd'
WHERE INTERNAL_CODE='IO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v5-4.xsd'
WHERE INTERNAL_CODE='YOI';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v5-4.xsd'
WHERE INTERNAL_CODE='SR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v5-4.xsd'
WHERE INTERNAL_CODE='AR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v5-4.xsd'
WHERE INTERNAL_CODE='TR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v5-4.xsd'
WHERE INTERNAL_CODE='BW';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v5-4.xsd'
WHERE INTERNAL_CODE='DLP';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityOrder-v5-4.xsd'
WHERE INTERNAL_CODE='CO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice SuspendedSentenceOrder-v5-4.xsd'
WHERE INTERNAL_CODE='SSO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice WarrantAfterFailureOrder-v5-4.xsd'
WHERE INTERNAL_CODE='BWA';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice WarrantAfterFailureOrder-v5-4.xsd'
WHERE INTERNAL_CODE='BWB';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderCustodialOrder-v5-4.xsd'
WHERE INTERNAL_CODE='COC';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderCustodialOrder-v5-4.xsd'
WHERE INTERNAL_CODE='COD';




--Stylesheet Version Updates
UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/SuspendedSentenceOrder-v1-3.xsl' WHERE INTERNAL_CODE = 'SSO';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v2-5.xsl' WHERE INTERNAL_CODE = 'IO';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044C-v1-2.xsl' WHERE INTERNAL_CODE = 'COC';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044D-v1-2.xsl' WHERE INTERNAL_CODE = 'COD';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2-6.xsl' WHERE INTERNAL_CODE = 'TR';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-9.xsl' WHERE INTERNAL_CODE = 'SR';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/AppealRecordSheet-v2-6.xsl' WHERE INTERNAL_CODE = 'AR';



COMMIT;