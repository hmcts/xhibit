create or replace PROCEDURE xhb_populate_ref_system_code (p_court_id IN xhb_court.court_id%TYPE) AS 

/** 
  * CGI crest to xhibit program
  *
  * MODULE      : xhb_populate_ref_system_code
  *
  * DESCRIPTION : JIRA tickets 1723
  *
  * Procedure                    Purpose
  * =========                    =======
  * xhb_populate_ref_system_code New entries in XHB_REF_SYSTEM_CODE will be required for receipt types.  These are used to populate the dropdown entries for "Receipt Type"
  *                              on case creation.  Entries will be created for each court so should run dynamically to generate all data required
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 23/03/2018    C Cash          1.0         First Version
  **/

--have a variable for each of the fields to be created if they are no in xhb_ref_system_code
 v_ew_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_vb_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_io_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_tc_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_bb_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_cs_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_cb_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 
 v_court_count      NUMBER := 0; --check that the court exists before processing else raise exception
 e_invalid_court_id EXCEPTION;
 
 --get a list of all non obsolete courts
 CURSOR courts_c IS
 SELECT xc.court_id
 FROM xhb_court xc
 WHERE nvl(xc.obs_ind,'-') <> 'Y';

 --get a list of the codes that are not in xhb_ref_system_code where court_id in court_c
 CURSOR codes_c (v_court_id IN xhb_court.court_id%TYPE)
 IS
 SELECT rsc.code
 ,      rsc.court_id
 FROM xhb_ref_system_code rsc
 WHERE rsc.court_id = v_court_id
 AND rsc.code IN ('EW','VB','IO','TC','BB','CS','CB')
 AND code_type IN ('CASE_RECEIPT_TYPE_T','CASE_RECEIPT_TYPE_S')
 AND nvl(obs_ind,'-') <> 'Y'
;

 BEGIN
 
 IF p_court_id IS NOT NULL THEN --user has specified a court so it needs to be checked that it does exist
  SELECT count(*)
  INTO v_court_count
  FROM xhb_court xc
  WHERE xc.court_id = p_court_id
  AND p_court_id is not null
  AND xc.obs_ind <> 'Y';
 END IF;
 
 /*Court doesnt exist so raise exception and exit*/
 IF p_court_id IS NOT NULL THEN
 IF v_court_count < 1
  THEN
   RAISE e_invalid_court_id;
 END IF;
 END IF;
 
 
 IF p_court_id IS NULL THEN
  FOR courts_r IN courts_c
   LOOP
    v_ew_code := NULL;
    v_vb_code := NULL;
    v_io_code := NULL;
    v_tc_code := NULL;
    v_bb_code := NULL;
    v_cs_code := NULL;
    v_cb_code := NULL;
    FOR codes_r IN codes_c (courts_r.court_id)
     LOOP
      --If the code exists fot courts_r.court_id then set the value so that they are not null in the INSERT section so wont go in again
      IF codes_r.code = 'EW' THEN v_ew_code := codes_r.code; END IF;
      IF codes_r.code = 'VB' THEN v_vb_code := codes_r.code; END IF;
      IF codes_r.code = 'IO' THEN v_io_code := codes_r.code; END IF;
      IF codes_r.code = 'TC' THEN v_tc_code := codes_r.code; END IF;
      IF codes_r.code = 'BB' THEN v_bb_code := codes_r.code; END IF;
      IF codes_r.code = 'CS' THEN v_cs_code := codes_r.code; END IF;
      IF codes_r.code = 'CB' THEN v_cb_code := codes_r.code; END IF;
     END LOOP; --codes_r
       
       --create 'EW' code
       IF v_ew_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'EW'                            --code
              , 'CASE_RECEIPT_TYPE_T'           --code_type
              , NULL                            --code_title
              ,'EITHER WAY'                     --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,courts_r.court_id                --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;     
       END IF;
       
       --create 'VB' code
       IF v_vb_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'VB'                            --code
              , 'CASE_RECEIPT_TYPE_T'           --code_type
              , NULL                            --code_title
              ,'VOLUNTARY BILL'                     --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,courts_r.court_id                --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;     
       END IF;   
       
       --create 'IO' code
       IF v_io_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'IO'                            --code
              , 'CASE_RECEIPT_TYPE_T'           --code_type
              , NULL                            --code_title
              ,'INDICTMENT ONLY'                --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,courts_r.court_id                --court_id
              ,'N'                              --obs_ind
              ); 
         ELSE NULL;     
       END IF; 
       
       --create 'TC' code
       IF v_tc_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'TC'                            --code
              , 'CASE_RECEIPT_TYPE_T'           --code_type
              , NULL                            --code_title
              ,'TRANSFER CERTIFICATE'           --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,courts_r.court_id                --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;     
       END IF;  
       
       --create 'BB' code
       IF v_bb_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'BB'                            --code
              , 'CASE_RECEIPT_TYPE_S'           --code_type
              , NULL                            --code_title
              ,'BRING BACK'                     --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,courts_r.court_id                --court_id
              ,'N'                              --obs_ind
              ); 
         ELSE NULL;     
       END IF;
       
       --create 'CS' code
       IF v_cs_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'CS'                            --code
              , 'CASE_RECEIPT_TYPE_S'           --code_type
              , NULL                            --code_title
              ,'COMMITTAL FOR SENTENCE'         --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,courts_r.court_id                --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;     
       END IF; 
       
       --create 'CB' code
       IF v_cb_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'CB'                            --code
              , 'CASE_RECEIPT_TYPE_S'           --code_type
              , NULL                            --code_title
              ,'COMMITTAL AFTER BREACH'         --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,courts_r.court_id                --court_id
              ,'N'                              --obs_ind
              ); 
         ELSE NULL;     
       END IF;      
       
   END LOOP; --court_r
  
 ELSE --court_id is specified so only run for that court
  FOR codes_r IN codes_c (p_court_id)
     LOOP
      --If the code exists fot courts_r.court_id then set the value so that they are not null in the INSERT section so wont go in again
      IF codes_r.code = 'EW' THEN v_ew_code := codes_r.code; END IF;
      IF codes_r.code = 'VB' THEN v_vb_code := codes_r.code; END IF;
      IF codes_r.code = 'IO' THEN v_io_code := codes_r.code; END IF;
      IF codes_r.code = 'TC' THEN v_tc_code := codes_r.code; END IF;
      IF codes_r.code = 'BB' THEN v_bb_code := codes_r.code; END IF;
      IF codes_r.code = 'CS' THEN v_cs_code := codes_r.code; END IF;
      IF codes_r.code = 'CB' THEN v_cb_code := codes_r.code; END IF;
     END LOOP; --codes_r
       
       --create 'EW' code
       IF v_ew_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'EW'                            --code
              , 'CASE_RECEIPT_TYPE_T'           --code_type
              , NULL                            --code_title
              ,'EITHER WAY'                     --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;     
       END IF;
       
       --create 'VB' code
       IF v_vb_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'VB'                            --code
              , 'CASE_RECEIPT_TYPE_T'           --code_type
              , NULL                            --code_title
              ,'VOLUNTARY BILL'                     --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;     
       END IF;   
       
       --create 'IO' code
       IF v_io_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'IO'                            --code
              , 'CASE_RECEIPT_TYPE_T'           --code_type
              , NULL                            --code_title
              ,'INDICTMENT ONLY'                --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                --court_id
              ,'N'                              --obs_ind
              ); 
         ELSE NULL;     
       END IF; 
       
       --create 'TC' code
       IF v_tc_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'TC'                            --code
              , 'CASE_RECEIPT_TYPE_T'           --code_type
              , NULL                            --code_title
              ,'TRANSFER CERTIFICATE'           --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;     
       END IF;  
       
       --create 'BB' code
       IF v_bb_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'BB'                            --code
              , 'CASE_RECEIPT_TYPE_S'           --code_type
              , NULL                            --code_title
              ,'BRING BACK'                     --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                --court_id
              ,'N'                              --obs_ind
              ); 
         ELSE NULL;     
       END IF;
       
       --create 'CS' code
       IF v_cs_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'CS'                            --code
              , 'CASE_RECEIPT_TYPE_S'           --code_type
              , NULL                            --code_title
              ,'COMMITTAL FOR SENTENCE'         --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;     
       END IF; 
       
       --create 'CB' code
       IF v_cb_code IS NULL THEN 
        INSERT INTO xhb_ref_system_code (ref_system_code_id
                                        ,code
                                        ,code_type
                                        ,code_title
                                        ,de_code
                                        ,ref_code_order
                                        ,last_update_date
                                        ,creation_date
                                        ,created_by
                                        ,last_updated_by
                                        ,version
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_system_code_seq.nextval --ref_system_code_id
              , 'CB'                            --code
              , 'CASE_RECEIPT_TYPE_S'           --code_type
              , NULL                            --code_title
              ,'COMMITTAL AFTER BREACH'         --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                --court_id
              ,'N'                              --obs_ind
              ); 
         ELSE NULL;     
       END IF;      
       
  END IF;
   
   --Exception catch
   EXCEPTION 
    WHEN e_invalid_court_id THEN raise_application_error(-20001,' Error exception xhb_populate_ref_system_code: '||p_court_id||' does not exist in XHB_COURT');
    WHEN OTHERS THEN raise_application_error(-20001,' Error exception xhb_populate_ref_system_code: '|| SQLCODE || ' : ' || SQLERRM);
   
   
  
 END xhb_populate_ref_system_code;
/