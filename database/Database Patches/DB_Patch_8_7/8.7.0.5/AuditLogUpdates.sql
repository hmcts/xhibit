
/*    ------------------------------------------------------------------
*     Create DB trigger that writes to audit log post insert into XHB_CHARGES_LOG (CTX-524)
*/    ------------------------------------------------------------------

@@XHB_CHARGES_LOG_ai_tr.sql;

/*    ------------------------------------------------------------------
*     Create DB trigger that writes to audit log post insert into XHB_CASE_PROSECUTOR_AGENCY (CTX-528 & CTX-516)
*/    ------------------------------------------------------------------

@@XHB_CASE_PROSECUTOR_AGENCY_ai_tr.sql


commit;
/