/* This script will be used to update Production 7_X_v0_4  to 7_6_v0_1 */



/**********************************/
/* Update to reference new schema */
/**********************************/

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 1;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice RunningList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 2;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice WarnedList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 3;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice FirmList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 4;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice Indictment-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice Skeleton-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 6;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityRehabOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 7;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 8;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentRehabOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 9;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice RemandOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 10;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BailOrder-v4-2.xsd' 
WHERE DOCUMENT_TYPE_ID = 11;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 12;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 13;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 14;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 15;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 16;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 20;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 21;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 28;

/**********************************/
/* Update to reference new stylesheet */
/**********************************/


Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/dailyListHtml.xsl'
 where DOCUMENT_TYPE_ID =1;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/runningListHtml.xsl'
 where DOCUMENT_TYPE_ID =2;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/warnedListHtml.xsl'
 where DOCUMENT_TYPE_ID =3;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/firmListHtml.xsl'
 where DOCUMENT_TYPE_ID =4;


Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/dailyPrisonListHtml.xsl'
 where DOCUMENT_TYPE_ID =21;



/*************************/
/* update Version No     */
/*************************/


UPDATE CJI_VERSION Set
SCHEMA_VERSION = '7_6_v0_1',
last_update_date = sysdate,
Updated_by = 'XHIBIT',
Display_name = 'CJSE Database Schema 7_6_v0_1',
Display_seq = 1
Where SCHEMA_NAME = 'CJSE';



