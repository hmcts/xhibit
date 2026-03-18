CREATE OR REPLACE PACKAGE BODY xhb_cpp_pkg AS

  PROCEDURE outstanding_cpp_formatting(p_results_out OUT SYS_REFCURSOR) IS
  BEGIN
     OPEN p_results_out FOR
       SELECT xcf.cpp_formatting_id,
              xcf.format_status,
              xcf.date_in
         FROM xhb_cpp_formatting xcf
        WHERE xcf.date_in < TRUNC(SYSDATE)-1
          AND xcf.format_status IN ('NP','IP','ND');
  END outstanding_cpp_formatting;
  
  PROCEDURE clear_cpp_formatting(p_cpp_formatting_id IN xhb_cpp_formatting.cpp_formatting_id%TYPE) IS
  BEGIN
    UPDATE xhb_cpp_formatting xcf
       SET xcf.format_status = 'MS'
     WHERE xcf.cpp_formatting_id = p_cpp_formatting_id;
  END clear_cpp_formatting;
  
  PROCEDURE outstanding_cpp_list(p_results_out OUT SYS_REFCURSOR) IS
  BEGIN
     OPEN p_results_out FOR
       SELECT xcl.cpp_list_id,
              xcl.status,
              xcl.time_loaded
         FROM xhb_cpp_list xcl
        WHERE xcl.time_loaded < TRUNC(SYSDATE)-1
          AND xcl.status IN ('NP','IP');
  END outstanding_cpp_list;

  PROCEDURE clear_cpp_list(p_cpp_list_id IN xhb_cpp_list.cpp_list_id%TYPE) IS
  BEGIN
    UPDATE xhb_cpp_list xcl
       SET xcl.status = 'MS'
     WHERE xcl.cpp_list_id = p_cpp_list_id;
  END clear_cpp_list;
  
  PROCEDURE outstanding_cpp_staging(p_results_out OUT SYS_REFCURSOR) IS
  BEGIN
     OPEN p_results_out FOR
       SELECT xcs.cpp_staging_inbound_id,
              xcs.validation_status,
              xcs.processing_status,
              xcs.acknowledgment_status,
              xcs.time_loaded
         FROM xhb_cpp_staging_inbound xcs
        WHERE xcs.time_loaded < TRUNC(SYSDATE)-1
          AND (xcs.validation_status IN ('NP','VF') OR 
               xcs.processing_status IN ('NP'))
          AND xcs.acknowledgment_status <> 'AF';
  END outstanding_cpp_staging;
  
  PROCEDURE clear_cpp_staging(p_cpp_staging_inbound_id IN xhb_cpp_staging_inbound.cpp_staging_inbound_id%TYPE) IS
  BEGIN
    UPDATE xhb_cpp_staging_inbound xcs
       SET xcs.acknowledgment_status = 'AF'
     WHERE xcs.cpp_staging_inbound_id = p_cpp_staging_inbound_id;
  END clear_cpp_staging;
  
END xhb_cpp_pkg;
/

