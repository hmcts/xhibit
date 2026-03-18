package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.Date;

public class RefCalendarValue {
	private Integer id;
	private String avail;
	private String sysAcAvail;
	private Date calDate;
	private String description;
	private Boolean isModified;
	private Integer version;

	public RefCalendarValue(Integer id, String avail, String sysAcAvail, Date calDate, String description,
			Boolean isNew, Boolean modified, Integer version) {
		super();
		this.id = id;
		this.avail = avail;
		this.sysAcAvail = sysAcAvail;
		this.calDate = calDate;
		this.description = description;
		this.isModified = modified;
		this.version = version;
	}

	public RefCalendarValue(String avail, String sysAcAvail, Date calDate, String description) {
		super();
		this.avail = avail;
		this.sysAcAvail = sysAcAvail;
		this.calDate = calDate;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the avail
	 */
	public String getAvail() {
		return avail;
	}

	/**
	 * @param avail
	 *            the avail to set
	 */
	public void setAvail(String avail) {
		this.avail = avail;
	}

	/**
	 * @return the sysAcAvail
	 */
	public String getSysAcAvail() {
		return sysAcAvail;
	}

	/**
	 * @param sysAcAvail
	 *            the sysAcAvail to set
	 */
	public void setSysAcAvail(String sysAcAvail) {
		this.sysAcAvail = sysAcAvail;
	}

	/**
	 * @return the calDate
	 */
	public Date getCalDate() {
		return calDate;
	}

	/**
	 * @param calDate
	 *            the calDate to set
	 */
	public void setCalDate(Date calDate) {
		this.calDate = calDate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the modified
	 */
	public Boolean isModified() {
		return isModified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(Boolean isModified) {
		this.isModified = isModified;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "RefCalendarValue [id=" + id + ", avail=" + avail + ", sysAcAvail=" + sysAcAvail + ", calDate=" + calDate
				+ ", description=" + description + ", isModified=" + isModified + ", version=" + version + "]";
	}
	
}
