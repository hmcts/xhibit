package uk.gov.courtservice.xhibit.business.vos.services.todaysschedule;

import java.sql.Timestamp;
import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class CourtLogHeaderValue extends CSAbstractValue {
	private static final long serialVersionUID = -1637887271475861479L;
	private Integer caseNumber;

    private Integer caseId;

    private JudgeValue judge;

    private Collection prosecutionAdvocates;

    private Collection respondentAdvocates;

    private Collection objectorAdvocates;

    /**
     * Collection of
     * uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.DefenceAdvocateValue
     */
    private Collection defenceAdvocates;

    private String hearingType;

    private Timestamp timeListed;

    private ShorthandwriterValue shorthandwriterValue;

    private Integer scheduledHearingId;

    private Integer linkedCaseID;

    /**
     * Collection of
     * uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue
     */
    private Collection defendants;

    private Collection appellants;

    /**
     * Collection of
     * uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.JusticeValue
     */
    private Collection justices;

    private String refCourtReporter;

    private String[] respondents;

    /**
     * Collection of
     * uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.AppellantAdvocateValue
     */
    private Collection appellantAdvocates;

    private String caseType;

    private Timestamp dateOfBirth;

    private Timestamp lastConvictionDate;

    private String caseSubType;

    /**
     * @roseuid 3DC9681F01B2
     */
    public CourtLogHeaderValue() {
    }

    public CourtLogHeaderValue(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    public Integer getScheduledHearingId() {
        return scheduledHearingId;
    }

    public ShorthandwriterValue getShorthandwriterValue() {
        return shorthandwriterValue;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public Collection getDefenceAdvocates() {
        return defenceAdvocates;
    }

    public Collection getDefendants() {
        return defendants;
    }

    public Collection getAppellants() {
        return appellants;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public String getHearingType() {
        return hearingType;
    }

    public JudgeValue getJudge() {
        return judge;
    }

    public Collection getProsecutionAdvocates() {
        return prosecutionAdvocates;
    }

    public Timestamp getTimeListed() {
        return timeListed;
    }

    public Integer getLinkedCaseID() {
        return linkedCaseID;
    }

    public Collection getJustices() {
        return justices;
    }

    public String getCourtReporter() {
        return refCourtReporter;
    }

    public String[] getRespondents() {
        return respondents;
    }

    public Collection getRespondentAdvocates() {
        return respondentAdvocates;
    }

    public Collection getAppellantAdvocates() {
        return appellantAdvocates;
    }

    public String getCaseType() {
        return caseType;
    }

    public Collection getObjectorAdvocates() {
        return objectorAdvocates;
    }

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public Timestamp getLastConvictionDate() {
        return lastConvictionDate;
    }

    public void setScheduledHearingId(Integer scheduledHearingId) {
        this.scheduledHearingId = scheduledHearingId;
    }

    public void setTimeListed(Timestamp timeListed) {
        this.timeListed = timeListed;
    }

    public void setJudge(JudgeValue judge) {
        this.judge = judge;
    }

    public void setProsecutionAdvocates(Collection prosecutionAdvocates) {
        this.prosecutionAdvocates = prosecutionAdvocates;
    }

    public void setObjectorAdvocates(Collection objectorAdvocates) {
        this.objectorAdvocates = objectorAdvocates;
    }

    public void setHearingType(String hearingType) {
        this.hearingType = hearingType;
    }

    public void setDefenceAdvocates(Collection defenceAdvocates) {
        this.defenceAdvocates = defenceAdvocates;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public void setLinkedCaseID(Integer linkedCaseID) {
        this.linkedCaseID = linkedCaseID;
    }

    public void setDefendants(Collection defendants) {
        this.defendants = defendants;
    }

    public void setAppellants(Collection appellants) {
        this.appellants = appellants;
    }

    public void setJustices(Collection justices) {
        this.justices = justices;
    }

    public void setRefCourtReporter(String refCourtReporter) {
        this.refCourtReporter = refCourtReporter;
    }

    public void setRespondents(String[] respondents) {
        this.respondents = respondents;
    }

    public void setRespondentAdvocates(Collection respondentAdvocates) {
        this.respondentAdvocates = respondentAdvocates;
    }

    public void setAppellantAdvocates(Collection appellantAdvocates) {
        this.appellantAdvocates = appellantAdvocates;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setShorthandwriterValue(ShorthandwriterValue shorthandwriterValue) {
        this.shorthandwriterValue = shorthandwriterValue;
    }

    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setLastConvictionDate(Timestamp lastConvictionDate) {
        this.lastConvictionDate = lastConvictionDate;
    }

    public void setCaseSubType(String caseSubType) {
        this.caseSubType = caseSubType;
    }

    public String getCaseSubType() {
        return caseSubType;
    }

}
