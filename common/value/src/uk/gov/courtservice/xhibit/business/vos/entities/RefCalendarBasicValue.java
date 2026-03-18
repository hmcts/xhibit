package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * RefCalendar BasicValue class.
 * 
 * @author grewalg
 *
 */
public class RefCalendarBasicValue extends CSAbstractValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1991071070467878848L;

	private String avail = null;

	private Date calDate = null;

	private Integer courtId = null;

	private String description = null;

	private String sysAcAvail = null;

	private Integer version = null;

	/**
	 * Default constructor.
	 */
	public RefCalendarBasicValue() {
	}

	/**
	 * Key constructor.
	 * 
	 * @param id
	 *            Integer
	 * @param version
	 *            Integer
	 */
	public RefCalendarBasicValue(Integer id, Integer version) {
		super(id, version);
	}

/**
 * Parameter Constructor
 * 
 * @param id
 * @param version
 * @param avail
 * @param calDate
 * @param courtId
 * @param description
 * @param sysAcAvail
 */
	public RefCalendarBasicValue(Integer id, Integer version, String avail, Date calDate, Integer courtId,
			String description, String sysAcAvail) {

		this(id, version);
		this.avail = avail;
		this.calDate = calDate;
		this.courtId = courtId;
		this.description = description;
		this.sysAcAvail = sysAcAvail;
		this.version = version;
	}

	public String getAvail() {
		return avail;
	}

	public void setAvail(String avail) {
		this.avail = avail;
	}

	public Date getCalDate() {
		return calDate;
	}

	public void setCalDate(Date calDate) {
		this.calDate = calDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSysAcAvail() {
		return sysAcAvail;
	}

	public void setSysAcAvail(String sysAcAvail) {
		this.sysAcAvail = sysAcAvail;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Integer getCourtId() {
		return courtId;
	}
}