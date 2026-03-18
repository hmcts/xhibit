SELECT
    COUNT(disp.disposal2_id) AS row_count
FROM 
    XHB_DISPOSAL2 disp
WHERE 
    disp.defendant_on_case_id = ?
AND
    disp.obs_ind != 'Y'