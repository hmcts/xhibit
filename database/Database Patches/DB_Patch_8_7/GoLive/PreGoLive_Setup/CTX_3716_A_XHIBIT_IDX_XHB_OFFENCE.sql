/*************************************************************************************************/
/*  CTX_3716 : STEP 2 - Creating required Indexes on XHIBIT.XHB_OFFENCE  table        */
/*             Login as USER : XHIBIT                                                            */
/*************************************************************************************************/
/*************** Create indexes on XHB_OFFENCE **************/
BEGIN
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('CTX_3716 : Step 2 - Creating INDEXES on XHBIBIT table XHB_OFFENCE');
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('                                                                          ');
END;
/
create index XHB_OFFENCE_DM_IDX on XHB_OFFENCE (CREST_OFFENCE_ID,OBS_IND,OFFENCE_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_OFFENCE_DM_IDX on XHB_OFFENCE (CREST_OFFENCE_ID,OBS_IND,OFFENCE_ID) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
END;
/

