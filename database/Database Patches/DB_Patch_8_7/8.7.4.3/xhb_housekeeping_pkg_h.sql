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

  PROCEDURE write_error_log_file(p_hk_run_id IN NUMBER);
 
  PROCEDURE obsolete_case (p_case_id    IN xhb_case.case_id%TYPE
                          ,p_del_reason IN VARCHAR2);
 
  PROCEDURE insert_case_history (p_case_id       IN xhb_case.case_id%TYPE
                                ,p_change_reason IN VARCHAR2);

  PROCEDURE delete_case_ctx (p_success_log  IN BOOLEAN DEFAULT FALSE);
                           
  PROCEDURE delete_sh_justice (p_justice_id IN XHB_SH_JUSTICE.SH_JUSTICE_ID%TYPE);  

  FUNCTION cases_for_deletion RETURN cases_for_deletion_tab PIPELINED;

END xhb_housekeeping_pkg;
/