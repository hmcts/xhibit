CREATE OR REPLACE PACKAGE BODY xhb_ref_local_pkg AS
    PROCEDURE end_hearings(p_court_id_in         IN XHB_HEARING.court_id%TYPE,
                           p_hearing_end_date_in IN VARCHAR2)
    IS
        l_hearing_end_date       DATE := to_date(p_hearing_end_date_in, 'YYYY-MM-DD HH24:MI:SS');
        l_trunc_hearing_end_date DATE := TRUNC(l_hearing_end_date);
    BEGIN
        UPDATE XHB_HEARING xh
        SET    xh.hearing_end_date = l_hearing_end_date
        WHERE  xh.court_id = p_court_id_in
        AND    EXISTS (SELECT 1
                       FROM   XHB_SCHEDULED_HEARING xsh 
                       WHERE  xsh.start_time IS NULL
                       AND    TRUNC(xsh.not_before_time) = l_trunc_hearing_end_date
                       AND    xh.hearing_id = xsh.hearing_id);

        -- Sets the START DATE FOR the scheduled hearing so that this will NOT be 
        -- processed again NEXT TIME the script IS RUN
        UPDATE XHB_SCHEDULED_HEARING xsh
        SET    START_TIME = l_hearing_end_date
        WHERE  START_TIME IS NULL
        AND    TRUNC(not_before_time) = l_trunc_hearing_end_date
        AND    EXISTS (SELECT 1
                       FROM   XHB_HEARING xh
                       WHERE  xh.hearing_id = xsh.hearing_id
                       AND    xh.court_id   = p_court_id_in);
    END end_hearings;
END xhb_ref_local_pkg;
/
show errors