INSERT INTO XHB_REF_LISTING_DATA
(ref_listing_data_id, ref_data_type, ref_data_value, obs_ind)
SELECT XHB_REF_LISTING_DATA_SEQ.NEXTVAL, vars.ref_data_type, vars.ref_data_value, 'N'
FROM 
(-- Note Classifications
 SELECT 'NOTE_CLASSIFICATION' ref_data_type,'Standard' ref_data_value FROM DUAL UNION
 SELECT 'NOTE_CLASSIFICATION' ref_data_type,'Priority' ref_data_value FROM DUAL UNION
 SELECT 'NOTE_CLASSIFICATION' ref_data_type,'Restricted' ref_data_value FROM DUAL UNION
 -- Note Types
 SELECT 'DCN' ref_data_type,'Default Case Note' ref_data_value FROM DUAL UNION
 SELECT 'HN' ref_data_type,'Highlight Note' ref_data_value FROM DUAL UNION
 SELECT 'IN' ref_data_type,'Interpreter Note' ref_data_value FROM DUAL UNION
 -- Predefined Notes
 SELECT 'PREDEFINED_LIST_NOTE' ref_data_type,'Predefined List Note 1' ref_data_value FROM DUAL UNION
 SELECT 'PREDEFINED_LIST_NOTE' ref_data_type,'Predefined List Note 2' ref_data_value FROM DUAL 
) vars 
WHERE NOT EXISTS (SELECT 1 FROM XHB_REF_LISTING_DATA curr
                   WHERE curr.ref_data_type = vars.ref_data_type 
                     AND curr.ref_data_value = vars.ref_data_value);
COMMIT;