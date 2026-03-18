/*Run initial cursor*/
SELECT cc.crest_court_id,
           xhc.court_id,
           cc.case_no,
           cc.case_type,
           xc.case_id,
           decode(cc.hrg_len_lo_unit,'H',1,'D',2,'W',3,'M',4,'Y',5) as trial_time_unit,
           cc.hrg_len_lo as trial_time_estimate
    from xhibit.xhb_court xhc,
         xhibit.xhb_case xc,
         data_mig.xhbstg_case_dm cc
    where cc.crest_court_id = :p_crest_court_id and
          cc.crest_court_id = xhc.crest_court_id and 
          xc.court_id = xhc.court_id and
          xc.case_number = cc.case_no and
          xc.case_type = cc.case_type and 
          NVL(cc.xhibit_etl_status,'N') not in ('I', 'U') and
          cc.xhibit_enrich_date is NULL  and
          NOT exists  -- if CASE_ID exists, NO insert or UPDATE required, ignore CREST data
          (select 'X' from xhibit.xhb_directions_for_case 
            where case_id = xc.case_id and 
                  nvl(obs_ind,'N') != 'Y'); 
	

"CREST_COURT_ID"              "COURT_ID"                    "CASE_NO"                     "CASE_TYPE"                   "CASE_ID"                     "TRIAL_TIME_UNIT"             "TRIAL_TIME_ESTIMATE"         
"453"                         "81"                          "20080061"                    "A"                           "620918"                      ""                            ""                            
"453"                         "81"                          "20087434"                    "T"                           "620981"                      ""                            ""                            
"453"                         "81"                          "20080068"                    "A"                           "620926"                      ""                            ""                            
"453"                         "81"                          "20080116"                    "S"                           "620913"                      ""                            ""                            
"453"                         "81"                          "20087392"                    "T"                           "621478"                      "2"                           "1"                           
"453"                         "81"                          "20080092"                    "A"                           "621161"                      ""                            ""                            
"453"                         "81"                          "20087538"                    "T"                           "621523"                      ""                            ""                            
"453"                         "81"                          "20087539"                    "T"                           "621525"                      ""                            ""                            
"453"                         "81"                          "20087476"                    "T"                           "621164"                      ""                            ""                            
"453"                         "81"                          "20087495"                    "T"                           "621479"                      ""                            ""                            
"453"                         "81"                          "20087497"                    "T"                           "621480"                      ""                            ""                            
"453"                         "81"                          "20087498"                    "T"                           "621481"                      ""                            ""                            
"453"                         "81"                          "20110023"                    "T"                           "621535"                      ""                            ""                            
"453"                         "81"                          "20110018"                    "S"                           "621529"                      ""                            ""                            
"453"                         "81"                          "20110017"                    "A"                           "621530"                      ""                            ""                            
"453"                         "81"                          "20110036"                    "T"                           "621527"                      ""                            ""                            
"453"                         "81"                          "20117019"                    "T"                           "621528"                      ""                            ""                            
"453"                         "81"                          "20117028"                    "T"                           "621462"                      ""                            ""                            
"453"                         "81"                          "20117037"                    "T"                           "621465"                      ""                            ""                            
"453"                         "81"                          "20110040"                    "T"                           "621466"                      ""                            ""                            
"453"                         "81"                          "20110041"                    "T"                           "621467"                      ""                            ""                            
"453"                         "81"                          "20110042"                    "T"                           "621468"                      ""                            ""                            
"453"                         "81"                          "20117076"                    "T"                           "621532"                      ""                            ""                            
"453"                         "81"                          "20110051"                    "T"                           "621533"                      ""                            ""                            
"453"                         "81"                          "20110023"                    "A"                           "621534"                      ""                            ""                            
"453"                         "81"                          "20140001"                    "S"                           "621539"                      ""                            ""                            
"453"                         "81"                          "20140001"                    "T"                           "621540"                      ""                            ""                            
"453"                         "81"                          "20147002"                    "T"                           "621541"                      ""                            ""                            
"453"                         "81"                          "20140002"                    "T"                           "621543"                      ""                            ""                            
"453"                         "81"                          "20140002"                    "S"                           "621544"                      ""                            ""                            
"453"                         "81"                          "20140003"                    "A"                           "621545"                      ""                            ""                            
"453"                         "81"                          "20140004"                    "T"                           "621546"                      ""                            ""                            
"453"                         "81"                          "20147003"                    "T"                           "621547"                      ""                            ""                            
"453"                         "81"                          "20140003"                    "S"                           "621550"                      ""                            ""                            
"453"                         "81"                          "20140005"                    "A"                           "621551"                      ""                            ""                            
"453"                         "81"                          "20147005"                    "T"                           "621554"                      ""                            ""                            
"453"                         "81"                          "20147006"                    "T"                           "621556"                      ""                            ""                            
"453"                         "81"                          "20140011"                    "T"                           "621560"                      ""                            ""                            
"453"                         "81"                          "20147011"                    "T"                           "621561"                      ""                            ""                            
"453"                         "81"                          "20147012"                    "T"                           "621562"                      ""                            ""                            
"453"                         "81"                          "20140013"                    "T"                           "621563"                      ""                            ""                            
"453"                         "81"                          "20147013"                    "T"                           "621564"                      ""                            ""                            
"453"                         "81"                          "20147014"                    "T"                           "621566"                      ""                            ""                            
"453"                         "81"                          "20147015"                    "T"                           "621567"                      ""                            ""                            
"453"                         "81"                          "20140004"                    "S"                           "621568"                      ""                            ""                            
"453"                         "81"                          "20140006"                    "S"                           "621569"                      ""                            ""                            
"453"                         "81"                          "20140015"                    "T"                           "621570"                      ""                            ""                            
"453"                         "81"                          "20140006"                    "A"                           "621571"                      ""                            ""                            
"453"                         "81"                          "20147018"                    "T"                           "621572"                      ""                            ""                            
"453"                         "81"                          "20147019"                    "T"                           "621573"                      ""                            ""                            
"453"                         "81"                          "20140016"                    "T"                           "621574"                      ""                            ""                            
"453"                         "81"                          "20147020"                    "T"                           "621575"                      ""                            ""                            
"453"                         "81"                          "20140008"                    "S"                           "621576"                      ""                            ""                            
"453"                         "81"                          "20140017"                    "T"                           "621577"                      ""                            ""                            
"453"                         "81"                          "20147027"                    "T"                           "621579"                      ""                            ""                            
"453"                         "81"                          "20140009"                    "S"                           "621581"                      ""                            ""                            
"453"                         "81"                          "20147028"                    "T"                           "621586"                      ""                            ""                            
"453"                         "81"                          "20140020"                    "T"                           "621585"                      ""                            ""                            
"453"                         "81"                          "20147029"                    "T"                           "621587"                      ""                            ""                            
"453"                         "81"                          "20140010"                    "S"                           "621588"                      ""                            ""                            
"453"                         "81"                          "20140011"                    "S"                           "621589"                      ""                            ""                            
"453"                         "81"                          "20140012"                    "S"                           "621590"                      ""                            ""                            
"453"                         "81"                          "20147030"                    "T"                           "621591"                      ""                            ""                            

--63 rows returned.  63 rows should be added to xhibit.xhb_directions_for_case

select count(*) from xhibit.xhb_directions_for_case;

--98 rows returned


begin
 dbms_output.enable(10000000);
 dm_process_pkg_cc.upd_xhb_dir_for_case_crest (453);
end;


CREST - COURT : 453 - Starting process of inserting new rows in XHB_CHARGES_LOG with the required data to be populated from CREST
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20080061v_case_type : A, v_case_id : 620918 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20087434v_case_type : T, v_case_id : 620981 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20080068v_case_type : A, v_case_id : 620926 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20080116v_case_type : S, v_case_id : 620913 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20087392v_case_type : T, v_case_id : 621478 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20080092v_case_type : A, v_case_id : 621161 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20087538v_case_type : T, v_case_id : 621523 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20087539v_case_type : T, v_case_id : 621525 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20087476v_case_type : T, v_case_id : 621164 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20087495v_case_type : T, v_case_id : 621479 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20087497v_case_type : T, v_case_id : 621480 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20087498v_case_type : T, v_case_id : 621481 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110023v_case_type : T, v_case_id : 621535 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110018v_case_type : S, v_case_id : 621529 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110017v_case_type : A, v_case_id : 621530 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110036v_case_type : T, v_case_id : 621527 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20117019v_case_type : T, v_case_id : 621528 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20117028v_case_type : T, v_case_id : 621462 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20117037v_case_type : T, v_case_id : 621465 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110040v_case_type : T, v_case_id : 621466 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110041v_case_type : T, v_case_id : 621467 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110042v_case_type : T, v_case_id : 621468 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20117076v_case_type : T, v_case_id : 621532 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110051v_case_type : T, v_case_id : 621533 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20110023v_case_type : A, v_case_id : 621534 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140001v_case_type : S, v_case_id : 621539 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140001v_case_type : T, v_case_id : 621540 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147002v_case_type : T, v_case_id : 621541 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140002v_case_type : T, v_case_id : 621543 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140002v_case_type : S, v_case_id : 621544 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140003v_case_type : A, v_case_id : 621545 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140004v_case_type : T, v_case_id : 621546 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147003v_case_type : T, v_case_id : 621547 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140003v_case_type : S, v_case_id : 621550 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140005v_case_type : A, v_case_id : 621551 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147005v_case_type : T, v_case_id : 621554 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147006v_case_type : T, v_case_id : 621556 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140011v_case_type : T, v_case_id : 621560 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147011v_case_type : T, v_case_id : 621561 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147012v_case_type : T, v_case_id : 621562 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140013v_case_type : T, v_case_id : 621563 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147013v_case_type : T, v_case_id : 621564 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147014v_case_type : T, v_case_id : 621566 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147015v_case_type : T, v_case_id : 621567 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140004v_case_type : S, v_case_id : 621568 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140006v_case_type : S, v_case_id : 621569 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140015v_case_type : T, v_case_id : 621570 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140006v_case_type : A, v_case_id : 621571 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147018v_case_type : T, v_case_id : 621572 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147019v_case_type : T, v_case_id : 621573 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140016v_case_type : T, v_case_id : 621574 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147020v_case_type : T, v_case_id : 621575 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140008v_case_type : S, v_case_id : 621576 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140017v_case_type : T, v_case_id : 621577 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147027v_case_type : T, v_case_id : 621579 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140009v_case_type : S, v_case_id : 621581 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147028v_case_type : T, v_case_id : 621586 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140020v_case_type : T, v_case_id : 621585 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147029v_case_type : T, v_case_id : 621587 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140010v_case_type : S, v_case_id : 621588 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140011v_case_type : S, v_case_id : 621589 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20140012v_case_type : S, v_case_id : 621590 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = 453
v_case_no : 20147030v_case_type : T, v_case_id : 621591 , v_xhibit_court_id 81
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2206:XHB_DIRECTIONS_FOR_CASE inserted 63 for CREST_COURT_ID : 453 successfully!

--run another count
select count(*) from xhibit.xhb_directions_for_case;

--161 rows returned which is correct (98+63)

                       "8112"                        ""                            ""                            "9"                           "9"                           ""                            ""                            ""                            ""                            "25-NOV-2008"                 "29-AUG-2018"                 "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              "1"                           "N"                           


rollback;	
	