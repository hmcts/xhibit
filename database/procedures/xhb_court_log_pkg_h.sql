CREATE OR REPLACE PACKAGE xhb_court_log_pkg AS
	   PROCEDURE get_by_case_id(results_out   OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE);

	   PROCEDURE get_by_case_id_date(results_out   OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

       PROCEDURE get_by_case_id_catdesc(results_out OUT SYS_REFCURSOR,
                                        case_id_in  IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        cat_desc_in IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE);

	   PROCEDURE get_by_case_id_date_catdesc(results_out    OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                cat_desc_in       IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE);

	   PROCEDURE get_by_case_eventdesc_date(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_desc_id_in  IN  XHB_COURT_LOG_ENTRY.event_desc_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

	   PROCEDURE get_by_case_eventtype_date_gt(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_type_in     IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

	   PROCEDURE get_by_case_id_eventtype(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_type_in     IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE);

	   PROCEDURE get_by_case_id_date_pd(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                public_display_in IN  XHB_COURT_LOG_EVENT_DESC.public_display%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

       PROCEDURE get_court_sched_hearing_value(results_out  OUT SYS_REFCURSOR,
                                               p_case_id_in IN  XHB_HEARING.case_id%TYPE);
END xhb_court_log_pkg;
/
show errors