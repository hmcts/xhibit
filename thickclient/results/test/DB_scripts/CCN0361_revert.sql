
--
-- Script to delete the Disposals reference data for the CCN0361 changes.
--


delete from  xhb_ref_disposal_line t
where t.disposal_code = 'CUSMSEC'
and t.template_version = 4
and t.court_id = 81;


-- set the template version back to 3
update xhb_ref_disposal_type t 
set t.template_version = 3
where
t.court_id = 81
and t.disposal_code = 'CUSMSEC';



--***********************************************

delete from  xhb_ref_disposal_line t
where t.disposal_code = 'CUSMCUR'
and t.template_version = 4
and t.court_id = 81;


-- set the template version back to 3
update xhb_ref_disposal_type t 
set t.template_version = 3
where
t.court_id = 81
and t.disposal_code = 'CUSMCUR';


commit;













