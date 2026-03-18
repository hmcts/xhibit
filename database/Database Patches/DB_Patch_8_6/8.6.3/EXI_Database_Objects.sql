-----------------------------------------------------------------------
--CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------


-- UPDATE the ExISS database, using table EXI_REF_GROUP

INSERT INTO EXI_REF_GROUP VALUES (NULL, 'XHIBITDetentionAndTrainingOrder');
INSERT INTO EXI_REF_GROUP VALUES (NULL, 'XHIBITYouthRehabilitationOrder');
INSERT INTO EXI_REF_GROUP VALUES (NULL, 'XHIBITDVLAD20');

COMMIT;

-- UPDATE the ExISS database, using table EXI_REF_TYPE

--Add new event for related disposal "Criminal Courts Charge"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12121','Criminal Courts Charge',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Slavery and Trafficking Reparation Order"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12122','Slavery and Trafficking Reparation Order',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;


--Add new event for related disposal "Slavery and Trafficking Reparaion Order with time to pay"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12123','Slavery and Trafficking Reparation Order with time to pay',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Slavery and Trafficking Reparation Order by installments"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12124','Slavery and Trafficking Reparation Order by installments',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;


--Add new event for related disposal "Forfeiture Order under Modern Slavery Act 2015"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12513','Forfeiture Order under Modern Salvery Act 2015',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;


--Add new event for related disposal "Slavery and Trafficking Prohibition Order"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12419','Slavery and Trafficking Prohibition Order',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Interim Slavery and Trafficking Prohibition Order"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'12420','Interim Slavery and Trafficking Prohibition Order',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Extended Activity Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13800','Extended Activity Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Supervision Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13801','Supervision Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Unpaid Work Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13802','Unpaid Work Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Programme Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13803','Programme Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Attendance Centre Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13804','Attendance Centre Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Prohibited Activity Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13805','Prohibited Activity Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Curfew Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13806','Curfew Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Exclusion Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13807','Exclusion Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Residence Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13808','Residence Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Local Authority Residence Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13809','Local Authority Residence Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Fostering requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13810','Fostering Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Mental Health Treatment Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13811','Mental Health Treatment Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Drug Treatment Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13812','Drug Treatment Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Drug Testing Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13813','Drug Testing Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Intoxicating Substance Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13814','Intoxicating Substance Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Education Requirement (YRO)"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'13815','Education Requirement',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Imprisonment - Extended under s236A CJA2003"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'11525','Imprisonment - Extended under s236A CJA2003',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Imprisonment - Minimum Imposed after 3 strikes (Young Offender) - Extended under s236A CJA2003"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'11526','Imprisonment - Minimum Imposed after 3 strikes (Young Offender) - Extended under s236A CJA2003',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Imprisonment - Minimum Imposed after 3 strikes - Extended under s236A CJA2003"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'11527','Imprisonment - Minimum Imposed after 3 strikes - Extended under s236A CJA2003',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for related disposal "Detention in Y.O.I. - Extended under s236A CJA2003"

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='CRN'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITEvent'),
'11528','Detention in Y.O.I. - Extended under s236A CJA2003',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;


--Add new event for new Detention And Training Order document

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITDetentionAndTrainingOrder'),
'DTO','Detention And Training Order',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for new Youth Rehabilitation Order document

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITYouthRehabilitationOrder'),
'YRO','Youth Rehabilitation Order',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;

--Add new event for new DVLA D20 document

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITDVLAD20'),
'D20','DVLA D20',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.7');

COMMIT;


--- Add missing Live events ---
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
                     '10316','Custodial Sentence Imposed',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10317','Defendant Acquitted',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10318','Breach of Community Order',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10319','Already Sentenced',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10631','Defendant read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10632','Interpreter read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10633','Appellant read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

COMMIT;

--Add new generic event for Witness read
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
                     '10630','Witness read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10817','Witness read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10818','Appellant read',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

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
                     '10819','Interpreter read',NU	LL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.7');

COMMIT;


--Update schema version info
update exi_ref_type set version = '5.7';

COMMIT;
