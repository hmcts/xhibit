/*    -------------------------------------------------------------------------------
*     CTX-1425
*/    -------------------------------------------------------------------------------

/*    -------------------------------------------------------------------------
*     Amend JP1,2,3,4 columns to 35 from 255
*/    --------------------------------------------------------------------------

ALTER TABLE XHB_CASE MODIFY ORIGINAL_JPS_1 VARCHAR2(35 BYTE);
ALTER TABLE XHB_CASE MODIFY ORIGINAL_JPS_2 VARCHAR2(35 BYTE);
ALTER TABLE XHB_CASE MODIFY ORIGINAL_JPS_3 VARCHAR2(35 BYTE);
ALTER TABLE XHB_CASE MODIFY ORIGINAL_JPS_4 VARCHAR2(35 BYTE);

ALTER TABLE AUD_CASE MODIFY ORIGINAL_JPS_1 VARCHAR2(35 BYTE);
ALTER TABLE AUD_CASE MODIFY ORIGINAL_JPS_2 VARCHAR2(35 BYTE);
ALTER TABLE AUD_CASE MODIFY ORIGINAL_JPS_3 VARCHAR2(35 BYTE);
ALTER TABLE AUD_CASE MODIFY ORIGINAL_JPS_4 VARCHAR2(35 BYTE);

COMMIT;