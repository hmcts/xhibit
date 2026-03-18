/*
 * Populate XHB_DOCUMENT_REPLY with List officer as the reply name
 *
 * Court IDs are obtained from the xhb_court table so this will work
 * for any number of courts
 */

DECLARE 

  var_court_id NUMBER;

  CURSOR c_court_id IS
    SELECT court_id
    FROM   xhb_court;

BEGIN

  OPEN c_court_id;

  LOOP

    FETCH c_court_id
    INTO  var_court_id;

    EXIT WHEN c_court_id%NOTFOUND;

    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'DL');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'DLP');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'WL');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'WLL');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'FL');
    INSERT INTO XHB_DOCUMENT_REPLY (reply_name, court_id, document_type) VALUES ('List Officer',var_court_id,'RL');

    COMMIT;

  END LOOP;

  CLOSE c_court_id;

END;
/
