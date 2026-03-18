/* deleteXHB_DOCUMENT_DIST_PRE6473_BKUP.sql                                      */

DECLARE  
  rowsbefore INTEGER;
  rowsafter INTEGER;
  l_start_time NUMBER;
  l_end_time NUMBER;
  l_diff NUMBER;
  
  
 BEGIN
  l_start_time := DBMS_UTILITY.get_time;
  
  select count(*) into rowsbefore from XHB_DOCUMENT_DIST_PRE6473_BKUP;

  DBMS_OUTPUT.put_line ('Current date: '||to_char(sysdate,'YYYY-MM-DD HH:MM:SS'));

  execute immediate 'truncate table XHB_DOCUMENT_DIST_PRE6473_BKUP';

  select count(*) into rowsafter from XHB_DOCUMENT_DIST_PRE6473_BKUP;

  l_end_time := DBMS_UTILITY.get_time;
  l_diff := (l_end_time - l_start_time)/100 ;

  DBMS_OUTPUT.put_line ('');
  DBMS_OUTPUT.put_line ('Rows before:' || rowsbefore);
  DBMS_OUTPUT.put_line ('Rows after:' || rowsafter);
  DBMS_OUTPUT.put_line ('Time taken: ' || l_diff || ' seconds' );              
  DBMS_OUTPUT.put_line ('');
END;
/
