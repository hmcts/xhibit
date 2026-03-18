package uk.gov.courtservice.xhibit.business.vos.services.todaysschedule;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class RespondantValue extends PersonValue {
	private static final long serialVersionUID = 5418151788048319787L;
    public RespondantValue() {
    }

    public RespondantValue(Integer ID, String firstName, String middleName, String surname) {
        super(ID, firstName, middleName, surname);
    }
}