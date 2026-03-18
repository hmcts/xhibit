-- Changes for XLC3-75
update xhb_ref_disposal_line set obs_ind='Y' where disposal_code='IMPE' and dil_seq_no=25;
update xhb_ref_disposal_line set form_print='Y' where disposal_code='IMPE' and dil_seq_no=40;
update xhb_ref_disposal_line set form_print='Y' where disposal_code='IMPE' and dil_seq_no=45;
update xhb_ref_disposal_line set mcgroup1='A' where disposal_code='IMPE' and dil_seq_no=40;
update xhb_ref_disposal_line set mcgroup1='B' where disposal_code='IMPE' and dil_seq_no=45;

-- Changes for XLC3-78
delete from xhb_ref_disposal_line where dil_seq_no in (23) and disposal_code='EXDO21' and data like '%to serve a period of imprisonment%';
update xhb_ref_disposal_line set form_print='N' where disposal_code='EXDO21' and dil_seq_no in (22);
update xhb_ref_disposal_line set form_print='Y' where disposal_code='EXDO21' and dil_seq_no in (21,40,50);
update xhb_ref_disposal_line set data='The court ordered that the defendant be sentenced to' where court_id=81 and disposal_code='EXDO21' and dil_seq_no in (21);
update xhb_ref_disposal_line set data='under section 279 Sentencing Act 2020 as follows: An extended sentence of', dil_seq_no=57 where disposal_code='EXDO21' and dil_seq_no in (23);
update xhb_ref_disposal_line set mcgroup1='A' where disposal_code='EXDO21' and dil_seq_no=40;
update xhb_ref_disposal_line set mcgroup1='B' where disposal_code='EXDO21' and dil_seq_no=50;

COMMIT;
/