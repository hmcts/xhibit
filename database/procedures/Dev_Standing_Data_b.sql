CREATE OR REPLACE PACKAGE BODY XHB_STANDING_DATA_PKG AS
    TYPE pk_array_type IS TABLE OF NUMBER(8) INDEX BY BINARY_INTEGER;
    TYPE pk_array_array_type IS TABLE OF pk_array_type INDEX BY VARCHAR2(30);

    g_array_lookup_cache_t pk_array_array_type;

    FUNCTION get_id_lookup(p_cache_name_in    IN VARCHAR2,
                           p_sequence_name_in IN VARCHAR2,
                           p_id_in            IN NUMBER) RETURN NUMBER IS
        l_seq_next_val NUMBER;
    BEGIN
        IF (p_id_in IS NULL) THEN
            RETURN NULL;
        END IF;

        RETURN g_array_lookup_cache_t(p_cache_name_in)(p_id_in);
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            -- If performance is significantly impacted, either use DBMS_SQL or
            -- extract this common code out to use the relevant sequence...
            EXECUTE IMMEDIATE 'SELECT ' || p_sequence_name_in || '.nextVal FROM dual'
            INTO l_seq_next_val;

            g_array_lookup_cache_t(p_cache_name_in)(p_id_in) := l_seq_next_val;
            RETURN l_seq_next_val;
    END get_id_lookup;


    FUNCTION lookup_xhb_address_id(p_id_in IN NUMBER) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('address', 'XHB_ADDRESS_SEQ', p_id_in);
    END lookup_xhb_address_id;


    FUNCTION lookup_xhb_ref_chamber_id(p_id_in IN NUMBER) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('ref_chamber', 'XHB_REF_CHAMBER_SEQ', p_id_in);
    END lookup_xhb_ref_chamber_id;


    FUNCTION lookup_xhb_ref_legal_rep_id(p_id_in IN NUMBER) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('ref_legal_rep', 'XHB_REF_LEGAL_REP_SEQ', p_id_in);
    END lookup_xhb_ref_legal_rep_id;


    FUNCTION lookup_xhb_ref_court_rep_f_id(p_id_in IN NUMBER) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('ref_court_rep', 'XHB_REF_COURT_REPORT_F_SEQ', p_id_in);
    END lookup_xhb_ref_court_rep_f_id;


    FUNCTION lookup_xhb_ref_disp_menu_id(p_id_in IN NUMBER) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('ref_disposal_menu', 'XHB_REF_DISPOSAL_MENU_SEQ', p_id_in);
    END lookup_xhb_ref_disp_menu_id;


    PROCEDURE add_reference_data(p_court_id_in IN NUMBER) IS
    BEGIN
        
        DBMS_APPLICATION_INFO.set_action('Starting add_reference_data(' || p_court_id_in || ')');

        -- first clear out our cache...
        g_array_lookup_cache_t.DELETE;

        ---------------------------------------------------------------
        -- Start off with the simple inserts that have no dependencies...
        ---------------------------------------------------------------

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_APP_RESULT');

        INSERT INTO XHB_REF_APP_RESULT
        SELECT XHB_REF_APP_RESULT_SEQ.NEXTVAL,
               APP_RESULT_CODE,
               APP_RESULT_DESCR1,
               HO_CODE,
               VARY_SENTENCE,
               APP_RESULT_DESCR2,
               LESSER_OFF_IND,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID,
               OBS_IND
        FROM   EXT_XHB_REF_APP_RESULT;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_DISPOSAL');

        INSERT INTO XHB_REF_DISPOSAL
        SELECT XHB_REF_DISPOSAL_SEQ.NEXTVAL,
               DISPOSAL_CODE,
               DISPOSAL_TITLE,
               CREST_MENU_GROUP,
               CREST_TEMPLATE_VERSION,
               DISP_TITLE1,
               DISP_TITLE2,
               DVLC_CODE,
               OBS_IND,
               p_court_id_in AS COURT_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_REF_DISPOSAL;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_DISPOSAL_LINE');

        INSERT INTO XHB_REF_DISPOSAL_LINE
        SELECT XHB_REF_DISPOSAL_LINE_SEQ.NEXTVAL,
               p_court_id_in AS COURT_ID,
               DISPOSAL_CODE,
               TEMPLATE_VERSION,
               DIL_SEQ_NO,
               DATA,
               INPUT_FLAG,
               SCREEN_PRINT,
               FORM_PRINT,
               DBDESTIN,
               PROMPT,
               FORMAT,
               MANDATORY,
               DBSOURCE,
               VALIDATION,
               MULTIPLE_CHOICE,
               MCGROUP1,
               MCGROUP2,
               CHAR_MAX,
               CONC_FLAG,
               LINE_INSERT,
               OBS_IND,
               NULL AS LAST_UPDATED_BY,
               NULL AS CREATED_BY,
               NULL AS CREATION_DATE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS VERSION
        FROM   EXT_XHB_REF_DISPOSAL_LINE;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_DISPOSAL_TYPE');

        INSERT INTO XHB_REF_DISPOSAL_TYPE
        SELECT XHB_REF_DISPOSAL_TYPE_SEQ.nextVal AS REF_DISPOSAL_TYPE_ID,
               p_court_id_in AS COURT_ID,
               TEMPLATE_VERSION,
               DISPOSAL_CODE,
               MENU_GROUP,
               TITLE,
               DISP_TITLE1,
               DISP_TITLE2,
               LINE_AVAIL,
               CATEGORY,
               OBS_IND,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_REF_DISPOSAL_TYPE;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_HEARING_TYPE');

        INSERT INTO XHB_REF_HEARING_TYPE
        SELECT XHB_REF_HEARING_TYPE_SEQ.NEXTVAL,
               HEARING_TYPE_CODE,
               HEARING_TYPE_DESC,
               CATEGORY,
               SEQ_NO,
               LIST_SEQUENCE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID,
               OBS_IND
        FROM   EXT_XHB_REF_HEARING_TYPE;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_JUDGE');

        INSERT INTO XHB_REF_JUDGE
        SELECT XHB_REF_JUDGE_SEQ.NEXTVAL,
               JUDGE_TYPE,
               CREST_JUDGE_ID,
               TITLE,
               FIRST_NAME,
               MIDDLE_NAME,
               SURNAME,
               FULL_LIST_TITLE1,
               FULL_LIST_TITLE2,
               FULL_LIST_TITLE3,
               STATS_CODE,
               INITIALS,
               HONOURS,
               JUD_VERS,
               OBS_IND,
               SOURCE_TABLE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID
        FROM   EXT_XHB_REF_JUDGE;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_JUSTICE');

        INSERT INTO XHB_REF_JUSTICE
        SELECT XHB_REF_JUSTICE_SEQ.NEXTVAL,
               JUSTICE_NAME,
               CREST_JUSTICE_ID,
               p_court_id_in AS COURT_ID,
               PSD_COURT_CODE,
               TITLE,
               INITIALS,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               OBS_IND
        FROM   EXT_XHB_REF_JUSTICE;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_OFFENCE');

        INSERT INTO XHB_REF_OFFENCE
        SELECT XHB_REF_OFFENCE_SEQ.NEXTVAL,
               OFFENCE_CODE,
               OFFENCE_DESC,
               HO_PROC_TYPE,
               HO_CLASS,
               HO_SUB_CLASS,
               DVLC_CODE,
               STATUTE,
               OFFENCE_CLASS,
               ACT_SECTION,
               OBS_IND,
               OFFENCE_DESC2,
               OFFENCE_GROUP,
               p_court_id_in AS COURT_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_REF_OFFENCE;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_SYSTEM_CODE');

        INSERT INTO XHB_REF_SYSTEM_CODE
        SELECT XHB_REF_SYSTEM_CODE_SEQ.NEXTVAL,
               CODE,
               CODE_TYPE,
               CODE_TITLE,
               DE_CODE,
               REF_CODE_ORDER,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID,
               OBS_IND
        FROM   EXT_XHB_REF_SYSTEM_CODE;        

        ---------------------------------------------------------------
        -- Load all of the addreses used by the ref data...
        ---------------------------------------------------------------

        DBMS_APPLICATION_INFO.set_action('Populating XHB_ADDRESS');

        INSERT INTO XHB_ADDRESS
        SELECT lookup_xhb_address_id(ADDRESS_ID) AS ADDRESS_ID,
               ADDRESS_1,
               ADDRESS_2,
               ADDRESS_3,
               ADDRESS_4,
               TOWN,
               COUNTY,
               POSTCODE,
               COUNTRY,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_ADDRESS exa;
        -- Only load those values that are not for defendants...
        --WHERE  NOT EXISTS (SELECT 1 FROM EXT_XHB_DEFENDANT exd WHERE exa.address_id = exd.address_id);

        ---------------------------------------------------------------
        -- Now start with the more complex inserts requiring lookups...
        ---------------------------------------------------------------

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_PROSECUTOR_AGENCY');

        INSERT INTO XHB_REF_PROSECUTOR_AGENCY
        SELECT XHB_REF_PROSECUTOR_AGENCY_SEQ.NEXTVAL,
               TITLE,
               PROSECUTOR_NAME_1,
               PROSECUTOR_NAME_2,
               PROSECUTOR_NAME_3,
               INITIALS,
               lookup_xhb_address_id(ADDRESS_ID) AS ADDRESS_ID,
               CREST_OPPOSER_ID,
               p_court_id_in AS COURT_ID,
               CPS_CODE,
               DX_REF,
               OBS_IND,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_REF_PROSECUTOR_AGENCY;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_COURT');

        INSERT INTO XHB_REF_COURT
        SELECT XHB_REF_COURT_SEQ.NEXTVAL,
               COURT_FULL_NAME,
               COURT_SHORT_NAME,
               NAME_PREFIX,
               COURT_TYPE,
               CREST_CODE,
               OBS_IND,
               IS_PSD,
               DX_REF,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               lookup_xhb_address_id(ADDRESS_ID) AS ADDRESS_ID,
               p_court_id_in AS COURT_ID
        FROM   EXT_XHB_REF_COURT;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_CHAMBER');

        INSERT INTO XHB_REF_CHAMBER
        SELECT lookup_xhb_ref_chamber_id(REF_CHAMBER_ID) AS REF_CHAMBER_ID,
               OBS_IND,
               IS_GLOBAL,
               DX_REF,
               LOCATION_CODE,
               CREST_CHAMBER_ID,
               FIRM_NAME,
               lookup_xhb_address_id(ADDRESS_ID) AS ADDRESS_ID,
               p_court_id_in AS COURT_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_REF_CHAMBER;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_LEGAL_REPRESENTATIVE');

        INSERT INTO XHB_REF_LEGAL_REPRESENTATIVE
        SELECT lookup_xhb_ref_legal_rep_id(REF_LEGAL_REP_ID) AS REF_LEGAL_REP_ID,
               FIRST_NAME,
               MIDDLE_NAME,
               SURNAME,
               TITLE,
               INITIALS,
               LEGAL_REP_TYPE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID,
               OBS_IND
        FROM   EXT_XHB_REF_LEGAL_REP;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_SOLICITOR_FIRM');

        INSERT INTO XHB_REF_SOLICITOR_FIRM
        SELECT XHB_REF_SOLICITOR_FIRM_SEQ.NEXTVAL AS REF_SOLICITOR_FIRM_ID,
               SOLICITOR_FIRM_NAME,
               CREST_SOF_ID,
               p_court_id_in AS COURT_ID,
               OBS_IND,
               SHORT_NAME,
               DX_REF,
               VAT_NO,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               lookup_xhb_address_id(ADDRESS_ID) AS ADDRESS_ID
        FROM   EXT_XHB_REF_SOLICITOR_FIRM;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_ADVOCATE');

        INSERT INTO XHB_REF_ADVOCATE
        SELECT XHB_REF_ADVOCATE_SEQ.NEXTVAL,
               IS_GLOBAL,
               CREST_ADVOCATE_ID,
               CREST_CHAMBER_ID,
               OBS_IND,
               YEAR_OF_CALL,
               VAT_NO,
               BAR_NO,
               HONOURS,
               ADV_TYPE_IND,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               lookup_xhb_ref_legal_rep_id(REF_LEGAL_REP_ID) AS REF_LEGAL_REP_ID,
               lookup_xhb_ref_chamber_id(REF_CHAMBER_ID) AS REF_CHAMBER_ID
        FROM   EXT_XHB_REF_ADVOCATE;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_COURT_REPORTER_FIRM');

        INSERT INTO XHB_REF_COURT_REPORTER_FIRM
        SELECT lookup_xhb_ref_court_rep_f_id(REF_COURT_REPORTER_FIRM_ID) AS REF_COURT_REPORTER_FIRM_ID,
               OBS_IND,
               DISPLAY_FIRST,
               DX_REF,
               VAT_NO,
               FIRM_NAME,
               lookup_xhb_address_id(ADDRESS_ID) AS ADDRESS_ID,
               p_court_id_in AS COURT_ID,
               NULL AS LAST_UPDATED_BY,
               NULL AS CREATED_BY,
               NULL AS CREATION_DATE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS VERSION,
               CREST_COURT_REPORTER_FIRM_ID
        FROM   EXT_XHB_REF_COURT_REP_FIRM;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_COURT_REPORTER');

        INSERT INTO XHB_REF_COURT_REPORTER
        SELECT XHB_REF_COURT_REPORTER_SEQ.NEXTVAL AS REF_COURT_REPORTER_ID,
               FIRST_NAME,
               MIDDLE_NAME,
               SURNAME,
               CREST_COURT_REPORTER_ID,
               INITIALS,
               REPORT_METHOD,
               OBS_IND,
               lookup_xhb_ref_court_rep_f_id(REF_COURT_REPORTER_FIRM_ID) AS REF_COURT_REPORTER_FIRM_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID
        FROM   EXT_XHB_REF_COURT_REPORTER;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_DISPOSAL_MENU');

        INSERT INTO XHB_REF_DISPOSAL_MENU
        SELECT lookup_xhb_ref_disp_menu_id(REF_DISPOSAL_MENU_ID) AS REF_DISPOSAL_MENU_ID,
               TITLE,
               DISPOSAL_CODE,
               PARENT,
               ABBREV,
               MENU_GROUP,
               SEQ_NO,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID,
               OBS_IND,
               MENU_ITEM_ID
        FROM   EXT_XHB_REF_DISPOSAL_MENU;

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_DISP_MENU_CASE_TYPE');

        INSERT INTO XHB_REF_DISP_MENU_CASE_TYPE
        SELECT XHB_R_DISP_MENU_CASE_TYPE_SEQ.NEXTVAL AS REF_DISPOSAL_MENU_CASE_TYPE_ID,
               lookup_xhb_ref_disp_menu_id(REF_DISPOSAL_MENU_ID) AS REF_DISPOSAL_MENU_ID,
               CASE_TYPE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_REF_DISP_MEN_CASE_TYPE;

/*
        -- Currently no sequence on the solicitor id column, so unable to auto-generate,
        -- no data currently in the baseline data, so leaving out...

        DBMS_APPLICATION_INFO.set_action('Populating XHB_REF_SOLICITOR');

        INSERT INTO XHB_REF_SOLICITOR
        SELECT ******SOLICITOR_ID*****, -- there is no sequence for this...
               CREST_SOLICITOR_NAME,
               IS_IN_CREST,
               lookup_xhb_ref_legal_rep_id(REF_LEGAL_REP_ID) AS REF_LEGAL_REP_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               OBS_IND,
               lookup_xhb_ref_sol_firm_id(REF_SOLICITOR_FIRM_ID) AS REF_SOLICITOR_FIRM_ID
        FROM   EXT_XHB_REF_SOLICITOR;
*/

        DBMS_APPLICATION_INFO.set_action('Clearing cache...');

        -- And finish off by clearing the cache...
        g_array_lookup_cache_t.DELETE;

        DBMS_APPLICATION_INFO.set_action('Finished add_reference_data');

        COMMIT;
    END add_reference_data;


    PROCEDURE DELETE_PUBLIC_DISPLAY_DATA(p_court_id_in IN NUMBER) IS
    BEGIN
        DELETE FROM XHB_DISPLAY_COURT_ROOM
        WHERE  COURT_ROOM_ID IN (
            SELECT cr.COURT_ROOM_ID
            FROM   XHB_COURT_ROOM cr, XHB_COURT_SITE cs
            WHERE  cr.COURT_SITE_ID = cs.COURT_SITE_ID
            AND    cs.COURT_ID = p_court_id_in);

        DELETE FROM XHB_DISPLAY
        WHERE  DISPLAY_ID IN (
            SELECT d.display_id 
            FROM   XHB_DISPLAY d, XHB_DISPLAY_LOCATION dl, XHB_COURT_SITE cs
            WHERE  d.display_location_id = dl.display_location_id
            AND    dl.court_site_id = cs.court_site_id
            AND    cs.court_id = p_court_id_in);

        DELETE FROM XHB_DISPLAY_LOCATION
        WHERE  DISPLAY_LOCATION_ID IN (
            SELECT dl.display_location_id 
            FROM   XHB_DISPLAY_LOCATION dl, XHB_COURT_SITE cs
            WHERE  dl.court_site_id = cs.court_site_id
            AND    cs.court_id = p_court_id_in);

        DELETE FROM XHB_ROTATION_SET_DD
        WHERE ROTATION_SET_ID IN (
            SELECT ROTATION_SET_ID
            FROM   XHB_ROTATION_SETS
            WHERE  COURT_ID = p_court_id_in);

        DELETE FROM XHB_ROTATION_SETS
        WHERE  COURT_ID = p_court_id_in;
    END DELETE_PUBLIC_DISPLAY_DATA;


    PROCEDURE DELETE_REMAINING_STANDING_DATA(p_court_id_in IN NUMBER) IS
    BEGIN
        DELETE FROM XHB_FORMB_RESULT WHERE COURT_ID = p_court_id_in;
    END DELETE_REMAINING_STANDING_DATA;


    PROCEDURE DELETE_REFERENCE_DATA(p_court_id_in IN NUMBER) IS
        l_address_id_t XHB_NUMBER_TABLE_TYP;
    BEGIN
        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_APP_RESULT(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_APP_RESULT    WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_DISPOSAL(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_DISPOSAL      WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_DISPOSAL_LINE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_DISPOSAL_LINE WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_DISPOSAL_TYPE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_DISPOSAL_TYPE WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_HEARING_TYPE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_HEARING_TYPE  WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_JUDGE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_JUDGE         WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_JUSTICE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_JUSTICE       WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_OFFENCE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_OFFENCE       WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_SYSTEM_CODE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_SYSTEM_CODE   WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_DISP_MENU_CASE_TYPE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_DISP_MENU_CASE_TYPE xrdmct1
        WHERE EXISTS (SELECT 1
                      FROM   XHB_REF_DISPOSAL_MENU xrdm,
                             XHB_REF_DISP_MENU_CASE_TYPE xrdmct
                      WHERE  xrdm.ref_disposal_menu_id = xrdmct.ref_disposal_menu_id
                      AND    xrdmct.ROWID = xrdmct1.ROWID
                      AND    xrdm.court_id = p_court_id_in);

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_DISPOSAL_MENU(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_DISPOSAL_MENU WHERE court_id = p_court_id_in;

        -- Store all address ids that we can safely delete...
        DBMS_APPLICATION_INFO.set_action('Storing address ids(' || p_court_id_in || ')');
        SELECT address_id
        BULK COLLECT INTO l_address_id_t
        FROM (SELECT address_id FROM XHB_REF_COURT_REPORTER_FIRM WHERE court_id = p_court_id_in
                UNION ALL
              SELECT address_id FROM XHB_REF_PROSECUTOR_AGENCY   WHERE court_id = p_court_id_in
                UNION ALL
              SELECT address_id FROM XHB_REF_COURT               WHERE court_id = p_court_id_in
                UNION ALL
              SELECT address_id FROM XHB_REF_CHAMBER             WHERE court_id = p_court_id_in
                UNION ALL
              SELECT address_id FROM XHB_REF_SOLICITOR_FIRM      WHERE court_id = p_court_id_in
        );

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_ADVOCATE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_ADVOCATE xra1
        WHERE EXISTS (SELECT 1
                      FROM   XHB_REF_CHAMBER xrc,
                             XHB_REF_ADVOCATE xra
                      WHERE  xrc.ref_chamber_id = xra.ref_chamber_id
                      AND    xra.ROWID = xra1.ROWID
                      AND    xrc.court_id = p_court_id_in);

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_COURT(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_COURT                WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_PROSECUTOR_AGENCY(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_PROSECUTOR_AGENCY    WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_SOLICITOR_FIRM(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_SOLICITOR_FIRM       WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_COURT_REPORTER(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_COURT_REPORTER       WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_COURT_REPORTER_FIRM(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_COURT_REPORTER_FIRM  WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_CHAMBER(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_CHAMBER              WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_REF_LEGAL_REPRESENTATIVE(' || p_court_id_in || ')');
        DELETE FROM XHB_REF_LEGAL_REPRESENTATIVE WHERE court_id = p_court_id_in;

        DBMS_APPLICATION_INFO.set_action('Delete XHB_ADDRESS(' || p_court_id_in || ')');
        DELETE FROM XHB_ADDRESS xa1
        WHERE EXISTS (SELECT 1
                      FROM   TABLE(cast(l_address_id_t AS xhb_number_table_typ)) t1,
                             XHB_ADDRESS xa
                      WHERE  xa.address_id = t1.column_value
                      AND    xa.ROWID = xa1.ROWID);

        DBMS_APPLICATION_INFO.set_action('Finished delete_reference_data');
        COMMIT;
    END DELETE_REFERENCE_DATA;


    PROCEDURE DELETE_COURT_STANDING_DATA(p_court_id_in IN NUMBER) IS
        var_address_id NUMBER;
    BEGIN
        DELETE_REMAINING_STANDING_DATA(p_court_id_in);
        DELETE_REFERENCE_DATA(p_court_id_in);
        DELETE_PUBLIC_DISPLAY_DATA(p_court_id_in);

        -- CR_LIVE_STATUS
        DELETE FROM xhb_cr_live_status
        WHERE  COURT_ROOM_ID IN (
            SELECT cr.COURT_ROOM_ID
            FROM   XHB_COURT_ROOM cr, XHB_COURT_SITE cs
            WHERE  cr.court_site_id = cs.court_site_id
            AND    cs.court_id = p_court_id_in);

        -- CREST_IMPORT
        DELETE FROM XHB_CREST_IMPORT
        WHERE  COURT_ID = p_court_id_in;

        -- DocumentReply
        DELETE FROM XHB_DOCUMENT_REPLY 
        WHERE  COURT_ID = p_court_id_in;

        -- PublicNotice
        DELETE FROM XHB_CONFIGURED_PUBLIC_NOTICE
        WHERE  COURT_ROOM_ID IN (
            SELECT cr.COURT_ROOM_ID
            FROM   XHB_COURT_ROOM cr, XHB_COURT_SITE cs
            WHERE  cr.court_site_id = cs.court_site_id
            AND    cs.court_id = p_court_id_in);

        DELETE FROM XHB_PUBLIC_NOTICE
        WHERE  COURT_ID = p_court_id_in;

        -- COURT/SITE/ROOM
        DELETE FROM xhb_court_room
        WHERE  COURT_ROOM_ID IN (
            SELECT cr.COURT_ROOM_ID
            FROM   XHB_COURT_ROOM cr, XHB_COURT_SITE cs
            WHERE  cr.court_site_id = cs.court_site_id
            AND    cs.court_id = p_court_id_in);

        DELETE FROM XHB_COURT_SITE
        WHERE COURT_ID = p_court_id_in;

        DELETE FROM XHB_TAA_COURT_INFO
        WHERE COURT_ID = p_court_id_in;

        BEGIN
            SELECT ADDRESS_ID INTO var_address_id
            FROM XHB_COURT
            WHERE COURT_ID = p_court_id_in;

            DELETE FROM XHB_CONTACT_DETAIL
            WHERE address_id = var_address_id;

            DELETE FROM XHB_COURT
            WHERE COURT_ID = p_court_id_in;

            DELETE FROM XHB_ADDRESS
            WHERE  ADDRESS_ID = var_address_id;
        EXCEPTION 
            WHEN NO_DATA_FOUND THEN
                NULL;
        END;

        COMMIT;    
    END DELETE_COURT_STANDING_DATA;


    PROCEDURE ADD_PUBLIC_DISPLAY(p_court_id_in IN NUMBER) IS
        var_public_view_rs NUMBER;
        var_court_room_rs NUMBER;
        var_jury_room_rs NUMBER;
        var_summary_by_name_rs NUMBER;
        var_status_rs NUMBER;
        var_daily_list_rs NUMBER;
        var_all_lists_rs NUMBER;

        var_court_id NUMBER;
        var_courtsite_id NUMBER;
        var_courtroom_id NUMBER;
        var_courtroom_num NUMBER;

        var_display_location NUMBER;
        var_display NUMBER;
    BEGIN
        DELETE_PUBLIC_DISPLAY_DATA(p_court_id_in);

        var_court_id := p_court_id_in;

        --_________________________
        --Set up the Rotation Sets.
        --¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
        SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_public_view_rs FROM dual;
        INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
        VALUES (var_public_view_rs, var_court_id, 'Public View', 'Y');

        SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_court_room_rs FROM dual;
        INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
        VALUES (var_court_room_rs, var_court_id, 'Court Room', 'Y');

        SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_jury_room_rs FROM dual;
        INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
        VALUES (var_jury_room_rs, var_court_id, 'Jury Room', 'Y');

        SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_summary_by_name_rs FROM dual;
        INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
        VALUES (var_summary_by_name_rs, var_court_id, 'Summary By Name', 'Y');

        SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_status_rs FROM dual;
        INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
        VALUES (var_status_rs, var_court_id, 'Status', 'Y');

        SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_daily_list_rs FROM dual;
        INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
        VALUES (var_daily_list_rs, var_court_id, 'Daily List', 'Y');

        SELECT XHB_ROTATION_SETS_SEQ.nextval INTO var_all_lists_rs FROM dual;
        INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
        VALUES (var_all_lists_rs, var_court_id, 'All Lists', 'Y');

        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_public_view_rs, 3, 20, 2); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_public_view_rs, 5, 20, 1); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_court_room_rs, 1, 20, 1); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_court_room_rs, 2, 20, 2); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_jury_room_rs, 6, 20, 1); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_summary_by_name_rs, 5, 20, 1); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_status_rs, 4, 20, 1); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_daily_list_rs, 3, 20, 1); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 1, 10, 2); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 2, 10, 3); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 3, 10, 4); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 4, 10, 1); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 5, 10, 6); 
        INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
        VALUES (XHB_ROTATION_SET_DD_SEQ.nextval, var_all_lists_rs, 6, 10, 5);

        COMMIT;

        FOR r_courtsite_id IN 
            (SELECT court_site_id
            FROM XHB_COURT_SITE
            WHERE court_id = var_court_id
            ORDER BY court_site_id) 
        LOOP
            var_courtsite_id := r_courtsite_id.court_site_id;
            --_____________________________
            --Set up the Display Locations.
            --¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'e_v', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 2, var_display_location, var_summary_by_name_rs, 'e_v_plasma_display', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;


            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'reception', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 2, var_display_location, var_daily_list_rs, 'reception_42_display', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;


            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'public_restaurant', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 1, var_display_location, var_public_view_rs, 'public_rest_18in_1', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 1, var_display_location, var_public_view_rs, 'public_rest_18in_2', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;


            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'jury_lounge', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 1, var_display_location, var_jury_room_rs, 'jury_lounge_18in_1', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 1, var_display_location, var_jury_room_rs, 'jury_lounge_18in_2', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;


            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'witness_service', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 1, var_display_location, var_public_view_rs, 'witness_18in_display', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;


            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'police_1st_floor', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 1, var_display_location, var_public_view_rs, 'police_18in_display', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;


            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'ps_s_o_1st_floor', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 1, var_display_location, var_public_view_rs, 'ps_sec_off_display', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;


            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'n_w_c_w_a', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 2, var_display_location, var_status_rs, 'nwcwa_plasma_display', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;


            SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
            INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
            VALUES (var_display_location, 'b_a_s_r', var_courtsite_id);

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 1, var_display_location, var_public_view_rs, 'b_a_s_r_18in_display', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;

            SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
            INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
            DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
            var_display, 2, var_display_location, var_public_view_rs, 'b_a_s_r_42in_display', 'enGB', 'Y'); 

            INSERT INTO XHB_DISPLAY_COURT_ROOM 
            SELECT var_display, court_room_id
            FROM XHB_COURT_ROOM
            WHERE court_site_id = var_courtsite_id;

            -- Loop Court Rooms for Court Detail Screens

            FOR r_courtroom_id IN 
                (SELECT court_room_id, crest_court_room_no
                FROM XHB_COURT_ROOM
                WHERE court_site_id = var_courtsite_id
                ORDER BY crest_court_room_no) 
            LOOP

                var_courtroom_id := r_courtroom_id.court_room_id;
                var_courtroom_num := r_courtroom_id.crest_court_room_no;

                SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
                INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
                VALUES (var_display_location, 'court_room_'||var_courtroom_num, var_courtsite_id);

                SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
                INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
                DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
                var_display, 1, var_display_location, var_court_room_rs, 'courtroom_'||var_courtroom_num||'_display', 'enGB', 'N'); 

                INSERT INTO XHB_DISPLAY_COURT_ROOM VALUES (var_display, var_courtroom_id);

            END LOOP; -- Court Room

        END LOOP; -- Court Site

        -- Set up the display for View information pages
        SELECT XHB_DISPLAY_LOCATION_SEQ.nextval INTO var_display_location FROM dual;
        INSERT INTO XHB_DISPLAY_LOCATION ( DISPLAY_LOCATION_ID, DESCRIPTION_CODE, COURT_SITE_ID )
        VALUES (var_display_location, 'v_i_p', var_courtsite_id);

        SELECT XHB_DISPLAY_SEQ.nextval INTO var_display FROM dual;
        INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
        DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
        var_display, 3, var_display_location, var_all_lists_rs, 'v_i_p', 'enGB', 'Y'); 

        INSERT INTO XHB_DISPLAY_COURT_ROOM 
        SELECT var_display, cr.court_room_id
        FROM XHB_COURT_ROOM cr, XHB_COURT_SITE cs
        WHERE cr.court_site_id = cs.court_site_id
        AND   cs.court_id = var_court_id;

        COMMIT;
    END ADD_PUBLIC_DISPLAY;


    -- This standing data can only be inserted after reference data is loaded
    PROCEDURE ADD_REMAINING_STANDING_DATA(p_court_id_in IN NUMBER) IS
    BEGIN
        -- FormBResult
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'G' AND COURT_ID =p_court_id_in ),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NG' AND COURT_ID =p_court_id_in ),'Verdict',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'AA' AND COURT_ID =p_court_id_in),'Verdict',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'AC' AND COURT_ID =p_court_id_in),'Verdict',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'DUD' AND COURT_ID =p_court_id_in),'Verdict',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GA' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GAJ' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GJJ' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GL' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GLJ' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'JUTA' AND COURT_ID =p_court_id_in),'Other',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGIS' AND COURT_ID =p_court_id_in),'Verdict',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGJJ' AND COURT_ID =p_court_id_in),'Verdict',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGJU' AND COURT_ID =p_court_id_in),'Verdict',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NV' AND COURT_ID =p_court_id_in),'Other',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'O' AND COURT_ID =p_court_id_in),'Other',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'RTG' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'RTNG' AND COURT_ID =p_court_id_in),'Verdict',p_court_id_in);

        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'NPT' AND COURT_ID =p_court_id_in),NULL,p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'AA' AND COURT_ID =p_court_id_in),NULL,p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'G' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'NG' AND COURT_ID =p_court_id_in),NULL,p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'AC' AND COURT_ID =p_court_id_in),NULL,p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPG' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPGJ' AND COURT_ID =p_court_id_in),NULL,p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPNG' AND COURT_ID =p_court_id_in),NULL,p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'GAO' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'GLO' AND COURT_ID =p_court_id_in),'Conviction',p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'O' AND COURT_ID =p_court_id_in),NULL,p_court_id_in);
        INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P',(SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'P' AND COURT_ID =p_court_id_in),NULL,p_court_id_in);

        COMMIT;
    END ADD_REMAINING_STANDING_DATA;


    PROCEDURE ADD_COURT_STANDING_DATA(p_court_name_in IN VARCHAR2) IS
        var_court_id NUMBER;
    BEGIN
        SELECT XHB_COURT_SEQ.NEXTVAL
        INTO var_court_id
        FROM dual;

        ADD_COURT_STANDING_DATA(var_court_id, p_court_name_in);
    END ADD_COURT_STANDING_DATA;


    PROCEDURE ADD_COURT_STANDING_DATA(p_court_id_in   IN NUMBER,
                                      p_court_name_in IN  VARCHAR2) IS
            var_contact_id NUMBER;
            var_address_id NUMBER;
            var_court_id NUMBER;
            var_courtsite_id NUMBER;
            var_courtroom_id NUMBER;
            var_courtroom_num NUMBER;
            var_crest_court_id NUMBER;
    BEGIN
            var_court_id := p_court_id_in;

            DELETE_COURT_STANDING_DATA(var_court_id);

            -- Court/site/room
            SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

            INSERT INTO XHB_ADDRESS (ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY)
            VALUES (var_address_id, p_court_name_in, p_court_name_in||' Street', NULL, NULL, 'Development', 'London', 'TR41 NNG', 'England');

            -- Contact details Dockford Training
            SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

            INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID)
            VALUES (var_contact_id, 'TEL', '020 8123 4567', var_address_id);

            SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

            INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
            VALUES (var_contact_id, 'FAX', '020 8123 7954', var_address_id);        

            -- Court
            SELECT MAX(CREST_COURT_ID) INTO var_crest_court_id FROM XHB_COURT;

            INSERT INTO XHB_COURT (COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE)
            VALUES (var_court_id, 'CROWN', 'SOUTH EASTERN', p_court_name_in, to_char(var_crest_court_id + 1), 'CROWN COURT', UPPER(SUBSTR(p_court_name_in,1,5)), var_address_id, 'CSA00110:90', 'Y','N', p_court_name_in||' PROBATION OFFICE', p_court_name_in,p_court_name_in||' CROWN COURT', '01AA'); 

            INSERT INTO XHB_TAA_COURT_INFO VALUES(var_court_id, 'CREST_IP_ADDRESS', '10.31.13.5');

            -- Court Site   
            SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

            INSERT INTO XHB_COURT_SITE (COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID)
            VALUES (var_courtsite_id , p_court_name_in, 'A', var_court_id, var_address_id, 'Court Site A', to_char(var_crest_court_id + 1)); 

            -- CourtRoom Dockford Training
            var_courtroom_num := 0;

            FOR i IN 1..10 LOOP
              var_courtroom_num := var_courtroom_num + 1;
              SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual;
              INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME)
              VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
            END LOOP;

            COMMIT;

            -- CR_LIVE_STATUS
            INSERT INTO xhb_cr_live_status (court_room_id, time_status_set, internet_status)
            select cr.court_room_id,
                   SYSDATE,
                   'No Information to display'
            FROM   xhb_court_room cr, xhb_court_site cs
            WHERE  cs.court_site_id = cr.court_site_id
            AND    cs.court_id = var_court_id;  

            -- CREST_IMPORT
            INSERT INTO  XHB_CREST_IMPORT(STATUS, IMPORT_TYPE, COURT_ID)
            SELECT 'N', IMPORT_TYPE, var_court_id
            FROM   XHB_CREST_IMPORT_TYPE;

            -- DocumentReply
            INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'DL');
            INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'DLP');
            INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'WL');
            INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'WLL');
            INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'FL');
            INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'RL');

            COMMIT;

            -- PublicDisplay_Court
            ADD_PUBLIC_DISPLAY(var_court_id);

            -- PublicNotice
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Reporting restrictions. For details please see Court Manager.', 100);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'In chambers, no entry.' , 200);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Members of the public are requested not to enter.' , 300);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'TV link in progress - please enter quietly.' , 400);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Video being played - please enter quietly.' , 500);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'If you wish to enter, please do so quietly.' , 600);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Reporting restrictions lifted.' , 700);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Please switch off mobile phones.' , 800);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Food and drink must not be consumed in the courtrooms.', 900);
            insert into XHB_PUBLIC_NOTICE (COURT_ID, PUBLIC_NOTICE_DESC, DEFINITIVE_PN_ID) values ( var_court_id,   'Bench warrant in progress.' , 1000);

            insert into xhb_configured_public_notice  ( IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID)
            select 0, court_room_id, public_notice_id 
            from   xhb_public_notice pn, xhb_court_room cr
            where  pn.court_id = var_court_id
            and    cr.court_room_id in ( select court_room_id 
                                         from   XHB_COURT_ROOM cr2, XHB_COURT_SITE cs
                                         where  cr2.court_site_id = cs.court_site_id
                                         and    cs.COURT_ID = var_court_id );

            COMMIT;

            -- Add Ref Data
            ADD_REFERENCE_DATA(var_court_id);

            -- FormBResult
            ADD_REMAINING_STANDING_DATA(var_court_id);
    END ADD_COURT_STANDING_DATA;
END XHB_STANDING_DATA_PKG;
/
show errors