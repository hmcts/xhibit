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
				DECODE( NVL(xlao.number_of_advocates,0), 0, 'N', 'Y') AS juniorcounsel,
				DECODE( NVL(xlao.number_of_qcs,0), 0, 'N', 'Y') AS queenscounsel,
				xlao.granted_by AS grantedby,
				INITCAP(xrs.solicitor_firm_name) AS solicitorname,
				xrs.dx_ref AS solicitordx,
				CASE WHEN xrs.solicitor_firm_name IS NOT NULL THEN get_address(xrs.address_id) ELSE '' END AS solicitoraddress,
				INITCAP(xrsc.de_code) AS prisonname
				
				
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
				DECODE( NVL(xlao.number_of_advocates,0), 0, 'N', 'Y') AS juniorcounsel,
				DECODE( NVL(xlao.number_of_qcs,0), 0, 'N', 'Y') AS queenscounsel,
				xlao.granted_by AS grantedby,
				INITCAP(xrs.solicitor_firm_name) AS solicitorname,
				xrs.dx_ref AS solicitordx,
				CASE WHEN xrs.solicitor_firm_name IS NOT NULL THEN get_address(xrs.address_id) ELSE '' END AS solicitoraddress
				
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

END XHB_PUBLIC_REP_PKG;
/
show errors
