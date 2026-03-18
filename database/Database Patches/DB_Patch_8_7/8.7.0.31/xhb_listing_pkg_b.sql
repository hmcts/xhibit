CREATE OR REPLACE PACKAGE BODY XHB_LISTING_PKG AS

  FUNCTION get_list_type_id(p_list_type IN XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE) 
    RETURN XHB_REF_LISTING_DATA.REF_LISTING_DATA_ID%TYPE IS
    v_list_type_id XHB_REF_LISTING_DATA.REF_LISTING_DATA_ID%TYPE;
    CURSOR C_xrld IS
    SELECT xrld.REF_LISTING_DATA_ID
      FROM XHB_REF_LISTING_DATA xrld
     WHERE xrld.REF_DATA_TYPE = 'LIST_TYPE'
       AND xrld.REF_DATA_VALUE = p_list_type;    
  BEGIN
    OPEN C_xrld;
    FETCH C_xrld INTO v_list_type_id; 
    CLOSE C_xrld;
    RETURN v_list_type_id;
  END get_list_type_id;
  
  
  PROCEDURE delete_list ( p_list_id IN XHB_LIST.LIST_ID%TYPE ) AS
  BEGIN
	UPDATE XHB_CASE_ON_LIST SET OBS_IND = 'Y' WHERE LIST_ID = p_list_id;
	UPDATE XHB_SITTING_ON_LIST SET OBS_IND = 'Y' WHERE LIST_ID = p_list_id;
	UPDATE XHB_LIST SET OBS_IND = 'Y', PUBLISH_STATUS = 'DELETED' WHERE LIST_ID = p_list_id;
    UPDATE XHB_DEF_ON_CASE_ON_LIST XD SET XD.OBS_IND = 'Y' WHERE XD.CASE_ON_LIST_ID IN 
      (SELECT XC.CASE_ON_LIST_ID FROM XHB_CASE_ON_LIST XC WHERE XC.LIST_ID = p_list_id);
  END delete_list;
  
  PROCEDURE find_cases_for_list_results( p_results_out   OUT SYS_REFCURSOR,
                      p_list_date     	IN  XHB_LIST.LIST_START_DATE%TYPE,
					  p_site_id			IN	XHB_COURT_SITE.COURT_SITE_ID%TYPE,
					  p_room_id			IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
					  p_court_id     	IN  XHB_COURT_SITE.COURT_ID%TYPE,
					  p_all_sites		IN 	NUMBER,
					  p_all_rooms		IN 	NUMBER,
					  p_floater_rooms	IN	NUMBER) AS
      BEGIN
	  
	  IF p_all_sites = 1 THEN
		-- Retrieve cases for all sites at the court
		OPEN p_results_out FOR
			SELECT 	XC.CASE_TYPE || XC.CASE_NUMBER AS CASE_NUMBER, 
					XCOL.CASE_ID,
					XC.CASE_TITLE, 
					XCS.COURT_SITE_NAME, 
					XCR.DISPLAY_NAME AS COURT_ROOM_NAME, 
					(SELECT DECODE(COUNT(*), 0, 'No', 'Yes') 
						FROM XHB_HEARING XH 
						WHERE XH.CASE_ID = XCOL.CASE_ID 
						AND XH.HEARING_START_DATE > SYSDATE) AS FUTURE_HEARINGS,
					XC.CASE_STATUS, 
					XRHT.HEARING_TYPE_CODE AS HEARING_TYPE, 
					DECODE( NVL(XCOL.CRACKED_INEFFECTIVE_ID,0),
						0,'Not Applicable',
						XRCE.CODE || ' - ' || XRCE.DESCRIPTION) AS EFFECTIVE_CRACKED_INEFFECTIVE
			FROM 	XHB_CASE_ON_LIST XCOL, 
					XHB_CASE XC, 
					XHB_COURT_SITE XCS, 
					XHB_COURT_ROOM XCR, 
					XHB_REF_HEARING_TYPE XRHT, 
					XHB_LIST XL,
					XHB_REF_CRACKED_EFFECTIVE XRCE
			WHERE 	XCOL.COURT_SITE_ID IN (SELECT T.COURT_SITE_ID FROM XHB_COURT_SITE T WHERE T.COURT_ID = p_court_id)
			AND		XC.CASE_ID = XCOL.CASE_ID
			AND 	XCS.COURT_SITE_ID = XCOL.COURT_SITE_ID
			AND 	XCR.COURT_ROOM_ID = XCOL.COURT_ROOM_ID
			AND 	XRHT.REF_HEARING_TYPE_ID = XCOL.HEARING_TYPE_ID
			AND 	XL.LIST_ID = XCOL.LIST_ID
			AND		p_list_date BETWEEN XL.LIST_START_DATE AND XL.LIST_END_DATE
			AND   	XRCE.REF_CRACKED_EFFECTIVE_ID (+)= XCOL.CRACKED_INEFFECTIVE_ID
			ORDER BY XC.CASE_TITLE;
	  
	  ELSIF p_all_rooms = 1 THEN
		-- Retrieve cases for all rooms at the court site
		OPEN p_results_out FOR
			SELECT 	XC.CASE_TYPE || XC.CASE_NUMBER AS CASE_NUMBER, 
					XCOL.CASE_ID,
					XC.CASE_TITLE, 
					XCS.COURT_SITE_NAME, 
					XCR.DISPLAY_NAME AS COURT_ROOM_NAME, 
					(SELECT DECODE(COUNT(*), 0, 'No', 'Yes') 
						FROM XHB_HEARING XH 
						WHERE XH.CASE_ID = XCOL.CASE_ID 
						AND XH.HEARING_START_DATE > SYSDATE) AS FUTURE_HEARINGS,
					XC.CASE_STATUS, 
					XRHT.HEARING_TYPE_CODE AS HEARING_TYPE, 
					DECODE( NVL(XCOL.CRACKED_INEFFECTIVE_ID,0),
						0,'Not Applicable',
						XRCE.CODE || ' - ' || XRCE.DESCRIPTION) AS EFFECTIVE_CRACKED_INEFFECTIVE
			FROM 	XHB_CASE_ON_LIST XCOL, 
					XHB_CASE XC, 
					XHB_COURT_SITE XCS, 
					XHB_COURT_ROOM XCR, 
					XHB_REF_HEARING_TYPE XRHT, 
					XHB_LIST XL,
					XHB_REF_CRACKED_EFFECTIVE XRCE
			WHERE 	XCOL.COURT_SITE_ID = p_site_id
			AND		XC.CASE_ID = XCOL.CASE_ID
			AND 	XCS.COURT_SITE_ID = XCOL.COURT_SITE_ID
			AND 	XCR.COURT_ROOM_ID = XCOL.COURT_ROOM_ID
			AND 	XRHT.REF_HEARING_TYPE_ID = XCOL.HEARING_TYPE_ID
			AND 	XL.LIST_ID = XCOL.LIST_ID
			AND		p_list_date BETWEEN XL.LIST_START_DATE AND XL.LIST_END_DATE
			AND   	XRCE.REF_CRACKED_EFFECTIVE_ID (+)= XCOL.CRACKED_INEFFECTIVE_ID
			ORDER BY XC.CASE_TITLE;
	  
	  ELSIF p_floater_rooms = 1 THEN
		-- Retrieve all cases assigned to the Floater Cases folder in the List for the specified Court Site
		OPEN p_results_out FOR
			SELECT 	XC.CASE_TYPE || XC.CASE_NUMBER AS CASE_NUMBER, 
					XCOL.CASE_ID,
					XC.CASE_TITLE, 
					XCS.COURT_SITE_NAME, 
					XCR.DISPLAY_NAME AS COURT_ROOM_NAME, 
					(SELECT DECODE(COUNT(*), 0, 'No', 'Yes') 
						FROM XHB_HEARING XH 
						WHERE XH.CASE_ID = XCOL.CASE_ID 
						AND XH.HEARING_START_DATE > SYSDATE) AS FUTURE_HEARINGS,
					XC.CASE_STATUS, 
					XRHT.HEARING_TYPE_CODE AS HEARING_TYPE, 
					DECODE( NVL(XCOL.CRACKED_INEFFECTIVE_ID,0),
						0,'Not Applicable',
						XRCE.CODE || ' - ' || XRCE.DESCRIPTION) AS EFFECTIVE_CRACKED_INEFFECTIVE
			FROM 	XHB_CASE_ON_LIST XCOL, 
					XHB_CASE XC, 
					XHB_COURT_SITE XCS, 
					XHB_COURT_ROOM XCR, 
					XHB_REF_HEARING_TYPE XRHT, 
					XHB_LIST XL,
					XHB_REF_CRACKED_EFFECTIVE XRCE
			WHERE 	XCOL.COURT_SITE_ID = p_site_id
			AND		NVL(XCOL.FLOATER_CASE, 'N') = 'Y'
			AND		XC.CASE_ID = XCOL.CASE_ID
			AND 	XCS.COURT_SITE_ID = XCOL.COURT_SITE_ID
			AND 	XCR.COURT_ROOM_ID = XCOL.COURT_ROOM_ID
			AND 	XRHT.REF_HEARING_TYPE_ID = XCOL.HEARING_TYPE_ID
			AND 	XL.LIST_ID = XCOL.LIST_ID
			AND		p_list_date BETWEEN XL.LIST_START_DATE AND XL.LIST_END_DATE
			AND   	XRCE.REF_CRACKED_EFFECTIVE_ID (+)= XCOL.CRACKED_INEFFECTIVE_ID
			ORDER BY XC.CASE_TITLE;
	  
	  ELSE
		-- Retrieve cases for a specific court site / court room
		OPEN p_results_out FOR
			SELECT 	XC.CASE_TYPE || XC.CASE_NUMBER AS CASE_NUMBER,
					XCOL.CASE_ID,
					XC.CASE_TITLE, 
					XCS.COURT_SITE_NAME, 
					XCR.DISPLAY_NAME AS COURT_ROOM_NAME, 
					(SELECT DECODE(COUNT(*), 0, 'No', 'Yes') 
						FROM XHB_HEARING XH 
						WHERE XH.CASE_ID = XCOL.CASE_ID 
						AND XH.HEARING_START_DATE > SYSDATE) AS FUTURE_HEARINGS,
					XC.CASE_STATUS, 
					XRHT.HEARING_TYPE_CODE AS HEARING_TYPE, 
					DECODE( NVL(XCOL.CRACKED_INEFFECTIVE_ID,0),
						0,'Not Applicable',
						XRCE.CODE || ' - ' || XRCE.DESCRIPTION) AS EFFECTIVE_CRACKED_INEFFECTIVE
			FROM 	XHB_CASE_ON_LIST XCOL, 
					XHB_CASE XC, 
					XHB_COURT_SITE XCS, 
					XHB_COURT_ROOM XCR, 
					XHB_REF_HEARING_TYPE XRHT, 
					XHB_LIST XL,
					XHB_REF_CRACKED_EFFECTIVE XRCE
			WHERE 	XCOL.COURT_SITE_ID = p_site_id
			AND		XCOL.COURT_ROOM_ID = p_room_id
			AND		XC.CASE_ID = XCOL.CASE_ID
			AND 	XCS.COURT_SITE_ID = XCOL.COURT_SITE_ID
			AND 	XCR.COURT_ROOM_ID = XCOL.COURT_ROOM_ID
			AND 	XRHT.REF_HEARING_TYPE_ID = XCOL.HEARING_TYPE_ID
			AND 	XL.LIST_ID = XCOL.LIST_ID
			AND		p_list_date BETWEEN XL.LIST_START_DATE AND XL.LIST_END_DATE
			AND   	XRCE.REF_CRACKED_EFFECTIVE_ID (+)= XCOL.CRACKED_INEFFECTIVE_ID
			ORDER BY XC.CASE_TITLE;
	  
	  END IF;
      
  END find_cases_for_list_results;
  
	PROCEDURE get_case_list_history( p_results_out   OUT SYS_REFCURSOR,
                      p_case_id     IN  XHB_CASE_ON_LIST.CASE_ID%TYPE) AS
      BEGIN
        OPEN p_results_out FOR
          SELECT xcol.CASE_ON_LIST_ID,
                xlluv.LIST_START_DATE,
                xlluv.LIST_END_DATE,
                xrld.REF_DATA_VALUE as LIST_TYPE,
                xcol.REASON_FOR_REMOVAL
          FROM XHB_CASE_ON_LIST xcol,
               XHB_LIST_LAST_UPDATED_V xlluv,
               XHB_REF_LISTING_DATA xrld
          WHERE xcol.CASE_ID = p_case_id
          AND xlluv.LIST_ID = xcol.LIST_ID
          AND xrld.REF_LISTING_DATA_ID = xlluv.LIST_TYPE_ID
          AND xcol.LIST_ID = xlluv.LIST_ID
          AND xrld.REF_DATA_TYPE = 'LIST_TYPE';
      
  END get_case_list_history;
  
    PROCEDURE get_defendants_on_list( p_results_out   OUT SYS_REFCURSOR,
                                    p_case_on_list_id     IN  XHB_DEF_ON_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE) AS
      BEGIN
        OPEN p_results_out FOR
          SELECT xd.*, xdoc.*
          FROM XHB_DEF_ON_CASE_ON_LIST xdonol,
               XHB_DEFENDANT_ON_CASE xdoc,
               XHB_DEFENDANT xd
          WHERE xdonol.CASE_ON_LIST_ID = p_case_on_list_id
          AND xdonol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
          AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID;
      
  END get_defendants_on_list;
   

  FUNCTION get_next_SOL_id RETURN XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE AS
    v_result XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE;
  BEGIN
    SELECT XHB_SITTING_ON_LIST_SEQ.NEXTVAL INTO v_result FROM DUAL;
    RETURN v_result;
  END get_next_SOL_id;

  FUNCTION get_next_COL_id RETURN XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE AS
    v_result XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE;
  BEGIN
    SELECT XHB_CASE_ON_LIST_SEQ.NEXTVAL INTO v_result FROM DUAL;
    RETURN v_result;
  END get_next_COL_id;

  PROCEDURE get_list_in_date_range(p_results_out  OUT SYS_REFCURSOR,
                                   p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                   p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                                   p_from_date    IN  XHB_LIST.LIST_START_DATE%TYPE,
                                   p_to_date      IN  XHB_LIST.LIST_END_DATE%TYPE) AS
    v_list_type_id XHB_REF_LISTING_DATA.REF_LISTING_DATA_ID%TYPE;
  BEGIN
    v_list_type_id := get_list_type_id(p_list_type => p_list_type);
    OPEN p_results_out FOR
       SELECT xlluv.*, xlluv.list_id "ID" FROM XHB_LIST_LAST_UPDATED_V xlluv
        WHERE xlluv.COURT_ID = p_court_id
          AND xlluv.LIST_TYPE_ID = v_list_type_id
          AND xlluv.LIST_START_DATE <= p_to_date 
          AND xlluv.LIST_END_DATE >= p_from_date; 
  END get_list_in_date_range;

  PROCEDURE get_latest_daily_list(p_results_out  OUT SYS_REFCURSOR,
                                  p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                  p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                                  p_diary_date   IN  XHB_LIST.LIST_START_DATE%TYPE) IS
    v_list_type_id XHB_REF_LISTING_DATA.REF_LISTING_DATA_ID%TYPE;
  BEGIN
    v_list_type_id := get_list_type_id(p_list_type => p_list_type);
    IF p_list_type = 'Daily' THEN 
       OPEN p_results_out FOR
          SELECT subquery.* FROM  
          (SELECT xl.*, xl.list_id "ID" FROM XHB_LIST_LAST_UPDATED_V xl
           WHERE xl.COURT_ID = p_court_id
             AND xl.LIST_TYPE_ID = v_list_type_id
             AND xl.LIST_START_DATE <= p_diary_date
           ORDER BY xl.LIST_START_DATE DESC) subquery
           WHERE ROWNUM = 1;
    ELSE 
       OPEN p_results_out FOR
          SELECT subquery.* FROM  
          (SELECT xl.*, xl.list_id "ID" FROM XHB_LIST_LAST_UPDATED_V xl
           WHERE xl.COURT_ID = p_court_id
             AND xl.LIST_TYPE_ID = v_list_type_id
             AND p_diary_date BETWEEN xl.LIST_START_DATE AND xl.LIST_END_DATE
           ORDER BY xl.LIST_START_DATE DESC) subquery
           WHERE ROWNUM = 1;
    END IF;
  END get_latest_daily_list;

  PROCEDURE get_final_list_for_date_daily(p_results_out  OUT SYS_REFCURSOR,
                                   p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                                   p_diary_date   IN  XHB_LIST.LIST_START_DATE%TYPE) AS
    v_list_type_id XHB_REF_LISTING_DATA.REF_LISTING_DATA_ID%TYPE;
  BEGIN
    v_list_type_id := get_list_type_id(p_list_type => 'Daily');
    OPEN p_results_out FOR
       SELECT xlluv.*, xlluv.list_id "ID" FROM XHB_LIST_LAST_UPDATED_V xlluv
        WHERE xlluv.COURT_ID = p_court_id
          AND xlluv.LIST_TYPE_ID = v_list_type_id
		  AND xlluv.DRAFT_OR_FINAL = 'F'
          AND xlluv.LIST_START_DATE = p_diary_date; 
  END get_final_list_for_date_daily;
  
  PROCEDURE get_list(p_results_out  OUT SYS_REFCURSOR,
                     p_court_id     IN  XHB_LIST.COURT_ID%TYPE,
                     p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                     p_row_limit    IN  NUMBER) AS
    v_list_type_id XHB_REF_LISTING_DATA.REF_LISTING_DATA_ID%TYPE;
  BEGIN
    v_list_type_id := get_list_type_id(p_list_type => p_list_type);
    OPEN p_results_out FOR
         SELECT subquery.* FROM  
         (SELECT xl.*, xl.list_id "ID" FROM XHB_LIST_LAST_UPDATED_V xl
          WHERE xl.COURT_ID = p_court_id
          AND xl.LIST_TYPE_ID = v_list_type_id
          ORDER BY xl.LIST_START_DATE DESC) subquery
         WHERE (ROWNUM <= p_row_limit OR p_row_limit IS NULL);
  END get_list;

  FUNCTION validate_lists_for_fixture (p_case_id IN XHB_CASE.CASE_ID%TYPE,
                                       p_fixture_date IN XHB_LIST.LIST_START_DATE%TYPE) RETURN VARCHAR2 AS
    v_return_code VARCHAR2(10) := 'NONE';
    n_count       NUMBER := 0;
  BEGIN
    -- Determine if the case exists on any Warned lists in this period
    SELECT COUNT(*) INTO n_count
    FROM xhb_case_on_list xcol, xhb_list xl, xhb_ref_listing_data xrld
    WHERE xcol.case_id = p_case_id
    AND NVL(xcol.obs_ind,'N') = 'N'
    AND xl.list_id = xcol.list_id
    AND p_fixture_date BETWEEN xl.list_start_date AND xl.list_end_date
    AND NVL(xl.obs_ind,'N') = 'N'
    AND xl.list_type_id = xrld.ref_listing_data_id
    AND xrld.ref_data_type = 'LIST_TYPE'
    AND xrld.ref_data_value = 'Warned';
    
    IF n_count > 0 THEN
      -- The case exists on a warned list in this period
      v_return_code := 'WARNED';
    ELSE
      -- Determine if the case exists on any reserved Firm lists in this period
      SELECT COUNT(*) INTO n_count
      FROM xhb_case_on_list xcol, xhb_list xl, xhb_ref_listing_data xrld
      WHERE xcol.case_id = p_case_id
      AND NVL(xcol.reserved,'N') = 'Y'
      AND NVL(xcol.obs_ind,'N') = 'N'
      AND xl.list_id = xcol.list_id
      AND p_fixture_date BETWEEN xl.list_start_date AND xl.list_end_date
      AND NVL(xl.obs_ind,'N') = 'N'
      AND xl.list_type_id = xrld.ref_listing_data_id
      AND xrld.ref_data_type = 'LIST_TYPE'
      AND xrld.ref_data_value = 'Firm';
      
      IF n_count > 0 THEN
        -- The case exists on a reserved firm list in this period
        v_return_code := 'FIRM';
      END IF;
    
    END IF;
    
    RETURN v_return_code;
  END validate_lists_for_fixture;
  
END XHB_LISTING_PKG;
/
show errors