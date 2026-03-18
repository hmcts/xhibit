CREATE OR REPLACE PACKAGE XHB_LISTING_PKG AS 

  PROCEDURE get_list(p_results_out   OUT SYS_REFCURSOR,
                      p_court_id     IN  XHB_LIST_LAST_UPDATED_V.COURT_ID%TYPE,
                      p_list_type    IN  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE,
                      p_from_date    IN  XHB_LIST_LAST_UPDATED_V.LIST_START_DATE%TYPE,
                      p_to_date      IN  XHB_LIST_LAST_UPDATED_V.LIST_START_DATE%TYPE);     
                      
  PROCEDURE get_case_listing_information( p_results_out   OUT SYS_REFCURSOR,
                      p_case_id     IN  XHB_CASE_ON_LIST.CASE_ID%TYPE);             

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
         SELECT xlluv.* FROM XHB_LIST_LAST_UPDATED_V xlluv, XHB_REF_LISTING_DATA xrld
          WHERE xlluv.COURT_ID = p_court_id
          AND xrld.REF_LISTING_DATA_ID = xlluv.LIST_TYPE_ID
          AND xrld.REF_DATA_TYPE = 'LIST_TYPE'
          AND xrld.REF_DATA_VALUE = p_list_type
          AND xlluv.LIST_START_DATE >= p_from_date 
          AND xlluv.LIST_START_DATE <= p_to_date; 
  END get_list;
  
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
                      

END XHB_LISTING_PKG;
/
