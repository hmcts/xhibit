CREATE OR REPLACE PACKAGE xhb_translation_pkg AS

    ----
    -- Get the current ref translation data for read, excludes audit and keys!
    ----
    FUNCTION get_ref_translation RETURN SYS_REFCURSOR;

END xhb_translation_pkg;
/
show errors
