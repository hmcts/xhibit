-- THIS PROCEDURE REPLACES MERCATORS MSP_RECORD_EVENTS1 CREATED AS PART OF M4ORA8 
-- AND CONTAINS A PATCH THAT ALLOWS TWO EVENTSERVERS TO BE LOAD BALANCED
-- ONLY TO BE USED IN PRE PROD OR LIVE
-- TO BE RUN AFTER M4ORA8

CREATE OR REPLACE PROCEDURE MSP_RECORD_EVENTS1 ( szTrigName Trigger_Registry.TriggerName%TYPE,
                                                 cActionType CHAR,
                                                 nRowId ROWID ) AS

  szSignalEvent VARCHAR2(50);

  nID              	NUMBER;
  nMOD			NUMBER;
  n_Serverid		NUMBER;

  CURSOR watch_events_cursor_MAX IS
    SELECT ID
    FROM   Trigger_Registry
    WHERE  TriggerName = szTrigName
    AND    ProcessDate IS NULL
    AND    SERVERID = (SELECT MAX(SERVERID) FROM Trigger_Registry)
    AND    ActionType = cActionType
    ORDER BY ID;

  CURSOR watch_events_cursor_MIN IS
    SELECT ID
    FROM   Trigger_Registry
    WHERE  TriggerName = szTrigName
    AND    ProcessDate IS NULL
    AND    SERVERID = (SELECT MIN(SERVERID) FROM Trigger_Registry)
    AND    ActionType = cActionType
    ORDER BY ID;

BEGIN

  nMOD :=  mod(to_number(to_char(sysdate, 'ss')),2);

  IF cActionType = 'R' THEN

    LOCK table Trigger_Registry IN ROW SHARE MODE;

  END IF;

  IF nMOD = 0 THEN

    OPEN watch_events_cursor_MAX;

    LOOP

      FETCH watch_events_cursor_MAX
      INTO  nID;

      EXIT WHEN watch_events_cursor_MAX%NOTFOUND;

      BEGIN szSignalEvent := 'MERCTRIG_' || nID;

        INSERT INTO Trigger_Events (id,st_rowid)
        VALUES (nID,nRowId);

        DBMS_ALERT.signal(szSignalEvent, szSignalEvent);

        EXCEPTION WHEN DUP_VAL_ON_INDEX THEN DBMS_ALERT.signal(szSignalEvent, szSignalEvent);

      END;

    END LOOP;

    CLOSE watch_events_cursor_MAX;

  ELSE

    OPEN watch_events_cursor_MIN;

    LOOP

      FETCH watch_events_cursor_MIN
      INTO  nID;

      EXIT WHEN watch_events_cursor_MIN%NOTFOUND;

      BEGIN szSignalEvent := 'MERCTRIG_' || nID;

        INSERT INTO Trigger_Events (id,st_rowid)
        VALUES (nID,nRowId);

        DBMS_ALERT.signal(szSignalEvent, szSignalEvent);

        EXCEPTION WHEN DUP_VAL_ON_INDEX THEN DBMS_ALERT.signal(szSignalEvent, szSignalEvent);

      END;

    END LOOP;

    CLOSE watch_events_cursor_MIN;

  END IF;

END;
/
