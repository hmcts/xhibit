package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: LinkedHearingRecordValues
 * </p>
 * <p>
 * Description: This is a value object that will store all the hearing record
 * data for each linked hearing record.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class LinkedHearingRecordValues extends CSAbstractValue {
	private static final long serialVersionUID = 595773530388947072L;
    private Collection hearingRecordValues;

    public LinkedHearingRecordValues() {
    }

    public void setHearingRecordValues(Collection hearingRecordValues) {
        this.hearingRecordValues = hearingRecordValues;
    }

    public Collection getHearingRecordValues() {
        return hearingRecordValues;
    }

}