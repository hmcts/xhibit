-----------------------------------------------------------------------
--CHANGES TO INCLUDE THE NEW MESSAGE TYPES AND ALTER THE SCHEMA VERSION 
-----------------------------------------------------------------------


-- UPDATE the ExISS database, using table EXI_REF_GROUP

INSERT INTO EXI_REF_GROUP VALUES (NULL, 'XHIBITActionOnConditionalDischargeOrder');
INSERT INTO EXI_REF_GROUP VALUES (NULL, 'XHIBITNoticeOfBreachOfSuspendedSentenceOrder');



COMMIT;

-- UPDATE the ExISS database, using table EXI_REF_TYPE


--Add new event for new Action on Conditional Discharge Order document

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITActionOnConditionalDischargeOrder'),
'ACD','Action On Conditional Discharge Order',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.9');

COMMIT;

--Add new event for new Notice of Breach of Suspended Sentence Order document

INSERT INTO EXI_REF_TYPE (TYPE_ID, OPERATION_ID, GROUP_ID, INTERNAL_CODE, INTERNAL_NAME, STYLESHEET_NAME, SCHEMA_NAME, SCHEMA_LOCATION, SCHEMA_VERSION, SECURITY_CLASSIFICATION, VERSION) VALUES (
NULL, (SELECT OPERATION_ID FROM EXI_REF_OPERATION WHERE INTERNAL_CODE='DOCUMENT'),
(SELECT GROUP_ID FROM EXI_REF_GROUP WHERE EXTERNAL_NAME='XHIBITNoticeOfBreachOfSuspendedSentenceOrder'),
'BSS','Notice Of Breach of Suspended Sentence Order',
NULL,'http://www.courtservice.gov.uk/schemas/courtservice', NULL, NULL, NULL, '5.9');

COMMIT;




--Update schema version info
update exi_ref_type set version = '5.9';

COMMIT;
