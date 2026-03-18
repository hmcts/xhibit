CREATE OR REPLACE PACKAGE BODY dar_message_pkg AS

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


END dar_message_pkg;
