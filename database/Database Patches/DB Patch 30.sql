/*
 * Patch for DB release 30
 *
 * Date Here 19th November 2003
 */

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

ALTER TABLE XHB_CREST_IMPORT ADD (LAST_UPDATE_DATE  DATE DEFAULT SYSDATE NOT NULL,
                                  CREATION_DATE     DATE DEFAULT SYSDATE NOT NULL,
                                  CREATED_BY        VARCHAR2(30) DEFAULT USER NOT NULL,
                                  LAST_UPDATED_BY   VARCHAR2(30) DEFAULT USER NOT NULL,
                                  VERSION           NUMBER(5) DEFAULT 1 NOT NULL);

CREATE TABLE MTBL_MERC_GLOBAL_LOCAL_DATA (
       COURT_ID         NUMBER(8),
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
       ADV_CENTRAL_IND  VARCHAR2(1))
         TABLESPACE MERCATORD
         STORAGE (INITIAL 1M
                  NEXT 1M
                  PCTINCREASE 0);


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 * The standard procedure is as follows:
 *
 *     1. Drop the audit table (AUD_)
 *     2. Recreate audit table as select * from XHB_ table with no rows
 *     3. Add the INSERT_EVENT column to the end of the audit table
 *
 * Need to create temporary table in a process on the live system:
 *
 *     1. Create a temporary audit table as a copy of the current audit table
 *     2. Drop the original audit table
 *     3. Create new audit table as select * from XHB_ table with no rows
 *     4. Add the INSERT_EVENT column to the end of the audit table
 *     5. Insert the data from the temporary audit table into the new audit table
 */

DROP TABLE AUD_CREST_IMPORT;

CREATE TABLE AUD_CREST_IMPORT TABLESPACE AUDITD AS SELECT * FROM XHB_CREST_IMPORT;
TRUNCATE TABLE AUD_CREST_IMPORT;
ALTER TABLE AUD_CREST_IMPORT ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);


/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 *
 * Note that these are generally the BUR (update and delete) triggers as the BIR
 * (insert) triggers will only change on renaming the auditing columns within the
 * XHB_ table.  However, always a good idea to recompile the BIR trigger.
 */

CREATE OR REPLACE TRIGGER XHB_CREST_IMPORT_BIR_TR
  BEFORE INSERT
  ON XHB_CREST_IMPORT
  FOR EACH ROW

BEGIN

  IF :NEW.CREST_IMPORT_ID IS NULL THEN

    SELECT XHB_CREST_IMPORT_SEQ.NEXTVAL
    INTO   :NEW.CREST_IMPORT_ID
    FROM   DUAL;

  END IF;

  IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
      (:NEW.CREATED_BY IS NULL)) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
           SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY,
           :NEW.CREATED_BY 
    FROM   DUAL;

  END IF;

  SELECT SYSDATE,
         SYSDATE,
         1
  INTO   :NEW.LAST_UPDATE_DATE,
         :NEW.CREATION_DATE,
         :NEW.VERSION
  FROM   DUAL;

END;
/

CREATE OR REPLACE TRIGGER XHB_CREST_IMPORT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CREST_IMPORT
  FOR EACH ROW

DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */
        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CREST_IMPORT') = 1) THEN

    INSERT INTO AUD_CREST_IMPORT
    VALUES (:old.CREST_IMPORT_ID,
            :old.STATUS,
            :old.COURT_ID,
            :old.IMPORT_TYPE,
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

CREATE OR REPLACE PACKAGE xhb_ref_advocate_pkg AS

  PROCEDURE xhb_comp_ref_advocate(p_court_id IN XHB_COURT.COURT_ID%TYPE);

END xhb_ref_advocate_pkg;
/
show errors

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
        AND    crest_advocate_id = l_advocate_record.adv_id
        AND    crest_chamber_id = l_advocate_record.cha_id;

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
show errors


/*
 * Changes, additions or deletion of standing data
 */

UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.AllCourtStatusPanelGenerator' WHERE PANEL_ID = 1;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.SummaryByNamePanelGenerator' WHERE PANEL_ID = 2;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.DailyListPanelGenerator' WHERE PANEL_ID = 3;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.CourtListPanelGenerator' WHERE PANEL_ID = 4;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.CourtDetailPanelGenerator' WHERE PANEL_ID = 5;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.JuryCurrentStatusPanelGenerator' WHERE PANEL_ID = 6;
COMMIT;

UPDATE XHB_PAGE SET DESCRIPTION='All Court Status' WHERE PAGE_ID=1;
UPDATE XHB_PAGE SET DESCRIPTION='Jury Current Status' WHERE PAGE_ID=6;
UPDATE XHB_PAGE SET NAME='SNARE~A_JuryCurrentStatus_1' WHERE PAGE_ID=6;
COMMIT;

UPDATE XHB_PAGE SET DESCRIPTION='All Court Status' WHERE PAGE_ID=21;
UPDATE XHB_PAGE SET DESCRIPTION='Jury Current Status' WHERE PAGE_ID=26;
UPDATE XHB_PAGE SET NAME='DOCKF~A_JuryCurrentStatus_1' WHERE PAGE_ID=26;
COMMIT;

UPDATE XHB_PAGE SET DESCRIPTION='All Court Status' WHERE PAGE_ID=31;
UPDATE XHB_PAGE SET DESCRIPTION='Jury Current Status' WHERE PAGE_ID=36;
UPDATE XHB_PAGE SET NAME='ISLEW~I_JuryCurrentStatus_1' WHERE PAGE_ID=36;
COMMIT;


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = 30, last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
