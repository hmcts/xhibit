/*
 *    -------------------------------------------------------------------------------
 *    CTX-1612
 *    -------------------------------------------------------------------------------
 */

update XHB_CASE SET CASE_DESCRIPTION = substr(CASE_DESCRIPTION, 1, 70);
update AUD_CASE SET CASE_DESCRIPTION = substr(CASE_DESCRIPTION, 1, 70);

alter table XHB_CASE modify (CASE_DESCRIPTION VARCHAR2(70));
alter table AUD_CASE modify (CASE_DESCRIPTION VARCHAR2(70));

commit;
