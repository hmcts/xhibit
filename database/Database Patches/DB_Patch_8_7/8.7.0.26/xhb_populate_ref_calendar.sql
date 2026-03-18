create or replace procedure xhb_populate_ref_calendar as 
/** 
  * DESCRIPTION :  
  *   Procedure                  Purpose
  *   ========================== =======
  *   xhb_populate_ref_calendar  CTX-1978 - populate the calendar data table for the next 3 years for each court
  *                              Procedure will be called from a dbms_scheduler job which will run on Jan 1st each year
  *  Assumptions                 If a new court is added to xhb_ref_court, this process will be run manually                
***/

CURSOR dates_c
 IS
  SELECT to_date(sysdate,'dd-mon-yyyy') + rownum -1 as CAL_DATE
  , CASE
     WHEN to_char(to_date(sysdate,'dd-mon-yyyy') + rownum -1,'DY') IN ('SAT','SUN') THEN 'Weekend'
     --ELSE 'WEEKDAY'
     END WEEKDAY
  , CASE
     WHEN to_char(to_date(sysdate,'dd-mon-yyyy') + rownum -1,'DY') IN ('SAT','SUN') THEN 'N'
     ELSE 'Y'
     END AVAILABLE   
  FROM all_objects
  WHERE rownum <= to_date (add_months(sysdate,36),'dd-mon-yyyy') - to_date(sysdate,'dd-mon-yyyy') +1;

 CURSOR courts_c
 IS
  SELECT court_id
  FROM xhb_court
  WHERE obs_ind <> 'Y' OR obs_ind IS NULL;
 
 v_row_exist NUMBER;
 v_row_cnt NUMBER;
 v_version NUMBER;
 v_err_code NUMBER;
 v_err_msg  VARCHAR2(100);
 
BEGIN
  
  FOR dates_r IN dates_c --get all of the dates for the 36 months
   LOOP
    FOR courts_r IN courts_c --pass in each date and check if there is a calendar record for it
     LOOP
     
     SELECT count (*)
     INTO v_row_exist
     FROM xhb_ref_calendar
     WHERE court_id = courts_r.court_id
     AND trunc(to_date(cal_date)) = trunc(to_date(dates_r.cal_date));

      IF v_row_exist = 0 --there are no dates for this court / date
       THEN
        SELECT count(*)
        INTO v_row_cnt
        FROM xhb_ref_calendar
        WHERE court_id = courts_r.court_id;
        
        IF v_row_cnt > 0 --there is a calendar for that court so add 1 to version
         THEN 
           SELECT MAX(version) +1
           INTO v_version
           FROM xhb_ref_calendar
           WHERE court_id = courts_r.court_id; 
         ELSE
         v_version := 1;
        END IF;
       
       --No record exists for that court / date so create a new      
       INSERT INTO xhb_ref_calendar(REF_CALENDAR_ID
                                    ,AVAIL
                                    ,CAL_DATE
                                    ,COURT_ID
                                    ,DESCRIPTION
                                    ,SYS_AC_AVAIL
                                    ,CREATED_BY
                                    ,CREATION_DATE
                                    ,LAST_UPDATED_BY
                                    ,LAST_UPDATE_DATE
                                    ,VERSION)
                VALUES (xhb_ref_calendar_seq.nextval
                       ,dates_r.available
                       ,dates_r.cal_date
                       ,courts_r.court_id
                       ,dates_r.weekday
                       ,'N'
                       ,'XHIBIT'
                       ,SYSDATE
                       ,'XHIBIT'
                       ,SYSDATE
                       ,v_version 
                       );
        END IF;
      END LOOP; --courts_c
    END LOOP;
  
 EXCEPTION
   WHEN OTHERS THEN 
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001,'Error in xhb_populate_ref_calendar :- ' || v_err_code || ' : ' || v_err_msg);
		   
END xhb_populate_ref_calendar;
/