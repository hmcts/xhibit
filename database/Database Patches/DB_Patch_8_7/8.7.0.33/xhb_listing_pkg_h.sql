CREATE OR REPLACE PACKAGE XHB_LISTING_PKG AS 

  PROCEDURE delete_list ( p_list_id IN XHB_LIST.LIST_ID%TYPE );
                      
  PROCEDURE find_cases_for_list_results( p_results_out   OUT SYS_REFCURSOR,
                      p_list_date     	IN  XHB_LIST.LIST_START_DATE%TYPE,
					  p_site_id			IN	XHB_COURT_SITE.COURT_SITE_ID%TYPE,
					  p_room_id			IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
					  p_court_id     	IN  XHB_COURT_SITE.COURT_ID%TYPE,
					  p_mode		IN 	NUMBER);
					  
  PROCEDURE update_case_details ( p_case_id IN XHB_CASE.CASE_ID%TYPE,
								  p_case_listing_entry_id IN XHB_CASE_LISTING_ENTRY.CASE_LISTING_ENTRY_ID%TYPE,
								  p_directions_for_case_id IN XHB_DIRECTIONS_FOR_CASE.DIRECTIONS_FOR_CASE_ID%TYPE,
								  p_hearing_type IN XHB_CASE.DEFAULT_HEARING_TYPE%TYPE,
								  p_time_est IN XHB_DIRECTIONS_FOR_CASE.TRIAL_TIME_ESTIMATE%TYPE,
								  p_time_unit IN XHB_DIRECTIONS_FOR_CASE.TRIAL_TIME_UNIT%TYPE,
								  p_judge IN XHB_CASE_LISTING_ENTRY.JUDGE_ID%TYPE,
								  p_court_id IN  XHB_COURT.COURT_ID%TYPE,
								  p_user IN XHB_CASE.LAST_UPDATED_BY%TYPE);
								  
  PROCEDURE get_case_list_history( p_results_out   OUT SYS_REFCURSOR,
                      p_case_id     IN  XHB_CASE_ON_LIST.CASE_ID%TYPE);
                      
  PROCEDURE get_defendants_on_list( p_results_out   OUT SYS_REFCURSOR,
                                    p_case_on_list_id     IN  XHB_DEF_ON_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE);

  -- Get next SittingOnListId
  FUNCTION get_next_SOL_id RETURN XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE;

  -- Get next CaseOnListId
  FUNCTION get_next_COL_id RETURN XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE;

  PROCEDURE get_list_in_date_range(p_results_out  OUT SYS_REFCURSOR,
                                   p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                   p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                                   p_from_date    IN  XHB_LIST.LIST_START_DATE%TYPE,
                                   p_to_date      IN  XHB_LIST.LIST_END_DATE%TYPE);

   PROCEDURE get_latest_list(p_results_out  OUT SYS_REFCURSOR,
                                   p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                   p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                                   p_diary_date   IN  XHB_LIST.LIST_START_DATE%TYPE);

  PROCEDURE get_final_list_for_date_daily(p_results_out  OUT SYS_REFCURSOR,
                                   p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                   p_diary_date   IN  XHB_LIST.LIST_START_DATE%TYPE);
								   
  FUNCTION validate_lists_for_fixture (p_case_id IN XHB_CASE.CASE_ID%TYPE,
                                       p_fixture_date IN XHB_LIST.LIST_START_DATE%TYPE) RETURN VARCHAR2;

  PROCEDURE get_list(p_results_out  OUT SYS_REFCURSOR,
                     p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                     p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                     p_row_limit    IN  NUMBER);
END XHB_LISTING_PKG;
/
show errors