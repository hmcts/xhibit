-----------------------------------------------------------------------
-- Includes all changes to be made to the CJIT database 
-----------------------------------------------------------------------

---------------------------------------------------------------------
--CHANGES TO REMOVE THE VERSION NUMBER FROM THE SCHEMA NAME 
---------------------------------------------------------------------

--Warrant After Failure Order 5061A
UPDATE CJI_DOCUMENT_TYPE SET  stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/WarrantAfterFailure5061A-v1-1.xsl'
                              WHERE internal_code='BWA';

--Warrant After Failure 5061B
UPDATE CJI_DOCUMENT_TYPE SET  stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/WarrantAfterFailure5061B-v1-1.xsl'
                              WHERE internal_code='BWB';

COMMIT;

