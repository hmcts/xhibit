/*************************************************************************************************/
/*                                                                                               */
/*  21/01/2019 - S.Sethuraman  - Script created                                                  */
/*                                                                                               */
/*  CTX_3576 : DM v0.25 - Sec 4.2.2 - Create DUMMY CourtSites                                    */
/*             This is a One off Script to create DUMMY CourtSite records in XHIBIT database     */
/*             This script doesn;t expect any parameters                                         */
/*                                                                                               */
/*  14/02/2019  :  S Sethuraman - CTX_3707 : DM FS V0.27 - Sec 4.2.2 - DEFAULT DISPLAY NAME SET  */
/*                                           for COURT_SITE and COURTROOM                        */
/*************************************************************************************************/
/*************** Create Dummy CourtSite and Address records in XHIBIT               **************/
DECLARE
    v_count_number_of_rows   NUMBER := 0;
    v_court_id               xhibit.xhb_court.court_id%TYPE;
    v_address_id             xhibit.xhb_address.address_id%TYPE;
    v_court_site_id          xhibit.xhb_court_site.court_site_id%type;
    v_err_message            xhbstg_lists_dm.xhibit_etl_err_message%TYPE;
BEGIN
DBMS_OUTPUT.ENABLE(1000000);
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
DBMS_OUTPUT.PUT_LINE('CTX_3576 : Create Dummy CourtSite and XHB_ADDRESS records in XHIBIT database');
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Creating Dummy XHB_CourtSite records and XHB_ADDRESS records in XHIBIT DATABASE');

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
         ,9999
         ,'CTX-3576_CREATE_DUMMY_COURTSITE records in XHIBIT - CTX-3576'
         ,SYSDATE 
         ,'I'
         ,'CTX-3576 - Starting process of inserting  Dummy ADDRESS records into XHB_ADDRESS / XHB_COURT_SITE for each XHB_COURT.court_id which is IN_SERVICE'
         ,NULL
         ,NULL
         ,SYSDATE
         ,'DATA MIGRATION'
         ,'DATA MIGRATION'
         );


    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Starting process of inserting  Dummy ADDRESS records into XHB_ADDRESS for each XHB_COURT.court_id which is IN_SERVICE');    

    FOR i in (
        SELECT distinct court_id 
          from xhibit.xhb_court  
         where in_service_flag = 'Y')
    LOOP
          BEGIN 
           
           v_court_id := i.court_id;
               
           BEGIN
              SELECT XHIBIT.XHB_ADDRESS_SEQ.NEXTVAL into v_address_id from dual;
           END;

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
                                        ,v_court_id
                                        ,'CTX-3576_CREATE_DUMMY_COURTSITE records'
                                        ,SYSDATE 
                                        ,'I'
                                        ,'INSERTING DUMMY XHB_ADDRESS - for COURT_ID '||i.court_id
                                        ,NULL
                                        ,NULL
                                        ,SYSDATE
                                        ,'DATA MIGRATION'
                                        ,'DATA MIGRATION'
                                        );
          
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'INSERTING DUMMY XHB_ADDRESS - for COURT_ID '||i.court_id);         
            
         -- INSERT DUMMY XHB_ADDRESS record  
                     INSERT INTO  xhibit.xhb_address
                                  ( address_id
                                  , address_1 
                                  , address_2   
                                  , address_3
                                  , address_4
                                  , town
                                  , county
                                  , postcode
                                  , country
                                  , last_update_date
                                  , creation_date
                                  , created_by
                                  , last_updated_by
                                  , version )
                           VALUES 
                                  (v_address_id
                                  ,'Court site for assigning'
                                  ,'Cases being heard in'
                                  ,'Other Crown Courts'
                                  , NULL
                                  , NULL
                                  , NULL
                                  , NULL
                                  , NULL
                                  , SYSDATE
                                  , SYSDATE
                                  , 'DATA_MIGRATION'
                                  , 'DATA_MIGRATION'                                                                                                      
                                  , '1');

    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'INSERTING DUMMY XHB_COURT_SITE - for COURT_ID '||i.court_id);         
 
           BEGIN
              SELECT XHIBIT.XHB_COURT_SITE_SEQ.NEXTVAL into v_court_site_id from dual;
           END;


         -- INSERT DUMMY XHB_COURT_SITE record  
                     INSERT INTO  xhibit.xhb_court_site
                                  ( court_site_id
                                  , court_site_name 
                                  , court_site_code   
                                  , court_id
                                  , address_id
                                  , last_update_date
                                  , creation_date
                                  , created_by
                                  , last_updated_by
                                  , version
                                  , obs_ind
                                  , display_name
                                  , crest_court_id
                                  , short_name
                                  , site_group
                                  , floater_text
                                  , list_name
                                  , tier )
                           VALUES 
                                  (v_court_site_id
                                  ,'OTHER CROWN COURT SITTINGS'
                                  ,'X'
                                  , v_court_id
                                  , v_address_id
                                  , SYSDATE
                                  , SYSDATE
                                  , 'DATA_MIGRATION'
                                  , 'DATA_MIGRATION'                                                                                                      
                                  , '1'
                                  , 'N'
                                  , 'DUMMY SITE' -- ctx-3707
                                  , NULL
                                  , 'X'
                                  , NULL
                                  , NULL
                                  , NULL
                                  , NULL);
                                    
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'INSERTING DUMMY XHB_COURT_ROOM - for COURT_ID '||i.court_id);         
 
         -- INSERT DUMMY XHB_COURT_ROOM record  
       INSERT INTO  xhibit.xhb_court_room
                                  ( court_room_id
                                  , court_room_name 
                                  , description   
                                  , crest_court_Room_no
                                  , court_site_id
                                  , last_update_date
                                  , creation_date
                                  , created_by
                                  , last_updated_by
                                  , version
                                  , obs_ind
                                  , display_name
                                  , security_ind
                                  , video_ind )
                           VALUES 
                                  (xhibit.xhb_court_room_seq.nextval
                                  ,'OTHER CROWN COURTS SITTINGS'
                                  ,'Dummy Courtroom to assign Cases being heard in other Courts'
                                  ,'1'
                                  , v_court_site_id
                                  , SYSDATE
                                  , SYSDATE
                                  , 'DATA_MIGRATION'
                                  , 'DATA_MIGRATION'                                                                                                      
                                  , '1'
                                  , 'N'
                                  , 'Dummy Courtroom' -- ctx-3707
                                  , NULL
                                  , NULL);
    

    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTX-3576_CREATE_DUMMY_COURTSITE records Court : '||v_court_id||' added successfuly!');
    
    v_count_number_of_rows := v_count_number_of_rows + 1;
    
        EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'!!! ERROR HAS OCCURRED IN CTX-3576_CREATE_DUMMY_COURTSITE records for COURT : '||v_court_id||'-'||SUBSTR(v_err_message,1,100));
        
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
                                        ,v_court_id
                                        ,'CTX-3576_CREATE_DUMMY_COURTSITE records'
                                        ,SYSDATE 
                                        ,'E'
                                        ,'CTX-3576_CREATE_DUMMY_COURTSITE records- Error inserting for court : '||v_court_id||' Error: '||SUBSTR(v_err_message,1,150)
                                        ,NULL
                                        ,v_count_number_of_rows
                                        ,SYSDATE
                                        ,'DATA MIGRATION'
                                        ,'DATA MIGRATION'
                                        );
        END; 
 
    END LOOP;

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
                                        ,v_court_id
                                        ,'CTX-3576_CREATE_DUMMY_COURTSITE records'
                                        ,SYSDATE 
                                        ,'I'
                                        ,'CTX-3576_CREATE_DUMMY_COURTSITE records : Inserted '||v_count_number_of_rows||' successfully!'
                                        ,NULL
                                        ,v_count_number_of_rows
                                        ,SYSDATE
                                        ,'DATA MIGRATION'
                                        ,'DATA MIGRATION'
                                        );

    COMMIT;
                       
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTX-3576_CREATE_DUMMY_COURTSITE records inserted '||v_count_number_of_rows||' successfully!');

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'!!! ERROR HAS OCCURRED IN CTX-3576_CREATE_DUMMY_COURTSITE records for COURT : '||v_court_id||SUBSTR(v_err_message,1,100));

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
                                        ,v_court_id
                                        ,'CTX-3576_CREATE_DUMMY_COURTSITE records'
                                        ,SYSDATE 
                                        ,'E'
                                        ,'CTX-3576_CREATE_DUMMY_COURTSITE records - Error inserting for COURT : '||v_court_id||' Error: '||SUBSTR(v_err_message,1,150)
                                        ,NULL
                                        ,v_count_number_of_rows
                                        ,SYSDATE
                                        ,'DATA MIGRATION'
                                        ,'DATA MIGRATION'
                                        );
                                        
        COMMIT;
END;
/
