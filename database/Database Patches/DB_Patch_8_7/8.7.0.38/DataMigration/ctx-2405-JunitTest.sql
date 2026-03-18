select * from xhbstg_case_hearing_day_dm
where crest_court_id = 453  and chd_id in (126243,126971, 126972)-- 2123 total rows for court 453 

----- Please NOTE Unit Test for 2405 - XHB_Defendant_on_case_on_list  covers CASE_ON_LIST SELECT CURSOR 1 DATA where CTD_ID is NOT NULL

---This Unit Test is part 2 where CTD_ID iS NULL 

------------------------ Pre-requisites - if required run these , if these are run already leave these ----------
--run xhb_lists first

begin
dbms_output.enable(1000000);
xhb_data_migration_process_pkg.upd_xhb_lists_with_crest(453);
end;

select * from xhibit.xhb_list where last_updated_by like 'DATA%' -- 1235

--run xhb_sitting_on_list next

begin
dbms_output.enable(1000000);
xhb_data_migration_process_pkg.upd_xhb_sit_on_list_crest(453);
end;


begin
dbms_output.enable(1000000);
xhb_data_migration_process_pkg.upd_xhb_case_on_list_crest(453);
end;



/*Run the initial cursor*/

SELECT xdoc.defendant_on_case_id
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
    ,    xhibit.xhb_list xl
    WHERE csastg.crest_court_id = :p_crest_court_id
    AND   csastg.crest_court_id = chdstg.crest_court_id
    AND   csastg.chd_id = chdstg.chd_id
    AND   chdstg.list_type != 'X' 
    AND   csastg.case_no = xc.case_number
    AND   csastg.case_type = xc.case_type
    AND   xc.court_id = :v_xhibit_court_id
    AND   xc.case_id = xdoc.case_id
    AND   nvl(xdoc.obs_ind,'N') <> 'Y'
    AND   xdoc.defendant_id = xd.defendant_id
    AND   xd.crest_defendant_id = csastg.sub_id
    AND   xd.court_id = xc.court_id
    AND   xc.case_id = xcol.case_id
    AND   xc.court_id = xl.court_id
    AND   xl.list_start_date = chdstg.list_date
    AND   xcol.list_id = xl.list_id
    AND   xl.list_type_id = (SELECT x.ref_listing_data_id
                             FROM xhibit.xhb_ref_listing_data x
                             WHERE x.ref_data_type = 'LIST_TYPE'
                             AND DECODE(chdstg.list_type,'D','Daily','F','Firm','W','Warned') = x.ref_data_value
                             AND NVL(x.obs_ind,'N') <> 'Y')
    AND   nvl(xcol.obs_ind,'N') <> 'Y'
    AND   NVL(csastg.xhibit_etl_status,'N') NOT IN ('I', 'U') 
    AND   csastg.xhibit_enrich_date IS NULL 
    /*Make sure no duplicate rows are created*/
    AND   NOT EXISTS (SELECT 'x'
                      FROM  xhibit.xhb_def_on_case_on_list xdocol
                      WHERE xdocol.defendant_on_case_id = xdoc.defendant_on_case_id
                      AND   xdocol.case_on_list_id = xcol.case_on_list_id
                      AND   xdocol.case_id = xc.case_id)
    ;
    
 --48 rows returned
 
"DEFENDANT_ON_CASE_ID"        "CASE_ON_LIST_ID"             "CASE_ID"                     "OBS_IND"                     "CASE_NO"                     "LIST_TYPE"                   "CASE_TYPE"                   "CHD_ID"                      "SUB_ID"                      
"954800"                      "1830"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1829"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1820"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1728"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1845"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1727"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1844"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1821"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1729"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1730"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126235"                      "29585"                       
"954800"                      "1731"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126240"                      "29585"                       
"954800"                      "1840"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126240"                      "29585"                       
"954800"                      "1732"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126240"                      "29585"                       
"954800"                      "1835"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126240"                      "29585"                       
"954800"                      "1826"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126240"                      "29585"                       
"954800"                      "1733"                        "621521"                      "N"                           "20087537"                    "F"                           "T"                           "126241"                      "29585"                       
"954800"                      "1846"                        "621521"                      "N"                           "20087537"                    "F"                           "T"                           "126241"                      "29585"                       
"954800"                      "1831"                        "621521"                      "N"                           "20087537"                    "F"                           "T"                           "126241"                      "29585"                       
"954800"                      "1822"                        "621521"                      "N"                           "20087537"                    "F"                           "T"                           "126241"                      "29585"                       
"954800"                      "1734"                        "621521"                      "N"                           "20087537"                    "F"                           "T"                           "126241"                      "29585"                       
"954800"                      "1819"                        "621521"                      "N"                           "20087537"                    "W"                           "T"                           "126243"                      "29585"                       
"954800"                      "1832"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126289"                      "29585"                       
"954800"                      "1843"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126289"                      "29585"                       
"954800"                      "1736"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126289"                      "29585"                       
"954800"                      "1823"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126289"                      "29585"                       
"954800"                      "1735"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126289"                      "29585"                       
"954800"                      "1737"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1836"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1828"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1838"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1837"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1740"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1739"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1738"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1839"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1827"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126299"                      "29585"                       
"954800"                      "1833"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1842"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1741"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1841"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1744"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1743"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1834"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1824"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1825"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954800"                      "1742"                        "621521"                      "N"                           "20087537"                    "D"                           "T"                           "126309"                      "29585"                       
"954820"                      "1847"                        "621543"                      "N"                           "20140002"                    "W"                           "T"                           "126971"                      "29738"                       
"954823"                      "1848"                        "621546"                      "N"                           "20140004"                    "W"                           "T"                           "126972"                      "29192"                       
 

SELECT count(*) FROM xhibit.XHB_DEF_ON_CASE_ON_LIST; --561 rows returned

begin
dbms_output.enable(1000000);
xhb_data_migration_process_pkg.upd_xhb_doc_on_list_crest(453);
end; 

--DBMS_OUTPUT

CREST - COURT : 453 - Starting process of inserting rows from CREST CASE_SUBJECT_APPEARANCE table
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126235, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126240, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126241, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126243, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126289, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126299, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20087537 ,v_case_type : T, v_sub_id: 29585 v_chd_id 126309, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20140002 ,v_case_type : T, v_sub_id: 29738 v_chd_id 126971, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = 453
v_case_no : 20140004 ,v_case_type : T, v_sub_id: 29192 v_chd_id 126972, v_xhibit_court_id 81
 
CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2405:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM processed 48 for CREST_COURT_ID : 453 successfully!
 
CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - 2779 for CREST_COURT_ID : 453

SELECT * FROM xhbstg_data_migration_log where action_name = 'upd_xhb_doc_on_list_crest- CTX-2405' ORDER BY 1 DESC;

"DATA_MIGRATION_LOG_ID"       "COURT_ID"                    "ACTION_NAME"                 "RUN_TIME"                    "LOG_MESSAGE_TYPE"            "LOG_MESSAGE"                 "ERROR_ROW_COUNT"             "SUCCESS_ROW_COUNT"           "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  
"212709"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "XHB_DEF_ON_CASE_ON_LIST : updating XHBSTG_CASE_SUB_APPEARANCE_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  453"""                            "2779"                        "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212708"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "XHB_DEF_ON_CASE_ON_LIST : Processed 48 successfully!"""                            "48"                          "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212707"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20140004 case type T sub id 29192 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212706"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954823 case_on_list_id : 1848, case_id : 621546 list type: W"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212705"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20140002 case type T sub id 29738 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212704"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954820 case_on_list_id : 1847, case_id : 621543 list type: W"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212703"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212702"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1742, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212701"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212700"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1825, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212699"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212698"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1824, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212697"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212696"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1834, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212695"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212694"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1743, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212693"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212692"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1744, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212691"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212690"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1841, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212689"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212688"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1741, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212687"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212686"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1842, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212685"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212684"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1833, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212683"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212682"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1827, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212681"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212680"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1839, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212679"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212678"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1738, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212677"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212676"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1739, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212675"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212674"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1740, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212673"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212672"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1837, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212671"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212670"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1838, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212669"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212668"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1828, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212667"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212666"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1836, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212665"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212664"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1737, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212663"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212662"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1735, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212661"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212660"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1823, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212659"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212658"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1736, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212657"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212656"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1843, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212655"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212654"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1832, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212653"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212652"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1819, case_id : 621521 list type: W"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212651"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212650"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1734, case_id : 621521 list type: F"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212649"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212648"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1822, case_id : 621521 list type: F"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212647"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212646"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1831, case_id : 621521 list type: F"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212645"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212644"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1846, case_id : 621521 list type: F"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212643"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212642"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1733, case_id : 621521 list type: F"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212641"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212640"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1826, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212639"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212638"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1835, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212637"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212636"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1732, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212635"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212634"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1840, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212633"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212632"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1731, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212631"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212630"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1730, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212629"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212628"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1729, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212627"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212626"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1821, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212625"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212624"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1844, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212623"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212622"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1727, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212621"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212620"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1845, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212619"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212618"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1728, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212617"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212616"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1820, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212615"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212614"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1829, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212613"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: 20087537 case type T sub id 29585 row with ETL_STATUS I"""                            "1"                           "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212612"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - 954800 case_on_list_id : 1830, case_id : 621521 list type: D"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"212611"                      "453"                         "upd_xhb_doc_on_list_crest- CTX-2405""21-SEP-2018"                 "I"                           "XHB_DEF_ON_CASE_ON_LIST - Starting process of inserting  rows from CREST CASE_SUBJECT_APPEARANCE table"""                            ""                            "21-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              


--48 rows processed as expected

SELECT 561 + 48 FROM dual; --609

SELECT count(*) FROM xhibit.XHB_DEF_ON_CASE_ON_LIST; --609 rows returned as expected
 