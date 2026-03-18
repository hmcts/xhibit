SET TERM OFF
/*
 * Filename:    XHIBIT2_Create_Public_Synonyms.sql
 *
 * Author:      Nick Sawyer
 *
 * Description: Creates PUBLIC sysnonyms
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Nick Sawyer            Initial revision
 *
 */
SET TERM ON

CONNECT system/&&system_password;

set serveroutput on
execute dbms_output.enable(100000)

prompt If on Production / PreProduction or Test environment enter Schema Owner
prompt If on a Development environment then leave blank
accept p_schema_owner prompt 'Please enter schema owner : '

DECLARE

  -- Dynamic SQL to create PUBLIC synonyms and grants for tables

  l_owner       VARCHAR2(255);
  l_org_table   VARCHAR2(255);
  l_sqlCmd1     VARCHAR2(255) := ' CREATE PUBLIC SYNONYM ';
  l_sqlCmd2     VARCHAR2(255) := ' FOR ';
  l_sqlCmd3     VARCHAR2(255) := '.';
  l_sqlCmd4     VARCHAR2(255) := 'GRANT SELECT,UPDATE,DELETE,INSERT ON ';
  l_sqlCmd5     VARCHAR2(255) := ' TO PUBLIC ';
  l_sqlCmdSyn   VARCHAR2(500);
  l_sqlCmdGrant VARCHAR2(500);

  CURSOR c_tables IS
    SELECT table_name
    FROM   dba_tables
    WHERE  owner = UPPER('&p_schema_owner');

BEGIN

  FOR record IN c_tables LOOP

    IF c_tables%FOUND THEN

      l_org_table := record.table_name;
      l_owner := UPPER('&p_schema_owner');

      l_sqlCmdSyn := l_sqlCmd1||l_org_table||l_sqlCmd2||l_owner||l_sqlCmd3||l_org_table;

--      DBMS_OUTPUT.PUT_LINE(l_sqlCmdSyn);

      EXECUTE IMMEDIATE l_sqlCmdSyn;

      l_sqlCmdGrant := l_sqlCmd4||l_org_table||l_sqlCmd5;

--      DBMS_OUTPUT.PUT_LINE(l_sqlCmdGrant);

      EXECUTE IMMEDIATE l_sqlCmdGrant;

    END IF;

  END LOOP;

END;
/
