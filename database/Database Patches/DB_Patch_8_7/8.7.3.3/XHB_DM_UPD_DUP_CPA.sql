CREATE OR REPLACE PROCEDURE XHB_DM_UPD_DUP_CPA (p_xhibit_court_id IN xhibit.xhb_court.court_id%TYPE) as
/*     30/05/2019    S Sethuraman  - 
                     CTX-4252 - After every DM - Suppress Duplicate CASE_PROSECUTOR_AGENCY records 
                     which are created due to invalid CREST DATA
                     Duplicates are identified as having same 
                     Court_id,CASE_ID,prosecutor_type,REF_PRSECUTOR_AGENCY_ID
                     Records can be obsoleted by marking OBS_IND = 'Y' 
*/
CURSOR C1 IS
select a.rowid,a.case_id,a.prosecutor_type,a.ref_prosecutor_agency_id 
from xhibit.xhb_case_prosecutor_agency a
where a.rowid in
(
select min(b.rowid)
 from xhibit.xhb_case_prosecutor_agency b where b.case_id in
(select case_id from xhibit.xhb_case where court_id = p_xhibit_court_id)
and nvl(b.obs_ind,'N') != 'Y'
group by b.case_id,b.prosecutor_type,b.ref_prosecutor_agency_id
having count(*) > 1);

BEGIN

FOR i in C1 LOOP

    UPDATE  XHIBIT.XHB_CASE_PROSECUTOR_AGENCY
       SET OBS_IND = 'Y'
     WHERE ROWID = i.rowid AND
           CASE_ID = i.case_id AND
           PROSECUTOR_TYPE = i.prosecutor_type AND
           REF_PROSECUTOR_AGENCY_ID = i.ref_prosecutor_agency_id AND
           NVL(OBS_IND,'N') != 'Y';

   COMMIT;
END LOOP;

END XHB_DM_UPD_DUP_CPA;
/
