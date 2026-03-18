--
--          Trigger Load Balancing Installation Script
--
-- DESCRIPTION:   Installs database objects required for Event Server database 
--                triggering on an Oracle 9 or above database.  The following
--                database objects are created and/or altered:
--
--                1. Tables, which track Event Server alive status.
--                2. Stored procedures, which interface to the tables.
--                3. Packages which implement the load balancing functionality.
--                4. Sequences that are used for load balancing.
--
-- REQUIREMENTS:  1. Oracle 9i Server
--         
--                2.
--                This script MUST be run by a user who can grant users of the
--                Event Server the following privileges:
--
--                  a. EXECUTE privilege on DBMS_SQL
--                  b. EXECUTE privilege on DBMS_TRANSACTION
--                  c. CREATE PUBLIC SYNONYM privilege
--                  d. DROP   PUBLIC SYNONYM privilege
--
--                The PUBLIC SYNONYM privileges are only needed at 
--                installation time.
--
--                The other privileges are necessary so that all Mercator DB 
--                triggering users will operate within a secure and pre-defined
--                Oracle environment.
--
-- INSTALL STEPS:
--                1. Before loading this script into SQL*Plus, issue 
--                   'SET SERVEROUTPUT ON' to see status messages.
--
--                2. If re-installing this script using a different user name,
--                   then follow the RE-INSTALL STEPS below.  Otherwise, just
--                   ignore this step.
--
--                3. Load and run the file from SQL*PLUS via the following
--                   command: 
--                   @TrigLbal.sql
--
-- RE-INSTALL STEPS:
--                Whenever re-installing under a different user name, all 
--                database objects associated with the old schema must be
--                properly deleted.  This can be done as follows:
--                   a. Comment out the lines that "CREATE" all the 
--                      database objects.
--                      Look below for 'RE-INSTALL' to determine which 
--                      section to comment out.
--
--                   b. Follow Install Step 3 above.
--
--                   c. Undo changes made in step a.
--
--                   d. Follow Step 3 above.
--
-- UN-INSTALL STEPS:
--                To remove this code and revert back to the original trigger
--                installation, do the following:
--                   a. Comment out the lines that "CREATE" all the 
--                      database objects.
--                      Look below for 'UN-INSTALL' to determine which 
--                      section to comment out.
--
--                   b. Follow Install Step 3 above.
--

DECLARE 

   nRc         INTEGER;
   nWarningCnt INTEGER;
   szMessage   VARCHAR2(500);
   szSchema    VARCHAR2(50);
   szSpfx      VARCHAR2(51);     -- Schema prefix: Schema + '.'

   PROCEDURE msp_exec_cmd (szDdlCmd  IN VARCHAR2, szMsg IN VARCHAR2 DEFAULT NULL) IS
      X_SEQ_NOTEXISTS     EXCEPTION;
      X_SYN_NOTEXISTS     EXCEPTION;
      X_PRO_NOTEXISTS     EXCEPTION;
      X_TBL_COLEXISTS     EXCEPTION;
      PRAGMA EXCEPTION_INIT   (X_SEQ_NOTEXISTS, -2289);
      PRAGMA EXCEPTION_INIT   (X_SYN_NOTEXISTS, -1432);
      PRAGMA EXCEPTION_INIT   (X_PRO_NOTEXISTS, -4043);
      PRAGMA EXCEPTION_INIT   (X_TBL_COLEXISTS, -1430);
      cid INTEGER;
   BEGIN

      cid := dbms_sql.open_cursor;

      dbms_sql.parse(cid, szDdlCmd, dbms_sql.native);

      IF szMsg IS NOT NULL THEN
         DBMS_OUTPUT.PUT_LINE(szMsg);
      END IF;

      dbms_sql.close_cursor(cid);

   EXCEPTION
      WHEN X_SEQ_NOTEXISTS OR X_SYN_NOTEXISTS OR X_PRO_NOTEXISTS OR X_TBL_COLEXISTS THEN 
         dbms_sql.close_cursor(cid);
   END;

BEGIN

   dbms_output.enable(8000);

   szSchema := USER;
   szSchema := UPPER(RTRIM(LTRIM(szSchema)));
   dbms_output.put_line('Mercator objects will be located in the following schema: ' || szSchema );
   szSpfx := szSchema || '.';

   --
   -- Drop any objects created from a previous execution of this script.
   --
   msp_exec_cmd   ('DROP    SEQUENCE       ' || szSpfx || 'mseq_load_bal',
                   'Dropped SEQUENCE       ' || szSpfx || 'mseq_load_bal');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_heartbeat',
                   'Dropped PUBLIC SYNONYM msp_heartbeat');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_heartbeat1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_heartbeat1');
   msp_exec_cmd   ('DROP    PACKAGE        ' || szSpfx || 'mpkg_load_bal',
                   'Dropped PACKAGE        ' || szSpfx || 'mpkg_load_bal');


   -- *********      RE-INSTALLATION TO A DIFFERENT SCHEMA     **********
   -- *********                    OR                          **********
   -- *********                UN-INSTALL                      **********
   --
   -- If un-installing or re-installing to a DIFFERENT schema, then comment
   -- out all the following CREATE commands by simply surrounding them with
   -- the comment characters.  See further below for the closing comment.
   -- /*

   --
   -- Privilege validation.
   --
   nWarningCnt := 0;

   -- Make sure the USER has EXECUTE privilege on the DBMS_SQL package.
   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc     
         FROM     ALL_TAB_PRIVS 
         WHERE    GRANTEE     IN (USER, 'PUBLIC')
           AND    TABLE_NAME  = 'DBMS_SQL'
           AND    PRIVILEGE   = 'EXECUTE';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required EXECUTE privilege on the DBMS_SQL package.');
      ELSE
         dbms_output.put_line('ERROR: The current user, ' || USER || ', requires EXECUTE privilege on the DBMS_SQL package.');
         raise_application_error(-20901, 'Insufficient privileges on the DBMS_SQL package.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted EXECUTE on the DBMS_SQL package in order ');
         dbms_output.put_line('for Mercator database triggering to work.  ');
         dbms_output.put_line('This privilege can be granted by the SYSDBA via the command: GRANT EXECUTE on DBMS_SQL TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc     
         FROM     ALL_TAB_PRIVS 
         WHERE    GRANTEE     IN (USER, 'PUBLIC')
           AND    TABLE_NAME  = 'DBMS_TRANSACTION'
           AND    PRIVILEGE   = 'EXECUTE';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required EXECUTE privilege on the DBMS_TRANSACTION package.');
      ELSE
         dbms_output.put_line('ERROR: The current user, ' || USER || ', requires EXECUTE privilege on the DBMS_TRANSACTION package.');
         raise_application_error(-20901, 'Insufficient privileges on the DBMS_TRANSACTION package.');
      END IF;

      EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted EXECUTE on the DBMS_TRANSACTION package in order ');
         dbms_output.put_line('for Mercator database triggering to work.  ');
         dbms_output.put_line('This privilege can be granted by the SYSDBA via the command: GRANT EXECUTE on DBMS_TRANSACTION TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

   --
   -- Sequence Definitions
   --
   msp_exec_cmd   ('CREATE  SEQUENCE ' || szSpfx || 'mseq_load_bal ' ||
                            'INCREMENT BY 1 START WITH 1 MAXVALUE 1000000 MINVALUE 1 CYCLE CACHE 100 NOORDER',
                   'Created SEQUENCE       ' || szSpfx || 'mseq_load_bal');

   --
   -- Table Definitions.
   --
   msp_exec_cmd   ('ALTER TABLE ' || szSpfx || 'mtbl_trigger_server ' ||
                         'ADD (alive_ts TIMESTAMP WITH TIME ZONE, ' ||
                              'alive_interval INTERVAL DAY TO SECOND DEFAULT ''0 0:1:0'' NOT NULL)',
                   'Altered TABLE          ' || szSpfx || 'mtbl_trigger_server');

   --
   -- Stored Procedure
   --
   msp_exec_cmd   ('CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_heartbeat1 (' ||
                      'es_id         IN    mtbl_trigger_server.ID%TYPE, ' ||
                	    'es_name       IN    mtbl_trigger_server.Name%TYPE DEFAULT NULL ) AS ' ||
                   'BEGIN ' ||
                      'IF es_id IS NULL THEN ' ||
                          'UPDATE mtbl_trigger_server ' ||
                                 'SET   alive_ts = SYSTIMESTAMP ' ||
                                 'WHERE  UPPER(name) = UPPER(es_name); ' ||
                      'ELSE ' ||
                          'UPDATE mtbl_trigger_server ' ||
                                 'SET   alive_ts = SYSTIMESTAMP ' ||
                                 'WHERE  id = es_id; ' ||
                      'END IF; ' ||
                   'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_heartbeat1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_heartbeat1 TO PUBLIC');
   msp_exec_cmd   ('CREATE  PUBLIC SYNONYM msp_heartbeat FOR ' || szSpfx || 'msp_heartbeat1', 
                   'Created PUBLIC SYNONYM msp_heartbeat');

   --
   -- Package definition
   --
   msp_exec_cmd  (
       'CREATE OR REPLACE PACKAGE ' || szSpfx || 'mpkg_load_bal AS ' ||
          'PROCEDURE get_es_id (sid OUT mtbl_trigger_server.ID%TYPE, fNewTx OUT BOOLEAN); ' ||
       'END mpkg_load_bal;',
                   'Created PACKAGE        ' || szSpfx || 'mpkg_load_bal');

   msp_exec_cmd  (
      'CREATE OR REPLACE PACKAGE BODY ' || szSpfx || 'mpkg_load_bal AS ' ||
         'lb_limit CONSTANT PLS_INTEGER := 256; ' ||
         'TYPE   VA_ES_LIST IS VARRAY(256) OF mtbl_trigger_server.ID%TYPE; ' ||
         'tx_id VARCHAR2(256);' ||
         'es_id mtbl_trigger_server.ID%TYPE; ' ||
      'PROCEDURE get_es_id (sid OUT mtbl_trigger_server.ID%TYPE, fNewTx OUT BOOLEAN) IS ' ||
         'nID    mtbl_trigger_server.ID%TYPE; ' ||
         'nSeq   PLS_INTEGER; ' ||
         'nMod   PLS_INTEGER; ' ||
         'cur_tx_id tx_id%TYPE; ' ||
         'CURSOR c_avail_es IS  ' ||
                'SELECT ID  ' ||
                'FROM   mtbl_trigger_server ' ||
                'WHERE  (NVL(alive_ts, SYSTIMESTAMP) + alive_interval >= SYSTIMESTAMP)  AND ' ||
                       '((startupdate > shutdowndate) OR (shutdowndate IS NULL) )' ||
                'ORDER BY ID; ' ||
         'CURSOR c_reg_es IS  ' ||
                'SELECT ID  ' ||
                'FROM   mtbl_trigger_server ' ||
                'WHERE  startupdate > shutdowndate OR shutdowndate IS NULL ' ||
                'ORDER BY ID; ' ||
         'es_list VA_ES_LIST; ' ||
      'BEGIN  ' ||
         'cur_tx_id := dbms_transaction.local_transaction_id(); ' ||
         'IF (tx_id != cur_tx_id)                          OR ' ||
            '(tx_id IS NULL     AND cur_tx_id IS NOT NULL) OR  ' ||
            '(tx_id IS NOT NULL AND cur_tx_id IS NULL) ' ||
         'THEN ' ||
            'tx_id := cur_tx_id; ' ||
            'SELECT mseq_load_bal.NEXTVAL INTO nSeq FROM DUAL; ' ||
            'OPEN c_avail_es; ' ||
            'FETCH c_avail_es BULK COLLECT INTO es_list LIMIT lb_limit; ' ||
            'CLOSE c_avail_es; ' ||
            'IF es_list.COUNT = 0 THEN ' ||
               'OPEN c_reg_es;                ' ||
               'FETCH c_reg_es BULK COLLECT INTO es_list LIMIT lb_limit; ' ||
               'CLOSE c_reg_es; ' ||
            'END IF; ' ||
            'IF es_list.COUNT = 0 THEN ' ||
               'sid := 0; ' ||
            'ELSE ' ||
               'nMod := (nSeq MOD es_list.COUNT) + 1; ' ||
               'sid := es_list(nMod); ' ||
            'END IF; ' ||
            'es_id  := sid; ' ||
            'fNewTx := TRUE; ' ||
         'ELSE ' ||
            'sid    := es_id; ' ||
            'fNewTx := FALSE; ' ||
         'END IF; ' ||
      'END; ' ||
      'BEGIN ' ||
         'tx_id := NULL; ' ||
         'es_id := 0; ' ||
      'END mpkg_load_bal; ',
                   'Created PACKAGE BODY   ' || szSpfx || 'mpkg_load_bal');

   --
   -- Event signaling routines.
   --
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE  ' || szSpfx || 'msp_record_events1( '  ||
         'szTrigName Trigger_Registry.TriggerName%TYPE, '                  ||
         'cActionType CHAR, '                ||
         'nRowId ROWID ) AS '                ||
         'szSignalEvent VARCHAR2(50); '      ||
         'nID    NUMBER; '                   ||
         'nEsId  Trigger_Registry.ServerID%TYPE; '     ||
         'fNewTx BOOLEAN; '                            ||
         'CURSOR watch_events_cursor IS '    ||
                'SELECT ID '                 ||
                'FROM   Trigger_Registry '   ||
                'WHERE  TriggerName = szTrigName ' ||
                  'AND  ProcessDate IS NULL '      ||
                  'AND  ActionType = cActionType ' ||
                  'AND  ServerID   = nEsId '       ||
                'ORDER BY ID; '                    ||
       'BEGIN '                                    ||
         'mpkg_load_bal.get_es_id (nEsId, fNewTx); ' ||
         'IF cActionType = ''R'' AND fNewTx THEN ' ||
            'LOCK table Trigger_Registry IN ROW SHARE MODE; ' ||
         'END IF; ' ||
         'OPEN watch_events_cursor; '              ||
         'LOOP '                                   ||
             'FETCH watch_events_cursor INTO nID; '||
             'EXIT WHEN watch_events_cursor%NOTFOUND; '     ||
             'BEGIN '                                       ||
               'INSERT INTO Trigger_Events '                            ||
                           '(id,st_rowid) '                             ||
                      'VALUES '                                         ||
                           '(nID,nRowId); '                             ||
               'IF fNewTx THEN '                                        ||
                  'szSignalEvent := ''MERCTRIG_'' || nID; '                ||
                  'DBMS_ALERT.signal(szSignalEvent, szSignalEvent); '      ||
               'END IF; '                                               ||
             'EXCEPTION '                                               ||
               'WHEN DUP_VAL_ON_INDEX THEN '                            ||
                  'DBMS_ALERT.signal(szSignalEvent, szSignalEvent); '   ||
             'END; '                                                    ||
         'END LOOP; '                                                   ||
         'CLOSE watch_events_cursor; '                                  ||
       'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_record_events1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_record_events1 TO PUBLIC');


   IF nWarningCnt > 0 THEN
      IF nWarningCnt = 1 THEN
         szMessage   := 'A warning condition was detected that needs ';
      ELSE
         szMessage   := 'Multiple warning conditions were detected that need ';
      END IF;
      szMessage      := szMessage || 'to be resolved before running Mercator DB triggering.  ';
      szMessage      := szMessage || 'Warning messages can be viewed from SQL*PLUS by issuing the ''SET SERVEROUTPUT ON'' command.';
      raise_application_error(-20999, szMessage);
   END IF;

   -- *********      RE-INSTALLATION TO A DIFFERENT SCHEMA     **********
   -- *********                    OR                          **********
   -- *********                UN-INSTALL                      **********
   --
   -- If uninstalling or re-installing to a DIFFERENT schema, then comment out
   -- all the previous CREATE commands by simply surrounding them with comment 
   -- characters.  See above for the opening comment.
   -- */

   -- *********                UN-INSTALL                      **********
   --                  
   -- If uninstalling, then just uncomment the follownig stored procedure.
   /*
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE  ' || szSpfx || 'msp_record_events1( '  ||
         'szTrigName Trigger_Registry.TriggerName%TYPE, '                  ||
         'cActionType CHAR, '                ||
         'nRowId ROWID ) AS '                ||
         'szSignalEvent VARCHAR2(50); '      ||
         'nID    NUMBER; '                   ||
         'CURSOR watch_events_cursor IS '    ||
                'SELECT ID '                 ||
                'FROM   Trigger_Registry '   ||
                'WHERE  TriggerName = szTrigName ' ||
                  'AND  ProcessDate IS NULL '      ||
                  'AND  ActionType = cActionType ' ||
                'ORDER BY ID; '                    ||
       'BEGIN '                                    ||
         'IF cActionType = ''R'' THEN ' ||
            'LOCK table Trigger_Registry IN ROW SHARE MODE; ' ||
         'END IF; ' ||
         'OPEN watch_events_cursor; '              ||
         'LOOP '                                   ||
             'FETCH watch_events_cursor INTO nID; '||
             'EXIT WHEN watch_events_cursor%NOTFOUND; '     ||
             'BEGIN '                                       ||
               'szSignalEvent := ''MERCTRIG_'' || nID; '    ||
               'INSERT INTO Trigger_Events '                ||
                           '(id,st_rowid) '   ||
                      'VALUES '                                         ||
                           '(nID,nRowId); '        ||
               'DBMS_ALERT.signal(szSignalEvent, szSignalEvent); '      ||
             'EXCEPTION '                                               ||
               'WHEN DUP_VAL_ON_INDEX THEN '                            ||
                  'DBMS_ALERT.signal(szSignalEvent, szSignalEvent); '   ||
             'END; '                                                    ||
         'END LOOP; '                                                   ||
         'CLOSE watch_events_cursor; '                                  ||
       'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_record_events1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_record_events1 TO PUBLIC');
   */

END;
/

