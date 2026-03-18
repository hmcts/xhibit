INSERT INTO XHB_REF_DISPOSAL_MENU 
  (REF_DISPOSAL_MENU_ID,
  COURT_ID,
  MENU_ITEM_ID,
  ABBREV,
  SEQ_NO,
  MENU_GROUP,
  OBS_IND,
  PARENT,
  DISPOSAL_CODE,
  TITLE)
SELECT 
  XHB_REF_DISPOSAL_MENU_SEQ.NEXTVAL REF_DISPOSAL_MENU_ID,
  XC.COURT_ID,
  NVL((SELECT MAX(MENU_ITEM_ID) FROM XHB_REF_DISPOSAL_MENU),0)+1 MENU_ITEM_ID,
  subqry.DISPOSAL_CODE ABBREV,
  subqry.NEXT_SEQ_NO SEQ_NO,
  'RS' MENU_GROUP,
  'N' OBS_IND,
  subqry.PARENT,
  subqry.DISPOSAL_CODE,
  subqry.TITLE
FROM XHB_COURT XC, 
      (SELECT xrdm.PARENT,
              'EXD1820' DISPOSAL_CODE,
              '(Extended Discretional 18 to 20)' TITLE,
              NVL(MAX(xrdm.SEQ_NO),0)+1 NEXT_SEQ_NO 
         FROM XHB_REF_DISPOSAL_MENU xrdm 
        WHERE xrdm.PARENT = 2650 -- 'DANOF'
     GROUP BY xrdm.PARENT) subqry
WHERE NVL(XC.OBS_IND,'N') = 'N'
AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_MENU xrdm2 
                 WHERE xrdm2.DISPOSAL_CODE = subqry.DISPOSAL_CODE 
                   AND xrdm2.COURT_ID = xc.COURT_ID );
                   
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
1 TEMPLATE_VERSION,
xrdm.DISPOSAL_CODE,
'RS' MENU_GROUP,
xrdm.COURT_ID,
'N' OBS_IND,
0 CATEGORY,
24 LINE_AVAIL,
NULL DISP_TITLE2,
NULL DISP_TITLE1,
xrdm.TITLE
FROM XHB_REF_DISPOSAL_MENU xrdm
WHERE xrdm.DISPOSAL_CODE = 'EXD1820'
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_TYPE xrdt2 
                   WHERE xrdt2.DISPOSAL_CODE = xrdm.DISPOSAL_CODE 
                     AND xrdt2.COURT_ID = xrdm.COURT_ID);
                   
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
  1 TEMPLATE_VERSION,
  subqry.DIL_SEQ_NO,
  xrdm.COURT_ID,
  subqry.LINE_INSERT,
  subqry.MULTIPLE_CHOICE,
  subqry.MANDATORY,
  subqry.FORM_PRINT,
  subqry.SCREEN_PRINT,
  subqry.INPUT_FLAG,
  'N' OBS_IND,
  subqry.VALIDATION,
  subqry.DBSOURCE,
  subqry.CONC_FLAG,
  NULL FORMAT,
  subqry.DATA,
  subqry.DBDESTIN,
  NULL CHAR_MAX,
  NULL MCGROUP2,
  NULL MCGROUP1,
  subqry.PROMPT
FROM XHB_REF_DISPOSAL_MENU xrdm, 
     (SELECT 10 DIL_SEQ_NO,
             'Result Date' PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             NULL CONC_FLAG,
             'Y' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D2' DBDESTIN,
             'UD1' DBSOURCE,
             'V6' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 20 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             ' - ',
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'N' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 21 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'The court ordered that the defendant be sentenced under',
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
             NULL PROMPT,
             'N' INPUT_FLAG,
             'The court ordered that the defendant be sentenced to detention under',
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'N' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 23 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'section 266 Sentencing Act 2020 to an extended sentence',
             NULL CONC_FLAG,
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
      SELECT 23 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'to serve a period of imprisonment as follows: '||
             'An extended sentence of',
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'N' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 30 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             '*************Delete if not applicable:************************' DATA,
             'N' CONC_FLAG,
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
      SELECT 40 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'imprisonment' DATA,
             'N' CONC_FLAG,
             'N' MANDATORY,
             'Y' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL
       UNION       
      SELECT 45 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             '*****************************************************************************' DATA,
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
      SELECT 50 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'detention' DATA,
             'N' CONC_FLAG,
             'N' MANDATORY,
             'Y' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL
       UNION       
      SELECT 55 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             '*****************************************************************************' DATA,
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
      SELECT 60 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'of' DATA,
             'N' CONC_FLAG,
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
      SELECT 70 DIL_SEQ_NO,
             '     Duration:' PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             'Y' CONC_FLAG,
             'Y' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D5' DBDESTIN,
             NULL DBSOURCE,
             'V22' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 80 DIL_SEQ_NO,
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
       SELECT 90 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'comprising a custodial term of' DATA,
             'N' CONC_FLAG,
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
      SELECT 100 DIL_SEQ_NO,
             '     Duration:' PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             'Y' CONC_FLAG,
             'Y' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D5' DBDESTIN,
             NULL DBSOURCE,
             'V22' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 110 DIL_SEQ_NO,
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
      SELECT 120 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'and an extension period of' DATA,
             'N' CONC_FLAG,
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
      SELECT 130 DIL_SEQ_NO,
             '     Duration:' PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             'Y' CONC_FLAG,
             'Y' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D5' DBDESTIN,
             NULL DBSOURCE,
             'V22' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 140 DIL_SEQ_NO,
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
      SELECT 150 DIL_SEQ_NO,
             '    Effective:' PROMPT,
             'Y' INPUT_FLAG,
             'N' DATA,
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             'V2' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 160 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             '(Enter consecutive/concurrent):' DATA,
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'Y' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL
        UNION
      SELECT 170 DIL_SEQ_NO,
             ' Replaced:' PROMPT,
             'Y' INPUT_FLAG,
             'N' DATA,
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             'D11' DBDESTIN,
             NULL DBSOURCE,
             'V2' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL
       UNION
      SELECT 180 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             '(Enter additional text):' DATA,
             NULL CONC_FLAG,
             'N' MANDATORY,
             'N' MULTIPLE_CHOICE,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'Y' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'N' FORM_PRINT
        FROM DUAL
        ) subqry
WHERE xrdm.DISPOSAL_CODE = 'EXD1820'
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_LINE xrdl2 
                   WHERE xrdl2.DISPOSAL_CODE = xrdm.DISPOSAL_CODE 
                     AND xrdl2.COURT_ID = xrdm.COURT_ID
                     AND NVL(xrdl2.DATA,'~') = NVL(subqry.DATA,'~')
                     AND NVL(xrdl2.PROMPT,'~') = NVL(subqry.PROMPT,'~'));

COMMIT;
/