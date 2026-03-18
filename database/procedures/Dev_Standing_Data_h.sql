CREATE OR REPLACE PACKAGE XHB_STANDING_DATA_PKG AS

    -- These functions are required to be publicly accessible as they are
    -- called from SQL statements...
    FUNCTION lookup_xhb_address_id(p_id_in IN NUMBER) RETURN NUMBER;
    FUNCTION lookup_xhb_ref_chamber_id(p_id_in IN NUMBER) RETURN NUMBER;
    FUNCTION lookup_xhb_ref_legal_rep_id(p_id_in IN NUMBER) RETURN NUMBER;
    FUNCTION lookup_xhb_ref_court_rep_f_id(p_id_in IN NUMBER) RETURN NUMBER;
    FUNCTION lookup_xhb_ref_disp_menu_id(p_id_in IN NUMBER) RETURN NUMBER;


    PROCEDURE delete_court_standing_data(p_court_id_in IN NUMBER);

    PROCEDURE add_court_standing_data(p_court_name_in IN VARCHAR2 );

    PROCEDURE add_court_standing_data(p_court_id_in   IN NUMBER,
                                      p_court_name_in IN VARCHAR2);

    PROCEDURE add_public_display(p_court_id_in IN NUMBER);

    PROCEDURE add_reference_data(p_court_id_in IN NUMBER);


    -- Included only for testing purposes...
    PROCEDURE DELETE_REFERENCE_DATA(p_court_id_in IN NUMBER);
END XHB_STANDING_DATA_PKG;
/
show errors