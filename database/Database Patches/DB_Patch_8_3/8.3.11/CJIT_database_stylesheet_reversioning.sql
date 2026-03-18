---------------------------------------------------------------------
--CHANGES TO REMOVE THE VERSION NUMBER FROM THE SCHEMA NAME 
---------------------------------------------------------------------


-- UPDATE the CJIT database, using table CJI_DOCUMENT_TYPE

--CommittalRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-7.xsl'
WHERE INTERNAL_CODE='SR'; 

--TrialRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2-5.xsl'
WHERE INTERNAL_CODE='TR'; 

COMMIT;

---------------------------------------------------------------------
--CHANGES TO UPDATE VERSION NUMBER IN THE SCHEMA NAME 
---------------------------------------------------------------------

--TrialRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v5-2-1.xsd'
WHERE INTERNAL_CODE='TR'; 

COMMIT;