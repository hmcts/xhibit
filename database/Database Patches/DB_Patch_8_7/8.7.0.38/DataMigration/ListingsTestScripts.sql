select * from xhbstg_case_sub_appearance_dm
where crest_court_id = 453 and case_no = '20087537'  -- 2788 rows 

     CASE_TYPE  CASE_NO  CHD_ID  SUB_ID  DEFENDANT_NO  COURT_LIST_IND  NOTIF_JOB_ID  COPY_DW_DEFT  CREST_COURT_ID  XHIBIT_COURT_ID  XHIBIT_ETL_STATUS  XHIBIT_LOADED_DATE  XHIBIT_ENRICH_DATE  XHIBIT_ETL_DATE  XHIBIT_ETL_ERR_MESSAGE
1  T  20087537  126299  29585  1        453      06/09/2018 13:59:51      
2  T  20087537  126309  29585  1        453      06/09/2018 13:59:51      
3  T  20087537  126235  29585  1        453      06/09/2018 13:59:51      
4  T  20087537  126240  29585  1        453      06/09/2018 13:59:51      
5  T  20087537  126241  29585  1        453      06/09/2018 13:59:51      
6  T  20087537	126243	29585	1				453			06/09/2018 13:59:51			
7	T	20087537	126289	29585	1				453			06/09/2018 13:59:51			

select * from xhbstg_case_hearing_day_dm
where crest_court_id = 453 

--run xhb_lists first

begin
dbms_output.enable(1000000);
dm_process_pkg_cc.upd_xhb_lists_with_crest(453);
end;

--run xhb_sitting_on_list next

begin
dbms_output.enable(1000000);
dm_process_pkg_cc.upd_xhb_sit_on_list_crest(453);
end;
 
-- Now run the case_on_list procedure :

begin
dbms_output.enable(1000000);
dm_process_pkg_ss.upd_xhb_case_on_list_crest(453);
end;
-- 14 rows procesed


select * from xhibit.xhb_Case_on_list where case_id = 621521

-- now run def_on_case_list 


-- select cursor for def_on_case_on_list -- fetches 63 rows 
  SELECT distinct  xdoc.defendant_on_case_id
    , xcol.case_on_list_id
    , xc.case_id
    , 'N' as OBS_IND
    , csastg.case_no
    , chdstg.list_type       
    , chdstg.case_type
    , chdstg.chd_id
    , csastg.sub_id
    FROM xhbstg_case_sub_appearance_dm csastg
    ,    xhbstg_case_hearing_day_dm chdstg
    ,    xhibit.xhb_case xc
    ,    xhibit.xhb_defendant_on_case xdoc
    ,    xhibit.xhb_defendant xd
    ,    xhibit.xhb_case_on_list xcol
 --   ,    xhibit.xhb_list xl
    WHERE csastg.crest_court_id = 453
 --   AND   csastg.crest_court_id = chdstg.crest_court_id
    AND   csastg.chd_id = chdstg.chd_id
    AND   csastg.case_no = chdstg.case_no
    AND   csastg.case_type = chdstg.case_type
    AND   chdstg.list_type != 'X'    
    AND   csastg.case_no = xc.case_number
    AND   csastg.case_type = xc.case_type
    AND   xc.court_id = 81
    AND   xc.case_id = xdoc.case_id
    AND   nvl(xdoc.obs_ind,'N') <> 'Y'
    AND   xdoc.defendant_id = xd.defendant_id
    AND   xd.crest_defendant_id = csastg.sub_id
    AND   xd.court_id = xc.court_id
    AND   xc.case_id = xcol.case_id
 /*   AND xc.court_id = xl.court_id
    AND xl.list_start_date = chdstg.list_date
    AND   xcol.list_id = xl.list_id
    AND   xl.list_type_id = (select x.ref_listing_data_id from xhibit.xhb_ref_listing_data x
                              where x.ref_data_type = 'LIST_TYPE'
                                 and DECODE(chdstg.list_type,'D','Daily','W','Warned','F','Firm') = x.ref_data_value
                     and   nvl(x.obs_ind,'N') != 'Y') */
    AND   nvl(xcol.obs_ind,'N') <> 'Y'
    AND   NVL(csastg.xhibit_etl_status,'N') NOT IN ('I', 'U') 
    AND   csastg.xhibit_enrich_date IS NULL 
    /*Make sure no duplicate rows are created*/
    AND   NOT EXISTS (SELECT 'x'
                      FROM  xhibit.xhb_def_on_case_on_list xdocol
                      WHERE xdocol.defendant_on_case_id = xdoc.defendant_on_case_id
                      AND   xdocol.case_on_list_id = xcol.case_on_list_id
                      AND   xdocol.case_id = xc.case_id) order by chd_id
    ;  -- 9 rows 

begin
dbms_output.enable(1000000);
dm_process_pkg_cc.upd_xhb_doc_on_list_crest(453);
end;

-------- DBMS_OUTPUT -------
CREST - COURT : 453 - Starting process of inserting rows from CREST CASE_SUBJECT_APPEARANCE table
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM processed 1 for CREST_COURT_ID : 453 successfully!
 
CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - 2781 for CREST_COURT_ID : 453

-------------------------------

-- After running the procedure, check the rows updated 

select * from data_mig.xhbstg_data_migration_log  
where action_name = 'upd_xhb_doc_on_list_crest- CTX-2405'  and lower(log_message) like '%processed%' 
-- success_row_count 63 as expected

select * from data_mig.xhbstg_data_migration_log  
where action_name = 'upd_xhb_doc_on_list_crest- CTX-2405'  and log_message like '%NOT updated%' 
-- success_row_count 2781 as expected

select 63+2781 from dual -- 2844 total rows in XHBSTG_CASE_SUB_APPEARANCE_DM 
-- some records in XHBSTG_CASE_HEARING_DAY_DM   have more than one sitting_on_list records 
-- which leads to the count NOT exactly tallying  shows success_count more by 5 i.e. 8+5 = 13 - BUT results seem to be fine

select * from xhbstg_case_sub_appearance_dm  
where trunc(xhibit_enrich_date) = trunc(sysdate) and  crest_court_id = 453 and xhibit_etl_status = 'I'
 -- 7 as expected

select * from xhbstg_case_sub_appearance_dm  
where trunc(xhibit_enrich_date) = trunc(sysdate) and crest_court_id = 453 and xhibit_etl_status <> 'N' 
-- 2781

select * from xhbstg_case_sub_appearance_dm  
where trunc(xhibit_enrich_date) = trunc(sysdate) and crest_court_id = 453 and xhibit_etl_status not in ('I', 'N' )
-- 2781



select * from xhibit.xhb_def_on_case_on_list  where last_updated_by like 'DATA%' -- 63


 
rollback;
    
    
          
                   
 
