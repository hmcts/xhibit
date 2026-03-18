CREATE OR REPLACE PACKAGE dar_message_pkg AS

   /*
   ** Assign default values to the globals.
   ** These will be initialised as part of package initialisation
   */
   g_msg_code_new            VARCHAR(1)                                  := 'N';
   g_msg_code_success        VARCHAR(1)                                  := 'S';
   g_msg_code_fail           VARCHAR(1)                                  := 'F';

   TYPE input_array is VARRAY(32) of INTEGER;
   TYPE split_tbl is table of varchar2(23767);
   TYPE failed_messages_tbl IS TABLE OF DAR_MESSAGE_STORE%ROWTYPE;

  /*
   * get_darts_property
   */
  FUNCTION get_darts_property(p_property_name IN DAR_DARTS_CONFIG.DARTS_PROPERTY_NAME%TYPE) 
     RETURN DAR_DARTS_CONFIG.DARTS_PROPERTY_VALUE%TYPE;
   
  /*
   * Get clob data for message payload for a given message id
   */
    PROCEDURE get_message_payload(clob_data_in  OUT SYS_REFCURSOR,
                                   message_id_in    IN DAR_MESSAGE_STORE.MESSAGE_ID%TYPE);

  /*
   * Insert Message into DAR_MESSAGE_STORE
   */
    FUNCTION insert_darts_message_store( xhibit_code_in  IN DAR_MESSAGE_STORE.XHIBIT_MESSAGE_CODE%TYPE,
                                         exiss_code_in   IN DAR_MESSAGE_STORE.EXISS_MESSAGE_CODE%TYPE,
                                         payload_in      IN DAR_MESSAGE_STORE.PAYLOAD%TYPE)
                                         RETURN DAR_MESSAGE_STORE.MESSAGE_ID%TYPE;

  FUNCTION success_darts_msg_store( success_ids  IN DARTS_MESSAGE_ID_ARRAY)
  RETURN NUMBER;

  FUNCTION fail_darts_msg_store( failures_ids       IN DARTS_MESSAGE_ID_ARRAY,
                                 failure_reasons    IN DARTS_MESSAGE_FAIL_ARRAY)
  RETURN NUMBER;

/* 
 *
 * Split a string using the delimiter provided 
 *
 */
  FUNCTION split(p_list varchar2,
     p_del varchar2)
  RETURN split_tbl pipelined;

/*
 * 
 * Function to establish whether message is high priority (1) or not (0)
 *
 */  
  FUNCTION IS_PRIORITY_MESSAGE(p_code VARCHAR2)
  RETURN NUMBER;

 FUNCTION is_numeric_YN(p_string IN VARCHAR2)
    RETURN VARCHAR2;
    
/*
 * Is the runnable period valid
 */
  FUNCTION is_runnable_period_YN(p_datetime IN DATE DEFAULT SYSDATE) 
     RETURN VARCHAR2;

/*
 * Get the resend cutoff limit
 */
  FUNCTION get_resend_cutoff_limit(p_datetime IN DATE DEFAULT SYSDATE)
     RETURN DATE;

/*
 * Get the failed messages as a pipelined table
 */
  FUNCTION get_failed_messages(p_datetime IN DATE DEFAULT SYSDATE) 
     RETURN failed_messages_tbl PIPELINED;
  
/*
 * Resend failed Darts messages
 */     
  PROCEDURE resend_failed_messages;
  
END dar_message_pkg;

/
show errors
