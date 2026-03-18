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
    
  FUNCTION get_removal_reason(p_ref_system_code_id IN XHB_REF_SYSTEM_CODE.REF_SYSTEM_CODE_ID%TYPE,
                              p_freetext           IN VARCHAR2) 
    RETURN VARCHAR2 IS
    v_result VARCHAR2(250);
    CURSOR C_xrsc IS
    SELECT RTRIM(xsrc.DE_CODE)||CASE WHEN p_freetext IS NOT NULL THEN ' '||p_freetext END removal_reason
      FROM XHB_REF_SYSTEM_CODE xsrc
     WHERE xsrc.REF_SYSTEM_CODE_ID = p_ref_system_code_id;
  BEGIN
    IF p_ref_system_code_id IS NOT NULL THEN
       OPEN C_xrsc;
       FETCH C_xrsc INTO v_result;
       CLOSE C_xrsc;
    ELSE
       v_result := p_freetext;
    END IF;
    RETURN v_result;
  END get_removal_reason;
  
  PROCEDURE delete_list ( p_list_id IN XHB_LIST.LIST_ID%TYPE ) AS
  BEGIN
	UPDATE XHB_CASE_ON_LIST SET OBS_IND = 'Y' WHERE LIST_ID = p_list_id;
	UPDATE XHB_SITTING_ON_LIST SET OBS_IND = 'Y' WHERE LIST_ID = p_list_id;
	UPDATE XHB_LIST SET OBS_IND = 'Y', PUBLISH_STATUS = 'DELETED' WHERE LIST_ID = p_list_id;
    UPDATE XHB_DEF_ON_CASE_ON_LIST XD SET XD.OBS_IND = 'Y' WHERE XD.CASE_ON_LIST_ID IN 
      (SELECT XC.CASE_ON_LIST_ID FROM XHB_CASE_ON_LIST XC WHERE XC.LIST_ID = p_list_id);
  END delete_list;
  
  FUNCTION case_on_list_exists_YN(p_case_id IN XHB_CASE_ON_LIST.CASE_ID%TYPE,
                                  p_date    IN XHB_CASE_ON_LIST.TIME_LISTED%TYPE DEFAULT NULL)
    RETURN VARCHAR2 IS
    v_result VARCHAR2(1);
    CURSOR C_xcol IS
    SELECT 'Y'
      FROM XHB_CASE_ON_LIST XCOL 
     WHERE XCOL.CASE_ID = p_case_id 
       AND NVL(XCOL.OBS_IND,'N') = 'N'
       AND XCOL.TIME_LISTED > p_date
       AND ROWNUM = 1;
    CURSOR C_xcol_any_date IS
    SELECT 'Y'
      FROM XHB_CASE_ON_LIST XCOL 
     WHERE XCOL.CASE_ID = p_case_id 
       AND NVL(XCOL.OBS_IND,'N') = 'N'
       AND ROWNUM = 1;
  BEGIN
    IF p_date IS NOT NULL THEN 
        OPEN C_xcol;
        FETCH C_xcol INTO v_result;
        CLOSE C_xcol;
    ELSE
        OPEN C_xcol_any_date;
        FETCH C_xcol_any_date INTO v_result;
        CLOSE C_xcol_any_date;
    END IF;
    RETURN NVL(v_result,'N');
  END case_on_list_exists_YN;
  
  FUNCTION fixtures_exist_YN(p_case_listing_entry_id IN XHB_CASE_DIARY_FIXTURE.CASE_LISTING_ENTRY_ID%TYPE,
                             p_date                  IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE)
    RETURN VARCHAR2 IS
    v_result VARCHAR2(1);
    CURSOR C_xcdf IS
    SELECT 'Y'
      FROM XHB_CASE_DIARY_FIXTURE xcdf 
     WHERE xcdf.CASE_LISTING_ENTRY_ID = p_case_listing_entry_id 
       AND NVL(xcdf.OBS_IND,'N') = 'N'
       AND xcdf.LISTING_DATE > p_date
       AND ROWNUM = 1;
  BEGIN
    IF p_case_listing_entry_id IS NOT NULL THEN 
       OPEN C_xcdf;
       FETCH C_xcdf INTO v_result;
       CLOSE C_xcdf;
    END IF;   
    RETURN NVL(v_result,'N');
  END fixtures_exist_YN;
  
  PROCEDURE find_cases_for_list_results( p_results_out   OUT SYS_REFCURSOR,
                      p_list_date     	IN  XHB_LIST.LIST_START_DATE%TYPE,
					  p_site_id			IN	XHB_COURT_SITE.COURT_SITE_ID%TYPE,
					  p_room_id			IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
					  p_court_id     	IN  XHB_COURT_SITE.COURT_ID%TYPE,
					  p_mode			IN 	NUMBER) AS
      BEGIN
	  
		OPEN p_results_out FOR
    SELECT DISTINCT 
				subqry.CASE_ID,
				subqry.CASE_NUMBER,
				subqry.CASE_TITLE, 
				subqry.COURT_SITE_CODE, 
				subqry.COURT_ROOM_NO, 
				subqry.SITTING_SEQUENCE_NO,
				subqry.IS_FLOATING,
				subqry.FUTURE_HEARINGS_XCOL,
				subqry.FUTURE_HEARINGS_XCDF,
				subqry.CASE_STATUS, 
				XRHT.HEARING_TYPE_CODE AS HEARING_TYPE, 
				CASE WHEN subqry.REF_CRACKED_EFFECTIVE_ID != 0 THEN subqry.REF_CRACKED_EFF_DESC
					 WHEN subqry.CASE_TYPE = 'T' AND XRHT.HEARING_TYPE_CODE IN ('TRL','TFL','TBK') THEN 'Missing'
					 ELSE 'Not Applicable' END AS EFFECTIVE_CRACKED_INEFFECTIVE,
				subqry.DEFAULT_HEARING_TYPE,
				subqry.TRIAL_TIME_ESTIMATE,
				subqry.TRIAL_TIME_UNIT,
				subqry.JUDGE_ID,
				subqry.CASE_LISTING_ENTRY_ID,
				subqry.DIRECTIONS_FOR_CASE_ID
    FROM (
    SELECT	XC.CASE_ID,
				XC.CASE_TYPE,
				XC.CASE_TYPE || XC.CASE_NUMBER AS CASE_NUMBER, 
				XC.CASE_TITLE, 
				XCS.COURT_SITE_CODE, 
				XCR.CREST_COURT_ROOM_NO AS COURT_ROOM_NO, 
				NVL(XS.SITTING_SEQUENCE_NO,-1) AS SITTING_SEQUENCE_NO,
				XS.IS_FLOATING,
				CASE WHEN case_on_list_exists_YN(XC.CASE_ID,SYSDATE) = 'Y' 
					THEN 'Yes' ELSE 'No' END AS FUTURE_HEARINGS_XCOL,
				CASE WHEN fixtures_exist_YN(XCLE.CASE_LISTING_ENTRY_ID,SYSDATE) = 'Y' 
					THEN 'Yes' ELSE 'No' END AS FUTURE_HEARINGS_XCDF,
				XHB_CASE_PKG.DETERMINE_CASE_STATUS(XC.CASE_ID) AS CASE_STATUS, 
				XH.REF_HEARING_TYPE_ID,
				XHL.CREST_LIST_ID,
				NVL(XSH.REF_CRACKED_EFFECTIVE_ID,0) REF_CRACKED_EFFECTIVE_ID,
				XRCE.DESCRIPTION REF_CRACKED_EFF_DESC,
				NVL(XC.DEFAULT_HEARING_TYPE,-1) AS DEFAULT_HEARING_TYPE,
				NVL(XDFC.TRIAL_TIME_ESTIMATE,-1) AS TRIAL_TIME_ESTIMATE,
				NVL(XDFC.TRIAL_TIME_UNIT,-1) AS TRIAL_TIME_UNIT,
				NVL(XCLE.JUDGE_ID,-1) AS JUDGE_ID,
				NVL(XCLE.CASE_LISTING_ENTRY_ID,-1) AS CASE_LISTING_ENTRY_ID,
				NVL(XDFC.DIRECTIONS_FOR_CASE_ID,-1) AS DIRECTIONS_FOR_CASE_ID
		FROM	XHB_HEARING_LIST XHL,
				XHB_HEARING XH,
				XHB_CASE XC,
				XHB_COURT_SITE XCS, 
				XHB_COURT_ROOM XCR, 
				XHB_SITTING XS,
				XHB_SCHEDULED_HEARING XSH,
				XHB_DIRECTIONS_FOR_CASE XDFC,
				XHB_CASE_LISTING_ENTRY XCLE,
				XHB_REF_CRACKED_EFFECTIVE XRCE
		WHERE	XHL.START_DATE IS NOT NULL
		AND		XHL.COURT_ID = p_court_id
		AND		XHL.START_DATE = p_list_date
		AND		XS.LIST_ID = XHL.LIST_ID
		AND		XSH.SITTING_ID = XS.SITTING_ID
		AND		XH.HEARING_ID = XSH.HEARING_ID
		AND		XC.CASE_ID = XH.CASE_ID
		AND		XC.CASE_TYPE IN ('A','S','T')
		AND		XDFC.CASE_ID (+)= XC.CASE_ID
		AND		XCLE.CASE_ID (+)= XC.CASE_ID
		AND		NVL(XCLE.OBS_IND(+), 'N') = 'N'
		AND		(	(p_mode = 1 )
				OR	(p_mode = 2 AND XS.IS_FLOATING = 1 AND XS.COURT_SITE_ID = p_site_id)
				OR	(p_mode = 3 AND XS.COURT_SITE_ID = p_site_id)
				OR	(p_mode = 4 AND XS.COURT_SITE_ID = p_site_id AND XS.COURT_ROOM_ID = p_room_id AND XS.IS_FLOATING != 1)
				)
		AND 	XCS.COURT_ID = XHL.COURT_ID
		AND 	XCS.COURT_SITE_ID = XS.COURT_SITE_ID
		AND		NVL(XCS.OBS_IND(+), 'N') = 'N'
		AND 	XCR.COURT_SITE_ID = XS.COURT_SITE_ID
		AND 	XCR.COURT_ROOM_ID = XS.COURT_ROOM_ID
		AND		NVL(XCR.OBS_IND(+), 'N') = 'N'
		AND		XRCE.REF_CRACKED_EFFECTIVE_ID (+)= XSH.REF_CRACKED_EFFECTIVE_ID) subqry,
			XHB_CASE_ON_LIST XCOL,
			XHB_REF_HEARING_TYPE XRHT
		WHERE XCOL.CASE_ID(+) = subqry.CASE_ID
		AND XCOL.LIST_ID(+) = subqry.CREST_LIST_ID
		AND NVL(XCOL.OBS_IND(+),'N') = 'N'
		AND XRHT.REF_HEARING_TYPE_ID = NVL(XCOL.HEARING_TYPE_ID,subqry.REF_HEARING_TYPE_ID)
		ORDER BY subqry.COURT_SITE_CODE, subqry.IS_FLOATING, subqry.COURT_ROOM_NO, subqry.SITTING_SEQUENCE_NO;
	  
  END find_cases_for_list_results;
  
  PROCEDURE get_cases_by_filter(p_results_out OUT SYS_REFCURSOR,
              p_court_id IN XHB_CASE.COURT_ID%TYPE,
						  p_CASE_TYPE IN VARCHAR2,
						  p_CASE_CLASS IN VARCHAR2,
						  p_BC_STATUS IN VARCHAR2,
						  p_DEFAULT_HEARING_TYPE IN xhb_ref_hearing_type.HEARING_TYPE_CODE%TYPE,
						  p_TIME_EST_FROM IN NUMBER,
						  p_TIME_EST_TO IN NUMBER,
						  p_UNITS IN NUMBER,
						  p_REQUIRED_JUDGE_TYPE IN xhb_case_listing_entry.REF_JUDGE_TYPE_ID%TYPE,
						  p_UNITS_WEEKS IN NUMBER,
						  p_SECURE_COURTROOM IN VARCHAR2,
						  p_JUVENILE_ONLY IN VARCHAR2
						 ) AS
  BEGIN
    OPEN p_results_out FOR
    SELECT  xc.case_id
    FROM  XHB_CASE xc,
          xhb_case_listing_entry xcle
    WHERE  xc.COURT_ID = p_court_id
    AND    xcle.case_id(+) = xc.case_id
    AND    NVL(xcle.OBS_IND(+),'N') = 'N'
    AND    NVL(fixtures_exist_YN(xcle.case_listing_entry_id(+),SYSDATE),'N') = 'N'
    AND    NVL(case_on_list_exists_YN(xc.case_id),'N') = 'N'
    AND    XHB_CASE_PKG.determine_case_status(xc.CASE_ID) =  'Open' 
    AND    CASE WHEN ((INSTR(p_CASE_CLASS, '1') > 0 AND xc.CLASS_CODE = 1) OR
                      (INSTR(p_CASE_CLASS, '2') > 0 AND xc.CLASS_CODE = 2) OR
                      (INSTR(p_CASE_CLASS, '3') > 0 AND xc.CLASS_CODE = 3)) THEN 'Y' 
			     WHEN p_CASE_CLASS IS NULL THEN 'Y'	 
			     ELSE 'N' END = 'Y'
    AND      (p_CASE_TYPE IS NULL OR xc.CASE_TYPE = p_CASE_TYPE)
    AND      (p_BC_STATUS IS NULL OR (p_BC_STATUS IS NOT NULL AND check_def_bc_status_on_case(xc.CASE_ID, p_BC_STATUS) = 'Y') )
    AND      (p_DEFAULT_HEARING_TYPE IS NULL OR 
              EXISTS (SELECT 1 FROM XHB_REF_HEARING_TYPE xrht
                       WHERE xc.DEFAULT_HEARING_TYPE = xrht.REF_HEARING_TYPE_ID
                         AND NVL(xrht.HEARING_TYPE_CODE, '~') = p_DEFAULT_HEARING_TYPE))  
    AND      (p_TIME_EST_FROM IS NULL OR 
              EXISTS (SELECT 1 FROM XHB_DIRECTIONS_FOR_CASE xdfc 
                       WHERE xc.CASE_ID = xdfc.CASE_ID 
                         AND xdfc.TRIAL_TIME_ESTIMATE BETWEEN p_TIME_EST_FROM AND p_TIME_EST_TO
                         AND xdfc.TRIAL_TIME_UNIT = p_UNITS
                         AND ROWNUM = 1))
    AND      (p_REQUIRED_JUDGE_TYPE IS NULL OR p_REQUIRED_JUDGE_TYPE = xcle.ref_judge_type_id)
    AND      (p_UNITS_WEEKS IS NULL OR  (TRUNC(sysdate) - (p_UNITS_WEEKS * 7) >= TRUNC( NVL(NVL(xc.COMMITTAL_DATE, xc.SENT_FOR_TRIAL_DATE), xc.APPEAL_LODGED_DATE))))
    AND      (p_SECURE_COURTROOM  = 'N' OR xc.SECURE_COURT = p_SECURE_COURTROOM )
    AND      (p_JUVENILE_ONLY = 'N' OR EXISTS (SELECT 1 FROM xhb_defendant_on_case xdoc WHERE xdoc.case_id = xc.case_id AND NVL(xdoc.is_juvenile, 'N') = 'Y' AND ROWNUM = 1 ));
  END get_cases_by_filter;

  PROCEDURE get_case_list_history( p_results_out  OUT SYS_REFCURSOR,
                                   p_case_id      IN  XHB_CASE_ON_LIST.CASE_ID%TYPE,
                                   p_row_limit    IN  NUMBER) AS
  BEGIN
        OPEN p_results_out FOR
          SELECT subquery.* FROM  
         (SELECT xcol.CASE_ON_LIST_ID,
                NULL CASE_DIARY_FIXTURE_ID,
                xlluv.LIST_START_DATE,
                xlluv.LIST_END_DATE,
                xrld.REF_DATA_VALUE as LIST_TYPE,
                xcs.COURT_SITE_CODE,
                xcr.CREST_COURT_ROOM_NO as COURT_ROOM_NO,
                xrht.HEARING_TYPE_CODE,
                NVL(xcol.FLOATER_CASE, 'N') as FLOATER_CASE,
                NVL(xcol.RESERVED, 'N') as RESERVED,
                xsol.SITTING_NUMBER,
                xlluv.PUBLISH_DATE,
                xlluv.PUBLISH_STATUS,
                xlluv.DRAFT_OR_FINAL,
                xlluv.LIST_NUMBER,
                xlluv.LAST_UPDATE_DATE,
                xcol.DATE_OF_REMOVAL,
                GET_REMOVAL_REASON(xcol.vacation_pre_defined_rson_id, xcol.REASON_FOR_REMOVAL) REASON_FOR_REMOVAL
          FROM XHB_CASE_ON_LIST xcol,
               XHB_SITTING_ON_LIST xsol,
               XHB_LIST_LAST_UPDATED_V xlluv,
               XHB_REF_HEARING_TYPE xrht,
               XHB_COURT_SITE xcs,
               XHB_COURT_ROOM xcr,
               XHB_REF_LISTING_DATA xrld
          WHERE xcol.CASE_ID = p_case_id
          AND xlluv.LIST_ID = xcol.LIST_ID
          AND xrld.REF_LISTING_DATA_ID = xlluv.LIST_TYPE_ID
          AND xrht.REF_HEARING_TYPE_ID = XCOL.HEARING_TYPE_ID
          AND xcs.COURT_SITE_ID(+) = xcol.COURT_SITE_ID
          AND xcr.COURT_ROOM_ID(+) = xcol.COURT_ROOM_ID
          AND xcol.LIST_ID = xlluv.LIST_ID
          AND xrld.REF_DATA_TYPE = 'LIST_TYPE'
          AND xcol.SITTING_ON_LIST_ID = xsol.SITTING_ON_LIST_ID(+)
          AND (NVL(xcol.OBS_IND,'N') = 'N' OR xcol.DATE_OF_REMOVAL IS NOT NULL)
          UNION ALL
          SELECT NULL CASE_ON_LIST_ID,
                xcdf.CASE_DIARY_FIXTURE_ID,
                xcdf.LISTING_DATE as LIST_START_DATE,
                xcdf.LISTING_DATE as LIST_END_DATE,
                'Fixture' as LIST_TYPE,
                xcs.COURT_SITE_CODE,
                NULL as COURT_ROOM_NO,
                xrht.HEARING_TYPE_CODE,
                'N' as FLOATER_CASE,
                'N' as RESERVED,
                NULL SITTING_NUMBER,
                NULL PUBLISH_DATE,
                NULL PUBLISH_STATUS,
                NULL DRAFT_OR_FINAL,
                NULL LIST_NUMBER,
                xcdf.LAST_UPDATE_DATE,
                xcdf.DATE_VACATED as DATE_OF_REMOVAL,
                GET_REMOVAL_REASON(xcdf.VACATION_PRE_DEFINED_RSON_ID, xcdf.VACATION_FREETEXT_REASON) REASON_FOR_REMOVAL
          FROM XHB_CASE_DIARY_FIXTURE xcdf,
               XHB_CASE_LISTING_ENTRY xcle,
               XHB_COURT_SITE xcs,
               XHB_REF_HEARING_TYPE xrht
          WHERE xcle.CASE_LISTING_ENTRY_ID = xcdf.CASE_LISTING_ENTRY_ID
            AND xcle.CASE_ID = p_case_id
            AND xrht.REF_HEARING_TYPE_ID(+) = xcdf.HEARING_TYPE_ID
            AND xcs.COURT_SITE_ID(+) = xcdf.COURT_SITE_ID
          ORDER BY 3 DESC) subquery 
          WHERE (ROWNUM <= p_row_limit OR p_row_limit IS NULL);
      
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
          AND NVL(xdonol.OBS_IND,'N') = 'N'
          AND NVL(xdoc.OBS_IND,'N') = 'N'
          AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID;
      
  END get_defendants_on_list;
   
  PROCEDURE get_defendants_on_fixture( p_results_out   OUT SYS_REFCURSOR,
                                       p_case_diary_fixture_id  IN  XHB_FIXTURE_DEFT_ATTENDING.CASE_DIARY_FIXTURE_ID%TYPE) AS
      BEGIN
        OPEN p_results_out FOR
        SELECT xd.*, xdoc.*
          FROM XHB_FIXTURE_DEFT_ATTENDING xfda,
               XHB_DEFENDANT_ON_CASE xdoc,
               XHB_DEFENDANT xd
          WHERE xfda.CASE_DIARY_FIXTURE_ID = p_case_diary_fixture_id
          AND xfda.ATTENDING = 'Y'
          AND xfda.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
          AND NVL(xfda.OBS_IND,'N') = 'N'
          AND NVL(xdoc.OBS_IND,'N') = 'N'
          AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID;
      
  END get_defendants_on_fixture;

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

  PROCEDURE get_latest_list(p_results_out  OUT SYS_REFCURSOR,
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
  END get_latest_list;

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

  FUNCTION get_fixture_count(p_court_id     IN  XHB_CASE_LISTING_ENTRY.COURT_ID%TYPE,
                                   p_from_date    IN  XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE,
                                   p_to_date      IN  XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE) RETURN NUMBER AS
    n_count       NUMBER := 0;
  BEGIN
    -- Get number of non-obsolete fixtures that are on or between the from and to dates
    SELECT COUNT(*) INTO n_count
    FROM xhb_case_listing_entry xcle, xhb_case_diary_fixture xcdf
    WHERE xcle.court_id = p_court_id
    AND xcdf.case_listing_entry_id = xcle.case_listing_entry_id
    AND xcdf.listing_date <= p_to_date
    AND xcdf.listing_date >= p_from_date
    AND NVL(xcdf.obs_ind,'N') = 'N';
    RETURN n_count;
  END get_fixture_count;
  
	/**********************************************************************************
	*
	* Procedure returns Y if a valid defendant on the case supplied has the BC status
	* specified, else N
	*
	**************************************************************************************/
	FUNCTION check_def_bc_status_on_case(p_case_id IN XHB_DEFENDANT_ON_CASE.CASE_ID%TYPE,
										 p_bc_status IN XHB_DEFENDANT_ON_CASE.CURRENT_BC_STATUS%TYPE) RETURN VARCHAR2 IS
		 
		v_return_value VARCHAR2(1);

	BEGIN
		SELECT DECODE(COUNT(*), 0, 'N', 'Y')
		INTO   v_return_value
		FROM   XHB_DEFENDANT_ON_CASE xdoc
		WHERE  NVL(xdoc.OBS_IND, 'N') <> 'Y'
		AND    xdoc.CASE_ID = p_case_id
		AND    NVL(xdoc.CURRENT_BC_STATUS, 'N/A') = p_bc_status;
	  
		RETURN v_return_value;
	END check_def_bc_status_on_case;
  
END XHB_LISTING_PKG;
/
show errors