CREATE OR REPLACE PACKAGE XHIBIT.XHB_CREATE_DMI_CAD_FILE_PKG /* AUTHID CURRENT_USER */ AS

x_checkpoint VARCHAR2(100);
v_run_hist_id NUMBER; --global place holder for the error log capturing.  This is the id of the history log

PROCEDURE log_event (p_cad_ref_code IN xhb_dmi_cad_ref_code.dmi_cad_ref_code_short_name%TYPE DEFAULT NULL
                    ,p_court_id     IN xhb_court.court_id%TYPE
                    ,p_module       IN VARCHAR2 --the procedure name that the insert has come from
                    ,p_message      IN VARCHAR2 DEFAULT NULL
                    );
                    
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
);


PROCEDURE build_clob  (p_date_from  IN  DATE DEFAULT NULL --date field not mandatory.  Will default to last calendar month if not populated
                      ,p_date_to    IN  DATE DEFAULT NULL --date field not mandatory.  Will default to last calendar month if not populated
                      ,p_err_code   OUT NUMBER     --error code handling
                      ,p_err_msg    OUT NOCOPY VARCHAR2   --error message
                      ,p_court_id   IN  xhb_court.court_id%TYPE DEFAULT NULL   --need to check if this will be the court ID or name
                      );
                      
FUNCTION get_batch (p_court     IN VARCHAR2
                   ,p_month     IN DATE
                   ) 
 RETURN VARCHAR2;
 
FUNCTION get_serial (p_case_number IN xhb_case.case_number%TYPE
                    ,p_case_type   IN xhb_case.case_type%TYPE
                    ,p_row         IN VARCHAR2 DEFAULT 'HEAD') --used in head and line rows.  Returned differently
 RETURN VARCHAR2;

FUNCTION get_disposal (p_field     IN VARCHAR2 --what field to return (amount or unit)
                      ,p_doc_id    IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                 --     ,p_doo_id    IN xhb_defendant_on_offence.defendant_on_offence_id%TYPE
                      ,p_offence_id   IN xhb_offence.offence_id%TYPE
                      ,p_disp      IN NUMBER --disposal 1,2,3,4
                      ) 

 RETURN VARCHAR2;
 
 FUNCTION get_remand_a (p_case_id      IN xhb_case.case_id%TYPE
                       ,p_defendant_id IN xhb_defendant.defendant_id%TYPE
                       ,p_doc_id       IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                       )
 RETURN NUMBER;
 
 FUNCTION get_remand_b (p_case_id      IN xhb_case.case_id%TYPE
                       ,p_defendant_id IN xhb_defendant.defendant_id%TYPE
                       ,p_doc_id       IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                       ,p_is_company   IN xhb_defendant.is_company%TYPE
                       ,p_doc_bc_status IN xhb_defendant_on_case.current_bc_status%TYPE
                       )
 RETURN NUMBER;     
 
FUNCTION get_remand_c (p_case_id         IN xhb_case.case_id%TYPE
                      ,p_defendant_id    IN xhb_defendant.defendant_id%TYPE
                      ,p_doc_id          IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                      ,p_is_company      IN xhb_defendant.is_company%TYPE
                      ,p_commm_bc_status IN xhb_defendant_on_case.comm_bc_status%TYPE
                      ,p_receipt_type    IN xhb_case.receipt_type%TYPE
                      )
 RETURN NUMBER;
 
FUNCTION format_disposal_text (p_disp_text IN VARCHAR2)
 RETURN VARCHAR2;
 
FUNCTION get_appearance_date (p_case_id      IN xhb_case.case_id%TYPE
                             ,p_doc_id       IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                             )
 RETURN VARCHAR2;

FUNCTION get_proc_type_no(  p_case_id      IN xhb_case.case_id%TYPE
                           ,p_charge_id    IN xhb_charge.charge_id%TYPE
                           ,p_offence_id   IN xhb_offence.offence_id%TYPE
                           )
 RETURN VARCHAR2;
 
FUNCTION get_plea( p_doc_id       IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                   ,p_charge_id    IN xhb_charge.charge_id%TYPE
                   ,p_offence_id   IN xhb_offence.offence_id%TYPE
                  )
 RETURN VARCHAR2;

 FUNCTION get_disqualification(p_field       IN VARCHAR2 --what field to return (code or period)
                             ,p_doc_id      IN xhb_defendant_on_case.defendant_on_case_id%TYPE
                             ,p_offence_id  IN xhb_offence.offence_id%TYPE 
                             )
 RETURN VARCHAR2;                      
  
END XHB_CREATE_DMI_CAD_FILE_PKG;
/
