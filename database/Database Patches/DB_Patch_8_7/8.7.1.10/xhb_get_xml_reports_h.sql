create or replace PACKAGE XHB_GET_XML_REPORTS AS 

/** 
  * CGI DISC PSS Program
  *
  * MODULE      : data extract report
  *
  * DESCRIPTION : JIRA tickets CTX-1215
  *
  *               Procedure                   Purpose
  *               =========                   =======
  *               main                        Call the select report and depending on the paramater passed in - generate the required report 
  *                                           and create the XML
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 18/01/2018   C Cash          1.0         First Version
  * 20/09/2018   D Burden        1.1         Refactored functions and reformatted to conform to the required XML schema
  **/

 /*Global Variables*/
  v_step               VARCHAR2(2);

  FUNCTION GET_TOWN_AND_COUNTY(p_town   IN XHB_ADDRESS.TOWN%TYPE,
                               p_county IN XHB_ADDRESS.COUNTY%TYPE,
                               p_delimiter IN CHAR DEFAULT ',') RETURN VARCHAR2;
  FUNCTION GET_FIRM_LIST_DATA(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB;
  FUNCTION GET_LIST_HEADER(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_CROWN_COURT(p_court_id XHB_COURT.COURT_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_SATELLITE_COURT_NAMES(p_court_id XHB_COURT.COURT_ID%TYPE) RETURN VARCHAR2;
  FUNCTION IS_SATELLITE_COURT_YN(p_court_site_id IN XHB_COURT_SITE.COURT_SITE_ID%TYPE) RETURN VARCHAR2;
  FUNCTION GET_FIRM_COURT_LISTS(p_list_id XHB_LIST.LIST_ID%TYPE, p_court_id XHB_COURT.COURT_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_COURT_HOUSE(p_court_house_id XHB_COURT_SITE.COURT_SITE_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_RESERVED_HEARINGS_ON_LIST(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLTYPE;

FUNCTION GET_DAILY_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR, p_show_courtroom_list IN INTEGER) RETURN CLOB;

  FUNCTION ESCAPE_BROKER_RELEASE_CHARS(p_string_to_escape VARCHAR2) RETURN VARCHAR2;

  FUNCTION GetInternalDailyListData(p_list_id XHB_LIST.LIST_ID%TYPE)
   RETURN CLOB;

  FUNCTION GET_INTERNAL_DL_SITTINGS(p_list_id XHB_SITTING_ON_LIST.LIST_ID%TYPE) RETURN XMLTYPE;
  FUNCTION GET_INTERNAL_DL_SCH_HEARINGS(p_list_id XHB_SITTING_ON_LIST.LIST_ID%TYPE) RETURN XMLTYPE;
  
  FUNCTION GET_DAILY_PRISON_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB;
  
  FUNCTION GET_RUNNING_LIST(p_court_id XHB_COURT.COURT_ID%TYPE, p_next_pub_running_list_id VARCHAR, p_unique_id VARCHAR) RETURN CLOB;
  FUNCTION GET_ORIGINATING_COURT(p_ref_court_id XHB_REF_COURT.REF_COURT_ID%TYPE) RETURN XMLType;
  FUNCTION GET_RECEIPT_TYPE(p_receipt_type XHB_CASE.RECEIPT_TYPE%TYPE, p_court_id XHB_COURT.COURT_ID%TYPE) RETURN VARCHAR;
  
  FUNCTION GET_WARNED_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR, p_annotated INTEGER,
                          p_include_standard_notes INTEGER,  p_include_priority_notes INTEGER,  p_include_restricted_notes INTEGER) RETURN CLOB;
   FUNCTION GET_WARNED_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR, p_annotated INTEGER,
                          p_include_standard_notes INTEGER,  p_include_priority_notes INTEGER,  p_include_restricted_notes INTEGER, p_publish INTEGER) RETURN CLOB;
  FUNCTION get_highlight_note(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN VARCHAR2;
  FUNCTION get_linked_cases(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType;
  FUNCTION get_other_notes(p_case_id XHB_CASE.CASE_ID%TYPE, p_include_standard_notes INTEGER,  p_include_priority_notes INTEGER,  p_include_restricted_notes INTEGER) RETURN XMLTYPE;
  FUNCTION get_non_available_dates(p_case_id XHB_CASE.CASE_ID%TYPE, p_list_start_date XHB_LIST.LIST_START_DATE%TYPE, p_list_end_date XHB_LIST.LIST_END_DATE%TYPE) RETURN XMLType;
  
 FUNCTION GetChargeCnt (p_case_id IN xhb_case.case_id%TYPE)
  RETURN NUMBER;

 FUNCTION get_contact_details(p_address_id XHB_ADDRESS.ADDRESS_ID%TYPE) RETURN XMLType;
 FUNCTION get_charges(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XMLType;
 FUNCTION get_charge_logs(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XMLType;
 FUNCTION get_defendant(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE, p_show_public_view IN INTEGER, p_show_prison_information IN INTEGER, p_show_defendant_name INTEGER) RETURN XMLType;
 FUNCTION get_defendant(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE, p_show_public_view IN INTEGER, p_show_prison_information IN INTEGER, p_show_defendant_name INTEGER, include_charge_log INTEGER) RETURN XMLType;
 FUNCTION get_defendants_on_case(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType;
 FUNCTION get_defendants_on_hearing(p_hearing_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLType;
 FUNCTION get_prosecution(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType;
 FUNCTION get_respondent(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType;
 FUNCTION get_hearing(p_case_on_list_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLTYPE;
 FUNCTION get_hearings_on_sitting(p_sitting_id XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLType;
FUNCTION get_sittings_in_courtsite(p_courtsite_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE,
                                     p_sitting_date XHB_SITTING_ON_LIST.TIME_LISTED%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLType;
FUNCTION get_floating_cases(p_court_site_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE,
                            p_hearing_date XHB_CASE_ON_LIST.TIME_LISTED%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLType;
 FUNCTION get_original_charges(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XMLType;
 FUNCTION to_xml_date_format(p_date TIMESTAMP) RETURN VARCHAR2;

END XHB_GET_XML_REPORTS;
/
show errors
