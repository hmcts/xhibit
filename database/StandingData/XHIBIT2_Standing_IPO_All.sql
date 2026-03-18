-- Modifications by ECY 13-Oct-2004:
--  Court House Address updated as per Kevin Nicholson's directions
--  'Original' Dockford CREST_COURT_ID changed from 499 to 496
--  XHB_COURT.CREST_IP_ADDRESS changes to Mercator spoke IP address
--  Public Notice allocation to Court Rooms added

DECLARE
    l_crest_court_id        NUMBER       := 499;
    l_court_full_name       VARCHAR2(50) := 'IPO Crown Court';
    l_court_short_name      VARCHAR2(5)  := 'IPO';
    l_site_id               VARCHAR2(5)  := 'A';
    l_probation_office_name VARCHAR2(20) := 'IPO Probation Office';
    l_no_of_court_rooms     NUMBER       := 8;

    --l_fax_number        VARCHAR2(20)     := '020 8123 7954';
    --l_tel_number        VARCHAR2(20)     := '020 8123 4567';

    l_address_id     NUMBER;
    l_court_site_id  NUMBER;
    l_court_id       NUMBER;
    
    
        PROCEDURE ADD_PUBLIC_DISPLAY(p_court_id_in IN NUMBER) IS
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
            var_court_id := p_court_id_in;
    
            --_________________________
            --Set up the Rotation Sets.
            --
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
    
            FOR r_courtsite_id IN 
                (SELECT court_site_id
                FROM XHB_COURT_SITE
                WHERE court_id = var_court_id
                ORDER BY court_site_id) 
            LOOP
                var_courtsite_id := r_courtsite_id.court_site_id;
                --_____________________________
                --Set up the Display Locations.
                --
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
                VALUES (var_display_location, 'jury_lounge', var_courtsite_id);
    
                SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
                INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
                DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
                var_display, 1, var_display_location, var_jury_room_rs, 'jury_lounge_18in_1', 'enGB', 'Y'); 
    
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
        END ADD_PUBLIC_DISPLAY;
BEGIN
    -- Court/site/room
    SELECT XHB_ADDRESS_SEQ.NEXTVAL,
           XHB_COURT_SITE_SEQ.NEXTVAL,
           XHB_COURT_SEQ.NEXTVAL
    INTO   l_address_id,
           l_court_site_id,
           l_court_id
    FROM   dual;

    -- Court address...
    INSERT INTO XHB_ADDRESS (ADDRESS_ID, ADDRESS_1) VALUES (l_address_id, l_court_full_name);

    update xhb_address
    set address_1 = 'Address Line 1',
    address_2 = 'Address Line 2',
    address_3 = 'Address Line 3',
    address_4 = 'Address Line 4',
    town = 'TestTown',
    county = 'TestCounty',
    postcode = 'UB11 1BQ'
    where address_id = l_address_id;

    -- Fax contact detail...
    --INSERT INTO XHB_CONTACT_DETAIL (CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
    --VALUES ('FAX', l_fax_number, l_address_id);        

    -- Telephone contact detail...
    --INSERT INTO XHB_CONTACT_DETAIL (CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID)
    --VALUES ('TEL', l_tel_number, l_address_id);

    -- Update current 499 to 496...
    update xhb_court
    set CREST_COURT_ID = 496
    where CREST_COURT_ID = 499;

    -- Court...
    INSERT INTO XHB_COURT (COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS,
                           IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE)
    VALUES (l_court_id, 'CROWN', 'SOUTH EASTERN', l_court_full_name, l_crest_court_id, 'CROWN COURT', l_court_short_name, l_address_id,
            '10.18.12.6:90', 'Y','N', l_probation_office_name, l_court_full_name, l_court_full_name, '01AA'); 

    -- Court Site...
    INSERT INTO XHB_COURT_SITE (COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID)
    VALUES (l_court_site_id , l_court_full_name, l_site_id, l_court_id, l_address_id, l_court_full_name, l_crest_court_id); 

    -- Create all of the court rooms...
    FOR i IN 1 .. l_no_of_court_rooms LOOP
        INSERT INTO XHB_COURT_ROOM
        (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME)
        VALUES
        (XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court ' || i, 'Court Room ' || i, i, l_court_site_id, 'N', 'Court Room ' || i);
    END LOOP;

    -- Create CR_LIVE_STATUS entries...
    INSERT INTO xhb_cr_live_status (court_room_id, time_status_set, internet_status)
    SELECT cr.court_room_id,
           SYSDATE,
           'No Information to display'
    FROM   xhb_court_room cr, xhb_court_site cs
    WHERE  cs.court_site_id = cr.court_site_id
    AND    cs.court_id = l_court_id;

    -- CREST_IMPORT
    INSERT INTO XHB_CREST_IMPORT (STATUS, IMPORT_TYPE, COURT_ID)
    SELECT 'N', IMPORT_TYPE, l_court_id
    FROM   XHB_CREST_IMPORT_TYPE;

    -- DocumentReply
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',l_court_id,'DL');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',l_court_id,'DLP');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',l_court_id,'WL');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',l_court_id,'WLL');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',l_court_id,'FL');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',l_court_id,'RL');

    -- PublicNotice
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'Reporting restrictions. For details please see Court Manager.', 100);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'In chambers, no entry.' , 200);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'Members of the public are requested not to enter.' , 300);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'TV link in progress - please enter quietly.' , 400);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'Video being played - please enter quietly.' , 500);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'If you wish to enter, please do so quietly.' , 600);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'Reporting restrictions lifted.' , 700);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'Please switch off mobile phones.' , 800);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'Food and drink must not be consumed in the courtrooms.', 900);
    insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( l_court_id,   'Bench warrant in progress.' , 1000);

    insert into xhb_configured_public_notice  ( IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID)
    select 0, court_room_id, public_notice_id 
    from   xhb_public_notice pn, xhb_court_room cr
    where  pn.court_id = l_court_id
    and    cr.court_room_id in (select court_room_id 
				from   XHB_COURT_ROOM cr2, XHB_COURT_SITE cs
				where  cr2.court_site_id = cs.court_site_id
				and    cs.COURT_ID = l_court_id );    

    ADD_PUBLIC_DISPLAY(l_court_id);
END;
/
