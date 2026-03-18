create or replace PACKAGE XHB_REF_CHAMBER_PKG AS

  -- Get next CrestChamberId
  FUNCTION get_next_crest_chamber_id RETURN XHB_REF_CHAMBER.CREST_CHAMBER_ID%TYPE;
  
  PROCEDURE populate_ref_chamber (p_ref_chamber_id  IN xhb_ref_chamber.ref_chamber_id%TYPE
                                ,p_obs_ind           IN xhb_ref_chamber.obs_ind%TYPE
                                ,p_is_global        IN xhb_ref_chamber.is_global%TYPE
                                ,p_dx_ref           IN xhb_ref_chamber.dx_ref%TYPE
                                ,p_location_code    IN  xhb_ref_chamber.location_code%TYPE
                                ,p_crest_chamber_id IN  xhb_ref_chamber.crest_chamber_id%TYPE
                                ,p_firm_name        IN  xhb_ref_chamber.firm_name%TYPE
                                ,p_address_id       IN  xhb_ref_chamber.address_id%TYPE
                                ,p_clerk_name       IN  xhb_ref_chamber.clerk_name%TYPE
                                ,p_user_name        IN xhb_ref_chamber.created_by%TYPE);

  END XHB_REF_CHAMBER_PKG;
/
show errors