--xhb_fixture_deft_attending
--FS has precision scales against fixture_deft_attending_id and version fields which are not in place

ALTER TABLE xhb_fixture_deft_attending RENAME COLUMN fixture_deft_attending_id to fixture_deft_attending_id_old;

ALTER TABLE xhb_fixture_deft_attending RENAME COLUMN version to version_old;

ALTER TABLE xhb_fixture_deft_attending ADD fixture_deft_attending_id NUMBER(8) NOT NULL;

ALTER TABLE xhb_fixture_deft_attending ADD version NUMBER(5) DEFAULT 1 NOT NULL;

UPDATE xhb_fixture_deft_attending SET fixture_deft_attending_id = fixture_deft_attending_id_old;

UPDATE xhb_fixture_deft_attending SET version = version_old;

ALTER TABLE xhb_fixture_deft_attending DROP COLUMN fixture_deft_attending_id_old;

ALTER TABLE xhb_fixture_deft_attending DROP COLUMN version_old;

ALTER TABLE xhb_fixture_deft_attending ADD CONSTRAINT XHB_FIXTURE_DEFT_ATTENDING_PK PRIMARY KEY (fixture_deft_attending_id);


--aud table
ALTER TABLE aud_fixture_deft_attending RENAME COLUMN fixture_deft_attending_id to fixture_deft_attending_id_old;

ALTER TABLE aud_fixture_deft_attending RENAME COLUMN version to version_old;

ALTER TABLE aud_fixture_deft_attending ADD fixture_deft_attending_id NUMBER(8) NOT NULL;

ALTER TABLE aud_fixture_deft_attending ADD version NUMBER(5) DEFAULT 1 NOT NULL;

UPDATE aud_fixture_deft_attending SET fixture_deft_attending_id = fixture_deft_attending_id_old;

UPDATE aud_fixture_deft_attending SET version = version_old;

ALTER TABLE aud_fixture_deft_attending DROP COLUMN fixture_deft_attending_id_old;

ALTER TABLE aud_fixture_deft_attending DROP COLUMN version_old;

commit;