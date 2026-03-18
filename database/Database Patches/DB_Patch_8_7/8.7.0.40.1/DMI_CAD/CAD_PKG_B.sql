CREATE OR REPLACE PACKAGE BODY XHIBIT.XHB_CREATE_DMI_CAD_FILE_PKG /* AUTHID CURRENT_USER */ AS

/** 
  * CGI crest to xhibit program
  *
  * MODULE      : xhb_create_dmi_cad_file_pkg
  *
  * DESCRIPTION : JIRA tickets ctx-1292 and its sub tasks
  *
  *               Procedure                   Purpose
  *               =========                   =======
  *               create_file                 This program will be used to replace the DMI CAD process.  A court is passed in a the text out derived from it.
  *                                           The program generates an output and populates the xml_document.
  *                                           p_date_from and p_date_to can't be in different months because it affects the batch number calculations
  *
  * VERSION HISTORY:
  *
  * Date          Author          Version     Nature of Change
  * ----------    -------         --------    ----------------------------------------
  * 03/11/2017    C Cash          1.0         First Version
  * 31/01/2018    C Cash          2.0         Added functionality for null p_court_id
  * 27/02/2018    C Cash          3.0         Added error logging functionality relating to ctx-1539 and ctx-1540
  **/

x_debug    VARCHAR2(4000);
x_clob     CLOB; --global clob.  This will be getting populated by build_clob procedure and will be passed to calling environment from create_output;

/** 
  * DESCRIPTION :  
  * Procedure    Purpose
  * =========    =======
  * log_event    ctx-1539. Record the the status of the file generation throughout the process and log each change
  *              XHB_DMI_CAD_RUN_HISTORY is used as a header table and each log event is stored in the XHB_DMI_CAD_RUN_LOG table with the dmi_cad_run_history_id
  *              used as the foreign key to join the header to the line rows.
  * Assumptions                  
***/
PROCEDURE log_event (p_cad_ref_code IN xhb_dmi_cad_ref_code.dmi_cad_ref_code_short_name%TYPE DEFAULT NULL
                    ,p_court_id     IN xhb_court.court_id%TYPE
                    ,p_module       IN VARCHAR2 --the procedure name that the insert has come from
                    ,p_message      IN VARCHAR2 DEFAULT NULL
                    )
 IS
 
 v_cad_ref_code_id NUMBER; --the ref code short name will be passed in and the id will be used to populate the log table
 invalid_ref_code EXCEPTION;
 
 BEGIN

 SELECT dmi_cad_ref_code_id
 INTO v_cad_ref_code_id
 FROM xhb_dmi_cad_ref_code
 WHERE dmi_cad_ref_code_short_name = p_cad_ref_code;
  
  IF upper(p_cad_ref_code) = 'S' --start the create process
   THEN
    
    --v_run_hist_id been set in xhb_execute_dmi_cad_file 

    --Create the header row
    INSERT INTO XHB_DMI_CAD_RUN_HISTORY(DMI_CAD_RUN_HISTORY_ID
                                       ,CREATION_DATE
                                       ,CREATED_BY
                                       ,START_RUN_TIME
                                       --,END_RUN_TIME
                                       ,DMI_CAD_REF_CODE_ID)
    VALUES (v_run_hist_id
           ,SYSDATE
           ,'XHIBIT'
           ,SYSDATE
           --,SYSDATE
           ,v_cad_ref_code_id --program started
           );
    
    --Create a detail row for the initial header row       
    INSERT INTO xhb_dmi_cad_run_log (DMI_CAD_RUN_LOG_ID
                                    ,DMI_CAD_RUN_HISTORY_ID
                                    ,DMI_CAD_REF_CODE_ID
                                    ,COURT_ID
                                    ,MODULE_NAME
                                    ,START_TIME)
      VALUES (xhb_dmi_cad_run_log_seq.nextval
             ,v_run_hist_id
             ,v_cad_ref_code_id 
             ,p_court_id
             ,p_module
             ,SYSDATE
             ); 
  
  
   ELSIF upper(p_cad_ref_code) = 'E' --Error/exception
    THEN
    INSERT INTO xhb_dmi_cad_run_log (DMI_CAD_RUN_LOG_ID
                                    ,DMI_CAD_RUN_HISTORY_ID
                                    ,DMI_CAD_REF_CODE_ID
                                    ,COURT_ID
                                    ,MODULE_NAME
                                    ,LOG_MESSAGE
                                    ,START_TIME)
      VALUES (xhb_dmi_cad_run_log_seq.nextval
             ,v_run_hist_id
             ,v_cad_ref_code_id 
             ,p_court_id
             ,p_module
             ,p_message
             ,SYSDATE
             ); 
   
   ELSIF upper(p_cad_ref_code) = 'P' --processing
    THEN
    INSERT INTO xhb_dmi_cad_run_log (DMI_CAD_RUN_LOG_ID
                                    ,DMI_CAD_RUN_HISTORY_ID
                                    ,DMI_CAD_REF_CODE_ID
                                    ,COURT_ID
                                    ,MODULE_NAME
                                    ,LOG_MESSAGE
                                    ,START_TIME)
      VALUES (xhb_dmi_cad_run_log_seq.nextval
             ,v_run_hist_id
             ,v_cad_ref_code_id 
             ,p_court_id
             ,p_module
             ,p_message
             ,SYSDATE
             ); 
   --Process the detail rows and populate the log detail rows table        
   ELSIF upper(p_cad_ref_code) = 'C' --process complete
    THEN 
     INSERT INTO xhb_dmi_cad_run_log (DMI_CAD_RUN_LOG_ID
                                     ,DMI_CAD_RUN_HISTORY_ID
                                     ,DMI_CAD_REF_CODE_ID
                                     ,COURT_ID
                                     ,MODULE_NAME
                                     ,LOG_MESSAGE
                                     ,START_TIME)
      VALUES (xhb_dmi_cad_run_log_seq.nextval
             ,v_run_hist_id
             ,v_cad_ref_code_id 
             ,p_court_id
             ,p_module
             ,p_message
             ,SYSDATE
             );
             
      UPDATE XHB_DMI_CAD_RUN_HISTORY SET END_RUN_TIME = SYSDATE WHERE DMI_CAD_REF_CODE_ID = v_run_hist_id;
                                    
             
   ELSIF upper(p_cad_ref_code) = 'X' --Unknown error
    THEN 
     INSERT INTO xhb_dmi_cad_run_log (DMI_CAD_RUN_LOG_ID
                                     ,DMI_CAD_RUN_HISTORY_ID
                                     ,DMI_CAD_REF_CODE_ID
                                     ,COURT_ID
                                     ,MODULE_NAME
                                     ,LOG_MESSAGE
                                     ,START_TIME)
      VALUES (xhb_dmi_cad_run_log_seq.nextval
             ,v_run_hist_id
             ,v_cad_ref_code_id 
             ,p_court_id
             ,p_module
             ,p_message
             ,SYSDATE
             );   
  
  END IF; 
 
  EXCEPTION 
   WHEN NO_DATA_FOUND THEN  log_event (p_cad_ref_code => 'X' --Exception raised.  Invalid ref code short_name
                                      ,p_court_id => p_court_id
                                      ,p_module   => 'xhb_create_dmi_cad_file_pkg.create_output'
                                      ,p_message   => 'Invalid dmi_ref_code_short_name passed in: ('||p_cad_ref_code||')'
                                      ); 
  
 END log_event;

/** 
  * DESCRIPTION :  
  *   Procedure          Purpose
  *   =========          =======
  *   create_output      Build the file string and insert the output into the xhb_clob table
  *                      If the dates are not populated then the date from and date to will default to the 1st and last
  *                      days of the previous calendar month
  *
  *  Assumptions                  
***/
PROCEDURE create_output 
(p_date_from  IN  DATE DEFAULT NULL --date field not mandatory.  Will default to first of previous calendar month if not populated
,p_date_to    IN  DATE DEFAULT NULL --date field not mandatory.  Will default to last of previous calendar month if not populated
,p_err_code   OUT NUMBER            --error code handling
,p_err_msg    OUT NOCOPY VARCHAR2   --error message
,p_court_id   IN  xhb_court.court_id%TYPE DEFAULT NULL 
,p_run_id     IN  NUMBER
,p_court_from IN  NUMBER DEFAULT NULL
,p_court_to   IN  NUMBER DEFAULT NULL
--,p_clob_out   OUT NOCOPY CLOB
)
AS

 invalid_dates     EXCEPTION;
 v_clob_id         NUMBER;
 v_date_from       DATE; 
 v_date_to         DATE; 

 CURSOR court_c IS
  SELECT court_id
  FROM xhb_court crt
  WHERE (crt.obs_ind <> 'Y' OR crt.obs_ind IS NULL)
  AND crest_court_id between p_court_from AND p_court_to
  ORDER BY CREST_COURT_ID ASC
  ;
  
  BEGIN
  
  v_run_hist_id := p_run_id;
  
  --Start the logging process for this load.  All transactions will be detail records for this initial insert
  log_event (p_cad_ref_code => 'S' --start logging
            ,p_court_id => p_court_id
            ,p_module   => 'xhb_create_dmi_cad_file_pkg.create_output'
            ,p_message  => 'Initial program call with the following parameters: p_date_from: '||p_date_from||' p_date_to: '||p_date_to||' p_court_id: '||p_court_id
            );
  
  --set the return clob to be null before the process starts
  x_clob := NULL; 
  
    /*Set the return to only return dates for the previous month*/
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

  /*If there is no court id passed in then the program will need to run for all courts.  If it is populated then it only needs to be run for one court*/
   IF p_court_id IS NULL
    THEN 
     FOR court_r IN court_c
      LOOP
       build_clob  (p_date_from  => v_date_from
                   ,p_date_to    => v_date_to
                   ,p_err_code   => p_err_code
                   ,p_err_msg    => p_err_msg
                   ,p_court_id   => court_r.court_id
                   ) ;
      END LOOP;
      
    ELSE  build_clob (p_date_from  => v_date_from
                     ,p_date_to    => v_date_to
                     ,p_err_code   => p_err_code
                     ,p_err_msg    => p_err_msg
                     ,p_court_id   => p_court_id
                     );
   END IF;  
   
   SELECT xhb_clob_seq.nextval
   INTO v_clob_id
   FROM dual;
   
   IF x_clob IS NOT NULL
    THEN
   
     INSERT INTO xhb_clob (clob_id, clob_data, last_update_date, creation_date, created_by, last_updated_by, version)
     VALUES (v_clob_id, x_clob, sysdate, sysdate, 'DMI_CREATE_DMI_CAD_FILE_PKG', 'DMI_CREATE_DMI_CAD_FILE_PKG', 1);
   
     log_event (p_cad_ref_code => 'C' --Complete
                     ,p_court_id => p_court_id
                     ,p_module   => 'xhb_create_dmi_cad_file_pkg.create_output'
                     ,p_message   => 'Process Complete.  CLOB_ID in xhb_clob table: '||v_clob_id
                    );
   END IF; 
         
     EXCEPTION
     WHEN invalid_dates THEN
           DBMS_OUTPUT.PUT_LINE ('p_date_from: '||v_date_from||' must be in the same calendar month as p_date_to: '||v_date_to);
           
           log_event (p_cad_ref_code => 'E' --Error
                     ,p_court_id => p_court_id
                     ,p_module   => 'xhb_create_dmi_cad_file_pkg.create_output'
                     ,p_message   => 'p_date_from: '||v_date_from||' must be in the same calendar month as p_date_to: '||v_date_to
                    ); 
           
     WHEN OTHERS THEN
           p_err_code := SQLCODE;
           p_err_msg  := SQLERRM;
           DBMS_OUTPUT.PUT_LINE('Error in xhb_create_dmi_cad_file_pkg.create_output - ' || p_err_code || ' : ' || p_err_msg||'. Debug message:' ||x_debug);     
           
           --raise_application_error(-20001,'line 229');
           
           log_event (p_cad_ref_code => 'E' --Error
                     ,p_court_id => p_court_id
                     ,p_module   => 'xhb_create_dmi_cad_file_pkg.create_output'
                     ,p_message  => 'WHEN OTHERS exception handler raised' 
                    ); 
     
END create_output;


/** o
  * DESCRIPTION :  
  *   Procedure          Purpose
  *   =========          =======
  *   build_clob         Split out the production of the data into a sepearate procedure.  This make is easier to manage if the court id is know at run time.  The create output proc
  *                      will loop through the courts if is doesnt have a court id and pass it here
  *
  *  Assumptions                  
***/

PROCEDURE build_clob  (p_date_from  IN  DATE DEFAULT NULL --date field not mandatory.  Will default to last calendar month if not populated
                      ,p_date_to    IN  DATE DEFAULT NULL --date field not mandatory.  Will default to last calendar month if not populated
                      ,p_err_code   OUT NUMBER     --error code handling
                      ,p_err_msg    OUT NOCOPY VARCHAR2   --error message
                      ,p_court_id   IN  xhb_court.court_id%TYPE DEFAULT NULL   --need to check if this will be the court ID or name
                      )
 IS
 
  -- p_data_string     VARCHAR2(4000);
  x_file_handle     UTL_FILE.FILE_TYPE;
  x_file_status     VARCHAR2(1)  := 'W';
  exit_procedure    EXCEPTION;
  v_case_id         NUMBER;
  v_doc_id          NUMBER;
  
 TYPE head_rec IS RECORD
 (COURT_CODE xhb_court.crest_court_id %TYPE
 ,APPEARANCE_DATE VARCHAR2(20)
 ,AGE VARCHAR2(5)
 ,REMANDA VARCHAR2(20)
 ,REMANDB VARCHAR2(20)
 ,REMANDC VARCHAR2(20)
 ,GENDER VARCHAR2(20)       
 ,FINAL_DRIVING_LICENCE_STATUS VARCHAR2(20)--xhb_defendant_on_case.final_driving_licence_status%TYPE
 ,SURNAME xhb_defendant.surname%TYPE                                   
 ,INITIALS xhb_defendant.initials%TYPE
 ,SELF_ETHNICITY VARCHAR2(10)
 ,RSC_ETHNIC_CODE VARCHAR2(10)
 ,DATE_OF_BIRTH VARCHAR2(20)             
 ,CREST_DEFENDANT_ID VARCHAR2(10)
 ,CASE_ID xhb_case.case_id%TYPE
 ,SERIAL VARCHAR2(100)
 ,BATCH_NUMBER VARCHAR2(25)
 ,DEFENDANT_ON_CASE_ID xhb_defendant_on_case.defendant_on_case_id%TYPE
 ,CASE_NUMBER xhb_case.case_number%TYPE
 ,COURT_ID xhb_court.court_id%TYPE
 ,DEFENDANT_ID xhb_defendant.defendant_id%TYPE);
  
 TYPE head_type IS TABLE OF head_rec;
 head_tt head_type;
      
 CURSOR head_cur (date_from DATE
               ,date_to   DATE
               ,v_court_id IN xhb_court.court_id%TYPE
               ) IS 
 SELECT  DISTINCT xco.crest_court_id as court_code
        ,NVL(xhb_create_dmi_cad_file_pkg.get_appearance_date(xca.case_id, xdoc.defendant_on_case_id),'        ') as APPEARANCE_DATE
        ,to_char(floor(months_between(trunc(sysdate),xd.date_of_birth)/12)) AS AGE
        ,xhb_create_dmi_cad_file_pkg.get_remand_a(xca.case_id, xdoc.defendant_id, xdoc.defendant_on_case_id) AS REMANDA
        ,xhb_create_dmi_cad_file_pkg.get_remand_b(xca.case_id,xdoc.defendant_id,xdoc.defendant_on_case_id,xd.gender,xdoc.comm_bc_status) AS REMANDB
        ,xhb_create_dmi_cad_file_pkg.get_remand_c(xca.case_id,xdoc.defendant_id,xdoc.defendant_on_case_id,xd.is_company, xdoc.comm_bc_status,xca.receipt_type) AS REMANDC
        ,NVL(decode(to_char(xd.gender),'0','3',xd.gender),' ') gender
        ,NVL(to_char(xdoc.final_driving_licence_status),'0') final_driving_licence_status
        ,xd.surname                                    
        ,xd.initials
        ,/*NVL(xd.ethnicity_self_defined,' ')*/ ' ' AS SELF_ETHNICITY
        ,/*NVL(xd.ethnic_appearance_code,' ')*/ ' ' AS RSC_ETHNIC_CODE
        ,to_char(xd.date_of_birth,'ddmmyyyy')  AS DATE_OF_BIRTH              
        ,NVL(to_char(xd.crest_defendant_id),' ') crest_defendant_id
        ,xca.case_id --used in to propagate down to the detail rows
        ,xhb_create_dmi_cad_file_pkg.get_serial(xca.case_number, xca.case_type) SERIAL
        ,xhb_create_dmi_cad_file_pkg.get_batch(xco.crest_court_id,p_date_from) BATCH_NUMBER
        ,xdoc.defendant_on_case_id --used in nested cursor
        ,xca.case_number
        ,xco.court_id
        ,xd.defendant_id
 FROM xhb_case xca
 ,    xhb_court xco
 ,    xhb_hearing xh
 ,    xhb_defendant_on_case xdoc
 ,    xhb_defendant xd
 ,    xhb_charge xc
 ,    xhb_offence xo
 WHERE 
 --1=1
 --AND xca.case_id = 2271119
   xca.court_id = v_court_id
 AND   xca.case_type IN ('T','S','A') --only for trial and sentence case types? Remand b instruction only refers to these
 AND   xca.court_id = xco.court_id
 AND   xca.case_id = xh.case_id
 AND   xca.case_id = xdoc.case_id
 AND   xdoc.defendant_id = xd.defendant_id
 AND   xca.case_id = xc.case_id
 AND   xc.charge_id = xo.charge_id
 AND   xc.charge_type <> 'O'
 AND   (xc.obs_ind <> 'Y' OR xc.obs_ind IS NULL)
 AND   (xco.obs_ind <> 'Y' OR xco.obs_ind IS NULL)
 AND   (xo.obs_ind <> 'Y' OR xo.obs_ind IS NULL)
 AND   xh.creation_date = (SELECT max(h.creation_date) --Brian Hingston advised to use creation_date
                           FROM xhb_hearing h
                           WHERE h.case_id = xca.case_id) 
 /* a case might have more than one defendant on case.  If either of them <> E then dont return either row
 AND NOT EXISTS (SELECT doc.case_id
                 ,      count(*)
                 FROM xhb_defendant_on_case doc
                 WHERE NVL(doc.results_verified,'-') <> 'E'--'R'
                 AND (doc.obs_ind <> 'Y' OR doc.obs_ind IS NULL)
                 AND doc.case_id = xca.case_id
                 GROUP BY case_id
                 HAVING COUNT (*) > 0
                )                    
 */
 AND xdoc.results_verified = 'E'                      
 --AND trunc(xdoc.date_exported) BETWEEN '01-JAN-2018' AND '30-APR-2018'
 AND (trunc(xdoc.date_exported) BETWEEN p_date_from AND p_date_to
  OR trunc(xdoc.amended_date_exported) BETWEEN p_date_from AND p_date_to
   )
   AND EXISTS ( SELECT 'x' 
              FROM xhb_case xhc1
              ,    xhb_charge xc1
              ,    xhb_offence xo1
              ,    xhb_ref_offence xrf1
              ,    xhb_defendant_on_case xdoc1
         --     ,    xhb_defendant_on_offence xdoo1
         --     ,    xhb_plea xp1
         --     ,    xhb_ref_system_code xrsc1
              ,    xhb_ref_court xcrt
              WHERE xhc1.case_id = xc1.case_id
              AND   xhc1.case_id = xdoc1.case_id
              AND   xc1.charge_id = xo1.charge_id
              AND   xc1.CHARGE_TYPE <> 'O'
              AND   xrf1.ref_offence_id = xo1.ref_offence_id
              AND   xhc1.case_id = xc.case_id
              AND   xdoc1.defendant_on_case_id = xdoc.defendant_on_case_id
           --   AND   xo1.offence_id = xdoo1.offence_id
              AND   xcrt.court_id = xhc1.court_id
            --  AND   xdoo1.defendant_on_offence_id = xp1.defendant_on_offence_id (+)
            --  AND   xp1.ref_plea_id = xrsc1.ref_system_code_id
             -- AND   xrsc1.code_type = 'PLEA'
              AND   xhc1.court_id = xcrt.court_id
              AND   xhc1.ref_court_id = xcrt.ref_court_id
              AND  (xdoc1.obs_ind <> 'Y' OR xdoc1.obs_ind IS NULL)
              AND  (xc1.obs_ind <> 'Y' OR xc1.obs_ind IS NULL)
              AND  (xrf1.obs_ind <> 'Y' OR xrf1.obs_ind IS NULL)
              AND  (xo1.obs_ind <> 'Y' OR xo1.obs_ind IS NULL)
       --       AND  (xdoo1.obs_ind <> 'Y' OR xdoo1.obs_ind IS NULL)
       --       AND  (xp1.obs_ind <> 'Y' OR xp1.obs_ind IS NULL)
              AND  EXISTS (/*(SELECT 1 FROM xhb_defendant_on_offence xdoo,xhb_plea xp
                            WHERE xdoo.defendant_on_case_id = xdoc.defendant_on_case_id 
                            AND xdoo.offence_id = xo.offence_id
                            AND xdoo.defendant_on_offence_id = xp.defendant_on_offence_id
                            AND xp.ref_plea_id IS NOT NULL)*/
              
               (SELECT 1 FROM xhb_defendant_on_offence 
                             WHERE defendant_on_case_id = xdoc1.defendant_on_case_id AND offence_id = xo1.offence_id) 
                             UNION 
                            (SELECT 1 FROM xhb_defendant_charge 
                             WHERE defendant_on_case_id = xdoc1.defendant_on_case_id AND charge_id=xc1.charge_id)
                 )
           )   

 --ORDER BY xd.surname ASC
 ; 
 
 TYPE line_rec IS RECORD
 (   HO_CLASS VARCHAR2(10)
    ,HO_SUB_CLASS VARCHAR2(10)
    ,HO_PROC_TYPE VARCHAR2(10)
    ,ICB_SEQUENCE_NO VARCHAR2(100)
    ,COUNT_NO VARCHAR2(10)
    ,COMMITTAL_DATE VARCHAR2(20)
    ,REF_COURT_ID xhb_ref_court.crest_code%TYPE
    ,OFFENCE_TEXT VARCHAR2(250)
    ,DISPOSAL1_TEXT VARCHAR2(250)
    ,DISPOSAL1_CODE VARCHAR2(100)
    ,DISPOSAL1_AMOUNT VARCHAR2(100)
    ,DISPOSAL1_UNIT VARCHAR2(100)
    ,DISPOSAL2_TEXT VARCHAR2(250)
    ,DISPOSAL2_CODE VARCHAR2(100)
    ,DISPOSAL2_AMOUNT VARCHAR2(100)
    ,DISPOSAL2_UNIT VARCHAR2(10)
    ,DISPOSAL3_TEXT VARCHAR2(250)
    ,DISPOSAL3_CODE VARCHAR2(100)
    ,DISPOSAL3_AMOUNT VARCHAR2(100)
    ,DISPOSAL3_UNIT VARCHAR2(100)
    ,DISPOSAL4_TEXT VARCHAR2(250)
    ,DISPOSAL4_CODE VARCHAR2(100)
    ,DISPOSAL4_AMOUNT VARCHAR2(100)
    ,DISPOSAL4_UNIT VARCHAR2(100)
    ,DEFENDANT_ON_CASE_ID xhb_defendant_on_case.defendant_on_case_id%TYPE
    ,CASE_ID xhb_case.case_id%TYPE
    ,OFFENCE_PLEA VARCHAR2(10)
    );
  
 TYPE line_type IS TABLE OF line_rec;
 line_tt line_type;
 
 CURSOR line_cur (v_case_id IN xhb_case.case_id%TYPE
                ,v_doc_id  IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                ) 
  IS
  SELECT NVL(xrf.ho_class,NVL(xo.crest_hoo_class_freetext,0)) HO_CLASS
        ,NVL(xrf.ho_sub_class,NVL(xo.crest_hoo_subclass_freetext,0)) HO_SUB_CLASS
        ,NVL(xhb_create_dmi_cad_file_pkg.get_proc_type_no(xhc.case_id,xc.charge_id,xo.offence_id),' ') AS HO_PROC_TYPE
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disqualification('DISQ_CODE',xdoc.defendant_on_case_id,xo.offence_id),'0') AS DISQ_CODE
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disqualification('DISQ_PERIOD',xdoc.defendant_on_case_id,xo.offence_id),'00') AS DISQ_PERIOD
        ,'00' AS ICB_SEQUENCE_NO
        ,NVL(xo.crest_offence_seq_no,'00') AS COUNT_NO
        ,NVL(to_char(xdoc.date_of_committal,'ddmmyyyy'),'00000000') AS COMMITTAL_DATE
        ,NVL(xcrt.crest_code,'0000') ref_court_id
        ,RPAD(xhb_create_dmi_cad_file_pkg.get_serial(xhc.case_number, xhc.case_type,'LINE'),8,' ')||''||
          RPAD(xrf.offence_code,8,' ')||' '||
          substr(xo.crest_offence_freetext,1,60) AS OFFENCE_TEXT
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('TEXT',xdoc.defendant_on_case_id,xo.offence_id,1),' ') as disposal1_text
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('CODE',xdoc.defendant_on_case_id,xo.offence_id,1),'000') as disposal1_code
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('AMOUNT',xdoc.defendant_on_case_id,xo.offence_id,1),'0000') as disposal1_amount
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('UNIT',xdoc.defendant_on_case_id,xo.offence_id,1),'0') as disposal1_unit
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('TEXT',xdoc.defendant_on_case_id,xo.offence_id,2),' ') as disposal2_text
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('CODE',xdoc.defendant_on_case_id,xo.offence_id,2),'000') as disposal2_code
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('AMOUNT',xdoc.defendant_on_case_id,xo.offence_id,2),'0000') as disposal2_amount
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('UNIT',xdoc.defendant_on_case_id,xo.offence_id,2),'0') as disposal2_unit
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('TEXT',xdoc.defendant_on_case_id,xo.offence_id,3),' ') as disposal3_text
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('CODE',xdoc.defendant_on_case_id,xo.offence_id,3),'000') as disposal3_code
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('AMOUNT',xdoc.defendant_on_case_id,xo.offence_id,3),'0000') as disposal3_amount
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('UNIT',xdoc.defendant_on_case_id,xo.offence_id,3),'0')as disposal3_unit
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('TEXT',xdoc.defendant_on_case_id,xo.offence_id,4),' ') as disposal4_text
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('CODE',xdoc.defendant_on_case_id,xo.offence_id,4),'000') as disposal4_code
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('AMOUNT',xdoc.defendant_on_case_id,xo.offence_id,4),'0000') as disposal4_amount
        ,NVL(xhb_create_dmi_cad_file_pkg.get_disposal ('UNIT',xdoc.defendant_on_case_id,xo.offence_id,4),'0') as disposal4_unit
        ,xdoc.defendant_on_case_id
        ,xdoc.case_id
        ,NVL(xhb_create_dmi_cad_file_pkg.get_plea(XDOC.DEFENDANT_ON_CASE_ID,xc.charge_id,xo.offence_id),' ') as offence_plea
     /*   ,CASE WHEN xrsc.de_code IN ('Autrefois Convict'
                            ,'Change of Plea: Not guilty to guilty (after jury sworn)'
                            ,'Change of Plea: Not guilty to guilty (no jury sworn)'
                            ,'Guilty'
                            ,'Guilty to alternative offence not charged namely'
                            ,'Guilty to lesser offence namely'
                            ) THEN '1'
            WHEN xrsc.de_code IN ('Change of Plea: Guilty to not guilty'
                            ,'Autrefois Acquit'
                            ,'Not guilty'
                            ,'Pardon') THEN '2' 
            WHEN xrsc.de_code IN ('No plea taken'
                            ,'OTHER (ENTER AS FREE TEXT)'
                            ) THEN '3'
       END OFFENCE_PLEA 
       */
  FROM xhb_case xhc
  ,    xhb_charge xc
  ,    xhb_offence xo
  ,    xhb_ref_offence xrf
  ,    xhb_defendant_on_case xdoc
 -- ,    xhb_defendant_on_offence xdoo
 -- ,    xhb_plea xp
 -- ,    xhb_ref_system_code xrsc
  ,    xhb_ref_court xcrt
  WHERE xhc.case_id = xc.case_id
  AND   xhc.case_id = xdoc.case_id
  AND   xc.charge_id = xo.charge_id
  AND   xrf.ref_offence_id = xo.ref_offence_id
  AND   xhc.case_id = v_case_id
  AND   xdoc.defendant_on_case_id = v_doc_id
  --AND   xo.offence_id = xdoo.offence_id
  --AND   xcrt.court_id = xhc.court_id
  --AND   xdoo.defendant_on_offence_id = xp.defendant_on_offence_id (+)
  --AND   xp.ref_plea_id = xrsc.ref_system_code_id
  --AND   xrsc.code_type = 'PLEA'
  AND   xc.charge_type <> 'O'
  AND   xhc.court_id = xcrt.court_id
  AND   xhc.ref_court_id = xcrt.ref_court_id
  AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
  AND  (xc.obs_ind <> 'Y' OR xc.obs_ind IS NULL)
  AND  (xrf.obs_ind <> 'Y' OR xrf.obs_ind IS NULL)
  AND  (xo.obs_ind <> 'Y' OR xo.obs_ind IS NULL)
  AND EXISTS ( /*(SELECT 1 FROM xhb_defendant_on_offence xdoo,xhb_plea xp
                WHERE xdoo.defendant_on_case_id = xdoc.defendant_on_case_id 
                   AND xdoo.offence_id = xo.offence_id
                   AND xdoo.defendant_on_offence_id = xp.defendant_on_offence_id
                   AND xp.ref_plea_id IS NOT NULL)*/
                    
                (SELECT 1 FROM xhb_defendant_on_offence 
                WHERE defendant_on_case_id = xdoc.defendant_on_case_id AND offence_id = xo.offence_id) 
         UNION (SELECT 1 FROM xhb_defendant_charge 
                 WHERE defendant_on_case_id = xdoc.defendant_on_case_id AND charge_id=xc.charge_id)
                 )
  --AND  (xdoo.obs_ind <> 'Y' OR xdoo.obs_ind IS NULL)
  --AND  (xp.obs_ind <> 'Y' OR xp.obs_ind IS NULL)
  ;
BEGIN
   
   x_debug := 'DMI CAD Program start';
   

     IF p_err_code != 0 THEN
         RAISE Exit_procedure;
     END IF;

       
       OPEN head_cur (p_date_from, p_date_to, p_court_id);
       LOOP
       FETCH head_cur BULK COLLECT INTO head_tt LIMIT 1000;
       
         IF head_tt IS NOT NULL AND head_tt.COUNT > 0 THEN
          dbms_output.put_line('Start Head Cursor:'||
                                   ' p_date_from: '||p_date_from||
                                   ' p_date_to: '||p_date_to||
                                   ' p_court_id: '||p_court_id);
               
          FOR i IN head_tt.FIRST..head_tt.LAST 
           LOOP
                      
              log_event (p_cad_ref_code => 'P' --In Progress
                        ,p_court_id => head_tt(i).court_id
                        ,p_module   => 'xhb_create_dmi_cad_file_pkg.build_clob head_r cursor'
                        ,p_message  => 'head cursor values:-  p_date_from: '||p_date_from||
                                       ' p_date_to: '||p_date_to||
                                       ' p_court_id: '||p_court_id
                        ); 
                      
              --build the text file with the data from dmi cursor
              --only start initilising x_clob if there are line rows present for that header
              x_debug := 'Head cursor entered';    
              x_clob :=
              x_clob         ||RPAD('SCKA',4,' ')                                             --JOBNAME (1,4)
                             ||RPAD('P',1,' ')||'   '                                         --RECORD_STATUS (5,5)
                             ||RPAD(NVL(head_tt(i).batch_number,' '),6,' ')                       --BATCH_NUMBER (9,14)
                             ||'000000'                                                       --Column not used but needs whitespace to stop the load failing (15,20)   
                             ||RPAD('NA',2,' ')                                               --RECORD_TYPE (21,22)
                             ||'43'--RPAD(NVL(head_r.force_location_code,' '),2,' ')                --FORCE (23,24)
                             ||LPAD(NVL(head_tt(i).court_code,' '),4,'0')                         --COURT (25,28)
                             ||head_tt(i).appearance_date                                         --APPEARANCE_DATE (this needs to be confirmed as it could be end_date) (29,36)
                             ||'  '                                                           --white space(36,39)
                             ||RPAD(head_tt(i).self_ethnicity,2,' ')                              --SELF_ETHNICITY (39,40)
                             ||RPAD(NVL(head_tt(i).serial,' '),8,' ')                             --SERIAL_NO (41,48)
                             ||'00' --Not required                                            --AGE (49,50)
                             ||head_tt(i).gender                                                  --SEX (51,51)
                             ||head_tt(i).final_driving_licence_status                            --LICENCE (52,52)
                             ||'9'                                                            --REMAND P (53,53)
                             ||RPAD(NVL(head_tt(i).remanda,''),1,' ')                             --REMAND A (54,54)
                             ||RPAD(NVL(head_tt(i).remandb,''),1,' ')                             --REMAND B (55,55)
                             ||RPAD(NVL(head_tt(i).remandc,''),1,' ')                             --REMAND C (56,56)
                             ||'000'                                                          --TOTAL_CURFEW_HOURS (no longer used) (57,59)
                             ||head_tt(i).RSC_ETHNIC_CODE                                         --RSC Ethnic Code (60,60)
                             ||RPAD(NVL(head_tt(i).surname,' '),20,' ')                           --SURNAME (61,80)
                             ||RPAD(NVL(head_tt(i).initials,' '),2,' ')                           --INITIALS (81,82)
                             ||RPAD(NVL(head_tt(i).date_of_birth,' '),8,' ')                      --DATE_OF_BIRTH (83,90)
                             ||'         '--CRO_NO can be ignored so pad with whitespace
                             /*||RPAD(NVL(head_r.initials,' '),2,' ')||RPAD(NVL(head_r.date_of_birth,' '),8,' ')||'9' --CRO_NO (81,99)*/
                             ||LPAD(NVL(head_tt(i).crest_defendant_id,' '),8,'0')                 --CREST_DEFENDANT_NO; (100,107)
                             ||CHR(10) --carriage return to start the detail line feed
                             ;
                  
                          
             --OPEN line_cur (v_case_id => head_tt(i).case_id
             --              ,v_doc_id  => head_tt(i).defendant_on_case_id); --start the line return with the detail data;
             FOR j IN line_cur (v_case_id => head_tt(i).case_id
                               ,v_doc_id  => head_tt(i).defendant_on_case_id) --start the line return with the detail data
              LOOP 
              --FETCH line_cur BULK COLLECT INTO line_tt LIMIT 1000;
                       
                -- IF line_tt IS NOT NULL AND line_tt.COUNT > 0 THEN
                            
                 -- FOR j IN line_tt.FIRST..line_tt.LAST 
                 --  LOOP
                                
                    v_case_id:= j.case_id;
                    v_doc_id := j.defendant_on_case_id; 
                               
                    dbms_output.put_line('Start Line Cursor'||'case id: '||head_tt(i).case_id||' defendant on case id: '||head_tt(i).defendant_on_case_id);
                    dbms_output.put_line('xhb_create_dmi_cad_file_pkg.build_clob line_r cursor :- '||
                                        'line cursor values:-  Defendant on case id: '||head_tt(i).defendant_on_case_id||
                                       ' Case Number: '||head_tt(i).case_number||
                                       ' Defendant On Case ID: '||head_tt(i).defendant_on_case_id||
                                       ' Defendant ID: '||head_tt(i).defendant_id||
                                       ' Case ID: '||j.case_id||
                                       ' Offence: '||j.offence_text||
                                       ' p_court_id: '||p_court_id);
                               
                    log_event (p_cad_ref_code => 'P' --In Progress
                            ,p_court_id => head_tt(i).court_id
                            ,p_module   => 'xhb_create_dmi_cad_file_pkg.build_clob line_r cursor'
                            ,p_message  => 'line cursor values:-  Defendant on case id: '||head_tt(i).defendant_on_case_id||
                                           ' Case Number: '||head_tt(i).case_number||
                                           ' Defendant On Case ID: '||head_tt(i).defendant_on_case_id||
                                           ' Defendant ID: '||head_tt(i).defendant_id||
                                           ' Case ID: '||head_tt(i).case_id||
                                           ' Offence: '||j.offence_text||
                                           ' p_court_id: '||p_court_id
                            ); 
                               
                    x_clob := x_clob ||RPAD('AA',2,' ')                   --Indicates start of line row (1,2)    
                                     ||RPAD(j.ho_class,3,'000')        --CLASSIFICATION (3,5)
                                     ||RPAD(j.ho_sub_class,2,'0')    --SUB CLASS (6,7)
                                     ||RPAD(j.ho_proc_type,2,'0')    --PROCEEDINGS (8,9)
                                     ||NVL(j.disq_code,'0')                    --DISQUALIFICATION (10,10)
                                     ||LPAD(j.disq_period,2,'0')                   --PERIOD OF DISQ (11,12)
                                     ||'00'                               --********unknown value but is all 00 in sample file************** (13,14)
                                     ||'   '                              --HO_ONLY --all values for this in the test file are blank (15,17)
                                     ||j.offence_plea                        --PLEA --all values for the this were either 1,2,3 in file.  Cant find in db (18,18)
                                     ||'2'                                --********unknown value but is all 2 in sample file************** (19,19)
                                     ||'00'                               --ICB_SEQUENCE_NO (20,21) value not required 12/7/18
                                     ||LPAD(j.count_no,2,'0')        --COUNT_NO (22,23)
                                     ||LPAD(j.disposal1_code,3,'0')  --DISPOSAL 1 (24,26)
                                     ||LPAD(NVL(j.disposal1_amount,0),4,'0')--AMOUNT (27,30)
                                     ||LPAD(NVL(j.disposal1_unit,'0'),1,'0')  --UNITS(31,31)
                                     ||LPAD(j.disposal2_code,3,'0')                    --DISPOSAL 2 (32,34)
                                     ||LPAD(NVL(j.disposal2_amount,'0'),4,'0')--AMOUNT (35,38)
                                     ||LPAD(NVL(j.disposal2_unit,'0'),1,'0')  --UNITS(39,39)
                                     ||'  '                               --whitespace for 2 space (40,41)
                                     ||LPAD(j.disposal3_code,3,'0')                    --DISPOSAL 3 (42,44)
                                     ||LPAD(NVL(j.disposal3_amount,'0'),4,'0')--AMOUNT (45,48)
                                     ||LPAD(NVL(j.disposal3_unit,'0'),1,'0')  --UNITS(49,49)
                                     ||LPAD(j.disposal4_code,3,'0')                    --DISPOSAL 4 (50,52)
                                     ||LPAD(NVL(j.disposal4_amount,'0'),4,'0')--AMOUNT (53,56)
                                     ||LPAD(NVL(j.disposal4_unit,'0'),1,' ')  --UNITS(57,57)
                                     ||'  '                               --whitespace for 2 space (58,59)
                                     ||RPAD('0',2,'0')                    --MULTIPLES_OF_OFFENCE (60,61)
                                     ||LPAD(j.ref_court_id,4,'0')    --COMMITTING_COURT (62,65)
                                     ||j.committal_date              --COMMITTAL_DATE (66,73)
                                     ||' '                                --whitespace for 1 space (74,74)
                                     ||RPAD(j.offence_text,78,' ')   --OFFENCE_TEXT (75,152)--presumably there is something to be added to this
                                     ||RPAD(j.disposal1_text,40,' ')
                                       ||RPAD(j.disposal2_text,40,' ')
                                       ||RPAD(j.disposal3_text,40,' ')
                                       ||RPAD(j.disposal4_text,40,' ') --DISPOSAL TEXT 1 (153,192) 
                                     ||CHR(10)
                    ; 
                   END LOOP; --line_r
                               
                 --END IF; --line_tt IS NOT NULL AND line_tt.COUNT > 0
                              
                 --EXIT WHEN line_cur%NOTFOUND;
                          
             --END LOOP;
             --CLOSE line_cur;
                           
           END LOOP; --head_r
                               
         END IF; --head_tt IS NOT NULL AND line_tt.COUNT > 0                      
        EXIT WHEN head_cur%NOTFOUND;
                  
       END LOOP;
       CLOSE head_cur;
       
      
   EXCEPTION
     WHEN OTHERS THEN
           p_err_code := SQLCODE;
           p_err_msg  := SQLERRM;
           DBMS_OUTPUT.PUT_LINE('Error in xhb_create_dmi_cad_file_pkg.build_clob - ' || p_err_code || ' : ' || p_err_msg||'. CaseId:' ||v_case_id|| 'v_doc_id: '||v_doc_id);     
           
           log_event (p_cad_ref_code => 'E' --Error handling
                     ,p_court_id => p_court_id
                     ,p_module   => 'xhb_create_dmi_cad_file_pkg.build_clob'
                     ,p_message  => 'Exception handler raised when others in xhb_create_dmi_cad_file_pkg.build_clob. '|| p_err_code || ' : ' || p_err_msg
                     ); 

 END build_clob;

/** 
  * DESCRIPTION :  
  *   Procedure                   Purpose
  *   =========                   =======
  *   get_batch                   Derive the batch number based on the court code passed in
  *
  *  Assumptions                  Batch range for each court is fixed.  If this changes then the PLSQL array will have to be changed too
  **/
FUNCTION get_batch (p_court     IN VARCHAR2
                   ,p_month     IN DATE
                   ) 
 RETURN VARCHAR2
 IS
 
 v_err_code    NUMBER;
 v_err_msg     VARCHAR2(1000);
 v_court_id    NUMBER; --crest_court_id field is used for the array.  For the error logging, court_id is used
 v_sql         VARCHAR2(250);
 v_module_name VARCHAR2(100) := 'xhb_create_dmi_cad_file_pkg.get_batch';
 v_log_message VARCHAR2(150):= 'Batch Number generated for p_court: '||p_court||' p_month: '||p_month;
 v_month       NUMBER;
 x_return      VARCHAR2(10) := ' '; --no value found
 
 /*Use a array to store all of the values from the initial batch range spreadsheet*/
 TYPE batch_no_tab IS TABLE OF VARCHAR2(10) INDEX BY VARCHAR2(10);
 b_tab batch_no_tab; --batch_table
  
 
 BEGIN
 
 /*Associative array to hold the first batch number for each court.  The first number represents January
  and for each subsequent month, the number goes up by 1 using x_month variable
  *
  * The first number of the array is the crest_court_id field in the XHB_COURT table and passed in from create_file
  */
  
  x_debug := 'get_batch function.  Array initialised';
  
  b_tab('401') := '107000'; b_tab('421') := '107400'; b_tab('441') := '107800'; b_tab('461') := '108200';
  b_tab('402') := '107020'; b_tab('422') := '107420'; b_tab('442') := '107820'; b_tab('462') := '108220';
  b_tab('403') := '107040'; b_tab('423') := '107440'; b_tab('443') := '107840'; b_tab('463') := '108240';
  b_tab('404') := '107060'; b_tab('424') := '107460'; b_tab('444') := '107860'; b_tab('464') := '108260';
  b_tab('405') := '107080'; b_tab('425') := '107480'; b_tab('445') := '107880'; b_tab('465') := '108280';
  b_tab('406') := '107100'; b_tab('426') := '107500'; b_tab('446') := '107900'; b_tab('466') := '108300';
  b_tab('407') := '107120'; b_tab('427') := '107520'; b_tab('447') := '107920'; b_tab('467') := '108320';
  b_tab('408') := '107140'; b_tab('428') := '107540'; b_tab('448') := '107940'; b_tab('468') := '108340';
  b_tab('409') := '107160'; b_tab('429') := '107560'; b_tab('449') := '107960'; b_tab('469') := '108360';
  b_tab('410') := '107180'; b_tab('430') := '107580'; b_tab('450') := '107980'; b_tab('470') := '108380';
  b_tab('411') := '107200'; b_tab('431') := '107600'; b_tab('451') := '108000'; b_tab('471') := '108400';
  b_tab('412') := '107220'; b_tab('432') := '107620'; b_tab('452') := '108020'; b_tab('472') := '108420';
  b_tab('413') := '107240'; b_tab('433') := '107640'; b_tab('453') := '108040'; b_tab('473') := '108440';
  b_tab('414') := '107260'; b_tab('434') := '107660'; b_tab('454') := '108060'; b_tab('474') := '108460';
  b_tab('415') := '107280'; b_tab('435') := '107680'; b_tab('455') := '108080'; b_tab('475') := '108480';
  b_tab('416') := '107300'; b_tab('436') := '107700'; b_tab('456') := '108100'; b_tab('476') := '108500';
  b_tab('417') := '107320'; b_tab('437') := '107720'; b_tab('457') := '108120'; b_tab('477') := '108520';
  b_tab('418') := '107340'; b_tab('438') := '107740'; b_tab('458') := '108140'; b_tab('478') := '108540';
  b_tab('419') := '107360'; b_tab('439') := '107760'; b_tab('459') := '108160'; b_tab('479') := '108560';
  b_tab('420') := '107380'; b_tab('440') := '107780'; b_tab('460') := '108180'; b_tab('480') := '108580';
  
  SELECT extract(month from p_month) - 1 "MONTH" 
  INTO v_month
  FROM dual;
  
   IF b_tab.exists(p_court)
    THEN 
     x_return := to_char(to_number(b_tab(p_court)) + v_month);
     
    /* v_sql := 'INSERT INTO xhb_dmi_cad_run_log (DMI_CAD_RUN_LOG_ID, DMI_CAD_RUN_HISTORY_ID, DMI_CAD_REF_CODE_ID, COURT_ID, MODULE_NAME, LOG_MESSAGE) '
             ||' VALUES (xhb_dmi_cad_run_log_seq.nextval, :hist, 2, :crt, :mod_name, :v_log_txt';
     
     EXECUTE IMMEDIATE v_sql USING v_run_hist_id, p_court, v_module_name, v_log_message;
    */
 
   ELSE x_return := ' '; --the create file procedure will manage this with RPAD
   END IF;

  RETURN x_return; 
 
 EXCEPTION
   WHEN OTHERS THEN 
    RETURN (' '); --if the program fails then return white space.  The create_file will handle the output.
           v_err_code := SQLCODE;
           v_err_msg  := SQLERRM;
           DBMS_OUTPUT.PUT_LINE('Error in xhb_create_dmi_cad_file_pkg.create_file formatting line - ' || v_err_code || ' : ' || v_err_msg);     
    
 END get_batch;

/** 
  * DESCRIPTION :  
  *   Procedure                   Purpose
  *   =========                   =======
  *   get_serial                  Derive the serial number based on the case number
  *
  *  Assumptions                  The serial number is the same as the case number with the exception of the first 2 digits of the case number which represents the year are replaced with S1
  *                               e.g. case number 20080062 becomes S1080062
  *                               This is the case for the cases pre 2000
  **/
FUNCTION get_serial (p_case_number IN xhb_case.case_number%TYPE
                    ,p_case_type   IN xhb_case.case_type%TYPE
                    ,p_row         IN VARCHAR2 DEFAULT 'HEAD') --used in head and line rows.  Returned differently
 RETURN VARCHAR2
 IS
 
 x_return VARCHAR2(10);
 
 BEGIN
  IF p_row = 'HEAD' THEN
   x_return := RPAD(p_case_type||substr(p_case_number,3),8,'0');
  ELSE 
   x_return := p_case_type||substr(p_case_number,3);
  END IF;
  RETURN x_return;
 
 EXCEPTION
 WHEN OTHERS THEN
           RETURN (' ');    
 
 END get_serial;

/** 
  * DESCRIPTION :  
  *   Procedure                   Purpose
  *   =========                   =======
  *   get_disposal               Used in DMI CAD file line row.  Called from procedure that builds the txt output and returns disposal values
  *                              Uses the xhb_disposal2 and  xhb_disposal_line tables. 
  *
  *  Assumptions                The first row for any line number will be a data value (the first field in the disposal screen in xhibit) so this can be
  *                             excluded from the return.  The values that we are interested in are value and unit.
  *                           
  *                             Only 4 non-driving disqualification disposals are to be processed.
  **/

FUNCTION get_disposal (p_field     IN VARCHAR2 --what field to return (amount or unit)
                      ,p_doc_id    IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                      ,p_offence_id   IN xhb_offence.offence_id%TYPE 
                      ,p_disp      IN NUMBER --disposal 1,2,3,4
                      ) 

 RETURN VARCHAR2
 IS
 
 /*Get the initial disposal data based on the defendant on case id/ defendant on offence*/
   CURSOR disp_c
    IS
    SELECT *
    FROM (   
    WITH disps AS ( 
    SELECT distinct dl.line_number
                   ,d.disposal2_id
                   ,dl.data
                   ,upper(rdl.prompt) PROMPT
                   ,d.defendant_on_offence_id
                   ,d.defendant_on_case_id
                   ,substr(xdc.crest_disposal_code||' '||xdc.disposal_description,1,40) disposal_text
                   ,xdc.ho_code, xdc.disposal_type
                   ,count(*) 
        FROM xhb_disposal2 d 
        ,    xhb_disposal_line dl
        ,    xhb_ref_disposal_type rdt
        ,    xhb_ref_disposal_line rdl
        ,    xhb_dmi_disposal_codes xdc
        WHERE d.disposal2_id = dl.disposal2_id
        AND d.defendant_on_case_id = p_doc_id
        AND d.ref_disposal_type_id = rdt.ref_disposal_type_id 
        AND RDT.DISPOSAL_CODE NOT IN ('DISQVC','NRADD','DISTEST','DISPCCA','DDPAS','TDISQ','DISREM','DISQAO')
        AND rdl.REF_DISPOSAL_LINE_ID = dl.ref_disposal_line_id
 --       AND rdt.disposal_code = xdc.ho_code
        AND rdl.prompt IS NOT NULL
        AND rdt.disposal_code = xdc.crest_disposal_code 
        
        AND (d.obs_ind <> 'Y' OR d.obs_ind IS NULL)
        AND (dl.obs_ind <> 'Y' OR dl.obs_ind IS NULL) 
        /*AND ((UPPER(rdl.prompt)) LIKE '%DURATION%' 
                     OR (UPPER(rdl.prompt)) LIKE '%AMOUNT%'
                     OR (UPPER(rdl.prompt)) LIKE '%UNIT%'
                    )*/
        GROUP BY dl.line_number
               ,d.disposal2_id
               ,dl.data
               ,upper(rdl.prompt)
               ,d.defendant_on_offence_id
               ,d.defendant_on_case_id
               ,substr(xdc.crest_disposal_code||' '||xdc.disposal_description,1,40)
               ,xdc.ho_code,xdc.disposal_type 
        UNION
        SELECT distinct dl.line_number
                   ,d.disposal2_id
                   ,dl.data
                   ,upper(rdl.prompt) PROMPT
                   ,d.defendant_on_offence_id
                   ,d.defendant_on_case_id
                   ,substr(xdc.crest_disposal_code||' '||xdc.disposal_description,1,40)
                   ,xdc.ho_code,xdc.disposal_type
                   ,count(*)
        FROM xhb_disposal2 d 
        ,    xhb_disposal_line dl
        ,    xhb_ref_disposal_type rdt
        ,    xhb_ref_disposal_line rdl
        ,    xhb_dmi_disposal_codes xdc
        WHERE d.disposal2_id = dl.disposal2_id
        --AND d.defendant_on_offence_id = p_doo_id
        AND d.defendant_on_offence_id IN (SELECT defendant_on_offence_id 
                                         FROM xhb_defendant_on_offence 
                                         WHERE defendant_on_case_id= p_doc_id
                                         AND offence_id= p_offence_id)
        AND d.ref_disposal_type_id = rdt.ref_disposal_type_id
        AND RDT.DISPOSAL_CODE NOT IN ('DISQVC','NRADD','DISTEST','DISPCCA','DDPAS','TDISQ','DISREM','DISQAO') 
        AND rdl.REF_DISPOSAL_LINE_ID = dl.ref_disposal_line_id
        AND rdt.disposal_code = xdc.crest_disposal_code 
        AND rdl.prompt IS NOT NULL
        AND (d.obs_ind <> 'Y' OR d.obs_ind IS NULL)
        AND (dl.obs_ind <> 'Y' OR dl.obs_ind IS NULL)
     /*   AND ((UPPER(rdl.prompt)) LIKE '%DURATION%' 
                     OR (UPPER(rdl.prompt)) LIKE '%AMOUNT%'
                     OR (UPPER(rdl.prompt)) LIKE '%UNIT%'
                    )*/
        GROUP BY dl.line_number
               ,d.disposal2_id
               ,dl.data
               ,upper(rdl.prompt)
               ,dl.line_number 
               ,d.defendant_on_offence_id
               ,d.defendant_on_case_id
               ,substr(xdc.crest_disposal_code||' '||xdc.disposal_description,1,40) 
               ,xdc.ho_code,xdc.disposal_type)
       SELECT line_number
           ,disposal2_id
           ,data
           ,prompt
           ,defendant_on_offence_id
           ,defendant_on_case_id
           ,disposal_text
           ,ho_code
           ,disposal_type
           ,DENSE_RANK() OVER (ORDER BY disposal2_id ASC) AS DISPOSAL_LINE
       FROM disps WHERE UPPER(data) NOT LIKE '%DELETE%' )
WHERE disposal_line = p_disp                                          
;
  
   
 x_return VARCHAR2(4000); 
 v_disposal_text1 VARCHAR2(150);
 v_disposal_text2 VARCHAR2(150);
 v_disposal_text3 VARCHAR2(150);
 v_disposal_text4 VARCHAR2(150);
 v_disposal_full_text VARCHAR2(250);
 
 -- new variables
 v_data xhb_disposal_line.data%type := NULL;
 v_unit xhb_ref_disposal_line.prompt%type := NULL;
 v_ho_amt  NUMBER;
 v_ho_unit NUMBER;
 BEGIN
 
  
  x_debug := 'get_disposal start';
  x_return := NULL; 
  FOR disp_r IN disp_c
   LOOP
    x_debug := 'get_disposal header row found';
    
    IF upper(p_field) = 'TEXT'
     THEN
      x_return := disp_r.disposal_text;
       
      --amount is passed in from main cursor but applies to duration and amount
      -- if it is life sentene disposal then AMOUNT:=99 and UNIT is 5-years
    ELSIF upper(p_field) = 'AMOUNT' and disp_r.disposal_type='LI' THEN
        x_return := 99;
    ELSIF upper(p_field) = 'UNIT' and disp_r.disposal_type='LI' THEN
        x_return := 5;
    ELSIF upper(p_field) IN ('AMOUNT','UNIT') THEN
        IF disp_r.prompt LIKE '%DURATION%' OR disp_r.prompt LIKE '%AMOUNT%' THEN
            v_data := disp_r.data;
        ELSIF disp_r.prompt LIKE '%UNIT%' THEN    
            v_unit := disp_r.data;
        END IF;     
     /* IF disp_r.prompt LIKE '%DURATION%' OR disp_r.prompt LIKE '%AMOUNT%'
       THEN
        IF disp_r.data LIKE '%.%'
         THEN
          x_return := LPAD(substr(disp_r.data,1,instr(disp_r.data,'.',1)-1),4,'0'); --if monetary value remove decimal
        ELSE
         x_return := LPAD(substr(disp_r.data,1,4),4,'0');
        END IF;
      END IF;*/
    
    ELSIF upper(p_field) = 'CODE'
     THEN
       x_return := disp_r.ho_code;
  
    --ELSIF upper(p_field) = 'UNIT'
     --THEN
      -- x_debug := 'get_disposal header and line found.  p_field = UNIT';
         
      -- IF disp_r.prompt LIKE '%UNIT%'
        --   THEN
           /*From the code that generated the DMI CAD file in the old DMI system
             decode(upper(ddis.units1), 'HOUR', '1', 'HOURS', '1','DAY', '2', 'DAYS', '2', 'WEEK', '3', 'WEEKS', '3',
               'MONTH', '4', 'MONTHS', '4', 'YEAR', '5', 'YEARS', '5', ddis.units1)*/
         /*   x_return := CASE WHEN UPPER(disp_r.data) IN ('HOUR','HOURS') THEN 1 
                             WHEN UPPER(disp_r.data) IN ('DAY','DAYS') THEN 2
                             WHEN UPPER(disp_r.data) IN ('WEEK','WEEKS') THEN 3
                             WHEN UPPER(disp_r.data) IN ('MONTH','MONTHS') THEN 4
                             WHEN UPPER(disp_r.data) IN ('YEARS','YEARS') THEN 5
                          ELSE 6
                        END;
       END IF;*/
        
    END IF;
    
   END LOOP; --disp_r 
   -- If unit in hr, days, weeks, months and year then return amount as it is
   IF UPPER(v_unit) IN ('HOUR','HOURS','DAY','DAYS','WEEK','WEEKS','MONTH','MONTHS','YEARS','YEARS') THEN   
    IF upper(p_field) = 'UNIT' THEN
        x_return := CASE WHEN UPPER(v_unit) IN ('HOUR','HOURS') THEN 1 
                             WHEN UPPER(v_unit) IN ('DAY','DAYS') THEN 2
                             WHEN UPPER(v_unit) IN ('WEEK','WEEKS') THEN 3
                             WHEN UPPER(v_unit) IN ('MONTH','MONTHS') THEN 4
                             WHEN UPPER(v_unit) IN ('YEARS','YEARS') THEN 5
                          ELSE NULL
                    END;
    ELSIF upper(p_field) = 'AMOUNT' THEN
        x_return := v_data ;  
    END IF;
   ELSIF v_data IS NOT NULL THEN 
    -- data is monetory so convert it to 4 character value as below: 
    IF v_data < 1 THEN
        v_ho_amt := v_data*100;
        v_ho_unit:= 6;
    ELSIF round(v_data) between 1 and 9999 THEN
        v_ho_amt := round(v_data);
        v_ho_unit:= 7;
    ELSIF round(v_data) between 10000 and 999949 THEN
        v_ho_amt := to_number(substr(to_char(round(round(v_data),-2),'099999'),2,4));
        v_ho_unit:= 8;    
    ELSIF round(v_data) between 999950 and 9999499 THEN
        v_ho_amt := to_number(substr(to_char(round(round(v_data),-3),'0999999'),2,4));
        v_ho_unit:= 9;    
    END IF;      
    IF  upper(p_field) = 'AMOUNT' THEN
        x_return := v_ho_amt;
    ELSIF upper(p_field) = 'UNIT' THEN
        x_return := v_ho_unit;
    END IF;
           
   END IF;
   RETURN x_return;
 
  EXCEPTION 
   WHEN no_data_found THEN x_return := NULL;
   WHEN OTHERS THEN
           
          log_event (p_cad_ref_code => 'E' --Error
                    ,p_court_id => NULL
                    ,p_module   => 'xhb_create_dmi_cad_file_pkg.get_disposal'
                    ,p_message  => 'Other exception raised.  Parameters passed in:-'||
                                   ' p_field: ' ||p_field||
                                   ' p_doc_id: '||p_doc_id||
                                   ' p_disp: '||p_disp
                    ); 

 END get_disposal;

/** 
  * DESCRIPTION :  
  *   Procedures                 Purpose
  *   =========                  =======
  *   get_remanda                Used in DMI CAD file header row.  Called from procedure that builds the txt output and returns remand values for P,A,B,C
  *                              Values: 1- on bail at all hearings
  *                              Values: 2- in custody at all hearings
  *                              Values: 3- on bail at first hearing and in custody at least once in any later hearing
  *                              Values: 4- in custody at the first hearing and onbail at least once in any later hearing
  *                              Values: 9- not applicable
  **
 */
 FUNCTION get_remand_a (p_case_id      IN xhb_case.case_id%TYPE
                       ,p_defendant_id IN xhb_defendant.defendant_id%TYPE
                       ,p_doc_id       IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                       )
 RETURN NUMBER
 IS
  
 v_first_hrg_bc_status       xhb_def_hearing_record.start_bail_status%TYPE;
 v_status_change_cnt         NUMBER;
 v_remand                    NUMBER := 9;
 x_debug                     VARCHAR2(250);
 x_default_remand            EXCEPTION; --If the remand can't be calculated then return 9
 
  --get the bc_status of the first hearing 
 CURSOR bc_status_c
  IS
  SELECT xdhr.start_bail_status
  FROM xhb_case xc
  ,    xhb_defendant_on_case xdoc
  ,    xhb_def_hearing_record xdhr
  ,    xhb_hearing xh
  WHERE xc.case_type IN ('T') --only use trial case types
  AND   xc.case_id = xdoc.case_id
  AND  xdoc.case_id = p_case_id
  AND  xdoc.defendant_on_case_id = p_doc_id
  AND  xdoc.defendant_id = p_defendant_id
  AND  xdoc.defendant_on_case_id = xdhr.defendant_on_case_id
  AND  xdhr.hearing_id = xh.hearing_id 
  AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
  AND  xdhr.hearing_start_date = (SELECT MIN(xdhr.hearing_start_date) as hearing_start_date--find the first hearing
                                  FROM xhb_case xc
                                  ,    xhb_defendant_on_case xdoc
                                  ,    xhb_def_hearing_record xdhr
                                  WHERE xc.case_id = xdoc.case_id
                                  AND  xdoc.defendant_on_case_id = p_doc_id
                                  AND  xdoc.case_id = p_case_id
                                  AND  xdoc.defendant_id = p_defendant_id
                                  AND  xdoc.defendant_on_case_id = xdhr.defendant_on_case_id
                                  AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
                                  )
  ORDER BY xdhr.hearing_start_date;
  
 bc_status_r bc_status_c%ROWTYPE;

 BEGIN
  
  /*Used a cursor because the test data was poor and there some occurances of multiple start dates for the same case and defendant*/
  OPEN bc_status_c;
   FETCH bc_status_c 
   INTO  bc_status_r;
   
  IF bc_status_c%FOUND
   THEN
    v_first_hrg_bc_status := bc_status_r.start_bail_status;
  END IF;
 
  CLOSE bc_status_c;

    --Check if there are any rows that have the bc_status populated that is not the same as the bc_status of the first hearing
    SELECT count(*)
    INTO v_status_change_cnt
    FROM xhb_case xc
    ,    xhb_defendant_on_case xdoc
    ,    xhb_def_hearing_record xdhr
    ,    xhb_hearing xh
    WHERE xc.case_type IN ('T')
    AND xc.case_id = xdoc.case_id
    AND  xdoc.case_id = p_case_id
    AND xdoc.defendant_on_case_id = xdhr.defendant_on_case_id
    AND xdoc.defendant_on_case_id = p_doc_id
    AND xdhr.hearing_id = xh.hearing_id
    --AND xdoc.defendant_id = p_defendant_id
    AND xdhr.start_bail_status <> v_first_hrg_bc_status
    --AND NVL(xdhr.start_bail_status,'-') <> v_first_hrg_bc_status --does a change to null count as a status change
    AND xdhr.hearing_start_date IS NOT NULL --does a null hearing start date need to be excluded.  Same sceanrio as issue in line above
    AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL);
        
    IF v_first_hrg_bc_status = 'B' 
     THEN
      IF v_status_change_cnt = 0 --status hasnt changed
       THEN
        v_remand := 1;
        ELSE
        v_remand := 3;
      END IF;
    ELSIF v_first_hrg_bc_status IN ('C','J')
     THEN
      IF v_status_change_cnt = 0
       THEN
        v_remand := 2;
       ELSE
        v_remand := 4;
      END IF;
    ELSE
     v_remand := 9;
    END IF;
    
    RETURN v_remand;
    
    EXCEPTION
     WHEN x_default_remand THEN RETURN 9;
     WHEN TOO_MANY_ROWS THEN RETURN 9;
     WHEN NO_DATA_FOUND THEN RETURN 9;
     WHEN OTHERS THEN RETURN 9;

END get_remand_a;

/** 
  * DESCRIPTION :  
  *   Procedures                 Purpose
  *   =========                  =======
  *   get_remand_b               Used in DMI CAD file header row.  Called from procedure that builds the txt output and returns remand values for P,A,B,C
  *                              Values: 1- on bail at all hearings
  *                              Values: 2- in custody at all hearings
  *                              Values: 3- on bail at first hearing and in custody at least once in any later hearing
  *                              Values: 4- in custody at the first hearing and onbail at least once in any later hearing
  *                              Values: 9- not applicable
  *  Functional Description
  *                              Start procedure to find defendant's Remand B status (trial and sentence cases). The Remand B code shows whether the defendant had a "put back for
  *                              sentence" (PB) hearing and, if so, shows what his/her B/C status was at the end of that hearing and the start of the hearing at which the Judge passed sentence.
  *
  *                              Get the defendant's earliest PB hearing in the current case. If there is a PB hearing, get its start_date, end_date and start and end bail_status. If
  *                              start_bc_status is null, convert it to `N' for "not applicable". 
  */
 FUNCTION get_remand_b (p_case_id      IN xhb_case.case_id%TYPE
                       ,p_defendant_id IN xhb_defendant.defendant_id%TYPE
                       ,p_doc_id       IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                       ,p_is_company   IN xhb_defendant.is_company%TYPE
                       ,p_doc_bc_status IN xhb_defendant_on_case.current_bc_status%TYPE
                       )
 RETURN NUMBER
 IS
 
 v_put_back_cnt               NUMBER;
 v_remand                     NUMBER := 9;
 v_pb_start_date              DATE;
 v_pb_end_date                DATE;
 v_pb_hrg_start_bc_status     xhb_def_hearing_record.start_bail_status%TYPE;
 v_pb_hrg_end_bc_status       xhb_def_hearing_record.end_bail_status%TYPE;
 v_sent_hrg_id                xhb_hearing.hearing_id%TYPE; --sentence hearing
 v_sent_hrg_start_date        xhb_def_hearing_record.hearing_start_date%TYPE; --sentence hearing start date
 v_sent_hrg_end_date          xhb_def_hearing_record.hearing_end_date%TYPE; --sentence hearing end date
 v_sent_start_bail_status     xhb_def_hearing_record.end_bail_status%TYPE;
 v_last_hrg_start_date        xhb_def_hearing_record.hearing_start_date%TYPE;
 v_count_subs_bc_hearing      NUMBER; --get a count of subsequent hearing (after 1st) that have a different bc_status
 v_count_subs_bc_n_hearing    NUMBER; --get a count of subsequent hearing (after 1st) that have a different bc_status and the status = N
 v_count_subs_hearing         NUMBER; --get a count of the subsequent hearings
 v_pb_bc_status_changed_cnt   NUMBER; --Hold the number of bail status changes
 v_pb_bc_status_changed       VARCHAR2(10); --determines if the bc status has changed in subsequent hearings
 v_cnt_n_bc_start_status      NUMBER; --Get the number of hearing where the start status is N
 v_cnt_bail_applications      NUMBER; --Get the number of successful bail application
 v_custody_found              VARCHAR2(1) := 'N'; --are there any hearing with a bail start or end status = 'C'
 v_bail_found                 VARCHAR2(1) := 'N'; --are there any hearings after that that have a start or end bail sttus of 'B'
 x_debug                      VARCHAR2(250) := 'sub_block_2_100';
 x_default_remand             EXCEPTION; --If the remand can't be calculated then return 9
   
  /*get the earliest put back for the case and defendant.  This will only be called if the count of put backs > 0*/
  CURSOR put_backs_c IS
    SELECT xdhr.hearing_start_date
    ,      xdhr.hearing_end_date
    ,      NVL(xdhr.start_bail_status,'N') pb_start_bail_status
    ,      NVL(xdhr.end_bail_status,'N')   pb_end_bail_status
    FROM xhb_case xc
    ,    xhb_defendant_on_case xdoc
    --,    xhb_defendant xd
    ,    xhb_hearing xh
    ,    xhb_def_hearing_record xdhr
    WHERE xc.case_id = xdoc.case_id
    AND   xc.case_type IN ('T','S') --only for trial and sentence case types
    --AND   xdoc.defendant_id = xd.defendant_id
    AND   xc.case_id = xh.case_id
    AND   xh.hearing_id = xdhr.hearing_id
    AND   xdoc.case_id = p_case_id 
    AND   xdoc.defendant_on_case_id = p_doc_id
    --AND   xdoc.DEFENDANT_ID = p_defendant_id
    AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
    AND  NVL(xdhr.ref_adjournment_id,00000) IN (SELECT xrsc.ref_system_code_id 
                                      FROM xhb_ref_system_code xrsc
                                      WHERE upper(xrsc.code_type) = 'PB_TYPE' --Put backs
                                      AND   upper(xrsc.code_title) LIKE '%ADJ%' --Adjournment
                                      AND   upper(xrsc.code) IN ('S','DS','SE','SC','SS','SO','SM') --put back for sentence codes
                                      AND xrsc.court_id = xc.court_id
                                      )
    ORDER BY xdhr.hearing_start_date ASC;
    
   put_backs_r put_backs_c%ROWTYPE; 
   
   /*Get the start date of the last hearing in which the defendant appeared on this case*/
   CURSOR last_hearing_c (v_pb_date IN DATE)
   IS
   SELECT xdhr.hearing_start_date AS last_hearing_start_date
   FROM xhb_case xc
   ,    xhb_defendant_on_case xdoc
   --,    xhb_defendant xd
   ,    xhb_hearing xh
   ,    xhb_def_hearing_record xdhr
   WHERE xc.case_id = xdoc.case_id
   AND   xc.case_type IN ('T','S')
   --AND   xdoc.defendant_id = xd.defendant_id
   AND   xdoc.defendant_on_case_id = p_doc_id
   AND   xc.case_id = xh.case_id
   AND   xh.hearing_id = xdhr.hearing_id   
   AND   xdoc.case_id = p_case_id
   --AND   xdoc.defendant_id = p_defendant_id
   AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
   AND   TRUNC(xdhr.hearing_start_date) > v_pb_date --The last hearing must not be a put back and must come after the first put back hearing
   AND   NVL(xdhr.ref_adjournment_id,00000) NOT IN (SELECT xrsc.ref_system_code_id 
                                         FROM xhb_ref_system_code xrsc
                                         WHERE upper(xrsc.code_type) = 'PB_TYPE' --Put backs
                                         AND   upper(xrsc.code_title) LIKE '%ADJ%' --Adjournment
                                         AND   upper(xrsc.code) IN ('S','DS','SE','SC','SS','SO','SM') --put back for sentence codes
                                         AND   xrsc.court_id = xc.court_id
                                         )
   ORDER BY 1 DESC --only get the last date.  Will use a FETCH rather than loop to only return one row
   ;
   
   last_hearing_r last_hearing_c%ROWTYPE;
   
  /*Get the count of status changes.  Get all of the hearings*/ 
  CURSOR bail_changes_c (v_date IN DATE) IS
  SELECT  xdhr.hearing_start_date
  ,       xdhr.start_bail_status
  ,       xdhr.end_bail_status
  FROM xhb_case xc
  ,    xhb_defendant_on_case xdoc
  --,    xhb_defendant xd
  ,    xhb_hearing xh
  ,    xhb_def_hearing_record xdhr
  WHERE xc.case_id = xdoc.case_id
  AND   xc.case_type IN ('T','S')
  --AND   xdoc.defendant_id = xd.defendant_id
  AND   xdoc.defendant_on_case_id = p_doc_id
  AND   xc.case_id = xh.case_id
  AND   xh.hearing_id = xdhr.hearing_id
  AND   xdoc.case_id = p_case_id --this is the case id that I have used for debugging
  --AND   xdoc.defendant_id = p_defendant_id
  AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
  AND   xdhr.hearing_start_date IS NOT NULL
  AND   trunc(xdhr.hearing_start_date) > trunc(v_date) --we already know the status of the first hearing so its only subsequent hearing to check
  ORDER BY xdhr.hearing_start_date
  ;
  CURSOR bail_changes_c1 (v_date IN DATE) IS
  SELECT  xdhr.hearing_start_date
  ,       xdhr.start_bail_status
  ,       xdhr.end_bail_status
  FROM xhb_case xc
  ,    xhb_defendant_on_case xdoc
  --,    xhb_defendant xd
  ,    xhb_hearing xh
  ,    xhb_def_hearing_record xdhr
  WHERE xc.case_id = xdoc.case_id
  AND   xc.case_type IN ('T','S')
  --AND   xdoc.defendant_id = xd.defendant_id
  AND   xdoc.defendant_on_case_id = p_doc_id
  AND   xc.case_id = xh.case_id
  AND   xh.hearing_id = xdhr.hearing_id
  AND   xdoc.case_id = p_case_id --this is the case id that I have used for debugging
  --AND   xdoc.defendant_id = p_defendant_id
  AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
  AND   xdhr.hearing_start_date IS NOT NULL
  AND   trunc(xdhr.hearing_start_date) > trunc(v_date) --we already know the status of the first hearing so its only subsequent hearing to check
  ORDER BY xdhr.hearing_start_date
  ;
 
  BEGIN

  IF NVL(p_is_company,'-') = 'Y' OR p_doc_bc_status = 'N' THEN --if it is a company or the defendant_bc_status = 'N' then remand := 9 and exit procedure
    RAISE x_default_remand;
    ELSE
     
    /*Get the number of put backs "put back for sentence" for the case_id and defendant_id*/
    SELECT COUNT (*) AS NO_OF_PUT_BACKS
    INTO v_put_back_cnt
    FROM xhb_case xc
    ,    xhb_defendant_on_case xdoc
    --,    xhb_defendant xd
    ,    xhb_hearing xh
    ,    xhb_def_hearing_record xdhr
    WHERE xc.case_id = xdoc.case_id
    AND   xdoc.defendant_on_case_id = p_doc_id
    AND   xc.case_type IN ('T','S')
    --AND   xdoc.defendant_id = xd.defendant_id
    AND   xc.case_id = xh.case_id
    AND   xh.hearing_id = xdhr.hearing_id   
    AND   xdoc.case_id = p_case_id-- 621538 used in debugging
    --AND   xdoc.defendant_id = p_defendant_id --133778 used in debugging
    AND   NVL(xdhr.ref_adjournment_id,0000) IN (SELECT xrsc.ref_system_code_id 
                                      FROM xhb_ref_system_code xrsc
                                      WHERE upper(xrsc.code_type) = 'PB_TYPE' --Put backs
                                      AND   upper(xrsc.code_title) LIKE '%ADJ%' --Adjournment
                                      AND   upper(xrsc.code) IN ('S','DS','SE','SC','SS','SO','SM') --put back for sentence codes
                                      AND   xrsc.court_id = xc.court_id
                                      )
    AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
    GROUP BY xc.case_id
    ,        xdoc.defendant_id
    ;
   
   /*Get the defendant's earliest PB hearing in the current case. If there is a PB hearing, get its start_date, end_date and start and end bail_status. If
   start_bc_status is null, convert it to `N' for "not applicable". */
   
   IF v_put_back_cnt > 0 --there have been put backs for sentence
    THEN
    DBMS_OUTPUT.PUT_LINE('Put-backs found for case_id: '||p_case_id||
                            ' defendant_on_case_id: '||p_doc_id||
                            ' p_defendant: '||p_defendant_id
                            );
    --get the earliest pb hearing
    OPEN put_backs_c;
     FETCH put_backs_c 
     INTO  put_backs_r;
   
    /*There are put bucks found for the case and defendant.  There were occurances in TST2 wherby a case has duplicate records with he same start hearing date.  To get around 
      the issue that this caused with TOO_MANY_ROWS, a cursor with an order by on the date just returns the first row that is returned
    */
    IF put_backs_c%FOUND 
     THEN
      v_pb_start_date  := put_backs_r.hearing_start_date;
      v_pb_end_date    := put_backs_r.hearing_end_date;
      v_pb_hrg_start_bc_status := put_backs_r.pb_start_bail_status;
      v_pb_hrg_end_bc_status   := put_backs_r.pb_end_bail_status;    --used in calculation near end of remand b calculations
      
    DBMS_OUTPUT.PUT_LINE('v_pb_start_date: '||v_pb_start_date||
                            ' v_pb_end_date: '||v_pb_end_date||
                            ' v_pb_hrg_start_bc_status: '||v_pb_hrg_start_bc_status||
                            ' v_pb_hrg_end_bc_status: '||v_pb_hrg_end_bc_status
                            );
      
      
    END IF;
 
    CLOSE put_backs_c;
    
    IF NVL(v_pb_hrg_start_bc_status,'N') = 'N'
     THEN
      RAISE x_default_remand;
    END IF;
    
    --Hearing in which the judge passed the sentence 
    SELECT DISTINCT xh.hearing_id --query returning more than one row hence the distinct.  Unsure if this is because of the test data.  Used case_id 621485 in debugging
     ,      xh.hearing_start_date
     ,      xh.hearing_end_date
     ,      NVL(xdhr.start_bail_status,'N') 
    INTO v_sent_hrg_id 
     ,   v_sent_hrg_start_date 
     ,   v_sent_hrg_end_date        
     ,   v_sent_start_bail_status
    FROM xhb_case xc
    ,    xhb_defendant_on_case xdoc
    --,    xhb_defendant xd
    ,    xhb_hearing xh
    ,    xhb_def_hearing_record xdhr
    ,    xhb_ref_system_code xrsc
    WHERE xc.case_type IN ('T','S')
    AND   xc.case_id = xdoc.case_id
    --AND   xdoc.defendant_id = xd.defendant_id
    AND   xdoc.defendant_on_case_id = p_doc_id
    AND   xc.case_id = xh.case_id
    AND   xh.hearing_id = xdhr.hearing_id 
    AND   xdhr.ref_def_hearing_type_id = xrsc.ref_system_code_id
    AND   xrsc.court_id = xc.court_id
    AND   xrsc.code = 'SEN'
    AND   xdoc.case_id = p_case_id
    --AND   xdoc.defendant_id = p_defendant_id
    --AND   xdhr.ref_adjournment_id IS NOT NULL
    AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
    AND rownum = 1
    ;
    
   ELSE RAISE x_default_remand; --if there are no put backs
    
  END IF; --v_put_back_cnt > 0
   
    /*Get the end date of the period in which the defendants bc status must be considered.  The end date is the start date of the last hearing in which the defendant appeared in the case.
      The last hearing must not be a put back and must come after the first put back hearing.  If the select returns null, create an error record and bypass
    */
   OPEN last_hearing_c(v_pb_start_date);
     FETCH last_hearing_c 
     INTO  last_hearing_r;

    IF last_hearing_c%FOUND 
     THEN
      v_last_hrg_start_date := last_hearing_r.last_hearing_start_date;
      
      DBMS_OUTPUT.PUT_LINE ('v_last_hrg_start_date :'||v_last_hrg_start_date||
                                ' p_case_id: '||p_case_id||     
                                ' p_defendant_id: '||p_defendant_id||
                                ' p_doc_id: '||p_doc_id||
                                ' p_is_company: '||p_is_company||
                                ' p_doc_bc_status: '||p_doc_bc_status
                                ); 
      
      IF v_last_hrg_start_date IS NULL THEN
       log_event (p_cad_ref_code => 'X' --Exception
                 ,p_court_id => NULL
                 ,p_module   => 'xhb_create_dmi_cad_file_pkg.get_remand_b'
                 ,p_message  => 'v_last_hrg_start_date is null.  Parameters passed in:-'||
                                ' p_case_id: '||p_case_id||     
                                ' p_defendant_id: '||p_defendant_id||
                                ' p_doc_id: '||p_doc_id||
                                ' p_is_company: '||p_is_company||
                                ' p_doc_bc_status: '||p_doc_bc_status
                                ); 
       
       RAISE x_default_remand;
      END IF;
    END IF;
 
    CLOSE last_hearing_c;

  /* Start procedure to find out whether the defendant's B/C status changed during the period.

   Check for the existence of later hearings where the defendant's start_bc_status is different to his end_bc_status in the first PB hearing. Also check whether the 
   status was `N' for any of those hearings. 
   
   Counts are executed to ascertain this information. If any start_bc_status is `N' set remand_b to 9 for "not applicable" and exit from the Remand B procedure. 
   
   If none are `N' check the result of the other count. If greater than 0 this shows that there was a change to the start_bc_status so we set the vc_pb_bc_status_changed to 
   `Y' and bypass further validation. 
   
   A change from 'C' to 'J' or from 'J' to 'C' is not regarded as a change in bail/custody status. */

  /*get the count of hearings where the start bc status is N.  If there are any, reaise the default exception and exit*/
 BEGIN
  SELECT count(*)
  INTO v_cnt_n_bc_start_status
  FROM xhb_case xc
  ,    xhb_defendant_on_case xdoc
  --,    xhb_defendant xd
  ,    xhb_hearing xh
  ,    xhb_def_hearing_record xdhr
  WHERE (NVL(xdhr.start_bail_status,'N') = 'N' --check the start and end bail status for the occurance of N statuses
      OR NVL(xdhr.end_bail_status,'N') = 'N')
  AND   xc.case_id = xdoc.case_id
  AND   xdoc.defendant_on_case_id = p_doc_id
  AND   xc.case_type IN ('T','S')
  --AND   xdoc.defendant_id = xd.defendant_id
  AND   xc.case_id = xh.case_id
  AND   xh.hearing_id = xdhr.hearing_id   
  AND   xdoc.case_id = p_case_id
  --AND   xdoc.defendant_id = p_defendant_id 
  AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
  AND   xdhr.hearing_start_date <> v_pb_start_date --exclude the first hearing
  AND   xdhr.start_bail_status <> v_pb_hrg_end_bc_status --end status of the first hearing which is calculated above
  AND   NVL(xdhr.ref_adjournment_id,0000) IN (SELECT xrsc.ref_system_code_id 
                                    FROM xhb_ref_system_code xrsc
                                    WHERE upper(xrsc.code_type) = 'PB_TYPE' --Put backs
                                    AND   upper(xrsc.code_title) LIKE '%ADJ%' --Adjournment
                                    AND   upper(xrsc.code) IN ('S','DS','SE','SC','SS','SO','SM') --put back for sentence codes
                                    AND   xrsc.court_id = xc.court_id 
                                     )
  GROUP BY xc.case_id
  ,        xdoc.defendant_id
  ;
  
  EXCEPTION
   WHEN NO_DATA_FOUND THEN
        v_cnt_n_bc_start_status:=0;
   WHEN OTHERS THEN
        v_cnt_n_bc_start_status:=0;
  END;
  IF v_cnt_n_bc_start_status > 0 --there are hearings after the first hearing where the start bail status = 'N'
   THEN
    RAISE x_default_remand;
   ELSE --Now get a count of changes after the first hearing where the status changes excluding C to J or J to C
    WITH bail_changes AS (SELECT xdhr.start_bail_status, LAG(xdhr.start_bail_status,1,xdhr.start_bail_status) OVER (ORDER BY xdhr.hearing_start_date) PREV_STATUS
                          FROM xhb_case xc
                          ,    xhb_defendant_on_case xdoc
                          --,    xhb_defendant xd
                          ,    xhb_hearing xh
                          ,    xhb_def_hearing_record xdhr
                         WHERE xc.case_id = xdoc.case_id
                         AND   xc.case_type IN ('T','S')
                         --AND   xdoc.defendant_id = xd.defendant_id
                         AND   xdoc.defendant_on_case_id = p_doc_id
                         AND   xc.case_id = xh.case_id
                         AND   xh.hearing_id = xdhr.hearing_id
                         AND   xdoc.case_id = p_case_id
                         --AND   xdoc.defendant_id = p_defendant_id
                         AND   xdhr.ref_adjournment_id IS NOT NULL
                         AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
                         AND   xdhr.hearing_start_date IS NOT NULL
                         AND   xdhr.hearing_start_date <> v_pb_start_date --exclude the first hearing
                         AND   xdhr.start_bail_status <> v_pb_hrg_end_bc_status --end status of the first hearing which is calculated above
                         AND   NVL(xdhr.ref_adjournment_id,0000) IN (SELECT xrsc.ref_system_code_id 
                                                           FROM xhb_ref_system_code xrsc
                                                           WHERE upper(xrsc.code_type) = 'PB_TYPE' --Put backs
                                                           AND   upper(xrsc.code_title) LIKE '%ADJ%' --Adjournment
                                                           AND   upper(xrsc.code) IN ('S','DS','SE','SC','SS','SO','SM') --put back for sentence codes
                                                           AND   xrsc.court_id = xc.court_id
                                                          )
                         )
    SELECT COUNT (CASE WHEN (bail_changes.start_bail_status,PREV_STATUS) IN (('C','J'),('J','C')) THEN NULL
                        ELSE DECODE (bail_changes.start_bail_status,prev_status,NULL,1)
                  END) bail_change_count
    INTO v_pb_bc_status_changed_cnt
    FROM bail_changes;
 
  
   IF v_pb_bc_status_changed_cnt > 0
    THEN
     v_pb_bc_status_changed := 'Y';
    ELSE
     v_pb_bc_status_changed := 'N';
   END IF;

  /* Check for the existence of csu_hrg_bail records that show a change in the defendant's B/C status during a hearing. 

  If the defendant's initial status was "on bail", the mere existence of a csu_hrg_bail record shows his/her status must have changed to "in custody" at some point,
  otherwise s/he would not have needed to make an application. 
  
  If the defendant's initial status was "in custody", there must exist a successful application in order for the defendant's status to have changed to "on bail". 
  
  If no records are found, set vc_pb_bc_status_changed to `N' and continue, otherwise set it to `Y' and bypass further validation. */
  
  /*Check if the first hearing had a custody bail status*/
    IF v_pb_hrg_start_bc_status = 'C' 
     OR v_pb_hrg_end_bc_status = 'C' THEN v_custody_found := 'Y';
    END IF;
  
    FOR bail_changes_r IN bail_changes_c (v_pb_start_date) --get the first hearing date
     LOOP
      IF bail_changes_r.start_bail_status = 'C' OR bail_changes_r.end_bail_status = 'C'
       THEN v_custody_found := 'Y';
        /*If there is a C record then try and find a later record that has a 'B' status*/
        FOR i IN bail_changes_c1 (bail_changes_r.hearing_start_date)
         LOOP
          IF i.start_bail_status = 'B' OR i.end_bail_status = 'B'
           THEN 
            v_bail_found := 'Y'; --there are custody records
           END IF;
          END LOOP;
        END IF; --custody check  
       END LOOP; --custody check loop
 
    IF v_custody_found = 'Y' AND v_bail_found = 'Y' --bail status was C but changed to B later then it can be determind that the status changed
      THEN 
       v_pb_bc_status_changed := 'Y';
    END IF;
  
  /* Set vn_ho_remand_b variable. The codes are as follows:

   1 - on bail at all hearings
   2 - in custody at all hearings
   3 - on bail at the first hearing and in custody at least once in any later
       hearing
   4 - in custody at the first hearing and on bail at least once in any later
       hearing
   9 - none of the above, or not applicable */

--    vc_chkpoint := 'main_control_2_290';

        IF v_pb_hrg_end_bc_status = 'B' THEN
           IF v_pb_bc_status_changed = 'N' THEN
              v_remand := 1;
            ELSE
             v_remand := 3;
           END IF;
       ELSIF v_pb_hrg_end_bc_status in ('C', 'J') THEN
           IF v_pb_bc_status_changed = 'N' THEN
              v_remand := 2;
            ELSE
              v_remand := 4;
           END IF;
        ELSE 
           v_remand := 9;
     END IF;
    
    END IF; --v_cnt_n_bc_start_status  
   END IF; --is a company or current_bc_status = 'N'
    RETURN v_remand;
   EXCEPTION
     WHEN x_default_remand THEN RETURN 9;
     WHEN TOO_MANY_ROWS THEN RETURN 9;
     WHEN NO_DATA_FOUND THEN RETURN 9;
     WHEN OTHERS THEN RETURN 9;
   
  END get_remand_b; --remand B sub block   

/* Start procedure to find defendant's Remand C status (trial and sentence cases). The Remand C code shows whether the defendant was on bail or in
   custody between his/her date of committal and the date of the first hearing.

   If the receipt_type is a Voluntary Bill (VB) or a Transfer Certificate (TC), set Remand C to 5. If the defendant is a company or the defendant's
   B/C status at --committal was `N' or null, set Remand C to 9 for "not applicable". In all these cases bypass further validation. */

FUNCTION get_remand_c (p_case_id         IN xhb_case.case_id%TYPE
                      ,p_defendant_id    IN xhb_defendant.defendant_id%TYPE
                      ,p_doc_id          IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                      ,p_is_company      IN xhb_defendant.is_company%TYPE
                      ,p_commm_bc_status IN xhb_defendant_on_case.comm_bc_status%TYPE
                      ,p_receipt_type    IN xhb_case.receipt_type%TYPE
                      )
 RETURN NUMBER
 IS
  
  x_default_remand       EXCEPTION;
  x_vb_tc                EXCEPTION; --voluntary bill/Transfer certificate
  v_fh_start_date        DATE; --first hearing start date
  v_fh_start_bail_status xhb_def_hearing_record.start_bail_status%TYPE;
  v_fh_end_bail_status   xhb_def_hearing_record.end_bail_status%TYPE;
  v_bail_status_changed  VARCHAR2(1) := 'N';
  v_cnt_bc_status_change NUMBER; --count the number of bail status changes
  v_remand               NUMBER := 9;
  
  CURSOR first_hearing_c
   IS
   SELECT xdhr.hearing_start_date AS first_hearing_start_date
   ,      xdhr.start_bail_status
   ,      xdhr.end_bail_status
   FROM xhb_case xc
   ,    xhb_defendant_on_case xdoc
   --,    xhb_defendant xd
   ,    xhb_hearing xh
   ,    xhb_def_hearing_record xdhr
   WHERE xc.case_id = xdoc.case_id
   AND   xc.case_type IN ('T','S')
   --AND   xdoc.defendant_id = xd.defendant_id
   AND   xdoc.defendant_on_case_id = p_doc_id
   AND   xc.case_id = xh.case_id
   AND   xh.hearing_id = xdhr.hearing_id   
   AND   xdoc.case_id = p_case_id
   --AND   xdoc.defendant_id = p_defendant_id
   AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
   ORDER BY 1 ASC --only get the last date.  Will use a FETCH rather than loop to only return one row
   ;
   
   first_hearing_r first_hearing_c%ROWTYPE;

  BEGIN
  
  IF p_receipt_type IN ('VB', 'TC') THEN
       RAISE x_vb_tc;
    END IF;

  IF p_is_company = 'Y' OR  upper(nvl(p_commm_bc_status,'N')) = 'N' THEN
       RAISE x_default_remand;
    END IF;
  
  /*Get the information about the first hearing on the case*/
   OPEN first_hearing_c;
    FETCH first_hearing_c 
    INTO  first_hearing_r;
     
    IF first_hearing_c%FOUND 
      THEN
       v_fh_start_date        := first_hearing_r.first_hearing_start_date;
       v_fh_start_bail_status := first_hearing_r.start_bail_status;
       v_fh_end_bail_status   := first_hearing_r.end_bail_status;
    END IF;
 
   CLOSE first_hearing_c;

/* Find out whether the defendant's bail/custody status has changed between --committal and the first hearing. The first check 
   determines whether the b/c status at the start of the first hearing is different to that on committal. 
   
   If not we need to check for the existence of bail applicationswithin the same period which is done by executing a count. 
   The result of the count will either be 0 or 1.

   The presence of a bail application implies that the defendant was in custody at some time, either in connection with this case or in connection
   with another case that falls in the same period. If the defendant's initial status was "on bail", it means that his/her status changed at some point to
   "in custody". The presence of a successful bail application implies that the defendant's status changed at some point from "in custody" to "on bail ".

   (NB The dmi_bail_application table only contains successful bail applications - CCSDI003 filters out the unsuccessful ones. This means that
   a change from "on bail" to "in custody" will go undetected if its only manifestation is an unsuccessful bail application). If no records are
   found, set vc_comm_status_changed to `N'. */

    
    IF translate(v_fh_start_bail_status, 'J', 'C') != translate(p_commm_bc_status, 'J', 'C') 
   THEN
    v_bail_status_changed := 'Y'; --status has changed
  ELSE --Now get a count of changes after the first hearing where the status changes excluding C to J or J to C
    WITH bail_changes AS (SELECT xdhr.start_bail_status, LAG(xdhr.start_bail_status,1,xdhr.start_bail_status) OVER (ORDER BY xdhr.hearing_start_date) PREV_STATUS
                          FROM xhb_case xc
                          ,    xhb_defendant_on_case xdoc
                          --,    xhb_defendant xd
                          ,    xhb_hearing xh
                          ,    xhb_def_hearing_record xdhr
                         WHERE xc.case_id = xdoc.case_id
                         AND   xc.case_type IN ('T','S')
                         --AND   xdoc.defendant_id = xd.defendant_id
                         AND   xdoc.defendant_on_case_id = p_doc_id
                         AND   xc.case_id = xh.case_id
                         AND   xh.hearing_id = xdhr.hearing_id
                         AND   xdoc.case_id = p_case_id
                         --AND   xdoc.defendant_id = p_defendant_id
                         AND   xdhr.ref_adjournment_id IS NOT NULL
                         AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL)
                         AND   xdhr.hearing_start_date IS NOT NULL
                         AND   xdhr.hearing_start_date <> v_fh_start_date --exclude the first hearing
                         AND   xdhr.start_bail_status <> v_fh_start_bail_status --end status of the first hearing which is calculated above
                         )
    SELECT COUNT (CASE WHEN (bail_changes.start_bail_status,PREV_STATUS) IN (('C','J'),('J','C')) THEN NULL
                        ELSE DECODE (bail_changes.start_bail_status,prev_status,NULL,1)
                  END) bail_change_count
    INTO v_cnt_bc_status_change
    FROM bail_changes;
    
    IF v_cnt_bc_status_change > 0 --bc status has changed
     THEN 
      v_bail_status_changed := 'Y';
    END IF;
    
    END IF;


/* Set vn_ho_remand_c variable. The codes are as follows:

   1 - on bail throughout the period
   2 - in custody throughout the period
   3 - on bail at --committal, and in custody at least once before the first
       hearing
   4 - in custody at --committal, and on bail at least once before the first
       hearing
   9 - none of the above, or not applicable */

    x_debug := 'main_control_2_320';

    IF p_commm_bc_status = 'B' THEN
     IF v_bail_status_changed = 'N' THEN
          v_remand := 1;
       ELSE
          v_remand := 3;
     END IF;
    ELSIF p_commm_bc_status in ('C', 'J') THEN
       IF v_bail_status_changed = 'N' THEN
          v_remand := 2;
       ELSE
          v_remand := 4;
       END IF;
    ELSE
       v_remand := 9;
    END IF;
   
 RETURN v_remand;

 EXCEPTION WHEN NO_DATA_FOUND THEN RETURN 9;
           WHEN TOO_MANY_ROWS THEN RETURN 9;
           WHEN x_vb_tc THEN RETURN 5; --vb or transfer certificate
           WHEN x_default_remand THEN RETURN 9;

 END get_remand_c;
 
FUNCTION format_disposal_text (p_disp_text IN VARCHAR2)
 RETURN VARCHAR2
 
 IS
 
 v_disp   VARCHAR2(250) := p_disp_text;
 v_disp_1 VARCHAR2(250);
 v_disp_2 VARCHAR2(250);
 v_disp_3 VARCHAR2(250);
 x_return VARCHAR2(250);
 
  
 BEGIN
 
  v_disp_1 := substr(p_disp_text,1,40);
  v_disp_2 := substr(p_disp_text,41,80);
  v_disp_3 := substr(p_disp_text,81,120);
 
 
  IF v_disp_1 = v_disp_2 AND v_disp_1 <> v_disp_3
   THEN
    x_return := v_disp_1;
   ELSE
    x_return := v_disp; 
  END IF;  
 
  
  RETURN x_return;
  
  EXCEPTION WHEN OTHERS THEN RETURN NULL;
           
  
 END format_disposal_text; 

/*Appearance date is the start date of the last scheduled hearing*/
FUNCTION get_appearance_date (p_case_id      IN xhb_case.case_id%TYPE
                             ,p_doc_id       IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                             )
 RETURN VARCHAR2
 IS
 
 x_return VARCHAR2(20) := '        ';
 
 BEGIN
  
  FOR i IN (SELECT TO_CHAR(MAX(xdhr.hearing_start_date),'DDMMYYYY') as appearance_date--find the first hearing
           FROM xhb_case xc
           ,    xhb_defendant_on_case xdoc
           ,    xhb_def_hearing_record xdhr
           WHERE xc.case_id = xdoc.case_id
           AND  xdoc.defendant_on_case_id = p_doc_id
           AND  xdoc.case_id = p_case_id
           AND  xdoc.defendant_on_case_id = xdhr.defendant_on_case_id
           AND  (xdoc.obs_ind <> 'Y' OR xdoc.obs_ind IS NULL))
  LOOP
   x_return := i.appearance_date;
  END LOOP;
 
  RETURN x_return;
  
  EXCEPTION WHEN OTHERS THEN RETURN '        ';
  
 END;

FUNCTION get_proc_type_no(p_case_id      IN xhb_case.case_id%TYPE,
                           p_charge_id    IN xhb_charge.charge_id%TYPE,
                           p_offence_id   IN xhb_offence.offence_id%TYPE
                           )
RETURN VARCHAR2 IS
n_proc_type_no xhb_ref_system_code.code%TYPE;
n_ref_sys_code xhb_charge.ref_system_code_id%TYPE;    
BEGIN
    BEGIN
        SELECT ref_system_code_id INTO n_ref_sys_code FROM xhb_charge 
        WHERE charge_id = p_charge_id AND xhb_charge.case_id=p_case_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            n_ref_sys_code := NULL;
        WHEN OTHERS THEN
            n_ref_sys_code := NULL;  
    END;
    IF n_ref_sys_code IS NULL THEN
        SELECT ref_system_code_id INTO n_ref_sys_code FROM xhb_offence 
        WHERE charge_id = p_charge_id AND xhb_offence.offence_id=p_offence_id;
                
    END IF;
    IF n_ref_sys_code IS NOT NULL THEN
        SELECT code  INTO n_proc_type_no FROM xhb_ref_system_code 
        WHERE code_title='HO_PROCEEDINGS_CODE' AND
              ref_system_code_id=n_ref_sys_code;
       
    ELSE
       n_proc_type_no := NULL;
    END IF;
    
    RETURN n_proc_type_no;
EXCEPTION
     WHEN NO_DATA_FOUND THEN
          n_proc_type_no := NULL;
          RETURN n_proc_type_no;
     WHEN OTHERS THEN
          n_proc_type_no := NULL;
          RETURN n_proc_type_no;
 END get_proc_type_no;
 
 
  
FUNCTION get_plea( p_doc_id       IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                   ,p_charge_id    IN xhb_charge.charge_id%TYPE
                   ,p_offence_id   IN xhb_offence.offence_id%TYPE
                  )
RETURN VARCHAR2 IS
 v_plea VARCHAR2(1);
BEGIN
    SELECT --xp.plea_id,xp.breach_admitted,xp.ref_plea_id,
    /*   CASE WHEN xp.ref_plea_id IS NOT NULL 
       THEN
            (SELECT CASE 
                WHEN xrsc.de_code IN ('Autrefois Convict'
                            ,'Change of Plea: Not guilty to guilty (after jury sworn)'
                            ,'Change of Plea: Not guilty to guilty (no jury sworn)'
                            ,'Guilty'
                            ,'Guilty to alternative offence not charged namely'
                            ,'Guilty to lesser offence namely'
                            ) THEN '1'
                WHEN xrsc.de_code IN ('Change of Plea: Guilty to not guilty'
                            ,'Autrefois Acquit'
                            ,'Not guilty'
                            ,'Pardon') THEN '2' 
                WHEN xrsc.de_code IN ('No plea taken'
                            ,'OTHER (ENTER AS FREE TEXT)'
                            ) THEN '3'
                END PLEA
            FROM xhb_ref_system_code xrsc 
            WHERE xrsc.ref_system_code_id=xp.ref_plea_id 
              AND xrsc.code_type='PLEA')
      */
      CASE WHEN xp.ref_plea_id IS NULL AND xp.breach_admitted='Y' 
       THEN '1'
       WHEN xp.ref_plea_id IS NULL AND xp.breach_admitted='N'
       THEN '2' 
       END OFFENCE_PLEA
       INTO v_plea
    FROM    xhb_plea xp,
            xhb_defendant_charge xdc
    WHERE   xdc.defendant_on_case_id= p_doc_id--1381610--1889239 
        AND xdc.defendant_charge_id = xp.defendant_charge_id
        AND xdc.charge_id = p_charge_id 
        AND xp.ref_plea_id IS NULL  
        AND (xp.obs_ind <> 'Y' OR xp.obs_ind IS NULL)
        AND (xdc.obs_ind <> 'Y' OR xdc.obs_ind IS NULL)
        AND rownum =1 
   --     order by XP.ref_PLEA_ID
UNION
    SELECT --xp.plea_id,xp.breach_admitted,xp.ref_plea_id,
       CASE WHEN xp.ref_plea_id IS NOT NULL 
       THEN
            (SELECT CASE 
                       WHEN xrsc.de_code IN ('Autrefois Convict'
                            ,'Change of Plea: Not guilty to guilty (after jury sworn)'
                            ,'Change of Plea: Not guilty to guilty (no jury sworn)'
                            ,'Guilty'
                            ,'Guilty to alternative offence not charged namely'
                            ,'Guilty to lesser offence namely'
                            ) THEN '1'
                       WHEN xrsc.de_code IN ('Change of Plea: Guilty to not guilty'
                            ,'Autrefois Acquit'
                            ,'Not guilty'
                            ,'Pardon') THEN '2' 
                       WHEN xrsc.de_code IN ('No plea taken'
                            ,'OTHER (ENTER AS FREE TEXT)'
                            ) THEN '3'
                       END PLEA
            
            FROM xhb_ref_system_code xrsc 
             WHERE xrsc.ref_system_code_id=xp.ref_plea_id 
              AND xrsc.code_type='PLEA')
   --    WHEN xp.ref_plea_id IS NULL AND xp.breach_admitted='Y'
    --   THEN '1'
    --   WHEN xp.ref_plea_id IS NULL AND xp.breach_admitted='N'
    --   THEN '2'     
       END OFFENCE_PLEA
    --INTO v_plea
    FROM   xhb_plea xp,
           xhb_defendant_on_offence xdoo
    WHERE xdoo.defendant_on_case_id = p_doc_id--1381610--1889239
        AND xdoo.defendant_on_offence_id = xp.defendant_on_offence_id
        AND xdoo.offence_id = p_offence_id
        AND  (xdoo.obs_ind <> 'Y' OR xdoo.obs_ind IS NULL)
        AND  (xdoo.obs_ind <> 'Y' OR xdoo.obs_ind IS NULL)
        AND xp.ref_plea_id IS NOT NULL and rownum=1;
        --ORDER by xp.plea_id desc;
        
    RETURN v_plea;   
EXCEPTION
    WHEN OTHERS THEN        
        v_plea:= NULL;
        RETURN v_plea;
END get_plea;

FUNCTION get_disqualification( p_field       IN VARCHAR2 --what field to return (code or period)
                             ,p_doc_id      IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                             ,p_offence_id  IN xhb_offence.offence_id%TYPE 
                             )

RETURN VARCHAR2 IS
--p_field     VARCHAR2(15) := 'DISQ_PERIOD';--DISQ_PERIOD'; --what field to return (amount or unit)
--p_doc_id     xhb_defendant_on_case.defendant_on_case_id%TYPE:=1865090 ;--1862690;--1865090;--1857547;
--p_offence_id    xhb_offence.offence_id%TYPE:=5588043;--5576233;--5588043;--5588038;--5560950;
 
 /*Get the initial disposal data based on the defendant on case id/ defendant on offence*/
 
 CURSOR disp_c
    IS
 SELECT *
    FROM (   
    WITH disps AS
 (
 SELECT distinct dl.line_number,dl.disposal_line_id
                   ,d.disposal2_id
                   ,dl.data
                   ,upper(rdl.prompt) PROMPT
                   ,d.defendant_on_offence_id
                   ,d.defendant_on_case_id, rdt.disposal_code
                   ,count(*) 
        FROM xhb_disposal2 d 
        ,    xhb_disposal_line dl
        ,    xhb_ref_disposal_type rdt
        ,    xhb_ref_disposal_line rdl
        WHERE d.disposal2_id = dl.disposal2_id
        AND d.defendant_on_case_id = p_doc_id
        AND d.ref_disposal_type_id = rdt.ref_disposal_type_id 
        AND RDT.DISPOSAL_CODE IN ('DISQ','DISTEST','DISTOA','DISPCCA','DISQAO',
                                  'DISQVC','DOSPAS','DISREM','LEND','LENDPP') 
        AND rdl.REF_DISPOSAL_LINE_ID = dl.ref_disposal_line_id
        AND (d.obs_ind <> 'Y' OR d.obs_ind IS NULL)
        AND (dl.obs_ind <> 'Y' OR dl.obs_ind IS NULL) 
        GROUP BY dl.line_number,dl.disposal_line_id
               ,d.disposal2_id
               ,dl.data
               ,upper(rdl.prompt)
               ,d.defendant_on_offence_id
               ,d.defendant_on_case_id, rdt.disposal_code 
        UNION
        SELECT distinct dl.line_number,dl.disposal_line_id
                   ,d.disposal2_id
                   ,dl.data
                   ,upper(rdl.prompt) PROMPT
                   ,d.defendant_on_offence_id
                   ,d.defendant_on_case_id, rdt.disposal_code
                   ,count(*)
        FROM xhb_disposal2 d 
        ,    xhb_disposal_line dl
        ,    xhb_ref_disposal_type rdt
        ,    xhb_ref_disposal_line rdl
          WHERE d.disposal2_id = dl.disposal2_id
         AND d.defendant_on_offence_id IN (SELECT defendant_on_offence_id 
                                         FROM xhb_defendant_on_offence 
                                         WHERE defendant_on_case_id= p_doc_id
                                         AND offence_id= p_offence_id)
        AND d.ref_disposal_type_id = rdt.ref_disposal_type_id
        AND RDT.DISPOSAL_CODE IN ('DISQ','DISTEST','DISTOA','DISPCCA','DISQAO',
                                  'DISQVC','DOSPAS','DISREM','LEND','LENDPP') 
        AND rdl.REF_DISPOSAL_LINE_ID = dl.ref_disposal_line_id
         AND (d.obs_ind <> 'Y' OR d.obs_ind IS NULL)
        AND (dl.obs_ind <> 'Y' OR dl.obs_ind IS NULL)
     
        GROUP BY dl.line_number,dl.disposal_line_id
               ,d.disposal2_id
               ,dl.data
               ,upper(rdl.prompt)
               ,dl.line_number 
               ,d.defendant_on_offence_id
               ,d.defendant_on_case_id,rdt.disposal_code )
       SELECT line_number,disposal_line_id
           ,disposal2_id
           ,data
           ,prompt
           ,defendant_on_offence_id
           ,defendant_on_case_id
           ,disposal_code
           ,DENSE_RANK() OVER (ORDER BY disposal_line_id ASC) AS DISPOSAL_LINE
       FROM disps);
   
 x_return VARCHAR2(4000);
 v_duration xhb_ref_disposal_line.prompt%type:=NULL;
 v_unit xhb_ref_disposal_line.prompt%type:=NULL;
 v_disp_code xhb_ref_disposal_type.disposal_code%type:=NULL;

 
 BEGIN
  
  --x_debug := 'get_disposal start';
  x_return := NULL; 
  FOR disp_r IN disp_c
   LOOP
    IF upper(p_field) = 'DISQ_CODE'
     THEN
        x_return := CASE WHEN UPPER(disp_r.disposal_code) IN ('DISQ','DISTOA','DISPCCA','DISQAO','DISQVC','DDSPAS','DISREM') THEN 1
                         WHEN UPPER(disp_r.disposal_code) IN ('DISTEST') THEN 2
                         WHEN UPPER(disp_r.disposal_code) IN ('LEND','LENDPP') THEN 3
                    END;    
   
    ELSIF upper(p_field) = 'DISQ_PERIOD' 
     THEN
      v_disp_code := disp_r.disposal_code;
      IF UPPER(disp_r.disposal_code) ='LENDPP' AND disp_r.prompt IS NULL THEN 
      
        IF regexp_like(disp_r.data ,'^[^a-zA-Z]*$') THEN  
            x_return := disp_r.data;
        END IF;
      ELSIF disp_r.disposal_code='DISQ' AND disp_r.prompt like '%UNIT%' AND  disp_r.data='DELETED' THEN
        x_return := 12;
      ELSIF disp_r.prompt LIKE '%DURATION%' THEN--and disp_r.disposal_code='DISQ' 
        v_duration := disp_r.data;
      ELSIF disp_r.prompt LIKE '%UNIT%' THEN
          v_unit := disp_r.data;
      END IF;
    END IF;
    
   END LOOP; 
   
  IF (upper(p_field) = 'DISQ_PERIOD') AND x_return IS NULL THEN
    IF UPPER(v_disp_code) = 'DISTEST' THEN x_return:= NULL;
    ELSIF UPPER(v_disp_code) ='LEND' THEN x_return := NULL;
    ELSIF UPPER(v_disp_code) IN ('DISQ','DISTOA','DISPCCA','DISQAO','DISQVC','DDSPAS','DISREM') THEN
       IF UPPER(v_unit) LIKE '%MONTH%' THEN
            x_return := CASE WHEN v_duration < 6   THEN 1
                           WHEN v_duration = 6   THEN 2
                           WHEN v_duration < 12  THEN 3
                           WHEN v_duration = 12  THEN 4
                           WHEN v_duration < 24  THEN 5
                           WHEN v_duration < 36  THEN 6
                           WHEN v_duration = 36  THEN 7
                           WHEN v_duration < 48  THEN 8
                           WHEN v_duration < 60  THEN 9
                           WHEN v_duration < 120 THEN 10 
                           ELSE 11
                        END;
       ELSIF UPPER(v_unit) LIKE '%YEAR%' THEN
            x_return := CASE WHEN v_duration = 1 THEN 4
                           WHEN v_duration = 3 THEN 7
                           WHEN v_duration > 10 THEN 11
                        END;  
       ELSIF UPPER(v_unit) LIKE '%WEEK%' THEN
            IF v_duration <= 26  THEN x_return:= 1; END IF; 
       END IF;                 
    END IF;
    
    END IF;
   RETURN x_return;
  --dbms_output.put_line('value: '||x_return|| 'Code: '|| v_disp_code);
  EXCEPTION 
   WHEN no_data_found THEN
      x_return := NULL;
   WHEN OTHERS THEN
       --    dbms_output.put_line('Error: '||SQLERRM);
          log_event (p_cad_ref_code => 'E' --Error
                    ,p_court_id => NULL
                    ,p_module   => 'xhb_create_dmi_cad_file_pkg.get_disqualification'
                    ,p_message  => 'Other exception raised.  Parameters passed in:-'||
                                   ' p_field: ' ||p_field||
                                   ' p_doc_id: '||p_doc_id||
                                   ' p_offence_id: '||p_offence_id
                    ); 

 END get_disqualification;



END XHB_CREATE_DMI_CAD_FILE_PKG;
/
