/*
 *    -------------------------------------------------------------------------------
 *    CTX-1504
 *    -------------------------------------------------------------------------------
 */

UPDATE XHB_ORDER_TYPE 
SET DESCRIPTION = 'Notice of Breach of Suspended Sentence Dealt With'
WHERE ORDER_TYPE_ID = 32;

commit;
