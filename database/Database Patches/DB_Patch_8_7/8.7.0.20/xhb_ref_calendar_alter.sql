ALTER TABLE XHB_REF_CALENDAR DROP
(secure_court, video_link);
 
ALTER TABLE AUD_REF_CALENDAR DROP
(secure_court, video_link);
 
 @@xhb_ref_calendar_bur_tr.sql;

commit;