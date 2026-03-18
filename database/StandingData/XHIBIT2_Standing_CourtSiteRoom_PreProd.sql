--  FILE_NAME:         XHIBIT2_Standing_CourtSiteRoom.sql                                                              
--  ENVIRONMENT:       System, Pre-production i.e. CAMBERLEY ONLY                                                          
--  DESCRIPTION:       This script populates XHB_COURT, XHB_COURT_SITE, XHB_COURT_ROOM, XHB_ADDRESS, XHB_CONTACT_DETAIL 
--                     to insert meta-data about a court.                                                              
--                     This script details specific data links/ids to data in CREST 
--                     ie. the CREST IP addresses and CREST_COURT_ID, CREST_COURT_ROOM_No.
--  STATUS DESCRIPTION:Script is suitable for running initially on set-up of the database on any environment.
--                     Adds data for Snaresbrook, Dockford(Snaresbrook training system), Isleworth
--  DEPENDENCIES:      It will have a dependencies if the test data has been loaded. 
--                     It may lose the sequence of the data put in.                     
--                     Note. THIS SCRIPT SHOULD ONLY BE RUN ON A CLEAN(EMPTY) XHIBIT DATABASE, AND MUST BE RUN FIRST
--  OWNER:             Cag Onganer,Sandip Sangha
-- 

DELETE FROM XHB_CONTACT_DETAIL;
DELETE FROM XHB_ADDRESS;
DELETE FROM XHB_COURT_ROOM;
DELETE FROM XHB_COURT_SITE;
DELETE FROM XHB_COURT;


DROP SEQUENCE XHB_CONTACT_DETAIL_SEQ;
CREATE SEQUENCE XHB_CONTACT_DETAIL_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1
NOCACHE NOCYCLE NOORDER;
DROP SEQUENCE XHB_ADDRESS_SEQ;
CREATE SEQUENCE XHB_ADDRESS_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1
NOCACHE NOCYCLE NOORDER;
DROP SEQUENCE XHB_COURT_ROOM_SEQ;
CREATE SEQUENCE XHB_COURT_ROOM_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1
NOCACHE NOCYCLE NOORDER;
DROP SEQUENCE XHB_COURT_SITE_SEQ;
CREATE SEQUENCE XHB_COURT_SITE_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1
NOCACHE NOCYCLE NOORDER;
DROP SEQUENCE XHB_COURT_SEQ;
CREATE SEQUENCE XHB_COURT_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1
NOCACHE NOCYCLE NOORDER;

DECLARE 

var_contact_id NUMBER;
var_address_id NUMBER;
var_court_id NUMBER;
var_courtsite_id NUMBER;
var_courtroom_id NUMBER;
var_courtroom_num NUMBER;

BEGIN


/* 
	BEGIN COURT = Snaresbrook Crown Court at System Test and Pre-production

		Snaresbrook Crown Court
		75 Hollybush Hill
		Snaresbrook 
		E11 1QW 
		
		DX 9824 WANSTEAD 2
		Tel: 020 8530 0000
		Fax: 020 8530 0072 

		LCD_STATUS_CODE:453

*/ 

-- Address Snaresbrook
SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY ) VALUES ( 
var_address_id, 'Snaresbrook Court House', '75 Hollybush Hill', NULL, NULL, 'Snaresbrook', 'London', 'E11 1QW', 'England');

-- Contact details Snarebrook
SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL ( CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
VALUES ( var_contact_id, 'TEL', '020 8530 0000', var_address_id);

SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL ( CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
VALUES ( var_contact_id, 'FAX', '020 8530 0072', var_address_id);

-- Court Snaresbrook
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;

INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE ) 
VALUES ( var_court_id, 'CROWN', 'SOUTH EASTERN', 'SNARESBROOK', '453', 'CROWN COURT', 'SNARE', var_address_id, 'CSA00110:90', 'Y','N', 'SNARESBROOK PROBATION OFFICE', 'Snaresbrook','SNARESBROOK CROWN COURT', '01AA'); 

-- CourtSite Snaresbrook
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID ) VALUES ( 
var_courtsite_id , 'SNARESBROOK', 'A', var_court_id, var_address_id, 'Court Site A', '453' ); 

-- CourtRoom Snaresbrook
var_courtroom_num := 0;
FOR i IN 1..20
LOOP
  var_courtroom_num := var_courtroom_num + 1;
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual; 
  INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) 
  VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
END LOOP;
COMMIT;


/* 
	BEGIN COURT = Dockford Crown Court (for Training at Snaresbrook)

		Training Centre
		75 Hollybush Hill
		Dockford 
		TR41 NNG 
		 

		LCD_STATUS_CODE:499
*/

-- Address Dockford (training)
SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY ) VALUES ( 
var_address_id, 'Training Centre', '75 Hollybush Hill', NULL, NULL, 'Dockford', 'London', 'TR41 NNG', 'England');

-- Court Dockford (training)
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;

INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE ) 
VALUES ( var_court_id, 'CROWN', 'LONDON', 'DOCKFORD TRAINING', '499', 'CROWN COURT', 'DOCKF', var_address_id, 'CSA00200:90', 'Y','N', 'DOCKFORD PROBATION OFFICE', 'DOCKFORD CROWN COURT','DOCKFORD CROWN COURT', '01AB'); 

-- CourtSite Dockford (training)
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID ) VALUES ( 
var_courtsite_id , 'DOCKFORD', 'A', var_court_id, var_address_id,  'Court Site A', '499' ); 

-- CourtRoom Dockford (training)
var_courtroom_num := 0;
FOR i IN 1..10
LOOP
  var_courtroom_num := var_courtroom_num + 1;
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual; 
  INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) 
  VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
END LOOP;
COMMIT;

/* 
	BEGIN COURT = Isleworth Crown Court

		Isleworth Crown Court
		36 Ridgeway Road
		Isleworth 
		TW7 5LP

		DX: 97420 ISLEWORTH 1
		Tel: 020 8380 4500
		Fax: 020 8568 5368 

		LCD_STATUS_CODE:475
*/


-- Address Isleworth
SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY ) VALUES ( 
var_address_id, 'Isleworth Court House','36 Ridgeway Road', NULL, NULL, 'Isleworth', 'Middlesex', 'TW7 5LP', 'England');

-- Court Isleworth
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;

INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE ) 
VALUES ( var_court_id, 'CROWN', 'LONDON', 'ISLEWORTH', '475', 'CROWN COURT', 'ISLEW', var_address_id, '10.25.5.20:90', 'Y', NULL, 'ISLEWORTH PROBATION OFFICE', 'Isleworth', 'ISLEWORTH CROWN COURT', '01AC'); 

-- CourtSite Isleworth
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID ) VALUES ( 
var_courtsite_id , 'ISLEWORTH', 'I', var_court_id, var_address_id,  'Court Site I', '475' ); 

-- CourtRoom Isleworth
var_courtroom_num := 0;
FOR i IN 1..8
LOOP
  var_courtroom_num := var_courtroom_num + 1;
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual; 
  INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) 
  VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
END LOOP;
COMMIT;



END;
/

