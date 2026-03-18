CREATE OR REPLACE PACKAGE XHB_LISTING_PKG AS 

  PROCEDURE delete_list ( p_list_id IN XHB_LIST.LIST_ID%TYPE );
                      
  PROCEDURE find_cases_for_list_results( p_results_out   OUT SYS_REFCURSOR,
                      p_list_date     	IN  XHB_LIST.LIST_START_DATE%TYPE,
					  p_site_id			IN	XHB_COURT_SITE.COURT_SITE_ID%TYPE,
					  p_room_id			IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
					  p_court_id     	IN  XHB_COURT_SITE.COURT_ID%TYPE,
					  p_mode		IN 	NUMBER);
								  
  PROCEDURE get_case_list_history( p_results_out   OUT SYS_REFCURSOR,
                      p_case_id     IN  XHB_CASE_ON_LIST.CASE_ID%TYPE,
                      p_row_limit    IN  NUMBER);
                      
  PROCEDURE get_defendants_on_list( p_results_out   OUT SYS_REFCURSOR,
                                    p_case_on_list_id     IN  XHB_DEF_ON_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE);

  PROCEDURE get_defendants_on_fixture( p_results_out   OUT SYS_REFCURSOR,
                                       p_case_diary_fixture_id  IN  XHB_FIXTURE_DEFT_ATTENDING.CASE_DIARY_FIXTURE_ID%TYPE);

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

  FUNCTION get_fixture_count(p_court_id     IN  XHB_CASE_LISTING_ENTRY.COURT_ID%TYPE,
                                   p_from_date    IN  XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE,
                                   p_to_date      IN  XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE) RETURN NUMBER;

END XHB_LISTING_PKG;
/
show errors