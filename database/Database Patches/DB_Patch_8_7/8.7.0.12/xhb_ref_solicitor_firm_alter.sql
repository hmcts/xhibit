ALTER TABLE XHB_REF_SOLICITOR_FIRM ADD
(la_code VARCHAR2(6),
 london_weighting VARCHAR2(1)
 );
 
ALTER TABLE AUD_REF_SOLICITOR_FIRM ADD
(la_code VARCHAR2(6),
 london_weighting VARCHAR2(1) );
 
 @@xhb_ref_solicitor_firm_bur_tr.sql;
/
commit;