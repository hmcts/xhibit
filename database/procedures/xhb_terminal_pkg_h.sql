CREATE OR REPLACE PACKAGE xhb_terminal_pkg AS
    FUNCTION get_terminals(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE) RETURN SYS_REFCURSOR;
    FUNCTION get_courts RETURN SYS_REFCURSOR;
    FUNCTION get_court_sites RETURN SYS_REFCURSOR;
    FUNCTION get_terminal_by_primary_key(p_terminal_id_in IN XHB_TERMINAL.terminal_id%TYPE) RETURN SYS_REFCURSOR;

    PROCEDURE maintain_terminal(p_terminal_name	IN xhb_terminal.terminal_name%TYPE,	p_location	IN xhb_terminal.location%TYPE);
	
	FUNCTION get_min_terminal_for_room(p_court_room_id	IN XHB_TERMINAL.court_room_id%TYPE) RETURN NUMBER;

END xhb_terminal_pkg;
/
show errors
