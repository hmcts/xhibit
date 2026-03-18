set termout off;
spool &1
 SELECT count(xcl.cpp_list_id)
         FROM xhb_cpp_list xcl, xhb_court xc
        WHERE xcl.time_loaded > TRUNC(SYSDATE)-1
          AND xcl.status IN ('NP','IP')
          AND xcl.court_code=xc.crest_court_id
          and xc.cpp_court='Y';
exit;
