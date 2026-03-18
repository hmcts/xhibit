CREATE OR REPLACE PROCEDURE XHIBIT.XHB_EXECUTE_DMI_CAD_FILE (p_date_from IN DATE DEFAULT NULL
                                                            ,p_date_to   IN DATE DEFAULT NULL) 
IS

/******************************************************************************
   NAME:       XHB_EXECUTE_DMI_CAD_FILE
   PURPOSE:   Call the package that generates the dmi cad file.  The package was too large
             to run for all courts into one clob so this procedure will run for each court 
             and create a clob for each court.  All of the clobs will then be merged later.

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        11/05/2018   L214442       1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     XHB_EXECUTE_DMI_CAD_FILE
      Sysdate:         11/05/2018
      Date and Time:   11/05/2018, 12:17:24, and 11/05/2018 12:17:24
     
******************************************************************************/


 x_err_code NUMBER;
 x_err_msg  VARCHAR2(100);
 v_date_from DATE;
 v_date_to DATE;
 invalid_dates EXCEPTION;
 v_run_hist_id NUMBER; -- set the batch job number here and then pass it into package

BEGIN

    SELECT XHB_DMI_CAD_RUN_HISTORY_SEQ.NEXTVAL
    INTO v_run_hist_id --this history id is then propagated down to the detail rows
    FROM dual;

    IF p_date_from IS NULL THEN
      SELECT trunc(trunc(sysdate,'MM')-1,'MM')
      INTO v_date_from
      FROM dual;
     ELSE v_date_from := p_date_from;
    END IF; 
    
    IF p_date_to IS NULL THEN
      SELECT trunc(sysdate,'MM')-1
      INTO v_date_to
      FROM dual;
     ELSE v_date_to := p_date_to;
    END IF; 

    IF extract(month from v_date_from) <> extract(month from v_date_to) --p_date_from and p_date_to must be in the same month for batch calculations
     THEN
      RAISE invalid_dates;
    END IF;
   
 FOR i IN (SELECT crt.court_id
           FROM xhb_court crt
           WHERE (crt.obs_ind <> 'Y' OR crt.obs_ind IS NULL)
           AND crt.is_pilot='Y' --and crt.crest_court_id <= 480
           --AND crt.crest_court_id =422
           ORDER BY crt.crest_court_id
           )
  LOOP
   BEGIN
    xhb_create_dmi_cad_file_pkg.create_output (p_date_from => v_date_from
                                              ,p_date_to   => v_date_to
                                              ,p_err_code  => x_err_code
                                              ,p_err_msg   => x_err_msg
                                              ,p_court_id  => i.court_id
                                              ,p_run_id    => v_run_hist_id
                                              );
   END;

  END LOOP;

 COMMIT;

   EXCEPTION
     WHEN invalid_dates THEN
           raise_application_error(-20001,'p_date_from: '||v_date_from||' must be in the same calendar month as p_date_to: '||v_date_to);
     
     WHEN NO_DATA_FOUND THEN
       NULL;
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END XHB_EXECUTE_DMI_CAD_FILE;
/

