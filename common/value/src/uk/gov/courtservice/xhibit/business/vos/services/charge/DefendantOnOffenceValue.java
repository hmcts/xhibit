package uk.gov.courtservice.xhibit.business.vos.services.charge;

import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: midtier to client value object for holding offenceId,
 * <b>defendantId</b>, crn and other defendantOnOffence attributes
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: DefendantOnOffenceValue.java,v 1.1 2004/04/21 09:40:35 pznwc5
 *          Exp $
 */
public class DefendantOnOffenceValue extends CSAbstractValue {
    private Integer offenceId;

    private Integer defendantId;

    private String crn;
           
    private Calendar dateOfArrest;
    private Calendar dateOfCharge;
    private Integer sequenceNo;
    private String isCommittedOnBail;
    private String interimD20;
    private Integer darRetentionPolicyId;
    private String obsInd;
    
    private static final long serialVersionUID = 1672315217860678511L;
   

    public String getObsInd() {
        return obsInd;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public Calendar getDateOfArrest() {
        return dateOfArrest;
    }

    public void setDateOfArrest(Calendar dateOfArrest) {
        this.dateOfArrest = dateOfArrest;
    }

    public Calendar getDateOfCharge() {
        return dateOfCharge;
    }

    public void setDateOfCharge(Calendar dateOfCharge) {
        this.dateOfCharge = dateOfCharge;
    }

    public String getIsCommittedOnBail() {
        return isCommittedOnBail;
    }

    public void setIsCommittedOnBail(String isCommittedOnBail) {
        this.isCommittedOnBail = isCommittedOnBail;
    }
    
    public String getInterimD20(){
    	return interimD20;
    }
    
    public void setInterimD20(String interimD20){
    	this.interimD20 = interimD20;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public DefendantOnOffenceValue(Integer id, Integer offenceId, Integer defendantId, String crn, 
            Calendar dateOfArrest, Calendar dateOfCharge, Integer sequenceNo, String isComittedOnBail, String interimD20) {
        this(offenceId,defendantId, crn);
        this.setId(id); 
        this.dateOfArrest = dateOfArrest;
        this.dateOfCharge = dateOfCharge;
        this.sequenceNo = sequenceNo;
        this.isCommittedOnBail = isComittedOnBail;
        this.interimD20 = interimD20;
    }
    
    public DefendantOnOffenceValue(Integer offenceId, Integer defendantId, String crn, 
            Calendar dateOfArrest, Calendar dateOfCharge, Integer sequenceNo, String isComittedOnBail, String interimD20) {
        this(offenceId,defendantId, crn);
        this.dateOfArrest = dateOfArrest;
        this.dateOfCharge = dateOfCharge;
        this.sequenceNo = sequenceNo;
        this.isCommittedOnBail = isComittedOnBail;
        this.interimD20 = interimD20;
    }
    
    public DefendantOnOffenceValue(Integer offenceId, Integer defendantId, String crn) {
        this.offenceId = offenceId;
        this.defendantId = defendantId;
        this.crn = crn;
    }

    public Integer getOffenceId() {
        return offenceId;
    }

    public void setOffenceId(Integer offenceId) {
        this.offenceId = offenceId;
    }

    public void setDefendantId(Integer defendantId) {
        this.defendantId = defendantId;
    }

    public Integer getDefendantId() {
        return defendantId;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getCrn() {
        return crn;
    }

	public Integer getDarRetentionPolicyId() {
		return darRetentionPolicyId;
	}

	public void setDarRetentionPolicyId(Integer darRetentionPolicyId) {
		this.darRetentionPolicyId = darRetentionPolicyId;
	}
}