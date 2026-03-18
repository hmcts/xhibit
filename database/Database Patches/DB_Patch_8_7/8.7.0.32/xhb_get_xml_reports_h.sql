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
  **/

 /*Global Variables*/
 
  v_return_clob CLOB;
  g_db_name              VARCHAR2(30);
  g_log_file             UTL_FILE.file_type;
  
  log_file UTL_FILE.file_type;
  v_debug_flag         VARCHAR2(1) := 'N';
  v_step               VARCHAR2(2);
  

 PROCEDURE GetReportRequest (p_report       IN  VARCHAR2
                            ,p_clob_out     OUT NOCOPY CLOB
                            );

 FUNCTION GetFlData 
  RETURN CLOB;
  
 FUNCTION GetDlData(p_list_id XHB_LIST.LIST_ID%TYPE) 
  RETURN CLOB;
  
 FUNCTION GetInternalDailyListData(p_list_id XHB_LIST.LIST_ID%TYPE) 
   RETURN CLOB;
  
 FUNCTION GetPlData 
  RETURN CLOB;
  
 FUNCTION GetRlData 
  RETURN CLOB;
  
  FUNCTION GetWlData 
   RETURN CLOB;
  
 FUNCTION GetChargeCnt (p_case_id IN xhb_case.case_id%TYPE)
  RETURN NUMBER;
 
  FUNCTION GetRefProsAgency  (p_case_id IN xhb_case.case_id%TYPE
                            ,p_field   IN VARCHAR2)
  RETURN VARCHAR2;

 FUNCTION get_contact_details(p_address_id XHB_ADDRESS.ADDRESS_ID%TYPE) RETURN XMLType;
 FUNCTION get_charges(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XMLType;
 FUNCTION get_defendants_on_hearing(p_hearing_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE) RETURN XMLType;
 FUNCTION get_hearings_on_sitting(p_sitting_id XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE) RETURN XMLType;
 FUNCTION get_sittings_in_courtsite(p_courtsite_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLType;
 FUNCTION get_floating_cases(p_list_id XHB_LIST.LIST_ID%TYPE, p_court_site_id XHB_COURT_SITE.COURT_SITE_ID%TYPE) RETURN XMLType;
 FUNCTION to_xml_date_format(p_date DATE) RETURN VARCHAR2;

END XHB_GET_XML_REPORTS;
/
show errors
