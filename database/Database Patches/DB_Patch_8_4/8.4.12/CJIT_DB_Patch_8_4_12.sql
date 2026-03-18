--- updates the CJI_DOCUMENT_TYPE table in order to update the stylesheets - defect 6548

--Update schema version number

@CJI_Document_Type.sql

--Update stylesheet version of SSO order
@CJI_Document_TYPE_SSO.sql

Commit;