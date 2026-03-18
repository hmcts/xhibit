SELECT 
    * 
FROM 
    (SELECT 
        ref_disposal_type_id,
        disposal_code,
        template_version,
        title,
        line_avail,
        category 
     FROM 
        xhb_ref_disposal_type
     WHERE
        court_id = ?
     AND
        disposal_code = ?
     AND
        obs_ind = 'N'
     ORDER BY
        template_version DESC)
WHERE
    rownum = 1




