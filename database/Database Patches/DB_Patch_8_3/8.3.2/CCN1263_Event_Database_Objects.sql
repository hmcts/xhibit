---------------------------------------------------------------------
--CCN 1263 - CHANGES RELATING TO THE CREATION OF NEW VERDICT MESSAGES
---------------------------------------------------------------------

-- INSERT NEW MESSGAES TYPES INTO THE COURT LOG EVENT DESCRIPTION TABLE WITHIN XHIBIT DB
--PLEASE NOTE THIS SCRIPT NEEDS TO BE RUN FOR EVERY COURT INSTANCE--
--GJ
INSERT INTO XHB_COURT_LOG_EVENT_DESC
values((select max (event_desc_id) + 1 from xhb_court_log_event_desc),  0,  1,  0,  1,  1,  0,  0,  0, 'LC_TEXT_', 'Capture Verdict',	1,	'Xhibit',	40756,	'Xhibit',	SYSDATE,	SYSDATE,	0,	'Guilty_Under_DVC_VA2004');

COMMIT;


--Update Package that populates the FOrmBResulots DB table in XHIBIT.
@xhb_post_merc_ref_data_pkg