create or replace PACKAGE xhb_housekeeping_pkg AS

/*********************************************************************************
* XHB_HOUSEKEEPING_PKG_H.SQL
* CCN0365 - Housekeping
* Deletes Case and Listing data
*
* Version  Date       Author   Comments
* 1.0      10/03/2009 D Field  Creation
* 1.1      17/03/2009 D Field  Added comments after code review
* 1.2      12/06/2018 C Cash   Obsolete Cases and populate XHB_CASE_HISTORY
* 1.3      29/06/2018 J Riley  Added delete_case_ctx which deletes records by case_id 
*                              for case-dependent tables including ctx-specific ones
* 1.4      26/02/2019 M Newman Added delete for XHB_SH_JUSTICE for CTX_3666
* 2.0      30/08/2019 S Sethuraman  Enhanced XHIBIT HOUSEKEEPING - CTX_4490
*          03/09/2019 S Sethuraman  CTX_4491
*          09/09/2019 S Sethuraman  CTX-4494 - Procedure write_del_record_to_history created
*          11/09/2019 S Sethuraman  CTX-4495 - Rollback transaction of above 4494 task if error at any stage 
*          17/09/2019 S Sethuraman  CTX-4498 : Delete records from XHB_HEARING, XHB_SITTING
*          18/09/2019 S Sethuraman  CTX-4499 : Delete records from XHB_LIST, XHB_SITTING_ON_LIST
*          19/09/2019 S Sethuraman  CTX-4500 : Delete records from XHB_COURTEL_LIST
*          30/09/2019 S Sethuraman  CTX-4501 : Housekeep running_list
*          29/01/2020 M Harris      CPP-18   : process_cpp_listing
************************************************************************************/

  TYPE cases_for_deletion_rec IS RECORD (case_id XHB_CASE.CASE_ID%TYPE);
  TYPE cases_for_deletion_tab IS TABLE OF cases_for_deletion_rec;

  PROCEDURE initiate_run  (p_run_type      IN VARCHAR2           -- (A)ll,(C)ases,(L)istings
                          ,p_case_limit    IN NUMBER DEFAULT 0   -- Limit on number of cases to delete
                          ,p_running_list  IN NUMBER             -- Months to keep running lists for
                          ,p_warned_list   IN NUMBER             -- Months to keep warning lists for
                          ,p_firm_list     IN NUMBER             -- Months to keep firm lists for
                          ,p_daily_list    IN NUMBER             -- Months to keep daily lists for
                          ,p_success_log   IN BOOLEAN DEFAULT FALSE -- Produced success log or not
                          ,p_total_streams IN NUMBER  DEFAULT 1  -- Total number of concurrent HK jobs
                          ,p_stream_number IN NUMBER  DEFAULT 1  -- The stream to process
                          );

  PROCEDURE write_metrics_log_file(p_hk_run_id IN NUMBER);
  PROCEDURE write_hk3_metrics_log_file(p_hk3_run_id IN NUMBER);
  PROCEDURE write_hk_cpp_metrics_log_file(p_hk_cpp_run_id IN NUMBER
										 ,p_MS_days IN NUMBER
										 ,p_MF_days IN NUMBER);

  PROCEDURE write_error_log_file(p_hk_run_id IN NUMBER);
  PROCEDURE write_hk3_error_log_file(p_hk3_run_id IN NUMBER);
  PROCEDURE write_hk_cpp_error_log_file(p_hk_cpp_run_id IN NUMBER);
 
  PROCEDURE obsolete_case (p_case_id    IN xhb_case.case_id%TYPE
                          ,p_del_reason IN VARCHAR2);
 
  PROCEDURE insert_case_history (p_case_id       IN xhb_case.case_id%TYPE
                                ,p_change_reason IN VARCHAR2);

  PROCEDURE delete_case_ctx (p_success_log  IN BOOLEAN DEFAULT FALSE);
                           
  PROCEDURE delete_sh_justice (p_justice_id IN XHB_SH_JUSTICE.SH_JUSTICE_ID%TYPE);  

  FUNCTION cases_for_deletion RETURN cases_for_deletion_tab PIPELINED;
  
  PROCEDURE delete_judge_usage (p_age IN NUMBER DEFAULT 2500);
  PROCEDURE delete_courtroom_usage (p_age IN NUMBER DEFAULT 2500);
  PROCEDURE delete_obsolete_judges (p_judge_limit IN NUMBER DEFAULT 0);
  PROCEDURE obsolete_unused_judges;
  PROCEDURE process_judges (p_age IN NUMBER DEFAULT 2500,
							p_judge_limit IN NUMBER DEFAULT 0);
-- ENHANCED XHIBIT HOUSEKEEPING
-- CTX-4490 
  PROCEDURE find_all_cases_eligible_for_hk (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                            , p_upper_limit IN NUMBER DEFAULT 10000);

-- CTX-4491                                              
  PROCEDURE find_all_hk_cases_for_deletion (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                            , p_upper_limit IN NUMBER DEFAULT 10000);
											
  PROCEDURE process_cad_hk (p_age IN NUMBER DEFAULT 180);
  PROCEDURE process_report_hk (p_age IN NUMBER DEFAULT 180);

-- CTX-4494, CTX-4495                                             
  PROCEDURE write_del_record_to_history (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                         , p_upper_limit IN NUMBER DEFAULT 10000);

-- CTX-4498                                             
  PROCEDURE del_list_data_part1 (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                         , p_upper_limit IN NUMBER DEFAULT 10000);

-- CTX-4499                                             
  PROCEDURE del_list_data_part2 (p_court_id IN xhibit.xhb_court.court_id%type DEFAULT NULL
                                         , p_upper_limit IN NUMBER DEFAULT 10000);

-- CTX-4500                                             
  PROCEDURE del_courtel_list_data (p_upper_limit IN NUMBER DEFAULT 10000,
                                   p_courtel_period IN NUMBER DEFAULT 180);
-- CTX-4501                                             
  PROCEDURE housekeep_running_list (p_upper_limit IN NUMBER DEFAULT 10000,
                                    p_running_obs_period IN NUMBER DEFAULT 180,
                                    p_running_del_period IN NUMBER DEFAULT 730);
									
-- CPP-101 
  PROCEDURE process_cpp_formatting(p_MS_days IN NUMBER DEFAULT 7,
								   p_MF_days IN NUMBER DEFAULT 30);
 
-- CPP-18 
  PROCEDURE process_cpp_listing(p_MS_days IN NUMBER DEFAULT 7,
                                p_MF_days IN NUMBER DEFAULT 30);
								
-- CPP-27
  PROCEDURE process_cpp_staging(p_vs_no_of_days IN NUMBER DEFAULT 7,
								   p_other_no_of_days IN NUMBER DEFAULT 30);
 
-- CPP-104 
  PROCEDURE process_cpp(p_MS_days IN NUMBER DEFAULT 7,
                        p_MF_days IN NUMBER DEFAULT 30);
  
-- CTX-4660, CPP-174
  PROCEDURE set_case_to_historic(p_case_id IN XHB_CASE.CASE_ID%TYPE);
  
  PROCEDURE delete_remand_reasons(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE);
  -- XLC4-92
  PROCEDURE delete_aggravating_reasons(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE);
 
--DVR-16
  PROCEDURE generate_darts_report(p_start_date IN XHB_CASE.CRP_LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE - 1)
								 ,p_end_date IN XHB_CASE.CRP_LAST_UPDATE_DATE%TYPE DEFAULT TRUNC(SYSDATE));
								 
  PROCEDURE generate_darts_report(p_start_date IN VARCHAR2
								 ,p_end_date IN VARCHAR2);
 
  PROCEDURE delete_case_retention_policy(p_case_id IN NUMBER, p_log_delete IN BOOLEAN DEFAULT TRUE);
 
END xhb_housekeeping_pkg;
/