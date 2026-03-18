SELECT 
    case_id,
    case_type,
    case_sub_type,
    case_number
FROM 
    xhb_case	 
WHERE
    case_id = ?