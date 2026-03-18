/*    -------------------------------------------------------------------------------
*     CTX-1385
*/    -------------------------------------------------------------------------------

INSERT INTO XHB_BW_HISTORY 
(DEFENDANT_ON_CASE_ID, BW_ISSUE_DATE, BC_STATUS_BW_ISSUED, BW_END_DATE, BC_STATUS_BW_ENDED, WITHDRAWN, ABSCONDED)
VALUES
(DEFENDANT_ON_CASE_ID, BW_ISSUE_DATE,  XHB_DEFENDANT_ON_CASE.CURRENT_BC_STATUS, null, null, null, null)

COMMIT;

 