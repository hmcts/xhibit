CREATE OR REPLACE PACKAGE xhb_custom_pkg AS

  FUNCTION is_connection_pool_user RETURN NUMBER;
 
  FUNCTION is_audit_required(table_name IN VARCHAR2) RETURN NUMBER;

  FUNCTION get_ref_judge_id (arg0 IN NUMBER) RETURN NUMBER;

END xhb_custom_pkg;
/
show errors
