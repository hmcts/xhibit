/* Remove this setting of a default value at this stage due to performance hist when running in Live as part of a larger set of scripts */
ALTER TABLE XHB_REF_CALENDAR ADD
(secure_court VARCHAR2(1),
 video_link  VARCHAR2(1)
 );
 
/* No need to set this on Audit table, not to mention the manssive performance hit when running it */
ALTER TABLE AUD_REF_CALENDAR ADD
(secure_court VARCHAR2(1),
 video_link  VARCHAR2(1)
 );
 
 @@xhb_ref_calendar_bur_tr.sql;
/
commit;
