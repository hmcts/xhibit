/*************************************************************************************************/
/*  CTX_3717 : Creating required Indexes on XHIBIT AUD Tables       */
/*             Login as USER : XHIBIT                                                            */
/*************************************************************************************************/
/*************** Create indexes on AUD_aDDRESS, AUD_DEFENDANT,AUD_DEFENDANT_ON_CASE **************/
BEGIN
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('CTX_3717 : Creating INDEXES on XHBIBIT AUD tables AUD_ADDRESS,AUD_DEFENDANT,AUD_DEFENDANT_ON_CASE');
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('                                                                          ');
END;
/
create index AUD_ADDRESS_IDX on Aud_Address (ADDRESS_ID,Version)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'AUD_ADDRESS_IDX on Aud_Address (ADDRESS_ID,Version) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index AUD_DEF_IDX on Aud_Defendant(DEFENDANT_ID,Version)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'AUD_DEF_IDX on Aud_Defendant (DEFENDANT_ID,Version) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index AUD_DEFONCASE_IDX on aud_defendant_on_case (DEFENDANT_ON_CASE_ID,Version)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'AUD_DEF_IDX on Aud_Defendant_on_Case (DEFENDANT_ON_CASE_ID,Version)) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
BEGIN
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
END;
/

