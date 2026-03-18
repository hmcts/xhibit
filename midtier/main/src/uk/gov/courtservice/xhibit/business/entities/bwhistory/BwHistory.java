package uk.gov.courtservice.xhibit.business.entities.bwhistory;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;

public interface BwHistory extends CSEntityLocal {
	public Integer getBwHistoryId();
	
	public Integer getDefendantOnCaseId();

	public  void setDefendantOnCase(DefendantOnCase defendantOnCase);

	public Date getBwIssueDate();

	public void setBwIssueDate(Date bwIssueDate);
	
	public Date getBwEndDate();

	public void setBwEndDate(Date bwEndDate);

	public String getBcStatusBwIssued();

	public void setBcStatusBwIssued(String bcStatusBwIssued);
	
	public String getBcStatusBwEnded();
	
	public void setBcStatusBwEnded(String bcStatusBwEnded);

	public String getWithdrawn();

	public void setWithdrawn(String withdrawn);

	public String getAbsconding();

	public void setAbsconding(String absconding);

	public String getObsInd();

	public void setObsInd(String obsInd);
}