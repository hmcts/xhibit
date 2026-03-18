/*    ------------------------------------------------------------------
*     Add column to XHB_PROSECUTOR_AGENCY table
*/    ------------------------------------------------------------------

ALTER TABLE XHB_CASE_PROSECUTOR_AGENCY
	ADD OBS_IND VARCHAR2(1);

ALTER TABLE AUD_CASE_PROSECUTOR_AGENCY
	ADD OBS_IND VARCHAR2(1);

	
/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_case_prosecutor_agency_bur_tr.sql
@@xhb_case_prosecutor_agency_ai_tr.sql

COMMIT;
/