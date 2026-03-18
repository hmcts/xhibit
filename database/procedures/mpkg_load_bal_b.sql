CREATE OR REPLACE PACKAGE BODY mpkg_load_bal AS lb_limit CONSTANT PLS_INTEGER := 256;
TYPE   VA_ES_LIST IS VARRAY(256) OF mtbl_trigger_server.ID%TYPE;
tx_id VARCHAR2(256);
es_id mtbl_trigger_server.ID%TYPE; 
PROCEDURE get_es_id (sid OUT mtbl_trigger_server.ID%TYPE, fNewTx OUT BOOLEAN) IS 
nID    mtbl_trigger_server.ID%TYPE;
nSeq   PLS_INTEGER; 
nMod   PLS_INTEGER; cur_tx_id tx_id%TYPE; 
CURSOR c_avail_es IS  SELECT ID  FROM   mtbl_trigger_server WHERE  (NVL(alive_ts, SYSTIMESTAMP) + alive_interval >= SYSTIMESTAMP)  AND ((startupdate > shutdowndate) OR (shutdowndate IS NULL) )ORDER BY ID;
CURSOR c_reg_es IS  SELECT ID  FROM   mtbl_trigger_server WHERE  startupdate > shutdowndate OR shutdowndate IS NULL ORDER BY ID; es_list VA_ES_LIST;
BEGIN  cur_tx_id := dbms_transaction.local_transaction_id();
IF (tx_id != cur_tx_id) 
OR (tx_id IS NULL AND cur_tx_id IS NOT NULL) 
OR  (tx_id IS NOT NULL AND cur_tx_id IS NULL) 
THEN tx_id := cur_tx_id;
SELECT mseq_load_bal.NEXTVAL INTO nSeq FROM DUAL; OPEN c_avail_es;
FETCH c_avail_es BULK COLLECT INTO es_list LIMIT lb_limit; CLOSE c_avail_es;
IF es_list.COUNT = 0 
THEN OPEN c_reg_es;
FETCH c_reg_es BULK COLLECT INTO es_list LIMIT lb_limit;
CLOSE c_reg_es;
END IF;
IF es_list.COUNT = 0 THEN sid := 0;
ELSE nMod := (nSeq MOD es_list.COUNT) + 1;
sid := es_list(nMod); 
END IF; es_id  := sid;
fNewTx := TRUE;
ELSE sid := es_id;
fNewTx := FALSE;
END IF;
END; 
BEGIN tx_id := NULL;
es_id := 0;
END mpkg_load_bal;
/