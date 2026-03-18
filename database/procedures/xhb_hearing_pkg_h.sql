-- See the body script for possible future enhancements
CREATE OR REPLACE PACKAGE xhb_hearing_pkg AS
    PROCEDURE get_hearing_leg_reps(p_hearing_cursor_out OUT  SYS_REFCURSOR,
                                   p_hearing_id_in       IN  XHB_HEARING_LEG_REP.hearing_id%TYPE,
                                   p_ref_legal_rep_id_in IN  XHB_HEARING_LEG_REP.ref_legal_rep_id%TYPE,
                                   p_date_in             IN  XHB_HEARING_LEG_REP.start_date%TYPE);
    PROCEDURE get_hearing_leg_reps_to_remove(p_hearing_cursor_out OUT SYS_REFCURSOR,
                                             p_hearing_id_in       IN  XHB_HEARING_LEG_REP.hearing_id%TYPE,
                                             p_ref_legal_rep_id_in IN  XHB_HEARING_LEG_REP.ref_legal_rep_id%TYPE,
                                             p_date_in             IN  XHB_HEARING_LEG_REP.start_date%TYPE);

END xhb_hearing_pkg;
/
show errors