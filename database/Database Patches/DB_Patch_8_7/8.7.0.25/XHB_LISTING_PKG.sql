CREATE OR REPLACE PACKAGE XHB_LISTING_PKG AS 

  PROCEDURE get_list(p_results_out   OUT SYS_REFCURSOR,
                      p_court_id     IN  XHB_LIST_LAST_UPDATED_V.COURT_ID%TYPE,
                      p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                      p_from_date    IN  XHB_LIST_LAST_UPDATED_V.LIST_START_DATE%TYPE,
                      p_to_date      IN  XHB_LIST_LAST_UPDATED_V.LIST_START_DATE%TYPE); 

  PROCEDURE find_cases_for_list_results( p_results_out   OUT SYS_REFCURSOR,
                      p_list_date     	IN  XHB_LIST.LIST_START_DATE%TYPE,
					  p_site_id			IN	XHB_COURT_SITE.COURT_SITE_ID%TYPE,
					  p_room_id			IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
					  p_court_id     	IN  XHB_COURT_SITE.COURT_ID%TYPE,
					  p_all_sites		IN 	NUMBER,
					  p_all_rooms		IN 	NUMBER,
					  p_floater_rooms	IN	NUMBER);
                      
  PROCEDURE get_case_listing_information( p_results_out   OUT SYS_REFCURSOR,
                      p_case_id     IN  XHB_CASE_ON_LIST.CASE_ID%TYPE);             

  -- Get next SittingOnListId
  FUNCTION get_next_SOL_id RETURN XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE;

  -- Get next CaseOnListId
  FUNCTION get_next_COL_id RETURN XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE;

END XHB_LISTING_PKG;
/


CREATE OR REPLACE PACKAGE BODY XHB_LISTING_PKG AS

  PROCEDURE get_list(p_results_out             OUT SYS_REFCURSOR,
                            p_court_id     IN  XHB_LIST_LAST_UPDATED_V.COURT_ID%TYPE,
                            p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                            p_from_date  IN    XHB_LIST_LAST_UPDATED_V.LIST_START_DATE%TYPE,
                            p_to_date    IN    XHB_LIST_LAST_UPDATED_V.LIST_START_DATE%TYPE) AS
  BEGIN
       OPEN p_results_out FOR
         SELECT xlluv.*, xlluv.list_id "ID" FROM XHB_LIST_LAST_UPDATED_V xlluv, XHB_REF_LISTING_DATA xrld
          WHERE xlluv.COURT_ID = p_court_id
          AND xrld.REF_LISTING_DATA_ID = xlluv.LIST_TYPE_ID
          AND xrld.REF_DATA_TYPE = 'LIST_TYPE'
          AND xrld.REF_DATA_VALUE = p_list_type
          AND xlluv.LIST_START_DATE >= p_from_date 
          AND xlluv.LIST_START_DATE <= p_to_date; 
  END get_list;
  
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
  
  PROCEDURE get_case_listing_information( p_results_out   OUT SYS_REFCURSOR,
                      p_case_id     IN  XHB_CASE_ON_LIST.CASE_ID%TYPE) AS
      BEGIN
        OPEN p_results_out FOR
          SELECT xlluv.LIST_START_DATE,
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
      
  END get_case_listing_information;
                      
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

END XHB_LISTING_PKG;
/
