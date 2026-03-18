/*
 * Script to drop any Mercator triggers left after eventserver shutdown.
 * Script also deletes records from appropriate Mercator tables, as recommended
 * in Mercator documentation.
 *
 * The script should be placed in the UTILITIES folder in the home directory of the
 * Mercator user on the Integration boxes.
 */

SET PAGESIZE 1000
SET LINESIZE 1000
SET FEEDBACK OFF
SET HEADING OFF
SET TRIMSPOOL ON

PROMPT	-- Get any triggers to be dropped
SPOOL temp1.sql

PROMPT	-- Create drop statement for Insert Row triggers
SELECT	'SELECT	''DROP TRIGGER '' || trigger_name || '';''' ||
	'FROM user_triggers ' ||
	'WHERE trigger_name LIKE ''' || 
	trigger_name || '%'';'
FROM	user_triggers
WHERE	trigger_name like '%IR'
/

PROMPT	-- Create drop statement for Update Row triggers
SELECT	'SELECT	''DROP TRIGGER '' || trigger_name || '';''' ||
	'FROM user_triggers ' ||
	'WHERE trigger_name LIKE ''' || 
	trigger_name || '%'';'
FROM	user_triggers
WHERE	trigger_name like '%UR'
/

PROMPT	-- Create drop statement for Insert Table triggers
SELECT	'SELECT	''DROP TRIGGER '' || trigger_name || '';''' ||
	'FROM user_triggers ' ||
	'WHERE trigger_name LIKE ''' || 
	trigger_name || '%'';'
FROM	user_triggers
WHERE	trigger_name like '%IT'
/

PROMPT	-- Create drop statement for Update Tables triggers
SELECT	'SELECT	''DROP TRIGGER '' || trigger_name || '';''' ||
	'FROM user_triggers ' ||
	'WHERE trigger_name LIKE ''' || 
	trigger_name || '%'';'
FROM	user_triggers
WHERE	trigger_name like '%UT'
/

SPOOL temp2.sql

PROMPT	-- Create drop script
@temp1

SPOOL OFF

SET FEEDBACK ON
SET HEADING ON
SET TRIMSPOOL OFF

PROMPT	Drop triggers
@temp2

PROMPT	Remove temporary files
!rm temp1.sql
!rm temp2.sql

PROMPT	Tidy associated tables
DELETE trigger_events
/

DELETE trigger_registry
/

DELETE trigger_catalog
/

COMMIT
/

PROMPT	Display charge_import_indicator status
@count
