/**
* CTX-4112 : FIX_XHB_ADDRESS_POSTCODE procedure  to be run from XHIBIT schema
*
* DESCRIPTION : FIX_XHB_ADDRESS_POSTCODE procedure is required to be run as part of GoLiveMaster.sql
*               GoLiveMaster.sql runs scripts post DM in XHIBIT schema
*               In order to run the fix_xhb_address_postcode procedure as well from XHIBIT schema,
*               1.  From XHIBIT schema, FIX_XHB_ADDRESS_POSTCODE procedure needs to be deployed in XHIBIT schema
*               2.  From DATA_MIG schema, GRANT ALL access to be given on the below DATA_MIG objects to XHIBIT schema
*                     a. XHBSTG_DATA_MIGRATION_LOG TABLE
*                     b. XHBSTG_DATA_MIGRATION_LOG_SEQ SEQUENCE
*               The above 2 tasks are one off and once implemented the procedure can be run from GoLiveMaster.sql from XHIBIT schema
*
*
**/

grant select, insert, update, references on xhbstg_data_migration_log to XHIBIT;

grant all on xhbstg_data_migration_log_seq to xhibit;

/**********************************************************************************/
/** run script FIX_XHB_ADDRESS_POSTCODE.sql from XHIBIT schema to deploy procedure FIX_XHB_ADDRESS_POSTCODE in XHIBIT ****/
/**********************************************************************************/
