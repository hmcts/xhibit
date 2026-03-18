SET SERVEROUTPUT ON 
ACCEPT in_xhb_court_id PROMPT "Enter XHIBIT Court ID : "
set termout off
set echo off
set serveroutput off
set headsep off
set verify off

column filename new_value spool_filename
  select 'Fix_Migrated_DEF_HEARING_REC_CourtID_'||'&&in_xhb_court_id'||'_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual;

set term on
spool &&spool_filename

column court_name new_val court_name

SELECT court_name as court_name from xhibit.xhb_court where court_id = nvl(&&in_xhb_court_id,NULL);

SELECT ' ' from dual;

EXEC IF '&&court_name' is NULL THEN DBMS_OUTPUT.PUT_LINE('Supplied XHIBIT COURT ID '||&&in_xhb_court_id ||' is NOT Valid, please retry with a valid XHIBIT_COURT_ID...'); DBMS_OUTPUT.PUT_LINE('                           '); END IF;

SELECT ' ' from dual;

set termout off
spool off

WHENEVER SQLERROR EXIT
EXEC IF '&&court_name' is NULL THEN DBMS_OUTPUT.PUT_LINE('############################################################################################'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Supplied XHIBIT Court ID is InValid ... Please retry with Valid XHIBIT Court_ID ...'); DBMS_OUTPUT.PUT_LINE('##'); DBMS_OUTPUT.PUT_LINE('############################################################################################'); RAISE_APPLICATION_ERROR(-20000,'Invalid XHIBIT Court ID '||&&in_xhb_court_id||'  supplied, please retry with valid Court ID ...'); END IF;

set term on
spool &&spool_filename append

ACCEPT  in_confirm_court PROMPT "You have selected &court_name , Please confirm in capitals (YES/NO) : "

EXEC IF '&&in_confirm_court'<>'YES' THEN DBMS_OUTPUT.PUT_LINE('############################################################################################'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Script cancelled as COURT not Confirmed, please run again if needed...'); DBMS_OUTPUT.PUT_LINE('##'); DBMS_OUTPUT.PUT_LINE('############################################################################################'); END IF;

set termout off
spool off

WHENEVER SQLERROR EXIT
EXEC IF '&in_confirm_court'<>'YES' THEN DBMS_OUTPUT.PUT_LINE('############################################################################################'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Script cancelled as COURT not Confirmed, please run again if needed...'); DBMS_OUTPUT.PUT_LINE('##'); DBMS_OUTPUT.PUT_LINE('############################################################################################'); RAISE_APPLICATION_ERROR(-20000,'Script cancelled as COURT not confirmed, please run again if needed...'); END IF;

SET SERVEROUTPUT ON 
spool &&spool_filename append
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;

BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_def_hearing_record for court ID: '||&&in_xhb_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

    MERGE INTO XHB_DEF_HEARING_RECORD xdhr
    USING (SELECT xh.hearing_id, -- CTX-4388 SELECT XH.HEARING_ID
                  xea.status_flag,
                  xea.court_clerk_export
             FROM XHB_EXPORTA xea, XHB_HEARING xh
             -- CTX-4388 link xea.hearing_id to xh.hearing_id
             --               if xea.hearing_hearing_id is NULL then link xea.linked)hearing_id to xh.linked_hearing_id
             --                  AND retreive all hearing_ids in XHB_HEARING where the linked_hearing_ids are matched
             --                update all the hearing_ids linked to that linked_hearing_id in DEF_HEARING
   WHERE xh.court_id = &&in_xhb_court_id AND
                  (xea.hearing_id = xh.hearing_id OR xea.linked_hearing_id = xh.linked_hearing_id) -- CTX-4388
                  ) subqry
    ON (subqry.hearing_id = xdhr.hearing_id)
    WHEN MATCHED THEN
         UPDATE SET xdhr.forma_status = subqry.status_flag,
                    xdhr.forma_court_clerk = subqry.court_clerk_export
              WHERE xdhr.forma_status is NULL; -- update ONLY if NOT updated already 
                                               -- MIGRATED COURTS ONLY - NOT TO OVERWRITE MANUAL CORRECTIONS MADE 
    DBMS_OUTPUT.PUT_LINE(TO_CHAR(SQL%ROWCOUNT)||' rows updated');

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_def_hearing_record.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

EXCEPTION
     WHEN OTHERS THEN
          raise_application_error(-20001,'Error in xhb_update_no_def :- ' || SQLCODE || ' : ' || SQLERRM);

END;
/
spool off
