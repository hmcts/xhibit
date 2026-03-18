/*
 * Filename:    PROD_DATA_Duplicate_defendant_cleanse.sql
 *
 * System:      Production and any enviroments create with Production data
 *
 * Date:        28th October 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 28/10/05	CR	Version 1	Created from scripts provided by Eddie Yates and Alex Brown. 
 */ 


PROMPT *******************************************************
PROMPT ****	ONLY RUN THIS SCRIPT ON PRODUCTION OR AN  **** 
PROMPT ****	ENVIRONMENT CREATED FROM PRODUCTION DATA  ****	
PROMPT *******************************************************



UPDATE	xhb_defendant
SET		crest_defendant_id = -1 * defendant_id
WHERE		court_id = 48
AND		crest_defendant_id IN (	SELECT	crest_defendant_id
						FROM		xhb_defendant
						GROUP BY 	crest_defendant_id,
								court_id
						HAVING	COUNT(*) > 1) 
/



UPDATE XHB_COURT_LOG_ENTRY SET DEFENDANT_ON_CASE_ID = 181 WHERE DEFENDANT_ON_CASE_ID = 167;

UPDATE XHB_SCHED_HEARING_DEFENDANT SET DEFENDANT_ON_CASE_ID = 181 WHERE DEFENDANT_ON_CASE_ID = 167;

UPDATE XHB_DEFENDANT_REFERENCE SET DEFENDANT_ID = 173 WHERE DEFENDANT_ID = 162;

DELETE FROM XHB_DEFENDANT_ON_CASE WHERE DEFENDANT_ON_CASE_ID = 167;

DELETE FROM XHB_DEFENDANT WHERE DEFENDANT_ID = 162;



