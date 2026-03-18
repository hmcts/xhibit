-----------------------------------------------------------------------
-- Includes all changes to be made to the CJIT database 
-----------------------------------------------------------------------

---------------------------------------------------------------------
--CHANGES TO INCREMENT THE VERSION NUMBER OF THE STYLESHEETS 
---------------------------------------------------------------------

--Imprisonment Order 5035C
UPDATE CJI_DOCUMENT_TYPE SET stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v2-4.xsl'
                              WHERE internal_code='IO';

--Suspended Sentence Order 
UPDATE CJI_DOCUMENT_TYPE SET stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/SuspendedSentenceOrder-v1-1.xsl
                              WHERE internal_code='SSO';

COMMIT;
