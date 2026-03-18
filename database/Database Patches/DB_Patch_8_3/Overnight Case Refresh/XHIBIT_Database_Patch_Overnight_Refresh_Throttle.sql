/*
 * Author        Version    Date         Description
 * ----------------------------------------------------------------------
 * B Hingston    0.0.2      06/May/2010  Add new row to config prop for new Overnight 
 *					 Refresh Process	
 * -----------------------------------------------------------------------
 *
 * Usage: This script must be run by logging in as xhibit
 */

SET serveroutput on
SET echo on

DELETE XHB_CONFIG_PROP WHERE PROPERTY_NAME = 'Overnight RefreshTimeDelay';

INSERT INTO XHB_CONFIG_PROP VALUES((SELECT MAX(CONFIG_PROP_ID) FROM XHB_CONFIG_PROP)+1, 
'Overnight RefreshTimeDelay', '2');


commit;