/*************************************************************************************************/
/*                                                                                               */
/*  14/02/2019 - S.Sethuraman  - Script created                                                  */
/*                                                                                               */
/*  CTX_3716 : Additional indexes recommended from the Live DRY RUN 09th Feb   added             */
/*                                                                                               */
/*  Schema : DATA_MIG - user DATA_MIG
/*************************************************************************************************/
/*************** Recreate XHBSTG_BW_HISTORY_DM with List Partition on Crest_Court_ID *************/
BEGIN
    DBMS_OUTPUT.ENABLE(1000000);
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('CTX_3716 : adding recommended Additional Indexes for XHBSTG tables                       ');
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Adding Additional indexes for XHBSTG tables');
END;
/
CREATE INDEX CASE_HD_LT_IDX ON XHBSTG_CASE_HEARING_DAY_DM(List_Type) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CASE_HD_LT_IDX ON XHBSTG_CASE_HEARING_DAY_DM(List_Type) LOCAL - Created');
END;
/
CREATE INDEX CASE_HD_CT_IDX ON XHBSTG_CASE_HEARING_DAY_DM(Case_Type) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CASE_HD_CT_IDX ON XHBSTG_CASE_HEARING_DAY_DM(Case_Type) LOCAL - Created');
END;
/
CREATE INDEX CASE_HD_CC_IDX ON XHBSTG_CASE_HEARING_DAY_DM(CTD_ID,CASE_NO) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CASE_HD_CC_IDX ON XHBSTG_CASE_HEARING_DAY_DM(CTD_ID,CASE_NO) LOCAL - Created');
END;
/
CREATE INDEX CASE_HD_CTC_IDX ON XHBSTG_CASE_HEARING_DAY_DM(CASE_TYPE,CASE_NO) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CASE_HD_CTC_IDX ON XHBSTG_CASE_HEARING_DAY_DM(CASE_TYPE,CASE_NO) LOCAL - Created');
END;
/
CREATE INDEX CASE_HD_CLLCC_IDX ON XHBSTG_CASE_HEARING_DAY_DM(CTD_ID,LIST_TYPE,LIST_DATE,CASE_NO,CASE_TYPE) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CASE_HD_CLLCC_IDX ON XHBSTG_CASE_HEARING_DAY_DM(CTD_ID,LIST_TYPE,LIST_DATE,CASE_NO,CASE_TYPE) LOCAL - Created');
END;
/
CREATE INDEX COURTROOM_DAY_IDX ON XHBSTG_COURTROOM_DAY_DM(CTD_ID) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') || ': ' || 'COURTROOM_DAY_IDX ON XHBSTG_COURTROOM_DAY_DM(CTD_ID) LOCAL - Created');
END;
/
CREATE INDEX COURTROOM_DAY_CCLLJ_IDX ON XHBSTG_COURTROOM_DAY_DM(CTD_ID,Courtroom_No,List_Date,list_type,Jud_Seq_No) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') || ': ' || 'COURTROOM_DAY_CCLLJ_IDX ON XHBSTG_COURTROOM_DAY_DM(CTD_ID,Courtroom_No,List_Date,list_type,Jud_Seq_No) LOCAL - Created');
END;
/
CREATE INDEX CASE_NOTE_IDX ON XHBSTG_CASE_NOTE_DM(CASE_NO,CASE_TYPE,XHIBIT_ETL_STATUS,XHIBIT_ENRICH_DATE) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') || ': ' || 'CASE_NOTE_IDX ON XHBSTG_CASE_NOTE_DM(CASE_NO,CASE_TYPE,XHIBIT_ETL_STATUS,XHIBIT_ENRICH_DATE) LOCAL - Created');
END;
/
CREATE INDEX CHG_DM_IDX ON XHBSTG_CHARGE_DM(XHIBIT_ENRICH_DATE,XHIBIT_ETL_STATUS,chg_id) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') || ': ' || 'CHG_DM_IDX ON XHBSTG_CHARGE_DM(XHIBIT_ENRICH_DATE,XHIBIT_ETL_STATUS,CHG_ID) LOCAL - Created');
END;
/
/********** CTX_3716 End   *********/

BEGIN
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTX_3716 : Necessary XHBSTG LOCAL indexes created Successfully');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
