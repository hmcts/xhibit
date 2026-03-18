rem Module Name     : dcr_3260_l
rem Filename        : dcr_3260_l.sql
rem Author          : Peter Berrell
rem Date            : 15 May 2011
rem Version         : 01
rem
rem Purpose
rem =======
rem
rem Change Control
rem ==============
rem Version     Date       Description      Control No.       Author
rem -------     ----       -----------      -----------       ------
rem 1.0        30/03/11    New script       RFC2871           Peter Berrell
rem 1.1        15/06/11    Post test defect 6682 fix.
rem 1.2        28/06/11    Add Replaced? flag line
rem 1.3        14/07/11    change multiple_choice and mcgroup1 flag values
rem

/* ---------------------------------*/
/* disposal_line entries for DDSPAS */
/* ---------------------------------*/

DELETE FROM disposal_line
WHERE disposal_code IN ('DDSPAS', 'DISREM')
AND dis_id = 0
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 20, 0, 0, 0, NULL, 'Y', 'Y', NULL, 'D2', 'Date of Resu
lt',
             NULL, 'Y', 'UD1', 'V6', NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 40, 1, 0, 0, 'Disqualification imposed at', NULL, 'Y',
 'Y', NULL, NULL,
             NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 60, 0, 0, 0, NULL, 'Y', 'Y', 'Y', NULL, 'enter locatio
n',
             NULL, 'Y', NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 80, 0, 0, 0, 'Magistrates'' Court', NULL, 'Y', 'Y', NU
LL, NULL,
             NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 100, 2, 0, 0, 'On', NULL, 'Y', 'Y', NULL, NULL,
             NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 120, 0, 0, 0, NULL, 'Y', 'Y', 'Y', 'D2', '   enter dat
e:',
             'F1', 'Y', NULL, 'V6', NULL,
             NULL, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 140, 1, 0, 0, 'was suspended pending appeal.', NULL, '
Y', 'Y', NULL, NULL,
             NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
           ) VALUES (
             'DDSPAS', 1, 160, 0, 0, 0, 'N', 'Y', 'Y', NULL, 'D11', '     Replac
ed?', NULL, 'Y', NULL,
             'V2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, N
ULL, NULL
           )
/


INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 180, 0, 0, 0, '(Press CREATE RECORD to insert addition
al text)', NULL, 'Y',
             NULL, NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', NU
LL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DDSPAS', 1, 200, 0, 0, 0, '=======================================
===================',
             NULL, 'Y', NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, N
ULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 20, 0, 0, 0, NULL, 'Y', 'Y', NULL, 'D2', 'Date of Resu
lt',
             NULL, 'Y', 'UD1', 'V6', NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 40, 1, 0, 0, 'Disqualification imposed at', NULL, 'Y',
 'Y', NULL, NULL,
             NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 60, 0, 0, 0, NULL, 'Y', 'Y', 'Y', NULL, 'enter locatio
n',
             NULL, 'Y', NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 80, 0, 0, 0, 'Magistrates'' Court', NULL, 'Y', 'Y', NU
LL, NULL,
             NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 100, 2, 0, 0, 'On', NULL, 'Y', 'Y', NULL, NULL,
             NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 120, 0, 0, 0, NULL, 'Y', 'Y', 'Y', 'D2', '   enter dat
e:',
             'F1', 'Y', NULL, 'V6', NULL,
             NULL, NULL, NULL, NULL, 'Y', NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 140, 1, 0, 0, 'was removed.', NULL, 'Y', 'Y', NULL, NU
LL,
             NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 160, 0, 0, 0, 'N', 'Y', 'Y', NULL, 'D11', '     Replac
ed?', NULL, 'Y', NULL,
             'V2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, N
ULL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 180, 0, 0, 0, '(Press CREATE RECORD to insert addition
al text)', NULL, 'Y',
             NULL, NULL, NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', NU
LL, NULL
            )
/

INSERT INTO disposal_line (
             disposal_code, template_version, dil_seq_no, llf, tlf, dis_id,
             data, input_flag, screen_print, form_print, dbdestin, prompt,
             format, mandatory, dbsource, validation, multiple_choice,
             mcgroup1, mcgroup2, tpc, char_max, conc_flag, del_data, del_g1,
             del_g2, line_insert, line_delete, lpc
            ) VALUES (
             'DISREM', 1, 200, 0, 0, 0, '=======================================
===================',
             NULL, 'Y', NULL, NULL, NULL, NULL,
             NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, N
ULL, NULL, NULL, NULL
            )
/

/* ----------------------*/
/* disposal_type entries */
/* ----------------------*/

DELETE FROM disposal_type
WHERE disposal_code IN ('DDSPAS', 'DISREM')
/

INSERT INTO disposal_type
            (template_version, disposal_code, menu_group, mo_category, obs_ind,
             title, disp_title2,
             category, footer_type, setup_module, header_type, report_id,
             line_avail, disp_title1, dvlc_code, dmi_ind, valid, comb_order
            )
     VALUES (1, 'DDSPAS', 'RS', NULL, NULL,
             'Driving Disqualification suspended pending appeal subsequent to im
position', NULL,
             'O', NULL, NULL, NULL, NULL, 12, NULL, NULL, 'NCUS', 'Y', 0
            )
/

INSERT INTO disposal_type (
            template_version, disposal_code, menu_group, mo_category, obs_ind,
            title, disp_title2,
            category, footer_type, setup_module, header_type, report_id,
            line_avail, disp_title1, dvlc_code, dmi_ind, valid, comb_order
            )
     VALUES (1, 'DISREM', 'RS', NULL, NULL,
            'Disqualification from driving removed', NULL,
            'O', NULL, NULL, NULL, NULL, 12, NULL, NULL, 'NCUS', 'Y', 0
            )
/
/* ----------------------*/
/* disposal_menu entries */
/* ----------------------*/

DELETE FROM disposal_menu
WHERE disposal_code IN ('DDSPAS', 'DISREM')
/

INSERT INTO disposal_menu (
            menu_item_id, seq_no, menu_group, abbrev, parent, disposal_code, tit
le
            )
     VALUES (2664, 7, 'RS', 'DDSPAS', 501, 'DDSPAS',
            'Driving Disqualification suspended pending appeal subsequent to imp
osition'
            )
/
INSERT INTO disposal_menu (
            menu_item_id, seq_no, menu_group, abbrev, parent, disposal_code, tit
le
            )
     VALUES (2665, 8, 'RS', 'DISREM', 501, 'DISREM', 'Disqualification from driv
ing removed'
            )
/

COMMIT
/
