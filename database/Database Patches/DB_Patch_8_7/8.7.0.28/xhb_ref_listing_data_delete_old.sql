BEGIN
  DELETE xhb_ref_listing_data rld
   WHERE EXISTS (SELECT 1
                   FROM xhb_ref_listing_data rld2
                  WHERE rld2.ref_data_type = 'NOTE_TYPE'
                    AND rld2.ref_data_value = rld.ref_data_type);

  COMMIT;
END;  
/