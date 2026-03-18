package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Collection;

/**
 * <p>
 * Title: HearingListComplexValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * HearingList enitity CMR fields.
 * </p>
 * <p>
 * Adds a collection - which is in fact a single item - a Court
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class HearingListComplexValue extends HearingListBasicValue {

	private static final long serialVersionUID = 1211061163969574218L;

	private Collection sittings;

    public HearingListComplexValue() {
        super();
    }

    public HearingListComplexValue(Integer version) {
        super(version);
    }

    public HearingListComplexValue(Integer listID, Integer version) {
        super(listID, version);
    }

    public Collection getSittings() {
        return sittings;
    }

    public void setSittings(Collection sittings) {
        this.sittings = sittings;
    }
}