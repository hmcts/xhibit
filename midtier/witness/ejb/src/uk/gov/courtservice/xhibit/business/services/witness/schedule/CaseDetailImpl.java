package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import uk.gov.courtservice.xhibit.business.entities.witness.XhbCaseValue;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.DurationLessThanMinimumException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoDirectionsForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoJudgeForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.interfaces.AbstractStateManagedObject;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;

/**
 * 
 * @author Neil Ellis
 * 
 * @version $Revision: 1.11 $
 */
public class CaseDetailImpl extends AbstractStateManagedObject implements CaseDetail {
    private XhbCaseValue aCase;
    
    private static final long serialVersionUID = -5939698314685417737L;

    private String[] defendants;

    private transient WitnessControllerBeanBusinessDelegate delegate;

    public CaseDetailImpl(final XhbCaseValue aCase, final String[] defendants) {
        this.aCase = aCase;
        this.defendants = defendants;
    }

    public XhbCaseValue getCase() {
        return aCase;
    }

    public Integer getId() {
        return aCase.getCaseId();
    }

    public String[] getDefendantNames() {
        return defendants;
    }

    public String getCourtName() {
        return aCase.getXhbCourt().getCourtName();
    }

    public String getCourtPrefix() {
        return aCase.getXhbCourt().getCourtPrefix();
    }

    public String getCourtCode() {
        return aCase.getXhbCourt().getCrestCourtId();
    }

    public String getBailMagCode() {
        return aCase.getBailMagCode();
    }

    public String getPoliceOfficerAttending() {
        return aCase.getPoliceOfficerAttending();
    }

    public String getCpsCaseWorker() {
        return aCase.getCpsCaseWorker();
    }

    public String getCaseTitle() {
        return aCase.getCaseTitle();
    }

    public String getCaseNumber() {
        return aCase.getCaseType() + aCase.getCaseNumber();
    }

    public String getCaseType() {
        return aCase.getCaseType();
    }

    public void setCpsCaseWorker(final String cpsCaseWorker) {
        aCase.setCpsCaseWorker(cpsCaseWorker);
    }

    public void setPoliceOfficerAttending(final String policeOfficerAttending) {
        aCase.setPoliceOfficerAttending(policeOfficerAttending);
    }

    public String getJudgeName(String userDisplayName) throws NoJudgeForCaseException {
        return getDelegate().getJudgeForCase(aCase.getPrimaryKey(), userDisplayName);
    }

    public WitnessControllerBeanBusinessDelegate getDelegate() {
        if (delegate == null) {
            delegate = WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        }
        return delegate;
    }

    public int getEstimatedCaseDuration() throws NoDirectionsForCaseException {

        return new Float(getDelegate().getEstimatedCaseDuration(aCase.getCaseId())).intValue();
    }

    public void setEstimatedCaseDuration(final float durationInDays, final boolean force)
            throws DurationLessThanMinimumException, NoDirectionsForCaseException {
        getDelegate().setEstimatedCaseDuration(aCase.getCaseId(), durationInDays, force);
    }

    public XhbCaseValue toXhbCaseValue() {
        return new XhbCaseValue(aCase);
    }

    public static CaseDetailImpl createCaseDetailValue(final XhbCaseValue aCase, final String[] defendants) {
        return new CaseDetailImpl(aCase, defendants);
    }

    public void update() throws ModificationException {
        WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance().updateCaseDetail(this);
    }

    public void remove() throws ModificationException {
        if (true)
            throw new ModificationException("WITNESS_XXX", "Cases may not be removed this way.");
    }

}
