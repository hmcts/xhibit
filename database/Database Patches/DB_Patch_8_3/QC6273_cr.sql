
-- Script for Quality Centre defect 6273
-- to improve the performance of 'unauthorised cases' query on XHIBIT.
-- M. Hewitt


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_QC6273_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


-- drop non-functional indexes
drop index XHB_OFFENCE_OBS_IND;
drop index XHB_DEFENDANT_ON_OFF_OBS_IND;
drop index XHB_PLEA_OBS;
drop index XHB_VERDICT_OBSIND;


-- set 'authorised' for all cases older than 1st April 2009
update xhb_defendant_on_case 
set results_verified = 'E', -- exported
    date_exported = to_date('01-04-2009','dd-mm-yyyy')
where defendant_on_case_id in (
 select doc.defendant_on_case_id 
 from xhb_defendant_on_case doc, xhb_case t
 where 
  t.last_update_date < to_date('01-04-2009','dd-mm-yyyy')
  and doc.last_update_date < to_date('01-04-2009','dd-mm-yyyy')
  and NVL(doc.results_verified,'N') in ('N','F')
  and t.case_id = doc.case_id);

commit;


-- new index on XHB_OFFENCE
create unique index XHB_OFFENCE_UNAUTH on XHB_OFFENCE (
OFFENCE_ID, CHARGE_ID, OBS_IND)
  tablespace XHIBITX;
declare 
begin
  dbms_stats.gather_index_stats(ownname => 'XHIBIT', 
    indname => 'XHB_OFFENCE_UNAUTH', estimate_percent => 10);
end;
/
show errors


-- new index on XHB_DEFENDANT_ON_CASE
create unique index XHB_DEFENDANT_CASE_UNAUTH on XHB_DEFENDANT_ON_CASE (
CASE_ID, OBS_IND, RESULTS_VERIFIED, DEFENDANT_ON_CASE_ID)
  tablespace XHIBITX;
declare 
begin
  dbms_stats.gather_index_stats(ownname => 'XHIBIT', 
    indname => 'XHB_DEFENDANT_CASE_UNAUTH', estimate_percent => 10);
end;
/
show errors


-- new index on XHB_DEFENDANT_OFF_UNAUTH
create unique index XHB_DEFENDANT_OFF_UNAUTH on XHB_DEFENDANT_ON_OFFENCE (
DEFENDANT_ON_CASE_ID, OBS_IND, DEFENDANT_ON_OFFENCE_ID, OFFENCE_ID)
  tablespace XHIBITX;
declare 
begin
  dbms_stats.gather_index_stats(ownname => 'XHIBIT', 
    indname => 'XHB_DEFENDANT_OFF_UNAUTH', estimate_percent => 10);
end;
/
show errors


-- new index on XHB_CASE
create unique index XHB_CASE_UNAUTH on XHB_CASE (
COURT_ID, LAST_UPDATE_DATE, CASE_ID)
  tablespace XHIBITX;
declare 
begin
  dbms_stats.gather_index_stats(ownname => 'XHIBIT', 
    indname => 'XHB_CASE_UNAUTH', estimate_percent => 10);
end;
/
show errors


CREATE OR REPLACE PACKAGE BODY xhb_unauthorised_cases_pkg AS
       PROCEDURE get_unauthorised_cases(p_results_out OUT SYS_REFCURSOR,
                             p_court_id      IN XHB_CASE.COURT_ID%TYPE) IS
       BEGIN
          /*The MINUS is used because we need every case with at least 1 deft that has not been authorised and has a
          disposal against all offences*/

          OPEN p_results_out FOR
             SELECT c.case_id,c.case_number,c.case_type,c.court_id
             FROM xhb_defendant_on_case t,xhb_case c
             WHERE c.case_id=t.case_id
             AND NVL(t.obs_ind,'N') <> 'Y'
             AND t.defendant_on_case_id IN(
                /*This gets all the defendants which haven't been authorised, but do have offences*/
                SELECT doc.defendant_on_case_id
                FROM xhb_case t ,
                xhb_defendant_on_case doc,
                xhb_defendant_on_offence dof,
                xhb_offence o,
                xhb_charge charge
                WHERE t.case_id=doc.case_id
                AND doc.defendant_on_case_id=dof.defendant_on_case_id
                AND o.offence_id=dof.offence_id
                AND charge.charge_id=o.charge_id
                AND charge.case_id=t.case_id
                AND charge.charge_type IN ('I','B','O','S','C')
                AND NVL(charge.obs_ind,'N') <> 'Y'
                AND NVL(dof.obs_ind,'N') <> 'Y'
                AND NVL(o.obs_ind,'N') <> 'Y'
                AND NVL(doc.results_verified,'N') <> 'E'
                AND t.court_id=p_court_id
                AND trunc(t.last_update_date) > sysdate-30
                GROUP BY doc.defendant_on_case_id

                MINUS
                /*This gets a list of defendant on cases which do not have a disposal against
                every offence*/
                SELECT defendant_on_case_id FROM(
                   SELECT 
                      doc.defendant_on_case_id,dof.defendant_on_offence_id,max(dis.disposal2_id)
                   FROM xhb_case cas,
                   xhb_defendant_on_case doc,
                   xhb_offence o,
                   xhb_charge c,
                   xhb_defendant_on_offence dof
                   LEFT OUTER JOIN xhb_disposal2 dis on(dis.defendant_on_offence_id=dof.defendant_on_offence_id AND nvl(dis.obs_ind,'N')<>'Y')
                   WHERE cas.case_id=doc.case_id
                   AND o.offence_id=dof.offence_id
                   AND o.charge_id = c.charge_id
                   AND c.case_id=cas.case_id
                   AND doc.defendant_on_case_id=dof.defendant_on_case_id
                   AND nvl(o.obs_ind,'N')<>'Y'
                   AND nvl(c.obs_ind,'N')<>'Y'
                   AND nvl(dof.obs_ind,'N')<>'Y'
                   AND c.charge_type in ('I','B','O','S','C')
                   AND cas.court_id=p_court_id
                   AND trunc(cas.last_update_date) > sysdate-30
                   GROUP BY 
                      doc.defendant_on_case_id,dof.defendant_on_offence_id
                   HAVING max(dis.disposal2_id) is null
                )
                GROUP BY defendant_on_case_id
             )
             GROUP BY c.case_id,c.case_number,c.case_type,c.court_id;
       END get_unauthorised_cases;

END xhb_unauthorised_cases_pkg;
/
show errors

spool off
