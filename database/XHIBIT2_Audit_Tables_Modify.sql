SET TERM OFF
/*
 * Filename:    XHIBIT2_Audit_Tables_Modify.sql
 *
 * Author:      Nick Sawyer
 *
 * Description: Script to alter each of the audit tables to add a column that
 *              will be populated as usual when the main table update trigger
 *              is fired.  The column will indicated the action that caused the
 *              trigger to fire, either an UPDATE or DELETE from the main table.
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Nick Sawyer            Initial revision
 *
 */
SET TERM ON

set serveroutput on
execute dbms_output.enable(100000)

prompt Adding columns to autit tables to indicate UPDATE/DELETED on main table

DECLARE

  /*
   * Cursor to select the audit tables that require the additional column.
   */

  CURSOR c_audit_tables is SELECT table_name
                           FROM   user_tables
                           WHERE  table_name like 'AUD_%';

  l_audit_table VARCHAR2(128);
  l_cmd_start   VARCHAR2(128) := ' ALTER TABLE ';
  l_cmd_end     VARCHAR2(128) := ' ADD (INSERT_EVENT VARCHAR2(1) NOT NULL)';
  l_cmd_full    VARCHAR2(512);

BEGIN

  OPEN c_audit_tables;

  LOOP

    FETCH c_audit_tables
    INTO  l_audit_table;

    EXIT WHEN c_audit_tables%NOTFOUND;

    IF l_audit_table = 'AUD_DISPLAY_COURT_ROOM' THEN

      l_cmd_full := 'ALTER TABLE AUD_DISPLAY_COURT_ROOM ADD (MODIFYING_USER VARCHAR2(30) DEFAULT ''X'' NOT NULL, INSERT_EVENT VARCHAR2(1) DEFAULT ''X'' NOT NULL)';

      EXECUTE IMMEDIATE l_cmd_full;

    ELSE

--      DBMS_OUTPUT.PUT_LINE('Adding INSERT_EVENT column to table: '||l_audit_table);

      l_cmd_full := l_cmd_start||l_audit_table||l_cmd_end;

--      DBMS_OUTPUT.PUT_LINE(l_cmd_full);

      EXECUTE IMMEDIATE l_cmd_full;

    END IF;

  END LOOP;

  CLOSE c_audit_tables;

END;
/
