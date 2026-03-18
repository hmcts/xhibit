create or replace PACKAGE BODY xhb_orders_pkg AS
    FUNCTION get_ref_courts_by_court_Id (
                COURT_ID_IN IN XHB_REF_COURT.COURT_ID%TYPE) RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;

    BEGIN
        OPEN v_return_cursor FOR
            SELECT
                REF_COURT_ID,
                COURT_FULL_NAME,
                COURT_SHORT_NAME,
                NAME_PREFIX,
                COURT_TYPE,
                CREST_CODE,
                REF_COURT.OBS_IND AS REF_COURT_OBS_IND,
                IS_PSD,
                DX_REF,
                REF_COURT.LAST_UPDATE_DATE AS COURT_LAST_UPDATE_DATE,
                REF_COURT.CREATION_DATE AS COURT_CREATION_DATE,
                REF_COURT.CREATED_BY AS COURT_CREATED_BY,
                REF_COURT.LAST_UPDATED_BY AS COURT_LAST_UPDATED_BY,
                REF_COURT.VERSION AS COURT_VERSION,
                REF_COURT.ADDRESS_ID AS COURT_ADDRESS_ID,
                COURT_ID,
                ADDRESS.ADDRESS_ID,
                ADDRESS_1,
                ADDRESS_2,
                ADDRESS_3,
                ADDRESS_4,
                TOWN,
                COUNTY,
                POSTCODE,
                COUNTRY,
                ADDRESS.LAST_UPDATE_DATE AS ADDRESS_LAST_UPDATE_DATE,
                ADDRESS.CREATION_DATE AS ADDRESS_CREATION_DATE,
                ADDRESS.CREATED_BY AS ADDRESS_CREATED_BY,
                ADDRESS.LAST_UPDATED_BY AS ADDRESS_LAST_UPDATED_BY,
                ADDRESS.VERSION AS ADDRESS_VERSION
            FROM  XHB_REF_COURT REF_COURT, XHB_ADDRESS ADDRESS
            WHERE REF_COURT.COURT_ID = COURT_ID_IN
            AND (REF_COURT.OBS_IND IS NULL OR REF_COURT.OBS_IND = 'N')
            AND REF_COURT.ADDRESS_ID = ADDRESS.ADDRESS_ID(+);
            RETURN v_return_cursor;
    END get_ref_courts_by_court_Id;

FUNCTION get_appeal_result_order(p_case_id IN XHB_CASE.CASE_ID%TYPE) RETURN CLOB IS
v_return_xml CLOB;
BEGIN
      SELECT
      XMLELEMENT("AppealResultOrder",
                  XMLFOREST(xc.CASE_TYPE || xc.CASE_NUMBER AS "CaseNumber",
                  INITCAP(xco.COURT_NAME) AS "CourtName",
                  xco.CREST_COURT_ID AS "CourtCode",
                  xdoc.PTIURN,
                  INITCAP(xd.first_name || ' ' || xd.middle_name || ' ' || xd.surname) AS "DefendantName",
                  XHB_PUBLIC_REP_PKG.get_address(xd.address_id) AS "DefendantAddress",
                  INITCAP(TO_CHAR( xd.DATE_OF_BIRTH, 'dd FMMONTH yyyy')) AS "DateOfBirth",
                  INITCAP(TO_CHAR(nvl(xc.MAG_CONVICTION_DATE, xc.ORIG_BODY_DECISION_DATE), 'dd FMMONTH yyyy')) AS "MagConvictionDate",
                  INITCAP(xrc.COURT_FULL_NAME) AS "MagsCourtName",
                  INITCAP(TO_CHAR(xv_main_verdict.VERDICT_DATE, 'dd FMMONTH yyyy')) AS "VerdictDate",
                  xrsc_main_verdict.CODE AS "VerdictCode",
				  INITCAP(TO_CHAR(SYSDATE, 'dd FMMONTH yyyy')) AS "TodaysDate",
                  (SELECT XMLAGG(XMLELEMENT("Offence",
                                      XMLELEMENT("OffenceDescription", xro.OFFENCE_DESC || ' '  || xro.OFFENCE_DESC2 ),

                                      (SELECT XMLAGG(XMLELEMENT("OriginalSentence", get_disposal_lines(original_xdisp.DISPOSAL2_ID)))
                                       FROM  XHB_DISPOSAL2 original_xdisp
                                       WHERE original_xdisp.DEFENDANT_ON_OFFENCE_ID = xdoo.DEFENDANT_ON_OFFENCE_ID
                                          AND original_xdisp.COURT_TYPE = 'M'
                                          AND NVL(original_xdisp.OBS_IND, '-') <> 'Y'),

                                      CASE WHEN xrsc_main_verdict.CODE = 'AB' OR xrsc_main_verdict.CODE = 'AC'
                                        THEN XMLELEMENT("AppealResultCode", xrsc_main_verdict.CODE)
                                        ELSE XMLELEMENT("AppealResults",
                                                        XMLELEMENT("AppealResultDescription", xrar.APP_RESULT_DESCR1 || xrar.APP_RESULT_DESCR2 || xv.APP_LESSER_OFF),
                                                        (SELECT XMLAGG(XMLELEMENT("AppealResultSentence",
                                                                    get_disposal_lines(result_xdisp.DISPOSAL2_ID)))
                                                        FROM XHB_DISPOSAL2 result_xdisp
                                                        WHERE result_xdisp.DEFENDANT_ON_OFFENCE_ID = xdoo.DEFENDANT_ON_OFFENCE_ID
                                                        AND result_xdisp.COURT_TYPE = 'C')) END
                                ))--Offence
                  FROM XHB_CHARGE xch
                  JOIN XHB_OFFENCE xo ON xo.CHARGE_ID = xch.CHARGE_ID AND NVL(xo.OBS_IND, '-') <> 'Y'
                  JOIN XHB_REF_OFFENCE xro ON xro.REF_OFFENCE_ID = xo.REF_OFFENCE_ID
                  JOIN XHB_DEFENDANT_ON_OFFENCE xdoo ON xdoo.OFFENCE_ID = xo.OFFENCE_ID AND NVL(xdoo.OBS_IND, '-') <> 'Y'
                  LEFT OUTER JOIN XHB_VERDICT xv ON xv.DEFENDANT_ON_OFFENCE_ID = xdoo.DEFENDANT_ON_OFFENCE_ID  AND NVL(xv.OBS_IND, '-') <> 'Y'
                  LEFT OUTER JOIN XHB_REF_APP_RESULT xrar ON xrar.REF_APP_RESULT_ID = xv.REF_APP_RESULT_ID
                  WHERE xch.CASE_ID = xc.CASE_ID
                  AND NVL(xch.OBS_IND, '-') <> 'Y') AS "Offences",

                  (SELECT XMLAGG(XMLELEMENT("OtherOrder",
                                            XMLATTRIBUTES(xdisp.COURT_TYPE AS "CourtType"),
                                            get_disposal_lines(xdisp.DISPOSAL2_ID)))
                        FROM XHB_DISPOSAL2 xdisp
                        WHERE xdisp.DEFENDANT_ON_CASE_ID = xdoc.DEFENDANT_ON_CASE_ID
                        AND NVL(xdisp.OBS_IND, '-') <> 'Y'
                    )"OtherOrders")
        ).getclobval() INTO v_return_xml
      FROM XHB_CASE xc
      JOIN XHB_COURT xco ON xco.COURT_ID = xc.COURT_ID
      JOIN XHB_DEFENDANT_ON_CASE xdoc ON xdoc.CASE_ID = xc.CASE_ID AND NVL(xdoc.OBS_IND, '-') <> 'Y'
      JOIN XHB_DEFENDANT xd ON xd.DEFENDANT_ID = xdoc.DEFENDANT_ID
      LEFT OUTER JOIN XHB_REF_COURT xrc ON xrc.REF_COURT_ID = xc.REF_COURT_ID
      JOIN XHB_VERDICT xv_main_verdict ON xv_main_verdict.CASE_ID = xc.CASE_ID
          AND xv_main_verdict.DEF_ON_CHARGE_OR_OFFENCE = 'A'
          AND NVL(xv_main_verdict.OBS_IND, '-') <> 'Y'
      JOIN XHB_REF_SYSTEM_CODE xrsc_main_verdict ON xrsc_main_verdict.REF_SYSTEM_CODE_ID =  xv_main_verdict.REF_VERDICT_ID
      WHERE xc.CASE_ID = p_case_id;

      RETURN v_return_xml;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
		WHEN OTHERS THEN
			INSERT INTO XHB_REPORT_ERR_LOG(REPORTS_LOG_ID, error_message, run_date)
			VALUES (XHB_REPORTS_ERROR_LOG_SEQ.NEXTVAL, 'Exception handler raised when others in XHB_ORDERS_PKG.get_appeal_result_order ' || ' : '  , SYSDATE);
			RAISE;
END get_appeal_result_order;

FUNCTION get_disposal_lines(p_disposal_id XHB_DISPOSAL2.DISPOSAL2_ID%TYPE) RETURN VARCHAR2 IS
v_disposal_lines VARCHAR2(10000);

 CURSOR get_disposal_c
 IS
  SELECT  NVL(format_disposal_line(xhb_disp_line.DATA,ref_data_view.VALIDATION), NVL(format_disposal_line(ref_data_view.DATA, ref_data_view.VALIDATION), '')) as dataLine
  FROM
        (SELECT   x_disp.DISPOSAL2_ID, xrd_line.REF_DISPOSAL_LINE_ID,   xrd_line.DATA,
        xrd_line.PROMPT, xrd_line.FORM_PRINT, xrd_line.LINE_INSERT, xrd_line.DIL_SEQ_NO, xrd_line.DBDESTIN, xrd_line.Validation    FROM
               ( SELECT * FROM XHB_REF_DISPOSAL_LINE WHERE NVL(OBS_IND,'N') <> 'Y') xrd_line,
               ( SELECT * FROM XHB_REF_DISPOSAL_TYPE WHERE NVL(OBS_IND,'N') <> 'Y') xrd_type,
               ( SELECT * FROM XHB_DISPOSAL2         WHERE NVL(OBS_IND,'N') <> 'Y') x_disp
        WHERE   xrd_type.TEMPLATE_VERSION = xrd_line.TEMPLATE_VERSION
        AND     xrd_type.DISPOSAL_CODE = xrd_line.DISPOSAL_CODE
        AND     x_disp.REF_DISPOSAL_TYPE_ID = xrd_type.REF_DISPOSAL_TYPE_ID
        AND     xrd_type.COURT_ID = xrd_line.COURT_ID
        AND     x_disp.DISPOSAL2_ID = p_disposal_id
        ORDER BY xrd_line.DIL_SEQ_NO  ) ref_data_view,
        (SELECT * FROM XHB_DISPOSAL_LINE WHERE NVL(OBS_IND,'N') != 'Y') xhb_disp_line
  WHERE xhb_disp_line.DISPOSAL2_ID(+) = ref_data_view.DISPOSAL2_ID
  AND   xhb_disp_line.REF_DISPOSAL_LINE_ID(+) = ref_data_view.REF_DISPOSAL_LINE_ID
  AND   xhb_disp_line.DEL_G1 IS NULL AND xhb_disp_line.DEL_G2 IS NULL
  AND ( ref_data_view.FORM_PRINT = 'Y'
    OR  (ref_data_view.LINE_INSERT='Y' AND xhb_disp_line.DATA IS NOT NULL and nvl(ref_data_view.PROMPT, 'XX') != 'Date of Result')
    OR  (ref_data_view.DBDESTIN = 'D15' AND xhb_disp_line.DATA = 'Y')
    OR  (ref_data_view.DBDESTIN = 'D11' AND xhb_disp_line.DATA = 'Y')   )
   AND  NVL(xhb_disp_line.DATA, NVL(ref_data_view.DATA, 'XXXX')) <> 'XXXX'
   ORDER BY  ref_data_view.DISPOSAL2_ID, DECODE(ref_data_view.DBDESTIN,'D11', 99999999, 'D15', 99999999, ref_data_view.DIL_SEQ_NO);
BEGIN
  FOR get_disposal_row IN get_disposal_c
   LOOP
       v_disposal_lines := v_disposal_lines|| chr(32) || get_disposal_row.dataLine; --For each loop iteration concatenate the disposals
    END LOOP;

  RETURN v_disposal_lines;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
    RETURN NULL;
		WHEN OTHERS THEN
    RAISE;

END get_disposal_lines;

FUNCTION format_disposal_line(p_data VARCHAR2, p_val XHB_REF_DISPOSAL_LINE.VALIDATION%TYPE) RETURN VARCHAR2 IS
v_disposal_lines VARCHAR2(240);
BEGIN
IF p_val = 'V4' THEN
RETURN to_char(p_data,'L9,999,999,999');
ELSE
RETURN p_data;
END IF;
END format_disposal_line;

END xhb_orders_pkg;
/
show errors
