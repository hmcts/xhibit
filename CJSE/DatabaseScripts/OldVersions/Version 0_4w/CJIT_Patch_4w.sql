/* This script will be used to update Production from 0_4v to the latest level - 0_4w */
/* This update is specifically for CR54 - the addition of CRESTChargeID */
/* plus CR71 - Changing indictment to Charges */
/* plus CR?? - Adding ASN to the BAil Order Stylesheet */

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice DailyList-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 1;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice RunningList-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 2;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice WarnedList-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 3;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice FirmList-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 4;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice Indictment-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice Skeleton-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 6;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice CommunityRehabOrder-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 7;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentOrder-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 8;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentRehabOrder-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 9;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice RemandOrder-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 10;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice BailOrder-v2-1.xsd' 
WHERE DOCUMENT_TYPE_ID = 11;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 12;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 13;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 14;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 15;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 16;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 20;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v2-1.xsd'
WHERE DOCUMENT_TYPE_ID = 21;

/* Changes for Cr71 */

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice/Indictment-v2-3.xsl'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET INTERNAL_CODE = 'CH'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET INTERNAL_NAME = 'Charges'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET EXTERNAL_NAME = 'CHARGES'
WHERE DOCUMENT_TYPE_ID = 5;


/* Changes for CR 77 */

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice/bailorder-v2-3.xsl'
WHERE DOCUMENT_TYPE_ID = 11;
