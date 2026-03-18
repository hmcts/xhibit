package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Collection;

/**
 * <p>
 * Title: FixtureDeftAttendingComplexValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */
public class FixtureDeftAttendingComplexValue extends FixtureDeftAttendingBasicValue {

	private static final long serialVersionUID = 1L;
	private Collection defendants;

	public Collection getDefendants() {
		return defendants;
	}

	public void setDefendants(Collection defendants) {
		this.defendants = defendants;
	}

	public FixtureDeftAttendingComplexValue() {
        super();
    }

    public FixtureDeftAttendingComplexValue(Integer id, Integer version) {
        super(id, version);
    }
}