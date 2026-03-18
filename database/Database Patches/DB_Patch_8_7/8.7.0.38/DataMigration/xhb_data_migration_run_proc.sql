create or replace PROCEDURE xhb_data_migration_run_proc (p_xhibit_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS 
 /**
* CGI CREST TO XHIBIT Program
*
* MODULE      : xhb_data_migration_run_proc
*
* DESCRIPTION : This procedure is to be used as a harness for the data migration to execute the individual data migration
*               procedures in the xhb_data_migration_process_pkg.  The running order is as per the data Migration v0.10 FS

* VERSION HISTORY:
*
* Date          Author(s)   Version    Nature of Change
* ----------    ----------  --------   -----------------------------------------------------------------------
* 18/10/2018    C.Cash.      1.0        Call the data migration scripts
*                                       
**/
BEGIN

 --Req.4975.DM.001 - ctx-2173
 BEGIN
  xhb_data_migration_process_pkg.list_crest_cases_not_in_xhibit(p_xhibit_court_id);
 END;

 --Req.4975.DM.002 - ctx-2174
 BEGIN
  --This procedure calls the function update_xhbstg_case_dm
  xhb_data_migration_process_pkg.merge_crest_cases_into_xhibit(p_xhibit_court_id);
 END;
 
 --Req.4975.DM.003 - ctx-2176 
  --this ticket is yet to be done and is connected to the broker process
 
 --Req.4975.DM.004 -ctx tickets ctx-2584/2478/2178
 BEGIN
  xhb_data_migration_process_pkg.update_xhb_case_with_crest(p_xhibit_court_id);
 END;

 --Req.4975.DM.005 -ctx tickets ctx-2179
 BEGIN
  xhb_data_migration_process_pkg.update_xhb_court_with_crest(p_xhibit_court_id);
 END;

 --Req.4975.DM.006/7/8 
 --CTX-2180. New CTX fields for XHB_DEFENDANT - Sec 4.3.2.3 req [4975.DM.006]
 --CTX-2181. New CTX fields for XHB_DEFENDANT_ON_CASE - Sec 4.3.2.4 req [4975.DM.007]
 --CTX-2182. New CTX fields for XHB_DEF_ON_CASE_REF_SOL_FIRM - Sec 4.3.2.5 req [4975.DM.008]
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_defendant_with_crest(p_xhibit_court_id);
 END;


 --Req.4975.DM.009 -ctx tickets ctx-2183
 BEGIN
  xhb_data_migration_process_pkg.update_xhb_cpa_with_crest(p_xhibit_court_id);
 END;

 --Req.4975.DM.010 -ctx tickets ctx-2184
 BEGIN
  xhb_data_migration_process_pkg.update_xhb_prsf_with_crest(p_xhibit_court_id);
 END;
 
 --Req.4975.DM.011 -ctx tickets ctx-2189
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_charges_log_crest(p_xhibit_court_id);
 END;
 
 --Req.4975.DM.012/13
 --CTX-2185. New CTX fields for XHB_LEGAL_AID_ORDER - Sec 4.3.2.9 req [4975.DM.012]
 --CTX-2610. New CTX fields for XHB_LEGAL_AID_AMENDMENT - Sec 4.3.2.10 req [4975.DM.013]
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_legal_aid_order_crest(p_xhibit_court_id);
 END;  

 --Req.4975.DM.014 -ctx tickets ctx-2197
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_case_history_crest(p_xhibit_court_id);
 END;
 
  --Req.4975.DM.015/16/20/21
  -- DESCRIPTION: CTX-2198 New CTX fields for XHB_CASE_LISTING_ENTRY - 
  --        Cases newly migrated from CREST - Sec 4.3.2.12 req [4975.DM.015]
  --        Cases already existing in XHIBIT and not in CASE_LISTING_ENTRY - Sec 4.3.2.12 req [4975.DM.016]
  -- DESCRIPTION: CTX-2201 New CTX fields for XHB_DIARY_NOTE_ENTRY - 
  --        Default List Notes from CASE table newly migrated from CREST - Sec 4.3.2.16 req [4975.DM.020]
  --        Other Notes from CASE_NOTE table - Sec 4.3.2.16 req [4975.DM.021]
  --         req [4975.DM.21A] trigger modification included in deployment
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_c_list_entry_crest(p_xhibit_court_id);
 END;
 
 --same as about with for records with no case number
 BEGIN
  xhb_data_migration_process_pkg.upd_diary_ne_no_case_crest(p_xhibit_court_id);
 END;
 

 --Req.4975.DM.017/18
 -- DESCRIPTION: CTX-2199 New CTX fields for XHB_CASE_DIARY_FIXTURE - 
 --        Cases new data to be populated from CREST - Sec 4.3.2.13 req [4975.DM.017]
 --              CTX-2200 New CTX fields for XHB_FIXTURE_DEFT_ATTENDING - 
 --        Cases new data to be populated from CREST - Sec 4.3.2.14 req [4975.DM.018]
 
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_c_diary_fixture_crest(p_xhibit_court_id);
 END;
 
  --Req.4975.DM.019 --CTX-2206
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_dir_for_case_crest(p_xhibit_court_id);
 END; 
 
   --Req.4975.DM.022 --CTX-2202
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_case_nad_with_crest(p_xhibit_court_id);
 END;
 
 --Req.4975.DM.023 --CTX-2186
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_ref_chamber_crest(p_xhibit_court_id);
 END;
 
 --Req.4975.DM.024 --CTX-2187
 BEGIN
  xhb_data_migration_process_pkg.update_xhb_rsf_with_crest(p_xhibit_court_id);
 END;
 
  --Req.4975.DM.025 --CTX-2203
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_ref_jud_tckt_crest(p_xhibit_court_id);
 END;
 
  --Req.4975.DM.026 --CTX-2204
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_bw_history_crest(p_xhibit_court_id);
 END;
 
   --Req.4975.DM.027 --CTX-2205
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_mon_ord_track_crest(p_xhibit_court_id);
 END;
 
    --Req.4975.DM.028 --CTX-2188
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_court_site_with_crest(p_xhibit_court_id);
 END;
 
     --Req.4975.DM.029 --CTX-2189
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_court_room_with_crest(p_xhibit_court_id);
 END;

     --Req.4975.DM.028 --CTX-2659
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_croom_usage_crest(p_xhibit_court_id);
 END;

     --Req.4975.DM.029 --CTX-2674
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_jud_usage_crest(p_xhibit_court_id);
 END;
 
 --Req.4975.DM.030 --CTX-2580
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_def_history_crest(p_xhibit_court_id);
 END;
 
  --Req.4975.DM.031 --CTX-2402
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_lists_with_crest(p_xhibit_court_id);
 END;
 
 --Req.4975.DM.032 --CTX-2403
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_sit_on_list_crest(p_xhibit_court_id);
 END;
 
  --Req.4975.DM.033 --CTX-2404
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_case_on_list_crest(p_xhibit_court_id);
 END;
 
 --Req.4975.DM.034 --CTX-2405
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_doc_on_list_crest(p_xhibit_court_id);
 END;
 
  --Req.4975.DM.035 --CTX-2468
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_offence_with_crest(p_xhibit_court_id);
 END;

  --Req.4975.DM.036 --CTX-2579
 BEGIN
  xhb_data_migration_process_pkg.upd_xhb_doc_history_crest(p_xhibit_court_id);
 END;

END xhb_data_migration_run_proc;
/
show errors