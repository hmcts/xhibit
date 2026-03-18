CREATE OR REPLACE PACKAGE BODY xhb_court_log_pkg AS
	   PROCEDURE get_by_case_id(results_out    OUT SYS_REFCURSOR,
                                    case_id_in IN  XHB_COURT_LOG_ENTRY.case_id%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE  COURT_LOG_ENTRY.CASE_ID = case_id_in	
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id;


       PROCEDURE get_by_case_id_date(results_out    OUT SYS_REFCURSOR,
                                     case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                     start_date_in  IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                     end_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE  COURT_LOG_ENTRY.CASE_ID = case_id_in	
                AND    COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_date;


       PROCEDURE get_by_case_id_catdesc(results_out OUT SYS_REFCURSOR,
                                        case_id_in  IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        cat_desc_in IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE)
       IS
       BEGIN
            OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_CATEGORY_DESC CATEGORY_DESC, XHB_COURT_LOG_CATEGORY CATEGORY
                WHERE  CATEGORY_DESC.category_desc_id = CATEGORY.category_desc_id
                AND    CATEGORY_DESC.category_description = cat_desc_in
                AND    COURT_LOG_ENTRY.EVENT_DESC_ID = CATEGORY.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in  
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;          
       END get_by_case_id_catdesc;


       PROCEDURE get_by_case_id_date_catdesc(results_out   OUT SYS_REFCURSOR,
                                             case_id_in    IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                             start_date_in IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                             end_date_in   IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                             cat_desc_in   IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_CATEGORY_DESC CATEGORY_DESC, XHB_COURT_LOG_CATEGORY CATEGORY
                WHERE  CATEGORY_DESC.category_desc_id = CATEGORY.category_desc_id
                AND    CATEGORY_DESC.category_description = cat_desc_in
                AND    COURT_LOG_ENTRY.EVENT_DESC_ID = CATEGORY.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in	
                AND    COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_date_catdesc;


       PROCEDURE get_by_case_eventdesc_date(results_out      OUT SYS_REFCURSOR,
                                            case_id_in       IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                            event_desc_id_in IN  XHB_COURT_LOG_ENTRY.event_desc_id%TYPE,
                                            start_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE  COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND    COURT_LOG_ENTRY.EVENT_DESC_ID = event_desc_id_in
                AND    COURT_LOG_ENTRY.DATE_TIME >= start_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_eventdesc_date;


       PROCEDURE get_by_case_eventtype_date_gt(results_out   OUT SYS_REFCURSOR,
                                               case_id_in    IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                               event_type_in IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE,
                                               start_date_in IN  XHB_COURT_LOG_ENTRY.date_time%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE  COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND    COURT_LOG_EVENT_DESC.EVENT_TYPE = event_type_in
                AND    COURT_LOG_ENTRY.DATE_TIME > start_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_eventtype_date_gt;


       PROCEDURE get_by_case_id_eventtype(results_out   OUT SYS_REFCURSOR,
                                          case_id_in    IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                          event_type_in IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.*
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE  COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND    COURT_LOG_EVENT_DESC.EVENT_TYPE = event_type_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_eventtype;


       PROCEDURE get_by_case_id_date_pd(results_out       OUT SYS_REFCURSOR,
                                        case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        public_display_in IN  XHB_COURT_LOG_EVENT_DESC.public_display%TYPE,
                                        start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                        end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.*
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE  COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND    COURT_LOG_EVENT_DESC.PUBLIC_DISPLAY = public_display_in
                AND    COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME DESC, COURT_LOG_ENTRY.ENTRY_ID DESC;		   
       END get_by_case_id_date_pd;


       PROCEDURE get_court_sched_hearing_value(results_out  OUT SYS_REFCURSOR,
                                               p_case_id_in IN  XHB_HEARING.case_id%TYPE)
       IS
       BEGIN
            OPEN results_out FOR
                SELECT xsh.scheduled_hearing_id,
                       xrht.hearing_type_code,
                       xhl.start_date
                FROM   xhb_hearing xh,
                       xhb_scheduled_hearing xsh,
                       xhb_ref_hearing_type xrht,
                       xhb_sitting xs,
                       xhb_hearing_list xhl
                WHERE  xh.hearing_id = xsh.hearing_id
                AND    xh.ref_hearing_type_id = xrht.ref_hearing_type_id
                AND    xsh.sitting_id = xs.sitting_id
                AND    xs.list_id = xhl.list_id
                AND    xh.case_id = p_case_id_in
                ORDER BY xhl.start_date, xsh.scheduled_hearing_id;
       END get_court_sched_hearing_value;
END xhb_court_log_pkg;
/
show errors