------------------------
--- Prior to CTX in 2019 an update to the CJI_DOCUMENT_TYPE table was only needed for the CJIT database schema.
--- However, since then any changes must also be applied to the XHIBIT.CJI_DOCUMENT_TYPE table
------------------------

--- Update Stylesheets

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder5044D-v1-8.xsl'
WHERE INTERNAL_CODE='COD';

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v3-1.xsl'
WHERE INTERNAL_CODE='IMPRISONMENTORDER';


COMMIT;
