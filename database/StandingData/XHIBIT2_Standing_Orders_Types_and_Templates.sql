/* FILE_NAME: XHIBIT2_Standing_Orders_Types_and_Templates.sql*/
/* ENVIRONMENT: Live System, Pre-production, System test, Integration, Development */
/* DESCRIPTION:The order types that are provided in XHIBIT2, and the templates relating to them */
/* STATUS DESCRIPTION: required for live system */
/* DEPENDENCIES: Dependency on XHB_REF_DISPOSAL data existing */
/* OWNER: Doug Climie */

DELETE FROM XHB_ORDER_TEMPLATE
/

DELETE FROM XHB_ORDER_TYPE
/

COMMIT
/


INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (1, 'BC', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Bail Conditions')
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (2, 'BW', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Bench Warrant')
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (3, 'CMPO', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Community Punishment Order (5042)')
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (4, 'CMPRO', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Community Punishment Rehabilitation Order (5042a)')
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (5, 'COMY', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Commitment of Young Offender (5044)')
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (6, 'CRO', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Community Rehabilitation Order (5037)')
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (7, 'IMPO', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Imprisonment (5035)')
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (8, 'RC', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Remand in Custody (5038)')
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (9, 'COMSENT', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Community Order');
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (10, 'SUS', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Suspended Sentence Order (6057)');
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (11, 'IO5035C', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Imprisonment Order(5035C)');
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (12, 'YO5044C', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Young Offenders Order - under 18 (5044C)');
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (13, 'YO5044D', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Young Offenders Order - 18 to 21 (5044D)');
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (14, 'BWA', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Warrant After Failure to Attend (5061A)');
/
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (15, 'BWC', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Warrant After Failure to Comply (5061B)');
/

INSERT INTO XHB_ORDER_TEMPLATE VALUES (1, '/metadata/OrderFOPTransform.xslt', '/metadata/BailOrder_Narrative.xml', '/metadata/BailOrderTemplate.xml', 1, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 1, null)
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (2, '/metadata/OrderFOPTransform.xslt', '/metadata/BenchWarrantOrder_Narrative.xml', '/metadata/BenchWarrantOrderTemplate.xml', 2, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 2, null)
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (3, '/metadata/OrderFOPTransform.xslt', '/metadata/CPO_Narrative.xml', '/metadata/CPOrderTemplate.xml', 3, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 3, null)
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (4, '/metadata/OrderFOPTransform.xslt', '/metadata/CPRO_Narrative.xml', '/metadata/CPROrderTemplate.xml', 4, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 4, null)
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (5, '/metadata/OrderFOPTransform.xslt', '/metadata/YoungOffendersOrder_Narrative.xml', '/metadata/YOIOrderTemplate.xml', 5, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 5, null)
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (6, '/metadata/OrderFOPTransform.xslt', '/metadata/CRO_Narrative.xml', '/metadata/CROrderTemplate.xml', 6, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 6, null)
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (7, '/metadata/OrderFOPTransform.xslt', '/metadata/ImprisonmentOrder_Narrative.xml', '/metadata/ImprisonmentOrderTemplate.xml', 7, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 7, null)
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (8, '/metadata/OrderFOPTransform.xslt', '/metadata/RemandOrder_Narrative.xml', '/metadata/RemandOrderTemplate.xml', 8, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 8, null)
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (9,  '/metadata/OrderFOPTransform.xslt', '/metadata/COMSENT_Narrative.xml', '/metadata/COMSENTOrderTemplate.xml', 9, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 9, NULL);
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (10,  '/metadata/OrderFOPTransform.xslt', '/metadata/SuspendedSentenceOrder_Narrative.xml', '/metadata/SuspendedSentenceOrderTemplate.xml',  10, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 10, NULL);
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (11, '/metadata/OrderFOPTransform.xslt', '/metadata/ImprisonmentOrder5035C_Narrative.xml', '/metadata/ImprisonmentOrder5035CTemplate.xml', 11, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 11, NULL);
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (12, '/metadata/OrderFOPTransform.xslt', '/metadata/YoungOffendersOrder5044C_Narrative.xml', '/metadata/YoungOffendersOrder5044CTemplate.xml', 12, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 12, NULL);
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (13, '/metadata/OrderFOPTransform.xslt', '/metadata/YoungOffendersOrder5044D_Narrative.xml', '/metadata/YoungOffendersOrder5044DTemplate.xml', 13, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 13, NULL);
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (14, '/metadata/OrderFOPTransform.xslt', '/metadata/WarrantAfterFailureOrder5061_Narrative.xml', '/metadata/WarrantAfterFailureOrder5061Template.xml', 14, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 14, NULL);
/
INSERT INTO XHB_ORDER_TEMPLATE VALUES (15, '/metadata/OrderFOPTransform.xslt', '/metadata/WarrantAfterFailureOrder5061_Narrative.xml', '/metadata/WarrantAfterFailureOrder5061Template.xml', 15, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 15, NULL);
/


  -- Bench Warrant after failure to Comply (5061B) - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (15, 15, NULL, 0);
/
  -- Bench Warrant after failure to Attend (5061A) - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (14, 14, NULL, 0);  
/
  -- Custodial/Young Offender Order for 18-21s - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (13, 13, NULL, 0);
/
  -- Custodial/Young Offender Order for under 18s - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (12, 12, NULL, 0);
/
  -- Imprisonment Order 5035C - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (11, 11, NULL, 0);
/
  -- Suspended Sentence Order - replaced by itself
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (10, 10, 10, 0);
/
  -- New Community Order replaced by New Community Order
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (9, 9, 9, 0);
/
  -- Remand Order - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (8, 8, NULL, 0);
/
  -- Imprisonment Order - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (7, 7, NULL, 0);
/
  -- Community Rehabilitation Order replaced by New Community Order
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (6, 6, 9, 0);
/
  -- Young Offender Order -  not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (5, 5, NULL, 0);
/
  -- Community Punishment and Rehabilitation Order replaced by New Community Order
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (4, 4, 9, 0);
/
  -- Community Punishment Order replaced by New Community Order
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (3, 3, 9, 0);
/
  -- Bench Warrant - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (2, 2, NULL, 0);
/
  -- Bail Order - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (1, 1, NULL, 0);
/

COMMIT
/

PROMPT Set ref_disposal_id foreign key value where 

update xhb_order_type xot
set xot.ref_disposal_id = (select ref_disposal_id from xhb_ref_disposal xrd
where xrd.disposal_code = xot.code)
/

PROMPT Now set the description via the ref_disposal_id link. this is set initially above
PROMPT but is updated here in case it has changed

update xhb_order_type xot
set xot.description = (select disposal_title from xhb_ref_disposal xrd
where xrd.ref_disposal_id = xot.ref_disposal_id)
where xot.ref_disposal_id is not null
/
COMMIT
/