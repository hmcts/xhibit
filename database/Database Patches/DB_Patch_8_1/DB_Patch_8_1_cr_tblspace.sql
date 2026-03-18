/*
 * Filename:    DB_Patch_8_1_cr_tblspace.sql
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
  select 'DBPATCH_8_1_cr_tblspace_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

CREATE TABLESPACE "AUDIEXI" 
    LOGGING 
    DATAFILE '/u01/oradata/csdbprd2/AUDIEXI_01.dbf' SIZE 100M
    REUSE EXTENT MANAGEMENT LOCAL BLOCKSIZE 8192 
    UNIFORM SIZE 2M SEGMENT SPACE MANAGEMENT AUTO ;

ALTER DATABASE DATAFILE '/u01/oradata/csdbprd2/AUDIEXI_01.dbf' AUTOEXTEND ON NEXT 100M MAXSIZE 3000M;  


CREATE TABLESPACE "EXISSD" 
    LOGGING 
    DATAFILE '/u03/oradata/csdbprd2/EXISSD_01.dbf' SIZE 100M 
    REUSE EXTENT MANAGEMENT LOCAL BLOCKSIZE 8192  
    UNIFORM SIZE 2M SEGMENT SPACE MANAGEMENT AUTO ;

ALTER DATABASE DATAFILE '/u03/oradata/csdbprd2/EXISSD_01.dbf' AUTOEXTEND ON NEXT 100M MAXSIZE 3000M; 


CREATE TABLESPACE "EXISSX" 
    LOGGING 
    DATAFILE '/u03/oradata/csdbprd2/EXISSX_01.dbf' SIZE 100M 
    REUSE EXTENT MANAGEMENT LOCAL BLOCKSIZE 8192  
    UNIFORM SIZE 2M SEGMENT SPACE MANAGEMENT AUTO ;

ALTER DATABASE DATAFILE '/u03/oradata/csdbprd2/EXISSX_01.dbf' AUTOEXTEND ON NEXT 100M MAXSIZE 3000M;

spool off 