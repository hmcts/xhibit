create or replace procedure xhb_update_xhb_offence (p_court_id IN xhb_court.court_id%TYPE DEFAULT NULL)
as 
/**
  * DESCRIPTION :
  *   Procedure                  Purpose
  *   ========================== =======
  *   xhb_update_xhb_offence     CTX-2684 - update xhb_offence.  Initially written in ctx-501
  *                              Initial code in 8.7.0.15 xhb_offence_update.sql performed poorly.  Lots of context switches between SQL and
  *                              PLSQL engines.  A bulk collect should have been used hence this procedure created
  *  Assumptions                 This package will need an execution call too.
  *                            
***/


    CURSOR offence_cur
    IS
    SELECT xo.offence_id
    ,      xcg.charge_id
    ,      xc.case_sub_type
    FROM xhb_case xc
    ,    xhb_charge xcg
    ,    xhb_offence xo
    WHERE xc.case_id = xcg.case_id
    AND   xcg.charge_id = xo.charge_id
    AND   xc.case_sub_type IS NOT NULL --no need to pull the case in if it going to set it to null anyway
    AND   xo.appeal_type IS NULL
    AND   xc.court_id = p_court_id
    AND   (xcg.obs_ind IS NULL OR xcg.obs_ind <> 'Y')
    AND   (xo.obs_ind IS NULL OR xo.obs_ind <> 'Y')
    ;

     TYPE offence_rec IS RECORD
    ( offence_id    xhb_offence.offence_id%TYPE
    , charge_id     xhb_charge.charge_id%TYPE
    , case_sub_type xhb_case.case_sub_type%TYPE
    );

    TYPE offence_type IS TABLE OF offence_rec;
    offence_tt  offence_type;

    
   v_err_code NUMBER;
   v_err_msg  VARCHAR2(100);

BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_offence for court ID: '||p_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');
     OPEN offence_cur;
        LOOP
           FETCH offence_cur BULK COLLECT INTO offence_tt LIMIT 1000;
         
          IF offence_tt IS NOT NULL AND offence_tt.COUNT > 0 THEN

             FOR i IN offence_tt.FIRST .. offence_tt.LAST
               LOOP
                --Run the updates
                DBMS_OUTPUT.PUT_LINE('Update xhb_offence. Setting appeal_type to:'||offence_tt(i).case_sub_type||' for offence_id: '|| offence_tt(i).offence_id);
                UPDATE xhb_offence
      			    SET appeal_type = offence_tt(i).case_sub_type
      			    WHERE offence_id = offence_tt(i).offence_id
                ;
               
               END LOOP;
			  COMMIT;
           END IF;
           EXIT WHEN offence_cur%NOTFOUND;
          END LOOP;
      CLOSE offence_cur;

      COMMIT;
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_offence.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

 EXCEPTION
   WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001,'Error in xhb_update_xhb_offence :- ' || v_err_code || ' : ' || v_err_msg);

END xhb_update_xhb_offence;
/