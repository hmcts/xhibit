CREATE OR REPLACE
PACKAGE BODY XHB_GET_DISTRIBUTION_LIST_XML AS
  
  FUNCTION GET_ORIGINATING_COURT(p_ref_court_id XHB_REF_COURT.REF_COURT_ID%TYPE) RETURN XMLType AS
  originatingCourt XMLTYPE;
  BEGIN
   SELECT XMLELEMENT("cs:CaseArrivedFrom"
                     , XMLELEMENT("cs:OriginatingCourt"
                          ,XMLELEMENT("cs:CourtHouseType", 'Magistrates Court')
                          ,XMLELEMENT("cs:CourtHouseCode" ,XMLATTRIBUTES(xmcrt.COURT_SHORT_NAME AS "cs:CourtHouseShortName"), xmcrt.CREST_CODE)--CourtHouseCode
                           ,XMLELEMENT("cs:CourtHouseName", xmcrt.COURT_FULL_NAME)--CourtHouseName 
                           ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(xaddm.ADDRESS_1 AS "Line"
                                                                        ,xaddm.ADDRESS_2 AS "Line"
                                                                        ,xaddm.ADDRESS_3 AS "Line"
                                                                        ,xaddm.TOWN AS "Line"
                                                                        ,xaddm.COUNTY AS "Line"
                                                                        ,xaddm.POSTCODE AS "PostCode")
                              )--CourtHouseAddress
                             ,XMLELEMENT("cs:CourtHouseDX",NVL(xcdmd.contact_value,''))
                             ,XMLELEMENT("cs:CourtHouseTelephone",NVL(xcdmp.contact_value,'')) 
                             ,XMLELEMENT("cs:CourtHouseFax",NVL(xcdmf.contact_value,''))
                      )--OriginatingCourt
             )--CaseArrivedFrom
             INTO originatingCourt
             FROM XHB_REF_COURT xmcrt --Magistrates Court (Original Cout)
            ,    xhb_address xaddm --Magistrates Court address (Original Cout)
            ,    xhb_contact_detail xcdmf --magistrate contact detail fax
            ,    xhb_contact_detail xcdmp --magistrate contact detail phone
            ,    xhb_contact_detail xcdmd --magistrate contact detail dx
            WHERE p_ref_court_id = xmcrt.court_id (+)
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
  
  FUNCTION GET_DAILY_DISTRIBUTION_LIST(p_list_id IN XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB IS
   v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   BEGIN
            SELECT XMLAGG(XMLELEMENT("cs:DailyList"
                                  , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice/xhibit' AS "xmlns:cs"
                                  , 'http://www.courtservice.gov.uk/schemas/courtservice/xhibit' AS "xmlns"
                                  , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                                  , 'http://www.courtservice.gov.uk/schemas/courtservice ListDistributionDailyList-v2-2.xsd' AS " xsi:schemaLocation")
                                  ,XMLELEMENT("cs:DocumentID", XMLFOREST('Daily Distribution List ' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' v' || xl.LIST_NUMBER || ' ' || xl.LIST_START_DATE AS "cs:DocumentName"
                                                                         ,v_unique_id AS "cs:UniqueID"
                                                                         ,'DLD' AS "cs:DocumentType"
                                                                         ,to_xml_date_format(sysdate) AS "cs:TimeStamp"
                                                                        )
                                   )
                                   , GET_LIST_HEADER(xl.LIST_ID) --ListHeader Is Duration required and what is it?
                                   , GET_CROWN_COURT(xl.COURT_ID) --CrownCourt
                                    ,XMLELEMENT("cs:CourtLists",
                                              (SELECT XMLAgg( XMLELEMENT("cs:CourtList"
                                                                         ,XMLATTRIBUTES(xcrts.COURT_SITE_ID AS "id")
                                                                          ,GET_COURT_HOUSE(xcrts.COURT_SITE_ID)--CourtHouse
                                                                          ,XMLELEMENT("cs:Sittings",(get_sittings_in_courtsite(xcrts.COURT_SITE_ID, xl.LIST_ID, xl.list_start_date, 0)),
                                                                                                    (get_floating_cases(xcrts.COURT_SITE_ID, xl.LIST_ID, xl.list_start_date, 0)))--sittings
                                                                         )--CourtList
                                               ) FROM XHB_COURT_SITE xcrts
                                                  WHERE xcrts.COURT_ID = xcrt.COURT_ID 
                                                  AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y'))
                                    )--CourtLists  
                         )--DailyList
             ) --header tag
             .getclobval() INTO v_return_xml
     FROM xhb_list xl
     ,    xhb_court xcrt
     WHERE xl.LIST_ID = p_list_id
     AND xcrt.COURT_ID = xl.COURT_ID
     AND (xcrt.obs_ind is null or xcrt.obs_ind <> 'Y');
     
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
      RAISE_APPLICATION_ERROR(-20001,'No List Information found for List ID:' || p_list_id || ' - ' || SQLERRM||' at step '||v_step);
 
    WHEN OTHERS THEN

			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);  
  END GET_DAILY_DISTRIBUTION_LIST;
  
   FUNCTION GET_FIRM_DISTRIBUTION_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  BEGIN
    SELECT XMLELEMENT("cs:FirmList"
                      , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice/xhibit' AS "xmlns:cs"
                                  , 'http://www.courtservice.gov.uk/schemas/courtservice/xhibit' AS "xmlns"
                                  , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                      , 'http://www.courtservice.gov.uk/schemas/courtservice ListDistributionFirmList-v2-2.xsd' AS " xsi:schemaLocation")
                      ,XMLELEMENT("cs:DocumentID",
                                    XMLFOREST('Firm Distribution List ' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' v' || xl.LIST_NUMBER || ' ' || xl.LIST_START_DATE AS "cs:DocumentName"
                                             ,v_unique_id AS "cs:UniqueID"
                                             ,'FLD' AS "cs:DocumentType"
                                             ,to_xml_date_format(sysdate) AS "cs:TimeStamp"
                                            )
                                )
                      ,GET_LIST_HEADER(xl.LIST_ID)--ListHeader
                      ,GET_CROWN_COURT(xl.COURT_ID) --CrownCourt
                      ,XMLELEMENT("cs:CourtLists", (GET_FIRM_COURT_LISTS(p_list_id, xl.COURT_ID)))--CourtLists
                      ,XMLFOREST(GET_RESERVED_HEARINGS_ON_LIST(xl.LIST_ID)AS "cs:ReserveList")  
             )--FirmList
             .getclobval() INTO v_return_xml
     FROM xhb_list xl
     WHERE xl.LIST_ID = p_list_id;
    
   v_step := '3';
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
      
  END GET_FIRM_DISTRIBUTION_LIST;
  
  FUNCTION GET_FIRM_COURT_LISTS(p_list_id XHB_LIST.LIST_ID%TYPE, p_court_id XHB_COURT.COURT_ID%TYPE) RETURN XMLTYPE AS
  firm_court_lists XMLType;
  BEGIN  
  SELECT XMLAgg( XMLELEMENT("cs:CourtList", XMLATTRIBUTES( xcrts.COURT_SITE_ID AS "id", to_char(court_dates.C_DATE, 'yyyy-mm-dd') AS "SittingDate")
                            ,GET_COURT_HOUSE(xcrts.COURT_SITE_ID)--CourtHouse
                            ,XMLELEMENT("cs:Sittings",(get_sittings_in_courtsite(xcrts.COURT_SITE_ID, p_list_id, court_dates.C_DATE, 0)),
                                                        (get_floating_cases(xcrts.COURT_SITE_ID, p_list_id, court_dates.C_DATE, 0)))--sittings
                            )--CourtList
    ) INTO firm_court_lists 
   FROM XHB_COURT xcrt, XHB_COURT_SITE xcrts, 
   (SELECT  trunc(xcol.TIME_LISTED) C_DATE , xcol.COURT_SITE_ID C_COURT_SITE_ID
       FROM xhb_case_on_list xcol
       WHERE xcol.LIST_ID = p_list_id
       AND  (xcol.RESERVED IS NULL OR xcol.RESERVED <> 'Y')
       AND  (xcol.obs_ind is null or xcol.obs_ind <> 'Y')  
       GROUP BY  trunc(xcol.TIME_LISTED), xcol.COURT_SITE_ID
       ORDER BY C_DATE) court_dates -- A list of all court dates on each court site for this list
    WHERE xcrt.COURT_ID = p_court_id
    AND xcrts.COURT_ID = xcrt.COURT_ID 
    AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y')
    AND   xcrts.COURT_SITE_ID = court_dates.C_COURT_SITE_ID
    ORDER BY court_dates.C_DATE;
        RETURN firm_court_lists;
  END GET_FIRM_COURT_LISTS;

  
  FUNCTION GET_RESERVED_HEARINGS_ON_LIST(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLType AS
  reservedHearings XMLType;
  BEGIN
    SELECT XMLAGG(get_hearing(xcol.CASE_ON_LIST_ID, 0))
    INTO reservedHearings
    FROM XHB_CASE_ON_LIST xcol
    WHERE xcol.LIST_ID = p_list_id
    AND xcol.RESERVED = 'Y'
    AND  (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
    ORDER BY xcol.TIME_LISTED;
    RETURN reservedHearings;
  END GET_RESERVED_HEARINGS_ON_LIST;
  
   /** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GET_WARNED_LIST   'Warned List' extract. JIRA ticket CTX-1371. GetReportRequest will pass in 'WL_DATA'. 
  **/
  
  
  FUNCTION GET_WARNED_DISTRIBUTION_LIST(p_list_id XHB_LIST.LIST_ID%TYPE, v_unique_id VARCHAR) RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  BEGIN
   SELECT XMLAGG(XMLELEMENT("cs:WarnedList"
                          , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice/xhibit' AS "xmlns:cs"
                          , 'http://www.courtservice.gov.uk/schemas/courtservice/xhibit' AS "xmlns"
                          , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                          , 'http://www.courtservice.gov.uk/schemas/courtservice ListDistributionWarnedList-v2-2.xsd' AS " xsi:schemaLocation")
                          ,XMLELEMENT("cs:DocumentID", XMLFOREST('Warned Distribution List ' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' v' || xl.LIST_NUMBER || ' ' || xl.LIST_START_DATE AS "cs:DocumentName"
                                                     ,v_unique_id AS "cs:UniqueID"
                                               ,'WLD' AS "cs:DocumentType"
                                               ,to_xml_date_format(sysdate) AS "cs:TimeStamp"
                                              )
                          )
                          , GET_LIST_HEADER(xl.LIST_ID) --ListHeader
                          , GET_CROWN_COURT(xl.COURT_ID) --CrownCourt
                          , XMLELEMENT("cs:WarnedListDetail", 
                                        XMLFOREST(xcrt.WL_FREE_TEXT AS "cs:Text"
                                                  , 'Any representation about the listing of a case should be made to the Listing Officer ' || 
                                                    CASE WHEN xcrt.WL_REP_PERIOD <= 0 THEN 'Immediately'
                                                         WHEN xcrt.WL_REP_PERIOD > 0 THEN 'no later than ' || xcrt.WL_REP_TIME || ' on ' || to_char(SYSDATE + xcrt.WL_REP_PERIOD, 'DD MONTH YYYY') END AS "cs:Text"
                                                  , 'The Prosecution authority is the Crown Prosecuting Authority unless otherwise stated' AS "cs:Text"
                                                  ,SYSDATE + xcrt.WL_REP_PERIOD AS "cs:DeadLineDate"
                                                  ,xcrt.WL_REP_TIME AS "cs:DeadlineTime")) --ListingInstructions           
                        , XMLELEMENT("cs:CourtLists"
                                    ,(SELECT XMLAgg(XMLELEMENT("cs:CourtList"
                                                    ,XMLATTRIBUTES( xcrts.COURT_SITE_ID AS "id")
                                                    ,XMLELEMENT("cs:CourtHouse", XMLConcat (
                                                          XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                          ,XMLELEMENT("cs:CourtHouseCode", XMLATTRIBUTES(xcrts.SHORT_NAME AS "CourtHouseShortName"), xcrts.CREST_COURT_ID)
                                                          ,XMLELEMENT("cs:CourtHouseName", 'at ' || xcrts.court_site_name)
                                                          ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(NVL(courtsite_address.ADDRESS_1, '-') AS "Line"
                                                                                                  ,NVL(courtsite_address.ADDRESS_2, '-') AS "Line"
                                                                                                  ,NVL(courtsite_address.ADDRESS_3, '-') AS "Line"
                                                                                                  ,courtsite_address.TOWN AS "Line"
                                                                                                  ,courtsite_address.COUNTY AS "Line"
                                                                                                  ,courtsite_address.POSTCODE AS "PostCode"
                                                                                                  )
                                                            ) 
                                                          , XMLFOREST(xcrt.DX_REF as "cs:CourtHouseDX"
                                                                    , courtsite_phone.CONTACT_VALUE AS "cs:CourtHouseTelephone"
                                                                    , courtsite_fax.CONTACT_VALUE AS "cs:CourtHouseFax"
                                                                    , xcrt.COURT_PREFIX AS "cs:Description"))
                                                     )--CourtHouse
                                                    ,XMLELEMENT("cs:WarnedForCourts", XMLELEMENT("cs:WarnedForCourt", xcrts.COURT_SITE_NAME))
                                        ,(SELECT XMLAgg(XMLELEMENT("cs:WithFixedDate",XMLATTRIBUTES(xrht.hearing_type_code AS "id", xrht.hearing_type_code AS "HearingType")
                                          ,XMLAgg(XMLELEMENT("cs:Fixture"
                                            , XMLATTRIBUTES(xcdf.CASE_DIARY_FIXTURE_ID AS "id")
                                            , XMLELEMENT("cs:FixedDate",xcdf.LISTING_DATE)
                                            ,get_other_notes(xc.CASE_ID)                                                          
                                             ,XMLELEMENT("cs:Cases"
                                                ,XMLELEMENT("cs:Case"
                                                                  , XMLATTRIBUTES(xc.CASE_ID AS "id")  
                                                                  , XMLELEMENT("cs:CaseNumber",xc.CASE_TYPE || xc.CASE_NUMBER)
                                                                  , GET_ORIGINATING_COURT(xc.REF_COURT_ID)
                                                                   , XMLFOREST( XMLFOREST(xrht.hearing_type_desc AS "cs:HearingDescription"
                                                                              ,xcdf.LISTING_DATE AS "cs:HearingDate"
                                                                              ,get_highlight_note(xc.CASE_ID) AS "cs:ListNote"
                                                                              )"cs:Hearing")--Hearing
                                                                  , XMLELEMENT("cs:Defendants"
                                                                              ,(SELECT XMLAgg(get_defendant(xdoc.DEFENDANT_ON_CASE_ID, 0, 0))
                                                                               FROM XHB_DEFENDANT_ON_CASE xdoc
                                                                              WHERE xc.CASE_ID = xdoc.CASE_ID))--Defendants
                                                                  , get_prosecution(xc.CASE_ID)--Prosecution 
                                                                 -- AppealDate
                                                                  --DateOfInstigation
                                                                  --MethodOfInstigation" 
                                                                ,XMLFOREST(xc.CLASS_CODE AS "cs:CaseClassNumber")
                                                            )--Case
                                              )--Cases
                                              ,get_linked_cases(xc.CASE_ID)AS "cs:LinkedCases"
                                              )ORDER BY DECODE(xc.CASE_TYPE,'T', 3, 'S', 2, 'A', 1, 0) )--Fixture
                                              ))
                                              FROM XHB_CASE_DIARY_FIXTURE xcdf, XHB_REF_HEARING_TYPE xrht, XHB_CASE_LISTING_ENTRY xcle, XHB_CASE xc, XHB_REF_COURT originatingCourt
                                              WHERE xcdf.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                              AND xcdf.COURT_SITE_ID = xcrts.COURT_SITE_ID
                                              AND xrht.COURT_ID = xl.COURT_ID
                                              AND xcdf.LISTING_DATE BETWEEN xl.LIST_START_DATE AND xl.LIST_END_DATE
                                              AND (xcdf.OBS_IND IS NULL OR xcdf.OBS_IND <> 'Y')
                                              AND xcdf.CASE_LISTING_ENTRY_ID = xcle.CASE_LISTING_ENTRY_ID
                                              AND xcle.CASE_ID = xc.CASE_ID
                                              AND xc.REF_COURT_ID = originatingCourt.REF_COURT_ID(+)
                                              GROUP BY xrht.HEARING_TYPE_CODE) --FixedDateFixtures
                                       ,(SELECT XMLAgg(XMLELEMENT("cs:WithoutFixedDate",XMLATTRIBUTES(xrht.hearing_type_code AS "id", xrht.hearing_type_code AS "HearingType")
                                          ,XMLAgg(XMLELEMENT("cs:Fixture" 
                                                   , XMLATTRIBUTES(xcol.CASE_ON_LIST_ID AS "id")
                                                   , get_other_notes(xc.CASE_ID)  
                                                   , XMLELEMENT("cs:Cases"
                                                      ,XMLELEMENT("cs:Case"
                                                         , XMLATTRIBUTES(xc.CASE_ID AS "id")  
                                                         , XMLELEMENT("cs:CaseNumber", xc.CASE_TYPE || xc.CASE_NUMBER)
                                                         , GET_ORIGINATING_COURT(xc.REF_COURT_ID)
                                                         , XMLFOREST(XMLFOREST(xrht.hearing_type_desc AS "cs:HearingDescription"
                                                                    --,xcdf.LISTING_DATE AS "cs:HearingDate"
                                                                    ,get_highlight_note(xc.CASE_ID) AS "cs:ListNote"
                                                                    )"cs:Hearing")--Hearing
                                                          ,XMLELEMENT("cs:Defendants",
                                                                      (SELECT XMLAgg(get_defendant(xdocol.DEFENDANT_ON_CASE_ID, 0, 0))
                                                                        FROM XHB_DEF_ON_CASE_ON_LIST xdocol
                                                                        WHERE xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                                                        AND (xdocol.OBS_IND IS NULL OR xdocol.OBS_IND <> 'Y')))--Defendants
                                                        ,GET_PROSECUTION(xc.CASE_ID)--Prosecution 
                                                         -- AppealDate
                                                        --DateOfInstigation
                                                        --cs:MethodOfInstigation" 
                                                        ,XMLFOREST(xc.CLASS_CODE AS "cs:CaseClassNumber")
                                                   )--Case
                                           )--Cases
                                          ,(get_linked_cases(xc.CASE_ID))AS "cs:LinkedCases"
                                          )ORDER BY DECODE(xc.CASE_TYPE,'T', 3, 'S', 2, 'A', 1, 0) )))
                                          FROM XHB_CASE_ON_LIST xcol, XHB_REF_HEARING_TYPE xrht, XHB_CASE xc, XHB_REF_COURT originatingCourt
                                          WHERE xcol.LIST_ID =xl.LIST_ID
                                          AND (xcol.OBS_IND IS NULL OR xcol.OBS_IND <> 'Y')
                                          AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                          AND xrht.COURT_ID = xl.COURT_ID
                                          AND xcol.COURT_SITE_ID = xcrts.COURT_SITE_ID
                                          AND xcol.CASE_ID = xc.CASE_ID
                                          AND xc.REF_COURT_ID = originatingCourt.REF_COURT_ID(+)
                                          GROUP BY xrht.HEARING_TYPE_CODE)
                                   )ORDER BY CASE WHEN xcrts.CREST_COURT_ID = xcrt.CREST_COURT_ID THEN 0 ELSE 1 END--CourtList
                                  ) FROM XHB_COURT_SITE xcrts, XHB_ADDRESS courtsite_address, XHB_CONTACT_DETAIL courtsite_phone, XHB_CONTACT_DETAIL courtsite_fax
                                          WHERE xcrts.COURT_ID = xcrt.COURT_ID 
                                          AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y')
                                          AND   xcrts.ADDRESS_ID = courtsite_address.ADDRESS_ID (+)
                                          AND   courtsite_address.ADDRESS_ID = courtsite_phone.ADDRESS_ID (+)
                                          AND   upper(courtsite_phone.CONTACT_TYPE(+)) = 'Phone' 
                                          AND   courtsite_address.ADDRESS_ID = courtsite_fax.ADDRESS_ID (+)
                                          AND   upper(courtsite_fax.CONTACT_TYPE(+)) = 'Fax')
                                )--CourtLists
                 )--WarnedList
     ) --header tag
     .getclobval() INTO v_return_xml
     FROM xhb_list xl,  xhb_court xcrt
     WHERE  xl.list_id = p_list_id
     AND   xl.court_id = xcrt.court_id;

   v_step := '5';
   v_xml_length := 0;
   
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
      
  END GET_WARNED_DISTRIBUTION_LIST; 
  
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
                               ,to_xml_date_format(NVL(xl.PUBLISH_DATE, sysdate)) AS "cs:PublishedTime"
                               ,xl.LIST_ID As "cs:CRESTlistID" --check this field is correct
                               , xl.LIST_END_DATE - xl.LIST_START_DATE AS "cs:Duration"
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
		<cs:CourtHouseCode CourtHouseShortName="SNARE">453</cs:CourtHouseCode>
		<cs:CourtHouseName>SNARESBROOK</cs:CourtHouseName>
		<cs:CourtHouseAddress>
			<Line>747 Snaresbrook Drive</Line>
			<Line>87 Dirrapuwd Dcrr</Line>
			<Line>Isnershire</Line>
			<PostCode>M44 4YK</PostCode>
		</cs:CourtHouseAddress>
		<cs:CourtHouseDX>wahoo</cs:CourtHouseDX>
		<cs:Description>CROWN COURT</cs:Description>
	</cs:CrownCourt>*/
  FUNCTION GET_CROWN_COURT(p_court_id XHB_COURT.COURT_ID%TYPE) RETURN XMLTYPE IS
  crownCourt XMLTYPE;
  BEGIN
   SELECT XMLELEMENT("cs:CrownCourt", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                                     ,XMLELEMENT("cs:CourtHouseCode", XMLATTRIBUTES(xcrt.SHORT_NAME AS "CourtHouseShortName"), xcrt.CREST_COURT_ID)
                                                                     ,XMLELEMENT("cs:CourtHouseName", xcrt.court_name)
                                                                     , XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(NVL(xadc.ADDRESS_1, '-') AS "Line"
                                                                                                                ,NVL(xadc.ADDRESS_2, '-') AS "Line"
                                                                                                                ,NVL(xadc.ADDRESS_3, '-') AS "Line"
                                                                                                                ,xadc.TOWN AS "Line"
                                                                                                                ,xadc.COUNTY AS "Line"
                                                                                                                ,xadc.POSTCODE AS "PostCode"
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
     AND   (xcrt.obs_ind is null or xcrt.obs_ind <> 'Y')
     AND   xcrt.address_id = xadc.address_id (+)
     AND   xadc.address_id = xcdp.address_id (+)
     AND   upper(xcdp.contact_type(+)) = 'Phone' 
     AND   xadc.address_id = xcdd.address_id (+)
     AND   upper(xcdd.contact_type(+)) = 'Fax';
     
     RETURN crownCourt;                                        
  END GET_CROWN_COURT;
  
   /*Get the court house Element for the COURT SITE ID passed in
  <cs:CourtHouse>
				<cs:CourtHouseType>Crown Court</cs:CourtHouseType>
				<cs:CourtHouseCode>453</cs:CourtHouseCode>
				<cs:CourtHouseName>SNARESBROOK</cs:CourtHouseName>
				<cs:CourtHouseAddress>
					<Line>747 Snaresbrook Drive</Line>
					<Line>87 Dirrapuwd Dcrr</Line>
					<Line>Isnershire</Line>
					<PostCode>M44 4YK</PostCode>
				</cs:CourtHouseAddress>
				<cs:CourtHouseDX>wahoo</cs:CourtHouseDX>
				<cs:Description>CROWN COURT</cs:Description>
			</cs:CourtHouse>*/
  FUNCTION GET_COURT_HOUSE(p_court_house_id XHB_COURT_SITE.COURT_SITE_ID%TYPE) RETURN XMLTYPE AS
  courtHouse XMLTYPE;
  BEGIN
   SELECT XMLELEMENT("cs:CourtHouse", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                ,XMLELEMENT("cs:CourtHouseCode", XMLATTRIBUTES(xcrts.SHORT_NAME AS "CourtHouseShortName"), xcrts.CREST_COURT_ID)
                                                ,XMLELEMENT("cs:CourtHouseName", xcrts.court_site_name)
                                                ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(NVL(courtsite_address.ADDRESS_1, '-') AS "Line"
                                                                                        ,NVL(courtsite_address.ADDRESS_2, '-') AS "Line"
                                                                                        ,NVL(courtsite_address.ADDRESS_3, '-') AS "Line"
                                                                                        ,courtsite_address.TOWN AS "Line"
                                                                                        ,courtsite_address.COUNTY AS "Line"
                                                                                        ,courtsite_address.POSTCODE AS "PostCode"
                                                                                        )
                                                  ) 
                                                , XMLFOREST(xcrt.DX_REF as "cs:CourtHouseDX"
                                                          , courtsite_phone.CONTACT_VALUE AS "cs:CourtHouseTelephone"
                                                          , courtsite_fax.CONTACT_VALUE AS "cs:CourtHouseFax"
                                                          , xcrt.COURT_PREFIX AS "cs:Description"))
                     )--CourtHouse
    INTO courtHouse
    FROM XHB_COURT xcrt, XHB_COURT_SITE xcrts, XHB_ADDRESS courtsite_address, XHB_CONTACT_DETAIL courtsite_phone, XHB_CONTACT_DETAIL courtsite_fax
    WHERE xcrts.COURT_SITE_ID = p_court_house_id
    AND   (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y')
    AND   xcrt.COURT_ID = xcrts.COURT_ID
    AND   xcrts.ADDRESS_ID = courtsite_address.ADDRESS_ID (+)
    AND   courtsite_address.ADDRESS_ID = courtsite_phone.ADDRESS_ID (+)
    AND   upper(courtsite_phone.CONTACT_TYPE(+)) = 'Phone' 
    AND   courtsite_address.ADDRESS_ID = courtsite_fax.ADDRESS_ID (+)
    AND   upper(courtsite_fax.CONTACT_TYPE(+)) = 'Fax';
    
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
  SELECT XMLFOREST((SELECT XMLAgg(XMLFOREST(prosecutor_email.CONTACT_VALUE AS "EmailAddress")) 
                  FROM XHB_CONTACT_DETAIL prosecutor_email 
                  WHERE prosecutor_email.ADDRESS_ID = p_address_id
                  AND (prosecutor_email.contact_type  = 'Non Secure Email'
                  OR   prosecutor_email.contact_type  = 'Secure Email')) AS "Email"
              ,(SELECT XMLAgg(XMLFOREST(prosecutor_phone.CONTACT_VALUE AS "TelNationalNumber"))  
                  FROM XHB_CONTACT_DETAIL prosecutor_phone 
                  WHERE prosecutor_phone.ADDRESS_ID = p_address_id
                  AND  prosecutor_phone.contact_type = 'Phone') AS "Telephone"
              ,(SELECT XMLAgg(XMLFOREST(prosecutor_fax.CONTACT_VALUE AS "FaxNationalNumber")) 
                  FROM XHB_CONTACT_DETAIL prosecutor_fax 
                  WHERE prosecutor_fax.ADDRESS_ID = p_address_id
                  AND  prosecutor_fax.contact_type = 'Fax') AS "Fax"   
            ) "ContactDetails"
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
                                    --ArraignmentDate
                                    --ConvictionDate
                                    /*,'tba' AS "cs:OffenceParticulars"*/
                                    , xo.CREST_OFFENCE_ID AS "cs:CRESToffenceNumber"
                                    --Plea
                                    --Verdict
                                    )
                                    ,XMLFOREST((SELECT XMLAgg(XMLELEMENT("cs:Disposal", xrd.DISPOSAL_TITLE))
                                                            FROM XHB_DISPOSAL xdisp, XHB_REF_DISPOSAL xrd
                                                            WHERE xdisp.DEFENDANT_ON_OFFENCE_ID = xdof.DEFENDANT_ON_OFFENCE_ID
                                                            AND xdisp.REF_DISPOSAL_ID = xrd.REF_DISPOSAL_ID                                                                          
                                                )"cs:Disposals"
                                    )
                                   --SentenceTerm
                                   --TermType
                                   --ForLife                                                                                                                             
                                    
                  )) INTO charges
                  FROM XHB_CHARGE xcge, XHB_OFFENCE xo, XHB_DEFENDANT_ON_OFFENCE xdof, XHB_REF_OFFENCE xro, XHB_ADDRESS offence_address, XHB_CASE xc
                  WHERE xdof.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
                   AND   xo.offence_id = xdof.offence_id  
                   AND   xcge.charge_id = xo.charge_id 
                   AND  xcge.CASE_ID = xc.CASE_ID
                   AND   xo.REF_OFFENCE_ID = xro.REF_OFFENCE_ID
                   AND  xo.LOCATION_ADDRESS_ID = offence_address.ADDRESS_ID (+)
                   AND   (xcge.obs_ind is null or xcge.obs_ind <> 'Y')
                   AND   (xo.obs_ind is null or xo.obs_ind <> 'Y')
                   AND   (xdof.obs_ind is null or xdof.obs_ind <> 'Y')
                   AND   (xro.obs_ind is null or xro.obs_ind <> 'Y');
        RETURN charges;           
  END get_charges;


  FUNCTION get_defendant(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE, p_show_public_view IN INTEGER, p_show_prison_information IN INTEGER) RETURN XMLType IS
  defendant XMLType;
  BEGIN
  SELECT XMLELEMENT("cs:Defendant"
                   , XMLATTRIBUTES( xd.CREST_DEFENDANT_ID AS "id")
                   , XMLELEMENT("cs:PersonalDetails",
                             XMLFOREST(XMLFOREST(CASE WHEN (p_show_public_view = 1 AND xdoc.IS_MASKED = 'Y') THEN ' ' ELSE xd.FIRST_NAME END AS "CitizenNameForename"
                                                ,CASE WHEN (p_show_public_view = 1 AND xdoc.IS_MASKED = 'Y') THEN xdoc.MASKED_NAME ELSE xd.SURNAME END AS "CitizenNameSurname")"cs:Name"
                                          , xdoc.MASKED_NAME AS "cs:MaskedName"
                                     , DECODE(xdoc.is_masked, 'Y', 'yes', 'no') AS "cs:IsMasked"
                              
                                     , XMLFOREST(xd.date_of_birth AS "BirthDate"
                                                 ,CASE WHEN xd.date_of_birth IS NOT NULL THEN 'not verified' ELSE NULL END AS "VerifiedBy") "cs:DateOfBirth"
                                     -- , xd.AGE
                                     , DECODE(xd.gender, 1, 'male', 2, 'female', 'unknown') AS "cs:Sex"
                                     , XMLFOREST(NVL(xadd.ADDRESS_1, '-') AS "Line"
                                                 ,NVL(xadd.ADDRESS_2, '-') AS "Line"
                                                 ,NVL(xadd.ADDRESS_3, '-') AS "Line"
                                                 ,xadd.TOWN AS "Line"
                                                 ,xadd.COUNTY AS "Line"
                                                 ,xadd.POSTCODE AS "PostCode"
                                                  ) "cs:Address" )          
                                )--PersonalDetails
                     , get_contact_details(xd.ADDRESS_ID) "cs:ContactDetails"
                     ,XMLFOREST(XMLFOREST(xdoc.ASN AS "cs:ASN")"cs:ASNs" 
                     ,xdoc.DEFENDANT_NUMBER AS "cs:DefendantNumber"
                     ,xd.CREST_DEFENDANT_ID AS "cs:CRESTdefendantID"
                     ,xdoc.PNC_ID AS "cs:PNCnumber"
                     ,xdoc.PTIURN AS "cs:URN"
                     --CRO
                     --,xc.magistrates_case_ref AS "cs:MagistratesCourtRefNumber"--check that this field is right
                     ,CASE WHEN (p_show_prison_information = 1) THEN xdr.REFERENCE_VALUE END AS "cs:PrisonerID"
                     ,XMLFOREST(CASE WHEN (p_show_prison_information = 1) THEN defPrisonLocation.REFERENCE_VALUE END AS "cs:Location") AS "cs:PrisonLocation"--PrisonLocation
                     ,DECODE(xdoc.CURRENT_BC_STATUS, 'B', 'On bail', 'C', 'In custody', 'N', 'Not applicable') AS "cs:CustodyStatus")
                     , XMLELEMENT("cs:Counsel"
                      , (SELECT XMLAgg(XMLELEMENT("cs:Solicitor"
                                           ,XMLATTRIBUTES(xrsf.CREST_SOF_ID AS "id")
                                           , XMLELEMENT("cs:Party"
                                             , XMLELEMENT("cs:Organisation"
                                                
                                               , XMLELEMENT("cs:OrganisationName",xrsf.solicitor_firm_name)--OrganisationName
                                               , XMLELEMENT("cs:OrganisationAddress",XMLFOREST(NVL(solicitor_address.ADDRESS_1, '-') AS "Line"
                                                                                             ,NVL(solicitor_address.ADDRESS_2, '-') AS "Line"
                                                                                             ,NVL(solicitor_address.ADDRESS_3, '-') AS "Line"
                                                                                             ,solicitor_address.TOWN AS "Line"
                                                                                             ,solicitor_address.COUNTY AS "Line"
                                                                                             ,solicitor_address.POSTCODE AS "PostCode")
                                                           )--OrganisationAddress
                                                         , XMLFOREST(xrsf.DX_REF AS "cs:OrganisationDX")
                                                         , get_contact_details( xrsf.ADDRESS_ID) "cs:ContactDetails"         
                                               )--Organisation
                                            )--Party
                                         , XMLFOREST(xdocrsf.REP_ST_DATE AS "cs:StartDate"
                                                  , xdocrsf.REP_END_DATE AS "cs:EndDate")
                                          )--Solicitor
                                          ) FROM XHB_DEF_ON_CASE_REF_SOL_FIRM xdocrsf, XHB_REF_SOLICITOR_FIRM xrsf, XHB_ADDRESS solicitor_address
                                          WHERE xdoc.DEFENDANT_ON_CASE_ID = xdocrsf.DEFENDANT_ON_CASE_ID 
                                          AND  (xdocrsf.obs_ind is null or xdocrsf.obs_ind <> 'Y')
                                          AND xdocrsf.REF_SOLICITOR_FIRM_ID = xrsf.REF_SOLICITOR_FIRM_ID
                                          AND  (xrsf.obs_ind is null or xrsf.obs_ind <> 'Y')
                                          AND xrsf.ADDRESS_ID = solicitor_address.ADDRESS_ID(+)))--Counsel
                        ,XMLFOREST(--,XMLATTRIBUTES(xhb_get_xml_reports.GetChargeCnt(xc.case_number) AS "NumberOfCharges"),
                                    (get_charges(xdoc.DEFENDANT_ON_CASE_ID)  )"cs:Charges"
                        )  --Charges                        
                        --AdditionalNotes
          ) INTO defendant
          FROM XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_ADDRESS xadd, XHB_DEFENDANT_REFERENCE xdr, XHB_DEFENDANT_REFERENCE defPrisonLocation
           WHERE xdoc.DEFENDANT_ON_CASE_ID = p_defendant_on_case_id
            AND   (xdoc.OBS_IND <> 'Y' or xdoc.OBS_IND is null)
            AND   xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
            AND   xd.address_id = xadd.address_id(+)
            AND   xdoc.DEFENDANT_ID = xdr.DEFENDANT_ID (+) --prisonerf
            AND   'PRISONER NUMBER' = upper(xdr.REFERENCE_NAME (+)) 
            and   xdoc.DEFENDANT_ID = defPrisonLocation.DEFENDANT_ID (+)
            and   'PRISONER LOCATION' = upper(defPrisonLocation.REFERENCE_NAME (+))
            AND ROWNUM = 1;
    RETURN defendant;
    
    EXCEPTION
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
      AND xdne_freetext.NOTE_CLASSIFICATION_ID =  freetext_noteclass.REF_LISTING_DATA_ID;
  return highlightNote;
  END get_highlight_note;
  
  
  /*Gets all notes that are not highlight notes for the case and aggregates them into one cs:Notes element*/
   FUNCTION get_other_notes(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLTYPE IS
  otherNotes XMLTYPE;
  BEGIN
  SELECT ( XMLFOREST( XMLAGG (XMLELEMENT (E, (xdne.CREATION_DATE || ' ' || NVL(xrld_predefined_note.REF_DATA_VALUE, xdne.DIARY_NOTE_TEXT) || CHR(10))
                                          )ORDER BY DECODE(noteclass.REF_DATA_VALUE, 'Priority', 0, 'Restricted', 1, 2), 
                                                    DECODE(xrld_note_type.REF_DATA_VALUE, 'IN', 0, 'CN', 1, 2),
                                                    xdne.CREATION_DATE
                              ).EXTRACT('//text()') AS "cs:Notes"))
    INTO otherNotes
    FROM XHB_DIARY_NOTE_ENTRY xdne, XHB_CASE_LISTING_ENTRY xcle, XHB_REF_LISTING_DATA xrld_note_type, XHB_REF_LISTING_DATA noteclass, XHB_REF_LISTING_DATA xrld_predefined_note
    WHERE p_case_id = xcle.CASE_ID
      AND xcle.CASE_LISTING_ENTRY_ID = xdne.CASE_LISTING_ENTRY_ID
      AND NVL(xdne.OBS_IND(+), '-') <> 'Y'
      AND xdne.DIARY_NOTE_PRE_DEFINED_ID = xrld_predefined_note.REF_LISTING_DATA_ID(+)
      AND xrld_predefined_note.REF_DATA_TYPE (+) = 'PREDEFINED_LIST_NOTE'
      AND xdne.NOTE_TYPE_ID = xrld_note_type.REF_LISTING_DATA_ID
      AND xrld_note_type.REF_DATA_TYPE = 'NOTE_TYPE'
      AND xrld_note_type.REF_DATA_VALUE <> 'HN'
      AND xdne.NOTE_CLASSIFICATION_ID =  noteclass.REF_LISTING_DATA_ID;
  return otherNotes;
  END get_other_notes;
  
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
  
  FUNCTION get_linked_cases(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType IS
  linked_cases XMLType;
  BEGIN
  SELECT XMLAgg(XMLELEMENT("cs:CaseNumber", MAX(linked_case.CASE_TYPE) ||  MAX(linked_case.CASE_NUMBER) || 
                            CASE WHEN MAX(xrht_linked_fixture_type.HEARING_TYPE_DESC) IS NOT NULL THEN ' FIX' END  
                          )
                          ORDER BY CASE WHEN MAX(xrht_linked_fixture_type.HEARING_TYPE_DESC) IS NOT NULL THEN 0  
                                         WHEN MAX(xcol_linked_hearings.HEARING_TYPE_ID) IS NOT NULL THEN 1 
                                         ELSE 2 END
                                    ,MAX(linked_case.CASE_TYPE)
                                    ,MAX(linked_case.CASE_NUMBER) )
    INTO linked_cases
    FROM  XHB_CASE xc,	XHB_CASE linked_case, XHB_CASE_DIARY_FIXTURE xcdf_linked_fixtures, XHB_CASE_LISTING_ENTRY xcle_linked_cases, XHB_REF_HEARING_TYPE xrht_linked_fixture_type
          ,XHB_CASE_ON_LIST xcol_linked_hearings, XHB_LIST xl_linked_caselist, XHB_REF_LISTING_DATA xrld_linked_list_type
    WHERE xc.CASE_ID = p_case_id
    AND linked_case.CASE_GROUP_NUMBER = xc.CASE_GROUP_NUMBER
    AND linked_case.CASE_ID <> xc.CASE_ID
    AND linked_case.CASE_ID = xcle_linked_cases.CASE_ID (+)
    AND xcle_linked_cases.CASE_LISTING_ENTRY_ID = xcdf_linked_fixtures.CASE_LISTING_ENTRY_ID (+)
    AND xcdf_linked_fixtures.HEARING_TYPE_ID = xrht_linked_fixture_type.REF_HEARING_TYPE_ID (+)
    AND linked_case.CASE_ID = xcol_linked_hearings.CASE_ID (+)
    AND xcol_linked_hearings.LIST_ID = xl_linked_caselist.LIST_ID (+)
    AND xl_linked_caselist.LIST_TYPE_ID = xrld_linked_list_type.REF_LISTING_DATA_ID (+)
    AND xrld_linked_list_type.REF_DATA_TYPE (+) = 'LIST_TYPE'
    AND xrld_linked_list_type.REF_DATA_VALUE (+) = 'Warned'
    GROUP BY linked_case.CASE_ID; 
    RETURN linked_cases;
  END get_linked_cases;

  FUNCTION get_defendants_on_hearing(p_hearing_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER) RETURN XMLType IS
  defendants XMLType;
  BEGIN
  SELECT XMLAgg(get_defendant(xdoc.DEFENDANT_ON_CASE_ID, 0, p_show_prison_information)) INTO defendants
  FROM XHB_DEF_ON_CASE_ON_LIST xdocol,  XHB_DEFENDANT_ON_CASE xdoc
    WHERE p_hearing_id = xdocol.CASE_ON_LIST_ID
    AND   (xdocol.OBS_IND <> 'Y' or xdocol.OBS_IND is null)
    AND   xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
    AND   (xdoc.OBS_IND <> 'Y' or xdoc.OBS_IND is null);
    RETURN defendants;
  END get_defendants_on_hearing;
  
  FUNCTION get_hearings_on_sitting(p_sitting_id XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER) RETURN XMLType IS
  hearings XMLType;
  BEGIN
  SELECT XMLAgg(get_hearing(xcol.CASE_ON_LIST_ID, p_show_prison_information)) INTO hearings
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
							<CitizenNameTitle>MRS</CitizenNameTitle>
							<CitizenNameForename>GINA</CitizenNameForename>
							<CitizenNameSurname>SIMS</CitizenNameSurname>
							<CitizenNameRequestedName>GINA</CitizenNameRequestedName>
							<cs:CRESTjudgeID>156</cs:CRESTjudgeID>
						</cs:Judge>
					</cs:Judiciary>
          <Hearings>
    <cs:Sitting/>*/      
  FUNCTION get_sittings_in_courtsite(p_courtsite_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE,
                                     p_sitting_date XHB_SITTING_ON_LIST.TIME_LISTED%TYPE, p_show_prison_information IN INTEGER) RETURN XMLType IS
  sittings XMLType;
  BEGIN
  SELECT XMLAgg( XMLELEMENT("cs:Sitting" 
                            ,XMLATTRIBUTES(xsol.SITTING_ON_LIST_ID AS "id")
                            ,XMLFOREST(xcr.crest_court_room_no AS "cs:CourtRoomNumber"
                                      ,xsol.SITTING_NUMBER AS "cs:SittingSequenceNo"
                                      ,CASE WHEN xrsc.DE_CODE IS NOT NULL THEN to_char(xsol.TIME_LISTED, 'hh24:mi:ss') END AS "cs:SittingAt"
                                      ,'T' as "cs:SittingPriority"
                                      , xsol.LIST_NOTE_TEXT as "cs:SittingNote")
                                      ,XMLELEMENT("cs:Judiciary",
                                            XMLELEMENT("cs:Judge",XMLFOREST(xrj.TITLE AS "CitizenNameTitle"
                                                                      , xrj.FIRST_NAME AS "CitizenNameForename"
                                                                      , NVL(xrj.SURNAME, 'N/A') AS "CitizenNameSurname"
                                                                      , xrj.HONOURS as "CitizenNameSuffix"
                                                                      , NVL(xrj.FULL_LIST_TITLE1, 'N/A') AS "CitizenNameRequestedName"
                                                                      , xrj.CREST_JUDGE_ID AS "cs:CRESTjudgeID"
                                                                     )
                                                )--judge
                                                ,XMLFOREST( XMLFOREST(xsol.JP1 AS "CitizenNameSurname", xsol.JP1 AS "CitizenNameRequestedName") "cs:Justice")
                                                ,XMLFOREST( XMLFOREST(xsol.JP2 AS "CitizenNameSurname", xsol.JP2 AS "CitizenNameRequestedName") "cs:Justice")
                                                ,XMLFOREST( XMLFOREST(xsol.JP3 AS "CitizenNameSurname", xsol.JP3 AS "CitizenNameRequestedName") "cs:Justice")
                                                ,XMLFOREST( XMLFOREST(xsol.JP4 AS "CitizenNameSurname", xsol.JP4 AS "CitizenNameRequestedName") "cs:Justice")
                                       )--judiciary
                                      ,XMLELEMENT("cs:Hearings", (get_hearings_on_sitting(xsol.SITTING_ON_LIST_ID, p_show_prison_information) ) )--Hearings                   
               )) INTO sittings 
               FROM XHB_SITTING_ON_LIST xsol, XHB_COURT_ROOM xcr, XHB_REF_JUDGE xrj, XHB_REF_SYSTEM_CODE xrsc
                    WHERE xsol.LIST_ID = p_list_id
                    AND  trunc(xsol.TIME_LISTED) = trunc(p_sitting_date)
                    AND (xsol.obs_ind is null OR xsol.obs_ind <> 'Y')
                    AND xsol.COURT_SITE_ID = p_courtsite_id
                    AND xsol.COURT_ROOM_ID = xcr.COURT_ROOM_ID
                    AND (xcr.obs_ind is null OR xcr.obs_ind <> 'Y')
                    AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID(+)
                    AND NVL(xrj.obs_ind(+), '-') <> 'Y'
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
							<CitizenNameSurname>N/A</CitizenNameSurname>
							<CitizenNameRequestedName>N/A</CitizenNameRequestedName>
							<cs:CRESTjudgeID>0</cs:CRESTjudgeID>
						</cs:Judge>
					</cs:Judiciary>
          <Hearings>
    <cs:Sitting/>*/      
FUNCTION get_floating_cases(p_court_site_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE,
                            p_hearing_date XHB_CASE_ON_LIST.TIME_LISTED%TYPE, p_show_prison_information IN INTEGER) RETURN XMLType IS
  floater_cases XMLType;
  BEGIN
  SELECT (XMLELEMENT("cs:Sitting"
            ,XMLATTRIBUTES(99 AS "id")              
            ,XMLFOREST(99 AS "cs:CourtRoomNumber"
                      ,1 AS "cs:SittingSequenceNo"
                      ,'F' as "cs:SittingPriority"
                      ,'FLOATERS - COURT TO BE ALLOCATED' as "cs:SittingNote")
                      ,XMLELEMENT("cs:Judiciary",
                                  XMLELEMENT("cs:Judge",XMLFOREST('N/A' AS "CitizenNameSurname"
                                                                  , 'N/A' AS "CitizenNameRequestedName"
                                                                  , 0 AS "cs:CRESTjudgeID" )
                                            )--judge
                                )--judiciary
                      ,XMLELEMENT("cs:Hearings",
                         (SELECT XMLAgg(get_hearing(xcol.CASE_ON_LIST_ID, p_show_prison_information)) 
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
          FROM DUAL
         WHERE EXISTS(SELECT 'X' FROM XHB_CASE_ON_LIST xcol
                                WHERE xcol.LIST_ID = p_list_id
                                AND   xcol.SITTING_ON_LIST_ID IS NULL
                                AND   xcol.FLOATER_CASE = 'Y'
                                AND   (xcol.RESERVED IS NULL OR xcol.RESERVED <> 'Y')
                                AND   trunc(xcol.TIME_LISTED) = trunc(p_hearing_date)
                                AND   xcol.COURT_SITE_ID = p_court_site_id 
                                AND   (xcol.obs_ind is null or xcol.obs_ind <> 'Y'));
  
      RETURN floater_cases;
  END get_floating_cases;

  FUNCTION to_xml_date_format(p_date DATE) RETURN VARCHAR2 IS
  BEGIN
  RETURN CASE WHEN p_date IS NOT NULL THEN
          to_char( p_date, 'yyyy-mm-dd') || 'T' || to_char(p_date, 'hh24:mi:ss')
          ELSE NULL END;
  END to_xml_date_format;


 FUNCTION get_prosecution(p_case_id XHB_CASE.CASE_ID%TYPE) RETURN XMLType IS
 prosecution XMLType;
 BEGIN
 SELECT(XMLELEMENT("cs:Prosecution"
         ,XMLATTRIBUTES(xrpa.CREST_OPPOSER_ID AS "id")
         ,XMLFOREST(xrpa.PROSECUTOR_NAME_3 AS "cs:ProsecutingReference"
                , XMLFOREST(xrpa.CPS_CODE AS "cs:OrganisationCode"
                           , xrpa.PROSECUTOR_NAME_1 || ' ' || xrpa.PROSECUTOR_NAME_2 || ' ' || xrpa.PROSECUTOR_NAME_3 AS "cs:OrganisationName"
                           , XMLFOREST(NVL(prosecutor_address.ADDRESS_1, '-') AS "Line"
                                      ,NVL(prosecutor_address.ADDRESS_2, '-') AS "Line"
                                      ,NVL(prosecutor_address.ADDRESS_3, '-') AS "Line"
                                      ,prosecutor_address.TOWN AS "Line"
                                      ,prosecutor_address.COUNTY AS "Line"
                                      ,prosecutor_address.POSTCODE AS "PostCode"
                                      ) "cs:OrganisationAddress"
                            ,xrpa.DX_REF AS "cs:OrganisationDX"
                            ,  get_contact_details(prosecutor_address.ADDRESS_ID) "cs:ContactDetails"
                            ) "cs:ProsecutingOrganisation"  
                      )
  )) INTO prosecution 
  FROM XHB_CASE xc, XHB_CASE_PROSECUTOR_AGENCY xcpa, XHB_REF_PROSECUTOR_AGENCY xrpa, XHB_ADDRESS prosecutor_address 
  WHERE xc.CASE_ID = p_case_id
  AND xcpa.CASE_ID =  xc.CASE_ID
  AND  xcpa.ref_prosecutor_agency_id = xrpa.ref_prosecutor_agency_id 
  AND  xrpa.ADDRESS_ID = prosecutor_address.ADDRESS_ID (+)
  AND NVL(xcpa.OBS_IND, '-') <> 'Y'
  AND  ((xc.CASE_TYPE = 'A' AND  NVL(xcpa.PROSECUTOR_TYPE, 'R') = 'R' AND NVL(xcpa.RESPONDENT_STATUS, 'R') = 'R')
    OR   (xc.CASE_TYPE <> 'A'))
  AND ROWNUM = 1;
  RETURN prosecution;
 END get_prosecution;

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
              <Line>TH</Line>
              <Line>SGF</Line>
              <Line>H</Line>
              <Line>GFHFDGH</Line>
            </cs:OrganisationAddress>
            <cs:OrganisationDX>KHFKX564</cs:OrganisationDX>
            </cs:ContactDetails>
          </cs:ProsecutingOrganisation>
        </cs:Prosecution>
        <cs:ListNote>Free text</cs:ListNote>
        <cs:NumberOfDefendants>1</cs:NumberOfDefendants>
        <cs:Defendants>
  </Hearing>*/
  FUNCTION get_hearing(p_case_on_list_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE, p_show_prison_information IN INTEGER) RETURN XMLTYPE IS
  hearing XMLType;
  BEGIN
   SELECT XMLELEMENT("cs:Hearing"
                         ,XMLATTRIBUTES( xcol.CASE_ON_LIST_ID AS "id")
                         ,XMLFOREST(xcol.SEQ_NO AS "cs:HearingSequenceNumber")
                         ,XMLELEMENT("cs:HearingDetails",XMLATTRIBUTES(xrht.hearing_type_code AS "HearingType"
                                                                       ,xcol.SEQ_NO AS "CRESTListSequence")
                                                     ,XMLFOREST(xrht.hearing_type_desc AS "cs:HearingDescription"
                                                               ,NVL(xcol.TIME_LISTED,'') AS "cs:HearingDate"
                                                               ,xcol.LIST_NOTE_TEXT AS "cs:ListNote"
                                                                )
                        )--HearingDetails
                      ,XMLFOREST(xcol.CASE_ON_LIST_ID AS "cs:CRESThearingID"
                      ,CASE WHEN xrsc.DE_CODE IS NOT NULL THEN xrsc.DE_CODE || ' ' || to_char(xcol.TIME_LISTED, 'HH:MI am') END AS "cs:TimeMarkingNote"
                      ,xc.CASE_TYPE || xc.CASE_NUMBER AS "cs:CaseNumber")
                      ,get_prosecution(xc.CASE_ID)
              ,XMLFOREST(xcol.LIST_NOTE_TEXT || CASE WHEN(xcol.LIST_NOTE_TEXT IS NOT NULL AND xrld.REF_DATA_VALUE IS NOT NULL) THEN ',  ' END || xrld.REF_DATA_VALUE  AS "cs:ListNote")
              ,XMLFOREST(xc.NO_DEFENDANTS_FOR_CASE AS "cs:NumberOfDefendants")
              ,XMLELEMENT("cs:Defendants",
                           (get_defendants_on_hearing(xcol.CASE_ON_LIST_ID, p_show_prison_information))--Defendant
                )--Defendants
                --Respondent
      )INTO hearing
      FROM XHB_CASE_ON_LIST xcol ,XHB_REF_HEARING_TYPE xrht, XHB_REF_SYSTEM_CODE xrsc,
          XHB_CASE xc, XHB_REF_LISTING_DATA xrld
      WHERE xcol.CASE_ON_LIST_ID = p_case_on_list_id
      AND   xcol.HEARING_TYPE_ID = xrht.ref_hearing_type_id
      AND   NVL(xrht.obs_ind, '-') <> 'Y'
      AND   xcol.TIME_MARKING_ID =  xrsc.REF_SYSTEM_CODE_ID (+)
      AND   xc.CASE_ID = xcol.CASE_ID
      AND xcol.LIST_NOTE_PREDEFINED_ID = xrld.REF_LISTING_DATA_ID(+)
      AND xrld.REF_DATA_TYPE(+) = 'PREDEFINED_LIST_NOTE'
      AND NVL(xrld.obs_ind(+), '-') <> 'Y';--Hearing

      RETURN hearing;
      
      EXCEPTION
       WHEN OTHERS THEN
			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step || p_case_on_list_id); 
  END get_hearing;

END XHB_GET_DISTRIBUTION_LIST_XML;
/
show errors
