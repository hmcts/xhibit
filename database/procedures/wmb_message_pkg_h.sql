SET serveroutput on
SET echo on

CREATE OR REPLACE PACKAGE wmb_message_pkg AS
    PROCEDURE send_wmb_message(p_schema_name IN VARCHAR2,
                               p_table_name IN VARCHAR2,
                               p_column_name IN VARCHAR2,
                               p_column_value IN VARCHAR2,
                               p_operation IN VARCHAR2,
                               p_row_id IN NUMBER);

    FUNCTION create_wmb_message_xml(p_schema_name IN VARCHAR2,
                                    p_table_name IN VARCHAR2,
                                    p_column_name IN VARCHAR2,
                                    p_column_value IN VARCHAR2,
                                    p_operation IN VARCHAR2,
                                    p_row_id IN NUMBER) RETURN VARCHAR2;
END wmb_message_pkg;
/

show errors;
