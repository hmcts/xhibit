update xhb_ref_disposal_type set d20_other_sentence='A' where disposal_code in ('IMP','IMPA','IMPE','IMPMIN','IMPMINE','IMPO','IMPX','INIMP','LIFE','LIFELIS','LIFESEC','LIMM');
update xhb_ref_disposal_type set d20_other_sentence='C' where disposal_code in ('CUSMCUR','CUSMSEC','CUSMSSE','SS','SSSO');
update xhb_ref_disposal_type set d20_other_sentence='E' where disposal_code in ('CD','CDO');
update xhb_ref_disposal_type set d20_other_sentence='J' where disposal_code in ('ABDIS');
update xhb_ref_disposal_type set d20_other_sentence='M' where disposal_code in ('ALTRTCS','ATCRQCS','CURFCS','CWRKCS','DRGRQCS','EXCARCS','EXCURCS','FTPRCS','MHTRTCS','PACOBCS','PRACTCS','PSACTCS','REHARCS','RESRQCS','SPVRQCS');
update xhb_ref_disposal_type set d20_other_sentence='P' where disposal_code in ('DET','DFL','DTO','IMPMYO','IMPMYOE','INDET','LIFELYO','LIFESYO','YOI','YOIE');

commit;
