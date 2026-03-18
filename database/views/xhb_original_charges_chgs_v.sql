CREATE OR REPLACE VIEW XHB_ORIGINAL_CHARGES_CHGS_V
AS
select decode(nvl(doo.obs_ind,'N'),'Y','Y',
           decode(nvl(o.obs_ind,'N'),'Y','Y',
           decode(nvl(ro.obs_ind,'N'),'Y','Y',
           decode(nvl(c.obs_ind,'N'),'Y','Y','N')))) obsolete,
       doo.defendant_on_case_id,
       doo.defendant_on_offence_id,
       doo.seq_no,
       o.offence_id,
       o.crest_offence_freetext,
       o.crest_offence_seq_no,
       ro.ref_offence_id,
       ro.offence_desc,
       ro.offence_code,
       c.charge_id,
       c.charge_type,
       c.crest_charge_seq_no
from   xhb_defendant_on_offence    doo,
       xhb_offence                 o,
       xhb_ref_offence             ro,
       xhb_charge                  c
where  doo.offence_id           =  o.offence_id
and    o.ref_offence_id         =  ro.ref_offence_id
and    o.charge_id              =  c.charge_id;