/*    -------------------------------------------------------------------------------
*     Ctx-1482
*/    -------------------------------------------------------------------------------

/*    ------------------------------------------------------------------
*     Create a temporary field holder
*     Re-create court_id with the number precision and default as per FS
*     Update the new field to what the previous value was
*     Drop the old field from the database
*     Enable the trigger back
*/    ------------------------------------------------------------------
--disable the trigger before running so that the audit table doesnt populated with this change
ALTER TRIGGER XHB_REF_JUDGE_TICKET_BUR_TR DISABLE;

ALTER TABLE xhb_ref_judge_ticket RENAME COLUMN COURT_ID to COURT_ID_OLD;

ALTER TABLE xhb_ref_judge_ticket ADD COURT_ID NUMBER(8);

UPDATE xhb_ref_judge_ticket SET COURT_ID = COURT_ID_OLD;

ALTER TABLE xhb_ref_judge_ticket DROP COLUMN COURT_ID_OLD;
								 

--Start the triggers back up after updates have run
ALTER TRIGGER XHB_REF_JUDGE_TICKET_BUR_TR ENABLE;	

/*--------------------------------------------------------------------------
     Modify AUD_REF_LISTING_DATA columns
----------------------------------------------------------------------------*/

ALTER TABLE aud_ref_judge_ticket RENAME COLUMN COURT_ID to COURT_ID_OLD;

ALTER TABLE aud_ref_judge_ticket ADD COURT_ID NUMBER(8);

UPDATE aud_ref_judge_ticket SET COURT_ID = COURT_ID_OLD;

ALTER TABLE aud_ref_judge_ticket DROP COLUMN COURT_ID_OLD;

commit;
