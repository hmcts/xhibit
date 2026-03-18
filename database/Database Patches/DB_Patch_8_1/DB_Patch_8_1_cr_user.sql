/*
 * Filename:    DB_Patch_8_1_cr_user.sql
 *
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   06/07/2006	K SHAH			changes made to comprise release 8.1
 *
 */ 


set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_8_1_cr_user_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

CREATE USER "EXISS"  PROFILE "DEFAULT" 
    IDENTIFIED BY "exiss" 
    DEFAULT TABLESPACE "EXISSD" 
    QUOTA unlimited on "EXISSD"
    QUOTA unlimited on "AUDIEXI"
    QUOTA unlimited on "EXISSX"
    TEMPORARY TABLESPACE "TEMP" 
    ACCOUNT UNLOCK;

GRANT CREATE ANY TRIGGER TO "EXISS";
GRANT CREATE PUBLIC SYNONYM TO "EXISS";
GRANT CREATE SESSION TO "EXISS";
GRANT CREATE VIEW TO "EXISS";
GRANT DROP ANY TRIGGER TO "EXISS";
GRANT DROP PUBLIC SYNONYM TO "EXISS";
GRANT UNLIMITED TABLESPACE TO "EXISS";
GRANT EXECUTE ON  "SYS"."DBMS_ALERT" TO "EXISS";
GRANT EXECUTE ON  "SYS"."DBMS_SQL" TO "EXISS";
GRANT EXECUTE ON  "SYS"."DBMS_TRANSACTION" TO "EXISS";
GRANT "RESOURCE" TO "EXISS";


spool off
