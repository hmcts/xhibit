package uk.gov.courtservice.xhibit.business.entities.defendanthistory;


import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * DefendantHistory
 * @author waltersn
 *
 */
public interface DefendantHistory extends CSEntityLocal {

	public Integer getDefendantHistoryId();

	public void setDefendantHistoryId(Integer defendantHistoryId);
	
	public Integer getDefendantId();

	public void setDefendantId(Integer defendantId);

	public Integer getCrestDefendantId();

	public void setCrestDefendantId(Integer crestDefendantId);
	
	public Integer getCourtId();

	public void setCourtId(Integer courtId);
	
	public String getSurname();
	
	public void setSurname(String surname);
	
	public String getFirstName();
	
	public void setFirstName(String firstName);
	
	public String getMiddleName();
	
	public void setMiddleName(String middleName);
	
	public void setDateOfBirth(Date dateOfBirth);
	
	public Date getDateOfBirth();
	
	public void setGender(Integer gender);
	
	public Integer getGender();
	
	public String getReasonDeleted();
	
	public void setReasonDeleted(String reasonDeleted);
	
	public void setDateArchived(Date dateArchived);

	public Date getDateArchived();

	

}
