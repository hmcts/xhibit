CREATE OR REPLACE PACKAGE xhb_crestformsbtof_pkg AS
       PROCEDURE number_of_offences(number_out   OUT SYS_REFCURSOR,
       				p_case_id_in     IN  XHB_DEFENDANT_ON_CASE.case_id%TYPE,
                                p_def_on_case_id IN  XHB_DEFENDANT_ON_CASE.defendant_id%TYPE,
                                p_charge_type    IN  XHB_CHARGE.charge_type%TYPE);

       PROCEDURE number_of_unrelated_disposals(number_out   OUT SYS_REFCURSOR,
                                p_def_on_case_id IN  XHB_DEFENDANT_ON_CASE.defendant_id%TYPE);
END xhb_crestformsbtof_pkg;
/
show errors