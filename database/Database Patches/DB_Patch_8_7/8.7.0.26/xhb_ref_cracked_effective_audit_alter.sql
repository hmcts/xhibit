/*Ctx-2060*/ 
/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_CRACKED_EFFECTIVE';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_CRACKED_EFFECTIVE', 'AUD_REF_CRACKED_EFFECTIVE', 'Y');

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REPORT_LOG';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REPORT_LOG', 'AUD_REPORT_LOG', 'Y');

commit;
 
