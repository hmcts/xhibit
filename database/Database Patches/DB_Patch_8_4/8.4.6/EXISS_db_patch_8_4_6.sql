-----------------------------------------------------------------------
-- Includes all changes to be made to the ExISS database 
-----------------------------------------------------------------------

-- Update the table EXI_VERSION

UPDATE EXI_VERSION SET SCHEMA_VERSION='8_4_6', DISPLAY_NAME='EXISS Database Schema 8_4_6' WHERE SCHEMA_NAME='EXISS';

-- Update the table EXI_REF_TYPE

@EXI_1332_event_ref_data.sql

COMMIT;
