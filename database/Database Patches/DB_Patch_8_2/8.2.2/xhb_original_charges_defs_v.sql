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
       d.surname
from   xhb_case                 case,
       xhb_defendant            d,
       xhb_defendant_on_case    doc
where  case.case_id             =  doc.case_id
and    doc.defendant_id         =  d.defendant_id
and   (doc.obs_ind is null or doc.obs_ind = 'N');
