CREATE OR REPLACE PROCEDURE XHB_DM_UPDATE_REPORT_LOG (p_xhibit_court_id IN xhb_court.court_id%TYPE) as
/*     20/02/2019    S Sethuraman  - 
                     CTX-3706 - update DATE_LAST_RUN column with SYSDATE in XHB_REPORT_LOG table
                     for crest_report_code = PRLIS (Running List) 
                     for the XHIBIT COURT ID that is newly migrated 
*/
BEGIN

    UPDATE  XHB_REPORT_LOG
   SET DATE_LAST_RUN = SYSDATE
 WHERE COURT_ID = p_xhibit_court_id AND
       CREST_REPORT_CODE = 'PRLIS';

   COMMIT;

END XHB_DM_UPDATE_REPORT_LOG;
/
