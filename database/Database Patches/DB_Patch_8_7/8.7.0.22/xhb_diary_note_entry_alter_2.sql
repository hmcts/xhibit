-- xhb_diary_note_entry
UPDATE xhb_diary_note_entry SET diary_note_text = substr(diary_note_text,1,100);
ALTER TABLE xhb_diary_note_entry MODIFY diary_note_text VARCHAR2(100);

-- aud_diary_note_entry
UPDATE aud_diary_note_entry SET diary_note_text = substr(diary_note_text,1,100);
ALTER TABLE aud_diary_note_entry MODIFY diary_note_text VARCHAR2(100);

commit;