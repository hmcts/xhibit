CREATE OR REPLACE PACKAGE xhb_cpp_pkg AS

  -- CPP.IWP.TP.002 and CPP.PD.HK.001
  PROCEDURE outstanding_cpp_formatting(p_results_out OUT SYS_REFCURSOR);
  PROCEDURE clear_cpp_formatting(p_cpp_formatting_id IN xhb_cpp_formatting.cpp_formatting_id%TYPE);

  -- CPP.L.002
  PROCEDURE outstanding_cpp_list(p_results_out OUT SYS_REFCURSOR);
  PROCEDURE clear_cpp_list(p_cpp_list_id IN xhb_cpp_list.cpp_list_id%TYPE);
  
  -- DSSX.0031
  PROCEDURE outstanding_cpp_staging(p_results_out OUT SYS_REFCURSOR);
  PROCEDURE clear_cpp_staging(p_cpp_staging_inbound_id IN xhb_cpp_staging_inbound.cpp_staging_inbound_id%TYPE);
  
END xhb_cpp_pkg;
/