create or replace PROCEDURE insert_ref_disp_line_disint(p_court_id IN xhb_court.court_id%TYPE) AS

/**
  * CGI crest to xhibit program
  *
  * MODULE      : insert_ref_disp_line
  *
  * DESCRIPTION : XLC - 16
  *
  * Procedure                    		Purpose
  * =========                   		 =======
  * insert_ref_disp_line			New entries in XHB_REF_DISPOSAL_LINE for new disposal
  **/

  --have a variable for each of the fields to be created if they are no in xhb_ref_disp_line
 v_20_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
 v_40_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
 v_50_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
 v_60_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
 v_90_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;

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
 SELECT rdl.dil_seq_no
 ,      rdl.court_id
 FROM xhb_ref_disposal_line rdl
 WHERE rdl.court_id = v_court_id
 AND rdl.dil_seq_no IN (20,40,50,60,90)
 AND rdl.disposal_code IN ('DISINT')
 AND nvl(rdl.obs_ind,'-') <> 'Y'
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
    v_20_code := NULL;
    v_40_code := NULL;
    v_50_code := NULL;
    v_60_code := NULL;
    v_90_code := NULL;
    FOR codes_r IN codes_c (courts_r.court_id)
     LOOP
      --If the code exists fot courts_r.court_id then set the value so that they are not null in the INSERT section so wont go in again
      IF codes_r.dil_seq_no = 20 THEN v_20_code := codes_r.dil_seq_no; END IF;
      IF codes_r.dil_seq_no = 40 THEN v_40_code := codes_r.dil_seq_no; END IF;
	  IF codes_r.dil_seq_no = 50 THEN v_50_code := codes_r.dil_seq_no; END IF;
      IF codes_r.dil_seq_no = 60 THEN v_60_code := codes_r.dil_seq_no; END IF;
      IF codes_r.dil_seq_no = 90 THEN v_90_code := codes_r.dil_seq_no; END IF;
     END LOOP; --codes_r

       --create '20' code
       IF v_20_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,20
			  ,null
			  ,'Y'
			  ,'Y'
			  ,'N'
			  ,'D2'
			  ,'Date of Result'
			  ,'Y'
			  ,'UD1'
			  ,'V6'
			  ,'N'
			  , null
			  ,'N'
              ,courts_r.court_id
              ,'N'
              );
         ELSE NULL;
       END IF;

	    --create '40' code
       IF v_40_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,40
			  ,'Interim Driving Disqualification'
			  ,'N'
			  ,'Y'
			  ,'Y'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,'N'
			  ,'N'
              ,courts_r.court_id
              ,'N'
              );
         ELSE NULL;
       END IF;

	   --create '50' code
       IF v_50_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,50
			  ,'N'
			  ,'Y'
			  ,'Y'
			  ,'N'
			  ,'D11'
			  ,'     Replaced?'
			  ,'Y'
			  ,null
			  ,'V2'
			  ,'N'
			  ,'N'
			  ,'N'
              ,courts_r.court_id
              ,'N'
              );
         ELSE NULL;
       END IF;

	       --create '60' code
       IF v_60_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,60
			  ,'(Press CREATE RECORD to insert additional lines)'
			  ,'N'
			  ,'Y'
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,'Y'
              ,courts_r.court_id
              ,'N'
              );
         ELSE NULL;
       END IF;

	       --create '90' code
       IF v_90_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,90
			  ,'======================================================'
			  ,'N'
			  ,'Y'
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,'N'
              ,courts_r.court_id
              ,'N'
              );
         ELSE NULL;
       END IF;
 END LOOP; --court_r

 ELSE --court_id is specified so only run for that court
  FOR codes_r IN codes_c (p_court_id)
     LOOP
      --If the code exists fot courts_r.court_id then set the value so that they are not null in the INSERT section so wont go in again
      IF codes_r.dil_seq_no = 20 THEN v_20_code := codes_r.dil_seq_no; END IF;
      IF codes_r.dil_seq_no = 40 THEN v_40_code := codes_r.dil_seq_no; END IF;
	  IF codes_r.dil_seq_no = 50 THEN v_50_code := codes_r.dil_seq_no; END IF;
      IF codes_r.dil_seq_no = 60 THEN v_60_code := codes_r.dil_seq_no; END IF;
      IF codes_r.dil_seq_no = 90 THEN v_90_code := codes_r.dil_seq_no; END IF;
     END LOOP; --codes_r

       --create '20' code
       IF v_20_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,20
			  ,null
			  ,'Y'
			  ,'Y'
			  ,'N'
			  ,'D2'
			  ,'Date of Result'
			  ,'Y'
			  ,'UD1'
			  ,'V6'
			  ,'N'
			  , null
			  ,'N'
              ,p_court_id
              ,'N'
              );
         ELSE NULL;
       END IF;

	    --create '40' code
       IF v_40_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,40
			  ,'Interim Driving Disqualification'
			  ,'N'
			  ,'Y'
			  ,'Y'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,'N'
			  ,'N'
        ,p_court_id
        ,'N'
              );
         ELSE NULL;
       END IF;

	       --create '50' code
       IF v_50_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,50
			  ,'N'
			  ,'Y'
			  ,'Y'
			  ,'N'
			  ,'D11'
			  ,'     Replaced?'
			  ,'Y'
			  ,null
			  ,'V2'
			  ,'N'
			  ,'N'
			  ,'N'
              ,p_court_id
              ,'N'
              );
         ELSE NULL;
       END IF;

	       --create '60' code
       IF v_60_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,60
			  ,'(Press CREATE RECORD to insert additional lines)'
			  ,'N'
			  ,'Y'
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,'Y'
              ,p_court_id
              ,'N'
              );
         ELSE NULL;
       END IF;

	       --create '90' code
       IF v_90_code IS NULL THEN
        INSERT INTO xhb_ref_disposal_line (ref_disposal_line_id
                                        ,disposal_code
                                        ,template_version
                                        ,dil_seq_no
                                        ,data
                                        ,input_flag
                                        ,screen_print
                                        ,form_print
                                        ,dbdestin
                                        ,prompt
										, mandatory
										,dbsource
										,validation
										,multiple_choice
										, conc_flag
										,line_insert
                                        ,court_id
                                        ,obs_ind)
        VALUES (xhb_ref_disposal_line_seq.nextval
			  , 'DISINT'
			  ,1
			  ,90
			  ,'======================================================'
			  ,'N'
			  ,'Y'
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,null
			  ,'N'
			  ,null
			  ,'N'
              ,p_court_id
              ,'N'
              );
         ELSE NULL;
       END IF;
       END IF;

   --Exception catch
   EXCEPTION
    WHEN e_invalid_court_id THEN raise_application_error(-20001,' Error exception insert ref disp line: '||p_court_id||' does not exist in XHB_COURT');
    WHEN OTHERS THEN raise_application_error(-20001,' Error exception insert ref disp line: '|| SQLCODE || ' : ' || SQLERRM);



 END insert_ref_disp_line_disint;
/
