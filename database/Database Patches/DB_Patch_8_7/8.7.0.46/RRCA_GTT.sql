/*    ------------------------------------------------------------------
 *    Global Temporary Table used for the RRCA report
 */   ------------------------------------------------------------------

CREATE GLOBAL TEMPORARY TABLE rrca_summary_data (
	sort_order			NUMBER
	,court_name			VARCHAR2(255 BYTE)
	,court_site_name	VARCHAR2(255 BYTE)
	,age_band_text		VARCHAR2(30)
	,custody_cases		NUMBER
	,non_custody_cases	NUMBER
	,total				NUMBER
	,age_band_code		VARCHAR2(1)
)
ON COMMIT DELETE ROWS;