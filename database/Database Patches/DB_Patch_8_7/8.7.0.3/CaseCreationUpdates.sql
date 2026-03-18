/*    ------------------------------------------------------------------
*     UPDATE XHB_CASE TABLE AND AUDIT TABLE - CTX-387
*/    ------------------------------------------------------------------
ALTER TABLE XHB_CASE ADD (ORIG_BODY_DECISION_DATE	DATE);


/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR CASE - CTX-387
*/	------------------------------------------------------------------
ALTER TABLE AUD_CASE ADD (ORIG_BODY_DECISION_DATE	DATE);


/*    ------------------------------------------------------------------
*     ALTER LENGTH OF CASE_DESCRIPTION column to 300 chars CTX-419
*/    ------------------------------------------------------------------
/* Remove this modify as its changed again in a later script and this avoids a performance hit when running in Live */
/* ALTER TABLE XHB_CASE MODIFY CASE_DESCRIPTION VARCHAR2(300); */


/*    ------------------------------------------------------------------
*     ALTER LENGTH OF CASE_DESCRIPTION column to 300 chars CTX-419
*/    ------------------------------------------------------------------
/* Remove this modify as its changed again in a later script and this avoids a performance hit when running in Live */
/* ALTER TABLE AUD_CASE MODIFY CASE_DESCRIPTION VARCHAR2(300); */

/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


@@XHB_CASE_bur_tr.sql;


/*    ------------------------------------------------------------------
*     Create DB trigger that writes to audit log post insert into XHB_CASE (CTX-423)
*/    ------------------------------------------------------------------


@@XHB_CASE_ai_tr.sql;


commit;
/