create or replace PROCEDURE xhb_populate_report_log AS

 CURSOR courts_c
 IS
 SELECT court_id
 FROM xhb_court
 WHERE (obs_ind <> 'Y' OR obs_ind IS NULL)
 ;
 
 CURSOR log_c (p_court_id    IN xhb_court.court_id%TYPE
              ,p_report_name IN VARCHAR2)
 IS
 SELECT *
 FROM xhb_report_log xl
 WHERE xl.court_id = p_court_id
 AND xl.report_name = p_report_name --report code couldnt be used because there are 2 rows for CTLRP
 ;

 V_ADJSS_EXIST VARCHAR2(10);  --'Defendants Put Back for Sentence'	
 V_DEFSS_EXIST VARCHAR2(10);  --'Defendants with Deferred Sentences'
 V_DOCAR_EXIST VARCHAR2(10);  --'Defendants with Outstanding Court of Appeal Results'
 V_LODR_EXIST VARCHAR2(10);   --'Listing Officers Diary'
 V_RELCJ_EXIST VARCHAR2(10);  --'Cases with Required Judge' 
 V_CTLRPA_EXIST VARCHAR2(10); --'Trials Approaching Custody Time Limits'
 V_CTLRPB_EXIST VARCHAR2(10); --'Custody Time Limit Reminder Letters'
 V_OUTC_EXIST VARCHAR2(10);   --'Outstanding Cases by Various Criteria'
 V_UNLC_EXIST VARCHAR2(10);   --'Unlisted Cases'
 V_NFIX_EXIST VARCHAR2(10);   --'Notification of Fixture'
 V_NHA_EXIST VARCHAR2(10);    --'Notice of Hearing of Appeal' 
 V_LFIX_EXIST VARCHAR2(10);   --'List of Fixed Dates' 	
 V_CFIX_EXIST VARCHAR2(10);   --'Cumulative List of Fixed Dates' 
 V_RAGE_EXIST VARCHAR2(10);   --'Cases over n weeks old with Listing History'
 V_RREC_EXIST VARCHAR2(10);   --'Cases Received and Disposed Of'
 V_RRCA_EXIST VARCHAR2(10);   --'Outstanding Trial cases by age'
 V_INFTRPC_EXIST VARCHAR2(10);--'Cracked Ineffective Trial Cases' 
 V_RUMO_EXIST VARCHAR2(10);   --'Unacknowledged Monetary Orders' 
 V_OBW_EXIST VARCHAR2(10);    --'Outstanding Bench Warrants' 
 V_RJS_EXIST VARCHAR2(10);    --'Report of Judge Sittings'
 V_RSIT_EXIST VARCHAR2(10);   --'Courtroom Sitting Times' 
 V_PRLIS_EXIST VARCHAR2(10);  --'Running List'
 V_DRSR_EXIST VARCHAR2(10);   --'Missing Trial Codes'
 V_NTRSF_EXIST VARCHAR2(10);  --'Notification of Case Transfer'	


 BEGIN
 
  FOR courts_r IN courts_c
   LOOP
     V_ADJSS_EXIST := null;
     V_DEFSS_EXIST := null; 
     V_DOCAR_EXIST := null;
     V_LODR_EXIST := null; 
     V_RELCJ_EXIST := null; 
     V_CTLRPA_EXIST := null; 
     V_CTLRPB_EXIST := null; 
     V_OUTC_EXIST := null; 
     V_UNLC_EXIST := null; 
     V_NFIX_EXIST := null; 
     V_NHA_EXIST := null; 
     V_LFIX_EXIST := null; 
     V_CFIX_EXIST := null; 
     V_RAGE_EXIST := null; 
     V_RREC_EXIST := null; 
     V_RRCA_EXIST := null; 
     V_INFTRPC_EXIST := null; 
     V_RUMO_EXIST := null; 
     V_OBW_EXIST := null; 
     V_RJS_EXIST := null; 
     V_RSIT_EXIST := null; 
     V_PRLIS_EXIST := null; 
     V_DRSR_EXIST := null; 
     V_NTRSF_EXIST := null; 
    FOR log_r IN log_c (courts_r.court_id,'Defendants Put Back for Sentence')
     LOOP
      V_ADJSS_EXIST := 'Y';
     END LOOP;
    FOR log_r IN log_c (courts_r.court_id,'Defendants with Deferred Sentences')
     LOOP
      V_DEFSS_EXIST :='Y';
     END LOOP;
    FOR log_r IN log_c (courts_r.court_id,'Defendants with Outstanding Court of Appeal Results')
     LOOP
      V_DOCAR_EXIST :='Y';
     END LOOP;
    FOR log_r IN log_c (courts_r.court_id,'Listing Officers Diary')
     LOOP
      V_LODR_EXIST :='Y';
     END LOOP;
    FOR log_r IN log_c (courts_r.court_id,'Cases with Required Judge')
     LOOP
      V_RELCJ_EXIST :='Y';
     END LOOP; 
    FOR log_r IN log_c (courts_r.court_id,'Trials Approaching Custody Time Limits')
     LOOP
      V_CTLRPA_EXIST :='Y';
     END LOOP;  
    FOR log_r IN log_c (courts_r.court_id,'Custody Time Limit Reminder Letters')
     LOOP
      V_CTLRPB_EXIST :='Y';
     END LOOP;
    FOR log_r IN log_c (courts_r.court_id,'Outstanding Cases by Various Criteria')
     LOOP
      V_OUTC_EXIST :='Y';
     END LOOP;
     FOR log_r IN log_c (courts_r.court_id,'Unlisted Cases')
     LOOP
      V_UNLC_EXIST :='Y';
     END LOOP;
     FOR log_r IN log_c (courts_r.court_id,'Notification of Fixture')
     LOOP
      V_NFIX_EXIST :='Y';
     END LOOP;
     FOR log_r IN log_c (courts_r.court_id,'Notice of Hearing of Appeal' ) 	
     LOOP
      V_NHA_EXIST :='Y';
     END LOOP;
     FOR log_r IN log_c (courts_r.court_id,'List of Fixed Dates' )
     LOOP
      V_LFIX_EXIST :='Y';
     END LOOP;
     FOR log_r IN log_c (courts_r.court_id,'Cumulative List of Fixed Dates' )
     LOOP
      V_CFIX_EXIST :='Y';
     END LOOP;
     FOR log_r IN log_c (courts_r.court_id,'Cases over n weeks old with Listing History' )
     LOOP
      V_RAGE_EXIST :='Y';
     END LOOP;
    FOR log_r IN log_c (courts_r.court_id,'Cases Received and Disposed Of'  )
     LOOP
      V_RREC_EXIST :='Y';
     END LOOP; 
    FOR log_r IN log_c (courts_r.court_id,'Outstanding Trial cases by age' )
     LOOP
      V_RRCA_EXIST :='Y';
     END LOOP;  
     FOR log_r IN log_c (courts_r.court_id, 'Cracked Ineffective Trial Cases' )
     LOOP
      V_INFTRPC_EXIST :='Y';
     END LOOP;  
    FOR log_r IN log_c (courts_r.court_id, 'Unacknowledged Monetary Orders' )
     LOOP
      V_RUMO_EXIST :='Y';
     END LOOP; 
    FOR log_r IN log_c (courts_r.court_id, 'Outstanding Bench Warrants' )
     LOOP
      V_OBW_EXIST :='Y';
     END LOOP; 
    FOR log_r IN log_c (courts_r.court_id, 'Report of Judge Sittings' )
     LOOP
      V_RJS_EXIST :='Y';
     END LOOP; 
    FOR log_r IN log_c (courts_r.court_id, 'Courtroom Sitting Times' )
     LOOP
      V_RSIT_EXIST :='Y';
     END LOOP; 
    FOR log_r IN log_c (courts_r.court_id, 'Running List' )
     LOOP
      V_PRLIS_EXIST :='Y';
     END LOOP; 
    FOR log_r IN log_c (courts_r.court_id, 'Missing Trial Codes')
     LOOP
      V_DRSR_EXIST :='Y';
     END LOOP;
    FOR log_r IN log_c (courts_r.court_id, 'Notification of Case Transfer' )
     LOOP
      V_NTRSF_EXIST :='Y';
     END LOOP;
   
   IF nvl(V_ADJSS_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Defendants Put Back for Sentence','ADJSS','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;
   
   IF nvl(V_DEFSS_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Defendants with Deferred Sentences','DEFSS','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;
   
   IF nvl(V_DOCAR_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Defendants with Outstanding Court of Appeal Results','DOCAR','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;

   IF nvl(V_LODR_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Listing Officers Diary','LODR','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;   
   
   IF nvl(V_RELCJ_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Cases with Required Judge','RELCJ','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF; 
   
   IF nvl(V_CTLRPA_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Trials Approaching Custody Time Limits','CTLRP','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;
   
   IF nvl(V_CTLRPB_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Custody Time Limit Reminder Letters','CTLRP','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;

   IF nvl(V_OUTC_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Outstanding Cases by Various Criteria','OUTC','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;
   
   IF nvl(V_UNLC_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Unlisted Cases','UNLC','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;  
   
   IF nvl(V_NFIX_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Notification of Fixture','NFIX','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;
   
   IF nvl(V_NHA_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Notice of Hearing of Appeal','NHA','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;

   IF nvl(V_LFIX_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'List of Fixed Dates','LFIX','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;   
   
   IF nvl(V_CFIX_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Cumulative List of Fixed Dates','CFIX','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;    
   
   IF nvl(V_RAGE_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Cases over n weeks old with Listing History','RAGE','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;
   
   IF nvl(V_RREC_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Cases Received and Disposed Of','RREC','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;   
   
   IF nvl(V_RRCA_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Outstanding Trial cases by age','RRCA','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;
   
   IF nvl(V_INFTRPC_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Cracked Ineffective Trial Cases','INFTRP/C','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;
   
   IF nvl(V_RUMO_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Unacknowledged Monetary Orders','RUMO','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;  
   
   IF nvl(V_OBW_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Outstanding Bench Warrants','OBW','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF; 
   
   IF nvl(V_RJS_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Report of Judge Sittings','RJS','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF; 
   
   IF nvl(V_RSIT_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Courtroom Sitting Times','RSIT','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;  
   
   IF nvl(V_PRLIS_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Running List','PRLIS','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF; 

   IF nvl(V_DRSR_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Missing Trial Codes','DRSR','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;  
   
   IF nvl(V_NTRSF_EXIST,'-') <> 'Y' 
    THEN INSERT INTO xhb_report_log (REPORT_LOG_ID,COURT_ID,REPORT_NAME,CREST_REPORT_CODE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,OBS_IND)
    VALUES (xhb_report_log_seq.nextval,courts_r.court_id,'Notification of Case Transfer','NTRSF','XHIBIT',sysdate,'XHIBIT',sysdate,'N');
   END IF;   
   
   END LOOP;
 
 
 
 END xhb_populate_report_log;
/