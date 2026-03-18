CREATE OR REPLACE PACKAGE BODY xhb_psr_request_pkg AS
       PROCEDURE get_issued_psrs(results_out      OUT SYS_REFCURSOR,
                                         court_id_in      IN  XHB_COURT.court_id%TYPE) AS
       BEGIN
            OPEN results_out FOR
                SELECT psr.psr_request_id PSR_REQUEST_ID,
                       case.case_type ||case.case_number CASENUMBER,
                       def.first_name||' '||def.middle_name||' '||def.surname DEFENDANT,
                       psr.psr_court_room PSR_COURT_ROOM,
                       psr.psr_status PSR_STATUS
                FROM   XHB_PSR_REQUEST psr,
                       XHB_CASE case,
                       XHB_DEFENDANT_ON_CASE doc,
                       XHB_DEFENDANT def
                WHERE  psr.defendant_on_case_id = doc.defendant_on_case_id
                AND    doc.defendant_id = def.defendant_id
                AND    (doc.obs_ind IS NULL OR doc.obs_ind <> 'Y')
                AND    doc.case_id = case.case_id
                AND    case.court_id = court_id_in
                AND    psr.psr_status IS NOT NULL
                AND    psr.psr_status IN ('ISSUED')
                ORDER BY psr.last_update_date,
                         defendant;
       END get_issued_psrs;

       PROCEDURE get_unissued_psrs(results_out      OUT SYS_REFCURSOR,
                                         court_id_in      IN  XHB_COURT.court_id%TYPE) AS
       BEGIN
            OPEN results_out FOR
                SELECT psr.psr_request_id PSR_REQUEST_ID,
                                   case.case_type ||case.case_number CASENUMBER,
                                   def.first_name||' '||def.middle_name||' '||def.surname DEFENDANT,
                                   psr.psr_court_room PSR_COURT_ROOM,
                                   psr.psr_status PSR_STATUS
                            FROM   XHB_PSR_REQUEST psr,
                                   XHB_CASE case,
                                   XHB_DEFENDANT_ON_CASE doc,
                                   XHB_DEFENDANT def
                            WHERE  psr.defendant_on_case_id = doc.defendant_on_case_id
                            AND    doc.defendant_id = def.defendant_id
                            AND    doc.case_id = case.case_id
                            AND    (doc.obs_ind IS NULL OR doc.obs_ind <> 'Y')
                            AND    case.court_id = court_id_in
                            AND   (psr.psr_status IS NULL OR psr.psr_status NOT IN ('ISSUED'))
                            AND   (PSR.PSR_REQUEST_TRIGGER IS NULL OR PSR.PSR_REQUEST_TRIGGER=0)
                            ORDER BY psr.last_update_date,
                         defendant;
       END get_unissued_psrs;

END xhb_psr_request_pkg;
/
show errors