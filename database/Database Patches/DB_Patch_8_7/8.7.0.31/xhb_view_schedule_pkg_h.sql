create or replace PACKAGE xhb_view_schedule_pkg AS
   -- deprecated - see comment below...
    PROCEDURE get_schedule(results_out      OUT SYS_REFCURSOR,
                           court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
                           start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE,
                           court_room_id_in IN  XHB_SITTING.court_room_id%TYPE);


   -- deprecated - see comment below...
    PROCEDURE get_daily_list(results_out   OUT SYS_REFCURSOR,
                             court_id_in   IN  XHB_HEARING_LIST.court_id%TYPE,
                             start_date_in IN  XHB_HEARING_LIST.start_date%TYPE);
                          
	PROCEDURE get_unpublished_daily_list(results_out   OUT SYS_REFCURSOR,
                            p_list_id       IN XHB_LIST.LIST_ID%TYPE,
                            show_court_list IN  XHB_CASE_ON_LIST.IS_COURT_ROOM_LIST_ENTRY%TYPE ,
                            show_public_view IN XHB_DEFENDANT_ON_CASE.is_masked%TYPE,
                            court_id_in   IN  XHB_HEARING_LIST.court_id%TYPE); 
        
	FUNCTION convert_to_boolean(p_yn_field VARCHAR2) RETURN NUMBER;

    -- The below functions are considerably more efficient versions of the above two
    -- generic procedures.  These are currently only used by the thinclient for the daily list
    -- and summary by name pages (on both witness and probation servives sites).
    -- The thickclient code should be changed to also use this code, and the above procedures
    -- removed when possible.


    FUNCTION get_daily_list_with_judge(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                       p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                       RETURN SYS_REFCURSOR;


    FUNCTION get_daily_list_with_witness(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                         p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                         RETURN SYS_REFCURSOR;


    FUNCTION get_daily_list_by_defendant(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                         p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                         RETURN SYS_REFCURSOR;
                                         
    FUNCTION get_next_list_by_type(p_court_id_in   IN XHB_XML_DOCUMENT.court_id%TYPE,
                                   p_start_date_in IN XHB_XML_DOCUMENT.date_created%TYPE,
                                   p_list_type_in  IN XHB_XML_DOCUMENT.document_type%TYPE)
                                   RETURN SYS_REFCURSOR;
                                         
END xhb_view_schedule_pkg;
/
show errors