-- xhb_case_diary_fixture 
ALTER TABLE xhb_case_diary_fixture ADD COURT_SITE_ID NUMBER(8);

ALTER TABLE xhb_case_diary_fixture ADD (CONSTRAINT xhb_case_diary_fixture_cs_id FOREIGN KEY (COURT_SITE_ID) REFERENCES xhb_court_site (COURT_SITE_ID));

-- aud_case_diary_fixture 
ALTER TABLE aud_case_diary_fixture ADD COURT_SITE_ID NUMBER(8);

@@xhb_casediaryfixture_bur_tr;

commit;