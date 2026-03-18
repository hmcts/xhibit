/*
 * Patch for DB Release 5.10.6
 *
 * Date Here 14th January 2004
 */

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

CREATE INDEX XHB_REF_ADVOCATE_CREST_ADV_IDX ON XHB_REF_ADVOCATE
  (CREST_ADVOCATE_ID ASC)
  TABLESPACE XHIBITREFX
  STORAGE (INITIAL 1M
           NEXT 1M
           PCTINCREASE 0);


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

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
                                   ADV_CENTRAL_IND  VARCHAR2(1));

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
    WHERE  TRUNC(last_update_date) != l_run_date;

    UPDATE XHB_REF_ADVOCATE
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date;

    UPDATE XHB_REF_CHAMBER
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date;

    DELETE FROM mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  END xhb_comp_ref_advocate;

END xhb_ref_advocate_pkg;
/


/*
 * Changes, additions or deletion of standing data
 */


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = '5.10.6', last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
