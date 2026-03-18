CREATE OR REPLACE PROCEDURE XHB_HEARING_TYPE_INSERT_AST (p_court_id IN XHB_REF_HEARING_TYPE.COURT_ID%TYPE) AS 

--variable for AST hearing type to be created if not in xhb_ref_hearing_type
 h_type_ast xhb_ref_hearing_type.hearing_type_code%TYPE DEFAULT NULL;
 
 v_court_count      NUMBER := 0;
 e_invalid_court_id EXCEPTION;
 v_court_exists		NUMBER := 0;

 --get a list of all non obsolete courts
 CURSOR courts_c IS
 SELECT xc.court_id
 FROM xhb_court xc
 WHERE nvl(xc.obs_ind,'-') <> 'Y';

BEGIN

 IF p_court_id IS NOT NULL THEN --user has specified a court so it needs to be checked that it does exist
  SELECT count(*)
  INTO v_court_count
  FROM xhb_ref_hearing_type xrht
  WHERE xrht.court_id = p_court_id
  AND hearing_type_code = 'AST'
  AND p_court_id is not null
  AND nvl(xrht.obs_ind, '-') <> 'Y';
 END IF;
 
 /*Check court exists in xhb_court */
 IF p_court_id IS NOT NULL THEN
  SELECT count(*)
  INTO v_court_exists
  FROM xhb_court xc
  WHERE xc.court_id = p_court_id
  AND nvl(xc.obs_ind,'-') <> 'Y';
 IF v_court_exists < 1 
  THEN
   RAISE e_invalid_court_id;
 END IF;
 END IF;
 
  /*Court doesn't exist so insert*/
 IF p_court_id IS NOT NULL THEN
 IF v_court_count = 0
  THEN
	INSERT INTO XHB_REF_HEARING_TYPE (COURT_ID, CATEGORY, HEARING_TYPE_CODE, HEARING_TYPE_DESC, SEQ_NO, LIST_SEQUENCE)
	SELECT subqry.* FROM (SELECT p_court_id COURT_ID, 'X' CATEGORY, keyed_data.* FROM (
	SELECT 'AST' HEARING_TYPE_CODE, 'For Appeal under the Stalking Prevention Act 2019' HEARING_TYPE_DESC, 95 SEQ_NO, 535 LIST_SEQUENCE FROM DUAL) keyed_data) subqry 
	WHERE NOT EXISTS (SELECT 1 FROM XHB_REF_HEARING_TYPE xrht 
                   WHERE xrht.HEARING_TYPE_CODE = subqry.HEARING_TYPE_CODE
                     AND xrht.COURT_ID = subqry.COURT_ID
                     AND xrht.CATEGORY = subqry.CATEGORY);
 END IF;
 END IF;
 
 
 IF p_court_id IS NULL THEN  
  FOR courts_r IN courts_c
   LOOP
    SELECT count(*)
    INTO v_court_count
    FROM xhb_ref_hearing_type xrht
    WHERE xrht.court_id = courts_r.court_id
    AND hearing_type_code = 'AST'
    AND courts_r.court_id is not null
    AND nvl(xrht.obs_ind, '-') <> 'Y';
	IF v_court_count = 0
	 THEN
	 INSERT INTO XHB_REF_HEARING_TYPE (COURT_ID, CATEGORY, HEARING_TYPE_CODE, HEARING_TYPE_DESC, SEQ_NO, LIST_SEQUENCE)
	 SELECT subqry.* FROM (SELECT courts_r.court_id COURT_ID, 'X' CATEGORY, keyed_data.* FROM (
	 SELECT 'AST' HEARING_TYPE_CODE, 'For Appeal under the Stalking Prevention Act 2019' HEARING_TYPE_DESC, 95 SEQ_NO, 535 LIST_SEQUENCE FROM DUAL) keyed_data) subqry 
	 WHERE NOT EXISTS (SELECT 1 FROM XHB_REF_HEARING_TYPE xrht 
                    WHERE xrht.HEARING_TYPE_CODE = subqry.HEARING_TYPE_CODE
                     AND xrht.COURT_ID = subqry.COURT_ID
                     AND xrht.CATEGORY = subqry.CATEGORY);
    END IF;
   END LOOP;
 END IF;

 
COMMIT;
	DBMS_OUTPUT.PUT_LINE('Executed xhb_hearing_type_insert_ast.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');
 
--Exception catch
EXCEPTION
    WHEN e_invalid_court_id THEN raise_application_error(-20001,' Error exception xhb_hearing_type_insert_ast: '||p_court_id||' does not exist in XHB_COURT');
    WHEN OTHERS THEN raise_application_error(-20001,' Error exception xhb_hearing_type_insert_ast: '|| SQLCODE || ' : ' || SQLERRM);	   
	   
END XHB_HEARING_TYPE_INSERT_AST;
/ 	   