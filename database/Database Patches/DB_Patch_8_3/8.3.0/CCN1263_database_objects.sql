/*
*   SQL CHANGES FOR CCN1263 BY KELVIN DAVIES ON 02/04/09
*   UPDATE XHB_CASE TABLE
*/


-- ALTER XHIBIT TABLES
ALTER TABLE XHB_CASE
      ADD Vulnerable_victim_indicator VARCHAR2(1);
      
ALTER TABLE AUD_CASE
      ADD Vulnerable_victim_indicator VARCHAR2(1);
      

--ALTER XHB_CASE TRIGGERS
-- ALTER XHB_CASE_BUR_TR
@xhb_case_bur_tr

