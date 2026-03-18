create or replace package xhb_data_migration_process_pkg as
/**
* CGI CREST TO XHIBIT Program
*
* MODULE      : xhb_data_migration_process_pkg
*
* DESCRIPTION : The package to load and enrich data taken from CREST database and then load into the XHIBIT database
*
*               To run procedures in this package 
*               1. login to sqlplus as data_mig/data_mig
*               2. SPOOL log_<procedure_name>.log
*               3. set serveroutput on size 1000000
                    begin
                    <package_name>.<procedure_name>(<court id>);
                    end;
                    /
*               5. SPOOL OFF
*
* VERSION HISTORY:
*
* Date          Author (s)                           Version    Nature of Change
* ----------    --------------------------------     --------   -----------------------------------------------------------------------
* 18/09/2018    C.Cash. S.Sethuraman. A.Dennis       1.0        Code is a merge of dm_process_pkg_abe, dm_process_pkg_cc, dm_process_ss
*                                                               for the data migration or crest to xhibit. 
* 26/11/2018    A Dennis                             1.1        CTX-2985 added get_next_list_seq_no
* 
* 04/12/2018    S Sethuraman                         1.2        CTX-3007 added new procedure update_xhb_rpa_mail_with_crest                     
*
* 18/01/2019    S Sethuraman                         1.3        CTX-3575 added new procedure parse_xhbstg_courtroom_day
* 14/02/2019    S Sethuraman                         1.4        CTX-3715 added parameter p_Crest_court_id to function get_next_list_seq_no
* 21/02/2019    S Sethuraman                         1.5        CTX-3743 added procedure upd_xhb_out_bw_history_crest
* 29/03/2019    A. Dennis                            1.6        CTX-3908 XHB_UPD_CASE_ON_LIST_FIXTURE added
**/

FUNCTION get_xhb_court_from_crest_court(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE)
RETURN NUMBER;

FUNCTION get_next_list_seq_no (p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE,  p_ctd_id  IN xhbstg_case_hearing_day_dm.ctd_id%TYPE)
RETURN  NUMBER;

PROCEDURE list_crest_cases_not_in_xhibit(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE);

FUNCTION update_xhbstg_case_dm( p_crest_court_id  IN xhbstg_case_dm.crest_court_id%TYPE
                              , p_case_no         IN xhbstg_case_dm.case_no%TYPE
                              , p_case_type       IN xhbstg_case_dm.case_type%TYPE
                              , p_xhibit_court_id IN xhbstg_case_dm.xhibit_court_id%TYPE)
RETURN NUMBER;

PROCEDURE merge_crest_cases_into_xhibit(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE);

FUNCTION get_court_id_receiving_site( p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE
                                    , p_ctl_id         IN xhbstg_case_dm.ctl_id%TYPE) 
RETURN NUMBER;

PROCEDURE update_xhb_case_with_crest(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE);

PROCEDURE update_xhb_cpa_with_crest(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE);

 PROCEDURE insert_dm_log (p_crest_court_id    IN xhbstg_data_migration_log.court_id%type 
                         ,p_action_name       IN xhbstg_data_migration_log.action_name%type
                         ,p_run_time          IN xhbstg_data_migration_log.run_time%type
                         ,p_log_msg_type      IN xhbstg_data_migration_log.log_message_type%type
                         ,p_log_msg           IN xhbstg_data_migration_log.log_message%type
                         ,p_err_row_count     IN xhbstg_data_migration_log.error_row_count%type
                         ,p_success_row_count IN xhbstg_data_migration_log.success_row_count%type
                         ,p_last_updated_by   IN xhbstg_data_migration_log.last_updated_by%type DEFAULT 'DATA MIGRATION'
                         ,p_created_by        IN xhbstg_data_migration_log.created_by%type DEFAULT 'DATA MIGRATION'
                         );

PROCEDURE update_xhb_prsf_with_crest(p_crest_court_id IN xhbstg_case_party_sof_dm.crest_court_id%TYPE);

PROCEDURE update_xhb_rsf_with_crest(p_crest_court_id IN xhbstg_case_party_sof_dm.crest_court_id%TYPE);

PROCEDURE update_xhb_rsf_mail_with_crest(p_crest_court_id IN xhbstg_case_party_sof_dm.crest_court_id%TYPE);

PROCEDURE upd_xhb_court_site_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE);

PROCEDURE upd_xhb_court_room_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE);

PROCEDURE upd_xhb_doc_on_list_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_case_nad_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE);

PROCEDURE upd_xhb_offence_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE);

PROCEDURE upd_xhb_sit_on_list_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_lists_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE);

PROCEDURE update_xhb_court_with_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_defendant_with_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_legal_aid_order_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_legal_aid_amend_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_ref_chamber_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_charges_log_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_case_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_def_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_c_list_entry_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_diary_ne_no_case_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_c_diary_fixture_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_dir_for_case_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_ref_jud_tckt_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_bw_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_mon_ord_track_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_croom_usage_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_doc_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_jud_usage_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_case_on_list_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE update_xhb_rpa_mail_with_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE parse_xhbstg_courtroom_day(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_out_bw_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE);

PROCEDURE upd_xhb_case_on_list_fixture(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE);

end xhb_data_migration_process_pkg;
/
