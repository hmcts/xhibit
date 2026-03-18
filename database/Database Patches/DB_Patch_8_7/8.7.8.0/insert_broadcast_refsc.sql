create or replace PROCEDURE insert_broadcast_refsc(p_court_id IN xhb_court.court_id%TYPE) AS

/**
  * CGI crest to xhibit program
  *
  * MODULE      : insert_broadcast_refsc
  *
  * DESCRIPTION : XLC - 12
  *
  * Procedure                    		Purpose
  * =========                   		 =======
  * insert_broadcast_refsc	                 New entries in XHB_REF_SYSTEM_CODE will be required for broadcast SR.
  *                             		 Entries will be created for each court so should run dynamically to generate all data required
  **/

--have a variable for each of the fields to be created if they are no in xhb_ref_system_code
 v_dr_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_pr_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_ub_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_al_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_ld_code xhb_ref_system_code.code%TYPE DEFAULT NULL;
 v_o_code xhb_ref_system_code.code%TYPE DEFAULT NULL;

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
 AND rsc.code IN ('DR','PR','UB','AL','LD','O')
 AND code_type IN ('TELE_APP_REFUSED')
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
    v_dr_code := NULL;
    v_pr_code := NULL;
    v_ub_code := NULL;
    v_al_code := NULL;
    v_ld_code := NULL;
    v_o_code := NULL;
    FOR codes_r IN codes_c (courts_r.court_id)
     LOOP
      --If the code exists fot courts_r.court_id then set the value so that they are not null in the INSERT section so wont go in again
      IF codes_r.code = 'DR' THEN v_dr_code := codes_r.code; END IF;
      IF codes_r.code = 'PR' THEN v_pr_code := codes_r.code; END IF;
      IF codes_r.code = 'UB' THEN v_ub_code := codes_r.code; END IF;
      IF codes_r.code = 'AL' THEN v_al_code := codes_r.code; END IF;
      IF codes_r.code = 'LD' THEN v_ld_code := codes_r.code; END IF;
      IF codes_r.code = 'O' THEN v_o_code := codes_r.code; END IF;
     END LOOP; --codes_r

       --create 'DR' code
       IF v_dr_code IS NULL THEN
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
              , 'DR'                            --code
              , 'TELE_APP_REFUSED'           	--code_type
              , NULL                            --code_title
              ,'Defense Representations'        --de_code
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

       --create 'PR' code
       IF v_pr_code IS NULL THEN
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
              , 'PR'                            --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Prosecution Representations'                     --de_code
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

       --create 'UB' code
       IF v_ub_code IS NULL THEN
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
              , 'UB'                            --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Case unsuitable for Broadcast'  --de_code
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

       --create 'AL' code
       IF v_al_code IS NULL THEN
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
              , 'AL'                            --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Application Late'               --de_code
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

       --create 'LD' code
       IF v_ld_code IS NULL THEN
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
              , 'LD'                            --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Likely disruption to Court'     --de_code
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

       --create 'o' code
       IF v_o_code IS NULL THEN
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
              , 'O'                             --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Other'                          --de_code
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
      IF codes_r.code = 'DR' THEN v_dr_code := codes_r.code; END IF;
      IF codes_r.code = 'PR' THEN v_pr_code := codes_r.code; END IF;
      IF codes_r.code = 'UB' THEN v_ub_code := codes_r.code; END IF;
      IF codes_r.code = 'AL' THEN v_al_code := codes_r.code; END IF;
      IF codes_r.code = 'LD' THEN v_ld_code := codes_r.code; END IF;
      IF codes_r.code = 'O' THEN v_o_code := codes_r.code; END IF;
     END LOOP; --codes_r

        --create 'DR' code
       IF v_dr_code IS NULL THEN
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
              , 'DR'                            --code
              , 'TELE_APP_REFUSED'           	--code_type
              , NULL                            --code_title
              ,'Defense Representations'        --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                       --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;
       END IF;

       --create 'PR' code
       IF v_pr_code IS NULL THEN
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
              , 'PR'                            --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Prosecution Representations'                     --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                       --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;
       END IF;

       --create 'UB' code
       IF v_ub_code IS NULL THEN
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
              , 'UB'                            --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Case unsuitable for Broadcast'  --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                       --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;
       END IF;

       --create 'AL' code
       IF v_al_code IS NULL THEN
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
              , 'AL'                            --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Application Late'               --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                       --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;
       END IF;

       --create 'LD' code
       IF v_ld_code IS NULL THEN
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
              , 'LD'                            --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Likely disruption to Court'     --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                       --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;
       END IF;

       --create 'o' code
       IF v_o_code IS NULL THEN
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
              , 'O'                             --code
              , 'TELE_APP_REFUSED'              --code_type
              , NULL                            --code_title
              ,'Other'                          --de_code
              ,NULL                             --ref_code_order
              ,sysdate                          --last_update_date
              ,sysdate                          --creation_date
              ,'XHIBIT'                         --created_by
              ,'XHIBIT'                         --last_updated_by
              ,1                                --version
              ,p_court_id                       --court_id
              ,'N'                              --obs_ind
              );
         ELSE NULL;
       END IF;

  END IF;

   --Exception catch
   EXCEPTION
    WHEN e_invalid_court_id THEN raise_application_error(-20001,' Error exception insert_broadcast_refsc: '||p_court_id||' does not exist in XHB_COURT');
    WHEN OTHERS THEN raise_application_error(-20001,' Error exception insert_broadcast_refsc: '|| SQLCODE || ' : ' || SQLERRM);



 END insert_broadcast_refsc;
/