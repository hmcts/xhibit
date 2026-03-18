/*ctx-1916*/
ALTER TABLE xhb_court 
 ADD (fl_rep_sort VARCHAR2(1) DEFAULT 'C' NOT NULL
     ,court_start_time VARCHAR2(8)
     ,wl_rep_sort VARCHAR2(1) DEFAULT 'C' NOT NULL
     ,wl_rep_period NUMBER(2)
     ,wl_rep_time VARCHAR2(8)
     ,wl_free_text VARCHAR2(240)
     );

ALTER TABLE aud_court 
 ADD (fl_rep_sort VARCHAR2(1)
     ,court_start_time VARCHAR2(8)
     ,wl_rep_sort VARCHAR2(1)
     ,wl_rep_period NUMBER(2)
     ,wl_rep_time VARCHAR2(8)
     ,wl_free_text VARCHAR2(240)
     );
     
@@xhb_court_bur_tr;

COMMIT;