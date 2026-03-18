-- Delete duplicate case totals
DELETE FROM xhb_dar_retention_policy xdrp1
WHERE xdrp1.case_id IS NOT NULL 
AND EXISTS (SELECT NULL FROM xhb_dar_retention_policy xdrp2
              WHERE xdrp2.case_id IS NOT NULL 
              AND xdrp2.case_id = xdrp1.case_id
              AND xdrp2.dar_retention_policy_id < xdrp1.dar_retention_policy_id);

-- Realign any case records that are now orphaned
UPDATE XHB_CASE xc
SET xc.dar_retention_policy_id =  
   (SELECT xdrp1.dar_retention_policy_id FROM xhb_dar_retention_policy xdrp1
      WHERE xc.case_id = xdrp1.case_id)
WHERE xc.dar_retention_policy_id IS NOT NULL
AND NOT EXISTS (SELECT NULL FROM xhb_dar_retention_policy xdrp2
      WHERE xc.dar_retention_policy_id = xdrp2.dar_retention_policy_id);
  
COMMIT;
/