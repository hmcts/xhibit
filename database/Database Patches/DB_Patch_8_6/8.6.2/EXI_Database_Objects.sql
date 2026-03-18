-----------------------------------------------------------------------
--CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------


-- UPDATE the ExISS database, using table EXI_REF_TYPE

--Add new event for related disposal "Participation in Rehabilitation Activity (Community Sentence)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13214','Participation in Rehabilitation Activity (Community Sentence)',
'','http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.6');

COMMIT;

--Add new event for related disposal "Participation in Rehabilitation Activity (Suspended Sentence)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13317','Participation in Rehabilitation Activity (Suspended Sentence)',
'','http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.6');

COMMIT;


--Add new event for related disposal "Criminal Behaviour Order"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'11812','Criminal Behaviour Order',
'','http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.6');

COMMIT;

--Add new event for related disposal "Sexual Harm Prevention Order"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12416','Sexual Harm Prevention Order',
'','http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.6');

COMMIT;


--Add new event for related disposal "Interim Sexual Harm Prevention Order"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12417','Interim Sexual Harm Prevention Order',
'','http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.6');

COMMIT;


--Add new event for related disposal "May be placed on Barring List"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12418','May be placed on Barring List',
'','http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.6');

COMMIT;

--Add new event for Custodial Sentence Imposed
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DEFENDANT'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10316','Custodial Sentence Imposed',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Add new event for Defendant Acquitted
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DEFENDANT'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10317','Defendant Acquitted',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;


--Add new event for Breach of Community Order
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DEFENDANT'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10318','Breach of Community Order',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Add new event for Already Sentenced
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DEFENDANT'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10319','Already Sentenced',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Add new event for Defendant Read
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CASE'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10631','Defendant read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Add new event for Interpreter read
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CASE'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10632','Interpreter read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Add new event for Witness read
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CASE'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10817','Witness read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Add new event for Appellant read
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CASE'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10818','Appellant read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Add new event for Interpreter read
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CASE'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10819','Interpreter read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Add new event for Witness read
INSERT INTO EXI_REF_TYPE (TYPE_ID,
                               OPERATION_ID, 
                               GROUP_ID, 
                               INTERNAL_CODE,
                               INTERNAL_NAME, 
                               STYLESHEET_NAME, 
                               SCHEMA_NAME,
                               SCHEMA_LOCATION, 
                               SCHEMA_VERSION,
                               SECURITY_CLASSIFICATION, 
                               VERSION) 
              VALUES(NULL,
                     (SELECT operation_id FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CASE'),
                     (SELECT group_id FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
                     '10630','Witness read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.6');

COMMIT;

--Update schema version info
update exi_ref_type set version = '5.6';

COMMIT;
