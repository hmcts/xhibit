/* DVR-177 */
update xhb_ref_disp_retention_policy xdp
set xdp.ref_dar_retention_policy_id = (select xrdrp.ref_dar_retention_policy_id from xhb_ref_dar_retention_policies xrdrp where xrdrp.policy_no = 2)
where xdp.disposal_code = 'SVRO'
and xdp.ref_dar_retention_policy_id != (select xrdrp.ref_dar_retention_policy_id from xhb_ref_dar_retention_policies xrdrp where xrdrp.policy_no = 2);

commit;

update xhb_ref_disp_retention_policy xdp
set has_duration = null where xdp.disposal_code = 'SVRO';

COMMIT;