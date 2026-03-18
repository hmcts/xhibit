package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import java.sql.Time;
import java.util.Arrays;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessValue;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * 
 */
public class WitnessSessionImpl extends WitnessDetailImpl implements WitnessSession, Comparable {
    protected static final Logger log = CSServices.getLogger(WitnessSessionImpl.class);

    private static String[] pagerNetworks;
    
    private static final long serialVersionUID = 3394547750640820183L;

    public int getDayNumber() {

        return witnessValue.getXhbSkeletonSession().getXhbSkeletonDay().getDayNumber();
    }

    public String getSessionType() {
        return witnessValue.getXhbSkeletonSession().getMorningOrAfternoon();

    }

    public WitnessDetail getAssociatedWitnessDetail() {

        return this;
    }

    public WitnessSummary getAssociatedWitnessSummary() {

        return this;
    }

    public TrialSession getTrialSession() {
        return new TrialSessionImpl(witnessValue.getXhbSkeletonSession());
    }

    public void setTrialSession(final TrialSession trialSession) {
        witnessValue.setXhbSkeletonSession((((TrialSessionImpl) trialSession).getSkeletonSession()));
        markAsModified();
    }

    // From Witness Summary

    public void update() throws ModificationException {
        if (isModified()) {
            WitnessDetail witnessDetail = WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                    .updateWitnessSession(this);
            setWitnessValue(((WitnessDetailImpl) witnessDetail).getWitnessValue());
        }
    }

    public WitnessSessionImpl(final XhbWitnessValue witnessValue) {
        super(witnessValue);
        this.witnessValue = witnessValue;

    }

    public int compareTo(final Object o) {
        return compareTo((WitnessSession) o);
    }

    public int compareTo(final WitnessSession witness) {
        int result = getDayNumber() - witness.getDayNumber();
        if (result != 0) {
            return result;
        } else {
            String st1 = getSessionType();
            String st2 = witness.getSessionType();
            Time st1dueAt = getDueAt();
            Time st2dueAt = witness.getDueAt();
            if (st1.equals("M")) {
                if (st2.equals("M")) {
                    // return 0;
                    // MM
                    boolean result2 = st1dueAt.before(st2dueAt);
                    if (result2) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return -1; // MA
                }
            } else {
                if (st2.equals("M")) {
                    return 1; // AM
                } else {
                    // return 0;
                    // AA
                    boolean result2 = st1dueAt.before(st2dueAt);
                    if (result2) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        }
    }

    // End of Witness Summary

    public String toString() {
        return "uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessDetailImpl{" + "witnessValue="
                + witnessValue + ", MAXIMUM_NOTE_LENGTH=" + MAXIMUM_NOTE_LENGTH + ", pagerNetworks="
                + (pagerNetworks == null ? null : "length:" + pagerNetworks.length + Arrays.asList(pagerNetworks))
                + "}";
    }
}
