create user xhibit 
identified by xhibit
default tablespace users
temporary tablespace temp
quota unlimited on users;

grant resource, create session to xhibit;
grant query rewrite to xhibit;
grant select on dba_synonyms to xhibit;

GRANT EXECUTE ON DBMS_ALERT TO XHIBIT;
GRANT EXECUTE ON DBMS_SQL   TO XHIBIT;
GRANT CREATE ANY TRIGGER    TO XHIBIT;
GRANT DROP   ANY TRIGGER    TO XHIBIT;
GRANT CREATE PUBLIC SYNONYM TO XHIBIT;
GRANT DROP   PUBLIC SYNONYM TO XHIBIT;
GRANT CREATE VIEW           TO XHIBIT;

