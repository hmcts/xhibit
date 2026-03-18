update xhb_ref_disposal_line set data='Disqualified (under Totting Up) for' where disposal_code in ('DISTOT') and dil_seq_no=40;

commit;
