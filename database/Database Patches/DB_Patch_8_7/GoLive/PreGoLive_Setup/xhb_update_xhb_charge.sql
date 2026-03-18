create or replace procedure xhb_update_xhb_charge (p_court_id IN xhb_court.court_id%TYPE DEFAULT NULL)
as 
/**
  * DESCRIPTION :
  *   Procedure                  Purpose
  *   ========================== =======
  *   xhb_update_xhb_charge      CTX-4302 - update xhb_charge.
  *                              Looks for records where there is no data in XHB_CHARGE.REF_SYSTEM_CODE_ID for
  *                              summary or committal offences and populates the column accordingly.
  *                            
***/


    CURSOR charge_cur
    IS
    SELECT xcg.charge_id, xcg.charge_type
    FROM  xhb_case xc, xhb_charge xcg
    WHERE xc.court_id = p_court_id
	AND	  xc.case_id = xcg.case_id
    AND   NVL(xcg.obs_ind,'N') <> 'Y'
	AND	  xcg.charge_type IN ('S','O')
	AND	  xcg.ref_system_code_id IS NULL;
	
	CURSOR get_ref_system_code_id (c_code_type VARCHAR2)
	IS 
	SELECT	ref_system_code_id
	FROM	xhb_ref_system_code
	WHERE	code_type = c_code_type
	AND		court_id = p_court_id
	AND		NVL(obs_ind,'N') <> 'Y';

    TYPE t_charge_id IS TABLE OF xhb_charge.charge_id%TYPE;
	TYPE t_charge_type IS TABLE OF xhb_charge.charge_type%TYPE; 
	TYPE t_ref_system_code_id IS TABLE OF xhb_charge.ref_system_code_id%TYPE;

    v_charge_ids  		t_charge_id;
	v_charge_types		t_charge_type;
	v_ref_system_codes	t_ref_system_code_id;

	v_committal_ref_id	xhb_charge.ref_system_code_id%TYPE;
	v_summary_ref_id	xhb_charge.ref_system_code_id%TYPE;
	
	v_total_updated		NUMBER;
    
	v_err_code NUMBER;
	v_err_msg  VARCHAR2(500);

BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_charge for court ID: '||p_court_id||'.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');
	
	-- Retrieve the committal offence ref_system_code_id to use
	OPEN get_ref_system_code_id('HO_PROC_SENT');
	FETCH get_ref_system_code_id INTO v_committal_ref_id;
	CLOSE get_ref_system_code_id;
	
	-- Retrieve the summary offence ref_system_code_id to use
	OPEN get_ref_system_code_id('HO_PROC_S41');
	FETCH get_ref_system_code_id INTO v_summary_ref_id;
	CLOSE get_ref_system_code_id;
	
	-- Initialise the collections and counts
	v_ref_system_codes := t_ref_system_code_id();
	v_total_updated := 0;
	
	-- Update any records that need to be updated
    OPEN charge_cur;
    LOOP
		FETCH charge_cur BULK COLLECT INTO v_charge_ids, v_charge_types LIMIT 1000;
		
		-- Increment the count
		v_total_updated := v_total_updated + v_charge_ids.COUNT;
		
		FOR charge_idx IN 1..v_charge_ids.COUNT
		LOOP
		
			v_ref_system_codes.extend();
			-- Determine which ref_system_code_id should be populated for each record
			IF v_charge_types(charge_idx) = 'S' THEN
				v_ref_system_codes(charge_idx) := v_committal_ref_id;
			ELSE
				v_ref_system_codes(charge_idx) := v_summary_ref_id;
			END IF;
		
		END LOOP;
		
		-- Perform the update
		FORALL i IN 1..v_charge_ids.COUNT
			UPDATE xhb_charge 
			SET ref_system_code_id = v_ref_system_codes(i) 
			WHERE charge_id = v_charge_ids(i);

		COMMIT;
         
		EXIT WHEN v_charge_ids.COUNT = 0;
		
		-- Reinitialise collections
		IF v_charge_ids IS NOT NULL THEN
			v_charge_ids.DELETE;
		END IF;
		
		IF v_charge_types IS NOT NULL THEN
			v_charge_types.DELETE;
		END IF;
		
		IF v_ref_system_codes IS NOT NULL THEN
			v_ref_system_codes.DELETE;
		END IF;
	
	END LOOP;
	CLOSE charge_cur;

	DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_charge.  Total records updated: '||v_total_updated);
    DBMS_OUTPUT.PUT_LINE('Executing xhb_update_xhb_charge.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

EXCEPTION
	WHEN OTHERS THEN
		v_err_code := SQLCODE;
		v_err_msg  := SQLERRM;
		raise_application_error(-20001,'Error in xhb_update_xhb_charge :- ' || v_err_code || ' : ' || v_err_msg);

END xhb_update_xhb_charge;
/