package uk.gov.courtservice.xhibit.business.services.witness.schedule;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionValue;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessValue;
import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.interfaces.AbstractStateManagedObject;
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
 * </p> *
 * <H1>This is not thread safe, under the covers it uses unsynchronized lazy
 * instantiation. You have been warned :-) </H1>
 * 
 * @author qzd3k3
 * 
 */
public class WitnessSummaryImpl extends AbstractStateManagedObject implements WitnessSummary {
    protected XhbWitnessValue witnessValue;

    private static final Logger log = CSServices.getLogger(WitnessSummaryImpl.class);
    
    private static final long serialVersionUID = 9132637690018801708L;

    public WitnessSummaryImpl(final XhbWitnessValue witnessValue) {
        this.witnessValue = witnessValue;
        // reduce the amount of data stored so that we don't have to Serialize
        // the whole lot
        this.witnessValue.setXhbCase(null);
        // set the skeleton schedule value
        ArrayList session = WitnessControllerBeanBusinessDelegate.DelegateFactory.getInstance().getSkeletonSession(
                witnessValue.getSessionId());
        XhbSkeletonSessionValue ss = (XhbSkeletonSessionValue) session.get(0);

        this.witnessValue.setXhbSkeletonSession(ss);
        setMetaState(UPDATED);
    }

    public XhbWitnessValue getWitnessValue() {
        return witnessValue;
    }

    public Integer getId() {
        return witnessValue.getWitnessId();
    }

    public String getName() {
        return witnessValue.getName();
    }

    public boolean getHasNumber() {
        if (witnessValue.getMobilenumber() != null || witnessValue.getPagernumber() != null) {
            return true;
        }
        return false;
    }

    public String getStatus() {
        return witnessValue.getStatus();
    }

    public String getType() {
        return witnessValue.getWitnessType();
    }

    public int getAge() {
        return witnessValue.getAge();
    }

    public Date getReleased() {
        final Timestamp releasedDateTime = witnessValue.getReleasedDateTime();
        if (releasedDateTime == null) {
            return null;
        }
        return new Date(releasedDateTime.getTime());
    }

    public Date getArrived() {

        final Timestamp actualArrivalDateTime = witnessValue.getActualArrivalDateTime();
        if (actualArrivalDateTime == null) {
            return null;
        }
        return new Date(actualArrivalDateTime.getTime());

    }

    public Time getDueAt() {
        final Time expectedArrivalDateTime = witnessValue.getExpectedArrivalTime();
        if (expectedArrivalDateTime == null) {
            return null;
        }
        return expectedArrivalDateTime;

    }

    public int getTotalTimeHours() {
        if (witnessValue.getCalculatedWitnessTime() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(witnessValue.getCalculatedWitnessTime());
            return cal.get(java.util.Calendar.HOUR);
        }

        return 0;
    }

    public int getTotalTimeMinutes() {
        if (witnessValue.getCalculatedWitnessTime() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(witnessValue.getCalculatedWitnessTime());
            return cal.get(java.util.Calendar.MINUTE);
        }

        return 0;
    }

    /**
     * @pre getMetaState() != REMOVED
     * @post getMetaState() == UPDATED
     * @throws ModificationException
     */
    public void update() throws ModificationException {
        if (true)
            throw new ModificationException("WITNESS_XXX", "This object is read only.");
    }

    /**
     * @pre getMetaState() != REMOVED
     * @post getMetaState() == REMOVED
     * @throws ModificationException
     */
    public void remove() throws ModificationException {
        if (true)
            throw new ModificationException("WITNESS_XXX", "This object is read only.");
    }

    public int compareTo(final Object o) {
        return compareTo((WitnessSummary) o);
    }
}
