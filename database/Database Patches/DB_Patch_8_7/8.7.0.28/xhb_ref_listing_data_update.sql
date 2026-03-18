BEGIN
    FOR rec IN (SELECT rld.ref_listing_data_id note_type_id,
                      (SELECT rld2.ref_listing_data_id
                         FROM xhb_ref_listing_data rld2
                        WHERE rld2.ref_data_type = rld.ref_data_value) old_note_type_id
                  FROM xhb_ref_listing_data rld
                 WHERE rld.ref_data_type = 'NOTE_TYPE') LOOP
        IF rec.old_note_type_id IS NOT NULL THEN
            UPDATE xhb_diary_note_entry dne
               SET dne.note_type_id = rec.note_type_id
             WHERE dne.note_type_id = rec.old_note_type_id;
        END IF;
    END LOOP;
    
    COMMIT;
END;
/