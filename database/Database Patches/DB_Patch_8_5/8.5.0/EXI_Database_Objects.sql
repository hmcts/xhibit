-----------------------------------------------------------------------
--CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------


-- UPDATE the ExISS database, using table EXI_REF_TYPE

--Add new event for Disqualified from Working With Children for Life (under 18)
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
                     '12414','Defendant Disqualified from Working With Children for Life (under 18)',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Disqualified from Working With Children for Life (over 18)
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
                     '12415','Defendant Disqualified from Working With Children for Life (over 18)',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Defendant Ordered to be Electronically Monitored
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
                     '13700','Defendant Ordered to be Electronically Monitored',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Electronic Monitoring Requirement Amended
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
                     '13701','Electronic Monitoring Requirement Amended',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Electronic Monitoring_Flag to be Removed
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
                     '13702','Electronic Monitoring_Flag to be Removed',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Defendant Subject to an Electronically Monitored Curfew
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
                     '13703','Defendant Subject to an Electronically Monitored Curfew',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Terms of Electronically Monitored Curfew Amended
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
                     '13704','Terms of Electronically Monitored Curfew Amended',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Requirement for an Electronic Curfew Removed
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
                     '13705','Requirement for an Electronic Curfew Removed',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Bail Conditions Ceased - Sentence Deferred
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
                     '10311','Bail Conditions Ceased - Sentence Deferred',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Bail Conditions Ceased - Non-Custodial Sentence Imposed
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
                     '10313','Bail Conditions Ceased - Non-Custodial Sentence Imposed',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

--Add new event for Bail Conditions Ceased - Defendant Deceased
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
                     '10312','Bail Conditions Ceased - Defendant Deceased',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

-- CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------


-- UPDATE the ExISS database, using table EXI_REF_TYPE

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
                     '13600','Convicted of a sexual offence-victim under 18 years of age-for an indefinite period',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

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
                     '13601','Convicted of a sexual offence-victim under 18 years of age- for 10 years',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;


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
                     '13602','Convicted of a sexual offence-victim under 18 years of age-for 3-7 years',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

COMMIT;

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
                     '13603','Convicted of a sexual offence-victim under 18 years of age-period to be specified later',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

commit;

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
                     '13604','Convicted of a sexual offence-victim under 18 years of age-for another period',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

commit;

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
                     '13605','Convicted of a sexual offence-victim under 18 years of age-for another period',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

commit;                     
                     
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
                              '13606','Convicted of a sexual offence-victim under 18 years of age-for 10 years',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

commit;

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
                              '13607','Convicted of a sexual offence-victim under 18 years of age-for 3 to 7 years',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

commit;


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
                              '13608','Convicted of a sexual offence-victim under 18 years of age-for 3 to 7 years',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

commit;

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
                              '13609','Convicted of a sexual offence-victim over 18 years of age- for another period',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');

commit;

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
                              '13610','Convicted of a sexual offence-victim over 18 years of age- period to be specified later',NULL,'http://www.courtservice.gov.uk/schemas/courtservice',NULL,NULL,NULL,'5.4');


commit;
