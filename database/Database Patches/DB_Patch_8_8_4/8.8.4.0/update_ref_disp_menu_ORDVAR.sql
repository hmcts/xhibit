INSERT INTO XHB_REF_DISPOSAL_TYPE
(REF_DISPOSAL_TYPE_ID,
TEMPLATE_VERSION,
DISPOSAL_CODE,
MENU_GROUP,
COURT_ID,
OBS_IND,
CATEGORY,
LINE_AVAIL,
DISP_TITLE2,
DISP_TITLE1,
TITLE)
SELECT 
XHB_REF_DISPOSAL_TYPE_SEQ.NEXTVAL REF_DISPOSAL_TYPE_ID,
xrdt.TEMPLATE_VERSION+1 TEMPLATE_VERSION,
xrdt.DISPOSAL_CODE,
xrdt.MENU_GROUP,
xrdt.COURT_ID,
'N' OBS_IND,
xrdt.CATEGORY,
xrdt.LINE_AVAIL,
xrdt.DISP_TITLE2,
xrdt.DISP_TITLE1,
xrdt.TITLE
FROM XHB_REF_DISPOSAL_TYPE xrdt,
     (SELECT 'ORDVAR' DISPOSAL_CODE, 
             4 TEMPLATE_VERSION 
        FROM DUAL) subqry
WHERE xrdt.DISPOSAL_CODE = subqry.DISPOSAL_CODE
  AND xrdt.TEMPLATE_VERSION = subqry.TEMPLATE_VERSION-1
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_TYPE xrdt2 
                   WHERE xrdt2.DISPOSAL_CODE = xrdt.DISPOSAL_CODE 
                     AND xrdt2.COURT_ID = xrdt.COURT_ID
                     AND xrdt2.TEMPLATE_VERSION = xrdt.TEMPLATE_VERSION+1);

INSERT INTO XHB_REF_DISPOSAL_LINE
  (REF_DISPOSAL_LINE_ID,
  DISPOSAL_CODE,
  TEMPLATE_VERSION,
  DIL_SEQ_NO,
  COURT_ID,
  LINE_INSERT,
  MULTIPLE_CHOICE,
  MANDATORY,
  FORM_PRINT,
  SCREEN_PRINT,
  INPUT_FLAG,
  OBS_IND,
  VALIDATION,
  DBSOURCE,
  CONC_FLAG,
  FORMAT,
  DATA,
  DBDESTIN,
  CHAR_MAX,
  MCGROUP2,
  MCGROUP1,
  PROMPT)
SELECT
  XHB_REF_DISPOSAL_LINE_SEQ.NEXTVAL REF_DISPOSAL_LINE_ID,
  xrdl.DISPOSAL_CODE,
  subqry.TEMPLATE_VERSION,
  xrdl.DIL_SEQ_NO,
  xrdl.COURT_ID,
  xrdl.LINE_INSERT,
  CASE WHEN xrdl.DIL_SEQ_NO IN (40) THEN 'Y' ELSE xrdl.MULTIPLE_CHOICE END MULTIPLE_CHOICE,
  xrdl.MANDATORY,
  xrdl.FORM_PRINT,
  xrdl.SCREEN_PRINT,
  xrdl.INPUT_FLAG,
  'N' OBS_IND,
  xrdl.VALIDATION,
  xrdl.DBSOURCE,
  CASE WHEN xrdl.DIL_SEQ_NO IN (40) THEN 'Y' ELSE xrdl.CONC_FLAG END CONC_FLAG,
  xrdl.FORMAT,
  xrdl.DATA,
  xrdl.DBDESTIN,
  xrdl.CHAR_MAX,
  xrdl.MCGROUP2,
  CASE WHEN xrdl.DIL_SEQ_NO IN (40,60,80,100,160) THEN 'B' ELSE NULL END MCGROUP1,
  xrdl.PROMPT
FROM XHB_REF_DISPOSAL_LINE xrdl,
     (SELECT 'ORDVAR' DISPOSAL_CODE, 
             4 TEMPLATE_VERSION 
        FROM DUAL) subqry
WHERE xrdl.DISPOSAL_CODE = subqry.DISPOSAL_CODE
  AND xrdl.template_version = subqry.TEMPLATE_VERSION-1
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_LINE xrdl2 
                   WHERE xrdl2.DISPOSAL_CODE = subqry.DISPOSAL_CODE 
                     AND xrdl2.COURT_ID = xrdl.COURT_ID
                     AND xrdl2.TEMPLATE_VERSION = subqry.TEMPLATE_VERSION
                     AND xrdl2.DIL_SEQ_NO = xrdl.DIL_SEQ_NO);

INSERT INTO XHB_REF_DISPOSAL_LINE
  (REF_DISPOSAL_LINE_ID,
  DISPOSAL_CODE,
  TEMPLATE_VERSION,
  DIL_SEQ_NO,
  COURT_ID,
  LINE_INSERT,
  MULTIPLE_CHOICE,
  MANDATORY,
  FORM_PRINT,
  SCREEN_PRINT,
  INPUT_FLAG,
  OBS_IND,
  VALIDATION,
  DBSOURCE,
  CONC_FLAG,
  FORMAT,
  DATA,
  DBDESTIN,
  CHAR_MAX,
  MCGROUP2,
  MCGROUP1,
  PROMPT)
SELECT
  XHB_REF_DISPOSAL_LINE_SEQ.NEXTVAL REF_DISPOSAL_LINE_ID,
  xrdm.DISPOSAL_CODE,
  subqry.TEMPLATE_VERSION,
  newline.DIL_SEQ_NO,
  xrdm.COURT_ID,
  newline.LINE_INSERT,
  newline.MULTIPLE_CHOICE,
  newline.MANDATORY,
  newline.FORM_PRINT,
  newline.SCREEN_PRINT,
  newline.INPUT_FLAG,
  'N' OBS_IND,
  newline.VALIDATION,
  newline.DBSOURCE,
  newline.CONC_FLAG,
  NULL FORMAT,
  newline.DATA,
  newline.DBDESTIN,
  NULL CHAR_MAX,
  NULL MCGROUP2,
  newline.MCGROUP1,
  newline.PROMPT
FROM XHB_REF_DISPOSAL_MENU xrdm, 
     (SELECT 21 DIL_SEQ_NO,
             NULL MCGROUP1,
             NULL PROMPT,
             'N' INPUT_FLAG,
             '*************Delete where not applicable***********************' DATA,
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 22 DIL_SEQ_NO,
             'A' MCGROUP1,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'SSO to continue, the operation period of the order to be extended by' DATA,
             'Y' CONC_FLAG,
             'N' MANDATORY,
             'Y' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 25 DIL_SEQ_NO,
             'A' MCGROUP1,
             '     Duration:' PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             'Y' CONC_FLAG,
             'Y' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D5' DBDESTIN,
             NULL DBSOURCE,
             'V3' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 26 DIL_SEQ_NO,
             'A' MCGROUP1,
             '        Units:' PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             'Y' CONC_FLAG,
             'Y' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D6' DBDESTIN,
             NULL DBSOURCE,
             'V1' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 30 DIL_SEQ_NO,
             'A' MCGROUP1,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'to give a total operational period of' DATA,
             'Y' CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 32 DIL_SEQ_NO,
             'A' MCGROUP1,
             '     Duration:' PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             'Y' CONC_FLAG,
             'Y' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D5' DBDESTIN,
             NULL DBSOURCE,
             'V3' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 33 DIL_SEQ_NO,
             'A' MCGROUP1,
             '        Units:' PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             'Y' CONC_FLAG,
             'Y' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D6' DBDESTIN,
             NULL DBSOURCE,
             'V1' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION 
      SELECT 170 DIL_SEQ_NO,
             NULL MCGROUP1,
             NULL PROMPT,
             'N' INPUT_FLAG,
             '*****************************************************************' DATA,
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL) newline,
     (SELECT 'ORDVAR' DISPOSAL_CODE, 
             4 TEMPLATE_VERSION 
        FROM DUAL) subqry
WHERE xrdm.DISPOSAL_CODE = subqry.DISPOSAL_CODE
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_LINE xrdl2 
                   WHERE xrdl2.DISPOSAL_CODE = subqry.DISPOSAL_CODE 
                     AND xrdl2.COURT_ID = xrdm.COURT_ID
                     AND xrdl2.TEMPLATE_VERSION = subqry.TEMPLATE_VERSION
                     AND xrdl2.DIL_SEQ_NO = newline.DIL_SEQ_NO);

COMMIT;
/