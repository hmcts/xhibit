/*
 *    -------------------------------------------------------------------------------
 *    CTX-1402
 *    -------------------------------------------------------------------------------
 */

update XHB_DEFENDANT SET PARENT_GUARDIAN_NAME = substr(PARENT_GUARDIAN_NAME, 1, 35);
update AUD_DEFENDANT SET PARENT_GUARDIAN_NAME = substr(PARENT_GUARDIAN_NAME, 1, 35);

alter table XHB_DEFENDANT modify (PARENT_GUARDIAN_NAME VARCHAR2(35));
alter table AUD_DEFENDANT modify (PARENT_GUARDIAN_NAME VARCHAR2(35));

commit;
