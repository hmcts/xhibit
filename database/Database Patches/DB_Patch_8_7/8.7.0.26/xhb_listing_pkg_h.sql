CREATE OR REPLACE PACKAGE XHB_LISTING_PKG AS 

  PROCEDURE get_list(p_results_out   OUT SYS_REFCURSOR,
                      p_court_id     IN  XHB_LIST_LAST_UPDATED_V.COURT_ID%TYPE,
                      p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                      p_from_date    IN  XHB_LIST_LAST_UPDATED_V.LIST_START_DATE%TYPE,
                      p_to_date      IN  XHB_LIST_LAST_UPDATED_V.LIST_START_DATE%TYPE);
					  
  PROCEDURE delete_list ( p_list_id IN XHB_LIST.LIST_ID%TYPE );
                      
  PROCEDURE get_case_list_history( p_results_out   OUT SYS_REFCURSOR,
                      p_case_id     IN  XHB_CASE_ON_LIST.CASE_ID%TYPE);
                      
  PROCEDURE get_defendants_on_list( p_results_out   OUT SYS_REFCURSOR,
                                    p_case_on_list_id     IN  XHB_DEF_ON_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE);  
					  
  PROCEDURE find_cases_for_list_results( p_results_out   OUT SYS_REFCURSOR,
                      p_list_date     	IN  XHB_LIST.LIST_START_DATE%TYPE,
					  p_site_id			IN	XHB_COURT_SITE.COURT_SITE_ID%TYPE,
					  p_room_id			IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
					  p_court_id     	IN  XHB_COURT_SITE.COURT_ID%TYPE,
					  p_all_sites		IN 	NUMBER,
					  p_all_rooms		IN 	NUMBER,
					  p_floater_rooms	IN	NUMBER);

  -- Get next SittingOnListId
  FUNCTION get_next_SOL_id RETURN XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE;

  -- Get next CaseOnListId
  FUNCTION get_next_COL_id RETURN XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE;

  PROCEDURE get_latest_unpublished_daily(p_results_out  OUT SYS_REFCURSOR,
                                         p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                         p_diary_date   IN  XHB_LIST.LIST_START_DATE%TYPE);

  PROCEDURE get_latest_published_daily(p_results_out  OUT SYS_REFCURSOR,
                                       p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                       p_diary_date   IN  XHB_LIST.LIST_START_DATE%TYPE);

  PROCEDURE get_published_list_for_date(p_results_out  OUT SYS_REFCURSOR,
                                        p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                        p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                                        p_diary_date   IN  XHB_LIST.LIST_START_DATE%TYPE);

  PROCEDURE get_list_in_date_range(p_results_out  OUT SYS_REFCURSOR,
                                   p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                   p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                                   p_from_date    IN  XHB_LIST.LIST_START_DATE%TYPE,
                                   p_to_date      IN  XHB_LIST.LIST_END_DATE%TYPE);

END XHB_LISTING_PKG;
/
show errors