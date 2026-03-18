set serveroutput on
execute dbms_output.enable(100000)

CLEAR SCREEN

SPOOL training_data_purge.log

PROMPT Please enter the COURT ID of the training court data you wish to purge

PROMPT

ACCEPT training_court PROMPT 'Enter Training Court ID : '

DECLARE

  checkCOURT_ID NUMBER;

BEGIN

  SELECT court_id
  INTO   checkCOURT_id
  FROM   XHB_TRAINING_COURT;

  IF &training_court != checkCOURT_id THEN

    DBMS_OUTPUT.PUT_LINE('-');
    DBMS_OUTPUT.PUT_LINE('-');
    DBMS_OUTPUT.PUT_LINE('Court ID '||&training_court||' is not a valid training court id');
    DBMS_OUTPUT.PUT_LINE('-');
    DBMS_OUTPUT.PUT_LINE('NO DATA HAS BEEN DELETE');

  ELSE

    DBMS_OUTPUT.PUT_LINE('-');
    DBMS_OUTPUT.PUT_LINE('-');

    XHIBIT.xhb_purge_training_data_proc(&training_court);

    DBMS_OUTPUT.PUT_LINE('Data hase been deleted for Court ID '||&training_court);

  END IF;

END;
/
spool off
exit
