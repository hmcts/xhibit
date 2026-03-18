DECLARE
	
	CURSOR get_pros_rep_records IS
	SELECT prosecutor_ref_sol_firm_id, case_pros_agency_id, rep_st_date
	FROM xhb_prosecutor_ref_sol_firm
	WHERE NVL(obs_ind,'N') <> 'Y'
	AND rep_type = 'L'
	AND legal_aid_order_id IS NULL
	ORDER BY case_pros_agency_id, rep_st_date;
	
	v_legal_aid_order_id	xhb_legal_aid_order.legal_aid_order_id%TYPE;

BEGIN

	FOR pros_rep_rec IN get_pros_rep_records LOOP
	
		BEGIN
			SELECT subquery.legal_aid_order_id INTO v_legal_aid_order_id
			FROM (
				SELECT legal_aid_order_id
				FROM xhb_legal_aid_order
				WHERE case_pros_agency_id IS NOT NULL
				AND case_pros_agency_id = pros_rep_rec.case_pros_agency_id
				AND ( NVL(obs_ind,'N') = 'N' OR (NVL(obs_ind,'N') = 'Y' AND date_of_revocation IS NOT NULL) )
				AND order_date <= pros_rep_rec.rep_st_date
				ORDER BY legal_aid_order_id DESC) subquery
			WHERE ROWNUM = 1;
			
			UPDATE xhb_prosecutor_ref_sol_firm
			SET legal_aid_order_id = v_legal_aid_order_id
			WHERE prosecutor_ref_sol_firm_id = pros_rep_rec.prosecutor_ref_sol_firm_id;
			
			COMMIT;
		
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				NULL;
		END;
	
	END LOOP;

END;

/