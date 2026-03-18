-------------------------------------------------------------------------------
-- THE PACKAGE BODY
--
-- The xhb_search_pkg contains all of the procedures used by the fast
-- lane readers.
-- 
-- Couple of comments on the package structure:
--
-- The method log_entry (and the script to create the required table) has been
-- left in following investiations into the number of times each method was
-- called (and the resultant changes).
--
-- Each method that requires a call to the convert_values function (which is
-- now package private) does so in the declaration section of the procedure,
-- this is to prevent the call being performed for every row in the query.
-- (this resulted from the log_entry investigation).
--
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY xhb_search_pkg AS

--    CREATE TABLE search_pkg_calls(procedure_name VARCHAR2(100), access_time DATE DEFAULT SYSDATE);
--
--    PROCEDURE log_entry(procedure_name_in IN search_pkg_calls.procedure_name%TYPE)
--    AS
--        PRAGMA AUTONOMOUS_TRANSACTION;
--    BEGIN
--        INSERT INTO search_pkg_calls (procedure_name) VALUES (procedure_name_in);
--        COMMIT;
--    END log_entry;





    -- TBD: Would declaring this deterministic be of benefit?
    -- How common are the search strings?
    FUNCTION convert_value(p_value_in IN VARCHAR2)
        RETURN VARCHAR2 AS
    BEGIN
        --log_entry('convert_value()');

        -- Only convert to uppercase for now
        RETURN UPPER(p_value_in);
    END convert_value;





    --
    -- Query the XHB_COURT table, with some details from the XHB_COURT_SITE table
    --
    PROCEDURE get_court(p_results_out       OUT SYS_REFCURSOR,
                        p_circuit_in        IN  XHB_COURT.circuit%TYPE,
                        p_court_site_id_in  IN  XHB_COURT_SITE.court_site_id%TYPE,
                        p_court_name_in     IN  XHB_COURT.court_name%TYPE,
                        p_court_prefix_in   IN  XHB_COURT.court_prefix%TYPE,
                        p_court_type_in     IN  XHB_COURT.court_type%TYPE,
                        p_crest_court_id_in IN  XHB_COURT.crest_court_id%TYPE,
                        p_short_name_in     IN  XHB_COURT.short_name%TYPE)
    AS
        l_circuit        CONSTANT XHB_COURT.circuit%TYPE        := convert_value(p_circuit_in);
        l_court_name     CONSTANT XHB_COURT.court_name%TYPE     := convert_value(p_court_name_in);
        l_court_prefix   CONSTANT XHB_COURT.court_prefix%TYPE   := convert_value(p_court_prefix_in);
        l_court_type     CONSTANT XHB_COURT.court_type%TYPE     := convert_value(p_court_type_in);
        l_crest_court_id CONSTANT XHB_COURT.crest_court_id%TYPE := convert_value(p_crest_court_id_in);
        l_short_name     CONSTANT XHB_COURT.short_name%TYPE     := convert_value(p_short_name_in);
    BEGIN
        --log_entry('get_court');

        OPEN p_results_out FOR
            SELECT c.court_id AS "id",
                   cs.court_site_code AS "COURT_CODE",
                   c.*
            FROM   XHB_COURT c,
                   XHB_COURT_SITE cs
            WHERE  c.court_id                =    cs.court_id
            AND    ((c.obs_ind IS NULL) OR (c.obs_ind = 'N'))
            AND    ((p_court_site_id_in IS NULL)
                        OR (cs.court_site_id        =    p_court_site_id_in))
            AND    ((l_circuit IS NULL)
                        OR (UPPER(c.circuit)        LIKE l_circuit))
            AND    ((l_court_name IS NULL)
                        OR (UPPER(c.court_name)     LIKE l_court_name))
            AND    ((l_court_prefix IS NULL)
                        OR (UPPER(c.court_prefix)   LIKE l_court_prefix))
            AND    ((l_court_type IS NULL)
                        OR (UPPER(c.court_type)     LIKE l_court_type))
            AND    ((l_crest_court_id IS NULL)
                        OR (UPPER(c.crest_court_id) LIKE l_crest_court_id))
            AND    ((l_short_name IS NULL)
                        OR (UPPER(c.short_name)     LIKE l_short_name));
    END get_court;


    --
    -- Query the XHB_COURT_ROOM table, but also with search criteria from
    -- XHB_COURT_SITE and XHB_COURT
    --
    PROCEDURE get_court_room(p_results_out            OUT SYS_REFCURSOR,
                             p_court_room_name_in     IN  XHB_COURT_ROOM.court_room_name%TYPE,
                             p_court_site_code_in     IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_site_id_in       IN  XHB_COURT_SITE.court_site_id%TYPE,
                             p_crest_court_room_no_in IN  XHB_COURT_ROOM.crest_court_room_no%TYPE,
                             p_short_name_in          IN  XHB_COURT.short_name%TYPE)
    AS
        l_court_room_name CONSTANT XHB_COURT_ROOM.court_room_name%TYPE := convert_value(p_court_room_name_in);
        l_court_site_code CONSTANT XHB_COURT_SITE.court_site_code%TYPE := convert_value(p_court_site_code_in);
        l_short_name      CONSTANT XHB_COURT.short_name%TYPE           := convert_value(p_short_name_in);
    BEGIN
        --log_entry('get_court_room');

        OPEN p_results_out FOR
            SELECT DISTINCT cr.court_room_id AS "id",
                   t.location AS location,
                   cr.*
            FROM   XHB_COURT_ROOM cr,
                   XHB_COURT_SITE cs,
                   XHB_COURT c,
                   XHB_TERMINAL t
            WHERE  cr.court_site_id = cs.court_site_id
            AND    c.court_id       = cs.court_id
            AND    cr.court_room_id = t.court_room_id(+)
            AND    ((cr.obs_ind IS NULL) OR (cr.obs_ind = 'N'))
            AND    ((l_court_room_name IS NULL)
                        OR (UPPER(cr.court_room_name)  LIKE l_court_room_name))
            AND    ((l_court_site_code IS NULL)
                        OR (UPPER(cs.court_site_code)  LIKE l_court_site_code))
            AND    ((p_court_site_id_in IS NULL)
                        OR (cs.court_site_id           =    p_court_site_id_in))
            AND    ((p_crest_court_room_no_in IS NULL)
                        OR (cr.crest_court_room_no     =    p_crest_court_room_no_in))
            AND    ((l_short_name IS NULL)
                        OR (UPPER(c.short_name)        LIKE l_short_name));
    END get_court_room;


    --
    -- Query the XHB_COURT_SITE table, but also with search criteria
    -- from XHB_COURT
    --
    PROCEDURE get_court_site(p_results_out         OUT SYS_REFCURSOR,
                             p_court_site_code_in  IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_id_in         IN  XHB_COURT.court_id%TYPE,
                             p_court_short_name_in IN  XHB_COURT.short_name%TYPE,
                             p_court_site_name_in  IN  XHB_COURT_SITE.court_site_name%TYPE)
    AS
        l_court_site_code  CONSTANT XHB_COURT_SITE.court_site_code%TYPE := convert_value(p_court_site_code_in);
        l_court_short_name CONSTANT XHB_COURT.short_name%TYPE           := convert_value(p_court_short_name_in);
        l_court_site_name  CONSTANT XHB_COURT_SITE.court_site_name%TYPE := convert_value(p_court_site_name_in);
    BEGIN
        --log_entry('get_court_site');

        OPEN p_results_out FOR
            SELECT cs.court_site_id AS "id",
                   cs.*
            FROM   XHB_COURT_SITE cs,
                   XHB_COURT c
            WHERE  cs.court_id = c.court_id
            AND    ((cs.obs_ind IS NULL) OR (cs.obs_ind = 'N'))
            AND    ((l_court_site_code IS NULL)
                        OR (UPPER(cs.court_site_code) LIKE l_court_site_code))
            AND    ((p_court_id_in IS NULL)
                        OR (c.court_id                =    p_court_id_in))
            AND    ((l_court_short_name IS NULL)
                        OR (UPPER(c.short_name)       LIKE l_court_short_name))
            AND    ((l_court_site_name IS NULL)
                        OR (UPPER(cs.court_site_name) LIKE l_court_site_name));
    END get_court_site;


    --
    -- Query the XHB_REF_ADVOCATE table, with some details from
    -- XHB_REF_LEGAL_REPRESENTATIVE, XHB_REF_CHAMBER and XHB_ADDRESS
    --
    PROCEDURE get_ref_advocate_complex(p_results_out         OUT SYS_REFCURSOR,
                                       p_adv_type_ind_in     IN  XHB_REF_ADVOCATE.adv_type_ind%TYPE,
                                       p_initials_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                                       p_first_name_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                       p_middle_name_in      IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                                       p_surname_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                       p_ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE,
                                       p_firm_name_in        IN  XHB_REF_CHAMBER.firm_name%TYPE,
                                       p_court_id_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
                                       p_defendant_id_in     IN  XHB_DEFENDANT.defendant_id%TYPE := -1,
                                       p_case_id_in          IN  XHB_CASE.case_id%TYPE := -1)
    AS
        l_adv_type_ind CONSTANT XHB_REF_ADVOCATE.adv_type_ind%TYPE            := convert_value(p_adv_type_ind_in);
        l_initials     CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE    := convert_value(p_initials_in);
        l_first_name   CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE  := convert_value(p_first_name_in);
        l_middle_name  CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE := convert_value(p_middle_name_in);
        l_surname      CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE     := convert_value(p_surname_in);
        l_firm_name    CONSTANT XHB_REF_CHAMBER.firm_name%TYPE                := convert_value(p_firm_name_in);
    BEGIN
        --log_entry('get_ref_advocate_complex');

       -- Optimised For CREST Form A Lookup
       IF p_defendant_id_in IS NOT NULL AND
          p_defendant_id_in <> -1 AND
          p_case_id_in IS NOT NULL AND
          p_case_id_in <> -1 THEN
          
          OPEN p_results_out FOR
              SELECT ra.ref_advocate_id AS "id",
                     rlr.title,
                     rlr.first_name,
                     rlr.initials,
                     rlr.middle_name,
                     rlr.surname,
                     rlr.court_id,
                     rlr.legal_rep_type,
                     ra.ref_chamber_id AS chamber_id,
                     ra.ref_legal_rep_id AS legal_rep_id,
                     ra.bar_no,
                     ra.is_global,
                     ra.crest_Advocate_Id,
                     ra.version,
                     ra.year_Of_Call,
                     ra.vat_No,
                     ra.crest_Chamber_Id,
                     ra.honours,
                     ra.adv_Type_Ind,
                     ra.obs_ind,
                     rc.firm_name,
                     a.address_1 AS address1,
                     a.address_2 AS address2,
                     a.address_3 AS address3,
                     a.address_4 AS address4,
                     a.town,
                     a.county,
                     a.postcode,
                     lal.available,
                     lal.crest_post_number,
                     lal.crest_adv_category
              FROM   XHB_REF_ADVOCATE ra,
                     XHB_REF_LEGAL_REPRESENTATIVE rlr,
                     XHB_REF_CHAMBER rc,
                     XHB_ADDRESS a,
                     XHB_LEO_ADV_LINK lal,
                     XHB_DEFENDANT_ON_CASE doc
              WHERE  ra.ref_legal_rep_id = rlr.ref_legal_rep_id
              AND    ra.ref_chamber_id = rc.ref_chamber_id
              AND    rc.address_id = a.address_id(+)
              AND    ((ra.obs_ind IS NULL) OR (ra.obs_ind = 'N'))
              AND    doc.case_id = p_case_id_in 
              AND    doc.defendant_id = p_defendant_id_in
              AND    ((lal.obs_ind IS NULL) OR (lal.obs_ind = 'N'))
              AND    lal.defendant_on_case_id = doc.defendant_on_case_id
              AND    lal.ref_advocate_id = ra.ref_advocate_id
              ORDER BY rlr.surname;
          
       ELSIF p_adv_type_ind_in IS NULL AND
          p_initials_in IS NULL AND
          p_first_name_in IS NULL AND
          p_middle_name_in IS NULL AND
          p_surname_in IS NULL AND
          p_ref_legal_rep_id_in IS NOT NULL AND
          p_firm_name_in IS NULL AND
          p_court_id_in IS NULL THEN

          OPEN p_results_out FOR
              SELECT ra.ref_advocate_id AS "id",
                     rlr.title,
                     rlr.first_name,
                     rlr.initials,
                     rlr.middle_name,
                     rlr.surname,
                     rlr.court_id,
                     rlr.legal_rep_type,
                     ra.ref_chamber_id AS chamber_id,
                     ra.ref_legal_rep_id AS legal_rep_id,
                     ra.bar_no,
                     ra.is_global,
                     ra.crest_Advocate_Id,
                     ra.version,
                     ra.year_Of_Call,
                     ra.vat_No,
                     ra.crest_Chamber_Id,
                     ra.honours,
                     ra.adv_Type_Ind,
                     ra.obs_ind,
                     rc.firm_name,
                     a.address_1 AS address1,
                     a.address_2 AS address2,
                     a.address_3 AS address3,
                     a.address_4 AS address4,
                     a.town,
                     a.county,
                     a.postcode,
                     null AS available,
                     null AS crest_post_number,
                     null AS crest_adv_category
              FROM   XHB_REF_ADVOCATE ra,
                     XHB_REF_LEGAL_REPRESENTATIVE rlr,
                     XHB_REF_CHAMBER rc,
                     XHB_ADDRESS a
              WHERE  ra.ref_legal_rep_id = rlr.ref_legal_rep_id
              AND    ra.ref_chamber_id = rc.ref_chamber_id
              AND    rc.address_id = a.address_id(+)
              AND    ((ra.obs_ind IS NULL) OR (ra.obs_ind = 'N'))
              AND    ra.ref_legal_rep_id = p_ref_legal_rep_id_in
              ORDER BY rlr.surname;

       ELSE

          OPEN p_results_out FOR
            SELECT ra.ref_advocate_id AS "id",
                   rlr.title,
                   rlr.first_name,
                   rlr.initials,
                   rlr.middle_name,
                   rlr.surname,
                   rlr.court_id,
                   rlr.legal_rep_type,
                   ra.ref_chamber_id AS chamber_id,
                   ra.ref_legal_rep_id AS legal_rep_id,
                   ra.bar_no,
                   ra.is_global,
                   ra.crest_Advocate_Id,
                   ra.version,
                   ra.year_Of_Call,
                   ra.vat_No,
                   ra.crest_Chamber_Id,
                   ra.honours,
                   ra.adv_Type_Ind,
                   ra.obs_ind,
                   rc.firm_name,
                   a.address_1 AS address1,
                   a.address_2 AS address2,
                   a.address_3 AS address3,
                   a.address_4 AS address4,
                   a.town,
                   a.county,
                   a.postcode,
                   null AS available,
                   null AS crest_post_number,
                   null AS crest_adv_category
            FROM   XHB_REF_ADVOCATE ra,
                   XHB_REF_LEGAL_REPRESENTATIVE rlr,
                   XHB_REF_CHAMBER rc,
                   XHB_ADDRESS a
            WHERE  ra.ref_legal_rep_id = rlr.ref_legal_rep_id
            AND    ra.ref_chamber_id = rc.ref_chamber_id
            AND    rc.address_id = a.address_id(+)
            AND    ((ra.obs_ind IS NULL) OR (ra.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rlr.court_id           =    p_court_id_in))
            AND    ((l_adv_type_ind IS NULL)
                        OR (UPPER(ra.adv_type_ind) LIKE l_adv_type_ind))
            AND    ((p_ref_legal_rep_id_in IS NULL)
                        OR (ra.ref_legal_rep_id    =    p_ref_legal_rep_id_in))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rlr.initials)    LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rlr.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)     LIKE l_surname))
            AND    ((l_firm_name IS NULL)
                        OR (UPPER(rc.firm_name)    LIKE l_firm_name))
            ORDER BY rlr.surname;

       END IF;

    END get_ref_advocate_complex;


    --
    -- Query the XHB_REF_APP_RESULT table
    --
    PROCEDURE get_ref_app_result(p_results_out        OUT SYS_REFCURSOR,
                                 p_court_id_in        IN  XHB_REF_APP_RESULT.court_id%TYPE,
                                 p_app_result_code_in IN  XHB_REF_APP_RESULT.app_result_code%TYPE,
                                 p_ho_code_in         IN  XHB_REF_APP_RESULT.ho_code%TYPE,
                                 p_vary_sentence_in   IN  XHB_REF_APP_RESULT.vary_sentence%TYPE,
                                 p_lesser_off_ind_in  IN  XHB_REF_APP_RESULT.lesser_off_ind%TYPE)
    AS
        l_app_result_code CONSTANT XHB_REF_APP_RESULT.app_result_code%TYPE := convert_value(p_app_result_code_in);
        l_vary_sentence   CONSTANT XHB_REF_APP_RESULT.vary_sentence%TYPE   := convert_value(p_vary_sentence_in);
        l_lesser_off_ind  CONSTANT XHB_REF_APP_RESULT.lesser_off_ind%TYPE  := convert_value(p_lesser_off_ind_in);
    BEGIN
        --log_entry('get_ref_app_result');

        OPEN p_results_out FOR
            SELECT rap.ref_app_result_id AS "id",
                   rap.ref_app_result_id AS ref_App_Res_Id,
                   rap.app_result_code AS code,
                   rap.app_result_descr1 AS description1,
                   rap.app_result_descr2 AS description2,
                   rap.court_id,
                   rap.vary_sentence,
                   rap.version,
                   rap.ho_code,
                   rap.lesser_off_ind,
                   rap.obs_ind
            FROM   XHB_REF_APP_RESULT rap
            WHERE  ((rap.obs_ind IS NULL) OR (rap.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rap.court_id               =    p_court_id_in))
            AND    ((l_app_result_code IS NULL)
                        OR (UPPER(rap.app_result_code) LIKE l_app_result_code))
            AND    ((p_ho_code_in IS NULL)
                        OR (rap.ho_code                =    p_ho_code_in))
            AND    ((l_vary_sentence IS NULL)
                        OR (UPPER(rap.vary_sentence)   LIKE l_vary_sentence))
            AND    ((l_lesser_off_ind IS NULL)
                        OR (UPPER(rap.lesser_off_ind)  LIKE l_lesser_off_ind));
    END get_ref_app_result;


    --
    -- Query the XHB_REF_COURT table, but also with search criteria
    -- from XHB_COURT
    --
    PROCEDURE get_ref_court(p_results_out         OUT SYS_REFCURSOR,
                            p_court_id_in         IN  XHB_REF_COURT.court_id%TYPE,
                            p_circuit_in          IN  XHB_COURT.circuit%TYPE,
                            p_court_full_name_in  IN  XHB_REF_COURT.court_full_name%TYPE,
                            p_court_prefix_in     IN  XHB_REF_COURT.name_prefix%TYPE,
                            p_court_type_in       IN  XHB_REF_COURT.court_type%TYPE,
                            p_crest_court_id_in   IN  XHB_COURT.crest_court_id%TYPE,
                            p_court_short_name_in IN  XHB_REF_COURT.court_short_name%TYPE,
                            p_is_psd_in           IN  XHB_REF_COURT.is_psd%TYPE)
    AS
        l_circuit          CONSTANT XHB_COURT.circuit%TYPE              := convert_value(p_circuit_in);
        l_court_full_name  CONSTANT XHB_REF_COURT.court_full_name%TYPE  := convert_value(p_court_full_name_in);
        l_court_prefix     CONSTANT XHB_REF_COURT.name_prefix%TYPE      := convert_value(p_court_prefix_in);
        l_court_type       CONSTANT XHB_REF_COURT.court_type%TYPE       := convert_value(p_court_type_in);
        l_court_short_name CONSTANT XHB_REF_COURT.court_short_name%TYPE := convert_value(p_court_short_name_in);
        l_is_psd           CONSTANT XHB_REF_COURT.is_psd%TYPE           := convert_value(p_is_psd_in);
    BEGIN
        --log_entry('get_ref_court');

        OPEN p_results_out FOR
            SELECT rc.ref_court_id AS "id",
                   rc.*
            FROM   XHB_REF_COURT rc,
                   XHB_COURT c
            WHERE  rc.court_id                =    c.court_id
            AND    ((rc.obs_ind IS NULL) OR (rc.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rc.court_id                =    p_court_id_in))
            AND    ((l_circuit IS NULL)
                        OR (UPPER(c.circuit)           LIKE l_circuit))
            AND    ((l_court_full_name IS NULL)
                        OR (UPPER(rc.court_full_name)  LIKE l_court_full_name))
            AND    ((l_court_prefix IS NULL)
                        OR (UPPER(rc.name_prefix)      LIKE l_court_prefix))
            AND    ((l_court_type IS NULL)
                        OR (UPPER(rc.court_type)       LIKE l_court_type))
            AND    ((p_crest_court_id_in IS NULL)
                        OR (c.crest_court_id           =    p_crest_court_id_in))
            AND    ((l_court_short_name IS NULL)
                        OR (UPPER(rc.court_short_name) LIKE l_court_short_name))
            AND    ((l_is_psd IS NULL)
                        OR (UPPER(rc.is_psd)           LIKE l_is_psd));
    END get_ref_court;


    --
    -- Query the XHB_REF_COURT_REPORTER table, but also with search criteria
    -- from XHB_REF_COURT_REPORTER_FIRM
    --
    PROCEDURE get_ref_court_reporter(p_results_out    OUT SYS_REFCURSOR,
                                     p_court_id_in    IN  XHB_REF_COURT_REPORTER.court_id%TYPE,
                                     p_firm_name_in   IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
                                     p_initials_in    IN  XHB_REF_COURT_REPORTER.initials%TYPE,
                                     p_first_name_in  IN  XHB_REF_COURT_REPORTER.first_name%TYPE,
                                     p_middle_name_in IN  XHB_REF_COURT_REPORTER.middle_name%TYPE,
                                     p_surname_in     IN  XHB_REF_COURT_REPORTER.surname%TYPE)
    AS
        l_firm_name   CONSTANT XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE := convert_value(p_firm_name_in);
        l_initials    CONSTANT XHB_REF_COURT_REPORTER.initials%TYPE       := convert_value(p_initials_in);
        l_first_name  CONSTANT XHB_REF_COURT_REPORTER.first_name%TYPE     := convert_value(p_first_name_in);
        l_middle_name CONSTANT XHB_REF_COURT_REPORTER.middle_name%TYPE    := convert_value(p_middle_name_in);
        l_surname     CONSTANT XHB_REF_COURT_REPORTER.surname%TYPE        := convert_value(p_surname_in);
    BEGIN
        --log_entry('get_ref_court_reporter');

        OPEN p_results_out FOR
            SELECT rcr.ref_court_reporter_id AS "id",
                   rcr.*
            FROM   XHB_REF_COURT_REPORTER rcr,
                   XHB_REF_COURT_REPORTER_FIRM rcrf
            WHERE  rcr.ref_court_reporter_firm_id = rcrf.ref_court_reporter_firm_id
            AND    ((rcr.obs_ind IS NULL) OR (rcr.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rcr.court_id           =    p_court_id_in))
            AND    ((l_firm_name IS NULL)
                        OR (UPPER(rcrf.firm_name)  LIKE l_firm_name))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rcr.initials)    LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rcr.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rcr.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rcr.surname)     LIKE l_surname));
    END get_ref_court_reporter;


    --
    -- Query the XHB_REF_HEARING_TYPE table
    --
    PROCEDURE get_ref_hearing_type(p_results_out          OUT SYS_REFCURSOR,
                                   p_hearing_type_code_in IN  XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
                           p_hearing_type_courtid_in IN  XHB_REF_HEARING_TYPE.court_id%TYPE)
    AS
        l_hearing_type_code CONSTANT XHB_REF_HEARING_TYPE.hearing_type_code%TYPE := convert_value(p_hearing_type_code_in);
    BEGIN
        --log_entry('get_ref_hearing_type');

        OPEN p_results_out FOR
            SELECT rht.ref_hearing_type_id AS "id",
                   rht.*
            FROM   XHB_REF_HEARING_TYPE rht
            WHERE  ((rht.obs_ind IS NULL) OR (rht.obs_ind = 'N'))
            AND    ((l_hearing_type_code IS NULL)
                        OR (UPPER(rht.hearing_type_code) LIKE l_hearing_type_code))
	    AND    ((p_hearing_type_courtid_in IS NULL)
                        OR (rht.court_id             =    p_hearing_type_courtid_in ));
    END get_ref_hearing_type;


    --
    -- Query the XHB_REF_JUDGE table
    --
    PROCEDURE get_ref_judge(p_results_out    OUT SYS_REFCURSOR,
                            p_first_name_in  IN  XHB_REF_JUDGE.first_name%TYPE,
                            p_middle_name_in IN  XHB_REF_JUDGE.middle_name%TYPE,
                            p_surname_in     IN  XHB_REF_JUDGE.surname%TYPE,
                            p_court_id_in    IN  XHB_REF_JUDGE.court_id%TYPE)
    AS
        l_first_name  CONSTANT XHB_REF_JUDGE.first_name%TYPE  := convert_value(p_first_name_in);
        l_middle_name CONSTANT XHB_REF_JUDGE.middle_name%TYPE := convert_value(p_middle_name_in);
        l_surname     CONSTANT XHB_REF_JUDGE.surname%TYPE     := convert_value(p_surname_in);
    BEGIN
        --log_entry('get_ref_judge');

        OPEN p_results_out FOR
            SELECT rj.ref_judge_id AS "id",
                   rj.*
            FROM   XHB_REF_JUDGE rj
            WHERE  ((rj.obs_ind IS NULL) OR (rj.obs_ind  = 'N'))
            AND    ((p_court_id_in IS NULL) OR (rj.court_id =  p_court_id_in))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rj.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rj.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rj.surname)     LIKE l_surname));
    END get_ref_judge;


    --
    -- Query the XHB_REF_JUSTICE table
    --
    PROCEDURE get_ref_justice(p_results_out     OUT SYS_REFCURSOR,
                              p_justice_name_in IN  XHB_REF_JUSTICE.justice_name%TYPE,
                              p_court_id_in     IN  XHB_REF_JUSTICE.court_id%TYPE)
    AS
        l_justice_name CONSTANT XHB_REF_JUSTICE.justice_name%TYPE := convert_value(p_justice_name_in);
    BEGIN
        --log_entry('get_ref_justice');

        OPEN p_results_out FOR
            SELECT rj.ref_justice_id AS "id",
                   rj.court_id AS court_i_d,
                   rj.*
            FROM   XHB_REF_JUSTICE rj
            WHERE  ((rj.obs_ind IS NULL) OR (rj.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL) OR (rj.court_id = p_court_id_in))
            AND    ((l_justice_name IS NULL)
                        OR (UPPER(rj.justice_name) LIKE l_justice_name));
    END get_ref_justice;


    --
    -- Query the XHB_REF_LEGAL_REPRESENTATIVE table
    --
    PROCEDURE get_ref_legal_representative(p_results_out       OUT SYS_REFCURSOR,
                                           p_court_id_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
                                           p_first_name_in     IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                           p_surname_in        IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                           p_legal_rep_type_in IN  XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE)
    AS
        l_first_name     CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE     := convert_value(p_first_name_in);
        l_surname        CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE        := convert_value(p_surname_in);
        l_legal_rep_type CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE := convert_value(p_legal_rep_type_in);
    BEGIN
        --log_entry('get_ref_legal_representative');

        OPEN p_results_out FOR
            SELECT rlr.ref_legal_rep_id AS "id",
                   rlr.*
            FROM   XHB_REF_LEGAL_REPRESENTATIVE rlr
            WHERE  ((rlr.obs_ind IS NULL) OR (rlr.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rlr.court_id              =    p_court_id_in))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)     LIKE l_first_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)        LIKE l_surname))
            AND    ((l_legal_rep_type IS NULL)
                        OR (UPPER(rlr.legal_rep_type) LIKE l_legal_rep_type));
    END get_ref_legal_representative;


    --
    -- Query the XHB_REF_OFFENCE table
    --
    PROCEDURE get_ref_offence(p_results_out     OUT SYS_REFCURSOR,
                              p_act_section_in  IN  XHB_REF_OFFENCE.act_section%TYPE,
                              p_court_id_in     IN  XHB_REF_OFFENCE.court_id%TYPE,
                              p_offence_desc_in IN  XHB_REF_OFFENCE.offence_desc%TYPE,
                              p_statute_in      IN  XHB_REF_OFFENCE.statute%TYPE,
                              p_offence_code_in IN  XHB_REF_OFFENCE.offence_code%TYPE,
	                      p_obs_ind_in      IN  XHB_REF_OFFENCE.obs_ind%TYPE,
                              p_bail_act_in     IN  XHB_REF_OFFENCE.bail_act%TYPE)
    AS
        l_act_section  CONSTANT XHB_REF_OFFENCE.act_section%TYPE  := convert_value(p_act_section_in);
        l_offence_desc CONSTANT XHB_REF_OFFENCE.offence_desc%TYPE := convert_value(p_offence_desc_in);
        l_statute      CONSTANT XHB_REF_OFFENCE.statute%TYPE      := convert_value(p_statute_in);
        l_offence_code CONSTANT XHB_REF_OFFENCE.offence_code%TYPE := convert_value(p_offence_code_in);
    BEGIN
        --log_entry('get_ref_offence');

        OPEN p_results_out FOR
            SELECT ro.ref_offence_id AS "id",
                   ro.*,
                   -- Can't find what this IS
                   'OFFENCE TYPE' AS offence_type
            FROM   XHB_REF_OFFENCE ro
            WHERE  ((p_obs_ind_in = 'Y') OR ((ro.obs_ind IS NULL) OR (ro.obs_ind = 'N')))
            AND    ((l_act_section IS NULL)
                        OR (UPPER(ro.act_section)  LIKE l_act_section))
            AND    ((p_court_id_in IS NULL)
                        OR (ro.court_id            =    p_court_id_in))
            AND    ((l_offence_desc IS NULL)
                        OR (UPPER(ro.offence_desc) LIKE l_offence_desc))
            AND    ((l_statute IS NULL)
                        OR (UPPER(ro.statute)      LIKE l_statute))
            AND    ((l_offence_code IS NULL)
                        OR (UPPER(ro.offence_code) LIKE l_offence_code))
            AND    ((p_bail_act_in IS NULL)
                        OR (ro.bail_act = p_bail_act_in))
            ORDER BY ro.offence_code;
    END get_ref_offence;


    --
    -- Query the XHB_REF_SOLICITOR_FIRM table
    --
    PROCEDURE get_ref_solicitor_firm_complex(p_results_out            OUT SYS_REFCURSOR,
                                             p_solicitor_firm_name_in IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                                             p_crest_sof_id_in        IN  XHB_REF_SOLICITOR_FIRM.crest_sof_id%TYPE,
                                             p_court_id_in            IN  XHB_REF_SOLICITOR_FIRM.court_id%TYPE)
    AS
        l_solicitor_firm_name CONSTANT XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE := convert_value(p_solicitor_firm_name_in);
    BEGIN
        --log_entry('get_ref_solicitor_firm_complex');

        OPEN p_results_out FOR
            SELECT rsf.ref_solicitor_firm_id AS "id",
                   rsf.*,
                   a.address_id,
                   a.address_1 AS address1,
                   a.address_2 AS address2,
                   a.address_3 AS address3,
                   a.address_4 AS address4,
                   a.town,
                   a.county,
                   a.postcode,
                   a.country
            FROM   XHB_REF_SOLICITOR_FIRM rsf,
                   XHB_ADDRESS a
            WHERE  rsf.address_id = a.address_id
            AND    ((rsf.obs_ind IS NULL) OR (rsf.obs_ind = 'N'))
            AND    ((l_solicitor_firm_name IS NULL)
                        OR (UPPER(rsf.solicitor_firm_name) LIKE l_solicitor_firm_name))
            AND    ((p_crest_sof_id_in IS NULL)
                        OR (rsf.crest_sof_id               =    p_crest_sof_id_in))
            AND    ((p_court_id_in IS NULL)
                        OR (rsf.court_id                   =    p_court_id_in));
    END get_ref_solicitor_firm_complex;


    --
    -- Query the XHB_REF_SYSTEM_CODE table
    --
    PROCEDURE get_ref_system_code(p_results_out   OUT SYS_REFCURSOR,
                                  p_court_id_in   IN  XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                  p_code_type_in  IN  XHB_REF_SYSTEM_CODE.code_type%TYPE,
                                  p_de_code_in    IN  XHB_REF_SYSTEM_CODE.de_code%TYPE,
                                  p_code_in       IN  XHB_REF_SYSTEM_CODE.code%TYPE,
                                  p_code_title_in IN  XHB_REF_SYSTEM_CODE.code_title%TYPE)
    AS
        l_code_type  CONSTANT XHB_REF_SYSTEM_CODE.code_type%TYPE  := convert_value(p_code_type_in);
        l_de_code    CONSTANT XHB_REF_SYSTEM_CODE.de_code%TYPE    := convert_value(p_de_code_in);
        l_code       CONSTANT XHB_REF_SYSTEM_CODE.code%TYPE       := convert_value(p_code_in);
        l_code_title CONSTANT XHB_REF_SYSTEM_CODE.code_title%TYPE := convert_value(p_code_title_in);
    BEGIN
        --log_entry('get_ref_system_code');

        OPEN p_results_out FOR
            SELECT rsc.ref_system_code_id AS "id",
                   rsc.de_code AS DECODE,
                   rsc.code,
                   rsc.code_Type,
                   rsc.court_Id,
                   rsc.version,
                   rsc.code_Title,
                   rsc.ref_Code_Order,
                   rsc.obs_Ind
            FROM   XHB_REF_SYSTEM_CODE rsc
            WHERE  ((rsc.obs_ind IS NULL) OR (rsc.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rsc.court_id          =    p_court_id_in))
            AND    ((l_code_type IS NULL)
                        OR (UPPER(rsc.code_type)  LIKE l_code_type))
            AND    ((l_de_code IS NULL)
                        OR (UPPER(rsc.de_code)    LIKE l_de_code))
            AND    ((l_code IS NULL)
                        OR (UPPER(rsc.code)       LIKE l_code))
            AND    ((l_code_title IS NULL)
                        OR (UPPER(rsc.code_title) LIKE l_code_title));
    END get_ref_system_code;


    --
    -- Query the XHB_REF_SOLICITOR table, but also with search criteria
    -- from XHB_REF_LEGAL_REPRESENTATIVE and XHB_REF_SOLICITOR_FIRM
    --
    PROCEDURE get_solicitor(p_results_out             OUT SYS_REFCURSOR,
                            p_ref_legal_rep_id_in     IN  XHB_REF_SOLICITOR.ref_legal_rep_id%TYPE,
                            p_initials_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                            p_first_name_in           IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                            p_middle_name_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                            p_surname_in              IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                            p_crest_solicitor_name_in IN  XHB_REF_SOLICITOR.crest_solicitor_name%TYPE,
                            p_solicitor_firm_name_in  IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                            p_court_id_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE)
    AS
        l_initials             CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE      := convert_value(p_initials_in);
        l_first_name           CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE    := convert_value(p_first_name_in);
        l_middle_name          CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE   := convert_value(p_middle_name_in);
        l_surname              CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE       := convert_value(p_surname_in);
        l_crest_solicitor_name CONSTANT XHB_REF_SOLICITOR.crest_solicitor_name%TYPE     := convert_value(p_crest_solicitor_name_in);
        l_solicitor_firm_name  CONSTANT XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE := convert_value(p_solicitor_firm_name_in);
    BEGIN
        --log_entry('get_solicitor');

        OPEN p_results_out FOR
            SELECT rs.solicitor_id AS "id",
                   rs.*,
                   rlr.*,
                   rsf.ref_solicitor_firm_id AS "firm_id",
                   rs.ref_legal_rep_id AS "legal_rep_id",
                   rs.is_in_crest AS "in_crest"
            FROM   XHB_REF_SOLICITOR rs,
                   XHB_REF_LEGAL_REPRESENTATIVE rlr,
                   XHB_REF_SOLICITOR_FIRM rsf
            WHERE  rs.ref_legal_rep_id = rlr.ref_legal_rep_id
            AND    ((p_court_id_in IS NULL) OR (rlr.court_id = p_court_id_in))
            AND    rs.ref_solicitor_firm_id = rsf.ref_solicitor_firm_id
            AND    ((rs.obs_ind IS NULL) OR (rs.obs_ind = 'N'))
            AND    ((p_ref_legal_rep_id_in IS NULL)
                        OR (rs.ref_legal_rep_id            =    p_ref_legal_rep_id_in))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rlr.initials)            LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)          LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rlr.middle_name)         LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)             LIKE l_surname))
            AND    ((l_crest_solicitor_name IS NULL)
                        OR (UPPER(rs.crest_solicitor_name) LIKE l_crest_solicitor_name))
            AND    ((l_solicitor_firm_name IS NULL)
                        OR (UPPER(rsf.solicitor_firm_name) LIKE l_solicitor_firm_name));
    END get_solicitor;
END xhb_search_pkg;
/
show errors