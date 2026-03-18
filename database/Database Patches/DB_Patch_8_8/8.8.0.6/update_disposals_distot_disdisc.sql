update xhb_ref_disposal_line set data='Disqualified till Test passed? (Y/N)' where disposal_code in ('DISDISC') and dil_seq_no=200;

update xhb_ref_disposal_line set data='Disqualified (Discretionary) for' where disposal_code in ('DISDISC') and dil_seq_no=40;
update xhb_ref_disposal_line set data='Disqualified (under Totting up) for' where disposal_code in ('DISTOT') and dil_seq_no=40;

commit;
