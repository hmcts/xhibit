UPDATE XHB_REF_DISPOSAL_MENU xrdm
 SET TITLE='Serious Terrorism Sentence'
 WHERE xrdm.disposal_code = 'STS'
   AND xrdm.TITLE='Serious Terrorism Sentencing';
   
UPDATE XHB_REF_DISPOSAL_TYPE xrdt
 SET TITLE='Serious Terrorism Sentence'
 WHERE xrdt.disposal_code = 'STS'
   AND xrdt.TITLE='Serious Terrorism Sentencing';
   
COMMIT;
/