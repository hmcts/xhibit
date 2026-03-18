SELECT
    xhb_ref_disposal_line.ref_disposal_line_id,
    ref_disposal_type.ref_disposal_type_id,
    xhb_ref_disposal_line.disposal_code,
    xhb_ref_disposal_line.template_version,
    xhb_ref_disposal_line.dil_seq_no,
    xhb_ref_disposal_line.data,
    xhb_ref_disposal_line.input_flag,
    xhb_ref_disposal_line.screen_print,
    xhb_ref_disposal_line.form_print,
    xhb_ref_disposal_line.dbdestin,
    xhb_ref_disposal_line.prompt,
    xhb_ref_disposal_line.format,
    xhb_ref_disposal_line.mandatory,
    xhb_ref_disposal_line.dbsource,
    xhb_ref_disposal_line.validation,
    xhb_ref_disposal_line.multiple_choice,
    xhb_ref_disposal_line.mcgroup1,
    xhb_ref_disposal_line.mcgroup2,
    xhb_ref_disposal_line.char_max,
    xhb_ref_disposal_line.line_insert
FROM 
    xhb_ref_disposal_line,
    (SELECT 
         ref_disposal_type_id,
         disposal_code,
         template_version,
         court_id
     FROM 
         xhb_ref_disposal_type
     WHERE
         ref_disposal_type_id = ?
     AND
         obs_ind = 'N') ref_disposal_type
WHERE 
    ref_disposal_type.disposal_code = xhb_ref_disposal_line.disposal_code
AND 
    ref_disposal_type.template_version = xhb_ref_disposal_line.template_version
AND 
    ref_disposal_type.court_id = xhb_ref_disposal_line.court_id
AND
    xhb_ref_disposal_line.obs_ind = 'N'

