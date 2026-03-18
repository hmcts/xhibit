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
D20_OTHER_SENTENCE,
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
xrdt.D20_OTHER_SENTENCE,
xrdt.TITLE
FROM XHB_REF_DISPOSAL_TYPE xrdt,
     (SELECT 'DET' DISPOSAL_CODE, 
             5 TEMPLATE_VERSION 
        FROM DUAL) subqry
WHERE xrdt.DISPOSAL_CODE = subqry.DISPOSAL_CODE
  AND xrdt.TEMPLATE_VERSION = subqry.TEMPLATE_VERSION-1
  AND NVL(obs_ind, 'N') = 'N'
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_TYPE xrdt2 
                   WHERE xrdt2.DISPOSAL_CODE = xrdt.DISPOSAL_CODE 
                     AND xrdt2.COURT_ID = xrdt.COURT_ID
                     AND xrdt2.TEMPLATE_VERSION = xrdt.TEMPLATE_VERSION+1);

--Updates title on disposal window
UPDATE XHB_REF_DISPOSAL_TYPE xrdt
SET xrdt.TITLE = 'Detention under s.91 PCC (Sentencing) Act 2000 / S250 CJA 2003' 
WHERE xrdt.DISPOSAL_CODE = 'DET'
AND xrdt.TEMPLATE_VERSION = 5
AND xrdt.TITLE != 'Detention under s.91 PCC (Sentencing) Act 2000 / S250 CJA 2003';

--Updates title on disposal
UPDATE XHB_REF_DISPOSAL xrd
SET xrd.DISPOSAL_TITLE = 'Detention under s.91 PCC (Sentencing) Act 2000 / S250 CJA 2003'
WHERE xrd.DISPOSAL_CODE = 'DET'
AND xrd.DISPOSAL_TITLE != 'Detention under s.91 PCC (Sentencing) Act 2000 / S250 CJA 2003';

--Updates title on disposal menu
UPDATE XHB_REF_DISPOSAL_MENU xrdm
SET xrdm.TITLE = 'Detention under s.91 PCC (Sentencing) Act 2000 / S250 CJA 2003'
WHERE xrdm.DISPOSAL_CODE = 'DET'
AND xrdm.TITLE != 'Detention under s.91 PCC (Sentencing) Act 2000 / S250 CJA 2003';


-- Copy existing lines 
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
  CASE WHEN xrdl.DIL_SEQ_NO = 80 THEN 'Y'
	ELSE xrdl.MULTIPLE_CHOICE END MULTIPLE_CHOICE,
  xrdl.MANDATORY,
  xrdl.FORM_PRINT,
  xrdl.SCREEN_PRINT,
  xrdl.INPUT_FLAG,
  'N' OBS_IND,
  xrdl.VALIDATION,
  xrdl.DBSOURCE,
  xrdl.CONC_FLAG,
  xrdl.FORMAT,
  xrdl.DATA || CASE WHEN DIL_SEQ_NO = 80 THEN ' Act 2000' ELSE '' END DATA,
  xrdl.DBDESTIN,
  xrdl.CHAR_MAX,
  xrdl.MCGROUP2,
  xrdl.MCGROUP1,
  CASE WHEN xrdl.DIL_SEQ_NO = 80 THEN ' '
	ELSE xrdl.PROMPT END PROMPT
FROM XHB_REF_DISPOSAL_LINE xrdl,
     (SELECT 'DET' DISPOSAL_CODE, 
             5 TEMPLATE_VERSION 
        FROM DUAL) subqry
WHERE xrdl.DISPOSAL_CODE = subqry.DISPOSAL_CODE
  AND xrdl.template_version = subqry.TEMPLATE_VERSION-1
  AND NVL(xrdl.obs_ind,'N') = 'N'
  AND xrdl.DIL_SEQ_NO NOT IN (90)
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_LINE xrdl2 
                   WHERE xrdl2.DISPOSAL_CODE = subqry.DISPOSAL_CODE 
                     AND xrdl2.COURT_ID = xrdl.COURT_ID
                     AND xrdl2.TEMPLATE_VERSION = subqry.TEMPLATE_VERSION
                     AND xrdl2.DIL_SEQ_NO = xrdl.DIL_SEQ_NO);


-- Insert new lines                     
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
  5 TEMPLATE_VERSION,
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
  subqry.MCGROUP1,
  subqry.PROMPT
FROM XHB_REF_DISPOSAL_MENU xrdm, 
     (SELECT 70 DIL_SEQ_NO,
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
	  SELECT 79 DIL_SEQ_NO,
             NULL MCGROUP1,
             ' ' PROMPT,
             'N' INPUT_FLAG,
             'detention under s.250 CJA 2003' DATA,
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
		SELECT 100 DIL_SEQ_NO,
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
        FROM DUAL
        ) subqry
WHERE xrdm.DISPOSAL_CODE = 'DET'
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_LINE xrdl2 
                   WHERE xrdl2.DISPOSAL_CODE = xrdm.DISPOSAL_CODE 
                     AND xrdl2.COURT_ID = xrdm.COURT_ID
                     AND NVL(xrdl2.DATA,'~') = NVL(subqry.DATA,'~')
                     AND NVL(xrdl2.PROMPT,'~') = NVL(subqry.PROMPT,'~'));
  
COMMIT;
/