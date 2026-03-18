/*    -------------------------------------------------------------------------------
*     CTX-1339
*/    -------------------------------------------------------------------------------

/*    -------------------------------------------------------------------------------
*     Disable the triggers so that none of the updates fire and populate the audit table
*     Create a temporary field holder
*     Re-create version with the number precision and default as per FS
*     Update the new field to what the previous value was
*     Drop the old field from the database
*     Enable the trigger back
*/    ---------------------------------------------------------------------------------
--check that there are no disabled triggers before running this or they will be enabled later
ALTER TABLE XHB_REF_LISTING_DATA DISABLE ALL TRIGGERS;

ALTER TABLE XHB_REF_LISTING_DATA RENAME COLUMN VERSION to VERSION_OLD;

ALTER TABLE XHB_REF_LISTING_DATA ADD VERSION NUMBER(5) DEFAULT 1 NOT NULL;

UPDATE XHB_REF_LISTING_DATA SET VERSION = VERSION_OLD;

ALTER TABLE XHB_REF_LISTING_DATA DROP COLUMN VERSION_OLD;


ALTER TABLE XHB_REF_LISTING_DATA MODIFY (REF_DATA_TYPE NOT NULL,
                                         REF_DATA_VALUE NOT NULL
										 );

--Start the triggers back up after updates have run
ALTER TABLE XHB_REF_LISTING_DATA ENABLE ALL TRIGGERS;										 
/*--------------------------------------------------------------------------
     Modify AUD_REF_LISTING_DATA columns
----------------------------------------------------------------------------*/

ALTER TABLE AUD_REF_LISTING_DATA RENAME COLUMN VERSION to VERSION_OLD;

ALTER TABLE AUD_REF_LISTING_DATA ADD VERSION NUMBER(5) DEFAULT 1 NOT NULL;

UPDATE AUD_REF_LISTING_DATA SET VERSION = VERSION_OLD;

ALTER TABLE AUD_REF_LISTING_DATA DROP COLUMN VERSION_OLD;

ALTER TABLE AUD_REF_LISTING_DATA MODIFY (REF_DATA_TYPE NOT NULL,
                                         REF_DATA_VALUE NOT NULL
										 );

commit;
