CREATE OR REPLACE PACKAGE "XHB_PUBLIC_REP_PKG" AS
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

PROCEDURE get_ogrda_order (p_results_out 	OUT SYS_REFCURSOR
						  ,p_court_id		IN XHB_COURT.COURT_ID%TYPE
						  ,p_legal_aid_id	IN XHB_LEGAL_AID_ORDER.LEGAL_AID_ORDER_ID%TYPE);

PROCEDURE get_ogrr_order (p_results_out OUT SYS_REFCURSOR
						 ,p_legal_aid_id	IN XHB_LEGAL_AID_ORDER.LEGAL_AID_ORDER_ID%TYPE);
             
PROCEDURE get_oarda_order (p_results_out 	OUT SYS_REFCURSOR
						  ,p_court_id		IN XHB_COURT.COURT_ID%TYPE
						  ,p_legal_aid_amendment_id	IN XHB_LEGAL_AID_AMENDMENT.LEGAL_AID_AMENDMENT_ID%TYPE);
              
PROCEDURE get_oarr_order (p_results_out 	OUT SYS_REFCURSOR
						  ,p_court_id		IN XHB_COURT.COURT_ID%TYPE
						  ,p_legal_aid_amendment_id	IN XHB_LEGAL_AID_AMENDMENT.LEGAL_AID_AMENDMENT_ID%TYPE);
              
PROCEDURE get_owrda_order (p_results_out 	OUT SYS_REFCURSOR
						  ,p_court_id		IN XHB_COURT.COURT_ID%TYPE
						  ,p_legal_aid_order_id	IN XHB_LEGAL_AID_ORDER.LEGAL_AID_ORDER_ID%TYPE);
              
PROCEDURE get_owrr_order (p_results_out 	OUT SYS_REFCURSOR
						  ,p_court_id		IN XHB_COURT.COURT_ID%TYPE
						  ,p_legal_aid_order_id	IN XHB_LEGAL_AID_ORDER.LEGAL_AID_ORDER_ID%TYPE);

FUNCTION get_address (p_address_id  XHB_ADDRESS.ADDRESS_ID%TYPE) RETURN VARCHAR2;

FUNCTION get_respondent_solicitor_id (p_case_pros_agency_id	XHB_CASE_PROSECUTOR_AGENCY.CASE_PROS_AGENCY_ID%TYPE) RETURN XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE;

FUNCTION get_prev_solicitor_on_order(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE, p_current_solicitor_on_order XHB_DEF_ON_CASE_REF_SOL_FIRM.DEF_ON_CASE_REF_SOL_FIRM_ID%TYPE)
RETURN XHB_DEF_ON_CASE_REF_SOL_FIRM.DEF_ON_CASE_REF_SOL_FIRM_ID%TYPE;

FUNCTION get_prev_respondent_on_order(p_case_pros_agency_id XHB_CASE_PROSECUTOR_AGENCY.CASE_PROS_AGENCY_ID%TYPE, p_current_pros_on_order_id XHB_PROSECUTOR_REF_SOL_FIRM.PROSECUTOR_REF_SOL_FIRM_ID%TYPE)
RETURN XHB_PROSECUTOR_REF_SOL_FIRM.PROSECUTOR_REF_SOL_FIRM_ID%TYPE;


END XHB_PUBLIC_REP_PKG;
/
show errors
