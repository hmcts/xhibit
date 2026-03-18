create or replace PROCEDURE xhb_update_ref_system_code (p_court_id IN xhb_court.court_id%TYPE) AS

/**
  * CGI crest to xhibit program
  *
  * MODULE      : xhb_update_ref_system_code
  *
  * DESCRIPTION : JIRA tickets 3483
  *
  * Procedure                    Purpose
  * =========                    =======
  * xhb_update_ref_system_code   Deletes Other Appeal from code type CASE APPEAL TYPE
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 16/01/2018    J Riley        1.0         First Version
  **/

 v_court_count      NUMBER := 0; --check that the court exists before processing else raise exception
 e_invalid_court_id EXCEPTION;

 --get a list of all non obsolete courts
 CURSOR courts_c IS
 SELECT xc.court_id
 FROM   xhb_court xc
 WHERE  nvl(xc.obs_ind, '-') <> 'Y'
 AND    xc.court_id = p_court_id
 ;

 BEGIN

 IF p_court_id IS NOT NULL THEN --user has specified a court so it needs to be checked that it does exist
     SELECT count(*)
     INTO   v_court_count
     FROM   xhb_court xc
     WHERE  xc.court_id = p_court_id
     AND    p_court_id is not null
     AND    xc.obs_ind <> 'Y';

    /* Court doesn't exist so raise exception and exit */
    IF v_court_count < 1 THEN
        RAISE e_invalid_court_id;
    END IF;

    DELETE FROM XHB_REF_SYSTEM_CODE 
    WHERE  CODE_TYPE = 'CASE_APPEAL_TYPE' 
    AND    DE_CODE   = 'OTHER APPEAL' 
    AND    COURT_ID  = p_court_id;
  
 ELSE
    /* p_court_id not specified, so loop through all the valid courts. */
    FOR courts_r IN courts_c
    LOOP

         DELETE FROM XHB_REF_SYSTEM_CODE 
         WHERE  CODE_TYPE = 'CASE_APPEAL_TYPE' 
         AND    DE_CODE   = 'OTHER APPEAL' 
         AND    COURT_ID  = courts_r.court_id;
  
    END LOOP; --court_r
 END IF;	


   --Exception catch
   EXCEPTION
    WHEN e_invalid_court_id THEN raise_application_error(-20001,' Error exception xhb_update_ref_system_code: '||p_court_id||' does not exist in XHB_COURT');
    WHEN OTHERS THEN raise_application_error(-20001,' Error exception xhb_populate_ref_system_code: '|| SQLCODE || ' : ' || SQLERRM);



 END xhb_update_ref_system_code;
/