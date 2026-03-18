package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.Calendar;

public class XhbList {
	private int courtId;
	private Calendar listStartDate;
	private String publishStatus;
	private String listType;
	
	public XhbList() {
		super();
	}
	public XhbList(int courtId, Calendar listStartDate, String publishStatus, String listType) {
		super();
		this.courtId = courtId;
		this.listStartDate = listStartDate;
		this.publishStatus = publishStatus;
		this.listType = listType;
	}
	/**
	 * @return the courtId
	 */
	public int getCourtId() {
		return courtId;
	}
	/**
	 * @param courtId the courtId to set
	 */
	public void setCourtId(int courtId) {
		this.courtId = courtId;
	}
	/**
	 * @return the listStartDate
	 */
	public Calendar getListStartDate() {
		return listStartDate;
	}
	/**
	 * @param listStartDate the listStartDate to set
	 */
	public void setListStartDate(Calendar listStartDate) {
		this.listStartDate = listStartDate;
	}
	/**
	 * @return the publishStatus
	 */
	public String getPublishStatus() {
		return publishStatus;
	}
	/**
	 * @param publishStatus the publishStatus to set
	 */
	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}
	/**
	 * @return the listType
	 */
	public String getListType() {
		return listType;
	}
	/**
	 * @param listType the listType to set
	 */
	public void setListType(String listType) {
		this.listType = listType;
	}
}
