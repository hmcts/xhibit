/*    -------------------------------------------------------------------------------
*     Ctx-1594
*/    -------------------------------------------------------------------------------

/*    ------------------------------------------------------------------
*     Alter tables xhb_ref_judge_ticket and aud_ref_judge_ticket to make 
*     court_id column not null.
*/    ------------------------------------------------------------------

ALTER TABLE xhb_ref_judge_ticket MODIFY (court_id NOT NULL);
								 
ALTER TABLE aud_ref_judge_ticket MODIFY (court_id NOT NULL);

commit;
