/*************************************************************************************************/
/*  CTX_3503 : STEP 2 - Creating required Indexes on XHIBIT.XHB_CASE_LISTING_ENTRY  table        */
/*             Login as USER : XHIBIT                                                            */
/*************************************************************************************************/
/*************** Create indexes on XHB_CASE_LISTING_ENTRY **************/
BEGIN
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('CTX_3503 : Step 2 - Creating INDEXES on XHBIBIT table XHB_CASE_LISTING_ENTRY');
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('                                                                          ');
END;
/
create index XHB_CLE_CASE_COURT_IDX on XHB_CASE_LISTING_ENTRY (CASE_ID,COURT_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_CLE_CASE_COURT_IDX on XHB_CASE_LISTING_ENTRY (CASE_ID,COURT_ID) - Created');
END;
/
create index XHB_CLE_CASE_CLE_IDX on XHB_CASE_LISTING_ENTRY (CASE_ID,CASE_LISTING_ENTRY_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_CLE_CASE_CLE_IDX on XHB_CASE_LISTING_ENTRY (CASE_ID,CASE_LISTING_ENTRY_ID) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
END;
/

