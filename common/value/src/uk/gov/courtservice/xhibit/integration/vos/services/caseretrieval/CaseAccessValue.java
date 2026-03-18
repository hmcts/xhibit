//Source file: D:\\SANDIP\\COURT SERVICES\\XHIBITVSS\\04_SYSTEM\\14_DESIGN_&_DEVELOPMENT_CODE\\SRC\\uk\\gov\\courtservice\\xhibit\\integration\\CaseAccessValue.java

package uk.gov.courtservice.xhibit.integration.vos.services.caseretrieval;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseAccessValue
 * </p>
 * <p>
 * Description: CaseAccessValue is intended to represent the status and access
 * information about a case.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
public class CaseAccessValue extends CSAbstractValue {
	
	static final long serialVersionUID = 1644236191849879877L;

    private Integer caseId;

    /**
     * Status
     */
    private String chargeImportIndicator;

    private Long leaseTimeDuration;

    /**
     * Date and time of refresh
     */
    /**
     * todo: report will be in xml format therefore change to appropriate type
     */
    private String differenceReport;

    private java.util.Calendar leaseTime;

    // (remove comment to activate xml code) private Document
    // differenceReport;

    /**
     * @roseuid 3DDBB36400E2
     */
    public CaseAccessValue() {

    }

    // delete constructor to activate xml code
    public CaseAccessValue(Integer caseId, String chargeImportIndicator, Long leaseTimeDuration,
            java.util.Calendar leaseTime, String differenceReport) {
        this.caseId = caseId;
        this.chargeImportIndicator = chargeImportIndicator;
        this.leaseTimeDuration = leaseTimeDuration;
        this.leaseTime = leaseTime;
        this.differenceReport = differenceReport;
    }

    /*
     * (remove comment to activate xml code) public CaseAccessValue(Integer
     * caseId, String chargeImportIndicator, Long leaseTimeDuration,
     * java.util.Calendar leaseTime, Document differenceReport) { this.caseId =
     * caseId; this.chargeImportIndicator = chargeImportIndicator;
     * this.leaseTimeDuration = leaseTimeDuration; this.leaseTime = leaseTime;
     * this.differenceReport = differenceReport; }
     */

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getChargeImportIndicator() {
        return chargeImportIndicator;
    }

    public void setChargeImportIndicator(String chargeImportIndicator) {
        this.chargeImportIndicator = chargeImportIndicator;
    }

    public String getDifferenceReport() {
        return differenceReport;
    }

    public void setDifferenceReport(String differenceReport) {
        this.differenceReport = differenceReport;
    }

    public Long getLeaseTimeDuration() {
        return leaseTimeDuration;
    }

    public void setLeaseTimeDuration(Long leaseTimeDuration) {
        this.leaseTimeDuration = leaseTimeDuration;
    }

    public java.util.Calendar getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(java.util.Calendar leaseTime) {
        this.leaseTime = leaseTime;
    }
    /*
     * (remove comment to activate xml code) public void
     * setDifferenceReport(Document differenceReport) { this.differenceReport =
     * differenceReport; } public Document getDifferenceReport() { return
     * differenceReport; }
     */
}
