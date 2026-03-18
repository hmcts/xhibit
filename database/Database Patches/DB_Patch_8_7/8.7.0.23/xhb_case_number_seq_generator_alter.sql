--ctx-1807

ALTER TABLE xhb_case_number_seq_generator MODIFY VERSION NOT NULL;
ALTER TABLE aud_case_number_seq_generator MODIFY VERSION NOT NULL;

COMMIT;