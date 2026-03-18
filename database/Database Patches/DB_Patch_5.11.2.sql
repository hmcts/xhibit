/*
 * Patch for DB Release 5.11.2 (System Test & Development)
 *
 * 13th April 2004
 */

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */


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


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

CREATE OR REPLACE PACKAGE BODY xhb_list_distribution_pkg AS

  PROCEDURE get_wll_unsub_rec_by_court_id (p_unsub_recip_cur IN OUT SYS_REFCURSOR,
                                           p_court_id        IN     NUMBER) IS

    BEGIN

      OPEN p_unsub_recip_cur FOR

      /*
       * For the 'solicitor_firm_address' values, a comma is only required after a
       * non-NULL value and also not at the end of the concatenation.  Due to the fact
       * that the last non-NULL value may not be the last field selected (a.country),
       * the last comma in that instace needs to be removed.  The RTRIM removes any
       * trailing unwanted commas from the full concatenation. ie. Postcode, becomes
       * Postcode.
       */

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
                                         FROM   xhb_wll_recipient)
        AND    rsf.address_id = a.address_id(+)
		AND	   rsf.obs_ind = 'N'
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
                           WHERE  wr.wll_recipient_id = xhb_document_distribution.wll_recipient_id)
        ORDER BY solicitor_firm_name;

  END get_wll_unsub_rec_by_court_id;

  PROCEDURE  get_dist_stat_by_court_id (p_dist_stat_cur IN OUT SYS_REFCURSOR,
                                        p_court_id      IN     NUMBER) IS

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

  PROCEDURE  get_wll_dist_stat_by_court_id (p_wll_dist_stat_cur IN OUT SYS_REFCURSOR,
                                            p_court_id          IN     NUMBER) IS

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

END xhb_list_distribution_pkg;
/
show errors


/*
 * Changes, additions or deletion of standing data
 */


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = '5.11.2', last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
