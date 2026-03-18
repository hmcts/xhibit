/* This script will be used to update Production from 0_4o to the latest level - 0_4p */


/* For CR23 the Stylesheet name will be different - change made under CR25 */

Update CJI_DOCUMENT_TYPE set stylesheet_name =
'http://www.courtservice.gov.uk/transforms/courtservice/DailyList-v2.xsl'
where DOCUMENT_TYPE_ID =1;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/RunningList-v2.xsl'
where DOCUMENT_TYPE_ID =2;

Update CJI_DOCUMENT_TYPE set stylesheet_name =
'http://www.courtservice.gov.uk/transforms/courtservice/WarnedList-v2.xsl'
where DOCUMENT_TYPE_ID =3;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/FirmList-v2.xsl'
where DOCUMENT_TYPE_ID =4;

Update CJI_DOCUMENT_TYPE set stylesheet_name =
'http://www.courtservice.gov.uk/transforms/courtservice/Indictment-v2.xsl'
 where DOCUMENT_TYPE_ID =5;

Update CJI_DOCUMENT_TYPE set stylesheet_name =
'http://www.courtservice.gov.uk/transforms/courtservice/Skeleton-v2.xsl'
where DOCUMENT_TYPE_ID =6;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/CommunityRehabOrder-v2.xsl'
 where DOCUMENT_TYPE_ID =7;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/CommunityPunishmentOrder-v2.xsl'
 where DOCUMENT_TYPE_ID =8;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/CommunityPunishmentRehabOrder-v2.xsl'
 where DOCUMENT_TYPE_ID =9;

Update CJI_DOCUMENT_TYPE set stylesheet_name =
'http://www.courtservice.gov.uk/transforms/courtservice/RemandOrder-v2.xsl'
 where DOCUMENT_TYPE_ID =10;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/BailOrder-v2.xsl' 
where DOCUMENT_TYPE_ID =11;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v2.xsl'
 where DOCUMENT_TYPE_ID =12;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder-v2.xsl'
where DOCUMENT_TYPE_ID =13;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2.xsl'
where DOCUMENT_TYPE_ID =14;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/AppealRecordSheet-v2.xsl'
where DOCUMENT_TYPE_ID =15;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2.xsl'
where DOCUMENT_TYPE_ID =16;
n
Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/BenchWarrant-v2.xsl'
 where DOCUMENT_TYPE_ID =20;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/DailyList-v2.xsl'
 where DOCUMENT_TYPE_ID =21;


/* For CR23 the Schema name will be different - change made under CR25 */
/* This change requires an addition to the CJI_DOCUMENT_TYPE table     */

alter table CJI_DOCUMENT_TYPE add column SCHEMA_NAME VarChar2(255) null;


Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v2.xsd'
where DOCUMENT_TYPE_ID =1;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice RunningList-v2.xsd'
where DOCUMENT_TYPE_ID =2;

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice WarnedList-v2.xsd'
where DOCUMENT_TYPE_ID =3;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice FirmList-v2.xsd'
where DOCUMENT_TYPE_ID =4;

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice Indictment-v2.xsd'
 where DOCUMENT_TYPE_ID =5;

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice Skeleton-v2.xsd'
where DOCUMENT_TYPE_ID =6;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice CommunityRehabOrder-v2.xsd'
 where DOCUMENT_TYPE_ID =7;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentOrder-v2.xsd'
 where DOCUMENT_TYPE_ID =8;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentRehabOrder-v2.xsd'
 where DOCUMENT_TYPE_ID =9;

Update CJI_DOCUMENT_TYPE set Schema_Name =
'http://www.courtservice.gov.uk/schemas/courtservice RemandOrder-v2.xsd'
 where DOCUMENT_TYPE_ID =10;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice BailOrder-v2.xsd' 
where DOCUMENT_TYPE_ID =11;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v2.xsd'
 where DOCUMENT_TYPE_ID =12;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v2.xsd'
where DOCUMENT_TYPE_ID =13;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v2.xsd'
where DOCUMENT_TYPE_ID =14;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v2.xsd'
where DOCUMENT_TYPE_ID =15;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v2.xsd'
where DOCUMENT_TYPE_ID =16;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'Unknown'
where DOCUMENT_TYPE_ID =17;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'Unknown'
where DOCUMENT_TYPE_ID =18;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'Unknown'
where DOCUMENT_TYPE_ID =19;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v2.xsd'
 where DOCUMENT_TYPE_ID =20;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v2.xsd'
 where DOCUMENT_TYPE_ID =21;

Update CJI_DOCUMENT_TYPE set Schema_Name = 
'Unknown'
where DOCUMENT_TYPE_ID =22;




