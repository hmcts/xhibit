create or replace PROCEDURE insert_ref_disp_line_distot(
    p_court_id IN xhb_court.court_id%TYPE)
AS
  /**
  * CGI crest to xhibit program
  *
  * MODULE      : insert_ref_disp_line
  *
  * DESCRIPTION : XLC - 13
  *
  * Procedure                      Purpose
  * =========                      =======
  * insert_ref_disp_line   New entries in XHB_REF_DISPOSAL_LINE for new disposal
  **/
  --have a variable for each of the fields to be created if they are no in xhb_ref_disp_line
  v_20_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_40_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_60_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_80_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_100_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_120_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_140_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_160_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_180_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_200_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_220_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_240_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_260_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_280_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_300_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_320_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_340_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_360_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_380_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_400_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_420_code xhb_ref_disposal_line.dil_seq_no%TYPE DEFAULT NULL;
  v_court_count      NUMBER := 0; --check that the court exists before processing else raise exception
  e_invalid_court_id EXCEPTION;
  --get a list of all non obsolete courts
  CURSOR courts_c
  IS
    SELECT xc.court_id FROM xhb_court xc WHERE NVL(xc.obs_ind,'-') <> 'Y';
  --get a list of the codes that are not in xhb_ref_system_code where court_id in court_c
  CURSOR codes_c (v_court_id IN xhb_court.court_id%TYPE)
  IS
    SELECT rdl.dil_seq_no ,
      rdl.court_id
    FROM xhb_ref_disposal_line rdl
    WHERE rdl.court_id        = v_court_id
    AND rdl.dil_seq_no       IN (20,40,60,80,100,120,140,160,180,200,220,240,260,280,300,320,340,360,380,400,420)
    AND rdl.disposal_code    IN ('DISTOT')
    AND NVL(rdl.obs_ind,'-') <> 'Y' ;
BEGIN
  IF p_court_id IS NOT NULL THEN --user has specified a court so it needs to be checked that it does exist
    SELECT COUNT(*)
    INTO v_court_count
    FROM xhb_court xc
    WHERE xc.court_id = p_court_id
    AND p_court_id   IS NOT NULL
    AND xc.obs_ind   <> 'Y';
  END IF;
  /*Court doesnt exist so raise exception and exit*/
  IF p_court_id     IS NOT NULL THEN
    IF v_court_count < 1 THEN
      RAISE e_invalid_court_id;
    END IF;
  END IF;
  IF p_court_id IS NULL THEN
    FOR courts_r IN courts_c
    LOOP
      v_20_code  := NULL;
      v_40_code  := NULL;
      v_60_code  := NULL;
      v_80_code  := NULL;
      v_100_code := NULL;
      v_120_code := NULL;
      v_140_code := NULL;
      v_160_code := NULL;
      v_180_code := NULL;
      v_200_code := NULL;
      v_220_code := NULL;
      v_240_code := NULL;
      v_260_code := NULL;
      v_280_code := NULL;
      v_300_code := NULL;
      v_320_code := NULL;
      v_340_code := NULL;
      v_360_code := NULL;
      v_380_code := NULL;
      v_400_code := NULL;
      v_420_code := NULL;
      FOR codes_r IN codes_c (courts_r.court_id)
      LOOP
        --If the code exists fot courts_r.court_id then set the value so that they are not null in the INSERT section so wont go in again
        IF codes_r.dil_seq_no = 20 THEN
          v_20_code          := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 40 THEN
          v_40_code          := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 60 THEN
          v_60_code          := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 80 THEN
          v_80_code          := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 100 THEN
          v_100_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 120 THEN
          v_120_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 140 THEN
          v_140_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 160 THEN
          v_160_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 180 THEN
          v_180_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 200 THEN
          v_200_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 220 THEN
          v_220_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 240 THEN
          v_240_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 260 THEN
          v_260_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 280 THEN
          v_280_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 300 THEN
          v_300_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 320 THEN
          v_320_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 340 THEN
          v_340_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 360 THEN
          v_360_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 380 THEN
          v_380_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 400 THEN
          v_400_code         := codes_r.dil_seq_no;
        END IF;
        IF codes_r.dil_seq_no = 420 THEN
          v_420_code         := codes_r.dil_seq_no;
        END IF;
      END LOOP; --codes_r
      --create '20' code
      IF v_20_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            20 ,
            NULL ,
            'Y' ,
            'Y' ,
            'N' ,
            'D2' ,
            'Result Date' ,
            'Y' ,
            'UD1' ,
            'V6' ,
            'N' ,
            NULL ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '40' code
      IF v_40_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            40 ,
            'Disqualified for' ,
            'N' ,
            'Y' ,
            'Y' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '60' code
      IF v_60_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            60 ,
            '(Enter period of disqualification)' ,
            'N' ,
            'Y' ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '80' code
      IF v_80_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            80 ,
            '*************Delete if not applicable:************************' ,
            'N' ,
            'Y' ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '100' code
      IF v_100_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            mcgroup1 ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            100 ,
            NULL ,
            'Y' ,
            'Y' ,
            'Y' ,
            'D5' ,
            '     Duration:' ,
            'Y' ,
            NULL ,
            'V3' ,
            'N' ,
            'A' ,
            'Y' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '120' code
      IF v_120_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            mcgroup1 ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            120 ,
            NULL ,
            'Y' ,
            'Y' ,
            'Y' ,
            NULL ,
            '        Units:' ,
            'Y' ,
            NULL ,
            'V1' ,
            'N' ,
            'A' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '160' code
      IF v_160_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            mcgroup1 ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            160 ,
            'Life' ,
            'N' ,
            'Y' ,
            'Y' ,
            'D11' ,
            '     or' ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            'B' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '180' code
      IF v_180_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            180 ,
            '*****************************************************************************' ,
            'N' ,
            'Y' ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '200' code
      IF v_200_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            200 ,
            'Disqualified till Extended Test passed? (Y/N)' ,
            'N' ,
            'Y' ,
            'Y' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '220' code
      IF v_220_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            220 ,
            'N' ,
            'Y' ,
            'Y' ,
            'N' ,
            'D20' ,
            '     DTETP-4' ,
            'Y' ,
            NULL ,
            'V2' ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '280' code
      IF v_280_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            280 ,
            'Amount of Reduction if Rehabilitation Course to be taken:' ,
            'N' ,
            'Y' ,
            'Y' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '300' code
      IF v_300_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            300 ,
            NULL ,
            'Y' ,
            'Y' ,
            'Y' ,
            'D6' ,
            '     Duration:' ,
            'N' ,
            NULL ,
            'V3' ,
            'N' ,
            'Y' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '320' code
      IF v_320_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            320 ,
            NULL ,
            'Y' ,
            'Y' ,
            'Y' ,
            NULL ,
            '        Units:' ,
            'N' ,
            NULL ,
            'V1' ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '340' code
      IF v_340_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            340 ,
            '(Tick the box below if an interim D20 was sent from the Crown Court)' ,
            'N' ,
            'Y' ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '360' code
      IF v_360_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            360 ,
            'N' ,
            'Y' ,
            'Y' ,
            'N' ,
            'D22' ,
            'Prev Interim' ,
            'Y' ,
            NULL ,
            'V2' ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '380' code
      IF v_380_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            380 ,
            'N' ,
            'Y' ,
            'Y' ,
            'N' ,
            'D11' ,
            '     Replaced?' ,
            'Y' ,
            NULL ,
            'V2' ,
            'N' ,
            'N' ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '400' code
      IF v_400_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            400 ,
            '(Press CREATE RECORD to insert additional lines)' ,
            'N' ,
            'Y' ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            'Y' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
      --create '420' code
      IF v_420_code IS NULL THEN
        INSERT
        INTO xhb_ref_disposal_line
          (
            ref_disposal_line_id ,
            disposal_code ,
            template_version ,
            dil_seq_no ,
            data ,
            input_flag ,
            screen_print ,
            form_print ,
            dbdestin ,
            prompt ,
            mandatory ,
            dbsource ,
            validation ,
            multiple_choice ,
            conc_flag ,
            line_insert ,
            court_id ,
            obs_ind
          )
          VALUES
          (
            xhb_ref_disposal_line_seq.nextval ,
            'DISTOT' ,
            1 ,
            420 ,
            '======================================================' ,
            'N' ,
            'Y' ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            NULL ,
            'N' ,
            NULL ,
            'N' ,
            courts_r.court_id ,
            'N'
          );
      ELSE
        NULL;
      END IF;
    END LOOP; --court_r
  ELSE        --court_id is specified so only run for that court
    FOR codes_r IN codes_c
    (
      p_court_id
    )
    LOOP
      --If the code exists fot courts_r.court_id then set the value so that they are not null in the INSERT section so wont go in again
      IF codes_r.dil_seq_no = 20 THEN
        v_20_code          := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 40 THEN
        v_40_code          := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 60 THEN
        v_60_code          := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 80 THEN
        v_80_code          := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 100 THEN
        v_100_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 120 THEN
        v_120_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 140 THEN
        v_140_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 160 THEN
        v_160_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 180 THEN
        v_180_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 200 THEN
        v_200_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 220 THEN
        v_220_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 240 THEN
        v_240_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 260 THEN
        v_260_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 280 THEN
        v_280_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 300 THEN
        v_300_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 320 THEN
        v_320_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 340 THEN
        v_340_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 360 THEN
        v_360_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 380 THEN
        v_380_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 400 THEN
        v_400_code         := codes_r.dil_seq_no;
      END IF;
      IF codes_r.dil_seq_no = 420 THEN
        v_420_code         := codes_r.dil_seq_no;
      END IF;
    END LOOP; --codes_r
    --create '20' code
    IF v_20_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          20 ,
          NULL ,
          'Y' ,
          'Y' ,
          'N' ,
          'D2' ,
          'Result Date' ,
          'Y' ,
          'UD1' ,
          'V6' ,
          'N' ,
          NULL ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '40' code
    IF v_40_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          40 ,
          'Disqualified for' ,
          'N' ,
          'Y' ,
          'Y' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '60' code
    IF v_60_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          60 ,
          '(Enter period of disqualification)' ,
          'N' ,
          'Y' ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '80' code
    IF v_80_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          80 ,
          '*************Delete if not applicable:************************' ,
          'N' ,
          'Y' ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '100' code
    IF v_100_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          mcgroup1 ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          100 ,
          NULL ,
          'Y' ,
          'Y' ,
          'Y' ,
          'D5' ,
          '     Duration:' ,
          'Y' ,
          NULL ,
          'V3' ,
          'N' ,
          'A' ,
          'Y' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '120' code
    IF v_120_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          mcgroup1 ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          120 ,
          NULL ,
          'Y' ,
          'Y' ,
          'Y' ,
          NULL ,
          '        Units:' ,
          'Y' ,
          NULL ,
          'V1' ,
          'N' ,
          'A' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '160' code
    IF v_160_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          mcgroup1 ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          160 ,
          'Life' ,
          'N' ,
          'Y' ,
          'Y' ,
          'D11' ,
          '     or' ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          'B' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '180' code
    IF v_180_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          180 ,
          '*****************************************************************************' ,
          'N' ,
          'Y' ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '200' code
    IF v_200_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          200 ,
          'Disqualified till Extended Test passed? (Y/N)' ,
          'N' ,
          'Y' ,
          'Y' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '220' code
    IF v_220_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          220 ,
          'N' ,
          'Y' ,
          'Y' ,
          'N' ,
          'D20' ,
          '     DTETP-4' ,
          'Y' ,
          NULL ,
          'V2' ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '280' code
    IF v_280_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          280 ,
          'Amount of Reduction if Rehabilitation Course to be taken:' ,
          'N' ,
          'Y' ,
          'Y' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '300' code
    IF v_300_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          300 ,
          NULL ,
          'Y' ,
          'Y' ,
          'Y' ,
          'D6' ,
          '     Duration:' ,
          'N' ,
          NULL ,
          'V3' ,
          'N' ,
          'Y' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '320' code
    IF v_320_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          320 ,
          NULL ,
          'Y' ,
          'Y' ,
          'Y' ,
          NULL ,
          '        Units:' ,
          'N' ,
          NULL ,
          'V1' ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '340' code
    IF v_340_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          340 ,
          '(Tick the box below if an interim D20 was sent from the Crown Court)' ,
          'N' ,
          'Y' ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '360' code
    IF v_360_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          360 ,
          'N' ,
          'Y' ,
          'Y' ,
          'N' ,
          'D22' ,
          'Prev Interim' ,
          'Y' ,
          NULL ,
          'V2' ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '380' code
    IF v_380_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          380 ,
          'N' ,
          'Y' ,
          'Y' ,
          'N' ,
          'D11' ,
          '     Replaced?' ,
          'Y' ,
          NULL ,
          'V2' ,
          'N' ,
          'N' ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '400' code
    IF v_400_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          400 ,
          '(Press CREATE RECORD to insert additional lines)' ,
          'N' ,
          'Y' ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          'Y' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
    --create '420' code
    IF v_420_code IS NULL THEN
      INSERT
      INTO xhb_ref_disposal_line
        (
          ref_disposal_line_id ,
          disposal_code ,
          template_version ,
          dil_seq_no ,
          data ,
          input_flag ,
          screen_print ,
          form_print ,
          dbdestin ,
          prompt ,
          mandatory ,
          dbsource ,
          validation ,
          multiple_choice ,
          conc_flag ,
          line_insert ,
          court_id ,
          obs_ind
        )
        VALUES
        (
          xhb_ref_disposal_line_seq.nextval ,
          'DISTOT' ,
          1 ,
          420 ,
          '======================================================' ,
          'N' ,
          'Y' ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          NULL ,
          'N' ,
          NULL ,
          'N' ,
          p_court_id ,
          'N'
        );
    ELSE
      NULL;
    END IF;
  END IF;
  --Exception catch
EXCEPTION
WHEN e_invalid_court_id THEN
  raise_application_error(-20001,' Error exception insert ref disp line: '||p_court_id||' does not exist in XHB_COURT');
WHEN OTHERS THEN
  raise_application_error(-20001,' Error exception insert ref disp line: '|| SQLCODE || ' : ' || SQLERRM);
END insert_ref_disp_line_distot;
/