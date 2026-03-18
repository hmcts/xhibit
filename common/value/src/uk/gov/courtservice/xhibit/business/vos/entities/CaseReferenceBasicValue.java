package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version 1.0
 *  * @version $Id: CaseReferenceBasicValue.java,v 1.3 2006/06/05 12:28:30 bzjrnl Exp $
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class CaseReferenceBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 6862386481800734509L;
    private Integer caseId;

    private Integer reportingRestrictions;

    public CaseReferenceBasicValue() {

    }

    public CaseReferenceBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    public CaseReferenceBasicValue(Integer id, Integer version, Integer caseId, Integer reportingRestrictions) {
        this(id, version);
        this.caseId = caseId;
        this.reportingRestrictions = reportingRestrictions;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public void setReportingRestrictions(Integer reportingRestrictions) {
        this.reportingRestrictions = reportingRestrictions;
    }

    public Integer getReportingRestrictions() {
        return reportingRestrictions;
    }

}