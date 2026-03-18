DECLARE
	
	CURSOR get_def_rep_records IS
	SELECT def_on_case_ref_sol_firm_id, defendant_on_case_id, rep_st_date
	FROM xhb_def_on_case_ref_sol_firm
	WHERE NVL(obs_ind,'N') <> 'Y'
	AND rep_type = 'L'
	AND legal_aid_order_id IS NULL
	ORDER BY defendant_on_case_id, rep_st_date;
	
	v_legal_aid_order_id	xhb_legal_aid_order.legal_aid_order_id%TYPE;

BEGIN

	FOR def_rep_rec IN get_def_rep_records LOOP
	
		BEGIN
			SELECT subquery.legal_aid_order_id INTO v_legal_aid_order_id
			FROM (
				SELECT legal_aid_order_id
				FROM xhb_legal_aid_order
				WHERE defendant_on_case_id IS NOT NULL
				AND defendant_on_case_id = def_rep_rec.defendant_on_case_id
				AND ( NVL(obs_ind,'N') = 'N' OR (NVL(obs_ind,'N') = 'Y' AND date_of_revocation IS NOT NULL) )
				AND order_date <= def_rep_rec.rep_st_date
				ORDER BY legal_aid_order_id DESC) subquery
			WHERE ROWNUM = 1;
			
			UPDATE xhb_def_on_case_ref_sol_firm
			SET legal_aid_order_id = v_legal_aid_order_id
			WHERE def_on_case_ref_sol_firm_id = def_rep_rec.def_on_case_ref_sol_firm_id;
			
			COMMIT;
		
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				NULL;
		END;
	
	END LOOP;

END;

/