/*
 * Filename:    DARTS_DB_Patch_8_8_6_0.sql
 *
 *
 * PLEASE CHANGE :  DARTS Database modifications.
 *
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 24/01/2021	M.Harris		Initial version
 * 25/01/2021	M.Harris		DVR-19
 *
 */

/*DVR-18*/
@@insert_dar_darts_config.sql;
@@dar_message_pkg_h.sql;
@@dar_message_pkg_b.sql;

/