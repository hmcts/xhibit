package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.Parameterizeable;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBasicValue;
import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;

public class VerdictValue extends ResultValue implements Parameterizeable, Serializable, UncodedOffenceInterface {
    
	final static long serialVersionUID = 239897530404406800L;
	
	private static final String DEFENDANT_ON_CHARGE = "C";

    private static final String DEFENDANT_ON_OFFENCE = "O";

    private static final String ON_CASE = "A";

    /**
     * List of appeal result codes that allow variation disposals to be recorded
     * Note: This list must be sorted alphabetically for the binary search to
     * work
     */
    public static final String[] variableAppealResultCodes = new String[] { "ACALO", "ACDSI", "ACDSV", "ADSI", "ASASV",
            "ASDSI" };

    // CCN0229 states the following not guilty codes should prompt the operator
    // to export the results. CCN0229 also states the following codes might
    // be added later: GA, GAJ, GL, GLJ
    /*
     * This list must be sorted alphabetically for the binary search to work.
     */
    public static final String[] notGuiltyExportReminderCodes = 
        new String[] { "AA", "NG", "NGIS", "NGJA", "NGJJ", "NGJU", "RTNG" };

    private XhbVerdictBasicValue xhbVerdictBasicValue;

    // Original plea and verdict ref ids used for VCO calculation
    private Integer originalRefPleaId;

    private Date originalPleaDate;

    private Integer originalRefVerdictId;

    private Date originalVerdictDate;

    // cached refVerdict data
    private String refVerdictCode;

    private String refVerdictDesc;

    private String refVerdictType;

    // cached refAppeal result for offence level data
    private String refAppealOffenceCode;

    private String refAppealOffenceDesc;

    // cached RefOffence data
    private String altRefOffenceCode;

    private String altRefOffenceDesc;

    // cached RefCourt data
    private Integer cccTransToRefCourtId;

    private String cccTransToRefCourtCode;

    private String cccTransToRefCourtDesc;

    // cached Hearing Date
    private Date hearingDate;

    private Long lastCalculatedDuration;

    // Constructors
    public VerdictValue() {
        this.xhbVerdictBasicValue = new XhbVerdictBasicValue();
    }

    public VerdictValue(XhbVerdictBasicValue xhbVerdictBasicValue) {
        if (xhbVerdictBasicValue == null) {
            throw new IllegalArgumentException("xhbVerdictBasicValue is null");
        }
        this.xhbVerdictBasicValue = xhbVerdictBasicValue;
    }

    public XhbVerdictBasicValue getXhbVerdictBasicValue() {
        return xhbVerdictBasicValue;
    }

    public void setVerdictBasicValue(XhbVerdictBasicValue xhbVerdictBasicValue) {
        this.xhbVerdictBasicValue = xhbVerdictBasicValue;
    }

    // accessors and mutators
    public Date getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(Date hearingDate) {
        this.hearingDate = hearingDate;
    }

    public void setLastCalculatedDuration(Long lastCalculatedDuration) {
        this.lastCalculatedDuration = lastCalculatedDuration;
    }

    public Long getLastCalculatedDuration() {
        return lastCalculatedDuration;
    }

    public Integer getOriginalRefPleaId() {
        return originalRefPleaId;
    }

    public void setOriginalRefPleaId(Integer originalRefPleaId) {
        this.originalRefPleaId = originalRefPleaId;
    }

    public Date getOriginalPleaDate() {
        return originalPleaDate;
    }

    public void setOriginalPleaDate(Date originalPleaDate) {
        this.originalPleaDate = originalPleaDate;
    }

    public Integer getOriginalRefVerdictId() {
        return originalRefVerdictId;
    }

    public void setOriginalRefVerdictId(Integer originalRefVerdictId) {
        this.originalRefVerdictId = originalRefVerdictId;
    }

    public Date getOriginalVerdictDate() {
        return originalVerdictDate;
    }

    public void setOriginalVerdictDate(Date originalVerdictDate) {
        this.originalVerdictDate = originalVerdictDate;
    }

    public String getRefVerdictCode() {
        return this.refVerdictCode;
    }

    public void setRefVerdictCode(String refVerdictCode) {
        this.refVerdictCode = refVerdictCode;
    }

    public String getRefVerdictDesc() {
        return this.refVerdictDesc;
    }

    public void setRefVerdictDesc(String refVerdictDesc) {
        this.refVerdictDesc = refVerdictDesc;
    }

    public void setRefVerdictType(String refVerdictType) {
        this.refVerdictType = refVerdictType;
    }

    public String getRefVerdictType() {
        return refVerdictType;
    }

    public String getRefAppealOffenceCode() {
        return this.refAppealOffenceCode;
    }

    public void setRefAppealOffenceCode(String refAppealOffenceCode) {
        this.refAppealOffenceCode = refAppealOffenceCode;
    }

    public String getRefAppealOffenceDesc() {
        return this.refAppealOffenceDesc;
    }

    public void setRefAppealOffenceDesc(String refAppealOffenceDesc) {
        this.refAppealOffenceDesc = refAppealOffenceDesc;
    }

    public Integer getAltRefOffenceId() {
        return this.xhbVerdictBasicValue.getAltRefOffenceId();
    }

    public void setAltRefOffenceId(Integer altRefOffenceId) {
        this.xhbVerdictBasicValue.setAltRefOffenceId(altRefOffenceId);
    }

    public String getAltRefOffenceCode() {
        return this.altRefOffenceCode;
    }

    /**
     * Sets the alternate ref offence code. This should be called from the
     * midtier when populating this VO.
     * 
     * @param altRefOffenceCode
     *            alternate ref offence code.
     */
    public void setViewAltRefOffenceCode(String altRefOffenceCode) {
        this.altRefOffenceCode = altRefOffenceCode;
    }

    public String getAltRefOffenceDesc() {
        return this.altRefOffenceDesc;
    }

    /**
     * Sets the alternate ref offence description. This should be called from
     * the midtier when populating this VO.
     * 
     * @param altRefOffenceDesc
     *            alternate ref offence description.
     */
    public void setViewAltRefOffenceDesc(String altRefOffenceDesc) {
        this.altRefOffenceDesc = altRefOffenceDesc;
    }

    /**
     * Sets the alternate ref offence code and description. This should be
     * called from the GUI to update tjhe alternate offence details.
     * 
     * @param altRefOffenceCode
     *            the alternate ref offence code.
     * @param altRefOffenceDesc
     *            the alternate ref offence description.
     */
    public void setAltRefOffenceCodeAndDesc(final String altRefOffenceCode, final String altRefOffenceDesc) {
        setViewAltRefOffenceCode(altRefOffenceCode);
        setViewAltRefOffenceDesc(altRefOffenceDesc);

        if (altRefOffenceCode == null || !altRefOffenceCode.equalsIgnoreCase(UNCODED_OFFENCE_REFERENCE_CODE)) {
            xhbVerdictBasicValue.setAltUncodedOffenceDesc(null);
        } else {
            xhbVerdictBasicValue.setAltUncodedOffenceDesc(altRefOffenceDesc);
        }
    }

    public String getCccTransToRefCourtCode() {
        return cccTransToRefCourtCode;
    }

    public void setCccTransToRefCourtCode(String cccTransToRefCourtCode) {
        this.cccTransToRefCourtCode = cccTransToRefCourtCode;
    }

    public String getCccTransToRefCourtDesc() {
        return cccTransToRefCourtDesc;
    }

    public void setCccTransToRefCourtDesc(String cccTransToRefCourtDesc) {
        this.cccTransToRefCourtDesc = cccTransToRefCourtDesc;
    }

    public Integer getCccTransToRefCourtId() {
        return cccTransToRefCourtId;
    }

    public void setCccTransToRefCourtId(Integer cccTransToRefCourtId) {
        this.cccTransToRefCourtId = cccTransToRefCourtId;
    }

    // wrapper support

    public Integer getVerdictId() {
        return xhbVerdictBasicValue.getVerdictId();
    }

    public Integer getDefendantChargeId() {
        return xhbVerdictBasicValue.getDefendantChargeId();
    }

    public void setDefendantChargeId(Integer defOnChargeId) {
        this.xhbVerdictBasicValue.setDefendantChargeId(defOnChargeId);
        this.xhbVerdictBasicValue.setDefendantOnOffenceId(null);
        this.xhbVerdictBasicValue.setCaseId(null);
        xhbVerdictBasicValue.setDefOnChargeOrOffence(DEFENDANT_ON_CHARGE);
    }

    public Integer getDefendantOnOffenceId() {
        return this.xhbVerdictBasicValue.getDefendantOnOffenceId();
    }

    public void setDefendantOnOffenceId(Integer defOnOffenceId) {
        this.xhbVerdictBasicValue.setDefendantOnOffenceId(defOnOffenceId);
        this.xhbVerdictBasicValue.setDefendantChargeId(null);
        this.xhbVerdictBasicValue.setCaseId(null);
        xhbVerdictBasicValue.setDefOnChargeOrOffence(DEFENDANT_ON_OFFENCE);
    }

    public Integer getJurorsAssenting() {
        return this.xhbVerdictBasicValue.getJurorsAssenting();
    }

    public void setJurorsDissenting(Integer jurorsDissenting) {
        this.xhbVerdictBasicValue.setJurorsDissenting(jurorsDissenting);
    }

    public Integer getJurorsDissenting() {
        return this.xhbVerdictBasicValue.getJurorsDissenting();
    }

    public void setJurorsAssenting(Integer jurorsAssenting) {
        this.xhbVerdictBasicValue.setJurorsAssenting(jurorsAssenting);
    }

    public boolean isObsolete() {
        return "Y".equals(this.xhbVerdictBasicValue.getObsInd());
    }

    public void setObsInd(boolean obsInd) {
        this.xhbVerdictBasicValue.setObsInd(obsInd ? "Y" : "N");
    }

    public Integer getRefVerdictId() {
        return this.xhbVerdictBasicValue.getRefVerdictId();
    }

    public void setRefVerdictId(Integer refVerdictId) {
        this.xhbVerdictBasicValue.setRefVerdictId(refVerdictId);
    }

    public Date getVerdictDate() {
        return xhbVerdictBasicValue.getVerdictDate();
    }

    public Calendar getVerdictDateCalendar() {
        Date verdictDate = xhbVerdictBasicValue.getVerdictDate();
        if (verdictDate == null) {
            return null;
        } else {
            Calendar verdictDateCalendar = Calendar.getInstance();
            verdictDateCalendar.setTime(verdictDate);
            return verdictDateCalendar;
        }
    }

    public void setVerdictDate(Calendar verdictDate) {
        if (verdictDate == null) {
            xhbVerdictBasicValue.setVerdictDate(null);
        } else {
            xhbVerdictBasicValue.setVerdictDate(verdictDate.getTime());
        }

        this.xhbVerdictBasicValue.setVerdictDate(verdictDate.getTime());
    }

    public void setVerdictDate(Date verdictDate) {
        xhbVerdictBasicValue.setVerdictDate(verdictDate);
    }

    public Integer getRefAppResultId() {
        return this.xhbVerdictBasicValue.getRefAppResultId();
    }

    public void setRefAppResultId(Integer refAppResultId) {
        this.xhbVerdictBasicValue.setRefAppResultId(refAppResultId);
    }

    public String getOtherVerdictText() {
        return xhbVerdictBasicValue.getOtherVerdictText();
    }

    public void setOtherVerdictText(String otherVerdictText) {
        xhbVerdictBasicValue.setOtherVerdictText(otherVerdictText);
    }

    public Integer getDisposal2Id() {
        return this.xhbVerdictBasicValue.getDisposal2Id();
    }

    public void setDisposal2Id(Integer disposal2Id) {
        this.xhbVerdictBasicValue.setDisposal2Id(disposal2Id);
    }

    public Integer getDefendantOnCaseId() {
        return this.xhbVerdictBasicValue.getDefendantOnCaseId();
    }

    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.xhbVerdictBasicValue.setDefendantOnCaseId(defendantOnCaseId);
    }

    public Integer getCaseId() {
        return this.xhbVerdictBasicValue.getCaseId();
    }

    public void setCaseId(Integer caseId) {
        this.xhbVerdictBasicValue.setCaseId(caseId);
        this.xhbVerdictBasicValue.setDefendantChargeId(null);
        this.xhbVerdictBasicValue.setDefendantOnOffenceId(null);
        xhbVerdictBasicValue.setDefOnChargeOrOffence(ON_CASE);
    }

    public String getAppLesserOffence() {
        return xhbVerdictBasicValue.getAppLesserOff();
    }

    public void setAppLesserOffence(String appLesserOffence) {
        xhbVerdictBasicValue.setAppLesserOff(appLesserOffence);
    }

    // Business
    public boolean isOnOffence() {
        String defOnChargeOrOffence = xhbVerdictBasicValue.getDefOnChargeOrOffence();
        if (defOnChargeOrOffence == null) {
            return getDefendantOnOffenceId() != null;
        } else {
            return DEFENDANT_ON_OFFENCE.equals(defOnChargeOrOffence);
        }
    }

    public boolean isOnCharge() {
        String defOnChargeOrOffence = xhbVerdictBasicValue.getDefOnChargeOrOffence();
        if (defOnChargeOrOffence == null) {
            return getDefendantChargeId() != null;
        } else {
            return DEFENDANT_ON_CHARGE.equals(defOnChargeOrOffence);
        }
    }

    public boolean isOnCase() {
        String defOnChargeOrOffence = xhbVerdictBasicValue.getDefOnChargeOrOffence();
        if (defOnChargeOrOffence == null) {
            return getCaseId() != null;
        } else {
            return ON_CASE.equals(defOnChargeOrOffence);
        }
    }

    
    public boolean isOnDisposal() {
        return xhbVerdictBasicValue.getDisposal2Id() != null;
    }
    
    // Return true if verdict was "guilty"
    public boolean isGuiltyVerdict(){
    	String code = getRefVerdictCode();
    	 return VerdictTypeEvent.GUILTY.equals(code) || VerdictTypeEvent.ALTERNATE.equals(code)
                 || VerdictTypeEvent.LESSER.equals(code) || VerdictTypeEvent.GUILTY_BY_OTHER_JURY.equals(code) || 
                 VerdictTypeEvent.GUILTY_BY_JUDGE_ALONE_DVC_VA.equals(code)||  VerdictTypeEvent.GUILTY_JUDGES_DIRECTION.equals(code)
                 || VerdictTypeEvent.ALTERNATE_JUDGES_DIRECTION.equals(code)|| VerdictTypeEvent.ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA.equals(code)
                 || VerdictTypeEvent.LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA.equals(code)|| VerdictTypeEvent.LESSER_JUDGES_DIRECTION.equals(code);
    }
    
    // Return true if verdict is guilty of a lesser offence
    public boolean isGuiltyOfLesser(){
    	String code = getRefVerdictCode();
   	 
    	return  VerdictTypeEvent.ALTERNATE.equals(code)
                || VerdictTypeEvent.LESSER.equals(code)|| VerdictTypeEvent.ALTERNATE_JUDGES_DIRECTION.equals(code)|| VerdictTypeEvent.ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA.equals(code)
                || VerdictTypeEvent.LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA.equals(code)|| VerdictTypeEvent.LESSER_JUDGES_DIRECTION.equals(code);
    }

    // Return true if verdict must record juror details
    public boolean hasAssentingDissenting() {
        String code = getRefVerdictCode();
        return VerdictTypeEvent.GUILTY.equals(code) || VerdictTypeEvent.ALTERNATE.equals(code)
                || VerdictTypeEvent.LESSER.equals(code) || VerdictTypeEvent.GUILTY_BY_OTHER_JURY.equals(code);
    }

    // Return true if verdict can record juror details
    public boolean getJurorsAssentingOption() {
        String code = getRefVerdictCode();
        return VerdictTypeEvent.DEFENDANT_FOUND_UNDER_DISABILITY.equals(code)
                || VerdictTypeEvent.ALTERNATE_JUDGES_DIRECTION.equals(code)
                || VerdictTypeEvent.LESSER_JUDGES_DIRECTION.equals(code) || VerdictTypeEvent.OTHER.equals(code);
    }

    public boolean isAppealResultVariable() {
        return (Arrays.binarySearch(variableAppealResultCodes, getRefAppealOffenceCode()) >= 0);
    }

    public boolean promptForExportReminder() {
        String refVerdictCode = getRefVerdictCode();
        if (refVerdictCode != null) {
            int position = Arrays.binarySearch(notGuiltyExportReminderCodes, refVerdictCode);
            return (position >= 0);
        }

        return false;
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(VerdictValue.class.getName());
        buffer.append(" {originalRefPleaId=");
        buffer.append(originalRefPleaId);
        buffer.append(", originalPleaDate=");
        buffer.append(originalPleaDate);
        buffer.append(", originalRefVerdictId=");
        buffer.append(originalRefVerdictId);
        buffer.append(", originalVerdictDate=");
        buffer.append(originalVerdictDate);
        buffer.append(", refVerdictCode=");
        buffer.append(refVerdictCode);
        buffer.append(", refVerdictDesc=");
        buffer.append(refVerdictDesc);
        buffer.append(", refVerdictType=");
        buffer.append(refVerdictType);
        buffer.append(", hearingDate=");
        buffer.append(hearingDate);
        buffer.append(",lastCalculatedDuration=");
        buffer.append(lastCalculatedDuration);
        buffer.append(", refAppealOffenceCode=");
        buffer.append(refAppealOffenceCode);
        buffer.append(", refAppealOffenceDesc=");
        buffer.append(refAppealOffenceDesc);
        buffer.append(", altRefOffenceCode=");
        buffer.append(altRefOffenceCode);
        buffer.append(", altRefOffenceDesc=");
        buffer.append(altRefOffenceDesc);
        buffer.append(", cccTransToRefCourtId=");
        buffer.append(cccTransToRefCourtId);
        buffer.append(", cccTransToRefCourtCode=");
        buffer.append(cccTransToRefCourtCode);
        buffer.append(", cccTransToRefCourtDesc=");
        buffer.append(cccTransToRefCourtDesc);
        buffer.append(", xhbVerdictBasicValue=");
        buffer.append(xhbVerdictBasicValue);
        buffer.append("}");
    }

    // Parameterizeable Implementation
    public Object[] getMessageParameters() {
        /**
         * @todo decide what parameters to pass in message
         */
        return new String[0];
    }
}