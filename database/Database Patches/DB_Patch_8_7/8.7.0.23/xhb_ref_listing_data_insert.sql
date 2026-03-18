INSERT INTO XHB_REF_LISTING_DATA
(ref_listing_data_id, ref_data_type, ref_data_value, obs_ind)
SELECT XHB_REF_LISTING_DATA_SEQ.NEXTVAL, vars.ref_data_type, vars.ref_data_value, 'N'
FROM 
(
 -- Note Types
 SELECT 'GDN' ref_data_type,'General Diary Note' ref_data_value FROM DUAL
) vars 
WHERE NOT EXISTS (SELECT 1 FROM XHB_REF_LISTING_DATA curr
                   WHERE curr.ref_data_type = vars.ref_data_type 
                     AND curr.ref_data_value = vars.ref_data_value);
COMMIT;