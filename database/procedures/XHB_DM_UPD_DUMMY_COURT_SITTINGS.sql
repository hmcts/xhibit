CREATE OR REPLACE PROCEDURE XHB_DM_UPD_DUMMY_SITTINGS (p_xhibit_court_id IN xhibit.xhb_court.court_id%TYPE) as
/*     22/05/2019    S Sethuraman  - 
                     CTX-4233 - suppress sittings for Future lists in Dummy courts from DM
                     for the XHIBIT COURT ID that is newly migrated 
*/
BEGIN

UPDATE  XHIBIT.XHB_SITTING_ON_LIST
   SET OBS_IND = 'Y',
       LAST_UPDATED_BY = 'DATA_MIGRATION'
 WHERE LIST_ID IN 
       (SELECT LIST_ID FROM XHIBIT.XHB_LIST 
         WHERE COURT_ID = p_xhibit_court_id)
   AND TRUNC(TIME_LISTED) >= sysdate
   AND COURT_SITE_ID = 
       (SELECT DISTINCT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE 
         WHERE COURT_ID = p_xhibit_court_id
           AND COURT_SITE_CODE = 'X');

   COMMIT;

END XHB_DM_UPD_DUMMY_SITTINGS;
/
