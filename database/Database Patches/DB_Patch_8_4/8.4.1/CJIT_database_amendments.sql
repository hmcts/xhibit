-----------------------------------------------------------------------
-- Includes all changes to be made to the CJIT database 
-----------------------------------------------------------------------

---------------------------------------------------------------------
--CHANGES TO REMOVE THE VERSION NUMBER FROM THE SCHEMA NAME 
---------------------------------------------------------------------

--Custodial Order 5044C
UPDATE CJI_DOCUMENT_TYPE SET  internal_name='Young Offender Order 5044C',
                              external_name='YOUNGOFFENDERORDER5044C',
                              stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044C-v1-0.xsl',
                              schema_name='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderCustodialOrder-v5-3.xsd'
              WHERE internal_code='COC';

--Custodial Order 5044D
UPDATE CJI_DOCUMENT_TYPE SET  internal_name='Young Offender Order 5044D',
                              external_name='YOUNGOFFENDERORDER5044D',
                              stylesheet_name='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044D-v1-0.xsl',
                              schema_name='http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderCustodialOrder-v5-3.xsd'
              WHERE internal_code='COD';

COMMIT;
