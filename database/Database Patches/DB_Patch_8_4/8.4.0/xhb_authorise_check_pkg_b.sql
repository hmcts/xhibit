CREATE OR REPLACE PACKAGE BODY xhb_authorise_check_pkg AS

	PROCEDURE get_warnings(
		p_results_out OUT SYS_REFCURSOR,
		p_case_id     IN XHB_CASE.CASE_ID%TYPE) 
	IS
	BEGIN
		OPEN p_results_out FOR

			-- defendant on case level
			select 
				xd.defendant_id,
				xd.first_name,
				xd.surname,
				xrdl.disposal_code, 
				to_date(xdl.data,'dd-mon-yyyy') disposal_result_date, 
				xh.hearing_end_date
			from
				(select * from xhb_disposal2 where nvl(obs_ind, 'N') <> 'Y') xd2, 
				(select * from xhb_disposal_line where nvl(obs_ind, 'N') <> 'Y') xdl, 
				(select * from xhb_ref_disposal_line where nvl(obs_ind, 'N') <> 'Y') xrdl,
				xhb_hearing xh,
				(select * from xhb_defendant_on_case where nvl(obs_ind, 'N') <> 'Y') xdoc,
				xhb_defendant xd
			where 
				xdl.ref_disposal_line_id = xrdl.ref_disposal_line_id
				and upper(xrdl.prompt) = 'DATE OF RESULT' -- important to use case insensative match
				and xrdl.validation = 'V6'
				and xdl.disposal2_id = xd2.disposal2_id
				and xh.case_id = p_case_id
				and xh.case_id = xdoc.case_id
				and xd2.defendant_on_case_id = xdoc.defendant_on_case_id
				and xd.defendant_id = xdoc.defendant_id
				and decode(xrdl.validation,'V6',to_date(xdl.data,'dd-mon-yyyy')) > xh.hearing_end_date
				-- Select the hearing with the latest end date.
				and xh.hearing_end_date =
     					(select max(nvl(hearing_end_date,sysdate)) 
      					from xhb_hearing 
      					where case_id = xh.case_id)
			UNION

			-- defendant on offence level
			select 
				xd.defendant_id,
				xd.first_name,
       				xd.surname,
       				xrdl.disposal_code, 
       				to_date(xdl.data,'dd-mon-yyyy') disposal_result_date, 
       				xh.hearing_end_date 
			from
				(select * from xhb_disposal2 where nvl(obs_ind, 'N') <> 'Y') xd2, 
				(select * from xhb_disposal_line where nvl(obs_ind, 'N') <> 'Y') xdl, 
				(select * from xhb_ref_disposal_line where nvl(obs_ind, 'N') <> 'Y') xrdl,
				xhb_hearing xh,
				(select * from xhb_defendant_on_case where nvl(obs_ind, 'N') <> 'Y') xdoc,
				(select * from xhb_defendant_on_offence where nvl(obs_ind, 'N') <> 'Y') xdoo,
				xhb_defendant xd
			where 
				xdl.ref_disposal_line_id = xrdl.ref_disposal_line_id
				and upper(xrdl.prompt) = 'DATE OF RESULT' -- important to use case insensative match
				and xrdl.validation = 'V6'
				and xdl.disposal2_id = xd2.disposal2_id
				and xh.case_id = p_case_id
				and xh.case_id = xdoc.case_id
				and xd2.defendant_on_offence_id = xdoo.defendant_on_offence_id
				and xdoo.defendant_on_case_id = xdoc.defendant_on_case_id
				and xd.defendant_id = xdoc.defendant_id
				and decode(xrdl.validation,'V6',to_date(xdl.data,'dd-mon-yyyy')) > xh.hearing_end_date
				-- Select the hearing with the latest end date.
				and xh.hearing_end_date =
     					(select max(nvl(hearing_end_date,sysdate)) 
      					from xhb_hearing 
      					where case_id = xh.case_id);


	END;

END xhb_authorise_check_pkg;
/
show errors;
