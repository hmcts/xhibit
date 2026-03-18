package uk.gov.courtservice.xhibit.business.vos.services.hearingschedule;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: Add Case Value Object
 * </p>
 * <p>
 * Description: This class defines the input card for bringing in the case that
 * doesn't currently exist in Xhibit
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version 1.0
 * 
 * <p>
 * Copied from AddCaseMVO
 * </p>
 */

public class AddCaseValue extends CSAbstractValue {

    private Integer caseNumber;

    private String caseType;

    private Integer courtID;

    // The following values are used when creating new 'U' cases
    private String caseTitle;

    private boolean createCaseOnCrest;

    // The following value is required to allow specific validation to
    // fire for EBW hearings and S type cases
    private String hearingType;
    
    private Integer hearingTypeId;

    /**
     * This value will be used to reference to the optimistic locking
     */
    private int updateCountMVO = -1;
    
    private static final long serialVersionUID = -2480806673097521450L;

    public AddCaseValue() {
    }

    /**
     * This method will call the super class to get the counter for the
     * optimistic locking.
     * 
     * @return int
     */
    public int getUpdateCountMVO() {
        return updateCountMVO;
    }

    /**
     * This method will call the super class to set the counter for the
     * optimistic locking.
     * 
     * @param updateCountMVO
     */
    public void setUpdateCountMVO(int updateCountMVO) {
        this.updateCountMVO = updateCountMVO;
    }

    /**
     * The Getter and Setter Methods
     */
    public Integer getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public String getCaseTitle() {
        return this.caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public boolean isCreateCaseOnCrest() {
        return this.createCaseOnCrest;
    }

    public void setCreateCaseOnCrest(boolean createCaseOnCrest) {
        this.createCaseOnCrest = createCaseOnCrest;
    }

    public String getHearingType() {
        return this.hearingType;
    }

    public void setHearingType(String hearingType) {
        this.hearingType = hearingType;
    }


	public Integer getHearingTypeId() {
		return hearingTypeId;
	}

	public void setHearingTypeId(Integer hearingTypeId) {
		this.hearingTypeId = hearingTypeId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddCaseValue [caseNumber=");
		builder.append(caseNumber);
		builder.append(", caseType=");
		builder.append(caseType);
		builder.append(", courtID=");
		builder.append(courtID);
		builder.append(", caseTitle=");
		builder.append(caseTitle);
		builder.append(", createCaseOnCrest=");
		builder.append(createCaseOnCrest);
		builder.append(", hearingType=");
		builder.append(hearingType);
		builder.append(", hearingTypeId=");
		builder.append(hearingTypeId);
		builder.append(", updateCountMVO=");
		builder.append(updateCountMVO);
		builder.append("]");
		return builder.toString();
	}
}