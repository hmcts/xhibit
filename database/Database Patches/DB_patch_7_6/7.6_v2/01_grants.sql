/*
 * Author:      Abbas Hussain
 *
 * Description: Grants for user
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Abbas Hussain            Initial revision
 *
 */

GRANT EXECUTE ON DBMS_ALERT TO cjit;
GRANT EXECUTE ON DBMS_SQL   TO cjit;
GRANT CREATE ANY TRIGGER    TO cjit;
GRANT DROP   ANY TRIGGER    TO cjit;
GRANT CREATE PUBLIC SYNONYM TO cjit;
GRANT DROP   PUBLIC SYNONYM TO cjit;
GRANT CREATE VIEW           TO cjit;
GRANT EXECUTE ON DBMS_TRANSACTION TO cjit;