update xhb_ref_dar_retention_policies set policy_description = '7 Years Minimum (Custodial)' 
where policy_no = 3 
and policy_description != '7 Years Minimum (Custodial)';

commit;