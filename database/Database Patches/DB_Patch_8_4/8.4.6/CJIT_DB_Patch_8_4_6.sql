-----------------------------------------------------------------------
-- Includes all changes to be made to the CJIT database 
-----------------------------------------------------------------------

-- Update the table CJI_DOCUMENT_TYPE stylesheet version - defect 6492

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-8.xsl' WHERE INTERNAL_CODE='SR';

-- Update the table CJI_DOCUMENT_TYPE external name - defect 6499

UPDATE CJI_DOCUMENT_TYPE SET EXTERNAL_NAME='BWA' WHERE INTERNAL_CODE='BWA';
UPDATE CJI_DOCUMENT_TYPE SET EXTERNAL_NAME='BWB' WHERE INTERNAL_CODE='BWB';
UPDATE CJI_DOCUMENT_TYPE SET EXTERNAL_NAME='COC' WHERE INTERNAL_CODE='COC';
UPDATE CJI_DOCUMENT_TYPE SET EXTERNAL_NAME='COD' WHERE INTERNAL_CODE='COD';
UPDATE CJI_DOCUMENT_TYPE SET EXTERNAL_NAME='SSO' WHERE INTERNAL_CODE='SSO';

COMMIT;
