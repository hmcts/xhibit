/**
* CGI DREST TO XHIBIT Program
*
* MODULE      : dm_process_pkg_abe
*
* DESCRIPTION : CTX-2174: This script should be run in the data_mig database schema to create the DML Error Logging table that will hold any
*               errors encountered during the MERGE between the CREST table XHBSTG_CASE_DM and the XHIBIT table XHB_CASE. This is because during
*               Oracle MERGE we do not have easy access to the individual rows involved to report on them. Therefore, when this script is
*               run the Oracle feature DBMS_ERRLOG.CREATE_ERROR_LOG will create a table named err$_xhbstg_case_dm. So when the procedure that 
*               we have written to perform the MERGE is called, any exceptions will be put into the table err$_xhbstg_case_dm and the MERGE
*               will run to the end. That is, an exception should not stop it from carrying on to the end, since they will be logged.
*               So at the end, to find any errors after merging say Crest Court Id 453 you can query the table like this:
*               SELECT ora_err_number$
*                    , ora_err_mesg$
*               FROM   err$_xhbstg_case_dm
*               WHERE  ora_err_tag$ = 'MERGE_453';
*
* PREREQUISITE : The table XHBSTG_CASE_DM must already exist in the data_mig schema.
*
* VERSION HISTORY:
*
* Date          Author      Version    Nature of Change
* ----------    -------     --------   ----------------------------------------
* 14/08/2018    A Dennis    0.1        Originally written
*
* 
**/
-- Please read the description above
BEGIN
    DBMS_ERRLOG.CREATE_ERROR_LOG(dml_table_name => 'xhbstg_case_dm');
END;
/
