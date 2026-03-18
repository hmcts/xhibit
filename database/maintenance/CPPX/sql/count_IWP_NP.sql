set termout off;
spool &1
SELECT count(xcf.cpp_formatting_id)
         FROM xhb_cpp_formatting xcf, xhb_court xc
        WHERE xcf.date_in > TRUNC(SYSDATE)-1
          AND xcf.format_status IN ('NP','IP','ND')
          AND xcf.court_id = xc.court_id
          and xc.cpp_court='Y';
exit;
