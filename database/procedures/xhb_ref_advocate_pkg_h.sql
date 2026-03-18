create or replace PACKAGE xhb_ref_advocate_pkg AS

  PROCEDURE find_by_crest_chamber_id(p_count_out OUT NUMBER, p_crest_chamber_id IN NUMBER);

  PROCEDURE xhb_comp_ref_advocate(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE xhb_comp_ref_advocate2(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE update_chamber_ref(p_crest_advocate_id  XHB_REF_ADVOCATE.CREST_ADVOCATE_ID%TYPE
                             , p_new_chamber_id     XHB_REF_ADVOCATE.CREST_CHAMBER_ID%TYPE
                             , p_user               XHB_REF_LEGAL_REPRESENTATIVE.LAST_UPDATED_BY%TYPE
						      );

  PROCEDURE update_counsel_details(p_ref_advocate_id  XHB_REF_ADVOCATE.REF_ADVOCATE_ID%TYPE
                                ,  p_first_name       XHB_REF_LEGAL_REPRESENTATIVE.FIRST_NAME%TYPE
                                ,  p_middle_name      XHB_REF_LEGAL_REPRESENTATIVE.MIDDLE_NAME%TYPE
                                ,  p_surname          XHB_REF_LEGAL_REPRESENTATIVE.SURNAME%TYPE
                                ,  p_title            XHB_REF_LEGAL_REPRESENTATIVE.TITLE%TYPE
                                ,  p_initials         XHB_REF_LEGAL_REPRESENTATIVE.INITIALS%TYPE
                                ,  p_legal_rep_type   XHB_REF_LEGAL_REPRESENTATIVE.LEGAL_REP_TYPE%TYPE
                                ,  p_year_of_call     XHB_REF_ADVOCATE.YEAR_OF_CALL%TYPE
                                ,  p_vat_no           XHB_REF_ADVOCATE.VAT_NO%TYPE
                                ,  p_bar_no           XHB_REF_ADVOCATE.BAR_NO%TYPE
                                ,  p_honours          XHB_REF_ADVOCATE.HONOURS%TYPE
                                ,  p_adv_type_ind     XHB_REF_ADVOCATE.ADV_TYPE_IND%TYPE
								,  p_user_name        XHB_REF_ADVOCATE.LAST_UPDATED_BY%TYPE
                                  );

  PROCEDURE insert_counsel_details(p_ref_advocate_id  XHB_REF_ADVOCATE.REF_ADVOCATE_ID%TYPE
                                ,  p_first_name       XHB_REF_LEGAL_REPRESENTATIVE.FIRST_NAME%TYPE
                                ,  p_middle_name      XHB_REF_LEGAL_REPRESENTATIVE.MIDDLE_NAME%TYPE
                                ,  p_surname          XHB_REF_LEGAL_REPRESENTATIVE.SURNAME%TYPE
                                ,  p_title            XHB_REF_LEGAL_REPRESENTATIVE.TITLE%TYPE
                                ,  p_initials         XHB_REF_LEGAL_REPRESENTATIVE.INITIALS%TYPE
                                ,  p_legal_rep_type   XHB_REF_LEGAL_REPRESENTATIVE.LEGAL_REP_TYPE%TYPE  -- is the same as advocate_type, S = Solicitor, A = Advocate (Barrister)
                                ,  p_year_of_call     XHB_REF_ADVOCATE.YEAR_OF_CALL%TYPE
                                ,  p_vat_no           XHB_REF_ADVOCATE.VAT_NO%TYPE
                                ,  p_bar_no           XHB_REF_ADVOCATE.BAR_NO%TYPE
                                ,  p_honours          XHB_REF_ADVOCATE.HONOURS%TYPE
                                ,  p_adv_type_ind     XHB_REF_ADVOCATE.ADV_TYPE_IND%TYPE      -- Effectively a QC flag [Y/N]
								,  p_chamber_ref_no   XHB_REF_ADVOCATE.CREST_CHAMBER_ID%TYPE  -- The CREST Chamber ID is the same for every court.
                                ,  p_user             XHB_REF_LEGAL_REPRESENTATIVE.LAST_UPDATED_BY%TYPE
                                  );

  PROCEDURE delete_counsel_details(p_crest_advocate_id  XHB_REF_ADVOCATE.CREST_ADVOCATE_ID%TYPE
                              ,  p_user               XHB_REF_LEGAL_REPRESENTATIVE.LAST_UPDATED_BY%TYPE);

  PROCEDURE update_chamber_details(p_crest_chamber_id  XHB_REF_CHAMBER.CREST_CHAMBER_ID%TYPE
                              ,  p_dx_ref            XHB_REF_CHAMBER.DX_REF%TYPE
                              ,  p_location_code     XHB_REF_CHAMBER.LOCATION_CODE%TYPE
                              ,  p_firm_name         XHB_REF_CHAMBER.FIRM_NAME%TYPE
                              ,  p_address_id        XHB_REF_CHAMBER.ADDRESS_ID%TYPE
                              ,  p_clerk_name        XHB_REF_CHAMBER.CLERK_NAME%TYPE
                              ,  p_address_1         XHB_ADDRESS.ADDRESS_1%TYPE
                              ,  p_address_2         XHB_ADDRESS.ADDRESS_2%TYPE
                              ,  p_address_3         XHB_ADDRESS.ADDRESS_3%TYPE
                              ,  p_address_4         XHB_ADDRESS.ADDRESS_4%TYPE
                              ,  p_town              XHB_ADDRESS.TOWN%TYPE
                              ,  p_county            XHB_ADDRESS.COUNTY%TYPE
                              ,  p_postcode          XHB_ADDRESS.POSTCODE%TYPE
                              ,  p_country           XHB_ADDRESS.COUNTRY%TYPE
                              ,  p_user              XHB_REF_LEGAL_REPRESENTATIVE.LAST_UPDATED_BY%TYPE
							  ,  p_phone_num         XHB_CONTACT_DETAIL.CONTACT_VALUE%TYPE
                              ,  p_fax_num           XHB_CONTACT_DETAIL.CONTACT_VALUE%TYPE
                              ,  p_email             XHB_CONTACT_DETAIL.CONTACT_VALUE%TYPE
                              ,  p_secure_email      XHB_CONTACT_DETAIL.CONTACT_VALUE%TYPE);

  PROCEDURE update_contact_detail(p_address_id    XHB_ADDRESS.ADDRESS_ID%TYPE
                              , p_contact_type  XHB_CONTACT_DETAIL.CONTACT_TYPE%TYPE
                              , p_contact_value XHB_CONTACT_DETAIL.CONTACT_VALUE%TYPE
                              , p_user          XHB_CONTACT_DETAIL.LAST_UPDATED_BY%TYPE);
END xhb_ref_advocate_pkg;
/
show errors