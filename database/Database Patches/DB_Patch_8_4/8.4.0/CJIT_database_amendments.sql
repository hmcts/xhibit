-----------------------------------------------------------------------
-- Includes all changes to be made to the CJIT database 
-----------------------------------------------------------------------


-- Update the table CJI_VERSION

UPDATE EXI_VERSION SET SCHEMA_VERSION='8_4', DISPLAY_NAME='CJSE Database Schema 8_4' WHERE SCHEMA_NAME='CJSE';

-- Update the table CJI_DOCUMENT_TYPE

@CJIT_database_schema_names.sql;

-- Update the table CJI_DOCUMENT_SECURITY

@CJIT_database_document_security.sql;

COMMIT;
