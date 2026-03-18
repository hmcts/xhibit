create or replace PACKAGE BODY xhb_formatting_pkg AS

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- Constant definitions for the document status'
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------

    G_NEW_DOCUMENT        CONSTANT VARCHAR2(2) := 'ND';
    G_FORMATTING_DOCUMENT CONSTANT VARCHAR2(2) := 'FD';  -- Formatting currently in process...
    G_DOCUMENT_READY      CONSTANT VARCHAR2(2) := 'DR';  -- Formatting successful...
    G_NEW_FORMATTING      CONSTANT VARCHAR2(2) := 'NF';  -- Second formatting try, when first one errors (FE)
    G_DELETE              CONSTANT VARCHAR2(2) := 'XX';
    G_FORMATTING_ERROR    CONSTANT VARCHAR2(2) := 'FE';  -- First attempt at formatting failed, may retry later...
    G_FORMATTING_FAILED   CONSTANT VARCHAR2(2) := 'FF';  -- Both formatting attempts  failed


    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- get_next_document_id
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    -- This function is autonomous so as to allow the method to update the
    -- status of the formatting object irrespective of the parent transaction.
    -- The reasoning behind this is that this function MUST return a document
    -- id that requires processing only once, and as it may be called by
    -- several concurrent connections a lock must be acquired.  And as we
    -- want any type of lock to be as short-lived as possible, crossing tiers
    -- would add significant overhead and a wider point of contention...
    --
    -- Also, this function has several hard-coded format_status'.  This is
    -- required to help performance due to the skewdness of the data in the
    -- affected tables...
    --
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    FUNCTION get_next_document_id RETURN XHB_FORMATTING.formatting_id%TYPE
    IS
        PRAGMA AUTONOMOUS_TRANSACTION;

        l_formatting_id     XHB_FORMATTING.formatting_id%TYPE;
        l_new_format_status XHB_FORMATTING.format_status%TYPE;
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

            SELECT formatting_id, 'FD'   -- Formatting document...
            INTO   l_formatting_id, l_new_format_status
            FROM   XHB_FORMATTING
            WHERE  format_status = 'ND'  -- New document...
            AND    is_valid_list_time_delay_YN(xml_document_clob_id) = 'Y' 
            AND    ROWNUM <= 1
            FOR UPDATE;

        EXCEPTION

            WHEN NO_DATA_FOUND THEN

                BEGIN

                    SELECT formatting_id, 'NF'   -- Second formatting attempt...
                    INTO   l_formatting_id, l_new_format_status
                    FROM   XHB_FORMATTING
                    WHERE  format_status = 'FE'  -- Formatting error...
                    AND    ROWNUM <= 1
                    FOR UPDATE;

                EXCEPTION

                    WHEN NO_DATA_FOUND THEN

                        -- autonomous transaction, therefore, ensure transaction finished...
                        ROLLBACK;
                        RETURN NULL;

                END;

        END;

        UPDATE XHB_FORMATTING
        SET    format_status = l_new_format_status
        WHERE  formatting_id = l_formatting_id;

        -- autonomous transaction, therefore, ensure transaction committed...
        COMMIT;
        RETURN l_formatting_id;

    EXCEPTION

        WHEN OTHERS THEN

            -- autonomous transaction, therefore, ensure transaction rolled back...
            ROLLBACK;
            -- re-raise the exception...
            RAISE;

    END get_next_document_id;

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- get_list_time_delay
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    FUNCTION get_list_time_delay(p_court_id IN XHB_COURT.COURT_ID%TYPE) 
    RETURN DATE IS
      l_time_delay DATE;
      CURSOR C_xc IS
      SELECT SYSDATE - (xcp.property_value / (24*60*60)) timedelay 
        FROM xhb_config_prop xcp, xhb_court xc 
       WHERE xcp.property_name = 'FORMATTING_LIST_DELAY'
         AND xc.court_id = p_court_id
         AND NVL(xc.cpp_court,'N') = 'Y';
    BEGIN
      OPEN C_xc;
      FETCH C_xc INTO l_time_delay;
      CLOSE C_xc;
      RETURN l_time_delay;
    END get_list_time_delay;

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- is_valid_list_time_delay_YN
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    FUNCTION is_valid_list_time_delay_YN(p_xml_document_clob_id IN XHB_FORMATTING.XML_DOCUMENT_CLOB_ID%TYPE) RETURN VARCHAR2 IS 
      l_valid_YN VARCHAR2(1);
      CURSOR C_valid IS
      SELECT CASE WHEN xdoc.timedelay IS NULL OR xdoc.creation_date <= xdoc.timedelay THEN 'Y' ELSE 'N' END valid_YN
        FROM (SELECT get_list_time_delay(xd.court_id) timedelay, xd.creation_date
                FROM XHB_XML_DOCUMENT xd
              WHERE xd.xml_document_clob_id = p_xml_document_clob_id
                AND SUBSTR(xd.document_type,1,2) IN ('DL','FL','WL')
             ) xdoc;
    BEGIN
      IF NVL(p_xml_document_clob_id,0) != 0 THEN  
         OPEN C_valid;
         FETCH C_valid INTO l_valid_YN;
         CLOSE C_valid;
      END IF;   
      RETURN NVL(l_valid_YN,'Y');
    END is_valid_list_time_delay_YN;

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- get_document_details
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    -- Acquire all of the document details for the xhb_formatting entry whose
    -- primary key is passed in.  This will guarantee that a BLOB entry is
    -- available for the formatted_document entry.
    --
    -- The returned resultset has been locked as "FOR UPDATE".
    --
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    FUNCTION get_document_details(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE)
    RETURN SYS_REFCURSOR
    IS

        l_blob_id XHB_BLOB.blob_id%TYPE;
        l_return_cursor SYS_REFCURSOR;

    BEGIN

        -- Always force the blob to be new...
       INSERT INTO XHB_BLOB
              (blob_id, blob_data)
       VALUES (XHB_BLOB_SEQ.NEXTVAL, EMPTY_BLOB())
       RETURNING blob_id
       INTO      l_blob_id;

       UPDATE XHB_FORMATTING
       SET    formatted_document_blob_id = l_blob_id
       WHERE  formatting_id = p_formatting_id_in;

        OPEN l_return_cursor FOR
            SELECT xf.distribution_type,
                   xf.mime_type,
                   xf.document_type,
                   xf.major_schema_version,
                   xf.minor_schema_version,
                   xf.language,
                   xf.country,
                   xc.clob_data AS XML_DOCUMENT,
                   xb.blob_data AS FORMATTED_DOCUMENT,
                   xf.court_id,
                   xf.formatting_id,
                   xf.XML_DOCUMENT_CLOB_ID
            FROM   XHB_FORMATTING xf,
                   XHB_CLOB xc,
                   XHB_BLOB xb
            WHERE  xf.formatted_document_blob_id = xb.blob_id
            AND    xf.xml_document_clob_id = xc.clob_id(+)
            AND    xf.formatting_id = p_formatting_id_in
        FOR UPDATE;

        RETURN l_return_cursor;

    END get_document_details;


    PROCEDURE update_document_status(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE,
                                     p_success_in       IN BOOLEAN)
    IS

        l_old_format_status XHB_FORMATTING.format_status%TYPE;
        l_new_format_status XHB_FORMATTING.format_status%TYPE := NULL;

    BEGIN

        IF (p_success_in) THEN

            l_new_format_status := G_DOCUMENT_READY;

        ELSE

            SELECT format_status
            INTO   l_old_format_status
            FROM   XHB_FORMATTING
            WHERE  formatting_id = p_formatting_id_in;

            IF (l_old_format_status = G_FORMATTING_DOCUMENT) THEN

                l_new_format_status := G_FORMATTING_ERROR;

            ELSIF (l_old_format_status = G_NEW_FORMATTING) THEN

                l_new_format_status := G_FORMATTING_FAILED;

            ELSE

                DBMS_OUTPUT.put_line('Original status was ' || l_old_format_status);
                -- Invalid original status... Should potentially throw an exception...
                RETURN;

            END IF;

        END IF;

        UPDATE XHB_FORMATTING
        SET    format_status = l_new_format_status
        WHERE  formatting_id = p_formatting_id_in;

    END update_document_status;

    PROCEDURE update_cpp_formatting(p_cpp_formatting_id IN XHB_CPP_FORMATTING.cpp_formatting_id%TYPE,
                                    p_error_message IN VARCHAR2)
    IS
    BEGIN

    UPDATE XHB_CPP_FORMATTING
    SET   ERROR_MESSAGE = p_error_message,
          FORMAT_STATUS = 'MF'
    WHERE CPP_FORMATTING_ID = p_cpp_formatting_id;

    END update_cpp_formatting;
	
	PROCEDURE update_cpp_formatting_status(p_cpp_formatting_id IN XHB_CPP_FORMATTING.cpp_formatting_id%TYPE,
                                    p_status IN XHB_CPP_FORMATTING.FORMAT_STATUS%TYPE)
    IS
    BEGIN

    UPDATE XHB_CPP_FORMATTING
    SET   FORMAT_STATUS = p_status
    WHERE CPP_FORMATTING_ID = p_cpp_formatting_id;

    END update_cpp_formatting_status;


    -- 0 represents false, anything else is true...
    PROCEDURE update_document_status(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE,
                                     p_success_in       IN NUMBER)
    IS
    BEGIN

        update_document_status(p_formatting_id_in, (p_success_in <> 0));

    END update_document_status;

    FUNCTION parse_XML_Char(p_char IN CHAR, p_replacement_char IN CHAR DEFAULT NULL)
       RETURN CHAR IS
    BEGIN
       RETURN CASE WHEN ASCII(p_char) BETWEEN 0 AND 8 OR
                        ASCII(p_char) BETWEEN 11 AND 12 OR
                        ASCII(p_char) BETWEEN 14 AND 31
                   THEN p_replacement_char ELSE p_char END;
    END parse_XML_Char;

    FUNCTION parse_XML_string(p_string IN VARCHAR2, p_replacement_char IN CHAR DEFAULT NULL)
       RETURN VARCHAR2 IS
       v_result VARCHAR2(32767);
       v_char   VARCHAR2(5); -- Unprintable chars can be more than 1 char
    BEGIN
       IF p_string IS NOT NULL THEN
          FOR chrNo IN 1..LENGTH(p_string) LOOP
              v_char := parse_XML_Char(p_char => SUBSTR(p_string,chrNo,1),p_replacement_char => p_replacement_char);
              IF v_char IS NOT NULL THEN
                   v_result := v_result || v_char;
              END IF;
          END LOOP;
       END IF;
       RETURN v_result;
    END parse_XML_string;

    FUNCTION wordwrapping_position(p_text IN VARCHAR2, p_field_length IN NUMBER) RETURN NUMBER IS
      v_last_space_chr_no NUMBER;
    BEGIN
      v_last_space_chr_no := INSTR(p_text,' ',-1);
      IF v_last_space_chr_no > 0 AND -- Contains a space, so we can wrap AND
         LENGTH(p_text) = p_field_length AND  -- Max Length reached AND
         SUBSTR(p_text,p_field_length,1) != ' ' THEN -- Last Chars not a space
         RETURN v_last_space_chr_no;
      ELSE
         RETURN 0;
      END IF;
    END wordwrapping_position;

    FUNCTION get_wordwrapped_string(p_text IN VARCHAR2, p_field_length IN NUMBER) RETURN VARCHAR2 IS
    BEGIN
      IF wordwrapping_position(p_text,p_field_length) > 0 THEN -- Last Chars not a space
         RETURN SUBSTR(p_text,1,wordwrapping_position(p_text,p_field_length));
      END IF;
      RETURN p_text;
    END get_wordwrapped_string;
    
    FUNCTION get_latest_xhibit_clob_Id(p_court_id IN XHB_FORMATTING.court_id%TYPE, p_document_type IN XHB_FORMATTING.DOCUMENT_TYPE%TYPE, 
    p_language IN XHB_FORMATTING.LANGUAGE%TYPE,
    p_court_site_name IN VARCHAR2) RETURN NUMBER IS
    
    v_formatting_row Xhb_Formatting%ROWTYPE;
    v_merge_row xhb_cpp_formatting_merge%ROWTYPE;

BEGIN

Select *
INTO v_formatting_row
from(select  o.* from Xhb_Formatting  o , Xhb_clob c
    where  o.court_Id =p_court_id
      and o.document_Type = p_document_type and o.FORMAT_STATUS!='ND' and o.language =p_language 
      and o.XML_DOCUMENT_CLOB_ID!=0 
      and o.date_in >= trunc(sysdate)-14
      and o.xml_document_clob_id=c.clob_id
      and c.clob_data like concat('%<courtsitename>',concat(p_court_site_name,'%')) order by o.date_in desc) where rownum=1;
   
  Select *
  into v_merge_row 
  from (select o2.* from xhb_cpp_formatting_merge o2
  where o2.formatting_id=v_formatting_row.formatting_id
  and (o2.obs_ind is null or o2.obs_ind = 'N')  order by o2.cpp_formatting_merge_id desc) 
  where rownum=1;
  
 
  IF v_merge_row.cpp_formatting_merge_id is not null THEN RETURN v_merge_row.xhibit_clob_id;
  ELSE RETURN v_formatting_row.XML_DOCUMENT_CLOB_ID;
  END IF;
   EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RETURN v_formatting_row.XML_DOCUMENT_CLOB_ID;
    END get_latest_xhibit_clob_Id;
    
END xhb_formatting_pkg;
/
show errors
