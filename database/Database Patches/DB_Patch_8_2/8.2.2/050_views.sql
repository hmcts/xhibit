create or replace view xhb_original_charges_chgs_v as
select doo.defendant_on_case_id,
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
and    o.charge_id              =  c.charge_id
and   (doo.obs_ind   is null or doo.obs_ind = 'N')
and   (o.obs_ind     is null or o.obs_ind   = 'N')
and   (ro.obs_ind    is null or ro.obs_ind  = 'N')
and   (c.obs_ind     is null or c.obs_ind   = 'N');



create or replace view xhb_original_charges_defs_v as
select case.court_id,
       case.case_id,
       case.case_type,
       case.case_number,
       doc.defendant_on_case_id,
       doc.asn,
       d.defendant_id,
       d.first_name,
       d.middle_name,
       d.surname,
       count(decode(c.charge_type,'I',1,null)) total_indictments,
       count(decode(c.charge_type,'G',1,null)) total_original_charges
from   xhb_case                 case,
       xhb_defendant            d,
       xhb_defendant_on_case    doc,
       xhb_defendant_on_offence doo,
       xhb_offence              o,
       xhb_charge               c
where  case.case_id             =  doc.case_id
and    doc.defendant_id         =  d.defendant_id
and    doc.defendant_on_case_id =  doo.defendant_on_case_id(+)
and    doo.offence_id           =  o.offence_id(+)
and    o.charge_id              =  c.charge_id(+)
and   (doc.obs_ind is null or doc.obs_ind = 'N')
and   (doo.obs_ind is null or doo.obs_ind = 'N')
and   (o.obs_ind   is null or o.obs_ind   = 'N')
and   (c.obs_ind   is null or c.obs_ind   = 'N')
group by case.court_id,
         case.case_id,
         case.case_type,
         case.case_number,
         doc.defendant_on_case_id,
         doc.asn,
         d.defendant_id,
         d.first_name,
         d.middle_name,
         d.surname;
