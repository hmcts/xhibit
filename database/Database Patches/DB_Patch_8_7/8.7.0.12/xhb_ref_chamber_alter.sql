ALTER TABLE XHB_REF_CHAMBER ADD
(clerk_name VARCHAR2(35)
 );
 
ALTER TABLE AUD_REF_CHAMBER ADD
(clerk_name VARCHAR2(35)
 );
 
 @@xhb_ref_chamber_bur_tr.sql;
/
commit;