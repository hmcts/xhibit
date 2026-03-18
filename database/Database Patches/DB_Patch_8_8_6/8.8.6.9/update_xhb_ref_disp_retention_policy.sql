update xhb_ref_disp_retention_policy xdp
set xdp.ref_dar_retention_policy_id = (select xrdrp.ref_dar_retention_policy_id from xhb_ref_dar_retention_policies xrdrp where xrdrp.policy_no = 1)
where xdp.disposal_code = 'NSP'
and xdp.ref_dar_retention_policy_id != (select xrdrp.ref_dar_retention_policy_id from xhb_ref_dar_retention_policies xrdrp where xrdrp.policy_no = 1);

update xhb_ref_disp_retention_policy
set has_duration = 'N', obs_ind = 'Y'
where disposal_code in('TIMP','TFINE','TPAY','TSUSP');

commit;