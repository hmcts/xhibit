---------------------------------------------------------------------
--CHANGES TO REMOVE THE VERSION NUMBER FROM THE SCHEMA NAME 
---------------------------------------------------------------------


-- UPDATE the CJIT database, using table CJI_DOCUMENT_TYPE

--ImprisonmentOrder
UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v2-2.xsl'
WHERE INTERNAL_CODE='IO'; 

--CommittalRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-6.xsl'
WHERE INTERNAL_CODE='SR'; 

--AppealRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/AppealRecordSheet-v2-5.xsl'
WHERE INTERNAL_CODE='AR'; 

--TrialRecordSheet
UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2-4.xsl'
WHERE INTERNAL_CODE='TR'; 

COMMIT;
