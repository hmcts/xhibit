create or replace procedure xhb_populate_ref_calendar_all as
/**
  * DESCRIPTION :
  *   Procedure                      Purpose
  *   ==========================     =======
  *   xhb_populate_ref_calendar_all  CTX-2684 - populate the calendar data table for the next 3 years for all courts 
  *                                  by calling procedure  xhb_populate_ref_calendar for each court in turn.  This proc 
  *                                  will be called from a dbms_scheduler job which will run on Jan 1st each year.
  *  Assumptions                     If a new court is added to xhb_ref_court, xhb_populate_ref_calendar will be run 
  *                                  manually to populate the calendar for the new court.
***/


    CURSOR courts_c
    IS
    SELECT court_id
    FROM   xhb_court
    WHERE  (nvl(obs_ind, 'N') <> 'Y' )
    AND    is_pilot = 'Y' 
    ORDER BY court_id;

    v_err_code NUMBER;
    v_err_msg  VARCHAR2(100);

BEGIN

    FOR courts_r IN courts_c
    LOOP
	    -- Call xhb_populate_ref_calendar for each court in turn.
        xhb_populate_ref_calendar (courts_r.court_id);
    END LOOP;

    EXCEPTION
        WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           raise_application_error(-20001, 'Error in xhb_populate_ref_calendar_all :- ' || v_err_code || ' : ' || v_err_msg);

END xhb_populate_ref_calendar_all;
/
show errors
