package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class CaseProsecutorAgencyComplexValue extends CaseProsecutorAgencyBasicValue {
	private static final long serialVersionUID = 6080958753172076906L;
    private CaseBasicValue caze;

    public CaseProsecutorAgencyComplexValue() {
    }

    public CaseProsecutorAgencyComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    public CaseBasicValue getCaze() {
        return caze;
    }

    public void setCaze(CaseBasicValue caze) {
        this.caze = caze;
    }
}