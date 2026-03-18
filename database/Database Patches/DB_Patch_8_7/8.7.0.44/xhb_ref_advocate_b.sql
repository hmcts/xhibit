CREATE OR REPLACE PACKAGE BODY xhb_ref_advocate_pkg AS

  PROCEDURE xhb_comp_ref_advocate(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  TYPE rec_ref_advocate IS RECORD (COURT_ID         NUMBER(8),
                                   ADDR1            VARCHAR2(30),
                                   ADDR2            VARCHAR2(30),
                                   ADDR3            VARCHAR2(30),
                                   ADDR4            VARCHAR2(30),
                                   TOWN             VARCHAR2(30),
                                   COUNTY           VARCHAR2(30),
                                   POSTCODE         VARCHAR2(8),
                                   TEL_NO           VARCHAR2(14),
                                   CLERK_NAME       VARCHAR2(35),
                                   FAX_NO           VARCHAR2(14),
                                   DX_REF           VARCHAR2(35),
                                   LOCATION_CODE    VARCHAR2(2),
                                   CHA_CENTRAL_IND  VARCHAR2(1),
                                   FIRM_NAME        VARCHAR2(35),
                                   ADV_ID           NUMBER(8),
                                   CHA_ID           NUMBER(8),
                                   SURNAME          VARCHAR2(35),
                                   FORENAME1        VARCHAR2(35),
                                   INITIALS         VARCHAR2(4),
                                   ADV_TYPE_IND     VARCHAR2(1),
                                   ADV_VERS         NUMBER(2),
                                   HONOURS          VARCHAR2(8),
                                   FORENAME2        VARCHAR2(35),
                                   OBS_IND          VARCHAR2(1),
                                   TITLE            VARCHAR2(25),
                                   BAR_NO           NUMBER(5),
                                   VAT_NO           VARCHAR2(9),
                                   YEAR_OF_CALL     NUMBER(4),
                                   ADV_TYPE         VARCHAR2(1),
                                   ADV_CENTRAL_IND  VARCHAR2(1),
				   CHA_OBS_IND	    VARCHAR2(1));

  l_advocate_record rec_ref_advocate;

  l_address_id_found  VARCHAR2(1) := 'N';
  l_advocate_count    NUMBER;
  l_cha_address_id    NUMBER;
  l_ref_chamber_id    NUMBER;
  l_ref_adv_id        NUMBER;
  l_ref_leg_rep_id    NUMBER;
  l_curr_address_id   NUMBER;
  l_curr_leg_rep_id   NUMBER;
  l_run_date          DATE := TRUNC(SYSDATE);

  CURSOR c_ref_advocate IS
    SELECT *
    FROM   mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  BEGIN

    OPEN c_ref_advocate;

    LOOP

      FETCH c_ref_advocate
      INTO  l_advocate_record;

      EXIT WHEN c_ref_advocate %NOTFOUND;

      BEGIN

        SELECT ref_chamber_id
        INTO   l_ref_chamber_id
        FROM   xhb_ref_chamber
        WHERE  TRUNC(last_update_date) = l_run_date
        AND    crest_chamber_id = l_advocate_record.cha_id
        AND    court_id = p_court_id;

        EXCEPTION

          WHEN NO_DATA_FOUND THEN

            /*
             * XHB_ADDRESS and XHB_REF_CHAMBER
             */

            BEGIN

              SELECT address_id
              INTO   l_cha_address_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              l_address_id_found := 'Y';

              EXCEPTION

                WHEN NO_DATA_FOUND THEN -- need to create both records

                  l_address_id_found := 'N';

            END;

            IF l_address_id_found = 'N' THEN -- no data returned so create both records

              INSERT INTO xhb_address (address_1,
                                       address_2,
                                       address_3,
                                       address_4,
                                       town,
                                       county,
                                       postcode)
                               VALUES (l_advocate_record.addr1,
                                       l_advocate_record.addr2,
                                       l_advocate_record.addr3,
                                       l_advocate_record.addr4,
                                       l_advocate_record.town,
                                       l_advocate_record.county,
                                       l_advocate_record.postcode);

              /*
               * Need to get the address ID that was just used to create the above row.
               */

              SELECT XHB_ADDRESS_SEQ.CURRVAL
              INTO   l_curr_address_id
              FROM   dual;

              INSERT INTO xhb_ref_chamber (obs_ind,
                                           is_global,
                                           dx_ref,
                                           location_code,
                                           crest_chamber_id,
                                           firm_name,
                                           address_id,
                                           court_id)
                                   VALUES (l_advocate_record.obs_ind,
                                           NVL(l_advocate_record.cha_central_ind,'N'),
                                           l_advocate_record.dx_ref,
                                           l_advocate_record.location_code,
                                           l_advocate_record.cha_id,
                                           l_advocate_record.firm_name,
                                           l_curr_address_id,
                                           p_court_id);

              SELECT XHB_REF_CHAMBER_SEQ.CURRVAL
              INTO   l_ref_chamber_id
              FROM   dual;

            ELSIF l_address_id_found = 'Y' AND
                  l_cha_address_id IS NULL THEN -- address_id found but is null so create address and then update
                                                -- XHB_REF_CHAMBER
              INSERT INTO xhb_address (address_1,
                                       address_2,
                                       address_3,
                                       address_4,
                                       town,
                                       county,
                                       postcode)
                               VALUES (l_advocate_record.addr1,
                                       l_advocate_record.addr2,
                                       l_advocate_record.addr3,
                                       l_advocate_record.addr4,
                                       l_advocate_record.town,
                                       l_advocate_record.county,
                                       l_advocate_record.postcode);

              /*
               * Need to get the address ID that was just used to create the above row.
               */

              SELECT XHB_ADDRESS_SEQ.CURRVAL
              INTO   l_curr_address_id
              FROM   dual;

              UPDATE XHB_REF_CHAMBER
              SET    address_id = l_curr_address_id,
                     obs_ind = l_advocate_record.obs_ind,
                     is_global = NVL(l_advocate_record.cha_central_ind,'N'),
                     dx_ref = l_advocate_record.dx_ref,
                     location_code = l_advocate_record.location_code,
                     firm_name = l_advocate_record.firm_name
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              SELECT ref_chamber_id
              INTO   l_ref_chamber_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

            ELSIF l_address_id_found = 'Y' AND
                  l_cha_address_id IS NOT NULL THEN -- There was a crest_chamber_id and a valid address_id in the XHB_REF_CHAMBER table

              l_curr_address_id := l_cha_address_id;

              UPDATE xhb_address
              SET    address_1 = l_advocate_record.addr1,
                     address_2 = l_advocate_record.addr2,
                     address_3 = l_advocate_record.addr3,
                     address_4 = l_advocate_record.addr4,
                     town = l_advocate_record.town,
                     county = l_advocate_record.county,
                     postcode = l_advocate_record.postcode
              WHERE  address_id = l_curr_address_id;

              UPDATE xhb_ref_chamber
              SET    obs_ind = l_advocate_record.obs_ind,
                     is_global = NVL(l_advocate_record.cha_central_ind,'N'),
                     dx_ref = l_advocate_record.dx_ref,
                     location_code = l_advocate_record.location_code,
                     firm_name = l_advocate_record.firm_name,
                     address_id = l_curr_address_id
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              SELECT ref_chamber_id
              INTO   l_ref_chamber_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

            END IF;

            /*
             * XHB_CONTACT_DETAIL
             */

            IF l_cha_address_id IS NULL OR
               l_address_id_found = 'N' THEN  -- must have created new address so create contact details

              INSERT INTO xhb_contact_detail (contact_type,
                                              contact_value,
                                              address_id)
                                      VALUES ('Phone',
                                              l_advocate_record.tel_no,
                                              l_curr_address_id);

              INSERT INTO xhb_contact_detail (contact_type,
                                              contact_value,
                                              address_id)
                                      VALUES ('Fax',
                                              l_advocate_record.fax_no,
                                              l_curr_address_id);

            ELSE

              UPDATE xhb_contact_detail
              SET    contact_value = l_advocate_record.tel_no
              WHERE  address_id = l_curr_address_id
              AND    contact_type IN ('Phone','Tel');

              UPDATE xhb_contact_detail
              SET    contact_value = l_advocate_record.fax_no
              WHERE  address_id = l_curr_address_id
              AND    contact_type IN ('Fax','FAX');

            END IF;

      END;

      /*
       * XHB_REF_ADVOCATE and XHB_REF_LEGAL_REPRESENTATIVE
       */

      /*
       * This part always done regardless of initial query to
       * obtain the ref_chamber_id.
       */

      BEGIN

        SELECT ref_advocate_id,
               ref_legal_rep_id
        INTO   l_ref_adv_id,
               l_ref_leg_rep_id
        FROM   xhb_ref_advocate
        WHERE  ref_legal_rep_id IN (SELECT ref_legal_rep_id
                                    FROM   xhb_ref_legal_representative
                                    WHERE  court_id = p_court_id)
        AND    crest_advocate_id = l_advocate_record.adv_id;

        l_advocate_count := 1;

        EXCEPTION

          WHEN NO_DATA_FOUND THEN -- need to create both records

            l_advocate_count := 0;

      END;

      IF l_advocate_count = 0 THEN -- insert records as does not exist

        INSERT INTO xhb_ref_legal_representative (first_name,
                                                  middle_name,
                                                  surname,
                                                  title,
                                                  initials,
                                                  legal_rep_type,
                                                  court_id,
                                                  obs_ind)
                                          VALUES (l_advocate_record.forename1,
                                                  l_advocate_record.forename2,
                                                  l_advocate_record.surname,
                                                  l_advocate_record.title,
                                                  l_advocate_record.initials,
                                                  l_advocate_record.adv_type,
                                                  p_court_id,
                                                  l_advocate_record.obs_ind);

        SELECT XHB_REF_LEGAL_REP_SEQ.CURRVAL
        INTO   l_curr_leg_rep_id
        FROM   dual;

        INSERT INTO xhb_ref_advocate (is_global,
                                      crest_advocate_id,
                                      crest_chamber_id,
                                      obs_ind,
                                      year_of_call,
                                      vat_no,
                                      bar_no,
                                      honours,
                                      adv_type_ind,
                                      ref_legal_rep_id,
                                      ref_chamber_id)
                              VALUES (NVL(l_advocate_record.adv_central_ind,'N'),
                                      l_advocate_record.adv_id,
                                      l_advocate_record.cha_id,
                                      l_advocate_record.obs_ind,
                                      l_advocate_record.year_of_call,
                                      l_advocate_record.vat_no,
                                      l_advocate_record.bar_no,
                                      l_advocate_record.honours,
                                      l_advocate_record.adv_type_ind,
                                      l_curr_leg_rep_id,
                                      l_ref_chamber_id);

      ELSIF l_advocate_count = 1 THEN

        UPDATE xhb_ref_legal_representative
        SET    first_name = l_advocate_record.forename1,
               middle_name = l_advocate_record.forename2,
               surname = l_advocate_record.surname,
               title = l_advocate_record.title,
               legal_rep_type = l_advocate_record.adv_type,
               court_id = p_court_id
        WHERE  ref_legal_rep_id = l_ref_leg_rep_id;

        UPDATE xhb_ref_advocate
        SET    is_global = NVL(l_advocate_record.adv_central_ind,'N'),
               crest_advocate_id = l_advocate_record.adv_id,
               crest_chamber_id = l_advocate_record.cha_id,
               obs_ind = l_advocate_record.obs_ind,
               year_of_call = l_advocate_record.year_of_call,
               vat_no = l_advocate_record.vat_no,
               bar_no = l_advocate_record.bar_no,
               honours = l_advocate_record.honours,
               adv_type_ind = l_advocate_record.adv_type_ind,
               ref_legal_rep_id = l_ref_leg_rep_id,
               ref_chamber_id = l_ref_chamber_id
        WHERE  ref_advocate_id = l_ref_adv_id;

      END IF;

    END LOOP;

    CLOSE c_ref_advocate;

    UPDATE XHB_REF_LEGAL_REPRESENTATIVE
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date
    AND    COURT_ID = p_court_id
    AND    REF_LEGAL_REP_ID NOT IN (SELECT REF_LEGAL_REP_ID
				    FROM   XHB_REF_SOLICITOR
				    WHERE  NVL(OBS_IND, 'N') = 'N');

    UPDATE XHB_REF_ADVOCATE
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date
    AND    REF_LEGAL_REP_ID IN (SELECT REF_LEGAL_REP_ID
				FROM   XHB_REF_LEGAL_REPRESENTATIVE
				WHERE  COURT_ID = p_court_id);

    UPDATE XHB_REF_CHAMBER
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date
    AND    COURT_ID = p_court_id;

    DELETE FROM mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  END xhb_comp_ref_advocate;

PROCEDURE xhb_comp_ref_advocate2(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  TYPE rec_ref_advocate IS RECORD (COURT_ID         NUMBER(8),
                                   ADDR1            VARCHAR2(30),
                                   ADDR2            VARCHAR2(30),
                                   ADDR3            VARCHAR2(30),
                                   ADDR4            VARCHAR2(30),
                                   TOWN             VARCHAR2(30),
                                   COUNTY           VARCHAR2(30),
                                   POSTCODE         VARCHAR2(8),
                                   TEL_NO           VARCHAR2(14),
                                   CLERK_NAME       VARCHAR2(35),
                                   FAX_NO           VARCHAR2(14),
                                   DX_REF           VARCHAR2(35),
                                   LOCATION_CODE    VARCHAR2(2),
                                   CHA_CENTRAL_IND  VARCHAR2(1),
                                   FIRM_NAME        VARCHAR2(35),
                                   ADV_ID           NUMBER(8),
                                   CHA_ID           NUMBER(8),
                                   SURNAME          VARCHAR2(35),
                                   FORENAME1        VARCHAR2(35),
                                   INITIALS         VARCHAR2(4),
                                   ADV_TYPE_IND     VARCHAR2(1),
                                   ADV_VERS         NUMBER(2),
                                   HONOURS          VARCHAR2(8),
                                   FORENAME2        VARCHAR2(35),
                                   OBS_IND          VARCHAR2(1),
                                   TITLE            VARCHAR2(25),
                                   BAR_NO           NUMBER(5),
                                   VAT_NO           VARCHAR2(9),
                                   YEAR_OF_CALL     NUMBER(4),
                                   ADV_TYPE         VARCHAR2(1),
                                   ADV_CENTRAL_IND  VARCHAR2(1),
				   CHA_OBS_IND	    VARCHAR2(1));

  l_advocate_record rec_ref_advocate;

  l_address_id_found  VARCHAR2(1) := 'N';
  l_advocate_count    NUMBER;
  l_cha_address_id    NUMBER;
  l_ref_chamber_id    NUMBER;
  l_ref_adv_id        NUMBER;
  l_ref_leg_rep_id    NUMBER;
  l_curr_address_id   NUMBER;
  l_curr_leg_rep_id   NUMBER;
  l_run_date          DATE := TRUNC(SYSDATE);

  CURSOR c_ref_advocate IS
    SELECT *
    FROM   mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  BEGIN

    OPEN c_ref_advocate;

    LOOP

      FETCH c_ref_advocate
      INTO  l_advocate_record;

      EXIT WHEN c_ref_advocate %NOTFOUND;

      BEGIN

        SELECT ref_chamber_id
        INTO   l_ref_chamber_id
        FROM   xhb_ref_chamber
        WHERE  TRUNC(last_update_date) = l_run_date
        AND    crest_chamber_id = l_advocate_record.cha_id
        AND    court_id = p_court_id;

        EXCEPTION

          WHEN NO_DATA_FOUND THEN

            /*
             * XHB_ADDRESS and XHB_REF_CHAMBER
             */

            BEGIN

              SELECT address_id
              INTO   l_cha_address_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              l_address_id_found := 'Y';

              EXCEPTION

                WHEN NO_DATA_FOUND THEN -- need to create both records

                  l_address_id_found := 'N';

            END;

            IF l_address_id_found = 'N' THEN -- no data returned so create both records

              INSERT INTO xhb_address (address_1,
                                       address_2,
                                       address_3,
                                       address_4,
                                       town,
                                       county,
                                       postcode)
                               VALUES (l_advocate_record.addr1,
                                       l_advocate_record.addr2,
                                       l_advocate_record.addr3,
                                       l_advocate_record.addr4,
                                       l_advocate_record.town,
                                       l_advocate_record.county,
                                       l_advocate_record.postcode);

              /*
               * Need to get the address ID that was just used to create the above row.
               */

              SELECT XHB_ADDRESS_SEQ.CURRVAL
              INTO   l_curr_address_id
              FROM   dual;

              INSERT INTO xhb_ref_chamber (obs_ind,
                                           is_global,
                                           dx_ref,
                                           location_code,
                                           crest_chamber_id,
                                           firm_name,
                                           address_id,
                                           court_id)
                                   VALUES (l_advocate_record.cha_obs_ind,
                                           NVL(l_advocate_record.cha_central_ind,'N'),
                                           l_advocate_record.dx_ref,
                                           l_advocate_record.location_code,
                                           l_advocate_record.cha_id,
                                           l_advocate_record.firm_name,
                                           l_curr_address_id,
                                           p_court_id);

              SELECT XHB_REF_CHAMBER_SEQ.CURRVAL
              INTO   l_ref_chamber_id
              FROM   dual;

            ELSIF l_address_id_found = 'Y' AND
                  l_cha_address_id IS NULL THEN -- address_id found but is null so create address and then update
                                                -- XHB_REF_CHAMBER
              INSERT INTO xhb_address (address_1,
                                       address_2,
                                       address_3,
                                       address_4,
                                       town,
                                       county,
                                       postcode)
                               VALUES (l_advocate_record.addr1,
                                       l_advocate_record.addr2,
                                       l_advocate_record.addr3,
                                       l_advocate_record.addr4,
                                       l_advocate_record.town,
                                       l_advocate_record.county,
                                       l_advocate_record.postcode);

              /*
               * Need to get the address ID that was just used to create the above row.
               */

              SELECT XHB_ADDRESS_SEQ.CURRVAL
              INTO   l_curr_address_id
              FROM   dual;

              UPDATE XHB_REF_CHAMBER
              SET    address_id = l_curr_address_id,
                     obs_ind = l_advocate_record.cha_obs_ind,
                     is_global = NVL(l_advocate_record.cha_central_ind,'N'),
                     dx_ref = l_advocate_record.dx_ref,
                     location_code = l_advocate_record.location_code,
                     firm_name = l_advocate_record.firm_name
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              SELECT ref_chamber_id
              INTO   l_ref_chamber_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

            ELSIF l_address_id_found = 'Y' AND
                  l_cha_address_id IS NOT NULL THEN -- There was a crest_chamber_id and a valid address_id in the XHB_REF_CHAMBER table

              l_curr_address_id := l_cha_address_id;

              UPDATE xhb_address
              SET    address_1 = l_advocate_record.addr1,
                     address_2 = l_advocate_record.addr2,
                     address_3 = l_advocate_record.addr3,
                     address_4 = l_advocate_record.addr4,
                     town = l_advocate_record.town,
                     county = l_advocate_record.county,
                     postcode = l_advocate_record.postcode
              WHERE  address_id = l_curr_address_id;

              UPDATE xhb_ref_chamber
              SET    obs_ind = l_advocate_record.cha_obs_ind,
                     is_global = NVL(l_advocate_record.cha_central_ind,'N'),
                     dx_ref = l_advocate_record.dx_ref,
                     location_code = l_advocate_record.location_code,
                     firm_name = l_advocate_record.firm_name,
                     address_id = l_curr_address_id
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              SELECT ref_chamber_id
              INTO   l_ref_chamber_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

            END IF;

            /*
             * XHB_CONTACT_DETAIL
             */

            IF l_cha_address_id IS NULL OR
               l_address_id_found = 'N' THEN  -- must have created new address so create contact details

              INSERT INTO xhb_contact_detail (contact_type,
                                              contact_value,
                                              address_id)
                                      VALUES ('Phone',
                                              l_advocate_record.tel_no,
                                              l_curr_address_id);

              INSERT INTO xhb_contact_detail (contact_type,
                                              contact_value,
                                              address_id)
                                      VALUES ('Fax',
                                              l_advocate_record.fax_no,
                                              l_curr_address_id);

            ELSE

              UPDATE xhb_contact_detail
              SET    contact_value = l_advocate_record.tel_no
              WHERE  address_id = l_curr_address_id
              AND    contact_type IN ('Phone','Tel');

              UPDATE xhb_contact_detail
              SET    contact_value = l_advocate_record.fax_no
              WHERE  address_id = l_curr_address_id
              AND    contact_type IN ('Fax','FAX');

            END IF;

      END;

      /*
       * XHB_REF_ADVOCATE and XHB_REF_LEGAL_REPRESENTATIVE
       */

      /*
       * This part always done regardless of initial query to
       * obtain the ref_chamber_id.
       */

      BEGIN

        SELECT ref_advocate_id,
               ref_legal_rep_id
        INTO   l_ref_adv_id,
               l_ref_leg_rep_id
        FROM   xhb_ref_advocate
        WHERE  ref_legal_rep_id IN (SELECT ref_legal_rep_id
                                    FROM   xhb_ref_legal_representative
                                    WHERE  court_id = p_court_id)
        AND    crest_advocate_id = l_advocate_record.adv_id;

        l_advocate_count := 1;

        EXCEPTION

          WHEN NO_DATA_FOUND THEN -- need to create both records

            l_advocate_count := 0;

      END;

      IF l_advocate_count = 0 THEN -- insert records as does not exist

        INSERT INTO xhb_ref_legal_representative (first_name,
                                                  middle_name,
                                                  surname,
                                                  title,
                                                  initials,
                                                  legal_rep_type,
                                                  court_id,
                                                  obs_ind)
                                          VALUES (l_advocate_record.forename1,
                                                  l_advocate_record.forename2,
                                                  l_advocate_record.surname,
                                                  l_advocate_record.title,
                                                  l_advocate_record.initials,
                                                  l_advocate_record.adv_type,
                                                  p_court_id,
                                                  l_advocate_record.obs_ind);

        SELECT XHB_REF_LEGAL_REP_SEQ.CURRVAL
        INTO   l_curr_leg_rep_id
        FROM   dual;

        INSERT INTO xhb_ref_advocate (is_global,
                                      crest_advocate_id,
                                      crest_chamber_id,
                                      obs_ind,
                                      year_of_call,
                                      vat_no,
                                      bar_no,
                                      honours,
                                      adv_type_ind,
                                      ref_legal_rep_id,
                                      ref_chamber_id)
                              VALUES (NVL(l_advocate_record.adv_central_ind,'N'),
                                      l_advocate_record.adv_id,
                                      l_advocate_record.cha_id,
                                      l_advocate_record.obs_ind,
                                      l_advocate_record.year_of_call,
                                      l_advocate_record.vat_no,
                                      l_advocate_record.bar_no,
                                      l_advocate_record.honours,
                                      l_advocate_record.adv_type_ind,
                                      l_curr_leg_rep_id,
                                      l_ref_chamber_id);

      ELSIF l_advocate_count = 1 THEN

        UPDATE xhb_ref_legal_representative
        SET    first_name = l_advocate_record.forename1,
               middle_name = l_advocate_record.forename2,
               surname = l_advocate_record.surname,
               title = l_advocate_record.title,
               legal_rep_type = l_advocate_record.adv_type,
               court_id = p_court_id
        WHERE  ref_legal_rep_id = l_ref_leg_rep_id;

        UPDATE xhb_ref_advocate
        SET    is_global = NVL(l_advocate_record.adv_central_ind,'N'),
               crest_advocate_id = l_advocate_record.adv_id,
               crest_chamber_id = l_advocate_record.cha_id,
               obs_ind = l_advocate_record.obs_ind,
               year_of_call = l_advocate_record.year_of_call,
               vat_no = l_advocate_record.vat_no,
               bar_no = l_advocate_record.bar_no,
               honours = l_advocate_record.honours,
               adv_type_ind = l_advocate_record.adv_type_ind,
               ref_legal_rep_id = l_ref_leg_rep_id,
               ref_chamber_id = l_ref_chamber_id
        WHERE  ref_advocate_id = l_ref_adv_id;

      END IF;

    END LOOP;

    CLOSE c_ref_advocate;

    DELETE FROM mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  END xhb_comp_ref_advocate2;

PROCEDURE update_chamber_ref(p_crest_advocate_id  XHB_REF_ADVOCATE.CREST_ADVOCATE_ID%TYPE
                           , p_new_chamber_id     XHB_REF_ADVOCATE.CREST_CHAMBER_ID%TYPE
                           , p_user              XHB_REF_LEGAL_REPRESENTATIVE.LAST_UPDATED_BY%TYPE
						    ) AS 
/****************************************************************************************
*   Procedure to update the Chambers for a given advocate when they move from 
*   one chambers to another.  Advocates and chambers have a record for each court so 
*   a record for each court will need to be updated.
*****************************************************************************************/

-- Local Variables
v_step                    VARCHAR2(10);
v_proc_name               VARCHAR2(30) := 'update_chamber_ref';
v_rows                    NUMBER(8) := 0;
v_err_code                NUMBER;
v_err_msg                 VARCHAR2(1000);
v_ref_advocate_id         XHB_REF_ADVOCATE.REF_ADVOCATE_ID%TYPE;
v_crest_chamber_id_old    XHB_REF_ADVOCATE.CREST_CHAMBER_ID%TYPE;
v_ref_legal_rep_id        XHB_REF_ADVOCATE.REF_LEGAL_REP_ID%TYPE;
v_ref_chamber_id          XHB_REF_CHAMBER.REF_CHAMBER_ID%TYPE;
v_crest_chamber_id_new    XHB_REF_CHAMBER.CREST_CHAMBER_ID%TYPE;

-- This will retrieve multiple rows because the advocate will have a record for each court.
Cursor c_advocate_all_courts is
SELECT rad.REF_ADVOCATE_ID, rad.CREST_CHAMBER_ID, rad.REF_LEGAL_REP_ID, rch.REF_CHAMBER_ID, rch.CREST_CHAMBER_ID 
FROM   XHB_REF_ADVOCATE rad, XHB_REF_LEGAL_REPRESENTATIVE rlr, XHB_REF_CHAMBER rch
WHERE  rad.CREST_ADVOCATE_ID = p_crest_advocate_id 
AND    nvl(rad.OBS_IND, 'N')  != 'Y' 
AND    nvl(rad.IS_GLOBAL, 'N') = 'Y'
AND    rad.REF_LEGAL_REP_ID = rlr.REF_LEGAL_REP_ID 
AND    rch.CREST_CHAMBER_ID = p_new_chamber_id
AND    rlr.COURT_ID = rch.court_id ;

BEGIN
    v_step := '2';
    OPEN c_advocate_all_courts;
	
	LOOP
        v_step := '3.' || To_char(v_rows);
		
	    FETCH c_advocate_all_courts INTO v_ref_advocate_id, v_crest_chamber_id_old, v_ref_legal_rep_id, v_ref_chamber_id, v_crest_chamber_id_new; 
        EXIT WHEN c_advocate_all_courts%NOTFOUND;       

        v_rows := v_rows + 1;

        v_step := '4.' || To_char(v_rows);
		
        UPDATE XHB_REF_ADVOCATE SET REF_CHAMBER_ID = v_ref_chamber_id, CREST_CHAMBER_ID = v_crest_chamber_id_new, LAST_UPDATED_BY = p_user 
    	WHERE  REF_ADVOCATE_ID  = v_ref_advocate_id 
	    AND    CREST_CHAMBER_ID = v_crest_chamber_id_old;
    End Loop;
	
	EXCEPTION
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in ' || v_proc_name || ': ' || v_err_code || ' : ' ||SQLERRM;
			
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg, sysdate);
      RAISE;

 END update_chamber_ref;
 
  PROCEDURE update_counsel_details(p_ref_advocate_id  XHB_REF_ADVOCATE.REF_ADVOCATE_ID%TYPE
                                ,  p_first_name       XHB_REF_LEGAL_REPRESENTATIVE.FIRST_NAME%TYPE
                                ,  p_middle_name      XHB_REF_LEGAL_REPRESENTATIVE.MIDDLE_NAME%TYPE
                                ,  p_surname          XHB_REF_LEGAL_REPRESENTATIVE.SURNAME%TYPE
                                ,  p_title            XHB_REF_LEGAL_REPRESENTATIVE.TITLE%TYPE
                                ,  p_initials         XHB_REF_LEGAL_REPRESENTATIVE.INITIALS%TYPE
                                ,  p_legal_rep_type   XHB_REF_LEGAL_REPRESENTATIVE.LEGAL_REP_TYPE%TYPE
                                ,  p_year_of_call     XHB_REF_ADVOCATE.YEAR_OF_CALL%TYPE
                                ,  p_vat_no           XHB_REF_ADVOCATE.VAT_NO%TYPE
                                ,  p_bar_no           XHB_REF_ADVOCATE.BAR_NO%TYPE
                                ,  p_honours          XHB_REF_ADVOCATE.HONOURS%TYPE
                                ,  p_adv_type_ind     XHB_REF_ADVOCATE.ADV_TYPE_IND%TYPE
								,  p_user_name        XHB_REF_ADVOCATE.LAST_UPDATED_BY%TYPE
                                  ) AS
/****************************************************************************************
*   Procedure to update the details for a given advocate. Details are in tables 
*   XHB_REF_LEGAL_REPRESENTATIVE and XHB_REF_ADVOCATE. They have a record for each court so 
*   all of them for a given advocate will need to be updated.  We will be given the 
*   REF_ADVOCATE_ID.  We need to use that to find the CREST_ADVOCATE_ID.  We update all
*   records with that CREST_ADVOCATE_ID.  Each of those records has a reference to 
*   REF_LEGAL_REP_ID, and each record in XHB_REF_LEGAL_REPRESENTATIVE with one of those 
*   REF_LEGAL_REP_IDs has also to be updated.
*****************************************************************************************/

-- Local Variables
v_step                    VARCHAR2(10);
v_proc_name               VARCHAR2(30) := 'update_counsel_details';
v_rows                    NUMBER(8) := 0;
v_err_code                NUMBER;
v_err_msg                 VARCHAR2(1000);
v_crest_advocate_id       XHB_REF_ADVOCATE.CREST_ADVOCATE_ID%TYPE;

BEGIN
    v_step := '1';
	-- First, look up the CREST_ADVOCATE_ID
	SELECT adv.CREST_ADVOCATE_ID 
	INTO   v_crest_advocate_id 
	FROM   XHB_REF_ADVOCATE adv
	WHERE  adv.REF_ADVOCATE_ID = p_ref_advocate_id;
	
	-- Now apply the update to all rows in XHB_REF_ADVOCATE for this advocate (there is one for each court)
    v_step := '2';
    UPDATE XHB_REF_ADVOCATE SET YEAR_OF_CALL = p_year_of_call, VAT_NO = p_vat_no, BAR_NO = p_bar_no, HONOURS = p_honours, ADV_TYPE_IND = p_adv_type_ind, LAST_UPDATED_BY = p_user_name 
	WHERE  CREST_ADVOCATE_ID = v_crest_advocate_id 
	AND     nvl(OBS_IND, 'N') != 'Y';
	
    -- Apply the update to all the Legal Representative records for this Advocate
    v_step := '3';
	UPDATE XHB_REF_LEGAL_REPRESENTATIVE SET FIRST_NAME = p_first_name, MIDDLE_NAME = p_middle_name, SURNAME = p_surname, TITLE = p_title, INITIALS = p_initials, LEGAL_REP_TYPE = p_legal_rep_type, LAST_UPDATED_BY = p_user_name 
	WHERE  REF_LEGAL_REP_ID IN (SELECT REF_LEGAL_REP_ID 
	                            FROM   XHB_REF_ADVOCATE 
	                            WHERE  CREST_ADVOCATE_ID = v_crest_advocate_id 
	                            AND    nvl(OBS_IND, 'N') != 'Y'
							   );
								
	EXCEPTION
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in ' || v_proc_name || ': ' || v_err_code || ' : ' ||SQLERRM;
			
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
      RAISE;

END update_counsel_details;

  PROCEDURE insert_counsel_details(p_ref_advocate_id  XHB_REF_ADVOCATE.REF_ADVOCATE_ID%TYPE
                                ,  p_first_name       XHB_REF_LEGAL_REPRESENTATIVE.FIRST_NAME%TYPE
                                ,  p_middle_name      XHB_REF_LEGAL_REPRESENTATIVE.MIDDLE_NAME%TYPE
                                ,  p_surname          XHB_REF_LEGAL_REPRESENTATIVE.SURNAME%TYPE
                                ,  p_title            XHB_REF_LEGAL_REPRESENTATIVE.TITLE%TYPE
                                ,  p_initials         XHB_REF_LEGAL_REPRESENTATIVE.INITIALS%TYPE
                                ,  p_legal_rep_type   XHB_REF_LEGAL_REPRESENTATIVE.LEGAL_REP_TYPE%TYPE  -- is the same as advocate_type, S = Solicitor, A = Advocate (Barrister)
                                ,  p_year_of_call     XHB_REF_ADVOCATE.YEAR_OF_CALL%TYPE
                                ,  p_vat_no           XHB_REF_ADVOCATE.VAT_NO%TYPE
                                ,  p_bar_no           XHB_REF_ADVOCATE.BAR_NO%TYPE
                                ,  p_honours          XHB_REF_ADVOCATE.HONOURS%TYPE
                                ,  p_adv_type_ind     XHB_REF_ADVOCATE.ADV_TYPE_IND%TYPE      -- Effectively a QC flag [Y/N]
								,  p_chamber_ref_no   XHB_REF_ADVOCATE.CREST_CHAMBER_ID%TYPE  -- The CREST Chamber ID is the same for every court.
                                ,  p_user             XHB_REF_LEGAL_REPRESENTATIVE.LAST_UPDATED_BY%TYPE
                                  ) AS
/****************************************************************************************
*   Procedure to insert the details for a new advocate. Details are in tables 
*   XHB_REF_LEGAL_REPRESENTATIVE and XHB_REF_ADVOCATE. They need a record for each court so 
*   all of them for a given advocate will need to be created.  We will be given the 
*   CREST_CHAMBER_ID.  We need to use that to find the COURT_ID  and REF_CHAMBER_ID for each 
*   instance.  We can get both from XHB_REF_CHAMBER.  COURT_ID is used in XHB_REF_LEGAL_REPRESENTATIVE 
*   and REF_CHAMBER_ID is used in XHB_REF_ADVOCATE.   [CTX-3153]
*****************************************************************************************/

-- Local Variables
v_step                    VARCHAR2(10);
v_proc_name               VARCHAR2(30) := 'insert_counsel_details';
v_rows                    NUMBER(8) := 0;
v_err_code                NUMBER;
v_err_msg                 VARCHAR2(1000);
v_crest_advocate_id       XHB_REF_ADVOCATE.CREST_ADVOCATE_ID%TYPE;
v_court_id                XHB_COURT.COURT_ID%TYPE;
v_legal_rep_id            XHB_REF_LEGAL_REPRESENTATIVE.REF_LEGAL_REP_ID%TYPE;
v_ref_chamber_id          XHB_REF_CHAMBER.REF_CHAMBER_ID%TYPE;

-- This cursor selects the court_id and chamber_id of all instances of the specified  chamber in XHB_REF_CHAMBER. (There is an instance for each court)
Cursor c_all_courts IS 
SELECT court_id, ref_chamber_id 
FROM   XHB_REF_CHAMBER 
WHERE  CREST_CHAMBER_ID = p_chamber_ref_no 
AND    nvl(OBS_IND, 'N') != 'Y';

BEGIN
    v_step := '1';
	-- First, generate the CREST_ADVOCATE_ID.  This value is the same for every court's instance of this advocate's record.
	SELECT XHB_REF_ADVOCATE_CREST_SEQ.NextVal 
	INTO   v_crest_advocate_id 
	FROM   DUAL;
	
	-- Now apply the insert to all courts in c_all_courts for this advocate
    v_step := '2';
	OPEN c_all_courts ;
	
	LOOP
        v_step := '3';
		
	    FETCH c_all_courts INTO v_court_id, v_ref_chamber_id ;
        EXIT WHEN c_all_courts%NOTFOUND;      
		
        v_step := '4';
		SELECT XHB_REF_LEGAL_REP_SEQ.NextVal 
		INTO   v_legal_rep_id 
		FROM DUAL;

        v_step := '5';
	    INSERT INTO XHB_REF_LEGAL_REPRESENTATIVE(REF_LEGAL_REP_ID, FIRST_NAME,   MIDDLE_NAME,   SURNAME,   TITLE,   INITIALS,   LEGAL_REP_TYPE,   COURT_ID,   OBS_IND, CREATED_BY, LAST_UPDATED_BY)
		                                  VALUES(v_legal_rep_id,   p_first_name, p_middle_name, p_surname, p_title, p_initials, p_legal_rep_type, v_court_id, 'N',     p_user,     p_user);
										  
        v_step := '6';
		INSERT INTO XHB_REF_ADVOCATE(REF_ADVOCATE_ID,              IS_GLOBAL, CREST_ADVOCATE_ID,   CREST_CHAMBER_ID, OBS_IND, YEAR_OF_CALL,   VAT_NO,   BAR_NO,   HONOURS,   ADV_TYPE_IND,   REF_LEGAL_REP_ID, REF_CHAMBER_ID,   CREATED_BY, LAST_UPDATED_BY) 
                              VALUES(XHB_REF_ADVOCATE_SEQ.NextVal, 'Y',       v_crest_advocate_id, p_chamber_ref_no, 'N',     p_year_of_call, p_vat_no, p_bar_no, p_honours, p_adv_type_ind, v_legal_rep_id,   v_ref_chamber_id, p_user,     p_user);		


    END LOOP;	
	
    v_step := '7';
	CLOSE c_all_courts;
	
								
	EXCEPTION
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in ' || v_proc_name || ': ' || v_err_code || ' : ' ||SQLERRM;
			
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
      RAISE;

END insert_counsel_details;

/****************************************************************************************
*   Procedure to delete (mark as obsolete) the details for an advocate. Details are in tables 
*   XHB_REF_LEGAL_REPRESENTATIVE and XHB_REF_ADVOCATE. They need a record for each court so 
*   all of them for a given advocate will need to be updated.  We will be given the 
*   CREST_ADVOCATE_ID.     [CTX-3152]
*****************************************************************************************/
PROCEDURE delete_counsel_details(p_crest_advocate_id  XHB_REF_ADVOCATE.CREST_ADVOCATE_ID%TYPE
                              ,  p_user               XHB_REF_LEGAL_REPRESENTATIVE.LAST_UPDATED_BY%TYPE) AS

-- Local Variables
v_step                    VARCHAR2(10);
v_proc_name               VARCHAR2(30) := 'delete_counsel_details';
v_err_code                NUMBER;
v_err_msg                 VARCHAR2(1000);

BEGIN
	-- We do XHB_REF_ADVOCATE first as it has a reference to XHB_REF_LEGAL_REPRESENTATIVE and thus is a dependant record.
    v_step := '2';
    UPDATE 	XHB_REF_ADVOCATE SET OBS_IND = 'Y', LAST_UPDATED_BY = p_user   -- This should update multiple rows, 
	WHERE   CREST_ADVOCATE_ID = p_crest_advocate_id;                       -- one for each court.
	
	-- Now we do XHB_REF_LEGAL_REPRESENTATIVE
    v_step := '3';
	UPDATE XHB_REF_LEGAL_REPRESENTATIVE SET  OBS_IND = 'Y', LAST_UPDATED_BY = p_user   
	WHERE  REF_LEGAL_REP_ID IN (SELECT REF_LEGAL_REP_ID 
	                            FROM   XHB_REF_ADVOCATE
	                            WHERE  CREST_ADVOCATE_ID = p_crest_advocate_id);
	
								
	EXCEPTION
        WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := 'Exception handler raised when others in ' || v_proc_name || ': ' || v_err_code || ' : ' ||SQLERRM;
			
            INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID,                    error_message, run_date)
                                   VALUES (XHB_REPORTS_ERROR_LOG_SEQ.nextval, v_err_msg,     sysdate);
      RAISE;

END delete_counsel_details;

END xhb_ref_advocate_pkg;
/
show errors