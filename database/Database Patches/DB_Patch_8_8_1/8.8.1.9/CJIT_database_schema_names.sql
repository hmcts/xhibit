------------------------
--- Prior to CTX in 2019 an update to the CJI_DOCUMENT_TYPE table was only needed for the CJIT database schema.
--- However, since then any changes must also be applied to the XHIBIT.CJI_DOCUMENT_TYPE table
--- WE MUST ensure that he the ACD and BSS documents are not sent to the CJSE Portal
------------------------


---Update Schema version
UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='DO NOT SEND' WHERE INTERNAL_CODE='ACD';

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME='DO NOT SEND' WHERE INTERNAL_CODE='BSS';


COMMIT;
