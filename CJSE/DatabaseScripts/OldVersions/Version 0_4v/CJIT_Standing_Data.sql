/*****************************************/
/* CJIT Insert Standing Data - v0.04v    */
/*****************************************/


/* Insert Document Status Data */

Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (1,'New');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (2,'Sent to BITS');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (3,'Registered');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (4,'Failed Registration');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (5,'Failed Registration-TimeOut');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (6,'Deregistration Requested');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (7,'Failed DeRegistration');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (8,'Delete');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (9,'Sent to BITS Failed');
Insert into CJI_DOCUMENT_STATUS (status_id , status_text )  Values (10,'Deregistration Successful');

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
 (5,'IN','Indictment','INDICTMENT','http://www.courtservice.gov.uk/transforms/courtservice/Indictment-v2-2.xsl', 
'http://www.courtservice.gov.uk/transforms/courtservice Indictment-v2.xsd','NPM',
'BITS.IndictmentOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values
 (6,'SS','Skeleton Schedule',
'SKELETONSCHEDULE','http://www.courtservice.gov.uk/transforms/courtservice/Skeleton-v2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice Skeleton-v2.xsd','NPM',
'BITS.SkeletonScheduleHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (7,'CRO','Community Rehabilitation Order',
'REHABORDER','http://www.courtservice.gov.uk/transforms/courtservice/CommunityRehabOrder-v2-2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice CommunityRehabOrder-v2.xsd', 'NPM',
'BITS.CommunityRehabOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (8,'CPO','Community Punishment Order',
'COMMPUNISHMENTORDER','http://www.courtservice.gov.uk/transforms/courtservice/CommunityPunishmentOrder-v2-2.xsl', 
'http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentOrder-v2.xsd','NPM',
'BITS.CommunityPunishmentOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (9,'CPR','Community Punishment and Rehabilitation Order',
'COMMPUNISHMENTANDREHABORDER','http://www.courtservice.gov.uk/transforms/courtservice/CommunityPunishmentRehabOrder-v2-2.xsl',
'http://www.courtservice.gov.uk/transforms/courtservice CommunityPunishmentRehabOrder-v2.xsd', 'NPM',
'BITS.CommunityPunishmentRehabOrderHandler', 'BITS.XMLTransformHandler', 'Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values
 (10,'RO','Remand Order','REMANDORDER','http://www.courtservice.gov.uk/transforms/courtservice/RemandOrder-v2-2.xsl', 
'http://www.courtservice.gov.uk/transforms/courtservice RemandOrder-v2.xsd', 'NPM',
'BITS.RemandOrderHandler', 'BITS.XMLTransformHandler','Y', 'N','N', 'Y');

Insert into CJI_DOCUMENT_TYPE values 
 (11,'BO','Bail Order','BAILORDER','http://www.courtservice.gov.uk/transforms/courtservice/BailOrder-v2-2.xsl',
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
'COMMFORSENTRECORDSHEET','http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-2.xsl',
'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v2.xsd', 'NPM',
'BITS.CommittalRecordSheetHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (15,'AR','Appeal Record Sheet',
'APPEALRECORDSHEET','http://www.courtservice.gov.uk/transforms/courtservice/AppealRecordSheet-v2-2.xsl',
'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v2.xsd', 'NPM',
'BITS.AppealRecordSheetHandler', 'BITS.XMLTransformHandler','Y', 'N','Y', 'N');

Insert into CJI_DOCUMENT_TYPE values 
 (16,'TR','Trial Record Sheet','TRIALRECORDSHEET',
'http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2-1.xsl', 
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
 (20,'BW','Bench Warrant','BENCHWARRANT','http://www.courtservice.gov.uk/transforms/courtservice/BenchWarrant-v2-2.xsl',
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


/* Insert CJO Security Role Data */

Insert into CJI_CJO_ROLE values (1, 'POL','Police Service','POL','Used by any police Force','Y');
Insert into CJI_CJO_ROLE values (2, 'CC','Crown Court','CC','Used by any CC','Y');
Insert into CJI_CJO_ROLE values (3, 'MC', 'Magistrates Court','MC','Used by any MC','Y');
Insert into CJI_CJO_ROLE values (4, 'CPS', 'Crown Prosecution','CPS','Used by any CPS','Y');
Insert into CJI_CJO_ROLE values (5, 'PRI', 'Prison Service','PRI','Used by any PRI','Y');
Insert into CJI_CJO_ROLE values (6, 'PRO', 'Probation Service','PRO','Used by any PRO','Y');
Insert into CJI_CJO_ROLE values (7, 'DEF', 'Defence','DEF','Used by any Defence','Y');
Insert into CJI_CJO_ROLE values (8, 'YOT', 'Youth Offending Team','YOT','Used by any YOT','Y');
Insert into CJI_CJO_ROLE values (9, 'WS', 'Witness Services','WS','Used by any WS','Y');
Insert into CJI_CJO_ROLE values (10, 'VS', 'Victim Support' ,'VS','Used by any VS','Y');
Insert into CJI_CJO_ROLE values (11, 'PA', 'Prosecution Advocate' ,'PA','Used by any PA','Y');
Insert into CJI_CJO_ROLE values (12, 'DA', 'Defence Advocate','DA','Used by any DA','Y');
Insert into CJI_CJO_ROLE values (13, 'JUD','Judge','JUD','Used by any Judge','Y');



/* Insert Security Role and Document Data */

/* should not have Prison accesss to normal Daily list - 1,5 */
/* Should only have 21,2 and 21,5 - Prison Daily */
/* CC has access to all documents by default  - so do not include that in Security*/
/* ID/ DOc Type/ CJO */

Insert into CJI_DOCUMENT_SECURITY values (	1	,	1	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	2	,	2	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	3	,	3	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	4	,	4	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	5	,	5	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	6	,	6	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	7	,	7	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	8	,	8	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	9	,	9	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	10	,	10	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	11	,	11	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	12	,	12	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	13	,	13	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	14	,	14	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	15	,	15	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	16	,	16	,	1	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	17	,	20	,	1	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	37	,	1	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	38	,	2	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	39	,	3	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	40	,	4	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	41	,	5	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	42	,	6	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	43	,	7	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	44	,	8	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	45	,	9	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	46	,	10	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	47	,	11	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	48	,	12	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	49	,	13	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	50	,	14	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	51	,	15	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	52	,	16	,	3	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	53	,	20	,	3	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	55	,	1	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	56	,	2	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	57	,	3	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	58	,	4	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	59	,	5	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	60	,	6	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	61	,	7	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	62	,	8	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	63	,	9	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	64	,	10	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	65	,	11	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	66	,	12	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	67	,	13	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	68	,	14	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	69	,	15	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	70	,	16	,	4	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	71	,	20	,	4	, sysdate);
						
						
Insert into CJI_DOCUMENT_SECURITY values (	74	,	2	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	75	,	3	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	76	,	4	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	77	,	5	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	78	,	6	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	79	,	7	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	80	,	8	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	81	,	9	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	82	,	10	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	83	,	11	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	84	,	12	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	85	,	13	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	86	,	14	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	87	,	15	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	88	,	16	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	89	,	20	,	5	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	90	,	21	,	5	, sysdate);

Insert into CJI_DOCUMENT_SECURITY values (	91	,	1	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	92	,	2	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	93	,	3	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	94	,	4	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	95	,	5	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	96	,	6	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	97	,	7	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	98	,	8	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	99	,	9	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	100	,	10	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	101	,	11	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	102	,	12	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	103	,	13	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	104	,	14	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	105	,	15	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	106	,	16	,	6	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	107	,	20	,	6	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	109	,	1	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	110	,	2	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	111	,	3	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	112	,	4	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	113	,	5	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	114	,	6	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	115	,	7	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	116	,	8	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	117	,	9	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	118	,	10	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	119	,	11	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	120	,	12	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	121	,	13	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	122	,	14	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	123	,	15	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	124	,	16	,	7	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	125	,	20	,	7	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	127	,	1	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	128	,	2	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	129	,	3	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	130	,	4	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	131	,	5	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	132	,	6	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	133	,	7	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	134	,	8	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	135	,	9	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	136	,	10	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	137	,	11	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	138	,	12	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	139	,	13	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	140	,	14	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	141	,	15	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	142	,	16	,	8	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	143	,	20	,	8	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	145	,	1	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	146	,	2	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	147	,	3	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	148	,	4	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	149	,	5	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	150	,	6	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	151	,	7	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	152	,	8	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	153	,	9	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	154	,	10	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	155	,	11	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	156	,	12	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	157	,	13	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	158	,	14	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	159	,	15	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	160	,	16	,	9	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	161	,	20	,	9	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	163	,	1	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	164	,	2	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	165	,	3	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	166	,	4	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	167	,	5	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	168	,	6	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	169	,	7	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	170	,	8	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	171	,	9	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	172	,	10	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	173	,	11	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	174	,	12	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	175	,	13	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	176	,	14	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	177	,	15	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	178	,	16	,	10	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	179	,	20	,	10	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	181	,	1	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	182	,	2	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	183	,	3	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	184	,	4	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	185	,	5	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	186	,	6	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	187	,	7	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	188	,	8	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	189	,	9	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	190	,	10	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	191	,	11	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	192	,	12	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	193	,	13	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	194	,	14	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	195	,	15	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	196	,	16	,	11	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	197	,	20	,	11	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	199	,	1	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	200	,	2	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	201	,	3	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	202	,	4	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	203	,	5	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	204	,	6	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	205	,	7	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	206	,	8	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	207	,	9	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	208	,	10	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	209	,	11	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	210	,	12	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	211	,	13	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	212	,	14	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	213	,	15	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	214	,	16	,	12	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	215	,	20	,	12	, sysdate);
						
Insert into CJI_DOCUMENT_SECURITY values (	217	,	1	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	218	,	2	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	219	,	3	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	220	,	4	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	221	,	5	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	222	,	6	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	223	,	7	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	224	,	8	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	225	,	9	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	226	,	10	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	227	,	11	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	228	,	12	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	229	,	13	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	230	,	14	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	231	,	15	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	232	,	16	,	13	, sysdate);
Insert into CJI_DOCUMENT_SECURITY values (	233	,	20	,	13	, sysdate);


/* Insert Event Status Data */

Insert into CJI_event_status values (1, 'New');
Insert into CJI_event_status values (2, 'Sent');
Insert into CJI_event_status values (3, 'Delete');
Insert into CJI_event_status values (4, 'Event Delivered');
Insert into CJI_event_status values (5, 'Event Delivered Failed');

/* Insert Valid CJSE Operation Names */

Insert into CJI_Event_Operation_Request values (1,'GenerateOtherCaseFileEventOperation');
Insert into CJI_Event_Operation_Request values (2,'GenerateOtherCRNEventOperation');
Insert into CJI_Event_Operation_Request values (3,'GenerateOtherDefendantEventOperation');
Insert into CJI_Event_Operation_Request values (4,'GenerateOtherDocumentEventOperation');

/* Insert Valid CJIP Status Names */

Insert into CJI_CJIP_Message_status values (1,'New');
Insert into CJI_CJIP_Message_status values (2,'Sent to CJIP');
Insert into CJI_CJIP_Message_status values (3,'Sent to CJIP - Failed');
Insert into CJI_CJIP_Message_status values (4,'Operation Failed');
Insert into CJI_CJIP_Message_status values (5,'Operation Success');
Insert into CJI_CJIP_Message_status values (6,'Delete');

/* Insert Valid AHM Status Names */

Insert into CJI_AHM_STATUS values (1, 'New');
Insert into CJI_AHM_STATUS values (2, 'Sent To Aggregator');
Insert into CJI_AHM_STATUS values (3, 'Delivered');
Insert into CJI_AHM_STATUS values (4, 'Delivery Pending');
Insert into CJI_AHM_STATUS values (5, 'Delivery Failed');
Insert into CJI_AHM_STATUS values (6, 'Delete');
Insert into CJI_AHM_STATUS values (7, 'Delivery to Aggregator Failed');

/* Insert Valid AHM Aggregator Status Names */

insert into CJI_AHM_AGG_STATUS values (1,4);
insert into CJI_AHM_AGG_STATUS values (2,3);
insert into CJI_AHM_AGG_STATUS values (3,5);
insert into CJI_AHM_AGG_STATUS values (4,5);
insert into CJI_AHM_AGG_STATUS values (5,4);
insert into CJI_AHM_AGG_STATUS values (6,3);

/* Insert Valid AHM Aggregator Reply Status Names */

Insert into CJI_AHM_REPLY_STATUS values (1, 'New');
Insert into CJI_AHM_REPLY_STATUS values (2, 'Read by Xhibit');
Insert into CJI_AHM_REPLY_STATUS values (3, 'Invalid Response Code');
Insert into CJI_AHM_REPLY_STATUS values (4, 'Delete');

/* Insert Valid AHM Device Types */

Insert into CJI_AHM_DEVICE_TYPE values (1, 'SMS','mnet:gsm-uk3:pref');
Insert into CJI_AHM_DEVICE_TYPE values (2, 'PageOne','mnet:pageone-gb:pref');
Insert into CJI_AHM_DEVICE_TYPE values (3, 'BT EasyReach','mnet:bteasyreach-gb:pref');
Insert into CJI_AHM_DEVICE_TYPE values (4, 'Vodafone Paging','mnet:vodafonepaging-gb:pref');

/* Insert Parameter values */
 
Insert into CJI_Parameter_value values ('BITSLastMessageTime', '2002-11-08T05:04:02.1234567');
Insert into CJI_Parameter_value values ('BITSPollInterval','5');
Insert into CJI_Parameter_value values ('BITSRegisterTimeOut','86400');
Insert into CJI_Parameter_value values ('BITSRegisterCheckCycle','86400');
Insert into CJI_Parameter_value values ('CJIPLastMessageTime', '2002-11-08T05:04:02.1234567');
Insert into CJI_Parameter_value values ('CJIPPollInterval','2');
Insert into CJI_Parameter_value values ('CJIPOperationTimeOut','3600');
Insert into CJI_Parameter_value values ('CJIPRegisterCheckCycle','86400');
Insert into CJI_Parameter_value values ('AggDeliveryTimeOut','3600');
Insert into CJI_Parameter_value values ('AggLastMessageTime', 'Sat, 14 Jul 2002 19:30:00 GMT');
Insert into CJI_Parameter_value values ('AggPollIntervalStatus','300');
Insert into CJI_Parameter_value values ('AggPollIntervalReply','300');
Insert into CJI_Parameter_value values ('AggRegisterTimeOut','3600');
Insert into CJI_Parameter_value values ('AggRegisterCheckCycle','86400');
Insert into CJI_Parameter_value values ('DocumentDeleteTimeOut','432000');
Insert into CJI_Parameter_value values ('AggDeleteTimeOut','86400');
Insert into CJI_Parameter_value values ('CJIPDeleteTimeOut','432000');
Insert into CJI_Parameter_value values ('DocumentExpiryTimeOut','2592000');
Insert into CJI_Parameter_value values ('AggMessageExpiryInterval','25200');
Insert into CJI_Parameter_value values ('AggLastMessageReplyTime', 'Sat, 14 Jul 2002 19:30:00 GMT');
Insert into CJI_Parameter_value values ('BITSDeleteTimeOut', '432000');
Insert into CJI_Parameter_value values ('MobileSysMONumber', '447781484135');
Insert into CJI_Parameter_value values ('MobileSysReplyMessage', 'An Incorrect reply code was received - this is an automated response - please resend your message with the correct code');

/* Insert CJIP Request Status values */

Insert into CJI_CJIP_Request_status values (1, 'New');
Insert into CJI_CJIP_Request_status values (2, 'Sent');
Insert into CJI_CJIP_Request_status values (3, 'Delete');
Insert into CJI_CJIP_Request_status values (4, 'Sent Failed');

/* Insert valid BITS operation names */

Insert into CJI_BITS_MESSAGE_OPERATIONS values (1, 'Register Document');
Insert into CJI_BITS_MESSAGE_OPERATIONS values (2, 'Return Request Document');

