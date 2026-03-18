/*************************************************************************************************/
/*  CTX_3727 : STEP 2 - Creating required Indexes on XHIBIT  tables                              */
/*             Login as USER : XHIBIT                                                            */
/*************************************************************************************************/
BEGIN
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('CTX_3727 : Step 2 - Creating INDEXES on XHBIBIT tables');
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('                                                                          ');
END;
/
create index XHB_DOC_HIST_COMP1_IDX on XHB_DEFENDANT_ON_CASE_HISTORY (DEFENDANT_HISTORY_ID,Case_History_Id,DEFENDANT_NUMBER)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_DOC_HIST_COMP1_IDX ON XHB_DEFENDANT_ON_CASE_HISTORY (DEFENDANT_HISTORY_ID,Case_History_Id,DEFENDANT_NUMBER) - Created');
END;
/
create index XHB_DH_CID_CDID_DHIST_IDX on XHB_DEFENDANT_HISTORY (court_id,crest_defendant_id,DEFENDANT_HISTORY_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_DH_CID_CDID_DHIST_IDX ON XHB_DEFENDANT_HISTORY (court_id,crest_defendant_id,DEFENDANT_HISTORY_ID) - Created');
END;
/
create index XHB_CH_CID_CNO_CTYPE_CHIS_IDX on XHB_CASE_HISTORY (court_id,Case_Number,Case_Type,Case_History_Id)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_CH_CID_CNO_CTYPE_CHIS_IDX on XHB_CASE_HISTORY (court_id,Case_Number,Case_Type,Case_History_Id) - Created');
END;
/
BEGIN
DBMS_OUTPUT.PUT_LINE('                                                                                ');
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
END;
/

