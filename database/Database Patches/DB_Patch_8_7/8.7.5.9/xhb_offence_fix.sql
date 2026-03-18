DECLARE

	-- Find all non obsolete offences on Appeal cases where the appeal type is null
	CURSOR c_get_offences_no_appeal_type IS
	SELECT xo.offence_id, xca.case_sub_type
	FROM xhb_case xca, xhb_offence xo, xhb_charge xch
	WHERE xca.case_type = 'A'
	AND xch.case_id  = xca.case_id
	AND xo.charge_id = xch.charge_id
	AND NVL(xo.obs_ind,'N') <> 'Y'
	AND xo.appeal_type IS NULL;
	
	lv_appeal_type	xhb_offence.appeal_type%TYPE;

BEGIN

	FOR offence_rec IN c_get_offences_no_appeal_type LOOP
	
		BEGIN
			-- Try and identify the appeal type from the xhb_defendant_on_offence
			SELECT appeal_against_type INTO lv_appeal_type 
			FROM xhb_defendant_on_offence 
			WHERE offence_id = offence_rec.offence_id 
			AND NVL(obs_ind,'N') <> 'Y'
			AND appeal_against_type IS NOT NULL
			AND ROWNUM = 1;
		
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				-- No appeal type on xhb_defendant_on_offence so use the case sub type
				lv_appeal_type := offence_rec.case_sub_type;
				
		END;
		
		-- Update the offence and commit
		UPDATE xhb_offence
		SET appeal_type = lv_appeal_type
		WHERE offence_id = offence_rec.offence_id;
		
		COMMIT;
	
	END LOOP;
	
END;
/