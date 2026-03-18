SET TERM OFF
/*
 * Filename:    XHIBIT2_Create_Packages.sql
 *
 * Author:      Nick Sawyer
 *
 * Description: Creates the xhb_custom_pkg package.
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Nick Sawyer            Initial revision
 *
 */
SET TERM ON

CREATE OR REPLACE PACKAGE xhb_custom_pkg IS

  FUNCTION is_connection_pool_user RETURN NUMBER;
 
  FUNCTION is_audit_required(table_name IN VARCHAR2) RETURN NUMBER;

  FUNCTION get_ref_judge_id (arg0 IN NUMBER) RETURN NUMBER;

END;
/

CREATE OR REPLACE PACKAGE BODY xhb_custom_pkg IS

  FUNCTION is_connection_pool_user RETURN NUMBER IS

    l_conn_user  VARCHAR2(255);
    l_curr_user  VARCHAR2(255);
    no_data      EXCEPTION;
    no_sess_user EXCEPTION;

  BEGIN

    SELECT sys_context('USERENV', 'SESSION_USER')
    INTO   l_curr_user
    FROM   dual;

    IF (l_curr_user IS NULL) THEN

      RAISE no_sess_user;

    END IF;

    SELECT connection_pool_user_name
    INTO   l_conn_user
    FROM   xhb_sys_user_information;

    IF (l_conn_user IS NULL) THEN

      RAISE no_data;

    END IF;

    IF ( l_conn_user != l_curr_user) THEN

      /* Not Conection Pool User */
      RETURN 0;

    ELSE

      /* Connection Pool User */
      RETURN 1;

    END IF;

    EXCEPTION

      WHEN no_sess_user THEN

        DBMS_OUTPUT.PUT_LINE('SYS_CONTEXT DID NOT RETURN SESSION_USER - '||sqlerrm);

        RETURN 1;

      WHEN no_data THEN

        DBMS_OUTPUT.PUT_LINE('XHB_SYS_USER_INFORMATION IS EMPTY - '||sqlerrm);

        RETURN 1;

      WHEN others THEN

        DBMS_OUTPUT.PUT_LINE('ERROR ENCOUNTERED IN IS_CONNECTION_POOL_USER - '||sqlerrm);

        RETURN 1;

  END is_connection_pool_user;

  FUNCTION is_audit_required (table_name IN VARCHAR2) RETURN NUMBER IS

  CURSOR c_audit IS
    SELECT auditable 
    FROM   xhb_sys_audit
    WHERE  table_to_audit = table_name;
  
    l_audit VARCHAR2(1);

  BEGIN

    IF (c_audit%ISOPEN) THEN

      CLOSE c_audit;

    END IF;

    OPEN c_audit;

    FETCH c_audit
    INTO  l_audit;

    CLOSE c_audit;

    IF l_audit = 'Y' THEN

      /* Audit is Required */
      RETURN 1;

    ELSE

      /* Audit is not Required */
      RETURN 0;

    END IF;

  END;

  FUNCTION get_ref_judge_id (arg0 IN NUMBER) RETURN NUMBER IS

    l_ref_judge_id  NUMBER;
    l_ref_judge_id1 NUMBER;

  BEGIN

    SELECT ref_judge_id
    INTO   l_ref_judge_id
    FROM   XHB_SCHED_HEARING_ATTENDEE
    WHERE  attendee_type = 'J'
    AND    scheduled_hearing_id = arg0
    AND    sh_attendee_id = (SELECT MAX(sh_attendee_id)
                             FROM   XHB_SCHED_HEARING_ATTENDEE
                             WHERE  attendee_type = 'J'
                             AND scheduled_hearing_id = arg0 );

    RETURN l_ref_judge_id;

    EXCEPTION

      WHEN NO_DATA_FOUND THEN

        BEGIN

          SELECT ref_judge_id
          INTO   l_ref_judge_id1
          FROM   XHB_SITTING, XHB_SCHEDULED_HEARING
          WHERE  XHB_SITTING.sitting_id = XHB_SCHEDULED_HEARING.sitting_id
          AND    scheduled_hearing_id = arg0;

          RETURN l_ref_judge_id1;

          EXCEPTION

            WHEN NO_DATA_FOUND THEN

              RETURN -1;

        END;

  END;


END;
/
