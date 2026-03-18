--ctx-1807
--These commands will not run if there is data in the table with null values in the fields changing to not null
TRUNCATE TABLE xhb_legal_aid_amendment;

ALTER TABLE xhb_legal_aid_amendment MODIFY amendment_type NOT NULL;
ALTER TABLE xhb_legal_aid_amendment MODIFY amendment_date NOT NULL;
ALTER TABLE xhb_legal_aid_amendment MODIFY version NOT NULL;

TRUNCATE TABLE aud_legal_aid_amendment;
ALTER TABLE aud_legal_aid_amendment MODIFY amendment_type NOT NULL;
ALTER TABLE aud_legal_aid_amendment MODIFY amendment_date NOT NULL;
ALTER TABLE aud_legal_aid_amendment MODIFY version NOT NULL;

COMMIT;