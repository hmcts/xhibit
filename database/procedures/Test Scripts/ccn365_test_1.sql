PL/SQL Developer Test script 3.0
111
-- Created on 16/03/2009 by FIELDDA 
declare 
  -- Local variables here
  l_xhb_case_before  integer;
  l_mtbl_case_before INTEGER;
  l_results          xhb_hk_results%ROWTYPE;
  i                  INTEGER;
  l_diff_1           INTEGER;
  l_diff_2           INTEGER;
  l_errors           INTEGER;
  l_xhb_lists_before INTEGER;
begin

  -- Test statements here
  DELETE mtbl_case_history;
  
  INSERT INTO mtbl_case_history
  (case_no
  ,case_type
  ,court_id
  ,case_id
  )
  SELECT case_number
  ,      case_type
  ,      court_id
  ,      case_id
  FROM   xhb_case
  WHERE  rownum < 21;
  
  SELECT COUNT(*)
  INTO   l_mtbl_case_before
  FROM   mtbl_case_history;
  
 
  SELECT COUNT(*)
  INTO   l_xhb_case_before
  FROM   xhb_case;

  SELECT COUNT(*)
  INTO   l_xhb_lists_before
  FROM   xhb_xml_document;
  
  xhb_housekeeping_pkg.initiate_run('C'
                                   ,10
                                   ,10000
                                   ,10000
                                   ,10000
                                   ,10000
                                   ,TRUE
                                   );  
  
  ----------      

  SELECT *
  INTO   l_results
  FROM   xhb_hk_results
  WHERE  hk_run_id = (SELECT MAX(hk_run_id)
                      FROM   xhb_hk_results
                     );  

  SELECT COUNT(*)
  INTO   l_errors
  FROM   xhb_hk_error_log
  WHERE  hk_run_id = (SELECT MAX(hk_run_id)
                      FROM   xhb_hk_results
                     );  

  dbms_output.put_line('Run ID      - '||l_results.hk_run_id);
  dbms_output.put_line('Case Status - '||l_results.case_status);
  dbms_output.put_line('Reported Cases Deleted - '||l_results.cases_deleted);
  dbms_output.put_line('Reported Cases Error   - '||l_results.cases_error);
  dbms_output.put_line('Error Table Count      - '||l_errors);
   dbms_output.put_line('------------------------------------------------------------');
  
  SELECT COUNT(*)
  INTO   i
  FROM   mtbl_case_history;

  dbms_output.put_line('MTBL_CASE_HISTORY - Before Run  - '||l_mtbl_case_before||' records.');
  dbms_output.put_line('MTBL_CASE_HISTORY - After Run   - '||i||' records.');
  l_diff_1 := l_mtbl_case_before - i;
  dbms_output.put_line('MTBL_CASE_HISTORY - Difference  - '||l_diff_1||' records.');
  dbms_output.put_line('------------------------------------------------------------');


  SELECT COUNT(*)
  INTO   i
  FROM   xhb_case;

  dbms_output.put_line('XHB_CASE - Before Run - '||l_xhb_case_before||' records.');
  dbms_output.put_line('XHB_CASE - After  Run - '||i||' records.');
  l_diff_2 := l_xhb_case_before - i;
  dbms_output.put_line('XHB_CASE - Difference - '||l_diff_2||' records.');
  dbms_output.put_line('------------------------------------------------------------');

  dbms_output.put_line('List Status - '||l_results.list_status);
  dbms_output.put_line('Reported Lists Deleted - '||l_results.lists_deleted);
  dbms_output.put_line('------------------------------------------------------------');
  dbms_output.put_line('XHB_XML_DOCUMENT - Before Run - '||l_xhb_lists_before);

  SELECT COUNT(*)
  INTO   i
  FROM   xhb_xml_document;

  dbms_output.put_line('XHB_XML_DOCUMENT - After Run  - '||i);
  l_diff_2 := l_xhb_lists_before - i;
  dbms_output.put_line('XHB_XML_DOCUMENT - Difference - '||l_diff_2||' records.');



end;
0
0
