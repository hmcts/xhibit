/*
* Author        Version    Date         Description
* ----------------------------------------------------------------------
* G Nagarajan   1.0        05/Mar/2009  Initial Version
*
*
*
* -----------------------------------------------------------------------
*Usage: This script must be run by logging as sys or system
*/
CREATE OR REPLACE DIRECTORY HK_LOGS AS '/home/oracle/housekeeping/logs';

GRANT ALL ON DIRECTORY HK_LOGS TO XHIBIT;

