-- delete duplicate directions_for_case records
DELETE FROM xhb_directions_for_case dfc1
WHERE EXISTS (SELECT 1 
                FROM xhb_directions_for_case dfc2
               WHERE dfc1.case_id = dfc2.case_id
                 AND dfc1.directions_for_case_id > 
                    dfc2.directions_for_case_id);
COMMIT;