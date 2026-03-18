/*
*   --------------------------------------------------------------------------------------
*   Script created by BH on 08/04/2011, for RFC2871
*   The aim of the script is to update the XHB_DEFENDANT_ON_CASE table
*   to include the new Re Authorised Recordsheet fields
*   --------------------------------------------------------------------------------------
*/

/*

/*    ------------------------------------------------------------------
*     UPDATE XHB_DEFENDANT_ON_CASE TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_DEFENDANT_ON_CASE ADD (AMENDED_DATE_EXPORTED DATE, AMENDED_REASON VARCHAR2(100));
ALTER TABLE AUD_DEFENDANT_ON_CASE ADD (AMENDED_DATE_EXPORTED DATE, AMENDED_REASON VARCHAR2(100));

@xhb_defendantoncase_bur_tr.sql


commit;





