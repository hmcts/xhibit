/*
Author      : G Nagarajan
Date        : 11/11/2008
Description : This script will create tablespaces, schema and grants for DARTS schema
Usage       : This script should be run as SYS

Version  Date       Author           Description
==============================================================================
1.0      11/11/08   G Nagarajan      Initial version
1.1      12/11/08   Paul Milner      create DARTS configuration table
1.2      18/11/08   Paul Milner      initial DARTS configuration setup
1.3      25/11/08   Paul Milner      Add darts.dump property to support DARTS stub test
1.4      28/11/08   Paul Milner      Add DAR_NEW_MESSAGES table (initial version)
1.4      22/12/08   G Nagarajan      Script was split to run as sys
==============================================================================
*/


set echo on
set term off
column filename new_value spool_filename
  select 'DARTSDB_CREATION_dba_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename



CREATE TABLESPACE DARTSD DATAFILE '/MOJ/oracle/data/CSDBPRD2/d09/dartsd01.dbf' SIZE 500M AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;

CREATE TABLESPACE DARTSX DATAFILE '/MOJ/oracle/data/CSDBPRD2/d10/dartsx01.dbf' SIZE 500M AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;

CREATE TABLESPACE AUDIDARTS DATAFILE '/MOJ/oracle/data/CSDBPRD2/d10/audidarts01.dbf' SIZE 500M AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;


CREATE USER DARTS IDENTIFIED BY DARTS DEFAULT TABLESPACE DARTSD TEMPORARY TABLESPACE TEMP QUOTA UNLIMITED ON DARTSD;
GRANT CONNECT, RESOURCE TO DARTS;
ALTER USER DARTS QUOTA UNLIMITED ON DARTSX;
ALTER USER DARTS QUOTA UNLIMITED ON AUDIDARTS;


spool off
