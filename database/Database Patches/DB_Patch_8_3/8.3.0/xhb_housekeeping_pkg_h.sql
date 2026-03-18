CREATE OR REPLACE PACKAGE xhb_housekeeping_pkg AS

/*********************************************************************************
* XHB_HOUSEKEEPING_PKG_H.SQL
* CCN0365 - Housekeping 
* Deletes Case and Listing data
*
* Version  Date       Author   Comments
* 1.0      10/03/2009 D Field  Creation 
* 1.1      17/03/2009 D Field  Added comments after code review
************************************************************************************/

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

 

END xhb_housekeeping_pkg;
/
