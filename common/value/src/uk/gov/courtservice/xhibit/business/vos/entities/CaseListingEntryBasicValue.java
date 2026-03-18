package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseListingEntryBasicValue
 * </p>
 * <p>
 * Description: CaseListingEntryBasicValue is intended to represent case entities as stored
 * in the CaseListingEntry table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */

public class CaseListingEntryBasicValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private Integer caseListingEntryId;
    private Integer caseId;
    private Integer refJudgeTypeId;
    private Integer courtId;
	private Integer courtSiteId;
	private Integer judgeId;
	private String obsInd;
 
	public CaseListingEntryBasicValue() {
        super();
    }
	
    public CaseListingEntryBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
    public Integer getCaseListingEntryId() {
		return caseListingEntryId;
	}

	public void setCaseListingEntryId(Integer caseListingEntryId) {
		this.caseListingEntryId = caseListingEntryId;
	}

    public Integer getCaseId() {
        return caseId;
    }
    
    public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
    
	public Integer getRefJudgeTypeId() {
		return refJudgeTypeId;
	}

	public void setRefJudgeTypeId(Integer refJudgeTypeId) {
		this.refJudgeTypeId = refJudgeTypeId;
	}

    public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

    public Integer getCourtSiteId() {
		return courtSiteId;
	}

	public void setCourtSiteId(Integer courtSiteId) {
		this.courtSiteId = courtSiteId;
	}

	public Integer getJudgeId() {
		return judgeId;
	}

	public void setJudgeId(Integer judgeId) {
		this.judgeId = judgeId;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}    
}
