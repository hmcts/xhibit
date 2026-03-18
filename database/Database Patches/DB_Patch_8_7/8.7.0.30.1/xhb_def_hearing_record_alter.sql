/*ctx-2358*/
ALTER TABLE xhb_def_hearing_record 
 ADD (ADJOURNED_FOR_POCA_HRG VARCHAR2(1) );

ALTER TABLE aud_def_hearing_record
 ADD (ADJOURNED_FOR_POCA_HRG VARCHAR2(1) );
     
@@xhb_def_hearing_record_bur_tr.sql;

COMMIT;