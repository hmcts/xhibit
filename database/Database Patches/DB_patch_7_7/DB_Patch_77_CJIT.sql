/*
 * Filename:    DB_Patch_7_7_CJIT.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        08 Decemeber 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 08/12/2005	K Shah			Database changes made to comprise release 7.7
 *
 */ 




ALTER TABLE CJIT.CJI_DOCUMENT_TYPE ADD MAJOR_SCHEMA_VERSION NUMBER(2);

ALTER TABLE CJIT.CJI_DOCUMENT_TYPE ADD MINOR_SCHEMA_VERSION NUMBER(2);
