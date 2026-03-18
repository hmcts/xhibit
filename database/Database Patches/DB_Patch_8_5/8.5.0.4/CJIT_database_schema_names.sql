--- Update Defendant Details XSL

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/DefendantDetails-v1-2.xsl'
WHERE INTERNAL_CODE='DD';


COMMIT;
