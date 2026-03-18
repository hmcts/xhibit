update xhb_ref_disposal_line set dbdestin='D26' where disposal_code in ('DISOBLG','DISTOT','DISDISC','DISINT') and data='Life';

update xhb_ref_disposal_line set form_print='Y' where disposal_code in ('DISOBLG','DISTOT','DISDISC','DISINT') and prompt like '%DTTP-1%';
update xhb_ref_disposal_line set form_print='Y' where disposal_code in ('DISOBLG','DISTOT','DISDISC','DISINT') and prompt like '%DTETP-4%';

update xhb_ref_disposal_line set dbdestin='D6' where disposal_code in ('DISDISC') and dil_seq_no=120;
update xhb_ref_disposal_line set dbdestin='D7' where disposal_code in ('DISDISC') and dil_seq_no=300;
update xhb_ref_disposal_line set dbdestin='D8' where disposal_code in ('DISDISC') and dil_seq_no=320;
update xhb_ref_disposal_line set dbdestin='D6' where disposal_code in ('DISOBLG') and dil_seq_no=120;
update xhb_ref_disposal_line set dbdestin='D27' where disposal_code in ('DISOBLG') and dil_seq_no=240;
update xhb_ref_disposal_line set dbdestin='D7' where disposal_code in ('DISOBLG') and dil_seq_no=300;
update xhb_ref_disposal_line set dbdestin='D8' where disposal_code in ('DISOBLG') and dil_seq_no=320;
update xhb_ref_disposal_line set dbdestin='D6' where disposal_code in ('DISTOT') and dil_seq_no=120;
update xhb_ref_disposal_line set dbdestin='D7' where disposal_code in ('DISTOT') and dil_seq_no=300;
update xhb_ref_disposal_line set dbdestin='D8' where disposal_code in ('DISTOT') and dil_seq_no=320;

update xhb_ref_disposal_line set mcgroup1='B' where disposal_code in ('DISOBLG','DISTOT','DISDISC','DISINT') and data='Life';
update xhb_ref_disposal_line set multiple_choice='Y' where disposal_code in ('DISOBLG','DISTOT','DISDISC','DISINT') and (data='Life' or dbdestin='D5' or dbdestin='D6');

commit;
