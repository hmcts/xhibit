--Initial cursor query

SELECT xstgf.sof_id
    ,      xstgf.crest_court_id
    ,      xstgf.london_weighting
    ,      xstgf.la_code
    ,      xcrt.court_id
    FROM  xhbstg_solicitor_firm_dm   xstgf
    ,     xhibit.xhb_court xcrt
    WHERE xstgf.crest_court_id = 453--p_crest_court_id
    AND   xstgf.crest_court_id = xcrt.crest_court_id
    AND   xstgf.sof_id IS NOT NULL
    AND EXISTS (SELECT 'x' --only return rows that can be updated rather than return everything
                FROM  xhibit.xhb_ref_solicitor_firm   xrsf
                WHERE xcrt.court_id = xrsf.court_id 
                AND   xrsf.crest_sof_id = xstgf.sof_id
                AND   xrsf.la_code <> xstgf.la_code
                )
                ;
--query result (these 2 records should get updated

11061	453	N	d344k	81
11251	453	N	T8838	81

select la_code, crest_sof_id from xhibit.xhb_ref_solicitor_firm where crest_sof_id IN (11061,11251) and court_id = 81;
                
BWD	11061
1	11251

--After running the la_code field should be updated to the above dataset

DECLARE
 BEGIN
  DM_PROCESS_PKG_CC.UPDATE_XHB_RSS_WITH_CREST(p_crest_court_id => 453);
 END;

select la_code, crest_sof_id from xhibit.xhb_ref_solicitor_firm where crest_sof_id IN (11061,11251) and court_id = 81;

--The 2 records have been updated as expected
d344k	11061
T8838	11251