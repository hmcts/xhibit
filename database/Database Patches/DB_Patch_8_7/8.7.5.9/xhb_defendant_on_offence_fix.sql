DECLARE

	-- Find all non obsolete xhb_defendant_on_offence records on Appeal cases where the appeal_against_type is null
	CURSOR c_get_xdoo_no_appeal_type IS
	SELECT xdoo.defendant_on_offence_id, xo.appeal_type, xca.case_sub_type
	FROM xhb_case xca, xhb_offence xo, xhb_charge xch, xhb_defendant_on_offence xdoo
	WHERE xca.case_type = 'A'
	AND xch.case_id  = xca.case_id
	AND xo.charge_id = xch.charge_id
	AND NVL(xo.obs_ind,'N') <> 'Y'
	AND xdoo.offence_id = xo.offence_id
	AND NVL(xdoo.obs_ind,'N') <> 'Y'
	AND xdoo.appeal_against_type IS NULL;
	
	lv_appeal_against_type	xhb_defendant_on_offence.appeal_against_type%TYPE;

BEGIN

	FOR xdoo_rec IN c_get_xdoo_no_appeal_type LOOP
	
		-- Use XHB_OFFENCE.APPEAL_TYPE or if that is NULL, use XHB_CASE.CASE_SUB_TYPE
		IF xdoo_rec.appeal_type IS NOT NULL THEN
			lv_appeal_against_type := xdoo_rec.appeal_type;
		ELSE
			lv_appeal_against_type := xdoo_rec.case_sub_type;
		END IF;
		
		-- Update the record and commit
		UPDATE xhb_defendant_on_offence
		SET appeal_against_type = lv_appeal_against_type
		WHERE defendant_on_offence_id = xdoo_rec.defendant_on_offence_id;
		
		COMMIT;
	
	END LOOP;
	
END;
/