/*Run initial cursor*/
SELECT xrld.ref_listing_data_id AS LIST_TYPE_ID
    ,      NULL AS LIST_PARENT_ID
    ,      xc.court_id
    ,      xlstg.start_date AS LIST_START_DATE
    ,      xlstg.end_date AS LIST_END_DATE
    ,      xlstg.date_published
    ,      CASE
            WHEN xlstg.date_published IS NOT NULL THEN 'SUCCESS'
            WHEN xlstg.date_published IS NULL THEN NULL
           END PUBLISH_STATUS
    ,      NULL AS PUBLISH_ERROR_REASON
    --,      NVL(xlstg.list_status,'D') DRAFT_OR_FINAL
    ,      SUBSTR(NVL(list_status,'D'),1,1) DRAFT_OR_FINAL
    ,      NVL(xlstg.edition_no,1) AS LIST_NUMBER --Brian confirmed to se to 1 if null.  XHB_LIST has a not null constraint on this field
    ,     'N' AS OBS_IND
    ,     xlstg.lst_id
    FROM xhbstg_lists_dm xlstg
    ,    xhibit.xhb_ref_listing_data xrld
    ,    xhibit.xhb_court xc
    WHERE xlstg.crest_court_id = :p_crest_court_id
    AND   xrld.ref_data_type = 'LIST_TYPE'
    AND   DECODE(xlstg.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
    AND   NVL(xrld.obs_ind,'N') <> 'Y'
    AND   NVL(xc.obs_ind,'N') <> 'Y'
    AND   xlstg.crest_court_id = xc.crest_court_id
    AND   NVL(xlstg.xhibit_etl_status,'N') not in ('I', 'U') 
    AND   xlstg.xhibit_enrich_date is NULL
    ;
	

"LIST_TYPE_ID"                "LIST_PARENT_ID"              "COURT_ID"                    "LIST_START_DATE"             "LIST_END_DATE"               "DATE_PUBLISHED"              "PUBLISH_STATUS"              "PUBLISH_ERROR_REASON"        "DRAFT_OR_FINAL"              "LIST_NUMBER"                 "OBS_IND"                     "LST_ID"                      
"38"                          ""                            "81"                          "16-OCT-2017"                 "16-OCT-2017"                 "13-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12389"                       
"38"                          ""                            "81"                          "12-OCT-2017"                 "12-OCT-2017"                 "10-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12387"                       
"38"                          ""                            "81"                          "10-OCT-2017"                 "10-OCT-2017"                 "09-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12385"                       
"38"                          ""                            "81"                          "06-OCT-2017"                 "06-OCT-2017"                 "03-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12383"                       
"38"                          ""                            "81"                          "04-OCT-2017"                 "04-OCT-2017"                 "03-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12381"                       
"38"                          ""                            "81"                          "02-OCT-2017"                 "02-OCT-2017"                 "28-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12379"                       
"38"                          ""                            "81"                          "27-SEP-2017"                 "27-SEP-2017"                 "25-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12376"                       
"38"                          ""                            "81"                          "29-AUG-2017"                 "29-AUG-2017"                 "29-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12357"                       
"38"                          ""                            "81"                          "25-AUG-2017"                 "25-AUG-2017"                 "24-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12355"                       
"38"                          ""                            "81"                          "23-AUG-2017"                 "23-AUG-2017"                 "22-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12353"                       
"38"                          ""                            "81"                          "18-AUG-2017"                 "18-AUG-2017"                 "17-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12351"                       
"38"                          ""                            "81"                          "03-AUG-2017"                 "03-AUG-2017"                 "03-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "6"                           "N"                           "12349"                       
"38"                          ""                            "81"                          "21-JUL-2017"                 "21-JUL-2017"                 "21-JUL-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12346"                       
"38"                          ""                            "81"                          "19-JUL-2017"                 "19-JUL-2017"                 "19-JUL-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12344"                       
"38"                          ""                            "81"                          "10-JUL-2017"                 "10-JUL-2017"                 "06-JUL-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12342"                       
"38"                          ""                            "81"                          "28-JUN-2017"                 "28-JUN-2017"                 "27-JUN-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12340"                       
"38"                          ""                            "81"                          "26-JUN-2017"                 "26-JUN-2017"                 "23-JUN-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12338"                       
"38"                          ""                            "81"                          "05-JUN-2017"                 "05-JUN-2017"                 "01-JUN-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12336"                       
"38"                          ""                            "81"                          "31-MAY-2017"                 "31-MAY-2017"                 "31-MAY-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12333"                       
"38"                          ""                            "81"                          "28-AUG-2014"                 "28-AUG-2014"                 "28-AUG-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12331"                       
"38"                          ""                            "81"                          "20-JUN-2014"                 "20-JUN-2014"                 "17-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12329"                       
"38"                          ""                            "81"                          "18-JUN-2014"                 "18-JUN-2014"                 "17-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12327"                       
"38"                          ""                            "81"                          "13-JUN-2014"                 "13-JUN-2014"                 "12-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12325"                       
"38"                          ""                            "81"                          "09-JUN-2014"                 "09-JUN-2014"                 "09-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12323"                       
"38"                          ""                            "81"                          "05-JUN-2014"                 "05-JUN-2014"                 "04-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12321"                       
"38"                          ""                            "81"                          "02-JUN-2014"                 "02-JUN-2014"                 "28-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12318"                       
"38"                          ""                            "81"                          "29-MAY-2014"                 "29-MAY-2014"                 "28-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12316"                       
"38"                          ""                            "81"                          "23-MAY-2014"                 "23-MAY-2014"                 "16-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12314"                       
"38"                          ""                            "81"                          "21-MAY-2014"                 "21-MAY-2014"                 "16-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12312"                       
"38"                          ""                            "81"                          "19-MAY-2014"                 "19-MAY-2014"                 "16-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12310"                       
"38"                          ""                            "81"                          "15-MAY-2014"                 "15-MAY-2014"                 "12-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12308"                       
"38"                          ""                            "81"                          "12-MAY-2014"                 "12-MAY-2014"                 "12-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12305"                       
"38"                          ""                            "81"                          "08-MAY-2014"                 "08-MAY-2014"                 "08-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12303"                       
"38"                          ""                            "81"                          "01-MAY-2014"                 "01-MAY-2014"                 "29-APR-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12301"                       
"38"                          ""                            "81"                          "29-APR-2014"                 "29-APR-2014"                 "29-APR-2014"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12299"                       
"38"                          ""                            "81"                          "17-APR-2014"                 "17-APR-2014"                 "17-APR-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12297"                       
"38"                          ""                            "81"                          "26-APR-2013"                 "26-APR-2013"                 "26-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12295"                       
"38"                          ""                            "81"                          "23-APR-2013"                 "23-APR-2013"                 "23-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12293"                       
"38"                          ""                            "81"                          "17-APR-2013"                 "17-APR-2013"                 "17-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12290"                       
"38"                          ""                            "81"                          "15-APR-2013"                 "15-APR-2013"                 "15-APR-2013"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12288"                       
"38"                          ""                            "81"                          "11-APR-2013"                 "11-APR-2013"                 "10-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12286"                       
"38"                          ""                            "81"                          "09-APR-2013"                 "09-APR-2013"                 "08-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12282"                       
"38"                          ""                            "81"                          "05-APR-2013"                 "05-APR-2013"                 "04-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12280"                       
"38"                          ""                            "81"                          "28-MAR-2013"                 "28-MAR-2013"                 "28-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12277"                       
"38"                          ""                            "81"                          "26-MAR-2013"                 "26-MAR-2013"                 "25-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12275"                       
"38"                          ""                            "81"                          "22-MAR-2013"                 "22-MAR-2013"                 "21-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12273"                       
"38"                          ""                            "81"                          "18-MAR-2013"                 "18-MAR-2013"                 "14-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12271"                       
"38"                          ""                            "81"                          "14-MAR-2013"                 "14-MAR-2013"                 "12-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12269"                       
"38"                          ""                            "81"                          "13-MAR-2013"                 "13-MAR-2013"                 "11-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12267"                       
"38"                          ""                            "81"                          "08-MAR-2013"                 "08-MAR-2013"                 "07-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12264"                       
"38"                          ""                            "81"                          "06-MAR-2013"                 "06-MAR-2013"                 "05-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12262"                       
"38"                          ""                            "81"                          "04-MAR-2013"                 "04-MAR-2013"                 "01-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12260"                       
"38"                          ""                            "81"                          "19-JUL-2011"                 "19-JUL-2011"                 "18-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12241"                       
"38"                          ""                            "81"                          "15-JUL-2011"                 "15-JUL-2011"                 "12-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12239"                       
"38"                          ""                            "81"                          "11-JUL-2011"                 "11-JUL-2011"                 "11-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12237"                       
"38"                          ""                            "81"                          "07-JUL-2011"                 "07-JUL-2011"                 "06-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12235"                       
"38"                          ""                            "81"                          "05-JUL-2011"                 "05-JUL-2011"                 "04-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12233"                       
"38"                          ""                            "81"                          "01-JUL-2011"                 "01-JUL-2011"                 "30-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12231"                       
"38"                          ""                            "81"                          "29-JUN-2011"                 "29-JUN-2011"                 "28-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12229"                       
"38"                          ""                            "81"                          "24-JUN-2011"                 "24-JUN-2011"                 "23-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12226"                       
"38"                          ""                            "81"                          "22-JUN-2011"                 "22-JUN-2011"                 "21-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12224"                       
"38"                          ""                            "81"                          "20-JUN-2011"                 "20-JUN-2011"                 "17-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12222"                       
"38"                          ""                            "81"                          "16-JUN-2011"                 "16-JUN-2011"                 "15-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12220"                       
"38"                          ""                            "81"                          "14-JUN-2011"                 "14-JUN-2011"                 "13-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12218"                       
"38"                          ""                            "81"                          "10-JUN-2011"                 "10-JUN-2011"                 "10-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12216"                       
"38"                          ""                            "81"                          "07-JUN-2011"                 "07-JUN-2011"                 "07-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12213"                       
"38"                          ""                            "81"                          "02-JUN-2011"                 "02-JUN-2011"                 "27-MAY-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12211"                       
"38"                          ""                            "81"                          "27-MAY-2011"                 "27-MAY-2011"                 "27-MAY-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12209"                       
"38"                          ""                            "81"                          "26-MAY-2011"                 "26-MAY-2011"                 "25-MAY-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12208"                       
"38"                          ""                            "81"                          "31-AUG-2009"                 "31-AUG-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12204"                       
"38"                          ""                            "81"                          "27-MAY-2009"                 "27-MAY-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12201"                       
"38"                          ""                            "81"                          "29-APR-2009"                 "29-APR-2009"                 "28-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12199"                       
"38"                          ""                            "81"                          "24-APR-2009"                 "24-APR-2009"                 "24-APR-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12197"                       
"38"                          ""                            "81"                          "22-APR-2009"                 "22-APR-2009"                 "22-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12195"                       
"38"                          ""                            "81"                          "20-APR-2009"                 "20-APR-2009"                 "20-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12193"                       
"38"                          ""                            "81"                          "16-APR-2009"                 "16-APR-2009"                 "15-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12191"                       
"38"                          ""                            "81"                          "09-APR-2009"                 "09-APR-2009"                 "09-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12188"                       
"38"                          ""                            "81"                          "07-APR-2009"                 "07-APR-2009"                 "07-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12186"                       
"38"                          ""                            "81"                          "03-APR-2009"                 "03-APR-2009"                 "03-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12184"                       
"38"                          ""                            "81"                          "01-APR-2009"                 "01-APR-2009"                 "31-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12182"                       
"38"                          ""                            "81"                          "31-MAR-2009"                 "31-MAR-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12178"                       
"38"                          ""                            "81"                          "28-MAR-2009"                 "28-MAR-2009"                 "26-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12174"                       
"38"                          ""                            "81"                          "26-MAR-2009"                 "26-MAR-2009"                 "25-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12172"                       
"38"                          ""                            "81"                          "24-MAR-2009"                 "24-MAR-2009"                 "23-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12170"                       
"38"                          ""                            "81"                          "05-SEP-2018"                 "05-SEP-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12624"                       
"38"                          ""                            "81"                          "29-AUG-2018"                 "29-AUG-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12622"                       
"38"                          ""                            "81"                          "25-AUG-2018"                 "25-AUG-2018"                 "21-AUG-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12620"                       
"38"                          ""                            "81"                          "06-AUG-2018"                 "06-AUG-2018"                 "30-AUG-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12617"                       
"38"                          ""                            "81"                          "02-AUG-2018"                 "02-AUG-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12616"                       
"38"                          ""                            "81"                          "30-JUL-2018"                 "30-JUL-2018"                 "27-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12614"                       
"38"                          ""                            "81"                          "26-JUL-2018"                 "26-JUL-2018"                 "24-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12612"                       
"38"                          ""                            "81"                          "24-JUL-2018"                 "24-JUL-2018"                 "23-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12610"                       
"38"                          ""                            "81"                          "20-JUL-2018"                 "20-JUL-2018"                 "16-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12607"                       
"38"                          ""                            "81"                          "19-JUL-2018"                 "19-JUL-2018"                 "18-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12606"                       
"38"                          ""                            "81"                          "18-JUL-2018"                 "18-JUL-2018"                 "16-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12605"                       
"38"                          ""                            "81"                          "17-JUL-2018"                 "17-JUL-2018"                 "16-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12604"                       
"38"                          ""                            "81"                          "16-JUL-2018"                 "16-JUL-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12603"                       
"38"                          ""                            "81"                          "16-JUL-2018"                 "16-JUL-2018"                 "16-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12602"                       
"38"                          ""                            "81"                          "13-JUL-2018"                 "13-JUL-2018"                 "12-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12601"                       
"38"                          ""                            "81"                          "12-JUL-2018"                 "12-JUL-2018"                 "06-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12600"                       
"38"                          ""                            "81"                          "11-JUL-2018"                 "11-JUL-2018"                 "10-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12599"                       
"38"                          ""                            "81"                          "10-JUL-2018"                 "10-JUL-2018"                 "06-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12597"                       
"38"                          ""                            "81"                          "09-JUL-2018"                 "09-JUL-2018"                 "06-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12596"                       
"38"                          ""                            "81"                          "06-JUL-2018"                 "06-JUL-2018"                 "05-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "4"                           "N"                           "12595"                       
"38"                          ""                            "81"                          "05-JUL-2018"                 "05-JUL-2018"                 "03-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12594"                       
"38"                          ""                            "81"                          "04-JUL-2018"                 "04-JUL-2018"                 "03-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12593"                       
"38"                          ""                            "81"                          "03-JUL-2018"                 "03-JUL-2018"                 "29-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12592"                       
"38"                          ""                            "81"                          "02-JUL-2018"                 "02-JUL-2018"                 "29-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12591"                       
"38"                          ""                            "81"                          "29-JUN-2018"                 "29-JUN-2018"                 "26-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12590"                       
"38"                          ""                            "81"                          "28-JUN-2018"                 "28-JUN-2018"                 "22-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12589"                       
"38"                          ""                            "81"                          "27-JUN-2018"                 "27-JUN-2018"                 "22-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12588"                       
"38"                          ""                            "81"                          "23-MAR-2009"                 "23-MAR-2009"                 "23-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12166"                       
"38"                          ""                            "81"                          "18-MAR-2009"                 "18-MAR-2009"                 "18-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12163"                       
"38"                          ""                            "81"                          "13-MAR-2009"                 "13-MAR-2009"                 "12-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12159"                       
"38"                          ""                            "81"                          "11-MAR-2009"                 "11-MAR-2009"                 "10-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12157"                       
"38"                          ""                            "81"                          "09-MAR-2009"                 "09-MAR-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12155"                       
"38"                          ""                            "81"                          "05-MAR-2009"                 "05-MAR-2009"                 "04-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12153"                       
"38"                          ""                            "81"                          "03-MAR-2009"                 "03-MAR-2009"                 "02-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12151"                       
"38"                          ""                            "81"                          "27-FEB-2009"                 "27-FEB-2009"                 "26-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12149"                       
"38"                          ""                            "81"                          "24-FEB-2009"                 "24-FEB-2009"                 "20-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12146"                       
"38"                          ""                            "81"                          "20-FEB-2009"                 "20-FEB-2009"                 "16-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12144"                       
"38"                          ""                            "81"                          "17-FEB-2009"                 "17-FEB-2009"                 "16-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12142"                       
"38"                          ""                            "81"                          "13-FEB-2009"                 "13-FEB-2009"                 "13-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12140"                       
"38"                          ""                            "81"                          "17-JAN-2009"                 "17-JAN-2009"                 "16-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12121"                       
"38"                          ""                            "81"                          "15-JAN-2009"                 "15-JAN-2009"                 "15-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12119"                       
"38"                          ""                            "81"                          "12-JAN-2009"                 "12-JAN-2009"                 "12-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12116"                       
"38"                          ""                            "81"                          "08-JAN-2009"                 "08-JAN-2009"                 "07-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12112"                       
"38"                          ""                            "81"                          "06-JAN-2009"                 "06-JAN-2009"                 "07-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12110"                       
"38"                          ""                            "81"                          "23-DEC-2008"                 "23-DEC-2008"                 "23-DEC-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12108"                       
"38"                          ""                            "81"                          "04-DEC-2008"                 "04-DEC-2008"                 "04-DEC-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12103"                       
"38"                          ""                            "81"                          "30-NOV-2008"                 "30-NOV-2008"                 "28-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12101"                       
"38"                          ""                            "81"                          "01-DEC-2008"                 "01-DEC-2008"                 "28-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12096"                       
"38"                          ""                            "81"                          "23-NOV-2008"                 "23-NOV-2008"                 "21-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12094"                       
"38"                          ""                            "81"                          "21-NOV-2008"                 "21-NOV-2008"                 "20-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12092"                       
"38"                          ""                            "81"                          "27-NOV-2008"                 "27-NOV-2008"                 "26-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12090"                       
"38"                          ""                            "81"                          "24-NOV-2008"                 "24-NOV-2008"                 "20-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12087"                       
"38"                          ""                            "81"                          "19-NOV-2008"                 "19-NOV-2008"                 "18-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12084"                       
"38"                          ""                            "81"                          "17-NOV-2008"                 "17-NOV-2008"                 "14-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12082"                       
"38"                          ""                            "81"                          "13-NOV-2008"                 "13-NOV-2008"                 "12-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12080"                       
"38"                          ""                            "81"                          "10-NOV-2008"                 "10-NOV-2008"                 "07-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12078"                       
"38"                          ""                            "81"                          "06-NOV-2008"                 "06-NOV-2008"                 "05-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12076"                       
"38"                          ""                            "81"                          "04-NOV-2008"                 "04-NOV-2008"                 "03-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12073"                       
"38"                          ""                            "81"                          "03-NOV-2008"                 "03-NOV-2008"                 "03-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12071"                       
"38"                          ""                            "81"                          "29-OCT-2008"                 "29-OCT-2008"                 "29-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12069"                       
"38"                          ""                            "81"                          "27-OCT-2008"                 "27-OCT-2008"                 "24-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12065"                       
"38"                          ""                            "81"                          "23-OCT-2008"                 "23-OCT-2008"                 "23-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12063"                       
"38"                          ""                            "81"                          "17-OCT-2008"                 "17-OCT-2008"                 "16-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12061"                       
"38"                          ""                            "81"                          "14-OCT-2008"                 "14-OCT-2008"                 "13-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12058"                       
"38"                          ""                            "81"                          "26-SEP-2008"                 "26-SEP-2008"                 "26-SEP-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12056"                       
"38"                          ""                            "81"                          "24-SEP-2008"                 "24-SEP-2008"                 "24-SEP-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12054"                       
"38"                          ""                            "81"                          "16-JUL-2008"                 "16-JUL-2008"                 "09-SEP-2008"                 "SUCCESS"                     ""                            "F"                           "4"                           "N"                           "12052"                       
"38"                          ""                            "81"                          "11-JUL-2008"                 "11-JUL-2008"                 "11-JUL-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12050"                       
"38"                          ""                            "81"                          "01-JUL-2008"                 "01-JUL-2008"                 "30-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12048"                       
"38"                          ""                            "81"                          "25-JUN-2008"                 "25-JUN-2008"                 "24-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "7"                           "N"                           "12045"                       
"38"                          ""                            "81"                          "23-JUN-2008"                 "23-JUN-2008"                 "20-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12043"                       
"38"                          ""                            "81"                          "13-JUN-2008"                 "13-JUN-2008"                 "12-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12041"                       
"38"                          ""                            "81"                          "11-JUN-2008"                 "11-JUN-2008"                 "10-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12039"                       
"38"                          ""                            "81"                          "09-JUN-2008"                 "09-JUN-2008"                 "06-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12037"                       
"38"                          ""                            "81"                          "05-JUN-2008"                 "05-JUN-2008"                 "04-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12035"                       
"38"                          ""                            "81"                          "02-JUN-2008"                 "02-JUN-2008"                 "30-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12032"                       
"38"                          ""                            "81"                          "29-MAY-2008"                 "29-MAY-2008"                 "29-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12030"                       
"38"                          ""                            "81"                          "23-MAY-2008"                 "23-MAY-2008"                 "23-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12026"                       
"38"                          ""                            "81"                          "21-MAY-2008"                 "21-MAY-2008"                 "21-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12024"                       
"38"                          ""                            "81"                          "19-MAY-2008"                 "19-MAY-2008"                 "19-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12022"                       
"38"                          ""                            "81"                          "25-JUN-2018"                 "25-JUN-2018"                 "20-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12586"                       
"38"                          ""                            "81"                          "21-JUN-2018"                 "21-JUN-2018"                 "20-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12584"                       
"38"                          ""                            "81"                          "19-JUN-2018"                 "19-JUN-2018"                 "19-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12582"                       
"38"                          ""                            "81"                          "15-JUN-2018"                 "15-JUN-2018"                 "08-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12577"                       
"38"                          ""                            "81"                          "13-JUN-2018"                 "13-JUN-2018"                 "08-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12575"                       
"38"                          ""                            "81"                          "11-JUN-2018"                 "11-JUN-2018"                 "13-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12573"                       
"38"                          ""                            "81"                          "06-JUN-2018"                 "06-JUN-2018"                 "05-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12571"                       
"38"                          ""                            "81"                          "07-JUN-2018"                 "07-JUN-2018"                 "06-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12569"                       
"38"                          ""                            "81"                          "04-JUN-2018"                 "04-JUN-2018"                 "04-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12567"                       
"38"                          ""                            "81"                          "31-MAY-2018"                 "31-MAY-2018"                 "29-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12564"                       
"38"                          ""                            "81"                          "29-MAY-2018"                 "29-MAY-2018"                 "29-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12562"                       
"38"                          ""                            "81"                          "24-MAY-2018"                 "24-MAY-2018"                 "18-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12560"                       
"38"                          ""                            "81"                          "22-MAY-2018"                 "22-MAY-2018"                 "21-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12558"                       
"38"                          ""                            "81"                          "18-MAY-2018"                 "18-MAY-2018"                 "10-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12556"                       
"38"                          ""                            "81"                          "16-MAY-2018"                 "16-MAY-2018"                 "15-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12554"                       
"38"                          ""                            "81"                          "14-MAY-2018"                 "14-MAY-2018"                 "10-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12552"                       
"38"                          ""                            "81"                          "02-MAY-2018"                 "02-MAY-2018"                 "27-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12549"                       
"38"                          ""                            "81"                          "30-APR-2018"                 "30-APR-2018"                 "27-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12547"                       
"38"                          ""                            "81"                          "26-APR-2018"                 "26-APR-2018"                 "25-APR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12545"                       
"38"                          ""                            "81"                          "24-APR-2018"                 "24-APR-2018"                 "20-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12543"                       
"38"                          ""                            "81"                          "20-APR-2018"                 "20-APR-2018"                 "12-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12541"                       
"38"                          ""                            "81"                          "18-APR-2018"                 "18-APR-2018"                 "12-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12539"                       
"38"                          ""                            "81"                          "13-APR-2018"                 "13-APR-2018"                 "10-APR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12536"                       
"38"                          ""                            "81"                          "11-APR-2018"                 "11-APR-2018"                 "05-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12534"                       
"38"                          ""                            "81"                          "09-APR-2018"                 "09-APR-2018"                 "05-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12532"                       
"38"                          ""                            "81"                          "05-APR-2018"                 "05-APR-2018"                 "05-APR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12530"                       
"38"                          ""                            "81"                          "03-APR-2018"                 "03-APR-2018"                 "29-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12528"                       
"38"                          ""                            "81"                          "29-MAR-2018"                 "29-MAR-2018"                 "28-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12526"                       
"38"                          ""                            "81"                          "26-MAR-2018"                 "26-MAR-2018"                 "23-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12523"                       
"38"                          ""                            "81"                          "24-MAR-2018"                 "24-MAR-2018"                 "23-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12521"                       
"38"                          ""                            "81"                          "22-MAR-2018"                 "22-MAR-2018"                 "16-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12519"                       
"38"                          ""                            "81"                          "20-MAR-2018"                 "20-MAR-2018"                 "16-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12517"                       
"38"                          ""                            "81"                          "18-MAR-2018"                 "18-MAR-2018"                 "16-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12515"                       
"38"                          ""                            "81"                          "16-MAR-2018"                 "16-MAR-2018"                 "15-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12513"                       
"38"                          ""                            "81"                          "14-MAR-2018"                 "14-MAR-2018"                 "14-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12511"                       
"38"                          ""                            "81"                          "09-MAR-2018"                 "09-MAR-2018"                 "01-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12508"                       
"38"                          ""                            "81"                          "07-MAR-2018"                 "07-MAR-2018"                 "06-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12506"                       
"38"                          ""                            "81"                          "05-MAR-2018"                 "05-MAR-2018"                 "01-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12504"                       
"38"                          ""                            "81"                          "03-MAR-2018"                 "03-MAR-2018"                 "27-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12502"                       
"38"                          ""                            "81"                          "10-MAR-2018"                 "10-MAR-2018"                 "27-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12500"                       
"38"                          ""                            "81"                          "01-MAR-2018"                 "01-MAR-2018"                 "26-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12498"                       
"38"                          ""                            "81"                          "26-FEB-2018"                 "26-FEB-2018"                 "26-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12495"                       
"38"                          ""                            "81"                          "23-FEB-2018"                 "23-FEB-2018"                 "16-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12493"                       
"38"                          ""                            "81"                          "21-FEB-2018"                 "21-FEB-2018"                 "20-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12491"                       
"38"                          ""                            "81"                          "21-AUG-2018"                 "21-AUG-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12619"                       
"38"                          ""                            "81"                          "07-AUG-2018"                 "07-AUG-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12618"                       
"38"                          ""                            "81"                          "31-JUL-2018"                 "31-JUL-2018"                 "31-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12615"                       
"38"                          ""                            "81"                          "27-JUL-2018"                 "27-JUL-2018"                 "23-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12613"                       
"38"                          ""                            "81"                          "25-JUL-2018"                 "25-JUL-2018"                 "23-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12611"                       
"38"                          ""                            "81"                          "23-JUL-2018"                 "23-JUL-2018"                 "17-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12608"                       
"38"                          ""                            "81"                          "01-FEB-2018"                 "01-FEB-2018"                 "30-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12471"                       
"38"                          ""                            "81"                          "30-JAN-2018"                 "30-JAN-2018"                 "30-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12469"                       
"38"                          ""                            "81"                          "29-JAN-2018"                 "29-JAN-2018"                 "23-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12467"                       
"38"                          ""                            "81"                          "24-JAN-2018"                 "24-JAN-2018"                 "23-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12465"                       
"38"                          ""                            "81"                          "22-JAN-2018"                 "22-JAN-2018"                 "19-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12463"                       
"38"                          ""                            "81"                          "07-MAY-2018"                 "07-MAY-2018"                 "07-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12461"                       
"38"                          ""                            "81"                          "17-JAN-2018"                 "17-JAN-2018"                 "16-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12458"                       
"38"                          ""                            "81"                          "15-JAN-2018"                 "15-JAN-2018"                 "12-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12456"                       
"38"                          ""                            "81"                          "11-JAN-2018"                 "11-JAN-2018"                 "04-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12454"                       
"38"                          ""                            "81"                          "09-JAN-2018"                 "09-JAN-2018"                 "04-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12452"                       
"38"                          ""                            "81"                          "05-JAN-2018"                 "05-JAN-2018"                 "03-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12450"                       
"38"                          ""                            "81"                          "03-JAN-2018"                 "03-JAN-2018"                 "03-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12448"                       
"38"                          ""                            "81"                          "29-DEC-2017"                 "29-DEC-2017"                 "22-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12445"                       
"38"                          ""                            "81"                          "27-DEC-2017"                 "27-DEC-2017"                 "22-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12443"                       
"38"                          ""                            "81"                          "25-DEC-2017"                 "25-DEC-2017"                 "22-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12441"                       
"38"                          ""                            "81"                          "21-DEC-2017"                 "21-DEC-2017"                 "15-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12439"                       
"38"                          ""                            "81"                          "19-DEC-2017"                 "19-DEC-2017"                 "14-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12437"                       
"38"                          ""                            "81"                          "15-DEC-2017"                 "15-DEC-2017"                 "12-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12433"                       
"38"                          ""                            "81"                          "12-DEC-2017"                 "12-DEC-2017"                 "12-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12430"                       
"38"                          ""                            "81"                          "08-DEC-2017"                 "08-DEC-2017"                 "04-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12428"                       
"38"                          ""                            "81"                          "06-DEC-2017"                 "06-DEC-2017"                 "04-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12426"                       
"38"                          ""                            "81"                          "04-DEC-2017"                 "04-DEC-2017"                 "01-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12424"                       
"38"                          ""                            "81"                          "30-NOV-2017"                 "30-NOV-2017"                 "29-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12422"                       
"38"                          ""                            "81"                          "28-NOV-2017"                 "28-NOV-2017"                 "27-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12420"                       
"38"                          ""                            "81"                          "23-NOV-2017"                 "23-NOV-2017"                 "21-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12417"                       
"38"                          ""                            "81"                          "21-NOV-2017"                 "21-NOV-2017"                 "17-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12415"                       
"38"                          ""                            "81"                          "17-NOV-2017"                 "17-NOV-2017"                 "16-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12413"                       
"38"                          ""                            "81"                          "15-NOV-2017"                 "15-NOV-2017"                 "13-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12411"                       
"38"                          ""                            "81"                          "13-NOV-2017"                 "13-NOV-2017"                 "09-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12409"                       
"38"                          ""                            "81"                          "09-NOV-2017"                 "09-NOV-2017"                 "09-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12407"                       
"38"                          ""                            "81"                          "06-NOV-2017"                 "06-NOV-2017"                 "03-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12404"                       
"38"                          ""                            "81"                          "02-NOV-2017"                 "02-NOV-2017"                 "30-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12402"                       
"38"                          ""                            "81"                          "31-OCT-2017"                 "31-OCT-2017"                 "30-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12400"                       
"38"                          ""                            "81"                          "27-OCT-2017"                 "27-OCT-2017"                 "20-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12398"                       
"38"                          ""                            "81"                          "25-OCT-2017"                 "25-OCT-2017"                 "20-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12396"                       
"38"                          ""                            "81"                          "23-OCT-2017"                 "23-OCT-2017"                 "20-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12394"                       
"38"                          ""                            "81"                          "19-OCT-2017"                 "19-OCT-2017"                 "17-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12392"                       
"38"                          ""                            "81"                          "02-FEB-2018"                 "02-FEB-2018"                 "30-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12472"                       
"38"                          ""                            "81"                          "31-JAN-2018"                 "31-JAN-2018"                 "30-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12470"                       
"38"                          ""                            "81"                          "26-JAN-2018"                 "26-JAN-2018"                 "24-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12468"                       
"38"                          ""                            "81"                          "25-JAN-2018"                 "25-JAN-2018"                 "23-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12466"                       
"38"                          ""                            "81"                          "23-JAN-2018"                 "23-JAN-2018"                 "21-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12464"                       
"38"                          ""                            "81"                          "19-JAN-2018"                 "19-JAN-2018"                 "16-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12460"                       
"38"                          ""                            "81"                          "18-JAN-2018"                 "18-JAN-2018"                 "16-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12459"                       
"38"                          ""                            "81"                          "16-JAN-2018"                 "16-JAN-2018"                 "15-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12457"                       
"38"                          ""                            "81"                          "12-JAN-2018"                 "12-JAN-2018"                 "04-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12455"                       
"38"                          ""                            "81"                          "10-JAN-2018"                 "10-JAN-2018"                 "04-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12453"                       
"38"                          ""                            "81"                          "08-JAN-2018"                 "08-JAN-2018"                 "04-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12451"                       
"38"                          ""                            "81"                          "04-JAN-2018"                 "04-JAN-2018"                 "03-JAN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12449"                       
"38"                          ""                            "81"                          "02-JAN-2018"                 "02-JAN-2018"                 "29-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12447"                       
"38"                          ""                            "81"                          "01-JAN-2018"                 "01-JAN-2018"                 "29-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12446"                       
"38"                          ""                            "81"                          "28-DEC-2017"                 "28-DEC-2017"                 "22-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12444"                       
"38"                          ""                            "81"                          "26-DEC-2017"                 "26-DEC-2017"                 "22-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12442"                       
"38"                          ""                            "81"                          "22-DEC-2017"                 "22-DEC-2017"                 "15-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12440"                       
"38"                          ""                            "81"                          "20-DEC-2017"                 "20-DEC-2017"                 "15-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12438"                       
"38"                          ""                            "81"                          "18-DEC-2017"                 "18-DEC-2017"                 "14-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12436"                       
"38"                          ""                            "81"                          "14-DEC-2017"                 "14-DEC-2017"                 "14-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12432"                       
"38"                          ""                            "81"                          "13-DEC-2017"                 "13-DEC-2017"                 "12-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12431"                       
"38"                          ""                            "81"                          "11-DEC-2017"                 "11-DEC-2017"                 "07-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12429"                       
"38"                          ""                            "81"                          "07-DEC-2017"                 "07-DEC-2017"                 "04-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12427"                       
"38"                          ""                            "81"                          "05-DEC-2017"                 "05-DEC-2017"                 "04-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12425"                       
"38"                          ""                            "81"                          "01-DEC-2017"                 "01-DEC-2017"                 "04-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12423"                       
"38"                          ""                            "81"                          "29-NOV-2017"                 "29-NOV-2017"                 "28-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12421"                       
"38"                          ""                            "81"                          "27-NOV-2017"                 "27-NOV-2017"                 "23-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12419"                       
"38"                          ""                            "81"                          "24-NOV-2017"                 "24-NOV-2017"                 "23-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12418"                       
"38"                          ""                            "81"                          "22-NOV-2017"                 "22-NOV-2017"                 "21-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12416"                       
"38"                          ""                            "81"                          "20-NOV-2017"                 "20-NOV-2017"                 "17-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12414"                       
"38"                          ""                            "81"                          "16-NOV-2017"                 "16-NOV-2017"                 "13-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12412"                       
"38"                          ""                            "81"                          "14-NOV-2017"                 "14-NOV-2017"                 "13-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12410"                       
"38"                          ""                            "81"                          "10-NOV-2017"                 "10-NOV-2017"                 "03-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12408"                       
"38"                          ""                            "81"                          "08-NOV-2017"                 "08-NOV-2017"                 "03-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12406"                       
"38"                          ""                            "81"                          "07-NOV-2017"                 "07-NOV-2017"                 "03-NOV-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12405"                       
"38"                          ""                            "81"                          "03-NOV-2017"                 "03-NOV-2017"                 "30-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12403"                       
"38"                          ""                            "81"                          "01-NOV-2017"                 "01-NOV-2017"                 "30-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12401"                       
"38"                          ""                            "81"                          "30-OCT-2017"                 "30-OCT-2017"                 "30-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12399"                       
"38"                          ""                            "81"                          "26-OCT-2017"                 "26-OCT-2017"                 "20-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12397"                       
"38"                          ""                            "81"                          "24-OCT-2017"                 "24-OCT-2017"                 "20-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12395"                       
"38"                          ""                            "81"                          "20-OCT-2017"                 "20-OCT-2017"                 "17-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12393"                       
"38"                          ""                            "81"                          "18-OCT-2017"                 "18-OCT-2017"                 "17-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12391"                       
"38"                          ""                            "81"                          "17-OCT-2017"                 "17-OCT-2017"                 "13-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12390"                       
"38"                          ""                            "81"                          "13-OCT-2017"                 "13-OCT-2017"                 "13-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12388"                       
"38"                          ""                            "81"                          "11-OCT-2017"                 "11-OCT-2017"                 "10-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12386"                       
"38"                          ""                            "81"                          "09-OCT-2017"                 "09-OCT-2017"                 "09-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12384"                       
"38"                          ""                            "81"                          "05-OCT-2017"                 "05-OCT-2017"                 "03-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12382"                       
"38"                          ""                            "81"                          "03-OCT-2017"                 "03-OCT-2017"                 "02-OCT-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12380"                       
"38"                          ""                            "81"                          "29-SEP-2017"                 "29-SEP-2017"                 "25-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12378"                       
"38"                          ""                            "81"                          "28-SEP-2017"                 "28-SEP-2017"                 "25-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12377"                       
"38"                          ""                            "81"                          "26-SEP-2017"                 "26-SEP-2017"                 "25-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12375"                       
"38"                          ""                            "81"                          "25-SEP-2017"                 "25-SEP-2017"                 "20-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12374"                       
"38"                          ""                            "81"                          "22-SEP-2017"                 "22-SEP-2017"                 "20-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12373"                       
"38"                          ""                            "81"                          "21-SEP-2017"                 "21-SEP-2017"                 "20-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12372"                       
"38"                          ""                            "81"                          "20-SEP-2017"                 "20-SEP-2017"                 "18-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12371"                       
"38"                          ""                            "81"                          "19-SEP-2017"                 "19-SEP-2017"                 "18-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12370"                       
"38"                          ""                            "81"                          "18-SEP-2017"                 "18-SEP-2017"                 "15-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12369"                       
"38"                          ""                            "81"                          "15-SEP-2017"                 "15-SEP-2017"                 "14-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12368"                       
"38"                          ""                            "81"                          "14-SEP-2017"                 "14-SEP-2017"                 "14-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12367"                       
"38"                          ""                            "81"                          "13-SEP-2017"                 "13-SEP-2017"                 "08-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12366"                       
"38"                          ""                            "81"                          "12-SEP-2017"                 "12-SEP-2017"                 "08-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12365"                       
"38"                          ""                            "81"                          "11-SEP-2017"                 "11-SEP-2017"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12364"                       
"38"                          ""                            "81"                          "08-SEP-2017"                 "08-SEP-2017"                 "01-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12363"                       
"38"                          ""                            "81"                          "07-SEP-2017"                 "07-SEP-2017"                 "01-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12362"                       
"38"                          ""                            "81"                          "06-SEP-2017"                 "06-SEP-2017"                 "01-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12361"                       
"38"                          ""                            "81"                          "05-SEP-2017"                 "05-SEP-2017"                 "01-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12360"                       
"38"                          ""                            "81"                          "04-SEP-2017"                 "04-SEP-2017"                 "01-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12359"                       
"38"                          ""                            "81"                          "01-SEP-2017"                 "01-SEP-2017"                 "01-SEP-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12358"                       
"38"                          ""                            "81"                          "30-AUG-2017"                 "30-AUG-2017"                 "29-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12356"                       
"38"                          ""                            "81"                          "24-AUG-2017"                 "24-AUG-2017"                 "23-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12354"                       
"38"                          ""                            "81"                          "22-AUG-2017"                 "22-AUG-2017"                 "21-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12352"                       
"38"                          ""                            "81"                          "04-AUG-2017"                 "04-AUG-2017"                 "03-AUG-2017"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12350"                       
"38"                          ""                            "81"                          "25-JUL-2017"                 "25-JUL-2017"                 "20-JUL-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12348"                       
"38"                          ""                            "81"                          "24-JUL-2017"                 "24-JUL-2017"                 "20-JUL-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12347"                       
"38"                          ""                            "81"                          "20-JUL-2017"                 "20-JUL-2017"                 "19-JUL-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12345"                       
"38"                          ""                            "81"                          "11-JUL-2017"                 "11-JUL-2017"                 "06-JUL-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12343"                       
"38"                          ""                            "81"                          "07-JUL-2017"                 "07-JUL-2017"                 "06-JUL-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12341"                       
"38"                          ""                            "81"                          "27-JUN-2017"                 "27-JUN-2017"                 "23-JUN-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12339"                       
"38"                          ""                            "81"                          "06-JUN-2017"                 "06-JUN-2017"                 "05-JUN-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12337"                       
"38"                          ""                            "81"                          "02-JUN-2017"                 "02-JUN-2017"                 "01-JUN-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12335"                       
"38"                          ""                            "81"                          "01-JUN-2017"                 "01-JUN-2017"                 "31-MAY-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12334"                       
"38"                          ""                            "81"                          "23-MAY-2017"                 "23-MAY-2017"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12332"                       
"38"                          ""                            "81"                          "04-AUG-2014"                 "04-AUG-2014"                 "04-AUG-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12330"                       
"38"                          ""                            "81"                          "19-JUN-2014"                 "19-JUN-2014"                 "17-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12328"                       
"38"                          ""                            "81"                          "17-JUN-2014"                 "17-JUN-2014"                 "17-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12326"                       
"38"                          ""                            "81"                          "12-JUN-2014"                 "12-JUN-2014"                 "12-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12324"                       
"38"                          ""                            "81"                          "06-JUN-2014"                 "06-JUN-2014"                 "04-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12322"                       
"38"                          ""                            "81"                          "04-JUN-2014"                 "04-JUN-2014"                 "04-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12320"                       
"38"                          ""                            "81"                          "03-JUN-2014"                 "03-JUN-2014"                 "03-JUN-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12319"                       
"38"                          ""                            "81"                          "30-MAY-2014"                 "30-MAY-2014"                 "28-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12317"                       
"38"                          ""                            "81"                          "28-MAY-2014"                 "28-MAY-2014"                 "28-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "4"                           "N"                           "12315"                       
"38"                          ""                            "81"                          "22-MAY-2014"                 "22-MAY-2014"                 "16-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12313"                       
"38"                          ""                            "81"                          "20-MAY-2014"                 "20-MAY-2014"                 "16-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12311"                       
"38"                          ""                            "81"                          "16-MAY-2014"                 "16-MAY-2014"                 "12-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12309"                       
"38"                          ""                            "81"                          "14-MAY-2014"                 "14-MAY-2014"                 "12-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12307"                       
"38"                          ""                            "81"                          "13-MAY-2014"                 "13-MAY-2014"                 "12-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12306"                       
"38"                          ""                            "81"                          "09-MAY-2014"                 "09-MAY-2014"                 "09-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12304"                       
"38"                          ""                            "81"                          "06-MAY-2014"                 "06-MAY-2014"                 "06-MAY-2014"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12302"                       
"38"                          ""                            "81"                          "30-APR-2014"                 "30-APR-2014"                 "28-APR-2014"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12300"                       
"38"                          ""                            "81"                          "28-APR-2014"                 "28-APR-2014"                 "28-APR-2014"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12298"                       
"38"                          ""                            "81"                          "02-MAY-2013"                 "02-MAY-2013"                 "02-MAY-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12296"                       
"38"                          ""                            "81"                          "24-APR-2013"                 "24-APR-2013"                 "23-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12294"                       
"38"                          ""                            "81"                          "22-APR-2013"                 "22-APR-2013"                 "22-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12292"                       
"38"                          ""                            "81"                          "18-APR-2013"                 "18-APR-2013"                 "18-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12291"                       
"38"                          ""                            "81"                          "16-APR-2013"                 "16-APR-2013"                 "15-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12289"                       
"38"                          ""                            "81"                          "12-APR-2013"                 "12-APR-2013"                 "12-APR-2013"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12287"                       
"38"                          ""                            "81"                          "10-APR-2013"                 "10-APR-2013"                 "09-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12285"                       
"38"                          ""                            "81"                          "08-APR-2013"                 "08-APR-2013"                 "05-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12281"                       
"38"                          ""                            "81"                          "04-APR-2013"                 "04-APR-2013"                 "04-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12279"                       
"38"                          ""                            "81"                          "03-APR-2013"                 "03-APR-2013"                 "03-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12278"                       
"38"                          ""                            "81"                          "27-MAR-2013"                 "27-MAR-2013"                 "26-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12276"                       
"38"                          ""                            "81"                          "25-MAR-2013"                 "25-MAR-2013"                 "25-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12274"                       
"38"                          ""                            "81"                          "21-MAR-2013"                 "21-MAR-2013"                 "21-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12272"                       
"38"                          ""                            "81"                          "15-MAR-2013"                 "15-MAR-2013"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12270"                       
"38"                          ""                            "81"                          "11-MAR-2013"                 "11-MAR-2013"                 "08-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12265"                       
"38"                          ""                            "81"                          "07-MAR-2013"                 "07-MAR-2013"                 "06-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12263"                       
"38"                          ""                            "81"                          "05-MAR-2013"                 "05-MAR-2013"                 "04-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12261"                       
"38"                          ""                            "81"                          "01-MAR-2013"                 "01-MAR-2013"                 "01-MAR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12259"                       
"38"                          ""                            "81"                          "21-NOV-2011"                 "21-NOV-2011"                 "18-NOV-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12258"                       
"38"                          ""                            "81"                          "18-NOV-2011"                 "18-NOV-2011"                 "17-NOV-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12257"                       
"38"                          ""                            "81"                          "17-NOV-2011"                 "17-NOV-2011"                 "16-NOV-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12256"                       
"38"                          ""                            "81"                          "16-NOV-2011"                 "16-NOV-2011"                 "15-NOV-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12255"                       
"38"                          ""                            "81"                          "16-SEP-2011"                 "16-SEP-2011"                 "16-SEP-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12254"                       
"38"                          ""                            "81"                          "26-AUG-2011"                 "26-AUG-2011"                 "26-AUG-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12253"                       
"38"                          ""                            "81"                          "16-AUG-2011"                 "16-AUG-2011"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12252"                       
"38"                          ""                            "81"                          "05-AUG-2011"                 "05-AUG-2011"                 "29-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12251"                       
"38"                          ""                            "81"                          "04-AUG-2011"                 "04-AUG-2011"                 "29-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12250"                       
"38"                          ""                            "81"                          "03-AUG-2011"                 "03-AUG-2011"                 "29-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12249"                       
"38"                          ""                            "81"                          "02-AUG-2011"                 "02-AUG-2011"                 "29-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12248"                       
"38"                          ""                            "81"                          "01-AUG-2011"                 "01-AUG-2011"                 "29-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12247"                       
"38"                          ""                            "81"                          "27-JUL-2011"                 "27-JUL-2011"                 "26-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12246"                       
"38"                          ""                            "81"                          "26-JUL-2011"                 "26-JUL-2011"                 ""                            ""                            ""                            "D"                           "3"                           "N"                           "12245"                       
"38"                          ""                            "81"                          "25-JUL-2011"                 "25-JUL-2011"                 "22-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12244"                       
"38"                          ""                            "81"                          "22-JUL-2011"                 "22-JUL-2011"                 "21-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12243"                       
"38"                          ""                            "81"                          "20-JUL-2011"                 "20-JUL-2011"                 "20-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12242"                       
"38"                          ""                            "81"                          "18-JUL-2011"                 "18-JUL-2011"                 "15-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12240"                       
"38"                          ""                            "81"                          "12-JUL-2011"                 "12-JUL-2011"                 "11-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12238"                       
"38"                          ""                            "81"                          "08-JUL-2011"                 "08-JUL-2011"                 "08-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12236"                       
"38"                          ""                            "81"                          "06-JUL-2011"                 "06-JUL-2011"                 "05-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12234"                       
"38"                          ""                            "81"                          "04-JUL-2011"                 "04-JUL-2011"                 "01-JUL-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12232"                       
"38"                          ""                            "81"                          "30-JUN-2011"                 "30-JUN-2011"                 "30-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12230"                       
"38"                          ""                            "81"                          "28-JUN-2011"                 "28-JUN-2011"                 "27-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12228"                       
"38"                          ""                            "81"                          "27-JUN-2011"                 "27-JUN-2011"                 "26-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12227"                       
"38"                          ""                            "81"                          "23-JUN-2011"                 "23-JUN-2011"                 "23-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12225"                       
"38"                          ""                            "81"                          "21-JUN-2011"                 "21-JUN-2011"                 "20-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12223"                       
"38"                          ""                            "81"                          "17-JUN-2011"                 "17-JUN-2011"                 "16-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12221"                       
"38"                          ""                            "81"                          "15-JUN-2011"                 "15-JUN-2011"                 "14-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12219"                       
"38"                          ""                            "81"                          "13-JUN-2011"                 "13-JUN-2011"                 "10-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12217"                       
"38"                          ""                            "81"                          "09-JUN-2011"                 "09-JUN-2011"                 "09-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12215"                       
"38"                          ""                            "81"                          "08-JUN-2011"                 "08-JUN-2011"                 "07-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12214"                       
"38"                          ""                            "81"                          "06-JUN-2011"                 "06-JUN-2011"                 "06-JUN-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12212"                       
"38"                          ""                            "81"                          "31-MAY-2011"                 "31-MAY-2011"                 "27-MAY-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12210"                       
"38"                          ""                            "81"                          "25-MAY-2011"                 "25-MAY-2011"                 "25-MAY-2011"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12207"                       
"38"                          ""                            "81"                          "22-OCT-2009"                 "22-OCT-2009"                 "22-OCT-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12205"                       
"38"                          ""                            "81"                          "28-APR-2009"                 "28-APR-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12198"                       
"38"                          ""                            "81"                          "23-APR-2009"                 "23-APR-2009"                 "23-APR-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12196"                       
"38"                          ""                            "81"                          "21-APR-2009"                 "21-APR-2009"                 "21-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12194"                       
"38"                          ""                            "81"                          "17-APR-2009"                 "17-APR-2009"                 "17-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12192"                       
"38"                          ""                            "81"                          "15-APR-2009"                 "15-APR-2009"                 "15-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12190"                       
"38"                          ""                            "81"                          "14-APR-2009"                 "14-APR-2009"                 "14-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12189"                       
"38"                          ""                            "81"                          "08-APR-2009"                 "08-APR-2009"                 "07-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12187"                       
"38"                          ""                            "81"                          "06-APR-2009"                 "06-APR-2009"                 "03-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12185"                       
"38"                          ""                            "81"                          "02-APR-2009"                 "02-APR-2009"                 "01-APR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12183"                       
"38"                          ""                            "81"                          "30-MAR-2009"                 "30-MAR-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12177"                       
"38"                          ""                            "81"                          "29-MAR-2009"                 "29-MAR-2009"                 "26-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12175"                       
"38"                          ""                            "81"                          "27-MAR-2009"                 "27-MAR-2009"                 "26-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12173"                       
"38"                          ""                            "81"                          "25-MAR-2009"                 "25-MAR-2009"                 "24-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12171"                       
"38"                          ""                            "81"                          "21-MAR-2009"                 "21-MAR-2009"                 "20-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12167"                       
"38"                          ""                            "81"                          "20-MAR-2009"                 "20-MAR-2009"                 "19-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12165"                       
"38"                          ""                            "81"                          "19-MAR-2009"                 "19-MAR-2009"                 "18-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12164"                       
"38"                          ""                            "81"                          "17-MAR-2009"                 "17-MAR-2009"                 "17-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12162"                       
"38"                          ""                            "81"                          "12-MAR-2009"                 "12-MAR-2009"                 "11-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "4"                           "N"                           "12158"                       
"38"                          ""                            "81"                          "10-MAR-2009"                 "10-MAR-2009"                 "09-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12156"                       
"38"                          ""                            "81"                          "06-MAR-2009"                 "06-MAR-2009"                 "05-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12154"                       
"38"                          ""                            "81"                          "04-MAR-2009"                 "04-MAR-2009"                 "03-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12152"                       
"38"                          ""                            "81"                          "02-MAR-2009"                 "02-MAR-2009"                 "27-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12150"                       
"38"                          ""                            "81"                          "26-FEB-2009"                 "26-FEB-2009"                 "25-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12148"                       
"38"                          ""                            "81"                          "25-FEB-2009"                 "25-FEB-2009"                 "25-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12147"                       
"38"                          ""                            "81"                          "23-FEB-2009"                 "23-FEB-2009"                 "16-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12145"                       
"38"                          ""                            "81"                          "18-FEB-2009"                 "18-FEB-2009"                 "16-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12143"                       
"38"                          ""                            "81"                          "16-FEB-2009"                 "16-FEB-2009"                 "16-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12141"                       
"38"                          ""                            "81"                          "12-FEB-2009"                 "12-FEB-2009"                 "12-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12139"                       
"38"                          ""                            "81"                          "11-FEB-2009"                 "11-FEB-2009"                 "11-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12138"                       
"38"                          ""                            "81"                          "10-FEB-2009"                 "10-FEB-2009"                 "10-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12137"                       
"38"                          ""                            "81"                          "06-FEB-2009"                 "06-FEB-2009"                 "05-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12135"                       
"38"                          ""                            "81"                          "05-FEB-2009"                 "05-FEB-2009"                 "05-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12134"                       
"38"                          ""                            "81"                          "04-FEB-2009"                 "04-FEB-2009"                 "04-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12133"                       
"38"                          ""                            "81"                          "31-JAN-2009"                 "31-JAN-2009"                 "30-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12132"                       
"38"                          ""                            "81"                          "28-JAN-2009"                 "28-JAN-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12131"                       
"38"                          ""                            "81"                          "27-JAN-2009"                 "27-JAN-2009"                 "26-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12130"                       
"38"                          ""                            "81"                          "26-JAN-2009"                 "26-JAN-2009"                 "25-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12129"                       
"38"                          ""                            "81"                          "25-JAN-2009"                 "25-JAN-2009"                 "25-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12128"                       
"38"                          ""                            "81"                          "23-JAN-2009"                 "23-JAN-2009"                 "23-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12127"                       
"38"                          ""                            "81"                          "22-JAN-2009"                 "22-JAN-2009"                 "21-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12126"                       
"38"                          ""                            "81"                          "21-JAN-2009"                 "21-JAN-2009"                 "20-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12125"                       
"38"                          ""                            "81"                          "20-JAN-2009"                 "20-JAN-2009"                 "19-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12124"                       
"38"                          ""                            "81"                          "19-FEB-2009"                 "19-FEB-2009"                 "16-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12123"                       
"38"                          ""                            "81"                          "19-JAN-2009"                 "19-JAN-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12122"                       
"38"                          ""                            "81"                          "16-JAN-2009"                 "16-JAN-2009"                 "15-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12120"                       
"38"                          ""                            "81"                          "14-JAN-2009"                 "14-JAN-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12118"                       
"38"                          ""                            "81"                          "13-JAN-2009"                 "13-JAN-2009"                 "15-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12117"                       
"38"                          ""                            "81"                          "09-JAN-2009"                 "09-JAN-2009"                 "07-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12113"                       
"38"                          ""                            "81"                          "07-JAN-2009"                 "07-JAN-2009"                 "07-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12111"                       
"38"                          ""                            "81"                          "24-DEC-2008"                 "24-DEC-2008"                 "24-DEC-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12109"                       
"38"                          ""                            "81"                          "08-DEC-2008"                 "08-DEC-2008"                 "08-DEC-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12107"                       
"38"                          ""                            "81"                          "05-DEC-2008"                 "05-DEC-2008"                 "06-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12104"                       
"38"                          ""                            "81"                          "03-DEC-2008"                 "03-DEC-2008"                 "13-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "4"                           "N"                           "12102"                       
"38"                          ""                            "81"                          "02-DEC-2008"                 "02-DEC-2008"                 "02-DEC-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12100"                       
"38"                          ""                            "81"                          "29-NOV-2008"                 "29-NOV-2008"                 "28-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12095"                       
"38"                          ""                            "81"                          "22-NOV-2008"                 "22-NOV-2008"                 "21-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12093"                       
"38"                          ""                            "81"                          "28-NOV-2008"                 "28-NOV-2008"                 "20-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12091"                       
"38"                          ""                            "81"                          "26-NOV-2008"                 "26-NOV-2008"                 "25-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12089"                       
"38"                          ""                            "81"                          "25-NOV-2008"                 "25-NOV-2008"                 "20-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12088"                       
"38"                          ""                            "81"                          "20-NOV-2008"                 "20-NOV-2008"                 "19-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12085"                       
"38"                          ""                            "81"                          "18-NOV-2008"                 "18-NOV-2008"                 "17-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12083"                       
"38"                          ""                            "81"                          "14-NOV-2008"                 "14-NOV-2008"                 "14-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12081"                       
"38"                          ""                            "81"                          "12-NOV-2008"                 "12-NOV-2008"                 "11-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12079"                       
"38"                          ""                            "81"                          "07-NOV-2008"                 "07-NOV-2008"                 "06-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12077"                       
"38"                          ""                            "81"                          "05-NOV-2008"                 "05-NOV-2008"                 "04-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12074"                       
"38"                          ""                            "81"                          "31-OCT-2008"                 "31-OCT-2008"                 "31-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12070"                       
"38"                          ""                            "81"                          "28-OCT-2008"                 "28-OCT-2008"                 "28-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12068"                       
"38"                          ""                            "81"                          "24-OCT-2008"                 "24-OCT-2008"                 "24-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12064"                       
"38"                          ""                            "81"                          "20-OCT-2008"                 "20-OCT-2008"                 "17-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12062"                       
"38"                          ""                            "81"                          "16-OCT-2008"                 "16-OCT-2008"                 "16-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12060"                       
"38"                          ""                            "81"                          "15-OCT-2008"                 "15-OCT-2008"                 "14-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12059"                       
"38"                          ""                            "81"                          "13-OCT-2008"                 "13-OCT-2008"                 "13-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12057"                       
"38"                          ""                            "81"                          "25-SEP-2008"                 "25-SEP-2008"                 "24-SEP-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12055"                       
"38"                          ""                            "81"                          "09-SEP-2008"                 "09-SEP-2008"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12053"                       
"38"                          ""                            "81"                          "14-JUL-2008"                 "14-JUL-2008"                 "11-JUL-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12051"                       
"38"                          ""                            "81"                          "03-JUL-2008"                 "03-JUL-2008"                 "03-JUL-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12049"                       
"38"                          ""                            "81"                          "30-JUN-2008"                 "30-JUN-2008"                 "30-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12047"                       
"38"                          ""                            "81"                          "26-JUN-2008"                 "26-JUN-2008"                 "25-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12046"                       
"38"                          ""                            "81"                          "24-JUN-2008"                 "24-JUN-2008"                 "23-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12044"                       
"38"                          ""                            "81"                          "16-JUN-2008"                 "16-JUN-2008"                 "13-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12042"                       
"38"                          ""                            "81"                          "12-JUN-2008"                 "12-JUN-2008"                 "11-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12040"                       
"38"                          ""                            "81"                          "10-JUN-2008"                 "10-JUN-2008"                 "09-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12038"                       
"38"                          ""                            "81"                          "06-JUN-2008"                 "06-JUN-2008"                 "05-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12036"                       
"38"                          ""                            "81"                          "04-JUN-2008"                 "04-JUN-2008"                 "03-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12034"                       
"38"                          ""                            "81"                          "03-JUN-2008"                 "03-JUN-2008"                 "03-JUN-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12033"                       
"38"                          ""                            "81"                          "30-MAY-2008"                 "30-MAY-2008"                 "29-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12031"                       
"38"                          ""                            "81"                          "27-MAY-2008"                 "27-MAY-2008"                 "23-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12027"                       
"38"                          ""                            "81"                          "22-MAY-2008"                 "22-MAY-2008"                 "21-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12025"                       
"38"                          ""                            "81"                          "20-MAY-2008"                 "20-MAY-2008"                 "19-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12023"                       
"38"                          ""                            "81"                          "16-MAY-2008"                 "16-MAY-2008"                 "15-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12021"                       
"38"                          ""                            "81"                          "15-MAY-2008"                 "15-MAY-2008"                 "14-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12020"                       
"38"                          ""                            "81"                          "14-MAY-2008"                 "14-MAY-2008"                 "13-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12019"                       
"38"                          ""                            "81"                          "13-MAY-2008"                 "13-MAY-2008"                 "12-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12018"                       
"38"                          ""                            "81"                          "12-MAY-2008"                 "12-MAY-2008"                 "12-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12017"                       
"38"                          ""                            "81"                          "10-MAY-2008"                 "10-MAY-2008"                 "09-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12016"                       
"38"                          ""                            "81"                          "09-MAY-2008"                 "09-MAY-2008"                 "09-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12015"                       
"38"                          ""                            "81"                          "08-MAY-2008"                 "08-MAY-2008"                 "08-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12014"                       
"38"                          ""                            "81"                          "07-MAY-2008"                 "07-MAY-2008"                 "07-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12013"                       
"38"                          ""                            "81"                          "02-MAY-2008"                 "02-MAY-2008"                 "02-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12012"                       
"38"                          ""                            "81"                          "06-MAY-2008"                 "06-MAY-2008"                 "02-MAY-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12011"                       
"38"                          ""                            "81"                          "10-APR-2008"                 "10-APR-2008"                 "08-APR-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12010"                       
"38"                          ""                            "81"                          "09-APR-2008"                 "09-APR-2008"                 "08-APR-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12009"                       
"38"                          ""                            "81"                          "08-APR-2008"                 "08-APR-2008"                 "08-APR-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12008"                       
"38"                          ""                            "81"                          "03-APR-2008"                 "03-APR-2008"                 "01-APR-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12007"                       
"38"                          ""                            "81"                          "02-APR-2008"                 "02-APR-2008"                 "01-APR-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12006"                       
"38"                          ""                            "81"                          "01-APR-2008"                 "01-APR-2008"                 "01-APR-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12005"                       
"38"                          ""                            "81"                          "26-MAR-2008"                 "26-MAR-2008"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12004"                       
"38"                          ""                            "81"                          "26-JUN-2018"                 "26-JUN-2018"                 "20-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12587"                       
"38"                          ""                            "81"                          "22-JUN-2018"                 "22-JUN-2018"                 "20-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12585"                       
"38"                          ""                            "81"                          "20-JUN-2018"                 "20-JUN-2018"                 "19-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12583"                       
"38"                          ""                            "81"                          "18-JUN-2018"                 "18-JUN-2018"                 "15-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12581"                       
"38"                          ""                            "81"                          "14-JUN-2018"                 "14-JUN-2018"                 "08-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12576"                       
"38"                          ""                            "81"                          "12-JUN-2018"                 "12-JUN-2018"                 "08-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12574"                       
"38"                          ""                            "81"                          "08-JUN-2018"                 "08-JUN-2018"                 "08-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12570"                       
"38"                          ""                            "81"                          "05-JUN-2018"                 "05-JUN-2018"                 "04-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12568"                       
"38"                          ""                            "81"                          "01-JUN-2018"                 "01-JUN-2018"                 "31-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12565"                       
"38"                          ""                            "81"                          "30-MAY-2018"                 "30-MAY-2018"                 "29-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12563"                       
"38"                          ""                            "81"                          "25-MAY-2018"                 "25-MAY-2018"                 "24-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12561"                       
"38"                          ""                            "81"                          "23-MAY-2018"                 "23-MAY-2018"                 "18-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12559"                       
"38"                          ""                            "81"                          "21-MAY-2018"                 "21-MAY-2018"                 "14-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12557"                       
"38"                          ""                            "81"                          "17-MAY-2018"                 "17-MAY-2018"                 "10-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12555"                       
"38"                          ""                            "81"                          "15-MAY-2018"                 "15-MAY-2018"                 "14-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12553"                       
"38"                          ""                            "81"                          "04-MAY-2018"                 "04-MAY-2018"                 "27-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12551"                       
"38"                          ""                            "81"                          "03-MAY-2018"                 "03-MAY-2018"                 "27-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12550"                       
"38"                          ""                            "81"                          "01-MAY-2018"                 "01-MAY-2018"                 "27-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12548"                       
"38"                          ""                            "81"                          "27-APR-2018"                 "27-APR-2018"                 "26-APR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12546"                       
"38"                          ""                            "81"                          "25-APR-2018"                 "25-APR-2018"                 "20-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12544"                       
"38"                          ""                            "81"                          "23-APR-2018"                 "23-APR-2018"                 "27-APR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12542"                       
"38"                          ""                            "81"                          "19-APR-2018"                 "19-APR-2018"                 "18-APR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12540"                       
"38"                          ""                            "81"                          "17-APR-2018"                 "17-APR-2018"                 "12-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12538"                       
"38"                          ""                            "81"                          "16-APR-2018"                 "16-APR-2018"                 "12-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12537"                       
"38"                          ""                            "81"                          "12-APR-2018"                 "12-APR-2018"                 "10-APR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12535"                       
"38"                          ""                            "81"                          "10-APR-2018"                 "10-APR-2018"                 "05-APR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12533"                       
"38"                          ""                            "81"                          "06-APR-2018"                 "06-APR-2018"                 "05-APR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12531"                       
"38"                          ""                            "81"                          "04-APR-2018"                 "04-APR-2018"                 "29-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12529"                       
"38"                          ""                            "81"                          "30-MAR-2018"                 "30-MAR-2018"                 "23-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12527"                       
"38"                          ""                            "81"                          "28-MAR-2018"                 "28-MAR-2018"                 "23-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12525"                       
"38"                          ""                            "81"                          "27-MAR-2018"                 "27-MAR-2018"                 "23-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12524"                       
"38"                          ""                            "81"                          "25-MAR-2018"                 "25-MAR-2018"                 "23-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12522"                       
"38"                          ""                            "81"                          "23-MAR-2018"                 "23-MAR-2018"                 "16-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12520"                       
"38"                          ""                            "81"                          "21-MAR-2018"                 "21-MAR-2018"                 "16-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12518"                       
"38"                          ""                            "81"                          "19-MAR-2018"                 "19-MAR-2018"                 "16-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12516"                       
"38"                          ""                            "81"                          "17-MAR-2018"                 "17-MAR-2018"                 "16-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12514"                       
"38"                          ""                            "81"                          "15-MAR-2018"                 "15-MAR-2018"                 "14-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12512"                       
"38"                          ""                            "81"                          "13-MAR-2018"                 "13-MAR-2018"                 "13-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12510"                       
"38"                          ""                            "81"                          "12-MAR-2018"                 "12-MAR-2018"                 "12-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12509"                       
"38"                          ""                            "81"                          "08-MAR-2018"                 "08-MAR-2018"                 "01-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12507"                       
"38"                          ""                            "81"                          "06-MAR-2018"                 "06-MAR-2018"                 "01-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12505"                       
"38"                          ""                            "81"                          "04-MAR-2018"                 "04-MAR-2018"                 "27-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12503"                       
"38"                          ""                            "81"                          "11-MAR-2018"                 "11-MAR-2018"                 "27-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12501"                       
"38"                          ""                            "81"                          "02-MAR-2018"                 "02-MAR-2018"                 "26-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12499"                       
"38"                          ""                            "81"                          "28-FEB-2018"                 "28-FEB-2018"                 "26-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12497"                       
"38"                          ""                            "81"                          "27-FEB-2018"                 "27-FEB-2018"                 "26-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12496"                       
"38"                          ""                            "81"                          "24-FEB-2018"                 "24-FEB-2018"                 "21-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12494"                       
"38"                          ""                            "81"                          "22-FEB-2018"                 "22-FEB-2018"                 "16-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12492"                       
"38"                          ""                            "81"                          "18-FEB-2018"                 "18-FEB-2018"                 "16-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12490"                       
"38"                          ""                            "81"                          "17-FEB-2018"                 "17-FEB-2018"                 "15-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12489"                       
"38"                          ""                            "81"                          "11-MAY-2018"                 "11-MAY-2018"                 "27-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12488"                       
"38"                          ""                            "81"                          "10-MAY-2018"                 "10-MAY-2018"                 "04-MAY-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12487"                       
"38"                          ""                            "81"                          "20-FEB-2018"                 "20-FEB-2018"                 "16-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12486"                       
"38"                          ""                            "81"                          "19-FEB-2018"                 "19-FEB-2018"                 "16-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12485"                       
"38"                          ""                            "81"                          "16-FEB-2018"                 "16-FEB-2018"                 "09-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12484"                       
"38"                          ""                            "81"                          "15-FEB-2018"                 "15-FEB-2018"                 "09-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12483"                       
"38"                          ""                            "81"                          "14-FEB-2018"                 "14-FEB-2018"                 "09-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12482"                       
"38"                          ""                            "81"                          "13-FEB-2018"                 "13-FEB-2018"                 "09-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12481"                       
"38"                          ""                            "81"                          "12-FEB-2018"                 "12-FEB-2018"                 "09-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12480"                       
"38"                          ""                            "81"                          "09-FEB-2018"                 "09-FEB-2018"                 "07-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12479"                       
"38"                          ""                            "81"                          "08-FEB-2018"                 "08-FEB-2018"                 "07-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12478"                       
"38"                          ""                            "81"                          "09-MAY-2018"                 "09-MAY-2018"                 "13-MAR-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12477"                       
"38"                          ""                            "81"                          "08-MAY-2018"                 "08-MAY-2018"                 "07-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12476"                       
"38"                          ""                            "81"                          "07-FEB-2018"                 "07-FEB-2018"                 "06-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12475"                       
"38"                          ""                            "81"                          "06-FEB-2018"                 "06-FEB-2018"                 "04-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12474"                       
"38"                          ""                            "81"                          "05-FEB-2018"                 "05-FEB-2018"                 "04-FEB-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12473"                       
"39"                          ""                            "81"                          "08-APR-2013"                 "12-APR-2013"                 "08-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12284"                       
"39"                          ""                            "81"                          "27-MAR-2009"                 "02-APR-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12176"                       
"39"                          ""                            "81"                          "16-JUL-2018"                 "20-JUL-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12598"                       
"39"                          ""                            "81"                          "12-MAR-2009"                 "18-MAR-2009"                 "12-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12161"                       
"39"                          ""                            "81"                          "06-DEC-2008"                 "06-DEC-2008"                 "01-APR-2009"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12106"                       
"39"                          ""                            "81"                          "24-OCT-2008"                 "30-OCT-2008"                 "24-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12067"                       
"39"                          ""                            "81"                          "23-MAY-2008"                 "29-MAY-2008"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12028"                       
"39"                          ""                            "81"                          "03-SEP-2018"                 "08-SEP-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12621"                       
"39"                          ""                            "81"                          "25-DEC-2017"                 "29-DEC-2017"                 "12-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12435"                       
"39"                          ""                            "81"                          "18-DEC-2017"                 "22-DEC-2017"                 "12-DEC-2017"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12434"                       
"39"                          ""                            "81"                          "27-MAY-2009"                 "02-JUN-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12200"                       
"39"                          ""                            "81"                          "31-MAR-2009"                 "06-APR-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12179"                       
"39"                          ""                            "81"                          "20-MAR-2009"                 "26-MAR-2009"                 "20-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12169"                       
"39"                          ""                            "81"                          "09-JAN-2009"                 "09-JAN-2009"                 "08-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12115"                       
"39"                          ""                            "81"                          "01-DEC-2008"                 "05-DEC-2008"                 "01-APR-2009"                 "SUCCESS"                     ""                            "F"                           "6"                           "N"                           "12098"                       
"39"                          ""                            "81"                          "05-NOV-2008"                 "05-NOV-2008"                 "01-APR-2009"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12075"                       
"39"                          ""                            "81"                          "03-NOV-2008"                 "03-NOV-2008"                 "03-NOV-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12072"                       
"39"                          ""                            "81"                          "18-JUN-2018"                 "22-JUN-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12579"                       
"39"                          ""                            "81"                          "11-JUN-2018"                 "15-JUN-2018"                 "12-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12572"                       
"39"                          ""                            "81"                          "04-JUN-2018"                 "08-JUN-2018"                 "06-JUN-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12566"                       
"40"                          ""                            "81"                          "31-MAR-2009"                 "31-MAR-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12180"                       
"40"                          ""                            "81"                          "20-MAR-2009"                 "26-MAR-2009"                 "20-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12168"                       
"40"                          ""                            "81"                          "24-SEP-2018"                 "28-SEP-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12626"                       
"40"                          ""                            "81"                          "09-JAN-2009"                 "09-JAN-2009"                 "15-JAN-2009"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12114"                       
"40"                          ""                            "81"                          "01-DEC-2008"                 "05-DEC-2008"                 "04-DEC-2008"                 "SUCCESS"                     ""                            "F"                           "3"                           "N"                           "12099"                       
"40"                          ""                            "81"                          "13-AUG-2018"                 "17-AUG-2018"                 "12-JUL-2018"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12580"                       
"40"                          ""                            "81"                          "11-SEP-2018"                 "17-SEP-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12625"                       
"40"                          ""                            "81"                          "03-SEP-2018"                 "08-SEP-2018"                 "29-AUG-2018"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12623"                       
"40"                          ""                            "81"                          "17-JAN-2018"                 "23-JAN-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12462"                       
"40"                          ""                            "81"                          "08-APR-2013"                 "12-APR-2013"                 "08-APR-2013"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12283"                       
"40"                          ""                            "81"                          "28-MAR-2013"                 "03-APR-2013"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12268"                       
"40"                          ""                            "81"                          "08-MAR-2013"                 "14-MAR-2013"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12266"                       
"40"                          ""                            "81"                          "27-MAY-2009"                 "02-JUN-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12202"                       
"40"                          ""                            "81"                          "01-APR-2009"                 "01-APR-2009"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12181"                       
"40"                          ""                            "81"                          "12-MAR-2009"                 "18-MAR-2009"                 "12-MAR-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12160"                       
"40"                          ""                            "81"                          "05-FEB-2009"                 "05-FEB-2009"                 "05-FEB-2009"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12136"                       
"40"                          ""                            "81"                          "06-DEC-2008"                 "06-DEC-2008"                 "04-DEC-2008"                 "SUCCESS"                     ""                            "F"                           "2"                           "N"                           "12105"                       
"40"                          ""                            "81"                          "24-OCT-2008"                 "30-OCT-2008"                 "24-OCT-2008"                 "SUCCESS"                     ""                            "F"                           "1"                           "N"                           "12066"                       
"40"                          ""                            "81"                          "23-MAY-2008"                 "29-MAY-2008"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12029"                       
"40"                          ""                            "81"                          "18-JUN-2018"                 "22-JUN-2018"                 ""                            ""                            ""                            "D"                           "1"                           "N"                           "12578"                       


--618 rows returned

SELECT count(*) from xhibit.XHB_LIST; --102 rows

SELECT (102+618) from dual; --720 rows


begin
 dbms_output.enable(10000000);
 dm_process_pkg_cc.upd_xhb_lists_with_crest (453);
end;


CREST - COURT : 453 - Starting inserting rows into XHB_LIST with data from CREST
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 189 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 190 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 191 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 192 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 193 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 194 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 195 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 196 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 197 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 198 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 199 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 200 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 201 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 202 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 203 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 204 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 205 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 206 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 207 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 208 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 209 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 210 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 211 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 212 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 213 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 214 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 215 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 216 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 217 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 218 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 219 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 220 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 221 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 222 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 223 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 224 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 225 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 226 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 227 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 228 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 229 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 230 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 231 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 232 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 233 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 234 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 235 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 236 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 237 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 238 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 239 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 240 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 241 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 242 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 243 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 244 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 245 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 246 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 247 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 248 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 249 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 250 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 251 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 252 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 253 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 254 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 255 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 256 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 257 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 258 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 259 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 260 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 261 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 262 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 263 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 264 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 265 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 266 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 267 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 268 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 269 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 270 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 271 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 272 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 273 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 274 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 275 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 276 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 277 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 278 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 279 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 280 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 281 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 282 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 283 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 284 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 285 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 286 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 287 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 288 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 289 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 290 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 291 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 292 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 293 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 294 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 295 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 296 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 297 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 298 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 299 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 300 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 301 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 302 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 303 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 304 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 305 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 306 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 307 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 308 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 309 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 310 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 311 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 312 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 313 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 314 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 315 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 316 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 317 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 318 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 319 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 320 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 321 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 322 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 323 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 324 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 325 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 326 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 327 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 328 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 329 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 330 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 331 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 332 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 333 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 334 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 335 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 336 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 337 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 338 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 339 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 340 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 341 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 342 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 343 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 344 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 345 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 346 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 347 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 348 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 349 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 350 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 351 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 352 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 353 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 354 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 355 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 356 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 357 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 358 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 359 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 360 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 361 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 362 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 363 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 364 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 365 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 366 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 367 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 368 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 369 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 370 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 371 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 372 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 373 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 374 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 375 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 376 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 377 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 378 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 379 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 380 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 381 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 382 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 383 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 384 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 385 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 386 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 387 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 388 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 389 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 390 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 391 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 392 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 393 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 394 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 395 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 396 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 397 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 398 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 399 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 400 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 401 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 402 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 403 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 404 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 405 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 406 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 407 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 408 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 409 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 410 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 411 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 412 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 413 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 414 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 415 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 416 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 417 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 418 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 419 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 420 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 421 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 422 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 423 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 424 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 425 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 426 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 427 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 428 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 429 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 430 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 431 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 432 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 433 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 434 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 435 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 436 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 437 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 438 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 439 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 440 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 441 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 442 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 443 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 444 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 445 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 446 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 447 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 448 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 449 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 450 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 451 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 452 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 453 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 454 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 455 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 456 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 457 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 458 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 459 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 460 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 461 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 462 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 463 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 464 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 465 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 466 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 467 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 468 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 469 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 470 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 471 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 472 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 473 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 474 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 475 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 476 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 477 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 478 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 479 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 480 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 481 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 482 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 483 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 484 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 485 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 486 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 487 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 488 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 489 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 490 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 491 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 492 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 493 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 494 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 495 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 496 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 497 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 498 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 499 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 500 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 501 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 502 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 503 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 504 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 505 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 506 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 507 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 508 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 509 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 510 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 511 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 512 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 513 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 514 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 515 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 516 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 517 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 518 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 519 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 520 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 521 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 522 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 523 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 524 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 525 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 526 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 527 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 528 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 529 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 530 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 531 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 532 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 533 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 534 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 535 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 536 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 537 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 538 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 539 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 540 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 541 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 542 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 543 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 544 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 545 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 546 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 547 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 548 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 549 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 550 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 551 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 552 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 553 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 554 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 555 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 556 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 557 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 558 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 559 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 560 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 561 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 562 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 563 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 564 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 565 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 566 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 567 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 568 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 569 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 570 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 571 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 572 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 573 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 574 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 575 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 576 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 577 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 578 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 579 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 580 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 581 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 582 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 583 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 584 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 585 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 586 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 587 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 588 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 589 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 590 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 591 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 592 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 593 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 594 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 595 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 596 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 597 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 598 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 599 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 600 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 601 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 602 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 603 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 604 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 605 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 606 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 607 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 608 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 609 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 610 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 611 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 612 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 613 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 614 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 615 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 616 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 617 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 618 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 619 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 620 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 621 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 622 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 623 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 624 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 625 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 626 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 627 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 628 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 629 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 630 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 631 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 632 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 633 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 634 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 635 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 636 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 637 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 638 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 639 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 640 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 641 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 642 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 643 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 644 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 645 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 646 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 647 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 648 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 649 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 650 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 651 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 652 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 653 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 654 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 655 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 656 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 657 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 658 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 659 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 660 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 661 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 662 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 663 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 664 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 665 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 666 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 667 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 668 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 669 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 670 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 671 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 672 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 673 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 674 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 675 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 676 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 677 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 678 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 679 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 680 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 681 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 682 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 683 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 684 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 685 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 686 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 687 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 688 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 689 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 690 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 691 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 692 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 693 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 694 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 695 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 696 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 697 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 698 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 699 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 700 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 701 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 702 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 703 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 704 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 705 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 706 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 707 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 708 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 709 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 710 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 711 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 712 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 713 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 714 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 715 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 716 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 717 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 718 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 719 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 720 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 721 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 722 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 723 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 724 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 725 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 726 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 727 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 728 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 729 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 730 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 731 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 732 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 733 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 734 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 735 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 736 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 737 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 738 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 739 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 740 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 741 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 742 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 743 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 744 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 745 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 746 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 747 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 748 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 749 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 750 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 751 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 752 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 753 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 754 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 755 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 756 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 757 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 758 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 759 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 760 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 761 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 762 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 763 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 764 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 765 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 766 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 767 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 768 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 769 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 770 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 771 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 772 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 773 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 774 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 775 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 776 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 777 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 778 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 779 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 780 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 781 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 782 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 783 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 784 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 785 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 786 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 787 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 788 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 789 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 790 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 791 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 792 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 793 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 794 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 795 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 796 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 797 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 798 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 799 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 800 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 801 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 802 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 803 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 804 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 805 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_LIST - processing crest court id - 453
Court ID : 81, list IS : 806 , v_xhibit_court_id 81
 
CTX-2402:XHB_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2402:xhbstg_list_dm - Crest Court : 453 - Updated no of rows : 1
 
CTX-2402:XHB_LIST processed 0 for CREST_COURT_ID : 453 successfully!
XHB_LIST Updated for CREST_COURT_ID : 453 successfully!

update xhbstg_lists_dm set XHIBIT_ETL_STATUS = NULL, XHIBIT_LOADED_DATE =NULL, XHIBIT_ENRICH_DATE =NULL, XHIBIT_ETL_DATE = NULL, XHIBIT_ETL_ERR_MESSAGE = NULL;  

SELECT count(*) from xhibit.XHB_LIST; --720 rows which is as expected 


--check data has gone in
select * from xhbstg_lists_dm where crest_court_id = 453;
"LST_ID"                      "START_DATE"                  "END_DATE"                    "DATE_PUBLISHED"              "LIST_TYPE"                   "CC_IND"                      "LIST_STATUS"                 "EDITION_NO"                  "FCL_START_TIME"              "CREST_COURT_ID"              "XHIBIT_COURT_ID"             "XHIBIT_ETL_STATUS"           "XHIBIT_LOADED_DATE"          "XHIBIT_ENRICH_DATE"          "XHIBIT_ETL_DATE"             "XHIBIT_ETL_ERR_MESSAGE"      
"12473"                       "05-FEB-2018"                 "05-FEB-2018"                 "04-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12474"                       "06-FEB-2018"                 "06-FEB-2018"                 "04-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12475"                       "07-FEB-2018"                 "07-FEB-2018"                 "06-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12476"                       "08-MAY-2018"                 "08-MAY-2018"                 "07-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12477"                       "09-MAY-2018"                 "09-MAY-2018"                 "13-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12478"                       "08-FEB-2018"                 "08-FEB-2018"                 "07-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12479"                       "09-FEB-2018"                 "09-FEB-2018"                 "07-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12480"                       "12-FEB-2018"                 "12-FEB-2018"                 "09-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12481"                       "13-FEB-2018"                 "13-FEB-2018"                 "09-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12482"                       "14-FEB-2018"                 "14-FEB-2018"                 "09-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12483"                       "15-FEB-2018"                 "15-FEB-2018"                 "09-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12484"                       "16-FEB-2018"                 "16-FEB-2018"                 "09-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12485"                       "19-FEB-2018"                 "19-FEB-2018"                 "16-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12486"                       "20-FEB-2018"                 "20-FEB-2018"                 "16-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12487"                       "10-MAY-2018"                 "10-MAY-2018"                 "04-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12488"                       "11-MAY-2018"                 "11-MAY-2018"                 "27-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12489"                       "17-FEB-2018"                 "17-FEB-2018"                 "15-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12490"                       "18-FEB-2018"                 "18-FEB-2018"                 "16-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12492"                       "22-FEB-2018"                 "22-FEB-2018"                 "16-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12494"                       "24-FEB-2018"                 "24-FEB-2018"                 "21-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12496"                       "27-FEB-2018"                 "27-FEB-2018"                 "26-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12497"                       "28-FEB-2018"                 "28-FEB-2018"                 "26-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12499"                       "02-MAR-2018"                 "02-MAR-2018"                 "26-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12501"                       "11-MAR-2018"                 "11-MAR-2018"                 "27-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12503"                       "04-MAR-2018"                 "04-MAR-2018"                 "27-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12505"                       "06-MAR-2018"                 "06-MAR-2018"                 "01-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12507"                       "08-MAR-2018"                 "08-MAR-2018"                 "01-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12509"                       "12-MAR-2018"                 "12-MAR-2018"                 "12-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12510"                       "13-MAR-2018"                 "13-MAR-2018"                 "13-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12512"                       "15-MAR-2018"                 "15-MAR-2018"                 "14-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12514"                       "17-MAR-2018"                 "17-MAR-2018"                 "16-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12516"                       "19-MAR-2018"                 "19-MAR-2018"                 "16-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12518"                       "21-MAR-2018"                 "21-MAR-2018"                 "16-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12520"                       "23-MAR-2018"                 "23-MAR-2018"                 "16-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12522"                       "25-MAR-2018"                 "25-MAR-2018"                 "23-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12524"                       "27-MAR-2018"                 "27-MAR-2018"                 "23-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12525"                       "28-MAR-2018"                 "28-MAR-2018"                 "23-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12527"                       "30-MAR-2018"                 "30-MAR-2018"                 "23-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12529"                       "04-APR-2018"                 "04-APR-2018"                 "29-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12531"                       "06-APR-2018"                 "06-APR-2018"                 "05-APR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12533"                       "10-APR-2018"                 "10-APR-2018"                 "05-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12535"                       "12-APR-2018"                 "12-APR-2018"                 "10-APR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12537"                       "16-APR-2018"                 "16-APR-2018"                 "12-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12538"                       "17-APR-2018"                 "17-APR-2018"                 "12-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12540"                       "19-APR-2018"                 "19-APR-2018"                 "18-APR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12542"                       "23-APR-2018"                 "23-APR-2018"                 "27-APR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12544"                       "25-APR-2018"                 "25-APR-2018"                 "20-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12546"                       "27-APR-2018"                 "27-APR-2018"                 "26-APR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12548"                       "01-MAY-2018"                 "01-MAY-2018"                 "27-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12550"                       "03-MAY-2018"                 "03-MAY-2018"                 "27-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12551"                       "04-MAY-2018"                 "04-MAY-2018"                 "27-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12553"                       "15-MAY-2018"                 "15-MAY-2018"                 "14-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12555"                       "17-MAY-2018"                 "17-MAY-2018"                 "10-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12557"                       "21-MAY-2018"                 "21-MAY-2018"                 "14-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12559"                       "23-MAY-2018"                 "23-MAY-2018"                 "18-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12561"                       "25-MAY-2018"                 "25-MAY-2018"                 "24-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12563"                       "30-MAY-2018"                 "30-MAY-2018"                 "29-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12565"                       "01-JUN-2018"                 "01-JUN-2018"                 "31-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12566"                       "04-JUN-2018"                 "08-JUN-2018"                 "06-JUN-2018"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12568"                       "05-JUN-2018"                 "05-JUN-2018"                 "04-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12570"                       "08-JUN-2018"                 "08-JUN-2018"                 "08-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12572"                       "11-JUN-2018"                 "15-JUN-2018"                 "12-JUN-2018"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12574"                       "12-JUN-2018"                 "12-JUN-2018"                 "08-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12576"                       "14-JUN-2018"                 "14-JUN-2018"                 "08-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12578"                       "18-JUN-2018"                 "22-JUN-2018"                 ""                            "F"                           "CR"                          "DRAFT"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12579"                       "18-JUN-2018"                 "22-JUN-2018"                 ""                            "W"                           "CR"                          "DRAFT"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12581"                       "18-JUN-2018"                 "18-JUN-2018"                 "15-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12583"                       "20-JUN-2018"                 "20-JUN-2018"                 "19-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12585"                       "22-JUN-2018"                 "22-JUN-2018"                 "20-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12587"                       "26-JUN-2018"                 "26-JUN-2018"                 "20-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12004"                       "26-MAR-2008"                 "26-MAR-2008"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12005"                       "01-APR-2008"                 "01-APR-2008"                 "01-APR-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12006"                       "02-APR-2008"                 "02-APR-2008"                 "01-APR-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12007"                       "03-APR-2008"                 "03-APR-2008"                 "01-APR-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12008"                       "08-APR-2008"                 "08-APR-2008"                 "08-APR-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12009"                       "09-APR-2008"                 "09-APR-2008"                 "08-APR-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12010"                       "10-APR-2008"                 "10-APR-2008"                 "08-APR-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12011"                       "06-MAY-2008"                 "06-MAY-2008"                 "02-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12012"                       "02-MAY-2008"                 "02-MAY-2008"                 "02-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12013"                       "07-MAY-2008"                 "07-MAY-2008"                 "07-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12014"                       "08-MAY-2008"                 "08-MAY-2008"                 "08-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12015"                       "09-MAY-2008"                 "09-MAY-2008"                 "09-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12016"                       "10-MAY-2008"                 "10-MAY-2008"                 "09-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12017"                       "12-MAY-2008"                 "12-MAY-2008"                 "12-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12018"                       "13-MAY-2008"                 "13-MAY-2008"                 "12-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12019"                       "14-MAY-2008"                 "14-MAY-2008"                 "13-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12020"                       "15-MAY-2008"                 "15-MAY-2008"                 "14-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12021"                       "16-MAY-2008"                 "16-MAY-2008"                 "15-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12023"                       "20-MAY-2008"                 "20-MAY-2008"                 "19-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12025"                       "22-MAY-2008"                 "22-MAY-2008"                 "21-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12027"                       "27-MAY-2008"                 "27-MAY-2008"                 "23-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12029"                       "23-MAY-2008"                 "29-MAY-2008"                 ""                            "F"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12031"                       "30-MAY-2008"                 "30-MAY-2008"                 "29-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12033"                       "03-JUN-2008"                 "03-JUN-2008"                 "03-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12034"                       "04-JUN-2008"                 "04-JUN-2008"                 "03-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12036"                       "06-JUN-2008"                 "06-JUN-2008"                 "05-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12038"                       "10-JUN-2008"                 "10-JUN-2008"                 "09-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12040"                       "12-JUN-2008"                 "12-JUN-2008"                 "11-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12042"                       "16-JUN-2008"                 "16-JUN-2008"                 "13-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12044"                       "24-JUN-2008"                 "24-JUN-2008"                 "23-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12046"                       "26-JUN-2008"                 "26-JUN-2008"                 "25-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12047"                       "30-JUN-2008"                 "30-JUN-2008"                 "30-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12049"                       "03-JUL-2008"                 "03-JUL-2008"                 "03-JUL-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12051"                       "14-JUL-2008"                 "14-JUL-2008"                 "11-JUL-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12053"                       "09-SEP-2008"                 "09-SEP-2008"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12055"                       "25-SEP-2008"                 "25-SEP-2008"                 "24-SEP-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12057"                       "13-OCT-2008"                 "13-OCT-2008"                 "13-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12059"                       "15-OCT-2008"                 "15-OCT-2008"                 "14-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12060"                       "16-OCT-2008"                 "16-OCT-2008"                 "16-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12062"                       "20-OCT-2008"                 "20-OCT-2008"                 "17-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12064"                       "24-OCT-2008"                 "24-OCT-2008"                 "24-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12066"                       "24-OCT-2008"                 "30-OCT-2008"                 "24-OCT-2008"                 "F"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12068"                       "28-OCT-2008"                 "28-OCT-2008"                 "28-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12070"                       "31-OCT-2008"                 "31-OCT-2008"                 "31-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12072"                       "03-NOV-2008"                 "03-NOV-2008"                 "03-NOV-2008"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12074"                       "05-NOV-2008"                 "05-NOV-2008"                 "04-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12075"                       "05-NOV-2008"                 "05-NOV-2008"                 "01-APR-2009"                 "W"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12077"                       "07-NOV-2008"                 "07-NOV-2008"                 "06-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12079"                       "12-NOV-2008"                 "12-NOV-2008"                 "11-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12081"                       "14-NOV-2008"                 "14-NOV-2008"                 "14-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12083"                       "18-NOV-2008"                 "18-NOV-2008"                 "17-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12085"                       "20-NOV-2008"                 "20-NOV-2008"                 "19-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12088"                       "25-NOV-2008"                 "25-NOV-2008"                 "20-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12089"                       "26-NOV-2008"                 "26-NOV-2008"                 "25-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12091"                       "28-NOV-2008"                 "28-NOV-2008"                 "20-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12093"                       "22-NOV-2008"                 "22-NOV-2008"                 "21-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12095"                       "29-NOV-2008"                 "29-NOV-2008"                 "28-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12098"                       "01-DEC-2008"                 "05-DEC-2008"                 "01-APR-2009"                 "W"                           "CR"                          "FINAL"                       "6"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12100"                       "02-DEC-2008"                 "02-DEC-2008"                 "02-DEC-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12102"                       "03-DEC-2008"                 "03-DEC-2008"                 "13-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "4"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12104"                       "05-DEC-2008"                 "05-DEC-2008"                 "06-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12105"                       "06-DEC-2008"                 "06-DEC-2008"                 "04-DEC-2008"                 "F"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12107"                       "08-DEC-2008"                 "08-DEC-2008"                 "08-DEC-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12109"                       "24-DEC-2008"                 "24-DEC-2008"                 "24-DEC-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12111"                       "07-JAN-2009"                 "07-JAN-2009"                 "07-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12113"                       "09-JAN-2009"                 "09-JAN-2009"                 "07-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12115"                       "09-JAN-2009"                 "09-JAN-2009"                 "08-JAN-2009"                 "W"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12117"                       "13-JAN-2009"                 "13-JAN-2009"                 "15-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12118"                       "14-JAN-2009"                 "14-JAN-2009"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12120"                       "16-JAN-2009"                 "16-JAN-2009"                 "15-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12122"                       "19-JAN-2009"                 "19-JAN-2009"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12123"                       "19-FEB-2009"                 "19-FEB-2009"                 "16-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12124"                       "20-JAN-2009"                 "20-JAN-2009"                 "19-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12125"                       "21-JAN-2009"                 "21-JAN-2009"                 "20-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12126"                       "22-JAN-2009"                 "22-JAN-2009"                 "21-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12127"                       "23-JAN-2009"                 "23-JAN-2009"                 "23-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12128"                       "25-JAN-2009"                 "25-JAN-2009"                 "25-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12129"                       "26-JAN-2009"                 "26-JAN-2009"                 "25-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12130"                       "27-JAN-2009"                 "27-JAN-2009"                 "26-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12131"                       "28-JAN-2009"                 "28-JAN-2009"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12132"                       "31-JAN-2009"                 "31-JAN-2009"                 "30-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12133"                       "04-FEB-2009"                 "04-FEB-2009"                 "04-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12134"                       "05-FEB-2009"                 "05-FEB-2009"                 "05-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12135"                       "06-FEB-2009"                 "06-FEB-2009"                 "05-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12136"                       "05-FEB-2009"                 "05-FEB-2009"                 "05-FEB-2009"                 "F"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12137"                       "10-FEB-2009"                 "10-FEB-2009"                 "10-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12138"                       "11-FEB-2009"                 "11-FEB-2009"                 "11-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12139"                       "12-FEB-2009"                 "12-FEB-2009"                 "12-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12141"                       "16-FEB-2009"                 "16-FEB-2009"                 "16-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12143"                       "18-FEB-2009"                 "18-FEB-2009"                 "16-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12145"                       "23-FEB-2009"                 "23-FEB-2009"                 "16-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12147"                       "25-FEB-2009"                 "25-FEB-2009"                 "25-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12148"                       "26-FEB-2009"                 "26-FEB-2009"                 "25-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12150"                       "02-MAR-2009"                 "02-MAR-2009"                 "27-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12152"                       "04-MAR-2009"                 "04-MAR-2009"                 "03-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12154"                       "06-MAR-2009"                 "06-MAR-2009"                 "05-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12156"                       "10-MAR-2009"                 "10-MAR-2009"                 "09-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12158"                       "12-MAR-2009"                 "12-MAR-2009"                 "11-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "4"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12160"                       "12-MAR-2009"                 "18-MAR-2009"                 "12-MAR-2009"                 "F"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12162"                       "17-MAR-2009"                 "17-MAR-2009"                 "17-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12164"                       "19-MAR-2009"                 "19-MAR-2009"                 "18-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12165"                       "20-MAR-2009"                 "20-MAR-2009"                 "19-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12167"                       "21-MAR-2009"                 "21-MAR-2009"                 "20-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12169"                       "20-MAR-2009"                 "26-MAR-2009"                 "20-MAR-2009"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12171"                       "25-MAR-2009"                 "25-MAR-2009"                 "24-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12173"                       "27-MAR-2009"                 "27-MAR-2009"                 "26-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12175"                       "29-MAR-2009"                 "29-MAR-2009"                 "26-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12177"                       "30-MAR-2009"                 "30-MAR-2009"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12179"                       "31-MAR-2009"                 "06-APR-2009"                 ""                            "W"                           "CV"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12181"                       "01-APR-2009"                 "01-APR-2009"                 ""                            "F"                           "CV"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12183"                       "02-APR-2009"                 "02-APR-2009"                 "01-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12185"                       "06-APR-2009"                 "06-APR-2009"                 "03-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12187"                       "08-APR-2009"                 "08-APR-2009"                 "07-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12189"                       "14-APR-2009"                 "14-APR-2009"                 "14-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12190"                       "15-APR-2009"                 "15-APR-2009"                 "15-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12192"                       "17-APR-2009"                 "17-APR-2009"                 "17-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12194"                       "21-APR-2009"                 "21-APR-2009"                 "21-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12196"                       "23-APR-2009"                 "23-APR-2009"                 "23-APR-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12198"                       "28-APR-2009"                 "28-APR-2009"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12200"                       "27-MAY-2009"                 "02-JUN-2009"                 ""                            "W"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12202"                       "27-MAY-2009"                 "02-JUN-2009"                 ""                            "F"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12205"                       "22-OCT-2009"                 "22-OCT-2009"                 "22-OCT-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12207"                       "25-MAY-2011"                 "25-MAY-2011"                 "25-MAY-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12210"                       "31-MAY-2011"                 "31-MAY-2011"                 "27-MAY-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12212"                       "06-JUN-2011"                 "06-JUN-2011"                 "06-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12214"                       "08-JUN-2011"                 "08-JUN-2011"                 "07-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12215"                       "09-JUN-2011"                 "09-JUN-2011"                 "09-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12217"                       "13-JUN-2011"                 "13-JUN-2011"                 "10-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12219"                       "15-JUN-2011"                 "15-JUN-2011"                 "14-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12221"                       "17-JUN-2011"                 "17-JUN-2011"                 "16-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12223"                       "21-JUN-2011"                 "21-JUN-2011"                 "20-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12225"                       "23-JUN-2011"                 "23-JUN-2011"                 "23-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12227"                       "27-JUN-2011"                 "27-JUN-2011"                 "26-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12228"                       "28-JUN-2011"                 "28-JUN-2011"                 "27-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12230"                       "30-JUN-2011"                 "30-JUN-2011"                 "30-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12232"                       "04-JUL-2011"                 "04-JUL-2011"                 "01-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12234"                       "06-JUL-2011"                 "06-JUL-2011"                 "05-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12236"                       "08-JUL-2011"                 "08-JUL-2011"                 "08-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12238"                       "12-JUL-2011"                 "12-JUL-2011"                 "11-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12240"                       "18-JUL-2011"                 "18-JUL-2011"                 "15-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12242"                       "20-JUL-2011"                 "20-JUL-2011"                 "20-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12243"                       "22-JUL-2011"                 "22-JUL-2011"                 "21-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12244"                       "25-JUL-2011"                 "25-JUL-2011"                 "22-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12245"                       "26-JUL-2011"                 "26-JUL-2011"                 ""                            "D"                           "CR"                          "DRAFT"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12246"                       "27-JUL-2011"                 "27-JUL-2011"                 "26-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12247"                       "01-AUG-2011"                 "01-AUG-2011"                 "29-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12248"                       "02-AUG-2011"                 "02-AUG-2011"                 "29-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12249"                       "03-AUG-2011"                 "03-AUG-2011"                 "29-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12250"                       "04-AUG-2011"                 "04-AUG-2011"                 "29-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12251"                       "05-AUG-2011"                 "05-AUG-2011"                 "29-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12252"                       "16-AUG-2011"                 "16-AUG-2011"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12253"                       "26-AUG-2011"                 "26-AUG-2011"                 "26-AUG-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12254"                       "16-SEP-2011"                 "16-SEP-2011"                 "16-SEP-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12255"                       "16-NOV-2011"                 "16-NOV-2011"                 "15-NOV-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12256"                       "17-NOV-2011"                 "17-NOV-2011"                 "16-NOV-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12257"                       "18-NOV-2011"                 "18-NOV-2011"                 "17-NOV-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12258"                       "21-NOV-2011"                 "21-NOV-2011"                 "18-NOV-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12259"                       "01-MAR-2013"                 "01-MAR-2013"                 "01-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12261"                       "05-MAR-2013"                 "05-MAR-2013"                 "04-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12263"                       "07-MAR-2013"                 "07-MAR-2013"                 "06-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12265"                       "11-MAR-2013"                 "11-MAR-2013"                 "08-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12266"                       "08-MAR-2013"                 "14-MAR-2013"                 ""                            "F"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12268"                       "28-MAR-2013"                 "03-APR-2013"                 ""                            "F"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12270"                       "15-MAR-2013"                 "15-MAR-2013"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12272"                       "21-MAR-2013"                 "21-MAR-2013"                 "21-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12274"                       "25-MAR-2013"                 "25-MAR-2013"                 "25-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12276"                       "27-MAR-2013"                 "27-MAR-2013"                 "26-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12278"                       "03-APR-2013"                 "03-APR-2013"                 "03-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12279"                       "04-APR-2013"                 "04-APR-2013"                 "04-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12281"                       "08-APR-2013"                 "08-APR-2013"                 "05-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12283"                       "08-APR-2013"                 "12-APR-2013"                 "08-APR-2013"                 "F"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12285"                       "10-APR-2013"                 "10-APR-2013"                 "09-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12287"                       "12-APR-2013"                 "12-APR-2013"                 "12-APR-2013"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12289"                       "16-APR-2013"                 "16-APR-2013"                 "15-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12291"                       "18-APR-2013"                 "18-APR-2013"                 "18-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12292"                       "22-APR-2013"                 "22-APR-2013"                 "22-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12294"                       "24-APR-2013"                 "24-APR-2013"                 "23-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12296"                       "02-MAY-2013"                 "02-MAY-2013"                 "02-MAY-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12298"                       "28-APR-2014"                 "28-APR-2014"                 "28-APR-2014"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12300"                       "30-APR-2014"                 "30-APR-2014"                 "28-APR-2014"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12302"                       "06-MAY-2014"                 "06-MAY-2014"                 "06-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12304"                       "09-MAY-2014"                 "09-MAY-2014"                 "09-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12306"                       "13-MAY-2014"                 "13-MAY-2014"                 "12-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12307"                       "14-MAY-2014"                 "14-MAY-2014"                 "12-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12309"                       "16-MAY-2014"                 "16-MAY-2014"                 "12-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12311"                       "20-MAY-2014"                 "20-MAY-2014"                 "16-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12313"                       "22-MAY-2014"                 "22-MAY-2014"                 "16-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12315"                       "28-MAY-2014"                 "28-MAY-2014"                 "28-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "4"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12317"                       "30-MAY-2014"                 "30-MAY-2014"                 "28-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12319"                       "03-JUN-2014"                 "03-JUN-2014"                 "03-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12320"                       "04-JUN-2014"                 "04-JUN-2014"                 "04-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12322"                       "06-JUN-2014"                 "06-JUN-2014"                 "04-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12324"                       "12-JUN-2014"                 "12-JUN-2014"                 "12-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12326"                       "17-JUN-2014"                 "17-JUN-2014"                 "17-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12328"                       "19-JUN-2014"                 "19-JUN-2014"                 "17-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12330"                       "04-AUG-2014"                 "04-AUG-2014"                 "04-AUG-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12332"                       "23-MAY-2017"                 "23-MAY-2017"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12334"                       "01-JUN-2017"                 "01-JUN-2017"                 "31-MAY-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12335"                       "02-JUN-2017"                 "02-JUN-2017"                 "01-JUN-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12337"                       "06-JUN-2017"                 "06-JUN-2017"                 "05-JUN-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12339"                       "27-JUN-2017"                 "27-JUN-2017"                 "23-JUN-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12341"                       "07-JUL-2017"                 "07-JUL-2017"                 "06-JUL-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12343"                       "11-JUL-2017"                 "11-JUL-2017"                 "06-JUL-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12345"                       "20-JUL-2017"                 "20-JUL-2017"                 "19-JUL-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12347"                       "24-JUL-2017"                 "24-JUL-2017"                 "20-JUL-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12348"                       "25-JUL-2017"                 "25-JUL-2017"                 "20-JUL-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12350"                       "04-AUG-2017"                 "04-AUG-2017"                 "03-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12352"                       "22-AUG-2017"                 "22-AUG-2017"                 "21-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12354"                       "24-AUG-2017"                 "24-AUG-2017"                 "23-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12356"                       "30-AUG-2017"                 "30-AUG-2017"                 "29-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12358"                       "01-SEP-2017"                 "01-SEP-2017"                 "01-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12359"                       "04-SEP-2017"                 "04-SEP-2017"                 "01-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12360"                       "05-SEP-2017"                 "05-SEP-2017"                 "01-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12361"                       "06-SEP-2017"                 "06-SEP-2017"                 "01-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12362"                       "07-SEP-2017"                 "07-SEP-2017"                 "01-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12363"                       "08-SEP-2017"                 "08-SEP-2017"                 "01-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12364"                       "11-SEP-2017"                 "11-SEP-2017"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12365"                       "12-SEP-2017"                 "12-SEP-2017"                 "08-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12366"                       "13-SEP-2017"                 "13-SEP-2017"                 "08-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12367"                       "14-SEP-2017"                 "14-SEP-2017"                 "14-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12368"                       "15-SEP-2017"                 "15-SEP-2017"                 "14-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12369"                       "18-SEP-2017"                 "18-SEP-2017"                 "15-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12370"                       "19-SEP-2017"                 "19-SEP-2017"                 "18-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12371"                       "20-SEP-2017"                 "20-SEP-2017"                 "18-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12372"                       "21-SEP-2017"                 "21-SEP-2017"                 "20-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12373"                       "22-SEP-2017"                 "22-SEP-2017"                 "20-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12374"                       "25-SEP-2017"                 "25-SEP-2017"                 "20-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12375"                       "26-SEP-2017"                 "26-SEP-2017"                 "25-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12377"                       "28-SEP-2017"                 "28-SEP-2017"                 "25-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12378"                       "29-SEP-2017"                 "29-SEP-2017"                 "25-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12380"                       "03-OCT-2017"                 "03-OCT-2017"                 "02-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12382"                       "05-OCT-2017"                 "05-OCT-2017"                 "03-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12384"                       "09-OCT-2017"                 "09-OCT-2017"                 "09-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12386"                       "11-OCT-2017"                 "11-OCT-2017"                 "10-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12388"                       "13-OCT-2017"                 "13-OCT-2017"                 "13-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12390"                       "17-OCT-2017"                 "17-OCT-2017"                 "13-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12391"                       "18-OCT-2017"                 "18-OCT-2017"                 "17-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12393"                       "20-OCT-2017"                 "20-OCT-2017"                 "17-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12395"                       "24-OCT-2017"                 "24-OCT-2017"                 "20-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12397"                       "26-OCT-2017"                 "26-OCT-2017"                 "20-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12399"                       "30-OCT-2017"                 "30-OCT-2017"                 "30-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12401"                       "01-NOV-2017"                 "01-NOV-2017"                 "30-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12403"                       "03-NOV-2017"                 "03-NOV-2017"                 "30-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12405"                       "07-NOV-2017"                 "07-NOV-2017"                 "03-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12406"                       "08-NOV-2017"                 "08-NOV-2017"                 "03-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12408"                       "10-NOV-2017"                 "10-NOV-2017"                 "03-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12410"                       "14-NOV-2017"                 "14-NOV-2017"                 "13-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12412"                       "16-NOV-2017"                 "16-NOV-2017"                 "13-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12414"                       "20-NOV-2017"                 "20-NOV-2017"                 "17-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12416"                       "22-NOV-2017"                 "22-NOV-2017"                 "21-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12418"                       "24-NOV-2017"                 "24-NOV-2017"                 "23-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12419"                       "27-NOV-2017"                 "27-NOV-2017"                 "23-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12421"                       "29-NOV-2017"                 "29-NOV-2017"                 "28-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12423"                       "01-DEC-2017"                 "01-DEC-2017"                 "04-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12425"                       "05-DEC-2017"                 "05-DEC-2017"                 "04-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12427"                       "07-DEC-2017"                 "07-DEC-2017"                 "04-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12429"                       "11-DEC-2017"                 "11-DEC-2017"                 "07-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12431"                       "13-DEC-2017"                 "13-DEC-2017"                 "12-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12432"                       "14-DEC-2017"                 "14-DEC-2017"                 "14-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12434"                       "18-DEC-2017"                 "22-DEC-2017"                 "12-DEC-2017"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12436"                       "18-DEC-2017"                 "18-DEC-2017"                 "14-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12438"                       "20-DEC-2017"                 "20-DEC-2017"                 "15-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12440"                       "22-DEC-2017"                 "22-DEC-2017"                 "15-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12442"                       "26-DEC-2017"                 "26-DEC-2017"                 "22-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12444"                       "28-DEC-2017"                 "28-DEC-2017"                 "22-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12446"                       "01-JAN-2018"                 "01-JAN-2018"                 "29-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12447"                       "02-JAN-2018"                 "02-JAN-2018"                 "29-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12449"                       "04-JAN-2018"                 "04-JAN-2018"                 "03-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12451"                       "08-JAN-2018"                 "08-JAN-2018"                 "04-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12453"                       "10-JAN-2018"                 "10-JAN-2018"                 "04-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12455"                       "12-JAN-2018"                 "12-JAN-2018"                 "04-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12457"                       "16-JAN-2018"                 "16-JAN-2018"                 "15-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12459"                       "18-JAN-2018"                 "18-JAN-2018"                 "16-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12460"                       "19-JAN-2018"                 "19-JAN-2018"                 "16-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12462"                       "17-JAN-2018"                 "23-JAN-2018"                 ""                            "F"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12464"                       "23-JAN-2018"                 "23-JAN-2018"                 "21-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12466"                       "25-JAN-2018"                 "25-JAN-2018"                 "23-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12468"                       "26-JAN-2018"                 "26-JAN-2018"                 "24-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12470"                       "31-JAN-2018"                 "31-JAN-2018"                 "30-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12472"                       "02-FEB-2018"                 "02-FEB-2018"                 "30-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12392"                       "19-OCT-2017"                 "19-OCT-2017"                 "17-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12394"                       "23-OCT-2017"                 "23-OCT-2017"                 "20-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12396"                       "25-OCT-2017"                 "25-OCT-2017"                 "20-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12398"                       "27-OCT-2017"                 "27-OCT-2017"                 "20-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12400"                       "31-OCT-2017"                 "31-OCT-2017"                 "30-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12402"                       "02-NOV-2017"                 "02-NOV-2017"                 "30-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12404"                       "06-NOV-2017"                 "06-NOV-2017"                 "03-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12407"                       "09-NOV-2017"                 "09-NOV-2017"                 "09-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12409"                       "13-NOV-2017"                 "13-NOV-2017"                 "09-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12411"                       "15-NOV-2017"                 "15-NOV-2017"                 "13-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12413"                       "17-NOV-2017"                 "17-NOV-2017"                 "16-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12415"                       "21-NOV-2017"                 "21-NOV-2017"                 "17-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12417"                       "23-NOV-2017"                 "23-NOV-2017"                 "21-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12420"                       "28-NOV-2017"                 "28-NOV-2017"                 "27-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12422"                       "30-NOV-2017"                 "30-NOV-2017"                 "29-NOV-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12424"                       "04-DEC-2017"                 "04-DEC-2017"                 "01-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12426"                       "06-DEC-2017"                 "06-DEC-2017"                 "04-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12428"                       "08-DEC-2017"                 "08-DEC-2017"                 "04-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12430"                       "12-DEC-2017"                 "12-DEC-2017"                 "12-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12433"                       "15-DEC-2017"                 "15-DEC-2017"                 "12-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12435"                       "25-DEC-2017"                 "29-DEC-2017"                 "12-DEC-2017"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12437"                       "19-DEC-2017"                 "19-DEC-2017"                 "14-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12439"                       "21-DEC-2017"                 "21-DEC-2017"                 "15-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12441"                       "25-DEC-2017"                 "25-DEC-2017"                 "22-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12443"                       "27-DEC-2017"                 "27-DEC-2017"                 "22-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12445"                       "29-DEC-2017"                 "29-DEC-2017"                 "22-DEC-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12448"                       "03-JAN-2018"                 "03-JAN-2018"                 "03-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12450"                       "05-JAN-2018"                 "05-JAN-2018"                 "03-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12452"                       "09-JAN-2018"                 "09-JAN-2018"                 "04-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12454"                       "11-JAN-2018"                 "11-JAN-2018"                 "04-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12456"                       "15-JAN-2018"                 "15-JAN-2018"                 "12-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12458"                       "17-JAN-2018"                 "17-JAN-2018"                 "16-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12461"                       "07-MAY-2018"                 "07-MAY-2018"                 "07-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12463"                       "22-JAN-2018"                 "22-JAN-2018"                 "19-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12465"                       "24-JAN-2018"                 "24-JAN-2018"                 "23-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12467"                       "29-JAN-2018"                 "29-JAN-2018"                 "23-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12469"                       "30-JAN-2018"                 "30-JAN-2018"                 "30-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12471"                       "01-FEB-2018"                 "01-FEB-2018"                 "30-JAN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12608"                       "23-JUL-2018"                 "23-JUL-2018"                 "17-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12611"                       "25-JUL-2018"                 "25-JUL-2018"                 "23-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12613"                       "27-JUL-2018"                 "27-JUL-2018"                 "23-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12615"                       "31-JUL-2018"                 "31-JUL-2018"                 "31-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12618"                       "07-AUG-2018"                 "07-AUG-2018"                 ""                            "D"                           "CO"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12619"                       "21-AUG-2018"                 "21-AUG-2018"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12621"                       "03-SEP-2018"                 "08-SEP-2018"                 ""                            "W"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12623"                       "03-SEP-2018"                 "08-SEP-2018"                 "29-AUG-2018"                 "F"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12625"                       "11-SEP-2018"                 "17-SEP-2018"                 ""                            "F"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12491"                       "21-FEB-2018"                 "21-FEB-2018"                 "20-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12493"                       "23-FEB-2018"                 "23-FEB-2018"                 "16-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12495"                       "26-FEB-2018"                 "26-FEB-2018"                 "26-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12498"                       "01-MAR-2018"                 "01-MAR-2018"                 "26-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12500"                       "10-MAR-2018"                 "10-MAR-2018"                 "27-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12502"                       "03-MAR-2018"                 "03-MAR-2018"                 "27-FEB-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12504"                       "05-MAR-2018"                 "05-MAR-2018"                 "01-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12506"                       "07-MAR-2018"                 "07-MAR-2018"                 "06-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12508"                       "09-MAR-2018"                 "09-MAR-2018"                 "01-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12511"                       "14-MAR-2018"                 "14-MAR-2018"                 "14-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12513"                       "16-MAR-2018"                 "16-MAR-2018"                 "15-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12515"                       "18-MAR-2018"                 "18-MAR-2018"                 "16-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12517"                       "20-MAR-2018"                 "20-MAR-2018"                 "16-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12519"                       "22-MAR-2018"                 "22-MAR-2018"                 "16-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12521"                       "24-MAR-2018"                 "24-MAR-2018"                 "23-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12523"                       "26-MAR-2018"                 "26-MAR-2018"                 "23-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12526"                       "29-MAR-2018"                 "29-MAR-2018"                 "28-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12528"                       "03-APR-2018"                 "03-APR-2018"                 "29-MAR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12530"                       "05-APR-2018"                 "05-APR-2018"                 "05-APR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12532"                       "09-APR-2018"                 "09-APR-2018"                 "05-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12534"                       "11-APR-2018"                 "11-APR-2018"                 "05-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12536"                       "13-APR-2018"                 "13-APR-2018"                 "10-APR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12539"                       "18-APR-2018"                 "18-APR-2018"                 "12-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12541"                       "20-APR-2018"                 "20-APR-2018"                 "12-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12543"                       "24-APR-2018"                 "24-APR-2018"                 "20-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12545"                       "26-APR-2018"                 "26-APR-2018"                 "25-APR-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12547"                       "30-APR-2018"                 "30-APR-2018"                 "27-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12549"                       "02-MAY-2018"                 "02-MAY-2018"                 "27-APR-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12552"                       "14-MAY-2018"                 "14-MAY-2018"                 "10-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12554"                       "16-MAY-2018"                 "16-MAY-2018"                 "15-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12556"                       "18-MAY-2018"                 "18-MAY-2018"                 "10-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12558"                       "22-MAY-2018"                 "22-MAY-2018"                 "21-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12560"                       "24-MAY-2018"                 "24-MAY-2018"                 "18-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12562"                       "29-MAY-2018"                 "29-MAY-2018"                 "29-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12564"                       "31-MAY-2018"                 "31-MAY-2018"                 "29-MAY-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12567"                       "04-JUN-2018"                 "04-JUN-2018"                 "04-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12569"                       "07-JUN-2018"                 "07-JUN-2018"                 "06-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12571"                       "06-JUN-2018"                 "06-JUN-2018"                 "05-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12573"                       "11-JUN-2018"                 "11-JUN-2018"                 "13-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12575"                       "13-JUN-2018"                 "13-JUN-2018"                 "08-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12577"                       "15-JUN-2018"                 "15-JUN-2018"                 "08-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12580"                       "13-AUG-2018"                 "17-AUG-2018"                 "12-JUL-2018"                 "F"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12582"                       "19-JUN-2018"                 "19-JUN-2018"                 "19-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12584"                       "21-JUN-2018"                 "21-JUN-2018"                 "20-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12586"                       "25-JUN-2018"                 "25-JUN-2018"                 "20-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12022"                       "19-MAY-2008"                 "19-MAY-2008"                 "19-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12024"                       "21-MAY-2008"                 "21-MAY-2008"                 "21-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12026"                       "23-MAY-2008"                 "23-MAY-2008"                 "23-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12028"                       "23-MAY-2008"                 "29-MAY-2008"                 ""                            "W"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12030"                       "29-MAY-2008"                 "29-MAY-2008"                 "29-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12032"                       "02-JUN-2008"                 "02-JUN-2008"                 "30-MAY-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12035"                       "05-JUN-2008"                 "05-JUN-2008"                 "04-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12037"                       "09-JUN-2008"                 "09-JUN-2008"                 "06-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12039"                       "11-JUN-2008"                 "11-JUN-2008"                 "10-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12041"                       "13-JUN-2008"                 "13-JUN-2008"                 "12-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12043"                       "23-JUN-2008"                 "23-JUN-2008"                 "20-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12045"                       "25-JUN-2008"                 "25-JUN-2008"                 "24-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "7"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12048"                       "01-JUL-2008"                 "01-JUL-2008"                 "30-JUN-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12050"                       "11-JUL-2008"                 "11-JUL-2008"                 "11-JUL-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12052"                       "16-JUL-2008"                 "16-JUL-2008"                 "09-SEP-2008"                 "D"                           "CR"                          "FINAL"                       "4"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12054"                       "24-SEP-2008"                 "24-SEP-2008"                 "24-SEP-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12056"                       "26-SEP-2008"                 "26-SEP-2008"                 "26-SEP-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12058"                       "14-OCT-2008"                 "14-OCT-2008"                 "13-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12061"                       "17-OCT-2008"                 "17-OCT-2008"                 "16-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12063"                       "23-OCT-2008"                 "23-OCT-2008"                 "23-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12065"                       "27-OCT-2008"                 "27-OCT-2008"                 "24-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12067"                       "24-OCT-2008"                 "30-OCT-2008"                 "24-OCT-2008"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12069"                       "29-OCT-2008"                 "29-OCT-2008"                 "29-OCT-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12071"                       "03-NOV-2008"                 "03-NOV-2008"                 "03-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12073"                       "04-NOV-2008"                 "04-NOV-2008"                 "03-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12076"                       "06-NOV-2008"                 "06-NOV-2008"                 "05-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12078"                       "10-NOV-2008"                 "10-NOV-2008"                 "07-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12080"                       "13-NOV-2008"                 "13-NOV-2008"                 "12-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12082"                       "17-NOV-2008"                 "17-NOV-2008"                 "14-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12084"                       "19-NOV-2008"                 "19-NOV-2008"                 "18-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12087"                       "24-NOV-2008"                 "24-NOV-2008"                 "20-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12090"                       "27-NOV-2008"                 "27-NOV-2008"                 "26-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12092"                       "21-NOV-2008"                 "21-NOV-2008"                 "20-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12094"                       "23-NOV-2008"                 "23-NOV-2008"                 "21-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12096"                       "01-DEC-2008"                 "01-DEC-2008"                 "28-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12099"                       "01-DEC-2008"                 "05-DEC-2008"                 "04-DEC-2008"                 "F"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12101"                       "30-NOV-2008"                 "30-NOV-2008"                 "28-NOV-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12103"                       "04-DEC-2008"                 "04-DEC-2008"                 "04-DEC-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12106"                       "06-DEC-2008"                 "06-DEC-2008"                 "01-APR-2009"                 "W"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12108"                       "23-DEC-2008"                 "23-DEC-2008"                 "23-DEC-2008"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12110"                       "06-JAN-2009"                 "06-JAN-2009"                 "07-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12112"                       "08-JAN-2009"                 "08-JAN-2009"                 "07-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12114"                       "09-JAN-2009"                 "09-JAN-2009"                 "15-JAN-2009"                 "F"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12116"                       "12-JAN-2009"                 "12-JAN-2009"                 "12-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12119"                       "15-JAN-2009"                 "15-JAN-2009"                 "15-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12121"                       "17-JAN-2009"                 "17-JAN-2009"                 "16-JAN-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12140"                       "13-FEB-2009"                 "13-FEB-2009"                 "13-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12142"                       "17-FEB-2009"                 "17-FEB-2009"                 "16-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12144"                       "20-FEB-2009"                 "20-FEB-2009"                 "16-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12146"                       "24-FEB-2009"                 "24-FEB-2009"                 "20-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12149"                       "27-FEB-2009"                 "27-FEB-2009"                 "26-FEB-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12151"                       "03-MAR-2009"                 "03-MAR-2009"                 "02-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12153"                       "05-MAR-2009"                 "05-MAR-2009"                 "04-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12155"                       "09-MAR-2009"                 "09-MAR-2009"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12157"                       "11-MAR-2009"                 "11-MAR-2009"                 "10-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12159"                       "13-MAR-2009"                 "13-MAR-2009"                 "12-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "3"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12161"                       "12-MAR-2009"                 "18-MAR-2009"                 "12-MAR-2009"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12163"                       "18-MAR-2009"                 "18-MAR-2009"                 "18-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12166"                       "23-MAR-2009"                 "23-MAR-2009"                 "23-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12588"                       "27-JUN-2018"                 "27-JUN-2018"                 "22-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12589"                       "28-JUN-2018"                 "28-JUN-2018"                 "22-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12590"                       "29-JUN-2018"                 "29-JUN-2018"                 "26-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12591"                       "02-JUL-2018"                 "02-JUL-2018"                 "29-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12592"                       "03-JUL-2018"                 "03-JUL-2018"                 "29-JUN-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12593"                       "04-JUL-2018"                 "04-JUL-2018"                 "03-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12594"                       "05-JUL-2018"                 "05-JUL-2018"                 "03-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12595"                       "06-JUL-2018"                 "06-JUL-2018"                 "05-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "4"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12596"                       "09-JUL-2018"                 "09-JUL-2018"                 "06-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12597"                       "10-JUL-2018"                 "10-JUL-2018"                 "06-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12598"                       "16-JUL-2018"                 "20-JUL-2018"                 ""                            "W"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12599"                       "11-JUL-2018"                 "11-JUL-2018"                 "10-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12600"                       "12-JUL-2018"                 "12-JUL-2018"                 "06-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12601"                       "13-JUL-2018"                 "13-JUL-2018"                 "12-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12602"                       "16-JUL-2018"                 "16-JUL-2018"                 "16-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12603"                       "16-JUL-2018"                 "16-JUL-2018"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12604"                       "17-JUL-2018"                 "17-JUL-2018"                 "16-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12605"                       "18-JUL-2018"                 "18-JUL-2018"                 "16-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12606"                       "19-JUL-2018"                 "19-JUL-2018"                 "18-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12607"                       "20-JUL-2018"                 "20-JUL-2018"                 "16-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12610"                       "24-JUL-2018"                 "24-JUL-2018"                 "23-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12612"                       "26-JUL-2018"                 "26-JUL-2018"                 "24-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12614"                       "30-JUL-2018"                 "30-JUL-2018"                 "27-JUL-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12616"                       "02-AUG-2018"                 "02-AUG-2018"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12617"                       "06-AUG-2018"                 "06-AUG-2018"                 "30-AUG-2018"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12620"                       "25-AUG-2018"                 "25-AUG-2018"                 "21-AUG-2018"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12622"                       "29-AUG-2018"                 "29-AUG-2018"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12624"                       "05-SEP-2018"                 "05-SEP-2018"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12626"                       "24-SEP-2018"                 "28-SEP-2018"                 ""                            "F"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12168"                       "20-MAR-2009"                 "26-MAR-2009"                 "20-MAR-2009"                 "F"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12170"                       "24-MAR-2009"                 "24-MAR-2009"                 "23-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12172"                       "26-MAR-2009"                 "26-MAR-2009"                 "25-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12174"                       "28-MAR-2009"                 "28-MAR-2009"                 "26-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12176"                       "27-MAR-2009"                 "02-APR-2009"                 ""                            "W"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12178"                       "31-MAR-2009"                 "31-MAR-2009"                 ""                            "D"                           "CO"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12180"                       "31-MAR-2009"                 "31-MAR-2009"                 ""                            "F"                           "CO"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12182"                       "01-APR-2009"                 "01-APR-2009"                 "31-MAR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12184"                       "03-APR-2009"                 "03-APR-2009"                 "03-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12186"                       "07-APR-2009"                 "07-APR-2009"                 "07-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12188"                       "09-APR-2009"                 "09-APR-2009"                 "09-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12191"                       "16-APR-2009"                 "16-APR-2009"                 "15-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12193"                       "20-APR-2009"                 "20-APR-2009"                 "20-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12195"                       "22-APR-2009"                 "22-APR-2009"                 "22-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12197"                       "24-APR-2009"                 "24-APR-2009"                 "24-APR-2009"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12199"                       "29-APR-2009"                 "29-APR-2009"                 "28-APR-2009"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12201"                       "27-MAY-2009"                 "27-MAY-2009"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12204"                       "31-AUG-2009"                 "31-AUG-2009"                 ""                            "D"                           "CR"                          ""                            ""                            ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12208"                       "26-MAY-2011"                 "26-MAY-2011"                 "25-MAY-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12209"                       "27-MAY-2011"                 "27-MAY-2011"                 "27-MAY-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12211"                       "02-JUN-2011"                 "02-JUN-2011"                 "27-MAY-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12213"                       "07-JUN-2011"                 "07-JUN-2011"                 "07-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12216"                       "10-JUN-2011"                 "10-JUN-2011"                 "10-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12218"                       "14-JUN-2011"                 "14-JUN-2011"                 "13-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12220"                       "16-JUN-2011"                 "16-JUN-2011"                 "15-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12222"                       "20-JUN-2011"                 "20-JUN-2011"                 "17-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12224"                       "22-JUN-2011"                 "22-JUN-2011"                 "21-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12226"                       "24-JUN-2011"                 "24-JUN-2011"                 "23-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12229"                       "29-JUN-2011"                 "29-JUN-2011"                 "28-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12231"                       "01-JUL-2011"                 "01-JUL-2011"                 "30-JUN-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12233"                       "05-JUL-2011"                 "05-JUL-2011"                 "04-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12235"                       "07-JUL-2011"                 "07-JUL-2011"                 "06-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12237"                       "11-JUL-2011"                 "11-JUL-2011"                 "11-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12239"                       "15-JUL-2011"                 "15-JUL-2011"                 "12-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12241"                       "19-JUL-2011"                 "19-JUL-2011"                 "18-JUL-2011"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12260"                       "04-MAR-2013"                 "04-MAR-2013"                 "01-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12262"                       "06-MAR-2013"                 "06-MAR-2013"                 "05-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12264"                       "08-MAR-2013"                 "08-MAR-2013"                 "07-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12267"                       "13-MAR-2013"                 "13-MAR-2013"                 "11-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12269"                       "14-MAR-2013"                 "14-MAR-2013"                 "12-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12271"                       "18-MAR-2013"                 "18-MAR-2013"                 "14-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12273"                       "22-MAR-2013"                 "22-MAR-2013"                 "21-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12275"                       "26-MAR-2013"                 "26-MAR-2013"                 "25-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12277"                       "28-MAR-2013"                 "28-MAR-2013"                 "28-MAR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12280"                       "05-APR-2013"                 "05-APR-2013"                 "04-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12282"                       "09-APR-2013"                 "09-APR-2013"                 "08-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12284"                       "08-APR-2013"                 "12-APR-2013"                 "08-APR-2013"                 "W"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12286"                       "11-APR-2013"                 "11-APR-2013"                 "10-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12288"                       "15-APR-2013"                 "15-APR-2013"                 "15-APR-2013"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12290"                       "17-APR-2013"                 "17-APR-2013"                 "17-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12293"                       "23-APR-2013"                 "23-APR-2013"                 "23-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12295"                       "26-APR-2013"                 "26-APR-2013"                 "26-APR-2013"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12297"                       "17-APR-2014"                 "17-APR-2014"                 "17-APR-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12299"                       "29-APR-2014"                 "29-APR-2014"                 "29-APR-2014"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12301"                       "01-MAY-2014"                 "01-MAY-2014"                 "29-APR-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12303"                       "08-MAY-2014"                 "08-MAY-2014"                 "08-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12305"                       "12-MAY-2014"                 "12-MAY-2014"                 "12-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12308"                       "15-MAY-2014"                 "15-MAY-2014"                 "12-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12310"                       "19-MAY-2014"                 "19-MAY-2014"                 "16-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12312"                       "21-MAY-2014"                 "21-MAY-2014"                 "16-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12314"                       "23-MAY-2014"                 "23-MAY-2014"                 "16-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12316"                       "29-MAY-2014"                 "29-MAY-2014"                 "28-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12318"                       "02-JUN-2014"                 "02-JUN-2014"                 "28-MAY-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12321"                       "05-JUN-2014"                 "05-JUN-2014"                 "04-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12323"                       "09-JUN-2014"                 "09-JUN-2014"                 "09-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12325"                       "13-JUN-2014"                 "13-JUN-2014"                 "12-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12327"                       "18-JUN-2014"                 "18-JUN-2014"                 "17-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12329"                       "20-JUN-2014"                 "20-JUN-2014"                 "17-JUN-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12331"                       "28-AUG-2014"                 "28-AUG-2014"                 "28-AUG-2014"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12333"                       "31-MAY-2017"                 "31-MAY-2017"                 "31-MAY-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12336"                       "05-JUN-2017"                 "05-JUN-2017"                 "01-JUN-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12338"                       "26-JUN-2017"                 "26-JUN-2017"                 "23-JUN-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12340"                       "28-JUN-2017"                 "28-JUN-2017"                 "27-JUN-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12342"                       "10-JUL-2017"                 "10-JUL-2017"                 "06-JUL-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12344"                       "19-JUL-2017"                 "19-JUL-2017"                 "19-JUL-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12346"                       "21-JUL-2017"                 "21-JUL-2017"                 "21-JUL-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12349"                       "03-AUG-2017"                 "03-AUG-2017"                 "03-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "6"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12351"                       "18-AUG-2017"                 "18-AUG-2017"                 "17-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12353"                       "23-AUG-2017"                 "23-AUG-2017"                 "22-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12355"                       "25-AUG-2017"                 "25-AUG-2017"                 "24-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12357"                       "29-AUG-2017"                 "29-AUG-2017"                 "29-AUG-2017"                 "D"                           "CR"                          "FINAL"                       "2"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12376"                       "27-SEP-2017"                 "27-SEP-2017"                 "25-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12379"                       "02-OCT-2017"                 "02-OCT-2017"                 "28-SEP-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12381"                       "04-OCT-2017"                 "04-OCT-2017"                 "03-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12383"                       "06-OCT-2017"                 "06-OCT-2017"                 "03-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12385"                       "10-OCT-2017"                 "10-OCT-2017"                 "09-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12387"                       "12-OCT-2017"                 "12-OCT-2017"                 "10-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            
"12389"                       "16-OCT-2017"                 "16-OCT-2017"                 "13-OCT-2017"                 "D"                           "CR"                          "FINAL"                       "1"                           ""                            "453"                         "81"                          "I"                           ""                            "07-SEP-2018"                 "07-SEP-2018"                 ""                            




rollback;	
	