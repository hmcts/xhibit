/*
 * Filename: exi_code_release_control_8131.sql
 *
 *
 *
 * Description: Script to rebuild the exiss schema code as part of a release.
 *              All package headers and package bodies are recreated regardless of whether they have
 *              have changed for a particular release or not.
 *
 *              This script needs updating should any new packages be added in future releases.
 *
 *		When releasing the code for a release check out the relevant branch from CVS.
 *		Then place this control script in the same directory as the code and call this script
 *		as part of the db_release patch script.
 *
 * Date:        13th July 2006
 *
 */
/*
 * HISTORY
 * =======
 * DATE		WHO		CHANGE ID	COMMENT
 * ----         ---     	---------       -------
 * 13/07/06	Kadu Shah			script created.
 * 16/10/06	Kadu Shah			Added changes for 1669
 * 15/05/007    Kadu Shah			Added change for PR59495 -  exi_application_support_pkg
 *
 */ 

Prompt First recreate the package headers...
@@exi_custom_pkg_h.sql
@@exi_flow_to_exiss_pkg_h.sql
@@exi_jms_message_pkg_h.sql
@@exi_flow_to_xhibit_pkg_h.sql
@@exi_application_support_pkg_h.sql 


prompt Now create package bodys.....

@@exi_custom_pkg_b.sql
@@exi_flow_to_exiss_pkg_b.sql
@@exi_jms_message_pkg_b.sql
@@exi_flow_to_xhibit_pkg_b.sql
@@exi_application_support_pkg_b.sql  



Prompt Package body compile complete. Please check and rectify any compilation errors.