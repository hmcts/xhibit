-- Legacy for CTC but no harm in other envs as no data will be deleted
@remove_existing_instances_of_new_ref_disposal_lines.sql;

-- Add new stored procedures
@insert_ref_disp_line_disdisc.sql;
@insert_ref_disp_line_disoblg.sql;
@insert_ref_disp_line_distot.sql;

-- Fix spelling mistakes from previous versions of this release
update xhb_ref_disposal_type set title='Interim Disqualification' where title='Interim Disquaification';
update xhb_ref_disposal_type set title='Disqualified under Totting Up' where title='Disquaified under Totting Up';
update xhb_ref_disposal_type set title='Discretionary Disqualification' where title='Discretionary Disquaification';
update xhb_ref_disposal_type set title='Obligatory Disqualification' where title='Obligatory Disquaification';

commit;

-- Now execute the stored procedures to add the new disposal line data
execute insert_ref_disp_line_disoblg(p_court_id => NULL);
execute insert_ref_disp_line_disdisc(p_court_id => NULL);
execute insert_ref_disp_line_distot(p_court_id => NULL);

commit;