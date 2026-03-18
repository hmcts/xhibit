package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

/**
 * RefCalendar ComplexValue class.
 * 
 * @author grewalg
 *
 */
public class RefCalendarComplexValue extends RefCalendarBasicValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8874943061620582735L;
	private CourtBasicValue court = null;

	/**
	 * Default constructor.
	 */
	public RefCalendarComplexValue() {
	}

	/**
	 * Key constructor.
	 * 
	 * @param id
	 *            Integer
	 * @param version
	 *            Integer
	 */
	public RefCalendarComplexValue(Integer id, Integer version) {
		super(id, version);
	}

/**
 * Parameter Constructor.
 * 
 * @param id
 * @param version
 * @param avail
 * @param calDate
 * @param courtId
 * @param description
 * @param sysAcAvail
 */
	public RefCalendarComplexValue(Integer id, Integer version, String avail, Date calDate, Integer courtId,
			String description, String sysAcAvail) {

		super(id, version, avail, calDate, courtId, description,  sysAcAvail);
	}

	public CourtBasicValue getCourt() {
		return court;
	}

	public void setCourt(CourtBasicValue courtBasicValue) {
		court = courtBasicValue;
	}
	public void setCourtId(CourtBasicValue newValue) {
		this.court = newValue;
	}

}