SELECT xcrl.crest_court_id
    ,      xcrl.site_code
    ,      xcrl.site_group
    ,      xcrl.floater_text
    ,      xcrl.list_name
    ,      xcrt.court_id
    FROM  xhbstg_courtroom_location_dm   xcrl
    ,     xhibit.xhb_court xcrt
    WHERE xcrl.crest_court_id = 453
    AND   xcrl.crest_court_id = xcrt.crest_court_id
    AND EXISTS (SELECT 'x' --only return rows that can be updated rather than return everything
                FROM  xhibit.xhb_court_site xcs
                WHERE xcrt.court_id = xcs.court_id 
                AND   xcs.court_site_code = xcrl.site_code
                AND   NVL(xcs.obs_ind,'-')<> 'Y'
                );

/* RESULT
453	A	1	FLOATERS - COURT TO BE ALLOCATED	SNARESBROOK CROWN COURT	81
*/

select crest_court_id, court_site_code, site_group, floater_text, list_name, obs_ind from XHIBIT.XHB_COURT_SITE where crest_court_id = 453 AND court_site_code = 'A';

/*RESULT *** The records below should be updated when the procedure is run
453	A			NULL  NULL  NULL
453	A			NULL  NULL  NULL
453	A			NULL  NULL  NULL
*/

DECLARE
 BEGIN dm_process_pkg_cc.upd_xhb_court_site_with_crest(p_crest_court_id => 453);
END;

select crest_court_id, court_site_code, site_group, floater_text, list_name from XHIBIT.XHB_COURT_SITE where crest_court_id = 453 AND court_site_code = 'A';
/* RESULT - Successful.  All non obsolete records updated
453	A				Y
453	A				Y
453	A	1	FLOATERS - COURT TO BE ALLOCATED	SNARESBROOK CROWN COURT	N
*/

select * 
from xhbstg_data_migration_log
WHERE action_name like '%CTX-2188%'
order by 1 desc;
/* RESULT

6865	453	upd_xhb_court_site_with_crest- CTX-2188	22-AUG-2018	I	XHB_COURT_SITE : updating xhbstg_courtroom_location_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  453		1	22-AUG-2018	DATA MIGRATION	DATA MIGRATION
