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

FUNCTION get_address (p_address_id  XHB_ADDRESS.ADDRESS_ID%TYPE) RETURN VARCHAR2;

FUNCTION get_respondent_solicitor_id (p_case_pros_agency_id	XHB_CASE_PROSECUTOR_AGENCY.CASE_PROS_AGENCY_ID%TYPE) RETURN XHB_REF_SOLICITOR_FIRM.REF_SOLICITOR_FIRM_ID%TYPE;
                           
END XHB_PUBLIC_REP_PKG;
/
show errors
