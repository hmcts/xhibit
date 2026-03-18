UPDATE  XHB_REF_APP_RES_D20_MAP xrardm SET xrardm.D20_RESULT = 'Allowed'
WHERE xrardm.APP_RESULT_CODE = 'ACD'
AND xrardm.D20_RESULT = 'Dismissed';

commit;