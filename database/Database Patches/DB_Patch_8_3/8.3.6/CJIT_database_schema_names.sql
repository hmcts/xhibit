---------------------------------------------------------------------
--CHANGES TO REMOVE THE VERSION NUMBER FROM THE SCHEMA NAME 
---------------------------------------------------------------------


-- UPDATE the CJIT database, using table CJI_DOCUMENT_TYPE
--PreSentenceReport
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice PreSentenceReport-v5-2.xsd'
WHERE INTERNAL_CODE='PSR';

--DailyList
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v5-2.xsd'
WHERE INTERNAL_CODE='DL'; 

--RunningList
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice RunningList-v5-2.xsd'
WHERE INTERNAL_CODE='RL';

--WarnedList
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice WarnedList-v5-2.xsd'
WHERE INTERNAL_CODE='WL'; 

--FirmList
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice FirmList-v5-2.xsd'
WHERE INTERNAL_CODE='FL'; 

--Indictment
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice Indictment-v5-2.xsd'
WHERE INTERNAL_CODE='CH'; 

--Skeleton
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice Skeleton-v5-2.xsd'
WHERE INTERNAL_CODE='SS'; 

--CommunityRehabOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommunityRehabOrder-v5-2.xsd'
WHERE INTERNAL_CODE='CRO'; 

--CommunityOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommunityOrder-v5-2.xsd'
WHERE INTERNAL_CODE='CO'; 


--CommunityPunishmentOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentOrder-v5-2.xsd'
WHERE INTERNAL_CODE='CPO'; 

--CommunityPunishmentRehabOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentRehabOrder-v5-2.xsd'
WHERE INTERNAL_CODE='CPR'; 

--RemandOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice RemandOrder-v5-2.xsd'
WHERE INTERNAL_CODE='RO'; 

--BailOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice BailOrder-v5-2.xsd'
WHERE INTERNAL_CODE='BO'; 

--ImprisonmentOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v5-2.xsd'
WHERE INTERNAL_CODE='IO'; 

--YoungOffenderOrder
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v5-2.xsd'
WHERE INTERNAL_CODE='YOI'; 

--CommittalRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v5-2.xsd'
WHERE INTERNAL_CODE='SR'; 

--AppealRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v5-2.xsd'
WHERE INTERNAL_CODE='AR'; 

--TrialRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v5-2.xsd'
WHERE INTERNAL_CODE='TR'; 

--BenchWarrant
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v5-2.xsd'
WHERE INTERNAL_CODE='BW'; 

--Prison Daily List
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice DailyList-v5-2.xsd'
WHERE INTERNAL_CODE='DLP';


COMMIT;
