SELECT
    COUNT(xhb_offence.offence_id) as row_count
FROM 
    xhb_defendant_on_case,
    xhb_charge,
    xhb_offence,
    xhb_defendant_on_offence
WHERE 
    xhb_defendant_on_case.case_id = ?
AND
    xhb_defendant_on_case.defendant_id = ?
AND
    xhb_charge.case_id = xhb_defendant_on_case.case_id
AND
    xhb_offence.charge_id = xhb_charge.charge_id 
AND
    xhb_defendant_on_offence.offence_id = xhb_offence.offence_id
AND
    xhb_defendant_on_offence.defendant_on_case_id = xhb_defendant_on_case.defendant_on_case_id
AND
    xhb_charge.charge_type = 'O'