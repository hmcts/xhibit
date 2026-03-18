/*************************************************************************************************/
/*  CTX_3777 : Creating required Indexes on XHIBIT Tables  for better performance                */
/*             Login as USER : XHIBIT                                                            */
/*************************************************************************************************/
BEGIN
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('CTX_3777 : Creating INDEXES on XHBIBIT tables ');
DBMS_OUTPUT.PUT_LINE('############################################################################');
DBMS_OUTPUT.PUT_LINE('                                                                          ');
END;
/
create index XHB_SCH_H_TRUNC_NOT_BEFTIM_IDX on XHB_SCHEDULED_HEARING (TRUNC(NOT_BEFORE_TIME))
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_SCH_H_NOT_BEFORE_TIME_IDX on XHB_SCHEDULED_HEARING (TRUNC(NOT_BEFORE_TIME))) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index XHB_CLOG_CAT_DESC_ID_IDX on XHB_COURT_LOG_CATEGORY_DESC(CATEGORY_DESCRIPTION,CATEGORY_DESC_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_CLOG_CAT_DESC_ID_IDX on XHB_COURT_LOG_CATEGORY_DESC(CATEGORY_DESCRIPTION,CATEGORY_DESC_ID) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index XHB_CLOG_ENTRY_CASEID_EDESCIDX on XHB_COURT_LOG_ENTRY(CASE_ID,EVENT_DESC_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_CLOG_ENTRY_CASEID_EDESCIDX on XHB_COURT_LOG_ENTRY(CASE_ID,EVENT_DESC_ID) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index XHB_HEARING_CASEID_HEARID_IDX on XHB_HEARING(CASE_ID,HEARING_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_HEARING_CASEID_HEARID_IDX on XHB_HEARING(CASE_ID,HEARING_ID) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index AUD_SCH_HEAR_HID_SCH_HID_IDX on AUD_SCHEDULED_HEARING(HEARING_ID,SCHEDULED_HEARING_ID)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'AUD_SCH_HEAR_HID_SCH_HID_IDX on AUD_SCHEDULED_HEARING(HEARING_ID,SCHEDULED_HEARING_ID) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index XHB_SCH_HEAR_NOT_BEFTIME_IDX on XHB_SCHEDULED_HEARING(NOT_BEFORE_TIME)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_SCH_HEAR_NOT_BEFTIME_IDX on XHB_SCHEDULED_HEARING(NOT_BEFORE_TIME) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index XHB_CLOGENTRY_CASEID_DTIME_IDX on XHB_COURT_LOG_ENTRY(CASE_ID,DATE_TIME)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_CLOGENTRY_CASEID_DTIME_IDX on XHB_COURT_LOG_ENTRY(CASE_ID,DATE_TIME) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
create index WMB_MONIT_MONDATE_STATUS_IDX on WMB_MONITORING(MONITORING_DATE,STATUS)
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'WMB_MONIT_MONDATE_STATUS_IDX on WMB_MONITORING(MONITORING_DATE,STATUS) - Created');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
BEGIN
DBMS_OUTPUT.PUT_LINE('#########################################################################################');
END;
/

