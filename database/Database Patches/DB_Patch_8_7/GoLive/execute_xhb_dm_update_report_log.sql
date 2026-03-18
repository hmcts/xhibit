--Post DATA MIGRATION - Run the script to  update the date_last_run column with sysdate in XHB_REPORT_LOG table for the XHIBIT COURT ID
BEGIN
 xhb_dm_update_report_log(p_xhibit_court_id => &1);
END;
/
