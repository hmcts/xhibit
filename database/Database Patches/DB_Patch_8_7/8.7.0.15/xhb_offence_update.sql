/*    ------------------------------------------------------------------
*     Update values in XHB_OFFENCE table for APPEAL_TYPE
*/    ------------------------------------------------------------------

DECLARE 
	CURSOR ALLCASES IS (SELECT * from XHB_CASE);
  	APPEAL_TYPE_VAR VARCHAR(1);
BEGIN
	FOR CURRENTCASE in ALLCASES 
	LOOP  
    	FOR FULL_RES IN (
      		SELECT CURRENTCASE.CASE_SUB_TYPE, XHB_CHARGE.CHARGE_ID
      		FROM XHB_OFFENCE, XHB_CHARGE
      		WHERE XHB_OFFENCE.CHARGE_ID=XHB_CHARGE.CHARGE_ID AND 
            	      XHB_CHARGE.CASE_ID=CURRENTCASE.CASE_ID AND
            	      XHB_OFFENCE.APPEAL_TYPE IS NULL)
    		LOOP

      			UPDATE XHB_OFFENCE
      			SET APPEAL_TYPE = FULL_RES.CASE_SUB_TYPE
      			WHERE CHARGE_ID=FULL_RES.CHARGE_ID;

   		END LOOP;
	END LOOP;
END;
/