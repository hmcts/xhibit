CREATE OR REPLACE PACKAGE BODY xhb_terminal_pkg AS

    FUNCTION is_identical(p_var1_in IN VARCHAR2, p_var2_in IN VARCHAR2) RETURN boolean IS
    BEGIN
         RETURN ( (p_var1_in IS NULL AND p_var2_in IS NULL) OR (p_var1_in = p_var2_in) );
    END is_identical;
    
    FUNCTION is_identical(p_var1_in IN NUMBER, p_var2_in IN NUMBER) RETURN boolean IS
    BEGIN
         RETURN ( (p_var1_in IS NULL AND p_var2_in IS NULL) OR (p_var1_in = p_var2_in) );
    END is_identical;

	PROCEDURE update_terminal(l_terminal_name     IN XHB_TERMINAL.TERMINAL_NAME%TYPE,
			  				  l_location_string   IN XHB_TERMINAL.LOCATION%TYPE,
  			  				  l_court_id          IN XHB_TERMINAL.COURT_ID%TYPE,
			  				  l_court_site_id     IN XHB_TERMINAL.COURT_SITE_ID%TYPE,
			  				  l_court_room_id     IN XHB_TERMINAL.COURT_ROOM_ID%TYPE,
			  				  l_courtroom_or_site IN XHB_TERMINAL.COURTROOM_OR_SITE%TYPE,
			  				  l_roaming           IN XHB_TERMINAL.ROAMING%TYPE) 
	IS
		l_old_terminal_entry   xhb_terminal%ROWTYPE;
	BEGIN
    	 SELECT *
		 INTO   l_old_terminal_entry 
    	 FROM   XHB_TERMINAL
    	 WHERE  terminal_name = l_terminal_name;

    	 IF (NOT (    is_identical(l_old_terminal_entry.location,          l_location_string)
                  AND is_identical(l_old_terminal_entry.court_room_id,     l_court_room_id)
            	  AND is_identical(l_old_terminal_entry.court_site_id,     l_court_site_id)
            	  AND is_identical(l_old_terminal_entry.courtroom_or_site, l_courtroom_or_site) 
            	  AND is_identical(l_old_terminal_entry.court_id,          l_court_id) 
            	  AND is_identical(l_old_terminal_entry.roaming,           l_roaming) 
            	 )
            )
        THEN 
        	 -- Update terminal with new location details
        	 UPDATE  xhb_terminal
        	 SET     location = l_location_string,
                     court_room_id = l_court_room_id,
                	 courtroom_or_site = l_courtroom_or_site,
                	 court_site_id = l_court_site_id,
                	 court_id = l_court_id,
                	 roaming = l_roaming
             WHERE 	 terminal_name = l_terminal_name;
        END IF;

		EXCEPTION
    		WHEN NO_DATA_FOUND THEN
        	-- doesn't exist, so insert it...
        	   INSERT INTO xhb_terminal
               		  (location,
                	  terminal_ip,
                	  terminal_name,
                	  court_room_id, 
                	  courtroom_or_site,
                	  court_site_id,
                	  court_id,
                	  roaming)
               VALUES (l_location_string,
               		  'N/A',
               		  l_terminal_name,
               		  l_court_room_id,
               		  l_courtroom_or_site,
               		  l_court_site_id,
               		  l_court_id,
               		  l_roaming);
	END;
	

    FUNCTION get_terminals(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE)
    RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT xt.TERMINAL_NAME,
                   xt.TERMINAL_ID,
                   NULL AS COURT_ROOM_NAME,
                   xcs.COURT_SITE_NAME
            FROM   XHB_TERMINAL xt, XHB_COURT_SITE xcs
            WHERE  xt.COURT_SITE_ID = xcs.COURT_SITE_ID
            AND    xcs.COURT_ID = p_court_id_in
            AND    xt.COURT_ROOM_ID is NULL
            UNION
            SELECT xt.TERMINAL_NAME,
                   xt.TERMINAL_ID,
                   xcr.COURT_ROOM_NAME AS COURT_ROOM_NAME,
                   xcs.COURT_SITE_NAME
            FROM   XHB_TERMINAL xt, XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs
            WHERE  xt.COURT_ROOM_ID = xcr.COURT_ROOM_ID
            AND    xcr.COURT_SITE_ID = xcs.COURT_SITE_ID
            AND    xcs.COURT_ID = p_court_id_in
            ORDER BY TERMINAL_NAME;

        RETURN v_return_cursor;
    END get_terminals;


    FUNCTION get_courts RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT COURT_NAME,
                   COURT_ID,
                   CREST_COURT_ID
            FROM   XHB_COURT
            ORDER BY COURT_NAME;

        RETURN v_return_cursor;
    END get_courts;
    

    FUNCTION get_court_sites RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
        	 SELECT XHB_COURT.COURT_NAME,
                    XHB_COURT.COURT_ID,
                    XHB_COURT.CREST_COURT_ID,
                    XHB_COURT_SITE.COURT_SITE_NAME,
                    XHB_COURT_SITE.COURT_SITE_ID    
             FROM   XHB_COURT,
                    XHB_COURT_SITE
             WHERE  XHB_COURT.COURT_ID = XHB_COURT_SITE.COURT_ID 
             ORDER BY XHB_COURT.COURT_NAME,
                      XHB_COURT_SITE.COURT_SITE_NAME;

        RETURN v_return_cursor;
    END get_court_sites;
    

    FUNCTION get_terminal_by_primary_key(p_terminal_id_in IN XHB_TERMINAL.terminal_id%TYPE)
    RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT TERMINAL_NAME,
                   TERMINAL_ID,
                   NULL AS COURT_ROOM_NAME,
                   NULL AS COURT_SITE_NAME
            FROM   XHB_TERMINAL
            WHERE  TERMINAL_ID = p_terminal_id_in;

        RETURN v_return_cursor;
    END get_terminal_by_primary_key;



	-- This procedure will create an entry in the XHB_TERMINAL table
	-- If any part of the court short name, the court site code, or the court room number
	-- are incorrect, then a custom error -80000 will be thrown.
	-- There should only be one insert/update at the end as the Java code will
	-- continue to process updates and WILL commit at the end.
	PROCEDURE maintain_terminal(p_terminal_name	IN xhb_terminal.terminal_name%TYPE,
			  					p_location		IN xhb_terminal.location%TYPE) 
	IS
        l_terminal_name     xhb_terminal.terminal_name%TYPE     := LOWER(p_terminal_name);
        l_location          xhb_terminal.location%TYPE          := UPPER(p_location);
		l_court_short_name	xhb_court.short_name%TYPE;
		l_court_site_code	xhb_court_site.court_site_code%TYPE;
		l_room_or_site		xhb_terminal.location%TYPE;
		l_courtroom_or_site	xhb_terminal.courtroom_or_site%TYPE;
		l_court_id			xhb_court.court_id%TYPE;
		l_court_site_id		xhb_court_site.court_site_id%TYPE;
		l_court_room_id		xhb_court_room.court_room_id%TYPE;
		l_location_string	xhb_terminal.location%TYPE          := '/';  -- always start with a /
        l_roaming           xhb_terminal.roaming%type := 'N'; -- default to not roaming

        l_2nd_slash NUMBER := INSTR(l_location, '/', 1, 2);
        l_3rd_slash NUMBER := INSTR(l_location, '/', 1, 3);
	BEGIN
        -- Retrieve Court element
		IF (l_2nd_slash = 0) 
		THEN
			l_court_short_name := SUBSTR(l_location, 2);
		ELSE
			l_court_short_name := SUBSTR(l_location, 2, l_2nd_slash - 2);
		END IF;

		IF (l_court_short_name = 'ROAM') THEN
			l_roaming := 'Y';
			l_court_id := NULL;
			l_court_site_id := NULL;
			l_court_room_id := NULL;
			l_courtroom_or_site := 'A';
			l_location_string := 'roam';
		ELSE
			-- Get Court House ID and start building location string
			SELECT	court_id,
					l_location_string || REPLACE(LOWER(display_name), ' ', '_')
			INTO	l_court_id,
					l_location_string
			FROM	xhb_court
			WHERE	short_name = l_court_short_name;

			-- Retrieve Court Site Element
			-- Check if there is no third slash, so that layout is /ISLEW/I
			IF (l_3rd_slash = 0) THEN
				l_court_site_code := SUBSTR(l_location, l_2nd_slash + 1);
			ELSE
				l_court_site_code := SUBSTR(l_location, 
				                            l_2nd_slash + 1, 
                                            l_3rd_slash - l_2nd_slash - 1);
			END IF;

			IF (l_court_site_code = 'ROAM') THEN
				l_roaming := 'Y';
				l_court_site_id := NULL;
				l_court_room_id := NULL;
				l_courtroom_or_site := 'C';
				l_location_string := l_location_string || '/roam';
			ELSE
				-- Get Court Site ID and and continue building location string
				SELECT	court_site_id,
						l_location_string || '/' || REPLACE(LOWER(display_name), ' ', '_')
				INTO	l_court_site_id,
						l_location_string
				FROM	xhb_court_site
				WHERE	court_id = l_court_id
				AND		court_site_code = l_court_site_code;

				-- Retrieve court room or location element
				-- Check if there is a thrid slash before getting court room
				IF (l_3rd_slash = 0) 
				THEN
					l_room_or_site := NULL;
				ELSE
					l_room_or_site := LOWER(REPLACE(SUBSTR(l_location, l_3rd_slash + 1), ' ', '_'));
				END IF;
				
				IF (l_room_or_site IS NOT NULL 
				    AND UPPER(l_room_or_site) = 'ROAM')
				THEN
					l_roaming := 'Y';
					l_court_room_id := NULL;
					l_courtroom_or_site := 'S';
					l_location_string := l_location_string || '/roam';
				ELSE
					-- Determine whether this is a Court Room or Site
					IF ( (LENGTH(l_room_or_site) > 0)
					   AND (RTRIM(l_room_or_site, '0123456789') IS NULL) )
					THEN

						-- Get the Court Room ID, set Court Room or Site variable to 'R', and complete location string
						SELECT	court_room_id,
								'R',
								l_location_string || '/' || REPLACE(LOWER(display_name), ' ', '_')
						INTO	l_court_room_id,
								l_courtroom_or_site,
								l_location_string
						FROM	xhb_court_room
						WHERE	court_site_id = l_court_site_id
						AND		crest_court_room_no = l_room_or_site;

					ELSE
						-- Set the Court Room ID to NULL, set Court Room or Site variable to 'S', and complete location string
						l_court_room_id     := NULL;
						l_courtroom_or_site := 'S';
						l_location_string   := l_location_string || '/' || l_room_or_site;
					END IF;

				END IF;
				
			END IF;
			
		END IF;

		update_terminal(l_terminal_name,
					    l_location_string,
				        l_court_id,
				        l_court_site_id,
				        l_court_room_id,
				        l_courtroom_or_site,
				        l_roaming);

		EXCEPTION
    		WHEN NO_DATA_FOUND THEN
				RAISE_APPLICATION_ERROR(-20001, SQLCODE || ':' || SQLERRM); 

	END maintain_terminal;
	
	FUNCTION get_min_terminal_for_room(p_court_room_id	IN XHB_TERMINAL.court_room_id%TYPE) 
		RETURN NUMBER AS
		l_terminal_id	XHB_TERMINAL.terminal_id%TYPE;
	BEGIN
		BEGIN
			SELECT MIN(terminal_id) INTO l_terminal_id
			FROM xhb_terminal
			WHERE court_room_id = p_court_room_id;
		EXCEPTION
			WHEN OTHERS THEN 
				l_terminal_id := 0;
		END;
		RETURN l_terminal_id;
	END get_min_terminal_for_room;

END xhb_terminal_pkg;
/
show errors
