/*
 * These procedures are run after a Mercator Crest ref data load.
 *
 * The Mercator Map now calls standing_post_merc which in turn calls
 * the four listed.
 *
 * standing_cr_live_status is called once after a build and then once
 * after adding a new court to the system.
 *
 */

CREATE OR REPLACE PACKAGE BODY xhb_post_merc_ref_data_pkg AS

  PROCEDURE standing_post_merc(p_main_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  BEGIN

    xhb_post_merc_ref_data_pkg.standing_formb_result(p_main_court_id);

    xhb_post_merc_ref_data_pkg.standing_order_disposal_xref(p_main_court_id);

    xhb_post_merc_ref_data_pkg.standing_orders_types_temps(p_main_court_id);

    xhb_post_merc_ref_data_pkg.standing_ref_disp_menu_ctype(p_main_court_id);

  END standing_post_merc;

  PROCEDURE standing_cr_live_status IS

  v_max_court_room_id NUMBER;

  BEGIN
  
  	-- insert into CR_LIVE_INTERNET

    SELECT MAX(court_room_id)
    INTO   v_max_court_room_id
    FROM   xhb_cr_live_internet;

    IF v_max_court_room_id IS NOT NULL THEN

      -- entries already exists so only insert for court rooms with higher court_room_id

      v_max_court_room_id := v_max_court_room_id + 1;

      INSERT INTO xhb_cr_live_internet (court_room_id, time_status_set, status)
      SELECT court_room_id,
             SYSDATE,
             'No Information to display'
      FROM   xhb_court_room
      WHERE  court_room_id >= v_max_court_room_id;

    ELSE

      -- table is empty so insert for ALL court rooms

      INSERT INTO xhb_cr_live_internet (court_room_id, time_status_set, status)
      SELECT court_room_id,
             SYSDATE,
             'No Information to display'
      FROM   xhb_court_room;

    END IF;
  
  	-- insert into CR_LIVE_DISPLAY

    SELECT MAX(court_room_id)
    INTO   v_max_court_room_id
    FROM   xhb_cr_live_display;

    IF v_max_court_room_id IS NOT NULL THEN

      -- entries already exists so only insert for court rooms with higher court_room_id

      v_max_court_room_id := v_max_court_room_id + 1;

      INSERT INTO xhb_cr_live_display (court_room_id, time_status_set)
      SELECT court_room_id,
             SYSDATE
      FROM   xhb_court_room
      WHERE  court_room_id >= v_max_court_room_id;

    ELSE

      -- table is empty so insert for ALL court rooms

      INSERT INTO xhb_cr_live_display (court_room_id, time_status_set)
      SELECT court_room_id,
             SYSDATE
      FROM   xhb_court_room;

    END IF;

  END standing_cr_live_status;

  PROCEDURE standing_formb_result (p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  BEGIN

    DELETE FROM XHB_FORMB_RESULT
    WHERE  COURT_ID = p_court_id;

    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'G' AND COURT_ID = p_court_id ), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NG' AND COURT_ID = p_court_id ), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'AA' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'AC' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'DUD' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GA' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GAJ' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GJJ' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GL' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GLJ' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'JUTA' AND COURT_ID = p_court_id), 'Other', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGIS' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGJJ' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGJU' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NV' AND COURT_ID = p_court_id), 'Other', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'O' AND COURT_ID = p_court_id), 'Other', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'RTG' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'RTNG' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGJA' AND COURT_ID = p_court_id), 'Verdict', p_court_id);

    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'NPT' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'AA' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'G' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'NG' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'AC' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPG' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPGJ' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPNG' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'GAO' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'GLO' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'O' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'P' AND COURT_ID = p_court_id), NULL, p_court_id);

    COMMIT;

  END standing_formb_result;

  PROCEDURE standing_order_disposal_xref(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  /*
   * DESCRIPTION:  This script loops through each court in XHB_COURT. Then it selects the CREST CO disposals
   *               that Xhibit is producing (second cursor). It then loops through the matching RC disposal types
   *               (the mapping is defined in the linked_rs_data cursor) and inserts a row into the
   *               XHB_ORDER_DISPOSAL_XREF table.
   *
   * DEPENDENCIES: This must be run AFTER the CREST XHB_REF_DISPOSAL_TYPE data has been created, otherwsie it will do nothing.
   */

  CURSOR xrd_co_data IS
    SELECT order_type_id,
           code
    FROM   XHB_ORDER_TYPE
    WHERE  code in ('RC', 'IMPO', 'COMY', 'CMPO', 'CMPRO', 'CRO', 'COMSENT');


  /* This cursor holds the actual details of the mapping */

  CURSOR linked_rs_data (v_order_code VARCHAR2) IS
    SELECT ref_disposal_type_id,
           disposal_code,
           menu_group
    FROM   XHB_REF_DISPOSAL_TYPE
    WHERE  menu_group in ('RS')
    AND    NVL(obs_ind,'N') = 'N'
    AND    court_id = p_court_id
    AND    (v_order_code = 'RC' AND disposal_code IN ('REMMENT','REMTREA')) OR
           (v_order_code = 'IMPO' AND disposal_code IN ('IMPMIN','LIFE','LIFESEC','LIMM','CSSP','CTFL','CUSTEXT','IMP','ODIMP')) OR
           (v_order_code = 'COMY' AND disposal_code IN ('DET','DWLT','IMPMYO','LIFESYO','YOI','CSSP','CTFL','CUSTEXT','IMP','ODIMP')) OR
           (v_order_code = 'CMPO' AND disposal_code IN ('CPO','CPODAR')) OR
           (v_order_code = 'CMPRO' AND disposal_code IN ('CPDRCUR','CPDRDAR','CPDREXC','CPDRHR','CPDRNH','CPDROA',
                                                               'CPDROC','CPDROD','CPDROG','CPDROR','CPRCUR','CPRDAR',
                                                               'CPREXC','CPRHR','CPRNH','CPROA','CPROC','CPROD',
                                                               'CPROG','CPROH','CPROR')) OR
	   (v_order_code = 'COMSENT' AND disposal_code IN ('CWRKCS', 'PSACTCS', 'PACOBCS','PRACTCS','CURFCS','EXCARCS',
                                                           'RESRQCS','MHTRTCS','DRGRQCS','ALTRTCS','SPVRQCS','ATCRQCS',
                                                           'EXCURCS')) OR
           (v_order_code = 'CRO' AND disposal_code IN ('CRCR','CRDA','CRDC','CRHR','CRNH','CROCUR','CRODAR','CROEXC','CROG','CRRA'));

  -- temp variables, for sqlplus check of data

  v_order_type_id            varchar2(20):= NULL;
  v_co_data_disposal_code    varchar2(20):= NULL;
  v_co_data_court_id         varchar2(20):= NULL;
  v_rs_ref_disposal_type_id  varchar2(20):= NULL;
  v_rs_data_disposal_code    varchar2(20):= NULL;

  BEGIN

    DELETE FROM XHB_ORDER_DISPOSAL_XREF
    WHERE  COURT_ID = p_court_id;

    /* Loop through each CO type we are interested in, get the matching RS values and create a row in the target table for each one.  */

    FOR r_xrd_co_data IN xrd_co_data LOOP

      FOR r_linked_rs_data IN linked_rs_data(r_xrd_co_data.code) LOOP  -- multiple rows for each CO code

	v_order_type_id := to_char(r_xrd_co_data.order_type_id);
        v_co_data_disposal_code := r_xrd_co_data.code;
        v_co_data_court_id := to_char(p_court_id);
        v_rs_ref_disposal_type_id := to_char(r_linked_rs_data.ref_disposal_type_id);
        v_rs_data_disposal_code := r_linked_rs_data.disposal_code;

        -- Create a record in the target table

        INSERT INTO XHB_ORDER_DISPOSAL_XREF (order_disposal_xref_id,
                                             court_id,
                                             rs_ref_disposal_type_id,
                                             order_type_id,
                                             version,
                                             creation_date,
                                             last_update_date,
                                             created_by,
                                             last_updated_by)
                                     VALUES (NULL,
                                             v_co_data_court_id,
                                             v_rs_ref_disposal_type_id,
                                             v_order_type_id,
                                             1,
                                             SYSDATE,
                                             SYSDATE,
                                             USER,
                                             USER);

        -- output details for visual confirmation (if serveroutput is set to on)

        -- dbms_output.put_line('Inserting row: '||'ID '||to_char(v_id)||':'||v_co_ref_disposal_type_id||':'||v_co_data_disposal_code);

      END LOOP;   -- RS code loop

    END LOOP;   -- CO code loop

    COMMIT;

  END standing_order_disposal_xref;

  PROCEDURE standing_orders_types_temps(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  /* DESCRIPTION:  The order types that are provided in XHIBIT2, and the templates relating to them.
   *
   * DEPENDENCIES: Dependency on XHB_REF_DISPOSAL data existing.
   */

  v_order_type_id_count     NUMBER;
  v_order_template_id_count NUMBER;
  v_order_type_mapping_id_count NUMBER;

  BEGIN

    SELECT count(*)
    INTO   v_order_type_id_count
    FROM   XHB_ORDER_TYPE;

    IF v_order_type_id_count = 0 THEN

      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (1, 'BC', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Bail Conditions');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (2, 'BW', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Bench Warrant');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (3, 'CMPO', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Community Punishment Order (5042)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (4, 'CMPRO', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Community Punishment Rehabilitation Order (5042a)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (5, 'COMY', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Commitment of Young Offender (5044)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (6, 'CRO', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Community Rehabilitation Order (5037)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (7, 'IMPO', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Imprisonment (5035)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (8, 'RC', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Remand in Custody (5038)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (9, 'COMSENT', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Community Order');

      COMMIT;

    END IF;

    SELECT count(*)
    INTO   v_order_template_id_count
    FROM   XHB_ORDER_TEMPLATE;

    IF v_order_template_id_count = 0 THEN

      INSERT INTO XHB_ORDER_TEMPLATE VALUES (1, '/metadata/OrderFOPTransform.xslt', '/metadata/BailOrder_Narrative.xml', '/metadata/BailOrderTemplate.xml', 1, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 1, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (2, '/metadata/OrderFOPTransform.xslt', '/metadata/BenchWarrantOrder_Narrative.xml', '/metadata/BenchWarrantOrderTemplate.xml', 2, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 2, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (3, '/metadata/OrderFOPTransform.xslt', '/metadata/CPO_Narrative.xml', '/metadata/CPOrderTemplate.xml', 3, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 3, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (4, '/metadata/OrderFOPTransform.xslt', '/metadata/CPRO_Narrative.xml', '/metadata/CPROrderTemplate.xml', 4, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 4, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (5, '/metadata/OrderFOPTransform.xslt', '/metadata/YoungOffendersOrder_Narrative.xml', '/metadata/YOIOrderTemplate.xml', 5, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 5, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (6, '/metadata/OrderFOPTransform.xslt', '/metadata/CRO_Narrative.xml', '/metadata/CROrderTemplate.xml', 6, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 6, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (7, '/metadata/OrderFOPTransform.xslt', '/metadata/ImprisonmentOrder_Narrative.xml', '/metadata/ImprisonmentOrderTemplate.xml', 7, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 7, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (8, '/metadata/OrderFOPTransform.xslt', '/metadata/RemandOrder_Narrative.xml', '/metadata/RemandOrderTemplate.xml', 8, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 8, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (9, '/metadata/OrderFOPTransform.xslt', '/metadata/COMSENT_Narrative.xml', '/metadata/COMSENTOrderTemplate.xml', 9, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 9, NULL);

      COMMIT;

    END IF;

    SELECT count(*)
    INTO   v_order_type_mapping_id_count
    FROM   XHB_ORDER_TYPE_MAPPING;

    IF v_order_type_mapping_id_count = 0 THEN

	-- New Community Order replaced by New Community Order
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (9, 9, 9, 0);
	-- Remand Order - not replaced
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (8, 8, NULL, 0);
	-- Imprisonment Order - not replaced
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (7, 7, NULL, 0);
	-- Community Rehabilitation Order replaced by New Community Order
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (6, 6, 9, 0);
	-- Young Offender Order - not replaced
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (5, 5, NULL, 0);
	-- Community Punishment and Rehabilitation Order replaced by New Community Order
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (4, 4, 9, 0);
	-- Community Punishment Order replaced by New Community Order
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (3, 3, 9, 0);
	-- Bench Warrant - not replaced
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (2, 2, NULL, 0);
	-- Bail Order - not replaced
	INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
		VALUES (1, 1, NULL, 0);

      COMMIT;

    END IF;


    /*
     * The following select statement updates the order type to the latest for the court
     * This is a temporary fix and is wrong for 2 reasons.
     *      1) REF_DISPOSAL_TYPE_ID is court specific
     *      2) We need to maintain mappings for old versions of orders (in case an old
     *         one is in progress)
     * This has been raised as a preemptive PR00334
     */
    UPDATE XHB_ORDER_TYPE xot
    SET    xot.REF_DISPOSAL_TYPE_ID = (SELECT
                                           xrdt.ref_disposal_type_id
                                       FROM
                                           (SELECT
                                               ref_disposal_type_id,
                                               disposal_code
                                            FROM
                                               xhb_ref_disposal_type
                                            WHERE
                                               court_id = p_court_id
                                            AND
                                               obs_ind = 'N'
                                            ORDER BY
                                               template_version DESC) xrdt
                                       WHERE
                                           xrdt.disposal_code = xot.code
                                       AND
                                           ROWNUM = 1);

    UPDATE XHB_ORDER_TYPE xot
    SET    xot.description = (SELECT title
                              FROM   xhb_ref_disposal_type xrdt
                              WHERE  xrdt.ref_disposal_type_id = xot.ref_disposal_type_id
                              AND    court_id = p_court_id)
    WHERE  xot.ref_disposal_type_id IS NOT NULL;

    COMMIT;

  END standing_orders_types_temps;

  PROCEDURE standing_ref_disp_menu_ctype(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  BEGIN

      DELETE FROM 
          XHB_REF_DISP_MENU_CASE_TYPE 
      WHERE 
          REF_DISPOSAL_MENU_ID IN (SELECT 
                                       REF_DISPOSAL_MENU_ID 
                                   FROM 
                                       XHB_REF_DISPOSAL_MENU 
                                   WHERE 
                                       COURT_ID = p_court_id); 
      
      INSERT INTO XHB_REF_DISP_MENU_CASE_TYPE (
             REF_DISPOSAL_MENU_CASE_TYPE_ID,
             REF_DISPOSAL_MENU_ID,
             CASE_TYPE)   
      SELECT 
             NULL,
             rdmm.REF_DISPOSAL_MENU_ID,
             'S'
      FROM   
          XHB_REF_DISPOSAL_MENU rdmm,
          XHB_REF_DISPOSAL_MENU rdmg
      WHERE
          (rdmm.parent = rdmg.menu_item_id OR rdmm.menu_item_id = rdmg.menu_item_id)
      AND 
          rdmg.abbrev = 'STNOIND'
      AND 
          rdmg.court_id = rdmm.court_id
      AND 
          rdmg.court_id = p_court_id;
        
      COMMIT;

  END standing_ref_disp_menu_ctype;

END xhb_post_merc_ref_data_pkg;
/
show errors
