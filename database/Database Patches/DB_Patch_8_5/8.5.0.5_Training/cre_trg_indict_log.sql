CREATE OR REPLACE TRIGGER X2_INDICTMENT_LOG_TRIGGER
  BEFORE INSERT OR UPDATE OR DELETE
  ON INDICTMENT_LOG
  FOR EACH ROW
DECLARE
  l_xhibit_court  VARCHAR2(1);
  l_case_type     VARCHAR2(1) := NULL;
  l_case_no       NUMBER(8)   := NULL;
  m_case_type     VARCHAR2(1) := NULL;
  m_case_no       NUMBER(8)   := NULL;
BEGIN

/******************************************************************************
   NAME:       X2_INDICTMENT_LOG_TRIGGER
   PURPOSE:    To Insert/Update x2_refresh_data_audit_log table for XHIBIT when
               any changes occur to the Indictment_log table.
   REVISIONS:
   Ver        Date               Author           Description
   ---------  ----------       ---------------  --------------------------------
----
   1.0        21-April-2011    D Field          Creation
*******************************************************************************/

  SELECT xhibit_court
  INTO   l_xhibit_court
  FROM   home_court;

  IF l_xhibit_court = 'Y' THEN --Record audit history only for an xhibit court.
    IF INSERTING THEN
      l_case_type := :NEW.case_type;
      l_case_no := :NEW.case_no;
    ELSE
      l_case_type := :OLD.case_type;
      l_case_no := :OLD.case_no;
    END IF;

    SELECT  case_type
    ,       case_no
    INTO    m_case_type
    ,       m_case_no
    FROM    X2_REFRESH_DATA_AUDIT_LOG
    WHERE   case_type = l_case_type
    AND     case_no = l_case_no;

    IF l_case_type = m_case_type AND l_case_no = m_case_no THEN
      UPDATE X2_REFRESH_DATA_AUDIT_LOG
      SET    update_date = sysdate
      WHERE  case_no = l_case_no
      AND case_type = l_case_type;
    END IF;

  END IF;

EXCEPTION

  WHEN NO_DATA_FOUND THEN
    INSERT INTO X2_REFRESH_DATA_AUDIT_LOG
    (case_type
    ,case_no
    ,update_date
    )
    VALUES
    (l_case_type
    ,l_case_no
    ,sysdate
    );
 END;

/

