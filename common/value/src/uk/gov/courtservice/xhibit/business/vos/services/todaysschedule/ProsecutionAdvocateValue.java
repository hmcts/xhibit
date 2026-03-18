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

public class ProsecutionAdvocateValue extends PersonValue {
	private static final long serialVersionUID = 4065073946666346165L;
    public ProsecutionAdvocateValue() {
    }

    public ProsecutionAdvocateValue(Integer ID, String firstName, String middleName, String surname) {
        super(ID, firstName, middleName, surname);
    }
}