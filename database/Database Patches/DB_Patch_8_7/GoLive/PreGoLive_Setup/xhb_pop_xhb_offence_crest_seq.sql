create or replace procedure xhb_pop_xhb_offence_crest_seq
as 
/**
  * DESCRIPTION :
  *   Procedure                     Purpose
  *   ============================= =======
  *   xhb_pop_xhb_offence_crest_seq CTX-4383 - script to identify XHB_OFFENCE records linked to an XHB_CHARGE record with a CHARGE_TYPE = 'G'
  *                                 where the XHB_OFFENCE.CREST_OFFENCE_SEQ_NO is NULL and update them accordingly.
  *  Assumptions                    This package will need an execution call too.
  *                            
***/

    CURSOR 	charges_cur
    IS
    SELECT 	xc.charge_id
    FROM  	xhb_charge xc
    WHERE 	NVL(xc.obs_ind,'N') <> 'Y'	-- We're not interested in obsolete XHB_CHARGE records, but will update obsolete XHB_OFFENCE records
    AND   	xc.charge_type = 'G'
	AND   	EXISTS (
				SELECT NULL FROM xhb_offence xo
				WHERE xo.charge_id = xc.charge_id
				AND	  xo.crest_offence_seq_no IS NULL
	);

	CURSOR 	charge_offences_cur (c_charge_id NUMBER)
	IS
	SELECT 	offence_id
	FROM	xhb_offence
	WHERE	charge_id = c_charge_id
	AND		crest_offence_seq_no IS NULL
	ORDER BY offence_id;

    v_next_seq_number NUMBER;
	v_count_updates	NUMBER;
	
	v_err_code NUMBER;
	v_err_msg  VARCHAR2(500);

BEGIN
    DBMS_OUTPUT.PUT_LINE('Executing xhb_pop_xhb_offence_crest_seq for all courts.  Start Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');
	
	-- Find all the XHB_CHARGE records that have XHB_OFFENCE records linked to them with a
	-- NULL crest_offence_seq_no
	FOR charge_rec IN charges_cur LOOP
	
		-- Identify the next highest crest_offence_seq_no on the offences linked to the charge (include obsolete records)
		SELECT NVL(MAX(crest_offence_seq_no),0) + 1
		INTO v_next_seq_number
		FROM xhb_offence 
		WHERE charge_id = charge_rec.charge_id
		AND crest_offence_seq_no IS NOT NULL;
		
		v_count_updates := 0;
		
		-- Find all the offences on the charge with the crest_offence_seq_no missing and populate it
		FOR offence_rec IN charge_offences_cur(charge_rec.charge_id) LOOP
		
			UPDATE xhb_offence SET crest_offence_seq_no = v_next_seq_number
			WHERE offence_id = offence_rec.offence_id;
			
			-- Increment the number of updates counter
			v_count_updates := v_count_updates + 1;
			
			-- Increment the next sequence number to use
			v_next_seq_number := v_next_seq_number + 1;
		
		END LOOP;
		
		DBMS_OUTPUT.PUT_LINE('Populated crest_offence_seq_no on '||v_count_updates||' xhb_offence(s) for charge_id '||charge_rec.charge_id);
		
		COMMIT;
	
	END LOOP;
	
    DBMS_OUTPUT.PUT_LINE('Executing xhb_pop_xhb_offence_crest_seq.  End Time: '||to_char(sysdate,'DD-MM-YYYY HH24:MI:SS')||'.');

	EXCEPTION
		WHEN OTHERS THEN
			ROLLBACK;
			v_err_code := SQLCODE;
			v_err_msg  := SQLERRM;
			raise_application_error(-20001,'Error in xhb_pop_xhb_offence_crest_seq :- ' || v_err_code || ' : ' || v_err_msg);

END xhb_pop_xhb_offence_crest_seq;
/