CREATE OR REPLACE PROCEDURE fix_xhb_address_postcode(p_court_id IN xhibit.xhb_court.court_id%TYPE)
/*************************************************************************************************/
/*                                                                                               */
/*  03/04/2019 - S.Sethuraman  - Script created                                                  */
/*                                                                                               */
/*  CFRA_634/ CTX_3938 : XHB_ADDRESS.POSTCODE column to be fixed for invalid format having       */
/*             multiple and trailing spaces                                                      */
/*             This is a One off Script to fix postcode with more than one space in it or with   */
/*             trailing or leading space in it                                                   */
/*             currently   addresses for solicitor firms and prosecution agencies handled        */            
/*************************************************************************************************/
AS
TYPE xhb_add_rec IS RECORD
    ( 
      court_id                     xhibit.xhb_court.court_id%TYPE
     ,address_id                   xhibit.xhb_address.address_id%TYPE
     ,old_postcode                 xhibit.xhb_address.postcode%TYPE
     ,new_postcode                 xhibit.xhb_address.postcode%TYPE
    );

    TYPE xhb_add_type IS TABLE OF xhb_add_rec;
    xhb_add_tt  xhb_add_type;
    
   CURSOR cur_sol_add_details IS
   select p_court_id as court_id,
         xadd.address_id,
         xadd.postcode as old_postcode,
         regexp_replace(trim(xadd.postcode),' {2,}',' ') new_postcode
    from xhibit.xhb_address xadd, xhibit.xhb_ref_solicitor_firm xrs
where xrs.court_id =  p_court_id and
      xrs.address_id = xadd.address_id and
      nvl(xrs.obs_ind,'N') <> 'Y' and
      (regexp_like(xadd.postcode,'^[ ]+.*$') or
       regexp_like(xadd.postcode,'^.*[ ]+$') or
       regexp_like(xadd.postcode,' {2,}'));
       
         CURSOR cur_pr_add_details IS
   select p_court_id as court_id,
         xadd.address_id,
         xadd.postcode as old_postcode,
         regexp_replace(trim(xadd.postcode),' {2,}',' ') new_postcode
    from xhibit.xhb_address xadd, xhibit.xhb_ref_prosecutor_agency xps
where xps.court_id =  p_court_id and
      xps.address_id = xadd.address_id and
      nvl(xps.obs_ind,'N') <> 'Y' and
      (regexp_like(xadd.postcode,'^[ ]+.*$') or
       regexp_like(xadd.postcode,'^.*[ ]+$') or
       regexp_like(xadd.postcode,' {2,}'));
  
    v_count_number_of_rows   NUMBER := 0;
    v_add_upd_rows      NUMBER := 0;
    v_court_id               xhibit.xhb_court.court_id%TYPE;
    v_address_id             xhibit.xhb_address.address_id%TYPE;
    v_old_postcode           xhibit.xhb_address.postcode%type;
    v_new_postcode           xhibit.xhb_address.postcode%type;
    v_err_message            varchar2(2000);
BEGIN
DBMS_OUTPUT.ENABLE(1000000);
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
DBMS_OUTPUT.PUT_LINE('CTX_3938 : Fix Solicitor firms and prosecution agencies address postcode in XHB_ADDRESS ');
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Fixing Solicitor firms and prosecution agencies address postcode in XHB_ADDRESS XHIBIT DATABASE');

INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX-3938_FIX_SOLICITOR_FIRM_POSTCODES in XHB_ADDRESS - CTX-3938'
         ,SYSDATE 
         ,'I'
         ,'CTX-3938 - Starting process of fixing Solicitor firm postcodes in XHB_ADDRESS for court id - '||p_court_id
         ,NULL
         ,NULL
         ,SYSDATE
         ,'DATA MIGRATION'
         ,'DATA MIGRATION'
         );


    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTX-3938 - Starting process of fixing Solicitor firm postcodes in XHB_ADDRESS for court id - '||p_court_id);    

   OPEN cur_sol_add_details;
    LOOP
    FETCH cur_sol_add_details BULK COLLECT INTO xhb_add_tt;
   
    IF xhb_add_tt IS NOT NULL AND xhb_add_tt.COUNT > 0 THEN
   
        FOR i IN xhb_add_tt.FIRST .. xhb_add_tt.LAST LOOP
    
          BEGIN 

 -- Should the insert fail we want to capture the failing row in the exception block so hold them here before update
            v_address_id     := xhb_add_tt(i).address_id;
            v_old_postcode     := xhb_add_tt(i).old_postcode;
            v_new_postcode     := xhb_add_tt(i).new_postcode;

            DBMS_OUTPUT.PUT_LINE('v_address_id : '||v_address_id||', v_old_postcode : '||v_old_postcode||' , v_new_postcode : '||v_new_postcode||' , p_court_id '||p_court_id);         
 
 INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX_3938_fix_xhb_address_postcode'
         ,SYSDATE 
         ,'I'
         ,'FIXING SOL XHB_ADDRESS : address_id - '||xhb_add_tt(i).address_id||', old_postcode - '||xhb_add_tt(i).old_postcode||', new postcode - '||xhb_add_tt(i).new_postcode
         ,NULL
         ,NULL
         ,SYSDATE
         ,'POST DATA MIGRATION'
         ,'POST DATA MIGRATION'
         );
                       
         
                         UPDATE XHIBIT.XHB_ADDRESS 
                            set postcode = xhb_add_tt(i).new_postcode
                          where address_id = xhb_add_tt(i).address_id and
                                postcode = xhb_add_tt(i).old_postcode;
                         
   v_add_upd_rows := SQL%ROWCOUNT;
  
        EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN fix_xhb_address_postcode for COURT : '||p_court_id||',address_id : '||v_address_id||' , old_postcode : '||v_old_postcode||' , v_new_postcode : '||v_new_postcode||'-'||SUBSTR(v_err_message,1,100));
 
 INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX_3938_fix_xhb_address_postcode'
         ,SYSDATE 
         ,'E'
         ,'fix_xhb_address_postcode - Error fixing SOL court_id : '||p_court_id||' , address_id : '||v_address_id||' , v_old_postcode : '||v_old_postcode||', v_new_postcode : '||v_new_postcode||'- Error: '||SUBSTR(v_err_message,1,150)
         ,NULL
         ,v_count_number_of_rows
         ,SYSDATE
         ,'POST DATA MIGRATION'
         ,'POST DATA MIGRATION'
         );       


        END; 
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_add_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_sol_add_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_sol_add_details;

 INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX_3938_fix_xhb_address_postcode'
         ,SYSDATE 
         ,'I'
         ,'Solicitor firm address postcode : FIXED '||v_count_number_of_rows||' successfully!'
         ,NULL
         ,v_count_number_of_rows
         ,SYSDATE
         ,'POST DATA MIGRATION'
         ,'POST DATA MIGRATION'
         );     
         
                        
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3938:Solicitor firm address postcode : FIXED '||v_count_number_of_rows||' successfully!'||' for COURT_ID : '||p_court_id);
    
    COMMIT;
    
    xhb_add_tt := null;
    v_count_number_of_rows := 0;
    
    /*******  PROSECUTOR AGENCY ADDRESS POSTCODE FIX ***********/
    
    INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX-3938_FIX_PROS_AGENCY_POSTCODES in XHB_ADDRESS - CTX-3938'
         ,SYSDATE 
         ,'I'
         ,'CTX-3938 - Starting process of fixing Prosecuoter agency postcodes in XHB_ADDRESS for court id - '||p_court_id
         ,NULL
         ,NULL
         ,SYSDATE
         ,'DATA MIGRATION'
         ,'DATA MIGRATION'
         );
    
 OPEN cur_pr_add_details;
    LOOP
    FETCH cur_pr_add_details BULK COLLECT INTO xhb_add_tt;
   
    IF xhb_add_tt IS NOT NULL AND xhb_add_tt.COUNT > 0 THEN
   
        FOR i IN xhb_add_tt.FIRST .. xhb_add_tt.LAST LOOP
    
          BEGIN 

 -- Should the insert fail we want to capture the failing row in the exception block so hold them here before update
            v_address_id     := xhb_add_tt(i).address_id;
            v_old_postcode     := xhb_add_tt(i).old_postcode;
            v_new_postcode     := xhb_add_tt(i).new_postcode;

            DBMS_OUTPUT.PUT_LINE('v_address_id : '||v_address_id||', v_old_postcode : '||v_old_postcode||' , v_new_postcode : '||v_new_postcode||' , p_court_id '||p_court_id);         
 
 INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX_3938_fix_xhb_address_postcode'
         ,SYSDATE 
         ,'I'
         ,'FIXING PROS XHB_ADDRESS : address_id - '||xhb_add_tt(i).address_id||', old_postcode - '||xhb_add_tt(i).old_postcode||', new postcode - '||xhb_add_tt(i).new_postcode
         ,NULL
         ,NULL
         ,SYSDATE
         ,'POST DATA MIGRATION'
         ,'POST DATA MIGRATION'
         );
                       
         
                         UPDATE XHIBIT.XHB_ADDRESS 
                            set postcode = xhb_add_tt(i).new_postcode
                          where address_id = xhb_add_tt(i).address_id and
                                postcode = xhb_add_tt(i).old_postcode;
                         
   v_add_upd_rows := SQL%ROWCOUNT;
  
        EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN fix_xhb_address_postcode for COURT : '||p_court_id||',address_id : '||v_address_id||' , old_postcode : '||v_old_postcode||' , v_new_postcode : '||v_new_postcode||'-'||SUBSTR(v_err_message,1,100));
 
 INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX_3938_fix_xhb_address_postcode'
         ,SYSDATE 
         ,'E'
         ,'fix_xhb_address_postcode - Error fixing PROS court_id : '||p_court_id||' , address_id : '||v_address_id||' , v_old_postcode : '||v_old_postcode||', v_new_postcode : '||v_new_postcode||'- Error: '||SUBSTR(v_err_message,1,150)
         ,NULL
         ,v_count_number_of_rows
         ,SYSDATE
         ,'POST DATA MIGRATION'
         ,'POST DATA MIGRATION'
         );       


        END; 
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_add_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_pr_add_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_pr_add_details;

 INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX_3938_fix_xhb_address_postcode'
         ,SYSDATE 
         ,'I'
         ,'Prosecutor agency address postcode : FIXED '||v_count_number_of_rows||' successfully!'
         ,NULL
         ,v_count_number_of_rows
         ,SYSDATE
         ,'POST DATA MIGRATION'
         ,'POST DATA MIGRATION'
         );     
         
                        
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3938:Prosecutor agency address postcode : FIXED '||v_count_number_of_rows||' successfully!'||' for COURT_ID : '||p_court_id);
    
    COMMIT;    
    
 EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
      
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN fix_xhb_address_postcode for COURT : '||p_court_id||',address_id : '||v_address_id||' , v_old_postcode : '||v_old_postcode||' ,v_new_postcode : '||v_new_postcode||'-'||SUBSTR(v_err_message,1,100));

 INSERT INTO DATA_MIG.xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (DATA_MIG.xhbstg_data_migration_log_seq.nextval 
         ,p_court_id
         ,'CTX_3938_fix_xhb_address_postcode'
         ,SYSDATE 
         ,'I'
         ,'Solicitor/Prosecutor address postcode : FIXED '||v_count_number_of_rows||' successfully!'
         ,NULL
         ,v_count_number_of_rows
         ,SYSDATE
         ,'POST DATA MIGRATION'
         ,'POST DATA MIGRATION'
         ); 

        COMMIT;
                    
END fix_xhb_address_postcode;
/                           
