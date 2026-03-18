
-- Add a new court log entry category for 1331
@xhb_court_logs_1331.sql

-- To have a hearing type at defendant level
alter table XHB_DEF_HEARING_RECORD add MP_HEARING_TYPE varchar2(1);

commit;
