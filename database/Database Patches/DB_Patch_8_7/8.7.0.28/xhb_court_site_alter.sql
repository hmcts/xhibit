/*ctx-1916*/
/*This script needs a default for the site group field unless it it ok to truncate table beforehand?*/
ALTER TABLE xhb_court_site 
 ADD (site_group NUMBER(5)
     ,floater_text VARCHAR2(100)
     ,list_name VARCHAR2(39)
     );

ALTER TABLE aud_court_site 
 ADD (site_group NUMBER(5)
     ,floater_text VARCHAR2(100)
     ,list_name VARCHAR2(39)
     );
     
@@xhb_courtsite_bur_tr;

COMMIT;