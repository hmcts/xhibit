create or replace PROCEDURE xhb_populate_rcs_screen (p_court_id  IN xhb_court.court_id%TYPE
                                                    ,p_from_date IN DATE
                                                    ,p_to_date   IN DATE)
AS 
/**
* CGI DREST TO XHIBIT Program
*
* MODULE      : xhb_populate_rcs_screen
*
* DESCRIPTION : Populate XHB_COURTROOM_USAGE and XHB_JUDGE_USAGE tables from hearing and sitting tables
*             : Jira Ticket ctx-2896
*
* VERSION HISTORY:
*
* Date          Author (s)                           Version    Nature of Change
* ----------    --------------------------------     --------   -----------------------------------------------------------------------
* 23/10/2018    C.Cash.                               1.0       Initial Version 
* 
**/

TYPE xhb_rcs_rec IS RECORD
    ( 
      scheduled_hearing_id     xhibit.xhb_scheduled_hearing.scheduled_hearing_id%TYPE
    --, sitting_start_date       DATE--xhibit.xhb_scheduled_hearing.start_time%TYPE
    --, sitting_end_date         DATE--xhibit.xhb_scheduled_hearing.end_time%TYPE
    , am_hours                 NUMBER
    , am_minutes               NUMBER
    , pm_hours                 NUMBER
    , pm_minutes               NUMBER
    , court_room_id            xhibit.xhb_court_room.court_room_id%TYPE
    , sitting_date             xhibit.xhb_scheduled_hearing.start_time%TYPE 
    , ref_judge_id             xhibit.xhb_sitting.ref_judge_id%TYPE
    );

    TYPE xhb_rcs_type IS TABLE OF xhb_rcs_rec;
    xhb_rcs_tt  xhb_rcs_type;


 CURSOR xhb_rcs_cur IS
  WITH
  inputs as (
            select xsh.scheduled_hearing_id
            , xsh.start_time as sitting_start_date
            , xsh.end_time as sitting_end_date
            , null
            , xcr.court_room_id
            , xsh.start_time AS sitting_date
            , xs.ref_judge_id
            FROM xhb_court xcrt
            ,    xhb_court_site xcs
            ,    xhb_court_room xcr
            ,    xhb_sitting xs
            ,    xhb_scheduled_hearing xsh
            WHERE xcrt.court_id = p_court_id
            AND trunc(xsh.start_time) BETWEEN p_from_date AND p_to_date
            AND xcrt.court_id = xcs.court_id
            AND xcs.court_site_id = xcr.court_site_id
            AND xs.court_room_id = xcr.court_room_id
            AND xs.court_site_id = xcs.court_site_id
            AND xsh.sitting_id = xs.sitting_id
            AND NVL(xcrt.obs_ind,'-') <> 'Y'
            AND NVL(xcs.obs_ind,'-') <> 'Y'
            AND NVL(xcr.obs_ind,'-') <> 'Y'
              )
        , prep as (
            select sitting_start_date
                 , sitting_end_date
                 , scheduled_hearing_id
                 , case when sitting_end_date <= trunc(sitting_start_date) + interval '16:30' hour to minute
                        then sitting_end_date
                        else trunc(sitting_start_date) + interval '16:30' hour to minute --latest time a court time can be is 16:30
                   end  as adj_end_date
                 , cast(trunc(sitting_start_date) as timestamp) + interval '12' hour as noon --get the difference between start time and noon
            from   inputs
          )
        , time_calc as (
            select to_char(sitting_start_date,'Dd-MON-YYYY HH24:MI:SS') sitting_start_date
                 , to_char(sitting_end_date,'Dd-MON-YYYY HH24:MI:SS') sitting_end_date
                 , least   (noon, adj_end_date) - least   (noon, sitting_start_date) as am_time
                 , greatest(noon, adj_end_date) - greatest(noon, sitting_start_date) as pm_time
                 , scheduled_hearing_id
            from   prep
        )
        select inputs.scheduled_hearing_id
             --, to_date(trunc(time_calc.sitting_start_date)) sitting_start_date
             --, to_date(trunc(time_calc.sitting_end_date)) sitting_end_date
             , extract(hour   from am_time) as am_hours
             , extract(minute from am_time) as am_minutes
             , extract(hour   from pm_time) as pm_hours
             , extract(minute from pm_time) as pm_minutes
             , inputs.court_room_id
             , inputs.sitting_date
             , inputs.ref_judge_id
        from  time_calc
        ,     inputs
        where time_calc.scheduled_hearing_id = inputs.scheduled_hearing_id
        order by inputs.ref_judge_id
        ,        inputs.sitting_date
        ,        inputs.court_room_id
        ;

 v_xcr_ins_rows       NUMBER := 0; --get a count of the number of xcr rows inserted
 v_xju_ins_rows       NUMBER := 0; --get a count of the number of xju rows inserted
 v_curr_ref_judge_id  xhb_judge_usage.ref_judge_id%TYPE :=0;
 v_next_ref_judge_id  xhb_judge_usage.ref_judge_id%TYPE :=0;
 v_curr_sitting_date  xhibit.xhb_scheduled_hearing.start_time%TYPE :=(sysdate-100); 
 v_next_sitting_date  xhibit.xhb_scheduled_hearing.start_time%TYPE :=(sysdate-100); 
 v_curr_court_room_id xhibit.xhb_court_room.court_room_id%TYPE :=0;
 v_next_court_room_id xhibit.xhb_court_room.court_room_id%TYPE :=0;
 v_ju_row_cnt         NUMBER:=0;
 
BEGIN
  
  OPEN xhb_rcs_cur;
    LOOP
     FETCH xhb_rcs_cur BULK COLLECT INTO xhb_rcs_tt LIMIT 1000;
    
      IF xhb_rcs_tt IS NOT NULL AND xhb_rcs_tt.COUNT > 0 THEN
       FOR i IN xhb_rcs_tt.FIRST .. xhb_rcs_tt.LAST 
        LOOP
          --1st iteration set the variables
           v_curr_ref_judge_id  := NVL(xhb_rcs_tt(i).ref_judge_id,00000);
           v_curr_sitting_date  := trunc(xhb_rcs_tt(i).sitting_date);
           v_curr_court_room_id := xhb_rcs_tt(i).court_room_id;
        
            DBMS_OUTPUT.PUT_LINE('curr sitting date : '||v_curr_sitting_date||' Old sitting date : '||v_next_sitting_date||CHR(10)||
                                 ' curr cr ID : '||v_curr_court_room_id||'cr ID : '||v_next_court_room_id||CHR(10)||
                                 ' curr judge : '||v_curr_ref_judge_id||' Next Judge : '||v_next_ref_judge_id||CHR(10)
                             );
        
         
           INSERT INTO xhb_court_room_usage  (court_room_usage_id
                                     ,court_room_id
                                     ,sitting_date
                                     ,am_time_hours
                                     ,am_time_mins
                                     ,pm_time_hours
                                     ,pm_time_mins)
           VALUES (xhb_court_room_usage_seq.nextval
           , xhb_rcs_tt(i).court_room_id
           , xhb_rcs_tt(i).sitting_date
           , xhb_rcs_tt(i).am_hours
           , xhb_rcs_tt(i).am_minutes
           , xhb_rcs_tt(i).pm_hours
           , xhb_rcs_tt(i).pm_minutes
           );

          v_xcr_ins_rows := SQL%ROWCOUNT;
          DBMS_OUTPUT.PUT_LINE(' ');
          DBMS_OUTPUT.PUT_LINE('CTX-2896:XHB_COURTROOM_USAGE inserts - Court Id: '||p_court_id||' For sittings between : '||p_from_date||' and : '||p_to_date||' - Inserted no of rows : '||v_xcr_ins_rows);
          
           IF v_curr_ref_judge_id = v_next_ref_judge_id AND 
              v_curr_sitting_date = v_next_sitting_date AND
              v_curr_court_room_id = v_next_court_room_id
            THEN
             NULL;
             
             ELSE             
             /*Check that the current row is not in the table as it may have been created outside of this run*/
             SELECT count(*)
             INTO v_ju_row_cnt
             FROM xhb_judge_usage
             WHERE trunc(sitting_date) = trunc(xhb_rcs_tt(i).sitting_date)
             AND court_room_id =  xhb_rcs_tt(i).court_room_id
             AND ref_judge_id = xhb_rcs_tt(i).ref_judge_id;
             
             
             IF v_ju_row_cnt = 0 AND xhb_rcs_tt(i).ref_judge_id IS NOT NULL
              THEN
               /*Load into the xhb_judge_usage table but do not create duplicates*/
               INSERT INTO xhb_judge_usage (judge_usage_id
                                           ,court_room_id
                                           ,sitting_date
                                           ,court_chambers_ind
                                           ,ref_judge_id)
               VALUES (xhb_judge_usage_seq.nextval
               , xhb_rcs_tt(i).court_room_id
               , xhb_rcs_tt(i).sitting_date
               , 'CRT'
               , xhb_rcs_tt(i).ref_judge_id
               );
               ELSE NULL;
              END IF;
          END IF; 
          
           v_next_ref_judge_id  := NVL(xhb_rcs_tt(i).ref_judge_id,00000);
           v_next_sitting_date  := trunc(xhb_rcs_tt(i).sitting_date);
           v_next_court_room_id := xhb_rcs_tt(i).court_room_id;                
         
        
        END LOOP;
      END IF;
      EXIT WHEN xhb_rcs_cur%NOTFOUND;
    END LOOP;
        
  CLOSE xhb_rcs_cur;                           
  
  --COMMIT;

  EXCEPTION
    WHEN OTHERS THEN
    ROLLBACK;
         DBMS_OUTPUT.PUT_LINE('!!! AN ERROR OCCURRED xhb_populate_rcs_screen: '||p_court_id||' ERROR: '||SUBSTR(SQLERRM,1,110));
END xhb_populate_rcs_screen;
/