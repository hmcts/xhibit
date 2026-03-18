CREATE OR REPLACE PACKAGE BODY XHIBIT.XHB_PDDA_PKG AS

    -- Function to find the next record in XHB_INTERNET_HTML that can be passed to PDDA.
    -- This should not discriminate by court, eventually we will get through all courts.
    -- It will look only for records from today. If the most recent record for a specific court
	-- has a STATUS='P' then no record will be retrieved from this court.
	-- If no courts have a record to return then no data will be returned from this function.
	-- Return the first record found
	FUNCTION get_iwp_next_eligible_record
(
  p_for_date IN DATE DEFAULT SYSDATE
)
  RETURN SYS_REFCURSOR
IS
  l_rc SYS_REFCURSOR;
BEGIN
  OPEN l_rc FOR
  SELECT *
  FROM (
    -- inside your OPEN l_rc FOR ... query
	WITH latest_two AS (
	  SELECT court_id,
			 SUM(CASE WHEN status = 'P' THEN 1 ELSE 0 END) AS p_in_top2
	  FROM (
		SELECT court_id, status,
			   ROW_NUMBER() OVER (
				 PARTITION BY court_id
				 ORDER BY last_update_date DESC, internet_html_id DESC
			   ) rn
		FROM xhb_internet_html
	  )
	  WHERE rn <= 2
	  GROUP BY court_id
	),
	c_today AS (
	  SELECT x.*,
			 ROW_NUMBER() OVER (
			   PARTITION BY x.court_id
			   ORDER BY x.last_update_date DESC, x.internet_html_id DESC
			 ) AS rn_today
	  FROM   xhb_internet_html x
	  WHERE  x.status = 'C'
		AND  x.last_update_date >= TRUNC(p_for_date)
		AND  x.last_update_date <  TRUNC(p_for_date) + 1
	)
	SELECT
	  c.internet_html_id,
	  c.status,
	  c.last_update_date,
	  c.creation_date,
	  c.created_by,
	  c.last_updated_by,
	  c.version,
	  c.court_id,
	  c.html_blob_id,
	  c.rn_today AS rn
	FROM   c_today c
	LEFT JOIN latest_two lt
	  ON lt.court_id = c.court_id
	WHERE  NVL(lt.p_in_top2, 0) < 2   -- exclude courts whose top-2 are both 'P'
	  AND  c.rn_today <= 2            -- up to 2 rows per court
	ORDER  BY c.last_update_date DESC, c.internet_html_id DESC
  )
  WHERE ROWNUM = 1;
  RETURN l_rc;
END get_iwp_next_eligible_record;



	-- Function to find one record per court in XHB_INTERNET_HTML that can be passed to PDDA.
	-- If a court does not have a valid record to be sent to PDDA then no records will be returned for this court.
	-- i.e. per court will either be 0 or 1 records returned.
    -- It will look only for records from today. If the most recent record for a specific court
	-- has a STATUS='P' then no record will be retrieved from this court.
	-- If no courts have a record to return then no data will be returned from this function.
	FUNCTION get_iwp_all_eligible_courts
	(
	  p_for_date IN DATE DEFAULT SYSDATE
	)
	  RETURN SYS_REFCURSOR
	AS
	  l_rc SYS_REFCURSOR;
	BEGIN
	  OPEN l_rc FOR
		WITH c_today AS (
		  SELECT x.*,
				 ROW_NUMBER() OVER (
				   PARTITION BY x.court_id
				   ORDER BY x.last_update_date DESC, x.internet_html_id DESC
				 ) AS rn
		  FROM   xhb_internet_html x
		  WHERE  x.status = 'C'
			AND  x.last_update_date >= TRUNC(p_for_date)
			AND  x.last_update_date <  TRUNC(p_for_date) + 1
		)
		SELECT c.*
		FROM   c_today c
		WHERE  c.rn = 1
		  AND  NOT EXISTS (
				 SELECT 1
				 FROM   xhb_internet_html p
				 WHERE  p.court_id = c.court_id
				   AND  p.status   = 'P'
				   AND  p.last_update_date > c.last_update_date
			   );

	  RETURN l_rc;
	END get_iwp_all_eligible_courts;

END XHB_PDDA_PKG;
/
