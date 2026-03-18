CREATE OR REPLACE PROCEDURE XHB_DM_UPD_CASE_LISTED (p_xhibit_court_id IN xhibit.xhb_court.court_id%TYPE) as
/*     22/05/2019    S Sethuraman  - 
                     CTX-4214 - update CASE_LISTED Flag to N for the cases migrated from DM
                     for the XHIBIT COURT ID that is newly migrated 
*/
BEGIN

    UPDATE  XHIBIT.XHB_CASE
   SET CASE_LISTED = 'N',
       LAST_UPDATED_BY = 'DATA_MIG'
 WHERE COURT_ID = p_xhibit_court_id AND
       CREATED_BY = 'DATA MIGRATION' AND
       TRUNC(CREATION_DATE) = SYSDATE;

   COMMIT;

END XHB_DM_UPD_CASE_LISTED;
/
