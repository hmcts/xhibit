/*ctx-2406*/
ALTER TABLE xhb_def_hearing_record 
 ADD (FORMA_STATUS VARCHAR2(1) );

ALTER TABLE aud_def_hearing_record
 ADD (FORMA_STATUS VARCHAR2(1) );
     
@@xhb_def_hearing_record_bur_tr.sql;

COMMIT;