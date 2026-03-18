package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// j2ee
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: DefendantNameMessageElement
 * </p>
 * <p>
 * Description: Returns the name of the defendant for this event, this applies
 * to a Defendant or Crn Level event, Case Level events should use the
 * DefendantNamesMessageElement.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: DefendantNameMessageElement.java,v 1.1 2004/04/20 14:40:56
 *          pznwc5 Exp $
 */

public class DefendantNameMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(DefendantNameMessageElement.class);

    public DefendantNameMessageElement() {
    }

    /**
     * Returns the defendant name for this Defendant Level event.
     * 
     * @param value
     *            Contains the defendantId to retrieve the name for.
     * @param theCase
     *            not used
     * @return The defendant name
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        // log start of method and check params
        logStartAndCheckParams(value, theCase);

        String defendantName;

        if (value.getDefendantOnCaseId() != null) {
            defendantName = getDefNameFromDefOnCase(value);
        } else {
            defendantName = getDefNameFromDefOnOffence(value);
        }

        return defendantName;
    }

    /**
     * Build defendant name in the same format used by the hearing header
     * 
     * @param defendant
     *            the defendant local reference to build the name for
     * @return the defendant name
     */
    public static StringBuffer buildDefendantName(XhbDefendant defendant) {
        StringBuffer sb = new StringBuffer();
        appendName(sb, defendant.getFirstName());
        appendName(sb, defendant.getMiddleName());
        // if no first or middle name, use initials
        if (sb.length() < 1)
            appendName(sb, defendant.getInitials());
        appendName(sb, defendant.getSurname());
        return sb;
    }

    // ---------------------------- Private Methods
    // -----------------------------//

    private void logStartAndCheckParams(CourtLogSubscriptionValue value, XhbCase theCase)
            throws IllegalArgumentException {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        // check parameters we're going to use
        if (value == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance "
                    + "must be passed to the getElement() method");
        }
        if (value.getCourtLogViewValue() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance containing a  "
                    + "CourtLogViewValue must be " + "passed to the getElement() method");
        }
        if (value.getDefendantOnCaseId() == null && value.getDefendantOnOffenceId() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue object with either a "
                    + "defendantOnCaseId or a defendantOnOffenceId must be provided "
                    + "to the getElement() method for court log event type: "
                    + value.getCourtLogViewValue().getEventType());
        }
    }

    private static void appendName(StringBuffer sb, String candidateName) {
        if (candidateName != null && !candidateName.equals("")) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(candidateName);
        }
    }

    // find the defendant entity via XhbDefendantOnCase, the defendant will
    // exist since the defendantId is a not null fk on XhbDefendantOnCase
    private String getDefNameFromDefOnCase(CourtLogSubscriptionValue value) {
        try {
            /**
             * @todo CourtLogSubscriptionValue will be changed to primary keys
             *       as a Longs, when this happens the conversion below will no
             *       longer be necessary
             */
            XhbDefendant defendant = XhbDefendantOnCaseBeanHelper.findByPrimaryKey(value.getDefendantOnCaseId())
                    .getXhbDefendant();

            StringBuffer sb = buildDefendantName(defendant);

            log.debug("Returning DefendantNameMessageElement " + sb.toString());

            return sb.toString();
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Could not find defendantOnCase for defendantOnCaseId "
                    + value.getDefendantOnCaseId(), e);
        }
    }

    // find the defendant entity via XhbDefendantOnOffence, the defendant
    // will
    // exist since the defendantOnCaseId is a not null fk on
    // XhbDefendantOnOffence
    // and defendantId is a not null fk on XhbDefendantOnCase
    private String getDefNameFromDefOnOffence(CourtLogSubscriptionValue value) {
        try {
            // find the defendant entity via XhbDefendantOnOffence
            /**
             * @todo CourtLogSubscriptionValue will be changed to primary keys
             *       as a Longs, when this happens the conversion below will no
             *       longer be necessary
             */
            XhbDefendant defendant = XhbDefendantOnOffenceBeanHelper.findByPrimaryKey(value.getDefendantOnOffenceId())
                    .getXhbDefendantOnCase().getXhbDefendant();

            StringBuffer sb = buildDefendantName(defendant);

            log.debug("Returning DefendantNameMessageElement " + sb.toString());

            return sb.toString();
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("Could not find defendantOnOffence for defendantOnOffenceId "
                    + value.getDefendantOnOffenceId(), e);
        }
    }
}