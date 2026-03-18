create or replace PACKAGE BODY XHB_GET_XML_REPORTS AS

  FUNCTION GET_TOWN_AND_COUNTY(p_town   IN XHB_ADDRESS.TOWN%TYPE,
                               p_county IN XHB_ADDRESS.COUNTY%TYPE,
                               p_delimiter IN CHAR DEFAULT ',') RETURN VARCHAR2 IS
    v_delimiter CHAR(1) := ' ';
  BEGIN    
    IF LENGTH(RTRIM(NVL(p_town,' '))) > 0 AND LENGTH(RTRIM(NVL(p_county,' '))) > 0 THEN 
       v_delimiter := p_delimiter;
    END IF;
    RETURN SUBSTR(CONCAT(RTRIM(p_town)||RTRIM(v_delimiter),RTRIM(p_county)), 1, 35);
  END GET_TOWN_AND_COUNTY;

/** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GET_FIRM_LIST_DATA   'Firm List' extract. JIRA ticket CTX-2375. Generated XML from the list tables that conforms to the FirmList.xsd schema 
  **/
  FUNCTION GET_FIRM_LIST_DATA(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
  
   CURSOR get_xml_c IS
    SELECT '<?xml version="1.0" encoding="UTF-8"?>' ||
            XMLELEMENT("cs:FirmList"
                      , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice' AS "xmlns:cs"
                      , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns:p1"
                      , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns"
                      , 'http://www.govtalk.gov.uk/people/bs7666' AS "xmlns:p2"
                      , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                      , cdt.SCHEMA_NAME AS " xsi:schemaLocation")
                      ,XMLELEMENT("cs:DocumentID",
                                    XMLFOREST('Firm List ' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' v' || xl.LIST_NUMBER || ' ' || xl.LIST_START_DATE AS "cs:DocumentName"
                                             ,v_unique_id AS "cs:UniqueID"
                                             ,'FL' AS "cs:DocumentType"
                                             ,to_xml_date_format(systimestamp) AS "cs:TimeStamp"
                                             ,'1.0' AS "cs:Version"
                                             ,cdt.SECURITY_CLASSIFICATION AS "cs:SecurityClassification"
                                             ,sysdate + NUMTODSINTERVAL(TO_NUMBER(cpv.PARAMETER_VALUE), 'SECOND') AS "cs:SellByDate"
                                             ,cdt.STYLESHEET_NAME AS "cs:XSLstylesheetURL" 
                                            )
                                )
                      ,GET_LIST_HEADER(xl.LIST_ID)--ListHeader
                      ,GET_CROWN_COURT(xl.COURT_ID) --CrownCourt
                      ,XMLELEMENT("cs:CourtLists", (GET_FIRM_COURT_LISTS(p_list_id, xl.COURT_ID)))--CourtLists
                      ,XMLFOREST(GET_RESERVED_HEARINGS_ON_LIST(xl.LIST_ID)AS "cs:ReserveList")  
             )--FirmList
             .getclobval() as xml_data
     FROM xhb_list xl, CJI_DOCUMENT_TYPE cdt, CJI_PARAMETER_VALUE cpv 
     WHERE xl.LIST_ID = p_list_id
     AND cdt.INTERNAL_CODE = 'FL'
     AND cpv.PARAMETER_NAME = 'DocumentExpiryTimeOut';

   BEGIN
   v_step := '3';
   v_xml_length := 0;
   
   FOR get_xml_r IN get_xml_c
    LOOP
     v_return_xml := get_xml_r.xml_data;
     v_xml_length := NVL(DBMS_LOB.getLength(lob_loc => v_return_xml), 0);
    END LOOP;
     
  IF v_xml_length = 0 THEN
    RAISE x_no_file;
    ELSE
    RETURN v_return_xml;
  END IF;
    
  EXCEPTION
    
    WHEN x_no_file THEN 
     RAISE_APPLICATION_ERROR(-20001,'No Data found for List ID ' || p_list_id || ' - ' || SQLERRM||' at step '||v_step);
 
    WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);
      
  END GET_FIRM_LIST_DATA;
  
  FUNCTION GET_FIRM_COURT_LISTS(p_list_id XHB_LIST.LIST_ID%TYPE, p_court_id XHB_COURT.COURT_ID%TYPE) RETURN XMLTYPE AS
  firm_court_lists XMLType;
  BEGIN  
  SELECT XMLAgg( XMLELEMENT("cs:CourtList", XMLATTRIBUTES(to_char(court_dates.C_DATE, 'yyyy-mm-dd') AS "SittingDate")
                            ,GET_COURT_HOUSE(xcrts.COURT_SITE_ID)--CourtHouse
                            ,XMLELEMENT("cs:Sittings",(get_sittings_in_courtsite(xcrts.COURT_SITE_ID, p_list_id, court_dates.C_DATE, 0, 0)),
                                                        (get_floating_cases(xcrts.COURT_SITE_ID, p_list_id, court_dates.C_DATE, 0, 0)))--sittings
                            )ORDER BY court_dates.C_DATE, xcrts.COURT_SITE_CODE--CourtList
   ) INTO firm_court_lists 
   FROM XHB_COURT xcrt, XHB_COURT_SITE xcrts, 
   (SELECT  trunc(xcol.TIME_LISTED) C_DATE , xcol.COURT_SITE_ID C_COURT_SITE_ID
       FROM xhb_case_on_list xcol
       WHERE xcol.LIST_ID = p_list_id
       AND  (xcol.RESERVED IS NULL OR xcol.RESERVED <> 'Y')
       AND  (xcol.obs_ind is null or xcol.obs_ind <> 'Y')  
       GROUP BY  trunc(xcol.TIME_LISTED), xcol.COURT_SITE_ID) court_dates -- A list of all court dates on each court site for this list
    WHERE xcrt.COURT_ID = p_court_id
    AND xcrts.COURT_ID = xcrt.COURT_ID 
    AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y')
    AND   xcrts.COURT_SITE_ID = court_dates.C_COURT_SITE_ID;
        RETURN firm_court_lists;
  END GET_FIRM_COURT_LISTS;

  
  FUNCTION GET_RESERVED_HEARINGS_ON_LIST(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLType AS
  reservedHearings XMLType;
  BEGIN
    SELECT XMLAGG(get_hearing(xcol.CASE_ON_LIST_ID, 0, 0)
                  ORDER BY xcol.SEQ_NO)
    INTO reservedHearings
    FROM XHB_CASE_ON_LIST xcol
    WHERE xcol.LIST_ID = p_list_id
    AND xcol.RESERVED = 'Y'
    AND  (xcol.obs_ind is null or xcol.obs_ind <> 'Y');
    RETURN reservedHearings;
  END GET_RESERVED_HEARINGS_ON_LIST;
  
    FUNCTION ESCAPE_BROKER_RELEASE_CHARS(p_string_to_escape VARCHAR2) RETURN VARCHAR2 AS
  BEGIN
    RETURN REPLACE(p_string_to_escape, '!', '!!');
  END ESCAPE_BROKER_RELEASE_CHARS;

  /**
  * DESCRIPTION :
  *               Procedure                   Purpose
  *               =========                   =======
  *               GetInternalDailyListData   'Daily List' extract-JIRA ticket CTX-2370. Generated XML from the list tables that conforms to the Internal_DailyList.xsd schema 
  **/  
  FUNCTION GetInternalDailyListData(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN CLOB AS
   v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
  BEGIN
                SELECT  XMLELEMENT("DLData"
                              ,XMLELEMENT("Court", ESCAPE_BROKER_RELEASE_CHARS(xcrt.CREST_COURT_ID))
                              ,(SELECT XMLAgg(XMLELEMENT("CourtSite", ESCAPE_BROKER_RELEASE_CHARS(xcrts.COURT_SITE_CODE || '|' || xcrts.COURT_SITE_NAME)))
                                        FROM XHB_COURT_SITE xcrts
                                        WHERE xcrts.COURT_ID = xcrt.COURT_ID 
                                        AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y')
                                        AND xcrts.COURT_SITE_ID IN (SELECT xcol.COURT_SITE_ID
                                                                    FROM XHB_CASE_ON_LIST xcol
                                                                    WHERE xcol.LIST_ID = xl.list_id
                                                                    AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y'))) 
                              ,(SELECT XMLAgg(XMLELEMENT("CourtRoom", ESCAPE_BROKER_RELEASE_CHARS(xcs.COURT_SITE_CODE || '|' || xcr.CREST_COURT_ROOM_NO)))
                                        FROM XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs
                                        WHERE xcr.COURT_ROOM_ID IN (SELECT xcol.COURT_ROOM_ID
                                                                  FROM XHB_CASE_ON_LIST xcol
                                                                  WHERE xcol.LIST_ID = xl.list_id
                                                                  AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y'))
                                        AND xcr.COURT_SITE_ID = xcs.COURT_SITE_ID )
                              ,(SELECT XMLAgg(XMLELEMENT("Case", ESCAPE_BROKER_RELEASE_CHARS(xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' || xrc.CREST_CODE || '|' || xc.PROS_AGENCY_REFERENCE || '|' || xc.INDICTMENT_INFO_1 || '|' || xc.INDICTMENT_INFO_2 || '|' ||
                                                                 xc.INDICTMENT_INFO_3 || '|' || xc.INDICTMENT_INFO_4 || '|' || xc.INDICTMENT_INFO_5 || '|' || xc.INDICTMENT_INFO_6 || '|' ||
                                                                 (CASE WHEN xc.CASE_TYPE = 'B' THEN xc.BAIL_MAG_CODE ELSE xc.CASE_TITLE END) || '|' || xc.MAGISTRATES_CASE_REF || '|' || xc.CASE_TITLE || '|' || xc.CASE_SUB_TYPE)))
                                            FROM XHB_CASE_ON_LIST xcol, XHB_CASE xc, XHB_REF_HEARING_TYPE xrht, XHB_REF_COURT xrc
                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                            AND xcol.CASE_ID = xc.CASE_ID
                                            AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                            AND xc.REF_COURT_ID = xrc.REF_COURT_ID(+))
                              ,(SELECT XMLAgg(XMLELEMENT("Hearing", ESCAPE_BROKER_RELEASE_CHARS(xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' || xrht.HEARING_TYPE_CODE || '|' || xrht.HEARING_TYPE_DESC || '|' || xrht.CATEGORY )))
                                            FROM XHB_CASE_ON_LIST xcol, XHB_CASE xc, XHB_REF_HEARING_TYPE xrht
                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                            AND xcol.CASE_ID = xc.CASE_ID
                                            AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID)
                              ,XMLELEMENT("HearingList", ESCAPE_BROKER_RELEASE_CHARS('D' || '|' || to_char(xl.LIST_START_DATE, 'YYYY-MM-DD HH24:MI:SS') || '|' || to_char(xl.LIST_END_DATE,'YYYY-MM-DD HH24:MI:SS') || '|' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL')
                                            || '|' || xl.LIST_NUMBER || '|' || to_char(xl.PUBLISH_DATE,'YYYY-MM-DD HH24:MI:SS') || '||' || xl.LIST_ID || '|' || substr(xcrt.COURT_TYPE,0 , 2) || '|' ))
                              ,GET_INTERNAL_DL_SITTINGS(xl.LIST_ID)
                              --Floating case sitting - Hard coded to 0
                              ,(SELECT XMLAgg(XMLELEMENT("Sitting",ESCAPE_BROKER_RELEASE_CHARS('0|F||' || TO_CHAR(xl.LIST_START_DATE, 'YYYY-MM-DDHH24:MI') || '||||||' || xl.LIST_ID || '|0|1|' || xcs.COURT_SITE_CODE )))
                                              FROM XHB_COURT_SITE xcs
                                              WHERE xcs.COURT_SITE_ID IN  (SELECT xcol.COURT_SITE_ID FROM XHB_CASE_ON_LIST xcol
                                                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                                                            AND  xcol.FLOATER_CASE = 'Y'
                                                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')))
                              , GET_INTERNAL_DL_SCH_HEARINGS(xl.LIST_ID)
                              ,(SELECT XMLAgg(XMLELEMENT("Defendant",ESCAPE_BROKER_RELEASE_CHARS(xd.CREST_DEFENDANT_ID || '|' || xd.FIRST_NAME || '|' || xd.MIDDLE_NAME || '|' || xd.SURNAME || '|' || xd.INITIALS || '|' || xd.DATE_OF_BIRTH || '|' || xd.GENDER || '|' || xd.PRISON_ID || '|' ||
                                                                    defendant_address.ADDRESS_1 || '|' || defendant_address.ADDRESS_2 || '|' || defendant_address.ADDRESS_3 || '|' || defendant_address.ADDRESS_4 || '|' || defendant_address.POSTCODE || '|' || defendant_address.TOWN || '|' || defendant_address.COUNTY)))
                                              FROM XHB_CASE_ON_LIST xcol, XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_ADDRESS defendant_address
                                              WHERE xcol.LIST_ID = xl.LIST_ID
                                              AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                              AND xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                              AND (xdocol.obs_ind is null or xdocol.obs_ind <> 'Y')
                                              AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                              AND (xdoc.obs_ind is null or xdoc.obs_ind <> 'Y')
                                              AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
                                              AND xd.ADDRESS_ID = defendant_address.ADDRESS_ID(+))
                              ,(SELECT XMLAgg(XMLELEMENT("DefendantOnCase", ESCAPE_BROKER_RELEASE_CHARS(xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' || xd.CREST_DEFENDANT_ID || '||' || xdoc.PNC_ID || '|' || xdoc.DEFENDANT_NUMBER || '|' || xdoc.IS_JUVENILE)))
                                            FROM XHB_CASE_ON_LIST xcol, XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_DEFENDANT_ON_CASE xdoc, XHB_CASE xc, XHB_DEFENDANT xd
                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                            AND xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                            AND (xdocol.obs_ind is null or xdocol.obs_ind <> 'Y')
                                            AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                            AND (xdoc.obs_ind is null or xdoc.obs_ind <> 'Y')
                                            AND xdoc.CASE_ID = xc.CASE_ID
                                            AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID)
                              ,(SELECT XMLAgg(XMLELEMENT("ScheduledHearingDefendant", ESCAPE_BROKER_RELEASE_CHARS(xd.CREST_DEFENDANT_ID || '|' || xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' ||  NVL(xcr.CREST_COURT_ROOM_NO,1)  || '|' || NVL(xrj.CREST_JUDGE_ID,0) || '|' || xrht.HEARING_TYPE_CODE || '|' ||
                                                                                      xcs.COURT_SITE_CODE || '|' || NVL(xsol.SITTING_NUMBER,0) || '|' || xrht.HEARING_TYPE_DESC || '|' || xrht.CATEGORY)))
                                            FROM XHB_CASE_ON_LIST xcol, XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_CASE xc, XHB_COURT_ROOM xcr, XHB_SITTING_ON_LIST xsol, XHB_COURT_SITE xcs, XHB_REF_HEARING_TYPE xrht, XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_REF_JUDGE xrj
                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                            AND xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                            AND (xdocol.obs_ind is null or xdocol.obs_ind <> 'Y')
                                            AND xcol.CASE_ID = xc.CASE_ID
                                            AND xcol.COURT_ROOM_ID = xcr.COURT_ROOM_ID(+)
                                            AND xcol.SITTING_ON_LIST_ID = xsol.SITTING_ON_LIST_ID(+)
                                            AND xcol.COURT_SITE_ID = xcs.COURT_SITE_ID(+)
                                            AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                            AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                            AND (xdoc.obs_ind is null or xdoc.obs_ind <> 'Y')
                                            AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
                                            AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID (+) )
                       ) .getclobval() INTO v_return_xml
           FROM XHB_LIST xl, XHB_COURT xcrt
           WHERE xl.LIST_ID = p_list_id
           AND xcrt.COURT_ID = xl.COURT_ID;

   v_step := '4';
   v_xml_length := 0;

   v_xml_length := NVL(DBMS_LOB.getLength(lob_loc => v_return_xml), 0);

  IF v_xml_length = 0 THEN
    RAISE x_no_file;
    ELSE
    RETURN v_return_xml;
  END IF;

  EXCEPTION
    WHEN x_no_file THEN
     RAISE_APPLICATION_ERROR(-20001,'No Data found for List ID ' || p_list_id || ' - ' || SQLERRM||' at step '||v_step);

    WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);

  END GetInternalDailyListData; 
  
  FUNCTION GET_INTERNAL_DL_SITTINGS(p_list_id XHB_SITTING_ON_LIST.LIST_ID%TYPE) RETURN XMLTYPE AS
  sittings XMLTYPE;
  BEGIN
    SELECT XMLAgg(
                  XMLELEMENT("Sitting",ESCAPE_BROKER_RELEASE_CHARS(xsol.SITTING_NUMBER || '|' || 'T' || '|' || TO_CHAR(xsol.TIME_LISTED, 'HH24:MI am') || '|' || TO_CHAR(xsol.TIME_LISTED, 'YYYY-MM-DDHH24:MI') ||  '|'
                                       || xsol.LIST_NOTE_TEXT || '|' || xsol.JP1 || '|' || xsol.JP2 || '|' || xsol.JP3 || '|' || xsol.JP4 || '|' 
                                       || xsol.LIST_ID || '|' || NVL(xrj.CREST_JUDGE_ID, 0) || '|' || xcr.CREST_COURT_ROOM_NO || '|' || xcs.COURT_SITE_CODE)
                            )ORDER BY  xcr.CREST_COURT_ROOM_NO, xsol.SITTING_NUMBER
                  )
                  INTO sittings
                  FROM XHB_SITTING_ON_LIST xsol, XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs, XHB_REF_JUDGE xrj
                  WHERE xsol.LIST_ID = p_list_id
                  AND xsol.COURT_ROOM_ID = xcr.COURT_ROOM_ID (+)
                  AND xsol.COURT_SITE_ID = xcs.COURT_SITE_ID (+)
                  AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID (+)
                  AND (xsol.obs_ind is null or xsol.obs_ind <> 'Y');
    RETURN sittings;
  END GET_INTERNAL_DL_SITTINGS;
  
   FUNCTION GET_INTERNAL_DL_SCH_HEARINGS(p_list_id XHB_SITTING_ON_LIST.LIST_ID%TYPE) RETURN XMLTYPE AS
  scheduled_hearings XMLTYPE;
  BEGIN
    SELECT XMLAgg(
                  XMLELEMENT("ScheduledHearing",ESCAPE_BROKER_RELEASE_CHARS(xcol.SEQ_NO || '|' || CASE WHEN(xrsc.DE_CODE IS NOT NULL) THEN xrsc.DE_CODE ||  TO_CHAR(xcol.TIME_LISTED, 'HH24:MI am') END ||  '|' ||  TO_CHAR(xcol.TIME_LISTED, 'YYYY-MM-DDHH24:MI') || '|' || TO_CHAR(xsol.TIME_LISTED, 'HH24:MI am') || '|'
                                                || SUBSTR(xcol.LIST_NOTE_TEXT || CASE WHEN(xcol.LIST_NOTE_TEXT IS NOT NULL AND xrld.REF_DATA_VALUE IS NOT NULL) THEN ',  ' END || xrld.REF_DATA_VALUE, 1, 78) || '|' || xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' || xrht.HEARING_TYPE_CODE || '|'
                                                || NVL(xcr.CREST_COURT_ROOM_NO, 1) || '|' || xcs.COURT_SITE_CODE || '|' || NVL(xrj.CREST_JUDGE_ID, 0) || '|' || NVL(xsol.SITTING_NUMBER, 0) || '|' ||  xrht.HEARING_TYPE_DESC || '|' || xrht.CATEGORY)
                            )ORDER BY xcr.CREST_COURT_ROOM_NO, xsol.SITTING_NUMBER, xcol.SEQ_NO
                )
                INTO scheduled_hearings
                FROM XHB_CASE_ON_LIST xcol, XHB_CASE xc, XHB_REF_SYSTEM_CODE xrsc, XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs,
                    XHB_SITTING_ON_LIST xsol, XHB_REF_HEARING_TYPE xrht, XHB_REF_JUDGE xrj, XHB_REF_LISTING_DATA xrld
                WHERE xcol.LIST_ID = p_list_id
                AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                AND xcol.CASE_ID = xc.CASE_ID
                AND xcol.TIME_MARKING_ID =  xrsc.REF_SYSTEM_CODE_ID (+)
                AND xcol.COURT_SITE_ID = xcs.COURT_SITE_ID (+)
                AND xcol.COURT_ROOM_ID = xcr.COURT_ROOM_ID (+)
                AND xcol.SITTING_ON_LIST_ID = xsol.SITTING_ON_LIST_ID(+)
                AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID (+)
                AND   xcol.LIST_NOTE_PREDEFINED_ID = xrld.REF_LISTING_DATA_ID(+)
                AND   xrld.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE';
    RETURN scheduled_hearings;
  END GET_INTERNAL_DL_SCH_HEARINGS;
  
  /** 
  * DESCRIPTION :  
  *               Procedure       Purpose
  *               =========       =======
  *               GET_DAILY_LIST 'Daily List' extract.  JIRA ticket CTX-2372. Generated XML from the list tables that conforms to the DailyList.xsd schema
  **/
  FUNCTION GET_DAILY_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR, p_show_courtroom_list IN INTEGER) RETURN CLOB AS

  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   CURSOR get_xml_c IS
            SELECT '<?xml version="1.0" encoding="UTF-8"?>' ||
                      XMLELEMENT("cs:DailyList"
                                  , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice' AS "xmlns:cs"
                                  , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns:p1"
                                  , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns"
                                  , 'http://www.govtalk.gov.uk/people/bs7666' AS "xmlns:p2"
                                  , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                                  , cdt.SCHEMA_NAME AS " xsi:schemaLocation")
                                  ,XMLELEMENT("cs:DocumentID", XMLFOREST('Daily List ' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' v' || xl.LIST_NUMBER || ' ' || xl.LIST_START_DATE AS "cs:DocumentName"
                                                                     ,v_unique_id AS "cs:UniqueID"
                                                                     ,'DL' AS "cs:DocumentType"
                                                                     ,to_xml_date_format(systimestamp) AS "cs:TimeStamp"
                                                                     ,'1.0' AS "cs:Version"
                                                                     ,cdt.SECURITY_CLASSIFICATION AS "cs:SecurityClassification"
                                                                     ,sysdate + NUMTODSINTERVAL(TO_NUMBER(cpv.PARAMETER_VALUE), 'SECOND') AS "cs:SellByDate"
                                                                     ,cdt.STYLESHEET_NAME AS "cs:XSLstylesheetURL" 
                                                                    )
                                            )
                                  , GET_LIST_HEADER(xl.LIST_ID) --ListHeader
                                  , GET_CROWN_COURT(xl.COURT_ID) --CrownCourt
                                  ,XMLELEMENT("cs:CourtLists",
                                              (SELECT XMLAgg( XMLELEMENT("cs:CourtList"
                                                                          ,CASE WHEN p_show_courtroom_list = 1 THEN
                                                                              XMLELEMENT("cs:CourtHouse", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court')
                                                                                        , XMLELEMENT("cs:CourtHouseName", xcrts.COURT_SITE_NAME)
                                                                                        , XMLELEMENT("cs:Description", xcrts.LIST_NAME)
                                                                                        ))--CourtHouse
                                                                            ELSE GET_COURT_HOUSE(xcrts.COURT_SITE_ID) END--CourtHouse
                                                                          , XMLELEMENT("cs:Sittings",(get_sittings_in_courtsite(xcrts.COURT_SITE_ID, xl.LIST_ID, xl.list_start_date, 0, p_show_courtroom_list)),
                                                                                                    (get_floating_cases(xcrts.COURT_SITE_ID, xl.LIST_ID, xl.list_start_date, 0, p_show_courtroom_list)))--sittings
                                                                         ) ORDER BY xcrts.COURT_SITE_CODE--CourtList
                                               ) FROM XHB_COURT_SITE xcrts
                                                  WHERE xcrts.COURT_ID = xcrt.COURT_ID 
                                                  AND (EXISTS (SELECT NULL FROM xhb_sitting_on_list xsol
                                                               WHERE xsol.list_id = xl.list_id
                                                                 AND xsol.court_site_id = xcrts.court_site_id
                                                                 AND NVL(xsol.obs_ind,'N') = 'N'
                                                                 AND ROWNUM = 1)
                                                       OR
                                                       EXISTS (SELECT NULL FROM xhb_case_on_list xcol
                                                               WHERE xcol.list_id = xl.list_id
                                                                 AND xcol.court_site_id = xcrts.court_site_id
                                                                 AND NVL(xcol.obs_ind,'N') = 'N'
                                                                 AND NVL(xcol.floater_case,'N') = 'Y'
                                                                 AND ROWNUM = 1))
                                                  AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y'))
                                    )--CourtLists  
                         )--DailyList
             .getclobval() as xml_data
     FROM xhb_list xl
     ,    xhb_court xcrt, CJI_DOCUMENT_TYPE cdt, CJI_PARAMETER_VALUE cpv 
     WHERE xl.LIST_ID = p_list_id
     AND xcrt.COURT_ID = xl.COURT_ID
     AND cdt.INTERNAL_CODE = 'DL'
     AND cpv.PARAMETER_NAME = 'DocumentExpiryTimeOut';

  get_xml_r get_xml_c%ROWTYPE;

   BEGIN
   v_step := '4';
   v_xml_length := 0;
   
    FOR get_xml_r IN get_xml_c
    LOOP
     v_return_xml := get_xml_r.xml_data;
     v_xml_length := NVL(DBMS_LOB.getLength(lob_loc => v_return_xml), 0);
    END LOOP;

  IF v_xml_length = 0 THEN
    RAISE x_no_file;
    ELSE
    RETURN v_return_xml;
  END IF;
    
  EXCEPTION
    
    WHEN x_no_file THEN 
      RAISE_APPLICATION_ERROR(-20001,'No List Information found for List ID:' || p_list_id || ' - ' || SQLERRM||' at step '||v_step);
 
    WHEN OTHERS THEN

			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);
      
  END GET_DAILY_LIST;
 
  
  /** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GetPlData   'Prison Daily List' extract. JIRA ticket CTX-1371. GetReportRequest will pass in 'PL_DATA'. 
  **/
  
  
  FUNCTION GET_DAILY_PRISON_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   CURSOR get_xml_c IS
       SELECT '<?xml version="1.0" encoding="UTF-8"?>' ||
              XMLELEMENT("cs:DailyList"
                                ,XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice' AS "xmlns:cs"
                                , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns:p1"
                                , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns"
                                , 'http://www.govtalk.gov.uk/people/bs7666' AS "xmlns:p2"
                                , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                                , cdt.SCHEMA_NAME AS " xsi:schemaLocation")
                                ,XMLELEMENT("cs:DocumentID", XMLFOREST('Daily Prison List ' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' v' || xl.LIST_NUMBER || ' ' || xl.LIST_START_DATE AS "cs:DocumentName"
                                                                     ,v_unique_id AS "cs:UniqueID"
                                                                     ,'DLP' AS "cs:DocumentType"
                                                                     ,to_xml_date_format(systimestamp) AS "cs:TimeStamp"
                                                                     ,'1.0' AS "cs:Version"
                                                                     ,cdt.SECURITY_CLASSIFICATION AS "cs:SecurityClassification"
                                                                     ,sysdate + NUMTODSINTERVAL(TO_NUMBER(cpv.PARAMETER_VALUE), 'SECOND') AS "cs:SellByDate"
                                                                     ,cdt.STYLESHEET_NAME AS "cs:XSLstylesheetURL"    
                                                                    )
                                            )
                                ,GET_LIST_HEADER(xl.LIST_ID) --ListHeader
                                ,GET_CROWN_COURT(xl.COURT_ID) --CrownCourt
                                ,XMLELEMENT("cs:CourtLists",
                                              (SELECT XMLAgg( XMLELEMENT("cs:CourtList"
                                                                          ,GET_COURT_HOUSE(xcrts.COURT_SITE_ID)--CourtHouse
                                                                          ,XMLELEMENT("cs:Sittings",(get_sittings_in_courtsite(xcrts.COURT_SITE_ID, xl.LIST_ID, xl.list_start_date, 1, 0)),
                                                                                                    (get_floating_cases(xcrts.COURT_SITE_ID, xl.LIST_ID, xl.list_start_date, 1, 0)))--sittings
                                                                         )ORDER BY xcrts.COURT_SITE_CODE--CourtList
                                               ) FROM XHB_COURT_SITE xcrts
                                                  WHERE xcrts.COURT_ID = xcrt.COURT_ID 
                                                  AND (EXISTS (SELECT NULL FROM xhb_sitting_on_list xsol
                                                               WHERE xsol.list_id = xl.list_id
                                                                 AND xsol.court_site_id = xcrts.court_site_id
                                                                 AND NVL(xsol.obs_ind,'N') = 'N'
                                                                 AND ROWNUM = 1)
                                                       OR
                                                       EXISTS (SELECT NULL FROM xhb_case_on_list xcol
                                                               WHERE xcol.list_id = xl.list_id
                                                                 AND xcol.court_site_id = xcrts.court_site_id
                                                                 AND NVL(xcol.obs_ind,'N') = 'N'
                                                                 AND NVL(xcol.floater_case,'N') = 'Y'
                                                                 AND ROWNUM = 1))
                                                  AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y'))
                                    )--CourtLists  
                         )--DailyPrisonList
             .getclobval() as xml_data
     FROM xhb_list xl
     ,    xhb_court xcrt, CJI_DOCUMENT_TYPE cdt, CJI_PARAMETER_VALUE cpv  
     WHERE xl.LIST_ID = p_list_id
     AND xcrt.COURT_ID = xl.COURT_ID
     AND cdt.INTERNAL_CODE = 'DLP'
     AND cpv.PARAMETER_NAME = 'DocumentExpiryTimeOut';

  get_xml_r get_xml_c%ROWTYPE;

   BEGIN
   v_step := '5';
   v_xml_length := 0;
   
    FOR get_xml_r IN get_xml_c
    LOOP
     v_return_xml := get_xml_r.xml_data;
     v_xml_length := NVL(DBMS_LOB.getLength(lob_loc => v_return_xml), 0);
    END LOOP;
   
   IF v_xml_length = 0 THEN
     RAISE x_no_file;
     ELSE
     RETURN v_return_xml;
   END IF;
    
  EXCEPTION
    
    WHEN x_no_file THEN 
     RETURN 'The length of the Prison Daily List XML extract is: '||v_xml_length;
 
    WHEN OTHERS THEN

			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);
      
  END GET_DAILY_PRISON_LIST;
 
 
  
  /** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GET_RUNNING_LIST   'Running List' extract. JIRA ticket CTX-1371. GetReportRequest will pass in 'RL_DATA'. 
  **/
  
  
  FUNCTION GET_RUNNING_LIST(p_court_id XHB_COURT.COURT_ID%TYPE, p_next_pub_running_list_id VARCHAR, p_unique_id VARCHAR) RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
   
  CURSOR get_xml_c IS               
  SELECT  '<?xml version="1.0" encoding="UTF-8"?>' ||
              XMLELEMENT("cs:RunningList" , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice' AS "xmlns:cs"
                                                , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns:p1"
                                                , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns"
                                                , 'http://www.govtalk.gov.uk/people/bs7666' AS "xmlns:p2"
                                                , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                                                ,  cdt.SCHEMA_NAME AS " xsi:schemaLocation"
                                    )
                  ,XMLELEMENT("cs:DocumentID", XMLFOREST('Running List ' AS "cs:DocumentName"
                                                     ,p_unique_id AS "cs:UniqueID"
                                                     ,'RL' AS "cs:DocumentType"
                                                     ,to_xml_date_format(systimestamp) AS "cs:TimeStamp"
                                                     ,'1.0' AS "cs:Version"
                                                     ,cdt.SECURITY_CLASSIFICATION AS "cs:SecurityClassification"
                                                     ,sysdate + NUMTODSINTERVAL(TO_NUMBER(cpv.PARAMETER_VALUE), 'SECOND') AS "cs:SellByDate"
                                                     ,cdt.STYLESHEET_NAME AS "cs:XSLstylesheetURL"    
                                                    )
                   )--DocumentId
                    ,XMLELEMENT("cs:ListHeader", XMLFOREST('Criminal' AS "cs:ListCategory"
                                                       ,sysdate AS "cs:StartDate"
                                                       ,1 AS "cs:Version"
                                                       ,to_xml_date_format(systimestamp) AS "cs:PublishedTime"
                                                       )
                  ) --ListHeader
                  , GET_CROWN_COURT(p_court_id) --CrownCourt
                      
      /*************************************************Trial cases fields start here***************************************************/
                , XMLFOREST((SELECT XMLAGG(
                                XMLELEMENT("cs:Case",
                                        XMLELEMENT("cs:CaseNumber",xc.CASE_TYPE || xc.CASE_NUMBER)
                                      --, GET_ORIGINATING_COURT(xc.REF_COURT_ID)--CaseArrivedFrom
                                      , get_defendants_on_case(xc.CASE_ID)--Defendants
                                      , get_prosecution(xc.CASE_ID)--Prosecution 
                                      --, 'cs:AppealCaseDescription'
                                      ,XMLFOREST(GET_RECEIPT_TYPE(xc.RECEIPT_TYPE, xc.COURT_ID) AS "cs:MethodOfInstigation"
                                                ,xc.SENT_FOR_TRIAL_DATE AS "cs:DateOfInstigation"
                                                ,xc.CLASS_CODE AS "cs:CaseClassNumber")
                                        )--Case
                              )
            FROM  xhb_case xc
              WHERE xc.case_type = 'T'
              AND  xc.PUB_RUNNING_LIST_ID = p_next_pub_running_list_id
              AND xc.COURT_ID = p_court_id )
            "cs:TrialCases")--TrialCases
                                    /*************************************************Committal for Sentence cases fields start here***************************************************/
          ,XMLFOREST((SELECT XMLAGG(
                        XMLELEMENT("cs:Case",
                                XMLELEMENT("cs:CaseNumber",xc.CASE_TYPE || xc.CASE_NUMBER)
                              , GET_ORIGINATING_COURT(xc.REF_COURT_ID)--CaseArrivedFrom
                              , get_defendants_on_case(xc.CASE_ID)--Defendants
                              , get_prosecution(xc.CASE_ID)--Prosecution 
                               --,AppealCaseDescription
                              ,XMLFOREST(GET_RECEIPT_TYPE(xc.RECEIPT_TYPE, xc.COURT_ID) AS "cs:MethodOfInstigation"
                                        ,xc.COMMITTAL_DATE AS "cs:DateOfInstigation"
                                        ,xc.CLASS_CODE AS "cs:CaseClassNumber")
                              )--Case
                      )
                      FROM  xhb_case xc
                      WHERE xc.case_type = 'S'
                      AND xc.COURT_ID = p_court_id
                      AND  xc.PUB_RUNNING_LIST_ID = p_next_pub_running_list_id
           )"cs:CommitalCases")--Committal for Sentence Cases
                                                /*************************************************Appeal cases fields start here***************************************************/
           ,XMLFOREST((SELECT XMLAGG(
                        XMLELEMENT("cs:Case",
                                XMLELEMENT("cs:CaseNumber",xc.CASE_TYPE || xc.CASE_NUMBER)
                              , GET_ORIGINATING_COURT(xc.REF_COURT_ID)--CaseArrivedFrom
                              , get_defendants_on_case(xc.CASE_ID)--Defendants
                              , get_prosecution(xc.CASE_ID)--Prosecution 
                              ,XMLFOREST(xrsc_appeal_type.DE_CODE AS "cs:AppealCaseDescription"
                                        ,GET_RECEIPT_TYPE(xc.RECEIPT_TYPE, xc.COURT_ID) AS "cs:MethodOfInstigation"
                                        ,xc.APPEAL_LODGED_DATE AS "cs:DateOfInstigation"
                                        ,xc.CLASS_CODE AS "cs:CaseClassNumber")
                              )--Case
                      )
                      FROM  xhb_case xc, XHB_REF_SYSTEM_CODE xrsc_appeal_type
                        WHERE xc.case_type = 'A'
                        AND xc.COURT_ID = p_court_id
                        AND xc.PUB_RUNNING_LIST_ID = p_next_pub_running_list_id
                        AND xc.CASE_SUB_TYPE = xrsc_appeal_type.CODE (+)
                        AND xc.COURT_ID = xrsc_appeal_type.COURT_ID (+)
                        AND xrsc_appeal_type.CODE_TYPE (+) ='CASE_APPEAL_TYPE')
          "cs:AppealCases")--Appeal Cases  
     )--RunningList 
             .getclobval() as xml_data
    FROM XHB_COURT xcrt, CJI_DOCUMENT_TYPE cdt, CJI_PARAMETER_VALUE cpv  
    WHERE p_court_id = xcrt.court_id
    AND cdt.INTERNAL_CODE = 'RL'
    AND cpv.PARAMETER_NAME = 'DocumentExpiryTimeOut';

  get_xml_r get_xml_c%ROWTYPE;

   BEGIN
   v_step := '5';
   v_xml_length := 0;
   
    FOR get_xml_r IN get_xml_c
    LOOP
     v_return_xml := get_xml_r.xml_data;
     v_xml_length := NVL(DBMS_LOB.getLength(lob_loc => v_return_xml), 0);
    END LOOP;
   
   IF v_xml_length = 0 THEN
     RAISE x_no_file;
     ELSE
     RETURN v_return_xml;
   END IF;
    
  EXCEPTION
    
    WHEN x_no_file THEN 
     RETURN 'The length of the Running List XML extract is: '||v_xml_length;
 
    WHEN OTHERS THEN

			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);
      
  END GET_RUNNING_LIST; 
  
  FUNCTION GET_ORIGINATING_COURT(p_ref_court_id XHB_REF_COURT.REF_COURT_ID%TYPE) RETURN XMLType AS
  originatingCourt XMLTYPE;
  BEGIN
   SELECT XMLELEMENT("cs:CaseArrivedFrom"
                     , XMLELEMENT("cs:OriginatingCourt"
                          ,XMLFOREST('Magistrates Court' AS "cs:CourtHouseType")
                          ,XMLELEMENT("cs:CourtHouseCode" ,XMLATTRIBUTES(NVL(xmcrt.COURT_SHORT_NAME, '---') AS "CourtHouseShortName"), LPAD(xmcrt.CREST_CODE, 4, 0))--CourtHouseCode
                           ,XMLELEMENT("cs:CourtHouseName", xmcrt.COURT_FULL_NAME)--CourtHouseName 
                           ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(NVL(xaddm.ADDRESS_1, '-') AS "p1:Line"
                                                                        ,NVL(xaddm.ADDRESS_2, '-') AS "p1:Line"
                                                                        ,NVL(xaddm.ADDRESS_3, '-') AS "p1:Line"
                                                                        ,NVL(xaddm.ADDRESS_4, '-') AS "p1:Line"
                                                                        ,GET_TOWN_AND_COUNTY(xaddm.TOWN,xaddm.COUNTY) AS "p1:Line")
                              )--CourtHouseAddress
                             ,XMLFOREST(xcdmd.contact_value AS "cs:CourtHouseDX")
                             ,XMLFOREST(xcdmp.contact_value AS "cs:CourtHouseTelephone")
                             ,XMLFOREST(xcdmf.contact_value AS "cs:CourtHouseFax")
                      )--OriginatingCourt
             )--CaseArrivedFrom
             INTO originatingCourt
             FROM XHB_REF_COURT xmcrt --Magistrates Court (Original Cout)
            ,    xhb_address xaddm --Magistrates Court address (Original Cout)
            ,    xhb_contact_detail xcdmf --magistrate contact detail fax
            ,    xhb_contact_detail xcdmp --magistrate contact detail phone
            ,    xhb_contact_detail xcdmd --magistrate contact detail dx
            WHERE p_ref_court_id = xmcrt.ref_court_id
            AND   xmcrt.address_id = xaddm.address_id (+)
            AND   xaddm.address_id = xcdmd.address_id (+)
            AND   upper(xcdmd.contact_type(+)) = 'SECURE EMAIL'
            AND   xaddm.address_id = xcdmp.address_id (+)
            AND   upper(xcdmp.contact_type(+)) = 'PHONE' 
            AND   xaddm.address_id = xcdmf.address_id (+)
            AND   upper(xcdmf.contact_type(+)) = 'FAX';
    RETURN originatingCourt;
  END GET_ORIGINATING_COURT;
  
  FUNCTION GET_RECEIPT_TYPE(p_receipt_type XHB_CASE.RECEIPT_TYPE%TYPE, p_court_id XHB_COURT.COURT_ID%TYPE) RETURN VARCHAR AS
  receipt_type VARCHAR(50);
  BEGIN
    SELECT DECODE(xrsc_receipt_type.DE_CODE,
                  'VOLUNTARY BILL', 'Voluntary bill',
                  'COMMITTAL AFTER BREACH', 'Committal',
                  'COMMITTAL FOR SENTENCE', 'Committal',
                  'COMMITTAL FOR TRIAL', 'Committal',
                  'TRANSFER CERTIFICATE', 'Transfer certificated',
                  'BRING BACK', 'Rehearing ordered')
    INTO receipt_type
    FROM XHB_REF_SYSTEM_CODE xrsc_receipt_type
    WHERE xrsc_receipt_type.CODE = p_receipt_type
    AND xrsc_receipt_type.COURT_ID = p_court_id
    AND xrsc_receipt_type.CODE_TYPE ='CASE_RECEIPT_TYPE';
    RETURN receipt_type;
  END GET_RECEIPT_TYPE;
  
  
   FUNCTION GET_WARNED_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR, p_annotated INTEGER,
                          p_include_standard_notes INTEGER,  p_include_priority_notes INTEGER,  p_include_restricted_notes INTEGER) RETURN CLOB AS
  v_return_xml CLOB;
  BEGIN
  SELECT GET_WARNED_LIST(p_list_id, v_unique_id, p_annotated,
                          p_include_standard_notes,  p_include_priority_notes,  p_include_restricted_notes, 0)
                          INTO v_return_xml
                          FROM DUAL;
  RETURN v_return_xml;
  END GET_WARNED_LIST;

   /**
  * DESCRIPTION :
  *               Procedure   Purpose
  *               =========   =======
  *               GET_WARNED_LIST   'Warned List' extract. JIRA ticket CTX-1371. GetReportRequest will pass in 'WL_DATA'.
  **/
  
  
 FUNCTION GET_WARNED_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR, p_annotated INTEGER,
                          p_include_standard_notes INTEGER,  p_include_priority_notes INTEGER,  p_include_restricted_notes INTEGER, p_publish INTEGER) RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   BEGIN
                       SELECT '<?xml version="1.0" encoding="UTF-8"?>' ||
                              XMLELEMENT("cs:WarnedList"
                                          , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice' AS "xmlns:cs"
                                          , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns:p1"
                                          , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns"
                                          , 'http://www.govtalk.gov.uk/people/bs7666' AS "xmlns:p2"
                                          , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                                          , cdt.SCHEMA_NAME AS " xsi:schemaLocation")
                                  ,XMLELEMENT("cs:DocumentID", XMLFOREST('Warned List ' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' v' || xl.LIST_NUMBER || ' ' || xl.LIST_START_DATE AS "cs:DocumentName"
                                                                     ,v_unique_id AS "cs:UniqueID"
                                                                     ,'WL' AS "cs:DocumentType"
                                                                     ,to_xml_date_format(systimestamp) AS "cs:TimeStamp"
                                                                     ,'1.0' AS "cs:Version"
                                                                     ,cdt.SECURITY_CLASSIFICATION AS "cs:SecurityClassification"
                                                                     ,sysdate + NUMTODSINTERVAL(TO_NUMBER(cpv.PARAMETER_VALUE), 'SECOND') AS "cs:SellByDate"
                                                                     ,cdt.STYLESHEET_NAME AS "cs:XSLstylesheetURL" 
                                                                    ,(CASE WHEN(p_include_standard_notes = 1) THEN 'Standard' END ||
                                                                      CASE WHEN(p_include_standard_notes = 1 AND (p_include_priority_notes = 1 OR p_include_restricted_notes = 1)) THEN ', ' END  ||
                                                                      CASE WHEN(p_include_priority_notes = 1) THEN 'Priority' END ||
                                                                      CASE WHEN(p_include_priority_notes = 1 AND p_include_restricted_notes = 1) THEN ', ' END ||
                                                                      CASE WHEN(p_include_restricted_notes = 1) THEN 'Restricted' END) AS "cs:DocumentInformation"
                                                                    )
                                     )
                                  , GET_LIST_HEADER(xl.LIST_ID) --ListHeader
                                  ,  XMLELEMENT("cs:CrownCourt", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                                     ,XMLELEMENT("cs:CourtHouseCode", LPAD(xcrt.CREST_COURT_ID, 4, 0))
                                                                     ,XMLELEMENT("cs:CourtHouseName", GET_SATELLITE_COURT_NAMES(xcrt.COURT_ID)))) --CrownCourt
                                  , XMLELEMENT("cs:ListingInstructions"
                                                ,XMLELEMENT("cs:ListingInstruction", xcrt.WL_FREE_TEXT)
                                                ,XMLELEMENT("cs:ListingInstruction", 'Any representation about the listing of a case should be made to the Listing Officer ' || 
                                                                                     CASE WHEN xcrt.WL_REP_PERIOD <= 0 THEN 'Immediately'
                                                                                          WHEN xcrt.WL_REP_PERIOD > 0 THEN 'no later than ' || xcrt.WL_REP_TIME || ' on ' || to_char(SYSDATE + xcrt.WL_REP_PERIOD, 'DD MONTH YYYY') END)
                                                ) --ListingInstructions           
                                  , XMLELEMENT("cs:CourtLists"
                                      ,(SELECT XMLAgg(XMLELEMENT("cs:CourtList"
                                                                ,XMLELEMENT("cs:CourtHouse", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                                  ,XMLELEMENT("cs:CourtHouseCode", LPAD(NVL(xcrts.SITE_GROUP, xcrts.COURT_SITE_ID), 4, 0))
                                                                  ,XMLELEMENT("cs:CourtHouseName", 'at ' || xcrts.LIST_NAME)
                                                                  , XMLFOREST( xcrt.COURT_PREFIX AS "cs:Description"))
                                                                   )--CourtHouse
                                                ,(SELECT XMLAgg(XMLELEMENT("cs:WithFixedDate",XMLATTRIBUTES(CASE WHEN p_publish = 0 THEN to_char(xrht.LIST_SEQUENCE) ELSE xrht.HEARING_TYPE_CODE END AS "HearingType")
                                                  ,XMLAgg(XMLELEMENT("cs:Fixture"
                                                    , XMLELEMENT("cs:FixedDate",xcdf.LISTING_DATE)
                                                    , CASE WHEN p_annotated = 1 THEN get_other_notes(xc.CASE_ID, p_include_standard_notes,  p_include_priority_notes,  p_include_restricted_notes)
                                                           WHEN p_annotated = 0 THEN XMLFOREST(xrld_predefined_note.REF_DATA_VALUE || CASE WHEN(xcdf.LIST_NOTE_TEXT IS NOT NULL AND xrld_predefined_note.REF_DATA_VALUE IS NOT NULL) THEN ',  ' END || xcdf.LIST_NOTE_TEXT AS "cs:Notes") END                                                        
                                                    , XMLELEMENT("cs:Cases"
                                                        ,XMLELEMENT("cs:Case"
                                                                          , XMLELEMENT("cs:CaseNumber",xc.CASE_TYPE || xc.CASE_NUMBER)
                                                                          , GET_ORIGINATING_COURT(xc.REF_COURT_ID )--CaseArrivedFrom
                                                                          , XMLFOREST( XMLFOREST(xrht.hearing_type_desc AS "cs:HearingDescription"
                                                                                      ,xcdf.LISTING_DATE AS "cs:HearingDate"
                                                                                      ,CASE WHEN p_annotated = 1 THEN get_highlight_note(xc.CASE_ID) END AS "cs:ListNote"
                                                                                      )"cs:Hearing")--Hearing
                                                                          ,(SELECT XMLAgg( XMLELEMENT("cs:Defendants",
                                                                                          XHB_GET_XML_REPORTS.get_defendant(xdoc.DEFENDANT_ON_CASE_ID, 0, 1, 1)))
                                                                            FROM XHB_DEFENDANT_ON_CASE xdoc, XHB_FIXTURE_DEFT_ATTENDING xfda
                                                                            WHERE xc.CASE_ID = xdoc.CASE_ID
                                                                            AND (xdoc.OBS_IND IS NULL OR xdoc.OBS_IND <> 'Y')
                                                                            AND xfda.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                                                            AND (xfda.OBS_IND IS NULL OR xfda.OBS_IND <> 'Y')
                                                                            AND xfda.ATTENDING = 'Y'
                                                                            AND xfda.CASE_DIARY_FIXTURE_ID = xcdf.CASE_DIARY_FIXTURE_ID)--Defendants
                                                                          , (SELECT XMLELEMENT("cs:Prosecution"
                                                                                              , XMLFOREST(xrpa.PROSECUTOR_NAME_3 AS "cs:ProsecutingReference"
                                                                                                    , XMLFOREST(xrpa.CPS_CODE AS "cs:OrganisationCode"
                                                                                                                       , 'Prosecutor: ' ||  REPLACE(xrpa.PROSECUTOR_NAME_1 || ' ' || xrpa.PROSECUTOR_NAME_2 || ' ' || xrpa.PROSECUTOR_NAME_3, 'CPS', 'Crown Prosecution Service') AS "cs:OrganisationName"
                                                                                                                        ,xrpa.DX_REF AS "cs:OrganisationDX"
                                                                                                        ) "cs:ProsecutingOrganisation"  
                                                                                ))
                                                                                FROM XHB_CASE_PROSECUTOR_AGENCY xcpa, XHB_REF_PROSECUTOR_AGENCY xrpa, XHB_ADDRESS prosecutor_address 
                                                                                WHERE xcpa.CASE_ID = xc.CASE_ID 
                                                                                AND  xcpa.ref_prosecutor_agency_id = xrpa.ref_prosecutor_agency_id 
                                                                                AND  NVL(xcpa.obs_ind,'N') <> 'Y'
                                                                                AND  ((xc.CASE_TYPE = 'A' AND  NVL(xcpa.PROSECUTOR_TYPE, 'R') = 'R' AND (NVL(xc.CASE_SUB_TYPE, '-') <> 'O' OR NVL(xcpa.RESPONDENT_STATUS, 'R') = 'R')) --If appeal case prosecutor is Respondent Type
                                                                                OR   (xc.CASE_TYPE <> 'A'))
                                                                                AND  xrpa.ADDRESS_ID = prosecutor_address.ADDRESS_ID (+))--Prosecution 
                                                                          ,CASE WHEN p_annotated = 1 THEN get_non_available_dates(xc.CASE_ID, xl.LIST_START_DATE, xl.LIST_END_DATE) END
																		  --Trial Time Estimate
																		  ,CASE WHEN p_annotated = 1 AND NVL(xdfc.trial_time_estimate, 0) > 0 THEN
																			XMLFOREST(xdfc.trial_time_estimate || ' ' ||(decode(xdfc.TRIAL_TIME_UNIT, '1', 'Hour(s)', '2','Day(s)','3','Week(s) ','4','Month(s)')) AS "cs:TrialTimeEstimate") 
																		   END
                                                                          --DateOfInstigation
                                                                          ,CASE WHEN p_annotated = 1 THEN XMLFOREST(FLOOR((trunc(sysdate) -  NVL(xc.SENT_FOR_TRIAL_DATE, NVL(xc.COMMITTAL_DATE, xc.APPEAL_LODGED_DATE))) / 7) || ' Week(s)' AS "cs:MethodOfInstigation") END --Age of case to nearest complete week
																		  
																			
																		 ,XMLFOREST(xc.CLASS_CODE AS "cs:CaseClassNumber")
                                                                    )--Case
                                                            )--Cases
                                                            ,get_linked_cases(xc.CASE_ID)AS "cs:LinkedCases"
                                                            )ORDER BY DECODE(xc.CASE_TYPE,'T', 3, 'S', 2, 'A', 1, 0) DESC, xc.CASE_NUMBER )--Fixture
                                                      )ORDER BY xrht.LIST_SEQUENCE)
                                                      FROM XHB_CASE_DIARY_FIXTURE xcdf, XHB_REF_HEARING_TYPE xrht, XHB_CASE_LISTING_ENTRY xcle, XHB_CASE xc, XHB_REF_LISTING_DATA xrld_predefined_note , XHB_DIRECTIONS_FOR_CASE xdfc
                                                      WHERE xcdf.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                                      AND xcdf.COURT_SITE_ID = xcrts.COURT_SITE_ID
                                                      AND xcdf.LISTING_DATE BETWEEN xl.LIST_START_DATE AND xl.LIST_END_DATE
                                                      AND (xcdf.OBS_IND IS NULL OR xcdf.OBS_IND <> 'Y')
                                                      AND xcdf.CASE_LISTING_ENTRY_ID = xcle.CASE_LISTING_ENTRY_ID
                                                      AND xcle.CASE_ID = xc.CASE_ID
                                                      AND xcdf.LIST_NOTE_PRE_DEFINED_ID = xrld_predefined_note.REF_LISTING_DATA_ID(+)
                                                      AND xrld_predefined_note.REF_DATA_TYPE (+) = 'PREDEFINED_LIST_NOTE'
													  AND xcle.CASE_ID = xdfc.CASE_ID(+)
                                                      GROUP BY xrht.HEARING_TYPE_CODE, xrht.LIST_SEQUENCE) --FixedDateFixtures
                                               ,(SELECT XMLAgg(XMLELEMENT("cs:WithoutFixedDate",XMLATTRIBUTES(CASE WHEN p_publish = 0 THEN to_char(xrht.LIST_SEQUENCE) ELSE xrht.HEARING_TYPE_CODE END AS "HearingType")
                                                  ,XMLAgg(XMLELEMENT("cs:Fixture" 
                                                          , CASE WHEN p_annotated = 1 THEN get_other_notes(xc.CASE_ID, p_include_standard_notes, p_include_priority_notes, p_include_restricted_notes) 
                                                                 WHEN p_annotated = 0 THEN XMLFOREST(xrld_predefined_note.REF_DATA_VALUE || CASE WHEN(xcol.LIST_NOTE_TEXT IS NOT NULL AND xrld_predefined_note.REF_DATA_VALUE IS NOT NULL) THEN ',  ' END || xcol.LIST_NOTE_TEXT AS "cs:Notes") END  
                                                          , XMLELEMENT("cs:Cases"
                                                             ,XMLELEMENT("cs:Case"
                                                                 , XMLELEMENT("cs:CaseNumber", xc.CASE_TYPE || xc.CASE_NUMBER)
                                                                 , GET_ORIGINATING_COURT(xc.REF_COURT_ID)
                                                                 , XMLFOREST(XMLFOREST(xrht.hearing_type_desc AS "cs:HearingDescription"
                                                                            --,xcdf.LISTING_DATE AS "cs:HearingDate"
                                                                            ,CASE WHEN p_annotated = 1 THEN get_highlight_note(xc.CASE_ID) END AS "cs:ListNote"
                                                                            )"cs:Hearing")--Hearing                                                                  
                                                                 ,(SELECT XMLAgg( XMLELEMENT("cs:Defendants", 
                                                                                 get_defendant(xdocol.DEFENDANT_ON_CASE_ID, 0, 1, 1)))
                                                                    FROM XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_DEFENDANT_ON_CASE xdoc
                                                                    WHERE xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                                                    AND (xdocol.OBS_IND IS NULL OR xdocol.OBS_IND <> 'Y')
                                                                    AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                                                    AND (xdoc.OBS_IND <> 'Y' or xdoc.OBS_IND is null))--Defendants
                                                                 ,(SELECT XMLELEMENT("cs:Prosecution"
                                                                                              , XMLFOREST(xrpa.PROSECUTOR_NAME_3 AS "cs:ProsecutingReference"
                                                                                                    , XMLFOREST(xrpa.CPS_CODE AS "cs:OrganisationCode"
                                                                                                                       , 'Prosecutor: ' ||  REPLACE(xrpa.PROSECUTOR_NAME_1 || ' ' || xrpa.PROSECUTOR_NAME_2 || ' ' || xrpa.PROSECUTOR_NAME_3, 'CPS', 'Crown Prosecution Service') AS "cs:OrganisationName"
                                                                                                                        ,xrpa.DX_REF AS "cs:OrganisationDX"
                                                                                                        ) "cs:ProsecutingOrganisation"  
                                                                                ))
                                                                      FROM XHB_CASE_PROSECUTOR_AGENCY xcpa, XHB_REF_PROSECUTOR_AGENCY xrpa, XHB_ADDRESS prosecutor_address
                                                                      WHERE xcpa.CASE_ID = xc.CASE_ID 
                                                                      AND  xcpa.ref_prosecutor_agency_id = xrpa.ref_prosecutor_agency_id
                                                                      AND  NVL(xcpa.obs_ind,'N') <> 'Y'
                                                                      AND  ((xc.CASE_TYPE = 'A' AND  NVL(xcpa.PROSECUTOR_TYPE, 'R') = 'R' AND (NVL(xc.CASE_SUB_TYPE, '-') <> 'O' OR NVL(xcpa.RESPONDENT_STATUS, 'R') = 'R')) --If appeal case prosecutor is Respondent Type
                                                                      OR   (xc.CASE_TYPE <> 'A'))
                                                                      AND  xrpa.ADDRESS_ID = prosecutor_address.ADDRESS_ID (+))--Prosecution
                                                                 ,CASE WHEN p_annotated = 1 THEN get_non_available_dates(xc.CASE_ID, xl.LIST_START_DATE, xl.LIST_END_DATE) END
																 --Trial Time Estimate
																,CASE WHEN p_annotated = 1 AND NVL(xdfc.trial_time_estimate, 0) > 0 THEN
																	XMLFOREST(xdfc.trial_time_estimate || ' ' ||(decode(xdfc.TRIAL_TIME_UNIT, '1', 'Hour(s)', '2','Day(s)','3','Week(s) ','4','Month(s)')) AS "cs:TrialTimeEstimate") 
																 END
                                                                 --DateOfInstigation
                                                                 ,CASE WHEN p_annotated = 1 THEN XMLFOREST(FLOOR((trunc(sysdate) -  NVL(xc.SENT_FOR_TRIAL_DATE, NVL(xc.COMMITTAL_DATE, xc.APPEAL_LODGED_DATE))) / 7) || ' Week(s)'  as "cs:MethodOfInstigation") END --Age of case to nearest complete week
                                                                 ,XMLFOREST(xc.CLASS_CODE AS "cs:CaseClassNumber")
						
                                                                 )--Case
                                                            )--Cases
                                                            ,(get_linked_cases(xc.CASE_ID))AS "cs:LinkedCases"
                                                      )ORDER BY DECODE(xc.CASE_TYPE,'T', 3, 'S', 2, 'A', 1, 0) DESC, xc.CASE_NUMBER )
                                                  )ORDER BY xrht.LIST_SEQUENCE)
                                                  FROM XHB_CASE_ON_LIST xcol, XHB_REF_HEARING_TYPE xrht, XHB_CASE xc, XHB_REF_COURT originatingCourt, XHB_REF_LISTING_DATA xrld_predefined_note, XHB_DIRECTIONS_FOR_CASE xdfc
                                                  WHERE xcol.LIST_ID =xl.LIST_ID
                                                  AND (xcol.OBS_IND IS NULL OR xcol.OBS_IND <> 'Y')
                                                  AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                                  AND xcol.COURT_SITE_ID = xcrts.COURT_SITE_ID
                                                  AND xcol.CASE_ID = xc.CASE_ID
                                                  AND xc.REF_COURT_ID = originatingCourt.REF_COURT_ID(+)
                                                  AND xcol.LIST_NOTE_PREDEFINED_ID = xrld_predefined_note.REF_LISTING_DATA_ID(+)
                                                  AND xrld_predefined_note.REF_DATA_TYPE (+) = 'PREDEFINED_LIST_NOTE'
												  AND xcol.CASE_ID = xdfc.CASE_ID(+)
                                                  GROUP BY xrht.HEARING_TYPE_CODE, xrht.LIST_SEQUENCE)
                                           )ORDER BY xcrts.SITE_GROUP, xcrts.COURT_SITE_CODE--CourtList
                                          ) FROM XHB_COURT_SITE xcrts
                                                  WHERE xcrts.COURT_ID = xcrt.COURT_ID 
                                                  AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y'))
                                        )--CourtLists
                    )--WarnedList
                   .getclobval() INTO v_return_xml
     FROM xhb_list xl,  xhb_court xcrt, CJI_DOCUMENT_TYPE cdt, CJI_PARAMETER_VALUE cpv  
     WHERE  xl.list_id = p_list_id
     AND   xl.court_id = xcrt.court_id
     AND cdt.INTERNAL_CODE = 'WL'
     AND cpv.PARAMETER_NAME = 'DocumentExpiryTimeOut';

   v_step := '5'; 
   v_xml_length := NVL(DBMS_LOB.getLength(lob_loc => v_return_xml), 0);
  
   IF v_xml_length = 0 THEN
     RAISE x_no_file;
     ELSE
     RETURN v_return_xml;
   END IF;
    
  EXCEPTION
    
    WHEN x_no_file THEN 
     RETURN 'The length of the Prison Daily List XML extract is: '||v_xml_length;
 
    WHEN OTHERS THEN

			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);
      
  END GET_WARNED_LIST; 
  
  
  /*Get the number of charges against the case*/
  FUNCTION GetChargeCnt (p_case_id IN xhb_case.case_id%TYPE)
  RETURN NUMBER
  IS
   v_charge_cnt NUMBER :=0;
  
  BEGIN
  
  SELECT count(*)
  INTO v_charge_cnt
  FROM xhb_charge
  WHERE case_id = p_case_id
  ;
  
   RETURN v_charge_cnt;
  
  END GetChargeCnt;
  
    /*Get List Header element for the XHB_LIST.LIST_ID passed in
  eg.<cs:ListHeader>
      <cs:ListCategory>Criminal</cs:ListCategory>
      <cs:StartDate>2018-07-17</cs:StartDate>
      <cs:EndDate>2018-07-20</cs:EndDate>
      <cs:Version>DRAFT 1</cs:Version>
      <cs:PublishedTime>2018-07-19T01:31:02</cs:PublishedTime>
      <cs:CRESTlistID>93</cs:CRESTlistID>
	</cs:ListHeader>
  */
  FUNCTION GET_LIST_HEADER(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLTYPE AS
  listHeader XMLTYPE;
  BEGIN
    SELECT XMLELEMENT("cs:ListHeader",
                      XMLFOREST('Criminal' AS "cs:ListCategory"
                               ,xl.list_start_date AS "cs:StartDate"
                               ,xl.list_end_date AS "cs:EndDate"
                               ,DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' ' || xl.LIST_NUMBER AS "cs:Version"
                               --,NVL('xhl.print_reference','') AS "CRESTprintRef" --check this field is correct
                               ,to_xml_date_format(NVL(xl.PUBLISH_DATE, systimestamp)) AS "cs:PublishedTime"
                               ,xl.LIST_ID As "cs:CRESTlistID" --check this field is correct
                             )
            ) --ListHeader
    INTO listHeader
    FROM XHB_LIST xl
    WHERE xl.LIST_ID = p_list_id;
    RETURN listHeader;
  END GET_LIST_HEADER;
  
  /** Get CrownCourt element for the XHB_COURT_ID passed in. 
  <cs:CrownCourt>
		<cs:CourtHouseType>Crown Court</cs:CourtHouseType>
		<cs:CourtHouseCode>453</cs:CourtHouseCode>
		<cs:CourtHouseName>SNARESBROOK</cs:CourtHouseName>
		<cs:CourtHouseAddress>
			<p1:Line>747 Snaresbrook Drive</p1:Line>
			<p1:Line>87 Dirrapuwd Dcrr</p1:Line>
			<p1:Line>Isnershire</p1:Line>
			<p1:PostCode>M44 4YK</p1:PostCode>
		</cs:CourtHouseAddress>
		<cs:CourtHouseDX>wahoo</cs:CourtHouseDX>
		<cs:Description>CROWN COURT</cs:Description>
	</cs:CrownCourt>*/
  FUNCTION GET_CROWN_COURT(p_court_id XHB_COURT.COURT_ID%TYPE) RETURN XMLTYPE IS
  crownCourt XMLTYPE;
  BEGIN
   SELECT XMLELEMENT("cs:CrownCourt", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                                     ,XMLELEMENT("cs:CourtHouseCode", LPAD(xcrt.CREST_COURT_ID, 4,0))
                                                                     ,XMLELEMENT("cs:CourtHouseName", xcrt.court_name)
                                                                     , XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(NVL(xadc.ADDRESS_1, '-') AS "p1:Line"
                                                                                                                ,NVL(xadc.ADDRESS_2, '-') AS "p1:Line"
                                                                                                                ,NVL(xadc.ADDRESS_3, '-') AS "p1:Line"
                                                                                                                ,NVL(xadc.ADDRESS_4, '-') AS "p1:Line"
                                                                                                                ,GET_TOWN_AND_COUNTY(xadc.TOWN,xadc.COUNTY) AS "p1:Line"
                                                                                                                ,xadc.POSTCODE AS "p1:PostCode"
                                                                                                                )
                                                                                 ) 
                                                                    , XMLFOREST(xcrt.DX_REF as "cs:CourtHouseDX"
                                                                              , xcdp.contact_value AS "cs:CourtHouseTelephone"
                                                                              , xcdd.contact_value AS "cs:CourtHouseFax"
                                                                              , xcrt.COURT_PREFIX AS "cs:Description"))
    )INTO crownCourt
    FROM xhb_court xcrt
     ,    xhb_address xadc --addressfor court
     ,    xhb_contact_detail xcdp --contact detail phone 
     ,    xhb_contact_detail xcdd --contact detail fax
     WHERE xcrt.COURT_ID = p_court_id
     AND   xcrt.address_id = xadc.address_id (+)
     AND   xadc.address_id = xcdp.address_id (+)
     AND   upper(xcdp.contact_type(+)) = 'Phone' 
     AND   xadc.address_id = xcdd.address_id (+)
     AND   upper(xcdd.contact_type(+)) = 'Fax';
     
     RETURN crownCourt;                                        
  END GET_CROWN_COURT;
  
  
  /*Function used to create a list of the Satellite Court Names to be displayed in the header of the Warned List preview
  It utilises the WM_CONCAT function to concatenate each court site's list name into one list.
  The WM_CONCAT function is obselete in Oracle version 12c and if the database is upgraded to 12c, this should be replaced by LISTAGG function*/
  FUNCTION GET_SATELLITE_COURT_NAMES(p_court_id XHB_COURT.COURT_ID%TYPE) RETURN VARCHAR2 AS
   satelliteCourtNames VARCHAR2(1000);
  BEGIN
    SELECT WM_CONCAT(' ' || courtNames.LIST_NAME) INTO satelliteCourtNames
    FROM (SELECT satellite_courts.LIST_NAME, satellite_courts.COURT_ID, DECODE(satellite_courts.COURT_SITE_CODE, 'A', 0,1)
           FROM XHB_COURT_SITE satellite_courts
           WHERE  satellite_courts.COURT_ID = p_court_id
           AND   NVL(satellite_courts.OBS_IND, '-') <> 'Y'
           AND    satellite_courts.COURT_SITE_CODE = 'A'
           UNION
           SELECT satellite_courts.LIST_NAME, satellite_courts.COURT_ID, DECODE(satellite_courts.COURT_SITE_CODE, 'A', 0,1)
           FROM XHB_COURT_SITE satellite_courts, XHB_COURT_SATELLITE xcs
           WHERE  satellite_courts.COURT_ID = p_court_id
           AND   NVL(satellite_courts.OBS_IND, '-') <> 'Y'
           AND   satellite_courts.COURT_SITE_ID = xcs.COURT_SITE_ID 
           AND   NVL(xcs.OBS_IND, '-') <> 'Y'
           ORDER BY 3 ) courtNames
    GROUP BY courtNames.COURT_ID;
    RETURN satelliteCourtNames;
  END GET_SATELLITE_COURT_NAMES;
  
  FUNCTION IS_SATELLITE_COURT_YN(p_court_site_id IN XHB_COURT_SITE.COURT_SITE_ID%TYPE) RETURN VARCHAR2 IS
    v_satellite_yn VARCHAR2(1);
    CURSOR C_court IS 
    SELECT 'Y'
      FROM XHB_COURT_SATELLITE xcs
     WHERE xcs.court_site_id = p_court_site_id
       AND NVL(xcs.obs_ind,'N') = 'N';
  BEGIN
    OPEN C_court;
    FETCH C_court INTO v_satellite_yn;
    CLOSE C_court;
    RETURN NVL(v_satellite_yn,'N');
  END;
   /*Get the court house Element for the COURT SITE ID passed in
  <cs:CourtHouse>
				<cs:CourtHouseType>Crown Court</cs:CourtHouseType>
				<cs:CourtHouseCode>453</cs:CourtHouseCode>
				<cs:CourtHouseName>SNARESBROOK</cs:CourtHouseName>
				<cs:Description>CROWN COURT</cs:Description>
			</cs:CourtHouse>*/
  FUNCTION GET_COURT_HOUSE(p_court_house_id XHB_COURT_SITE.COURT_SITE_ID%TYPE) RETURN XMLTYPE AS
  courtHouse XMLTYPE;
  BEGIN
   SELECT XMLELEMENT("cs:CourtHouse", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                ,XMLELEMENT("cs:CourtHouseCode", LPAD(NVL(xcrts.SITE_GROUP, xcrts.COURT_SITE_ID), 4,0))
                                                ,XMLELEMENT("cs:CourtHouseName", xcrts.court_site_name)
                                                ,XMLFOREST( CASE IS_SATELLITE_COURT_YN(xcrts.COURT_SITE_ID)
                                                                 WHEN 'Y' THEN 'SATELLITE' 
                                                                 ELSE xcrt.COURT_PREFIX END AS "cs:Description"))
                     )--CourtHouse
    INTO courtHouse
    FROM XHB_COURT xcrt, XHB_COURT_SITE xcrts
    WHERE xcrts.COURT_SITE_ID = p_court_house_id
    AND   xcrt.COURT_ID = xcrts.COURT_ID;
    
    RETURN courtHouse;
  END GET_COURT_HOUSE;
  
  /*Get the Phone, Fax and email address for the address ID passed in
  EG <cs:ContactDetails>
					<Telephone>
							<TelNationalNumber>0234 758390</TelNationalNumber>
					</Telephone>
					<Fax>
    					<FaxNationalNumber>0234 8574034</FaxNationalNumber>
  				</Fax>
			</cs:ContactDetails>*/
  FUNCTION get_contact_details(p_address_id XHB_ADDRESS.ADDRESS_ID%TYPE) RETURN XMLType IS
   contact_details XMLType;
  BEGIN
  SELECT XMLELEMENT("cs:ContactDetails",(SELECT XMLAgg(XMLFOREST(XMLFOREST(prosecutor_phone.CONTACT_VALUE AS "TelNationalNumber")AS "Telephone"))
                  FROM XHB_CONTACT_DETAIL prosecutor_phone
                  WHERE prosecutor_phone.ADDRESS_ID = p_address_id
                  AND  prosecutor_phone.contact_type = 'Phone')
            )
    INTO contact_details
    FROM dual;
    return contact_details;
  END get_contact_details;

  /* Returns a list of all charges for the XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID passed in:
  <cs:Charge IndictmentCountNumber="1" CJSoffenceCode="AA60004">
      <cs:CRESTchargeID>343678</cs:CRESTchargeID>
      <cs:CaseNumber>T20140005</cs:CaseNumber>
      <cs:OffenceStatement>Person having charge procuring abandonment of animal</cs:OffenceStatement>
      <cs:OffenceStartDateTime>2018-03-29T12:00:00</cs:OffenceStartDateTime>
      <cs:CRESToffenceNumber>648145</cs:CRESToffenceNumber>
    </cs:Charge>*/
  FUNCTION get_charges(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XMLType IS
  charges XMLType;
  BEGIN
  SELECT XMLAgg( XMLELEMENT("cs:Charge" ,XMLATTRIBUTES(NVL(xcge.CREST_CHARGE_SEQ_NO, 1) AS "IndictmentCountNumber" , xro.OFFENCE_CODE AS "CJSoffenceCode")
                           ,XMLFOREST(xdof.crn_id AS "cs:CRN"
                                     ,xcge.crest_charge_id AS "cs:CRESTchargeID"
                                     , xc.CASE_TYPE || xc.CASE_NUMBER AS "cs:CaseNumber"
                                    , xro.OFFENCE_DESC AS "cs:OffenceStatement"
                                    , xo.FORCE_LOCATION_CODE AS "cs:ArrestingPoliceForceCode"
                                    , DECODE(xdof.IS_COMMITTED_ON_BAIL, 'Y', 'yes', 'N', 'no', null) AS "cs:CommittedOnBail"
                                    , to_xml_date_format(xo.START_DATE) AS "cs:OffenceStartDateTime"
                                    , to_xml_date_format(xo.END_DATE) AS "cs:OffenceEndDateTime"
                                    --ArraignmentDate
                                    --ConvictionDate
                                    /*,'tba' AS "cs:OffenceParticulars"*/
                                    , xo.CREST_OFFENCE_ID AS "cs:CRESToffenceNumber"
                                    --Plea
                                    --Verdict
                                    )
                                   --SentenceTerm
                                   --TermType
                                   --ForLife                                                                                                                             
                                    
                  )) INTO charges
                  FROM XHB_CHARGE xcge, XHB_OFFENCE xo, XHB_DEFENDANT_ON_OFFENCE xdof, XHB_REF_OFFENCE xro, XHB_CASE xc
                  WHERE xdof.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
                   AND   xo.offence_id = xdof.offence_id  
                   AND   xcge.charge_id = xo.charge_id 
                   AND  xcge.CASE_ID = xc.CASE_ID
                   AND   xo.REF_OFFENCE_ID = xro.REF_OFFENCE_ID
                   AND   (xdof.obs_ind is null or xdof.obs_ind <> 'Y');
        RETURN charges;
  END get_charges;

  /* Returns a list of all charges for the XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID passed in:
  <cs:Charge IndictmentCountNumber="1" CJSoffenceCode="AA60004">
      <cs:CRESTchargeID>343678</cs:CRESTchargeID>
      <cs:CaseNumber>T20140005</cs:CaseNumber>
      <cs:OffenceStatement>Person having charge procuring abandonment of animal</cs:OffenceStatement>
      <cs:OffenceStartDateTime>2018-03-29T12:00:00</cs:OffenceStartDateTime>
      <cs:CRESToffenceNumber>648145</cs:CRESToffenceNumber>
    </cs:Charge>*/
  FUNCTION get_original_charges(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XMLType IS 
  charges XMLType;
  BEGIN
  SELECT XMLAgg( XMLELEMENT("cs:OriginalCharge"
                           ,XMLFOREST(xo.CREST_OFFENCE_SEQ_NO AS "cs:OriginalChargeSequenceNumber")
                           ,XMLFOREST(xo.CREST_OFFENCE_FREETEXT AS "cs:OriginalChargeDescription")
                  )) INTO charges
                  FROM XHB_CHARGE xcge, XHB_OFFENCE xo, XHB_DEFENDANT_ON_OFFENCE xdof, XHB_REF_OFFENCE xro, XHB_ADDRESS offence_address, XHB_CASE xc
                  WHERE xdof.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
                   AND   xo.offence_id = xdof.offence_id  
                   AND   xcge.charge_id = xo.charge_id 
                   AND   xcge.CASE_ID = xc.CASE_ID
                   AND   xo.REF_OFFENCE_ID = xro.REF_OFFENCE_ID
                   AND   xo.LOCATION_ADDRESS_ID = offence_address.ADDRESS_ID (+)
                   AND   (xdof.obs_ind is null or xdof.obs_ind <> 'Y')
                   AND   xcge.CHARGE_TYPE = 'G'
                   AND NOT EXISTS (SELECT 'X' FROM XHB_CHARGE xcge2, XHB_OFFENCE xo2, XHB_DEFENDANT_ON_OFFENCE xdof2
                                WHERE xdof2.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
                                 AND   xo2.OFFENCE_ID = xdof2.OFFENCE_ID  
                                 AND   xcge2.CHARGE_ID = xo2.CHARGE_ID
                                 AND   xcge2.CHARGE_TYPE = 'I');
        RETURN charges;           
  END get_original_charges;


  FUNCTION get_defendant(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE, p_show_public_view IN INTEGER, p_show_prison_information IN INTEGER, p_show_defendant_name INTEGER) RETURN XMLType IS
  defendant XMLType;
  BEGIN
  SELECT XMLELEMENT("cs:Defendant",
                   XMLELEMENT("cs:PersonalDetails",
                             XMLFOREST(XMLFOREST(CASE WHEN (p_show_public_view = 1 AND xdoc.IS_MASKED = 'Y') OR (p_show_defendant_name = 0) THEN ' ' ELSE xd.FIRST_NAME END AS "p1:CitizenNameForename"
                                                ,CASE WHEN (p_show_public_view = 1 AND xdoc.IS_MASKED = 'Y') THEN xdoc.MASKED_NAME WHEN (p_show_defendant_name = 0) THEN ' ' ELSE xd.SURNAME END AS "p1:CitizenNameSurname") "cs:Name"
                                     , xdoc.MASKED_NAME AS "cs:MaskedName"
                                     , DECODE(xdoc.is_masked, 'Y', 'yes', 'no') AS "cs:IsMasked"
                                     , XMLFOREST(xd.date_of_birth AS "p1:BirthDate"
                                                 ,CASE WHEN xd.date_of_birth IS NOT NULL THEN 'not verified' ELSE NULL END AS "p1:VerifiedBy") "cs:DateOfBirth"
                                     -- , xd.AGE
                                     , DECODE(xd.gender, 1, 'male', 2, 'female', 'unknown') AS "cs:Sex"
                                     , xdoc.NATIONALITY AS "cs:Nationality")
                                )--PersonalDetails
                     , get_contact_details(xd.ADDRESS_ID)-- "cs:ContactDetails"
                     ,XMLFOREST(XMLFOREST(xdoc.ASN AS "cs:ASN")"cs:ASNs" 
                     ,xd.CREST_DEFENDANT_ID AS "cs:CRESTdefendantID"
                     ,xdoc.PTIURN AS "cs:URN"
                     --CRO
                     --,xc.magistrates_case_ref AS "cs:MagistratesCourtRefNumber"--check that this field is right
                     ,CASE WHEN (p_show_prison_information = 1) THEN xdr.REFERENCE_VALUE END AS "cs:PrisonerID"
                     ,XMLFOREST(CASE WHEN (p_show_prison_information = 1) THEN NVL(prisonLoc.DE_CODE, defPrisonLocation.REFERENCE_VALUE) END AS "cs:Location") AS "cs:PrisonLocation"--PrisonLocation
                     ,DECODE(NVL(xd.CURRENT_PRISON_STATUS,'N'), 'Y', 'In custody', 'Not applicable') AS "cs:CustodyStatus")
                     , XMLELEMENT("cs:Counsel"
                      , (SELECT XMLAgg(XMLELEMENT("cs:Solicitor"
                                     , XMLELEMENT("cs:Party"
                                       , XMLELEMENT("cs:Organisation"
                                          
                                         , XMLELEMENT("cs:OrganisationName",xrsf.SHORT_NAME)--OrganisationName
                                         , XMLELEMENT("cs:OrganisationAddress",XMLFOREST(NVL(solicitor_address.ADDRESS_1, '-') AS "p1:Line"
                                                                                       ,NVL(solicitor_address.ADDRESS_2, '-') AS "p1:Line"
                                                                                       ,NVL(solicitor_address.ADDRESS_3, '-') AS "p1:Line"
                                                                                       ,NVL(solicitor_address.ADDRESS_4, '-') AS "p1:Line"
                                                                                       ,GET_TOWN_AND_COUNTY(solicitor_address.TOWN,solicitor_address.COUNTY) AS "p1:Line")
                                                     )--OrganisationAddress
                                                   , XMLFOREST(xrsf.DX_REF AS "cs:OrganisationDX")
                                                   , get_contact_details( xrsf.ADDRESS_ID) --"cs:ContactDetails"         
                                                   )--Organisation
                                                 )--Party
                                         , XMLFOREST(xdocrsf.REP_ST_DATE AS "cs:StartDate"
                                                  , xdocrsf.REP_END_DATE AS "cs:EndDate")
                                          ) --Solicitor
                                          ) FROM XHB_DEF_ON_CASE_REF_SOL_FIRM xdocrsf, XHB_REF_SOLICITOR_FIRM xrsf, XHB_ADDRESS solicitor_address
                                          WHERE xdoc.DEFENDANT_ON_CASE_ID = xdocrsf.DEFENDANT_ON_CASE_ID 
                                          AND  (xdocrsf.obs_ind is null or xdocrsf.obs_ind <> 'Y')
                                          AND xdocrsf.REF_SOLICITOR_FIRM_ID = xrsf.REF_SOLICITOR_FIRM_ID
                                          AND xdocrsf.REF_SOLICITOR_FIRM_ID = XHB_REPORT_PKG.GET_SOLICITOR_ID(xdocrsf.DEFENDANT_ON_CASE_ID)
                                          AND xrsf.ADDRESS_ID = solicitor_address.ADDRESS_ID(+)))--Counsel
                        ,XMLFOREST((get_charges(xdoc.DEFENDANT_ON_CASE_ID)  )"cs:Charges")  --Charges
                        ,XMLFOREST((get_original_charges(xdoc.DEFENDANT_ON_CASE_ID) )"cs:OriginalCharges") --OriginalCharges
                        
                        --AdditionalNotes
                        ,XMLFOREST(DECODE(xdoc.CUSTODIAL, 'Y', 'custodial', 
                                          DECODE(xdoc.SUSPENDED, 'Y', 'suspended'), 
                                          DECODE(xdoc.RECOMMENDED_DEPORTATION, 'Y', 'recommendedDeportation'),
                                          DECODE(xdoc.SERIOUS_DRUG_OFFENCE, 'Y', 'seriousDrugOffence'))
                           AS "cs:DeportationReason")
                        ,XMLFOREST((SELECT XMLAgg(XMLFOREST(xrhst.TITLE AS "cs:Reason")) 
                                FROM XHB_HATE_SENTENCING xhs, XHB_REF_HATE_SENTENCING_TYPE xrhst
                                WHERE xdoc.DEFENDANT_ON_CASE_ID = xhs.DEFENDANT_ON_CASE_ID
                                AND  xhs.REF_HATE_SENT_TYPE_ID = xrhst.REF_HATE_SENTENCING_TYPE_ID)AS "cs:HateCrime") 
                        ,XMLFOREST(xdoc.DEFENDANT_NUMBER AS "cs:DefendantNumber")
          ) INTO defendant
          FROM XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_DEFENDANT_REFERENCE xdr, XHB_DEFENDANT_REFERENCE defPrisonLocation
		       ,XHB_REF_SYSTEM_CODE prisonLoc
           WHERE xdoc.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
            AND   NVL(XDOC.OBS_IND,'N') = 'N'
            AND   xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
            AND   xdoc.DEFENDANT_ID = xdr.DEFENDANT_ID (+) --prisonerf
            AND   'PRISONER NUMBER' = upper(xdr.REFERENCE_NAME (+)) 
            and   xdoc.DEFENDANT_ID = defPrisonLocation.DEFENDANT_ID (+)
            and   'PRISONER LOCATION' = upper(defPrisonLocation.REFERENCE_NAME (+))
            and   prisonLoc.COURT_ID(+) = xd.COURT_ID
            and   prisonLoc.CODE_TYPE(+) = 'PRISON_ID'
            and   prisonLoc.CODE(+) = xd.PRISON_ID
            AND ROWNUM = 1;
    RETURN defendant;
    
    EXCEPTION
       WHEN NO_DATA_FOUND THEN RETURN NULL;
       WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||p_defendant_on_case_id);
  END get_defendant;

 FUNCTION get_highlight_note(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN VARCHAR2 IS
  highlightNote VARCHAR2(400);
  BEGIN
  
  SELECT xdne_freetext.CREATION_DATE || ' ' || xdne_freetext.DIARY_NOTE_TEXT 
    INTO highlightNote
    FROM XHB_CASE_LISTING_ENTRY xcle, XHB_DIARY_NOTE_ENTRY xdne_freetext, XHB_REF_LISTING_DATA xrld_note_type, XHB_REF_LISTING_DATA freetext_noteclass                          
    WHERE p_case_id = xcle.CASE_ID(+)
      AND xcle.CASE_LISTING_ENTRY_ID = xdne_freetext.CASE_LISTING_ENTRY_ID(+)
      AND NVL(xdne_freetext.OBS_IND(+), '-') <> 'Y'
      AND xdne_freetext.NOTE_TYPE_ID = xrld_note_type.REF_LISTING_DATA_ID
      AND xdne_freetext.DIARY_NOTE_TEXT(+) IS NOT NULL
      AND xrld_note_type.REF_DATA_TYPE = 'NOTE_TYPE'
      AND xrld_note_type.REF_DATA_VALUE = 'HN'
      AND xdne_freetext.NOTE_CLASSIFICATION_ID =  freetext_noteclass.REF_LISTING_DATA_ID (+);
  return highlightNote;
  END get_highlight_note;
  
  
  /*Gets all diary notes that are not highlight notes for the case and aggregates them into one cs:Notes element*/
   FUNCTION get_other_notes(p_case_id XHB_CASE.CASE_ID%TYPE, p_include_standard_notes INTEGER,  p_include_priority_notes INTEGER,  p_include_restricted_notes INTEGER) RETURN XMLTYPE IS
  otherNotes XMLTYPE;
  BEGIN
  SELECT ( XMLFOREST( XMLAGG (XMLELEMENT (E, (CASE WHEN((p_include_standard_notes = 1 AND noteclass.REF_DATA_VALUE = 'Standard') OR
                                                        (p_include_priority_notes = 1 AND noteclass.REF_DATA_VALUE = 'Priority') OR
                                                        (p_include_restricted_notes = 1 AND noteclass.REF_DATA_VALUE = 'Restricted') OR
                                                        (noteclass.REF_DATA_VALUE IS NULL))
                                                        THEN xdne.CREATION_DATE || ' ' || NVL(xrld_predefined_note.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) || CHR(10) END)
                                          )ORDER BY DECODE(noteclass.REF_DATA_VALUE, 'Priority', 0, 'Restricted', 1, 2), 
                                                    DECODE(xrld_note_type.REF_DATA_VALUE, 'IN', 0, 'CN', 1, 2),
                                                    xdne.CREATION_DATE
                              ).EXTRACT('//text()') AS "cs:Notes"))
    INTO otherNotes
    FROM XHB_DIARY_NOTE_ENTRY xdne, XHB_REF_LISTING_DATA xrld_note_type, XHB_REF_LISTING_DATA noteclass, XHB_REF_LISTING_DATA xrld_predefined_note
    WHERE ((xdne.CASE_LISTING_ENTRY_ID IS NOT NULL AND 
            xdne.CASE_LISTING_ENTRY_ID = (SELECT xcle.CASE_LISTING_ENTRY_ID
                                            FROM XHB_CASE_LISTING_ENTRY xcle
                                           WHERE xcle.CASE_ID = p_case_id))
            OR 
            (xdne.CASE_ID IS NOT NULL AND xdne.CASE_ID = p_case_id))
      AND NVL(xdne.OBS_IND,'N') = 'N'
      AND xdne.DIARY_NOTE_PRE_DEFINED_ID = xrld_predefined_note.REF_LISTING_DATA_ID(+)
      AND xrld_predefined_note.REF_DATA_TYPE (+) = 'PREDEFINED_LIST_NOTE'
      AND xdne.NOTE_TYPE_ID = xrld_note_type.REF_LISTING_DATA_ID
      AND xrld_note_type.REF_DATA_TYPE = 'NOTE_TYPE'
      AND xrld_note_type.REF_DATA_VALUE IN ('IN', 'CN', 'GDN')
      AND xdne.NOTE_CLASSIFICATION_ID =  noteclass.REF_LISTING_DATA_ID;
  return otherNotes;
  END get_other_notes;
  
  FUNCTION get_linked_cases(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType IS
  linked_cases XMLType;
  BEGIN
  SELECT XMLAgg(XMLELEMENT("cs:CaseNumber", MAX(linked_case.CASE_TYPE) ||  MAX(linked_case.CASE_NUMBER) || 
                            CASE WHEN MAX(xcdf_linked_fixtures.HEARING_TYPE_ID) IS NOT NULL THEN ' FIX' END
                          )
                          ORDER BY CASE WHEN MAX(xcdf_linked_fixtures.HEARING_TYPE_ID) IS NOT NULL THEN 0  
                                         WHEN MAX(xcol_linked_hearings.HEARING_TYPE_ID) IS NOT NULL THEN 1 
                                         ELSE 2 END
                                    ,MAX(linked_case.CASE_TYPE)
                                    ,MAX(linked_case.CASE_NUMBER) )
    INTO linked_cases
    FROM  XHB_CASE xc,	XHB_CASE linked_case, XHB_CASE_DIARY_FIXTURE xcdf_linked_fixtures, XHB_CASE_LISTING_ENTRY xcle_linked_cases
          ,XHB_CASE_ON_LIST xcol_linked_hearings, XHB_LIST xl_linked_caselist, XHB_REF_LISTING_DATA xrld_linked_list_type
    WHERE xc.CASE_ID = p_case_id
    AND linked_case.CASE_GROUP_NUMBER = xc.CASE_GROUP_NUMBER
    AND linked_case.CASE_ID <> xc.CASE_ID
    AND linked_case.CASE_ID = xcle_linked_cases.CASE_ID (+)
    AND xcle_linked_cases.CASE_LISTING_ENTRY_ID = xcdf_linked_fixtures.CASE_LISTING_ENTRY_ID (+)
    AND linked_case.CASE_ID = xcol_linked_hearings.CASE_ID (+)
    AND xcol_linked_hearings.LIST_ID = xl_linked_caselist.LIST_ID (+)
    AND xl_linked_caselist.LIST_TYPE_ID = xrld_linked_list_type.REF_LISTING_DATA_ID (+)
    AND xrld_linked_list_type.REF_DATA_TYPE (+) = 'LIST_TYPE'
    AND xrld_linked_list_type.REF_DATA_VALUE (+) = 'Warned'
    GROUP BY linked_case.CASE_ID; 
    RETURN linked_cases;
  END get_linked_cases;

 FUNCTION get_non_available_dates(p_case_id XHB_CASE.CASE_ID%TYPE, p_list_start_date XHB_LIST.LIST_START_DATE%TYPE, p_list_end_date XHB_LIST.LIST_END_DATE%TYPE) RETURN XMLType IS
  non_available_dates XMLType;
  BEGIN
  SELECT XMLFOREST(XMLAGG(XMLELEMENT(E, xcnad.START_DATE || ' to ' || xcnad.END_DATE || ', ' )).EXTRACT('//text()') AS "cs:AppealCaseDescription")
    INTO non_available_dates
    FROM  XHB_CASE_NON_AVAIL_DAYS xcnad
    WHERE xcnad.CASE_ID = p_case_id
    AND p_list_start_date <= xcnad.END_DATE      
    AND p_list_end_date >= xcnad.START_DATE      
    AND NVL(xcnad.OBS_IND, '-') <> 'Y';
    RETURN non_available_dates;
  END get_non_available_dates;

  FUNCTION get_defendants_on_case(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType IS
	defendants XMLType;
  BEGIN
    SELECT XMLAgg( XMLELEMENT("cs:Defendants",
                              get_defendant(xdoc.DEFENDANT_ON_CASE_ID, 0, 0, 1)))
    INTO   defendants
    FROM XHB_DEFENDANT_ON_CASE xdoc
    WHERE p_case_id = xdoc.CASE_ID
    AND   (xdoc.OBS_IND <> 'Y' or xdoc.OBS_IND is null);
    
    IF defendants IS NULL THEN
       SELECT XMLELEMENT("cs:Defendants",
				XMLELEMENT("cs:Defendant",
					XMLELEMENT("cs:PersonalDetails",
					   XMLFOREST(XMLFOREST('**No Deft/Applt**' AS "p1:CitizenNameSurname") AS "cs:Name"
								,'yes' AS "cs:IsMasked")
							  )--PersonalDetails
						  )--Defendant
				)--Defendants
       INTO   defendants
       FROM   DUAL;
    END IF;
    RETURN defendants;
  END get_defendants_on_case;
  
  FUNCTION get_defendants_on_hearing(p_hearing_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLType IS
  show_defendant_name INTEGER;
  defendants XMLType;
  BEGIN
  
  SELECT CASE WHEN xcol.IS_COURT_ROOM_LIST_ENTRY = 'Y' OR p_show_court_list = 0 THEN 1 ELSE 0 END INTO show_defendant_name
  FROM XHB_CASE_ON_LIST xcol
  WHERE xcol.CASE_ON_LIST_ID = p_hearing_id;
  
  SELECT XMLAgg(get_defendant(xdoc.DEFENDANT_ON_CASE_ID, 0, p_show_prison_information, 
	CASE WHEN p_show_court_list = 0 THEN 1	-- Always display name if not the court list
		 WHEN p_show_court_list = 1 AND show_defendant_name = 0 THEN 0	-- Court List but case court room list indiciator is not selected so no names
		 WHEN p_show_court_list = 1 AND show_defendant_name = 1 AND NVL(xdocol.IS_COURT_ROOM_LIST_ENTRY,'-') = 'N' THEN 0	-- Court List and case court room list indiciator is selected but defendant is not so no name
		 ELSE 1	-- All other scenarios display name
	END)) INTO defendants
  FROM XHB_DEF_ON_CASE_ON_LIST xdocol,  XHB_DEFENDANT_ON_CASE xdoc
    WHERE p_hearing_id = xdocol.CASE_ON_LIST_ID
    AND   (xdocol.OBS_IND <> 'Y' or xdocol.OBS_IND is null)
    AND   xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
    AND   (xdoc.OBS_IND <> 'Y' or xdoc.OBS_IND is null);
    
    IF defendants IS NULL THEN
       SELECT XMLELEMENT("cs:Defendant",
                XMLELEMENT("cs:PersonalDetails",
                   XMLFOREST(XMLFOREST(SUBSTR(xc.case_title, 36, 35) AS "p1:CitizenNameForename",
                                       SUBSTR(xc.case_title, 71, 35) AS "p1:CitizenNameForename",
                                       SUBSTR(xc.case_title, 1, 35) AS "p1:CitizenNameSurname"
                                       ) AS "cs:Name"
                            ,'yes' AS "cs:IsMasked")
                          )--PersonalDetails
                      )--Defendant 
       INTO   defendants
       FROM   XHB_CASE xc, XHB_CASE_ON_LIST xcol
       WHERE  xc.CASE_ID = xcol.CASE_ID
       AND    NVL(xcol.OBS_IND,'N') = 'N'
       AND    xcol.CASE_ON_LIST_ID = p_hearing_id;
    END IF;
    RETURN defendants;
  END get_defendants_on_hearing;
  
  FUNCTION get_hearings_on_sitting(p_sitting_id XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLType IS
  hearings XMLType;
  BEGIN
  SELECT XMLAgg(get_hearing(xcol.CASE_ON_LIST_ID, p_show_prison_information, p_show_court_list)
                ORDER BY xcol.SEQ_NO) INTO hearings
         FROM XHB_CASE_ON_LIST xcol 
         WHERE xcol.SITTING_ON_LIST_ID = p_sitting_id
         AND   (xcol.RESERVED IS NULL OR xcol.RESERVED <> 'Y')
         AND   (xcol.obs_ind is null or xcol.obs_ind <> 'Y');
      RETURN hearings;
  END get_hearings_on_sitting;

/*<cs:Sitting>
					<cs:CourtRoomNumber>3</cs:CourtRoomNumber>
					<cs:SittingSequenceNo>1</cs:SittingSequenceNo>
					<cs:SittingAt>12:00:00</cs:SittingAt>
					<cs:SittingPriority>T</cs:SittingPriority>
					<cs:Judiciary>
						<cs:Judge>
							<p1:CitizenNameTitle>MRS</p1:CitizenNameTitle>
							<p1:CitizenNameForename>GINA</p1:CitizenNameForename>
							<p1:CitizenNameSurname>SIMS</p1:CitizenNameSurname>
							<p1:CitizenNameRequestedName>GINA</p1:CitizenNameRequestedName>
							<cs:CRESTjudgeID>156</cs:CRESTjudgeID>
						</cs:Judge>
					</cs:Judiciary>
          <Hearings>
    <cs:Sitting/>*/      
  FUNCTION get_sittings_in_courtsite(p_courtsite_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE,
                                     p_sitting_date XHB_SITTING_ON_LIST.TIME_LISTED%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLType IS
  sittings XMLType;
  BEGIN
  SELECT XMLAgg( XMLELEMENT("cs:Sitting", 
                            XMLFOREST(xcr.crest_court_room_no AS "cs:CourtRoomNumber"
                                      ,xsol.SITTING_NUMBER AS "cs:SittingSequenceNo"
                                      ,CASE WHEN xrsc.DE_CODE IS NOT NULL THEN to_char(xsol.TIME_LISTED, 'hh24:mi:ss') END AS "cs:SittingAt"
                                      ,'T' as "cs:SittingPriority"
                                      , xsol.LIST_NOTE_TEXT as "cs:SittingNote")
                                      ,XMLELEMENT("cs:Judiciary",
                                            XMLELEMENT("cs:Judge",XMLFOREST(xrj.TITLE AS "p1:CitizenNameTitle"
                                                                      , xrj.FIRST_NAME AS "p1:CitizenNameForename"
                                                                      , NVL(xrj.SURNAME, 'N/A') AS "p1:CitizenNameSurname"
                                                                      , xrj.HONOURS as "p1:CitizenNameSuffix"
                                                                      , NVL(xrj.FULL_LIST_TITLE1, 'N/A') AS "p1:CitizenNameRequestedName"
                                                                      , xrj.CREST_JUDGE_ID AS "cs:CRESTjudgeID"
                                                                     )
                                                )--judge
                                                ,XMLFOREST( XMLFOREST(xsol.JP1 AS "p1:CitizenNameSurname", xsol.JP1 AS "p1:CitizenNameRequestedName") "cs:Justice")
                                                ,XMLFOREST( XMLFOREST(xsol.JP2 AS "p1:CitizenNameSurname", xsol.JP2 AS "p1:CitizenNameRequestedName") "cs:Justice")
                                                ,XMLFOREST( XMLFOREST(xsol.JP3 AS "p1:CitizenNameSurname", xsol.JP3 AS "p1:CitizenNameRequestedName") "cs:Justice")
                                                ,XMLFOREST( XMLFOREST(xsol.JP4 AS "p1:CitizenNameSurname", xsol.JP4 AS "p1:CitizenNameRequestedName") "cs:Justice")
                                       )--judiciary
                                      ,XMLELEMENT("cs:Hearings", (get_hearings_on_sitting(xsol.SITTING_ON_LIST_ID, p_show_prison_information, p_show_court_list) ) )--Hearings
               ) ORDER BY xcr.CREST_COURT_ROOM_NO, xsol.SITTING_NUMBER ) INTO sittings 
               FROM XHB_SITTING_ON_LIST xsol, XHB_COURT_ROOM xcr, XHB_REF_JUDGE xrj, XHB_REF_SYSTEM_CODE xrsc
                    WHERE xsol.LIST_ID = p_list_id
                    AND  trunc(xsol.TIME_LISTED) = trunc(p_sitting_date)
                    AND (xsol.obs_ind is null OR xsol.obs_ind <> 'Y')
                    AND xsol.COURT_SITE_ID = p_courtsite_id
                    AND xsol.COURT_ROOM_ID = xcr.COURT_ROOM_ID
                    AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID(+)
                    AND xsol.TIME_MARKING_ID = xrsc.REF_SYSTEM_CODE_ID(+);
             RETURN sittings;
  END get_sittings_in_courtsite;

/* Generates a dummy sitting element to store floating hearings as these do not have have a sitting
<cs:Sitting>
					<cs:CourtRoomNumber>99</cs:CourtRoomNumber>
					<cs:SittingSequenceNo>1</cs:SittingSequenceNo>
					<cs:SittingPriority>F</cs:SittingPriority>
					<cs:SittingNote>FLOATERS - COURT TO BE ALLOCATED</cs:SittingNote>
					<cs:Judiciary>
						<cs:Judge>
							<p1:CitizenNameSurname>N/A</p1:CitizenNameSurname>
							<p1:CitizenNameRequestedName>N/A</p1:CitizenNameRequestedName>
							<cs:CRESTjudgeID>0</cs:CRESTjudgeID>
						</cs:Judge>
					</cs:Judiciary>
          <Hearings>
    <cs:Sitting/>*/      
FUNCTION get_floating_cases(p_court_site_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE,
                            p_hearing_date XHB_CASE_ON_LIST.TIME_LISTED%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLType IS
  floater_cases XMLType;
  BEGIN
  SELECT (XMLELEMENT("cs:Sitting", 
             XMLFOREST(99 AS "cs:CourtRoomNumber"
                      ,1 AS "cs:SittingSequenceNo"
                      ,'F' as "cs:SittingPriority"
                      ,NVL(xcrts.FLOATER_TEXT, 'FLOATERS - COURT TO BE ALLOCATED') as "cs:SittingNote")
                      ,XMLELEMENT("cs:Judiciary",
                                  XMLELEMENT("cs:Judge",XMLFOREST('N/A' AS "p1:CitizenNameSurname"
                                                                  , 'N/A' AS "p1:CitizenNameRequestedName"
                                                                  , 0 AS "cs:CRESTjudgeID" )
                                            )--judge
                                )--judiciary
                      ,XMLELEMENT("cs:Hearings",
                         (SELECT XMLAgg(get_hearing(xcol.CASE_ON_LIST_ID, p_show_prison_information, p_show_court_list)
                                        ORDER BY xcol.SEQ_NO)
                                   FROM XHB_CASE_ON_LIST xcol
                                    WHERE xcol.LIST_ID = p_list_id
                                    AND   xcol.SITTING_ON_LIST_ID IS NULL
                                    AND   xcol.FLOATER_CASE = 'Y'
                                    AND   trunc(xcol.TIME_LISTED) = trunc(p_hearing_date)
                                    AND   xcol.COURT_SITE_ID = p_court_site_id 
                                    AND   (xcol.RESERVED IS NULL OR xcol.RESERVED <> 'Y')
                                    AND   (xcol.obs_ind is null or xcol.obs_ind <> 'Y') )--Hearing
                                   )--Hearings   
                        )--Sitting
              )       
          INTO floater_cases
          FROM XHB_COURT_SITE xcrts
          WHERE xcrts.COURT_SITE_ID = p_court_site_id
          AND EXISTS(SELECT 'X' FROM XHB_CASE_ON_LIST xcol
                                WHERE xcol.LIST_ID = p_list_id
                                AND   xcol.SITTING_ON_LIST_ID IS NULL
                                AND   xcol.FLOATER_CASE = 'Y'
                                AND   (xcol.RESERVED IS NULL OR xcol.RESERVED <> 'Y')
                                AND   trunc(xcol.TIME_LISTED) = trunc(p_hearing_date)
                                AND   xcol.COURT_SITE_ID = p_court_site_id 
                                AND   (xcol.obs_ind is null or xcol.obs_ind <> 'Y'));
  
      RETURN floater_cases;
  END get_floating_cases;

  FUNCTION to_xml_date_format(p_date TIMESTAMP) RETURN VARCHAR2 IS
  BEGIN
  RETURN CASE WHEN p_date IS NOT NULL THEN
          to_char( p_date, 'yyyy-mm-dd') || 'T' || to_char(p_date, 'hh24:mi:ss.FF3')
          ELSE NULL END;
  END to_xml_date_format;


 FUNCTION get_prosecution(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType IS
 prosecution XMLType;
 BEGIN
 SELECT(XMLAGG(XMLELEMENT("cs:Prosecution"
         ,XMLFOREST(xrpa.PROSECUTOR_NAME_3 AS "cs:ProsecutingReference"
                , XMLFOREST(xrpa.CPS_CODE AS "cs:OrganisationCode"
                           , xrpa.PROSECUTOR_NAME_1 || ' ' || xrpa.PROSECUTOR_NAME_2 || ' ' || xrpa.PROSECUTOR_NAME_3 AS "cs:OrganisationName"
                   ) "cs:ProsecutingOrganisation"
         )
  ))) INTO prosecution
  FROM XHB_CASE xc, XHB_CASE_PROSECUTOR_AGENCY xcpa, XHB_REF_PROSECUTOR_AGENCY xrpa
  WHERE xc.CASE_ID = p_case_id
  AND xcpa.CASE_ID =  xc.CASE_ID
  AND  xcpa.ref_prosecutor_agency_id = xrpa.ref_prosecutor_agency_id
  AND NVL(xcpa.OBS_IND, '-') <> 'Y'
  AND  ((xc.CASE_TYPE = 'A' AND  NVL(xcpa.PROSECUTOR_TYPE, 'R') = 'R' AND (NVL(xc.CASE_SUB_TYPE, '-') <> 'O' OR NVL(xcpa.RESPONDENT_STATUS, 'R') = 'R')) --If appeal case prosecutor is Respondent Type
    OR   (xc.CASE_TYPE <> 'A'))
  AND ROWNUM = 1;
  RETURN prosecution;
 END get_prosecution;
 
 FUNCTION get_respondent(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType IS
 respondent XMLType;
 BEGIN
 SELECT(XMLAGG(XMLELEMENT("cs:Respondent"
         ,XMLFOREST(NVL(xrpa.PROSECUTOR_NAME_1,'-') AS "cs:RespondentName1",
					NVL(xrpa.PROSECUTOR_NAME_2,'-') AS "cs:RespondentName2",
					NVL(xrpa.PROSECUTOR_NAME_3,'-') AS "cs:RespondentName3")
  ))) INTO respondent
  FROM XHB_CASE xc, XHB_CASE_PROSECUTOR_AGENCY xcpa, XHB_REF_PROSECUTOR_AGENCY xrpa
  WHERE xc.CASE_ID = p_case_id
  AND xc.CASE_TYPE = 'A' AND NVL(xc.CASE_SUB_TYPE, '-') = 'O'	-- Must be a Miscellaneous Appeal case to return a Respondent
  AND xcpa.CASE_ID =  xc.CASE_ID
  AND NVL(xcpa.PROSECUTOR_TYPE, '-') = 'R'
  AND NVL(xcpa.RESPONDENT_STATUS, '-') = 'R'
  AND  xcpa.ref_prosecutor_agency_id = xrpa.ref_prosecutor_agency_id
  AND NVL(xcpa.OBS_IND, '-') <> 'Y'
  AND ROWNUM = 1	-- Only return the first Respondent
  ORDER BY xcpa.CASE_PROS_AGENCY_ID;	-- Order by primary key so the same record is always returned when multiple respondents
  RETURN respondent;
 END get_respondent;

/*Generate the Hearing XML for the specified XHB_CASE_ON_LIST_ID
  <cs:Hearing>
        <cs:HearingSequenceNumber>1</cs:HearingSequenceNumber>
        <cs:HearingDetails HearingType="AEH">
          <cs:HearingDescription>Admissibility of Evidence - Half day</cs:HearingDescription>
          <cs:HearingDate>2018-08-10</cs:HearingDate>
          <cs:ListNote>Free text</cs:ListNote>
        </cs:HearingDetails>
        <cs:CaseNumber>T20100005</cs:CaseNumber>
        <cs:Prosecution>
          <cs:ProsecutingOrganisation>
            <cs:OrganisationCode>001</cs:OrganisationCode>
            <cs:OrganisationName>  CPS</cs:OrganisationName>
            <cs:OrganisationAddress>
              <p1:Line>TH</p1:Line>
              <p1:Line>SGF</p1:Line>
              <p1:Line>H</p1:Line>
              <p1:Line>GFHFDGH</p1:Line>
            </cs:OrganisationAddress>
            <cs:OrganisationDX>KHFKX564</cs:OrganisationDX>
            </cs:ContactDetails>
          </cs:ProsecutingOrganisation>
        </cs:Prosecution>
        <cs:ListNote>Free text</cs:ListNote>
        <cs:NumberOfDefendants>1</cs:NumberOfDefendants>
        <cs:Defendants>
  </Hearing>*/
  FUNCTION get_hearing(p_case_on_list_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER, p_show_court_list IN INTEGER) RETURN XMLTYPE IS
  hearing XMLType;
  BEGIN
   SELECT XMLELEMENT("cs:Hearing",XMLFOREST(xcol.SEQ_NO AS "cs:HearingSequenceNumber")
                     ,XMLELEMENT("cs:HearingDetails",XMLATTRIBUTES(xrht.hearing_type_code AS "HearingType")
                                               ,XMLFOREST(xrht.hearing_type_desc AS "cs:HearingDescription"
                                                         ,NVL(xcol.TIME_LISTED,'') AS "cs:HearingDate"
                                                         ,xcol.LIST_NOTE_TEXT AS "cs:ListNote"
                                                          )
                    )--HearingDetails
            -- ,XMLFOREST('CRESTHearingID' AS "CRESTHearingID")
               ,XMLFOREST( CASE WHEN xrsc.DE_CODE IS NOT NULL THEN xrsc.DE_CODE || ' ' || to_char(xcol.TIME_LISTED, 'HH:MI am') END AS "cs:TimeMarkingNote"
               ,xc.CASE_TYPE || xc.CASE_NUMBER AS "cs:CaseNumber")
               , get_prosecution(xc.CASE_ID)--Prosecution 
               ,(SELECT XMLELEMENT("cs:CommittingCourt" 
                            ,XMLELEMENT("cs:CourtHouseType", 'Magistrates Court')
                            ,XMLELEMENT("cs:CourtHouseCode",XMLATTRIBUTES(NVL(xrcrt.COURT_SHORT_NAME, '---') AS "CourtHouseShortName"), LPAD(xrcrt.CREST_CODE, 4, 0))
                            ,XMLELEMENT("cs:CourtHouseName", xrcrt.COURT_FULL_NAME))
               FROM XHB_REF_COURT xrcrt 
               WHERE xc.REF_COURT_ID = xrcrt.REF_COURT_ID) -- "CommittingCourt"
              ,XMLFOREST(xcol.LIST_NOTE_TEXT || CASE WHEN(xcol.LIST_NOTE_TEXT IS NOT NULL AND xrld.REF_DATA_VALUE IS NOT NULL) THEN ',  ' END || xrld.REF_DATA_VALUE  AS "cs:ListNote")
              ,XMLFOREST(xc.NO_DEFENDANTS_FOR_CASE AS "cs:NumberOfDefendants")
              ,XMLELEMENT("cs:Defendants",
                           (get_defendants_on_hearing(xcol.CASE_ON_LIST_ID, p_show_prison_information, p_show_court_list))--Defendant
                )--Defendants
              ,CASE WHEN p_show_court_list = 1 THEN XMLELEMENT("cs:RespondentDetails",
                           (get_respondent(xc.CASE_ID))--Respondent
                ) END
      )INTO hearing
      FROM XHB_CASE_ON_LIST xcol ,XHB_REF_HEARING_TYPE xrht, XHB_REF_SYSTEM_CODE xrsc,
          XHB_CASE xc, XHB_REF_LISTING_DATA xrld
      WHERE xcol.CASE_ON_LIST_ID = p_case_on_list_id
      AND   xcol.HEARING_TYPE_ID = xrht.ref_hearing_type_id
      AND   NVL(xcol.obs_ind, '-') <> 'Y'
      AND   xcol.TIME_MARKING_ID =  xrsc.REF_SYSTEM_CODE_ID (+)
      AND   xc.CASE_ID = xcol.CASE_ID
      AND   xcol.LIST_NOTE_PREDEFINED_ID = xrld.REF_LISTING_DATA_ID(+)
      AND   xrld.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE';--Hearing

      RETURN hearing;
      
      EXCEPTION
       WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step || p_case_on_list_id); 
  END get_hearing;

END XHB_GET_XML_REPORTS;
/
show errors
