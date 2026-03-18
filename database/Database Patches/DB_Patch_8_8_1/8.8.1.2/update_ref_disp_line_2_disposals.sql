update xhb_ref_disposal_line
	set dbdestin='D9' where disposal_code in ('IMPE','YOIE')
	and prompt like '%Effective:%' and data = 'N';
COMMIT;
/