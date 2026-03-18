package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseBasicValue
 * </p>
 * <p>
 * Description: CaseLeaseTimeBasicValue is intended to represent CaseLeaseTime
 * entities as stored in the XHB_TIME table.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class CaseLeaseTimeBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 2100950155138154436L;
	private Integer caseId;

    private java.util.Calendar leaseTime;

    public CaseLeaseTimeBasicValue() {
        super();
    }

    public CaseLeaseTimeBasicValue(Integer id) {
        super(id, new Integer(1));
    }

    public CaseLeaseTimeBasicValue(Integer id, Integer caseId, Calendar leaseTime) {
        this(id);
        this.caseId = caseId;
        this.leaseTime = leaseTime;

    }

    public CaseLeaseTimeBasicValue(Integer caseId, Calendar leaseTime) {
        this();
        this.caseId = caseId;
        this.leaseTime = leaseTime;

    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public void setLeaseTime(java.util.Calendar leaseTime) {
        this.leaseTime = leaseTime;
    }

    public java.util.Calendar getLeaseTime() {
        return leaseTime;
    }
}