UPDATE XHB_REF_DISPOSAL_MENU
SET TITLE = 'Imprisonment - (Extended)'
WHERE disposal_code = 'IMPE'
AND TITLE != 'Imprisonment - (Extended)';

                   
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
'Imprisonment - (Extended)' TITLE
FROM XHB_REF_DISPOSAL_TYPE xrdt
WHERE xrdt.DISPOSAL_CODE = 'IMPE'
  AND xrdt.TEMPLATE_VERSION = 2
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
  xrdt.DISPOSAL_CODE,
  xrdt.TEMPLATE_VERSION,
  subqry.DIL_SEQ_NO,
  xrdt.COURT_ID,
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
FROM XHB_REF_DISPOSAL_TYPE xrdt, 
     (SELECT 20 DIL_SEQ_NO,
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
      SELECT 25 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'Extended under s278 of the Sentencing Act 2020.',
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
             'The court ordered that the defendant be sentenced to imprisonment',
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
      SELECT 35 DIL_SEQ_NO,
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
             'under section 236a Criminal Justice Act 2003' DATA,
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
      SELECT 44 DIL_SEQ_NO,
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
      SELECT 45 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'under section 278 Sentencing Act 2020' DATA,
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
      SELECT 49 DIL_SEQ_NO,
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
             'to a Special Custodial Sentence of',
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
      SELECT 60 DIL_SEQ_NO,
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
      SELECT 100 DIL_SEQ_NO,
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
      SELECT 140 DIL_SEQ_NO,
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
      SELECT 150 DIL_SEQ_NO,
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
      SELECT 190 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'and an extended licence period of 1 year' DATA,
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
      SELECT 200 DIL_SEQ_NO,
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
      SELECT 240 DIL_SEQ_NO,
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
      SELECT 250 DIL_SEQ_NO,
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
      SELECT 260 DIL_SEQ_NO,
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
WHERE xrdt.DISPOSAL_CODE = 'IMPE'
  AND xrdt.template_version = 3
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_LINE xrdl2 
                   WHERE xrdl2.DISPOSAL_CODE = xrdt.DISPOSAL_CODE 
                     AND xrdl2.COURT_ID = xrdt.COURT_ID
                     AND xrdl2.TEMPLATE_VERSION = xrdt.TEMPLATE_VERSION
                     AND NVL(xrdl2.DATA,'~') = NVL(subqry.DATA,'~')
                     AND NVL(xrdl2.PROMPT,'~') = NVL(subqry.PROMPT,'~'));

COMMIT;
/