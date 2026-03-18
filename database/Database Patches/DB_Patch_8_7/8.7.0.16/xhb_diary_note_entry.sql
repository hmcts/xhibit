/*    ------------------------------------------------------------------
*     XHB_DIARY_NOTE_ENTRY
*     AUD_DIARY_NOTE_ENTRY
*     REPLACE TRIGGERS
*/    ------------------------------------------------------------------

ALTER TABLE XHB_DIARY_NOTE_ENTRY ADD (OBS_IND VARCHAR2(1) DEFAULT NULL);


/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR DIARY_NOTE_ENTRY
*/	------------------------------------------------------------------

ALTER TABLE AUD_DIARY_NOTE_ENTRY ADD (OBS_IND VARCHAR2(1) DEFAULT NULL);

/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_diary_note_entry_bur_tr.sql;

commit;

/