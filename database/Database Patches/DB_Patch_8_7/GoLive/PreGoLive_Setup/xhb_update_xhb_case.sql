CREATE OR REPLACE PROCEDURE xhb_update_xhb_case (p_court_id IN xhb_court.court_id%TYPE)
AS 
/**
  * NAME         : xhb_update_xhb_case
  * DESCRIPTION  : CTX-1684 Select max case_number from xhb_case for the court and case type.  For each row returned 
  *                create a new record in xhb_case_number_seq_generator and + 100.  Increase the version by 1.
  * TICKET ISSUES: 8.7.0 Update xhb_case set case_listed = 'Y'
  *                8.7.0.6 Set the default value on xhb_case.case_status to 'O'
  *                8.7.0.17 Set the value of video_link to N for all NULL records
  * PARAMETERS   : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * NOTE         : There are DDL scripts required along with these updates.  See spreadsheet attached to ticket for more info. 
**/

 v_err_msg VARCHAR2(250);
 v_err_code NUMBER := 0;
    
BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_case for court ID: '||p_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');
    
    UPDATE xhb_case
    SET case_listed = 'Y'
    WHERE court_id = p_court_id
    ;
    
    UPDATE xhb_case
    SET case_status = 'O'
    WHERE court_id = p_court_id
    AND case_status IS NULL;  
    
    UPDATE xhb_case
    SET video_link_required = 'N'
    WHERE court_id = p_court_id
    AND video_link_required IS NULL;  
    
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_case.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

 EXCEPTION
   WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001,'Error in xhb_update_xhb_case :- ' || v_err_code || ' : ' || v_err_msg);
  
  
  
END xhb_update_xhb_case;
/