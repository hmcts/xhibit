package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// jdk
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: ChargeIdentificationMessageElement
 * </p>
 * <p>
 * Description: Builds the message element which describes the charge\offence
 * details. The format of the string depends on the charge type.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: ChargeIdentificationMessageElement.java,v 1.1 2004/04/20
 *          14:40:56 pznwc5 Exp $
 */

public class ChargeIdentificationMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(ChargeIdentificationMessageElement.class);

    private Locale currLocale = Locale.UK;

    private static final String MSG_ELEM_BUNDLE_FILE = "XHIBITCjseEventText";

    private ResourceBundle messageElementBundle;

    public ChargeIdentificationMessageElement() {
        // load the prpoerties file containing the start section text
        messageElementBundle = CSServices.getConfigServices().getBundle(MSG_ELEM_BUNDLE_FILE, currLocale);

    }

    /**
     * Returns the message element which describes the charge and offence the
     * event relates to.
     * 
     * @param value
     *            contains the defendantOmOfenceId
     * @param theCase
     *            not used
     * @return String describing the charge and offence the event relates to
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);
        StringBuffer sb = new StringBuffer();

        // check parameters we're going to use
        if (value == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance "
                    + "must be passed to the getElement() method");
        }
        if (value.getCourtLogViewValue() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance containing a  "
                    + "CourtLogViewValue must be " + "passed to the getElement() method");
        }
        if (value.getDefendantOnOffenceId() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue object with a defendantOnOffenceId "
                    + "must be provided to the getElement() method for court log event type: "
                    + value.getCourtLogViewValue().getEventType());
        }

        try {
            /**
             * @todo CourtLogSubscriptionValue will be changed to primary keys
             *       as a Longs, when this happens the conversion below will no
             *       longer be necessary
             */
            XhbDefendantOnOffence defOnOffence = XhbDefendantOnOffenceBeanHelper.findByPrimaryKey(value
                    .getDefendantOnOffenceId());

            // find the charge type and build the first part of the string
            String chargeType = defOnOffence.getXhbOffence().getXhbCharge().getChargeType();

            // If Indictment we need the Indictment number
            if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
                sb.append(defOnOffence.getXhbOffence().getXhbCharge().getCrestChargeSeqNo());
            }

            // Get the relevant text from the properties file
            sb.append(messageElementBundle.getString(chargeType));

            // Add the count\offence number
            sb.append(defOnOffence.getXhbOffence().getCrestOffenceSeqNo());

            log.debug("Returning ChargeIdentificationMessageElement " + sb.toString());
            return sb.toString();
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new CSUnrecoverableException("DefendantOnOfence not found for id " + value.getDefendantOnOffenceId(),
                    e);
        }
    }
}