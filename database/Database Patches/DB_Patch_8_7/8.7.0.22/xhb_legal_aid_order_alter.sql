--ctx-1721
--xhb_legal_aid_order
ALTER TABLE xhb_legal_aid_order MODIFY psd_ro_ref VARCHAR2(20);
ALTER TABLE aud_legal_aid_order MODIFY psd_ro_ref VARCHAR2(20);

commit;