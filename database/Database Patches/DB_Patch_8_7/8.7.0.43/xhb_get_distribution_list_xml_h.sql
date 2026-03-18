create or replace PACKAGE XHB_GET_DISTRIBUTION_LIST_XML AS 

/** 
  * CGI DISC PSS Program
  *
  * MODULE      : data extract report
  *
  * DESCRIPTION : JIRA tickets CTX-2629
  *
  *               Procedure                   Purpose
  *               =========                   =======
  *               GET_DAILY_DISTRIBUTION_LIST Generate a Daily List For Distribution XML - Mostly a copy of XHB_GET_REPORTS_XML.GET_DAILY_LIST with alterations to conform to the Distributed List schema
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 04/10/2018   D Burden        1.0         Added Function to generate a Daily List For Distribution XML
  **/

 /*Global Variables*/
  v_step               VARCHAR2(2);

  FUNCTION GET_DAILY_DISTRIBUTION_LIST(p_list_id IN XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB;
  FUNCTION GET_FIRM_DISTRIBUTION_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB;
  FUNCTION GET_WARNED_DISTRIBUTION_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB;
  

  FUNCTION GET_LIST_HEADER(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_CROWN_COURT(p_court_id XHB_COURT.COURT_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_COURT_HOUSE(p_court_house_id XHB_COURT_SITE.COURT_SITE_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_FIRM_COURT_LISTS(p_list_id XHB_LIST.LIST_ID%TYPE, p_court_id XHB_COURT.COURT_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_RESERVED_HEARINGS_ON_LIST(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLTYPE;
  
  FUNCTION GET_ORIGINATING_COURT(p_ref_court_id XHB_REF_COURT.REF_COURT_ID%TYPE) RETURN XMLType;
  FUNCTION GET_RECEIPT_TYPE(p_receipt_type XHB_CASE.RECEIPT_TYPE%TYPE, p_court_id XHB_COURT.COURT_ID%TYPE) RETURN VARCHAR;
  FUNCTION get_highlight_note(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN VARCHAR2;
  FUNCTION get_linked_cases(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType;
  FUNCTION get_other_notes(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLTYPE;
  FUNCTION get_non_available_dates(p_case_id XHB_CASE.CASE_ID%TYPE, p_list_start_date XHB_LIST.LIST_START_DATE%TYPE, p_list_end_date XHB_LIST.LIST_END_DATE%TYPE) RETURN XMLType;
  
 FUNCTION get_charges(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XMLType;
 FUNCTION get_defendant(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE, p_show_public_view INTEGER, p_show_prison_information IN INTEGER) RETURN XMLType;
 FUNCTION get_defendants_on_hearing(p_hearing_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER) RETURN XMLType;
 FUNCTION get_prosecution(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType;
 FUNCTION get_hearing(p_case_on_list_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER) RETURN XMLTYPE;
 FUNCTION get_hearings_on_sitting(p_sitting_id XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER) RETURN XMLType;
 FUNCTION get_sittings_in_courtsite(p_courtsite_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE,
                                    p_sitting_date XHB_SITTING_ON_LIST.TIME_LISTED%TYPE, p_show_prison_information IN INTEGER) RETURN XMLType;
 FUNCTION get_floating_cases(p_court_site_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE,
                            p_hearing_date XHB_CASE_ON_LIST.TIME_LISTED%TYPE, p_show_prison_information IN INTEGER) RETURN XMLType;
 FUNCTION to_xml_date_format(p_date DATE) RETURN VARCHAR2; 

END XHB_GET_DISTRIBUTION_LIST_XML;
/
show errors
