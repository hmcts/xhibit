package uk.gov.courtservice.xhibit.business.entities.bwhistory;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;

abstract public class BwHistoryBean extends CSEntityBean implements EntityBean {
	
	public Integer ejbCreate(DefendantOnCase defendantOnCase, Date bwIssueDate, Date bwEndDate, 
							   String bcStatusBwIssued, String bcStatusBwEnded, String withdrawn, String absconding,
							   String obsInd, String userDisplayName) throws CreateException {
		
		setBwIssueDate(bwIssueDate);
		setBwEndDate(bwEndDate);
		setBcStatusBwIssued(bcStatusBwIssued);
		setBcStatusBwEnded(bcStatusBwEnded);
		setWithdrawn(withdrawn);
		setAbsconding(absconding);
		setObsInd(obsInd);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		
		return null;
	}
	
	@SuppressWarnings("unused")
	public void ejbPostCreate(DefendantOnCase defendantOnCase, Date bwIssueDate, Date bwEndDate, 
			   String bcStatusBwIssued, String bcStatusBwEnded, String withdrawn, String absconding,
			   String obsInd, String userDisplayName) throws CreateException {
		
		setDefendantOnCase(defendantOnCase);
	}
	
	// ------------------------------CMP
	// Fields------------------------------------
	
	public abstract void setBwHistoryId(Integer bwHistoryId);
	
	public abstract Integer getBwHistoryId();

	public abstract Integer getDefendantOnCaseId();

	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);

	public abstract Date getBwIssueDate();

	public abstract void setBwIssueDate(Date bwIssueDate);
	
	public abstract Date getBwEndDate();

	public abstract void setBwEndDate(Date bwEndDate);

	public abstract String getBcStatusBwIssued();

	public abstract void setBcStatusBwIssued(String bcStatusBwIssued);
	
	public abstract String getBcStatusBwEnded();
	
	public abstract void setBcStatusBwEnded(String bcStatusBwEnded);

	public abstract String getWithdrawn();

	public abstract void setWithdrawn(String withdrawn);

	public abstract String getAbsconding();

	public abstract void setAbsconding(String absconding);

	public abstract String getObsInd();

	public abstract void setObsInd(String obsInd);
	
	public abstract DefendantOnCase getDefendantOnCase(); 
	
	public abstract void setDefendantOnCase(DefendantOnCase defendantOnCase);
	
}