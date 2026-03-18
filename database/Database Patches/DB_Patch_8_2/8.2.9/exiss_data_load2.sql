update exi_ref_type
set    version = '1.2'
where  internal_code in (
'UPDCASE',
'NEWCASE',
'DISCASE'
);

commit;


