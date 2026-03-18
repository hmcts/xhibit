/*ctx-2124*/

ALTER TABLE xhb_court_satellite
 ADD (OBS_IND VARCHAR2(1) DEFAULT 'N' );

ALTER TABLE aud_court_satellite 
 ADD (OBS_IND VARCHAR2(1) );
     
@@xhb_court_satellite_bur_tr;

COMMIT;