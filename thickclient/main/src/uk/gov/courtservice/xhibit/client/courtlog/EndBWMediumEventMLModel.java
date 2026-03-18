package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Calendar;

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
 * Company:
 * </p>
 * 
 * @author Deepak Rath
 * @version 1.0
 */
public class EndBWMediumEventMLModel extends MediumEventMLModel{
	 
	 protected Calendar EndDate;
	 
	 protected int bWHistoryId;

	/**
	 * @return the bWHistoryId
	 */
	public int getbWHistoryId() {
		return bWHistoryId;
	}

	/**
	 * @param bWHistoryId the bWHistoryId to set
	 */
	public void setbWHistoryId(int bWHistoryId) {
		this.bWHistoryId = bWHistoryId;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return EndDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		EndDate = endDate;
	}

}
