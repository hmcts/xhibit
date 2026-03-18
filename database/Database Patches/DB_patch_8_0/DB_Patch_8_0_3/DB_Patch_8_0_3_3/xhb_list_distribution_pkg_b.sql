CREATE OR REPLACE PACKAGE BODY xhb_list_distribution_pkg AS

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- Constant definitions for the document status'
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------

    -- 'DA' and 'PE' are also defined later as constants to SQL queries to help
    -- in the CBO's calculations for optimal plan...

    --G_NEW_DOCUMENT          CONSTANT VARCHAR2(2) := 'ND';
    G_DOCUMENT_APPROVED     CONSTANT VARCHAR2(2) := 'DA';  -- The list has been approved for processing...
    G_PROCESSING_DOCUMENT   CONSTANT VARCHAR2(2) := 'PD';  -- Processing currently in progress, 1st attempt...
    G_PROCESSING_DOCUMENT_2 CONSTANT VARCHAR2(2) := 'SD';  -- Processing currently in progress, 2nd attempt...
    G_PROCESSING_COMPLETE   CONSTANT VARCHAR2(2) := 'PC';  -- Processing successful...
    G_PROCESSING_ERROR      CONSTANT VARCHAR2(2) := 'PE';  -- First attempt at processing failed, may retry later...
    G_PROCESSING_FAILED     CONSTANT VARCHAR2(2) := 'PF';  -- Both process attempts failed...

    G_DOCUMENT_READY        CONSTANT VARCHAR2(2) := 'DR';  -- Document (letter) ready for processing...
    --G_DELETE                CONSTANT VARCHAR2(2) := 'XX';





    ----
    -- TO BE REMOVED: get_wll_unsub_rec_by_court_id
    ----
    PROCEDURE get_wll_unsub_rec_by_court_id (
                                              p_unsub_recip_cur IN OUT SYS_REFCURSOR,
                                              p_court_id        IN     NUMBER
                                            )
    IS

    BEGIN

      /*
       * For the 'solicitor_firm_address' values, a comma is only required after a
       * non-NULL value and also not at the end of the concatenation.  Due to the fact
       * that the last non-NULL value may not be the last field selected (a.country),
       * the last comma in that instace needs to be removed.  The RTRIM removes any
       * trailing unwanted commas from the full concatenation. ie. Postcode, becomes
       * Postcode.
       */
      OPEN p_unsub_recip_cur FOR
        SELECT rsf.crest_sof_id,
               rsf.solicitor_firm_name,
               RTRIM(NVL2(a.address_1,a.address_1||',',NULL)
                     ||NVL2(a.address_2,a.address_2||',',NULL)
                     ||NVL2(a.address_3,a.address_3||',',NULL)
                     ||NVL2(a.address_4,a.address_4||',',NULL)
                     ||NVL2(a.town,a.town||',',NULL)
                     ||NVL2(a.county,a.county||',',NULL)
                     ||NVL2(a.postcode,a.postcode||',',NULL)
                     ||NVL(a.country,NULL),',') solictior_firm_address,
               NULL solicitor_firm_fax,
               NULL solicitor_firm_email,
               rsf.court_id, 
               NULL wll_recipient_id
        FROM   xhb_ref_solicitor_firm rsf,
               xhb_address a
        WHERE  rsf.court_id = p_court_id
        AND    rsf.crest_sof_id NOT IN (SELECT NVL(crest_solicitor_firm_id, -1)
                                         FROM   xhb_wll_recipient wll
                                         WHERE  wll.court_id = p_court_id)                  
        AND    rsf.address_id = a.address_id(+)
        AND    rsf.obs_ind = 'N'
        UNION    
        SELECT wr.crest_solicitor_firm_id crest_sof_id,
               wr.solicitor_firm_name,
               wr.solictior_firm_address,
               wr.solicitor_firm_fax,
               wr.solicitor_firm_email,
               wr.court_id, 
               wr.wll_recipient_id
        FROM   xhb_wll_recipient wr
        WHERE  wr.court_id = p_court_id
        AND    NOT EXISTS (SELECT 1
                           FROM   xhb_document_distribution 
                           WHERE  wr.wll_recipient_id = xhb_document_distribution.wll_recipient_id
                           AND    wr.court_id = p_court_id)
        ORDER BY solicitor_firm_name;

    END get_wll_unsub_rec_by_court_id;


    ----
    -- TO BE REMOVED: get_dist_stat_by_court_id
    ----
    PROCEDURE get_dist_stat_by_court_id (
                                          p_dist_stat_cur IN OUT SYS_REFCURSOR,
                                          p_court_id      IN     NUMBER
                                        )
    IS

    BEGIN

      OPEN p_dist_stat_cur FOR
          SELECT dc.DOC_CONTROL_ID,
                 dc.STATUS,
                 dc.EXPIRY_DATE,
                 dc.DISTRIBUTION_TYPE,
                 dc.MIME_TYPE,
                 dc.DOCUMENT_TYPE,
                 dc.LAST_UPDATE_DATE,
                 dc.CREATION_DATE,
                 dc.CREATED_BY,
                 dc.LAST_UPDATED_BY,
                 dc.VERSION,
                 dc.FORMATTING_ID,
                 dc.COURT_ID,
                 dc.DISTRIBUTED_DATE,
                 dc.XML_DOCUMENT_ID,
                 dr.DOCUMENT_RECIPIENT_ID,
                 dr.DOC_RECIPIENT_NAME,
                 dr.DOC_RECIPIENT_FAX,
                 dr.DOC_RECIPIENT_EMAIL
          FROM   XHB_DOCUMENT_CONTROL dc,
                 XHB_DOCUMENT_RECIPIENT dr
          WHERE  dc.STATUS != 'XX'
          AND    dc.STATUS != 'XA'
          AND   (dc.DOCUMENT_TYPE != 'IWP' OR  (dc.DOCUMENT_TYPE = 'IWP'
                                           AND (dc.STATUS != 'SE' AND dc.STATUS != 'SF')))
          AND    dc.COURT_ID = p_court_id
          AND    dc.DOC_CONTROL_ID = dr.DOC_CONTROL_ID
          ORDER BY dc.CREATION_DATE DESC;

    END get_dist_stat_by_court_id;


    ----
    -- TO BE REMOVED: get_wll_dist_stat_by_court_id
    ----
    PROCEDURE get_wll_dist_stat_by_court_id (
                                              p_wll_dist_stat_cur IN OUT SYS_REFCURSOR,
                                              p_court_id          IN     NUMBER
                                            )
    IS

    BEGIN

      OPEN p_wll_dist_stat_cur FOR
          SELECT DISTINCT xd1.XML_DOCUMENT_ID,
                 xd1.DATE_CREATED,
                 xd1.DOCUMENT_TITLE,
                 xd2.STATUS,
                 xd1.EXPIRY_DATE,
                 xd1.DOCUMENT_TYPE,
                 xd1.LAST_UPDATE_DATE,
                 xd1.CREATION_DATE,
                 xd1.CREATED_BY,
                 xd1.LAST_UPDATED_BY,
                 xd1.VERSION,
                 xd1.COURT_ID
          FROM   XHB_XML_DOCUMENT xd1,
                 XHB_WLL_CONTROL wc,
                 XHB_WLL_DOCUMENT wd,
                 XHB_XML_DOCUMENT xd2
          WHERE  xd1.XML_DOCUMENT_ID = wc.XML_DOCUMENT_ID
          AND    wc.WLL_CONTROL_ID = wd.WLL_CONTROL_ID
          AND    wd.XML_DOCUMENT_ID = xd2.XML_DOCUMENT_ID
          AND    xd1.DOCUMENT_TYPE = 'WL'
          AND    (xd2.STATUS != 'XX' AND xd2.STATUS != 'XA')
          AND    xd1.COURT_ID = p_court_id;

    END get_wll_dist_stat_by_court_id;


    ----
    -- get_sub_wll_rec_by_court_id
    ----
    --      Returns all subscribed wll_recipients with their document_distribution data
    ----
    FUNCTION get_sub_wll_rec_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR

            SELECT
               xwr.wll_recipient_id,
               xwr.crest_solicitor_firm_id,
               xwr.solicitor_firm_name,
               xwr.solictior_firm_address,
               xwr.solicitor_firm_fax,
               xwr.solicitor_firm_email,
               xwr.last_update_date AS recipient_last_update_date,
               xwr.creation_date AS recipient_creation_date,
               xwr.created_by AS recipient_created_by,
               xwr.last_updated_by AS recipient_last_updated_by,
               xwr.version AS recipient_version,
               xwr.court_id,
               xwr.recipient_type,
               xdd.doc_distribution_id,
               xdd.distribution_type,
               xdd.document_type AS document_type,
               xdd.mime_type AS mime_type,
               xdd.last_update_date AS distribution_last_update_date,
               xdd.creation_date AS distribution_creation_date,
               xdd.created_by AS distribution_created_by,
               xdd.last_updated_by AS distribution_last_updated_by,
               xdd.version AS distribution_version,
               xdd.recipient_id,
               xdd.wll_recipient_id,
               xdd.use_pref_dist_type
            FROM
               xhb_wll_recipient xwr,
               xhb_document_distribution xdd
            WHERE
               xwr.wll_recipient_id = xdd.wll_recipient_id
            AND
               xwr.court_id = p_court_id;

         RETURN l_return_cursor;

    END get_sub_wll_rec_by_court_id;


    ----
    -- get_unsub_wll_rec_by_court_id
    ----
    --      Returns all unsubscribed wll_recipients 
    ----
    FUNCTION get_unsub_wll_rec_by_court_id(
                                           p_court_id    IN     NUMBER
                                          )
                                          RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR

             SELECT
                xwr.wll_recipient_id,
                xwr.crest_solicitor_firm_id,
                xwr.solicitor_firm_name,
                xwr.solictior_firm_address,
                xwr.solicitor_firm_fax,
                xwr.solicitor_firm_email,
                xwr.last_update_date AS recipient_last_update_date,
                xwr.creation_date AS recipient_creation_date,
                xwr.created_by AS recipient_created_by,
                xwr.last_updated_by AS recipient_last_updated_by,
                xwr.version AS recipient_version,
                xwr.court_id,
                xwr.recipient_type
             FROM
                xhb_wll_recipient xwr
             WHERE
                NOT EXISTS (SELECT
                                1
                            FROM
                                xhb_document_distribution xdd
                            WHERE
                                xwr.wll_recipient_id = xdd.wll_recipient_id)
             AND
                xwr.court_id = p_court_id	   
         UNION ALL
             SELECT
                 NULL AS wll_recipient_id,
                 xrsf.crest_sof_id AS crest_solicitor_firm_id,
                 xrsf.solicitor_firm_name,
                 RTRIM(NVL2(xa.address_1, xa.address_1 || ',', NULL) ||
                       NVL2(xa.address_2, xa.address_2 || ',', NULL) ||
                       NVL2(xa.address_3, xa.address_3 || ',', NULL) ||
                       NVL2(xa.address_4, xa.address_4 || ',', NULL) ||
                       NVL2(xa.town, xa.town || ',', NULL) ||
                       NVL2(xa.county, xa.county || ',', NULL) ||
                       NVL2(xa.postcode, xa.postcode || ',', NULL) ||
                       NVL(xa.country, NULL), ',') AS solictior_firm_address,
                 NULL AS solicitor_firm_fax,
                 NULL AS solicitor_firm_email,
                 NULL AS recipient_last_update_date,
                 NULL AS recipient_creation_date,
                 NULL AS recipient_created_by,
                 NULL AS recipient_last_updated_by,
                 NULL AS recipient_version,
                 xrsf.court_id,
                 'S' AS recipient_type
             FROM
                 xhb_ref_solicitor_firm xrsf,
                 xhb_address xa
             WHERE          
                 NOT EXISTS (SELECT
                                1
                             FROM
                                xhb_wll_recipient xwr
                             WHERE
                                xwr.crest_solicitor_firm_id = xrsf.crest_sof_id
                             AND
                                xwr.court_id = xrsf.court_id
                             AND
                                xwr.recipient_type = 'S')
             AND
                 xrsf.address_id = xa.address_id(+)
             AND
                 (xrsf.obs_ind IS NULL or xrsf.obs_ind = 'N')
             AND
                 xrsf.court_id = p_court_id					
         UNION ALL        
             SELECT
                 NULL AS wll_recipient_id,
                 TO_NUMBER(xrpa.crest_opposer_id)  AS crest_solicitor_firm_id,
                 RTRIM(NVL2(xrpa.title, xrpa.title || ' ', NULL) ||
                       NVL2(xrpa.prosecutor_name_1, xrpa.prosecutor_name_1 || ' ', NULL) ||
                       NVL(xrpa.prosecutor_name_3, NULL), ' ') AS solicitor_firm_name,
                 RTRIM(NVL2(xa.address_1, xa.address_1 || ',', NULL) ||
                       NVL2(xa.address_2, xa.address_2 || ',', NULL) ||
                       NVL2(xa.address_3, xa.address_3 || ',', NULL) ||
                       NVL2(xa.address_4, xa.address_4 || ',', NULL) ||
                       NVL2(xa.town, xa.town || ',', NULL) ||
                       NVL2(xa.county, xa.county || ',', NULL) ||
                       NVL2(xa.postcode, xa.postcode || ',', NULL) ||
                       NVL(xa.country, NULL), ',') AS solictior_firm_address,
                 NULL AS solicitor_firm_fax,
                 NULL AS solicitor_firm_email,
                 NULL AS recipient_last_update_date,
                 NULL AS recipient_creation_date,
                 NULL AS recipient_created_by,
                 NULL AS recipient_last_updated_by,
                 NULL AS recipient_version,
                 xrpa.court_id,
                 'O' AS recipient_type
             FROM
                 xhb_ref_prosecutor_agency xrpa,
                 xhb_address xa
             WHERE          
                 NOT EXISTS (SELECT
                                1
                             FROM
                                xhb_wll_recipient xwr
                             WHERE
                                xwr.crest_solicitor_firm_id = xrpa.crest_opposer_id
                             AND
                                xwr.court_id = xrpa.court_id
                             AND
                                xwr.recipient_type = 'O')
             AND
                 xrpa.address_id = xa.address_id(+)
             AND
                 (xrpa.obs_ind IS NULL or xrpa.obs_ind = 'N')
             AND
                 xrpa.court_id = p_court_id;

         RETURN l_return_cursor;

    END get_unsub_wll_rec_by_court_id;

    ---
    -- get_letter_xml
    ---
    --      Returns clobs containing the letters for the specified list
    ---
    FUNCTION get_letter_xml(
                             p_wll_control_id IN   NUMBER,
                             p_include_post   IN   NUMBER,
                             p_include_email  IN   NUMBER,
                             p_include_fax    IN   NUMBER
                           )
                           RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR
            SELECT 
	            xhb_clob.clob_data
            FROM
	            xhb_clob,
	            xhb_xml_document,
	            xhb_wll_document
            WHERE
	            xhb_clob.clob_id = xhb_xml_document.xml_document_clob_id
            AND
   	         xhb_wll_document.xml_document_id = xhb_xml_document.xml_document_id 
            AND
   	         xhb_wll_document.wll_control_id = p_wll_control_id   
            AND
   	         ((p_include_post <> 0 AND (xhb_xml_document.status = 'PR' OR xhb_xml_document.status = 'DP')) OR 
	             (p_include_email <> 0 AND xhb_xml_document.status = 'DF') OR
   	          (p_include_fax <> 0 AND xhb_xml_document.status = 'DE'));

         RETURN l_return_cursor;

    END get_letter_xml;

    ----
    -- get_wll_control_by_pk
    ----
    --      Returns wll_control and the status of its letters
    ----
    FUNCTION get_wll_control_by_pk(
                                   p_wll_control_id    IN     NUMBER
                                  )
                                  RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT
               xwc.wll_control_id, 
               xwc.status, 
               xwc.expiry_date, 
               xwc.last_update_date, 
               xwc.creation_date, 
               xwc.created_by, 
               xwc.last_updated_by, 
               xwc.version, 
               xwc.xml_document_id,
               xxd.document_type,
               xxd.document_title,
               xxd.language,                       
               xxd.country,
               xxd.major_schema_version,
               xxd.minor_schema_version,
               xxd.date_created,
               ids.letter_ready_count,
               ids.letter_faxed_count,
               ids.letter_emailed_count,
               ids.letter_print_required_count,
               ids.letter_printed_count,
               ids.letter_error_count,
               ids.letter_deleted_count,
               ids.letter_archived_count
            FROM
                 xhb_wll_control xwc,
                 xhb_xml_document xxd,
                 (SELECT
                      xxd1.xml_document_id,
                      SUM(decode(xxd2.status, 'DR', 1, 0)) letter_ready_count,
                      SUM(decode(xxd2.status, 'DF', 1, 0)) letter_faxed_count,
                      SUM(decode(xxd2.status, 'DE', 1, 0)) letter_emailed_count,
                      SUM(decode(xxd2.status, 'PR', 1, 0)) letter_print_required_count,
                      SUM(decode(xxd2.status, 'DP', 1, 0)) letter_printed_count,
                      SUM(decode(xxd2.status, 'PF', 1, 0)) letter_error_count,
                      SUM(decode(xxd2.status, 'XX', 1, 0)) letter_deleted_count,
                      SUM(decode(xxd2.status, 'XA', 1, 0)) letter_archived_count
                  FROM
                     xhb_wll_control xwc,
                     xhb_xml_document xxd1,
                     xhb_wll_document xwd,
                     xhb_xml_document xxd2
                  WHERE
                     xwc.xml_document_id = xxd1.xml_document_id
                  AND
                     xwd.wll_control_id(+) = xwc.wll_control_id
                  AND
                     xxd2.xml_document_id(+) = xwd.xml_document_id
                  AND
                     xwc.wll_control_id = p_wll_control_id
                  GROUP BY
                     xxd1.xml_document_id) ids
            WHERE
               xwc.xml_document_id = xxd.xml_document_id
            AND
               ids.xml_document_id = xxd.xml_document_id;

        RETURN l_return_cursor;

    END get_wll_control_by_pk;

    ----
    -- get_wll_control_by_court_id 
    ----
    --      Returns details of the status of a distribution list and its letters
    ----
    FUNCTION get_wll_control_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT
               xwc.wll_control_id, 
               xwc.status, 
               xwc.expiry_date, 
               xwc.last_update_date, 
               xwc.creation_date, 
               xwc.created_by, 
               xwc.last_updated_by, 
               xwc.version, 
               xwc.xml_document_id,
               xxd.document_type,
               xxd.document_title,
               xxd.language,                       
               xxd.country,
               xxd.major_schema_version,
               xxd.minor_schema_version,              
               xxd.date_created,
               ids.letter_ready_count,
               ids.letter_faxed_count,
               ids.letter_emailed_count,
               ids.letter_print_required_count,
               ids.letter_printed_count,
               ids.letter_error_count,
               ids.letter_deleted_count,
               ids.letter_archived_count
            FROM
                 xhb_wll_control xwc,
                 xhb_xml_document xxd,
                 (SELECT
                      xxd1.xml_document_id,
                      SUM(decode(xxd2.status, 'DR', 1, 0)) letter_ready_count,
                      SUM(decode(xxd2.status, 'DF', 1, 0)) letter_faxed_count,
                      SUM(decode(xxd2.status, 'DE', 1, 0)) letter_emailed_count,
                      SUM(decode(xxd2.status, 'PR', 1, 0)) letter_print_required_count,
                      SUM(decode(xxd2.status, 'DP', 1, 0)) letter_printed_count,
                      SUM(decode(xxd2.status, 'PF', 1, 0)) letter_error_count,
                      SUM(decode(xxd2.status, 'XX', 1, 0)) letter_deleted_count,
                      SUM(decode(xxd2.status, 'XA', 1, 0)) letter_archived_count
                  FROM
                     xhb_wll_control xwc,
                     xhb_xml_document xxd1,
                     xhb_wll_document xwd,
                     xhb_xml_document xxd2
                  WHERE
                     xwc.xml_document_id = xxd1.xml_document_id
                  AND
                     xwd.wll_control_id(+) = xwc.wll_control_id
                  AND
                     xxd2.xml_document_id(+) = xwd.xml_document_id
                  AND
                     xxd1.document_type IN ('DLD', 'FLD', 'WLD')
                  AND
                     xwc.status NOT IN ('XX', 'XA')
                  AND
                     xxd1.court_id = p_court_id
                  GROUP BY
                     xxd1.xml_document_id) ids
            WHERE
               xwc.xml_document_id = xxd.xml_document_id
            AND
               ids.xml_document_id = xxd.xml_document_id;

        RETURN l_return_cursor;

    END get_wll_control_by_court_id;

    ----
    -- get_doc_control_by_court_id
    ----
    --      Returns all the docment_control records with their recipient data
    ----
    FUNCTION get_doc_control_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT 
                xdc.doc_control_id,
                xdc.status,
                xdc.expiry_date,
                xdc.distribution_type,
                xdc.mime_type,
                xdc.document_type,
                xdc.last_update_date,
                xdc.creation_date,
                xdc.created_by,
                xdc.last_updated_by,
                xdc.version,
                xdc.formatting_id,
                xdc.court_id,
                xdc.distributed_date,
                xdc.xml_document_id,
                xdc.formatted_document_blob_id,
                xdr.doc_recipient_name
            FROM
               xhb_document_control xdc,
               xhb_document_recipient xdr
            WHERE  
               xdc.status != 'XX'
            AND    
               xdc.status != 'XA'
            AND
               (xdc.document_type != 'IWP' OR  (xdc.document_type = 'IWP' AND xdc.status != 'SE' AND xdc.status != 'SF'))
            AND    
               xdc.doc_control_id = xdr.doc_control_id
            AND
               xdc.court_id = p_court_id
            AND
               xdc.last_update_date >= SYSDATE - 90;

        RETURN l_return_cursor;

    END get_doc_control_by_court_id;

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- get_next_control_id
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    -- This function is autonomous so as to allow the method to update the
    -- status of the wll control object irrespective of the parent transaction.
    -- The reasoning behind this is that this function MUST return a control
    -- id that requires processing only once, and as it may be called by
    -- several concurrent connections a lock must be acquired.  And as we
    -- want any type of lock to be as short-lived as possible, crossing tiers
    -- would add significant overhead and a wider point of contention...
    --
    -- Also, this function has several hard-coded status'.  This is
    -- required to help performance due to the skewdness of the data in the
    -- affected tables...
    --
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    FUNCTION get_next_control_id RETURN XHB_WLL_CONTROL.wll_control_id%TYPE
    IS

        PRAGMA AUTONOMOUS_TRANSACTION;

        l_wll_control_id XHB_WLL_CONTROL.wll_control_id%TYPE;
        l_new_status     XHB_WLL_CONTROL.status%TYPE;

    BEGIN

        -- NOT TOTALLY CORRECT AS NOT SORTED BY THE ID...
        -- However, previous version did not sort by id either...
        --
        -- If this were to implemented correctly, then AQ would be the implementation
        -- of choice, however, it is believed that we do not have the licences for this.
        -- Therefore, having to do in a single select for update call without ordering
        -- and hoping that this will not cause too many issues.  Inline views cannot be
        -- used, as we need to guarantee that simultaneous calls do not return the same
        -- value...

        BEGIN

            SELECT wll_control_id, G_PROCESSING_DOCUMENT
            INTO   l_wll_control_id, l_new_status
            FROM   XHB_WLL_CONTROL
            WHERE  status = 'DA'
            AND    ROWNUM <= 1
            FOR UPDATE;

        EXCEPTION

            WHEN NO_DATA_FOUND THEN

                BEGIN

                    SELECT wll_control_id, G_PROCESSING_DOCUMENT_2
                    INTO   l_wll_control_id, l_new_status
                    FROM   XHB_WLL_CONTROL
                    WHERE  status = 'PE'         -- Processing error...
                    AND    ROWNUM <= 1
                    FOR UPDATE;

                EXCEPTION

                    WHEN NO_DATA_FOUND THEN

                        -- autonomous transaction, therefore, ensure transaction rolled back...
                        ROLLBACK;
                        RETURN NULL;

                END;

        END;

        UPDATE XHB_WLL_CONTROL
        SET    status = l_new_status
        WHERE  wll_control_id = l_wll_control_id;

        -- autonomous transaction, therefore, ensure transaction committed...
        COMMIT;
        RETURN l_wll_control_id;

    EXCEPTION

        WHEN OTHERS THEN

            -- autonomous transaction, therefore, ensure transaction rolled back...
            ROLLBACK;
            -- re-raise the exception...
            RAISE;

    END get_next_control_id;


    ----
    -- update_control_status
    ----
    --     p_wll_control_id_in - The wll_control_id of the wll_control to update
    --     p_success_in        - 0 represents false, anything else is true
    ----
    PROCEDURE update_control_status (
                                      p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE,
                                      p_success_in        IN NUMBER
                                    )
    IS

        l_old_status XHB_WLL_CONTROL.status%TYPE;
        l_new_status XHB_WLL_CONTROL.status%TYPE := NULL;

    BEGIN

        IF (p_success_in <> 0) THEN

            l_new_status := G_PROCESSING_COMPLETE;

        ELSE

            SELECT status
            INTO   l_old_status
            FROM   XHB_WLL_CONTROL
            WHERE  wll_control_id = p_wll_control_id_in;

            IF (l_old_status = G_PROCESSING_DOCUMENT) THEN

                l_new_status := G_PROCESSING_ERROR;

            ELSIF (l_old_status = G_PROCESSING_DOCUMENT_2) THEN

                l_new_status := G_PROCESSING_FAILED;

            ELSE

                -- Invalid original status... Should potentially throw an exception...
                RETURN;

            END IF;

        END IF;

        UPDATE XHB_WLL_CONTROL
        SET    status = l_new_status
        WHERE  wll_control_id = p_wll_control_id_in;

    END update_control_status;


    ----
    -- get_xml_document
    ----
    --     p_wll_control_id_in - The wll_control_id of the wll_control to get
    --                           the details for
    ----
    FUNCTION get_xml_document (
                                p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE
                              )
                              RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT xwc.wll_control_id,
                   xc.clob_data AS xml_document,
                   xxd.document_type,
                   xxd.court_id,
                   xxd.major_schema_version,
                   xxd.minor_schema_version,
                   xxd.language,
                   xxd.country
            FROM   XHB_XML_DOCUMENT xxd,
                   XHB_WLL_CONTROL xwc,
                   XHB_CLOB xc
            WHERE  xxd.xml_document_id = xwc.xml_document_id
            AND    xwc.wll_control_id = p_wll_control_id_in
            AND    xxd.xml_document_clob_id = xc.clob_id;

        RETURN l_return_cursor;

    END get_xml_document;

    ----
    -- create_list_letter
    ----
    --     p_wll_control_id_in - The wll_control_id of the original list the letter was part of
    --     p_court_id_in       - The court id the letter belongs to
    --     p_document_type_in  - The type of the letter to be inserted
    --     p_document_title_in - The title of the letter to be inserted
    --     p_major_schema_version_in - The major schema version of the letter to be inserted
    --     p_major_schema_version_in - The minor schema version of the letter to be inserted 
    --     p_language_in       - The language of the letter to be inserted 
    --     p_country_in        - The country of the letter to be inserted 
    --     p_recipient_id_in   - The id of the letter recipient
    --     p_recipient_type_in - The type of the recipient
    --
    --     Return the clob that the letter can be written to externally inside of a cursor
    ----
    FUNCTION create_list_letter (
                                  p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE,
                                  p_court_id_in       IN XHB_XML_DOCUMENT.court_id%TYPE,
                                  p_document_type_in  IN XHB_XML_DOCUMENT.document_type%TYPE,
                                  p_document_title_in IN XHB_XML_DOCUMENT.document_title%TYPE,
                                  p_major_schema_version_in IN XHB_XML_DOCUMENT.major_schema_version%TYPE,
                                  p_minor_schema_version_in IN XHB_XML_DOCUMENT.minor_schema_version%TYPE,
                                  p_language_in IN XHB_XML_DOCUMENT.language%TYPE,
                                  p_country_in IN XHB_XML_DOCUMENT.country%TYPE,
                                  p_recipient_id_in   IN XHB_WLL_RECIPIENT.crest_solicitor_firm_id%TYPE,
                                  p_recipient_type_in IN XHB_WLL_RECIPIENT.recipient_type%TYPE
                                )
                                RETURN SYS_REFCURSOR
    IS

        l_clob_id XHB_CLOB.clob_id%TYPE;
        l_return_cursor SYS_REFCURSOR;

        l_xml_document_id  XHB_XML_DOCUMENT.xml_document_id%TYPE;
        l_wll_recipient_id XHB_WLL_DOCUMENT.wll_recipient_id%TYPE;

    BEGIN

        -- Always force the clob to be new...
        INSERT INTO XHB_CLOB
        (clob_id, clob_data)
        VALUES
        (XHB_CLOB_SEQ.NEXTVAL, EMPTY_CLOB())
        RETURNING clob_id
        INTO      l_clob_id;

        --
        -- Insert a new entry into the xhb_xml_document table, and retrieve the
        -- primary key and the clob values that were inserted for use later...
        --
        INSERT INTO XHB_XML_DOCUMENT (
                                       date_created,
                                       document_title,
                                       status,
                                       document_type,
                                       major_schema_version,
                                       minor_schema_version,
                                       language,
                                       country,
                                       court_id,
                                       xml_document_clob_id
                                     )
                              VALUES (
                                       TRUNC(SYSDATE),
                                       p_document_title_in,
                                       G_DOCUMENT_READY,
                                       p_document_type_in,
                                       p_major_schema_version_in,
                                       p_minor_schema_version_in,
                                       p_language_in,
                                       p_country_in,
                                       p_court_id_in,
                                       l_clob_id
                                     )
        RETURNING xml_document_id
        INTO      l_xml_document_id;

        --
        -- If required, lookup the entry in xhb_wll_recipient...
        --
        IF (p_recipient_id_in IS NOT NULL) AND (p_recipient_type_in IS NOT NULL) THEN

            -- Define inside an anonymous PL/SQL block so that we can continue
            -- if no recipient is found
            BEGIN

                SELECT wll_recipient_id
                INTO   l_wll_recipient_id
                FROM   xhb_wll_recipient
                WHERE  recipient_type          = p_recipient_type_in
                AND    court_id                = p_court_id_in
                AND    crest_solicitor_firm_id = p_recipient_id_in;

            EXCEPTION

                WHEN NO_DATA_FOUND THEN
                    -- if not found, then there can be no recipient...
                    NULL;

            END;

        END IF;

        --
        -- Now insert the required entry into xhb_wll_document...
        --
        INSERT INTO XHB_WLL_DOCUMENT (
                                       wll_recipient_id,
                                       wll_control_id,
                                       xml_document_id
                                     )
                              VALUES (
                                       l_wll_recipient_id,
                                       p_wll_control_id_in,
                                       l_xml_document_id
                                     );

        -- Due to an issue with JDBC/Oracle/Weblogic we need to return a
        -- ResultSet instead of the preferable single CLOB value...

        OPEN l_return_cursor FOR
            SELECT clob_data
            FROM   xhb_clob
            WHERE  clob_id = l_clob_id;

        RETURN l_return_cursor;

    END create_list_letter;

    ---
    -- get_blob_data
    ---
    --      Returns blobs containing the internet web page
    ---
    FUNCTION get_blob_data(
                             p_blob_id IN   NUMBER
                           )
                           RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR
            SELECT
	            XHB_BLOB.blob_data
            FROM
	            XHB_BLOB
            WHERE
	            XHB_BLOB.blob_id = p_blob_id;

         RETURN l_return_cursor;

    END get_blob_data;

END xhb_list_distribution_pkg;
/
show errors
