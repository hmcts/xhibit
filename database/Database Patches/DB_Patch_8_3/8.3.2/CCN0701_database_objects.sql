
alter table AUD_BREACH 
      modify REF_COURT_ID null;

alter table XHB_BREACH 
      modify REF_COURT_ID null;

ALTER TABLE aud_ref_offence 
      Add bail_act VARCHAR2(1);

ALTER TABLE xhb_ref_offence
      Add bail_act VARCHAR2(1);

@xhb_ref_offence_bur_tr



@xhb_search_pkg_h
@xhb_search_pkg_b



INSERT INTO XHB_COURT_LOG_EVENT_DESC (
	EVENT_DESC_ID,
	FLAGGED_EVENT,
	EDITABLE,
	SEND_TO_MERCATOR,
	UPDATE_LINKED_CASES,
	PUBLISH_TO_SUBSCRIBERS,
	CLEAR_PUBLIC_DISPLAYS,
	E_INFORM,
	PUBLIC_DISPLAY,
	LINKED_CASE_TEXT,
	EVENT_DESCRIPTION,
	EVENT_TYPE,
	PUBLIC_NOTICE,
	SHORT_DESCRIPTION
) values (
	1000,
	0,
	1,
	0,
	1,
	1,
	0,
	0,
	0,
	'LC_TEXT_',	
	'Capture Plea',
	60106,
	0,
	'Create_Plea'
);



DELETE FROM mtbl_map_route WHERE lookup_value = 'F';

INSERT INTO mtbl_map_route (route_id, map_name, lookup_value, lookup_value2, map_to_run)
VALUES (22, 'AddChargeToCase_Route', 'F', NULL, 'Breaches_Insert_Trigger');

INSERT INTO mtbl_map_route (route_id, map_name, lookup_value, lookup_value2, map_to_run)
VALUES (23, 'AddOffence_Route', 'F', NULL, 'Breaches_Add_Trigger');

INSERT INTO mtbl_map_route (route_id, map_name, lookup_value, lookup_value2, map_to_run)
VALUES (24, 'DeleteCharge_Route', 'F', NULL, 'Breaches_Delete_Trigger');

INSERT INTO mtbl_map_route (route_id, map_name, lookup_value, lookup_value2, map_to_run)
VALUES (25, 'UpdateOffence_Route', 'F', NULL, 'Breaches_Offence_Update_Trigger');

INSERT INTO mtbl_map_route (route_id, map_name, lookup_value, lookup_value2, map_to_run)
VALUES (26, 'DeleteOffence_Route', 'F', null, 'Breaches_Offences_Delete_Trigger');

COMMIT;

