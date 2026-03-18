/*Initial query with all fields.  This data should be inserted into XHB_COURT_ROOM_SAGE*/
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
            WHERE xcrt.court_id = 81
            AND trunc(xsh.start_time) BETWEEN '05-MAY-2018' AND '12-MAY-2018'
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
        
"SCHEDULED_HEARING_ID"        "AM_HOURS"                    "AM_MINUTES"                  "PM_HOURS"                    "PM_MINUTES"                  "COURT_ROOM_ID"               "SITTING_DATE"                "REF_JUDGE_ID"                
"772454"                      "1"                           "45"                          "0"                           "0"                           "8112"                        "10-MAY-2018"                 "34366"                       
"772461"                      "0"                           "0"                           "2"                           "30"                          "8112"                        "10-MAY-2018"                 "34366"                       
"772448"                      "2"                           "0"                           "0"                           "30"                          "8112"                        "07-MAY-2018"                 "34762"                       
"772458"                      "0"                           "0"                           "2"                           "15"                          "8112"                        "07-MAY-2018"                 "34762"                       
"772464"                      "1"                           "45"                          "1"                           "0"                           "8112"                        "09-MAY-2018"                 "34852"                       
"772460"                      "0"                           "0"                           "2"                           "45"                          "8112"                        "09-MAY-2018"                 "34852"                       
"772450"                      "1"                           "30"                          "0"                           "30"                          "8112"                        "08-MAY-2018"                 "34895"                       
"772459"                      "0"                           "0"                           "2"                           "15"                          "8112"                        "08-MAY-2018"                 "34895"                       
"772404"                      "3"                           "57"                          "4"                           "30"                          "8112"                        "08-MAY-2018"                 ""                            
"772438"                      "0"                           "0"                           "0"                           "-58"                         "8112"                        "08-MAY-2018"                 ""                            
"772456"                      "2"                           "0"                           "0"                           "30"                          "8112"                        "11-MAY-2018"                 ""                            
"772462"                      "0"                           "0"                           "1"                           "15"                          "8112"                        "11-MAY-2018"                 ""                            

--12 rows returned        
        

--Of the rows inserted into XHB_COURT_ROOM_USAGE, no duplicates should be created in XHB_JUDGE_USAGE
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
            WHERE xcrt.court_id = 81
            AND trunc(xsh.start_time) BETWEEN '05-MAY-2018' AND '12-MAY-2018'
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
        select distinct
              inputs.court_room_id
             , trunc(inputs.sitting_date)
             , inputs.ref_judge_id
        from  time_calc
        ,     inputs
        where time_calc.scheduled_hearing_id = inputs.scheduled_hearing_id
        order by inputs.ref_judge_id
        ,        trunc(inputs.sitting_date)
        ,        inputs.court_room_id
        ;
"COURT_ROOM_ID"               "TRUNC(INPUTS.SITTING_DATE)"  "REF_JUDGE_ID"                
"8112"                        "10-MAY-2018"                 "34366"                       
"8112"                        "07-MAY-2018"                 "34762"                       
"8112"                        "09-MAY-2018"                 "34852"                       
"8112"                        "08-MAY-2018"                 "34895"                       
"8112"                        "08-MAY-2018"                 ""                            
"8112"                        "11-MAY-2018"                 ""                            

--After run, there should be 12 rows inserted into XHB_COURT_ROOM and 6 into XHB_JUDGE_USAGE

select * from XHB_COURT_ROOM_USAGE where trunc(creation_date) = trunc(sysdate);

"COURT_ROOM_USAGE_ID"         "COURT_ROOM_ID"               "AM_TIME_CIV_HOURS"           "AM_TIME_CIV_MINS"            "AM_TIME_HOURS"               "AM_TIME_MINS"                "PM_TIME_CIV_HOURS"           "PM_TIME_CIV_MINS"            "PM_TIME_HOURS"               "PM_TIME_MINS"                "SITTING_DATE"                "LAST_UPDATE_DATE"            "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  "VERSION"                     "OBS_IND"                     
"346"                         "8112"                        ""                            ""                            "1"                           "45"                          ""                            ""                            "0"                           "0"                           "10-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"347"                         "8112"                        ""                            ""                            "0"                           "0"                           ""                            ""                            "2"                           "30"                          "10-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"348"                         "8112"                        ""                            ""                            "2"                           "0"                           ""                            ""                            "0"                           "30"                          "07-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"349"                         "8112"                        ""                            ""                            "0"                           "0"                           ""                            ""                            "2"                           "15"                          "07-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"350"                         "8112"                        ""                            ""                            "1"                           "45"                          ""                            ""                            "1"                           "0"                           "09-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"351"                         "8112"                        ""                            ""                            "0"                           "0"                           ""                            ""                            "2"                           "45"                          "09-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"352"                         "8112"                        ""                            ""                            "1"                           "30"                          ""                            ""                            "0"                           "30"                          "08-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"353"                         "8112"                        ""                            ""                            "0"                           "0"                           ""                            ""                            "2"                           "15"                          "08-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"354"                         "8112"                        ""                            ""                            "3"                           "57"                          ""                            ""                            "4"                           "30"                          "08-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"355"                         "8112"                        ""                            ""                            "0"                           "0"                           ""                            ""                            "0"                           "-58"                         "08-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"356"                         "8112"                        ""                            ""                            "2"                           "0"                           ""                            ""                            "0"                           "30"                          "11-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"357"                         "8112"                        ""                            ""                            "0"                           "0"                           ""                            ""                            "1"                           "15"                          "11-MAY-2018"                 "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
--12 rows inserted

select * from xhb_judge_usage where trunc(creation_date) = trunc(sysdate) ;

"JUDGE_USAGE_ID"              "COURT_ROOM_ID"               "COURT_CHAMBERS_IND"          "REF_JUDGE_ID"                "MAIN_WORK_TYPE"              "SITTING_DATE"                "TYPE_OF_WORK"                "LAST_UPDATE_DATE"            "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  "VERSION"                     "OBS_IND"                     
"160"                         "8112"                        "CRT"                         "34366"                       ""                            "10-MAY-2018"                 ""                            "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"161"                         "8112"                        "CRT"                         "34762"                       ""                            "07-MAY-2018"                 ""                            "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"162"                         "8112"                        "CRT"                         "34852"                       ""                            "09-MAY-2018"                 ""                            "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"163"                         "8112"                        "CRT"                         "34895"                       ""                            "08-MAY-2018"                 ""                            "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"164"                         "8112"                        "CRT"                         ""                            ""                            "08-MAY-2018"                 ""                            "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            
"165"                         "8112"                        "CRT"                         ""                            ""                            "11-MAY-2018"                 ""                            "25-OCT-2018"                 "25-OCT-2018"                 "XHIBIT"                      "XHIBIT"                      "1"                           ""                            

--6 rows inserted

--Test successful

ROLLBACK;