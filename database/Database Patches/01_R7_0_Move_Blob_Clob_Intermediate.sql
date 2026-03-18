/* Script to copy exisiting data into the new tables for blob_clob movement */
/* Run post script to create tablespaces, tables and modify columns         */
/* Author:                                                                  */
/* Date:                                                                    */
/* Modified By: Andy Greene                                                 */
/* 10 Jan 2005                                                              */
/*                                                                          */

SPOOL blob_clob_move_2.txt;

-- Set up SQL*Plus to show the output for later, and include timings for curiosity...

SET SERVEROUTPUT ON SIZE 1000000;
SET TIMING ON;
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                         Actually move the data                            --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

ALTER TRIGGER XHB_FORMATTING_AUR_TR DISABLE;

-- Each area is in its own anonymous PL/SQL block, this is to ensure that a
-- whole move process is completed as a single transaction...


   declare update_formatting_count integer;
   commit_count integer := 1001;
   

-- Disable one of the after update row triggers on xhb_formatting, this trigger
-- inserts into another table all rows that had problems (a business process),
-- we do not want to do this as we are only updating...


BEGIN

    DBMS_OUTPUT.put_line('Updating xhb_formatting - the clobs...');


  SELECT COUNT(*)  
           INTO update_formatting_count 
    FROM 
           XHB_FORMATTING 
    WHERE 
           xml_document_clob_id IS NULL;

    WHILE (update_formatting_count > 0) LOOP

           UPDATE XHB_FORMATTING
                  SET    xml_document_clob_id = XHB_CLOB_SEQ.NEXTVAL
           WHERE  xml_document_clob_id IS NULL
           AND RoWNUM < commit_count;

           DBMS_OUTPUT.put_line('Updated ' || SQL%ROWCOUNT || ' xhb_formatting entries...');

    -- Only insert rows if we updated anything...
           IF (SQL%ROWCOUNT > 0) THEN

               INSERT INTO XHB_CLOB (clob_id, clob_data)
                      SELECT xml_document_clob_id, xml_document
               FROM   
                      XHB_FORMATTING
               WHERE  xml_document_clob_id IS NOT NULL
                      AND NOT EXISTS (SELECT 1
                                      FROM   XHB_CLOB
                                      WHERE  clob_id = xml_document_clob_id);

            DBMS_OUTPUT.put_line('Inserted ' || SQL%ROWCOUNT || ' xhb_formatting entries into xhb_clob...');

            END IF;

            update_formatting_count := update_formatting_count - SQL%ROWCOUNT;

            COMMIT;

    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
show err



declare update_formatting_count integer;
        commit_count integer := 1001;

BEGIN
    DBMS_OUTPUT.put_line('Updating xhb_formatting - the blobs...');

    SELECT COUNT(*) INTO update_formatting_count 
           FROM XHB_FORMATTING
    WHERE  formatted_document IS NOT NULL
           AND    formatted_document_blob_id IS NULL;

    WHILE (update_formatting_count > 0) LOOP

          UPDATE XHB_FORMATTING
          SET    formatted_document_blob_id = XHB_BLOB_SEQ.NEXTVAL
          WHERE  formatted_document IS NOT NULL
          AND    formatted_document_blob_id IS NULL
          AND    ROWNUM < commit_count;

          DBMS_OUTPUT.put_line('Updated ' || SQL%ROWCOUNT || ' xhb_formatting entries...');

          -- Only insert rows if we updated anything...
          IF (SQL%ROWCOUNT > 0) THEN

             INSERT INTO XHB_BLOB (blob_id, blob_data)
             SELECT formatted_document_blob_id, formatted_document
             FROM   XHB_FORMATTING
             WHERE  formatted_document_blob_id IS NOT NULL
             AND NOT EXISTS (SELECT 1
                             FROM   XHB_BLOB
                             WHERE  blob_id = formatted_document_blob_id);

             DBMS_OUTPUT.put_line('Inserted ' || SQL%ROWCOUNT || ' xhb_formatting entries into xhb_blob...');

           END IF;

           update_formatting_count := update_formatting_count - SQL%ROWCOUNT;

           COMMIT;

     END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
show err

-- Re-enable the after update row trigger...
ALTER TRIGGER XHB_FORMATTING_AUR_TR ENABLE;





declare update_formatting_count integer;
        commit_count integer := 1001;


-- Move the existing xhb_email data...
BEGIN
    DBMS_OUTPUT.put_line('Updating xhb_email...');

    SELECT COUNT(*) INTO update_formatting_count FROM XHB_EMAIL
    WHERE  mime_body_blob_id IS NULL;

    WHILE (update_formatting_count > 0) LOOP

         UPDATE XHB_EMAIL
         SET    mime_body_blob_id = XHB_BLOB_SEQ.NEXTVAL
         WHERE  mime_body_blob_id IS NULL
         AND ROWNUM < commit_count;

         DBMS_OUTPUT.put_line('Updated ' || SQL%ROWCOUNT || ' xhb_email entries...');

         -- Only insert rows if we updated anything...
         IF (SQL%ROWCOUNT > 0) THEN

            INSERT INTO XHB_BLOB (blob_id, blob_data)
            SELECT mime_body_blob_id, mime_body
            FROM   XHB_EMAIL
            WHERE  mime_body_blob_id IS NOT NULL
            AND NOT EXISTS (SELECT 1
                        FROM   xhb_blob
                        WHERE  blob_id = mime_body_blob_id);

            DBMS_OUTPUT.put_line('Inserted ' || SQL%ROWCOUNT || ' xhb_email entries into xhb_blob...');

          END IF;

          update_formatting_count := update_formatting_count - SQL%ROWCOUNT;

          COMMIT;

    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
show err





-- Move the existing xhb_internet_html data...
BEGIN
    -- This table is tiny in production (5 rows for one court), as each row is
    -- always updated, instead of new rows added, therefore, always update...

    DBMS_OUTPUT.put_line('Updating xhb_internet_html...');

    UPDATE XHB_INTERNET_HTML
    SET    html_clob_id = NVL2(html, XHB_CLOB_SEQ.NEXTVAL, NULL);

    DBMS_OUTPUT.put_line('Updated ' || SQL%ROWCOUNT || ' xhb_internet_html entries...');

    -- Only insert rows if we updated anything...
    IF (SQL%ROWCOUNT > 0) THEN

        INSERT INTO XHB_CLOB (clob_id, clob_data)
        SELECT html_clob_id, html
        FROM   XHB_INTERNET_HTML
        WHERE  html_clob_id IS NOT NULL
        AND NOT EXISTS (SELECT 1
                        FROM   XHB_CLOB
                        WHERE  clob_id = html_clob_id);

        DBMS_OUTPUT.put_line('Inserted ' || SQL%ROWCOUNT || ' xhb_internet_html entries into xhb_clob...');

    END IF;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
show err





-- Move the existing xhb_xml_document data...

declare update_formatting_count integer;
        commit_count integer := 1001;

BEGIN
    DBMS_OUTPUT.put_line('Updating xhb_xml_document...');


    SELECT COUNT(*) INTO update_formatting_count FROM XHB_XML_DOCUMENT
    WHERE  xml_document_clob_id IS NULL;
    
    WHILE (update_formatting_count > 0) LOOP

       UPDATE XHB_XML_DOCUMENT
       SET    xml_document_clob_id = XHB_CLOB_SEQ.NEXTVAL
       WHERE  xml_document_clob_id IS NULL
       AND ROWNUM < commit_count;

       DBMS_OUTPUT.put_line('Updated ' || SQL%ROWCOUNT || ' xhb_xml_document entries...');

        -- Only insert rows if we updated anything...
       IF (SQL%ROWCOUNT > 0) THEN

          INSERT INTO XHB_CLOB (clob_id, clob_data)
          SELECT xml_document_clob_id, xml_document
          FROM   XHB_XML_DOCUMENT
          WHERE  xml_document_clob_id IS NOT NULL
          AND NOT EXISTS (SELECT 1
                        FROM   XHB_CLOB
                        WHERE  clob_id = xml_document_clob_id);

          DBMS_OUTPUT.put_line('Inserted ' || SQL%ROWCOUNT || ' xhb_xml_document entries into xhb_clob...');

       END IF;

       update_formatting_count := update_formatting_count - SQL%ROWCOUNT;

       COMMIT;


    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
show err



declare update_formatting_count integer;
        commit_count integer := 1001;

-- Move the existing xhb_document_control data...
BEGIN
    DBMS_OUTPUT.put_line('Updating xhb_xml_document_control...');

    SELECT COUNT(*) INTO update_formatting_count FROM XHB_DOCUMENT_CONTROL
    WHERE  formatted_document IS NOT NULL
    AND    formatted_document_blob_id IS NULL;

    WHILE (update_formatting_count > 0) LOOP

       UPDATE XHB_DOCUMENT_CONTROL
       SET    formatted_document_blob_id = XHB_BLOB_SEQ.NEXTVAL
       WHERE  formatted_document IS NOT NULL
       AND    formatted_document_blob_id IS NULL
       AND ROWNUM < commit_count;

       DBMS_OUTPUT.put_line('Updated ' || SQL%ROWCOUNT || ' xhb_xml_document_control entries...');

       -- Only insert rows if we updated anything...
       IF (SQL%ROWCOUNT > 0) THEN

          INSERT INTO XHB_BLOB (blob_id, blob_data)
          SELECT formatted_document_blob_id, formatted_document
          FROM   XHB_DOCUMENT_CONTROL
          WHERE  formatted_document_blob_id IS NOT NULL
          AND    NOT EXISTS (SELECT 1
                           FROM   XHB_BLOB
                           WHERE  blob_id = formatted_document_blob_id);

          DBMS_OUTPUT.put_line('Inserted ' || SQL%ROWCOUNT || ' xhb_xml_document_control entries into xhb_blob...');

       END IF;

       update_formatting_count := update_formatting_count - SQL%ROWCOUNT;

      COMMIT;

    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
show err





-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                             Turn spooling off                             --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

SPOOL off;