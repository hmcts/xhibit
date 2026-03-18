CREATE OR REPLACE PACKAGE BODY xhb_connected_user_pkg AS
       PROCEDURE get_by_later_than_date(results_out    OUT SYS_REFCURSOR,
                                    activity_date_in IN  XHB_CONNECTED_USER.last_access_time%TYPE)
        IS
        BEGIN
            OPEN results_out FOR
                SELECT connected_user.* 
                FROM   XHB_CONNECTED_USER connected_user
                WHERE  (activity_date_in IS NULL OR connected_user.LAST_ACCESS_TIME > activity_date_in OR connected_user.LAST_ACCESS_TIME IS NULL)
                ORDER BY  DECODE(connected_user.component,'PublicDisplay',1,0),connected_user.last_access_time DESC;
        END get_by_later_than_date;

        PROCEDURE get_connected_users(results_out  OUT SYS_REFCURSOR)
        IS
        BEGIN
            OPEN results_out FOR
                SELECT connected_user.*
                FROM     XHB_CONNECTED_USER connected_user
                ORDER BY  DECODE(connected_user.component,'PublicDisplay',1,0),connected_user.last_access_time DESC;
        END get_connected_users;

        PROCEDURE get_nonPD_by_later_than_date(results_out    OUT SYS_REFCURSOR,
                                    activity_date_in IN  XHB_CONNECTED_USER.last_access_time%TYPE)
        IS
        BEGIN
            OPEN results_out FOR
               SELECT CONNECTED_USER.* 
               FROM   XHB_CONNECTED_USER CONNECTED_USER
               WHERE  (activity_date_in IS NULL OR CONNECTED_USER.LAST_ACCESS_TIME = activity_date_in)
               AND CONNECTED_USER.COMPONENT != 'PublicDisplay';         
        END get_nonPD_by_later_than_date;


        PROCEDURE get_nonPD_connected_users(results_out  OUT SYS_REFCURSOR)
        IS
        BEGIN
           OPEN results_out FOR
               SELECT CONNECTED_USER.* 
               FROM XHB_CONNECTED_USER CONNECTED_USER
               WHERE CONNECTED_USER.COMPONENT != 'PublicDisplay';
        END get_nonPD_connected_users;

        PROCEDURE get_PD_by_later_than_date(results_out    OUT SYS_REFCURSOR,
                                    activity_date_in IN  XHB_CONNECTED_USER.last_access_time%TYPE)
        IS
        BEGIN
           OPEN results_out FOR
              SELECT CONNECTED_USER.* 
              FROM   XHB_CONNECTED_USER CONNECTED_USER
              WHERE  (activity_date_in IS NULL OR CONNECTED_USER.LAST_ACCESS_TIME = activity_date_in)
              AND CONNECTED_USER.COMPONENT = 'PublicDisplay'
              ORDER BY LAST_ACCESS_TIME DESC;         
        END get_PD_by_later_than_date;


        PROCEDURE get_PD_connected_users(results_out  OUT SYS_REFCURSOR)
        IS
        BEGIN
          OPEN results_out FOR
              SELECT CONNECTED_USER.* 
              FROM XHB_CONNECTED_USER CONNECTED_USER
              WHERE CONNECTED_USER.COMPONENT = 'PublicDisplay'
              ORDER BY LAST_ACCESS_TIME DESC;
        END get_PD_connected_users;
END xhb_connected_user_pkg;
/
show errors