/*    -------------------------------------------------------------------------------
*     CTX-1339
*/    -------------------------------------------------------------------------------

/*    ------------------------------------------------------------------
*     Disable the trigger so that none of the updates fire and populate the audit table
*     Create a temporary field holder
*     Re-create version with the number precision and default as per FS
*     Update the new field to what the previous value was
*     Drop the old field from the database
*     Enable the trigger back
*/    ------------------------------------------------------------------
--check that there are no disabled triggers before running this or they will be enabled later
ALTER TABLE XHB_DIARY_NOTE_ENTRY DISABLE ALL TRIGGERS;

ALTER TABLE XHB_DIARY_NOTE_ENTRY RENAME COLUMN VERSION to VERSION_OLD;

ALTER TABLE XHB_DIARY_NOTE_ENTRY ADD VERSION NUMBER(5) DEFAULT 1 NOT NULL;

UPDATE XHB_DIARY_NOTE_ENTRY SET VERSION = VERSION_OLD;

ALTER TABLE XHB_DIARY_NOTE_ENTRY DROP COLUMN VERSION_OLD;

ALTER TABLE XHB_DIARY_NOTE_ENTRY MODIFY (NOTE_TYPE_ID NOT NULL,
                                         COURT_ID NUMBER(8,0) NULL
										 );
										 

--Start the triggers back up after updates have run
ALTER TABLE XHB_DIARY_NOTE_ENTRY ENABLE ALL TRIGGERS;	
/*--------------------------------------------------------------------------
     Modify AUD_DIARY_NOTE_ENTRY columns
----------------------------------------------------------------------------*/

ALTER TABLE AUD_DIARY_NOTE_ENTRY RENAME COLUMN VERSION to VERSION_OLD;

ALTER TABLE AUD_DIARY_NOTE_ENTRY ADD VERSION NUMBER(5) DEFAULT 1 NOT NULL;

UPDATE AUD_DIARY_NOTE_ENTRY SET VERSION = VERSION_OLD;

ALTER TABLE AUD_DIARY_NOTE_ENTRY DROP COLUMN VERSION_OLD;

ALTER TABLE AUD_DIARY_NOTE_ENTRY MODIFY (NOTE_TYPE_ID NOT NULL,
                                         COURT_ID NUMBER(8,0) NULL
										 );

commit;
