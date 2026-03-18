package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CaseNonAvailDaysBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer caseId, Date startDate,
			Date endDate, String reason, String obsInd, String userDisplayName) throws CreateException {
    	setCaseId(caseId);
    	setStartDate(startDate);
    	setEndDate(endDate);
    	setReason(reason);
    	setObsInd(obsInd);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(Integer caseId, Date startDate,
			Date endDate, String reason, String obsInd, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
	public abstract Integer getNadId();
	public abstract Integer getCaseId();
	public abstract Date getStartDate();
	public abstract Date getEndDate();
	public abstract String getReason();
	public abstract String getObsInd();
	
	public abstract void setNadId(Integer nadId);
	public abstract void setCaseId(Integer caseId);
	public abstract void setStartDate(Date startDate);
	public abstract void setEndDate(Date endDate);
	public abstract void setReason(String reason);
	public abstract void setObsInd(String obsInd);
}