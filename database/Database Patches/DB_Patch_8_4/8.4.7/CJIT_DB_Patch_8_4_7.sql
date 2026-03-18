-----------------------------------------------------------------------
-- Includes all changes to be made to the CJIT database 
-----------------------------------------------------------------------

-- Update the table CJI_DOCUMENT_TYPE stylesheet version - defect 6512

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/WarrantAfterFailure5061A-v1-0.xsl' WHERE INTERNAL_CODE='BWA';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/WarrantAfterFailure5061B-v1-0.xsl' WHERE INTERNAL_CODE='BWB';


COMMIT;
