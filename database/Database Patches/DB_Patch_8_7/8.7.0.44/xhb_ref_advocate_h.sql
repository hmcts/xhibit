CREATE OR REPLACE PACKAGE xhb_ref_advocate_pkg AS

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
END xhb_ref_advocate_pkg;
/
show errors