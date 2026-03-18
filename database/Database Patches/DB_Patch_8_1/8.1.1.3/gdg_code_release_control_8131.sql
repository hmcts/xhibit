/*
 * Filename: gdg_code_release_control_8131.sql
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
 * 12/03/06	Kadu Shah			script created.
 * 15/05/07     Kadu Shah			Added gdg_application_support_pkg  -  PR59495
 *
 */ 

Prompt First recreate the package headers...
@@gdg_scjse_gateway_common_pkg_h.sql
@@gdg_scjse_gateway_inbound_pkg_h.sql
@@gdg_scjse_gateway_jms_pkg_h.sql
@@gdg_scjse_gateway_outbound_pkg_h.sql
@@gdg_application_support_pkg_h.sql 


prompt Now create package bodys.....

@@gdg_scjse_gateway_common_pkg_b.sql
@@gdg_scjse_gateway_inbound_pkg_b.sql
@@gdg_scjse_gateway_jms_pkg_b.sql
@@gdg_scjse_gateway_outbound_pkg_b.sql
@@gdg_application_support_pkg_b.sql 



Prompt Package body compile complete. Please check and rectify any compilation errors.