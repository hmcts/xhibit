create or replace procedure xhb_update_xhb_defendant (p_court_id IN xhb_court.court_id%TYPE)
AS 
/**
  * NAME         : xhb_update_xhb_case
  * DESCRIPTION  : CTX-2684 - set the value of xhb_defendant.current_prison status to 'N' plus for any records where
  *                it is null, set these to N
  * TICKET ISSUES: 8.7.0 Update xhb_case set case_listed = 'Y'
  *                8.7.0.6 Set the default value on xhb_case.case_status to 'O'
  *                8.7.0.17 Set the value of video_link to N for all NULL records
  * PARAMETERS   : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * NOTE         : There are DDL scripts required along with these updates.  See spreadsheet attached to ticket for more info. 
**/

 v_err_msg VARCHAR2(250);
 v_err_code NUMBER := 0;
    
BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_defendant for court ID: '||p_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');
    
        
    UPDATE xhb_defendant xd
    SET xd.current_prison_status = 'N'
    WHERE xd.current_prison_status IS NULL
    AND EXISTS (SELECT 'x'
                FROM xhb_defendant_on_case xdoc
                ,    xhb_case xc
                WHERE xdoc.case_id = xc.case_id
                AND   (xdoc.obs_ind IS NULL OR xdoc.obs_ind <> 'Y')
                AND   xdoc.defendant_id = xd.defendant_id
                AND   xc.court_id = p_court_id);  
    
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_defendant.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

 EXCEPTION
   WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001,'Error in xhb_update_xhb_defendant :- ' || v_err_code || ' : ' || v_err_msg);
  
END xhb_update_xhb_defendant;
/