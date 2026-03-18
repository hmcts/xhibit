package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Charges log Basic Value.
 * 
 */
public class ChargesLogBasicValue extends CSAbstractValue {

   private static final long serialVersionUID = 1L;
   private Integer caseId = null;
   private Integer sequenceNo = null;
   private String chargesInfo = null;
   private String obsInd = null;


    /**
     * Default constructor
     */
    public ChargesLogBasicValue() {
    }
    
    /**
     * Loaded constructor - used in general trial
     */
    public ChargesLogBasicValue(Integer caseid, Integer seqNo, String chargesInfo) {
    	setCaseId(caseid);
    	setSequenceNo(seqNo);
    	setChargesInfo(chargesInfo);
    }

    public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getChargesInfo() {
		return chargesInfo;
	}

	public void setChargesInfo(String chargesInfo) {
		this.chargesInfo = chargesInfo;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

	/**
     * Initializes the Id and version number
     * 
     * @param id
     * @param version
     */
    public ChargesLogBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    
}