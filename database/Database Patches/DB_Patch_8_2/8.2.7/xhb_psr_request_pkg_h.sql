CREATE OR REPLACE PACKAGE xhb_psr_request_pkg AS
	   PROCEDURE get_issued_psrs(results_out      OUT SYS_REFCURSOR,
                                         court_id_in      IN  XHB_COURT.court_id%TYPE);
										 
										 
	   PROCEDURE get_unissued_psrs(results_out      OUT SYS_REFCURSOR,
                                         court_id_in      IN  XHB_COURT.court_id%TYPE);										 
END xhb_psr_request_pkg;
/
show errors
