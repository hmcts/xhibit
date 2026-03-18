/* This script will be used to update Production from 0_4y to the latest level - 0_4z_a */
/* This update is adds a version table to the CJSE database in order to keep track of schema versions */

/* Update Stylesheet references for CR44  */

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-4.xsl'
where DOCUMENT_TYPE_ID =14;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/AppealRecordSheet-v2-3.xsl'
where DOCUMENT_TYPE_ID =15;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2-2.xsl'
where DOCUMENT_TYPE_ID =16;

/* Update Schema references for CR44  */

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v2-2.xsd'
where DOCUMENT_TYPE_ID =1;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice RunningList-v2-2.xsd'
where DOCUMENT_TYPE_ID =2;

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice WarnedList-v2-2.xsd'
where DOCUMENT_TYPE_ID =3;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice FirmList-v2-2.xsd'
where DOCUMENT_TYPE_ID =4;

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice Indictment-v2-2.xsd'
 where DOCUMENT_TYPE_ID =5;

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice Skeleton-v2-2.xsd'
where DOCUMENT_TYPE_ID =6;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice CommunityRehabOrder-v2-2.xsd'
 where DOCUMENT_TYPE_ID =7;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentOrder-v2-2.xsd'
 where DOCUMENT_TYPE_ID =8;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentRehabOrder-v2-2.xsd'
 where DOCUMENT_TYPE_ID =9;

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice RemandOrder-v2-2.xsd'
 where DOCUMENT_TYPE_ID =10;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice BailOrder-v2-2.xsd' 
where DOCUMENT_TYPE_ID =11;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v2-2.xsd'
 where DOCUMENT_TYPE_ID =12;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v2-2.xsd'
where DOCUMENT_TYPE_ID =13;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v2-2.xsd'
where DOCUMENT_TYPE_ID =14;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v2-2.xsd'
where DOCUMENT_TYPE_ID =15;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v2-2.xsd'
where DOCUMENT_TYPE_ID =16;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v2-2.xsd'
 where DOCUMENT_TYPE_ID =20;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v2-2.xsd'
 where DOCUMENT_TYPE_ID =21;



INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION,
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 23, 'DLD', 'Daily List for Distribution',  'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');

INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION,
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 24, 'FLD', 'Firm List for Distribution',  'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');

INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION, 
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 25, 'WLD', 'Warned List for Distribution',  'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');

INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION,
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 26, 'DLL', 'Daily List Letters', 'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');

INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION,
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 27, 'FLL', 'Firm List Letters', 'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');



UPDATE CJI_VERSION Set
SCHEMA_VERSION = '0_4z_a',
last_update_date = sysdate,
Updated_by = 'XHIBIT',
Display_name = 'CJSE Database Schema 0_4z_a',
Display_seq = 1
Where SCHEMA_NAME = 'CJSE';
