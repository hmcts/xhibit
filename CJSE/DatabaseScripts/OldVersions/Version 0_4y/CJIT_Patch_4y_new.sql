/* This script will be used to update Production from 0_4x to the latest level - 0_4y */
/* This update is adds a version table to the CJSE database in order to keep track of schema versions */

/* To cover CJIP relocation: */

INSERT INTO CJI_CJIP_REQUEST_STATUS VALUES (5,'Pending');

INSERT INTO CJI_DOCUMENT_STATUS VALUES (11,'Pending');


UPDATE CJI_VERSION Set
SCHEMA_VERSION = '0_4y',
last_update_date = sysdate,
Updated_by = 'XHIBIT',
Display_name = 'CJSE Database Schema 0_4x',
Display_seq = 1
Where SCHEMA_NAME = 'CJSE';
