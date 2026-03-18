package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Basic value object used to represent the values returned from Defendant 
 * History table.
 * @author waltersn
 *
 */
public class DefendantHistoryBasicValue extends CSAbstractValue {
	
	private static final long serialVersionUID = 5894300892100764259L;
	private Integer defendantHistoryId;
	private Integer defendantId;
	private Integer crestDefendantId;
	private Integer courtId;
	private String surname;
	private String firstName;
	private String middleName;
	private Date dateOfBirth;
	private Integer gender;
	private String reasonDeleted;
	private Date dateArchived;
	
	public DefendantHistoryBasicValue() {
        super();
    }
	
    public DefendantHistoryBasicValue(Integer id, Integer version) {
        super(id, version);
    }

	public Integer getDefendantHistoryId() {
		return defendantHistoryId;
	}

	public void setDefendantHistoryId(Integer defendantHistoryId) {
		this.defendantHistoryId = defendantHistoryId;
	}

	public Integer getDefendantId() {
		return defendantId;
	}

	public void setDefendantId(Integer defendantId) {
		this.defendantId = defendantId;
	}

	public Integer getCrestDefendantId() {
		return crestDefendantId;
	}

	public void setCrestDefendantId(Integer crestDefendantId) {
		this.crestDefendantId = crestDefendantId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getReasonDeleted() {
		return reasonDeleted;
	}

	public void setReasonDeleted(String reasonDeleted) {
		this.reasonDeleted = reasonDeleted;
	}

	public Date getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}
	
	public String getGenderString() {
    	Integer genderNo = getGender();
    	if (genderNo != null) {
    		if (genderNo == 0)
    			return "C";
    		if (genderNo == 1)
    			return "M";
    		if (genderNo == 2)
    			return "F";
    	}
   		return "";
    }

	

	

}
