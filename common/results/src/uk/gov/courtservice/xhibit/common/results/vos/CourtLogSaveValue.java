package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;

/**
 * Abstract class to allow extraction of common code from the value objects that
 * will be used for logging of court log events. Contained within this value
 * object are all of the standard values required when creating a court log
 * event.
 * 
 * @author tz0d5m
 * @version $Revision: 1.6 $
 */
public abstract class CourtLogSaveValue extends ResultSaveValue {
	
	static final long serialVersionUID = -4450485740707681548L;
	
    /** Constant representation of an appeal case type */
    private static final String CASE_TYPE_APPEAL = "A";

    /** Constant representation of an criminal appeal case sub-types */
    private static final String[] CASE_SUB_TYPE_CRIM_APPEAL = { "C", "S", "B" };

    /** Constant representation of a miscellaneous appeal case sub-type */
    private static final String CASE_SUB_TYPE_MISC_APPEAL = "O";

    private final String chargeType;

    private Integer scheduledHearingId = null;

    private Date courtLogDate = null;

    private boolean inCourt = false;

    public CourtLogSaveValue(String operation, Integer caseId, Integer caseNumber, String caseType, String chargeType) {
        super(operation, caseId, caseNumber, caseType, null);

        this.chargeType = chargeType;
    }

    public CourtLogSaveValue(String operation, Integer caseId, Integer caseNumber, String caseType, String chargeType,
            Integer defendantOnCaseId, Integer scheduledHearingId, Date courtLogDate, boolean inCourt) {
        super(operation, caseId, caseNumber, caseType, defendantOnCaseId);

        this.chargeType = chargeType;
        this.scheduledHearingId = scheduledHearingId;
        this.courtLogDate = courtLogDate;
        this.inCourt = inCourt;
    }

    public Integer getScheduledHearingId() {
        return this.scheduledHearingId;
    }

    public Date getCourtLogDate() {
        if (courtLogDate == null) {
            courtLogDate = new Date();
        }

        return courtLogDate;
    }

    public boolean isInCourt() {
        return this.inCourt;
    }

    public String getChargeType() {
        return this.chargeType;
    }

    /**
     * Does this value object represent an appeal case.
     * 
     * @return <i>true</i> if an appeal case, <i>false</i> otherwise.
     */
    public final boolean isAppealCase() {
        return CASE_TYPE_APPEAL.equals(getCourtLogCaseType());
    }

    /**
     * Does this value object represent a criminal appeal case, a case is a
     * criminal appeal if it is an appeal case, and has a case sub-type
     * represented by the CASE_SUB_TYPE_MISC_APPEAL constant.
     * 
     * @param caseSubType
     *            The <code>String</code> representation of the case sub-type.
     * @return <i>true</i> if a criminal appeal case, <i>false</i> otherwise.
     * 
     * @see #isAppealCase()
     * @see #CASE_SUB_TYPE_MISC_APPEAL
     */
    public final boolean isCriminalAppeal(String caseSubType) {
        if (isAppealCase()) {
            for (int i = 0; i < CASE_SUB_TYPE_CRIM_APPEAL.length; i++) {
                if (CASE_SUB_TYPE_CRIM_APPEAL[i].equals(caseSubType)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Does this value object represent a miscellaneous appeal case, a case is a
     * miscellaneous appeal if it is an appeal case, and has a case sub-type
     * represented by the CASE_SUB_TYPE_MISC_APPEAL constant.
     * 
     * @param caseSubType
     *            The <code>String</code> representation of the case sub-type.
     * @return <i>true</i> if a miscellaneous appeal case, <i>false</i>
     *         otherwise.
     * 
     * @see #isAppealCase()
     * @see #CASE_SUB_TYPE_MISC_APPEAL
     */
    public final boolean isMiscAppeal(String caseSubType) {
        return (isAppealCase() && CASE_SUB_TYPE_MISC_APPEAL.equals(caseSubType));
    }

    /**
     * Does this value object represent a charge on count.
     * 
     * @return <i>true</i> if on count, <i>false</i> otherwise.
     */
    public final boolean isOnCount() {
        return ChargeTypes.INDICTMENT.getChargeType().equals(chargeType);
    }

    /**
     * Does this value object represent a charge on summary.
     * 
     * @return <i>true</i> if on summary, <i>false</i> otherwise.
     */
    public final boolean isOnSummary() {
        return ChargeTypes.SECTION_41.getChargeType().equals(chargeType);
    }

    /**
     * Does this value object represent a charge on breach.
     * 
     * @return <i>true</i> if on breach, <i>false</i> otherwise.
     */
    public final boolean isOnBreach() {
        return ChargeTypes.isBreachChargeType(chargeType);
    }

    // required to determine what cjse parameters need to be added...
    public abstract boolean isOnOffence();

    // Debug
    public void appendDebugParameters(StringBuffer buffer) {
        super.appendDebugParameters(buffer);

        buffer.append(", scheduledHearingId=");
        buffer.append(this.scheduledHearingId);
        buffer.append(", chargeType=");
        buffer.append(chargeType);
        buffer.append(", courtLogDate=");
        buffer.append(this.courtLogDate);
        buffer.append(", inCourt=");
        buffer.append(this.inCourt);
    }

    public abstract Integer getCourtLogEvent();

    public abstract Integer getDefendantOnOffenceId();
}
