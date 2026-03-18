/*Ctx-1911*/
ALTER TABLE xhb_def_hearing_record ADD (trial_in_def_absence VARCHAR2(1),
                                        sentence_in_def_absence VARCHAR2(1)
                                        );


ALTER TABLE aud_def_hearing_record ADD (trial_in_def_absence VARCHAR2(1),
                                        sentence_in_def_absence VARCHAR2(1)
                                        );
@@xhb_def_hearing_record_bur_tr.sql;

commit;