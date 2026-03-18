CREATE OR REPLACE PROCEDURE XHB_DM_UPD_MISC_APP_CASES (p_xhibit_court_id IN xhibit.xhb_court.court_id%TYPE) as
/*     23/05/2019    S Sethuraman  - 
                     CTX-4251 - Refresh Misc appeal cases prior to Data migration 
                     for the XHIBIT Court ID that is going to be migrated
                     1st updated to RF to initate trigger 
                     2nd updated to S on same set of records 
*/
BEGIN

UPDATE  XHIBIT.XHB_CASE
   SET CHARGE_IMPORT_INDICATOR = 'RF'
 WHERE COURT_ID = p_xhibit_court_id 
   AND CASE_TYPE = 'A'
   AND CASE_SUB_TYPE = 'O';

   COMMIT;

UPDATE  XHIBIT.XHB_CASE
   SET CHARGE_IMPORT_INDICATOR = 'S'
 WHERE COURT_ID = p_xhibit_court_id 
   AND CASE_TYPE = 'A'
   AND CASE_SUB_TYPE = 'O';

   COMMIT;

END XHB_DM_UPD_MISC_APP_CASES;
/
