/*************************************************************************************************/
/*  CTX_3807 / CTX-3819 : Creating required Indexes on XHIBIT Tables  for better performance                */
/*             Login as USER : XHIBIT                                                            */
/*************************************************************************************************/
BEGIN
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('CTX_3807-CTX-3819 : Creating INDEXES on XHBIBIT tables ');
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('                                                                          ');
END;
/
create index XHB_SCH_H_ADDHU_NOT_BEFTIM_IDX on XHB_SCHEDULED_HEARING (Add_Hearing_Used,NOT_BEFORE_TIME)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_SCH_H_ADDHU_NOT_BEFTIM_IDX on XHB_SCHEDULED_HEARING (Add_Hearing_Used,NOT_BEFORE_TIME) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index XHB_HEARING_HID_COURTID_IDX on XHB_HEARING(HEARING_ID,Court_Id)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_HEARING_HID_COURTID_IDX on XHB_HEARING(HEARING_ID,Court_Id) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
DROP INDEX XHB_OFFENCE_DM_IDX
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Index XHB_OFFENCE_DM_IDX - Dropped');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index XHB_OFFENCE_DM_IDX on XHB_OFFENCE(Crest_Offence_Id,Obs_Ind,Charge_Id,OFFENCE_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_OFFENCE_DM_IDX on XHB_OFFENCE(Crest_Offence_Id,Obs_Ind,Charge_Id,OFFENCE_ID) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
BEGIN
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
END;
/

