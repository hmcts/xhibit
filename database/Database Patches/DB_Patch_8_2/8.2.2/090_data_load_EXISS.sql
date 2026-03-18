update exi_ref_type
set    version = '5.0'
where  internal_code in (
'AR',
'BO',
'BW',
'CH',
'CO',
'CPO',
'CPR',
'CRO',
'SR',
'DL',
'FL',
'IO',
'RL',
'RO',
'SS',
'TR',
'WL',
'YOI',
'PSR',
'DLP'
);

commit;

