/*ctx-2124*/

ALTER TABLE xhb_court_site 
 ADD (TIER VARCHAR2(1)  );

ALTER TABLE aud_court_site 
 ADD (TIER VARCHAR2(1)  );
     
@@xhb_courtsite_bur_tr;

COMMIT;