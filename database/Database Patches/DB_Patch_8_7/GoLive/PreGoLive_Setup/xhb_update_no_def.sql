create or replace procedure xhb_update_no_def (p_court_id IN xhb_court.court_id%TYPE DEFAULT NULL)
as 
/**
  * DESCRIPTION :
  *   Procedure                  Purpose
  *   ========================== =======
  *   xhb_update_no_def          CTX-2684 - update xhb_update_no_def.
  *                              Initial code in 8.7.0.06 xhb_update_no_def.sql performed poorly.  Lots of context switches between SQL and
  *                              PLSQL engines.  A bulk collect should have been used hence this procedure created
  *  Assumptions                 This package will need an execution call too.
  *  Functional Specification    Case Maintenance v1.4 [X.D.009]
  *  Functionality               If there are any cases that have no defendant then set it to 1.  Only select the rows where the counts are different                   
***/

    CURSOR def_cur
    IS
    SELECT xc.case_id
    ,     (SELECT NVL(DECODE(count(*),0,1, count(*)),1)--if there are none set to 1
           FROM xhb_defendant_on_case xdoc
           WHERE xdoc.case_id = xc.case_id
           AND NVL(xdoc.obs_ind,'N') <> 'Y'
           ) doc_cnt
    ,      no_defendants_for_case        
    FROM xhb_case xc
    WHERE xc.court_id = p_court_id
    AND xc.no_defendants_for_case <>(SELECT NVL(DECODE(count(*),0,1, count(*)),1)--if there are none set to 1
                                     FROM xhb_defendant_on_case xdoc
                                     WHERE xdoc.case_id = xc.case_id
                                     AND NVL(xdoc.obs_ind,'N') <> 'Y'
                                     )
   ;

    
     TYPE def_rec IS RECORD
    ( case_id                 xhb_case.case_id%TYPE
    , doc_cnt                 NUMBER
    , no_defendants_for_case xhb_case.no_defendants_for_case%TYPE
    );

    TYPE def_type IS TABLE OF def_rec;
    def_tt  def_type;

   v_err_code NUMBER;
   v_err_msg  VARCHAR2(100);

BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_no_def for court ID: '||p_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');
     OPEN def_cur;
        LOOP
         FETCH def_cur BULK COLLECT INTO def_tt LIMIT 1000;
         
          IF def_tt IS NOT NULL AND def_tt.COUNT > 0 THEN
             FOR i IN def_tt.FIRST .. def_tt.LAST
               LOOP
               DBMS_OUTPUT.PUT_LINE('Executing xhb_update_no_def for court ID: '||p_court_id||
                                    '.  Setting no_defendants_for_case to: '||def_tt(i).doc_cnt||
                                    ' for case_id: '||def_tt(i).case_id);
                --Run the updates
                UPDATE xhb_case
                SET no_defendants_for_case = def_tt(i).doc_cnt
                WHERE case_id = def_tt(i).case_id;
               
               END LOOP;
              COMMIT;
           END IF;
           EXIT WHEN def_cur%NOTFOUND;
        END LOOP;
      CLOSE def_cur;

      COMMIT;
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_no_def.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

 EXCEPTION
   WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001,'Error in xhb_update_no_def :- ' || v_err_code || ' : ' || v_err_msg);

END xhb_update_no_def;
/