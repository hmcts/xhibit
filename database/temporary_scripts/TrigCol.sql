--
--          Trigger Column Based Installation Script
--
-- DESCRIPTION:   Installs database objects required for Event Server database 
--                triggering on an Oracle 8 or above database.  The following
--                database objects are created and/or altered:
--
--                1. Stored procedures, which interface to the tables.
--                2. Public synonyms.
--
-- REQUIREMENTS:  1. Oracle 8i Server or above
--         
--                2.
--                This script MUST be run by a user who can grant users of the
--                Event Server the following privileges:
--
--                  a. EXECUTE privilege on DBMS_SQL
--                  b. CREATE PUBLIC SYNONYM privilege
--                  c. DROP   PUBLIC SYNONYM privilege
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
--                   @TrigCol.sql
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
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_create_trigger_cc',
                   'Dropped PUBLIC SYNONYM msp_create_trigger_cc');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_create_trigger2',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_create_trigger2');


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

   --
   -- Stored Procedure for column based triggering
   --
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_create_trigger2 ('  ||
                                       'szTrigName    IN   VARCHAR2, '     ||
                                       'szTblSchema   IN   VARCHAR2, '     ||
                                       'szTblName     IN   VARCHAR2, '     ||
                                       'szEvntParms   IN   VARCHAR2, '     ||
                                       'cEventType    IN   CHAR, '         ||
                                       'cActionType   IN   CHAR, '         ||
                                       'szWhenCondition IN VARCHAR2 '      ||
                                       ') AS '  ||
         'cid           INTEGER; '        ||
         'szTrigCmd     VARCHAR2(512); '  ||
         'szEventType   VARCHAR2(20); '   ||
         'szActionType  VARCHAR2(20); '   ||
         'szTrigBody    VARCHAR2(200); '  ||
         'szRowClause   VARCHAR2(20); '   ||
      'BEGIN '                            ||
         'cid := dbms_sql.open_cursor; ' ||
         'IF    cEventType    =  ''I'' THEN '      ||
               'szEventType   := '' INSERT ''; '   ||
         'ELSIF cEventType    =  ''U'' THEN '      ||
               'szEventType   := '' UPDATE ''; '   ||
         'ELSIF cEventType    =  ''D'' THEN '      ||
               'szEventType   := '' DELETE ''; '   ||
         'ELSE '                                   ||
            'raise_application_error (-20100, ''Invalid event type: '' || cEventType); ' ||
         'END IF; '                                ||
         'IF    cActionType   =  ''R'' THEN '      ||
               'szActionType  := '' AFTER ''; '    ||
               'szRowClause   := '' FOR EACH ROW ''; ' ||
               'szTrigBody    := '' BEGIN '' || ''' || szSpfx || ''' || ''msp_record_events1('' || szEvntParms || ''); END;'' ' || '; ' ||
         'ELSIF cActionType   =  ''T'' THEN '                        ||
               'szActionType  := '' BEFORE ''; '                     ||
               'szRowClause   := '' ''; '          ||
               'szTrigBody    := '' BEGIN '' || ''' || szSpfx || ''' || ''msp_record_events1('' || szEvntParms || ''); END;'' ' || '; ' ||
         'ELSE '                                                           ||
            'raise_application_error (-20101, ''Invalid action type: '' || cActionType); ' ||
         'END IF; '                             ||
         'szTrigCmd  := ''CREATE TRIGGER '' || ''' || szSpfx || ''' || szTrigName || szActionType || szEventType || ' ||
                              '''ON '' || szTblSchema || ''.'' || szTblName || szRowClause || szWhenCondition || szTrigBody; ' || 
         'dbms_sql.parse (cid, szTrigCmd, dbms_sql.native); ' ||
         'dbms_sql.close_cursor (cid); ' ||
      'EXCEPTION ' ||
         'WHEN OTHERS THEN '  ||
            'dbms_sql.close_cursor(cid); ' ||
            'raise_application_error(-20102, ''Mercator Trigger Definition Failure. '' || szTrigCmd, TRUE); ' ||
            'RAISE; '                      ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_create_trigger2');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_create_trigger2 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_create_trigger_cc FOR ' || szSpfx || 'msp_create_trigger2',
                   'Created PUBLIC SYNONYM msp_create_trigger_cc');

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

END;
/

