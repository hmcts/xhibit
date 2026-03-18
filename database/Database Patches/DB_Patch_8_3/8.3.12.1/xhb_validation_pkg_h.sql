SET serveroutput on
SET echo on

CREATE OR REPLACE PACKAGE xhb_validation_pkg AS
    FUNCTION request_validation(p_validation_id IN NUMBER,
                                p_schema_name IN VARCHAR2,
                                p_clob_data IN CLOB) RETURN RAW;

    FUNCTION update_validation(p_validation_id IN NUMBER,
                                p_status IN VARCHAR2,
                                p_details IN VARCHAR2) RETURN NUMBER;
END xhb_validation_pkg;
/

SHOW errors;
