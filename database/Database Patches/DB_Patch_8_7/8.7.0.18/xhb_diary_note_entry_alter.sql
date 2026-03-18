/*    -------------------------------------------------------------------------------
*     CTX-1340
*     Drop columns start and end dates from XHB_DIARY_NOTE_ENTRY and audit table
*     Alter the associated triggers so that they do not reference those fields
*/    -------------------------------------------------------------------------------

/*    ------------------------------------------------------------------
*     Remove columns
*/    ------------------------------------------------------------------

ALTER TABLE xhb_diary_note_entry DROP (diary_start_date, diary_end_date);
ALTER TABLE aud_diary_note_entry DROP (diary_start_date, diary_end_date);


/*	------------------------------------------------------------------
/*	Add diary_date field to XHB_DIARY_NOTE_ENTRY and audit table
*/	------------------------------------------------------------------

ALTER TABLE xhb_diary_note_entry ADD (DIARY_DATE DATE);
ALTER TABLE aud_diary_note_entry ADD (DIARY_DATE DATE);

/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_diary_note_entry_bur_tr.sql;

commit;
