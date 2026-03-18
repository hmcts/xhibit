/*
 * Filename: xhb_code_release_control_822.sql
 *
 *
 *
 * Description: Script to rebuild the xhibit database code as part of an xhibit release.
 *              All package headers and package bodies are recreated regardless of whether they have
 *              have changed for a particular release or not.
 *
 *              This script needs updating should any new packages be added in future releases.
 *
 *		When releasing the code for a release check out the relevant branch from CVS.
 *		Then place this control script in the same directory as the code and call this script
 *		as part of the db_release patch script.
 *
 * Date:        6th April 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 16/05/05	SS			script created.
 * 16/05/05	SS			Commented out xhb_training_list,xhb_training_utils,xhb_standing data
 *					Steve tully confirmed these scripts only required for training environment.
 * 16/05/05	SS			Added in the mpkg_load_bal package required for release 7.4.1.
 * 22/11/05	Kadu Shah		Added packages for release 7.7 xhb_crestformsbtof_pkg and xhb_list_distribution_pkg
 * 02/12/05     Kadu Shah		Deleted package Xhb_connected_user_pkg as per NE's mail
 * 13/02/06	Kadu Shah		set echo off prior to calling proc sql & set it to on
 *					again at the end
 * 16/02/06	Kadu Shah		Removed calls to package xhb_cr_live_status_pkg 
 * 14/07/06	Kadu Shah		Added xhb_message_broker_pkg - header and body
 * 13/10/06     Steve Miles             Added xhb_instant_messaging_pkg - header and body
 * 13/09/07	Graham Dixon		Added xhb_defendents_pkg, xhb_charges_pkg & xhb_1745_data_migration_pkg - headers & bodies
 * 13/09/09     Graham Dixon            Changed "defendents" to "defendants" in package names
 */ 

set echo off

Prompt First recreate the package headers...

@@counsel_h.sql
--@@Dev_Standing_Data_h.sql
@@mpkg_load_bal_h.sql
@@xhb_court_log_pkg_h.sql
@@xhb_crestformsbtof_pkg_h.sql
@@xhb_custom_pkg_h.sql
@@xhb_daily_list_pkg_h.sql
@@xhb_formatting_pkg_h.sql
@@xhb_hearing_pkg_h.sql
@@xhb_import_export_status_pkg_h.sql
@@xhb_list_distribution_pkg_h.sql
@@xhb_orders_pkg_h.sql
@@xhb_post_merc_ref_data_pkg_h.sql
@@xhb_psr_request_pkg_h.sql
@@xhb_public_display_pkg_h.sql
@@xhb_ref_advocate_h.sql
@@xhb_ref_local_pkg_h.sql
@@xhb_search_pkg_h.sql
@@xhb_terminal_pkg_h.sql
--@@xhb_training_list_pkg_h.sql
--@@xhb_training_utils_pkg_h.sql
@@xhb_translation_pkg_h.sql
@@xhb_view_schedule_pkg_h.sql
@@xhb_message_broker_pkg_h.sql
@@xhb_instant_messaging_pkg_h.sql
@@xhb_defendants_pkg_h.sql
@@xhb_charges_pkg_h.sql
@@xhb_1745_data_migration_pkg_h.sql

prompt Now create package bodys.....

@@counsel_b.sql
--@@Dev_Standing_Data_b.sql
@@mpkg_load_bal_b.sql
@@xhb_court_log_pkg_b.sql
@@xhb_crestformsbtof_pkg_b.sql
@@xhb_custom_pkg_b.sql
@@xhb_daily_list_pkg_b.sql
@@xhb_formatting_pkg_b.sql
@@xhb_hearing_pkg_b.sql
@@xhb_import_export_status_pkg_b.sql
@@xhb_list_distribution_pkg_b.sql
@@xhb_orders_pkg_b.sql
@@xhb_post_merc_ref_data_pkg_b.sql
@@xhb_psr_request_pkg_b.sql
@@xhb_public_display_pkg_b.sql
@@xhb_ref_advocate_b.sql
@@xhb_ref_local_pkg_b.sql
@@xhb_search_pkg_b.sql
@@xhb_terminal_pkg_b.sql
--@@xhb_training_list_pkg_b.sql
--@@xhb_training_utils_pkg_b.sql
@@xhb_translation_pkg_b.sql
@@xhb_view_schedule_pkg_b.sql
@@xhb_message_broker_pkg_b.sql
@@xhb_instant_messaging_pkg_b.sql
@@xhb_defendants_pkg_b.sql
@@xhb_charges_pkg_b.sql
@@xhb_1745_data_migration_pkg_b.sql

set echo on

Prompt Package body compile complete. Please check and rectify any compilation errors.

