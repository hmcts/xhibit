UPDATE XHB_REF_DISPOSAL_LINE xrdl
   SET xrdl.data = 'to a Special Custodial Sentence of'
 WHERE xrdl.disposal_code = 'DETTO'
   AND xrdl.data = 'to a special custodial sentence of';
COMMIT;
/