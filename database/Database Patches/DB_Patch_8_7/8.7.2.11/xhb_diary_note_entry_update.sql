 UPDATE xhb_diary_note_entry dne
 SET dne.COURT_ID = 
     CASE WHEN dne.CASE_LISTING_ENTRY_ID IS NOT NULL THEN 
                    (SELECT cle.COURT_ID
                       FROM xhb_case_listing_entry cle
                      WHERE cle.CASE_LISTING_ENTRY_ID = dne.CASE_LISTING_ENTRY_ID) 
     ELSE
                    (SELECT xc.COURT_ID
                       FROM xhb_case xc
                      WHERE xc.CASE_ID = dne.CASE_ID) 
     END
 WHERE dne.COURT_ID IS NULL AND (dne.CASE_LISTING_ENTRY_ID IS NOT NULL OR dne.CASE_ID IS NOT NULL);
 COMMIT;