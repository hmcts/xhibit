DROP TABLE CJI_DOCUMENT_TYPE
/

CREATE TABLE CJI_DOCUMENT_TYPE (
	DOCUMENT_TYPE_ID NUMBER ( 8 ) NOT NULL,
	INTERNAL_CODE CHAR ( 3 ) NOT NULL,
	INTERNAL_NAME VARCHAR2 ( 100 ) NOT NULL,
	EXTERNAL_NAME VARCHAR2 ( 50 ) NOT NULL,
	STYLESHEET_NAME VARCHAR2 ( 255 ),
	SCHEMA_NAME VARCHAR2 ( 255 ),
	SECURITY_CLASSIFICATION VARCHAR2 ( 50 ) NOT NULL,
	BITS_DOCUMENT_CONFIG VARCHAR2 ( 50 ),
	BITS_TRANSFORM_CONFIG VARCHAR2 ( 50 ),
	REGISTERED_IN_CJSE VARCHAR2 ( 1 ) NOT NULL,
	REGISTER_AGAINST_LOCATION VARCHAR2 ( 1 ) NOT NULL,
	REGISTER_AGAINST_CRN VARCHAR2 ( 1 ) NOT NULL,
	REGISTER_AGAINST_DEFENDANT VARCHAR2 ( 1 ) NOT NULL
	)
TABLESPACE DATAD
   STORAGE  (
   INITIAL 1M
   NEXT 1M
   PCTINCREASE 0 
  )
/
ALTER TABLE CJI_DOCUMENT_TYPE
       ADD  (PRIMARY KEY (DOCUMENT_TYPE_ID)
       USING INDEX
      TABLESPACE DATAX
   STORAGE  (
   INITIAL 1M
   NEXT 1M
   PCTINCREASE 0 
  ) ) 
/

/* Insert Document Type Data */

Insert into CJI_DOCUMENT_TYPE values
 (1,'DL','Daily List',
'DAILYLIST','http://www.courtservice.gov.uk/transforms/courtservice/DailyList-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice DailyList-v2.xsd','NPM',
'BITS.DailyListHandler', 'BITS.XMLTransformHandler','Y', 'N', 'Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (2,'RL','Running List','RUNNINGLIST','http://www.courtservice.gov.uk/transforms/courtservice/RunningList-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice RunningList-v2.xsd','NPM',
'BITS.RunningListHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values
 (3,'WL','Warned List',
'WARNEDLIST','http://www.courtservice.gov.uk/transforms/courtservice/WarnedList-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice WarnedList-v2.xsd','NPM',
'BITS.WarnedListHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (4,'FL','Firm List',
'FIRMLIST','http://www.courtservice.gov.uk/transforms/courtservice/FirmList-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice FirmList-v2.xsd','NPM',
'BITS.FirmListHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values
 (5,'IN','Indictment','INDICTMENT','http://www.courtservice.gov.uk/transforms/courtservice/Indictment-v2.xsl', 
'http://www.courtservice.gov.uk/transforms/courtservice Indictment-v2.xsd','NPM',
'BITS.IndictmentOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values
 (6,'SS','Skeleton Schedule',
'SKELETONSCHEDULE','http://www.courtservice.gov.uk/transforms/courtservice/Skeleton-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice Skeleton-v2.xsd','NPM',
'BITS.SkeletonScheduleHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (7,'CRO','Community Rehabilitation Order',
'REHABORDER','http://www.courtservice.gov.uk/transforms/courtservice/CommunityRehabOrder-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice CommunityRehabOrder-v2.xsd', 'NPM',
'BITS.CommunityRehabOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (8,'CPO','Community Punishment Order',
'COMMPUNISHMENTORDER','http://www.courtservice.gov.uk/transforms/courtservice/CommunityPunishmentOrder-v2.xsl', 
'http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentOrder-v2.xsd','NPM',
'BITS.CommunityPunishmentOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (9,'CPR','Community Punishment and Rehabilitation Order',
'COMMPUNISHMENTANDREHABORDER','http://www.courtservice.gov.uk/transforms/courtservice/CommunityPunishmentRehabOrder-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentRehabOrder-v2.xsd', 'NPM',
'BITS.CommunityPunishmentRehabOrderHandler', 'BITS.XMLTransformHandler', 'Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values
 (10,'RO','Remand Order','REMANDORDER','http://www.courtservice.gov.uk/transforms/courtservice/RemandOrder-v2.xsl', 
'http://www.courtservice.gov.uk/transforms/courtservice RemandOrder-v2.xsd', 'NPM',
'BITS.RemandOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','N', 'Y');

Insert into CJI_DOCUMENT_TYPE values 
 (11,'BO','Bail Order','BAILORDER','http://www.courtservice.gov.uk/transforms/courtservice/BailOrder-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice BailOrder-v2.xsd' , 'NPM',
'BITS.BailOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','N', 'Y');

Insert into CJI_DOCUMENT_TYPE values 
 (12,'IO','Imprisonment Order',
'IMPRISONMENTORDER','http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v2.xsl', 
'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v2.xsd','NPM',
'BITS.ImprisonmentOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (13,'YOI','YOI Order','YOUNGOFFENDERORDER',
'http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder-v2.xsl', 
'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v2.xsd', 'Restricted',
'BITS.YoungOffenderOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (14,'SR','Committal for Sentence Record Sheet',
'COMMFORSENTRECORDSHEET','http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2.xsl',
'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v2.xsd', 'NPM',
'BITS.CommitalRecordSheetHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (15,'AR','Appeal Record Sheet',
'APPEALRECORDSHEET','http://www.courtservice.gov.uk/transforms/courtservice/AppealRecordSheet-v2.xsl',
'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v2.xsd', 'NPM',
'BITS.AppealRecordSheetHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (16,'TR','Trial Record Sheet','TRIALRECORDSHEET',
'http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2.xsl', 
'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v2.xsd','NPM',
'BITS.TrialRecordSheetHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (17,'WW','Witness Warrant Order',
'Unknown','Unknown', 
'Unknown','NPM',
'Unknown', 'Unknown','N', 'N','N', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (18,'MC','Memorandum Of Conviction','Unknown','Unknown',
'Unknown','NPM ',
'Unknown','Unknown','N', 'N','N', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (19,'NA','Notice Of Appeal','Unknown','Unknown',
'Unknown','NPM ',
'Unknown','Unknown','N', 'N','N', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (20,'BW','Bench Warrant','BENCHWARRANT','http://www.courtservice.gov.uk/transforms/courtservice/BenchWarrant-v2.xsl',
'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v2.xsd', 'NPM',
'BITS.BenchWarrantHandler', 'BITS.XMLTransformHandler','Y', 'N','N', 'Y');

Insert into CJI_DOCUMENT_TYPE values 
 (21,'DLP','Prison Daily List','DAILYLIST','http://www.courtservice.gov.uk/transforms/courtservice/DailyList-v2.xsl',
'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v2.xsd', 'Restricted',
'BITS.DailyListHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (22,'WLL','Warned List Letters','Unknown','Unknown',
'Unknown','NPM',
'Unknown','Unknown','N', 'N','N', 'N');

commit;
/

