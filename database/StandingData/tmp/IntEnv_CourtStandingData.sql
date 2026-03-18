/*
 ===================================================================================================================
 | LAST MODIFIED BY $Author: cz4lvy $
 | LAST MODIFIED ON $Date: 2003/05/14 11:41:26 $
 | REVISION NO $Revision: 1.1 $
 | $NoKeywords $
 ===================================================================================================================
 | SERVER        : ORACLE 9i
 | REUSABILITY 	 : LOW
 | DESCRIPTION   : This script populates XHB_COURT, XHB_COURT_SITE, XHB_COURT_ROOM, XHB_ADDRESS to insert meta-data 
 |                 about a court with 1 courtsite and 10 courtrooms, to be used in the integration environment
 |	
 | AUTHOR        : Rakesh Lakhani
 | DATE CREATED  : 23 Apr 2003
 ===================================================================================================================
 | DEPENDENCIES:  
 ===================================================================================================================
 ===================================================================================================================
 | LIMITATIONS   : This script may have court that is present.   
 ===================================================================================================================

 ===================================================================================================================


 ===================================================================================================================
 | REVISION NOTES:
 ===================================================================================================================
 | VERSION 	 | DATE             		| MODIFIED BY          	| DESCRIPTION OF CHANGE
 ===================================================================================================================
 ===================================================================================================================
 | ANTICIPATED ENHANCEMENTS:
 ===================================================================================================================
 | 1. This script can be used to automate the setup of metadata of court information each time a new court is added to XHIBIT.
 | 2. A control mechanism to see if the court has already been loaded. This option should give the ability to delete.
 ===================================================================================================================
 | EXAMPLE CODE :
 ===================================================================================================================
  
 ===================================================================================================================

*/

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

-- Get primary keys for new records that have foreign key relationships
DECLARE 
var_address_id NUMBER;
var_court_id NUMBER;
var_courtsite_id NUMBER;
var_courtroom_id NUMBER;

BEGIN

SELECT XHB_ADDRESS_SEQ.NEXTVAL INTO var_address_id FROM dual;
SELECT XHB_COURT_SEQ.NEXTVAL INTO var_court_id FROM dual;
SELECT XHB_COURT_SITE_SEQ.NEXTVAL INTO var_courtsite_id FROM dual;

-- Added in order to avoid the problem when sequence number next value is less than IDs maximum number which may come from test data
--SELECT MAX(ADDRESS_ID)+1  INTO var_address_id FROM XHB_ADDRESS;
--SELECT MAX(COURT_ID)+1  INTO var_court_id  FROM XHB_COURT;
--SELECT MAX(COURT_SITE_ID)+1  INTO var_courtsite_id  FROM XHB_COURT_SITE;
--SELECT MAX(COURT_ROOM_ID)+1  INTO var_courtroom_id  FROM XHB_COURT_ROOM;

-- Address
INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY ) VALUES ( 
var_address_id, '36 Ridgeway Road', NULL, NULL, NULL, 'Isleworth', 'Middlesex', 'TW7 5LP', 'England');

-- Court
INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME ) VALUES ( 
var_court_id, 'CROWN', 'LONDON', 'ISLEWORTH', '475', 'THIS COLUMN TO BE REMOVED', 'ISLEW', var_address_id, 'csa00110:90', 'Y', NULL, 'PROBATION OFFICE2', 'INTERNET COURT', 'Isleworth Crown Court'); 

-- CourtSite
INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
var_courtsite_id, 'ISLEWORTH', 'I', var_court_id, var_address_id, NULL, 'ISLEWORTH'); 

-- CourtRoom
INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 1', ' ', 1, var_courtsite_id, NULL, 'Court 1'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 2', ' ', 2, var_courtsite_id, NULL, 'Court 2'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 3', ' ', 3, var_courtsite_id, NULL, 'Court 3'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 4', ' ', 4, var_courtsite_id, NULL, 'Court 4'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 5', ' ', 5, var_courtsite_id, NULL, 'Court 5'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 6', ' ', 6, var_courtsite_id, NULL, 'Court 6'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 7', ' ', 7, var_courtsite_id, NULL, 'Court 7'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 8', ' ', 8, var_courtsite_id, NULL, 'Court 8'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 9', ' ', 9, var_courtsite_id, NULL, 'Court 9'); 

INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES ( 
XHB_COURT_ROOM_SEQ.NEXTVAL, 'Court 10', ' ', 10, var_courtsite_id, NULL, 'Court 10'); 

END;

/
/* 
	END COURT = ISLEWORTH Crown Court
*/