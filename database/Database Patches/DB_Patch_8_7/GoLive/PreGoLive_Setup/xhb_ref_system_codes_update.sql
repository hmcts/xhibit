CREATE OR REPLACE PROCEDURE xhb_ref_system_codes_update(p_court_id IN xhb_court.court_id%TYPE)
AS
BEGIN
               DELETE xhb_ref_system_code xrsc
               WHERE xrsc.code_type = 'REASON_REMOVED'
               AND xrsc.code_title = 'VACATED TRIAL'
               AND xrsc.court_id = p_court_id
               AND xrsc.code = 'Z';

              MERGE INTO xhb_ref_system_code xrsc
              USING (
                      SELECT * FROM (
                               SELECT 'N' code, 'Accused bound over at PTPH' de_code FROM DUAL UNION
                               SELECT 'Q' code, 'Guilty plea at PTPH trl fix at PLY' de_code FROM DUAL UNION
                               SELECT 'R' code, 'Transferred Out' de_code FROM DUAL UNION
                               SELECT 'S' code, 'Error' de_code FROM DUAL), (
                               SELECT 'REASON_REMOVED' code_type, 'VACATED TRIAL' code_title, p_court_id court_id FROM DUAL)
               ) subqry ON (
                       xrsc.code_type = subqry.code_type AND
                       xrsc.code_title = subqry.code_title AND
                       xrsc.court_id = subqry.court_id AND
                       xrsc.code = subqry.code)
               WHEN MATCHED THEN
               UPDATE SET xrsc.de_code = subqry.de_code
               WHEN NOT MATCHED THEN
               INSERT (REF_SYSTEM_CODE_ID, COURT_ID, CODE_TYPE, CODE_TITLE, CODE, DE_CODE, OBS_IND)
               VALUES (XHB_REF_SYSTEM_CODE_SEQ.NEXTVAL, subqry.COURT_ID, subqry.CODE_TYPE, subqry.CODE_TITLE, subqry.CODE, subqry.DE_CODE, 'N');

               COMMIT;

      EXCEPTION
         WHEN OTHERS THEN
              RAISE;
END;
/
