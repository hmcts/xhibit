CREATE OR REPLACE PACKAGE exi_custom_pkg AS

  FUNCTION is_connection_pool_user RETURN NUMBER;

  FUNCTION is_audit_required(table_name IN VARCHAR2) RETURN NUMBER;
  
END exi_custom_pkg;
/

show errors;