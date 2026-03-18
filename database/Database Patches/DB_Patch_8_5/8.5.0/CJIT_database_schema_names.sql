--- Update Trial Record Sheet XSL

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME='http://www.courtservice.gov.uk/transforms/courtservice/TrialRecordSheet-v2-8.xsl'
WHERE INTERNAL_CODE='TR';

--- Insert new row for Defendant Details document

Insert into CJI_DOCUMENT_TYPE 
(
 DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME,
 EXTERNAL_NAME, STYLESHEET_NAME,
 SECURITY_CLASSIFICATION,
 BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG,
 REGISTERED_IN_CJSE,
 REGISTER_AGAINST_LOCATION,
 REGISTER_AGAINST_CRN,
 REGISTER_AGAINST_DEFENDANT,
 SCHEMA_NAME
)
VALUES((select max(document_type_id) + 1 from cji_document_type),'DD','Defendant Details','DD',
'http://www.courtservice.gov.uk/transforms/courtservice/DefendantDetails-v1-0.xsl',
'NPM','BITS.DefendantDetailsHandler','BITS.XMLTransformHandler','Y','N','N','Y',
'http://www.courtservice.gov.uk/schemas/courtservice DefendantDetails-v5-4.xsd');

COMMIT;
