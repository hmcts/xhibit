CREATE OR REPLACE PACKAGE BODY dar_message_pkg AS

  INVALID_RANGE EXCEPTION;
  PRAGMA EXCEPTION_INIT(INVALID_RANGE, -20110);
  INVALID_HOURS EXCEPTION;
  PRAGMA EXCEPTION_INIT(INVALID_HOURS, -20111);
  INVALID_BATCH EXCEPTION;
  PRAGMA EXCEPTION_INIT(INVALID_BATCH, -20112);

/*
 * get_darts_property
 */
  FUNCTION get_darts_property(p_property_name IN DAR_DARTS_CONFIG.DARTS_PROPERTY_NAME%TYPE) 
     RETURN DAR_DARTS_CONFIG.DARTS_PROPERTY_VALUE%TYPE IS
     v_property DAR_DARTS_CONFIG.DARTS_PROPERTY_VALUE%TYPE; 
     CURSOR c_config IS 
     SELECT ddc.darts_property_value
       FROM DAR_DARTS_CONFIG ddc
      WHERE ddc.darts_property_name = p_property_name;
  BEGIN
     OPEN c_config;
     FETCH c_config INTO v_property;
     CLOSE c_config;
     RETURN v_property;
  END get_darts_property;

/*
 * is_numeric
 */
 FUNCTION is_numeric_YN(p_string IN VARCHAR2)
    RETURN VARCHAR2 IS
    v_dummy   NUMBER;
    v_numeric VARCHAR2(1) := 'N';
 BEGIN
    v_dummy := TO_NUMBER(p_string);
    RETURN 'Y';
 EXCEPTION 
      WHEN OTHERS THEN RETURN 'N';
 END is_numeric_YN;

/*
 *
 * get_message_payload
 *
 */
     PROCEDURE get_message_payload( clob_data_in     OUT SYS_REFCURSOR,
                                    message_id_in    IN DAR_MESSAGE_STORE.MESSAGE_ID%TYPE) IS
     BEGIN

       OPEN clob_data_in  FOR
       SELECT PAYLOAD
       FROM  DAR_MESSAGE_STORE
       WHERE MESSAGE_ID = message_id_in;

     END get_message_payload;

/*
 *
 * insert_darts_message_store
 *
 */
    FUNCTION insert_darts_message_store( xhibit_code_in  IN DAR_MESSAGE_STORE.XHIBIT_MESSAGE_CODE%TYPE,
                                         exiss_code_in   IN DAR_MESSAGE_STORE.EXISS_MESSAGE_CODE%TYPE,
                                         payload_in      IN DAR_MESSAGE_STORE.PAYLOAD%TYPE)
                                         RETURN DAR_MESSAGE_STORE.MESSAGE_ID%TYPE
         IS
             message_id_out      DAR_MESSAGE_STORE.MESSAGE_ID%TYPE;

         BEGIN
            INSERT INTO DAR_MESSAGE_STORE
               (XHIBIT_MESSAGE_CODE, EXISS_MESSAGE_CODE, PAYLOAD, STATUS_CODE  )
                 VALUES
                     (xhibit_code_in, exiss_code_in, payload_in, 'N')
                 RETURNING MESSAGE_ID
                 INTO      message_id_out;
                 RETURN message_id_out;
          END insert_darts_message_store;

/*
 *
 * success_darts_msg_stor
 *
 */
 FUNCTION success_darts_msg_store( success_ids  IN DARTS_MESSAGE_ID_ARRAY)
  RETURN NUMBER
  IS
  BEGIN
    FOR i in success_ids.FIRST .. success_ids.LAST
    LOOP
        UPDATE DAR_MESSAGE_STORE set STATUS_CODE = g_msg_code_success
        where MESSAGE_ID = success_ids(i);
    END LOOP;

    RETURN 0;
  END success_darts_msg_store;

/*
 *
 * fail_darts_msg_store
 *
 */
 FUNCTION fail_darts_msg_store( failures_ids       IN DARTS_MESSAGE_ID_ARRAY,
                                failure_reasons    IN DARTS_MESSAGE_FAIL_ARRAY)
   RETURN NUMBER
  IS
  BEGIN
    FOR i in  failures_ids.FIRST .. failures_ids.LAST
    LOOP
        UPDATE DAR_MESSAGE_STORE SET STATUS_CODE = g_msg_code_fail,
                                     STATUS_DETAIL = failure_reasons(i)
                               WHERE MESSAGE_ID = failures_ids(i);
    END LOOP;
    RETURN 0;
  END fail_darts_msg_store;


/* 
 *
 * Split a string using the delimiter provided 
 *
 */
  FUNCTION split(p_list varchar2,
     p_del varchar2)
  RETURN split_tbl pipelined
  is
  l_idx pls_integer;
  l_list varchar2(23767) := p_list;  
  begin
  loop
    l_idx := instr(l_list, p_del);
    if l_idx > 0 then
      pipe row(substr(l_list,1,l_idx-1));
      l_list := substr(l_list, l_idx+length(p_del));
    else
      pipe row(l_list);
      exit;
    end if;
  end loop;
  return;
  end split;

/*
 * 
 * Function to establish whether message is high priority (1) or not (0)
 *
 */  
  FUNCTION IS_PRIORITY_MESSAGE(p_code VARCHAR2)
  RETURN NUMBER
  is
    v_priority_messages varchar2(32767);
    v_count_priority NUMBER;
  BEGIN
    SELECT DARTS_PROPERTY_VALUE INTO v_priority_messages FROM DAR_DARTS_CONFIG WHERE DARTS_PROPERTY_NAME='darts.priorityMessages';
    SELECT COUNT(*) INTO v_count_priority FROM table(DAR_MESSAGE_PKG.split(v_priority_messages,',')) where COLUMN_VALUE = p_code;
    RETURN v_count_priority;  
  END IS_PRIORITY_MESSAGE;


/*
 * Get the minimum and the maximum from a passed in string.
 */
  PROCEDURE get_range_from_string(p_string          IN  VARCHAR2, 
                                  po_min            OUT VARCHAR2,
                                  po_max            OUT VARCHAR2,
                                  p_range_delimiter IN  VARCHAR2 DEFAULT '-',
                                  p_numeric_YN      IN  VARCHAR2 DEFAULT 'Y') IS
     v_range_pos NUMBER;
  BEGIN
     v_range_pos := INSTR(p_string, p_range_delimiter);
     IF v_range_pos = 0 THEN
        dbms_output.put_line('RANGE DELIMITER: '||p_range_delimiter);
        dbms_output.put_line('INVALID RANGE: '||p_string);
        RAISE INVALID_RANGE;
     END IF;
     po_min := LTRIM(RTRIM(SUBSTR(p_string, 1, v_range_pos-length(p_range_delimiter))));
     dbms_output.put_line('Min = '||po_min);
     po_max := LTRIM(RTRIM(SUBSTR(p_string, v_range_pos+length(p_range_delimiter))));
     dbms_output.put_line('Max = '||po_max);
     IF po_min IS NOT NULL AND is_numeric_YN(po_min) = 'N' THEN 
        DBMS_OUTPUT.PUT_LINE('INVALID NUMBER: Min='||po_min);
        RAISE INVALID_RANGE;
     ELSIF po_max IS NOT NULL AND is_numeric_YN(po_max) = 'N' THEN 
        DBMS_OUTPUT.PUT_LINE('INVALID NUMBER: Max='||po_max);
        RAISE INVALID_RANGE;  
     ELSIF po_min IS NOT NULL AND po_max IS NOT NULL AND po_max < po_min THEN
        -- Max cannot be less than min
        dbms_output.put_line('INVALID RANGE: '||po_max||' < '||po_min);
        RAISE INVALID_RANGE;
     END IF;
  END get_range_from_string;

/*
 * Is the runnable period valid
 */
  FUNCTION is_runnable_period_YN(p_datetime IN DATE DEFAULT SYSDATE) 
     RETURN VARCHAR2 IS
     MAX_LENGTH       CONSTANT NUMBER := 2;
     v_runnable_YN    VARCHAR2(1) := 'Y';
     v_dontrun_period DAR_DARTS_CONFIG.DARTS_PROPERTY_VALUE%TYPE;
     v_min            DAR_DARTS_CONFIG.DARTS_PROPERTY_VALUE%TYPE;
     v_max            DAR_DARTS_CONFIG.DARTS_PROPERTY_VALUE%TYPE;
  BEGIN
     -- Get the config
     v_dontrun_period := get_darts_property(p_property_name => 'darts.resend.dontrun_period');
     IF v_dontrun_period IS NOT NULL THEN 
        -- Get the ranges (invalid ranges will get an exception thrown)
        get_range_from_string(p_string => v_dontrun_period, po_min => v_min, po_max => v_max);
        
        -- Validate the range parameters
        IF v_min IS NULL OR LENGTH(v_min)>MAX_LENGTH THEN
           DBMS_OUTPUT.PUT_LINE('INVALID LENGTH: Min='||v_min);
           RAISE INVALID_RANGE;
        ELSIF v_max IS NULL OR LENGTH(v_max)>MAX_LENGTH THEN
           DBMS_OUTPUT.PUT_LINE('INVALID LENGTH: Max='||v_max);
           RAISE INVALID_RANGE; 
        END IF;
        
        -- Check if this is NOT a runnable hour
        IF TO_NUMBER(TO_CHAR(p_datetime,'HH24')) BETWEEN TO_NUMBER(v_min) AND TO_NUMBER(v_max) THEN 
           v_runnable_YN := 'N';
        END IF;
        
     END IF;
    
     RETURN v_runnable_YN;
  END is_runnable_period_YN;
  
/*
 * Get the failed messages as a pipelined table
 */
  FUNCTION get_resend_cutoff_limit(p_datetime IN DATE DEFAULT SYSDATE)
     RETURN DATE IS
     v_cutoff_limit DAR_DARTS_CONFIG.DARTS_PROPERTY_VALUE%TYPE;
  BEGIN
     v_cutoff_limit := get_darts_property(p_property_name => 'darts.resend.history_cutoff_limit_hrs');
     IF v_cutoff_limit IS NOT NULL THEN
        IF is_numeric_YN(v_cutoff_limit) = 'N' THEN 
           RAISE INVALID_HOURS;
        END IF;
        RETURN p_datetime - (TO_NUMBER(v_cutoff_limit)/24);
     END IF;
     RETURN NULL;
  END get_resend_cutoff_limit;

/*
 * Get the failed messages as a pipelined table
 */
  FUNCTION get_failed_messages(p_datetime IN DATE DEFAULT SYSDATE)
     RETURN failed_messages_tbl PIPELINED IS
     v_last_update_limit   DATE;
     v_creation_date_limit DATE;
  BEGIN
     -- Last updated prior to 1 hour ago
     v_last_update_limit := p_datetime - (1/24);
     -- Get the cutoff limit
     v_creation_date_limit := get_resend_cutoff_limit(p_datetime => p_datetime);
     -- Get the records
     FOR rec IN (SELECT d.* 
                   FROM dar_message_store d
                  WHERE d.status_code NOT IN ('S','FR')
                    AND d.last_update_date <= v_last_update_limit
                    AND (d.creation_date >= v_creation_date_limit OR v_creation_date_limit IS NULL)
                    AND NOT (d.status_code = 'F' AND d.status_detail LIKE '404 : Courthouse Not Found%') 
                    AND NOT (d.status_code = 'F' AND d.status_detail LIKE '404 : Handler Not Found%')
                ORDER BY d.message_id DESC
        ) LOOP
        PIPE ROW (rec);
     END LOOP;
  END get_failed_messages;

/*
 * Resend the failed message to darts
 */
  PROCEDURE resend_failed_message(p_rec IN DAR_MESSAGE_STORE%ROWTYPE) IS 
  BEGIN
     dbms_output.put_line('Resending message_id='||p_rec.message_id);
  
     -- Update the dar_message_store record
     UPDATE DAR_MESSAGE_STORE d
        SET d.status_code = 'N', d.status_detail = 'DARTS Resend'
      WHERE d.message_id = p_rec.message_id;
      
     -- Insert the record back into DARTS_NEW_MSG
     INSERT INTO DAR_NEW_MESSAGES
     (MESSAGE_ID,
      XHIBIT_MESSAGE_CODE,
      EXISS_MESSAGE_CODE,
      PAYLOAD,
      RETRY_COUNT,
      NEXT_RETRY_TIME,
      CREATION_DATE,
      LAST_UPDATE_DATE)
     VALUES 
     (p_rec.MESSAGE_ID,
      p_rec.XHIBIT_MESSAGE_CODE,
      p_rec.EXISS_MESSAGE_CODE,
      p_rec.PAYLOAD,
      0,
      SYSDATE,
      p_rec.CREATION_DATE,
      SYSDATE);
  EXCEPTION 
       WHEN OTHERS THEN 
            dbms_output.put_line('ERROR: '||SQLERRM);
            RAISE;
  END resend_failed_message;


/*
 * Loop through the failed messages and resend them to darts
 */
  PROCEDURE resend_failed_messages IS 
     v_count       NUMBER := 0;
     v_batch_limit DAR_DARTS_CONFIG.DARTS_PROPERTY_VALUE%TYPE;
  BEGIN
     IF is_runnable_period_YN = 'N' THEN 
        dbms_output.put_line('Not a runable period');
        RETURN;
     END IF;   
     dbms_output.put_line('Resending messages');
     -- Get the config
     v_batch_limit := get_darts_property(p_property_name => 'darts.resend.max.message_batch');
     IF v_batch_limit IS NOT NULL THEN 
        dbms_output.put_line('Batch Limit = '||v_batch_limit);
        IF is_numeric_YN(v_batch_limit) = 'N' THEN
           RAISE INVALID_BATCH;
        END IF;   
     END IF;
     -- Loop through the failed messages
     FOR rec IN (SELECT d.* FROM TABLE(get_failed_messages) d) LOOP
         BEGIN
            -- Check the batch limit hasn't been exceeded
            EXIT WHEN v_batch_limit IS NOT NULL AND TO_NUMBER(v_batch_limit) <= v_count;
            -- Increment the counter
            v_count := v_count + 1;
            -- Resend the message
            resend_failed_message(p_rec => rec);
            -- Commit the record
            COMMIT;
         EXCEPTION 
              WHEN OTHERS THEN 
                   -- Something went wrong, rollback this record change.
                   ROLLBACK;
         END;     
     END LOOP;
     DBMS_OUTPUT.PUT_LINE('Processed '||v_count||' records');
  END resend_failed_messages;

END dar_message_pkg;


/
show errors

