/*************************************************************************************************/
/*                                                                                               */
/*  21/02/2019 - S.Sethuraman  - Script created                                                  */
/*                                                                                               */
/*  CTX_3727 : Additional indexes recommended from the Live DRY RUN 16th Feb   added             */
/*                                                                                               */
/*  Schema : DATA_MIG - user DATA_MIG
/*************************************************************************************************/
BEGIN
    DBMS_OUTPUT.ENABLE(1000000);
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('CTX_3727 : adding recommended Additional Indexes for XHBSTG tables                       ');
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Adding Additional indexes for XHBSTG tables');
END;
/
CREATE INDEX CHD_LT_CTD_IDX ON XHBSTG_CASE_HEARING_DAY_DM(List_Type,CTD_ID) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CHD_LT_CTD_IDX ON XHBSTG_CASE_HEARING_DAY_DM(List_Type,CTD_ID) LOCAL - Created');
END;
/
CREATE INDEX CHD_COMP1_IDX ON XHBSTG_CASE_HEARING_DAY_DM(List_type,Ctd_Id,Xhibit_Etl_Status,Xhibit_Enrich_Date,List_Date,Chd_Site_Code) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CHD_COMP1_IDX ON XHBSTG_CASE_HEARING_DAY_DM(List_type,Ctd_Id,Xhibit_Etl_Status,Xhibit_Enrich_Date,List_Date,Chd_Site_Code) LOCAL - Created');
END;
/
CREATE INDEX CTD_CNO_LDATE_LT_JSEQ_IDX ON XHBSTG_COURTROOM_DAY_DM(courtroom_no,List_Date,List_Type,Jud_Seq_No) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTD_CNO_LDATE_LT_JSEQ_IDX ON XHBSTG_COURTROOM_DAY_DM(courtroom_no,List_Date,List_Type,Jud_Seq_No) LOCAL - Created');
END;
/
CREATE INDEX NAD_CNO_CTYPE_IDX ON XHBSTG_NON_AVAIL_DATES_DM(CASE_NO,CASE_TYPE) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'NAD_CNO_CTYPE_IDX ON ON XHBSTG_NON_AVAIL_DATES_DM(CASE_NO,CASE_TYPE) LOCAL - Created');
END;
/
CREATE INDEX CPSOF_CPFID_IDX ON XHBSTG_CASE_PARTY_SOF_DM(CPF_ID) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CPSOF_CPFID_IDX ON XHBSTG_CASE_PARTY_SOF_DM(CPF_ID) LOCAL - Created');
END;
/
/********** Also add COURT_ID index on XHBSTG_DATA_MIGRATION_LOG table - just index NOT LOCAL index as NOT partitioned */
CREATE INDEX DMLOG_COURT_ID_IDX ON XHBSTG_DATA_MIGRATION_LOG(COURT_ID) 
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'DMLOG_COURT_ID_IDX ON XHBSTG_DATA_MIGRATION_LOG(COURT_ID) - Created');
END;
/
/********** CTX_3727 End   *********/

BEGIN
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTX_3727 : Necessary XHBSTG LOCAL indexes created Successfully');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
