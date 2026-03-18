-------------------------------------------------------------------------------
-- Possible future enhancements:
-------------------------------------------------------------------------------
--   Change the types for the in parameters to be the column types instead;
--   Replace counsel_type type declaration to use SYS_REFCURSOR;
--   Investigate the views to see if they can be improved;
--   For search_counsel & search_defendants see if we can ignore the firstname
--       and surname fields on the database if they are null (similar to how
--       the checks for null on the past in parameters are done);
--   See if the passed in values (for VARCHAR2's) is 0 length or just spaces,
--       if so, would we want to convert to null, and therefore ignore it?;
--   Change name of package to be consistent with Oracle coding standards
--       e.g. counsel_facilities_pkg
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY counselfacilities AS
    PROCEDURE get_counsel_sign_in(p_counsel_cursor_out OUT counsel_type,
                                  p_court_id_in        IN  NUMBER,
                                  p_start_date_in      IN  DATE,
                                  p_court_room_id_in   IN  NUMBER) IS
    BEGIN
        -- Vastly improved this query by removing the two sub-queries with the
        -- minus operations
        OPEN p_counsel_cursor_out FOR
			 SELECT * FROM (
            SELECT *
            FROM   xhb_counsel_facilities_sh_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_court_room_id_in IS NULL)
                   OR (court_room_id = p_court_room_id_in))
        UNION
            SELECT *
            FROM   xhb_counsel_facilities_shdid_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_court_room_id_in IS NULL)
                   OR (court_room_id = p_court_room_id_in))
		) ORDER BY court_site_code, is_floating, crest_court_room_no,
				   sitting_sequence_no, time_listed, sh_sequence_no;
    END get_counsel_sign_in;


    PROCEDURE search_counsel(p_counsel_cursor_out OUT counsel_type,
                             p_court_id_in        IN  NUMBER,
                             p_start_date_in      IN  DATE,
                             p_first_name_in      IN  VARCHAR2,
                             p_surname_in         IN  VARCHAR2) IS
    BEGIN
    	OPEN p_counsel_cursor_out FOR
            SELECT * FROM
            (
            SELECT * 
            FROM   XHB_COUNSEL_FACILITIES_SH_V
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    sh_leg_rep_id IS NOT NULL
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(leg_rep_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(leg_rep_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
        UNION
            SELECT * 
            FROM   XHB_COUNSEL_FACILITIES_SHDID_V
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    sh_leg_rep_id IS NOT NULL
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(leg_rep_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(leg_rep_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
		) ORDER BY court_site_code, is_floating, crest_court_room_no,
				   sitting_sequence_no, time_listed, sh_sequence_no;
    END search_counsel;


    PROCEDURE search_defendants(p_counsel_cursor_out OUT counsel_type,
                                p_court_id_in        IN  NUMBER,
                                p_start_date_in      IN  DATE,
                                p_first_name_in      IN  VARCHAR2,
                                p_surname_in         IN  VARCHAR2) IS
    BEGIN
        OPEN p_counsel_cursor_out FOR
            SELECT *
            FROM   xhb_counsel_facilities_sh_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(def_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(def_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
        UNION
            SELECT *
            FROM   xhb_counsel_facilities_shdid_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(def_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(def_surname, '%'))    LIKE UPPER(p_surname_in || '%')));
    END search_defendants;


    --
    -- NEW FUNCTION FOR CR51...
    --
    FUNCTION get_court_room_list(p_court_id_in      IN NUMBER,
                                 p_start_date_in    IN DATE,
                                 p_court_room_id_in IN NUMBER) RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        -- Really do not like this query, will look into further for the 7 release...
        -- Do not like, however, it is still considerably more efficient than the previous
        -- version (see get_counsel_sign_in), and could be improved and integrated further
        -- with other areas with the use of more common views...

        OPEN v_return_cursor FOR
            SELECT -- court data...
                   xcourt.court_type,
                   xcourt.court_name,
                   xcourt.short_name AS court_short_name,
                   xhl.court_id,
                   xhl.start_date,
                   SYSDATE AS request_date,
                   -- Court room details...
                   xcs.short_name AS court_site_short_name,
                   xcs.court_site_code,
                   xcr.court_room_id,
                   xcr.display_name AS court_site_display_name,
                   xcr.crest_court_room_no,
                   -- sitting data...
                   xs.sitting_id,
                   xs.sitting_time,
                   xs.sitting_sequence_no,
                   xs.is_floating AS floating,
                   -- Sitting judge...
                   NVL(xrj.full_list_title1, xrj.surname) AS judge_name,
                   -- scheduled hearing data...
                   xsh.scheduled_hearing_id,
                   xsh.sequence_no,
                   NVL(xsh.not_before_time, xsh.original_time) AS not_before_time,
                   xrht.hearing_type_desc AS hearing_type,
                   xc.case_id,
                   xc.case_type,
                   xc.case_number,
                   xc.case_title,
                   -- defendant details...
                   xd.defendant_id  AS def_id,
                   xd.first_name    AS def_first_name,
                   xd.middle_name   AS def_middle_name,
                   xd.surname       AS def_surname,
                   xdoc.is_masked   AS def_is_masked,
                   xdoc.masked_name AS def_masked_name,
                   -- Court staff...
                   xss.sh_staff_id AS staff_id,
                   xss.staff_role,
                   xss.staff_name,
                   -- Defence advocates...
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.ref_legal_rep_id END AS def_advocate_id,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.title END            AS def_advocate_title,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.first_name END       AS def_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.middle_name END      AS def_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.surname END          AS def_advocate_surname,
                   -- Prosecution and responent advocates and objectors...
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.ref_legal_rep_id END AS pros_advocate_id,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.title END            AS pros_advocate_title,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.first_name END       AS pros_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.middle_name END      AS pros_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.surname END          AS pros_advocate_surname
            FROM   XHB_HEARING_LIST xhl,
                   XHB_SITTING xs,
                   XHB_COURT xcourt,
                   XHB_COURT_ROOM xcr,
                   XHB_COURT_SITE xcs,
                   XHB_SCHEDULED_HEARING xsh,
                   XHB_HEARING xh,
                   XHB_CASE xc,
                   XHB_REF_HEARING_TYPE xrht,
                   XHB_SCHED_HEARING_DEFENDANT xshd,
                   XHB_DEFENDANT_ON_CASE xdoc,
                   XHB_DEFENDANT xd,
                   XHB_SCHED_HEARING_ATTENDEE xsha,
                   XHB_SH_STAFF xss,
                   XHB_REF_JUDGE xrj,
                   XHB_SH_LEG_REP xslr,
                   XHB_REF_LEGAL_REPRESENTATIVE xrlr
            WHERE  xhl.list_id = xs.list_id
            AND    xs.court_room_id = xcr.court_room_id
            AND    xs.court_site_id = xcs.court_site_id
            AND    xsh.sitting_id = xs.sitting_id
            AND    xsh.hearing_id = xh.hearing_id
            AND    xh.ref_hearing_type_id = xrht.ref_hearing_type_id
            AND    xh.case_id = xc.case_id
            AND    xcs.court_id = xhl.court_id
            AND    xrht.court_id = xhl.court_id
            AND    xrht.court_id = xcourt.court_id
            AND    xsh.scheduled_hearing_id = xshd.scheduled_hearing_id(+)
            AND    xshd.defendant_on_case_id = xdoc.defendant_on_case_id(+)
            AND    xdoc.defendant_id = xd.defendant_id(+)
            AND    xsh.scheduled_hearing_id = xsha.scheduled_hearing_id(+)
            AND    xsha.sh_staff_id = xss.sh_staff_id(+)
            AND    Xhb_Custom_Pkg.GET_REF_JUDGE_ID(xsh.SCHEDULED_HEARING_ID) = xrj.REF_JUDGE_ID(+) 
            AND    xslr.ref_legal_rep_id = xrlr.ref_legal_rep_id(+)
            AND    xshd.sched_hear_def_id = xslr.sched_hear_def_id(+)
            AND    xhl.court_id = xcourt.court_id
            AND    xhl.start_date  = p_start_date_in
            AND    xhl.court_id    = p_court_id_in
            AND    xcs.court_id    = p_court_id_in
            AND    (p_court_room_id_in IS NULL OR (xcr.court_room_id = p_court_room_id_in AND xs.is_floating = 0))
UNION
            SELECT -- court data...
                   xcourt.court_type,
                   xcourt.court_name,
                   xcourt.short_name AS court_short_name,
                   xhl.court_id,
                   xhl.start_date,
                   SYSDATE AS request_date,
                   -- Court room details...
                   xcs.short_name AS court_site_short_name,
                   xcs.court_site_code,
                   xcr.court_room_id,
                   xcr.display_name AS court_site_display_name,
                   xcr.crest_court_room_no,
                   -- sitting data...
                   xs.sitting_id,
                   xs.sitting_time,
                   xs.sitting_sequence_no,
                   xs.is_floating AS floating,
                   -- Sitting judge...
                   NVL(xrj.full_list_title1, xrj.surname) AS judge_name,
                   -- scheduled hearing data...
                   xsh.scheduled_hearing_id,
                   xsh.sequence_no,
                   NVL(xsh.not_before_time, xsh.original_time) AS not_before_time,
                   xrht.hearing_type_desc AS hearing_type,
                   xc.case_id,
                   xc.case_type,
                   xc.case_number,
                   xc.case_title,
                   -- defendant details...
                   xd.defendant_id  AS def_id,
                   xd.first_name    AS def_first_name,
                   xd.middle_name   AS def_middle_name,
                   xd.surname       AS def_surname,
                   xdoc.is_masked   AS def_is_masked,
                   xdoc.masked_name AS def_masked_name,
                   -- Court staff...
                   xss.sh_staff_id AS staff_id,
                   xss.staff_role,
                   xss.staff_name,
                   -- Defence advocates...
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.ref_legal_rep_id END AS def_advocate_id,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.title END            AS def_advocate_title,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.first_name END       AS def_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.middle_name END      AS def_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.surname END          AS def_advocate_surname,
                   -- Prosecution and responent advocates and objectors...
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.ref_legal_rep_id END AS pros_advocate_id,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.title END            AS pros_advocate_title,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.first_name END       AS pros_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.middle_name END      AS pros_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.surname END          AS pros_advocate_surname
            FROM   XHB_HEARING_LIST xhl,
                   XHB_SITTING xs,
                   XHB_COURT xcourt,
                   XHB_COURT_ROOM xcr,
                   XHB_COURT_SITE xcs,
                   XHB_SCHEDULED_HEARING xsh,
                   XHB_HEARING xh,
                   XHB_CASE xc,
                   XHB_REF_HEARING_TYPE xrht,
                   XHB_SCHED_HEARING_DEFENDANT xshd,
                   XHB_DEFENDANT_ON_CASE xdoc,
                   XHB_DEFENDANT xd,
                   XHB_SCHED_HEARING_ATTENDEE xsha,
                   XHB_SH_STAFF xss,
                   XHB_REF_JUDGE xrj,
                   XHB_SH_LEG_REP xslr,
                   XHB_REF_LEGAL_REPRESENTATIVE xrlr
            WHERE  xhl.list_id = xs.list_id
            AND    xs.court_room_id = xcr.court_room_id
            AND    xs.court_site_id = xcs.court_site_id
            AND    xsh.sitting_id = xs.sitting_id
            AND    xsh.hearing_id = xh.hearing_id
            AND    xh.ref_hearing_type_id = xrht.ref_hearing_type_id
            AND    xh.case_id = xc.case_id
            AND    xcs.court_id = xhl.court_id
            AND    xrht.court_id = xhl.court_id
            AND    xrht.court_id = xcourt.court_id
            AND    xsh.scheduled_hearing_id = xshd.scheduled_hearing_id(+)
            AND    xshd.defendant_on_case_id = xdoc.defendant_on_case_id(+)
            AND    xdoc.defendant_id = xd.defendant_id(+)
            AND    xsh.scheduled_hearing_id = xsha.scheduled_hearing_id(+)
            AND    xsha.sh_staff_id = xss.sh_staff_id(+)
            AND    Xhb_Custom_Pkg.GET_REF_JUDGE_ID(xsh.SCHEDULED_HEARING_ID) = xrj.REF_JUDGE_ID(+) 
            AND    xslr.ref_legal_rep_id = xrlr.ref_legal_rep_id(+)
            AND    (xsh.scheduled_hearing_id = xslr.scheduled_hearing_id(+) AND xslr.sched_hear_def_id IS NULL)
            AND    xhl.court_id = xcourt.court_id
            AND    xhl.start_date  = p_start_date_in
            AND    xhl.court_id    = p_court_id_in
            AND    xcs.court_id    = p_court_id_in
            AND    (p_court_room_id_in IS NULL OR (xcr.court_room_id = p_court_room_id_in AND xs.is_floating = 0))
            ORDER BY court_site_code, floating, crest_court_room_no,
                   sitting_sequence_no, not_before_time, sequence_no;

        RETURN v_return_cursor;
    END get_court_room_list;
END counselfacilities;
/
show errors