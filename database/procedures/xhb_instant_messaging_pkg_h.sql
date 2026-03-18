CREATE OR REPLACE PACKAGE xhb_instant_messaging_pkg AS
    FUNCTION get_all_message_groups RETURN SYS_REFCURSOR;

END xhb_instant_messaging_pkg;
/
show errors
