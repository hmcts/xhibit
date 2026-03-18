SELECT 
    defendant_on_case_id 
FROM 
    xhb_Defendant_on_case
WHERE
    case_id = ?
AND 
    defendant_id = ?