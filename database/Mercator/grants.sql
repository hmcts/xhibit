/*
 * Author:      Nick Sawyer
 *
 * Description: Grants for user
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Nick Sawyer            Initial revision
 * 0.2         Nick Sawyer            Added create view privilege
 *
 */

GRANT EXECUTE ON DBMS_ALERT TO XHIBIT;
GRANT EXECUTE ON DBMS_SQL   TO XHIBIT;
GRANT CREATE ANY TRIGGER    TO XHIBIT;
GRANT DROP   ANY TRIGGER    TO XHIBIT;
GRANT CREATE PUBLIC SYNONYM TO XHIBIT;
GRANT DROP   PUBLIC SYNONYM TO XHIBIT;
GRANT CREATE VIEW           TO XHIBIT;
