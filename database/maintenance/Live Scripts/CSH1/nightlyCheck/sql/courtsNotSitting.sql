SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF
select COURT_NAME COURT_NOT_SITTING from xhb_court 
where crest_court_id in 
( 
SELECT 
	fromid
FROM
	mtbl_merc_dl_storage 
WHERE
	SUBSTR(additional3,1,10) =  	SUBSTR(to_char(sysdate+1,'yyyy-mm-dd'),1,10)
   AND 
	LENGTH(message) < 2500 
AND
	(upper(to_char(message)) like '%NOT SITTING%' OR 	upper(to_char(message)) like '%NO COURT SITTING%'
	)
)
/

SET HEADING ON

EXIT

