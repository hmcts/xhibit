-----------------------------------------------------------------------
-- Includes all changes to be made to the CJIT database 
-----------------------------------------------------------------------

---------------------------------------------------------------------
--CHANGES TO INCREMENT THE VERSION NUMBER OF THE STYLESHEETS 
---------------------------------------------------------------------

--Young Offender Order 50044C
UPDATE CJI_DOCUMENT_TYPE SET  stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044C-v1-1.xsl'
                              WHERE internal_code='COC';

--Young Offender Order 5044D
UPDATE CJI_DOCUMENT_TYPE SET  stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044D-v1-1.xsl'
                              WHERE internal_code='COD';

--Warrant After Failure Order 5061A
UPDATE CJI_DOCUMENT_TYPE SET  stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/WarrantAfterFailure5061A-v1-2.xsl'
                              WHERE internal_code='BWA';

COMMIT;
