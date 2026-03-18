CREATE OR REPLACE PACKAGE BODY xhb_crestformsbtof_pkg AS
    --
    -- Return the offences for the case, defendant and charge type
    --
	PROCEDURE number_of_offences(number_out   OUT SYS_REFCURSOR,
       				p_case_id_in     IN  XHB_DEFENDANT_ON_CASE.case_id%TYPE,
                                p_def_on_case_id IN  XHB_DEFENDANT_ON_CASE.defendant_id%TYPE,
                                p_charge_type    IN  XHB_CHARGE.charge_type%TYPE)
        AS
        BEGIN
	    OPEN number_out FOR
		SELECT
    			xhb_offence.offence_id
		FROM 
		    xhb_defendant_on_case,
		    xhb_charge,
		    xhb_offence,
		    xhb_defendant_on_offence
		WHERE 
		    xhb_defendant_on_case.case_id = p_case_id_in
		AND
		    xhb_defendant_on_case.defendant_id = p_def_on_case_id
		AND
		    xhb_charge.case_id = xhb_defendant_on_case.case_id
		AND
		    xhb_offence.charge_id = xhb_charge.charge_id 
		AND
		    xhb_defendant_on_offence.offence_id = xhb_offence.offence_id
		AND
		    xhb_defendant_on_offence.defendant_on_case_id = xhb_defendant_on_case.defendant_on_case_id
		AND
		    xhb_charge.charge_type = p_charge_type
		AND 
		   (xhb_charge.obs_ind IS NULL OR xhb_charge.obs_ind = 'N') 
		AND 
		   (xhb_offence.obs_ind IS NULL OR xhb_offence.obs_ind = 'N') 
		AND 
		   (xhb_defendant_on_offence.obs_ind IS NULL OR xhb_defendant_on_offence.obs_ind = 'N');
        END number_of_offences;
        
    --
    -- Query the XHB_DISPOSAL2 table to retrieve the number of unrelated disposals
    --
       PROCEDURE number_of_unrelated_disposals(number_out   OUT SYS_REFCURSOR,
                                p_def_on_case_id IN  XHB_DEFENDANT_ON_CASE.defendant_id%TYPE)
        AS
        BEGIN
	    OPEN number_out FOR
		SELECT
    			disp.disposal2_id
		FROM 
    			XHB_DISPOSAL2 disp
		WHERE 
    			disp.defendant_on_case_id = p_def_on_case_id
		AND
    			disp.obs_ind != 'Y';
        END number_of_unrelated_disposals;
END xhb_crestformsbtof_pkg;
/
show errors