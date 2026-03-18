SELECT
	xhb_sh_staff.staff_name
FROM
    xhb_sh_staff
WHERE
	xhb_sh_staff.sh_staff_id = (SELECT 
                                    MAX(xhb_sh_staff.sh_staff_id)
                                FROM
                                   	xhb_sh_staff,
                                   	xhb_sched_hearing_attendee	                                	
                                WHERE
                                    xhb_sh_staff.staff_role = 'CC'
                                AND
                                    xhb_sched_hearing_attendee.sh_staff_id = xhb_sh_staff.sh_staff_id 
                                AND	 
                                    xhb_sched_hearing_attendee.scheduled_hearing_id = ?)