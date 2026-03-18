@ECHO OFF

SET SOURCE_FOLDER="D:\projects\XHIBIT"

ECHO.
ECHO About to create a directory called %1 under %SOURCE_FOLDER% Dev\Builds, are you sure?
ECHO.

:LOOP

SET Choice=
SET /P Choice=Y or N: 

IF NOT '%Choice%'=='' SET Choice=%Choice:~0,1%
ECHO.

IF /I '%Choice%'=='Y' GOTO LabelYes
IF /I '%Choice%'=='N' GOTO LabelNo

ECHO "%Choice%" is not valid. Please try again.
ECHO.
GOTO LOOP

:LabelYes

mkdir %1
cd %1
mkdir "Pre Prod"
mkdir "Prod"
mkdir "System Test"

copy "%SOURCE_FOLDER%\database\Mercator\grants.sql" ".\System Test\01_grants.sql"
copy "%SOURCE_FOLDER%\database\Mercator\grants.sql" ".\Pre Prod\01_grants.sql"
copy "%SOURCE_FOLDER%\database\Mercator\grants.sql" ".\Prod\01_grants.sql"

copy "%SOURCE_FOLDER%\database\Mercator\dropm4ora8.sql" ".\System Test\02_dropm4ora8.sql"
copy "%SOURCE_FOLDER%\database\Mercator\dropm4ora8.sql" ".\Pre Prod\02_dropm4ora8.sql"
copy "%SOURCE_FOLDER%\database\Mercator\dropm4ora8.sql" ".\Prod\02_dropm4ora8.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Sequences.sql" ".\System Test\03a_XHIBIT2_Create_Sequences.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Sequences.sql" ".\Pre Prod\03a_XHIBIT2_Create_Sequences.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Sequences.sql" ".\Prod\03a_XHIBIT2_Create_Sequences.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Tables_SysTest.sql" ".\System Test\03b_XHIBIT2_Create_Tables_SysTest.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Tables_PreProd.sql" ".\Pre Prod\03b_XHIBIT2_Create_Tables_PreProd.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Tables_Prod.sql" ".\Prod\03b_XHIBIT2_Create_Tables_Prod.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Indexes_SysTest.sql" ".\System Test\03c_XHIBIT2_Create_Indexes_SysTest.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Indexes_PreProd.sql" ".\Pre Prod\03c_XHIBIT2_Create_Indexes_PreProd.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Indexes_Prod.sql" ".\Prod\03c_XHIBIT2_Create_Indexes_Prod.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Audit_Tables.sql" ".\System Test\03d_XHIBIT2_Create_Audit_Tables.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Audit_Tables.sql" ".\Pre Prod\03d_XHIBIT2_Create_Audit_Tables.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Audit_Tables.sql" ".\Prod\03d_XHIBIT2_Create_Audit_Tables.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Audit_Tables_Modify.sql" ".\System Test\03e_XHIBIT2_Audit_Tables_Modify.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Audit_Tables_Modify.sql" ".\Pre Prod\03e_XHIBIT2_Audit_Tables_Modify.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Audit_Tables_Modify.sql" ".\Prod\03e_XHIBIT2_Audit_Tables_Modify.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Foreign_Keys.sql" ".\System Test\03f_XHIBIT2_Create_Foreign_Keys.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Foreign_Keys.sql" ".\Pre Prod\03f_XHIBIT2_Create_Foreign_Keys.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Foreign_Keys.sql" ".\Prod\03f_XHIBIT2_Create_Foreign_Keys.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Packages.sql" ".\System Test\03g_XHIBIT2_Create_Packages.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Packages.sql" ".\Pre Prod\03g_XHIBIT2_Create_Packages.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Packages.sql" ".\Prod\03g_XHIBIT2_Create_Packages.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Triggers.sql" ".\System Test\03h_XHIBIT2_Create_Triggers.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Triggers.sql" ".\Pre Prod\03h_XHIBIT2_Create_Triggers.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Create_Triggers.sql" ".\Prod\03h_XHIBIT2_Create_Triggers.sql"

copy "%SOURCE_FOLDER%\database\XHIBIT2_Misc_Inserts.sql" ".\System Test\03i_XHIBIT2_Misc_Inserts.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Misc_Inserts.sql" ".\Pre Prod\03i_XHIBIT2_Misc_Inserts.sql"
copy "%SOURCE_FOLDER%\database\XHIBIT2_Misc_Inserts.sql" ".\Prod\03i_XHIBIT2_Misc_Inserts.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtSiteRoom_SysTest.sql" ".\System Test\04_XHIBIT2_Standing_CourtSiteRoom_SysTest.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtSiteRoom_PreProd.sql" ".\Pre Prod\04_XHIBIT2_Standing_CourtSiteRoom_PreProd.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtSiteRoom_Prod.sql" ".\Prod\04_XHIBIT2_Standing_CourtSiteRoom_Prod.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtLogStaticData.sql" ".\System Test\05_XHIBIT2_Standing_CourtLogStaticData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtLogStaticData.sql" ".\Pre Prod\05_XHIBIT2_Standing_CourtLogStaticData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CourtLogStaticData.sql" ".\Prod\05_XHIBIT2_Standing_CourtLogStaticData.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicNotice_Ref.sql" ".\System Test\06_XHIBIT2_Standing_PublicNotice_Ref.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicNotice_Ref.sql" ".\Pre Prod\06_XHIBIT2_Standing_PublicNotice_Ref.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicNotice_Ref.sql" ".\Prod\06_XHIBIT2_Standing_PublicNotice_Ref.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic.sql" ".\System Test\07_XHIBIT2_Standing_CRESTImportStatic.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic.sql" ".\Pre Prod\07_XHIBIT2_Standing_CRESTImportStatic.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CRESTImportStatic.sql" ".\Prod\07_XHIBIT2_Standing_CRESTImportStatic.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_HTTPRetryData.sql" ".\System Test\07a_XHIBIT2_Standing_HTTPRetryData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_HTTPRetryData.sql" ".\Pre Prod\07a_XHIBIT2_Standing_HTTPRetryData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_HTTPRetryData.sql" ".\Prod\07a_XHIBIT2_Standing_HTTPRetryData.sql"

copy "%SOURCE_FOLDER%\database\Mercator\m4ora8.sql" ".\System Test\08_m4ora8.sql"
copy "%SOURCE_FOLDER%\database\Mercator\m4ora8.sql" ".\Pre Prod\08_m4ora8.sql"
copy "%SOURCE_FOLDER%\database\Mercator\m4ora8.sql" ".\Prod\08_m4ora8.sql"

copy "%SOURCE_FOLDER%\database\Mercator\msp_record_events1.sql" ".\Pre Prod\09_msp_record_events1.sql"
copy "%SOURCE_FOLDER%\database\Mercator\msp_record_events1.sql" ".\Prod\09_msp_record_events1.sql"
copy "%SOURCE_FOLDER%\database\Mercator\Merc_Standing_Data.sql" ".\System Test\09_Merc_Standing_Data.sql"

copy "%SOURCE_FOLDER%\database\Mercator\Merc_Standing_Data.sql" ".\Pre Prod\10_Merc_Standing_Data.sql"
copy "%SOURCE_FOLDER%\database\Mercator\Merc_Standing_Data.sql" ".\Prod\10_Merc_Standing_Data.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplayConfigStaticData.sql" ".\System Test\10_XHIBIT2_Standing_PublicDisplayConfigStaticData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplayConfigStaticData.sql" ".\Pre Prod\11_XHIBIT2_Standing_PublicDisplayConfigStaticData.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplayConfigStaticData.sql" ".\Prod\11_XHIBIT2_Standing_PublicDisplayConfigStaticData.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CC_Info.sql" ".\System Test\11_XHIBIT2_Standing_CC_Info.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CC_Info.sql" ".\Pre Prod\12_XHIBIT2_Standing_CC_Info.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_CC_Info.sql" ".\Prod\12_XHIBIT2_Standing_CC_Info.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Status_Data.sql" ".\System Test\12_XHIBIT2_Standing_Orders_Status_Data.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Status_Data.sql" ".\Pre Prod\13_XHIBIT2_Standing_Orders_Status_Data.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Orders_Status_Data.sql" ".\Prod\13_XHIBIT2_Standing_Orders_Status_Data.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Skeleton_Delivery_Status.sql" ".\System Test\13_XHIBIT2_Standing_Skeleton_Delivery_Status.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Skeleton_Delivery_Status.sql" ".\Pre Prod\14_XHIBIT2_Standing_Skeleton_Delivery_Status.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Skeleton_Delivery_Status.sql" ".\Prod\14_XHIBIT2_Standing_Skeleton_Delivery_Status.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Document_Reply.sql" ".\System Test\14_XHIBIT2_Standing_Document_Reply.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Document_Reply.sql" ".\Pre Prod\15_XHIBIT2_Standing_Document_Reply.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_Document_Reply.sql" ".\Prod\15_XHIBIT2_Standing_Document_Reply.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay.sql" ".\System Test\15_XHIBIT2_Standing_PublicDisplay.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay.sql" ".\Pre Prod\16_XHIBIT2_Standing_PublicDisplay.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay.sql" ".\Prod\16_XHIBIT2_Standing_PublicDisplay.sql"

copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay_Court.sql" ".\System Test\16_XHIBIT2_Standing_PublicDisplay_Court.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay_Court.sql" ".\Pre Prod\17_XHIBIT2_Standing_PublicDisplay_Court.sql"
copy "%SOURCE_FOLDER%\database\StandingData\XHIBIT2_Standing_PublicDisplay_Court.sql" ".\Prod\17_XHIBIT2_Standing_PublicDisplay_Court.sql"

copy "%SOURCE_FOLDER%\database\views\sh_view.sql" ".\System Test\17_sh_view.sql"
copy "%SOURCE_FOLDER%\database\views\sh_view.sql" ".\Pre Prod\18_sh_view.sql"
copy "%SOURCE_FOLDER%\database\views\sh_view.sql" ".\Prod\18_sh_view.sql"

copy "%SOURCE_FOLDER%\database\views\shdid_view.sql" ".\System Test\18_shdid_view.sql"
copy "%SOURCE_FOLDER%\database\views\shdid_view.sql" ".\Pre Prod\19_shdid_view.sql"
copy "%SOURCE_FOLDER%\database\views\shdid_view.sql" ".\Prod\19_shdid_view.sql"

copy "%SOURCE_FOLDER%\database\views\daily_list_view.sql" ".\System Test\18a_daily_list_view.sql"
copy "%SOURCE_FOLDER%\database\views\daily_list_view.sql" ".\Pre Prod\19a_daily_list_view.sql"
copy "%SOURCE_FOLDER%\database\views\daily_list_view.sql" ".\Prod\19a_daily_list_view.sql"

copy "%SOURCE_FOLDER%\database\procedures\counsel_h.sql" ".\System Test\19_counsel_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\counsel_h.sql" ".\Pre Prod\20_counsel_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\counsel_h.sql" ".\Prod\20_counsel_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\counsel_b.sql" ".\System Test\20_counsel_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\counsel_b.sql" ".\Pre Prod\21_counsel_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\counsel_b.sql" ".\Prod\21_counsel_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_terminal_pkg_h.sql" ".\System Test\20a_xhb_terminal_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_terminal_pkg_h.sql" ".\Pre Prod\21a_xhb_terminal_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_terminal_pkg_h.sql" ".\Prod\21a_xhb_terminal_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_terminal_pkg_b.sql" ".\System Test\20a_xhb_terminal_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_terminal_pkg_b.sql" ".\Pre Prod\21a_xhb_terminal_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_terminal_pkg_b.sql" ".\Prod\21a_xhb_terminal_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_orders_pkg_h.sql" ".\System Test\20b_xhb_orders_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_orders_pkg_h.sql" ".\Pre Prod\21b_xhb_orders_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_orders_pkg_h.sql" ".\Prod\21b_xhb_orders_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_orders_pkg_b.sql" ".\System Test\21b_xhb_orders_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_orders_pkg_b.sql" ".\Pre Prod\22b_xhb_orders_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_orders_pkg_b.sql" ".\Prod\22b_xhb_orders_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_list_distribution_pkg_h.sql" ".\System Test\21_xhb_list_distribution_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_list_distribution_pkg_h.sql" ".\Pre Prod\22_xhb_list_distribution_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_list_distribution_pkg_h.sql" ".\Prod\22_xhb_list_distribution_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_list_distribution_pkg_b.sql" ".\System Test\22_xhb_list_distribution_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_list_distribution_pkg_b.sql" ".\Pre Prod\23_xhb_list_distribution_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_list_distribution_pkg_b.sql" ".\Prod\23_xhb_list_distribution_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_search_pkg_h.sql" ".\System Test\23_xhb_search_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_search_pkg_h.sql" ".\Pre Prod\24_xhb_search_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_search_pkg_h.sql" ".\Prod\24_xhb_search_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_search_pkg_b.sql" ".\System Test\24_xhb_search_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_search_pkg_b.sql" ".\Pre Prod\25_xhb_search_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_search_pkg_b.sql" ".\Prod\25_xhb_search_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_view_schedule_pkg_h.sql" ".\System Test\25_xhb_view_schedule_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_view_schedule_pkg_h.sql" ".\Pre Prod\26_xhb_view_schedule_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_view_schedule_pkg_h.sql" ".\Prod\26_xhb_view_schedule_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_view_schedule_pkg_b.sql" ".\System Test\26_xhb_view_schedule_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_view_schedule_pkg_b.sql" ".\Pre Prod\27_xhb_view_schedule_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_view_schedule_pkg_b.sql" ".\Prod\27_xhb_view_schedule_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_court_log_pkg_h.sql" ".\System Test\27_xhb_court_log_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_court_log_pkg_h.sql" ".\Pre Prod\28_xhb_court_log_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_court_log_pkg_h.sql" ".\Prod\28_xhb_court_log_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_court_log_pkg_b.sql" ".\System Test\28_xhb_court_log_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_court_log_pkg_b.sql" ".\Pre Prod\29_xhb_court_log_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_court_log_pkg_b.sql" ".\Prod\29_xhb_court_log_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_pkg_h.sql" ".\System Test\29_xhb_cr_live_status_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_pkg_h.sql" ".\Pre Prod\30_xhb_cr_live_status_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_pkg_h.sql" ".\Prod\30_xhb_cr_live_status_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_pkg_b.sql" ".\System Test\30_xhb_cr_live_status_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_pkg_b.sql" ".\Pre Prod\31_xhb_cr_live_status_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_pkg_b.sql" ".\Prod\31_xhb_cr_live_status_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_job.sql" ".\System Test\31_xhb_cr_live_status_job.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_job.sql" ".\Pre Prod\32_xhb_cr_live_status_job.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_cr_live_status_job.sql" ".\Prod\32_xhb_cr_live_status_job.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_ref_advocate_h.sql" ".\System Test\32_xhb_ref_advocate_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_ref_advocate_h.sql" ".\Pre Prod\33_xhb_ref_advocate_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_ref_advocate_h.sql" ".\Prod\33_xhb_ref_advocate_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_ref_advocate_b.sql" ".\System Test\33_xhb_ref_advocate_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_ref_advocate_b.sql" ".\Pre Prod\34_xhb_ref_advocate_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_ref_advocate_b.sql" ".\Prod\34_xhb_ref_advocate_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_public_display_pkg_h.sql" ".\System Test\34_xhb_public_display_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_public_display_pkg_h.sql" ".\Pre Prod\35_xhb_public_display_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_public_display_pkg_h.sql" ".\Prod\35_xhb_public_display_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_public_display_pkg_b.sql" ".\System Test\35_xhb_public_display_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_public_display_pkg_b.sql" ".\Pre Prod\36_xhb_public_display_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_public_display_pkg_b.sql" ".\Prod\36_xhb_public_display_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_psr_request_pkg_h.sql" ".\System Test\36_xhb_psr_request_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_psr_request_pkg_h.sql" ".\Pre Prod\37_xhb_psr_request_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_psr_request_pkg_h.sql" ".\Prod\37_xhb_psr_request_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_psr_request_pkg_b.sql" ".\System Test\37_xhb_psr_request_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_psr_request_pkg_b.sql" ".\Pre Prod\38_xhb_psr_request_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_psr_request_pkg_b.sql" ".\Prod\38_xhb_psr_request_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_post_merc_ref_data_pkg_h.sql" ".\System Test\38_xhb_post_merc_ref_data_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_post_merc_ref_data_pkg_h.sql" ".\Pre Prod\39_xhb_post_merc_ref_data_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_post_merc_ref_data_pkg_h.sql" ".\Prod\39_xhb_post_merc_ref_data_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_post_merc_ref_data_pkg_b.sql" ".\System Test\39_xhb_post_merc_ref_data_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_post_merc_ref_data_pkg_b.sql" ".\Pre Prod\40_xhb_post_merc_ref_data_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_post_merc_ref_data_pkg_b.sql" ".\Prod\40_xhb_post_merc_ref_data_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_daily_list_pkg_h.sql" ".\System Test\40_xhb_daily_list_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_daily_list_pkg_h.sql" ".\Pre Prod\41_xhb_daily_list_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_daily_list_pkg_h.sql" ".\Prod\41_xhb_daily_list_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\procedures\xhb_daily_list_pkg_b.sql" ".\System Test\41_xhb_daily_list_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_daily_list_pkg_b.sql" ".\Pre Prod\42_xhb_daily_list_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\procedures\xhb_daily_list_pkg_b.sql" ".\Prod\42_xhb_daily_list_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_pkg_h.sql" ".\System Test\cji_delete_documents_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_pkg_h.sql" ".\Pre Prod\cji_delete_documents_pkg_h.sql"
copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_pkg_h.sql" ".\Prod\cji_delete_documents_pkg_h.sql"

copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_pkg_b.sql" ".\System Test\cji_delete_documents_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_pkg_b.sql" ".\Pre Prod\cji_delete_documents_pkg_b.sql"
copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_pkg_b.sql" ".\Prod\cji_delete_documents_pkg_b.sql"

copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_job.sql" ".\System Test\cji_delete_documents_job.sql"
copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_job.sql" ".\Pre Prod\cji_delete_documents_job.sql"
copy "%SOURCE_FOLDER%\database\CJIT\cji_delete_documents_job.sql" ".\Prod\cji_delete_documents_job.sql"

copy "%SOURCE_FOLDER%\database\temporary_scripts\End_hearing_script.sql" ".\System Test\End_hearing_script.sql"
copy "%SOURCE_FOLDER%\database\temporary_scripts\End_hearing_script.sql" ".\Pre Prod\End_hearing_script.sql"
copy "%SOURCE_FOLDER%\database\temporary_scripts\End_hearing_script.sql" ".\Prod\End_hearing_script.sql"

copy "%SOURCE_FOLDER%\database\Mercator\Merc_Tidy_Mercator_Triggers.sql" ".\System Test\Merc_Tidy_Mercator_Triggers.sql"
copy "%SOURCE_FOLDER%\database\Mercator\Merc_Tidy_Mercator_Triggers.sql" ".\Pre Prod\Merc_Tidy_Mercator_Triggers.sql"
copy "%SOURCE_FOLDER%\database\Mercator\Merc_Tidy_Mercator_Triggers.sql" ".\Prod\Merc_Tidy_Mercator_Triggers.sql"

ECHO.
ECHO PLEASE ENSURE THAT THE CORRESPONDING PATCH FILES ARE COPIED TO THE BUILD DIRECTORY
ECHO.

cd ..

:LabelNo
ECHO.
