
--
-- Script to modify the Disposals reference data with the CCN0361 changes.
-- This script can be used to test CCN0361 before the CREST changes have been implemented.
--


delete from  xhb_ref_disposal_line t
where t.disposal_code = 'CUSMSEC'
and t.template_version = 4
and t.court_id = 81;


insert into xhb_ref_disposal_line (
 court_id, 
 disposal_code, 
 template_version, 
 dil_seq_no,
 data, 
 input_flag,
 screen_print,
 form_print,
 dbdestin,
 prompt,
 format,
 mandatory, 
 dbsource,
 validation,
 multiple_choice,
 mcgroup1,
 mcgroup2,
 char_max,
 conc_flag,
 line_insert
)
select
 t.court_id, 
 t.disposal_code, 
 4, 
 t.dil_seq_no,
 t.data, 
 t.input_flag,
 t.screen_print,
 t.form_print,
 t.dbdestin,
 t.prompt,
 t.format,
 t.mandatory, 
 t.dbsource,
 t.validation,
 t.multiple_choice,
 t.mcgroup1,
 t.mcgroup2,
 t.char_max,
 t.conc_flag,
 t.line_insert
from xhb_ref_disposal_line t 
where t.disposal_code = 'CUSMSEC'
and t.template_version = 3
and nvl(t.obs_ind, '?') <> 'Y'
and t.court_id = 81;


insert into xhb_ref_disposal_line (
 court_id, 
 disposal_code, 
 template_version, 
 dil_seq_no,
 data, 
 input_flag,
 screen_print,
 form_print,
 mandatory, 
 conc_flag
) values (
 81,
 'CUSMSEC',
 4,
 65,
 '(must be weeks or months)',
 'N',
 'Y',
 'N',
 'N',
 'N'
);


update xhb_ref_disposal_line t
set t.validation = 'V19'
where t.disposal_code = 'CUSMSEC'
and t.template_version = 4
and t.validation = 'V11'
and nvl(t.obs_ind, '?') <> 'Y'
and t.court_id = 81;



update xhb_ref_disposal_line t
set t.validation = 'V20'
where t.disposal_code = 'CUSMSEC'
and t.template_version = 4
and t.validation = 'V14'
and nvl(t.obs_ind, '?') <> 'Y'
and t.court_id = 81;


update xhb_ref_disposal_type t 
set t.template_version = 4
where
t.court_id = 81
and t.disposal_code = 'CUSMSEC';



--***********************************************

delete from  xhb_ref_disposal_line t
where t.disposal_code = 'CUSMCUR'
and t.template_version = 4
and t.court_id = 81;


insert into xhb_ref_disposal_line (
 court_id, 
 disposal_code, 
 template_version, 
 dil_seq_no,
 data, 
 input_flag,
 screen_print,
 form_print,
 dbdestin,
 prompt,
 format,
 mandatory, 
 dbsource,
 validation,
 multiple_choice,
 mcgroup1,
 mcgroup2,
 char_max,
 conc_flag,
 line_insert
)
select
 t.court_id, 
 t.disposal_code, 
 4, 
 t.dil_seq_no,
 t.data, 
 t.input_flag,
 t.screen_print,
 t.form_print,
 t.dbdestin,
 t.prompt,
 t.format,
 t.mandatory, 
 t.dbsource,
 t.validation,
 t.multiple_choice,
 t.mcgroup1,
 t.mcgroup2,
 t.char_max,
 t.conc_flag,
 t.line_insert
from xhb_ref_disposal_line t 
where t.disposal_code = 'CUSMCUR'
and t.template_version = 3
and nvl(t.obs_ind, '?') <> 'Y'
and t.court_id = 81;


insert into xhb_ref_disposal_line (
 court_id, 
 disposal_code, 
 template_version, 
 dil_seq_no,
 data, 
 input_flag,
 screen_print,
 form_print,
 mandatory, 
 conc_flag
) values (
 81,
 'CUSMCUR',
 4,
 65,
 '(must be weeks or months)',
 'N',
 'Y',
 'N',
 'N',
 'N'
);



update xhb_ref_disposal_line t
set t.validation = 'V19'
where t.disposal_code = 'CUSMCUR'
and t.template_version = 4
and t.validation = 'V11'
and nvl(t.obs_ind, '?') <> 'Y'
and t.court_id = 81;



update xhb_ref_disposal_line t
set t.validation = 'V20'
where t.disposal_code = 'CUSMCUR'
and t.template_version = 4
and t.validation = 'V14'
and nvl(t.obs_ind, '?') <> 'Y'
and t.court_id = 81;


update xhb_ref_disposal_type t 
set t.template_version = 4
where
t.court_id = 81
and t.disposal_code = 'CUSMCUR';


commit;











