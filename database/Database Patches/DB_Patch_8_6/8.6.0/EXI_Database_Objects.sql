-----------------------------------------------------------------------
--CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------


-- UPDATE the ExISS database, using table EXI_REF_TYPE

--Add new event for Victims Surcharge
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
                     '12120','Victims Surcharge',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Foreign Travel Prohibition (CS)
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
                     '13213','Foreign Travel Prohibition',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Foreign Travel Prohibition (SS)
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
                     '13315','Foreign Travel Prohibition',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Suspended Sentence (CJA2003) (Neither Concurrent or Consecutive)
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
                     '13316','Suspended Sentence (CJA2003) (Neither Concurrent or Consectutive)',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Extended Sentence Discretional Over 18
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
                     '13503','Extended Sentence Discretional Over 18',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Extended Sentence Automatic Over 18
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
                     '13504','Extended Sentence Automatic Over 18',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Extended Sentence Discretional Under 18
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
                     '13505','Extended Sentence Discretional Under 18',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Extended Sentence Automatic Under 18
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
                     '13506','Extended Sentence Automatic Under 18',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;


--Add new event for Mandatory Life Sentence for Second Listed Offence
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
                     '11523','Mandatory Life Sentence for Second Listed Offence',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Mandatory Life Sentence for Second Listed Offence (Young Offender)
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
                     '11524','Mandatory Life Sentence for Second Listed Offence (Young Offender)',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;


--Add new event for Remand to Local Authority Accommodation
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
                     '10314','Remand to Local Authority Accommodation',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Add new event for Remand to Youth Detention Accommodation
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
                     '10315','Remand to Youth Detention Accommodation',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.5');

COMMIT;

--Update schema version info
update exi_ref_type set version = '5.5';

COMMIT;
