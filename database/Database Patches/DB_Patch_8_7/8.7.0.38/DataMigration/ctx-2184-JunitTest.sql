--Initial cursor query

--Check the rows that are to be updated (the rows that are in the staging table and xhibit table
--9 rows returned for court 453
    SELECT xcpsf.sol_ref
    ,      xcpsf.cpf_id
    ,      xcpsf.crest_court_id
    FROM  xhbstg_case_party_sof_dm   xcpsf
    WHERE xcpsf.crest_court_id = 453
    AND xcpsf.sol_ref IS NOT NULL
    AND EXISTS (SELECT 'x'
                FROM  xhibit.xhb_prosecutor_ref_sol_firm   xprsf
                WHERE xcpsf.crest_court_id = 453
                AND xprsf.CREST_CPF_ID = xcpsf.cpf_id);
  

S002	      30534	453
asdfasdfas	30926	453
test	      30941	453
1234abcd	  30944	453
S002	      30532	453
S002	      30533	453
S002	      30535	453
S002	      30536	453
S002	      30538	453

--check the staging table rows to be updated
select sol_ref, cpf_id, crest_court_id from xhbstg_case_party_sof_dm where cpf_id IN (30534,30926,30941,30944,30532,30533,30535,30536,30538);

--result of staging table query
S002	      30534	453
asdfasdfas	30926	453
test	      30941	453
1234abcd	  30944	453
S002	      30532	453
S002	      30533	453
S002	      30535	453
S002	      30536	453
S002	      30538	453

--run the update script
DECLARE
BEGIN
 dm_process_pkg_cc.update_xhb_prsf_with_crest(p_crest_court_id => 453);
END;

commit;

--re-check the data after the update
--Check the rows that are to be updated
SELECT XPRSF.REF_SOLICITOR_FIRM_ID, XPRSF.SOLICITOR_REF, XPRSF.CREST_CPF_ID
FROM xhibit.xhb_prosecutor_ref_sol_firm   xprsf    
WHERE  1=1
AND  NVL(xprsf.obs_ind,'N')!='Y' 
AND   exists  (SELECT 'x'
              FROM  xhbstg_case_party_sof_dm   xcpsf
              WHERE xcpsf.crest_court_id = 453
              AND xprsf.CREST_CPF_ID = xcpsf.cpf_id)
;

--result after run
92291	  S002	    30534
92295		NULL      31035
91754		NULL      30884
91379	asdfasdfas	30926
92272		NULL      30933
92272	  test	30941
91754	1234abcd	30944
92295		NULL    31034
94964	S002	30532
92286	S002	30533
94236	S002	30535
92290	S002	30535
92290	S002	30536
92290	S002	30538


--Message after run 
CTX-2184:XHBSTG_CASE_PARTY_SOF_DM - Crest Court : 453 - Updated no of rows : 1
XHB_PROSECUTOR_REF_SOL_FIRM Updated for CREST_COURT_ID : 453 successfuly!
