-- xhb_case_diary_fixture 
UPDATE xhb_case_diary_fixture SET list_note_text = substr(list_note_text,1,100);
ALTER TABLE xhb_case_diary_fixture MODIFY list_note_text VARCHAR2(100);

-- aud_case_diary_fixture 
UPDATE aud_case_diary_fixture SET list_note_text = substr(list_note_text,1,100);
ALTER TABLE aud_case_diary_fixture MODIFY list_note_text VARCHAR2(100);

commit;