SELECT 
    xhb_defendant.defendant_id,
    xhb_defendant.first_name,
    xhb_defendant.middle_name,
    xhb_defendant.surname
FROM
    xhb_defendant,
    xhb_defendant_on_case
WHERE
    xhb_defendant.defendant_id = xhb_defendant_on_case.defendant_id
AND
    xhb_defendant_on_case.case_id = ?
AND
    (xhb_defendant_on_case.obs_ind is null OR xhb_defendant_on_case.obs_ind <> 'Y')