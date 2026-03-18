CREATE OR REPLACE PROCEDURE xhb_populate_pub_running_list (p_court_id xhb_court.court_id%TYPE)
AS 
/**
  * DESCRIPTION :
  *   Procedure                      Purpose
  *   ============================== ===============================================================================================
  *   Author:                        Chris Cash on 21/09/18
  *   xhb_populate_pub_running_list  CTX-2539 
  *   Functional Specification       Reports v0.87 [4975.REP.DB.008]
  *   Assumptions                    N/A
  *                            
***/

    CURSOR prl_c
    IS
    SELECT case_id
    ,      pub_running_list_id
    FROM xhb_case xc
    WHERE xc.court_id = p_court_id
    ;

     TYPE prl_rec IS RECORD
    ( case_id             xhb_case.case_id%TYPE
    , pub_running_list_id xhb_case.pub_running_list_id%TYPE
    );

    TYPE prl_type IS TABLE OF prl_rec;
    prl_tt  prl_type;

    v_prl_id   xhb_pub_running_list.pub_running_list_id%TYPE;
    v_err_code NUMBER;
    v_err_msg  VARCHAR2(100);

BEGIN
  
    --'to create a new row in XHB_PUB_RUNNING_LIST.  This will be used to assign all existing cases in XHIBIT to a 'dummy running list'
    DBMS_OUTPUT.PUT_LINE('Start xhb_populate_pub_running_list for court ID: '||p_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');
    DBMS_OUTPUT.PUT_LINE(' ----------------------');
    
    SELECT xhb_pub_running_list_seq.NEXTVAL
    INTO v_prl_id
    FROM dual;
    
    DBMS_OUTPUT.PUT_LINE('Create new row in xhb_pub_running_list.  New pub_running_list_id: '||v_prl_id);
    DBMS_OUTPUT.PUT_LINE(' ----------------------');
    INSERT INTO xhb_pub_running_list (PUB_RUNNING_LIST_ID
                                      ,COURT_ID
                                      ,PUBLISHED_DATE
                                      ,OBS_IND
                                      ,LAST_UPDATE_DATE
                                      ,CREATION_DATE
                                      ,LAST_UPDATED_BY
                                      ,CREATED_BY
                                      ,VERSION)
    VALUES (v_prl_id
           ,p_court_id
           ,sysdate
           ,'Y' --obs_ind set to Y
           , sysdate
           , sysdate
           , 'XHIBIT'
           , 'XHIBIT'
           ,1);


    --Now set all of the cases for that court under that pub running list that has just been created
    DBMS_OUTPUT.PUT_LINE('Assign all cases for court_id: '||p_court_id||' to new pub_running_list_id: '||v_prl_id);
     OPEN prl_c;
        LOOP
         FETCH prl_c BULK COLLECT INTO prl_tt LIMIT 1000;
         
          IF prl_tt IS NOT NULL AND prl_tt.COUNT > 0 THEN

             FOR i IN prl_tt.FIRST .. prl_tt.LAST
               LOOP
                --Run the updates
                DBMS_OUTPUT.PUT_LINE('Case ID: '||prl_tt(i).case_id||
                                     ' assigned to new pub_running_list_id: '||v_prl_id||
                                     ' from: '||prl_tt(i).pub_running_list_id);
                UPDATE xhb_case xc
                SET pub_running_list_id = v_prl_id
                WHERE xc.case_id = prl_tt(i).case_id
                AND   xc.court_id = p_court_id;
               
               END LOOP;
			      COMMIT;
           END IF;
           EXIT WHEN prl_c%NOTFOUND;
          END LOOP;
      CLOSE prl_c;

      COMMIT;
    DBMS_OUTPUT.PUT_LINE('Executing xhb_populate_pub_running_list.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

 EXCEPTION
   WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001,'Error in xhb_populate_pub_running_list :- ' || v_err_code || ' : ' || v_err_msg);

END xhb_populate_pub_running_list;
/