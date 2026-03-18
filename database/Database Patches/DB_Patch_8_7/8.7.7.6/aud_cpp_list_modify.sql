
/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CPP_LIST ';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CPP_LIST', 'AUD_CPP_LIST', 'Y');


commit;
