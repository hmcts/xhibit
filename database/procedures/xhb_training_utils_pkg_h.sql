CREATE OR REPLACE PACKAGE training_utils_pkg AS
    PROCEDURE get_court_details(p_results_out OUT SYS_REFCURSOR,
                                p_court_id    IN  XHB_COURT.COURT_ID%TYPE);

    PROCEDURE request_xhibit_restore(p_results_out OUT SYS_REFCURSOR,
                                     p_court_id    IN  XHB_COURT.COURT_ID%TYPE);

    PROCEDURE request_mld_restore(p_results_out OUT SYS_REFCURSOR,
                                  p_court_id    IN  XHB_COURT.COURT_ID%TYPE);

    PROCEDURE get_restore_states(p_results_out OUT SYS_REFCURSOR,
                                 p_court_id IN XHB_COURT.COURT_ID%TYPE);

    PROCEDURE restore_xhibit(p_court_id IN XHB_COURT.COURT_ID%TYPE);

    PROCEDURE restore_mld_data(p_court_id IN XHB_COURT.COURT_ID%TYPE);

    PROCEDURE insert_mld_data(p_court_id IN XHB_COURT.COURT_ID%TYPE);

    PROCEDURE check_xhibit_restore_state(p_results_out OUT SYS_REFCURSOR,
                                         p_court_id    IN  XHB_COURT.COURT_ID%TYPE);

    FUNCTION get_ref_justice_id(p_court_id_in         IN XHB_REF_JUSTICE.court_id%TYPE,
                                p_crest_justice_id_in IN XHB_REF_JUSTICE.crest_justice_id%TYPE)
                                RETURN XHB_REF_JUSTICE.ref_justice_id%TYPE;

    FUNCTION get_ref_judge_id(p_court_id_in       IN XHB_REF_JUDGE.court_id%TYPE,
                              p_crest_judge_id_in IN XHB_REF_JUDGE.crest_judge_id%TYPE)
                              RETURN XHB_REF_JUDGE.ref_judge_id%TYPE;

    FUNCTION get_court_site_id(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE)
                               RETURN XHB_COURT_SITE.court_site_id%TYPE;

    FUNCTION get_court_room_id(p_court_id_in            IN XHB_COURT_SITE.court_id%TYPE,
                               p_crest_court_room_no_in IN XHB_COURT_ROOM.crest_court_room_no%TYPE)
                               RETURN XHB_COURT_ROOM.court_room_id%TYPE;

    FUNCTION get_ref_court_id(p_court_id_in         IN XHB_REF_COURT.court_id%TYPE,
                              p_court_short_name_in IN XHB_REF_COURT.court_short_name%TYPE)
                              RETURN XHB_REF_COURT.ref_court_id%TYPE;

    FUNCTION get_ref_hearing_type_id(p_court_id_in          IN XHB_REF_HEARING_TYPE.court_id%TYPE,
                                     p_hearing_type_code_in IN XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
                                     p_case_type_in         IN XHB_CASE.case_type%TYPE)
                                     RETURN XHB_REF_HEARING_TYPE.ref_hearing_type_id%TYPE;

    FUNCTION get_ref_system_code_id(p_court_id_in  IN XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                    p_code_in      IN XHB_REF_SYSTEM_CODE.code%TYPE,
                                    p_code_type_in IN XHB_REF_SYSTEM_CODE.code_type%TYPE)
                                    RETURN XHB_REF_SYSTEM_CODE.ref_system_code_id%TYPE;

    FUNCTION get_ref_offence_id(p_court_id_in     IN XHB_REF_OFFENCE.court_id%TYPE,
                                p_offence_code_in IN XHB_REF_OFFENCE.offence_code%TYPE)
                                RETURN XHB_REF_OFFENCE.ref_offence_id%TYPE;

    FUNCTION get_ref_prosecutor_agency_id(p_court_id_in         IN XHB_REF_PROSECUTOR_AGENCY.court_id%TYPE,
                                          p_crest_opposer_id_in IN XHB_REF_PROSECUTOR_AGENCY.crest_opposer_id%TYPE)
                                          RETURN XHB_REF_PROSECUTOR_AGENCY.ref_prosecutor_agency_id%TYPE;

    FUNCTION get_ref_app_result_id(p_court_id_in        IN XHB_REF_APP_RESULT.court_id%TYPE,
                                   p_app_result_code_in IN XHB_REF_APP_RESULT.app_result_code%TYPE)
                                   RETURN XHB_REF_APP_RESULT.ref_app_result_id%TYPE;


    FUNCTION get_ref_disposal_type_id(p_court_id_in         IN XHB_REF_DISPOSAL_TYPE.court_id%TYPE,
                                      p_template_version_in IN XHB_REF_DISPOSAL_TYPE.template_version%TYPE,
                                      p_disposal_code_in    IN XHB_REF_DISPOSAL_TYPE.disposal_code%TYPE,
                                      p_menu_group_in       IN XHB_REF_DISPOSAL_TYPE.menu_group%TYPE)
                                      RETURN XHB_REF_DISPOSAL_TYPE.ref_disposal_type_id%TYPE;

    FUNCTION get_ref_disposal_line_id(p_court_id_in         IN XHB_REF_DISPOSAL_LINE.court_id%TYPE,
                                      p_disposal_code_in    IN XHB_REF_DISPOSAL_LINE.disposal_code%TYPE,
                                      p_template_version_in IN XHB_REF_DISPOSAL_LINE.template_version%TYPE,
                                      p_dil_seq_no_in       IN XHB_REF_DISPOSAL_LINE.dil_seq_no%TYPE)
                                      RETURN XHB_REF_DISPOSAL_LINE.ref_disposal_line_id%TYPE;
                                   
    FUNCTION get_case_type(p_case_id_in IN EXT_XHB_CASE.case_id%TYPE)
                           RETURN EXT_XHB_CASE.case_type%TYPE;

    FUNCTION lookup_xhb_hearing_list_id(p_list_id_in IN XHB_HEARING_LIST.list_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_sitting_id(p_sitting_id_in IN XHB_SITTING.sitting_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_case_id(p_case_id_in IN XHB_CASE.case_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_hearing_id(p_hearing_id_in IN XHB_HEARING.hearing_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_sched_hearing_id(p_scheduled_hearing_id_in IN XHB_SCHEDULED_HEARING.scheduled_hearing_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_address_id(p_address_id_in IN XHB_ADDRESS.address_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_defendant_id(p_defendant_id_in IN XHB_DEFENDANT.defendant_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_def_on_case_id(p_defendant_on_case_id_in IN XHB_DEFENDANT_ON_CASE.defendant_on_case_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_charge_id(p_charge_id_in IN XHB_CHARGE.charge_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_defendant_charge_id(p_defendant_charge_id_in IN XHB_DEFENDANT_CHARGE.defendant_charge_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_offence_id(p_offence_id_in IN XHB_OFFENCE.offence_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_def_on_offence_id(p_defendant_on_offence_id_in IN XHB_DEFENDANT_ON_OFFENCE.offence_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_sched_def_id(p_sched_hearing_def_id_in IN XHB_SCHED_HEARING_DEFENDANT.sched_hear_def_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_breach_id(p_breach_id_in IN XHB_BREACH.breach_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_disposal2_id(p_disposal2_id_in IN XHB_DISPOSAL2.disposal2_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_clob_id(p_clob_id_in IN XHB_CLOB.clob_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_xml_document_id(p_xml_document_id_in IN XHB_XML_DOCUMENT.xml_document_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_wll_control_id(p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE) RETURN NUMBER;

    PROCEDURE populate_tables(p_court_id_in IN XHB_COURT.court_id%TYPE);
END training_utils_pkg;
/
show errors
