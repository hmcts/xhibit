SET TERM OFF
/*
 * Filename:    XHIBIT2_Create_Audit_Tables_Prod.sql
 *
 * Author:      Nick Sawyer
 *
 * Description: Creates all AUDIT (AUD_) tables for XHIBIT2
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Nick Sawyer            Initial revision
 *
 */
SET TERM ON

DECLARE

  -- Dynamic SQL to create audit tables as below

  l_xhb_table  varchar2(255);
  l_aud_table  varchar2(255);
  l_sqlCmd1    varchar2(255) := ' CREATE TABLE ';
  l_sqlCmd3    varchar2(255) := ' AS SELECT * FROM ';
  l_sqlCmd2    varchar2(255) := ' TABLESPACE AUDITD ';
  l_sqlCmdFull varchar2(500);

  CURSOR c_xhb_tables IS
    SELECT table_name
    FROM   user_tables
    WHERE  table_name LIKE 'XHB%'
    AND    table_name NOT IN ('XHB_SYS_AUDIT',
                              'XHB_SYS_USER_INFORMATION',
                              'XHB_VERSION');

BEGIN

  FOR record in c_xhb_tables LOOP

    IF c_xhb_tables%FOUND THEN

      l_xhb_table := record.table_name;
      l_aud_table := replace(record.table_name, 'XHB', 'AUD');
      l_sqlCmdFull := l_sqlCmd1||l_aud_table||l_sqlCmd2||l_sqlCmd3||l_xhb_table;

--      DBMS_OUTPUT.PUT_LINE(l_sqlCmdFull);

      EXECUTE IMMEDIATE l_sqlCmdFull;

    END IF;

  END LOOP;

END;
/
