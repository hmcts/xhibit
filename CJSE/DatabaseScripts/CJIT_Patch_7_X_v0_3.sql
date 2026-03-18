/* This script will be used to update Production from 7_X_v0_2 to the latest level - 7_X_v0_3 */
/* This update is adds a version table to the CJSE database in order to keep track of schema versions */


/****************/
/* BITS System Failure - replaced space with / */
/****************/
Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/ImprisonmentOrder-v2-1.xsl'
 where DOCUMENT_TYPE_ID =12;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/YoungOffenderOrder-v2-1.xsl'
 where DOCUMENT_TYPE_ID =13;




/*************************/
/* update Version No     */
/*************************/


UPDATE CJI_VERSION Set
SCHEMA_VERSION = '7_X_v0_3',
last_update_date = sysdate,
Updated_by = 'XHIBIT',
Display_name = 'CJSE Database Schema 7_X_v0_3',
Display_seq = 1
Where SCHEMA_NAME = 'CJSE';
