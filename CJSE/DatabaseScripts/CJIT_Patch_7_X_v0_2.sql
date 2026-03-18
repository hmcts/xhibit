/* This script will be used to update Production from 0_4z_b to the latest level - 7_X_v0_1 */
/* This update is adds a version table to the CJSE database in order to keep track of schema versions */


/* PR Fix */
Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/Indictment-v2-4.xsl'
where DOCUMENT_TYPE_ID =5;

/* RFC 1354 */
/* Petty Session to Local Justice */

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/CommunityRehabOrder-v2-3.xsl'
 where DOCUMENT_TYPE_ID =7;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/CommunityPunishmentOrder-v2-3.xsl'
 where DOCUMENT_TYPE_ID =8;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/CommunityPunishmentRehabOrder-v2-3.xsl'
 where DOCUMENT_TYPE_ID =9;


/* CR 28 */

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/BailOrder-v2-3.xsl'
 where DOCUMENT_TYPE_ID =11;



/****************/
/* For RFC 1344 */
/****************/
Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v2-1.xsd'
 where DOCUMENT_TYPE_ID =12;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v2-1.xsl'
 where DOCUMENT_TYPE_ID =13;


/*************************/
/* Add new document type */
/*************************/

Insert into CJI_DOCUMENT_TYPE values 
 (28,'CO','Community Order',
'COMMORDER','http://www.courtservice.gov.uk/transforms/courtservice/CommunityOrder-v2.xsl',
 'NPM',
'BITS.CommunityOrderHandler', 'BITS.XMLTransformHandler', 'Y', 'N','Y', 'N',
'http://www.courtservice.gov.uk/transforms/courtservice CommunityOrder-v4.xsd');

Insert into CJI_DOCUMENT_SECURITY values (	234	,	28	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	235	,	28	,	2	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	236	,	28	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	237	,	28	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	238	,	28	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	239	,	28	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	240	,	28	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	241	,	28	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	242	,	28	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	243	,	28	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	244	,	28	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	245	,	28	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	246	,	28	,	13	, sysdate);


/**********************************/
/* Update to reference new schema */
/**********************************/

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice DailyList-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 1;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice RunningList-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 2;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice WarnedList-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 3;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice FirmList-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 4;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice Indictment-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice Skeleton-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 6;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice CommunityRehabOrder-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 7;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentOrder-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 8;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentRehabOrder-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 9;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice RemandOrder-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 10;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice BailOrder-v4.xsd' 
WHERE DOCUMENT_TYPE_ID = 11;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 12;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 13;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 14;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 15;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 16;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 20;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4.xsd'
WHERE DOCUMENT_TYPE_ID = 21;



/*************************/
/* update Version No     */
/*************************/


UPDATE CJI_VERSION Set
SCHEMA_VERSION = '7_X_v0_1',
last_update_date = sysdate,
Updated_by = 'XHIBIT',
Display_name = 'CJSE Database Schema 7_X_v0_1',
Display_seq = 1
Where SCHEMA_NAME = 'CJSE';
