CREATE OR REPLACE PACKAGE mpkg_load_bal AS PROCEDURE get_es_id (sid OUT mtbl_trigger_server.ID%TYPE, fNewTx OUT BOOLEAN); END mpkg_load_bal;
/