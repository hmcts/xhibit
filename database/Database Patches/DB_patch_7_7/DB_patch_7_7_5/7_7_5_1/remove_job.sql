/*
 * Filename:    remove_job.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        16th February 2006
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 16/02/2006	K SHAH			Remove database job xhb_cr_live_status_pkg.update_xhb_cr_live_tatus
 * 03/03/2006   C RANAWEERA		DBA_JOBS changed to USER_JOBS due to XHIBIT user privileges 
 */

set echo on
spool remove_job.log

select job, substr(what,1,50) 
from user_jobs;



DECLARE
     nid number;
BEGIN
     select job
     into nid
     from user_jobs
     where upper(what) = 'XHB_CR_LIVE_STATUS_PKG.UPDATE_XHB_CR_LIVE_STATUS;';
     DBMS_JOB.REMOVE(nid);
END;
/
COMMIT;


select job, substr(what,1,50)
from user_jobs;

spool off
