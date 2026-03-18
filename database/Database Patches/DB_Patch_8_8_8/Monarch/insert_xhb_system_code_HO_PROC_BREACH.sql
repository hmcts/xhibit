INSERT INTO xhb_ref_system_code (code, code_type, code_title, de_code, court_id, obs_ind)
SELECT x.code, x.code_type, x.code_title, x.de_code, xc.court_id, 'N' obs_ind FROM 
(SELECT '97' code, 'HO_PROC_BREACH' code_type, 'HO_PROCEEDINGS_CODE' code_title, 'Application to amend CO/SSO' de_code FROM DUAL) x,
(SELECT DISTINCT court_id FROM xhb_ref_system_code xrsc
  WHERE xrsc.code_type = 'HO_PROC_BREACH' AND xrsc.code_title = 'HO_PROCEEDINGS_CODE' AND xrsc.obs_ind = 'N' 
  ORDER BY xrsc.court_id) xc
WHERE NOT EXISTS 
(SELECT 1 FROM xhb_ref_system_code x2 
  WHERE x2.code = x.code AND x2.code_type = x.code_type AND x2.de_code = x.de_code AND x2.court_id = xc.court_id);
	
COMMIT;
/