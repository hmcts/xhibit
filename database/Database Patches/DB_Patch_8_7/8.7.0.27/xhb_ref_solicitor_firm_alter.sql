/*ctx-1992*/
ALTER TABLE xhb_ref_solicitor_firm DROP COLUMN LONDON_WEIGHTING;

ALTER TABLE aud_ref_solicitor_firm DROP COLUMN LONDON_WEIGHTING;

@@xhb_refsolicitorfirm_bur_tr.sql;


COMMIT;
/

