
-------------------------------------------------------------------------------
--
-- This element of calls SQL relating to CCN1388 
--
-------------------------------------------------------------------------------
-- Put a call to your SQL file here e.g. @xhb_Form5061B.sql


--Update Package that populates the Orders reference table DB table in XHIBIT.
--The package is called by the broker Reference data transfer maps
@xhb_post_merc_ref_data_pkg_b.sql

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
