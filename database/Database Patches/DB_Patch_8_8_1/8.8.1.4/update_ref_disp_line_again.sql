-- Changes for XLC3-78 / XLC3-81 / XLC3-82
delete from xhb_ref_disposal_line where disposal_code='EXDO21' and dil_seq_no in (22, 60);
update xhb_ref_disposal_line set data='The court ordered that the defendant be sentenced to' where disposal_code='EXDO21' and dil_seq_no in (21);

delete from xhb_ref_disposal_line where dil_seq_no in (23) and disposal_code='STS' and data like '%to serve a period of imprisonment%';
update xhb_ref_disposal_line set data='The court ordered that the defendant be sentenced to' where disposal_code='STS' and dil_seq_no in (21);
update xhb_ref_disposal_line set data='under section 252a Sentencing Act 2020 as follows: An extended sentence of', dil_seq_no=57 where disposal_code='STS' and dil_seq_no in (22);
update xhb_ref_disposal_line set form_print='Y' where disposal_code='STS' and dil_seq_no in (40,50);
update xhb_ref_disposal_line set mcgroup1='A' where disposal_code='STS' and dil_seq_no=40;
update xhb_ref_disposal_line set mcgroup1='B' where disposal_code='STS' and dil_seq_no=50;
delete from xhb_ref_disposal_line where dil_seq_no in (60,65) and disposal_code='STS';


delete from xhb_ref_disposal_line where dil_seq_no in (23) and disposal_code='EXD1820' and data like '%to serve a period of imprisonment%';
update xhb_ref_disposal_line set data='The court ordered that the defendant be sentenced to', form_print='Y' where disposal_code='EXD1820' and dil_seq_no in (21);
update xhb_ref_disposal_line set data='under section 266 Sentencing Act 2020 as follows: An extended sentence of', dil_seq_no=57 where disposal_code='EXD1820' and dil_seq_no in (23);
update xhb_ref_disposal_line set form_print='Y' where disposal_code='EXD1820' and dil_seq_no in (40,50);
update xhb_ref_disposal_line set mcgroup1='A' where disposal_code='EXD1820' and dil_seq_no=40;
update xhb_ref_disposal_line set mcgroup1='B' where disposal_code='EXD1820' and dil_seq_no=50;
delete from xhb_ref_disposal_line where dil_seq_no in (22,60) and disposal_code='EXD1820';

COMMIT;
/