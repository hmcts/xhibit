CREATE OR REPLACE PACKAGE gdg_scjse_gateway_common_pkg AS

    /*
    * Get Config Properties
    */
    PROCEDURE GET_CONFIG_PROPERTIES(p_results_out OUT SYS_REFCURSOR);

    /*
    * Get Config Properties By Property Code
    */
    PROCEDURE GET_CONFIG_PROPERTY_BY_CODE(p_results_out OUT SYS_REFCURSOR
                                         ,p_property_code_in IN gdg_config_properties.property_code%type);

END gdg_scjse_gateway_common_pkg;
/
show errors;

