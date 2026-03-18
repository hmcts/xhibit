create or replace PROCEDURE insert_ref_disp_line_trail(p_disposal_code IN xhb_ref_disposal_line.disposal_code%TYPE, p_dil_seq_1 IN xhb_ref_disposal_line.dil_seq_no%TYPE, p_dil_seq_2 IN xhb_ref_disposal_line.dil_seq_no%TYPE, p_version_no IN xhb_ref_disposal_line.template_version%TYPE) AS

/**
  * CGI crest to xhibit program
  *
  * MODULE      : insert_ref_disp_line
  *
  * DESCRIPTION : XLC - 77
  *
  * Procedure                    		Purpose
  * =========                   		 =======
  * insert_ref_disp_line			New entries in XHB_REF_DISPOSAL_LINE for new disposal
  **/

 BEGIN

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
  p_version_no TEMPLATE_VERSION,
  subqry.DIL_SEQ_NO,
  xrdm.COURT_ID,
  subqry.LINE_INSERT,
  'N' MULTIPLE_CHOICE,
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
     (SELECT p_dil_seq_1 DIL_SEQ_NO,
             NULL PROMPT,
             'N' INPUT_FLAG,
             'Trail Monitoring? (Y/N)' DATA,
             'N' CONC_FLAG,
             'N' MANDATORY,
             NULL DBDESTIN,
             NULL DBSOURCE,
             NULL VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL
       UNION
      SELECT p_dil_seq_2 DIL_SEQ_NO,
             null PROMPT,
             'Y' INPUT_FLAG,
             NULL DATA,
             'N' CONC_FLAG,
             'Y' MANDATORY,
             'D18' DBDESTIN,
             null DBSOURCE,
             'V2' VALIDATION,
             'N' LINE_INSERT,
             'Y' SCREEN_PRINT,
             'Y' FORM_PRINT
        FROM DUAL) subqry
WHERE xrdm.DISPOSAL_CODE = p_disposal_code
AND nvl(xrdm.obs_ind,'-') <> 'Y'
  AND NOT EXISTS (SELECT NULL FROM XHB_REF_DISPOSAL_LINE xrdl2 
                   WHERE xrdl2.DISPOSAL_CODE = xrdm.DISPOSAL_CODE 
                     AND xrdl2.COURT_ID = xrdm.COURT_ID
                     AND NVL(xrdl2.DIL_SEQ_NO,0) = NVL(subqry.DIL_SEQ_NO,0));

  


 END insert_ref_disp_line_trail;
/