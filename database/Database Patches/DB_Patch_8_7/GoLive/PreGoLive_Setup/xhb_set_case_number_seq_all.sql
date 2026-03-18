create or replace PROCEDURE xhb_set_case_number_seq_all 
AS
/**
  * NAME       : xhb_set_case_number_seq_all
  * DESCRIPTION: CTX-3421 Select max case_number from xhb_case for the given court and case type.  For each row returned 
  *              If there is already a record for the case_type/ court_id then do nothing else create a new record in xhb_case_number_seq_generator and + 100.  
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
  

    CURSOR courts_c
    IS
    SELECT court_id
    FROM   xhb_court
    WHERE  (nvl(obs_ind, 'N') <> 'Y' )
    AND    nvl(is_pilot, 'N') = 'Y' 
    ORDER BY court_id;

BEGIN
    FOR courts_r IN courts_c
    LOOP
        DBMS_OUTPUT.PUT_LINE('Executing xhb_set_case_number_seq_all for court ID: ' || courts_r.court_id || '.  Start Time: ' || to_char(sysdate, 'DD-MM-YYYY HH24:MI:SS') || '.');
	    -- Call xhb_set_case_number_sequences for each court in turn.
        xhb_set_case_number_sequences (courts_r.court_id);
    END LOOP;

    EXCEPTION
        WHEN OTHERS THEN
           raise_application_error(-20001, 'Error in xhb_populate_ref_calendar_all :- ' || SQLCODE || ' : ' || SQLERRM);

END xhb_set_case_number_seq_all;
/
show errors

   
