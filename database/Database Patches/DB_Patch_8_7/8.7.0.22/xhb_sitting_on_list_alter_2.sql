--xhb_sitting_on_list
UPDATE xhb_sitting_on_list SET jp1 = substr(jp1,1,35);
UPDATE xhb_sitting_on_list SET jp2 = substr(jp2,1,35);
UPDATE xhb_sitting_on_list SET jp3 = substr(jp3,1,35);
UPDATE xhb_sitting_on_list SET jp4 = substr(jp4,1,35);
UPDATE xhb_sitting_on_list SET list_note_text = substr(list_note_text,1,100);

ALTER TABLE xhb_sitting_on_list MODIFY jp1 VARCHAR2(35);
ALTER TABLE xhb_sitting_on_list MODIFY jp2 VARCHAR2(35);
ALTER TABLE xhb_sitting_on_list MODIFY jp3 VARCHAR2(35);
ALTER TABLE xhb_sitting_on_list MODIFY jp4 VARCHAR2(35);
ALTER TABLE xhb_sitting_on_list MODIFY list_note_text VARCHAR2(100);

UPDATE aud_sitting_on_list SET jp1 = substr(jp1,1,35);
UPDATE aud_sitting_on_list SET jp2 = substr(jp2,1,35);
UPDATE aud_sitting_on_list SET jp3 = substr(jp3,1,35);
UPDATE aud_sitting_on_list SET jp4 = substr(jp4,1,35);
UPDATE aud_sitting_on_list SET list_note_text = substr(list_note_text,1,100);


ALTER TABLE aud_sitting_on_list MODIFY jp1 VARCHAR2(35);
ALTER TABLE aud_sitting_on_list MODIFY jp2 VARCHAR2(35);
ALTER TABLE aud_sitting_on_list MODIFY jp3 VARCHAR2(35);
ALTER TABLE aud_sitting_on_list MODIFY jp4 VARCHAR2(35);
ALTER TABLE aud_sitting_on_list MODIFY list_note_text VARCHAR2(100);

commit;