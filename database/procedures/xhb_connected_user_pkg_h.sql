CREATE OR REPLACE PACKAGE xhb_connected_user_pkg AS
       PROCEDURE get_by_later_than_date(results_out   OUT SYS_REFCURSOR,
                                activity_date_in      IN  XHB_CONNECTED_USER.last_access_time%TYPE);

       PROCEDURE get_connected_users(results_out      OUT SYS_REFCURSOR);
       
       PROCEDURE get_nonPD_by_later_than_date(results_out   OUT SYS_REFCURSOR,
                                       activity_date_in      IN  XHB_CONNECTED_USER.last_access_time%TYPE);
       
       PROCEDURE get_nonPD_connected_users(results_out      OUT SYS_REFCURSOR);
       
       PROCEDURE get_PD_by_later_than_date(results_out   OUT SYS_REFCURSOR,
                                       activity_date_in      IN  XHB_CONNECTED_USER.last_access_time%TYPE);
       
       PROCEDURE get_PD_connected_users(results_out      OUT SYS_REFCURSOR);

END xhb_connected_user_pkg;
/
show errors