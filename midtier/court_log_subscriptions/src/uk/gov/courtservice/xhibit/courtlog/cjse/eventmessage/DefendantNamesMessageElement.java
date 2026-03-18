package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: DefendantNamesMessageElement
 * </p>
 * <p>
 * Description: Returns the name of the defendant for this event, this applies
 * to a Case Level event, Defendant Level events should use the
 * DefendantNameMessageElement.If there is more than one defendant on the case
 * the name of an arbitrairy defendant is returned and 'and others' is appended.
 * This message element supports the English text only, if other languages are
 * required (e.g. Welsh) a new version of the class must be created and
 * refrenced in the message string for that language. Localisation is not
 * performed at the message element level as the large majority of message
 * elements are language independent.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: DefendantNamesMessageElement.java,v 1.2 2004/05/07 13:02:01
 *          tz0d5m Exp $
 */
public class DefendantNamesMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(DefendantNamesMessageElement.class);

    private Locale currLocale = Locale.UK;

    private static final String MSG_ELEM_BUNDLE_FILE = "XHIBITCjseEventText";

    private ResourceBundle messageElementBundle;

    public DefendantNamesMessageElement() {
        // load the prpoerties file containing the start section text
        messageElementBundle = CSServices.getConfigServices().getBundle(MSG_ELEM_BUNDLE_FILE, currLocale);
    }

    /**
     * Returns the defendant name for this case.
     * 
     * @param value
     *            Contains the defendantId to retrieve the name for.
     * @param theCase
     *            An XhbCase instance which is used for CASE level events to
     *            retrieve the number of defendants on the case.
     * @param eventLevel
     *            Alternate behaviour is required for CASE level events.
     * @return The defendant name
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        // log start of method and check params
        logStartAndCheckParams(value, theCase);

        Collection defOnCases = theCase.getXhbDefendantOnCases();
        if (defOnCases.size() < 1) {
            // no defendants on the case
            return "";
        }

        // build the defendant name from the first element in the collection
        StringBuffer sb = DefendantNameMessageElement.buildDefendantName(((XhbDefendantOnCase) defOnCases.iterator()
                .next()).getXhbDefendant());

        // if there are > 1 defendants on the case, append ' and others'
        if (theCase.getXhbDefendantOnCases().size() > 1) {
            sb.append(" " + messageElementBundle.getString("andOthers"));
        }

        return sb.toString();
    }

    // ---------------------------- Private Methods
    // -----------------------------//

    private void logStartAndCheckParams(CourtLogSubscriptionValue value, XhbCase theCase)
            throws IllegalArgumentException {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        // check parameters we're going to use
        if (theCase == null) {
            throw new IllegalArgumentException("An XhbCase instance must be provided to the getElement() method.");
        }
    }
}
