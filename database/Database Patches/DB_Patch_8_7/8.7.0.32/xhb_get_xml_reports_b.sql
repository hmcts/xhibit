create or replace PACKAGE BODY XHB_GET_XML_REPORTS AS
 
  
  PROCEDURE GetReportRequest (p_report       IN  VARCHAR2
                             ,p_clob_out     OUT NOCOPY CLOB
                            ) AS
                            
  v_return_clob CLOB;                          
  BEGIN
   /*DECLARE
      x_clob_out CLOB;
     BEGIN
      xhb_get_xml_reports.GetReportRequest(p_report => 'FL_DATA'
                                          ,p_clob_out => x_clob_out);
      SYS.DBMS_OUTPUT.PUT_LINE (x_clob_out);   
    END GetReportRequest; */
  
    v_step := '1';
    IF upper(p_report) NOT IN ('FL_DATA', 'DL_DATA', 'PL_DATA', 'WL_DATA')
     THEN RAISE_APPLICATION_ERROR(-20001,'Invalid report name paramater');

    ELSE
    v_step := '2';
     IF upper(p_report) = 'FL_DATA'
      THEN 
       SELECT GetFlData
       INTO v_return_clob
       FROM dual;
         p_clob_out := v_return_clob;
      ELSIF upper(p_report) = 'DL_DATA'
      THEN
       SELECT GetDlData(68)
       INTO v_return_clob
       FROM dual;
         p_clob_out := v_return_clob;
      ELSIF upper(p_report) = 'PL_DATA'
      THEN
       SELECT GetPlData
       INTO v_return_clob
       FROM dual;
         p_clob_out := v_return_clob;
      ELSIF upper(p_report) = 'RL_DATA'
      THEN
       SELECT GetRlData
       INTO v_return_clob
       FROM dual;
         p_clob_out := v_return_clob; 
       ELSIF upper(p_report) = 'WL_DATA'
      THEN
       SELECT GetWlData
       INTO v_return_clob
       FROM dual;
         p_clob_out := v_return_clob;  
     END IF;
    END IF;
    
  END GetReportRequest;

/** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GetFlData   'Firm List' extract. JIRA ticket CTX-1359. GetReportRequest will pass in 'FL_DATA'. 
  **/
  
  
  FUNCTION GetFlData RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   CURSOR get_xml_c IS
    SELECT XMLELEMENT("cs:FLData",
                                  XMLELEMENT("cs:CourtSite", XMLFOREST(xcs.court_site_code AS "cs:SITE_CODE"
                                                                  , xcs.court_site_name AS "cs:SITE_NAME"
                                                                   )
                                            ),
                                  XMLELEMENT("cs:CourtRoom", XMLFOREST(xcr.court_site_id AS "cs:SITE_CODE"
                                                                  , xcr.court_room_id AS "cs:COURTROOM_NUMBER"
                                                                   )
                                            ),
                                  XMLELEMENT("cs:Case", XMLFOREST(xc.case_number AS "cs:CASE_NO"
                                                             , xc.case_type AS "cs:CASE_TYPE"
                                                             , xrj.psd_court_code AS "cs:PSD_CT_CODE"
                                                             , 'DECODE2214' AS "DECODE2214"
                                                             , xc.indictment_info_1 AS "cs:IND_COMMENTS1"
                                                             , xc.indictment_info_2 AS "cs:IND_COMMENTS2"
                                                             , xc.indictment_info_3 AS "cs:IND_COMMENTS3"
                                                             , xc.indictment_info_4 AS "cs:IND_COMMENTS4"
                                                             , xc.indictment_info_5 AS "cs:IND_COMMENTS5"
                                                             , xc.indictment_info_6 AS "cs:IND_COMMENTS6"
                                                             , CASE
                                                                  WHEN xc.case_type = 'B' THEN xc.case_title 
                                                                  ELSE 'SHORT NAME' --unsure as to what field this refers
                                                                END "cs:FUNC2318"
                                                             , 'PSD_REFERENCE' AS "cs:PSD_REFERENCE"
                                                             , 'NAME_1' AS "cs:NAME_1"
                                                             , xo.appeal_type AS "cs:appeal_type"
                                                              )
                                            ),
                                  XMLELEMENT("cs:Hearing", XMLFOREST(xc.case_number AS "cs:CASE_NO"
                                                                  ,xc.case_type AS "cs:CASE_TYPE"
                                                                  ,'CODE' AS "cs:CODE"
                                                                  ,xrht.hearing_type_code AS "cs:HEARING_TYPE"
                                                                  ,xrht.category AS "cs:CATEGORY"
                                                                  )
                                            ),
                                  XMLELEMENT("cs:HearingList", XMLFOREST(xhl.list_type AS "cs:LIST_TYPE"
                                                                  ,xhl.start_date AS "cs:LIST_DATE"
                                                                  ,xhl.end_date AS "cs:END_DATE"
                                                                  ,xhl.status AS "cs:LIST_STATUS"
                                                                  ,xhl.edition_no AS "cs:EDITION_NO"
                                                                  ,xhl.published_time AS "cs:DATE_PUBLISHED"
                                                                  ,'SHORT_NAME' AS "cs:SHORT_NAME"
                                                                  ,xhl.list_id AS "cs:LIST_ID"
                                                                  ,'CC_IND' AS "cs:CC_IND"
                                                                  ,xhl.print_reference AS "cs:PRINT_JOB_ID"
                                                                  )
                                            ),
                                  XMLELEMENT("cs:Sitting", XMLFOREST(xsol.judge_ref_id AS "cs:JUD_SEQ_NO"
                                                                  ,'PRIORITY' as "cs:PRORITY"
                                                                  ,xs.sitting_time AS "cs:START_TIME"
                                                                  ,xl.list_start_date AS "cs:START_DATE"
                                                                  ,xs.sitting_note AS "cs:SITTING_NOTE"
                                                                  ,xsol.jp1 AS "cs:JP1"
                                                                  ,xsol.jp2 AS "cs:JP2"
                                                                  ,xsol.jp3 AS "cs:JP3"
                                                                  ,xsol.jp4 AS "cs:JP4"
                                                                  ,xs.list_id AS "cs:LST_ID"
                                                                  ,xs.ref_judge_id AS "cs:JUD_ID"
                                                                  ,xcr.court_room_name AS "cs:COURTROOM_NO"
                                                                  ,xcs.court_site_code AS "cs:SITE_CODE"
                                                                  )
                                            ),
                                    XMLELEMENT("cs:ScheduledHearing", XMLFOREST(xrht.list_sequence
                                                                  ,xrsc.code as "cs:TIME_MARKING" --need to check this.  xcol onlytable with ref to time_marking_id
                                                                  ,xl.list_start_date AS "cs:START_DATE"
                                                                  ,xs.sitting_time AS "cs:START_TIME"
                                                                  ,xsh.listing_note AS "cs:LIST_NOTES"
                                                                  ,xc.case_number AS "cs:CASE_NO"
                                                                  ,xc.case_type AS "cs:CASE_TYPE" 
                                                                  ,'CODE' AS "cs:CODE"
                                                                  ,xcr.court_room_name AS "cs:COURTROOM_NO"
                                                                  ,xcs.court_site_code AS "cs:SITE_CODE" 
                                                                  ,xs.ref_judge_id AS "cs:JUD_ID"
                                                                  ,xsol.judge_ref_id AS "cs:JUD_SEQ_NO"
                                                                  ,xrht.hearing_type_code AS "cs:HEARING_TYPE"
                                                                  ,xrht.category AS "cs:CATEGORY"
                                                                  )
                                            ),
                                       XMLELEMENT("cs:Defendant", XMLFOREST(xd.defendant_id AS "cs:SUB_ID"
                                                                  ,xd.first_name AS "cs:FORENAME1"
                                                                  ,xd.middle_name AS "cs:FORENAME2"
                                                                  ,xd.surname AS "cs:SURNAME"
                                                                  ,xd.initials AS "cs:INITIALS"
                                                                  ,xd.date_of_birth AS "cs:DOB"
                                                                  ,xd.first_name AS "cs:FORENAME1"
                                                                  ,'EX' AS "cs:EX"
                                                                  ,xad.address_1 AS "cs:ADDR1"
                                                                  ,xad.address_2 AS "cs:ADDR2"
                                                                  ,xad.address_3 AS "cs:ADDR3"
                                                                  ,xad.address_4 AS "cs:ADDR4"
                                                                  ,xad.postcode AS "cs:POSTCODE"
                                                                  ,xad.town AS "cs:TOWN"
                                                                  ,xad.county AS "cs:SUB_COUNTY"
                                                                  )
                                            ),
                                      XMLELEMENT("cs:DefendantRefLoc", XMLFOREST(xdr.defendant_id AS "cs:SUB_ID"
                                                                  ,xdr.reference_value AS "cs:PRISONER_NO"
                                                                  )
                                            ),
                                      XMLELEMENT("cs:DefendantRefID", XMLFOREST(xdr.defendant_id AS "cs:Defendant_id"
                                                                  ,'REMAND_PRISON' as "cs:REMAND_PRISON"
                                                                  )
                                            ),
                                      XMLELEMENT("cs:DefendantOnCase", XMLFOREST(xc.case_number AS "cs:CASE_NO"
                                                                  ,xc.case_type AS "cs:CASE_TYPE"
                                                                  ,xd.defendant_id AS "cs:SUB_ID"
                                                                  ,xdoc.pnc_id AS "cs:POLICE_NATIONAL_COMPUTER"
                                                                  ,xdoc.defendant_number AS "cs:DEF_NO"
                                                                  ,xdoc.is_juvenile AS "cs:JUVENILE_ID"
                                                                  )
                                            ),
                                    XMLELEMENT("cs:ScheduledHearingDefendant", XMLFOREST(xd.defendant_id AS "cs:SUB_ID"
                                                                  ,xc.case_number AS "cs:CASE_NO"
                                                                  ,xc.case_type AS "cs:CASE_TYPE"
                                                                  ,xcr.court_room_id AS "cs:COURTROOM_ID"
                                                                  ,xs.ref_judge_id AS "cs:JUD_ID"
                                                                  ,'CODE' AS "cs:CODE"
                                                                  ,xcs.court_site_code AS "cs:SITE_CODE"
                                                                  ,xsol.judge_ref_id AS "cs:JUD_SEQ_NO"
                                                                  ,xrht.hearing_type_code AS "cs:HEARING_TYPE"
                                                                  ,xrht.category AS "cs:CATEGORY"
                                                                  )||CHR(10) --have carriage return to start next line
                                      --      ) --xmlagg tag removed
                        )
             )
             .getclobval() as xml_data
     FROM xhb_case xc 
     ,    xhb_case_on_list xcol --new table
     ,    xhb_list xl
     ,    xhb_sitting xs
     ,    xhb_sitting_on_list xsol
     ,    xhb_court_site xcs
     ,    xhb_court xcrt
     ,    xhb_court_room xcr
     ,    xhb_ref_justice xrj
     ,    xhb_charge xcge
     ,    xhb_offence xo
     ,    xhb_hearing xh
     ,    xhb_scheduled_hearing xsh
     ,    xhb_ref_hearing_type xrht
     ,    xhb_hearing_list xhl
     ,    xhb_ref_system_code xrsc
     ,    xhb_defendant_on_case xdoc
     ,    xhb_defendant xd
     ,    xhb_defendant_reference xdr --used for prisoner fields
     ,    xhb_address xad
     WHERE xc.case_id = xcol.case_id
     AND   xcol.list_id = xl.list_id
     AND   xcol.sitting_on_list_id = xsol.sitting_on_list_id
     AND   xl.list_id = xsol.list_id (+)
     AND   xcol.court_site_id = xcs.court_site_id
     AND   xcs.court_id = xcrt.court_id
     AND   xcol.court_room_id = xcr.court_room_id
     AND   xcrt.court_id = xrj.court_id (+)
     AND   xc.case_id = xcge.case_id
     AND   xcge.charge_id = xo.charge_id
     AND   xc.case_id = xh.case_id (+) --does this need an outer join.  Can there be case with no hearings?
     AND   xh.hearing_id = xsh.hearing_id
     AND   xh.ref_hearing_type_id = xrht.ref_hearing_type_id
     AND   xcrt.court_id = xhl.court_id (+)
     AND   xcol.time_marking_id = xrsc.ref_system_code_id (+)
     AND   xdoc.case_id = xc.case_id
     AND   xdoc.defendant_id = xd.defendant_id
     AND   xd.address_id = xad.address_id
     AND   xdoc.defendant_id = xdr.defendant_id (+)--prisonerf
     AND   upper(reference_name) = 'PRISONER NUMBER'
     AND   (xcol.obs_ind <> 'Y' or xcol.obs_ind IS NULL)
     AND   (xsol.obs_ind <> 'Y' or xsol.obs_ind IS NULL)
     AND   (xcs.obs_ind <> 'Y' or xcs.obs_ind IS NULL)
     AND   (xcrt.obs_ind <> 'Y' or xcrt.obs_ind IS NULL)
     AND   (xcr.obs_ind <> 'Y' or xcr.obs_ind IS NULL)
     AND   (xrj.obs_ind <> 'Y' or xrj.obs_ind IS NULL)
     AND   (xcge.obs_ind <> 'Y' or xcge.obs_ind IS NULL)
     AND   (xo.obs_ind <> 'Y' or xo.obs_ind IS NULL)
     AND   (xrht.obs_ind <> 'Y' or xrht.obs_ind IS NULL)
     AND   (xrsc.obs_ind <> 'Y' or xrsc.obs_ind IS NULL)
     AND   (xdoc.obs_ind <> 'Y' or xdoc.obs_ind IS NULL)
     ;
    
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
     RETURN 'The length of the Firm List XML extract is: '||v_xml_length;
 
    WHEN OTHERS THEN

			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);
      
  END GetFlData;
  
  /** 
  * DESCRIPTION :  
  *               Procedure                   Purpose
  *               =========                   =======
  *               GetInternalDailyListData   'Daily List' extract. JIRA ticket CTX-1363. GetReportRequest will pass in 'DL_DATA'. 
  **/  
  FUNCTION GetInternalDailyListData(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN CLOB AS
   v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   CURSOR get_xml_c IS
                SELECT  XMLELEMENT("DLData"
                                    , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice' AS "xmlns"
                                    , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                                    , 'http://www.courtservice.gov.uk/schemas/courtservice Internal_DailyList.xsd' AS " xsi:schemaLocation")
                              ,XMLELEMENT("Court", xcrt.CREST_COURT_ID)
                              ,(SELECT XMLAgg(XMLELEMENT("CourtSite", xcrts.COURT_SITE_CODE || '|' || xcrts.COURT_SITE_NAME)) 
                                        FROM XHB_COURT_SITE xcrts
                                        WHERE xcrts.COURT_ID = xcrt.COURT_ID 
                                        AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y')
                                        AND xcrts.COURT_SITE_ID IN (SELECT xcol.COURT_SITE_ID
                                                                    FROM XHB_CASE_ON_LIST xcol
                                                                    WHERE xcol.LIST_ID = xl.list_id
                                                                    AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y'))) 
                              ,(SELECT XMLAgg(XMLELEMENT("CourtRoom", xcs.COURT_SITE_CODE || '|' || xcr.CREST_COURT_ROOM_NO)) 
                                        FROM XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs
                                        WHERE (xcr.obs_ind is null or xcr.obs_ind <> 'Y')
                                        AND xcr.COURT_ROOM_ID IN (SELECT xcol.COURT_ROOM_ID
                                                                  FROM XHB_CASE_ON_LIST xcol
                                                                  WHERE xcol.LIST_ID = xl.list_id
                                                                  AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                        AND xcr.COURT_SITE_ID = xcs.COURT_SITE_ID ))
                              ,(SELECT XMLAgg(XMLELEMENT("Case", xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' || xrc.CREST_CODE || '|' || xc.PROS_AGENCY_REFERENCE || '|' || xc.INDICTMENT_INFO_1 || '|' || xc.INDICTMENT_INFO_2 || '|' ||
                                                                 xc.INDICTMENT_INFO_3 || '|' || xc.INDICTMENT_INFO_4 || '|' || xc.INDICTMENT_INFO_5 || '|' || xc.INDICTMENT_INFO_6 || '|' ||
                                                                 (CASE WHEN xc.CASE_TYPE = 'B' THEN xc.BAIL_MAG_CODE ELSE xc.CASE_TITLE END) || '|' || xc.MAGISTRATES_CASE_REF || '|' || xc.CASE_TITLE || '|' || xc.CASE_SUB_TYPE))
                                            FROM XHB_CASE_ON_LIST xcol, XHB_CASE xc, XHB_REF_HEARING_TYPE xrht, XHB_REF_COURT xrc
                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                            AND xcol.CASE_ID = xc.CASE_ID
                                            AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                            AND xc.REF_COURT_ID = xrc.REF_COURT_ID) 
                              ,(SELECT XMLAgg(XMLELEMENT("Hearing",xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' || xrht.HEARING_TYPE_CODE || '|' || xrht.HEARING_TYPE_DESC || '|' || xrht.CATEGORY ))
                                            FROM XHB_CASE_ON_LIST xcol, XHB_CASE xc, XHB_REF_HEARING_TYPE xrht
                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                            AND xcol.CASE_ID = xc.CASE_ID
                                            AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID) 
                              ,XMLELEMENT("HearingList", 'D' || '|' || to_char(xl.LIST_START_DATE, 'YYYY-MM-DD HH:MI:SS') || '|' || to_char(xl.LIST_END_DATE,'YYYY-MM-DD HH:MI:SS') || '|' || xl.DRAFT_OR_FINAL 
                                            || '|' || xl.LIST_NUMBER || '|' || to_char(xl.PUBLISH_DATE,'YYYY-MM-DD HH:MI:SS') || '|' || xcrt.SHORT_NAME || '|' || xl.LIST_ID || '|' || substr(xcrt.COURT_TYPE,0 , 2) || '|' )
                              ,(SELECT XMLAgg(XMLELEMENT("Sitting",xsol.SITTING_NUMBER || '|' || 'T' || '|' || TO_CHAR(xsol.TIME_LISTED, 'hh:mi') || '|' || TO_CHAR(xsol.TIME_LISTED, 'YYYY-MM-DD HH:MI:SS') || '|'
                                                          ||xsol.LIST_NOTE_TEXT || '|' || xsol.JP1 || '|' || xsol.JP2 || '|' || xsol.JP3 || '|' || xsol.JP4 || '|' || 
                                                  xsol.LIST_ID || '|' || xrj.CREST_JUDGE_ID || '|' || xcr.CREST_COURT_ROOM_NO || '|' || xcs.COURT_SITE_CODE ))
                                              FROM XHB_SITTING_ON_LIST xsol, XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs, XHB_REF_JUDGE xrj
                                              WHERE xsol.LIST_ID = xl.LIST_ID
                                              AND (xsol.obs_ind is null or xsol.obs_ind <> 'Y')
                                              AND xsol.COURT_ROOM_ID = xcr.COURT_ROOM_ID (+)
                                              AND xsol.COURT_SITE_ID = xcs.COURT_SITE_ID (+)
                                              AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID (+))
                              --Floating case sitting
                              ,(SELECT XMLAgg(XMLELEMENT("Sitting",'99|F|||||||||' || xl.LIST_ID || '|l||' || xcs.COURT_SITE_CODE ))
                                              FROM XHB_COURT_SITE xcs
                                              WHERE xcs.COURT_SITE_ID IN  (SELECT xcol.COURT_SITE_ID FROM XHB_CASE_ON_LIST xcol
                                                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                                                            AND  xcol.FLOATER_CASE = 'Y'
                                                                            AND  xcol.COURT_SITE_ID = xcs.COURT_SITE_ID
                                                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')))
                              ,(SELECT XMLAgg(XMLELEMENT("ScheduledHearing",xcol.SEQ_NO || '|' || xrsc.DE_CODE || '|' ||  TO_CHAR(xcol.TIME_LISTED, 'YYYY-MM-DD HH:MI:SS') || '|' || TO_CHAR(xcol.TIME_LISTED, 'hh:mi') || '|'
                                                            || xcol.LIST_NOTE_TEXT || '|' || xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' || xrht.HEARING_TYPE_CODE || '|' || 
                                                                            xcr.CREST_COURT_ROOM_NO || '|' || xcs.COURT_SITE_CODE || '|' || xrj.CREST_JUDGE_ID || '|' || nvl(xsol.SITTING_NUMBER, 99) || '|' ||  xrht.HEARING_TYPE_DESC || '|' || xrht.CATEGORY))
                                              FROM XHB_CASE_ON_LIST xcol, XHB_CASE xc, XHB_REF_SYSTEM_CODE xrsc, XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs, XHB_SITTING_ON_LIST xsol, XHB_REF_HEARING_TYPE xrht, XHB_REF_JUDGE xrj
                                              WHERE xcol.LIST_ID = xl.LIST_ID
                                              AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                              AND xcol.CASE_ID = xc.CASE_ID
                                              AND xcol.TIME_MARKING_ID =  xrsc.REF_SYSTEM_CODE_ID (+)
                                              AND xcol.COURT_SITE_ID = xcs.COURT_SITE_ID (+)
                                              AND xcol.COURT_ROOM_ID = xcr.COURT_ROOM_ID (+)
                                              AND xcol.SITTING_ON_LIST_ID = xsol.SITTING_ON_LIST_ID(+)
                                              AND (xsol.obs_ind is null or xsol.obs_ind <> 'Y')
                                              AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                              AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID (+)) 
                              ,(SELECT XMLAgg(XMLELEMENT("Defendant",xd.CREST_DEFENDANT_ID || '|' || xd.FIRST_NAME || '||' || xd.MIDDLE_NAME || '|' || xd.SURNAME || '|' || xd.INITIALS || '|' || xd.DATE_OF_BIRTH || '|' || xd.GENDER || '|' || xd.PRISON_ID || '|' || 
                                                                    defendant_address.ADDRESS_1 || '|' || defendant_address.ADDRESS_2 || '|' || defendant_address.ADDRESS_3 || '|' || defendant_address.ADDRESS_4 || '|' || defendant_address.POSTCODE || '|' || defendant_address.TOWN || '|' || defendant_address.COUNTY))
                                              FROM XHB_CASE_ON_LIST xcol, XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_ADDRESS defendant_address
                                              WHERE xcol.LIST_ID = xl.LIST_ID
                                              AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                              AND xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                              AND (xdocol.obs_ind is null or xdocol.obs_ind <> 'Y')
                                              AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                              AND (xdoc.obs_ind is null or xdoc.obs_ind <> 'Y')
                                              AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
                                              AND xd.ADDRESS_ID = defendant_address.ADDRESS_ID(+))
                              ,(SELECT XMLAgg(XMLELEMENT("DefendantRefLoc",xd.CREST_DEFENDANT_ID || '|' || xdr.REFERENCE_VALUE))
                                              FROM XHB_CASE_ON_LIST xcol, XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_DEFENDANT_REFERENCE xdr
                                              WHERE xcol.LIST_ID = xl.LIST_ID
                                              AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                              AND xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                              AND (xdocol.obs_ind is null or xdocol.obs_ind <> 'Y')
                                              AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                              AND (xdoc.obs_ind is null or xdoc.obs_ind <> 'Y')
                                              AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
                                              AND xd.DEFENDANT_ID = xdr.DEFENDANT_ID
                                              AND xdr.REFERENCE_NAME ='Prisoner Number' )
                              ,(SELECT XMLAgg(XMLELEMENT("DefendantRefID", xd.CREST_DEFENDANT_ID || '|' || xdr.REFERENCE_VALUE))
                                              FROM XHB_CASE_ON_LIST xcol, XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_DEFENDANT_REFERENCE xdr
                                              WHERE xcol.LIST_ID = xl.LIST_ID
                                              AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                              AND xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                              AND (xdocol.obs_ind is null or xdocol.obs_ind <> 'Y')
                                              AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                              AND (xdoc.obs_ind is null or xdoc.obs_ind <> 'Y')
                                              AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
                                              AND xd.DEFENDANT_ID = xdr.DEFENDANT_ID
                                              AND xdr.REFERENCE_NAME ='Prisoner Location' )
                              ,(SELECT XMLAgg(XMLELEMENT("DefendantOnCase", xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' || xd.CREST_DEFENDANT_ID || '||' || xdoc.PNC_ID || '|' || xdoc.DEFENDANT_NUMBER || '|' || xdoc.IS_JUVENILE))
                                            FROM XHB_CASE_ON_LIST xcol, XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_DEFENDANT_ON_CASE xdoc, XHB_CASE xc, XHB_DEFENDANT xd
                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                            AND xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                            AND (xdocol.obs_ind is null or xdocol.obs_ind <> 'Y')
                                            AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                            AND (xdoc.obs_ind is null or xdoc.obs_ind <> 'Y')
                                            AND xdoc.CASE_ID = xc.CASE_ID
                                            AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID)
                              ,(SELECT XMLAgg(XMLELEMENT("ScheduledHearingDefendant", xd.CREST_DEFENDANT_ID || '|' || xc.CASE_TYPE || xc.CASE_NUMBER || '|' || xc.CASE_TYPE || '|' ||  xcr.CREST_COURT_ROOM_NO  || '|' || xrj.CREST_JUDGE_ID || '|' || '|' || xrht.HEARING_TYPE_CODE || '|' ||
                                                                                      xcs.COURT_SITE_CODE || '|' || xsol.SITTING_NUMBER || '|' || xrht.HEARING_TYPE_DESC || '|' || xrht.CATEGORY))
                                            FROM XHB_CASE_ON_LIST xcol, XHB_DEF_ON_CASE_ON_LIST xdocol, XHB_CASE xc, XHB_COURT_ROOM xcr, XHB_SITTING_ON_LIST xsol, XHB_COURT_SITE xcs, XHB_REF_HEARING_TYPE xrht, XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_REF_JUDGE xrj
                                            WHERE xcol.LIST_ID = xl.LIST_ID
                                            AND (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                            AND xcol.CASE_ON_LIST_ID = xdocol.CASE_ON_LIST_ID
                                            AND (xdocol.obs_ind is null or xdocol.obs_ind <> 'Y')
                                            AND xcol.CASE_ID = xc.CASE_ID
                                            AND xcol.COURT_ROOM_ID = xcr.COURT_ROOM_ID(+)
                                            AND xcol.SITTING_ON_LIST_ID = xsol.SITTING_ON_LIST_ID(+)
                                            AND (xsol.obs_ind is null or xsol.obs_ind <> 'Y')
                                            AND xcol.COURT_SITE_ID = xcs.COURT_SITE_ID(+)                                    
                                            AND xcol.HEARING_TYPE_ID = xrht.REF_HEARING_TYPE_ID
                                            AND xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                                            AND (xdoc.obs_ind is null or xdoc.obs_ind <> 'Y')
                                            AND xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
                                            AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID (+) )
                       ) .getclobval() as xml_data              
           FROM XHB_LIST xl, XHB_COURT xcrt,  XHB_REF_LISTING_DATA xrld 
           WHERE xl.LIST_ID = p_list_id
           AND xrld.REF_LISTING_DATA_ID = xl.LIST_TYPE_ID 
           AND xrld.REF_DATA_TYPE = 'LIST_TYPE'
           AND xcrt.COURT_ID = xl.COURT_ID
           AND (xcrt.obs_ind is null or xcrt.obs_ind <> 'Y');
             
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
     RAISE_APPLICATION_ERROR(-20001,'No Data found for List ID ' || p_list_id || ' - ' || SQLERRM||' at step '||v_step);
 
    WHEN OTHERS THEN

			RAISE_APPLICATION_ERROR(-20001,'When others exception ' || SQLERRM||' at step '||v_step);
          
  
  
  END GetInternalDailyListData;
  
  
  /** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GetDlData   'Daily List' extract. JIRA ticket CTX-1363. GetReportRequest will pass in 'DL_DATA'. 
  **/  
  FUNCTION GetDlData(p_list_id XHB_LIST.LIST_ID%TYPE) RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   CURSOR get_xml_c IS
            SELECT XMLAGG(XMLELEMENT("cs:DailyList"
                                  , XMLATTRIBUTES('http://www.courtservice.gov.uk/schemas/courtservice' AS "xmlns:cs"
                                  , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns:p1"
                                  , 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns"
                                  , 'http://www.govtalk.gov.uk/people/bs7666' AS "xmlns:p2"
                                  , 'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                                  , 'http://www.courtservice.gov.uk/schemas/courtservice DailyList.xsd' AS " xsi:schemaLocation")
                                  ,XMLELEMENT("cs:DocumentID", XMLFOREST('Daily List ' || DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' v' || xl.LIST_NUMBER || ' ' || xl.LIST_START_DATE AS "cs:DocumentName"
                                                                     ,sysdate AS "cs:UniqueID"
                                                                     ,'DL' AS "cs:DocumentType"
                                                                     ,to_xml_date_format(sysdate) AS "cs:TimeStamp"
                                                                    )
                                            )
                                  ,XMLELEMENT("cs:ListHeader", XMLFOREST('Criminal' AS "cs:ListCategory"
                                                                     ,xl.list_start_date AS "cs:StartDate"
                                                                     ,xl.list_end_date AS "cs:EndDate"
                                                                     ,DECODE(xl.DRAFT_OR_FINAL, 'D', 'DRAFT', 'F', 'FINAL') || ' ' || xl.LIST_NUMBER AS "cs:Version"
                                                                     --,NVL('xhl.print_reference','') AS "CRESTprintRef" --check this field is correct
                                                                     ,to_xml_date_format(NVL(xl.PUBLISH_DATE, sysdate)) AS "cs:PublishedTime"
                                                                     ,xl.LIST_ID As "cs:CRESTlistID" --check this field is correct
                                                                   )
                                            ), --ListHeader
                                  XMLELEMENT("cs:CrownCourt", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                                     ,XMLELEMENT("cs:CourtHouseCode", XMLATTRIBUTES(xcrt.SHORT_NAME AS "CourtHouseShortName"), xcrt.CREST_COURT_ID)
                                                                     ,XMLELEMENT("cs:CourtHouseName", xcrt.court_name)
                                                                     , XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(xadc.address_1 AS "p1:Line"
                                                                                                                ,xadc.address_2 AS "p1:Line"
                                                                                                                ,xadc.address_3 AS "p1:Line"
                                                                                                                ,xadc.postcode AS "p1:PostCode"
                                                                                                                )
                                                                                 ) 
                                                                    , XMLFOREST(xcrt.DX_REF as "cs:CourtHouseDX"
                                                                              , xcdp.contact_value AS "cs:CourtHouseTelephone"
                                                                              , xcdd.contact_value AS "cs:CourtHouseFax"
                                                                              , xcrt.COURT_PREFIX AS "cs:Description"))
                                             ), --CrownCourt
                                XMLELEMENT("cs:CourtLists",
                                (SELECT XMLAgg( XMLELEMENT("cs:CourtList",
                                  XMLELEMENT("cs:CourtHouse", XMLConcat (XMLELEMENT( "cs:CourtHouseType", 'Crown Court') 
                                                                        ,XMLELEMENT("cs:CourtHouseCode", XMLATTRIBUTES(xcrts.SHORT_NAME AS "CourtHouseShortName"), xcrts.CREST_COURT_ID)
                                                                        ,XMLELEMENT("cs:CourtHouseName", xcrts.court_site_name)
                                                                        ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(xadc.address_1 AS "p1:Line"
                                                                                                                ,xadc.address_2 AS "p1:Line"
                                                                                                                ,xadc.address_3 AS "p1:Line"
                                                                                                                ,xadc.postcode AS "p1:PostCode"
                                                                                                                )
                                                                                 ) 
                                                                        , XMLFOREST(xcrt.DX_REF as "cs:CourtHouseDX"
                                                                                  , courtsite_phone.contact_value AS "cs:CourtHouseTelephone"
                                                                                  , courtsite_fax.contact_value AS "cs:CourtHouseFax"
                                                                                  , xcrt.COURT_PREFIX AS "cs:Description"))
                                             )--CourtHouse
                                              ,XMLELEMENT("cs:Sittings",(get_sittings_in_courtsite(xcrts.COURT_SITE_ID, xl.LIST_ID)),
                                                                          (get_floating_cases(xl.LIST_ID, xcrts.COURT_SITE_ID)))--sittings
                                           )--CourtList
                                           ) FROM XHB_COURT_SITE xcrts, XHB_ADDRESS courtsite_address,xhb_contact_detail courtsite_phone, xhb_contact_detail courtsite_fax
                                              WHERE xcrts.COURT_ID = xcrt.COURT_ID 
                                              AND (xcrts.obs_ind is null or xcrts.obs_ind <> 'Y')
                                              AND xcrts.ADDRESS_ID = courtsite_address.ADDRESS_ID (+)
                                              AND   courtsite_address.ADDRESS_ID = courtsite_phone.ADDRESS_ID (+)
                                              AND   upper(courtsite_phone.contact_type(+)) = 'Phone' 
                                              AND   courtsite_address.ADDRESS_ID = courtsite_fax.ADDRESS_ID (+)
                                              AND   upper(courtsite_fax.contact_type(+)) = 'Fax'    )
                                          )--CourtLists  
                         )--DailyList
             ) --header tag
             .getclobval() as xml_data
     FROM xhb_list xl
     ,    xhb_court xcrt
     ,    xhb_address xadc --addressfor court
     ,    xhb_contact_detail xcdp --contact detail phone 
     ,    xhb_contact_detail xcdd --contact detail fax
     WHERE xl.LIST_ID = p_list_id
     AND xcrt.COURT_ID = xl.COURT_ID
     AND   (xcrt.obs_ind is null or xcrt.obs_ind <> 'Y')
     AND   xcrt.address_id = xadc.address_id (+)
     AND   xadc.address_id = xcdp.address_id (+)
     AND   upper(xcdp.contact_type(+)) = 'Phone' 
     AND   xadc.address_id = xcdd.address_id (+)
     AND   upper(xcdd.contact_type(+)) = 'Fax'    
     

     ;
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
      
  END GetDlData;
 
  
  /** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GetPlData   'Prison Daily List' extract. JIRA ticket CTX-1371. GetReportRequest will pass in 'PL_DATA'. 
  **/
  
  
  FUNCTION GetPlData RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   CURSOR get_xml_c IS
       SELECT XMLAGG(XMLELEMENT("DailyPrisonList",
                                  XMLELEMENT("ListHeader", XMLFOREST('ListCategory' AS "ListCategory"
                                                                     ,xl.list_start_date AS "StartDate"
                                                                     ,xl.list_end_date AS "EndDate"
                                                                     ,xl.version AS "Version"
                                                                     ,NVL(xhl.print_reference,'') AS "CRESTprintRef" --check this field is correct
                                                                     ,NVL(xhl.published_time,'') AS "PublishedTime"
                                                                     ,NVL(xhl.crest_list_id,'') As "CRESTlistID" --check this field is correct
                                                                   )
                                            ), --ListHeader
                                  XMLELEMENT("CrownCourt", XMLELEMENT("CourtHouseType", xcrt.court_type)
                                                                     ,XMLELEMENT("CourtHouseCode",XMLATTRIBUTES(xcrt.short_name AS "CourtHouseShortName"))
                                                                     --,xcrt.court_name AS "CourtHouseName")
                                                                     ,XMLELEMENT("CourtHouseName",xcrt.court_name)
                                                                     , XMLELEMENT("CourtHouseAddress", XMLFOREST(xadc.address_1 AS "Line"
                                                                                                                ,xadc.address_2 AS "Line"
                                                                                                                ,xadc.address_3 AS "Line"
                                                                                                                ,xadc.postcode AS "PostCode"
                                                                                                                )
                                                                                 )
                                                                    ,XMLELEMENT("CourtHouseDX",NVL(xcdd.contact_value,''))
                                                                    ,XMLELEMENT("CourtHouseTelephone",NVL(xcdp.contact_value,''))
                                             ), --CrownCourt
                                XMLELEMENT("CourtLists",
                                 XMLELEMENT("CourtList",
                                  XMLELEMENT("CourtHouse", XMLFOREST(xcrt.court_type AS "CourtHouseType"
                                                                    ,xcrt.court_code AS "CourtHouseCode"
                                                                    ,xcrt.court_name AS "CourtHouseName"
                                                                    )
                                             )--CourtHouse
                                 ,XMLELEMENT("Sittings"
                                   ,XMLELEMENT("Sitting", XMLFOREST(xcr.crest_court_room_no AS "CourtRoomNumber"
                                                                    ,xs.sitting_sequence_no AS "SittingSequenceNo"
                                                                    ,xs.sitting_time AS "SittingAt"
                                                                    ,'PRIORITY' as "SittingPriority"
                                                                    ),XMLELEMENT("Judiciary",
                                                                       XMLELEMENT("Judge",XMLFOREST('CitizenNameSurname' AS "CitizenNameSurname"
                                                                                                       ,'CitizenNameRequestedName' AS "CitizenNameRequestedName"
                                                                                                       ,xs.ref_judge_id AS "CRESTjudgeID"  --check that this is correct
                                                                                                        )
                                                                                  )--judge
                                                                                 )--judiciary
                                                                     ,XMLELEMENT("Hearings",
                                                                       XMLELEMENT("Hearing",XMLFOREST(xsh.sequence_no AS "HearingSequenceNumber")
                                                                                                     ,XMLELEMENT("HearingDetails",XMLATTRIBUTES(xrht.hearing_type_code AS "HearingType")
                                                                                                                                 ,XMLFOREST(xrht.hearing_type_desc AS "HearingDescription"
                                                                                                                                           ,NVL(xsh.DATE_OF_HEARING,'') AS "HearingDate" 
                                                                                                                                            )
                                                                                                                )--HearingDetails                            
                                                                                          ,XMLFOREST('CRESTHearingID' AS "CRESTHearingID")
                                                                                          ,XMLFOREST('TimeMarkingNote' AS "TimeMarkingNote")
                                                                                          ,XMLFOREST(xc.case_number AS "CaseNumber")
                                                                                          ,XMLELEMENT("Prosecution",XMLATTRIBUTES(GetRefProsAgency(xc.case_id,'AUTHORITY') AS "ProsecutingAuthority")
                                                                                                                   ,XMLFOREST('ProsecutingReference' AS "ProsecutingReference")
                                                                                                                             ,XMLELEMENT("ProsecutingOrganisation",XMLFOREST('OrganisationName' AS "OrganisationName")) 
                                                                                                    )--Prosecution 
                                                                                          ,XMLELEMENT("CommittingCourt",XMLFOREST(xcrt.court_type AS "CourtHouseType")
                                                                                                                       ,XMLELEMENT("CourtHouseCode",XMLATTRIBUTES(xcrt.short_name AS "CourtHouseShortName"))
                                                                                                                       ,XMLELEMENT("CourtHouseName",xcrt.court_name)
                                                                                               ,XMLELEMENT("CourtHouseAddress", XMLFOREST(xadc.address_1 AS "Line"
                                                                                                                                         ,xadc.address_2 AS "Line"
                                                                                                                                         ,xadc.address_3 AS "Line"
                                                                                                                                         ,xadc.postcode AS "PostCode")
                                                                                                          )--CourtHouseAddress
                                                                                              ,XMLELEMENT("CourtHouseTelephone",NVL(xcdp.contact_value,'')) 
                                                                                                      )--CommittingCourt
                                                                                          ,XMLELEMENT("ListNote",XMLFOREST(NVL(xsol.list_note_text,'') AS "ListNote"))
                                                                                           ,XMLELEMENT("Defendants",
                                                                                              XMLELEMENT("Defendant",
                                                                                               XMLELEMENT("PersonalDetails",
                                                                                                XMLELEMENT("Name",XMLFOREST(xd.first_name AS "CitizenNameForename"
                                                                                                                           ,xd.middle_name AS "CitizenNameForename"
                                                                                                                           ,xd.surname AS "CitizenNameSurname"
                                                                                                                            )
                                                                                                           )--Name
                                                                                                           , XMLELEMENT("IsMasked",xdoc.is_masked)
                                                                                                           , XMLELEMENT("DateOfBirth",XMLFOREST(xd.date_of_birth AS "BirthDate"
                                                                                                                                               ,'VerfiedBy' AS "VerfiedBy"
                                                                                                                                               )
                                                                                                                        )--DateOfBirth
                                                                                                           , XMLELEMENT("Sex",xd.gender)
                                                                                                           , XMLELEMENT("Address",XMLFOREST(xadd.address_1 AS "Line"
                                                                                                                                           ,xadd.address_2 AS "Line"
                                                                                                                                           ,xadd.address_3 AS "Line"
                                                                                                                                           ,xadd.postcode AS "PostCode"
                                                                                                                                            )
                                                                                                                       )--Address  
                                                                                                         )--PersonalDetails
                                                                                               ,XMLELEMENT("ASNs",XMLFOREST(xdoc.asn AS "ASN"))
                                                                                               ,XMLELEMENT("CRESTdefendantID",xd.crest_defendant_id)
                                                                                               ,XMLELEMENT("PNCnumber",xdoc.pnc_id)
                                                                                               ,XMLELEMENT("URN",'URN')
                                                                                               ,XMLELEMENT("MagistratesCourtRefNumber",xc.magistrates_case_ref)--check that this field is right
                                                                                               ,XMLELEMENT("CustodyStatus",xdoc.CURRENT_BC_STATUS)
                                                                                               ,XMLELEMENT("Charges",XMLATTRIBUTES(xhb_get_xml_reports.GetChargeCnt(xc.case_number) AS "NumberOfCharges")
                                                                                                                    ,XMLELEMENT("Charge" ,XMLATTRIBUTES('IndictmentCountNumber' AS "IndictmentCountNumber", 'CJSoffenceCode' AS "CJSoffenceCode")
                                                                                                                                         ,XMLFOREST(xdof.crn_id AS "CRN"
                                                                                                                                                   ,xcge.crest_charge_id AS "CRESTchargeID"
                                                                                                                                                  ,'tba' AS "OffenceStatement"
                                                                                                                                                  ,'tba' AS "OffenceParticulars")
                                                                                                                                )--Charge 
                                                                                                          ) --Charges
                                                                                                        )--Defendant
                                                                                                      )--Defendants
                                                                                  )--Hearing
                                                                                 )--Hearings
                                             )--Sitting
                                            )--sittings
                                           )--CourtList
                                          )--CourtLists  
                         )--DailyList
             ) --header tag
             .getclobval() as xml_data
     FROM xhb_case xc 
     ,    xhb_case_on_list xcol --new table
     ,    xhb_list xl
     ,    xhb_hearing_list xhl
     ,    xhb_hearing xh
     ,    xhb_ref_hearing_type xrht 
     ,    xhb_scheduled_hearing xsh
     ,    xhb_sitting xs
     ,    xhb_sitting_on_list xsol
     ,    xhb_court_site xcs
     ,    xhb_court xcrt
     ,    xhb_court_room xcr
     ,    xhb_address xadc --addressfor court
     ,    xhb_contact_detail xcdp --contact detail phone 
     ,    xhb_contact_detail xcdd --contact detail dx
     ,    xhb_defendant_on_case xdoc
     ,    xhb_defendant xd
     ,    xhb_address xadd --defendant address
     ,    xhb_charge xcge
     ,    xhb_offence xo
     ,    xhb_defendant_on_offence xdof
     WHERE xc.case_id = xcol.case_id
     AND   xcol.list_id = xl.list_id
     AND   xl.list_id = xhl.list_id (+)
     AND   xc.case_id = xh.case_id
     AND   xh.hearing_id = xsh.hearing_id
     AND   xh.ref_hearing_type_id = xrht.ref_hearing_type_id
     AND   xcol.sitting_on_list_id = xsol.sitting_on_list_id
     AND   xl.list_id = xsol.list_id (+)
     AND   xcol.court_site_id = xcs.court_site_id
     AND   xcs.court_id = xcrt.court_id
     AND   xcol.court_room_id = xcr.court_room_id
     AND   xcrt.address_id = xadc.address_id (+)
     AND   xadc.address_id = xcdp.address_id (+)
     AND   upper(xcdp.contact_type(+)) = 'PHONE' 
     AND   xadc.address_id = xcdd.address_id (+)
     AND   upper(xcdd.contact_type(+)) = 'SECURE EMAIL'
     AND   xc.case_id = xdoc.case_id
     AND   xdoc.defendant_id = xd.defendant_id
     AND   xd.address_id = xadd.address_id(+)
     AND   xc.case_id = xcge.case_id (+)
     AND   xcge.charge_id = xo.charge_id (+)
     AND   xo.offence_id = xdof.offence_id (+)
     AND   xcol.obs_ind <> 'Y'
     AND   xsol.obs_ind <> 'Y'
     AND   xcs.obs_ind <> 'Y'
     AND   xcrt.obs_ind <> 'Y'
     AND   xcr.obs_ind <> 'Y'
     AND   xcge.obs_ind <> 'Y'
     AND   xo.obs_ind <> 'Y'
     AND   xrht.obs_ind <> 'Y'
     AND   xdoc.obs_ind <> 'Y'
     AND   xdof.obs_ind <> 'Y'
     ;


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
      
  END GetPlData;
 
 
  
  /** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GetRlData   'Running List' extract. JIRA ticket CTX-1371. GetReportRequest will pass in 'RL_DATA'. 
  **/
  
  
  FUNCTION GetRlData RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   CURSOR get_xml_c IS 
    WITH header_fields AS 
                      (SELECT 'ListCategory' AS ListCategory
                       ,      xl.list_id
                       ,      xhl.start_date
                       ,      xhl.end_date
                       ,      xhl.crest_list_id
                       ,      xhl.version
                       ,      xhl.print_reference AS CRESTprintRef
                       ,      xhl.published_time
                       ,      xcrt.court_type AS CourtHouseType
                       ,      xcrt.court_code AS CourtHouseCode
                       ,      xcrt.court_name AS CourtHouseName
                       ,      xaddc.address_1
                       ,      xaddc.address_2
                       ,      xaddc.address_3
                       ,      xaddc.address_4
                       ,      xaddc.postcode
                       ,      xcdd.contact_value AS  CourtHouseDX
                       ,      xcdp.contact_value AS  CourtHouseTelephone
                       ,      xcdfx.contact_value AS  CourtHouseFax
                       FROM xhb_court xcrt
                       ,    xhb_court_site xcs
                       --,    xhb_court_room xcr
                       ,    xhb_address xaddc --court address
                       ,    xhb_list xl
                       ,    xhb_hearing_list xhl
                       ,    xhb_contact_detail xcdd --court contact detail dx
                       ,    xhb_contact_detail xcdp --court contact detail phone 
                       ,    xhb_contact_detail xcdfx --court contact detail fax
                      WHERE xcrt.court_id = xcs.court_id 
                     -- AND   xcs.court_site_id = xcr.court_site_id
                      AND   xcs.address_id = xaddc.address_id
                      AND   xcrt.court_id = xl.court_id
                      AND   xl.list_id = xhl.list_id(+)
                      AND   xaddc.address_id = xcdd.address_id (+)
                      AND   upper(xcdd.contact_type(+)) = 'SECURE EMAIL'
                      AND   xaddc.address_id = xcdp.address_id (+)
                      AND   upper(xcdp.contact_type(+)) = 'PHONE' 
                      AND   xaddc.address_id = xcdfx.address_id (+)
                      AND   upper(xcdfx.contact_type(+)) = 'FAX'
                      AND   NVL(xcs.obs_ind,'-') <> 'Y'
                      AND   NVL(xcrt.obs_ind,'-') <> 'Y'
                      --AND   NVL(xcr.obs_ind,'-') <> 'Y'
                      --AND   xcrt.court_id = :p_court_id
                      --AND   xcs.court_site_id = :p_court_site_id
                      --AND   xcr.court_room_id = :p_court_room_id
                      )
/********************************************************************************************************************************
*
*Start gathering the data for trial cases
*
**********************************************************************************************************************************/
, trial_cases AS (SELECT xc.case_number
                  ,      xcol.list_id
                  ,      xcrt.court_id
                  ,      xcs.court_site_id
                  ,      xcrt.court_type
                  ,      xcrt.court_code
                  ,      xcrt.court_name AS CourtHouseName
                  --,      xcr.court_room_id
                  ,      xaddc.address_1 CourtAddr1
                  ,      xaddc.address_2 CourtAddr2
                  ,      xaddc.address_3 CourtAddr3
                  ,      xaddc.address_4 CourtAddr4
                  ,      xaddc.postcode  CourtPostCode
                  ,      xcdd.contact_value AS  CourtHouseDX
                  ,      xcdp.contact_value AS  CourtHouseTelephone
                  ,      xcdfx.contact_value AS  CourtHouseFax
                  ,      xrht.hearing_type_code
                  ,      xcdf.listing_date
				          ,      xcdf.list_note_text
                  ,      xrht.hearing_type_desc AS HearingDescription
                  ,      xsh.date_of_hearing AS HearingDate
                  ,      xd.first_name AS CitizenNameForename
                  ,      xd.surname AS CitizenNameSurname
                  ,      xd.date_of_birth AS BirthDate
                  ,     'VerfiedBy' AS VerfiedBy
                  ,      xd.gender AS sex
                  ,      xdoc.is_masked
                  ,      xaddd.address_1 AS DefAddr1
                  ,      xaddd.address_2 AS DefAddr2
                  ,      xaddd.address_3 AS DefAddr3
                  ,      xaddd.postcode AS DefPostCode
                  ,      xdoc.asn AS ASN
                  ,      xd.crest_defendant_id AS CRESTdefendantID
                  ,      'URN' as URN
                  ,      NVL(xrsc1.de_code,'Not Available') AS PrisonLocation
                  ,      xdoc.current_bc_status AS CustodyStatus
                  ,      xrsf.solicitor_firm_name
                  ,      xadds.address_1 AS SolAddr1
                  ,      xadds.address_2 AS SolAddr2
                  ,      xadds.address_3 AS SolAddr3
                  ,      xadds.postcode AS SolPostCode
                  ,      xcdsp.contact_value SolPhone
                  ,      xcdsf.contact_value SolFax
                  ,      xdocrsf.rep_st_date SolRepStartDate
                  ,      'AdditonalNotes' as AdditonalNotes
                  ,      rpa.prosecutor_name_3 AS ProsOrgaisation
                  ,      xaddp.address_1 AS ProsAddr1
                  ,      xaddp.address_2 AS ProsAddr2
                  ,      xaddp.address_3 AS ProsAddr3
                  ,      xaddp.address_4 AS ProsAddr4
                  ,      xaddp.postcode AS  ProsPostCode 
                  ,      xcdpp.contact_value ProsPhone
                  ,      xcdpf.contact_value ProsFax
                  ,      xc.case_class
                  ,      xmcrt.court_type MagCourtType
                  ,      xmcrt.short_name AS MagCourtHouseShortName
                  ,      xmcrt.court_name MagCourtName
                  ,      xaddm.address_1 MagAddr1
                  ,      xaddm.address_2 MagAddr2
                  ,      xaddm.address_3 MagAddr3
                  ,      xaddm.address_4 MagAddr4
                  ,      xaddm.postcode MagPostcode
                  ,      xcdmp.contact_value MagPhone
                  ,      xcdmf.contact_value MagFax
                  ,      xcdmd.contact_value MagDx
                  FROM xhb_case_on_list xcol
                  ,    xhb_case xc
                  ,    xhb_list xl
                  ,    xhb_case_listing_entry xcle
                  ,    xhb_case_diary_fixture xcdf
                  ,    xhb_court xcrt
                  ,    xhb_court_site xcs
                  ,    xhb_hearing_list xhl
                  ,    xhb_hearing xh
                  ,    xhb_scheduled_hearing xsh
                  ,    xhb_ref_hearing_type xrht
                  ,    xhb_address xaddc --court address
                  ,    xhb_contact_detail xcdd --court contact detail dx
                  ,    xhb_contact_detail xcdp --court contact detail phone 
                  ,    xhb_contact_detail xcdfx --court contact detail fax
                  ,    xhb_defendant_on_case xdoc
                  ,    xhb_defendant xd
                  ,    xhb_address xaddd
                  ,    xhb_ref_system_code xrsc1
                  ,    xhb_def_on_case_ref_sol_firm xdocrsf
                  ,    xhb_ref_solicitor_firm xrsf
                  ,    xhb_address xadds 
                  ,    xhb_contact_detail xcdsf --contact detail fax
                  ,    xhb_contact_detail xcdsp --contact detail phone
                  ,    xhb_case_prosecutor_agency cpa
                  ,    xhb_ref_prosecutor_agency rpa
                  ,    xhb_address xaddp --Prosecution Address
                  ,    xhb_contact_detail xcdpf --Prosecution contact detail fax
                  ,    xhb_contact_detail xcdpp --Prosecution contact detail phone
                  ,    xhb_court xmcrt --Magistrates Court (Original Cout)
                  ,    xhb_address xaddm --Magistrates Court address (Original Cout)
                  ,    xhb_contact_detail xcdmf --magistrate contact detail fax
                  ,    xhb_contact_detail xcdmp --magistrate contact detail phone
                  ,    xhb_contact_detail xcdmd --magistrate contact detail dx
                  WHERE xc.case_type = 'T'
                  AND   xcol.case_id = xc.case_id
                  AND   xcol.list_id = xl.list_id
                  AND   xc.case_id = xcle.case_id (+)
                  AND   xcle.case_listing_entry_id = xcdf.case_listing_entry_id (+)
                  AND   xcrt.court_id = xl.court_id
                  AND   xcrt.court_id = xcs.court_id 
                  AND   xl.list_id = xhl.list_id(+)
                  AND   xh.case_id = xc.case_id
                  AND   xh.hearing_id = xsh.hearing_id (+)
                  AND   xh.ref_hearing_type_id = xrht.ref_hearing_type_id
                  AND   xcs.address_id = xaddc.address_id
                  AND   xaddc.address_id = xcdd.address_id (+)
                  AND   upper(xcdd.contact_type(+)) = 'SECURE EMAIL'
                  AND   xaddc.address_id = xcdp.address_id (+)
                  AND   upper(xcdp.contact_type(+)) = 'PHONE' 
                  AND   xaddc.address_id = xcdfx.address_id (+)
                  AND   upper(xcdfx.contact_type(+)) = 'FAX'
                  AND   xc.case_id = xdoc.case_id
                  AND   xdoc.defendant_id = xd.defendant_id
                  AND   xd.address_id = xaddd.address_id (+)
                  AND   xd.prison_id = xrsc1.code (+)
                  AND   xrsc1.code_type (+)= 'PRISON_ID'
                  AND   xdoc.defendant_on_case_id = xdocrsf.defendant_on_case_id (+)
                  AND   xdocrsf.ref_solicitor_firm_id = xrsf.ref_solicitor_firm_id (+)
                  AND   NVL(xrsf.address_id,0) = xadds.address_id (+)
                  AND   xadds.address_id = xcdsf.address_id (+)
                  AND   upper(xcdsf.contact_type(+)) = 'FAX' --solicitor fax
                  AND   xadds.address_id = xcdsp.address_id (+)
                  AND   upper(xcdsp.contact_type(+)) = 'PHONE' --solicitor phone
                  AND   xc.case_id = cpa.case_id (+)
                  AND   cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id(+)
                  AND   rpa.address_id = xaddp.address_id (+)
                  AND   xaddp.address_id = xcdpf.address_id (+)
                  AND   upper(xcdpf.contact_type(+)) = 'FAX' --Prosecution fax
                  AND   xaddp.address_id = xcdpp.address_id (+)
                  AND   upper(xcdpp.contact_type(+)) = 'PHONE' --Proseutcion phone
                  AND   xc.ref_court_id = xmcrt.court_id (+)
                  AND   xmcrt.address_id = xaddm.address_id (+)
                  AND   xaddm.address_id = xcdmd.address_id (+)
                  AND   upper(xcdmd.contact_type(+)) = 'SECURE EMAIL'
                  AND   xaddm.address_id = xcdmp.address_id (+)
                  AND   upper(xcdmp.contact_type(+)) = 'PHONE' 
                  AND   xaddm.address_id = xcdmf.address_id (+)
                  AND   upper(xcdmf.contact_type(+)) = 'FAX'
                  AND   (xcs.obs_ind <> 'Y' OR xcs.obs_ind IS NULL)
                  AND   (xcrt.obs_ind <> 'Y' OR xcrt.obs_ind IS NULL)
                  AND   (xrht.obs_ind <> 'Y' OR xrht.obs_ind IS NULL)
                  AND   (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
                  )
/********************************************************************************************************************************
*
*Start gathering the data for committal cases
*
**********************************************************************************************************************************/
, comm_cases  AS (SELECT xc.case_number
                  ,      xcol.list_id
                  ,      xcrt.court_id
                  ,      xcs.court_site_id
                  ,      xcrt.court_type
                  ,      xcrt.court_code
                  ,      xcrt.court_name AS CourtHouseName
                  --,      xcr.court_room_id
                  ,      xaddc.address_1 CourtAddr1
                  ,      xaddc.address_2 CourtAddr2
                  ,      xaddc.address_3 CourtAddr3
                  ,      xaddc.address_4 CourtAddr4
                  ,      xaddc.postcode  CourtPostCode
                  ,      xcdd.contact_value AS  CourtHouseDX
                  ,      xcdp.contact_value AS  CourtHouseTelephone
                  ,      xcdfx.contact_value AS  CourtHouseFax
                  ,      xrht.hearing_type_code
                  ,      xcdf.listing_date
				          ,      xcdf.list_note_text
                  ,      xrht.hearing_type_desc AS HearingDescription
                  ,      xsh.date_of_hearing AS HearingDate
                  ,      xd.first_name AS CitizenNameForename
                  ,      xd.middle_name AS CitizenNameForename2
                  ,      xd.surname AS CitizenNameSurname
                  ,      xd.date_of_birth AS BirthDate
                  ,     'VerfiedBy' AS VerfiedBy
                  ,      xd.gender AS sex
                  ,      xdoc.is_masked
                  ,      xaddd.address_1 AS DefAddr1
                  ,      xaddd.address_2 AS DefAddr2
                  ,      xaddd.address_3 AS DefAddr3
                  ,      xaddd.postcode AS DefPostCode
                  ,      xdoc.asn AS ASN
                  ,      xd.crest_defendant_id AS CRESTdefendantID
                  ,      'URN' as URN
                  ,      NVL(xrsc1.de_code,'Not Available') AS PrisonLocation
                  ,      xdoc.current_bc_status AS CustodyStatus
                  ,      xrsf.solicitor_firm_name
                  ,      xadds.address_1 AS SolAddr1
                  ,      xadds.address_2 AS SolAddr2
                  ,      xadds.address_3 AS SolAddr3
                  ,      xadds.postcode AS SolPostCode
                  ,      xcdsp.contact_value SolPhone
                  ,      xcdsf.contact_value SolFax
                  ,      xdocrsf.rep_st_date SolRepStartDate
                  ,      'AdditonalNotes' as AdditonalNotes
                  ,      rpa.prosecutor_name_3 AS ProsOrgaisation
                  ,      xaddp.address_1 AS ProsAddr1
                  ,      xaddp.address_2 AS ProsAddr2
                  ,      xaddp.address_3 AS ProsAddr3
                  ,      xaddp.address_4 AS ProsAddr4
                  ,      xaddp.postcode AS  ProsPostCode
                  ,      xcdpp.contact_value ProsPhone
                  ,      xcdpf.contact_value ProsFax
                  ,      xc.case_class
                  ,      xmcrt.court_type MagCourtType
                  ,      xmcrt.short_name AS MagCourtHouseShortName
                  ,      xmcrt.court_name MagCourtName
                  ,      xaddm.address_1 MagAddr1
                  ,      xaddm.address_2 MagAddr2
                  ,      xaddm.address_3 MagAddr3
                  ,      xaddm.address_4 MagAddr4
                  ,      xaddm.postcode MagPostcode
                  ,      xcdmp.contact_value MagPhone
                  ,      xcdmf.contact_value MagFax
                  ,      xcdmd.contact_value MagDx
                  ,      'IndictmentCountNumber' as IndictmentCountNumber
                  ,      'CJSoffenceCode' as CJSoffenceCode
                  ,      xdof.crn_id AS CRN
                  ,      xcge.crest_charge_id AS CRESTchargeID
                  ,      'OffenceStatement' AS OffenceStatement
                  ,      'OffenceParticulars' AS OffenceParticulars
                  FROM xhb_case_on_list xcol
                  ,    xhb_case xc
                  ,    xhb_list xl
                  ,    xhb_case_listing_entry xcle
                  ,    xhb_case_diary_fixture xcdf
                  ,    xhb_court xcrt
                  ,    xhb_court_site xcs
                  ,    xhb_hearing_list xhl
                  ,    xhb_hearing xh
                  ,    xhb_scheduled_hearing xsh
                  ,    xhb_ref_hearing_type xrht
                  ,    xhb_address xaddc --court address
                  ,    xhb_contact_detail xcdd --court contact detail dx
                  ,    xhb_contact_detail xcdp --court contact detail phone 
                  ,    xhb_contact_detail xcdfx --court contact detail fax
                  ,    xhb_defendant_on_case xdoc
                  ,    xhb_defendant xd
                  ,    xhb_address xaddd
                  ,    xhb_ref_system_code xrsc1
                  ,    xhb_def_on_case_ref_sol_firm xdocrsf
                  ,    xhb_ref_solicitor_firm xrsf
                  ,    xhb_address xadds 
                  ,    xhb_contact_detail xcdsf --contact detail fax
                  ,    xhb_contact_detail xcdsp --contact detail phone
                  ,    xhb_case_prosecutor_agency cpa
                  ,    xhb_ref_prosecutor_agency rpa
                  ,    xhb_address xaddp --Prosecution Address
                  ,    xhb_contact_detail xcdpf --Prosecution contact detail fax
                  ,    xhb_contact_detail xcdpp --Prosecution contact detail phone
                  ,    xhb_court xmcrt --Magistrates Court (Original Cout)
                  ,    xhb_address xaddm --Magistrates Court address (Original Cout)
                  ,    xhb_contact_detail xcdmf --magistrate contact detail fax
                  ,    xhb_contact_detail xcdmp --magistrate contact detail phone
                  ,    xhb_contact_detail xcdmd --magistrate contact detail dx
                  ,    xhb_charge xcge
                  ,    xhb_offence xo
                  ,    xhb_defendant_on_offence xdof
                  WHERE xc.case_type = 'C'
                  AND   xcol.case_id = xc.case_id
                  AND   xcol.list_id = xl.list_id
                  AND   xc.case_id = xcle.case_id (+)
                  AND   xcle.case_listing_entry_id = xcdf.case_listing_entry_id (+)
                  AND   xcrt.court_id = xl.court_id
                  AND   xcrt.court_id = xcs.court_id 
                  AND   xl.list_id = xhl.list_id(+)
                  AND   xh.case_id = xc.case_id
                  AND   xh.hearing_id = xsh.hearing_id (+)
                  AND   xh.ref_hearing_type_id = xrht.ref_hearing_type_id
                  AND   xcs.address_id = xaddc.address_id
                  AND   xaddc.address_id = xcdd.address_id (+)
                  AND   upper(xcdd.contact_type(+)) = 'SECURE EMAIL'
                  AND   xaddc.address_id = xcdp.address_id (+)
                  AND   upper(xcdp.contact_type(+)) = 'PHONE' 
                  AND   xaddc.address_id = xcdfx.address_id (+)
                  AND   upper(xcdfx.contact_type(+)) = 'FAX'
                  AND   xc.case_id = xdoc.case_id
                  AND   xdoc.defendant_id = xd.defendant_id
                  AND   xd.address_id = xaddd.address_id (+)
                  AND   xd.prison_id = xrsc1.code (+)
                  AND   xrsc1.code_type (+)= 'PRISON_ID'
                  AND   xdoc.defendant_on_case_id = xdocrsf.defendant_on_case_id (+)
                  AND   xdocrsf.ref_solicitor_firm_id = xrsf.ref_solicitor_firm_id (+)
                  AND   NVL(xrsf.address_id,0) = xadds.address_id (+)
                  AND   xadds.address_id = xcdsf.address_id (+)
                  AND   upper(xcdsf.contact_type(+)) = 'FAX' --solicitor fax
                  AND   xadds.address_id = xcdsp.address_id (+)
                  AND   upper(xcdsp.contact_type(+)) = 'PHONE' --solicitor phone
                  AND   xc.case_id = cpa.case_id (+)
                  AND   cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id(+)
                  AND   rpa.address_id = xaddp.address_id (+)
                  AND   xaddp.address_id = xcdpf.address_id (+)
                  AND   upper(xcdpf.contact_type(+)) = 'FAX' --Prosecution fax
                  AND   xaddp.address_id = xcdpp.address_id (+)
                  AND   upper(xcdpp.contact_type(+)) = 'PHONE' --Proseutcion phone
                  AND   xc.ref_court_id = xmcrt.court_id (+)
                  AND   xmcrt.address_id = xaddm.address_id (+)
                  AND   xaddm.address_id = xcdmd.address_id (+)
                  AND   upper(xcdmd.contact_type(+)) = 'SECURE EMAIL'
                  AND   xaddm.address_id = xcdmp.address_id (+)
                  AND   upper(xcdmp.contact_type(+)) = 'PHONE' 
                  AND   xaddm.address_id = xcdmf.address_id (+)
                  AND   upper(xcdmf.contact_type(+)) = 'FAX'
                  AND   xc.case_id = xcge.case_id (+)
                  AND   xcge.charge_id = xo.charge_id (+)
                  AND   xo.offence_id = xdof.offence_id (+)
                  AND   (xcs.obs_ind <> 'Y' OR xcs.obs_ind IS NULL)
                  AND   (xcrt.obs_ind <> 'Y' OR xcrt.obs_ind IS NULL)
                  AND   (xcge.obs_ind <> 'Y' OR xcge.obs_ind IS NULL)
                  AND   (xo.obs_ind <> 'Y' OR xo.obs_ind IS NULL)
                  AND   (xrht.obs_ind <> 'Y' OR xrht.obs_ind IS NULL)
                  AND   (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
                  AND   (xdof.obs_ind <> 'Y' OR xdof.obs_ind IS NULL)
                  )
/********************************************************************************************************************************
*
*Start gathering the data for appeal cases
*
**********************************************************************************************************************************/
,appeal_cases  AS (SELECT xc.case_number
                  ,      xcol.list_id
                  ,      xcrt.court_id
                  ,      xcs.court_site_id
                  ,      xcrt.court_type
                  ,      xcrt.court_code
                  ,      xcrt.court_name AS CourtHouseName
                  --,      xcr.court_room_id
                  ,      xaddc.address_1 CourtAddr1
                  ,      xaddc.address_2 CourtAddr2
                  ,      xaddc.address_3 CourtAddr3
                  ,      xaddc.address_4 CourtAddr4
                  ,      xaddc.postcode  CourtPostCode
                  ,      xcdd.contact_value AS  CourtHouseDX
                  ,      xcdp.contact_value AS  CourtHouseTelephone
                  ,      xcdfx.contact_value AS  CourtHouseFax
                  ,      xrht.hearing_type_code
                  ,      xcdf.listing_date
				          ,      xcdf.list_note_text
                  ,      xrht.hearing_type_desc AS HearingDescription
                  ,      xsh.date_of_hearing AS HearingDate
                  ,      xd.first_name AS CitizenNameForename
                  ,      xd.middle_name AS CitizenNameForename2
                  ,      xd.surname AS CitizenNameSurname
                  ,      xd.date_of_birth AS BirthDate
                  ,     'VerfiedBy' AS VerfiedBy
                  ,      xd.gender AS sex
                  ,      xdoc.is_masked
                  ,      xaddd.address_1 AS DefAddr1
                  ,      xaddd.address_2 AS DefAddr2
                  ,      xaddd.address_3 AS DefAddr3
                  ,      xaddd.postcode AS DefPostCode
                  ,      xdoc.asn AS ASN
                  ,      xd.crest_defendant_id AS CRESTdefendantID
                  ,      'URN' as URN
                  ,      NVL(xrsc1.de_code,'Not Available') AS PrisonLocation
                  ,      xdoc.current_bc_status AS CustodyStatus
                  ,      xrsf.solicitor_firm_name
                  ,      xadds.address_1 AS 
                  ,      xadds.address_2 AS SolAddr2
                  ,      xadds.address_3 AS SolAddr3
                  ,      xadds.postcode AS SolPostCode
                  ,      xcdsp.contact_value SolPhone
                  ,      xcdsf.contact_value SolFax
                  ,      xdocrsf.rep_st_date SolRepStartDate
                  ,      'AdditonalNotes' as AdditonalNotes
                  ,      rpa.prosecutor_name_3 AS ProsOrgaisation
                  ,      xaddp.address_1 AS ProsAddr1
                  ,      xaddp.address_2 AS ProsAddr2
                  ,      xaddp.address_3 AS ProsAddr3
                  ,      xaddp.address_4 AS ProsAddr4
                  ,      xaddp.postcode AS  ProsPostCode
                  ,      xcdpp.contact_value ProsPhone
                  ,      xcdpf.contact_value ProsFax
                  ,      xc.case_class
                  ,      xmcrt.court_type MagCourtType
                  ,      xmcrt.short_name AS MagCourtHouseShortName
                  ,      xmcrt.court_name MagCourtName
                  ,      xaddm.address_1 MagAddr1
                  ,      xaddm.address_2 MagAddr2
                  ,      xaddm.address_3 MagAddr3
                  ,      xaddm.address_4 MagAddr4
                  ,      xaddm.postcode MagPostcode
                  ,      xcdmp.contact_value MagPhone
                  ,      xcdmf.contact_value MagFax
                  ,      xcdmd.contact_value MagDx
                  ,      xc.magistrates_case_ref AS MagistratesCourtRefNumber
                  ,      'CJSoffenceCode' as CJSoffenceCode
                  ,      'IndictmentCountNumber' as IndictmentCountNumber
                  ,      'OffenceStatement' AS OffenceStatement
                  ,      'OffenceParticulars' AS OffenceParticulars
                  FROM xhb_case_on_list xcol
                  ,    xhb_case xc
                  ,    xhb_list xl
                  ,    xhb_case_listing_entry xcle
                  ,    xhb_case_diary_fixture xcdf
                  ,    xhb_court xcrt
                  ,    xhb_court_site xcs
                  ,    xhb_hearing_list xhl
                  ,    xhb_hearing xh
                  ,    xhb_scheduled_hearing xsh
                  ,    xhb_ref_hearing_type xrht
                  ,    xhb_address xaddc --court address
                  ,    xhb_contact_detail xcdd --court contact detail dx
                  ,    xhb_contact_detail xcdp --court contact detail phone 
                  ,    xhb_contact_detail xcdfx --court contact detail fax
                  ,    xhb_defendant_on_case xdoc
                  ,    xhb_defendant xd
                  ,    xhb_address xaddd
                  ,    xhb_ref_system_code xrsc1
                  ,    xhb_def_on_case_ref_sol_firm xdocrsf
                  ,    xhb_ref_solicitor_firm xrsf
                  ,    xhb_address xadds 
                  ,    xhb_contact_detail xcdsf --contact detail fax
                  ,    xhb_contact_detail xcdsp --contact detail phone
                  ,    xhb_case_prosecutor_agency cpa
                  ,    xhb_ref_prosecutor_agency rpa
                  ,    xhb_address xaddp --Prosecution Address
                  ,    xhb_contact_detail xcdpf --Prosecution contact detail fax
                  ,    xhb_contact_detail xcdpp --Prosecution contact detail phone
                  ,    xhb_court xmcrt --Magistrates Court (Original Cout)
                  ,    xhb_address xaddm --Magistrates Court address (Original Cout)
                  ,    xhb_contact_detail xcdmf --magistrate contact detail fax
                  ,    xhb_contact_detail xcdmp --magistrate contact detail phone
                  ,    xhb_contact_detail xcdmd --magistrate contact detail dx
                  ,    xhb_charge xcge
                  ,    xhb_offence xo
                  ,    xhb_defendant_on_offence xdof
                  WHERE xc.case_type = 'A'
                  AND   xcol.case_id = xc.case_id
                  AND   xcol.list_id = xl.list_id
                  AND   xc.case_id = xcle.case_id (+)
                  AND   xcle.case_listing_entry_id = xcdf.case_listing_entry_id (+)
                  AND   xcrt.court_id = xl.court_id
                  AND   xcrt.court_id = xcs.court_id 
                  AND   xl.list_id = xhl.list_id(+)
                  AND   xh.case_id = xc.case_id
                  AND   xh.hearing_id = xsh.hearing_id (+)
                  AND   xh.ref_hearing_type_id = xrht.ref_hearing_type_id
                  AND   xcs.address_id = xaddc.address_id
                  AND   xaddc.address_id = xcdd.address_id (+)
                  AND   upper(xcdd.contact_type(+)) = 'SECURE EMAIL'
                  AND   xaddc.address_id = xcdp.address_id (+)
                  AND   upper(xcdp.contact_type(+)) = 'PHONE' 
                  AND   xaddc.address_id = xcdfx.address_id (+)
                  AND   upper(xcdfx.contact_type(+)) = 'FAX'
                  AND   xc.case_id = xdoc.case_id
                  AND   xdoc.defendant_id = xd.defendant_id
                  AND   xd.address_id = xaddd.address_id (+)
                  AND   xd.prison_id = xrsc1.code (+)
                  AND   xrsc1.code_type (+)= 'PRISON_ID'
                  AND   xdoc.defendant_on_case_id = xdocrsf.defendant_on_case_id (+)
                  AND   xdocrsf.ref_solicitor_firm_id = xrsf.ref_solicitor_firm_id (+)
                  AND   NVL(xrsf.address_id,0) = xadds.address_id (+)
                  AND   xadds.address_id = xcdsf.address_id (+)
                  AND   upper(xcdsf.contact_type(+)) = 'FAX' --solicitor fax
                  AND   xadds.address_id = xcdsp.address_id (+)
                  AND   upper(xcdsp.contact_type(+)) = 'PHONE' --solicitor phone
                  AND   xc.case_id = cpa.case_id (+)
                  AND   cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id(+)
                  AND   rpa.address_id = xaddp.address_id (+)
                  AND   xaddp.address_id = xcdpf.address_id (+)
                  AND   upper(xcdpf.contact_type(+)) = 'FAX' --Prosecution fax
                  AND   xaddp.address_id = xcdpp.address_id (+)
                  AND   upper(xcdpp.contact_type(+)) = 'PHONE' --Proseutcion phone
                  AND   xc.ref_court_id = xmcrt.court_id (+)
                  AND   xmcrt.address_id = xaddm.address_id (+)
                  AND   xaddm.address_id = xcdmd.address_id (+)
                  AND   upper(xcdmd.contact_type(+)) = 'SECURE EMAIL'
                  AND   xaddm.address_id = xcdmp.address_id (+)
                  AND   upper(xcdmp.contact_type(+)) = 'PHONE' 
                  AND   xaddm.address_id = xcdmf.address_id (+)
                  AND   upper(xcdmf.contact_type(+)) = 'FAX'
                  AND   xc.case_id = xcge.case_id (+)
                  AND   xcge.charge_id = xo.charge_id (+)
                  AND   xo.offence_id = xdof.offence_id (+)
                  AND   (xcs.obs_ind <> 'Y' OR xcs.obs_ind IS NULL)
                  AND   (xcrt.obs_ind <> 'Y' OR xcrt.obs_ind IS NULL)
                  AND   (xcge.obs_ind <> 'Y' OR xcge.obs_ind IS NULL)
                  AND   (xo.obs_ind <> 'Y' OR xo.obs_ind IS NULL)
                  AND   (xrht.obs_ind <> 'Y' OR xrht.obs_ind IS NULL)
                  AND   (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
                  AND   (xdof.obs_ind <> 'Y' OR xdof.obs_ind IS NULL)
                  )
/***********************************************END OF APPEAL CASES*********************************************/                  
SELECT XMLELEMENT("cs:RunningList",XMLAttributes('http://www.courtservice.gov.uk/schemas/courtservice' AS "xmlns:cs"
                                                 ,'http://www.w3.org/2001/XMLSchema-instance' AS "xmlns:xsi"
                                                 ,'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails' AS "xmlns:apd"
                                                 --,'http://www.courtservice.gov.uk/schemas/courtservice RunningList-v5-9.xsd' AS "xsi:schemaLocation"
                                                 ),
                                  XMLELEMENT("cs:DocumentID",XMLFOREST('DocumentName' AS "cs:DocumentName"
                                                                     ,'UniqueID' AS "cs:UniqueID"
                                                                     ,'DocumentType' AS "cs:DocumentType"
                                                                     ,'DocumentInformation' AS "cs:DocumentInformation"
                                                                   )
                                            ),--DocumentId
                                  XMLELEMENT("cs:ListHeader", XMLFOREST('ListCategory' AS "cs:ListCategory"
                                                                     ,hf.start_date AS "cs:StartDate"
                                                                     ,hf.end_date AS "cs:EndDate"
                                                                     ,hf.version AS "cs:Version"
                                                                     ,NVL(hf.CRESTprintRef,'') AS "cs:CRESTprintRef" --check this field is correct
                                                                     ,NVL(hf.published_time,'') AS "cs:PublishedTime"
                                                                     ,NVL(hf.crest_list_id,'') As "cs:CRESTlistID" --check this field is correct
                                                                   )
                                            ), --ListHeader
                                  XMLELEMENT("cs:CrownCourt", XMLELEMENT("cs:CourtHouseType", hf.CourtHouseType)
                                                          ,XMLELEMENT("cs:CourtHouseCode",XMLATTRIBUTES(hf.CourtHouseCode AS "CourtHouseShortName"))
                                                                     --,xcrt.court_name AS "CourtHouseName")
                                                          ,XMLELEMENT("cs:CourtHouseName",hf.CourtHouseName)
                                                          ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(hf.address_1 AS "apd:Line"
                                                                                                       ,hf.address_2 AS "apd:Line"
                                                                                                       ,hf.address_3 AS "apd:Line"
                                                                                                       ,hf.postcode AS "apd:PostCode"
                                                                                                       )
                                                                    )
                                                          ,XMLELEMENT("cs:CourtHouseDX",NVL(hf.CourtHouseDX,''))
                                                          ,XMLELEMENT("cs:CourtHouseTelephone",NVL(hf.CourtHouseTelephone,''))
                                                          ,XMLELEMENT("cs:CourtHouseFax",NVL(hf.CourtHouseFax,''))
                                             ) --CrownCourt
                               ,      
                                /*************************************************Trial cases fields start here***************************************************/
                                 XMLELEMENT("cs:TrialCases",
                                  XMLELEMENT("cs:Case", XMLELEMENT("cs:CaseNumber",NVL(tc.case_number,''))
                                                   , XMLELEMENT("cs:CaseArrivedFrom"
                                                   , XMLELEMENT("cs:OriginatingCourt",XMLFOREST(tc.MagCourtType AS "cs:CourtHouseType")
                                                       ,XMLELEMENT("cs:CourtHouseCode" ,XMLATTRIBUTES(tc.MagCourtHouseShortName AS "cs:CourtHouseShortName"))--CourtHouseCode
                                                       ,XMLELEMENT("cs:CourtHouseName", tc.MagCourtName)--CourtHouseName                                           
                                                       ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(tc.MagAddr1 AS "apd:Line"
                                                                                                    ,tc.MagAddr2 AS "apd:Line"
                                                                                                    ,tc.MagAddr3 AS "apd:Line"
                                                                                                    ,tc.MagPostcode AS "apd:PostCode"
                                                                                                  )
                                                                  )--CourtHouseAddress
                                                         ,XMLELEMENT("cs:CourtHouseDX",NVL(tc.MagDx,''))
                                                         ,XMLELEMENT("cs:CourtHouseTelephone",NVL(tc.MagPhone,''))         
                                                              )--OriginatingCourt
                                                             )--CaseArrivedFrom
                                                   , XMLELEMENT("cs:Hearing",XMLELEMENT("cs:HearingDescription",tc.HearingDescription)
                                                                         ,XMLELEMENT("cs:HearingDate",tc.HearingDate)
                                                               )--Hearing
                                                   , XMLELEMENT("cs:Defendants"
                                                     , XMLELEMENT("cs:Defendant"
                                                       , XMLELEMENT("cs:PersonalDetails"
                                                         , XMLELEMENT("cs:Name",XMLFOREST(tc.CitizenNameForename AS "apd:CitizenNameForename"
                                                                                         ,tc.CitizenNameSurname AS "apd:CitizenNameSurname")
                                                                     )--Name
                                                         , XMLELEMENT("cs:IsMasked",tc.is_masked)
                                                         , XMLELEMENT("cs:DateOfBirth",XMLFOREST(tc.BirthDate AS "apd:BirthDate"
                                                                                                ,tc.VerfiedBy AS "apd:VerfiedBy")
                                                                     )--DateOfBirth
                                                         , XMLELEMENT("cs:Sex",tc.sex) 
                                                         , XMLELEMENT("cs:Address",XMLFOREST(tc.DefAddr1 AS "apd:Line"
                                                                                            ,tc.DefAddr2 AS "apd:Line"
                                                                                            ,tc.DefAddr3 AS "apd:Line"
                                                                                            ,tc.DefPostCode AS "apd:PostCode")
                                                                     )--Address  
                                                                   )--PersonalDetails
                                                         , XMLELEMENT("ASNs",XMLFOREST(tc.asn AS "cs:ASN"))
                                                         , XMLELEMENT("cs:CRESTdefendantID",tc.CRESTdefendantID)
                                                         , XMLELEMENT("cs:URN",tc.urn)
                                                         , XMLELEMENT("cs:PrisonLocation",XMLFOREST(tc.PrisonLocation AS "cs:Location")) --Check this field
                                                         , XMLELEMENT("cs:CustodyStatus",tc.CustodyStatus)
                                                         , XMLELEMENT("cs:Consel"
                                                          , XMLELEMENT("cs:Solicitor"
                                                           , XMLELEMENT("cs:Party"
                                                             , XMLELEMENT("cs:Organisation"
                                                               , XMLELEMENT("cs:OrganisationName",tc.solicitor_firm_name)--OrganisationName
                                                               , XMLELEMENT("cs:OrganisationAddress",XMLFOREST(tc.SolAddr1 AS "apd:Line"
                                                                                                           ,tc.SolAddr2 AS "apd:Line"
                                                                                                           ,tc.SolAddr3 AS "apd:Line"
                                                                                                           ,tc.SolPostCode AS "apd:PostCode")
                                                                           )--OrganisationAddress
                                                               , XMLELEMENT("cs:ContactDetails"
                                                                 ,XMLELEMENT("apd:Telephone"
                                                                  ,XMLELEMENT("apd:TelNationalNumber",NVL(tc.SolPhone,''))
                                                                            )--Telephone
                                                                 ,XMLELEMENT("apd:Fax"
                                                                  ,XMLELEMENT("apd:FaxNationalNumber",NVL(tc.SolFax,''))
                                                                            )--Fax
                                                                           )--ContactDetails                  
                                                                         )--Organisation
                                                                       )--Party
                                                               ,XMLELEMENT("cs:StartDate",NVL(tc.SolRepStartDate,''))        
                                                                      )--Solicitor
                                                                    )--Counsel
                                                               ,XMLELEMENT("cs:Additional_notes",tc.AdditonalNotes)      
                                                                 )--Defendant
                                                               )--Defendants
                                                               ,XMLELEMENT("cs:Prosecution",XMLATTRIBUTES(tc.ProsOrgaisation AS "cs:ProsecutingAuthority")
                                                                ,XMLELEMENT("cs:ProsecutingOrganisation"
                                                                 ,XMLELEMENT("cs:OrganisationName",'OrganisationName')
                                                                  ,XMLELEMENT("cs:OrganisationAddress",XMLFOREST(tc.ProsAddr1 AS "apd:Line"
                                                                                                             ,tc.ProsAddr2 AS "apd:Line"
                                                                                                             ,tc.ProsAddr3 AS "apd:Line"
                                                                                                             ,tc.ProsAddr4 AS "apd:Line"
                                                                                                             ,tc.ProsPostCode AS "apd:PostCode")
                                                                             )--OrganisationAddress
                                                                  , XMLELEMENT("cs:ContactDetails"
                                                                    ,XMLELEMENT("apd:Telephone"
                                                                     ,XMLELEMENT("apd:TelNationalNumber",tc.ProsPhone)
                                                                                )--Telephone
                                                                    ,XMLELEMENT("apd:Fax"
                                                                     ,XMLELEMENT("apd:FaxNationalNumber",tc.ProsFax)
                                                                                )--Fax
                                                                              )--ContactDetails                                                  
                                                                           )--ProsecutingOrganisation
                                                                          )--Prosecution 
                                                                    ,XMLELEMENT("cs:CaseClassNumber",tc.case_class)
                                            )--Case
                                          )--TrialCases
                                        /*************************************************Trial cases fields start here***************************************************/
                                ,XMLELEMENT("cs:CommittalCases",
                                  XMLELEMENT("cs:Case", XMLELEMENT("cs:CaseNumber",NVL(cc.case_number,''))
                                                   , XMLELEMENT("cs:CaseArrivedFrom"
                                                   , XMLELEMENT("cs:OriginatingCourt",XMLFOREST(cc.MagCourtType AS "cs:CourtHouseType")
                                                       ,XMLELEMENT("cs:CourtHouseCode" ,XMLATTRIBUTES(cc.MagCourtHouseShortName AS "cs:CourtHouseShortName"))--CourtHouseCode
                                                       ,XMLELEMENT("cs:CourtHouseName", cc.MagCourtName)--CourtHouseName                                           
                                                       ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(cc.MagAddr1 AS "apd:Line"
                                                                                                 ,cc.MagAddr2 AS "apd:Line"
                                                                                                 ,cc.MagAddr3 AS "apd:Line"
                                                                                                 ,cc.MagPostcode AS "apd:PostCode"
                                                                                                  )
                                                                  )--CourtHouseAddress
                                                         ,XMLELEMENT("cs:CourtHouseDX",NVL(cc.MagDx,''))
                                                         ,XMLELEMENT("cs:CourtHouseTelephone",NVL(cc.MagPhone,''))         
                                                              )--OriginatingCourt
                                                             )--CaseArrivedFrom
                                                    , XMLELEMENT("cs:Defendants"
                                                     , XMLELEMENT("cs:Defendant"
                                                       , XMLELEMENT("cs:PersonalDetails"
                                                         , XMLELEMENT("cs:Name",XMLFOREST(cc.CitizenNameForename AS "apd:CitizenNameForename"
                                                                                      ,cc.CitizenNameForename2 AS "apd:CitizenNameForename"
                                                                                      ,cc.CitizenNameSurname AS "apd:CitizenNameSurname")
                                                                     )--Name
                                                         , XMLELEMENT("cs:IsMasked",tc.is_masked)
                                                         , XMLELEMENT("cs:DateOfBirth",XMLFOREST(cc.BirthDate AS "apd:BirthDate"
                                                                                                ,cc.VerfiedBy AS "apd:VerfiedBy")
                                                                     )--DateOfBirth
                                                         , XMLELEMENT("cs:Sex",cc.sex) 
                                                         , XMLELEMENT("cs:Address",XMLFOREST(cc.DefAddr1 AS "apd:Line"
                                                                                            ,cc.DefAddr2 AS "apd:Line"
                                                                                            ,cc.DefAddr3 AS "apd:Line"
                                                                                            ,cc.DefPostCode AS "apd:PostCode")
                                                                     )--Address  
                                                                   )--PersonalDetails
                                                         , XMLELEMENT("cs:ASNs",XMLFOREST(cc.asn AS "cs:ASN"))
                                                         , XMLELEMENT("cs:CRESTdefendantID",cc.CRESTdefendantID)
                                                         , XMLELEMENT("cs:URN",cc.urn)
                                                         , XMLELEMENT("cs:PrisonLocation",XMLFOREST(cc.PrisonLocation AS "cs:Location")) --Check this field
                                                         , XMLELEMENT("cs:CustodyStatus",cc.CustodyStatus)
                                                         ,XMLELEMENT("cs:Charges",XMLATTRIBUTES(xhb_get_xml_reports.GetChargeCnt(cc.case_number) AS "cs:NumberOfCharges")
                                                          ,XMLELEMENT("cs:Charge" ,XMLATTRIBUTES(cc.IndictmentCountNumber AS "IndictmentCountNumber" , cc.CJSoffenceCode AS "CJSoffenceCode")
                                                             ,XMLFOREST(cc.crn AS "cs:CRN"
                                                                       ,cc.CRESTchargeID AS "cs:CRESTchargeID"
                                                                       ,cc.OffenceStatement AS "cs:ffenceStatement"
                                                                       ,cc.OffenceParticulars AS "cs:OffenceParticulars")
                                                                      )--charge
                                                                    )--Charges
                                                                 )--Defendant
                                                               )--Defendants
                                                               ,XMLELEMENT("cs:Prosecution",XMLATTRIBUTES(tc.ProsOrgaisation AS "ProsecutingAuthority")
                                                                ,XMLELEMENT("cs:ProsecutingOrganisation"
                                                                 ,XMLELEMENT("cs:OrganisationName",'OrganisationName')
                                                                  ,XMLELEMENT("cs:OrganisationAddress",XMLFOREST(tc.ProsAddr1 AS "apd:Line"
                                                                                                             ,tc.ProsAddr2 AS "apd:Line"
                                                                                                             ,tc.ProsAddr3 AS "apd:Line"
                                                                                                             ,tc.ProsAddr4 AS "apd:Line"
                                                                                                             ,tc.ProsPostCode AS "apd:PostCode")
                                                                             )--OrganisationAddress
                                                                  , XMLELEMENT("cs:ContactDetails"
                                                                    ,XMLELEMENT("apd:Telephone"
                                                                     ,XMLELEMENT("apd:TelNationalNumber",tc.ProsPhone)
                                                                                )--Telephone
                                                                    ,XMLELEMENT("apd:Fax"
                                                                     ,XMLELEMENT("apd:FaxNationalNumber",tc.ProsFax)
                                                                                )--Fax
                                                                              )--ContactDetails                                                  
                                                                           )--ProsecutingOrganisation
                                                                          )--Prosecution 
                                                                   -- ,XMLELEMENT("CaseClassNumber",tc.case_class) was originally in but removed
                                            )--Case
                                          )--Committal Cases
                                                /*************************************************Appeal cases fields start here***************************************************/
                                ,XMLELEMENT("cs:AppealCases",
                                  XMLELEMENT("cs:Case", XMLELEMENT("cs:CaseNumber",NVL(ac.case_number,''))
                                                   , XMLELEMENT("cs:CaseArrivedFrom"
                                                   , XMLELEMENT("cs:OriginatingCourt",XMLFOREST(ac.MagCourtType AS "cs:CourtHouseType")
                                                       ,XMLELEMENT("cs:CourtHouseCode" ,XMLATTRIBUTES(ac.MagCourtHouseShortName AS "cs:CourtHouseShortName"))--CourtHouseCode
                                                       ,XMLELEMENT("cs:CourtHouseName", ac.MagCourtName)--CourtHouseName                                           
                                                       ,XMLELEMENT("cs:CourtHouseAddress", XMLFOREST(ac.MagAddr1 AS "apd:Line"
                                                                                                 ,ac.MagAddr2 AS "apd:Line"
                                                                                                 ,ac.MagAddr3 AS "apd:Line"
                                                                                                 ,ac.MagPostcode AS "apd:PostCode"
                                                                                                  )
                                                                  )--CourtHouseAddress
                                                         ,XMLELEMENT("cs:CourtHouseDX",NVL(ac.MagDx,''))
                                                         ,XMLELEMENT("cs:CourtHouseTelephone",NVL(ac.MagPhone,''))         
                                                              )--OriginatingCourt
                                                             )--CaseArrivedFrom
                                                    , XMLELEMENT("cs:Defendants"
                                                     , XMLELEMENT("cs:Defendant"
                                                       , XMLELEMENT("cs:PersonalDetails"
                                                         , XMLELEMENT("cs:Name",XMLFOREST(ac.CitizenNameForename AS "apd:CitizenNameForename"
                                                                                      ,ac.CitizenNameForename2 AS "apd:CitizenNameForename"
                                                                                      ,ac.CitizenNameSurname AS "apd:CitizenNameSurname")
                                                                     )--Name
                                                         , XMLELEMENT("cs:IsMasked",ac.is_masked)
                                                         , XMLELEMENT("cs:DateOfBirth",XMLFOREST(ac.BirthDate AS "apd:BirthDate"
                                                                                             ,ac.VerfiedBy AS "apd:VerfiedBy")
                                                                     )--DateOfBirth
                                                         , XMLELEMENT("cs:Sex",ac.sex) 
                                                         , XMLELEMENT("cs:Address",XMLFOREST(ac.DefAddr1 AS "apd:Line"
                                                                                         ,ac.DefAddr2 AS "apd:Line"
                                                                                         ,ac.DefAddr3 AS "apd:Line"
                                                                                         ,ac.DefPostCode AS "apd:PostCode")
                                                                     )--Address  
                                                                   )--PersonalDetails
                                                         , XMLELEMENT("cs:CRESTdefendantID",ac.CRESTdefendantID)
                                                         , XMLELEMENT("cs:URN",ac.urn)
                                                         , XMLELEMENT("cs:MagistratesCourtRefNumber",ac.MagistratesCourtRefNumber)
                                                         , XMLELEMENT("cs:PrisonLocation",XMLFOREST(ac.PrisonLocation AS "cs:Location")) --Check this field
                                                         , XMLELEMENT("cs:CustodyStatus",ac.CustodyStatus)
                                                         , XMLELEMENT("cs:Charges",XMLATTRIBUTES(xhb_get_xml_reports.GetChargeCnt(ac.case_number) AS "NumberOfCharges")
                                                          ,XMLELEMENT("cs:Charge" ,XMLATTRIBUTES(ac.IndictmentCountNumber AS "IndictmentCountNumber" , ac.CJSoffenceCode AS "CJSoffenceCode")
                                                             ,XMLFOREST(cc.CRESTchargeID AS "cs:CRESTchargeID"
                                                                       ,cc.OffenceStatement AS "cs:OffenceStatement"
                                                                       ,cc.OffenceParticulars AS "cs:OffenceParticulars")
                                                                      )--charge
                                                                    )--Charges
                                                                 )--Defendant
                                                               )--Defendants
                                                               ,XMLELEMENT("cs:Prosecution",XMLATTRIBUTES(ac.ProsOrgaisation AS "ProsecutingAuthority")
                                                                ,XMLELEMENT("cs:ProsecutingOrganisation"
                                                                 ,XMLELEMENT("cs:OrganisationName",'OrganisationName')
                                                                  ,XMLELEMENT("cs:OrganisationAddress",XMLFOREST(ac.ProsAddr1 AS "apd:Line"
                                                                                                             ,ac.ProsAddr2 AS "apd:Line"
                                                                                                             ,ac.ProsAddr3 AS "apd:Line"
                                                                                                             ,ac.ProsAddr4 AS "apd:Line"
                                                                                                             ,ac.ProsPostCode AS "apd:PostCode")
                                                                             )--OrganisationAddress
                                                                  , XMLELEMENT("cs:ContactDetails"
                                                                    ,XMLELEMENT("apd:Telephone"
                                                                     ,XMLELEMENT("apd:TelNationalNumber",ac.ProsPhone)
                                                                                )--Telephone
                                                                    ,XMLELEMENT("apd:Fax"
                                                                     ,XMLELEMENT("apd:FaxNationalNumber",ac.ProsFax)
                                                                                )--Fax
                                                                              )--ContactDetails                                                  
                                                                           )--ProsecutingOrganisation
                                                                          )--Prosecution 
                                                                    ,XMLELEMENT("CaseClassNumber",ac.case_class)
                                            )--Case
                                          )--Appeal Cases  
                         )--RunningList removed xmlagg tag
             .getclobval() as xml_data
FROM  header_fields hf
,     trial_cases tc
,     comm_cases cc
,     appeal_cases ac
WHERE hf.list_id = tc.list_id (+)
AND   hf.list_id = cc.list_id (+)
AND   hf.list_id = ac.list_id (+)
;

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
      
  END GetRlData; 
  
   /** 
  * DESCRIPTION :  
  *               Procedure   Purpose
  *               =========   =======
  *               GetWlData   'Warned List' extract. JIRA ticket CTX-1371. GetReportRequest will pass in 'WL_DATA'. 
  **/
  
  
  FUNCTION GetWlData RETURN CLOB AS
  
  v_return_xml CLOB;
  v_xml_length NUMBER;
  x_no_file    EXCEPTION;
  
   CURSOR get_xml_c IS
                       SELECT XMLAGG(XMLELEMENT("WarnedList",
                                  XMLELEMENT("ListHeader", XMLFOREST('ListCategory' AS "ListCategory"
                                                                     ,xl.list_start_date AS "StartDate"
                                                                     ,xl.list_end_date AS "EndDate"
                                                                     ,xl.version AS "Version"
                                                                     ,NVL(xhl.print_reference,'') AS "CRESTprintRef" --check this field is correct
                                                                     ,NVL(xhl.published_time,'') AS "PublishedTime"
                                                                     ,NVL(xhl.crest_list_id,'') As "CRESTlistID" --check this field is correct
                                                                   )
                                            ), --ListHeader
                                  XMLELEMENT("CrownCourt", XMLFOREST(xcrt.court_type AS "CourtHouseType"
                                                                     ,xcrt.court_code AS "CourtHouseCode"
                                                                     ,xcrt.court_name AS "CourtHouseName")
                                                                     , XMLELEMENT("CourtHouseAddress", XMLFOREST(xadc.address_1 AS "Line"
                                                                                                                ,xadc.address_2 AS "Line"
                                                                                                                ,xadc.address_3 AS "Line"
                                                                                                                ,xadc.postcode AS "PostCode"
                                                                                                                )
                                                                                 )
                                                                    , XMLELEMENT("CourtHouseDX",xcdd.contact_value)
                                                                    , XMLELEMENT("CourtHouseTelephone",xcdp.contact_value)
                                                                    , XMLELEMENT("CourtHouseFax",xcdfx.contact_value)
                                             ), --CrownCourt
                                XMLELEMENT("ListingInstructions", XMLELEMENT("ListingInstruction",'ListingInstructions - what field does this refer to?????')
                                             ), --ListingInstructions           
                                XMLELEMENT("CourtLists",
                                 XMLELEMENT("CourtList",
                                  XMLELEMENT("CourtHouse", XMLFOREST(xcrt.court_type AS "CourtHouseType"
                                                                    ,xcrt.court_code AS "CourtHouseCode"
                                                                    ,xcrt.court_name AS "CourtHouseName"
                                                                    )
                                                         , XMLELEMENT("CourtHouseAddress", XMLFOREST(xadc.address_1 AS "Line"
                                                                                                                ,xadc.address_2 AS "Line"
                                                                                                                ,xadc.address_3 AS "Line"
                                                                                                                ,xadc.postcode AS "PostCode"
                                                                                                                )
                                                                      )
                                                                    , XMLELEMENT("CourtHouseDX",xcdd.contact_value)
                                                                    , XMLELEMENT("CourtHouseTelephone",xcdp.contact_value)
                                                                    , XMLELEMENT("CourtHouseFax",xcdfx.contact_value)
                                             )--CourtHouse
                                ,XMLELEMENT("WithFixedDate",XMLATTRIBUTES(xrht.hearing_type_code AS "HearingType")
                                 ,XMLELEMENT("Fixture"
                                  , XMLELEMENT("FixedDate",xcdf.listing_date||' make sure this field is FixedDate')
                                  , XMLELEMENT("Notes",xcdf.list_note_text||' make sure this field is list note')
                                   ,XMLELEMENT("Cases"
                                    ,XMLELEMENT("Case", XMLELEMENT("CaseNumber",NVL(xc.case_number,''))
                                                   , XMLELEMENT("CaseArrivedFrom"
                                                   , XMLELEMENT("OriginatingCourt",XMLFOREST(xcrt.court_type AS "CourtHouseType")
                                                       ,XMLELEMENT("CourtHouseCode" ,XMLATTRIBUTES(xcrt.short_name AS "CourtHouseShortName"))--CourtHouseCode
                                                       ,XMLELEMENT("CourtHouseName", xcrt.court_name)--CourtHouseName                                           
                                                              )--OriginatingCourt
                                                             )--CaseArrivedFrom
                                                   , XMLELEMENT("Hearing",XMLELEMENT("HearingDescription",xrht.hearing_type_desc)
                                                                         ,XMLELEMENT("HearingDate",xsh.date_of_hearing)
                                                               )--Hearing
                                                   , XMLELEMENT("Defendants"
                                                     , XMLELEMENT("Defendant"
                                                       , XMLELEMENT("PersonalDetails"
                                                         , XMLELEMENT("Name",XMLFOREST(xd.first_name AS "CitizenNameForename"
                                                                                      ,xd.surname AS "CitizenNameSurname")
                                                                     )--Name
                                                         , XMLELEMENT("IsMasked",xdoc.is_masked)
                                                         , XMLELEMENT("DateOfBirth",XMLFOREST(xd.date_of_birth AS "BirthDate"
                                                                                            ,'VerfiedBy' AS "VerfiedBy")
                                                                     )--DateOfBirth
                                                         , XMLELEMENT("Sex",xd.gender) 
                                                         , XMLELEMENT("Address",XMLFOREST(xadd.address_1 AS "Line"
                                                                                         ,xadd.address_2 AS "Line"
                                                                                         ,xadd.address_3 AS "Line"
                                                                                         ,xadd.postcode AS "PostCode")
                                                                     )--Address  
                                                                   )--PersonalDetails
                                                         , XMLELEMENT("ASNs",XMLFOREST(xdoc.asn AS "ASN"))
                                                         , XMLELEMENT("CRESTdefendantID",xd.crest_defendant_id)
                                                         , XMLELEMENT("URN",'URN')
                                                         , XMLELEMENT("MagistratesCourtRefNumber",xc.magistrates_case_ref||' Make sure this is xc.magistrates_case_ref')
                                                         , XMLELEMENT("Charges"
                                                           ,XMLELEMENT("Charge" ,XMLATTRIBUTES('IndictmentCountNumber' AS "IndictmentCountNumber" , 'CJSoffenceCode' AS "CJSoffenceCode")
                                                           ,XMLFOREST(xdof.crn_id AS "CRN"
                                                                      ,xcge.crest_charge_id AS "CRESTchargeID"
                                                                      ,'tba' AS "OffenceStatement"
                                                                      ,'tba' AS "OffenceParticulars")
                                                                      )--Charge 
                                                                     ) --Charges
                                                      )--Defendant
                                                    )--Defendants
                                        ,XMLELEMENT("Prosecution",XMLATTRIBUTES(GetRefProsAgency(xc.case_id,'AUTHORITY') AS "ProsecutingAuthority")
                                                                 ,XMLFOREST('ProsecutingReference' AS "ProsecutingReference")
                                                                 ,XMLELEMENT("ProsecutingOrganisation",XMLFOREST('OrganisationName' AS "OrganisationName")) 
                                                   )--Prosecution 
                                                  )--Hearing
                                                )--Hearings
                                             )--Sitting
                                            )--sittings
                                           )--CourtList
                                          )--CourtLists  
                         )--DailyList
             ) --header tag
             .getclobval() as xml_data
     FROM xhb_case xc 
     ,    xhb_case_on_list xcol --new table
     ,    xhb_case_listing_entry xcle
     ,    xhb_case_diary_fixture xcdf
     ,    xhb_list xl
     ,    xhb_hearing_list xhl
     ,    xhb_hearing xh
     ,    xhb_ref_hearing_type xrht 
     ,    xhb_scheduled_hearing xsh
     ,    xhb_sitting xs
     ,    xhb_sitting_on_list xsol
     ,    xhb_court_site xcs
     ,    xhb_court xcrt
     ,    xhb_court_room xcr
     ,    xhb_address xadc --addressfor court
     ,    xhb_contact_detail xcdp --contact detail phone 
     ,    xhb_contact_detail xcdd --contact detail dx
     ,    xhb_contact_detail xcdfx --contact detail fax
     ,    xhb_defendant_on_case xdoc
     ,    xhb_defendant xd
     ,    xhb_address xadd --defendant address
     ,    xhb_charge xcge
     ,    xhb_offence xo
     ,    xhb_defendant_on_offence xdof
     ,    xhb_ref_system_code xrsc1
     ,    xhb_def_on_case_ref_sol_firm xdocrsf
     ,    xhb_ref_solicitor_firm xrsf
     ,    xhb_address sxa -- xhb_def_on_case_ref_sol_firm solicitor address
     ,    xhb_contact_detail xcdsp --contact detail phone
     ,    xhb_contact_detail xcdsf --contact detail fax
     WHERE xc.case_id = xcol.case_id
     AND   xcol.list_id = xl.list_id
     AND   xc.case_id = xcle.case_id (+)
     AND   xcle.case_listing_entry_id = xcdf.case_listing_entry_id (+)
     AND   xl.list_id = xhl.list_id (+)
     AND   xc.case_id = xh.case_id
     AND   xh.hearing_id = xsh.hearing_id
     AND   xh.ref_hearing_type_id = xrht.ref_hearing_type_id
     AND   xcol.sitting_on_list_id = xsol.sitting_on_list_id
     AND   xl.list_id = xsol.list_id (+)
     AND   xcol.court_site_id = xcs.court_site_id
     AND   xcs.court_id = xcrt.court_id
     AND   xcol.court_room_id = xcr.court_room_id
     AND   xcrt.address_id = xadc.address_id (+)
     AND   xadc.address_id = xcdp.address_id (+)
     AND   upper(xcdp.contact_type(+)) = 'PHONE' 
     AND   xadc.address_id = xcdd.address_id (+)
     AND   upper(xcdd.contact_type(+)) = 'SECURE EMAIL'
     AND   xadc.address_id = xcdfx.address_id (+)
     AND   upper(xcdfx.contact_type(+)) = 'FAX'
     AND   xc.case_id = xdoc.case_id
     AND   xdoc.defendant_id = xd.defendant_id
     AND   xdoc.obs_ind <> 'Y'
     AND   xd.address_id = xadd.address_id(+)
     AND   xc.case_id = xcge.case_id (+)
     AND   xcge.charge_id = xo.charge_id (+)
     AND   xo.offence_id = xdof.offence_id (+)
     AND   xd.prison_id = xrsc1.code
     AND   xrsc1.code_type = 'PRISON_ID'
     AND   xd.court_id = xrsc1.court_id (+) --make sure this is right.  Joining xd using court_id
     AND   xdoc.defendant_on_case_id = xdocrsf.defendant_on_case_id (+)
     AND   xdocrsf.ref_solicitor_firm_id = xrsf.ref_solicitor_firm_id (+)
     AND   NVL(xrsf.address_id,0) = sxa.address_id (+)
     AND   sxa.address_id = xcdsf.address_id (+)
     AND   upper(xcdsf.contact_type(+)) = 'FAX' --solicitor fax
     AND   xadc.address_id = xcdsp.address_id (+)
     AND   upper(xcdsp.contact_type(+)) = 'PHONE' --solicitor phone
     AND   xcol.obs_ind <> 'Y'
     AND   xrht.obs_ind <> 'Y'
     AND   xsol.obs_ind <> 'Y'
     AND   xcs.obs_ind <> 'Y'
     AND   xcrt.obs_ind <> 'Y'
     AND   xcr.obs_ind <> 'Y'
     AND   xcge.obs_ind <> 'Y'
     AND   xo.obs_ind <> 'Y'
     AND   xrht.obs_ind <> 'Y'
     AND   xdoc.obs_ind <> 'Y'
     AND   xdof.obs_ind <> 'Y'
     ;

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
      
  END GetWlData; 
  
  
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
 
 /*Get the ref prosecutor agency- used in most of the xml outputs*/ 
 FUNCTION GetRefProsAgency  (p_case_id IN xhb_case.case_id%TYPE
                            ,p_field   IN VARCHAR2)
  RETURN VARCHAR2
  IS
  x_return VARCHAR2(100);
  
  CURSOR pros_c IS
  SELECT rpa.prosecutor_name_3
  ,      rpa.address_id
  FROM xhb_case_prosecutor_agency cpa
  ,    xhb_ref_prosecutor_agency rpa
  WHERE cpa.ref_prosecutor_agency_id = rpa.ref_prosecutor_agency_id
  AND cpa.case_id = p_case_id;
  
  pros_r pros_c%ROWTYPE;
  
  BEGIN
  
   OPEN pros_c;
   FETCH pros_c
   INTO pros_r;
  
  IF upper(p_field) = 'AUTHORITY' 
   THEN
    x_return := pros_r.prosecutor_name_3;
   ELSIF upper(p_field) = 'ADDRESS'
    THEN 
     x_return := to_char(pros_r.address_id);
  END IF;
  
   IF x_return IS NULL
    THEN 
      x_return := ' ';
   END IF;
   
   RETURN x_return;
   
  EXCEPTION 
   WHEN no_data_found THEN x_return :='-';
   WHEN OTHERS THEN x_return:= ' '|| SQLCODE||' '|| SQLERRM;
     
  END GetRefProsAgency;  
  
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
  
  
  FUNCTION get_charges(p_defendant_on_case_id XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE) RETURN XMLType IS
  charges XMLType;
  BEGIN
  SELECT XMLAgg( XMLELEMENT("cs:Charge" ,XMLATTRIBUTES(NVL(xcge.CREST_CHARGE_SEQ_NO, 1) AS "IndictmentCountNumber" , xro.OFFENCE_CODE AS "CJSoffenceCode")
                           ,XMLFOREST(xdof.crn_id AS "cs:CRN"
                                     ,xcge.crest_charge_id AS "cs:CRESTchargeID"
                                     , xc.CASE_TYPE || xc.CASE_NUMBER AS "cs:CaseNumber"
                                    , xro.OFFENCE_DESC AS "cs:OffenceStatement"
                                    , XMLFOREST(offence_address.ADDRESS_1 AS "p1:Line"
                                               ,offence_address.ADDRESS_2 AS "p1:Line"
                                               ,offence_address.ADDRESS_3 AS "p1:Line"
                                               ,offence_address.POSTCODE AS "p1:PostCode") "cs:OffenceLocation"
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


  FUNCTION get_defendants_on_hearing(p_hearing_id XHB_CASE_ON_LIST.CASE_ON_LIST_ID%TYPE) RETURN XMLType IS
  defendants XMLType;
  BEGIN
  SELECT XMLAgg(XMLELEMENT("cs:Defendant",
                          XMLELEMENT("cs:PersonalDetails",
                                XMLFOREST(XMLFOREST(xd.FIRST_NAME AS "p1:CitizenNameForename"
                                                    ,xd.SURNAME AS "p1:CitizenNameSurname"
                                                    )"cs:Name"
                                          , xdoc.MASKED_NAME AS "cs:MaskedName"
                                          , DECODE(xdoc.is_masked, 'Y', 'yes', 'no') AS "cs:IsMasked"
                                  
                                         , XMLFOREST(xd.date_of_birth AS "p1:BirthDate"
                                                     ,CASE WHEN xd.date_of_birth IS NOT NULL THEN 'not verified' ELSE NULL END AS "p1:VerifiedBy") "cs:DateOfBirth"
                                         -- , xd.AGE
                                         , DECODE(xd.gender, 1, 'male', 2, 'female', 'unknown') AS "cs:Sex"
                                         , XMLFOREST(xadd.address_1 AS "p1:Line"
                                                                 ,xadd.address_2 AS "p1:Line"
                                                                 ,xadd.address_3 AS "p1:Line"
                                                                 ,xadd.postcode AS "p1:PostCode"
                                                      ) "cs:Address" 
                                         , xdoc.NATIONALITY AS "cs:Nationality"
                                        )          
                                )--PersonalDetails
                     , get_contact_details(xd.ADDRESS_ID) "cs:ContactDetails"
                     ,XMLFOREST(XMLFOREST(xdoc.ASN AS "cs:ASN")"cs:ASNs" 
                     ,xd.CREST_DEFENDANT_ID AS "cs:CRESTdefendantID"
                     ,xdoc.PNC_ID AS "cs:PNCnumber"
                     --,"URN",'URN')
                     --CRO
                     --,xc.magistrates_case_ref AS "cs:MagistratesCourtRefNumber"--check that this field is right
                     ,xdr.REFERENCE_VALUE AS "cs:PrisonerID"
                     --PrisonLocation
                     ,DECODE(xdoc.CURRENT_BC_STATUS, 'B', 'On bail', 'C', 'In custody', 'N', 'Not applicable') AS "cs:CustodyStatus")
                     , XMLELEMENT("cs:Counsel"
                      , (SELECT XMLAgg(XMLELEMENT("cs:Solicitor"
                                     , XMLELEMENT("cs:Party"
                                       , XMLELEMENT("cs:Organisation"
                                          
                                         , XMLELEMENT("cs:OrganisationName",xrsf.solicitor_firm_name)--OrganisationName
                                         , XMLELEMENT("cs:OrganisationAddress",XMLFOREST(solicitor_address.ADDRESS_1 AS "p1:Line"
                                                                                       ,solicitor_address.ADDRESS_2 AS "p1:Line"
                                                                                       ,solicitor_address.ADDRESS_3 AS "p1:Line"
                                                                                       ,solicitor_address.POSTCODE AS "p1:PostCode")
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
                                          AND xrsf.ADDRESS_ID = solicitor_address.ADDRESS_ID(+))                                                                                                                     
                            )--Counsel
                            ,XMLFOREST(--,XMLATTRIBUTES(xhb_get_xml_reports.GetChargeCnt(xc.case_number) AS "NumberOfCharges"),
                                        (get_charges(xdoc.DEFENDANT_ON_CASE_ID)  )"cs:Charges"
                            )  --Charges
                            --OriginalCharges
                            --AdditionalNotes
                            --DeportationReason
                            ,(SELECT XMLAgg(XMLFOREST(xrhst.TITLE AS "cs:Reason")) AS "cs:HateCrime"
                                    FROM XHB_HATE_SENTENCING xhs, XHB_REF_HATE_SENTENCING_TYPE xrhst
                                    WHERE xdoc.DEFENDANT_ON_CASE_ID = xhs.DEFENDANT_ON_CASE_ID
                                    AND  xhs.REF_HATE_SENT_TYPE_ID = xrhst.REF_HATE_SENTENCING_TYPE_ID)
                            ,XMLFOREST(xdoc.DEFENDANT_NUMBER AS "cs:DefendantNumber")
                                
                            )) INTO defendants
                            FROM XHB_DEF_ON_CASE_ON_LIST xdocol,  XHB_DEFENDANT_ON_CASE xdoc, XHB_DEFENDANT xd, XHB_ADDRESS xadd, XHB_DEFENDANT_REFERENCE xdr
                              WHERE p_hearing_id = xdocol.CASE_ON_LIST_ID
                              AND   (xdocol.OBS_IND <> 'Y' or xdocol.OBS_IND is null)
                              AND   xdocol.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                              AND   (xdoc.OBS_IND <> 'Y' or xdoc.OBS_IND is null)
                              AND   xdoc.DEFENDANT_ID = xd.DEFENDANT_ID
                              AND   xd.address_id = xadd.address_id(+)
                              AND   xdoc.DEFENDANT_ID = xdr.DEFENDANT_ID (+) --prisonerf
                              AND   'PRISONER NUMBER' = upper(xdr.REFERENCE_NAME (+))  ;
              RETURN defendants;
  END get_defendants_on_hearing;
  
  FUNCTION get_hearings_on_sitting(p_sitting_id XHB_SITTING_ON_LIST.SITTING_ON_LIST_ID%TYPE) RETURN XMLType IS
  hearings XMLType;
  BEGIN
  SELECT XMLAgg(XMLELEMENT("cs:Hearing",XMLFOREST(xcol.SEQ_NO AS "cs:HearingSequenceNumber")
                         ,XMLELEMENT("cs:HearingDetails",XMLATTRIBUTES(xrht.hearing_type_code AS "HearingType")
                                                     ,XMLFOREST(xrht.hearing_type_desc AS "cs:HearingDescription"
                                                               ,NVL(xcol.TIME_LISTED,'') AS "cs:HearingDate" 
                                                               ,xcol.LIST_NOTE_TEXT AS "cs:ListNote"
                                                                )
                                    )--HearingDetails                            
                   -- ,XMLFOREST('CRESTHearingID' AS "CRESTHearingID")
                    ,XMLFOREST(xrsc.DE_CODE AS "cs:TimeMarkingNote"
                    ,xc.CASE_TYPE || xc.CASE_NUMBER AS "cs:CaseNumber")
                    ,XMLFOREST(
                                      -- ,XMLFOREST('ProsecutingReference' AS "ProsecutingReference")
                                      XMLFOREST( XMLFOREST(xrpa.CPS_CODE AS "cs:OrganisationCode"
                                                             , xrpa.PROSECUTOR_NAME_1 || ' ' || xrpa.PROSECUTOR_NAME_2 || ' ' || xrpa.PROSECUTOR_NAME_3 AS "cs:OrganisationName"
                                                             , XMLFOREST(prosecutor_address.address_1 AS "p1:Line"
                                                                        ,prosecutor_address.address_2 AS "p1:Line"
                                                                        ,prosecutor_address.address_3 AS "p1:Line"
                                                                        ,prosecutor_address.postcode AS "p1:PostCode"
                                                                        ) "cs:OrganisationAddress"
                                                              ,xrpa.DX_REF AS "cs:OrganisationDX"
                                                              ,  get_contact_details(prosecutor_address.ADDRESS_ID) "cs:ContactDetails"
                                                              ) "cs:ProsecutingOrganisation"  
                                                        )"cs:Prosecution"
                                ) 
--                                                                                          ,XMLELEMENT("CommittingCourt",XMLFOREST(xcrt.court_type AS "CourtHouseType")
--                                                                                                                       ,XMLELEMENT("CourtHouseCode",XMLATTRIBUTES(xcrt.short_name AS "CourtHouseShortName"))
--                                                                                                                       ,XMLELEMENT("CourtHouseName",xcrt.court_name)
--                                                                                               ,XMLELEMENT("CourtHouseAddress", XMLFOREST(xadc.address_1 AS "Line"
--                                                                                                                                         ,xadc.address_2 AS "Line"
--                                                                                                                                         ,xadc.address_3 AS "Line"
--                                                                                                                                         ,xadc.postcode AS "PostCode")
--                                                                                                          )--CourtHouseAddress
--                                                                                              ,XMLELEMENT("CourtHouseTelephone",NVL(xcdp.contact_value,'')) 
--                                                                                                      )--CommittingCourt
              ,XMLFOREST(xcol.list_note_text AS "cs:ListNote")
              ,XMLFOREST(xc.NO_DEFENDANTS_FOR_CASE AS "cs:NumberOfDefendants")
              ,XMLELEMENT("cs:Defendants",
                           (get_defendants_on_hearing(xcol.CASE_ON_LIST_ID))--Defendant
                )--Defendants
                --Respondent
      )) INTO hearings
         FROM XHB_CASE_ON_LIST xcol ,XHB_REF_HEARING_TYPE xrht, XHB_REF_SYSTEM_CODE xrsc,
              XHB_CASE xc, XHB_CASE_PROSECUTOR_AGENCY xcpa, XHB_REF_PROSECUTOR_AGENCY xrpa,
              XHB_ADDRESS prosecutor_address  
         WHERE xcol.SITTING_ON_LIST_ID = p_sitting_id
        AND   (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
        AND   xcol.HEARING_TYPE_ID = xrht.ref_hearing_type_id
        AND   (xrht.obs_ind is null or xrht.obs_ind <> 'Y')
        AND   xcol.TIME_MARKING_ID =  xrsc.REF_SYSTEM_CODE_ID (+)
        AND   xc.CASE_ID = xcol.CASE_ID                                                                                    
        AND  xc.CASE_ID = xcpa.CASE_ID (+) 
        AND  xcpa.ref_prosecutor_agency_id = xrpa.ref_prosecutor_agency_id (+)
        AND  xrpa.ADDRESS_ID = prosecutor_address.ADDRESS_ID (+);
      RETURN hearings;
  END get_hearings_on_sitting;

  FUNCTION get_sittings_in_courtsite(p_courtsite_id XHB_COURT_SITE.COURT_SITE_ID%TYPE, p_list_id XHB_LIST.LIST_ID%TYPE) RETURN XMLType IS
  sittings XMLType;
  BEGIN
  SELECT XMLAgg( XMLELEMENT("cs:Sitting", 
                            XMLFOREST(xcr.crest_court_room_no AS "cs:CourtRoomNumber"
                                      ,xsol.SITTING_NUMBER AS "cs:SittingSequenceNo"
                                      ,to_char(xsol.TIME_LISTED, 'hh:mi:ss') AS "cs:SittingAt"
                                      ,'T' as "cs:SittingPriority"
                                      , xsol.LIST_NOTE_TEXT as "cs:SittingNote"
                                      )
                                      ,XMLELEMENT("cs:Judiciary",
                                            XMLELEMENT("cs:Judge",XMLFOREST(xrj.TITLE AS "p1:CitizenNameTitle"
                                                                      , xrj.FIRST_NAME AS "p1:CitizenNameForename"
                                                                      , NVL(xrj.SURNAME, 'N&#47;A') AS "p1:CitizenNameSurname"
                                                                      , xrj.HONOURS as "p1:CitizenNameSuffix"
                                                                      , NVL(xrj.FIRST_NAME, 'N&#47;A') AS "p1:CitizenNameRequestedName"
                                                                      , xrj.CREST_JUDGE_ID AS "cs:CRESTjudgeID"
                                                                     )
                                                )--judge
                                    ,XMLFOREST( XMLFOREST(xsol.JP1 AS "p1:CitizenNameSurname") "cs:Justice")
                                    ,XMLFOREST( XMLFOREST(xsol.JP2 AS "p1:CitizenNameSurname") "cs:Justice")
                                    ,XMLFOREST( XMLFOREST(xsol.JP3 AS "p1:CitizenNameSurname") "cs:Justice")
                                    ,XMLFOREST( XMLFOREST(xsol.JP4 AS "p1:CitizenNameSurname") "cs:Justice")
                                    )--judiciary
                                ,XMLELEMENT("cs:Hearings", (get_hearings_on_sitting(xsol.SITTING_ON_LIST_ID) ) )--Hearings                   
               )) INTO sittings 
               FROM XHB_SITTING_ON_LIST xsol, XHB_COURT_ROOM xcr, XHB_REF_JUDGE xrj
                    WHERE xsol.LIST_ID = p_list_id
                    AND (xsol.obs_ind is null OR xsol.obs_ind <> 'Y')
                    AND xsol.COURT_SITE_ID = p_courtsite_id
                    AND xsol.COURT_ROOM_ID = xcr.COURT_ROOM_ID
                    AND (xcr.obs_ind is null OR xcr.obs_ind <> 'Y')
                    AND xsol.JUDGE_REF_ID = xrj.REF_JUDGE_ID(+)
                    AND (xrj.obs_ind is null OR xrj.obs_ind <> 'Y');
             RETURN sittings;       
  END get_sittings_in_courtsite;


FUNCTION get_floating_cases(p_list_id XHB_LIST.LIST_ID%TYPE, p_court_site_id XHB_COURT_SITE.COURT_SITE_ID%TYPE) RETURN XMLType IS
  floater_cases XMLType;
  BEGIN
  SELECT (XMLELEMENT("cs:Sitting", 
             XMLFOREST(99 AS "cs:CourtRoomNumber"
                      ,1 AS "cs:SittingSequenceNo"
                      ,'F' as "cs:SittingPriority"
                      ,'FLOATERS - COURT TO BE ALLOCATED' as "cs:SittingNote")
                      ,XMLELEMENT("cs:Judiciary",
                                  XMLELEMENT("cs:Judge",XMLFOREST('N&#47;A' AS "p1:CitizenNameSurname"
                                                                  , 'N&#47;A' AS "p1:CitizenNameRequestedName"
                                                                  , 0 AS "cs:CRESTjudgeID" )
                                            )--judge
                                )--judiciary
                      ,XMLELEMENT("cs:Hearings",
                         (SELECT XMLAgg(XMLELEMENT("cs:Hearing"
                                                   ,XMLFOREST(xcol.SEQ_NO AS "cs:HearingSequenceNumber")
                                                   ,XMLELEMENT("cs:HearingDetails",XMLATTRIBUTES(xrht.hearing_type_code AS "HearingType")
                                                                                  ,XMLFOREST(xrht.hearing_type_desc AS "cs:HearingDescription"
                                                                                             ,NVL(xcol.TIME_LISTED,'') AS "cs:HearingDate" 
                                                                                             ,xcol.LIST_NOTE_TEXT AS "cs:ListNote"
                                                                                            )
                                                              )--HearingDetails                            
                                                 -- ,XMLFOREST('CRESTHearingID' AS "CRESTHearingID")
                                                  ,XMLFOREST(xrsc.DE_CODE AS "cs:TimeMarkingNote"
                                                  ,xc.CASE_TYPE || xc.CASE_NUMBER AS "cs:CaseNumber")
                                                  ,XMLFOREST(
                                                                -- ,XMLFOREST('ProsecutingReference' AS "ProsecutingReference")
                                                                XMLFOREST( XMLFOREST(xrpa.CPS_CODE AS "cs:OrganisationCode"
                                                                                       , xrpa.PROSECUTOR_NAME_1 || ' ' || xrpa.PROSECUTOR_NAME_2 || ' ' || xrpa.PROSECUTOR_NAME_3 AS "cs:OrganisationName"
                                                                                       , XMLFOREST(prosecutor_address.address_1 AS "p1:Line"
                                                                                                  ,prosecutor_address.address_2 AS "p1:Line"
                                                                                                  ,prosecutor_address.address_3 AS "p1:Line"
                                                                                                  ,prosecutor_address.postcode AS "p1:PostCode"
                                                                                                  ) "cs:OrganisationAddress"
                                                                                        ,xrpa.DX_REF AS "cs:OrganisationDX"
                                                                                        ,  get_contact_details(prosecutor_address.ADDRESS_ID) "cs:ContactDetails"
                                                                                        ) "cs:ProsecutingOrganisation"  
                                                                                  )"cs:Prosecution"
                                                          ) 
--                                                                                          ,XMLELEMENT("CommittingCourt",XMLFOREST(xcrt.court_type AS "CourtHouseType")
--                                                                                                                       ,XMLELEMENT("CourtHouseCode",XMLATTRIBUTES(xcrt.short_name AS "CourtHouseShortName"))
--                                                                                                                       ,XMLELEMENT("CourtHouseName",xcrt.court_name)
--                                                                                               ,XMLELEMENT("CourtHouseAddress", XMLFOREST(xadc.address_1 AS "Line"
--                                                                                                                                         ,xadc.address_2 AS "Line"
--                                                                                                                                         ,xadc.address_3 AS "Line"
--                                                                                                                                         ,xadc.postcode AS "PostCode")
--                                                                                                          )--CourtHouseAddress
--                                                                                              ,XMLELEMENT("CourtHouseTelephone",NVL(xcdp.contact_value,'')) 
--                                                                                                      )--CommittingCourt
                                              ,XMLFOREST(xcol.list_note_text AS "cs:ListNote")
                                              ,XMLFOREST(xc.NO_DEFENDANTS_FOR_CASE AS "cs:NumberOfDefendants")
                                              ,XMLELEMENT("cs:Defendants",
                                                           (get_defendants_on_hearing(xcol.CASE_ON_LIST_ID))--Defendant
                                                )--Defendants
                                                --Respondent
                                )) 
                                   FROM XHB_CASE_ON_LIST xcol ,XHB_REF_HEARING_TYPE xrht, XHB_REF_SYSTEM_CODE xrsc,
                                        XHB_CASE xc, XHB_CASE_PROSECUTOR_AGENCY xcpa, XHB_REF_PROSECUTOR_AGENCY xrpa,
                                        XHB_ADDRESS prosecutor_address  
                                    WHERE xcol.LIST_ID = p_list_id
                                    AND   xcol.SITTING_ON_LIST_ID IS NULL
                                    AND   xcol.FLOATER_CASE = 'Y'
                                    AND   xcol.COURT_SITE_ID = p_court_site_id 
                                    AND   (xcol.obs_ind is null or xcol.obs_ind <> 'Y')
                                    AND   xcol.HEARING_TYPE_ID = xrht.ref_hearing_type_id
                                    AND   (xrht.obs_ind is null or xrht.obs_ind <> 'Y')
                                    AND   xcol.TIME_MARKING_ID =  xrsc.REF_SYSTEM_CODE_ID (+)
                                    AND   xc.CASE_ID = xcol.CASE_ID                                                                                    
                                    AND  xc.CASE_ID = xcpa.CASE_ID (+) 
                                    AND  xcpa.ref_prosecutor_agency_id = xrpa.ref_prosecutor_agency_id (+)
                                    AND  xrpa.ADDRESS_ID = prosecutor_address.ADDRESS_ID (+) )--Hearing
                                   )--Hearings   
                        )--Sitting
              )       
          INTO floater_cases
          FROM DUAL
         WHERE EXISTS(SELECT 'X' FROM XHB_CASE_ON_LIST xcol
                                WHERE xcol.LIST_ID = p_list_id
                                AND   xcol.SITTING_ON_LIST_ID IS NULL
                                AND   xcol.FLOATER_CASE = 'Y'
                                AND   xcol.COURT_SITE_ID = p_court_site_id 
                                AND   (xcol.obs_ind is null or xcol.obs_ind <> 'Y'));
  
      RETURN floater_cases;
  END get_floating_cases;

  FUNCTION to_xml_date_format(p_date DATE) RETURN VARCHAR2 IS
  BEGIN
  RETURN CASE WHEN p_date IS NOT NULL THEN
          to_char( p_date, 'yyyy-mm-dd') || 'T' || to_char(p_date, 'hh:mi:ss')
          ELSE NULL END;
  END to_xml_date_format;


END XHB_GET_XML_REPORTS;
/
show errors
