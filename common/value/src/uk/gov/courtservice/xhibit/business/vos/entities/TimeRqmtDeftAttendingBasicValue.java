package uk.gov.courtservice.xhibit.business.vos.entities;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: TimeRqmtDeftAttendingBasicValue
 * </p>
 * <p>
 * Description: TimeRqmtDeftAttendingBasicValue is intended to represent case entities as stored
 * in the TimeRqmtDeftAttending table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */

public class TimeRqmtDeftAttendingBasicValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer defendantOnCaseId;
	private Integer caseDiaryTimeRqmtId;
	private String attending;

	public TimeRqmtDeftAttendingBasicValue() {
        super();
    }
    
    /**
     * @return java.lang.Integer
     */
	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}


	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}


	public Integer getCaseDiaryTimeRqmtId() {
		return caseDiaryTimeRqmtId;
	}


	public void setCaseDiaryTimeRqmtId(Integer caseDiaryTimeRqmtId) {
		this.caseDiaryTimeRqmtId = caseDiaryTimeRqmtId;
	}


	public String getAttending() {
		return attending;
	}


	public void setAttending(String attending) {
		this.attending = attending;
	}

	


    

    
}
