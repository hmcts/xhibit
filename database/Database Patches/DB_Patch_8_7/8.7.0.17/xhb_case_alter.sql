/*    -------------------------------------------------------------------------------
*     CTX-1339
*/    -------------------------------------------------------------------------------

/*    -------------------------------------------------------------------------
*     Set  VIDEO_LINK_REQUIRED to N if it is null then modify column to NOT NULL
*/    --------------------------------------------------------------------------

UPDATE XHB_CASE SET VIDEO_LINK_REQUIRED = 'N' WHERE VIDEO_LINK_REQUIRED IS NULL;

ALTER TABLE XHB_CASE MODIFY VIDEO_LINK_REQUIRED DEFAULT 'N' NOT NULL;


commit;


