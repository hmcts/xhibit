/*
 * Filename:    DB_Patch_6.1.1.1.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        5th July 2004
 *
 */


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

CREATE INDEX SCHED_HEAR_MOVED_F_CT_RM_IDX ON XHB_SCHEDULED_HEARING (MOVED_FROM_COURT_ROOM_ID)
  TABLESPACE XHIBITX
  STORAGE (INITIAL 256K
           NEXT 256K
           PCTINCREASE 0);


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 * The standard procedure is as follows:
 *
 *     1. Drop the audit table (AUD_)
 *     2. Recreate audit table as select * from XHB_ table with no rows
 *     3. Add the INSERT_EVENT column to the end of the audit table
 *
 * Need to create temporary table in a process on the live system:
 *
 *     1. Create a temporary audit table as a copy of the current audit table
 *     2. Drop the original audit table
 *     3. Create new audit table as select * from XHB_ table with no rows
 *     4. Add the INSERT_EVENT column to the end of the audit table
 *     5. Insert the data from the temporary audit table into the new audit table
 */


/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 *
 * Note that these are generally the BUR (update and delete) triggers as the BIR
 * (insert) triggers will only change on renaming the auditing columns within the
 * XHB_ table.  However, always a good idea to recompile the BIR trigger.
 */


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of standing data
 */

DELETE FROM xhb_court_log_category
WHERE  event_desc_id = (SELECT event_desc_id FROM xhb_court_log_event_desc WHERE event_type = 10500)
AND    category_desc_id = (SELECT category_desc_id FROM xhb_court_log_category_desc WHERE category_description = 'Calc_Jury_Out_Time_Start');


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = '6.1.1.1', last_update_date = SYSDATE, updated_by = 'RELEASE' WHERE SCHEMA_NAME = 'XHIBIT';
UPDATE XHB_VERSION SET schema_version = '6.1.1.1', last_update_date = SYSDATE, updated_by = 'RELEASE' WHERE SCHEMA_NAME = 'MERCATOR';

COMMIT;
