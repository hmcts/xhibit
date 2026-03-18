DECLARE
    v_new_desc xhb_ref_offence.offence_desc%TYPE;
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
BEGIN
    -- Loop through the records with non-standard chars in them
    FOR rec IN (SELECT xro.ref_offence_id,
                       xro.offence_desc
                  FROM xhb_ref_offence xro
                 WHERE TRANSLATE(UPPER(xro.offence_desc),
                         '~ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789[](),#/-.:;" ','~') IS NOT NULL
                ) LOOP
        -- Get the new description minus any unprintable chars
        v_new_desc := parse_XML_string(p_string=>rec.offence_desc);
        -- If there is any change in length then update the desc
        IF LENGTH(RTRIM(rec.offence_desc)) != LENGTH(RTRIM(v_new_desc)) THEN
           UPDATE xhb_ref_offence upd
              SET upd.offence_desc = v_new_desc
            WHERE upd.ref_offence_id = rec.ref_offence_id;
            COMMIT;
        END IF;
    END LOOP;
END;
/