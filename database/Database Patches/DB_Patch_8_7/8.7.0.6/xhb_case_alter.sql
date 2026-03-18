/*    ------------------------------------------------------------------
*     Add column to XHB_CASE table
*/    ------------------------------------------------------------------
/* Remove this setting of a default value at this stage due to performance hist when running in Live as part of a larger set of scripts */
ALTER TABLE XHB_CASE
	ADD CASE_STATUS VARCHAR2(1);


/*	------------------------------------------------------------------
/*	 Add column to AUDIT TABLE FOR XHB_CASE table
*/	------------------------------------------------------------------

/* No need to set this on Audit table, not to mention the manssive performance hit when running it */
ALTER TABLE AUD_CASE
	ADD CASE_STATUS VARCHAR2(1);


/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------



@@XHB_CASE_bur_tr.sql;


/*    ------------------------------------------------------------------
*     Create DB trigger that writes to audit log post insert into XHB_CASE
*/    ------------------------------------------------------------------


@@XHB_CASE_ai_tr.sql;


commit;
/