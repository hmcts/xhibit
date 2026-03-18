/*ctx-1916*/
ALTER TABLE xhb_court_room 
 ADD (security_ind VARCHAR2(1)
     ,video_ind VARCHAR2(1)
     );

ALTER TABLE aud_court_room 
 ADD (security_ind VARCHAR2(1)
     ,video_ind VARCHAR2(1)
     );


@@xhb_courtroom_bur_tr;

COMMIT;