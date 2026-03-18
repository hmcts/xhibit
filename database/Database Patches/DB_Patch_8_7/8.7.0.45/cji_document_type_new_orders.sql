REM INSERTING into CJI_DOCUMENT_TYPE
SET DEFINE OFF;

Insert into CJI_DOCUMENT_TYPE (DOCUMENT_TYPE_ID,INTERNAL_CODE,INTERNAL_NAME,EXTERNAL_NAME,STYLESHEET_NAME,SECURITY_CLASSIFICATION,BITS_DOCUMENT_CONFIG,BITS_TRANSFORM_CONFIG,REGISTERED_IN_CJSE,REGISTER_AGAINST_LOCATION,REGISTER_AGAINST_CRN,REGISTER_AGAINST_DEFENDANT,SCHEMA_NAME,MAJOR_SCHEMA_VERSION,MINOR_SCHEMA_VERSION) values ((select max(document_type_id)+1 from cji_document_type),'ACD','ActionOnConditionalDischargeOrder','ACD','http://www.courtservice.gov.uk/transforms/courtservice/ActionOnConditionalDischargeOrder-v1-0.xsl','NPM','BITS.ActionConditionalDischargeHandler','BITS.XMLTransformHandler','Y','N','N','Y','http://www.courtservice.gov.uk/schemas/courtservice ActionOnConditionalDischargeOrder-v5-9.xsd',null,null);
commit;

Insert into CJI_DOCUMENT_TYPE (DOCUMENT_TYPE_ID,INTERNAL_CODE,INTERNAL_NAME,EXTERNAL_NAME,STYLESHEET_NAME,SECURITY_CLASSIFICATION,BITS_DOCUMENT_CONFIG,BITS_TRANSFORM_CONFIG,REGISTERED_IN_CJSE,REGISTER_AGAINST_LOCATION,REGISTER_AGAINST_CRN,REGISTER_AGAINST_DEFENDANT,SCHEMA_NAME,MAJOR_SCHEMA_VERSION,MINOR_SCHEMA_VERSION) values ((select max(document_type_id)+1 from cji_document_type),'BSS','NoticeOfBreachOfSuspendedSentenceOrder','BSS','http://www.courtservice.gov.uk/transforms/courtservice/NoticeOfBreachOfSuspendedSentenceOrder-v1-0.xsl','NPM','BITS.NoticeBreachSuspendedSentenceHandler','BITS.XMLTransformHandler','Y','N','N','Y','http://www.courtservice.gov.uk/schemas/courtservice NoticeBreachSuspendedSentenceOrder-v5-9.xsd',null,null);

commit;