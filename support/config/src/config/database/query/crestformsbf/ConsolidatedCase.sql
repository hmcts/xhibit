SELECT 
    c2.case_id,
    cs.case_type,
    cs.case_sub_type,
    cs.case_number
FROM   
    xhb_charge c1, 
    xhb_charge c2,
    xhb_case cs,
    xhb_joinder_charge jc1,
    xhb_joinder_charge jc2
WHERE
    c1.case_id = ?
AND
    jc1.charge_id = c1.charge_id
AND
    jc2.joinder_id = jc1.joinder_id
AND
    c2.charge_id = jc2.charge_id
AND
    c2.charge_id <> c1.charge_id
AND 
    c2.case_id = cs.case_id