SELECT
    c1.case_id,
    c1.case_type,
    c1.case_sub_type,
    c1.case_number
FROM 
    XHB_CASE c1,
    XHB_HEARING h1,
    XHB_HEARING h2	 
WHERE
    h1.case_id = ?
AND	 
    h2.linked_hearing_id = h1.linked_hearing_id    
AND    
    h2.case_id != h1.case_id	
AND 
    c1.case_id = h2.case_id
    
