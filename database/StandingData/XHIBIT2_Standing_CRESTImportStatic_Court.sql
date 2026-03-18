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
    
    DELETE FROM XHB_CREST_IMPORT 
    WHERE  COURT_ID=var_court_id;
    
    INSERT INTO  XHB_CREST_IMPORT(STATUS, IMPORT_TYPE, COURT_ID)
    SELECT 'N', IMPORT_TYPE, var_court_id
    FROM   XHB_CREST_IMPORT_TYPE;
    
    COMMIT;
    
  END LOOP;
    
  CLOSE c_court_id;
    
END;
/
