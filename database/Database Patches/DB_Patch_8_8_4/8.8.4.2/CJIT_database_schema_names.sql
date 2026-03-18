--- Update Stylesheets

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/DetentionAndTrainingOrder-v1-3.xsl'
WHERE INTERNAL_CODE='DTO';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v3-2.xsl'
WHERE INTERNAL_CODE='IO';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044C-v1-6.xsl'
WHERE INTERNAL_CODE='COC';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044D-v1-9.xsl'
WHERE INTERNAL_CODE='COD';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/RemandOrder-v2-5.xsl'
WHERE INTERNAL_CODE='RO';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2-11.xsl'
WHERE INTERNAL_CODE='TR';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/AppealRecordSheet-v2-8.xsl'
WHERE INTERNAL_CODE='AR';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-12.xsl'
WHERE INTERNAL_CODE='SR';


---Update Schema version
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice PreSentenceReport-v6-3.xsd'
WHERE INTERNAL_CODE='PSR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v6-3.xsd'
WHERE INTERNAL_CODE='DL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice RunningList-v6-3.xsd'
WHERE INTERNAL_CODE='RL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice WarnedList-v6-3.xsd'
WHERE INTERNAL_CODE='WL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice FirmList-v6-3.xsd'
WHERE INTERNAL_CODE='FL';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice Indictment-v6-3.xsd'
WHERE INTERNAL_CODE='CH';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice Skeleton-v6-3.xsd'
WHERE INTERNAL_CODE='SS';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityRehabOrder-v6-3.xsd'
WHERE INTERNAL_CODE='CRO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentOrder-v6-3.xsd'
WHERE INTERNAL_CODE='CPO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentRehabOrder-v6-3.xsd'
WHERE INTERNAL_CODE='CPR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice RemandOrder-v6-3.xsd'
WHERE INTERNAL_CODE='RO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice BailOrder-v6-3.xsd'
WHERE INTERNAL_CODE='BO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v6-3.xsd'
WHERE INTERNAL_CODE='IO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v6-3.xsd'
WHERE INTERNAL_CODE='YOI';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v6-3.xsd'
WHERE INTERNAL_CODE='SR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v6-3.xsd'
WHERE INTERNAL_CODE='AR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v6-3.xsd'
WHERE INTERNAL_CODE='TR';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v6-3.xsd'
WHERE INTERNAL_CODE='BW';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v6-3.xsd'
WHERE INTERNAL_CODE='DLP';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/transforms/courtservice CommunityOrder-v6-3.xsd'
WHERE INTERNAL_CODE='CO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice SuspendedSentenceOrder-v6-3.xsd'
WHERE INTERNAL_CODE='SSO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice WarrantAfterFailureOrder-v6-3.xsd'
WHERE INTERNAL_CODE='BWA';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice WarrantAfterFailureOrder-v6-3.xsd'
WHERE INTERNAL_CODE='BWB';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderCustodialOrder-v6-3.xsd'
WHERE INTERNAL_CODE='COC';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderCustodialOrder-v6-3.xsd'
WHERE INTERNAL_CODE='COD';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DefendantDetails-v6-3.xsd'
WHERE INTERNAL_CODE='DD';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DetentionAndTrainingOrder-v6-3.xsd'
WHERE INTERNAL_CODE='DTO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YouthRehabilitationOrder-v6-3.xsd'
WHERE INTERNAL_CODE='YRO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DVLAD20-v6-3.xsd'
WHERE INTERNAL_CODE='D20';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice ReleaseFromPrisonOrder-v6-3.xsd'
WHERE INTERNAL_CODE='RPO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice NoticeOfAcquittalOrder-v6-3.xsd'
WHERE INTERNAL_CODE='NAO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice MonetaryOrder-v6-3.xsd'
WHERE INTERNAL_CODE='MO';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice ActionOnConditionalDischargeOrder-v6-3.xsd'
WHERE INTERNAL_CODE='ACD';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice NoticeBreachSuspendedSentenceOrder-v6-3.xsd'
WHERE INTERNAL_CODE='BSS';


COMMIT;
