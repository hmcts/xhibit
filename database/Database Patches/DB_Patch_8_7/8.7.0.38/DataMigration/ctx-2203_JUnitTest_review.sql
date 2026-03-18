/*Run initial cursor*/
 SELECT cjt.crest_court_id,
           xhc.court_id,
           xrj.ref_judge_id as judge_id,
           cjt.ticket_type,
           cjt.jud_id
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_judge_ticket_dm cjt,
         xhibit.xhb_ref_judge xrj
    where cjt.crest_court_id = :p_crest_court_id and
          cjt.crest_court_id = xhc.crest_court_id and
          xrj.court_id = xhc.court_id and
          xrj.crest_judge_id = cjt.jud_id and   
          NVL(cjt.xhibit_etl_status,'N') not in ('I', 'U') and
          cjt.xhibit_enrich_date is NULL  and
          NOT exists 
          (select 'X' from xhibit.xhb_ref_judge_ticket
            where court_id = xhc.court_id and 
                  judge_id = xrj.ref_judge_id and 
                  nvl(obs_ind,'N') != 'Y');

--14 rows returned
"CREST_COURT_ID"              "COURT_ID"                    "JUDGE_ID"                    "TICKET_TYPE"                 "JUD_ID"                      
"453"                         "81"                          "34938"                       "MUR"                         "782"                         
"453"                         "81"                          "35615"                       "ATT"                         "823"                         
"453"                         "81"                          "35615"                       "FRA"                         "823"                         
"453"                         "81"                          "35615"                       "RAP"                         "823"                         
"453"                         "81"                          "34828"                       "ATT"                         "636"                         
"453"                         "81"                          "34620"                       "MUR"                         "345"                         
"453"                         "81"                          "35616"                       "MUR"                         "824"                         
"453"                         "81"                          "35616"                       "RAP"                         "824"                         
"453"                         "81"                          "34796"                       "MUR"                         "595"                         
"453"                         "81"                          "34796"                       "RAP"                         "595"                         
"453"                         "81"                          "36861"                       "ATT"                         "825"                         
"453"                         "81"                          "36861"                       "FRA"                         "825"                         
"453"                         "81"                          "36861"                       "MUR"                         "825"                         
"453"                         "81"                          "36861"                       "RAP"                         "825"                         

SELECT count(*)
FROM xhibit.xhb_ref_judge_ticket
WHERE court_id = 81; --xhibit court_id

--60 rows.  After running there should be 74 rows (60+14)

--expect 14 rows to be inserted
DECLARE
 BEGIN dm_process_pkg_cc.upd_xhb_ref_jud_tckt_crest(p_crest_court_id => 453);
END;


--DBMS_OUTPUT
CREST - COURT : 453 - Starting process of inserting new rows in XHB_CHARGES_LOG with the required data to be populated from CREST
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 34938, v_jud_id : 782 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 35615, v_jud_id : 823 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 3
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 35615, v_jud_id : 823 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 3
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 35615, v_jud_id : 823 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 3
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 34828, v_jud_id : 636 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 34620, v_jud_id : 345 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 35616, v_jud_id : 824 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 2
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 35616, v_jud_id : 824 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 2
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 34796, v_jud_id : 595 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 2
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 34796, v_jud_id : 595 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 2
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 36861, v_jud_id : 825 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 4
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 36861, v_jud_id : 825 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 4
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 36861, v_jud_id : 825 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 4
 
INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = 453
v_judge_id : 36861, v_jud_id : 825 , v_xhibit_court_id 81
 
CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : 453 - Updated no of rows : 4
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM processed 14 for CREST_COURT_ID : 453 successfully!
 
CTX-2203:XHBSTG_JUDGE_TICKET_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - 0 for CREST_COURT_ID : 453

--re-check the count
SELECT count(*)
FROM xhibit.xhb_ref_judge_ticket
WHERE court_id = 81; --xhibit court_id

--74 rows returned as expected


rollback;	
	