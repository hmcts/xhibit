BEGIN 
  -- CTX-4660 Create XHB_CASE_HISTORY records for cases set to status 'H'
  FOR rec IN (SELECT xc.case_id FROM xhb_case xc
               WHERE xc.case_status = 'H'
                 AND NOT EXISTS (SELECT 1 FROM xhb_case_history xch
                                  WHERE xch.case_number = xc.case_number
                                    AND xch.case_type = xc.case_type
                                    AND xch.court_id = xc.court_id)) LOOP
      xhb_housekeeping_pkg.set_case_to_historic(p_case_id => rec.case_id);
  END LOOP;
  COMMIT;
END;
/