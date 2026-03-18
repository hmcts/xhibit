/*    -------------------------------------------------------------------------------
*     CTX-1447
*/    -------------------------------------------------------------------------------

UPDATE XHB_DEFENDANT 
SET CURRENT_PRISON_STATUS='N' 
WHERE CURRENT_PRISON_STATUS IS NULL;

ALTER TABLE XHB_DEFENDANT 
MODIFY CURRENT_PRISON_STATUS VARCHAR2(1) DEFAULT 'N' NOT NULL;


COMMIT;