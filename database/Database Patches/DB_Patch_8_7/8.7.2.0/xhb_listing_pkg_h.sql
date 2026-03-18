CREATE OR REPLACE PACKAGE XHB_LISTING_PKG AS 

  PROCEDURE delete_list ( p_list_id IN XHB_LIST.LIST_ID%TYPE );
                      
  PROCEDURE find_cases_for_list_results( p_results_out   OUT SYS_REFCURSOR,
                      p_list_date     	IN  XHB_LIST.LIST_START_DATE%TYPE,
					  p_site_id			IN	XHB_COURT_SITE.COURT_SITE_ID%TYPE,
					  p_room_id			IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
					  p_court_id     	IN  XHB_COURT_SITE.COURT_ID%TYPE,
					  p_mode		IN 	NUMBER);
		
  PROCEDURE get_cases_by_filter(p_results_out OUT SYS_REFCURSOR,
						  p_court_id IN XHB_CASE.COURT_ID%TYPE,
						  p_CASE_TYPE IN VARCHAR2,
						  p_CASE_CLASS IN VARCHAR2,
						  p_BC_STATUS IN VARCHAR2,
						  p_DEFAULT_HEARING_TYPE IN xhb_ref_hearing_type.HEARING_TYPE_CODE%TYPE,
						  p_TIME_EST_FROM IN NUMBER,
						  p_TIME_EST_TO IN NUMBER,
						  p_UNITS IN NUMBER,
						  p_REQUIRED_JUDGE_TYPE IN xhb_case_listing_entry.REF_JUDGE_TYPE_ID%TYPE,
						  p_UNITS_WEEKS IN NUMBER,
						  p_SECURE_COURTROOM IN VARCHAR2,
						  p_JUVENILE_ONLY IN VARCHAR2);	

  FUNCTION get_removal_reason(p_ref_system_code_id IN XHB_REF_SYSTEM_CODE.REF_SYSTEM_CODE_ID%TYPE,
                              p_freetext           IN VARCHAR2) 
    RETURN VARCHAR2;

  FUNCTION case_on_list_exists_YN(p_case_id IN XHB_CASE_ON_LIST.CASE_ID%TYPE,
                                  p_date    IN XHB_CASE_ON_LIST.TIME_LISTED%TYPE DEFAULT NULL)
    RETURN VARCHAR2;

  FUNCTION fixtures_exist_YN(p_case_listing_entry_id IN XHB_CASE_DIARY_FIXTURE.CASE_LISTING_ENTRY_ID%TYPE,
                             p_date                  IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE)
    RETURN VARCHAR2;
    
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
								   
  FUNCTION check_def_bc_status_on_case(p_case_id IN XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE,
									   p_bc_status IN XHB_DEFENDANT_ON_CASE.CURRENT_BC_STATUS%TYPE) RETURN VARCHAR2;

  FUNCTION get_existing_list_id(p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                p_list_type_id IN XHB_REF_LISTING_DATA.REF_LISTING_DATA_ID%TYPE,
                                p_start_date   IN  XHB_LIST.LIST_START_DATE%TYPE,
                                p_end_date     IN  XHB_LIST.LIST_END_DATE%TYPE DEFAULT NULL) 
    RETURN XHB_LIST.LIST_ID%TYPE;

    FUNCTION get_existing_def_on_case_id(p_list_id     IN  XHB_CASE_ON_LIST.LIST_ID%TYPE,
                                p_case_id              IN XHB_CASE_ON_LIST.CASE_ID%TYPE,
                                p_list_date            IN XHB_CASE_ON_LIST.TIME_LISTED%TYPE,
                                p_defendant_on_case_id IN XHB_DEF_ON_CASE_ON_LIST.DEFENDANT_ON_CASE_ID%TYPE) 
    RETURN XHB_DEF_ON_CASE_ON_LIST.DEF_ON_CASE_ON_LIST_ID%TYPE;
END XHB_LISTING_PKG;
/
show errors