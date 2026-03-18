/*
 * Filename:    Delete_fix_PR57556.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        15th February 2006
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 15/02/2006	K SHAH			Created as a standalone script for DB_Patch_7_5_5
 *
 */

set echo on
spool delete_fix_PR57556.log


/*  Delete script needs to be run to fix PR57556 */

select count(*) from xhb_defendant_on_offence;
delete xhb_defendant_on_offence where offence_id in (select offence_id from xhb_offence where charge_id =11499);
select count(*) from xhb_defendant_on_offence;


select count(*) from xhb_offence;
delete xhb_offence where charge_id = 11499;
select count(*) from xhb_offence;


select count(*) from xhb_charge;
delete xhb_charge where charge_id = 11499;
select count(*) from xhb_charge;

COMMIT;

spool off
