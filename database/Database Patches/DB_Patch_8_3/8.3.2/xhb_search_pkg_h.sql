-------------------------------------------------------------------------------
-- THE PACKAGE HEADER
-- The xhb_search_pkg contains all of the procedures used by the fast
-- lane readers.
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE xhb_search_pkg AS
    PROCEDURE get_court(p_results_out       OUT SYS_REFCURSOR,
                        p_circuit_in        IN  XHB_COURT.circuit%TYPE,
                        p_court_site_id_in  IN  XHB_COURT_SITE.court_site_id%TYPE,
                        p_court_name_in     IN  XHB_COURT.court_name%TYPE,
                        p_court_prefix_in   IN  XHB_COURT.court_prefix%TYPE,
                        p_court_type_in     IN  XHB_COURT.court_type%TYPE,
                        p_crest_court_id_in IN  XHB_COURT.crest_court_id%TYPE,
                        p_short_name_in     IN  XHB_COURT.short_name%TYPE);


    PROCEDURE get_court_room(p_results_out            OUT SYS_REFCURSOR,
                             p_court_room_name_in     IN  XHB_COURT_ROOM.court_room_name%TYPE,
                             p_court_site_code_in     IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_site_id_in       IN  XHB_COURT_SITE.court_site_id%TYPE,
                             p_crest_court_room_no_in IN  XHB_COURT_ROOM.crest_court_room_no%TYPE,
                             p_short_name_in          IN  XHB_COURT.short_name%TYPE);


    PROCEDURE get_court_site(p_results_out         OUT SYS_REFCURSOR,
                             p_court_site_code_in  IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_id_in         IN  XHB_COURT.court_id%TYPE,
                             p_court_short_name_in IN  XHB_COURT.short_name%TYPE,
                             p_court_site_name_in  IN  XHB_COURT_SITE.court_site_name%TYPE);


    PROCEDURE get_ref_advocate_complex(p_results_out         OUT SYS_REFCURSOR,
                                       p_adv_type_ind_in     IN  XHB_REF_ADVOCATE.adv_type_ind%TYPE,
                                       p_initials_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                                       p_first_name_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                       p_middle_name_in      IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                                       p_surname_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                       p_ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE,
                                       p_firm_name_in        IN  XHB_REF_CHAMBER.firm_name%TYPE,
                                       p_court_id_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
                                       p_defendant_id_in     IN  XHB_DEFENDANT.defendant_id%TYPE := -1,
                                       p_case_id_in          IN  XHB_CASE.case_id%TYPE := -1);


    PROCEDURE get_ref_app_result(p_results_out        OUT SYS_REFCURSOR,
                                 p_court_id_in        IN  XHB_REF_APP_RESULT.court_id%TYPE,
                                 p_app_result_code_in IN  XHB_REF_APP_RESULT.app_result_code%TYPE,
                                 p_ho_code_in         IN  XHB_REF_APP_RESULT.ho_code%TYPE,
                                 p_vary_sentence_in   IN  XHB_REF_APP_RESULT.vary_sentence%TYPE,
                                 p_lesser_off_ind_in  IN  XHB_REF_APP_RESULT.lesser_off_ind%TYPE);


    PROCEDURE get_ref_court(p_results_out         OUT SYS_REFCURSOR,
                            p_court_id_in         IN  XHB_REF_COURT.court_id%TYPE,
                            p_circuit_in          IN  XHB_COURT.circuit%TYPE,
                            p_court_full_name_in  IN  XHB_REF_COURT.court_full_name%TYPE,
                            p_court_prefix_in     IN  XHB_REF_COURT.name_prefix%TYPE,
                            p_court_type_in       IN  XHB_REF_COURT.court_type%TYPE,
                            p_crest_court_id_in   IN  XHB_COURT.crest_court_id%TYPE,
                            p_court_short_name_in IN  XHB_REF_COURT.court_short_name%TYPE,
                            p_is_psd_in           IN  XHB_REF_COURT.is_psd%TYPE);


    PROCEDURE get_ref_court_reporter(p_results_out    OUT SYS_REFCURSOR,
                                     p_court_id_in    IN  XHB_REF_COURT_REPORTER.court_id%TYPE,
                                     p_firm_name_in   IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
                                     p_initials_in    IN  XHB_REF_COURT_REPORTER.initials%TYPE,
                                     p_first_name_in  IN  XHB_REF_COURT_REPORTER.first_name%TYPE,
                                     p_middle_name_in IN  XHB_REF_COURT_REPORTER.middle_name%TYPE,
                                     p_surname_in     IN  XHB_REF_COURT_REPORTER.surname%TYPE);


    PROCEDURE get_ref_hearing_type(p_results_out             OUT SYS_REFCURSOR,
                                   p_hearing_type_code_in    IN  XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
				   p_hearing_type_courtid_in IN  XHB_REF_HEARING_TYPE.court_id%TYPE);


    PROCEDURE get_ref_judge(p_results_out    OUT SYS_REFCURSOR,
                            p_first_name_in  IN  XHB_REF_JUDGE.first_name%TYPE,
                            p_middle_name_in IN  XHB_REF_JUDGE.middle_name%TYPE,
                            p_surname_in     IN  XHB_REF_JUDGE.surname%TYPE,
                            p_court_id_in    IN  XHB_REF_JUDGE.court_id%TYPE);


    PROCEDURE get_ref_justice(p_results_out     OUT SYS_REFCURSOR,
                              p_justice_name_in IN  XHB_REF_JUSTICE.justice_name%TYPE,
                              p_court_id_in     IN  XHB_REF_JUSTICE.court_id%TYPE);


    PROCEDURE get_ref_legal_representative(p_results_out       OUT SYS_REFCURSOR,
                                           p_court_id_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
                                           p_first_name_in     IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                           p_surname_in        IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                           p_legal_rep_type_in IN  XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE);


    PROCEDURE get_ref_offence(p_results_out     OUT SYS_REFCURSOR,
                              p_act_section_in  IN  XHB_REF_OFFENCE.act_section%TYPE,
                              p_court_id_in     IN  XHB_REF_OFFENCE.court_id%TYPE,
                              p_offence_desc_in IN  XHB_REF_OFFENCE.offence_desc%TYPE,
                              p_statute_in      IN  XHB_REF_OFFENCE.statute%TYPE,
                              p_offence_code_in IN  XHB_REF_OFFENCE.offence_code%TYPE,
	                      p_obs_ind_in      IN  XHB_REF_OFFENCE.obs_ind%TYPE,
                              p_bail_act_in     IN  XHB_REF_OFFENCE.bail_act%TYPE := NULL);


    PROCEDURE get_ref_solicitor_firm_complex(p_results_out            OUT SYS_REFCURSOR,
                                             p_solicitor_firm_name_in IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                                             p_crest_sof_id_in        IN  XHB_REF_SOLICITOR_FIRM.crest_sof_id%TYPE,
                                             p_court_id_in            IN  XHB_REF_SOLICITOR_FIRM.court_id%TYPE);


    PROCEDURE get_ref_system_code(p_results_out   OUT SYS_REFCURSOR,
                                  p_court_id_in   IN  XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                  p_code_type_in  IN  XHB_REF_SYSTEM_CODE.code_type%TYPE,
                                  p_de_code_in    IN  XHB_REF_SYSTEM_CODE.de_code%TYPE,
                                  p_code_in       IN  XHB_REF_SYSTEM_CODE.code%TYPE,
                                  p_code_title_in IN  XHB_REF_SYSTEM_CODE.code_title%TYPE);


    PROCEDURE get_solicitor(p_results_out             OUT SYS_REFCURSOR,
                            p_ref_legal_rep_id_in     IN  XHB_REF_SOLICITOR.ref_legal_rep_id%TYPE,
                            p_initials_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                            p_first_name_in           IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                            p_middle_name_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                            p_surname_in              IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                            p_crest_solicitor_name_in IN  XHB_REF_SOLICITOR.crest_solicitor_name%TYPE,
                            p_solicitor_firm_name_in  IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                            p_court_id_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE);
END xhb_search_pkg;
/
show errors