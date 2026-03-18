delete from xhb_disposal_reference 
where dis_ref_id < 0;

delete from xhb_disposal 
where disposal_id < 0;

delete from xhb_defendant_on_offence 
where defendant_on_offence_id < 0;

delete from xhb_defendant_on_case 
where defendant_on_case_id < 0;

delete from xhb_ref_disposal
where ref_disposal_id < 0;
