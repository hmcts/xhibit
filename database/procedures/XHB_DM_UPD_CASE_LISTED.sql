CREATE OR REPLACE PROCEDURE XHB_DM_UPD_CASE_LISTED (p_xhibit_court_id IN xhibit.xhb_court.court_id%TYPE) as
/*     22/05/2019    V1.0 S Sethuraman  - 
                        CTX-4214 - update CASE_LISTED Flag to N for the cases migrated from DM
                        for the XHIBIT COURT ID that is newly migrated 
       04/06/2019   V1.1 S Sethuraman -
                    After observing real time scenario from Live run on 01/06, the SQL is 
                    amended as recommended in Jira ticket to refer to XHB_HEARING instead of sysdate check                  
*/
BEGIN

    UPDATE  XHIBIT.XHB_CASE CS
   SET CS.CASE_LISTED = 'N',
       CS.LAST_UPDATED_BY = 'DATA_MIG'
 WHERE CS.COURT_ID = p_xhibit_court_id AND
       CS.CREATED_BY = 'DATA MIGRATION' AND
       CS.CASE_LISTED = 'Y' AND
       NOT EXISTS (SELECT 'X' FROM XHIBIT.XHB_HEARING HR WHERE HR.CASE_ID = CS.CASE_ID);

   COMMIT;

END XHB_DM_UPD_CASE_LISTED;
/
