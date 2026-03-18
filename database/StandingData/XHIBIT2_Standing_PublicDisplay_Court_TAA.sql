/*
 * FILE_NAME:          XHIBIT2_Standing_PublicDisplay_Court.sql
 *
 * ENVIRONMENT:        Training Only
 *
 * DESCRIPTION:        Sets up the public displays for each court/site/room 
 *                     on the training database
 *
 * STATUS DESCRIPTION: Script is suitable for running initially on set-up of the database.
 *                     Re-running of the script is possible as all data will be dropped at
 *                     the start
 *
 * DEPENDENCIES:       Data must exist in XHB_COURT, XHB_COURT_SITE, XHB_COURT_ROOM
 *
 * OWNER:              Rakesh Lakhani
 */ 

DELETE FROM XHB_DISPLAY_COURT_ROOM;

DELETE FROM XHB_DISPLAY;

DELETE FROM XHB_DISPLAY_LOCATION;

DELETE FROM XHB_ROTATION_SET_DD;

DELETE FROM XHB_ROTATION_SETS;

COMMIT;

DECLARE 

var_public_view_rs NUMBER;
var_court_room_rs NUMBER;
var_jury_room_rs NUMBER;
var_summary_by_name_rs NUMBER;
var_status_rs NUMBER;
var_daily_list_rs NUMBER;
var_all_lists_rs NUMBER;

var_court_id NUMBER;
var_courtsite_id NUMBER;
var_courtroom_id NUMBER;
var_courtroom_num NUMBER;

var_display_location NUMBER;
var_display NUMBER;

BEGIN

-- LOOP COURTS

FOR r_court_id IN 
	(SELECT court_id
	FROM XHB_COURT
	ORDER BY court_id) 
LOOP

var_court_id := r_court_id.court_id;
--_________________________
--Set up the Rotation Sets.
--¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_public_view_rs FROM dual;
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
VALUES (var_public_view_rs, var_court_id, 'Public View', 'Y');

SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_court_room_rs FROM dual;
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
VALUES (var_court_room_rs, var_court_id, 'Court Room', 'Y');

SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_jury_room_rs FROM dual;
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
VALUES (var_jury_room_rs, var_court_id, 'Jury Room', 'Y');

SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_summary_by_name_rs FROM dual;
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
VALUES (var_summary_by_name_rs, var_court_id, 'Summary By Name', 'Y');

SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_status_rs FROM dual;
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
VALUES (var_status_rs, var_court_id, 'Status', 'Y');

SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_daily_list_rs FROM dual;
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
VALUES (var_daily_list_rs, var_court_id, 'Daily List', 'Y');

SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_all_lists_rs FROM dual;
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
VALUES (var_all_lists_rs, var_court_id, 'All Lists', 'Y');

INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_public_view_rs, 3, 20, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_public_view_rs, 5, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_court_room_rs, 1, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_court_room_rs, 2, 20, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_jury_room_rs, 6, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_summary_by_name_rs, 5, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_status_rs, 4, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_daily_list_rs, 3, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 1, 10, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 2, 10, 3); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 3, 10, 4); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 4, 10, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 5, 10, 6); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 6, 10, 5);
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 7, 10, 5);

COMMIT;

FOR r_courtsite_id IN 
	(SELECT court_site_id
	FROM XHB_COURT_SITE
	WHERE court_id = var_court_id
	ORDER BY court_site_id) 
LOOP

var_courtsite_id := r_courtsite_id.court_site_id;
--_____________________________
--Set up the Display Locations.
--¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'e_v', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 2, var_display_location, var_summary_by_name_rs, 'e_v_plasma_display', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;


SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'reception', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 2, var_display_location, var_daily_list_rs, 'reception_42_display', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;


SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'public_restaurant', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_public_view_rs, 'public_rest_18in_1', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_public_view_rs, 'public_rest_18in_2', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;


SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'jury_lounge', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_jury_room_rs, 'jury_lounge_18in_1', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_jury_room_rs, 'jury_lounge_18in_2', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;


SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'witness_service', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_public_view_rs, 'witness_18in_display', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;


SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'police_1st_floor', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_public_view_rs, 'police_18in_display', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;


SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'ps_s_o_1st_floor', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_public_view_rs, 'ps_sec_off_display', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;


SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'n_w_c_w_a', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 2, var_display_location, var_status_rs, 'nwcwa_plasma_display', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;


SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'b_a_s_r', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_public_view_rs, 'b_a_s_r_18in_display', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 2, var_display_location, var_public_view_rs, 'b_a_s_r_42in_display', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, court_room_id
FROM XHB_COURT_ROOM
WHERE court_site_id = var_courtsite_id;

-- Loop Court Rooms for Court Detail Screens

FOR r_courtroom_id IN 
	(SELECT court_room_id, crest_court_room_no
	FROM XHB_COURT_ROOM
	WHERE court_site_id = var_courtsite_id
	ORDER BY crest_court_room_no) 
LOOP

var_courtroom_id := r_courtroom_id.court_room_id;
var_courtroom_num := r_courtroom_id.crest_court_room_no;

SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'court_room_'||var_courtroom_num, var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 1, var_display_location, var_court_room_rs, 'courtroom_'||var_courtroom_num||'_display', 'enGB', 'N'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM VALUES (var_display, var_courtroom_id);

END LOOP; -- Court Room

END LOOP; -- Court Site

-- Set up the display for View information pages
SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
VALUES (var_display_location, 'v_i_p', var_courtsite_id);

SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
var_display, 3, var_display_location, var_all_lists_rs, 'v_i_p', 'enGB', 'Y'); 

INSERT INTO XHB_DISPLAY_COURT_ROOM 
SELECT var_display, cr.court_room_id
FROM XHB_COURT_ROOM cr, XHB_COURT_SITE cs
WHERE cr.court_site_id = cs.court_site_id
AND   cs.court_id = var_court_id;


END LOOP; -- Court

COMMIT;

END;


-- Reset sequences
DECLARE
	CURSOR c1 IS SELECT utc.table_name, utc.column_name AS PRIMARY_KEY_COLUMN_NAME,
		   	  	 		us.sequence_name, us.increment_by, us.last_number 
		   	  	 FROM   user_tab_columns utc, user_sequences us
				 WHERE  utc.table_name = SUBSTR(us.sequence_name, 0, LENGTH(us.sequence_name) - 4)
				 AND	   utc.column_id = 1
				 AND	   (utc.table_name LIKE 'XHB_REF%'
				 OR		    utc.table_name IN ('XHB_ROTATION_SETS', 'XHB_ROTATION_SET_DD', 'XHB_DISPLAY_LOCATION',
				 						   	   'XHB_DISPLAY'));
    l_max_id NUMBER;
BEGIN
	 FOR rec IN c1 LOOP
	 	 EXECUTE IMMEDIATE 'SELECT MAX(' || rec.primary_key_column_name || ') FROM ' || rec.table_name
		 INTO l_max_id;
		 
		 IF (l_max_id > rec.last_number) THEN
		 	l_max_id := l_max_id - rec.last_number + 1;
			EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || rec.sequence_name || ' INCREMENT BY ' || l_max_id;
			EXECUTE IMMEDIATE 'SELECT ' || rec.sequence_name || '.NEXTVAL FROM dual' INTO l_max_id;
			EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || rec.sequence_name || ' INCREMENT BY ' || rec.increment_by;
		 END IF;
	 END LOOP;
END;