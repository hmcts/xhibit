--  FILE_NAME:         XHIBIT2_Standing_CourtSiteRoom_TAA.sql
--  ENVIRONMENT:       Training Administration Application ONLY
--  DESCRIPTION:       This script populates XHB_COURT, XHB_COURT_SITE, XHB_COURT_ROOM, XHB_ADDRESS, XHB_CONTACT_DETAIL
--                     to insert meta-data about a court.
--                     This script details specific data links/ids to data in CREST
--                     ie. the CREST IP addresses and CREST_COURT_ID, CREST_COURT_ROOM_No.
--  STATUS DESCRIPTION:
--
--  DEPENDENCIES:      It will have a dependencies if the test data has been loaded.
--                     It may lose the sequence of the data put in.
--                     Note. THIS SCRIPT SHOULD ONLY BE RUN ON A CLEAN(EMPTY) XHIBIT DATABASE, AND MUST BE RUN FIRST
--  OWNER:             Nick Sawyer
-- 

DELETE FROM XHB_CONTACT_DETAIL;
DELETE FROM XHB_ADDRESS;
DELETE FROM XHB_COURT_ROOM;
DELETE FROM XHB_COURT_SITE;
DELETE FROM XHB_COURT;


DROP SEQUENCE XHB_CONTACT_DETAIL_SEQ;
CREATE SEQUENCE XHB_CONTACT_DETAIL_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1 NOCACHE NOCYCLE NOORDER;

DROP SEQUENCE XHB_ADDRESS_SEQ;
CREATE SEQUENCE XHB_ADDRESS_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1 NOCACHE NOCYCLE NOORDER;

DROP SEQUENCE XHB_COURT_ROOM_SEQ;
CREATE SEQUENCE XHB_COURT_ROOM_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1 NOCACHE NOCYCLE NOORDER;

DROP SEQUENCE XHB_COURT_SITE_SEQ;
CREATE SEQUENCE XHB_COURT_SITE_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1 NOCACHE NOCYCLE NOORDER;

DROP SEQUENCE XHB_COURT_SEQ;
CREATE SEQUENCE XHB_COURT_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1 NOCACHE NOCYCLE NOORDER;

DECLARE 

  var_contact_id NUMBER;
  var_address_id NUMBER;
  var_court_id NUMBER;
  var_courtsite_id NUMBER;
  var_courtroom_id NUMBER;
  var_courtroom_num NUMBER;

BEGIN

/* 
 * Dockford Crown Court for TAA
 *
 * Dockford Crown Court
 * Dockford Training Street
 * Dockford
 * London
 * TR41 NNG
 *
 * LCD_STATUS_CODE:491
 *
 */ 

-- Address Dockford Training
SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

INSERT INTO XHB_ADDRESS (ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY)
VALUES (var_address_id, 'Dockford Court House', 'Dockford Training Street', NULL, NULL, 'Dockford', 'London', 'TR41 NNG', 'England');

-- Contact details Dockford Training
SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID)
VALUES (var_contact_id, 'TEL', '020 8123 4567', var_address_id);

SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
VALUES (var_contact_id, 'FAX', '020 8123 7954', var_address_id);

-- Court Dockford Training
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;

INSERT INTO XHB_COURT (COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE)
VALUES (var_court_id, 'CROWN', 'SOUTH EASTERN', 'DOCKFORD', '491', 'CROWN COURT', 'DOCKF', var_address_id, 'CSA00110:90', 'Y','N', 'DOCKFORD PROBATION OFFICE', 'Dockford','DOCKFORD CROWN COURT', '01AA'); 

INSERT INTO XHB_TAA_COURT_INFO VALUES(var_court_id, 'CREST_IP_ADDRESS', '10.31.13.5');

-- CourtSite Dockford Training
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

INSERT INTO XHB_COURT_SITE (COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID)
VALUES (var_courtsite_id , 'DOCKFORD', 'A', var_court_id, var_address_id, 'Court Site A', '491'); 

-- CourtRoom Dockford Training
var_courtroom_num := 0;

FOR i IN 1..13 LOOP
  var_courtroom_num := var_courtroom_num + 1;
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual; 
  INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME)
  VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
END LOOP;

COMMIT;

/* 
 * Hockford Crown Court for TAA
 *
 * Hockford Crown Court
 * Hockford Training Street
 * Hockford
 * London
 * TR41 NNG
 *
 * LCD_STATUS_CODE:492
 *
 */ 

-- Address Hockford Training
SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

INSERT INTO XHB_ADDRESS (ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY)
VALUES (var_address_id, 'Hockford Court House', 'Hockford Training Street', NULL, NULL, 'Hockford', 'London', 'TR41 NNG', 'England');

-- Contact details Hockford Training
SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID)
VALUES (var_contact_id, 'TEL', '020 8123 4567', var_address_id);

SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
VALUES ( var_contact_id, 'FAX', '020 8123 7954', var_address_id);

-- Court Hockford Training
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;

INSERT INTO XHB_COURT (COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE)
VALUES (var_court_id, 'CROWN', 'SOUTH EASTERN', 'HOCKFORD', '492', 'CROWN COURT', 'HOCKF', var_address_id, 'CSA00110:90', 'Y','N', 'HOCKFORD PROBATION OFFICE', 'Hockford','HOCKFORD CROWN COURT', '01AB'); 

INSERT INTO XHB_TAA_COURT_INFO VALUES(var_court_id, 'CREST_IP_ADDRESS', '10.31.13.5');

-- CourtSite Hockford Training
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

INSERT INTO XHB_COURT_SITE (COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID)
VALUES (var_courtsite_id , 'HOCKFORD', 'A', var_court_id, var_address_id, 'Court Site A', '492'); 

-- CourtRoom Hockford Training
var_courtroom_num := 0;

FOR i IN 1..13 LOOP
  var_courtroom_num := var_courtroom_num + 1;
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual; 
  INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME)
  VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
END LOOP;

COMMIT;

/* 
 * Lockford Crown Court for TAA
 *
 * Lockford Crown Court
 * Lockford Training Street
 * Lockford
 * London
 * TR41 NNG
 *
 * LCD_STATUS_CODE:493
 *
 */ 

-- Address Lockford Training
SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

INSERT INTO XHB_ADDRESS (ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY)
VALUES (var_address_id, 'Lockford Court House', 'Lockford Training Street', NULL, NULL, 'Lockford', 'London', 'TR41 NNG', 'England');

-- Contact details Lockford Training
SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID)
VALUES (var_contact_id, 'TEL', '020 8123 4567', var_address_id);

SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
VALUES ( var_contact_id, 'FAX', '020 8123 7954', var_address_id);

-- Court Lockford Training
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;

INSERT INTO XHB_COURT (COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE)
VALUES (var_court_id, 'CROWN', 'SOUTH EASTERN', 'LOCKFORD', '493', 'CROWN COURT', 'LOCKF', var_address_id, 'CSA00110:90', 'Y','N', 'LOCKFORD PROBATION OFFICE', 'Lockford','LOCKFORD CROWN COURT', '01AC'); 

INSERT INTO XHB_TAA_COURT_INFO VALUES(var_court_id, 'CREST_IP_ADDRESS', '10.31.13.5');

-- CourtSite Lockford Training
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

INSERT INTO XHB_COURT_SITE (COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID)
VALUES (var_courtsite_id , 'LOCKFORD', 'A', var_court_id, var_address_id, 'Court Site A', '493'); 

-- CourtRoom Lockford Training
var_courtroom_num := 0;

FOR i IN 1..13 LOOP
  var_courtroom_num := var_courtroom_num + 1;
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual; 
  INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME)
  VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
END LOOP;

COMMIT;

/* 
 * Mockford Crown Court for TAA
 *
 * Mockford Crown Court
 * Mockford Training Street
 * Mockford
 * London
 * TR41 NNG
 *
 * LCD_STATUS_CODE:494
 *
 */ 

-- Address Mockford Training
SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

INSERT INTO XHB_ADDRESS (ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY)
VALUES (var_address_id, 'Mockford Court House', 'Mockford Training Street', NULL, NULL, 'Mockford', 'London', 'TR41 NNG', 'England');

-- Contact details Mockford Training
SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID)
VALUES (var_contact_id, 'TEL', '020 8123 4567', var_address_id);

SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
VALUES ( var_contact_id, 'FAX', '020 8123 7954', var_address_id);

-- Court Mockford Training
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;

INSERT INTO XHB_COURT (COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE)
VALUES (var_court_id, 'CROWN', 'SOUTH EASTERN', 'MOCKFORD', '494', 'CROWN COURT', 'MOCKF', var_address_id, 'CSA00110:90', 'Y','N', 'MOCKFORD PROBATION OFFICE', 'Mockford','MOCKFORD CROWN COURT', '01AD'); 

INSERT INTO XHB_TAA_COURT_INFO VALUES(var_court_id, 'CREST_IP_ADDRESS', '10.31.13.5');

-- CourtSite Mockford Training
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

INSERT INTO XHB_COURT_SITE (COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID)
VALUES (var_courtsite_id , 'MOCKFORD', 'A', var_court_id, var_address_id, 'Court Site A', '494'); 

-- CourtRoom Mockford Training
var_courtroom_num := 0;

FOR i IN 1..13 LOOP
  var_courtroom_num := var_courtroom_num + 1;
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual; 
  INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME)
  VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
END LOOP;

COMMIT;
/* 
 * Sockford Crown Court for TAA
 *
 * Sockford Crown Court
 * Sockford Training Street
 * Sockford
 * London
 * TR41 NNG
 *
 * LCD_STATUS_CODE:495
 *
 */ 

-- Address Sockford Training
SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;

INSERT INTO XHB_ADDRESS (ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY)
VALUES (var_address_id, 'Sockford Court House', 'Sockford Training Street', NULL, NULL, 'Sockford', 'London', 'TR41 NNG', 'England');

-- Contact details Sockford Training
SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID)
VALUES (var_contact_id, 'TEL', '020 8123 4567', var_address_id);

SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL INTO var_contact_id FROM dual;

INSERT INTO XHB_CONTACT_DETAIL (CONTACT_ID, CONTACT_TYPE, CONTACT_VALUE, ADDRESS_ID) 
VALUES ( var_contact_id, 'FAX', '020 8123 7954', var_address_id);

-- Court Sockford Training
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;

INSERT INTO XHB_COURT (COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME, COURT_CODE)
VALUES (var_court_id, 'CROWN', 'SOUTH EASTERN', 'SOCKFORD', '495', 'CROWN COURT', 'SOCKF', var_address_id, 'CSA00110:90', 'Y','N', 'SOCKFORD PROBATION OFFICE', 'Sockford','SOCKFORD CROWN COURT', '01AE'); 

INSERT INTO XHB_TAA_COURT_INFO VALUES(var_court_id, 'CREST_IP_ADDRESS', '10.31.13.5');

-- CourtSite Sockford Training
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

INSERT INTO XHB_COURT_SITE (COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME, CREST_COURT_ID)
VALUES (var_courtsite_id , 'SOCKFORD', 'A', var_court_id, var_address_id, 'Court Site A', '495'); 

-- CourtRoom Sockford Training
var_courtroom_num := 0;

FOR i IN 1..13 LOOP
  var_courtroom_num := var_courtroom_num + 1;
  SELECT XHB_COURT_ROOM_SEQ.NEXTVAL INTO var_courtroom_id FROM dual; 
  INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME)
  VALUES (var_courtroom_id, 'Court '||var_courtroom_num, 'Court Room '||var_courtroom_num, var_courtroom_num, var_courtsite_id, 'N', 'Court Room '||var_courtroom_num);
END LOOP;

COMMIT;

END;
/

