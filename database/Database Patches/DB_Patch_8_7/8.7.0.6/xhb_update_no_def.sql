/*    ------------------------------------------------------------------
*     Update values in XHB_CASE table for NO_DEFENDANTS_FOR_CASE
*/    ------------------------------------------------------------------

DECLARE
 CURSOR ALLCASES IS (SELECT * from XHB_CASE);
 currentVal number;
BEGIN
 FOR CURRENTCASE in ALLCASES
   LOOP
   SELECT COUNT(CASE_ID)
          INTO currentVal
          FROM XHB_DEFENDANT_ON_CASE
          WHERE XHB_DEFENDANT_ON_CASE.CASE_ID = (CURRENTCASE.CASE_ID);
          
          
  IF currentVal=0 THEN currentVal:=1;
  END IF;
  
  UPDATE XHB_CASE
  SET NO_DEFENDANTS_FOR_CASE = currentVal
  WHERE CASE_id = (CURRENTCASE.CASE_ID);
   END LOOP;
END;

/