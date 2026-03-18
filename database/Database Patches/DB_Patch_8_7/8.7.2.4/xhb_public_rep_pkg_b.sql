CREATE OR REPLACE PACKAGE BODY "XHB_PUBLIC_REP_PKG" AS
 /**
  * CGI CREST to XHIBIT Program
  *
  * MODULE      : XHB_PUBLIC_REP_PKG
  *
  * DESCRIPTION : This package contains stored procedures for the Public Representation Module
  *
  * VERSION HISTORY:
  *
  * Date          Author            Version    Nature of Change
  * ----------    -------           --------   ----------------------------------------
  * 09/04/2019    Chris Vincent       1.0      First revision
  *
  **************************************************************************************/

---------------------------------------------------------------------------------------
-- Name: get_ogrda_order
-- Description: Order Granting Representation for Defendants and Appellants
---------------------------------------------------------------------------------------
PROCEDURE get_ogrda_order (p_results_out 	OUT SYS_REFCURSOR
						  ,p_court_id		IN XHB_COURT.COURT_ID%TYPE
						  ,p_legal_aid_id	IN XHB_LEGAL_AID_ORDER.LEGAL_AID_ORDER_ID%TYPE) AS

	v_err_code 	NUMBER;
	v_err_msg  	VARCHAR2(1000);
	
	BEGIN
	
		OPEN p_results_out FOR 
			SELECT
				xc.case_type || xc.case_number AS casenumber,
				xc.case_type AS casetype,
				xc.mag_conviction_date AS magconvictiondate,
				INITCAP(xco.court_name) AS courtname,
				xco.crest_court_id AS crestcourtcode,
				INITCAP(xrc.court_full_name) AS courtfullname,
				INITCAP(xd.first_name || ' ' || xd.surname) AS defendantname,
				NVL(xd.current_prison_status,'N') AS incustody,
				get_address(xd.address_id) AS defendantaddress,
				TRUNC(xlao.order_date) AS orderdate,
				DECODE( NVL(xlao.number_of_advocates,0)  - NVL(xlao.number_of_qcs,0), 0, 'N', 'Y') AS juniorcounsel,
				DECODE( NVL(xlao.number_of_qcs,0), 0, 'N', 'Y') AS queenscounsel,
				xlao.granted_by AS grantedby,
				INITCAP(xrs.solicitor_firm_name) AS solicitorname,
				xrs.dx_ref AS solicitordx,
				CASE WHEN xrs.solicitor_firm_name IS NOT NULL THEN get_address(xrs.address_id) ELSE '' END AS solicitoraddress,
				INITCAP(xrsc.de_code) AS prisonname,
        		get_address(xco.ADDRESS_ID) AS court_address,
        		XHB_REPORT_PKG.getPhoneNum(xco.ADDRESS_ID) AS COURT_TELEPHONE
			FROM 
				xhb_case xc,
				xhb_court xco,
				xhb_ref_court xrc,
				xhb_defendant_on_case xdoc,
				xhb_defendant xd,
				xhb_legal_aid_order xlao,
				xhb_ref_solicitor_firm xrs,
				xhb_ref_system_code xrsc

			WHERE 	xlao.LEGAL_AID_ORDER_ID = p_legal_aid_id
			AND		xc.case_id = xdoc.case_id
			AND		xco.court_id = xc.court_id
			AND		xrc.ref_court_id (+)= xc.ref_court_id
      		AND		xlao.defendant_on_case_id = xdoc.defendant_on_case_id
			AND		xd.defendant_id = xdoc.defendant_id
			AND		xrsc.code (+)= xd.prison_id
			AND		xrsc.code_type (+)= 'PRISON_ID'
			AND		xrsc.court_id (+)= p_court_id
      		AND 	NVL(xrsc.obs_ind(+),'N') <> 'Y'
			AND		xrs.ref_solicitor_firm_id(+) = xhb_report_pkg.get_solicitor_id(xdoc.defendant_on_case_id);

	EXCEPTION
		WHEN OTHERS THEN
			v_err_code := SQLCODE;
			v_err_msg  := SQLERRM;
			-- NOT SURE IF THIS IS REQUIRED FOR RUNNING PUBLIC REP ORDERS OR WHETHER A DIFFERENT ERROR LOG TABLE SHOULD BE USED???
			INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
			VALUES (XHB_REPORTS_ERROR_LOG_SEQ.NEXTVAL, 'Exception handler raised when others in XHB_PUBLIC_REP_PKG.get_ogrda_order '|| v_err_code || ' : ' ||v_err_msg, SYSDATE);
			RAISE;

END get_ogrda_order;

---------------------------------------------------------------------------------------
-- Name: get_ogrr_order
-- Description: Order Granting Representation for Respondents
---------------------------------------------------------------------------------------
PROCEDURE get_ogrr_order (p_results_out OUT SYS_REFCURSOR
						 ,p_legal_aid_id	IN XHB_LEGAL_AID_ORDER.LEGAL_AID_ORDER_ID%TYPE) AS

	v_err_code 	NUMBER;
	v_err_msg  	VARCHAR2(1000);
	
	BEGIN
	
		OPEN p_results_out FOR 
			SELECT
				xc.case_type || xc.case_number AS casenumber,
				xc.case_type AS casetype,
				xc.mag_conviction_date AS magconvictiondate,
				INITCAP(xco.court_name) AS courtname,
				xco.crest_court_id AS crestcourtcode,
				INITCAP(xrc.court_full_name) AS courtfullname,
				INITCAP(xrpa.prosecutor_name_1 || ' ' || xrpa.prosecutor_name_3) AS respondentname,
				get_address(xrpa.address_id) AS respondentaddress,
				xrpa.dx_ref AS respondentdx,
				TRUNC(xlao.order_date) AS orderdate,
				DECODE( NVL(xlao.number_of_advocates,0) - NVL(xlao.number_of_qcs,0), 0, 'N', 'Y') AS juniorcounsel,
				DECODE( NVL(xlao.number_of_qcs,0), 0, 'N', 'Y') AS queenscounsel,
				xlao.granted_by AS grantedby,
				INITCAP(xrs.solicitor_firm_name) AS solicitorname,
				xrs.dx_ref AS solicitordx,
				CASE WHEN xrs.solicitor_firm_name IS NOT NULL THEN get_address(xrs.address_id) ELSE '' END AS solicitoraddress,
				get_address(xco.ADDRESS_ID) AS court_address,
        		XHB_REPORT_PKG.getPhoneNum(xco.ADDRESS_ID) AS COURT_TELEPHONE
			FROM 
				xhb_case xc,
				xhb_court xco,
				xhb_ref_court xrc,
				xhb_case_prosecutor_agency xcpa,
				xhb_ref_prosecutor_agency xrpa,
				xhb_legal_aid_order xlao,
				xhb_ref_solicitor_firm xrs
			WHERE 	xlao.legal_aid_order_id = p_legal_aid_id
			AND		xc.case_id = xcpa.case_id
			AND		xco.court_id = xc.court_id
			AND		xrc.ref_court_id (+)= xc.ref_court_id
			AND		xlao.case_pros_agency_id = xcpa.case_pros_agency_id
      		AND		xrpa.ref_prosecutor_agency_id = xcpa.ref_prosecutor_agency_id
      		AND   xrs.REF_SOLICITOR_FIRM_ID(+) = get_respondent_solicitor_id(xcpa.CASE_PROS_AGENCY_ID);

	EXCEPTION
		WHEN OTHERS THEN
			v_err_code := SQLCODE;
			v_err_msg  := SQLERRM;
			-- NOT SURE IF THIS IS REQUIRED FOR RUNNING PUBLIC REP ORDERS OR WHETHER A DIFFERENT ERROR LOG TABLE SHOULD BE USED???
			INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
			VALUES (XHB_REPORTS_ERROR_LOG_SEQ.NEXTVAL, 'Exception handler raised when others in XHB_PUBLIC_REP_PKG.get_ogrr_order '|| v_err_code || ' : ' ||v_err_msg, SYSDATE);
			RAISE;
        
END get_ogrr_order;

---------------------------------------------------------------------------------------
-- Name: get_oarda_order
-- Description: Order Amending Representation for Defendants and Appellants
---------------------------------------------------------------------------------------
PROCEDURE get_oarda_order (p_results_out 	OUT SYS_REFCURSOR
						  ,p_court_id		IN XHB_COURT.COURT_ID%TYPE
						  ,p_legal_aid_amendment_id	IN XHB_LEGAL_AID_AMENDMENT.LEGAL_AID_AMENDMENT_ID%TYPE) AS

	v_err_code 	NUMBER;
	v_err_msg  	VARCHAR2(1000);

	BEGIN

		OPEN p_results_out FOR
			SELECT
				xc.case_type || xc.case_number AS case_number,
				INITCAP(xco.court_name) AS court_name,
				xco.crest_court_id AS court_code,
				INITCAP(xd.first_name || ' ' || xd.surname) AS defendant_name,
				NVL(xd.current_prison_status,'N') AS in_custody,
				get_address(xd.address_id) AS defendant_address,
				INITCAP(TO_CHAR(xlam.AMENDMENT_DATE, 'dd FMMONTH yyyy')) AS amendment_date,
				NVL(xlao.number_of_advocates,0) - NVL(xlao.number_of_qcs,0) AS junior_counsel,
				NVL(xlao.number_of_qcs,0) AS queens_counsel,
				INITCAP(new_xrs.solicitor_firm_name) AS new_solicitor_name,
				new_xrs.dx_ref AS new_solicitor_dx,
				CASE WHEN new_xrs.solicitor_firm_name IS NOT NULL THEN get_address(new_xrs.address_id) ELSE '' END AS new_solicitor_address,
				INITCAP(previous_xrs.solicitor_firm_name) AS previous_solicitor_name,
				previous_xrs.dx_ref AS previous_solicitor_dx,
				CASE WHEN previous_xrs.solicitor_firm_name IS NOT NULL THEN get_address(previous_xrs.address_id) ELSE '' END AS previous_solicitor_address,
        INITCAP(xrsc.de_code) AS prison_name,
        xlam.AMENDMENT_TYPE,
        get_address(xco.ADDRESS_ID) AS court_address,
        XHB_REPORT_PKG.getPhoneNum(xco.ADDRESS_ID) AS COURT_TELEPHONE
			FROM
      XHB_LEGAL_AID_AMENDMENT xlam
      INNER JOIN xhb_legal_aid_order xlao ON xlao.LEGAL_AID_ORDER_ID = xlam.LEGAL_AID_ORDER_ID AND 	NVL(xlao.obs_ind,'N') <> 'Y'
      INNER JOIN  xhb_defendant_on_case xdoc ON xlao.defendant_on_case_id = xdoc.defendant_on_case_id
      INNER JOIN xhb_case xc ON xc.case_id = xdoc.case_id
      INNER JOIN xhb_court xco ON xco.court_id = xc.court_id 
      LEFT OUTER JOIN xhb_ref_court xrc ON xrc.ref_court_id = xc.ref_court_id	
      INNER JOIN xhb_defendant xd ON xd.defendant_id = xdoc.defendant_id
      LEFT OUTER JOIN xhb_ref_system_code xrsc 
        ON xrsc.code = xd.prison_id
        AND		xrsc.code_type = 'PRISON_ID'
        AND		xrsc.court_id = p_court_id
        AND 	NVL(xrsc.obs_ind,'N') <> 'Y'
       LEFT OUTER JOIN xhb_def_on_case_ref_sol_firm new_xdocrsf ON new_xdocrsf.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
        AND   new_xdocrsf.rep_st_date <= SYSDATE
        AND (new_xdocrsf.rep_end_date IS NULL OR new_xdocrsf.rep_end_date >= sysdate)
        AND 	NVL(new_xdocrsf.obs_ind,'N') <> 'Y'
      LEFT OUTER JOIN xhb_ref_solicitor_firm new_xrs ON new_xrs.ref_solicitor_firm_id  = new_xdocrsf.REF_SOLICITOR_FIRM_ID 
      LEFT OUTER JOIN xhb_def_on_case_ref_sol_firm previous_xdocrsf 
       ON previous_xdocrsf.DEF_ON_CASE_REF_SOL_FIRM_ID = get_prev_solicitor_on_order(xdoc.DEFENDANT_ON_CASE_ID, new_xdocrsf.DEF_ON_CASE_REF_SOL_FIRM_ID)
        LEFT OUTER JOIN xhb_ref_solicitor_firm previous_xrs ON previous_xrs.ref_solicitor_firm_id = previous_xdocrsf.REF_SOLICITOR_FIRM_ID
      WHERE xlam.LEGAL_AID_AMENDMENT_ID = p_legal_aid_amendment_id;

	EXCEPTION
		WHEN OTHERS THEN
			v_err_code := SQLCODE;
			v_err_msg  := SQLERRM;
			INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
			VALUES (XHB_REPORTS_ERROR_LOG_SEQ.NEXTVAL, 'Exception handler raised when others in XHB_PUBLIC_REP_PKG.get_oarda_order '|| v_err_code || ' : ' ||v_err_msg, SYSDATE);
			RAISE;

END get_oarda_order;

---------------------------------------------------------------------------------------
-- Name: get_oarr_order
-- Description: Order Amending Representation for Respondents
---------------------------------------------------------------------------------------
PROCEDURE get_oarr_order (p_results_out 	OUT SYS_REFCURSOR
						  ,p_court_id		IN XHB_COURT.COURT_ID%TYPE
						  ,p_legal_aid_amendment_id	IN XHB_LEGAL_AID_AMENDMENT.LEGAL_AID_AMENDMENT_ID%TYPE) AS

	v_err_code 	NUMBER;
	v_err_msg  	VARCHAR2(1000);

	BEGIN

		OPEN p_results_out FOR
			SELECT
				xc.CASE_TYPE || xc.CASE_NUMBER AS case_number,
				INITCAP(xco.COURT_NAME) AS court_name,
				xco.CREST_COURT_ID AS court_code,
        xrpa.PROSECUTOR_NAME_1 || ' ' || xrpa.PROSECUTOR_NAME_3 AS prosecutor_name,
        get_address(xrpa.ADDRESS_ID) as prosecutor_address,
				INITCAP(TO_CHAR(xlam.AMENDMENT_DATE, 'dd FMMONTH yyyy')) AS amendment_date,
				NVL(xlao.number_of_advocates,0) - NVL(xlao.number_of_qcs,0) AS junior_counsel,
				NVL(xlao.number_of_qcs,0) AS queens_counsel,
				INITCAP(new_xrs.SOLICITOR_FIRM_NAME) AS new_solicitor_name,
				new_xrs.DX_REF AS new_solicitor_dx,
				CASE WHEN new_xrs.SOLICITOR_FIRM_NAME IS NOT NULL THEN get_address(new_xrs.ADDRESS_ID) ELSE '' END AS new_solicitor_address,
				INITCAP(previous_xrs.SOLICITOR_FIRM_NAME) AS previous_solicitor_name,
				previous_xrs.DX_REF AS previous_solicitor_dx,
				CASE WHEN previous_xrs.SOLICITOR_FIRM_NAME IS NOT NULL THEN get_address(previous_xrs.ADDRESS_ID) ELSE '' END AS previous_solicitor_address,
        xlam.AMENDMENT_TYPE,
        get_address(xco.ADDRESS_ID) AS COURT_ADDRESS,
        XHB_REPORT_PKG.getPhoneNum(xco.ADDRESS_ID) AS COURT_TELEPHONE
			FROM
      XHB_LEGAL_AID_AMENDMENT xlam
      INNER JOIN XHB_LEGAL_AID_ORDER xlao ON xlao.LEGAL_AID_ORDER_ID = xlam.LEGAL_AID_ORDER_ID AND 	NVL(xlao.OBS_IND,'N') <> 'Y'
      LEFT OUTER JOIN XHB_CASE_PROSECUTOR_AGENCY xcpa ON xcpa.CASE_PROS_AGENCY_ID = xlao.CASE_PROS_AGENCY_ID
        AND 	NVL(xcpa.OBS_IND,'N') <> 'Y'
      INNER JOIN XHB_REF_PROSECUTOR_AGENCY xrpa ON	xrpa.REF_PROSECUTOR_AGENCY_ID = xcpa.REF_PROSECUTOR_AGENCY_ID
      INNER JOIN XHB_CASE xc ON xc.CASE_ID = xcpa.CASE_ID
      INNER JOIN XHB_COURT xco ON xco.COURT_ID = xc.COURT_ID 
      LEFT OUTER JOIN XHB_REF_COURT xrc ON xrc.REF_COURT_ID = xc.REF_COURT_ID	
      --New solicitor
      LEFT OUTER JOIN XHB_PROSECUTOR_REF_SOL_FIRM new_xprsf ON new_xprsf.case_pros_agency_id = xcpa.case_pros_agency_id
        AND NVL(new_xprsf.OBS_IND,'N') <> 'Y'
        AND   	new_xprsf.REP_ST_DATE <= SYSDATE
        AND   	(new_xprsf.REP_END_DATE IS NULL OR new_xprsf.REP_END_DATE >= SYSDATE)
      LEFT OUTER JOIN XHB_REF_SOLICITOR_FIRM new_xrs ON new_xrs.REF_SOLICITOR_FIRM_ID = new_xprsf.REF_SOLICITOR_FIRM_ID
      --Previous solicitor
      LEFT OUTER JOIN XHB_PROSECUTOR_REF_SOL_FIRM previous_xprsf ON previous_xprsf.PROSECUTOR_REF_SOL_FIRM_ID = get_prev_respondent_on_order(xcpa.case_pros_agency_id, new_xprsf.PROSECUTOR_REF_SOL_FIRM_ID)
        AND NVL(previous_xprsf.OBS_IND,'N') <> 'Y'
      LEFT OUTER JOIN XHB_REF_SOLICITOR_FIRM previous_xrs ON previous_xrs.REF_SOLICITOR_FIRM_ID = previous_xprsf.REF_SOLICITOR_FIRM_ID

			WHERE xlam.LEGAL_AID_AMENDMENT_ID = p_legal_aid_amendment_id;

	EXCEPTION
		WHEN OTHERS THEN
			v_err_code := SQLCODE;
			v_err_msg  := SQLERRM;
			INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
			VALUES (XHB_REPORTS_ERROR_LOG_SEQ.NEXTVAL, 'Exception handler raised when others in XHB_PUBLIC_REP_PKG.get_oarr_order '|| v_err_code || ' : ' ||v_err_msg, SYSDATE);
			RAISE;

END get_oarr_order;

---------------------------------------------------------------------------------------
-- Name: getAddress
-- Description: Returns a composite string of address columns, separated by NewLine chrs.
-- 				Care is taken not to add null columns which would make the whole returned
--				string null.  It also avoids adding NewLine chrs for null fields.
---------------------------------------------------------------------------------------
FUNCTION get_address (p_address_id  XHB_ADDRESS.ADDRESS_ID%TYPE) RETURN VARCHAR2 IS 

v_composite_address   VARCHAR2(500);

BEGIN
    -- Create string containing composite address. The Replace(s) remove any spurious NewLines/* from where an address column contains a null value.
    SELECT  REPLACE(REPLACE(INITCAP(ad.address_1) || CHR(10) || INITCAP(NVL(ad.address_2, '*')) || CHR(10) || INITCAP(NVL(ad.address_3, '*')) || CHR(10) || INITCAP(NVL(ad.address_4, '*')) || CHR(10) || INITCAP(NVL(ad.town, '*')) || CHR(10) || INITCAP(NVL(ad.county, '*')) || CHR(10) || NVL(ad.postcode, '*'), '*' || CHR(10)), '*') 
    INTO    v_composite_address 
    FROM    xhb_address ad 
    WHERE   ad.address_id = p_address_id;
    
    RETURN  v_composite_address;
    
END get_address;

---------------------------------------------------------------------------------------
-- Name: get_respondent_solicitor_id
-- Description: Returns the identifier of the current solicitor representing the respondent.
---------------------------------------------------------------------------------------
FUNCTION get_respondent_solicitor_id (p_case_pros_agency_id	XHB_CASE_PROSECUTOR_AGENCY.CASE_PROS_AGENCY_ID%TYPE) RETURN XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE IS

	solicitor_return xhb_ref_solicitor_firm.ref_solicitor_firm_id%TYPE := NULL;

BEGIN

	SELECT	xrsf.ref_solicitor_firm_id
	INTO 	solicitor_return
	FROM 	xhb_prosecutor_ref_sol_firm xprsf, xhb_ref_solicitor_firm xrsf
	WHERE 	xprsf.case_pros_agency_id (+)= p_case_pros_agency_id
	AND   	xprsf.ref_solicitor_firm_id = xrsf.ref_solicitor_firm_id
	AND   	NVL(xprsf.obs_ind, 'N') <> 'Y' 
	AND   	NVL(xrsf.obs_ind, 'N') <> 'Y'
	AND   	xprsf.rep_st_date <= SYSDATE
	AND   	(xprsf.rep_end_date IS NULL OR xprsf.rep_end_date >= SYSDATE)
	AND 	ROWNUM < 2;
	RETURN 	solicitor_return;

EXCEPTION
	WHEN NO_DATA_FOUND THEN
		RETURN NULL; -- If no solicitors found, return null

END get_respondent_solicitor_id;

FUNCTION get_prev_solicitor_on_order(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE, p_current_solicitor_on_order XHB_DEF_ON_CASE_REF_SOL_FIRM.DEF_ON_CASE_REF_SOL_FIRM_ID%TYPE) RETURN XHB_DEF_ON_CASE_REF_SOL_FIRM.DEF_ON_CASE_REF_SOL_FIRM_ID%TYPE IS 
	p_previous_solicitor_on_order XHB_DEF_ON_CASE_REF_SOL_FIRM.DEF_ON_CASE_REF_SOL_FIRM_ID%TYPE := NULL;
BEGIN

 SELECT MAX(previous_xdocrsf.DEF_ON_CASE_REF_SOL_FIRM_ID)
  INTO p_previous_solicitor_on_order
  FROM xhb_def_on_case_ref_sol_firm previous_xdocrsf
  WHERE previous_xdocrsf.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
  AND 	NVL(previous_xdocrsf.obs_ind,'N') <> 'Y'
  AND   previous_xdocrsf.DEF_ON_CASE_REF_SOL_FIRM_ID <> p_current_solicitor_on_order;
  RETURN p_previous_solicitor_on_order;
END get_prev_solicitor_on_order;

FUNCTION get_prev_respondent_on_order(p_case_pros_agency_id XHB_CASE_PROSECUTOR_AGENCY.CASE_PROS_AGENCY_ID%TYPE, p_current_pros_on_order_id XHB_PROSECUTOR_REF_SOL_FIRM.PROSECUTOR_REF_SOL_FIRM_ID%TYPE) RETURN XHB_PROSECUTOR_REF_SOL_FIRM.PROSECUTOR_REF_SOL_FIRM_ID%TYPE IS 
	v_previous_respondent_on_order XHB_PROSECUTOR_REF_SOL_FIRM.PROSECUTOR_REF_SOL_FIRM_ID%TYPE := NULL;
BEGIN
 SELECT MAX(previous_xprsf.PROSECUTOR_REF_SOL_FIRM_ID)
  INTO v_previous_respondent_on_order
  FROM XHB_PROSECUTOR_REF_SOL_FIRM previous_xprsf
  WHERE previous_xprsf.CASE_PROS_AGENCY_ID = p_case_pros_agency_id
  AND 	NVL(previous_xprsf.OBS_IND,'N') <> 'Y'
  AND   previous_xprsf.PROSECUTOR_REF_SOL_FIRM_ID <> p_current_pros_on_order_id;
  RETURN v_previous_respondent_on_order;
END get_prev_respondent_on_order;

END XHB_PUBLIC_REP_PKG;
/
show errors
