SELECT
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
    menu_group = ?
AND
    obs_ind = 'N'
ORDER BY
    ref_disposal_type_id