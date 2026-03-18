delete from XHB_Breach where charge_id in (select charge_id from xhb_charge where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'));

delete from XHB_Case_Prosecutor_Agency where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF');

delete from XHB_Plea where def_on_charge_or_offence = 'O' and defendant_on_offence_id in (select defendant_on_offence_id from xhb_defendant_on_offence where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF')));

delete from XHB_Plea where def_on_charge_or_offence = 'C' and defendant_charge_id in (select defendant_charge_id from xhb_defendant_charge where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF')));

delete from XHB_verdict where def_on_charge_or_offence = 'O' and defendant_on_offence_id in (select defendant_on_offence_id from xhb_defendant_on_offence where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF')));

delete from XHB_verdict where def_on_charge_or_offence = 'C' and defendant_charge_id in (select defendant_charge_id from xhb_defendant_charge where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF')));

delete from XHB_verdict where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF');

delete from XHB_verdict where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'));

delete from XHB_Disposal_Line where disposal2_id in (select disposal2_id from XHB_Disposal2 where defendant_on_offence_id in (select defendant_on_offence_id from xhb_defendant_on_offence where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'))));

delete from XHB_Disposal_Line where disposal2_id in (select disposal2_id from XHB_Disposal2 where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF')));

delete from XHB_Disposal2 where defendant_on_offence_id in (select defendant_on_offence_id from xhb_defendant_on_offence where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF')));

delete from XHB_COURT_LOG_ENTRY where defendant_on_offence_id in (select defendant_on_offence_id from xhb_defendant_on_offence where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF')));


delete from XHB_Disposal2 where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'));

delete from XHB_Defendant_on_Offence where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'));

delete from XHB_Offence where charge_id in (select charge_id from xhb_charge where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'));

delete from XHB_JOINDER_CHARGE where charge_id in (select charge_id from xhb_charge where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'));

delete from XHB_Defendant_Charge where charge_id in (select charge_id from xhb_charge where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'));

delete from XHB_Charge where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF');

delete from XHB_Case_App_Reason where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF');

delete from XHB_DEF_ON_CASE_REF_SOL_FIRM where defendant_on_case_id in (select defendant_on_case_id from xhb_defendant_on_case where case_id in (select case_id from xhb_case where charge_import_indicator = 'LF'));
