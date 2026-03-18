create  or
replace view
XHB_COUNSEL_FACILITIES_VIEW  as
    	select distinct
		sitting.court_room_id                         	COURT_ROOM_ID,    
		sitting.is_floating                           	IS_FLOATING,
		hearing_list.start_date                       	START_DATE,
		hearing_list.court_id                       	COURT_ID,
		court_room.description                       	COURT_ROOM_DESCRIPTION,    
		court_room.display_name                       	COURT_ROOM_DISPLAY_NAME, 
		court_room.court_room_name                    	COURT_ROOM_NAME,   
		court_room.crest_court_room_no                	CREST_COURT_ROOM_NO,
		ref_hearing_type.hearing_type_code            	HEARING_TYPE_CODE,    
		ref_hearing_type.hearing_type_desc            	HEARING_TYPE_DESC,
		sh_staff.staff_name                           	STAFF_NAME, 
		sh_staff.staff_role                           	STAFF_ROLE,
		scheduled_hearing.scheduled_hearing_id        	SCHEDULED_HEARING_ID, 
		scheduled_hearing.original_time               	SH_ORIGINAL_TIME, 
		scheduled_hearing.not_before_time             	SH_NOT_BEFORE_TIME,
		sched_hearing_defendant.sched_hear_def_id     	SCHED_HEAR_DEF_ID,
		defendant_on_case.defendant_on_case_id        	DEFENDANT_ON_CASE_ID, 
		defendant_on_case.is_masked                   	IS_MASKED, 
		defendant_on_case.masked_name 			MASKED_NAME,
		defendant.defendant_id				DEFENDANT_ID, 
		defendant.first_name 				DEF_FIRST_NAME, 
		defendant.middle_name 				DEF_MIDDLE_NAME, 
		defendant.surname 				DEF_SURNAME, 
		defendant.initials 				DEF_INITIALS,
		xcase.case_id					CASE_ID, 
		xcase.case_number 				CASE_NUMBER, 
		xcase.case_type					CASE_TYPE, 
		xcase.case_sub_type				CASE_SUB_TYPE,
		sh_leg_rep.sh_leg_rep_id			SH_LEG_REP_ID, 
		sh_leg_rep.legal_role				LEGAL_ROLE,
		legal_rep.ref_legal_rep_id			REF_LEGAL_REP_ID, 
		legal_rep.first_name 				LEG_REP_FIRST_NAME, 
		legal_rep.middle_name 				LEG_REP_MIDDLE_NAME, 
		legal_rep.surname 				LEG_REP_SURNAME, 
		legal_rep.title 				LEG_REP_TITLE, 
		legal_rep.initials 				LEG_REP_INITIALS, 
		legal_rep.legal_rep_type			LEGAL_REP_TYPE,
		advocate.ref_advocate_id			REF_ADVOCATE_ID,
		chamber.ref_chamber_id				REF_CHAMBER_ID, 
		chamber.firm_name 				CHAMBER_FIRM_NAME,
		adv_address.address_id 				ADV_ADDRESS_ID, 
		adv_address.address_1 				ADV_ADDRESS_1,
		adv_address.address_2 				ADV_ADDRESS_2,
		adv_address.address_3 				ADV_ADDRESS_3,
		adv_address.address_4  				ADV_ADDRESS_4,
		adv_address.town  				ADV_TOWN,
		adv_address.county 				ADV_COUNTY,
		adv_address.country 				ADV_COUNTRY,
		adv_address.postcode 				ADV_POSTCODE,
		solicitor.solicitor_id				SOLICITOR_ID,
		solfirm.ref_solicitor_firm_id			REF_SOLICITOR_FIRM_ID, 
		solfirm.solicitor_firm_name			SOLICITOR_FIRM_NAME,
		sol_address.address_id 				SOL_ADDRESS_ID, 
		sol_address.address_1  				SOL_ADDRESS_1,
		sol_address.address_2  				SOL_ADDRESS_2,
		sol_address.address_3  				SOL_ADDRESS_3,
		sol_address.address_4  				SOL_ADDRESS_4,
		sol_address.town  				SOL_TOWN,
		sol_address.county  				SOL_COUNTY,
		sol_address.country  				SOL_COUNTRY,
		sol_address.postcode 				SOL_POSTCODE
             from 
		xhb_hearing_list             HEARING_LIST,
		xhb_sitting                  SITTING,
		xhb_court_room               COURT_ROOM,
		xhb_scheduled_hearing        SCHEDULED_HEARING,
		xhb_hearing                  HEARING,
		xhb_ref_hearing_type         REF_HEARING_TYPE,
		xhb_sched_hearing_attendee   SH_ATTENDEE,
		xhb_sh_staff                 SH_STAFF,
		xhb_sched_hearing_defendant  SCHED_HEARING_DEFENDANT,
		xhb_defendant_on_case        DEFENDANT_ON_CASE,
		xhb_defendant                DEFENDANT,
		xhb_case                     XCASE,
		xhb_sh_leg_rep               SH_LEG_REP,
		xhb_ref_legal_representative LEGAL_REP,
		xhb_ref_advocate             ADVOCATE,
		xhb_ref_chamber              CHAMBER,
		xhb_ref_solicitor            SOLICITOR,
		xhb_ref_solicitor_firm       SOLFIRM,
		xhb_address                  ADV_ADDRESS,
		xhb_address                  SOL_ADDRESS		
             where
		( hearing_list.list_id = sitting.list_id ) and
		( sitting.court_room_id = court_room.court_room_id ) and
		( scheduled_hearing.sitting_id = sitting.sitting_id ) and
		( scheduled_hearing.scheduled_hearing_id = sched_hearing_defendant.scheduled_hearing_id(+) ) and
		( scheduled_hearing.hearing_id = hearing.hearing_id ) and
		( scheduled_hearing.scheduled_hearing_id = sh_attendee.scheduled_hearing_id(+) ) and	
		( sh_attendee.sh_staff_id = sh_staff.sh_staff_id(+) ) and						   
		( hearing.case_id = xcase.case_id ) and
		( hearing.ref_hearing_type_id = ref_hearing_type.ref_hearing_type_id ) and
		( sched_hearing_defendant.defendant_on_case_id = defendant_on_case.defendant_on_case_id(+) ) and	
		( defendant_on_case.defendant_id = defendant.defendant_id(+) ) and
		( scheduled_hearing.scheduled_hearing_id = sh_leg_rep.scheduled_hearing_id(+)) and
		( sh_leg_rep.ref_legal_rep_id = legal_rep.ref_legal_rep_id(+) ) and	
		( legal_rep.ref_legal_rep_id = advocate.ref_legal_rep_id(+) ) and
		( advocate.ref_chamber_id = chamber.ref_chamber_id(+) ) and
		( chamber.address_id = adv_address.address_id(+) ) and
		( legal_rep.ref_legal_rep_id = solicitor.ref_legal_rep_id(+) ) and
		( solicitor.ref_solicitor_firm_id = solfirm.ref_solicitor_firm_id(+) ) and
		( solfirm.address_id = sol_address.address_id(+) ) ;

show errors