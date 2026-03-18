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
  ref_disposal_type_id = ?
AND
  obs_ind = 'N'




