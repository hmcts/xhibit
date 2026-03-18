create or replace procedure xhb_populate_ref_calendar (p_court_id IN xhb_court.court_id%TYPE)as
/**
  * DESCRIPTION :
  *   Procedure                  Purpose
  *   ========================== =======
  *   xhb_populate_ref_calendar  CTX-2684 - populate the calendar data table for the next 3 years for each court
  *                              Procedure will be called from a dbms_scheduler job which will run on Jan 1st each year
  *  Assumptions                 If a new court is added to xhb_ref_court, this process will be run manually
***/


    CURSOR courts_c
    IS
    SELECT court_id
    FROM xhb_court
    WHERE (obs_ind <> 'Y' OR obs_ind IS NULL)
    AND court_id = p_court_id
    ORDER BY court_id
    ;

     TYPE dates_rec IS RECORD
    ( CAL_DATE  DATE
    , WEEKDAY   VARCHAR2(10)
    , AVAILABLE VARCHAR2(1)
    );

    TYPE dates_type IS TABLE OF dates_rec;
    dates_tt  dates_type;


    CURSOR dates_cur (p_court_id IN xhb_court.court_id%TYPE)
     IS
      SELECT TRUNC(sysdate) + (rownum -1) as CAL_DATE
      , CASE
         WHEN to_char(sysdate + rownum -1,'DY') IN ('SAT','SUN') THEN 'Weekend'
         --ELSE 'WEEKDAY'
         END WEEKDAY
      , CASE
         WHEN to_char(sysdate + rownum -1,'DY') IN ('SAT','SUN') THEN 'N'
         ELSE 'Y'
         END AVAILABLE
      FROM all_objects
      WHERE ROWNUM <= add_months(TRUNC(sysdate),36) - (TRUNC(sysdate) +1);

   v_row_exist NUMBER;
   v_row_cnt NUMBER;
   v_version NUMBER :=1;
   v_err_code NUMBER;
   v_err_msg  VARCHAR2(100);
   v_ref_calendar_id   xhb_ref_calendar.ref_calendar_id%TYPE;

BEGIN

 FOR courts_r IN courts_c
 LOOP
  DBMS_OUTPUT.PUT_LINE('Processing dates for Court ID: '||courts_r.court_id);
     OPEN dates_cur (courts_r.court_id);
        LOOP
           FETCH dates_cur BULK COLLECT INTO dates_tt LIMIT 1000;

          IF dates_tt IS NOT NULL AND dates_tt.COUNT > 0 THEN

             FOR i IN dates_tt.FIRST .. dates_tt.LAST
               LOOP

			   -- Check to ensure the date does not already exist
			   SELECT count (*)
			   INTO v_row_exist
			   FROM xhb_ref_calendar
			   WHERE court_id = courts_r.court_id
			   AND trunc(cal_date) = trunc(dates_tt(i).cal_date);
			   
			   IF v_row_exist = 0 THEN

				   SELECT xhb_ref_calendar_seq.nextval
				   INTO v_ref_calendar_id
				   FROM dual;

                   --DBMS_OUTPUT.PUT_LINE('Insert a row for date: '||dates_tt(i).cal_date||' Court ID: '||courts_r.court_id);
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
                            VALUES (v_ref_calendar_id
                                   ,dates_tt(i).available
                                   ,trunc(dates_tt(i).cal_date)
                                   ,courts_r.court_id
                                   ,dates_tt(i).weekday
                                   ,'N'
                                   ,'XHIBIT'
                                   ,SYSDATE
                                   ,'XHIBIT'
                                   ,SYSDATE
                                   ,v_version
                                   );
				   END IF;

             END LOOP;
           END IF;
           EXIT WHEN dates_cur%NOTFOUND;
          END LOOP;
      CLOSE dates_cur;
      COMMIT;
 END LOOP;

 EXCEPTION
   WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001,'Error in xhb_populate_ref_calendar :- ' || v_err_code || ' : ' || v_err_msg);

END xhb_populate_ref_calendar;
/