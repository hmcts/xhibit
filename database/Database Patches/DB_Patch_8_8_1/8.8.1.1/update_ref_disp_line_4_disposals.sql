update xhb_ref_disposal_line
	set dbdestin='D9' where disposal_code in ('STS','DETTO','EXDO21','EXD1820')
	and prompt like '%Effective:%' and data = 'N';
COMMIT;
/