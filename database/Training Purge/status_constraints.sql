---------------------------------------------------------------------------------------
----NAME: Procedure for enabling or disabling constraints                          ----
----DATE: 09 May 2003                                                              ----
----PROJECT: XHIBIT2                                                               ----
----AUTHOR: S Sangha                                                               ----
---------------------------------------------------------------------------------------

PROMPT 
PROMPT Creating Procedure SWITCH_CONSTRAINTS
CREATE OR REPLACE PROCEDURE SWITCH_CONSTRAINTS(
  pAble IN VARCHAR2 )
IS

vPKName VARCHAR2(80);
tblName VARCHAR2(80);
curOpen INTEGER;
errExecute INTEGER;

CURSOR curTbl IS
  SELECT table_name 
    FROM user_tables
  WHERE table_name like 'XHB%'
  and table_name not in ('XHB_SYS_AUDIT', 'XHB_SYS_USER_INFORMATION')
  and table_name in (SELECT table_name from USER_CONSTRAINTS WHERE constraint_type='P');

CURSOR curFK(pcPKName IN VARCHAR2) IS
  SELECT constraint_name, table_name
    FROM user_constraints
   WHERE r_constraint_name = pcPKName;


BEGIN

  FOR tbl IN curTbl LOOP

    tblName := tbl.table_name;

    BEGIN
      SELECT constraint_name INTO vPKName FROM user_constraints
        WHERE table_name = tblName
        AND constraint_type = 'P';
    END;

    FOR fk IN curFK(vPKName) LOOP
      curOpen  := dbms_sql.open_cursor;
      dbms_sql.parse(curOpen,'ALTER TABLE '||fk.table_name||' '
			||pAble||' CONSTRAINT '||fk.constraint_name, 1);
      errExecute  := dbms_sql.execute(curOpen);
      dbms_sql.close_cursor(curOpen);
    END LOOP;

END LOOP;

END SWITCH_CONSTRAINTS;
/                                              