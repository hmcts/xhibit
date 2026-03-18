--
--          Mercator Triggering Installation Script
--
-- DESCRIPTION:   Installs database objects required for Mercator database 
--                triggering on an Oracle 8 or above database.  The following
--                database objects are created:
--
--                1. Tables, which track Event Server watch events.
--                2. Stored procedures, which interface to the tables.
--                3. Public synonyms to the tables and stored procedures.
--                4. Sequences that generate unique trigger IDs.
--
-- REQUIREMENT:   This script MUST be run by a user who has the following
--                privileges (e.g. 'SYSTEM' or a new user called 'MERCATOR').
--
--                1. EXECUTE privilege on DBMS_ALERT
--                   (e.g. GRANT EXECUTE ON DBMS_ALERT TO MERCATOR).
--                2. EXECUTE privilege on DBMS_SQL
--                   (e.g. GRANT EXECUTE ON DBMS_SQL   TO MERCATOR).
--                3. CREATE ANY TRIGGER system privilege
--                   (e.g. GRANT CREATE ANY TRIGGER    TO MERCATOR).
--                4. DROP ANY TRIGGER system privilege
--                   (e.g. GRANT DROP   ANY TRIGGER    TO MERCATOR).
--                5. CREATE PUBLIC SYNONYM privilege
--                    e.g. GRANT CREATE PUBLIC SYNONYM TO MERCATOR).
--                6. DROP   PUBLIC SYNONYM privilege
--                    e.g. GRANT DROP   PUBLIC SYNONYM TO MERCATOR).
--
--                The PUBLIC SYNONYM privileges are only needed at 
--                installation time.
--
--                The other privileges are necessary so that all Mercator DB 
--                triggering users will operate within a secure and pre-defined
--                Oracle environment.
--
--                The full extent of Mercator triggering functionality can be
--                found within the msp_create_trigger stored procedure.  
--
-- INSTALL STEPS:
--                1. Before loading this script into SQL*Plus, issue 
--                   'SET SERVEROUTPUT ON' to see status messages.
--
--                2. If re-installing this script using a different user name,
--                   then follow the RE-INSTALL STEPS below.
--
--                3. Once loaded into SQL*PLUS, use the slash (/) to
--                   to activate the script.
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
--                   b. Follow Step 3 above.
--
--                   c. Undo changes made in step a.
--
--                   d. Follow Step 3 above.
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
      X_TBL_NOTEXISTS     EXCEPTION;
      X_PRO_NOTEXISTS     EXCEPTION;
      PRAGMA EXCEPTION_INIT   (X_SEQ_NOTEXISTS, -2289);
      PRAGMA EXCEPTION_INIT   (X_SYN_NOTEXISTS, -1432);
      PRAGMA EXCEPTION_INIT   (X_TBL_NOTEXISTS, -942);
      PRAGMA EXCEPTION_INIT   (X_PRO_NOTEXISTS, -4043);
      cid INTEGER;
   BEGIN

      cid := dbms_sql.open_cursor;

      dbms_sql.parse(cid, szDdlCmd, dbms_sql.native);

      IF szMsg IS NOT NULL THEN
         DBMS_OUTPUT.PUT_LINE(szMsg);
      END IF;

      dbms_sql.close_cursor(cid);

   EXCEPTION
      WHEN X_SEQ_NOTEXISTS OR X_SYN_NOTEXISTS OR X_TBL_NOTEXISTS OR X_PRO_NOTEXISTS THEN 
         dbms_sql.close_cursor(cid);
   END;

   PROCEDURE msp_trigger_cleanup (szSchema IN VARCHAR2) IS
      X_TBL_NOTEXISTS         EXCEPTION;
      PRAGMA EXCEPTION_INIT   (X_TBL_NOTEXISTS, -942);

      szDmlCmd    VARCHAR2(200);
      szTrigName  VARCHAR2(100);

      cid         INTEGER;
      nTemp       INTEGER;
   BEGIN
      cid := dbms_sql.open_cursor;

      szDmlCmd := 'SELECT TriggerName ' ||
                    'FROM   Trigger_Catalog';

      dbms_sql.parse(cid, szDmlCmd, dbms_sql.native);

      dbms_sql.define_column(cid, 1, szTrigName, 100);

      nTemp := dbms_sql.execute(cid);

      LOOP 
          IF dbms_sql.fetch_rows( cid ) <= 0 THEN
             EXIT;
          END IF;
          
          dbms_sql.column_value (cid, 1, szTrigName);

          BEGIN
            msp_exec_cmd ('DROP   TRIGGER         ' || szSchema || szTrigName,
                          'Dropped TRIGGER        ' || szSchema || szTrigName);
          EXCEPTION 
          WHEN OTHERS THEN 
             NULL; 
          END; 

      END LOOP; 

      dbms_sql.close_cursor(cid);

   EXCEPTION
      WHEN X_TBL_NOTEXISTS THEN 
         dbms_sql.close_cursor(cid);
   END;

BEGIN

   dbms_output.enable(8000);

   szSchema := USER;
   szSchema := UPPER(RTRIM(LTRIM(szSchema)));
   dbms_output.put_line('Mercator objects will be located in the following schema: ' || szSchema );
   szSpfx := szSchema || '.';

   -- Before dropping database objects, make sure that any dynamically created
   -- trigger entries are dropped.
   msp_trigger_cleanup(szSpfx);

   --
   -- Drop any objects created from a previous execution of this script.
   --
   msp_exec_cmd   ('DROP    SEQUENCE       ' || szSpfx || 'mseq_server_id',
                   'Dropped SEQUENCE       ' || szSpfx || 'mseq_server_id');
   msp_exec_cmd   ('DROP    SEQUENCE       ' || szSpfx || 'mseq_registry_id',
                   'Dropped SEQUENCE       ' || szSpfx || 'mseq_registry_id');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM trigger_events',
                   'Dropped PUBLIC SYNONYM trigger_events');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM trigger_registry',
                   'Dropped PUBLIC SYNONYM trigger_registry');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM trigger_catalog',
                   'Dropped PUBLIC SYNONYM trigger_catalog');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM trigger_server',
                   'Dropped PUBLIC SYNONYM trigger_server');
   msp_exec_cmd   ('DROP    TABLE          ' || szSpfx || 'mtbl_trigger_events',
                   'Dropped TABLE          ' || szSpfx || 'mtbl_trigger_events');
   msp_exec_cmd   ('DROP    TABLE          ' || szSpfx || 'mtbl_trigger_registry',
                   'Dropped TABLE          ' || szSpfx || 'mtbl_trigger_registry');
   msp_exec_cmd   ('DROP    TABLE          ' || szSpfx || 'mtbl_trigger_catalog',
                   'Dropped TABLE          ' || szSpfx || 'mtbl_trigger_catalog');
   msp_exec_cmd   ('DROP    TABLE          ' || szSpfx || 'mtbl_trigger_server',
                   'Dropped TABLE          ' || szSpfx || 'mtbl_trigger_server');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_get_server_id',
                   'Dropped PUBLIC SYNONYM msp_get_server_id');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_register',
                   'Dropped PUBLIC SYNONYM msp_register');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_register_srvr',
                   'Dropped PUBLIC SYNONYM msp_register_srvr');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_shutdown_srvr',
                   'Dropped PUBLIC SYNONYM msp_shutdown_srvr');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_trig_exists',
                   'Dropped PUBLIC SYNONYM msp_trig_exists');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_wait_event',
                   'Dropped PUBLIC SYNONYM msp_wait_event');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_signal_event',
                   'Dropped PUBLIC SYNONYM msp_signal_event');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_remove_event',
                   'Dropped PUBLIC SYNONYM msp_remove_event');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_create_trigger',
                   'Dropped PUBLIC SYNONYM msp_create_trigger');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_drop_trigger',
                   'Dropped PUBLIC SYNONYM msp_drop_trigger');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_get_server_id1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_get_server_id1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_register1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_register1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_register_srvr1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_register_srvr1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_shutdown_srvr1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_shutdown_srvr1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_trig_exists1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_trig_exists1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_record_events1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_record_events1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_wait_event1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_wait_event1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_signal_event1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_signal_event1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_remove_event1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_remove_event1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_create_trigger1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_create_trigger1');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_drop_trigger1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_drop_trigger1');


   -- *********      RE-INSTALLATION TO A DIFFERENT SCHEMA     **********
   --
   -- If re-installing to a DIFFERENT schema, then comment out all the following
   -- CREATE commands by simply surrounding them with the comment characters.
   -- See further below for the closing comment.
   -- /*

   --
   -- Privilege validation.
   --
   nWarningCnt := 0;

   -- Make sure the USER has EXECUTE privilege on the DBMS_ALERT package.
   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc     
         FROM     ALL_TAB_PRIVS 
         WHERE    GRANTEE     IN (USER, 'PUBLIC')
           AND    TABLE_NAME  = 'DBMS_ALERT'
           AND    PRIVILEGE   = 'EXECUTE';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required EXECUTE privilege on the DBMS_ALERT package.');
      ELSE
         dbms_output.put_line('ERROR: The current user, ' || USER || ', requires EXECUTE privilege on the DBMS_ALERT package.');
         raise_application_error(-20900, 'Insufficient privileges on the DBMS_ALERT package.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted EXECUTE on the DBMS_ALERT package in order ');
         dbms_output.put_line('for Mercator database triggering to work.  ');
         dbms_output.put_line('This privilege can be granted by the SYSDBA via the command: GRANT EXECUTE on DBMS_ALERT TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

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

   -- Make sure the CREATE ANY TRIGGER system privilege is granted to the current user.
   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc
         FROM     USER_SYS_PRIVS 
         WHERE    PRIVILEGE   = 'CREATE ANY TRIGGER';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required CREATE ANY TRIGGER system privilege.');
      ELSE
         msp_exec_cmd   ('GRANT CREATE ANY TRIGGER to ' || USER,
                         'Granted CREATE ANY TRIGGER to ' || USER);
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted the CREATE ANY TRIGGER system privilege in order ');
         dbms_output.put_line('for Mercator database triggering to work across all schemas.  ');
         dbms_output.put_line('This system privilege can be granted by the DBA via the command: GRANT CREATE ANY TRIGGER TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

   -- Make sure the DROP ANY TRIGGER system privilege is granted to the current user.
   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc 
         FROM     USER_SYS_PRIVS 
         WHERE    PRIVILEGE   = 'DROP ANY TRIGGER';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required DROP ANY TRIGGER system privilege.');
      ELSE
         msp_exec_cmd   ('GRANT DROP ANY TRIGGER to ' || USER,
                         'Granted DROP ANY TRIGGER to ' || USER);
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted the DROP ANY TRIGGER system privilege in order ');
         dbms_output.put_line('for Mercator database triggering to work across all schemas.  ');
         dbms_output.put_line('This system privilege can be granted by the DBA via the command: GRANT DROP ANY TRIGGER TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

   --
   -- Sequence Definitions
   --
   msp_exec_cmd   ('CREATE  SEQUENCE ' || szSpfx || 'mseq_server_id ' ||
                            'INCREMENT BY 1 START WITH 1 MAXVALUE 999999999 CYCLE CACHE 5',
                   'Created SEQUENCE       ' || szSpfx || 'mseq_server_id');
   msp_exec_cmd   ('CREATE  SEQUENCE ' || szSpfx || 'mseq_registry_id ' ||
                            'INCREMENT BY 1 START WITH 1 MAXVALUE 999999999 CYCLE CACHE 10',
                   'Created SEQUENCE       ' || szSpfx || 'mseq_registry_id');
   --
   -- Table Definitions.
   --
   msp_exec_cmd   (
      'CREATE TABLE ' || szSpfx || 'mtbl_trigger_server ( ' ||
        'ID                NUMBER(9), ' ||
        'Name              VARCHAR2 (80), ' ||
        'Address           VARCHAR2 (30), ' ||
        'StartupDate       DATE DEFAULT SYSDATE, ' ||
        'ShutdownDate      DATE, PRIMARY KEY (ID) ) ' ||
	'TABLESPACE	   MERCATORD ' ||
        'STORAGE (INITIAL 1K NEXT 1K MINEXTENTS 1 MAXEXTENTS 200 PCTINCREASE 0) ',
                   'Created TABLE          ' || szSpfx || 'mtbl_trigger_server');
   msp_exec_cmd   ('GRANT SELECT, INSERT, UPDATE, DELETE ON ' || szSpfx || 'mtbl_trigger_server TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM trigger_server FOR ' || szSpfx || 'mtbl_trigger_server',
                   'Created PUBLIC SYNONYM trigger_server');

   msp_exec_cmd   (
      'CREATE TABLE ' || szSpfx || 'mtbl_trigger_catalog ( ' ||
        'TableName         VARCHAR2 (33) CHECK (TableName IS NOT NULL), ' ||
		  'TableSchema       VARCHAR2 (30),' ||
        'InsertEnabled     CHAR (1) CHECK (InsertEnabled IN (''T'',''F'') AND InsertEnabled IS NOT NULL),' ||
        'UpdateEnabled     CHAR (1) CHECK (UpdateEnabled IN (''T'',''F'') AND UpdateEnabled IS NOT NULL),' ||
        'DeleteEnabled     CHAR (1) CHECK (DeleteEnabled IN (''T'',''F'') AND DeleteEnabled IS NOT NULL),' ||
		  'TriggerName       VARCHAR2 (33) CHECK (TriggerName IS NOT NULL),' ||
		  'UserSchema        VARCHAR2 (30) CHECK (UserSchema IS NOT NULL),' ||
        'RowBased          CHAR (1) CHECK (RowBased IN (''T'',''F'') AND RowBased IS NOT NULL),' ||
        'TableBased        CHAR (1) CHECK (TableBased IN (''T'',''F'') AND TableBased IS NOT NULL),' ||
		  'ServerID          NUMBER(9)  CHECK (ServerID IS NOT NULL),' ||
		  'EntryDate         DATE DEFAULT SYSDATE,' ||
		  'CONSTRAINT mtbl_trigger_catalog FOREIGN KEY (ServerID) REFERENCES mtbl_trigger_server(ID) ) ' ||
	'TABLESPACE	   MERCATORD ' ||
        'STORAGE (INITIAL 5K NEXT 5K MINEXTENTS 1 MAXEXTENTS 200 PCTINCREASE 0) ',
                   'Created TABLE          ' || szSpfx || 'mtbl_trigger_catalog'); 
   msp_exec_cmd   ('GRANT SELECT, INSERT, UPDATE, DELETE ON ' || szSpfx || 'mtbl_trigger_catalog TO PUBLIC');
   msp_exec_cmd   ('CREATE  PUBLIC SYNONYM trigger_catalog FOR ' || szSpfx || 'mtbl_trigger_catalog',
                   'Created PUBLIC SYNONYM trigger_catalog');

   msp_exec_cmd   (
      'CREATE TABLE ' || szSpfx || 'mtbl_trigger_registry (' ||
        'ID                NUMBER(9),' ||
        'TriggerName       VARCHAR2 (33),' ||
        'ActionType        CHAR(1) CHECK (ActionType IN (''R'', ''T'')),' ||
        'EntryDate         DATE DEFAULT SYSDATE,' ||
        'ProcessDate       DATE,' ||
        'ReProcess         CHAR (1) DEFAULT NULL CHECK (ReProcess IN (''T'', ''F'')),' ||
        'ServerID	         NUMBER(9),' ||
        'WatchNum          NUMBER(5),' ||
        'CardNum	         NUMBER(5),' ||
        'CONSTRAINT mtbl_trigger_registry_pk PRIMARY KEY (ID),' ||
        'CONSTRAINT mtbl_trigger_registry_fk FOREIGN KEY (ServerID) REFERENCES mtbl_trigger_server(ID) ) ' ||
	'TABLESPACE	   MERCATORD ' ||
        'STORAGE (INITIAL 100K NEXT 100K MINEXTENTS 1 MAXEXTENTS 200 PCTINCREASE 0) ' ||
        'PCTFREE    5 ' ||
        'INITRANS  10 ',
                   'Created TABLE          ' || szSpfx || 'mtbl_trigger_registry'); 
   msp_exec_cmd   ('GRANT SELECT, UPDATE, DELETE ON ' || szSpfx || 'mtbl_trigger_registry TO PUBLIC');
   msp_exec_cmd   ('CREATE  PUBLIC SYNONYM trigger_registry FOR ' || szSpfx || 'mtbl_trigger_registry',
                   'Created PUBLIC SYNONYM trigger_registry');

   msp_exec_cmd   (
      'CREATE TABLE ' || szSpfx || 'mtbl_trigger_events (' ||
      		'ID                NUMBER(9), ' ||   
      		'ST_RowID          ROWID , ' ||
      		'CONSTRAINT mtbl_trigger_events_pk PRIMARY KEY (ID, ST_RowID), ' ||
      		'CONSTRAINT mtbl_trigger_events_fk FOREIGN KEY (ID) REFERENCES ' || szSpfx || 'mtbl_trigger_registry(ID) ON DELETE CASCADE ) ' ||
            'ORGANIZATION INDEX PCTTHRESHOLD 10 ' ||
	'TABLESPACE	   MERCATORD ' ||
            'STORAGE (INITIAL 5M NEXT 5M MINEXTENTS 1 MAXEXTENTS 500 PCTINCREASE 0) ' ||
            'PCTFREE    5 ' ||
            'INITRANS  10 ',
                   'Created TABLE          ' || szSpfx || 'mtbl_trigger_events');

   msp_exec_cmd   ('GRANT SELECT, INSERT, DELETE ON ' || szSpfx || 'mtbl_trigger_events TO PUBLIC');
   msp_exec_cmd   ('CREATE  PUBLIC SYNONYM trigger_events FOR ' || szSpfx || 'mtbl_trigger_events',
                   'Created PUBLIC SYNONYM trigger_events'); 


   --
   -- Stored Procedures
   --
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_register1 (' ||
         'nNewEventID   OUT   NUMBER,' ||
   		'szTriggerName IN    Trigger_Registry.TriggerName%TYPE,' ||
   		'cActionType 	IN    CHAR 		   DEFAULT ''R'', ' ||
   		'nOldEventID   IN    NUMBER      DEFAULT 0, ' ||
   		'nServerID 		IN    NUMBER      DEFAULT NULL, ' ||
   		'nWatchNum 		IN    NUMBER      DEFAULT NULL, ' ||
   		'nCardNum 		IN    NUMBER      DEFAULT NULL ) AS ' ||
      'BEGIN ' ||
         'SAVEPOINT a; ' ||
         'nNewEventID := -1; ' ||
         'IF cActionType = ''R'' THEN ' ||
            'LOCK TABLE Trigger_Registry IN EXCLUSIVE MODE; ' ||
         'END IF; ' ||
         'INSERT INTO Trigger_Registry (TriggerName, ActionType, ServerID, WatchNum, CardNum, ID) ' ||
         'VALUES (szTriggerName, cActionType, nServerID, nWatchNum, nCardNum, mseq_registry_id.NEXTVAL); ' ||
         'SELECT mseq_registry_id.CURRVAL ' ||
           'INTO nNewEventID ' ||
           'FROM DUAL; ' ||
         'IF nOldEventID <> 0 THEN ' ||
            'UPDATE Trigger_Registry ' ||
               'SET ProcessDate = SYSDATE ' ||
             'WHERE ID = nOldEventID; ' ||
         'END IF; ' ||
         'DBMS_ALERT.REGISTER(''MERCTRIG_'' || nNewEventID); ' ||
      'EXCEPTION ' ||
         'WHEN OTHERS THEN ' ||
            'ROLLBACK TO a; ' ||
            'RAISE; ' ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_register1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_register1 TO PUBLIC');
   msp_exec_cmd   ('CREATE  PUBLIC SYNONYM msp_register FOR ' || szSpfx || 'msp_register1', 
                   'Created PUBLIC SYNONYM msp_register');

   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_get_server_id1 (' ||
                                       'nID      OUT   NUMBER,' ||
                                       'szName   IN    Trigger_Server.Name%TYPE) AS ' ||
      'BEGIN ' ||
         'SELECT   ID ' ||
         'INTO     nID ' ||
         'FROM     Trigger_Server ' ||
         'WHERE    Name      = szName; ' ||
      'EXCEPTION ' ||
         'WHEN NO_DATA_FOUND THEN ' ||
            'nID := -1; ' ||
      'END; ',
                   'Created PROCEDURE      ' || szSpfx || 'msp_get_server_id1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_get_server_id1 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_get_server_id FOR ' || szSpfx || 'msp_get_server_id1',
                   'Created PUBLIC SYNONYM msp_get_server_id');

   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_register_srvr1 (' ||
                                    'nSrvrID   OUT  Trigger_Server.ID%TYPE,' ||
                                    'szName    IN   Trigger_Server.Name%TYPE,' ||
                                    'szAddress IN   Trigger_Server.Address%TYPE) AS ' ||
      'BEGIN ' ||
         'msp_get_server_id (nSrvrID, szName);' ||
         'IF nSrvrID > 0 THEN ' ||
            'UPDATE Trigger_Server ' ||
            'SET    StartupDate = SYSDATE,' ||
                   'Address     = szAddress ' ||
            'WHERE  ID = nSrvrID; ' ||
            'RETURN; ' ||
         'END IF; ' ||
         'INSERT ' ||
           'INTO Trigger_Server (ID, Name, Address) ' ||
         'VALUES                (mseq_server_id.NEXTVAL, szName, szAddress); ' ||
         'SELECT mseq_server_id.CURRVAL ' ||
           'INTO nSrvrID ' ||
           'FROM DUAL; ' ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_register_srvr1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_register_srvr1 TO PUBLIC');
   msp_exec_cmd   ('CREATE  PUBLIC SYNONYM msp_register_srvr FOR ' || szSpfx || 'msp_register_srvr1',
                   'Created PUBLIC SYNONYM msp_register_srvr');

   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_shutdown_srvr1 (nID IN Trigger_Server.ID%TYPE) AS ' ||
      'BEGIN  ' ||
         'UPDATE Trigger_Server ' ||
         'SET    ShutdownDate = SYSDATE ' ||
         'WHERE  ID = nID; ' ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_shutdown_srvr1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_shutdown_srvr1 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_shutdown_srvr FOR ' || szSpfx || 'msp_shutdown_srvr1',
                   'Created PUBLIC SYNONYM msp_shutdown_srvr');

   -- Validates if name of a trigger is already defined for the target
   -- table.
   --
   -- RETURN:  
   --         -1     General Error
   --          0     Duplicate Trigger Name exists.
   --       NULL     If no existing trigger name and no existing table trigger of cEventType
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_trig_exists1 (' ||
                                    'nResult      OUT   NUMBER, ' ||
                                    'szTrigger    IN    Trigger_Catalog.TriggerName%TYPE, ' ||
                                    'szTable      IN    Trigger_Catalog.TableName%TYPE, ' ||
                                    'szTblSchema  IN    Trigger_Catalog.TableSchema%TYPE DEFAULT NULL) AS ' ||
         'cExists CHAR; ' ||
      'BEGIN ' ||
         'cExists := ''0''; ' ||
         'SELECT   ''1'' ' ||
         'INTO     cExists ' ||
         'FROM     ALL_TRIGGERS ' ||
         'WHERE    TRIGGER_NAME = UPPER(szTrigger); ' ||
         'nResult := 0; ' ||
      'EXCEPTION ' ||
         'WHEN NO_DATA_FOUND THEN ' ||
            'nResult := NULL; ' ||
         'WHEN OTHERS THEN ' ||
            'nResult := -1; ' ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_trig_exists1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_trig_exists1 TO PUBLIC');
   msp_exec_cmd   ('CREATE  PUBLIC SYNONYM msp_trig_exists FOR ' || szSpfx || 'msp_trig_exists1',
                   'Created PUBLIC SYNONYM msp_trig_exists');

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

   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_wait_event1 ('   ||
                                       'nID       IN    INTEGER, '     ||
                                       'nStatus   OUT   INTEGER) AS '   ||
         'szMsg VARCHAR2(256); ' ||
      'BEGIN ' ||
         'dbms_alert.waitone(''MERCTRIG_'' || nID, szMsg, nStatus); ' ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_wait_event1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_wait_event1 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_wait_event FOR ' || szSpfx || 'msp_wait_event1',
                   'Created PUBLIC SYNONYM msp_wait_event');

   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_signal_event1 ('   ||
                                 		'nID   IN    INTEGER) AS ' ||
      'BEGIN '                               ||
         'dbms_alert.signal(''MERCTRIG_'' || nID, ''FROM_MERCATOR''); '   ||
         'COMMIT; '                          ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_signal_event1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_signal_event1 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_signal_event FOR ' || szSpfx || 'msp_signal_event1',
                   'Created PUBLIC SYNONYM msp_signal_event');

   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_remove_event1 ('   ||
                                       'nID      IN    INTEGER) AS '     ||
      'BEGIN ' ||
         'dbms_alert.remove(''MERCTRIG_'' || nID); ' ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_remove_event1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_remove_event1 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_remove_event FOR ' || szSpfx || 'msp_remove_event1',
                   'Created PUBLIC SYNONYM msp_remove_event');


   --
   -- Event triggering function.
   --
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_create_trigger1 ('  ||
                                       'szTrigName    IN   VARCHAR2, '     ||
                                       'szTblSchema   IN   VARCHAR2, '     ||
                                       'szTblName     IN   VARCHAR2, '     ||
                                       'szEvntParms   IN   VARCHAR2, '     ||
                                       'cEventType    IN   CHAR, '     ||
                                       'cActionType   IN   CHAR) AS '  ||
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
                              '''ON '' || szTblSchema || ''.'' || szTblName || szRowClause || szTrigBody; ' || 
         'dbms_sql.parse (cid, szTrigCmd, dbms_sql.native); ' ||
         'dbms_sql.close_cursor (cid); ' ||
      'EXCEPTION ' ||
         'WHEN OTHERS THEN '  ||
            'dbms_sql.close_cursor(cid); ' ||
            'raise_application_error(-20102, ''Mercator Trigger Definition Failure. '' || szTrigCmd, TRUE); ' ||
            'RAISE; '                      ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_create_trigger1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_create_trigger1 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_create_trigger FOR ' || szSpfx || 'msp_create_trigger1',
                   'Created PUBLIC SYNONYM msp_create_trigger');

   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_drop_trigger1 ('    ||
                                       'szTrigName    IN   VARCHAR2) AS '  ||
         'cid           INTEGER; '        ||
         'szTrigCmd     VARCHAR2(256); '  ||
      'BEGIN '                            ||
         'szTrigCmd  := ''DROP TRIGGER '' || ''' || szSpfx || ''' || szTrigName ' || ';' ||
         'cid := dbms_sql.open_cursor; ' ||
         'dbms_sql.parse (cid, szTrigCmd, dbms_sql.native); ' ||
         'dbms_sql.close_cursor (cid); ' ||
      'EXCEPTION ' ||
         'WHEN OTHERS THEN '  ||
            'dbms_sql.close_cursor(cid); ' ||
            'RAISE; '                      ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_drop_trigger1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_drop_trigger1 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_drop_trigger FOR ' || szSpfx || 'msp_drop_trigger1',
                   'Created PUBLIC SYNONYM msp_drop_trigger');

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
   --
   -- If re-installing to a DIFFERENT schema, then comment out all the previous
   -- CREATE commands by simply surrounding them with comment characters.
   -- See above for the opening comment.
   -- */

END;
/

