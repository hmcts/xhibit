CREATE OR REPLACE PROCEDURE xhb_update_def_hearing_record (p_court_id IN XHB_HEARING.COURT_ID%TYPE) AS
/**
  * DESCRIPTION :
  *   Procedure                      Purpose
  *   ==========================     =======
  *   xhb_update_def_hearing_record  CTX-2474
  *  Assumptions                     This package will need an execution call too.
  *  Functionality                   Migrate the CREST Form A data into XHIBIT
  *  CTX-4388 - Add linked hearing records as well
***/
BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_def_hearing_record for court ID: '||p_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

    MERGE INTO XHB_DEF_HEARING_RECORD xdhr
    USING (SELECT xh.hearing_id, -- CTX-4388, SELECT XH.HEARING_ID
                  xea.status_flag,
                  xea.court_clerk_export
             FROM XHB_EXPORTA xea, XHB_HEARING xh
             -- CTX-4388 link xea.hearing_id to xh.hearing_id
             --               if xea.hearing_hearing_id is NULL then link xea.linked)hearing_id to xh.linked_hearing_id
             --                  AND retreive all hearing_ids in XHB_HEARING where the linked_hearing_ids are matched
             --                update all the hearing_ids linked to that linked_hearing_id in DEF_HEARING
            WHERE xh.court_id = p_court_id AND
                  (xea.hearing_id = xh.hearing_id OR xea.linked_hearing_id = xh.linked_hearing_id) -- CTX-4388
                  ) subqry
    ON (subqry.hearing_id = xdhr.hearing_id)
    WHEN MATCHED THEN
         UPDATE SET xdhr.forma_status = subqry.status_flag,
                    xdhr.forma_court_clerk = subqry.court_clerk_export;
    DBMS_OUTPUT.PUT_LINE(TO_CHAR(SQL%ROWCOUNT)||' rows updated');

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_def_hearing_record.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

EXCEPTION
     WHEN OTHERS THEN
          raise_application_error(-20001,'Error in xhb_update_no_def :- ' || SQLCODE || ' : ' || SQLERRM);

END xhb_update_def_hearing_record;
/
