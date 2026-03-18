/*
 * Filename:    DB_Patch_5.12.1_Prod.sql
 *
 * System:      Pre-Production & Production
 *
 * Date:        6th April 2004
 *
 */

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

ALTER TABLE XHB_PSR_REQUEST MODIFY(PROBATION_OFFICE_EMAIL  VARCHAR2(255),
                                   RECIPIENT_EMAIL         VARCHAR2(255),
                                   DEFENDANT_SURNAME       VARCHAR2(35),
                                   DEFENDANT_FORENAMES     VARCHAR2(255));

CREATE TABLE temp_xhb_case AS SELECT case_id, no_pros_witness FROM xhb_case;
ALTER TRIGGER xhb_case_bur_tr DISABLE;
UPDATE xhb_case SET no_pros_witness = NULL;

ALTER TABLE XHB_CASE MODIFY(NO_PROS_WITNESS NUMBER(3));

UPDATE xhb_case c SET c.no_pros_witness = (SELECT tc.no_pros_witness FROM temp_xhb_case tc WHERE tc.case_id = c.case_id);
DROP TABLE temp_xhb_case;
ALTER TRIGGER xhb_case_bur_tr ENABLE;


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

ALTER TABLE AUD_PSR_REQUEST MODIFY(PROBATION_OFFICE_EMAIL  VARCHAR2(255),
                                   RECIPIENT_EMAIL         VARCHAR2(255),
                                   DEFENDANT_SURNAME       VARCHAR2(35),
                                   DEFENDANT_FORENAMES     VARCHAR2(255));

CREATE TABLE TEMP_AUD_CASE TABLESPACE AUDITD AS SELECT * FROM AUD_CASE;

DROP TABLE AUD_CASE;

CREATE TABLE AUD_CASE TABLESPACE AUDITD AS SELECT * FROM XHB_CASE WHERE 1 = 0;
ALTER TABLE AUD_CASE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

INSERT INTO aud_case (SELECT * FROM temp_aud_case);

DROP TABLE temp_aud_case;


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

ALTER TRIGGER XHB_PSR_REQUEST_BIR_TR COMPILE;
ALTER TRIGGER XHB_PSR_REQUEST_BUR_TR COMPILE;

ALTER TRIGGER XHB_CASE_BIR_TR COMPILE;
ALTER TRIGGER XHB_CASE_BUR_TR COMPILE;


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of standing data
 */


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = '5.12.1', last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
