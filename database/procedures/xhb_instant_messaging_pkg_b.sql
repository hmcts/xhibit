CREATE OR REPLACE PACKAGE BODY xhb_instant_messaging_pkg AS


    FUNCTION get_all_message_groups
    RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR


	SELECT xt.LOCATION AS COURT_ROOM_NAME, 
	       xcs.DISPLAY_NAME AS SITE_NAME, 
	       xc.DISPLAY_NAME AS COURT_NAME 
	FROM   XHB_TERMINAL xt, XHB_COURT_SITE xcs, XHB_COURT xc 
	WHERE  xt.COURT_SITE_ID = xcs.COURT_SITE_ID 
	AND    xcs.COURT_ID = xc.COURT_ID 
        AND    xt.COURT_ROOM_ID IS NULL 
        UNION 
	SELECT xcr.COURT_ROOM_NAME AS COURT_ROOM_NAME, 
               xcs.DISPLAY_NAME AS SITE_NAME, 
	       xc.DISPLAY_NAME AS COURT_NAME 
        FROM   XHB_TERMINAL xt, XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs, XHB_COURT xc 
        WHERE  xt.COURT_ROOM_ID = xcr.COURT_ROOM_ID 
        AND    xcr.COURT_SITE_ID = xcs.COURT_SITE_ID 
	AND    xcs.COURT_ID = xc.COURT_ID 
        ORDER BY COURT_NAME, SITE_NAME, COURT_ROOM_NAME;

        RETURN v_return_cursor;
    END get_all_message_groups;



END xhb_instant_messaging_pkg;
/
show errors
