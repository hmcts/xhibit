set pagesize 10000
set head off
set echo off

/*
 * Patch for DB release 24
 */

/*
 * Changes to XHB_ table definitions
 *
 * This may include removing foreign keys and possibly other constraints
 */

PROMPT Dropping Foreign keys on XHB_COURT_LOG_ENTRY

spool drop_foreign_keys_cle.sql

SELECT 'ALTER TABLE '||table_name||' DROP CONSTRAINT '||constraint_name||';'
FROM   user_constraints
WHERE  table_name = 'XHB_COURT_LOG_ENTRY'
AND    constraint_type = 'R';

spool off

set echo on
@drop_foreign_keys_cle.sql
set echo off

ALTER TABLE xhb_court_log_entry RENAME COLUMN defendant_id TO defendant_on_offence_id;

ALTER TABLE XHB_COURT_LOG_ENTRY
      ADD (FOREIGN KEY (EVENT_DESC_ID)
           REFERENCES XHB_COURT_LOG_EVENT_DESC);

ALTER TABLE XHB_COURT_LOG_ENTRY
      ADD (FOREIGN KEY (CASE_ID)
           REFERENCES XHB_CASE);

ALTER TABLE XHB_COURT_LOG_ENTRY
      ADD (FOREIGN KEY (DEFENDANT_ON_CASE_ID)
           REFERENCES XHB_DEFENDANT_ON_CASE);

ALTER TABLE XHB_COURT_LOG_ENTRY
      ADD (FOREIGN KEY (DEFENDANT_ON_OFFENCE_ID)
           REFERENCES XHB_DEFENDANT_ON_OFFENCE);

/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 * The standard procedure is as follows:
 *
 *     1. Drop the audit table (AUD_)
 *     2. Recreate audit table as select * from XHB_ table
 *     3. Truncate the audit table (this may need to change in the future)
 *     4. Add the INSERT_EVENT column to the end of the audit table
 */

DROP TABLE AUD_COURT_LOG_ENTRY;
CREATE TABLE AUD_COURT_LOG_ENTRY TABLESPACE AUDITD AS SELECT * FROM XHB_COURT_LOG_ENTRY;
TRUNCATE TABLE AUD_COURT_LOG_ENTRY;
ALTER TABLE AUD_COURT_LOG_ENTRY ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 *
 * Note that these are generally the BUR (update and delete) triggers as the BIR
 * (insert) triggers will only change on renaming the auditing columns within the
 * XHB_ table
 */

ALTER TRIGGER XHB_COURT_LOG_ENTRY_BIR_TR COMPILE;

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_ENTRY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_LOG_ENTRY
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_COURT_LOG_ENTRY_BUR_TR */

DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_ENTRY') = 1) THEN

    INSERT INTO AUD_COURT_LOG_ENTRY 
    VALUES (:old.ENTRY_ID, 
            :old.CASE_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.DATE_TIME, 
            :old.EVENT_DESC_ID,
            :old.LOG_ENTRY_XML,
            :old.DEFENDANT_ON_CASE_ID,
            :old.DEFENDANT_ON_OFFENCE_ID,
            l_trig_event);

  END IF;

END;
/

-------------------------------------------------------------------------------
-- THE PACKAGE HEADER
-- The xhibit_search_pkg contains all of the procedures used by the fast
-- lane readers. 
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE xhibit_search_pkg AS
	-- As we are using Oracle 9i, should be able to use SYS_REFCURSOR, but it
	-- does not work on my development machine, would it need to be enabled???
	TYPE weak_ref_cursor_type IS REF CURSOR;

	-- Having to declare here as need to run from SQL statements in PL/SQL!
	-- TBD: Would declaring this deterministic be of benefit?
	-- How common are the search strings?
	FUNCTION convert_value(value_in IN VARCHAR2) RETURN VARCHAR2;


	PROCEDURE get_contact_detail(results_out      OUT weak_ref_cursor_type,
			  					 address_id_in	  IN  XHB_CONTACT_DETAIL.address_id%TYPE,
								 contact_type_in  IN  XHB_CONTACT_DETAIL.contact_type%TYPE,
								 contact_value_in IN  XHB_CONTACT_DETAIL.contact_value%TYPE);


	PROCEDURE get_court(results_out       OUT weak_ref_cursor_type,
						circuit_in        IN  XHB_COURT.circuit%TYPE,
						court_site_id_in  IN  XHB_COURT_SITE.court_site_id%TYPE,
					 	court_name_in     IN  XHB_COURT.court_name%TYPE,
						court_prefix_in   IN  XHB_COURT.court_prefix%TYPE,
	   				 	court_type_in     IN  XHB_COURT.court_type%TYPE,
	   				 	crest_court_id_in IN  XHB_COURT.crest_court_id%TYPE,
					 	short_name_in     IN  XHB_COURT.short_name%TYPE);


	PROCEDURE get_court_room(results_out            OUT weak_ref_cursor_type,
	                         court_room_name_in     IN  XHB_COURT_ROOM.court_room_name%TYPE,
							 court_site_code_in     IN  XHB_COURT_SITE.court_site_code%TYPE,
							 court_site_id_in       IN  XHB_COURT_SITE.court_site_id%TYPE,
							 crest_court_room_no_in IN  XHB_COURT_ROOM.crest_court_room_no%TYPE,
							 short_name_in			IN  XHB_COURT.short_name%TYPE);


	PROCEDURE get_court_site(results_out         OUT weak_ref_cursor_type,
			  				 court_site_code_in  IN  XHB_COURT_SITE.court_site_code%TYPE,
							 court_id_in		 IN  XHB_COURT.court_id%TYPE,
							 court_short_name_in IN  XHB_COURT.short_name%TYPE,
							 court_site_name_in  IN  XHB_COURT_SITE.court_site_name%TYPE);


	PROCEDURE get_terminal(results_out         OUT weak_ref_cursor_type,
						   terminal_name_in	   IN  XHB_TERMINAL.terminal_name%TYPE);


	PROCEDURE get_ref_advocate(results_out         OUT weak_ref_cursor_type,
						   	   adv_type_ind_in     IN  XHB_REF_ADVOCATE.adv_type_ind%TYPE,
						   	   initials_in		   IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
						   	   first_name_in	   IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
						   	   middle_name_in	   IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
						   	   surname_in		   IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
						   	   ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE);


	PROCEDURE get_ref_app_result(results_out        OUT weak_ref_cursor_type,
			  					 court_id_in		IN  XHB_REF_APP_RESULT.court_id%TYPE,
								 app_result_code_in IN  XHB_REF_APP_RESULT.app_result_code%TYPE,
								 ho_code_in			IN  XHB_REF_APP_RESULT.ho_code%TYPE,
								 vary_sentence_in	IN  XHB_REF_APP_RESULT.vary_sentence%TYPE,
								 lesser_off_ind_in  IN  XHB_REF_APP_RESULT.lesser_off_ind%TYPE);


	PROCEDURE get_ref_court(results_out         OUT weak_ref_cursor_type,
			  				court_id_in         IN  XHB_REF_COURT.court_id%TYPE,
							circuit_in			IN	XHB_COURT.circuit%TYPE,
							court_full_name_in  IN  XHB_REF_COURT.court_full_name%TYPE,
							court_prefix_in	    IN  XHB_REF_COURT.name_prefix%TYPE,
							court_type_in	    IN  XHB_REF_COURT.court_type%TYPE,
							crest_court_id_in 	IN	XHB_COURT.crest_court_id%TYPE,
							court_short_name_in IN  XHB_REF_COURT.court_short_name%TYPE,
							is_psd_in			IN	XHB_REF_COURT.is_psd%TYPE);


	PROCEDURE get_ref_court_reporter(results_out    OUT weak_ref_cursor_type,
			  						 court_id_in    IN  XHB_REF_COURT_REPORTER.court_id%TYPE,
									 firm_name_in	IN	XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
									 initials_in	IN	XHB_REF_COURT_REPORTER.initials%TYPE,
									 first_name_in	IN	XHB_REF_COURT_REPORTER.first_name%TYPE,
									 middle_name_in	IN	XHB_REF_COURT_REPORTER.middle_name%TYPE,
									 surname_in		IN	XHB_REF_COURT_REPORTER.surname%TYPE);


	PROCEDURE get_ref_court_reporter_firm(results_out  OUT weak_ref_cursor_type,
			  							  firm_name_in IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE);
	

	PROCEDURE get_ref_disposal(results_out 		 OUT weak_ref_cursor_type,
							   disposal_code_in  IN  XHB_REF_DISPOSAL.disposal_code%TYPE,
							   disposal_title_in IN  XHB_REF_DISPOSAL.disposal_title%TYPE,
							   court_id_in		 IN  XHB_REF_DISPOSAL.court_id%TYPE);


	PROCEDURE get_ref_disposal_menu(results_out           OUT weak_ref_cursor_type,
			  						abbrev_in             IN  XHB_REF_DISPOSAL_MENU.abbrev%TYPE,
									court_id_in           IN  XHB_REF_DISPOSAL_MENU.court_id%TYPE,
									crest_menu_item_id_in IN  XHB_REF_DISPOSAL_MENU.crest_menu_item_id%TYPE,
									disposal_code_in	  IN  XHB_REF_DISPOSAL_MENU.disposal_code%TYPE,
									menu_group_in		  IN  XHB_REF_DISPOSAL_MENU.menu_group%TYPE,
									parent_in			  IN  XHB_REF_DISPOSAL_MENU.parent%TYPE,
									title_in			  IN  XHB_REF_DISPOSAL_MENU.title%TYPE);
	

	PROCEDURE get_ref_hearing_type(results_out          OUT weak_ref_cursor_type,
								   hearing_type_code_in	IN XHB_REF_HEARING_TYPE.hearing_type_code%TYPE);


	PROCEDURE get_ref_judge(results_out    OUT weak_ref_cursor_type,
							first_name_in  IN  XHB_REF_JUDGE.first_name%TYPE,
							middle_name_in IN  XHB_REF_JUDGE.middle_name%TYPE,
							surname_in     IN  XHB_REF_JUDGE.surname%TYPE);


    PROCEDURE get_ref_justice(results_out     OUT weak_ref_cursor_type,
							  justice_name_in IN  XHB_REF_JUSTICE.justice_name%TYPE);


	PROCEDURE get_ref_legal_representative(results_out 		 OUT weak_ref_cursor_type,
										   court_id_in		 IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
										   first_name_in	 IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
										   surname_in		 IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
										   legal_rep_type_in IN  XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE);


	PROCEDURE get_ref_offence(results_out     OUT weak_ref_cursor_type,
							  act_section_in  IN  XHB_REF_OFFENCE.act_section%TYPE,
							  court_id_in     IN  XHB_REF_OFFENCE.court_id%TYPE,
							  offence_desc_in IN  XHB_REF_OFFENCE.offence_desc%TYPE,
							  statute_in      IN  XHB_REF_OFFENCE.statute%TYPE,
							  offence_code_in IN  XHB_REF_OFFENCE.offence_code%TYPE);


	PROCEDURE get_ref_solicitor_firm(results_out 			OUT weak_ref_cursor_type,
									 solicitor_firm_name_in IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
									 crest_sof_id_in		IN  XHB_REF_SOLICITOR_FIRM.crest_sof_id%TYPE,
									 court_id_in			IN  XHB_REF_SOLICITOR_FIRM.court_id%TYPE);


	PROCEDURE get_ref_system_code(results_out   OUT weak_ref_cursor_type,
								  court_id_in   IN  XHB_REF_SYSTEM_CODE.court_id%TYPE,
								  code_type_in  IN  XHB_REF_SYSTEM_CODE.code_type%TYPE,
								  de_code_in    IN  XHB_REF_SYSTEM_CODE.de_code%TYPE,
								  code_in       IN  XHB_REF_SYSTEM_CODE.code%TYPE,
								  code_title_in IN  XHB_REF_SYSTEM_CODE.code_title%TYPE);


	PROCEDURE get_solicitor(results_out OUT weak_ref_cursor_type,
			  				ref_legal_rep_id_in IN XHB_REF_SOLICITOR.ref_legal_rep_id%TYPE,
							initials_in IN XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
							first_name_in IN XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
							middle_name_in IN XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
							surname_in IN XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
							crest_solicitor_name_in IN XHB_REF_SOLICITOR.crest_solicitor_name%TYPE,
							solicitor_firm_name_in IN XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE);


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
			  SELECT cr.court_room_id AS "id",
			  		 cr.*,
					 -- A dummy value, if required will need to be found
					 'LOCATION' AS location
			  FROM   XHB_COURT_ROOM cr, XHB_COURT_SITE cs, XHB_COURT c
			  WHERE  cr.court_site_id          =    cs.court_site_id
			  AND	 c.court_id                =    cs.court_id
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
						   	   ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE) AS
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
			  FROM   XHB_REF_ADVOCATE ra, XHB_REF_LEGAL_REPRESENTATIVE rlr
			  WHERE	 ra.ref_legal_rep_id = rlr.ref_legal_rep_id
			  AND	 ((ra.obs_ind IS NULL) OR (ra.obs_ind = 'N'))
			  AND	 ((adv_type_ind_in IS NULL) 
			  		 OR (UPPER(ra.adv_type_ind) LIKE convert_value(adv_type_ind_in)))
			  AND	 ((ref_legal_rep_id_in IS NULL)
			  		 OR (ra.ref_legal_rep_id    =    ref_legal_rep_id_in))		  
			  AND	 ((initials_in IS NULL)
			  		 OR (UPPER(rlr.initials)    LIKE convert_value(initials_in)))
			  AND	 ((first_name_in IS NULL)
			  		 OR (UPPER(rlr.first_name)  LIKE convert_value(first_name_in)))
			  AND	 ((middle_name_in IS NULL)
			  		 OR (UPPER(rlr.middle_name) LIKE convert_value(middle_name_in)))
			  AND	 ((surname_in IS NULL)
			  		 OR (UPPER(rlr.surname)     LIKE convert_value(surname_in)))
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

CREATE TABLE XHB_VERSION (
       SCHEMA_NAME       VARCHAR(10),
       SCHEMA_VERSION    NUMBER,
       LAST_UPDATE_DATE  DATE,
       UPDATED_BY        VARCHAR2(30))
         TABLESPACE XHIBITD
         STORAGE (INITIAL 64K
                  NEXT 64K
                  PCTINCREASE 0);

INSERT INTO XHB_VERSION VALUES ('XHIBIT',
                                '24',
                                SYSDATE,
                                SYS_CONTEXT('USERENV', 'SESSION_USER'));

CREATE OR REPLACE PACKAGE xhb_list_distribution_pkg AS

  PROCEDURE  get_wll_unsub_rec_by_court_id (p_unsub_recip_cur IN OUT SYS_REFCURSOR,
                                            p_court_id        IN     NUMBER);

  PROCEDURE  get_dist_stat_by_court_id (p_dist_stat_cur IN OUT SYS_REFCURSOR,
                                        p_court_id      IN     NUMBER);

  PROCEDURE  get_wll_dist_stat_by_court_id (p_wll_dist_stat_cur IN OUT SYS_REFCURSOR,
                                            p_court_id          IN     NUMBER);

END xhb_list_distribution_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_list_distribution_pkg AS

  PROCEDURE get_wll_unsub_rec_by_court_id (p_unsub_recip_cur IN OUT SYS_REFCURSOR,
                                           p_court_id        IN     NUMBER) IS

    BEGIN

      OPEN p_unsub_recip_cur FOR

      /*
       * For the 'solicitor_firm_address' values, a comma is only required after a
       * non-NULL value and also not at the end of the concatenation.  Due to the fact
       * that the last non-NULL value may not be the last field selected (a.country),
       * the last comma in that instace needs to be removed.  The RTRIM removes any
       * trailing unwanted commas from the full concatenation. ie. Postcode, becomes
       * Postcode.
       */

        SELECT rsf.crest_sof_id,
               rsf.solicitor_firm_name,
               RTRIM(NVL2(a.address_1,a.address_1||',',NULL)
                     ||NVL2(a.address_2,a.address_2||',',NULL)
                     ||NVL2(a.address_3,a.address_3||',',NULL)
                     ||NVL2(a.address_4,a.address_4||',',NULL)
                     ||NVL2(a.town,a.town||',',NULL)
                     ||NVL2(a.county,a.county||',',NULL)
                     ||NVL2(a.postcode,a.postcode||',',NULL)
                     ||NVL(a.country,NULL),',') solictior_firm_address,
               NULL solicitor_firm_fax,
               NULL solicitor_firm_email,
               rsf.court_id, 
               NULL wll_recipient_id
        FROM   xhb_ref_solicitor_firm rsf,
               xhb_address a
        WHERE  rsf.court_id = p_court_id
        AND    rsf.crest_sof_id NOT IN (SELECT NVL(crest_solicitor_firm_id, -1)
                                         FROM   xhb_wll_recipient)
        AND    rsf.address_id = a.address_id(+)
        UNION    
        SELECT wr.crest_solicitor_firm_id crest_sof_id,
               wr.solicitor_firm_name,
               wr.solictior_firm_address,
               wr.solicitor_firm_fax,
               wr.solicitor_firm_email,
               wr.court_id, 
               wr.wll_recipient_id
        FROM   xhb_wll_recipient wr
        WHERE  wr.court_id = p_court_id
        AND    NOT EXISTS (SELECT 1
                           FROM   xhb_document_distribution 
                           WHERE  wr.wll_recipient_id = xhb_document_distribution.wll_recipient_id)
        ORDER BY solicitor_firm_name;

  END get_wll_unsub_rec_by_court_id;

  PROCEDURE  get_dist_stat_by_court_id (p_dist_stat_cur IN OUT SYS_REFCURSOR,
                                        p_court_id      IN     NUMBER) IS

    BEGIN

      OPEN p_dist_stat_cur FOR

      SELECT dc.DOC_CONTROL_ID,
             dc.STATUS,
             dc.EXPIRY_DATE,
             dc.DISTRIBUTION_TYPE,
             dc.MIME_TYPE,
             dc.DOCUMENT_TYPE,
             dc.LAST_UPDATE_DATE,
             dc.CREATION_DATE,
             dc.CREATED_BY,
             dc.LAST_UPDATED_BY,
             dc.VERSION,
             dc.FORMATTING_ID,
             dc.COURT_ID,
             dc.DISTRIBUTED_DATE,
             dc.XML_DOCUMENT_ID
      FROM   XHB_DOCUMENT_CONTROL dc
      WHERE  dc.STATUS != 'XX'
             AND dc.STATUS != 'XA'
             AND (dc.DOCUMENT_TYPE != 'IWP' OR  (dc.DOCUMENT_TYPE = 'IWP'
                                            AND (dc.STATUS != 'SE' AND dc.STATUS != 'SF')))
             AND dc.COURT_ID = p_court_id;


  END get_dist_stat_by_court_id;

  PROCEDURE  get_wll_dist_stat_by_court_id (p_wll_dist_stat_cur IN OUT SYS_REFCURSOR,
                                            p_court_id          IN     NUMBER) IS

    BEGIN

      OPEN p_wll_dist_stat_cur FOR

      SELECT DISTINCT xd1.XML_DOCUMENT_ID,
             xd1.DATE_CREATED,
             xd1.DOCUMENT_TITLE,
             xd2.STATUS,
             xd1.EXPIRY_DATE,
             xd1.DOCUMENT_TYPE,
             xd1.LAST_UPDATE_DATE,
             xd1.CREATION_DATE,
             xd1.CREATED_BY,
             xd1.LAST_UPDATED_BY,
             xd1.VERSION,
             xd1.COURT_ID
      FROM   XHB_XML_DOCUMENT xd1,
             XHB_WLL_CONTROL wc,
             XHB_WLL_DOCUMENT wd,
             XHB_XML_DOCUMENT xd2
      WHERE  xd1.XML_DOCUMENT_ID = wc.XML_DOCUMENT_ID
      AND    wc.WLL_CONTROL_ID = wd.WLL_CONTROL_ID
      AND    wd.XML_DOCUMENT_ID = xd2.XML_DOCUMENT_ID
      AND    xd1.DOCUMENT_TYPE = 'WL'
      AND    (xd2.STATUS = 'ND' OR xd2.STATUS = 'PR' OR xd2.STATUS = 'DP')
      AND    xd1.COURT_ID = p_court_id;

  END get_wll_dist_stat_by_court_id;

END xhb_list_distribution_pkg;
/
show errors

set head on
set echo on

UPDATE xhb_court SET court_code = '01AA' where court_id = 1;
UPDATE xhb_court SET court_code = '01AB' where court_id = 2;
UPDATE xhb_court SET court_code = '01AC' where court_id = 3;

-- See the body script for possible future enhancements
CREATE OR REPLACE PACKAGE counselfacilities AS
    TYPE counsel_type IS REF CURSOR;

    PROCEDURE get_counsel_sign_in(p_counsel_cursor_out OUT counsel_type,
                                  p_court_id_in        IN  NUMBER,
                                  p_start_date_in      IN  DATE,
                                  p_court_room_id_in   IN  NUMBER);


    PROCEDURE search_counsel(p_counsel_cursor_out OUT counsel_type,
                             p_court_id_in        IN  NUMBER,
                             p_start_date_in      IN  DATE,
                             p_first_name_in      IN  VARCHAR2,
                             p_surname_in         IN  VARCHAR2);


    PROCEDURE search_defendants(p_counsel_cursor_out OUT counsel_type,
                                p_court_id_in        IN  NUMBER,
                                p_start_date_in      IN  DATE,
                                p_first_name_in      IN  VARCHAR2,
                                p_surname_in         IN  VARCHAR2);
END counselfacilities;
/

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
                   OR (court_room_id = p_court_room_id_in));
    END get_counsel_sign_in;


    PROCEDURE search_counsel(p_counsel_cursor_out OUT counsel_type,
                             p_court_id_in        IN  NUMBER,
                             p_start_date_in      IN  DATE,
                             p_first_name_in      IN  VARCHAR2,
                             p_surname_in         IN  VARCHAR2) IS
    BEGIN
    	OPEN p_counsel_cursor_out FOR
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
                   OR (UPPER(NVL(leg_rep_surname, '%'))    LIKE UPPER(p_surname_in || '%')));
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
END counselfacilities;
/

