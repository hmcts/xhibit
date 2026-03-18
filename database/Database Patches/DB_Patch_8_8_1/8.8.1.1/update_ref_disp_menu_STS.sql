DELETE XHB_REF_DISPOSAL_LINE xrdl
 WHERE xrdl.disposal_code = 'STS'
   AND xrdl.data = ' - ';
COMMIT;
/