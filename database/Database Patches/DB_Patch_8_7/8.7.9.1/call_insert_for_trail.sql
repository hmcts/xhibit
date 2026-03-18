@insert_ref_disp_line_trail.sql;

execute insert_ref_disp_line_trail('EXCURSS',380,400,1);
execute insert_ref_disp_line_trail('EXCURCS',380,400,1);
execute insert_ref_disp_line_trail('CURFSS',260,280,1);
execute insert_ref_disp_line_trail('CURFCS',260,280,1);
execute insert_ref_disp_line_trail('EXCARSS',260,280,1);
execute insert_ref_disp_line_trail('EXCARCS',260,280,1);
execute insert_ref_disp_line_trail('COWEM',120,140,3);
execute insert_ref_disp_line_trail('EXMON',220,240,2);
execute insert_ref_disp_line_trail('DTO',380,400,3);

drop procedure insert_ref_disp_line_trail;

commit;