CREATE OR REPLACE PACKAGE BODY gdg_scjse_gateway_common_pkg AS

    PROCEDURE GET_CONFIG_PROPERTIES(p_results_out OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_results_out FOR

            SELECT PROPERTY_ID ID,
                   PROPERTY_CODE,
                   PROPERTY_NAME,
                   PROPERTY_VALUE,
                   PROPERTY_TIMESTAMP
            FROM   GDG_CONFIG_PROPERTIES;
    END GET_CONFIG_PROPERTIES;

    PROCEDURE GET_CONFIG_PROPERTY_BY_CODE(p_results_out OUT SYS_REFCURSOR
                                         ,p_property_code_in IN gdg_config_properties.property_code%type) IS
    BEGIN
        OPEN p_results_out FOR
            SELECT PROPERTY_ID ID,
                   PROPERTY_CODE,
                   PROPERTY_NAME,
                   PROPERTY_VALUE,
                   PROPERTY_TIMESTAMP
            FROM   GDG_CONFIG_PROPERTIES
            WHERE  property_code         = p_property_code_in;
    END GET_CONFIG_PROPERTY_BY_CODE;
    
END gdg_scjse_gateway_common_pkg;
/
show error;



 