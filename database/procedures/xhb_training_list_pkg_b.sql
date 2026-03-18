CREATE OR REPLACE PACKAGE BODY training_list_pkg AS
    /*
     *
     * GET_XML_CLOB
     *
     * A package-private utility method to extract the common code for loading
     * a CLOB from a file on the local database server.
     *
     * This function loads the specified file from the default directory for
     * the training application and returns it as a CLOB.
     *
     */
    FUNCTION get_xml_clob(p_file_name_in IN VARCHAR2) RETURN CLOB IS
        daily_list_bfile BFILE := BFILENAME('DBDIR', p_file_name_in);
        daily_list CLOB := ' ';
        destination_offset INTEGER := 1;
        source_offset INTEGER := 1;
        language_context INTEGER := DBMS_LOB.default_lang_ctx;
        warning_message INTEGER;
    BEGIN
        DBMS_LOB.OPEN(daily_list_bfile);
        DBMS_LOB.OPEN(daily_list, DBMS_LOB.LOB_READWRITE);

        DBMS_LOB.loadClobFromFile(daily_list, daily_list_bfile, DBMS_LOB.LOBMAXSIZE,
        destination_offset, source_offset, NLS_CHARSET_ID('UTF8'),
        language_context, warning_message);

        DBMS_LOB.CLOSE(daily_list_bfile);
        DBMS_LOB.CLOSE(daily_list);

        -- Check for the only possible warning message...
        IF (warning_message = DBMS_LOB.WARN_INCONVERTIBLE_CHAR) THEN
            DBMS_OUTPUT.put_line('Warning! Some characters could not be converted');
        END IF;

        RETURN ltrim(daily_list);
    END get_xml_clob;


    /*
     *
     * GET_TOMORROWS_LIST
     *
     * This procedure will obtain the available list from an OS souce
     * file on the Xhibit database server and load it into the
     * XHB_XML_DOCUMENT_TABLE.
     *
     */
    PROCEDURE get_tomorrows_list(p_results_out OUT SYS_REFCURSOR,
                                 p_court_id    IN  XHB_COURT.COURT_ID%TYPE,
                                 p_date        IN  DATE)
    IS
        doc_filename  VARCHAR2(256);
        status_count  NUMBER;
        list_count    NUMBER;
        xml_clob      CLOB;
		l_court_name  VARCHAR2(100);
		l_xml         XMLType;	
		l_clob_id     NUMBER;
	BEGIN
        SELECT count(*)
        INTO   status_count
        FROM   xhb_training_status
        WHERE  court_id = p_court_id;

        IF status_count > 0 THEN
            -- There are rows for this court.
            -- Check that there is no purge or load in progress
            SELECT count(*)
            INTO   status_count
            FROM   xhb_training_status
            WHERE  court_id = p_court_id
            AND    (operation_code = 'P' OR operation_code = 'L')
            AND    (status_code = 'R' OR status_code = 'I');

            IF status_count > 0 THEN
                -- Database schema being purged so no actions allowed
                RAISE_APPLICATION_ERROR(-20007,'Purge/Load cycle already in progress');
            END IF;
        END IF;

        SELECT count(*)
        INTO   list_count
        FROM   xhb_xml_document
        WHERE  court_id = p_court_id
        AND    document_type = 'DL'
        AND    date_created = p_date;

        IF list_count > 0 THEN
            -- A list for tomorrow is already loaded into the XHB_XML_DOCUMENT table
            RAISE_APPLICATION_ERROR(-20002,'There is already a tomorrows list for this date');
        END IF;

	    SELECT COURT_NAME
		INTO   l_court_name
		FROM   XHB_COURT
		WHERE  COURT_ID = p_court_id;

		-- First add tomorrows list
        SELECT value2
        INTO   doc_filename
        FROM   xhb_training_ref_data
        WHERE  parameter = 'TOMORROWS_LIST'
        AND    court_id = p_court_id;

        IF doc_filename IS NULL THEN
            RAISE_APPLICATION_ERROR(-20900, SQLCODE ||': '|| SQLERRM);
        END IF;

		xml_clob := get_xml_clob(doc_filename);
		l_xml := XMLType.CREATEXML(xml_clob);
    
		SELECT UPDATEXML(l_xml, '/cs:DailyList/cs:ListHeader/cs:StartDate/text()', TO_CHAR(p_date, 'YYYY-MM-DD'),
						 		'/cs:DailyList/cs:ListHeader/cs:EndDate/text()',   TO_CHAR(p_date, 'YYYY-MM-DD'),
								'/cs:DailyList/cs:CrownCourt/cs:CourtHouseName/text()', l_court_name, 
								'/cs:DailyList/cs:CourtLists/cs:CourtList/cs:CourtHouse/cs:CourtHouseName/text()', l_court_name,
								'/cs:DailyList/cs:ListHeader/cs:PublishedTime/text()', TO_CHAR(sysdate, 'YYYY-MM-DD') || 'T14:10:00' 
								
						 )
	    INTO l_xml
		FROM dual;

		-- This section of code will add <?xml version="1.0" encoding="iso-8859-1"?> back 
		-- to the start of the xml_document if it is missing (UPDATEXML seems to strip it off).
		IF (DBMS_LOB.INSTR(xml_clob, '?>') > 0) THEN
		   IF (DBMS_LOB.INSTR(l_xml.getClobVal(), '?>') > 0) THEN
			   xml_clob := l_xml.getClobVal();
		   ELSE     
			   xml_clob := DBMS_LOB.SUBSTR(xml_clob, DBMS_LOB.INSTR(xml_clob, '?>') + 1);
			   DBMS_LOB.APPEND(xml_clob, l_xml.getClobVal());
		   END IF;
		ELSE
		   xml_clob := l_xml.getClobVal();
		END IF;

        INSERT INTO XHB_CLOB
               (clob_id, clob_data)
        VALUES (XHB_CLOB_SEQ.NEXTVAL, xml_clob)
        RETURNING clob_id
        INTO      l_clob_id;
		
        INSERT INTO xhb_xml_document (DATE_CREATED,
                                      DOCUMENT_TITLE,
                                      STATUS,
                                      EXPIRY_DATE,
                                      DOCUMENT_TYPE,
                                      COURT_ID,
                                      XML_DOCUMENT_CLOB_ID,
                                      LANGUAGE,
                                      COUNTRY)
                              VALUES (p_date,
                                     'Daily List FINAL v1 ' || to_char(p_date, 'YYYY-MM-DD HH24:MI:SS'),
                                     'ND',
                                      NULL,
                                     'DL',
                                      p_court_id,
                                      l_clob_id,
                                     'en',
                                     'GB');

		-- Now add a daily list for distribution
		SELECT value2
		INTO   doc_filename
		FROM   xhb_training_ref_data
		WHERE  parameter = 'DAILY_LIST_DIST'
		AND    court_id = p_court_id;

		IF doc_filename IS NULL THEN
			RAISE_APPLICATION_ERROR(-20900, SQLCODE ||': '|| SQLERRM);
		END IF;

		xml_clob := get_xml_clob(doc_filename);
		l_xml := XMLType.CREATEXML(xml_clob);

		SELECT UPDATEXML(l_xml, '/DailyList/ListHeader/StartDate/text()', TO_CHAR(p_date, 'YYYY-MM-DD'),
								'/DailyList/ListHeader/EndDate/text()',   TO_CHAR(p_date, 'YYYY-MM-DD'),
								'/DailyList/CrownCourt/CourtHouseName/text()', l_court_name, 
								'/DailyList/CrownCourt/CourtHouseAddress/Line[5]/text()', l_court_name,
								'/DailyList/CrownCourt/CourtHouseDX/text()', 'DX 97420 ' || l_court_name || ' 1',
								'/DailyList/CourtLists/CourtList/CourtHouse/CourtHouseName/text()', l_court_name,
								'/DailyList/ListHeader/PublishedTime/text()', TO_CHAR(sysdate, 'YYYY-MM-DD') || 'T14:10:00' 

						 )
		INTO l_xml
		FROM dual;

		-- This section of code will add <?xml version="1.0" encoding="iso-8859-1"?> back 
		-- to the start of the xml_document if it is missing (UPDATEXML seems to strip it off).
		IF (DBMS_LOB.INSTR(xml_clob, '?>') > 0) THEN
		   IF (DBMS_LOB.INSTR(l_xml.getClobVal(), '?>') > 0) THEN
			   xml_clob := l_xml.getClobVal();
		   ELSE     
			   xml_clob := DBMS_LOB.SUBSTR(xml_clob, DBMS_LOB.INSTR(xml_clob, '?>') + 1);
			   DBMS_LOB.APPEND(xml_clob, l_xml.getClobVal());
		   END IF;
		ELSE
		   xml_clob := l_xml.getClobVal();
		END IF;

		INSERT INTO XHB_CLOB
			   (clob_id, clob_data)
		VALUES (XHB_CLOB_SEQ.NEXTVAL, xml_clob)
		RETURNING clob_id
		INTO      l_clob_id;

		INSERT INTO xhb_xml_document (XML_DOCUMENT_ID,
                                              DATE_CREATED,
					      DOCUMENT_TITLE,
					      STATUS,
					      EXPIRY_DATE,
					      DOCUMENT_TYPE,
					      COURT_ID,
					      XML_DOCUMENT_CLOB_ID,
					      LANGUAGE,
					      COUNTRY)
				      VALUES (XHB_XML_DOCUMENT_SEQ.nextval,
                                              p_date,
					     'Daily List FINAL v1 ' || to_char(p_date, 'YYYY-MM-DD HH24:MI:SS'),
					     'ND',
					      NULL,
					     'DLD',
					      p_court_id,
					      l_clob_id,
					     'en',
					     'GB');

        INSERT INTO xhb_wll_control (STATUS,
                                     EXPIRY_DATE,
                                     XML_DOCUMENT_ID)
                             VALUES ('ND',
                                     sysdate + 1,
                                     XHB_XML_DOCUMENT_SEQ.currval);

        OPEN p_results_out FOR
            SELECT *
            FROM   xhb_training_status
            WHERE  court_id = -1;
    END get_tomorrows_list;


    /*
     *
     * GET_WARNED_LIST
     *
     * This procedure will obtain the available list from an OS souce
     * file on the Xhibit database server and load it into the
     * XHB_XML_DOCUMENT_TABLE.
     *
     */
    PROCEDURE get_warned_list(p_results_out OUT SYS_REFCURSOR,
                              p_court_id    IN XHB_COURT.COURT_ID%TYPE,
                              p_date        IN DATE) IS
        doc_filename  VARCHAR2(256);
        status_count  NUMBER;
        list_count    NUMBER;
        xml_clob      CLOB;
		l_court_name  VARCHAR2(100);
		l_court_code  VARCHAR2(3);
		l_xml         XMLType;	
        l_clob_id     NUMBER;
    BEGIN
        SELECT count(*)
        INTO   status_count
        FROM   xhb_training_status
        WHERE  court_id = p_court_id;

        IF status_count > 0 THEN
            -- There are rows for this court.
            -- Check that there is no purge or load in progress
            SELECT count(*)
            INTO   status_count
            FROM   xhb_training_status
            WHERE  court_id = p_court_id
            AND    (operation_code = 'P' OR operation_code = 'L')
            AND    (status_code = 'R' OR status_code = 'I');

            IF status_count > 0 THEN
                -- Database schema being purged so no actions allowed
                RAISE_APPLICATION_ERROR(-20007,'Purge/Load cycle already in progress');
            END IF;
        END IF;

        SELECT count(*)
        INTO   list_count
        FROM   xhb_xml_document
        WHERE  court_id = p_court_id
        AND    document_type = 'WL'
        AND    date_created = p_date;

        IF list_count > 0 THEN
            -- A warned list is already loaded into the XHB_XML_DOCUMENT table
            RAISE_APPLICATION_ERROR(-20004,'There is already a warned list for the selected date');
        END IF;

	    SELECT COURT_NAME, CREST_COURT_ID
		INTO   l_court_name, l_court_code
		FROM   XHB_COURT
		WHERE  COURT_ID = p_court_id;

		-- Add the current warned list
        SELECT value2
        INTO   doc_filename
        FROM   xhb_training_ref_data
        WHERE  parameter = 'WARNED_LIST'
        AND    court_id = p_court_id;

        IF doc_filename IS NULL THEN
            RAISE_APPLICATION_ERROR(-20900, SQLCODE ||': '|| SQLERRM);
        END IF;

        xml_clob := get_xml_clob(doc_filename);
		l_xml := XMLType.CREATEXML(xml_clob);
    
		SELECT UPDATEXML(l_xml, '/cs:WarnedList/cs:ListHeader/cs:StartDate/text()', TO_CHAR(p_date, 'YYYY-MM-DD'),
						 		'/cs:WarnedList/cs:ListHeader/cs:EndDate/text()',   TO_CHAR(p_date+14, 'YYYY-MM-DD'),
								'/cs:WarnedList/cs:CrownCourt/cs:CourtHouseName/text()', l_court_name, 
								'/cs:WarnedList/cs:CourtLists/cs:CourtList/cs:CourtHouse/cs:CourtHouseName/text()', l_court_name,
								'/cs:WarnedList/cs:ListHeader/cs:PublishedTime/text()', TO_CHAR(sysdate, 'YYYY-MM-DD') || 'T14:10:00' 
								
						 )
	    INTO l_xml
		FROM dual;

		-- This section of code will add <?xml version="1.0" encoding="iso-8859-1"?> back 
		-- to the start of the xml_document if it is missing (UPDATEXML seems to strip it off).
		IF (DBMS_LOB.INSTR(xml_clob, '?>') > 0) THEN
		   IF (DBMS_LOB.INSTR(l_xml.getClobVal(), '?>') > 0) THEN
			   xml_clob := l_xml.getClobVal();
		   ELSE     
			   xml_clob := DBMS_LOB.SUBSTR(xml_clob, DBMS_LOB.INSTR(xml_clob, '?>') + 1);
			   DBMS_LOB.APPEND(xml_clob, l_xml.getClobVal());
		   END IF;
		ELSE
		   xml_clob := l_xml.getClobVal();
		END IF;

        INSERT INTO XHB_CLOB
               (clob_id, clob_data)
        VALUES (XHB_CLOB_SEQ.NEXTVAL, xml_clob)
        RETURNING clob_id
        INTO      l_clob_id;
		
        INSERT INTO xhb_xml_document (DATE_CREATED,
                                      DOCUMENT_TITLE,
                                      STATUS,
                                      EXPIRY_DATE,
                                      DOCUMENT_TYPE,
                                      COURT_ID,
                                      XML_DOCUMENT_CLOB_ID,
                                      LANGUAGE,
                                      COUNTRY)
                              VALUES (p_date,
                                     'Warned List FINAL v1 ' || to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS'),
                                     'ND',
                                      NULL,
                                     'WL',
                                      p_court_id,
                                      l_clob_id,
                                     'en',
                                     'GB');

		-- Add the warned list for distribution (ie warned list letters)
        SELECT value2
        INTO   doc_filename
        FROM   xhb_training_ref_data
        WHERE  parameter = 'WARNED_LIST_DIST'
        AND    court_id = p_court_id;

        IF doc_filename IS NULL THEN
            RAISE_APPLICATION_ERROR(-20900, SQLCODE ||': '|| SQLERRM);
        END IF;

        xml_clob := get_xml_clob(doc_filename);
		l_xml := XMLType.CREATEXML(xml_clob);

		SELECT UPDATEXML(l_xml, '/WarnedList/ListHeader/StartDate/text()', TO_CHAR(p_date, 'YYYY-MM-DD'),
								'/WarnedList/ListHeader/EndDate/text()',   TO_CHAR(p_date+14, 'YYYY-MM-DD'),
								'/WarnedList/CrownCourt/CourtHouseName/text()', l_court_name,
								'/WarnedList/CrownCourt/CourtHouseCode/text()', l_court_code,
								'/WarnedList/CrownCourt/CourtHouseAddress/Line[3]/text()', l_court_name,
								'/WarnedList/CrownCourt/CourtHouseDX/text()', 'DX 97420 ' || l_court_name || ' 1',
								'/WarnedList/WarnedListDetail/DeadLineDate/text()', TO_CHAR(p_date+14, 'YYYY-MM-DD'),
								'/WarnedList/CourtLists/CourtList/CourtHouse/CourtHouseName/text()', 'at ' || l_court_name,
								'/WarnedList/CourtLists/CourtList/CourtHouse/CourtHouseCode/text()', l_court_code,
								'/WarnedList/CourtLists/CourtList/CourtHouse/CourtHouseAddress/Line[3]/text()', l_court_name,
								'/WarnedList/CourtLists/CourtList/CourtHouse/CourtHouseDX/text()', 'DX 97420 ' || l_court_name || ' 1',
								'/WarnedList/CourtLists/CourtList/WarnedForCourts/WarnedForCourt[1]/text()', 'at ' || l_court_name,
								'/WarnedList/CourtLists/CourtList/WarnedForCourts/WarnedForCourt[2]/text()', 'at ANNEXE - ' || l_court_name || ' SITE 2',
								'/WarnedList/CourtLists/CourtList/WarnedForCourts/WarnedForCourt[3]/text()', 'at VIRTUAL ANNEXE- ' || l_court_name || ' SITE 3',
								'/WarnedList/ListHeader/PublishedTime/text()', TO_CHAR(sysdate, 'YYYY-MM-DD') || 'T14:10:00' 
						 )
		INTO l_xml
		FROM dual;

		-- This section of code will add <?xml version="1.0" encoding="iso-8859-1"?> back 
		-- to the start of the xml_document if it is missing (UPDATEXML seems to strip it off).
		IF (DBMS_LOB.INSTR(xml_clob, '?>') > 0) THEN
		   IF (DBMS_LOB.INSTR(l_xml.getClobVal(), '?>') > 0) THEN
			   xml_clob := l_xml.getClobVal();
		   ELSE     
			   xml_clob := DBMS_LOB.SUBSTR(xml_clob, DBMS_LOB.INSTR(xml_clob, '?>') + 1);
			   DBMS_LOB.APPEND(xml_clob, l_xml.getClobVal());
		   END IF;
		ELSE
		   xml_clob := l_xml.getClobVal();
		END IF;

        INSERT INTO XHB_CLOB
               (clob_id, clob_data)
        VALUES (XHB_CLOB_SEQ.NEXTVAL, xml_clob)
        RETURNING clob_id
        INTO      l_clob_id;
		
        INSERT INTO xhb_xml_document (XML_DOCUMENT_ID,
                                      DATE_CREATED,
                                      DOCUMENT_TITLE,
                                      STATUS,
                                      EXPIRY_DATE,
                                      DOCUMENT_TYPE,
                                      COURT_ID,
                                      XML_DOCUMENT_CLOB_ID,
                                      LANGUAGE,
                                      COUNTRY)
                              VALUES (XHB_XML_DOCUMENT_SEQ.nextval,
                                      p_date,
                                     'Warned List FINAL v1 ' || to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS'),
                                     'ND',
                                      NULL,
                                     'WLD',
                                      p_court_id,
                                      l_clob_id,
                                     'en',
                                     'GB');

        INSERT INTO xhb_wll_control (STATUS,
                                     EXPIRY_DATE,
                                     XML_DOCUMENT_ID)
                             VALUES ('ND',
                                     sysdate + 1,
                                     XHB_XML_DOCUMENT_SEQ.currval);

        OPEN p_results_out FOR
            SELECT *
            FROM   xhb_training_status
            WHERE  court_id = -1;
    END get_warned_list;


    /*
     *
     * GET_DAILY_LIST_DATES
     *
     */
    PROCEDURE get_daily_list_dates(p_results_out OUT SYS_REFCURSOR,
                                   p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS
    BEGIN
        OPEN p_results_out FOR
            SELECT  list_id,
                    list_type,
                    start_date,
                    end_date,
                    status,
                    edition_no,
                    published_time,
                    print_reference,
                    crest_list_id,
                    court_id,
                    list_court_type
            FROM    xhb_hearing_list
            WHERE   court_id = p_court_id;
    END get_daily_list_dates;


    /*
     *
     * AMEND_DAILT_LIST_DATE
     *
     */
    PROCEDURE amend_daily_list_date(p_results_out OUT SYS_REFCURSOR,
                                    p_court_id    IN   XHB_COURT.COURT_ID%TYPE,
                                    p_list_id     IN   XHB_HEARING_LIST.LIST_ID%TYPE,
                                    p_date        IN   DATE) IS
        daily_list_count   NUMBER;
        original_list_date DATE;
    BEGIN
        SELECT count(*)
        INTO   daily_list_count
        FROM   xhb_hearing_list
        WHERE  court_id = p_court_id
        AND    start_date = p_date;

        IF daily_list_count != 0 THEN
            RAISE_APPLICATION_ERROR(-20006, 'List already exists for that date');
        END IF;

        BEGIN
            SELECT start_date INTO original_list_date
            FROM   XHB_HEARING_LIST
            WHERE  list_id = p_list_id;

            UPDATE xhb_hearing_list
            SET    start_date = p_date,
                   end_date = p_date
            WHERE  list_id = p_list_id
            AND    court_id = p_court_id;

            UPDATE XHB_SCHEDULED_HEARING
            SET    original_time = TRUNC(p_date),
                   not_before_time = TO_DATE(TO_CHAR(p_date,'DD/MON/YYYY')
                                     ||TO_CHAR(not_before_time,'HH24:MI:SS'),'DD/MON/YYYYHH24:MI:SS')
            WHERE  sitting_id IN (SELECT sitting_id
                                  FROM   XHB_SITTING
                                  WHERE  list_id = p_list_id);

            UPDATE XHB_COURT_LOG_ENTRY
            SET    DATE_TIME = TO_DATE(TO_CHAR(p_date,'DD/MON/YYYY')||TO_CHAR(DATE_TIME,'HH24:MI:SS'),'DD/MON/YYYYHH24:MI:SS')
            WHERE  SCHEDULED_HEARING_ID IN (SELECT scheduled_hearing_id 
                                            FROM XHB_SCHEDULED_HEARING sh, XHB_SITTING s  
                                            WHERE sh.sitting_id = s.sitting_id
                                            AND   s.list_id = p_list_id);

            UPDATE XHB_COURT_LOG_ENTRY
            SET    DATE_TIME = TO_DATE(TO_CHAR(p_date,'DD/MON/YYYY')||TO_CHAR(DATE_TIME,'HH24:MI:SS'),'DD/MON/YYYYHH24:MI:SS')
            WHERE  TRUNC(DATE_TIME) = TRUNC(original_list_date)
            AND    scheduled_hearing_id IS NULL;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20900,SQLCODE || ': ' || SQLERRM);
        END;

        OPEN p_results_out FOR
            SELECT  list_id,
                    list_type,
                    start_date,
                    end_date,
                    status,
                    edition_no,
                    published_time,
                    print_reference,
                    crest_list_id,
                    court_id,
                    list_court_type
            FROM    xhb_hearing_list
            WHERE   court_id = p_court_id
            AND     list_id  = p_list_id;
    END amend_daily_list_date;
END training_list_pkg;
/
show errors
