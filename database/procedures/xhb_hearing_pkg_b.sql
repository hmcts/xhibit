-------------------------------------------------------------------------------
-- Possible future enhancements:
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY xhb_hearing_pkg AS
    PROCEDURE get_hearing_leg_reps(p_hearing_cursor_out  OUT SYS_REFCURSOR,
                                   p_hearing_id_in       IN  XHB_HEARING_LEG_REP.hearing_id%TYPE,
                                   p_ref_legal_rep_id_in IN  XHB_HEARING_LEG_REP.ref_legal_rep_id%TYPE,
                                   p_date_in             IN  XHB_HEARING_LEG_REP.start_date%TYPE) IS
        l_date XHB_HEARING_LEG_REP.start_date%TYPE := trunc(p_date_in);    	
    BEGIN
        OPEN p_hearing_cursor_out FOR
		SELECT * 
		FROM XHB_HEARING_LEG_REP
		WHERE hearing_id = p_hearing_id_in 
		AND ref_legal_rep_id = p_ref_legal_rep_id_in
		AND (l_date BETWEEN TRUNC(start_date) AND TRUNC(end_date)
		OR   l_date = TRUNC(start_date) -1
		OR   l_date = TRUNC(end_date)   +1);
    END get_hearing_leg_reps;

    PROCEDURE get_hearing_leg_reps_to_remove(p_hearing_cursor_out  OUT SYS_REFCURSOR,
                                   p_hearing_id_in       IN  XHB_HEARING_LEG_REP.hearing_id%TYPE,
                                   p_ref_legal_rep_id_in IN  XHB_HEARING_LEG_REP.ref_legal_rep_id%TYPE,
                                   p_date_in             IN  XHB_HEARING_LEG_REP.start_date%TYPE) IS
    	l_date XHB_HEARING_LEG_REP.start_date%TYPE := trunc(p_date_in);
    BEGIN
        OPEN p_hearing_cursor_out FOR
		SELECT * 
		FROM XHB_HEARING_LEG_REP
		WHERE hearing_id = p_hearing_id_in 
		AND ref_legal_rep_id = p_ref_legal_rep_id_in
		AND l_date between TRUNC(start_date) AND TRUNC(end_date);
    END get_hearing_leg_reps_to_remove;
END xhb_hearing_pkg;
/
show errors