DECLARE
PROCEDURE xhb_ref_hearing_insert_PST (p_court_id IN XHB_REF_HEARING_TYPE.COURT_ID%TYPE) AS 

BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_ref_hearing_insert_PST for court ID: '||p_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

INSERT INTO XHB_REF_HEARING_TYPE (COURT_ID, CATEGORY, HEARING_TYPE_CODE, HEARING_TYPE_DESC, SEQ_NO, LIST_SEQUENCE)
SELECT subqry.* FROM (SELECT p_court_id COURT_ID, 'X' CATEGORY, keyed_data.* FROM (
SELECT 'PST' HEARING_TYPE_CODE, 'For Application under the Stalking Prevention Act' HEARING_TYPE_DESC, 95 SEQ_NO, 535 LIST_SEQUENCE FROM DUAL) keyed_data) subqry
WHERE NOT EXISTS (SELECT 1 FROM XHB_REF_HEARING_TYPE xrht 
    WHERE xrht.HEARING_TYPE_CODE = subqry.HEARING_TYPE_CODE
    AND xrht.COURT_ID = subqry.COURT_ID
    AND xrht.CATEGORY = subqry.CATEGORY);
					 
COMMIT;
    DBMS_OUTPUT.PUT_LINE('Executed xhb_ref_hearing_insert_PST.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

EXCEPTION
     WHEN OTHERS THEN
        raise_application_error(-20001,'Error in xhb_ref_hearing_insert_PST :- ' || SQLCODE || ' : ' || SQLERRM);
END xhb_ref_hearing_insert_PST;

BEGIN
	FOR rec IN (SELECT xc.COURT_ID
				FROM XHB_COURT xc
				WHERE NVL(obs_ind,'N') = 'N') LOOP
		xhb_ref_hearing_insert_PST(p_court_id=>rec.COURT_ID);
	END LOOP;
		
END;
/
