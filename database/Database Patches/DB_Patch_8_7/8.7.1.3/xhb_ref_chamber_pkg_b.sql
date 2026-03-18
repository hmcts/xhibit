create or replace PACKAGE BODY XHB_REF_CHAMBER_PKG AS

  FUNCTION get_next_crest_chamber_id RETURN XHB_REF_CHAMBER.CREST_CHAMBER_ID%TYPE AS
    v_result XHB_REF_CHAMBER.CREST_CHAMBER_ID%TYPE;
  BEGIN
    SELECT XHB_REF_CHAMBER_CREST_SEQ.NEXTVAL INTO v_result FROM DUAL;
    RETURN v_result;
  END get_next_crest_chamber_id;



PROCEDURE populate_ref_chamber (p_ref_chamber_id  IN xhb_ref_chamber.ref_chamber_id%TYPE
                                ,p_obs_ind           IN xhb_ref_chamber.obs_ind%TYPE
                                ,p_is_global        IN xhb_ref_chamber.is_global%TYPE
                                ,p_dx_ref           IN xhb_ref_chamber.dx_ref%TYPE
                                ,p_location_code    IN  xhb_ref_chamber.location_code%TYPE
                                ,p_crest_chamber_id IN  xhb_ref_chamber.crest_chamber_id%TYPE
                                ,p_firm_name        IN  xhb_ref_chamber.firm_name%TYPE
                                ,p_address_id       IN  xhb_ref_chamber.address_id%TYPE
                                ,p_clerk_name       IN  xhb_ref_chamber.clerk_name%TYPE
                                ,p_user_name        IN xhb_ref_chamber.created_by%TYPE)
IS
 
 TYPE xhb_crt_rec IS RECORD
      (court_id xhb_court.court_id%TYPE);

    TYPE xhb_crt_type IS TABLE OF xhb_crt_rec;
    xhb_crt_tt  xhb_crt_type;


 CURSOR xhb_crt_cur IS
 SELECT court_id
 FROM xhb_court
 WHERE nvl(obs_ind,'N') <> 'Y';

 
BEGIN
  OPEN xhb_crt_cur;
    LOOP
     FETCH xhb_crt_cur BULK COLLECT INTO xhb_crt_tt LIMIT 1000;
    
      IF xhb_crt_tt IS NOT NULL AND xhb_crt_tt.COUNT > 0 THEN
       FOR i IN xhb_crt_tt.FIRST .. xhb_crt_tt.LAST 
        LOOP
           INSERT INTO xhb_ref_chamber  (ref_chamber_id
                                        ,obs_ind   
                                        ,is_global
                                        ,dx_ref   
                                        ,location_code
                                        ,crest_chamber_id
                                        ,firm_name       
                                        ,address_id      
                                        ,court_id        
                                        ,clerk_name
                                        ,last_updated_by
                                        ,created_by)
           VALUES (xhb_ref_chamber_seq.nextval
                 ,p_obs_ind           
                  ,p_is_global       
                  ,p_dx_ref          
                  ,p_location_code   
                  ,p_crest_chamber_id
                  ,p_firm_name       
                  ,p_address_id      
                  ,xhb_crt_tt(i).court_id        
                  ,p_clerk_name
                  ,p_user_name
                  ,p_user_name
           );

          
         
        END LOOP;
      END IF;
      EXIT WHEN xhb_crt_cur%NOTFOUND;
    END LOOP;
        
  CLOSE xhb_crt_cur;                           
  
  EXCEPTION
    WHEN OTHERS THEN
    ROLLBACK;
         DBMS_OUTPUT.PUT_LINE('!!! AN ERROR OCCURRED populate_ref_chamber:  ERROR: '||SUBSTR(SQLERRM,1,110));
 END populate_ref_chamber;
 
 PROCEDURE delete_ref_chamber (p_ref_chamber_id IN xhb_ref_chamber.ref_chamber_id%TYPE, p_user_name IN XHB_REF_CHAMBER.created_by%TYPE) AS
 BEGIN
    UPDATE XHB_REF_CHAMBER
    SET OBS_IND = 'Y',
        LAST_UPDATED_BY = p_user_name    
    WHERE crest_chamber_id = p_ref_chamber_id;
 
 END delete_ref_chamber;

END XHB_REF_CHAMBER_PKG;
/
show errors