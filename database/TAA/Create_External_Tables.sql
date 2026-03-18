/*
CREATE TABLESPACE "XHIBIT_BASELINE" LOGGING 
DATAFILE 'D:\APPS\ORACLE\ORADATA\ORA9DEV\XHIBIT_BASELINE.dbf'
SIZE 50M EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;
*/

CREATE TABLE EXT_XHB_CASE TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- start with the de-normalised joins...
       xrc.court_short_name,
       -- the rest of the data 
       xc.CASE_ID,
       xc.CASE_NUMBER,
       xc.CASE_TYPE,
       xc.MAG_CONVICTION_DATE,
       xc.CASE_SUB_TYPE,
       xc.CASE_TITLE,
       xc.CASE_DESCRIPTION,
       xc.LINKED_CASE_ID,
       xc.BAIL_MAG_CODE,
       xc.CHARGE_IMPORT_INDICATOR,
       xc.SEVERED_IND,
       xc.INDICT_RESP,
       xc.DATE_IND_REC,
       xc.PROS_AGENCY_REFERENCE,
       xc.CASE_CLASS,
       xc.JUDGE_REASON_FOR_APPEAL,
       xc.RESULTS_VERIFIED,
       xc.LENGTH_TAPE,
       xc.NO_PAGE_PROS_EVIDENCE,
       xc.NO_PROS_WITNESS,
       xc.EST_PDH_TRIAL_LENGTH,
       xc.INDICTMENT_INFO_1,
       xc.INDICTMENT_INFO_2,
       xc.INDICTMENT_INFO_3,
       xc.INDICTMENT_INFO_4,
       xc.INDICTMENT_INFO_5,
       xc.INDICTMENT_INFO_6,
       xc.POLICE_OFFICER_ATTENDING,
       xc.CPS_CASE_WORKER,
       xc.EXPORT_CHARGES,
       xc.IND_CHANGE_STATUS,
       xc.MAGISTRATES_CASE_REF,
       xc.CLASS_CODE,
       xc.OFFENCE_GROUP_UPDATE,
       xc.CCC_TRANS_TO_REF_COURT_ID,
       xc.RECEIPT_TYPE
FROM   xhibit.XHB_CASE xc,
       xhibit.XHB_REF_COURT xrc
WHERE  xc.ref_court_id = xrc.ref_court_id(+);

CREATE TABLE ext_xhb_hearing TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- start with the de-normalised joins...
       xrht.hearing_type_code,
       -- the rest of the data
       HEARING_ID,
       CASE_ID,
       MP_HEARING_TYPE,
       LAST_CALCULATED_DURATION,
       HEARING_START_DATE,
       HEARING_END_DATE,
       LINKED_HEARING_ID
FROM   xhibit.XHB_HEARING xh,
       xhibit.XHB_REF_HEARING_TYPE xrht
WHERE  xh.ref_hearing_type_id = xrht.ref_hearing_type_id;

CREATE TABLE ext_xhb_scheduled_hearing TABLESPACE "XHIBIT_BASELINE" AS
SELECT SCHEDULED_HEARING_ID,
       SEQUENCE_NO,
       NOT_BEFORE_TIME,
       ORIGINAL_TIME,
       LISTING_NOTE,
       HEARING_PROGRESS,
       SITTING_ID,
       HEARING_ID,
       MOVED_FROM,
       LINKED_SH_ID,
       END_TIME,
       START_TIME,
       DATE_OF_HEARING,
       IS_CASE_ACTIVE,
       MOVED_FROM_COURT_ROOM_ID
FROM   xhibit.XHB_SCHEDULED_HEARING;

CREATE TABLE ext_xhb_sitting TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- The denormalised data...
       xrj1.crest_justice_id AS crest_justice1_id,
       xrj2.crest_justice_id AS crest_justice2_id,
       xrj3.crest_justice_id AS crest_justice3_id,
       xrj4.crest_justice_id AS crest_justice4_id,
       xrj.crest_judge_id,
       xcr.crest_court_room_no,
       -- The other data...
       xs.SITTING_ID,
       xs.SITTING_SEQUENCE_NO,
       xs.IS_SITTING_JUDGE,
       xs.SITTING_TIME,
       xs.SITTING_NOTE,
       xs.IS_FLOATING,
       xs.LIST_ID,
       xs.JUSTICENAME4,
       xs.JUSTICENAME3,
       xs.JUSTICENAME2,
       xs.JUSTICENAME1
FROM   xhibit.XHB_SITTING xs,
       xhibit.XHB_REF_JUSTICE xrj1,
       xhibit.XHB_REF_JUSTICE xrj2,
       xhibit.XHB_REF_JUSTICE xrj3,
       xhibit.XHB_REF_JUSTICE xrj4,
       xhibit.XHB_REF_JUDGE xrj,
       xhibit.XHB_COURT_ROOM xcr
WHERE  xs.ref_justice1_id = xrj1.ref_justice_id(+)
AND    xs.ref_justice2_id = xrj2.ref_justice_id(+)
AND    xs.ref_justice3_id = xrj3.ref_justice_id(+)
AND    xs.ref_justice4_id = xrj4.ref_justice_id(+)
AND    xs.ref_judge_id = xrj.ref_judge_id(+)
AND    xs.court_room_id = xcr.court_room_id;

CREATE TABLE ext_xhb_hearing_list TABLESPACE "XHIBIT_BASELINE" AS
SELECT LIST_ID, 
       LIST_TYPE, 
       START_DATE, 
       END_DATE, 
       STATUS, 
       EDITION_NO, 
       PUBLISHED_TIME, 
       PRINT_REFERENCE, 
       CREST_LIST_ID, 
       LIST_COURT_TYPE 
FROM   xhibit.XHB_HEARING_LIST;

CREATE TABLE ext_xhb_address TABLESPACE "XHIBIT_BASELINE" AS
SELECT ADDRESS_ID,
       ADDRESS_1,
       ADDRESS_2,
       ADDRESS_3,
       ADDRESS_4,
       TOWN,
       COUNTY,
       POSTCODE,
       COUNTRY
FROM   xhibit.XHB_ADDRESS;

CREATE TABLE ext_xhb_defendant TABLESPACE "XHIBIT_BASELINE" AS
SELECT DEFENDANT_ID,
       CREST_DEFENDANT_ID,
       FIRST_NAME,
       MIDDLE_NAME,
       SURNAME,
       INITIALS,
       DATE_OF_BIRTH,
       GENDER,
       LAST_CONVICTION_DATE,
       IS_COMPANY,
       ADDRESS_ID,
       COURT_ID,
       PRISON_ID
FROM   xhibit.XHB_DEFENDANT;

CREATE TABLE ext_xhb_charge TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- The denormalised data...
       xrsc.code,
       xrsc.code_type,
       -- The other data...
       xc.CHARGE_ID,
       xc.CHARGE_TYPE,
       xc.PROS_PAPER_SERVED_DATE,
       xc.CREST_CHARGE_ID,
       xc.CREST_CHARGE_SEQ_NO,
       xc.CASE_ID,
       xc.OBS_IND,
       xc.IND_SIGNED_DATE
FROM   xhibit.xhb_charge xc,
       xhibit.xhb_ref_system_code xrsc
WHERE  xc.ref_system_code_id = xrsc.ref_system_code_id(+);

CREATE TABLE ext_xhb_defendant_on_case TABLESPACE "XHIBIT_BASELINE" AS
SELECT DEFENDANT_ON_CASE_ID,
       NO_OF_TICS,
       FINAL_DRIVING_LICENCE_STATUS,
       PTIURN,
       IS_JUVENILE,
       IS_MASKED,
       MASKED_NAME,
       CASE_ID,
       DEFENDANT_ID,
       OBS_IND,
       RESULTS_VERIFIED,
       DEFENDANT_NUMBER,
       DATE_OF_COMMITTAL,
       PNC_ID,
       COLLECT_MAGISTRATE_COURT_ID
FROM   xhibit.xhb_defendant_on_case;

CREATE TABLE ext_xhb_defendant_charge TABLESPACE "XHIBIT_BASELINE" AS
SELECT DEFENDANT_CHARGE_ID,
       CHARGE_ID,
       DEFENDANT_ON_CASE_ID,
       OBS_IND
FROM   xhibit.XHB_DEFENDANT_CHARGE;

CREATE TABLE ext_xhb_offence TABLESPACE "XHIBIT_BASELINE" AS
SELECT xro.offence_code,
       xrsc.code,
       xrsc.code_type,
       xo.OFFENCE_ID,
       xo.CREST_OFFENCE_ID,
       xo.CREST_OFFENCE_SEQ_NO,
       xo.CREST_OFFENCE_FREETEXT,
       xo.MULTIPLE,
       xo.CREST_HOO_CLASS_FREETEXT,
       xo.CREST_HOO_SUBCLASS_FREETEXT,
       xo.CHARGE_ID,
       xo.OBS_IND
FROM   xhibit.XHB_OFFENCE xo,
       xhibit.XHB_REF_OFFENCE xro,
       xhibit.XHB_REF_SYSTEM_CODE xrsc
WHERE  xo.ref_offence_id = xro.ref_offence_id
AND    xo.ref_system_code_id = xrsc.ref_system_code_id(+);

CREATE TABLE ext_xhb_def_on_offence TABLESPACE "XHIBIT_BASELINE" AS
SELECT DEFENDANT_ON_OFFENCE_ID,
       APPEAL_AGAINST_TYPE,
       DEFENDANT_ON_CASE_ID,
       OFFENCE_ID,
       OBS_IND,
       IS_STAYED,
       CRN_ID,
       VCO_FLAG,
       VCO_DATE
FROM   xhibit.XHB_DEFENDANT_ON_OFFENCE;

CREATE TABLE ext_xhb_court_log_entry TABLESPACE "XHIBIT_BASELINE" AS
SELECT ENTRY_ID,
       CASE_ID,
       DATE_TIME,
       EVENT_DESC_ID,
       LOG_ENTRY_XML,
       DEFENDANT_ON_CASE_ID,
       DEFENDANT_ON_OFFENCE_ID,
       SCHEDULED_HEARING_ID
FROM   xhibit.XHB_COURT_LOG_ENTRY;

CREATE TABLE ext_xhb_sched_hearing_def TABLESPACE "XHIBIT_BASELINE" AS
SELECT sched_hear_def_id, scheduled_hearing_id, defendant_on_case_id
FROM   xhibit.XHB_SCHED_HEARING_DEFENDANT;

-- ANDY new ones...

CREATE TABLE EXT_XHB_BREACH TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- start with the de-normalised joins...
       xrc.court_short_name,
       -- the rest of the data 
       xb.BREACH_ID,
       xb.ORIGINAL_SENTENCE,
       xb.ORIGINAL_SENTENCE_DATE,
       xb.ORIGINAL_COURT_TYPE,
       xb.DATE_PUT,
       xb.BREACH_TYPE,
       xb.BRING_BACK,
       xb.CHARGE_ID,
       --xb.REF_COURT_ID,
       --xb.LAST_UPDATE_DATE,
       --xb.CREATION_DATE,
       --xb.CREATED_BY,
       --xb.LAST_UPDATED_BY,
       --xb.VERSION,
       xb.OBS_IND
FROM   xhb_breach xb,
       XHB_REF_COURT xrc
WHERE  xb.ref_court_id = xrc.ref_court_id;

/*
CREATE TABLE EXT_XHB_CONTACT_DETAIL TABLESPACE "XHIBIT_BASELINE" AS
SELECT CONTACT_ID,
       CONTACT_TYPE,
       CONTACT_VALUE,
       EMAIL_FORMAT,
       PAGER_NET,
       ADDRESS_ID
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION
FROM   xhb_contact_detail;
*/

CREATE TABLE EXT_XHB_DEFENDANT_REFERENCE TABLESPACE "XHIBIT_BASELINE" AS
SELECT DEF_REF_ID,
       REFERENCE_VALUE,
       REFERENCE_NAME,
       CATEGORY,
       DEFENDANT_ID
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION
FROM xhb_defendant_reference;

CREATE TABLE EXT_XHB_CASE_PROSECUTOR_AGENCY TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- start with the de-normalised joins...
       xrpa.crest_opposer_id,
       -- the rest of the data
       CASE_PROS_AGENCY_ID,
       PROSECUTOR_TYPE,
       CASE_ID
       --REF_PROSECUTOR_AGENCY_ID
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION
FROM   xhb_case_prosecutor_agency xcpa,
       xhb_ref_prosecutor_agency xrpa
WHERE  xrpa.ref_prosecutor_agency_id = xcpa.ref_prosecutor_agency_id;

CREATE TABLE EXT_XHB_PLEA TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- start with the de-normalised joins...
       xrsc.code,
       xrsc.code_type,
       xro.offence_code,
       -- the rest of the data 
       xp.PLEA_ID,
       --xp.REF_PLEA_ID,
       xp.OTHER_PLEA_TEXT,
       xp.BREACH_ADMITTED,
       --xp.ALT_REF_OFFENCE_ID,
       xp.ARRAIGNMENT_DATE,
       xp.DEF_ON_CHARGE_OR_OFFENCE,
       xp.OBS_IND,
       xp.DEFENDANT_CHARGE_ID,
       xp.DEFENDANT_ON_OFFENCE_ID,
       --xp.LAST_UPDATE_DATE,
       --xp.CREATION_DATE,
       --xp.CREATED_BY,
       --xp.LAST_UPDATED_BY,
       --xp.VERSION,
       xp.ALT_UNCODED_OFFENCE_DESC
FROM   XHB_PLEA xp,
       XHB_REF_SYSTEM_CODE xrsc,
       XHB_REF_OFFENCE xro
WHERE  xp.ref_plea_id = xrsc.ref_system_code_id(+)
AND    xp.alt_ref_offence_id = xro.ref_offence_id(+);

CREATE TABLE EXT_XHB_VERDICT TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- start with the de-normalised joins...
       xrsc.code,
       xrsc.code_type,
       xro.offence_code,
       xrap.app_result_code,
       -- the rest of the data 
       xv.VERDICT_ID,
       xv.OBS_IND,
       xv.DEF_ON_CHARGE_OR_OFFENCE,
       xv.JURORS_DISSENTING,
       xv.JURORS_ASSENTING,
       --xv.ALT_REF_OFFENCE_ID,
       xv.VERDICT_DATE,
       --xv.REF_VERDICT_ID,
       --VERSION,
       --LAST_UPDATED_BY,
       xv.OTHER_VERDICT_TEXT,
       --xv.CREATED_BY,
       --xv.CREATION_DATE,
       --xv.LAST_UPDATE_DATE,
       xv.DEFENDANT_ON_OFFENCE_ID,
       xv.DEFENDANT_CHARGE_ID,
       --xv.REF_APP_RESULT_ID,
       xv.CASE_ID,
       xv.APP_LESSER_OFF,
       xv.ALT_UNCODED_OFFENCE_DESC,
       xv.DISPOSAL2_ID,
       xv.DEFENDANT_ON_CASE_ID
FROM   XHB_VERDICT xv,
       XHB_REF_SYSTEM_CODE xrsc,
       XHB_REF_OFFENCE xro,
       XHB_REF_APP_RESULT xrap
WHERE  xv.ref_verdict_id = xrsc.ref_system_code_id(+)
AND    xv.alt_ref_offence_id = xro.ref_offence_id(+)
AND    xv.ref_app_result_id = xrap.ref_app_result_id(+);

CREATE TABLE EXT_XHB_DISPOSAL2 TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- start with the de-normalised joins...
       xrdt.template_version,
       xrdt.disposal_code,
       xrdt.menu_group,
       -- the rest of the data 
       xd2.DISPOSAL2_ID,
       --xd2.REF_DISPOSAL_TYPE_ID,
       xd2.DEFENDANT_ON_OFFENCE_ID,
       xd2.DEFENDANT_ON_CASE_ID,
       xd2.DIS_ID,
       xd2.COURT_TYPE,
       xd2.PSD_DISPOSAL2_ID,
       xd2.OBS_IND
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION
FROM   XHB_DISPOSAL2 xd2,
       XHB_REF_DISPOSAL_TYPE xrdt
WHERE  xd2.ref_disposal_type_id = xrdt.ref_disposal_type_id;

CREATE TABLE EXT_XHB_DISPOSAL_LINE TABLESPACE "XHIBIT_BASELINE" AS
SELECT -- start with the de-normalised joins...
       xrdl.disposal_code,
       xrdl.template_version,
       xrdl.dil_seq_no,
       -- the rest of the data 
       xdl.DISPOSAL_LINE_ID,
       --xdl.REF_DISPOSAL_LINE_ID,
       xdl.DISPOSAL2_ID,
       xdl.LINE_NUMBER,
       xdl.DATA,
       xdl.DEL_DATA,
       xdl.DEL_G1,
       xdl.DEL_G2,
       xdl.OBS_IND
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION
FROM   XHB_DISPOSAL_LINE xdl,
       XHB_REF_DISPOSAL_LINE xrdl
WHERE  xdl.ref_disposal_line_id = xrdl.ref_disposal_line_id;

CREATE TABLE EXT_XHB_CLOB TABLESPACE "XHIBIT_BASELINE" AS
SELECT CLOB_ID,
       CLOB_DATA
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION
FROM   XHB_CLOB
WHERE  CLOB_ID IN (SELECT XML_DOCUMENT_CLOB_ID FROM XHB_XML_DOCUMENT) ;

CREATE TABLE EXT_XHB_XML_DOCUMENT TABLESPACE "XHIBIT_BASELINE" AS
SELECT XML_DOCUMENT_ID,
       DATE_CREATED,
       DOCUMENT_TITLE,
       STATUS,
       EXPIRY_DATE,
       DOCUMENT_TYPE,
       XML_DOCUMENT_CLOB_ID,
       LANGUAGE,
       COUNTRY
FROM   XHB_XML_DOCUMENT;

CREATE TABLE EXT_XHB_WLL_CONTROL TABLESPACE "XHIBIT_BASELINE" AS
SELECT WLL_CONTROL_ID,
       STATUS,
       EXPIRY_DATE,
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION,
       XML_DOCUMENT_ID
FROM   XHB_WLL_CONTROL;

CREATE TABLE EXT_XHB_WLL_DOCUMENT TABLESPACE "XHIBIT_BASELINE" AS
SELECT WLL_DOCUMENT_ID,
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION,
       WLL_RECIPIENT_ID,
       WLL_CONTROL_ID,
       XML_DOCUMENT_ID
FROM   XHB_WLL_DOCUMENT;

CREATE TABLE EXT_XHB_ORDER TABLESPACE "XHIBIT_BASELINE" AS
SELECT ORDER_ID,
       DELIVERY_DATE,
       SIGNED_BY,
       SIGNING_DATE,
       DATA_XML,
       ORDER_DELIVERY_STATUS_ID,
       ORDER_STATUS_ID,
       ORDER_TEMPLATE_ID,
       --VERSION,
       --LAST_UPDATE_DATE,
       DEFENDANT_ON_CASE_ID,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       DESCRIPTION
FROM   XHB_ORDER;


































/*
--- THIS STUFF IS IN PROGRESS.........
CREATE TABLE ext_xhb_ref_advocate TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_ADVOCATE_ID,
       IS_GLOBAL,
       CREST_ADVOCATE_ID,
       CREST_CHAMBER_ID,
       OBS_IND,
       YEAR_OF_CALL,
       VAT_NO,
       BAR_NO,
       HONOURS,
       ADV_TYPE_IND,
       REF_LEGAL_REP_ID,   -- LOOKUP
       REF_CHAMBER_ID      -- LOOKUP
FROM   XHB_REF_ADVOCATE;

CREATE TABLE ext_xhb_ref_app_result TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_APP_RESULT_ID,
       APP_RESULT_CODE,
       APP_RESULT_DESCR1,
       HO_CODE,
       VARY_SENTENCE,
       APP_RESULT_DESCR2,
       LESSER_OFF_IND,
       OBS_IND
FROM   XHB_REF_APP_RESULT;

CREATE TABLE ext_xhb_ref_chamber TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_CHAMBER_ID,
       OBS_IND,
       IS_GLOBAL,
       DX_REF,
       LOCATION_CODE,
       CREST_CHAMBER_ID,
       FIRM_NAME,
       ADDRESS_ID      -- LOOKUP
FROM   XHB_REF_CHAMBER;

CREATE TABLE ext_xhb_ref_court TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_COURT_ID,
       COURT_FULL_NAME,
       COURT_SHORT_NAME,
       NAME_PREFIX,
       COURT_TYPE,
       CREST_CODE,
       OBS_IND,
       IS_PSD,
       DX_REF,
       ADDRESS_ID      -- LOOKUP
FROM   XHB_REF_COURT;

CREATE TABLE ext_xhb_ref_court_reporter TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_COURT_REPORTER_ID,
       FIRST_NAME,
       MIDDLE_NAME,
       SURNAME,
       CREST_COURT_REPORTER_ID,   -- LOOKUP???
       INITIALS,
       REPORT_METHOD,
       OBS_IND,
       REF_COURT_REPORTER_FIRM_ID -- LOOKUP
FROM   XHB_REF_COURT_REPORTER;

CREATE TABLE ext_xhb_ref_court_rep_firm TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_COURT_REPORTER_FIRM_ID,
       OBS_IND,
       DISPLAY_FIRST,
       DX_REF,
       VAT_NO,
       FIRM_NAME,
       ADDRESS_ID,   -- LOOKUP
       CREST_COURT_REPORTER_FIRM_ID  -- LOOKUP??
FROM XHB_REF_COURT_REPORTER_FIRM;

CREATE TABLE ext_xhb_ref_disposal TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_DISPOSAL_ID,
       DISPOSAL_CODE,
       DISPOSAL_TITLE,
       CREST_MENU_GROUP,
       CREST_TEMPLATE_VERSION,
       DISP_TITLE1,
       DISP_TITLE2,
       DVLC_CODE,
       OBS_IND
FROM   XHB_REF_DISPOSAL;

CREATE TABLE ext_xhb_ref_disposal_line TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_DISPOSAL_LINE_ID,
       DISPOSAL_CODE,
       TEMPLATE_VERSION,
       DIL_SEQ_NO,
       DATA,
       INPUT_FLAG,
       SCREEN_PRINT,
       FORM_PRINT,
       DBDESTIN,
       PROMPT,
       FORMAT,
       MANDATORY,
       DBSOURCE,
       VALIDATION,
       MULTIPLE_CHOICE,
       MCGROUP1,
       MCGROUP2,
       CHAR_MAX,
       CONC_FLAG,
       LINE_INSERT,
       OBS_IND
FROM XHB_REF_DISPOSAL_LINE;

CREATE TABLE ext_xhb_ref_disposal_menu TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_DISPOSAL_MENU_ID,
       TITLE,
       DISPOSAL_CODE,
       PARENT,
       ABBREV,
       MENU_GROUP,
       SEQ_NO,
       OBS_IND,
       MENU_ITEM_ID  -- LOOKUP???
FROM XHB_REF_DISPOSAL_MENU;

CREATE TABLE ext_xhb_ref_disposal_type TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_DISPOSAL_TYPE_ID,
       TEMPLATE_VERSION,
       DISPOSAL_CODE,
       MENU_GROUP,
       TITLE,
       DISP_TITLE1,
       DISP_TITLE2,
       LINE_AVAIL,
       CATEGORY,
       OBS_IND
FROM   XHB_REF_DISPOSAL_TYPE;

CREATE TABLE ext_xhb_ref_disp_men_case_type TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_DISPOSAL_MENU_CASE_TYPE_ID,
       REF_DISPOSAL_MENU_ID,          -- LOOKUP???
       CASE_TYPE
FROM XHB_REF_DISP_MENU_CASE_TYPE;

CREATE TABLE ext_xhb_ref_hearing_type TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_HEARING_TYPE_ID,
       HEARING_TYPE_CODE,
       HEARING_TYPE_DESC,
       CATEGORY,
       SEQ_NO,
       LIST_SEQUENCE,
       OBS_IND
FROM   XHB_REF_HEARING_TYPE;

CREATE TABLE ext_xhb_ref_judge TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_JUDGE_ID,
       JUDGE_TYPE,
       CREST_JUDGE_ID,
       TITLE,
       FIRST_NAME,
       MIDDLE_NAME,
       SURNAME,
       FULL_LIST_TITLE1,
       FULL_LIST_TITLE2,
       FULL_LIST_TITLE3,
       STATS_CODE,
       INITIALS,
       HONOURS,
       JUD_VERS,
       OBS_IND,
       SOURCE_TABLE
FROM   XHB_REF_JUDGE;

CREATE TABLE ext_xhb_ref_justice TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_JUSTICE_ID,
       JUSTICE_NAME,
       CREST_JUSTICE_ID,
       PSD_COURT_CODE,
       TITLE,
       INITIALS,
       OBS_IND
FROM   XHB_REF_JUSTICE;

CREATE TABLE ext_xhb_ref_legal_rep TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_LEGAL_REP_ID,
       FIRST_NAME,
       MIDDLE_NAME,
       SURNAME,
       TITLE,
       INITIALS,
       LEGAL_REP_TYPE,
       OBS_IND
FROM   XHB_REF_LEGAL_REPRESENTATIVE;

CREATE TABLE ext_xhb_ref_offence TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_OFFENCE_ID,
       OFFENCE_CODE,
       OFFENCE_DESC,
       HO_PROC_TYPE,
       HO_CLASS,
       HO_SUB_CLASS,
       DVLC_CODE,
       STATUTE,
       OFFENCE_CLASS,
       ACT_SECTION,
       OBS_IND,
       OFFENCE_DESC2,
       OFFENCE_GROUP
FROM   XHB_REF_OFFENCE;

CREATE TABLE ext_xhb_ref_prosecutor_agency TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_PROSECUTOR_AGENCY_ID,
       TITLE,
       PROSECUTOR_NAME_1,
       PROSECUTOR_NAME_2,
       PROSECUTOR_NAME_3,
       INITIALS,
       ADDRESS_ID,       -- LOOKUP
       CREST_OPPOSER_ID,
       CPS_CODE,
       DX_REF,
       OBS_IND
FROM   XHB_REF_PROSECUTOR_AGENCY;

CREATE TABLE ext_xhb_ref_solicitor TABLESPACE "XHIBIT_BASELINE" AS
SELECT SOLICITOR_ID,
       CREST_SOLICITOR_NAME,
       IS_IN_CREST,
       REF_LEGAL_REP_ID,      -- LOOKUP
       OBS_IND,
       REF_SOLICITOR_FIRM_ID  -- LOOKUP
FROM   XHB_REF_SOLICITOR;

CREATE TABLE ext_xhb_ref_solicitor_firm TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_SOLICITOR_FIRM_ID,
       SOLICITOR_FIRM_NAME,
       CREST_SOF_ID,
       OBS_IND,
       SHORT_NAME,
       DX_REF,
       VAT_NO,
       ADDRESS_ID -- LOOKUP
FROM   XHB_REF_SOLICITOR_FIRM;

CREATE TABLE ext_xhb_ref_system_code TABLESPACE "XHIBIT_BASELINE" AS
SELECT REF_SYSTEM_CODE_ID,
       CODE,
       CODE_TYPE,
       CODE_TITLE,
       DE_CODE,
       REF_CODE_ORDER,
       OBS_IND
FROM   XHB_REF_SYSTEM_CODE;
*/


/*
-- THE CREATE TABLE AND POPULATION OF DATA FROM THE XHB_ORDER_DISPOSAL_XREF TABLE...

CREATE TABLE EXT_XHB_ORDER_DISPOSAL_XREF AS
SELECT -- start with the de-normalised joins...
       rs_xrdt.template_version AS rs_template_version,
       rs_xrdt.disposal_code    AS rs_disposal_code,
       rs_xrdt.menu_group       AS rs_menu_group,
       co_xrdt.template_version AS co_template_version,
       co_xrdt.disposal_code    AS co_disposal_code,
       co_xrdt.menu_group       AS co_menu_group,
       -- the rest of the data 
       xodx.ORDER_DISPOSAL_XREF_ID
       --LAST_UPDATE_DATE,
       --CREATION_DATE,
       --CREATED_BY,
       --LAST_UPDATED_BY,
       --VERSION,
       --COURT_ID,
       --xodx.RS_REF_DISPOSAL_TYPE_ID,
       --xodx.CO_REF_DISPOSAL_TYPE_ID
FROM   XHB_ORDER_DISPOSAL_XREF xodx,
       XHB_REF_DISPOSAL_TYPE rs_xrdt,
       XHB_REF_DISPOSAL_TYPE co_xrdt
WHERE  xodx.RS_REF_DISPOSAL_TYPE_ID = rs_xrdt.REF_DISPOSAL_TYPE_ID
AND    xodx.CO_REF_DISPOSAL_TYPE_ID = co_xrdt.REF_DISPOSAL_TYPE_ID;

INSERT INTO XHB_ORDER_DISPOSAL_XREF
SELECT XHB_ORDER_DISPOSAL_XREF_SEQ.NEXTVAL AS ORDER_DISPOSAL_XREF_ID,
       NULL AS LAST_UPDATE_DATE,
       NULL AS CREATION_DATE,
       NULL AS CREATED_BY,
       NULL AS LAST_UPDATED_BY,
       NULL AS VERSION,
       p_court_id_in AS COURT_ID,
       training_utils_pkg.get_ref_disposal_type_id(p_court_id_in, rs_template_version, rs_disposal_code, rs_menu_group) AS RS_REF_DISPOSAL_TYPE_ID,
       training_utils_pkg.get_ref_disposal_type_id(p_court_id_in, co_template_version, co_disposal_code, co_menu_group) AS CO_REF_DISPOSAL_TYPE_ID
FROM   EXT_XHB_ORDER_DISPOSAL_XREF;
*/