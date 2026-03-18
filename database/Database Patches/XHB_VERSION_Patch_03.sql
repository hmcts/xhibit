/*
 * Filename:    XHB_VERSION_Patch_03.sql
 *
 * System:      All
 *
 * Date:        27th July 2004
 *
 */

/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = '6.1.5', last_update_date = SYSDATE, updated_by = 'RELEASE' WHERE SCHEMA_NAME = 'JAVA';

COMMIT;
