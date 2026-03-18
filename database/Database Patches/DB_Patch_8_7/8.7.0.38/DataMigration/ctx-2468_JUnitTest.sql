/*Run initial cursor*/
SELECT stg_dm.appeal_type
    ,      xc.case_id
    ,      xo.offence_id
    ,      xo.crest_offence_id
    ,      stg_dm.chg_id
    FROM  xhbstg_charge_dm stg_dm
    ,     xhibit.xhb_offence xo
    ,     xhibit.xhb_charge xchg
    ,     xhibit.xhb_case xc
    WHERE stg_dm.chg_id = xo.crest_offence_id
    AND   xo.charge_id = xchg.charge_id
    AND   xchg.case_id = xc.case_id
    AND   xc.court_id = 81--v_xhibit_court_id
    AND   stg_dm.crest_court_id = 453--p_crest_court_id
    AND   xc.case_type = 'A'
    AND   NVL(xo.obs_ind,'N')<> 'Y'
    AND   NVL(xchg.obs_ind,'N')<> 'Y'
    AND   NVL(stg_dm.xhibit_etl_status,'N') != 'U'
    AND   stg_dm.xhibit_enrich_date IS NULL
    ;
--No rows returned.  There are no rows in the dataset for case_type = 'A'.  As a test, the  "AND   xc.case_type = 'A'    " was commented out

SELECT stg_dm.appeal_type
    ,      xc.case_id
    ,      xo.offence_id
    ,      xo.crest_offence_id
    ,      stg_dm.chg_id
    FROM  xhbstg_charge_dm stg_dm
    ,     xhibit.xhb_offence xo
    ,     xhibit.xhb_charge xchg
    ,     xhibit.xhb_case xc
    WHERE stg_dm.chg_id = xo.crest_offence_id
    AND   xo.charge_id = xchg.charge_id
    AND   xchg.case_id = xc.case_id
    AND   xc.court_id = 81--v_xhibit_court_id
    AND   stg_dm.crest_court_id = 453--p_crest_court_id
    --AND   xc.case_type = 'A'
    AND   NVL(xo.obs_ind,'N')<> 'Y'
    AND   NVL(xchg.obs_ind,'N')<> 'Y'
    AND   NVL(stg_dm.xhibit_etl_status,'N') != 'U'
    AND   stg_dm.xhibit_enrich_date IS NULL
    ;

--33 rows
"APPEAL_TYPE"                 "CASE_ID"                     "OFFENCE_ID"                  "CREST_OFFENCE_ID"            "CHG_ID"                      
""                            "621164"                      "647117"                      "24335"                       "24335"                       
""                            "620913"                      "646975"                      "24147"                       "24147"                       
""                            "620913"                      "646976"                      "24148"                       "24148"                       
""                            "620913"                      "646993"                      "24149"                       "24149"                       
""                            "620913"                      "646974"                      "24186"                       "24186"                       
""                            "621486"                      "647682"                      "24450"                       "24450"                       
""                            "621491"                      "647692"                      "24451"                       "24451"                       
""                            "621508"                      "647706"                      "24464"                       "24464"                       
""                            "621508"                      "647707"                      "24465"                       "24465"                       
"C"                           "621508"                      "647705"                      "24469"                       "24469"                       
""                            "621478"                      "647737"                      "24665"                       "24665"                       
""                            "621478"                      "647738"                      "24692"                       "24692"                       
""                            "621481"                      "647720"                      "25042"                       "25042"                       
""                            "621481"                      "647719"                      "25043"                       "25043"                       
""                            "621481"                      "647721"                      "25044"                       "25044"                       
""                            "621465"                      "647730"                      "25546"                       "25546"                       
""                            "621533"                      "647733"                      "25557"                       "25557"                       
""                            "621544"                      "647745"                      "25560"                       "25560"                       
""                            "621544"                      "647746"                      "25561"                       "25561"                       
""                            "621544"                      "647747"                      "25562"                       "25562"                       
""                            "621544"                      "647750"                      "25563"                       "25563"                       
""                            "621544"                      "647751"                      "25564"                       "25564"                       
""                            "621544"                      "647748"                      "25565"                       "25565"                       
""                            "621563"                      "647789"                      "25569"                       "25569"                       
""                            "621563"                      "647790"                      "25570"                       "25570"                       
""                            "621569"                      "647792"                      "25571"                       "25571"                       
""                            "621569"                      "647793"                      "25572"                       "25572"                       
""                            "621576"                      "647799"                      "25574"                       "25574"                       
""                            "621576"                      "647798"                      "25575"                       "25575"                       
""                            "621589"                      "647803"                      "25578"                       "25578"                       
""                            "621535"                      "647739"                      "25515"                       "25515"                       
""                            "621535"                      "647740"                      "25516"                       "25516"                       
""                            "621587"                      "647807"                      "25579"                       "25579"                       

DECLARE
 BEGIN dm_process_pkg_cc.upd_xhb_offence_with_crest(p_crest_court_id => 453);
END;


--DBMS_OUTPUT

CREST - COURT : 453 - Starting process of updating existing row in XHB_OFFENCE for new columns with the required data from CREST
CREST - COURT : 453 - Starting process of updating existing rows in XHB_OFFENCE for new columns with the required data from CREST
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647117 for charge id 24335 on case id 621164
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 646975 for charge id 24147 on case id 620913
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 646976 for charge id 24148 on case id 620913
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 646993 for charge id 24149 on case id 620913
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 646974 for charge id 24186 on case id 620913
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647682 for charge id 24450 on case id 621486
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647692 for charge id 24451 on case id 621491
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647706 for charge id 24464 on case id 621508
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647707 for charge id 24465 on case id 621508
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to C for offence id 647705 for charge id 24469 on case id 621508
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647737 for charge id 24665 on case id 621478
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647738 for charge id 24692 on case id 621478
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647720 for charge id 25042 on case id 621481
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647719 for charge id 25043 on case id 621481
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647721 for charge id 25044 on case id 621481
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647730 for charge id 25546 on case id 621465
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647733 for charge id 25557 on case id 621533
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647745 for charge id 25560 on case id 621544
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647746 for charge id 25561 on case id 621544
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647747 for charge id 25562 on case id 621544
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647750 for charge id 25563 on case id 621544
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647751 for charge id 25564 on case id 621544
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647748 for charge id 25565 on case id 621544
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647789 for charge id 25569 on case id 621563
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647790 for charge id 25570 on case id 621563
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647792 for charge id 25571 on case id 621569
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647793 for charge id 25572 on case id 621569
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647799 for charge id 25574 on case id 621576
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647798 for charge id 25575 on case id 621576
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647803 for charge id 25578 on case id 621589
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647739 for charge id 25515 on case id 621535
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647740 for charge id 25516 on case id 621535
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647807 for charge id 25579 on case id 621587
 
CTX-2468:XHB_OFFENCE - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:xhbstg_charge_dm - Crest Court : 453 - Updated no of rows : 1
 
CTX-2468:XHB_OFFENCE processed 33 for CREST_COURT_ID : 453 successfully!
XHB_OFFENCE Updated for CREST_COURT_ID : 453 successfully!

--33 records updated which is correct

--check data has gone in

SELECT * FROM xhbstg_data_migration_log WHERE action_name = 'upd_xhb_offence_with_crest- CTX-2468';
"DATA_MIGRATION_LOG_ID"       "COURT_ID"                    "ACTION_NAME"                 "RUN_TIME"                    "LOG_MESSAGE_TYPE"            "LOG_MESSAGE"                 "ERROR_ROW_COUNT"             "SUCCESS_ROW_COUNT"           "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  
"127570"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "XHB_OFFENCE - Starting process of updating existing rows for new columns with the required data from CREST"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127571"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647117 for charge id 24335 on case id 621164"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127572"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647117 for charge id 24335 on case id 621164 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127573"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 646975 for charge id 24147 on case id 620913"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127574"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 646975 for charge id 24147 on case id 620913 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127575"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 646976 for charge id 24148 on case id 620913"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127576"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 646976 for charge id 24148 on case id 620913 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127577"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 646993 for charge id 24149 on case id 620913"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127578"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 646993 for charge id 24149 on case id 620913 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127579"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 646974 for charge id 24186 on case id 620913"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127580"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 646974 for charge id 24186 on case id 620913 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127581"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647682 for charge id 24450 on case id 621486"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127582"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647682 for charge id 24450 on case id 621486 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127583"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647692 for charge id 24451 on case id 621491"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127584"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647692 for charge id 24451 on case id 621491 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127585"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647706 for charge id 24464 on case id 621508"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127586"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647706 for charge id 24464 on case id 621508 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127587"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647707 for charge id 24465 on case id 621508"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127588"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647707 for charge id 24465 on case id 621508 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127589"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to C for offence id 647705 for charge id 24469 on case id 621508"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127590"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to C for offence id 647705 for charge id 24469 on case id 621508 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127591"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647737 for charge id 24665 on case id 621478"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127592"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647737 for charge id 24665 on case id 621478 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127593"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647738 for charge id 24692 on case id 621478"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127594"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647738 for charge id 24692 on case id 621478 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127595"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647720 for charge id 25042 on case id 621481"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127596"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647720 for charge id 25042 on case id 621481 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127597"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647719 for charge id 25043 on case id 621481"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127598"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647719 for charge id 25043 on case id 621481 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127599"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647721 for charge id 25044 on case id 621481"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127600"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647721 for charge id 25044 on case id 621481 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127601"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647730 for charge id 25546 on case id 621465"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127602"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647730 for charge id 25546 on case id 621465 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127603"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647733 for charge id 25557 on case id 621533"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127604"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647733 for charge id 25557 on case id 621533 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127605"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647745 for charge id 25560 on case id 621544"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127606"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647745 for charge id 25560 on case id 621544 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127607"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647746 for charge id 25561 on case id 621544"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127608"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647746 for charge id 25561 on case id 621544 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127609"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647747 for charge id 25562 on case id 621544"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127610"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647747 for charge id 25562 on case id 621544 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127611"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647750 for charge id 25563 on case id 621544"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127612"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647750 for charge id 25563 on case id 621544 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127613"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647751 for charge id 25564 on case id 621544"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127614"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647751 for charge id 25564 on case id 621544 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127615"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647748 for charge id 25565 on case id 621544"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127616"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647748 for charge id 25565 on case id 621544 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127617"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647789 for charge id 25569 on case id 621563"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127618"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647789 for charge id 25569 on case id 621563 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127619"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647790 for charge id 25570 on case id 621563"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127620"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647790 for charge id 25570 on case id 621563 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127621"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647792 for charge id 25571 on case id 621569"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127622"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647792 for charge id 25571 on case id 621569 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127623"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647793 for charge id 25572 on case id 621569"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127624"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647793 for charge id 25572 on case id 621569 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127625"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647799 for charge id 25574 on case id 621576"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127626"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647799 for charge id 25574 on case id 621576 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127627"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647798 for charge id 25575 on case id 621576"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127628"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647798 for charge id 25575 on case id 621576 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127629"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647803 for charge id 25578 on case id 621589"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127630"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647803 for charge id 25578 on case id 621589 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127631"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647739 for charge id 25515 on case id 621535"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127632"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647739 for charge id 25515 on case id 621535 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127633"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647740 for charge id 25516 on case id 621535"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127634"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647740 for charge id 25516 on case id 621535 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127635"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE - processing crest court id - 453 setting appeal_type to  for offence id 647807 for charge id 25579 on case id 621587"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127636"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "UPDATING XHB_OFFENCE processing crest court id - 453 setting appeal_type to  for offence id 647807 for charge id 25579 on case id 621587 with ETL_STATUS U"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127637"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "XHB_OFFENCE : Processed 33 successfully!"""                            "33"                          "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"127638"                      "453"                         "upd_xhb_offence_with_crest- CTX-2468""03-SEP-2018"                 "I"                           "XHB_OFFENCE : updating xhbstg_charge_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  453"""                            "2154"                        "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              


rollback;	
	