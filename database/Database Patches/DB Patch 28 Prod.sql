/*
 * Patch for DB release 28
 *
 * 30th October 2003
 */

/*
 * Changes to XHB_ table definitions
 *
 * This may include removing foreign keys and possibly other constraints
 */

CREATE UNIQUE INDEX xhb_sys_audit_table_to_aud_idx ON XHB_SYS_AUDIT
  (TABLE_TO_AUDIT)
  TABLESPACE XHIBITX
  STORAGE (INITIAL 1M
           NEXT 1M
           PCTINCREASE 0);

CREATE UNIQUE INDEX xhb_court_short_name_idx ON XHB_COURT
  (SHORT_NAME)
  TABLESPACE XHIBITX
  STORAGE (INITIAL 1M
           NEXT 1M
           PCTINCREASE 0);

CREATE UNIQUE INDEX xhb_terminal_terminal_name_idx ON XHB_TERMINAL
  (TERMINAL_NAME)
  TABLESPACE XHIBITX
  STORAGE (INITIAL 1M
           NEXT 1M
           PCTINCREASE 0);


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 * The standard procedure is as follows:
 *
 *     1. Drop the audit table (AUD_)
 *     2. Recreate audit table as select * from XHB_ table with no rows
 *     3. Add the INSERT_EVENT column to the end of the audit table
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 *
 * Note that these are generally the BUR (update and delete) triggers as the BIR
 * (insert) triggers will only change on renaming the auditing columns within the
 * XHB_ table
 */


/*
 * Additional packages/procedures/functions or Changes to these
 */

-------------------------------------------------------------------------------
-- THE PACKAGE BODY
-- The xhibit_search_pkg contains all of the procedures used by the fast
-- lane readers. 
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY xhibit_search_pkg AS
	FUNCTION convert_value(value_in IN VARCHAR2)
						   RETURN VARCHAR2 AS
	BEGIN
		 -- Would we also want to append a % to the start/end of this???
		 RETURN UPPER(value_in);
	END convert_value;




	--
	-- Query the XHB_CONTACT_DETAIL table
	--
	PROCEDURE get_contact_detail(results_out      OUT weak_ref_cursor_type,
			  					 address_id_in	  IN  XHB_CONTACT_DETAIL.address_id%TYPE,
								 contact_type_in  IN  XHB_CONTACT_DETAIL.contact_type%TYPE,
								 contact_value_in IN  XHB_CONTACT_DETAIL.contact_value%TYPE) AS
    BEGIN
		 OPEN results_out FOR
			  SELECT cd.contact_id AS "id",
			  		 cd.*
			  FROM	 XHB_CONTACT_DETAIL cd
			  WHERE	 ((address_id_in IS NULL)
			  		 OR (cd.address_id = address_id_in))	 
			  AND	 ((contact_type_in IS NULL)
			  		 OR (UPPER(cd.contact_type)  LIKE convert_value(contact_type_in)))
			  AND	 ((contact_value_in IS NULL)
			  		 OR (UPPER(cd.contact_value) LIKE convert_value(contact_value_in)));			  
	END get_contact_detail;


	--
	-- Query the XHB_COURT table, with some details from the XHB_COURT_SITE table
	--
	PROCEDURE get_court(results_out       OUT weak_ref_cursor_type,
			  			circuit_in        IN  XHB_COURT.circuit%TYPE,
						court_site_id_in  IN  XHB_COURT_SITE.court_site_id%TYPE,
					 	court_name_in     IN  XHB_COURT.court_name%TYPE,
						court_prefix_in   IN  XHB_COURT.court_prefix%TYPE,
	   				 	court_type_in     IN  XHB_COURT.court_type%TYPE,
	   				 	crest_court_id_in IN  XHB_COURT.crest_court_id%TYPE,
					 	short_name_in     IN  XHB_COURT.short_name%TYPE) AS
    BEGIN
		 OPEN results_out FOR
			  SELECT c.court_id AS "id",
			  		 cs.court_site_code AS "COURT_CODE",
			 	 	 c.*
		 	  FROM   XHB_COURT c, XHB_COURT_SITE cs
			  WHERE  c.court_id              =    cs.court_id
			  AND	 ((c.obs_ind IS NULL) OR (c.obs_ind = 'N'))
			  AND	 ((court_site_id_in IS NULL)
			  		 OR (UPPER(cs.court_site_id) LIKE convert_value(court_site_id_in)))
			  AND	 ((circuit_in IS NULL)
			  		 OR (UPPER(c.circuit)        LIKE convert_value(circuit_in)))
			  AND	 ((court_name_in IS NULL)
			  		 OR (UPPER(c.court_name)     LIKE convert_value(court_name_in)))
			  AND	 ((court_prefix_in IS NULL)
			  		 OR (UPPER(c.court_prefix)   LIKE convert_value(court_prefix_in)))
			  AND	 ((court_type_in IS NULL)
			  		 OR (UPPER(c.court_type)     LIKE convert_value(court_type_in)))
			  AND	 ((crest_court_id_in IS NULL)	 
			  		 OR (UPPER(c.crest_court_id) LIKE convert_value(crest_court_id_in)))
			  AND	 ((short_name_in IS NULL)
			  		 OR (UPPER(c.short_name)     LIKE convert_value(short_name_in)));
	END get_court;


	--
	-- Query the XHB_COURT_ROOM table, but also with search criteria from 
	-- XHB_COURT_SITE and XHB_COURT
	--
	PROCEDURE get_court_room(results_out            OUT weak_ref_cursor_type,
	                         court_room_name_in     IN  XHB_COURT_ROOM.court_room_name%TYPE,
							 court_site_code_in     IN  XHB_COURT_SITE.court_site_code%TYPE,
							 court_site_id_in       IN  XHB_COURT_SITE.court_site_id%TYPE,
							 crest_court_room_no_in IN  XHB_COURT_ROOM.crest_court_room_no%TYPE,
							 short_name_in			IN  XHB_COURT.short_name%TYPE) AS
    BEGIN
		 OPEN results_out FOR	 		 	  
			SELECT DISTINCT cr.court_room_id AS "id",
				 t.location AS location,
				 cr.*
			FROM   XHB_COURT_ROOM cr,
				 XHB_COURT_SITE cs,
				 XHB_COURT c,
				 XHB_TERMINAL t
			WHERE  cr.court_site_id = cs.court_site_id
			AND	 c.court_id       = cs.court_id
			AND	 cr.court_room_id = t.court_room_id(+)
			AND	 ((cr.obs_ind IS NULL) OR (cr.obs_ind = 'N'))
			AND	 ((court_room_name_in IS NULL)
			  		 OR (UPPER(cr.court_room_name) LIKE convert_value(court_room_name_in)))
			AND	 ((court_site_code_in IS NULL)
			  		 OR (UPPER(cs.court_site_code) LIKE convert_value(court_site_code_in)))
			AND	 ((court_site_id_in IS NULL)
			 		 OR (cs.court_site_id 		   =    court_site_id_in))
			AND	 ((crest_court_room_no_in IS NULL)
			  		 OR (cr.crest_court_room_no    =    crest_court_room_no_in))
			AND	 ((short_name_in IS NULL)
			  		 OR (UPPER(c.short_name) 	   LIKE convert_value(short_name_in)));
	END get_court_room;


	--
	-- Query the XHB_COURT_SITE table, but also with search criteria
	-- from XHB_COURT
	--
	PROCEDURE get_court_site(results_out         OUT weak_ref_cursor_type,
			  				 court_site_code_in  IN  XHB_COURT_SITE.court_site_code%TYPE,
							 court_id_in		 IN  XHB_COURT.court_id%TYPE,
							 court_short_name_in IN  XHB_COURT.short_name%TYPE,
							 court_site_name_in  IN  XHB_COURT_SITE.court_site_name%TYPE) AS
    BEGIN
		 OPEN results_out FOR
	     	  SELECT cs.court_site_id AS "id",
			  		 cs.*
	    	  FROM   XHB_COURT_SITE cs, XHB_COURT c
			  WHERE  cs.court_id               =    c.court_id
			  AND	 ((cs.obs_ind IS NULL) OR (cs.obs_ind = 'N'))
			  AND	 ((court_site_code_in IS NULL)
			  		 OR (UPPER(cs.court_site_code) LIKE convert_value(court_site_code_in)))
			  AND	 ((court_id_in IS NULL)	 
			  		 OR (c.court_id                =    court_id_in))
			  AND	 ((court_short_name_in IS NULL)
			  		 OR (UPPER(c.short_name) 	    LIKE convert_value(court_short_name_in)))
			  AND	 ((court_site_name_in IS NULL)
			  		 OR (UPPER(cs.court_site_name) LIKE convert_value(court_site_name_in)));
	END get_court_site;


	--
	-- Query the XHB_TERMINAL table
	--
	PROCEDURE get_terminal(results_out         OUT weak_ref_cursor_type,
						   terminal_name_in	IN  XHB_TERMINAL.terminal_name%TYPE) AS
	BEGIN
		 OPEN results_out FOR
		 	  SELECT t.terminal_id AS "id",
			  		 t.*
			  FROM   XHB_TERMINAL t
			  WHERE  ((terminal_name_in IS NULL)
			  		 OR (UPPER(t.terminal_name) LIKE  convert_value(terminal_name_in)));
	END get_terminal;


	--
	-- Query the XHB_REF_ADVOCATE table, with some details from
	-- XHB_REF_LEGAL_REPRESENTATIVE
	--
	PROCEDURE get_ref_advocate(results_out         OUT weak_ref_cursor_type,
						   	   adv_type_ind_in     IN  XHB_REF_ADVOCATE.adv_type_ind%TYPE,
						   	   initials_in		   IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
						   	   first_name_in	   IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
						   	   middle_name_in	   IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
						   	   surname_in		   IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
						   	   ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE,
                               firm_name_in        IN  XHB_REF_CHAMBER.firm_name%TYPE) AS
    BEGIN
		 OPEN results_out FOR
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
                     ra.obs_ind
              FROM   XHB_REF_ADVOCATE ra, XHB_REF_LEGAL_REPRESENTATIVE rlr, XHB_REF_CHAMBER rc
              WHERE  ra.ref_legal_rep_id = rlr.ref_legal_rep_id
              AND    ra.ref_chamber_id = rc.ref_chamber_id
              AND    ((ra.obs_ind IS NULL) OR (ra.obs_ind = 'N'))
              AND    ((adv_type_ind_in IS NULL)
                     OR (UPPER(ra.adv_type_ind) LIKE convert_value(adv_type_ind_in)))
              AND    ((ref_legal_rep_id_in IS NULL)
                     OR (ra.ref_legal_rep_id    =    ref_legal_rep_id_in))
              AND    ((initials_in IS NULL)
                     OR (UPPER(rlr.initials)    LIKE convert_value(initials_in)))
              AND    ((first_name_in IS NULL)
                     OR (UPPER(rlr.first_name)  LIKE convert_value(first_name_in)))
              AND    ((middle_name_in IS NULL)
                     OR (UPPER(rlr.middle_name) LIKE convert_value(middle_name_in)))
              AND    ((surname_in IS NULL)
                     OR (UPPER(rlr.surname)     LIKE convert_value(surname_in)))
              AND    ((firm_name_in IS NULL)
                     OR (UPPER(rc.firm_name)    LIKE convert_value(firm_name_in)))
              ORDER BY rlr.surname;
    END get_ref_advocate;


	--
	-- Query the XHB_REF_APP_RESULT table
	--
	PROCEDURE get_ref_app_result(results_out        OUT weak_ref_cursor_type,
			  					 court_id_in		IN  XHB_REF_APP_RESULT.court_id%TYPE,
								 app_result_code_in IN  XHB_REF_APP_RESULT.app_result_code%TYPE,
								 ho_code_in			IN  XHB_REF_APP_RESULT.ho_code%TYPE,
								 vary_sentence_in	IN  XHB_REF_APP_RESULT.vary_sentence%TYPE,
								 lesser_off_ind_in  IN  XHB_REF_APP_RESULT.lesser_off_ind%TYPE) AS
	BEGIN
		 OPEN results_out FOR
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
			  AND	 ((court_id_in IS NULL)
			  		 OR (rap.court_id               =    court_id_in))
			  AND	 ((app_result_code_in IS NULL)
			  		 OR (UPPER(rap.app_result_code) LIKE convert_value(app_result_code_in)))
			  AND	 ((ho_code_in IS NULL)
			  		 OR (rap.ho_code                =    ho_code_in))
			  AND	 ((vary_sentence_in IS NULL)
			  		 OR (UPPER(rap.vary_sentence)   LIKE convert_value(vary_sentence_in)))
			  AND	 ((lesser_off_ind_in IS NULL)
			  		 OR (UPPER(rap.lesser_off_ind)  LIKE convert_value(lesser_off_ind_in)));
	END get_ref_app_result;


	--
	-- Query the XHB_REF_COURT table, but also with search criteria
	-- from XHB_COURT
	--
	PROCEDURE get_ref_court(results_out         OUT weak_ref_cursor_type,
			  				court_id_in         IN  XHB_REF_COURT.court_id%TYPE,
							circuit_in			IN	XHB_COURT.circuit%TYPE,
							court_full_name_in  IN  XHB_REF_COURT.court_full_name%TYPE,
							court_prefix_in	    IN  XHB_REF_COURT.name_prefix%TYPE,
							court_type_in	    IN  XHB_REF_COURT.court_type%TYPE,
							crest_court_id_in 	IN	XHB_COURT.crest_court_id%TYPE,
							court_short_name_in IN  XHB_REF_COURT.court_short_name%TYPE,
							is_psd_in			IN	XHB_REF_COURT.is_psd%TYPE) AS
    BEGIN
		 OPEN results_out FOR
			  SELECT rc.ref_court_id AS "id",
			  		 rc.*
			  FROM   XHB_REF_COURT rc, XHB_COURT c
			  WHERE  rc.court_id                =    c.court_id
			  AND	 ((rc.obs_ind IS NULL) OR (rc.obs_ind = 'N'))
			  AND	 ((court_id_in IS NULL)
			  		 OR (rc.court_id                =    court_id_in))
			  AND	 ((circuit_in IS NULL)
			  		 OR (UPPER(c.circuit) 		    LIKE convert_value(circuit_in)))
			  AND	 ((court_full_name_in IS NULL)
			  		 OR (UPPER(rc.court_full_name)  LIKE convert_value(court_full_name_in)))
			  AND	 ((court_prefix_in IS NULL)
			  		 OR (UPPER(rc.name_prefix) 	    LIKE convert_value(court_prefix_in)))
			  AND	 ((court_type_in IS NULL)
			  		 OR (UPPER(rc.court_type) 	    LIKE convert_value(court_type_in)))
			  AND	 ((crest_court_id_in IS NULL)
			  		 OR (c.crest_court_id 		    =    crest_court_id_in))
			  AND	 ((court_short_name_in IS NULL)
			  		 OR (UPPER(rc.court_short_name) LIKE convert_value(court_short_name_in)))
			  AND	 ((is_psd_in IS NULL)
			  		 OR (UPPER(rc.is_psd)  		    LIKE convert_value(is_psd_in)));
	END get_ref_court;


	--
	-- Query the XHB_REF_COURT_REPORTER table, but also with search criteria
	-- from XHB_REF_COURT_REPORTER_FIRM
	--
	PROCEDURE get_ref_court_reporter(results_out    OUT weak_ref_cursor_type,
			  						 court_id_in    IN  XHB_REF_COURT_REPORTER.court_id%TYPE,
									 firm_name_in	IN	XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
									 initials_in	IN	XHB_REF_COURT_REPORTER.initials%TYPE,
									 first_name_in	IN	XHB_REF_COURT_REPORTER.first_name%TYPE,
									 middle_name_in	IN	XHB_REF_COURT_REPORTER.middle_name%TYPE,
									 surname_in		IN	XHB_REF_COURT_REPORTER.surname%TYPE) AS
    BEGIN
		 OPEN results_out FOR
		 	SELECT rcr.ref_court_reporter_id AS "id",
				   rcr.*
			FROM   XHB_REF_COURT_REPORTER rcr, XHB_REF_COURT_REPORTER_FIRM rcrf 
			WHERE  rcr.ref_court_reporter_firm_id = rcrf.ref_court_reporter_firm_id
			AND	   ((rcr.obs_ind IS NULL) OR (rcr.obs_ind = 'N'))
			AND	   ((court_id_in IS NULL)
				   OR (rcr.court_id           =    court_id_in))
			AND	   ((firm_name_in IS NULL)
				   OR (UPPER(rcrf.firm_name)  LIKE convert_value(firm_name_in)))
			AND	   ((initials_in IS NULL)
				   OR (UPPER(rcr.initials)    LIKE convert_value(initials_in)))
			AND	   ((first_name_in IS NULL)
				   OR (UPPER(rcr.first_name)  LIKE convert_value(first_name_in)))
			AND	   ((middle_name_in IS NULL)
				   OR (UPPER(rcr.middle_name) LIKE convert_value(middle_name_in)))
			AND	   ((surname_in IS NULL)
				   OR (UPPER(rcr.surname)     LIKE convert_value(surname_in)));
	END get_ref_court_reporter;

	
	--
	-- Query the XHB_REF_COURT_REPORTER_FIRM table
	--
	PROCEDURE get_ref_court_reporter_firm(results_out  OUT weak_ref_cursor_type,
			  							  firm_name_in IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE) AS
	BEGIN
		 OPEN results_out FOR
		 	SELECT rcrf.ref_court_reporter_firm_id AS "id",
				   rcrf.*
			FROM   XHB_REF_COURT_REPORTER_FIRM rcrf			
			WHERE  ((rcrf.obs_ind IS NULL) OR (rcrf.obs_ind = 'N'))
			AND	   ((firm_name_in IS NULL)
				   OR (UPPER(rcrf.firm_name) LIKE convert_value(firm_name_in))); 
	END get_ref_court_reporter_firm;

	
	--
	-- Query the XHB_REF_DISPOSAL table
	--
	PROCEDURE get_ref_disposal(results_out 		 OUT weak_ref_cursor_type,
							   disposal_code_in  IN  XHB_REF_DISPOSAL.disposal_code%TYPE,
							   disposal_title_in IN  XHB_REF_DISPOSAL.disposal_title%TYPE,
							   court_id_in		 IN  XHB_REF_DISPOSAL.court_id%TYPE) AS
	BEGIN
		 OPEN results_out FOR
		 	SELECT rd.ref_disposal_id AS "id",
				   rd.*
			FROM   XHB_REF_DISPOSAL rd
			WHERE  ((rd.obs_ind IS NULL) OR (rd.obs_ind = 'N'))
			AND	   ((disposal_code_in IS NULL)
				   OR (UPPER(rd.disposal_code) 	LIKE convert_value(disposal_code_in)))
			AND	   ((disposal_title_in IS NULL)
				   OR (UPPER(rd.disposal_title) LIKE convert_value(disposal_title_in)))
			AND	   ((court_id_in IS NULL)
				   OR (rd.court_id              =    court_id_in));
	END get_ref_disposal;

	
	--
	-- Query the XHB_REF_DISPOSAL_MENU table
	--
	PROCEDURE get_ref_disposal_menu(results_out           OUT weak_ref_cursor_type,
			  						abbrev_in             IN  XHB_REF_DISPOSAL_MENU.abbrev%TYPE,
									court_id_in           IN  XHB_REF_DISPOSAL_MENU.court_id%TYPE,
									crest_menu_item_id_in IN  XHB_REF_DISPOSAL_MENU.crest_menu_item_id%TYPE,
									disposal_code_in	  IN  XHB_REF_DISPOSAL_MENU.disposal_code%TYPE,
									menu_group_in		  IN  XHB_REF_DISPOSAL_MENU.menu_group%TYPE,
									parent_in			  IN  XHB_REF_DISPOSAL_MENU.parent%TYPE,
									title_in			  IN  XHB_REF_DISPOSAL_MENU.title%TYPE) AS
	BEGIN
		 OPEN results_out FOR
			SELECT rdm.ref_disposal_menu_id AS "id",
				   rdm.title AS viewable_title,
				   rdm.*
			FROM   XHB_REF_DISPOSAL_MENU rdm
			WHERE  ((rdm.obs_ind IS NULL) OR (rdm.obs_ind = 'N'))
			AND	   ((abbrev_in IS NULL)
				   OR (UPPER(rdm.abbrev)        LIKE convert_value(abbrev_in)))
			AND	   ((court_id_in IS NULL)
				   OR (rdm.court_id             =    court_id_in))
			AND	   ((crest_menu_item_id_in IS NULL)
				   OR (rdm.crest_menu_item_id   =    crest_menu_item_id_in))
			AND	   ((disposal_code_in IS NULL)
				   OR (UPPER(rdm.disposal_code) LIKE convert_value(disposal_code_in)))
			AND	   ((menu_group_in IS NULL)
				   OR (UPPER(rdm.menu_group)    LIKE convert_value(menu_group_in)))
			AND	   ((parent_in IS NULL)
				   OR (rdm.parent               =    parent_in))
			AND	   ((title_in IS NULL)
				   OR (UPPER(rdm.title)         LIKE convert_value(title_in)));
	END get_ref_disposal_menu;
	
	
	--
	-- Query the XHB_REF_HEARING_TYPE table
	--
	PROCEDURE get_ref_hearing_type(results_out          OUT weak_ref_cursor_type,
								   hearing_type_code_in	IN XHB_REF_HEARING_TYPE.hearing_type_code%TYPE) AS
	BEGIN
		 OPEN results_out FOR
		 	SELECT rht.ref_hearing_type_id AS "id",
				   rht.*
			FROM   XHB_REF_HEARING_TYPE rht
			WHERE  ((rht.obs_ind IS NULL) OR (rht.obs_ind = 'N'))
			AND	   ((hearing_type_code_in IS NULL)
				   OR (UPPER(rht.hearing_type_code) LIKE convert_value(hearing_type_code_in)));
	END get_ref_hearing_type;



	--
	-- Query the XHB_REF_JUDGE table
	--
	PROCEDURE get_ref_judge(results_out    OUT weak_ref_cursor_type,
							first_name_in  IN  XHB_REF_JUDGE.first_name%TYPE,
							middle_name_in IN  XHB_REF_JUDGE.middle_name%TYPE,
							surname_in	   IN  XHB_REF_JUDGE.surname%TYPE) AS
	BEGIN
		 OPEN results_out FOR
		 	SELECT rj.ref_judge_id AS "id",
				   rj.*
			FROM   XHB_REF_JUDGE rj
			WHERE  ((rj.obs_ind IS NULL) OR (rj.obs_ind  = 'N'))
			AND	   ((first_name_in IS NULL)
				   OR (UPPER(rj.first_name)  LIKE convert_value(first_name_in)))
			AND	   ((middle_name_in IS NULL)
				   OR (UPPER(rj.middle_name) LIKE convert_value(middle_name_in)))
			AND	   ((surname_in IS NULL)
				   OR (UPPER(rj.surname)     LIKE convert_value(surname_in)));
	END get_ref_judge;
	
	
	--
	-- Query the XHB_REF_JUSTICE table
	--
	PROCEDURE get_ref_justice(results_out     OUT weak_ref_cursor_type,
							  justice_name_in IN  XHB_REF_JUSTICE.justice_name%TYPE) AS
	BEGIN
		 OPEN results_out FOR
			SELECT rj.ref_justice_id AS "id",
				   rj.court_id AS court_i_d,
				   rj.*
			FROM   XHB_REF_JUSTICE rj			
			WHERE  ((rj.obs_ind IS NULL) OR (rj.obs_ind = 'N'))
			AND	   ((justice_name_in IS NULL)
				   OR (UPPER(rj.justice_name) LIKE convert_value(justice_name_in)));
	END get_ref_justice;
	
	
	--
	-- Query the XHB_REF_LEGAL_REPRESENTATIVE table
	--
	PROCEDURE get_ref_legal_representative(results_out 		 OUT weak_ref_cursor_type,
										   court_id_in		 IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
										   first_name_in	 IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
										   surname_in		 IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
										   legal_rep_type_in IN  XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE) AS
	BEGIN
		 OPEN results_out FOR
			SELECT rlr.ref_legal_rep_id AS "id",
				   rlr.*
			FROM   XHB_REF_LEGAL_REPRESENTATIVE rlr
			WHERE  ((rlr.obs_ind IS NULL) OR (rlr.obs_ind = 'N'))
			AND	   ((court_id_in IS NULL)
				   OR (rlr.court_id             =    court_id_in))
			AND	   ((first_name_in IS NULL)
				   OR (UPPER(first_name)		LIKE convert_value(first_name_in)))
			AND	   ((surname_in IS NULL)
				   OR (UPPER(surname_in) 	  	LIKE convert_value(surname_in)))
			AND	   ((legal_rep_type_in IS NULL)
				   OR (UPPER(legal_rep_type_in) LIKE convert_value(legal_rep_type_in)));
	END get_ref_legal_representative;


	--
	-- Query the XHB_REF_OFFENCE table
	--
	PROCEDURE get_ref_offence(results_out  	  OUT weak_ref_cursor_type,
							  act_section_in  IN  XHB_REF_OFFENCE.act_section%TYPE,
							  court_id_in	  IN  XHB_REF_OFFENCE.court_id%TYPE,
							  offence_desc_in IN  XHB_REF_OFFENCE.offence_desc%TYPE,
							  statute_in 	  IN  XHB_REF_OFFENCE.statute%TYPE,
							  offence_code_in IN  XHB_REF_OFFENCE.offence_code%TYPE) AS
	BEGIN
		 OPEN results_out FOR
			SELECT ro.ref_offence_id AS "id",
				   ro.*,
				   -- Can't find what this IS
				   'OFFENCE TYPE' AS offence_type
			FROM   XHB_REF_OFFENCE ro
			WHERE  ((ro.obs_ind IS NULL) OR (ro.obs_ind = 'N'))
			AND	   ((act_section_in IS NULL)
				   OR (UPPER(ro.act_section)  LIKE convert_value(act_section_in)))
			AND	   ((court_id_in IS NULL)
				   OR (ro.court_id 			  =    court_id_in))
			AND	   ((offence_desc_in IS NULL)
				   OR (UPPER(ro.offence_desc) LIKE convert_value(offence_desc_in)))
			AND	   ((statute_in IS NULL)
				   OR (UPPER(ro.statute) 	  LIKE convert_value(statute_in)))
			AND	   ((offence_code_in IS NULL)
				   OR (UPPER(ro.offence_code) LIKE convert_value(offence_code_in)))
			ORDER BY ro.offence_code;
	END get_ref_offence;


	--
	-- Query the XHB_REF_SOLICITOR_FIRM table
	--
	PROCEDURE get_ref_solicitor_firm(results_out 			OUT weak_ref_cursor_type,
									 solicitor_firm_name_in IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
									 crest_sof_id_in		IN  XHB_REF_SOLICITOR_FIRM.crest_sof_id%TYPE,
									 court_id_in			IN  XHB_REF_SOLICITOR_FIRM.court_id%TYPE) AS
	BEGIN
		 OPEN results_out FOR
		 	SELECT rsf.ref_solicitor_firm_id AS "id",
				   rsf.*
			FROM   XHB_REF_SOLICITOR_FIRM rsf
			WHERE  ((rsf.obs_ind IS NULL) OR (rsf.obs_ind = 'N'))
			AND	   ((solicitor_firm_name_in IS NULL)
				   OR (UPPER(rsf.solicitor_firm_name) LIKE convert_value(solicitor_firm_name_in)))
			AND	   ((crest_sof_id_in IS NULL)
				   OR (rsf.crest_sof_id               =    crest_sof_id_in))
			AND	   ((court_id_in IS NULL)
				   OR (rsf.court_id                   =    court_id_in));
	END get_ref_solicitor_firm;
	

	--
	-- Query the XHB_REF_SYSTEM_CODE table
	--
	PROCEDURE get_ref_system_code(results_out   OUT weak_ref_cursor_type,
								  court_id_in	IN  XHB_REF_SYSTEM_CODE.court_id%TYPE,
								  code_type_in	IN  XHB_REF_SYSTEM_CODE.code_type%TYPE,
								  de_code_in	IN	XHB_REF_SYSTEM_CODE.de_code%TYPE,
								  code_in		IN	XHB_REF_SYSTEM_CODE.code%TYPE,
								  code_title_in IN	XHB_REF_SYSTEM_CODE.code_title%TYPE) AS
	BEGIN
		 OPEN results_out FOR
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
			AND	   ((court_id_in IS NULL)
				   OR (rsc.court_id          =    court_id_in))
			AND	   ((code_type_in IS NULL)
				   OR (UPPER(rsc.code_type)  LIKE convert_value(code_type_in)))
			AND	   ((de_code_in IS NULL)
				   OR (UPPER(rsc.de_code) 	 LIKE convert_value(de_code_in)))
			AND	   ((code_in IS NULL)
				   OR (UPPER(rsc.code) 	  	 LIKE convert_value(code_in)))
			AND	   ((code_title_in IS NULL)
				   OR (UPPER(rsc.code_title) LIKE convert_value(code_title_in)));
	END get_ref_system_code;


	--
	-- Query the XHB_REF_SOLICITOR table, but also with search criteria
	-- from XHB_REF_LEGAL_REPRESENTATIVE and XHB_REF_SOLICITOR_FIRM
	--
	PROCEDURE get_solicitor(results_out OUT weak_ref_cursor_type,
			  				ref_legal_rep_id_in IN XHB_REF_SOLICITOR.ref_legal_rep_id%TYPE,
							initials_in IN XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
							first_name_in IN XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
							middle_name_in IN XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
							surname_in IN XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
							crest_solicitor_name_in IN XHB_REF_SOLICITOR.crest_solicitor_name%TYPE,
							solicitor_firm_name_in IN XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE) AS
	BEGIN
		 OPEN results_out FOR
			SELECT rs.solicitor_id AS "id",
                   rs.*,
                   rlr.*,
                   rsf.ref_solicitor_firm_id AS "firm_id",
                   rs.ref_legal_rep_id AS "legal_rep_id",
                   rs.is_in_crest AS "in_crest"
			FROM   XHB_REF_SOLICITOR rs, XHB_REF_LEGAL_REPRESENTATIVE rlr, XHB_REF_SOLICITOR_FIRM rsf
			WHERE  rs.ref_legal_rep_id = rlr.ref_legal_rep_id
			AND	   rs.ref_solicitor_firm_id = rsf.ref_solicitor_firm_id
			AND	   ((rs.obs_ind IS NULL) OR (rs.obs_ind = 'N'))
			AND	   ((ref_legal_rep_id_in IS NULL)
				   OR (rs.ref_legal_rep_id = ref_legal_rep_id_in))
			AND	   ((initials_in IS NULL)
				   OR (UPPER(rlr.initials) LIKE convert_value(initials_in)))
			AND	   ((first_name_in IS NULL)
				   OR (UPPER(rlr.first_name) LIKE convert_value(first_name_in)))
			AND	   ((middle_name_in IS NULL)
				   OR (UPPER(rlr.middle_name) LIKE convert_value(middle_name_in)))
			AND	   ((surname_in IS NULL)
				   OR (UPPER(rlr.surname) LIKE convert_value(surname_in)))
			AND	   ((crest_solicitor_name_in IS NULL)
				   OR (UPPER(rs.crest_solicitor_name) LIKE convert_value(crest_solicitor_name_in)))
			AND	   ((solicitor_firm_name_in IS NULL)
				   OR (UPPER(rsf.solicitor_firm_name) LIKE convert_value(solicitor_firm_name_in)));
	END get_solicitor;


/*
  	-- All of these ones have a criteria object but am unable to complete the
	-- stored procedures for them, and therefore they are being excluded from
	-- the fast lane reader process for now...
    PROCEDURE get_plea(results_out OUT weak_ref_cursor_type);

	PROCEDURE get_ref_chamber(results_out OUT weak_ref_cursor_type);

	PROCEDURE get_ref_home_office_Proceeding(results_out OUT weak_ref_cursor_type);

	PROCEDURE get_ref_prosecutor_agency(results_out OUT weak_ref_cursor_type);

	PROCEDURE get_verdict(results_out OUT weak_ref_cursor_type);
*/
END xhibit_search_pkg;
/
show errors

CREATE OR REPLACE PACKAGE xhb_court_log_pkg AS

  PROCEDURE get_by_case_id(results_out  OUT SYS_REFCURSOR,
                           case_id_in   IN  XHB_COURT_LOG_ENTRY.case_id%TYPE);

  PROCEDURE get_by_case_id_date(results_out    OUT SYS_REFCURSOR,
                                case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                start_date_in  IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

  PROCEDURE get_by_case_id_date_catdesc(results_out    OUT SYS_REFCURSOR,
                                        case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        start_date_in  IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                        end_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                        cat_desc_in    IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE);

END xhb_court_log_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_court_log_pkg AS

  PROCEDURE get_by_case_id(results_out  OUT SYS_REFCURSOR,
                           case_id_in   IN  XHB_COURT_LOG_ENTRY.case_id%TYPE) AS

  BEGIN

    OPEN results_out FOR

      SELECT ENTRY_ID,
             CASE_ID,
             VERSION,
             LAST_UPDATED_BY,
             CREATED_BY,
             CREATION_DATE,
             LAST_UPDATE_DATE,
             DATE_TIME,
             EVENT_DESC_ID,
             LOG_ENTRY_XML,
             DEFENDANT_ON_CASE_ID,
             DEFENDANT_ON_OFFENCE_ID
      FROM   XHB_COURT_LOG_ENTRY
      WHERE  CASE_ID = case_id_in	
      ORDER  BY DATE_TIME;		   

  END get_by_case_id;

  PROCEDURE get_by_case_id_date(results_out    OUT SYS_REFCURSOR,
                                case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                start_date_in  IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE) AS

  BEGIN

    OPEN results_out FOR

      SELECT ENTRY_ID,
             CASE_ID,
             VERSION,
             LAST_UPDATED_BY,
             CREATED_BY,
             CREATION_DATE,
             LAST_UPDATE_DATE,
             DATE_TIME,
             EVENT_DESC_ID,
             LOG_ENTRY_XML,
             DEFENDANT_ON_CASE_ID,
             DEFENDANT_ON_OFFENCE_ID
      FROM   XHB_COURT_LOG_ENTRY
      WHERE  CASE_ID = case_id_in	
      AND    DATE_TIME BETWEEN start_date_in AND end_date_in
      ORDER  BY DATE_TIME;		   

  END get_by_case_id_date;

  PROCEDURE get_by_case_id_date_catdesc(results_out    OUT SYS_REFCURSOR,
                                        case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        start_date_in  IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                        end_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                        cat_desc_in    IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE) AS

  BEGIN

    OPEN results_out FOR

      SELECT COURT_LOG_ENTRY.ENTRY_ID,
             COURT_LOG_ENTRY.CASE_ID,
             COURT_LOG_ENTRY.VERSION,
             COURT_LOG_ENTRY.LAST_UPDATED_BY,
             COURT_LOG_ENTRY.CREATED_BY,
             COURT_LOG_ENTRY.CREATION_DATE,
             COURT_LOG_ENTRY.LAST_UPDATE_DATE,
             COURT_LOG_ENTRY.DATE_TIME,
             COURT_LOG_ENTRY.EVENT_DESC_ID,
             COURT_LOG_ENTRY.LOG_ENTRY_XML,
             COURT_LOG_ENTRY.DEFENDANT_ON_CASE_ID,
             COURT_LOG_ENTRY.DEFENDANT_ON_OFFENCE_ID
      FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY,
             XHB_COURT_LOG_CATEGORY_DESC CATEGORY_DESC,
             XHB_COURT_LOG_CATEGORY CATEGORY
      WHERE  CATEGORY_DESC.category_desc_id = CATEGORY.category_desc_id
      AND    CATEGORY_DESC.category_description = cat_desc_in
      AND    COURT_LOG_ENTRY.EVENT_DESC_ID = CATEGORY.EVENT_DESC_ID
      AND    COURT_LOG_ENTRY.CASE_ID = case_id_in	
      AND    COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
      ORDER  BY COURT_LOG_ENTRY.DATE_TIME;		   

  END get_by_case_id_date_catdesc;

END xhb_court_log_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_view_schedule_pkg AS
	   PROCEDURE get_schedule(results_out      OUT SYS_REFCURSOR,
                              court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
							  start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE,
							  court_room_id_in IN  XHB_SITTING.court_room_id%TYPE) AS
       BEGIN
	   		-- would we want to validate the passed in parameters?
			-- court_id_in and start_date_in to be not null?
	   
	        OPEN results_out FOR
				SELECT DISTINCT
					HEARING_LIST.START_DATE,
				      SITTING.SITTING_SEQUENCE_NO, SITTING.IS_FLOATING,
				      COURT_ROOM.COURT_ROOM_ID, COURT_ROOM.COURT_ROOM_NAME, COURT_ROOM.DESCRIPTION, COURT_ROOM.CREST_COURT_ROOM_NO, COURT_ROOM.COURT_SITE_ID, COURT_ROOM.VERSION AS COURT_ROOM_VERSION, COURT_ROOM.DISPLAY_NAME,
				      SCHEDULED_HEARING.SCHEDULED_HEARING_ID, SCHEDULED_HEARING.SEQUENCE_NO, SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME, SCHEDULED_HEARING.LISTING_NOTE, SCHEDULED_HEARING.HEARING_PROGRESS, SCHEDULED_HEARING.SITTING_ID, SCHEDULED_HEARING.HEARING_ID, SCHEDULED_HEARING.MOVED_FROM, SCHEDULED_HEARING.VERSION AS SCHEDULED_HEARING_VERSION, SCHEDULED_HEARING.LINKED_SH_ID, SCHEDULED_HEARING.END_TIME, SCHEDULED_HEARING.START_TIME, SCHEDULED_HEARING.DATE_OF_HEARING, SCHEDULED_HEARING.IS_CASE_ACTIVE,
				      CASE.CASE_ID, CASE.CASE_NUMBER, CASE.CASE_TYPE, CASE.MAG_CONVICTION_DATE, CASE.CASE_SUB_TYPE, CASE.CASE_TITLE, CASE.CASE_DESCRIPTION, CASE.LINKED_CASE_ID, CASE.BAIL_MAG_CODE, CASE.REF_COURT_ID, CASE.COURT_ID, CASE.CHARGE_IMPORT_INDICATOR, CASE.SEVERED_IND, CASE.INDICT_RESP, CASE.DATE_IND_REC, CASE.PROS_AGENCY_REFERENCE, CASE.VERSION AS CASE_VERSION, CASE.CASE_CLASS, CASE.JUDGE_REASON_FOR_APPEAL, CASE.RESULTS_VERIFIED, CASE.LENGTH_TAPE, CASE.NO_PAGE_PROS_EVIDENCE, CASE.NO_PROS_WITNESS, CASE.EST_PDH_TRIAL_LENGTH, CASE.INDICTMENT_INFO_1, CASE.INDICTMENT_INFO_2, CASE.INDICTMENT_INFO_3, CASE.INDICTMENT_INFO_4, CASE.INDICTMENT_INFO_5, CASE.INDICTMENT_INFO_6, CASE.POLICE_OFFICER_ATTENDING, CASE.CPS_CASE_WORKER, CASE.EXPORT_CHARGES, CASE.IND_CHANGE_STATUS,
				      REF_HEARING_TYPE.REF_HEARING_TYPE_ID, REF_HEARING_TYPE.HEARING_TYPE_CODE, REF_HEARING_TYPE.HEARING_TYPE_DESC, REF_HEARING_TYPE.CATEGORY, REF_HEARING_TYPE.SEQ_NO, REF_HEARING_TYPE.LIST_SEQUENCE, REF_HEARING_TYPE.VERSION AS REF_HEARING_TYPE_VERSION, REF_HEARING_TYPE.COURT_ID, REF_HEARING_TYPE.OBS_IND AS REF_HEARING_TYPE_OBS_IND,
				      DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID, DEFENDANT_ON_CASE.FINAL_DRIVING_LICENCE_STATUS, DEFENDANT_ON_CASE.PTIURN, DEFENDANT_ON_CASE.IS_JUVENILE, DEFENDANT_ON_CASE.IS_MASKED, DEFENDANT_ON_CASE.MASKED_NAME, DEFENDANT_ON_CASE.CASE_ID, DEFENDANT_ON_CASE.DEFENDANT_ID, DEFENDANT_ON_CASE.VERSION AS DEFENDANT_ON_CASE_VERSION, DEFENDANT_ON_CASE.OBS_IND AS DEFENDANT_ON_CASE_OBS_IND, DEFENDANT_ON_CASE.RESULTS_VERIFIED, DEFENDANT_ON_CASE.DEFENDANT_NUMBER, DEFENDANT_ON_CASE.DATE_OF_COMMITTAL, DEFENDANT_ON_CASE.NO_OF_TICS,
				      DEFENDANT.CREST_DEFENDANT_ID, DEFENDANT.FIRST_NAME, DEFENDANT.MIDDLE_NAME, DEFENDANT.SURNAME, DEFENDANT.INITIALS, DEFENDANT.DATE_OF_BIRTH, DEFENDANT.GENDER, DEFENDANT.LAST_CONVICTION_DATE, DEFENDANT.IS_COMPANY, DEFENDANT.VERSION AS DEFENDANT_ON_VERSION, DEFENDANT.ADDRESS_ID, DEFENDANT.COURT_ID,
				      REF_JUDGE.TITLE JUDGE_TITLE, REF_JUDGE.FIRST_NAME JUDGE_FIRST_NAME, REF_JUDGE.MIDDLE_NAME JUDGE_MIDDLE_NAME, REF_JUDGE.SURNAME JUDGE_SURNAME, REF_JUDGE.REF_JUDGE_ID JUDGE_ID, REF_JUDGE.FULL_LIST_TITLE1 JUDGE_FULL_TITLE1				
				FROM  XHB_HEARING_LIST HEARING_LIST,
				      XHB_SITTING SITTING,
				      XHB_COURT_ROOM COURT_ROOM,
				      XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				      XHB_HEARING HEARING,
				      XHB_CASE CASE,
				      XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
				      XHB_DEFENDANT DEFENDANT,
				      XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				      XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				      XHB_REF_JUDGE REF_JUDGE
				WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
				AND	  SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
				AND   SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
				AND   SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
				AND   HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
				AND	  HEARING.CASE_ID = CASE.CASE_ID
				AND   SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
				AND   SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
				AND   DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
				AND   XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
				AND   HEARING_LIST.COURT_ID = court_id_in
				AND   HEARING_LIST.START_DATE = start_date_in
				AND   ((court_room_id_in IS NULL) OR (SITTING.COURT_ROOM_ID = court_room_id_in))	
				ORDER BY SCHEDULED_HEARING.SCHEDULED_HEARING_ID;		   
       END get_schedule;


	   PROCEDURE get_daily_list(results_out      OUT SYS_REFCURSOR,
                                court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
							    start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE) AS
       BEGIN
	   		OPEN results_out FOR
				SELECT DISTINCT
					  'Daily List' DOCUMENT_NAME, SYSDATE UNIQUEID, 'Criminal' LISTCATEGORY,
					  HEARING_LIST.LIST_ID, NVL(HEARING_LIST.LIST_TYPE,'Unspecified List Type') DOCUMENTTYPE, HEARING_LIST.START_DATE LIST_START_DATE, HEARING_LIST.END_DATE LIST_END_DATE, LTRIM(NVL(HEARING_LIST.STATUS,'Unspecified Status') || ' ' || NVL(HEARING_LIST.EDITION_NO,-1)) LIST_VERSION, NVL(HEARING_LIST.PRINT_REFERENCE,'Unspecified Print Reference') LIST_PRINT_REF, NVL(HEARING_LIST.PUBLISHED_TIME,'01/JAN/1900') LIST_PUBLISHED, HEARING_LIST.CREST_LIST_ID LIST_CREST_LIST_ID,
					  COURTHOUSE.COURT_ID COURTHOUSE_COURT_ID, COURTHOUSE.COURT_PREFIX COURTHOUSE_TYPE, COURTHOUSE.COURT_CODE COURTHOUSE_CODE, COURTHOUSE.COURT_NAME COURTHOUSE_NAME,
					  COURTHOUSE_ADDRESS.ADDRESS_ID COURTHOUSE_ADDRESS_ID, COURTHOUSE_ADDRESS.ADDRESS_1 COURTHOUSE_ADDRESS_1, COURTHOUSE_ADDRESS.ADDRESS_2 COURTHOUSE_ADDRESS_2, COURTHOUSE_ADDRESS.ADDRESS_3 COURTHOUSE_ADDRESS_3, COURTHOUSE_ADDRESS.ADDRESS_4 COURTHOUSE_ADDRESS_4, COURTHOUSE_ADDRESS.TOWN COURTHOUSE_TOWN, COURTHOUSE_ADDRESS.COUNTY COURTHOUSE_COUNTY, COURTHOUSE_ADDRESS.POSTCODE COURTHOUSE_POSTCODE,
					  COURTSITE.COURT_SITE_ID, COURTSITE.COURT_ID COURTSITE_COURT_ID, COURTSITE.COURT_SITE_CODE COURTSITE_CODE, COURTSITE.COURT_SITE_NAME COURTSITE_NAME,
					  COURTSITE_ADDRESS.ADDRESS_ID COURTSITE_ADDRESS_ID, COURTSITE_ADDRESS.ADDRESS_1 COURTSITE_ADDRESS_1, COURTSITE_ADDRESS.ADDRESS_2 COURTSITE_ADDRESS_2, COURTSITE_ADDRESS.ADDRESS_3 COURTSITE_ADDRESS_3, COURTSITE_ADDRESS.ADDRESS_4 COURTSITE_ADDRESS_4, COURTSITE_ADDRESS.TOWN COURTSITE_TOWN, COURTSITE_ADDRESS.COUNTY COURTSITE_COUNTY, COURTSITE_ADDRESS.POSTCODE COURTSITE_POSTCODE,
					  COURT_ROOM.COURT_ROOM_ID, COURT_ROOM.COURT_ROOM_NAME, COURT_ROOM.DESCRIPTION, COURT_ROOM.CREST_COURT_ROOM_NO, COURT_ROOM.COURT_SITE_ID, COURT_ROOM.VERSION AS COURT_ROOM_VERSION, COURT_ROOM.DISPLAY_NAME,
					  SITTING.SITTING_ID, SITTING.SITTING_SEQUENCE_NO, SITTING.IS_FLOATING, SITTING.SITTING_NOTE, SITTING.SITTING_TIME,
					  REF_JUDGE.REF_JUDGE_ID JUDGE_ID, REF_JUDGE.TITLE JUDGE_TITLE, REF_JUDGE.FIRST_NAME JUDGE_FIRST_NAME, REF_JUDGE.MIDDLE_NAME JUDGE_MIDDLE_NAME, NVL(REF_JUDGE.SURNAME,' ') JUDGE_SURNAME, REF_JUDGE.FULL_LIST_TITLE1 JUDGE_FULL_TITLE1,
					  REF_JUSTICE1.REF_JUSTICE_ID JUSTICE1_ID, DECODE(REF_JUSTICE1.REF_JUSTICE_ID, NULL, '', REF_JUSTICE1.TITLE) JUSTICE1_TITLE, DECODE(REF_JUSTICE1.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME1, REF_JUSTICE1.JUSTICE_NAME) JUSTICE1_NAME,
					  REF_JUSTICE2.REF_JUSTICE_ID JUSTICE2_ID, DECODE(REF_JUSTICE2.REF_JUSTICE_ID, NULL, '', REF_JUSTICE2.TITLE) JUSTICE2_TITLE, DECODE(REF_JUSTICE2.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME2, REF_JUSTICE2.JUSTICE_NAME) JUSTICE2_NAME,
					  REF_JUSTICE3.REF_JUSTICE_ID JUSTICE3_ID, DECODE(REF_JUSTICE3.REF_JUSTICE_ID, NULL, '', REF_JUSTICE3.TITLE) JUSTICE3_TITLE, DECODE(REF_JUSTICE3.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME3, REF_JUSTICE3.JUSTICE_NAME) JUSTICE3_NAME,
					  REF_JUSTICE4.REF_JUSTICE_ID JUSTICE4_ID, DECODE(REF_JUSTICE4.REF_JUSTICE_ID, NULL, '', REF_JUSTICE4.TITLE) JUSTICE4_TITLE, DECODE(REF_JUSTICE4.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME4, REF_JUSTICE4.JUSTICE_NAME) JUSTICE4_NAME,
					  SCHEDULED_HEARING.SCHEDULED_HEARING_ID, SCHEDULED_HEARING.SEQUENCE_NO, SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME, SCHEDULED_HEARING.LISTING_NOTE, SCHEDULED_HEARING.HEARING_PROGRESS, SCHEDULED_HEARING.SITTING_ID, SCHEDULED_HEARING.HEARING_ID, SCHEDULED_HEARING.MOVED_FROM, SCHEDULED_HEARING.VERSION AS SCHEDULED_HEARING_VERSION, SCHEDULED_HEARING.LINKED_SH_ID, SCHEDULED_HEARING.END_TIME, SCHEDULED_HEARING.START_TIME, SCHEDULED_HEARING.DATE_OF_HEARING, SCHEDULED_HEARING.IS_CASE_ACTIVE,
					  REF_HEARING_TYPE.REF_HEARING_TYPE_ID, REF_HEARING_TYPE.HEARING_TYPE_CODE, REF_HEARING_TYPE.HEARING_TYPE_DESC, REF_HEARING_TYPE.CATEGORY, REF_HEARING_TYPE.SEQ_NO, REF_HEARING_TYPE.LIST_SEQUENCE, REF_HEARING_TYPE.VERSION AS REF_HEARING_TYPE_VERSION, REF_HEARING_TYPE.COURT_ID, REF_HEARING_TYPE.OBS_IND AS REF_HEARING_TYPE_OBS_IND,
					  CASE.CASE_ID, CASE.CASE_NUMBER, CASE.CASE_TYPE, CASE.MAG_CONVICTION_DATE, CASE.CASE_SUB_TYPE, CASE.CASE_TITLE, CASE.CASE_DESCRIPTION, CASE.LINKED_CASE_ID, CASE.BAIL_MAG_CODE, CASE.REF_COURT_ID, CASE.COURT_ID, CASE.CHARGE_IMPORT_INDICATOR, CASE.SEVERED_IND, CASE.INDICT_RESP, CASE.DATE_IND_REC, CASE.PROS_AGENCY_REFERENCE, CASE.VERSION AS CASE_VERSION, CASE.CASE_CLASS, CASE.JUDGE_REASON_FOR_APPEAL, CASE.RESULTS_VERIFIED, CASE.LENGTH_TAPE, CASE.NO_PAGE_PROS_EVIDENCE, CASE.NO_PROS_WITNESS, CASE.EST_PDH_TRIAL_LENGTH, CASE.INDICTMENT_INFO_1, CASE.INDICTMENT_INFO_2, CASE.INDICTMENT_INFO_3, CASE.INDICTMENT_INFO_4, CASE.INDICTMENT_INFO_5, CASE.INDICTMENT_INFO_6, CASE.POLICE_OFFICER_ATTENDING, CASE.CPS_CASE_WORKER, CASE.EXPORT_CHARGES, CASE.IND_CHANGE_STATUS,
					  REF_COURT.REF_COURT_ID, REF_COURT.COURT_TYPE, REF_COURT.CREST_CODE, REF_COURT.COURT_SHORT_NAME, REF_COURT.COURT_FULL_NAME,
					  DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID, DEFENDANT_ON_CASE.FINAL_DRIVING_LICENCE_STATUS, DEFENDANT_ON_CASE.PTIURN, DEFENDANT_ON_CASE.IS_JUVENILE, DEFENDANT_ON_CASE.IS_MASKED, DEFENDANT_ON_CASE.MASKED_NAME, DEFENDANT_ON_CASE.CASE_ID, DEFENDANT_ON_CASE.DEFENDANT_ID, DEFENDANT_ON_CASE.VERSION AS DEFENDANT_ON_CASE_VERSION, DEFENDANT_ON_CASE.OBS_IND AS DEFENDANT_ON_CASE_OBS_IND, DEFENDANT_ON_CASE.RESULTS_VERIFIED, DEFENDANT_ON_CASE.DEFENDANT_NUMBER, DEFENDANT_ON_CASE.DATE_OF_COMMITTAL, DEFENDANT_ON_CASE.NO_OF_TICS,
					  DEFENDANT.CREST_DEFENDANT_ID, DEFENDANT.FIRST_NAME, DEFENDANT.MIDDLE_NAME, NVL(DEFENDANT.SURNAME,' ') SURNAME, DEFENDANT.INITIALS, DEFENDANT.DATE_OF_BIRTH, DEFENDANT.GENDER, DEFENDANT.LAST_CONVICTION_DATE, DEFENDANT.IS_COMPANY, DEFENDANT.VERSION AS DEFENDANT_ON_VERSION, DEFENDANT.ADDRESS_ID, DEFENDANT.COURT_ID, HEARING_LIST.START_DATE
				FROM  XHB_HEARING_LIST HEARING_LIST,
  					  XHB_SITTING SITTING,
  					  XHB_COURT_ROOM COURT_ROOM,
  					  XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
  					  XHB_HEARING HEARING,
  					  XHB_CASE CASE,
  					  XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
  					  XHB_DEFENDANT DEFENDANT,
  					  XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
  					  XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
  					  XHB_REF_JUDGE REF_JUDGE,
  					  XHB_REF_JUSTICE REF_JUSTICE1,
  					  XHB_REF_JUSTICE REF_JUSTICE2,
  					  XHB_REF_JUSTICE REF_JUSTICE3,
  					  XHB_REF_JUSTICE REF_JUSTICE4,
  					  XHB_REF_COURT REF_COURT,
  					  XHB_COURT_SITE COURTSITE,
  					  XHB_COURT COURTHOUSE,
  					  XHB_ADDRESS COURTSITE_ADDRESS,
  					  XHB_ADDRESS COURTHOUSE_ADDRESS
				WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
				AND	  SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
				AND	  SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
				AND	  SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
				AND	  HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
				AND	  HEARING.CASE_ID = CASE.CASE_ID
				AND	  SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
				AND	  SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
				AND	  DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
				AND	  XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
				AND	  SITTING.REF_JUSTICE1_ID = REF_JUSTICE1.REF_JUSTICE_ID(+)
				AND	  SITTING.REF_JUSTICE2_ID = REF_JUSTICE2.REF_JUSTICE_ID(+)
				AND	  SITTING.REF_JUSTICE3_ID = REF_JUSTICE3.REF_JUSTICE_ID(+)
				AND	  SITTING.REF_JUSTICE4_ID = REF_JUSTICE4.REF_JUSTICE_ID(+)
				AND	  CASE.REF_COURT_ID = REF_COURT.REF_COURT_ID(+)
				AND	  SITTING.COURT_SITE_ID = COURTSITE.COURT_SITE_ID(+)
				AND	  COURTSITE.ADDRESS_ID = COURTSITE_ADDRESS.ADDRESS_ID(+)
				AND	  COURTSITE.COURT_ID = COURTHOUSE.COURT_ID(+)
				AND	  COURTHOUSE.ADDRESS_ID = COURTHOUSE_ADDRESS.ADDRESS_ID(+)
				AND	  HEARING_LIST.COURT_ID = court_id_in
				AND	  HEARING_LIST.START_DATE = start_date_in
				ORDER BY COURTSITE.COURT_SITE_CODE,
  					  	 SITTING.IS_FLOATING,
  						 COURT_ROOM.CREST_COURT_ROOM_NO,
  						 SITTING.SITTING_SEQUENCE_NO,
  						 SCHEDULED_HEARING.SEQUENCE_NO;
	   END get_daily_list;
END xhb_view_schedule_pkg;
/
show errors


/*
 * Any standing data inserts or changes
 */

UPDATE XHB_COURT_LOG_EVENT_DESC SET PUBLIC_DISPLAY = '0', E_INFORM = '0', PUBLIC_NOTICE = '0' WHERE EVENT_DESC_ID = '31';


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = 28, last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
