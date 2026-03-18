set termout off;
spool &1

SELECT count(cpp_staging_inbound_id)
         FROM xhb_cpp_staging_inbound xcs, xhb_court xc
        WHERE xcs.time_loaded < TRUNC(SYSDATE)
          AND xcs.time_loaded > TRUNC(SYSDATE)-1
          AND xcs.acknowledgment_status is null
          AND xcs.court_code=xc.crest_court_id
          and xc.cpp_court='Y';

exit;
