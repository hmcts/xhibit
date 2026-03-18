package uk.gov.courtservice.xhibit.business.entities.defendanthistory;

import javax.ejb.CreateException;
import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Defendant History Bean.
 * @author waltersn
 *
 */
abstract public class DefendantHistoryBean extends CSEntityBean {

	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer defendantHistoryId, Integer defendantId, Integer crestDefendantId,
        Integer courtId, String surname, String firstName, String middleName, Date dateOfBirth, Integer gender,
        String reasonDeleted, Date dateArchived) throws CreateException {
		setDefendantHistoryId(defendantHistoryId);
		setDefendantId(defendantId);
		setCrestDefendantId(crestDefendantId);
		setCourtId(courtId);
		setSurname(surname);
		setFirstName(firstName);
		setMiddleName(middleName);
		setDateOfBirth(dateOfBirth);
		setGender(gender);
		setReasonDeleted(reasonDeleted);
		setDateArchived(dateArchived);
		return null;
	}

	public void ejbPostCreate(Integer defendantHistoryId, Integer defendantId, Integer crestDefendantId,
        Integer courtId, String surname, String firstName, String middleName, Date dateOfBirth, Integer gender,
        String reasonDeleted, Date dateArchived) throws CreateException {
	}

	public abstract Integer getDefendantHistoryId();

	public abstract void setDefendantHistoryId(Integer defendantHistoryId);
	
	public abstract Integer getDefendantId();

	public abstract void setDefendantId(Integer defendantId);

	public abstract Integer getCrestDefendantId();

	public abstract void setCrestDefendantId(Integer crestDefendantId);
	
	public abstract Integer getCourtId();

	public abstract void setCourtId(Integer courtId);
	
	public abstract String getSurname();
	
	public abstract void setSurname(String surname);
	
	public abstract String getFirstName();
	
	public abstract void setFirstName(String firstName);
	
	public abstract String getMiddleName();
	
	public abstract void setMiddleName(String middleName);
	
	public abstract void setDateOfBirth(Date dateOfBirth);

	public abstract Date getDateOfBirth();

	public abstract void setGender(Integer gender);

	public abstract Integer getGender();
  
	public abstract String getReasonDeleted();
	
	public abstract void setReasonDeleted(String reasonDeleted);
	
	public abstract void setDateArchived(Date dateArchived);

	public abstract Date getDateArchived();

}
