create or replace PROCEDURE xhb_set_case_number_sequences (p_court_id xhb_court.court_id%TYPE)
AS
/**
  * NAME       : xhb_set_case_number_sequences
  * DESCRIPTION: CTX-2452 Select max case_number from xhb_case for the court and case type.  For each row returned 
  *              If there is already a record for the case_type/ court_id then do nothing else create a new record in xhb_case_number_seq_generator and + 100.  
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
  
   v_err_message VARCHAR2(250);
   v_row_exist   NUMBER:=0; --count in the there is a row for the court/type combination
  
  BEGIN
  DBMS_OUTPUT.PUT_LINE('Executing xhb_set_case_number_sequences for court ID: ' || p_court_id || '.  Start Time: ' || to_char(sysdate, 'DD-MM-YYYY HH24:MI:SS') || '.');
   
    v_row_exist := 0; --reset the value after each insert   
   /*Trial*/
    SELECT count(*)
    INTO v_row_exist
    FROM xhb_case_number_seq_generator
    WHERE case_type = 'TRIAL' 
    AND CURRENT_SEQUENCE < 7000
    AND COURT_ID = p_court_id
    ;
   
   IF v_row_exist = 0 THEN
   DBMS_OUTPUT.PUT_LINE('Insert TRIAL sequence for court ID: ' || p_court_id);
     INSERT INTO xhb_case_number_seq_generator(CASE_TYPE
                                             , COURT_ID
                                             , CURRENT_SEQUENCE
                                              )
         VALUES ('TRIAL'
               , p_court_id
               , NVL((SELECT MAX(substr(case_number, 5, 4)) + 100
                      FROM xhb_case 
                      WHERE court_id = p_court_id 
                      AND case_type = 'T' 
                      AND substr(case_number, 1, 4) = to_number(to_char(sysdate, 'YYYY'))
                      AND substr(case_number, 5, 4) < 7000)
                  , 1)
                );
    END IF;
   v_row_exist := 0; --reset the value after each insert
    /*Trial Indictment*/
    SELECT count(*)
    INTO v_row_exist
    FROM xhb_case_number_seq_generator
    WHERE case_type = 'TRIAL_INDICTMENT' 
    AND CURRENT_SEQUENCE >= 7000
    AND COURT_ID = p_court_id
    ;
 
   IF v_row_exist = 0
    THEN
    DBMS_OUTPUT.PUT_LINE('Insert TRIAL INDICTMENT sequence for court ID: '||p_court_id);
    INSERT INTO xhb_case_number_seq_generator(CASE_TYPE
                                           , COURT_ID
                                           , CURRENT_SEQUENCE
                                            )
     VALUES ('TRIAL INDICTMENT'
            ,p_court_id
            ,NVL((SELECT MAX(substr(case_number, 5, 4)) + 100
                  FROM xhb_case 
                  WHERE court_id = p_court_id 
                  AND case_type = 'T' 
                  AND substr(case_number, 1, 4) = to_number(to_char(sysdate, 'YYYY'))
                  AND substr(case_number, 5, 4) >= 7000)
              , 7000)
            );
  END IF;
    
    v_row_exist := 0; --reset the value after each insert
   /*Sentence*/ 
   SELECT count(*)
   INTO v_row_exist
   FROM xhb_case_number_seq_generator
   WHERE case_type = 'SENTENCE'
   AND COURT_ID = p_court_id
    ; 
   
   IF v_row_exist = 0
    THEN
    DBMS_OUTPUT.PUT_LINE('Insert SENTENCE sequence for court ID: '||p_court_id);
       INSERT INTO xhb_case_number_seq_generator(CASE_TYPE
                                                ,COURT_ID
                                                ,CURRENT_SEQUENCE
                                                )
       VALUES ('SENTENCE'
              ,p_court_id
              ,NVL((SELECT MAX(substr(case_number,5,4))+100
                    FROM xhb_case 
                    WHERE court_id = p_court_id 
                    AND case_type = 'S' 
                    AND substr(case_number,1,4) = to_number(to_char(sysdate,'YYYY'))
                    )
                ,1)
              );
    END IF;
    v_row_exist := 0; --reset the value after each insert
  /*Appeal*/
   SELECT count(*)
   INTO v_row_exist
   FROM xhb_case_number_seq_generator
   WHERE case_type = 'APPEAL' 
   AND COURT_ID = p_court_id
    ;
    
   IF v_row_exist = 0
    THEN 
    DBMS_OUTPUT.PUT_LINE('Insert APPEAL sequence for court ID: '||p_court_id);
       INSERT INTO xhb_case_number_seq_generator(CASE_TYPE
                                                ,COURT_ID
                                                ,CURRENT_SEQUENCE
                                                )
       VALUES ('APPEAL'
              ,p_court_id
              ,NVL((SELECT MAX(substr(case_number,5,4))+100
                    FROM xhb_case 
                    WHERE court_id = p_court_id 
                    AND case_type = 'A' 
                    AND substr(case_number,1,4) = to_number(to_char(sysdate,'YYYY')))
                ,1)
              );
    END IF;
    
    v_row_exist := 0; --reset the value after each insert
    
    /*B*/
   SELECT count(*)
   INTO v_row_exist
   FROM xhb_case_number_seq_generator
   WHERE case_type = 'B'
   AND COURT_ID = p_court_id;
   
    IF v_row_exist = 0
     THEN
     DBMS_OUTPUT.PUT_LINE('Insert B sequence for court ID: '||p_court_id); 
       INSERT INTO xhb_case_number_seq_generator(CASE_TYPE
                                                ,COURT_ID
                                                ,CURRENT_SEQUENCE
                                                )
       VALUES ('B'
              ,p_court_id
              ,NVL((SELECT MAX(substr(case_number, 5, 4) )+ 100
                    FROM xhb_case 
                    WHERE court_id = p_court_id 
                    AND case_type = 'B' 
                    AND substr(case_number,1,4) = to_number(to_char(sysdate,'YYYY')))
                ,1)
              );
    END IF;       
   
   v_row_exist := 0; --reset the value after each insert
   /*U*/
   SELECT count(*)
   INTO v_row_exist
   FROM xhb_case_number_seq_generator
   WHERE case_type = 'U'
   AND COURT_ID = p_court_id; 
   
   IF v_row_exist = 0
     THEN
      DBMS_OUTPUT.PUT_LINE('Insert U sequence for court ID: '||p_court_id); 
       INSERT INTO xhb_case_number_seq_generator(CASE_TYPE
                                               , COURT_ID
                                               , CURRENT_SEQUENCE
                                                )
       VALUES ('U'
             , p_court_id
             , NVL((SELECT MAX(substr(case_number, 5, 4)) + 100
                    FROM xhb_case 
                    WHERE court_id = p_court_id 
                    AND case_type = 'U' 
                    AND substr(case_number, 1, 4) = to_number(to_char(sysdate,'YYYY')))
                ,1)
              );
    END IF;
  
    
    DBMS_OUTPUT.PUT_LINE('Executing xhb_set_case_number_sequences.  End Time: ' || to_char(sysdate, 'DD-MM-YYYY HH24:MI:SS')||'.');
  
  COMMIT;
  
  EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN xhb_set_case_number_sequences for CREST_COURT : '||p_court_id||'-'||SUBSTR(v_err_message, 1, 150));

  
 END xhb_set_case_number_sequences;
/
show errors
