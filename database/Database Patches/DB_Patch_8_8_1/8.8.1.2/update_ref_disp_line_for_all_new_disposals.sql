UPDATE XHB_REF_DISPOSAL_LINE xrdm
 SET xrdm.DATA='********** Delete where not appropriate: **********************'
 WHERE xrdm.disposal_code in ('STS','IMPE','EXD1820','EXDO21')
   AND xrdm.DATA='*************Delete if not applicable:************************';
  
COMMIT;
/