SELECT
    menu_item_id,    
    title,
    abbrev,
    disposal_code,
    parent,    
    seq_no,
    menu_group
FROM
    xhb_ref_disposal_menu
WHERE
    court_id = ?
AND
    menu_group = ?
AND
    obs_ind = 'N'

